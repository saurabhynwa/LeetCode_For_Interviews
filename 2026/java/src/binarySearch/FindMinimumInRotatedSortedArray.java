package src.binarySearch;

/*
Suppose an array of length n sorted in ascending order is rotated between 1 and n times. For example, the array nums =
[0,1,2,4,5,6,7] might become: [4,5,6,7,0,1,2] if it was rotated 4 times. [0,1,2,4,5,6,7] if it was rotated 7 times.
Notice that rotating an array [a[0], a[1], a[2], ..., a[n-1]] 1 time results in the array [a[n-1], a[0], a[1], a[2], ..., a[n-2]].

Given the sorted rotated array nums of unique elements, return the minimum element of this array. You must write an
algorithm that runs in O(log n) time.

Example 1:
Input: nums = [3,4,5,1,2]
Output: 1
Explanation: The original array was [1,2,3,4,5] rotated 3 times.

Example 2:
Input: nums = [4,5,6,7,0,1,2]
Output: 0
Explanation: The original array was [0,1,2,4,5,6,7] and it was rotated 4 times.

Example 3:
Input: nums = [11,13,15,17]
Output: 11
Explanation: The original array was [11,13,15,17] and it was rotated 4 times.

Constraints:

n == nums.length
1 <= n <= 5000
-5000 <= nums[i] <= 5000
All the integers of nums are unique.
nums is sorted and rotated between 1 and n times.
 */

/*
Time Complexity: O(log N)
Space Complexity: O(1)
 */
public class FindMinimumInRotatedSortedArray {
    private int getMinimumNumber(int[] nums){
        // sanity check
        if(nums == null || nums.length == 0){
            return 0;
        }

        // we will use binary search. Our 'mid' gives us 2 smaller sub arrays out of which one has to be sorted for
        // sure. Either it will be the left side, in which case nums[left] is the smallest element, so we then extend
        // our search to the right of mid or if it is the right of 'mid' then nums[mid] itself is the smallest. So we
        // then search to the left of mid.
        int left = 0;
        int right = nums.length - 1;
        int minElement = Integer.MAX_VALUE;

        while(left <= right){
            int mid = left + (right - left) / 2; // to avoid integer overflow

            if(nums[left] <= nums[mid]){
                // "<=" necessary in case array is of size 2 or 1 or in general, left & mid will collide on same index
                // at some point of time.

                minElement = Math.min(minElement, nums[left]);
                // extend search to the right side of min
                left = mid + 1;
            } else {
                minElement = Math.min(minElement, nums[mid]);
                // extend search to the left side of min
                right = mid -1;
            }
        }

        return minElement;
    }

    public static void main(String[] args) {
        FindMinimumInRotatedSortedArray obj = new FindMinimumInRotatedSortedArray();
        System.out.println(obj.getMinimumNumber(new int[]{3,4,5,1,2}));
        System.out.println(obj.getMinimumNumber(new int[]{4,5,6,7,0,1,2}));
        System.out.println(obj.getMinimumNumber(new int[]{11,13,15,17}));
    }
}
