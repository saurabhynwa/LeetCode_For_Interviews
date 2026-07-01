package src.dynamic_programming;

/*
You are given a 0-indexed array of integers nums of length n. You are initially positioned at index 0. Each element
nums[i] represents the maximum length of a forward jump from index i. In other words, if you are at index i, you can
jump to any index (i + j) where:

0 <= j <= nums[i] and
i + j < n

Return the minimum number of jumps to reach index n - 1. The test cases are generated such that you can reach index n - 1.

Example 1:
Input: nums = [2,3,1,1,4]
Output: 2
Explanation: The minimum number of jumps to reach the last index is 2. Jump 1 step from index 0 to 1, then 3 steps to the last index.

Example 2:
Input: nums = [2,3,0,1,4]
Output: 2

Constraints:

1 <= nums.length <= 104
0 <= nums[i] <= 1000
It's guaranteed that you can reach nums[n - 1].
 */

/*
Time Complexity = O(N)
Space Complexity = O(1)
 */

public class JumpGameII {
    public int jump(int[] nums) {
        // Edge Case Validation: If the array has 1 or fewer elements,
        // you are already standing on the finish line. Exactly 0 jumps required.
        if (nums == null || nums.length <= 1) {
            return 0;
        }

        int totalJumpsExecuted = 0;
        int maxReachableHorizon = 0;

        // This acts as our current wave boundary fence
        int currentJumpEndBoundary = 0;
        int finalDestinationIndex = nums.length - 1;

        // Loop runs up to 'finalDestinationIndex - 1'.
        // We do not evaluate the last index because once our boundary fence
        // reaches or clears the finish line, we stop counting jumps!
        for (int currentIndex = 0; currentIndex < finalDestinationIndex; currentIndex++) {

            // Constantly update the maximum horizon we can see from this tier step
            int potentialJumpHorizon = currentIndex + nums[currentIndex];
            maxReachableHorizon = Math.max(maxReachableHorizon, potentialJumpHorizon);

            // THE FENCE HIT: We have reached the end of our current jump's maximum range
            if (currentIndex == currentJumpEndBoundary) {

                // We are forced to execute another jump to step into the next wave tier
                totalJumpsExecuted++;

                // Advance our fence forward to the absolute best horizon we saw during this wave
                currentJumpEndBoundary = maxReachableHorizon;

                // EARLY TERMINATION HOOK: If our new fence successfully covers or
                // clears the final destination, no more jumps need to be computed!
                if (currentJumpEndBoundary >= finalDestinationIndex) {
                    break;
                }
            }
        }

        return totalJumpsExecuted;
    }

    public static void main(String[] args) {
        JumpGameII obj = new JumpGameII();
        System.out.println(obj.jump(new int[]{2,3,1,1,4}));
    }
}

