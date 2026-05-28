package src.binarySearch;

/*
There is an integer array nums sorted in ascending order (with distinct values). Prior to being passed to your function,
nums is possibly left rotated at an unknown index k (1 <= k < nums.length) such that the resulting array is [nums[k],
nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed). For example, [0,1,2,4,5,6,7] might be left
rotated by 3 indices and become [4,5,6,7,0,1,2]. Given the array nums after the possible rotation and an integer target,
return the index of target if it is in nums, or -1 if it is not in nums.

You must write an algorithm with O(log n) runtime complexity.

Example 1:

Input: nums = [4,5,6,7,0,1,2], target = 0
Output: 4

Example 2:
Input: nums = [4,5,6,7,0,1,2], target = 3
Output: -1

Example 3:
Input: nums = [1], target = 0
Output: -1

Constraints:

1 <= nums.length <= 5000
-104 <= nums[i] <= 104
All values of nums are unique.
nums is an ascending array that is possibly rotated.
-104 <= target <= 104
 */

/*
Time Complexity: O(log N)
Space Complexity: O(1)
 */
public class SearchRotatedSortedArray {
    private int getTargetIndexFromRotatedSortedArray(int[] nums, int target){
        // sanity check
        if(nums == null || nums.length == 0){
            return -1;
        }

        // Logic: Binary search. We know that the array is sorted, but has been rotated. So the array will have 2 parts
        // of sorted smaller arrays. We will start with binary search, and then we will check where our mid is landing.
        // The value at 'mid' gives us 2 possibilities. Either the left portion of 'mid' is balanced or the right one is
        // You then further adjust your boundary as per target value.
        int left = 0;
        int right = nums.length - 1;

        while(left <= right){
            int mid = left + (right - left) / 2; // to avoid integer overflow trap

            if(nums[mid] == target){
                return mid;
            }

            /*
            we use <= instead of < targets the exact boundary condition where standard binary search code usually breaks
            down. The reason the equals sign (<=) is absolutely mandatory is to handle the scenario when your search
            window shrinks down to exactly two elements or one element.

            When left and 'mid' land on the exact same index, using a strict < will cause the code to make the wrong
            choice and throw you into an infinite loop or cause you to miss the target entirely.
             */

            // now check whether the left portion is balanced or not w.r.t mid
            if(nums[left] <= nums[mid]){
                // check whether the target lies inside this range.
                if(nums[left] <= target && target < nums[mid]){
                    // search left of 'mid'
                    right = mid - 1;
                } else {
                    // search left of 'mid'
                    left = mid + 1;
                }
            } else {
                // target lies on the right side of 'mid'.
                // Check if it is in the range on the right side
                if(nums[mid] < target && target <= nums[right]){
                    // search to the right of 'mid'
                    left = mid + 1;
                } else {
                    // search to the left of 'mid'
                    right = mid - 1;
                }
            }
        }

        return -1;
    }

    public static void main(String[] args){
        SearchRotatedSortedArray obj = new SearchRotatedSortedArray();
        System.out.println(obj.getTargetIndexFromRotatedSortedArray(new int[]{4,5,6,7,0,1,2}, 0));
        System.out.println(obj.getTargetIndexFromRotatedSortedArray(new int[]{4,5,6,7,0,1,2}, 3));
        System.out.println(obj.getTargetIndexFromRotatedSortedArray(new int[]{1}, 0));
    }
}
