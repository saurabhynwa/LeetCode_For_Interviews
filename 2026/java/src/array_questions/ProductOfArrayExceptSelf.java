package src.array_questions;

/*
Given an integer array nums, return an array answer such that answer[i] is equal to the product of all the elements of
nums except nums[i]. The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.

You must write an algorithm that runs in O(n) time and without using the division operation.

Example 1:
Input: nums = [1,2,3,4]
Output: [24,12,8,6]

Example 2:
Input: nums = [-1,1,0,-3,3]
Output: [0,0,9,0,0]

Constraints:

2 <= nums.length <= 105
-30 <= nums[i] <= 30
The input is generated such that answer[i] is guaranteed to fit in a 32-bit integer.

Follow up: Can you solve the problem in O(1) extra space complexity? (The output array does not count as extra space for
 space complexity analysis.)
 */

/*
Time Complexity: O(N) where N is the length of the array. We make exactly two separate, linear passes through the data.

Space Complexity: O(1) Constant Space. As per the official LeetCode problem specifications, the output array does not
count as extra space for the purpose of complexity analysis. We only allocated one single primitive integer variable
(runningSuffixProduct), leaving zero garbage collection footprint on the heap.
 */
public class ProductOfArrayExceptSelf {
    private int[] getProductExceptSelf(int[] nums){
        // edge case
        if(nums == null || nums.length == 0){
            return new int[0];
        }

        // Logic: for the current index value of nums the product is made up of two parts. Multiplication of all the
        // values to its left and product of that with multiplication of all the values to the right. We do it in 2 pass

        // left pass => leftProductAggregate is the value at index on the result array before the current index and to
        // this we multiply the previous index value w.r.t to the input array nums
        int[] result = new int[nums.length];
        result[0] = 1; // there is no left element to the zero index

        for(int current = 1; current < result.length; current++){
            // previous nums value * left product aggregate
            result[current] = nums[current - 1] * result[current - 1];
        }

        // right pass, we start from end. Right product aggregate now stays updated in a variable. The current value on
        // result array is the product of left values for the current index. Now this place needs to be updated for the
        // final result. That would be, leftProductAggregate * rightProductAggregate. Now once we do that, we move to
        // the left. While doing so, we need to update the rightProductAggregate. Now for the newer index on the left,
        // the rightProductAggregate would be the multiplication of the value to its right and the rightProductAggregate
        // so far. So while moving towards left from the current position, we update the rightProductAggregate.
        int rightProductAggregate = 1;

        for(int current = result.length - 1; current >= 0; current--){
            // leftProductAggregate * rightProductAggregate
            result[current] = result[current] * rightProductAggregate;

            // now before we move to the left index, we update our rightProductAggregate
            rightProductAggregate = rightProductAggregate * nums[current];
        }

        return result;
    }

    private void printResult(int[] nums){
        for(int num: nums){
            System.out.print(num + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ProductOfArrayExceptSelf productOfArrayExceptSelf = new ProductOfArrayExceptSelf();
        productOfArrayExceptSelf.printResult(productOfArrayExceptSelf.getProductExceptSelf(new int[]{1,2,3,4}));
    }
}
