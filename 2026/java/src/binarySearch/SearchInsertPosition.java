package src.binarySearch;

/*
Given a sorted array of distinct integers and a target value, return the index if the target is found. If not, return
the index where it would be if it were inserted in order. You must write an algorithm with O(log n) runtime complexity.

Example 1:
Input: nums = [1,3,5,6], target = 5
Output: 2

Example 2:
Input: nums = [1,3,5,6], target = 2
Output: 1

Example 3:
Input: nums = [1,3,5,6], target = 7

Output: 4

Constraints:

1 <= nums.length <= 104
-104 <= nums[i] <= 104
nums contains distinct values sorted in ascending order.
-104 <= target <= 104
 */

/*
Time Complexity: O(log N)

Space Complexity: O(1)
 */

/*
Q) Why calculation of 'mid = left + (right - left)/2' matters ?
Ans: Imagine you are performing a binary search on a massive dataset (like an enterprise transactional table).
left = 2,000,000,000 (2 Billion)
right = 2,100,000,000 (2.1 Billion)
Both of these numbers are perfectly valid integers because they are under the 2.14 Billion limit. But look at what
happens when the computer evaluates your formula: mid = (left + right) / 2
First, it tries to compute left + right: (2,000,000,000 + 2,100,000,000 = 4,100,000,000)
The Crash: 4,100,000,000 is way bigger than the max capacity of 2,147,483,647 (int 2 ^ 31 - 1). The bits roll over, and
Java interprets this giant sum as -194,967,296. Next, it divides by 2: (-194,967,296 / 2 = -97,483,648 )
The Result: Your mid-index becomes -97,483,648. The very next line nums[mid] tries to look up a negative array index,
causing the entire system to instantly crash with an ArrayIndexOutOfBoundsException.

Safe Calculation: mid = left + (right - left) / 2
Now let's run the exact same numbers through the defensive formula. Instead of adding the two giant numbers together,
this formula calculates the distance between them first.
First, it computes right - left: (2,100,000,000 - 2,000,000,000 = 100,000,000). 100 Million is a small number. No
overflow risk!. Next, it divides that distance by 2: (100,000,000 / 2 = 50,000,000)
Finally, it adds that safe offset back to left:(2,000,000,000 + 50,000,000 = 2,050,000,000)
 */
public class SearchInsertPosition {
    private int getInsertIndex(int[] nums, int target){
        // sanity check
        if(nums == null || nums.length == 0){
            return 0;
        }

        // Given that array is sorted we use binary search. Two scenarios. Target will be found at mid, else it won't be
        // found. In that case return the left pointer, as that will be the place where we would want that element to
        // be present. You can also figure that out using mid, see the custom logic below. You came up with the logic
        // for mid-return by yourself.

        int left = 0;
        int right = nums.length - 1;
        int mid = 0;

        // binary search
        while(left <= right){
            mid = left + (right - left) / 2; // to avoid Integer overflow
            // another cool way to do this without causing overflow is mid = (left + right) >>> 1;
            int valueAtMid = nums[mid];

            if(valueAtMid == target){
                return mid;
            } else if(valueAtMid < target){
                // search right of mid
                left = mid + 1;
            } else {
                // search left of mid
                right = mid -1;
            }
        }

        return left; // binary search fails to find the element then left pointer is where the element should be present

        /*
        Even this works:
        if(target > nums[mid]){
            return mid + 1;
        } else {
            // target < nums[mid]. It cannot be == nums[mid], because then it would have been found in the binary search
            return mid;
        }
         */
    }

    public static void main(String[] args){
        SearchInsertPosition searchInsertPosition = new SearchInsertPosition();
        System.out.println(searchInsertPosition.getInsertIndex(new int[]{1,3,5,6}, 5));
        System.out.println(searchInsertPosition.getInsertIndex(new int[]{1,3,5,6}, 0));
        System.out.println(searchInsertPosition.getInsertIndex(new int[]{1,3,5,6}, 2));
        System.out.println(searchInsertPosition.getInsertIndex(new int[]{1,3,5,6}, 7));
    }
}
