package src.interviews.binarysearch;

/*
Company - AlphaSense

2 servers, time taken to upgrade each server (t1 & t2). Only one server undergoes upgrade at any given second. There
maybe seconds during which no server is undergoing an upgrade.

int req1: first server receives requests at multiples of req1.
int t1: total time in seconds to upgrade first server.
int req2: 2nd server receives requests at multiples of req2.
int t2: total time seconds to upgrade second server.

Return minimum total time in seconds to upgrade both servers. Return type is long.

Example:
STDIN | FUNCTION
----------------
  3   |  req1 = 3
  2   |  t1 = 2
  4   |  req2 = 4
  1   |  t2 = 1

Output = 3. First server upgrades at 1st and 2nd seconds. Second server upgrades at 3rd second.
 */

/*
Time Complexity: (O(log(high)))
1. The Binary Search Loop: The search space ranges from low = 1 to high = 10^{15}. Because binary search cuts the active
 search window exactly in half during every single loop iteration, the loop will run at most (log_2(10^{15}) approx 50)
 times [1].
2. Constant-Time Math Operations: Inside the loop, finding the GCD takes (O(log(min(req1, req2)))) due to the
 Euclidean division algorithm, which is negligible (at most a few operations for standard 32-bit integers). All other
 divisions, subtractions, and comparisons take (O(1)) constant time.
3. Total Time: Overwhelmingly fast. Even if the server upgrade constraints are scaled to massive numbers, the execution
completes in under 1 millisecond because it completely bypasses second-by-second iteration.

Space Complexity: (O(1))
1. Fixed Variable Footprint: The algorithm relies strictly on a few primitive long variables (low, high, mid, blocked1,
etc.) to track bounds and mathematical slices.
2. No Allocation Overhead: There are no arrays, lists, maps, or recursive call stacks that scale up with larger inputs.
3. Total Space: Constant (O(1)) memory.
 */

public class ServerUpgrade {

    /**
     * Calculates the Greatest Common Divisor (GCD) using the Euclidean algorithm.
     */
    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    /**
     * Finds the minimum total time required to upgrade both servers.
     */
    public static long getMinUpgradationTime(int req1, int t1, int req2, int t2) {
        // Step 1: Set up the binary search net (low to a high maximum value)
        long low = 1;
        long high = 1_000_000_000_000_000L; // 10^15 handles large constraints cleanly
        long optimalTimeFound = high;

        // Step 2: Calculate LCM to find the exact seconds where BOTH servers are blocked (clash points)
        long r1 = req1;
        long r2 = req2;
        long lcm = (r1 * r2) / gcd(r1, r2);

        // Step 3: Start cutting the timeline in half
        while (low <= high) {
            long mid = low + (high - low) / 2; // Test a hypothetical window of 'mid' seconds

            // --- INCLUSION-EXCLUSION MATH ---

            // Total seconds individually blocked by requests
            long blocked1 = mid / r1;
            long blocked2 = mid / r2;
            long blockedBoth = mid / lcm; // Wasted seconds where both are busy simultaneously

            // Exclusive seconds: One server is blocked, forcing the mechanic to work on the other
            long onlyServer2CanWork = blocked1 - blockedBoth;
            long onlyServer1CanWork = blocked2 - blockedBoth;

            // Shared Free Seconds: Neither server is blocked, either one can be upgraded
            long sharedFreeTime = mid - (blocked1 + blocked2 - blockedBoth);

            // Deduct the exclusive slots from their required upgrade targets
            long remainingT1 = Math.max(0L, t1 - onlyServer1CanWork);
            long remainingT2 = Math.max(0L, t2 - onlyServer2CanWork);

            // --- THE VALIDITY CHECK ---
            // If the leftover work can fit inside the shared free time slots, this 'mid' window works!
            if (remainingT1 + remainingT2 <= sharedFreeTime) {
                optimalTimeFound = mid;  // Record this valid timeline
                high = mid - 1;          // Shrink the ceiling to look for a faster completion time
            } else {
                low = mid + 1;           // Not enough time, expand the floor to test a larger window
            }
        }

        return optimalTimeFound;
    }

    /**
     * Driver method utilizing modern Java 21+ Instance/Empty main syntax
     */
    static void main() {
        // Test Case 1: Our paper trace example (Sports Car & Truck)
        // req1=2, t1=3, req2=3, t2=2 -> Should take exactly 5 seconds
        System.out.println("Test Case 1 Result: " + getMinUpgradationTime(2, 3, 3, 2)); // Output: 5

        // Test Case 2: Both blocked at the exact same interval frequencies
        // req1=2, t1=1, req2=2, t2=3 -> Should take exactly 7 seconds
        System.out.println("Test Case 2 Result: " + getMinUpgradationTime(2, 1, 2, 3)); // Output: 7

        // Test Case 3: Large constraints where standard hour-by-hour loops would crash/timeout
        System.out.println("Test Case 3 (Large) Result: " + getMinUpgradationTime(777777, 1000000, 888888, 2000000));
    }
}

