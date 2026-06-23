package src.two_pointers;

/*
Given an array of integers nums, half of the integers in nums are odd, and the other half are even. Sort the array so
that whenever nums[i] is odd, 'i' is odd, and whenever nums[i] is even, 'i' is even. Return any answer array that
satisfies this condition.

Example 1:

Input: nums = [4,2,5,7]
Output: [4,5,2,7]
Explanation: [4,7,2,5], [2,5,4,7], [2,7,4,5] would also have been accepted.
Example 2:

Input: nums = [2,3]
Output: [2,3]


Constraints:

2 <= nums.length <= 2 * 104
nums.length is even.
Half of the integers in nums are even.
0 <= nums[i] <= 1000

 */

/*
Time Complexity = O(N)
Space Complexity = O(1)
 */

public class SortArrayByParityII {
    private int[] sortArrayByParity(int[] nums){
        if (nums == null || nums.length == 0) {
            return nums;
        }

        int evenPointer = 0;
        int oddPointer = 1;
        int arrayLength = nums.length;

        // Loop runs until one of our index boundaries walks off the array edge
        while (evenPointer < arrayLength && oddPointer < arrayLength) {

            // 1. Advance the even pointer as long as the numbers are correctly even
            if (nums[evenPointer] % 2 == 0) {
                evenPointer += 2;
            }
            // 2. Advance the odd pointer as long as the numbers are correctly odd
            else if (nums[oddPointer] % 2 != 0) {
                oddPointer += 2;
            }
            // 3. Both pointers are stuck on confirmed mismatches of opposite types.
            // Swap them to fix both index invariants simultaneously!
            else {
                int temporaryValueExchange = nums[evenPointer];
                nums[evenPointer] = nums[oddPointer];
                nums[oddPointer] = temporaryValueExchange;

                // Advance both pointers forward past the fixed elements
                evenPointer += 2;
                oddPointer += 2;
            }
        }

        return nums;
    }

    private void printResult(int[] nums){
        for(int num: nums){
            System.out.print(num + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        SortArrayByParityII obj = new SortArrayByParityII();
        obj.printResult(obj.sortArrayByParity(new int[]{4,2,5,7}));
    }
}
