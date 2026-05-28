package src.slidingWindow;

/*
Given an array of positive integers nums and a positive integer target, return the minimal length of a subarray whose
sum is greater than or equal to target. If there is no such subarray, return 0 instead.

Example 1:
Input: target = 7, nums = [2,3,1,2,4,3]
Output: 2
Explanation: The subarray [4,3] has the minimal length under the problem constraint.

Example 2:
Input: target = 4, nums = [1,4,4]
Output: 1

Example 3:
Input: target = 11, nums = [1,1,1,1,1,1,1,1]
Output: 0

Constraints:

1 <= target <= 109
1 <= nums.length <= 105
1 <= nums[i] <= 104
 */

/*
Time Complexity : O(N)
Space Complexity: O(1)
 */
public class MinimumSizeSubArraySum {
    private int minSubArrayLen(int target, int[] nums) {
        // sanity check
        if(nums == null || nums.length == 0){
            return 0;
        }

        int minLength = Integer.MAX_VALUE;
        int windowSum = 0;

        for(int start = 0, end = 0; end < nums.length; end++){
            windowSum += nums[end];

            while(windowSum >= target){
                minLength = Math.min(minLength, end - start + 1);
                // slide window (elastic style)
                windowSum -= nums[start];
                start++;
            }
        }

        return minLength == Integer.MAX_VALUE ? 0 : minLength;
    }

    static void main() {
        MinimumSizeSubArraySum obj = new MinimumSizeSubArraySum();
        System.out.println(obj.minSubArrayLen(7, new int[]{2,3,1,2,4,3}));
    }
}
