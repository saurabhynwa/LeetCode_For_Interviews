package src.dynamic_programming;

/*
You are given an integer array nums. You are initially positioned at the array's first index, and each element in the
array represents your maximum jump length at that position. Return true if you can reach the last index, or false otherwise.

Example 1:
Input: nums = [2,3,1,1,4]
Output: true
Explanation: Jump 1 step from index 0 to 1, then 3 steps to the last index.

Example 2:
Input: nums = [3,2,1,0,4]
Output: false
Explanation: You will always arrive at index 3 no matter what. Its maximum jump length is 0, which makes it impossible
to reach the last index.

Constraints:

1 <= nums.length <= 104
0 <= nums[i] <= 105
 */

/*
Time Complexity: O(N)
Space Complexity: O(1)
 */

public class JumpGame {
    public boolean canJump(int[] nums) {
        // Edge Case Validation: An empty or null pointer array is structurally invalid
        if (nums == null || nums.length == 0) {
            return false;
        }

        int maximumReachableIndex = 0;
        int finalDestinationIndex = nums.length - 1;

        // Perform a single, linear greedy pass across the array indices
        for (int currentIndex = 0; currentIndex <= finalDestinationIndex; currentIndex++) {

            // THE DEAD ZONE CHECK: If our active pointer outpaces our historical maximum horizon,
            // we have hit an un-passable gap (e.g., trapped by a previous 0 cell value). Fail fast!
            if (currentIndex > maximumReachableIndex) {
                return false;
            }

            // Greedy Update: Maximize our horizon boundary from the current vantage point
            int potentialJumpHorizon = currentIndex + nums[currentIndex];
            maximumReachableIndex = Math.max(maximumReachableIndex, potentialJumpHorizon);

            // EARLY TERMINATION OPTIMIZATION: If the current horizon successfully clears or
            // touches the finish line, we have guaranteed a path. Return true immediately!
            if (maximumReachableIndex >= finalDestinationIndex) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        JumpGame jumpGame = new JumpGame();
        System.out.println(jumpGame.canJump(new int[]{2,3,1,1,4}));
        System.out.println(jumpGame.canJump(new int[]{3,2,1,0,4}));
    }
}

