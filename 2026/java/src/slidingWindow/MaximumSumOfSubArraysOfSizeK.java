package src.slidingWindow;

/*
Given an array of integers nums and an integer k, find the maximum sum of any contiguous subarray of size k.

1. This problem uses a fixed-size sliding window to efficiently find the maximum sum among all subarrays of length k.
2. Instead of recalculating the sum for each subarray from scratch, we slide the window across the array and update the
sum incrementally.
3. This approach is efficient because we calculate each window's sum in constant time by:
    3.1 Adding the new element entering the window (nums[end])
    3.2 Subtracting the old element leaving the window (nums[start])
4. Instead of summing k elements for each window (which would be O(n*k)), we do constant work per window, giving us
O(n) time complexity.
 */

public class MaximumSumOfSubArraysOfSizeK {
    public int getMaximumSumOfSubArraysOfSizeK(int[] nums, int k){
        int maxSum = Integer.MIN_VALUE;

        //sanity check
        if(nums == null || k <= 0 || nums.length < k){
            return maxSum;
        }

        int windowSum = 0;

        // Logic: Sliding window. You use two pointers, start and end. You keep adding to a windowSum using the end
        // pointer. After every addition from end pointer you check whether the end - start + 1 == k, i.e, you verify
        // whether the window has been achieved or not. If yes, then you take the max of maxSum against the windowSum
        // which is the running sum. Now, since we have achieved our window, we would now like to move to a new window.
        // Moving to a new window means letting go of the old start and adding the new end. So the window slides !
        // Meaning you subtract the old start, bump up the start pointer and add the new end. That way for every new
        // window you are only doing 2 operations, subtracting old start and adding new end, all the middle elements
        // remain the same.

        for(int startPtr = 0, endPtr = 0; endPtr < nums.length - 1; endPtr++){
            // we use endPtr to add to the windowSum
            windowSum += nums[endPtr];

            // check for window
            if (endPtr - startPtr + 1 == k){
                // check for the new maxSum
                maxSum = Math.max(maxSum, windowSum);
                // remove the old start from the existing windowSum
                windowSum -= nums[startPtr];
                // increment the startPtr for the new window
                startPtr++;
            }
        }

        return maxSum;
    }

    static void main(){
        MaximumSumOfSubArraysOfSizeK maximumSumOfSubArraysOfSizeK = new MaximumSumOfSubArraysOfSizeK();
        System.out.println(maximumSumOfSubArraysOfSizeK.getMaximumSumOfSubArraysOfSizeK(new int[]{2,1,5,1,3,2}, 3));
    }
}
