package src.slidingWindow;

/*
Given a binary array nums, return the maximum number of consecutive 1's in the array if you can flip at most one 0.

Example 1:

Input: nums = [1,0,1,1,0]
Output: 4
Explanation:
- If we flip the first zero, nums becomes [1,1,1,1,0] and we have 4 consecutive ones.
- If we flip the second zero, nums becomes [1,0,1,1,1] and we have 3 consecutive ones.
The max number of consecutive ones is 4.
Example 2:

Input: nums = [1,0,1,1,0,1]
Output: 4
Explanation:
- If we flip the first zero, nums becomes [1,1,1,1,0,1] and we have 4 consecutive ones.
- If we flip the second zero, nums becomes [1,0,1,1,1,1] and we have 4 consecutive ones.
The max number of consecutive ones is 4.


Constraints:

1 <= nums.length <= 105
nums[i] is either 0 or 1.
 */

/*
Time Complexity = O(n)

Space Complexity = O(1)
 */

public class MaxConsecutiveOnesFlipOneZeroAtMost {
    public int findMaxConsecutiveOnes(int[] nums) {
        // sanity check
        if(nums == null || nums.length == 0){
            return 0;
        }

        // logic: sliding window. Window is valid till we have 1 zero. Moment we have more than 1, we slide window by
        // moving start pointer
        int zeroCountInWindow = 0;
        int result = 0;

        for(int startPtr = 0, endPtr = 0; endPtr < nums.length; endPtr++){
            int current = nums[endPtr];

            if(current == 0){
                zeroCountInWindow++;
            }

            while(zeroCountInWindow > 1){
                // move the start pointer
                int currentStart = nums[startPtr];

                // check if start pointer value is zero, then reduce the count as we are going to move the start pointer
                if(currentStart == 0){
                    zeroCountInWindow--;
                }

                // move start pointer
                startPtr++;
            }

            // check for result
            result = Math.max(result, endPtr - startPtr + 1);
        }

        return result;
    }

    static void main(){
        MaxConsecutiveOnesFlipOneZeroAtMost maxConsecutiveOnesFlipOneZeroAtMost = new MaxConsecutiveOnesFlipOneZeroAtMost();
        System.out.println(maxConsecutiveOnesFlipOneZeroAtMost.findMaxConsecutiveOnes(new int[]{1,0,1,1,0}));
        System.out.println(maxConsecutiveOnesFlipOneZeroAtMost.findMaxConsecutiveOnes(new int[]{1,0,1,1,0,1}));
    }
}
