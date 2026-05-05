package src.two_pointers;

/*
Given an integer array nums, return the number of triplets chosen from the array that can make triangles if we take
them as side lengths of a triangle.

Example 1:

Input: nums = [2,2,3,4]
Output: 3

Explanation: Valid combinations are:
2,3,4 (using the first 2)
2,3,4 (using the second 2)
2,2,3
Example 2:

Input: nums = [4,2,3,4]
Output: 4

Constraints:

1 <= nums.length <= 1000
0 <= nums[i] <= 1000
*/

import java.util.Arrays;

/*
Time Complexity = O(n ^ 2). In Big O notation, we only care about the biggest "time eater."
1. Sorting => n log n
2. two pointers => O(n ^ 2), because they get initialized everytime 'i' changes, resulting into a secondary scan for
every 'i'.

 */

// Trick: 1) need to sort the array 2) initialize 'i' from end
public class ValidTriangleNumber {
    private int getValidTriangleCombinations(int[] nums){
        int result = 0;
        // sanity check
        if(nums == null || nums.length < 2){
            return result;
        }

        // Logic: Valid triangle is one where the sum of two of any three sides exceeds the third.
        // a + b > c, a + c > b and b + c > a needs to be true. If we sort the array, then we need to check only one
        // condition, other two will automatically be true
        Arrays.sort(nums);

        // two pointer approach. Array is sorted, so our iterator starts from the greatest element which is at the last.
        // if the left and right pointer sum is greater than the 'i' value, it means all the elements inside the left
        // and right range also suffice. So we take right - left as the possible combination count. Then we decrease the
        // range by moving the right pointer to test whether a + b > c still holds true. If a + b < c then we increase
        // the left pointer to increase the addition outcome of the two numbers
        for(int i = nums.length - 1; i > 1; i--){
            int smallerPtr = 0;
            int largerPtr = i - 1;

            while(smallerPtr < largerPtr){
                if(nums[smallerPtr] + nums[largerPtr] > nums[i]){
                    // all the elements inside this range are possible combinations
                    result += largerPtr - smallerPtr;
                    // now test whether a + b > c still holds true if you make b smaller
                    largerPtr--;
                } else {
                    // a + b is smaller or equal to c. Means we need to increase our base.
                    smallerPtr++;
                }
            }
        }

        return result;
    }

    static void main(){
        ValidTriangleNumber validTriangleNumber = new ValidTriangleNumber();
        System.out.println(validTriangleNumber.getValidTriangleCombinations(new int[]{4,2,3,4}));
        System.out.println(validTriangleNumber.getValidTriangleCombinations(new int[]{2,2,3,4}));
    }
}
