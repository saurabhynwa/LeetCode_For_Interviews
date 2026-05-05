package src.two_pointers;

/*
Given an integer array nums, move all 0's to the end of it while maintaining the relative order of the non-zero elements.

Note that you must do this in-place without making a copy of the array.

Example 1:

Input: nums = [0,1,0,3,12]
Output: [1,3,12,0,0]
Example 2:

Input: nums = [0]
Output: [0]

Constraints:

1 <= nums.length <= 104
-231 <= nums[i] <= 231 - 1


Follow up: Could you minimize the total number of operations done?

Time Complexity = O(n)
1. Why it is (O(n)) (Linear Time) ?
Ans: The positionPtr is initialized outside the loop and never resets. It only moves from left to right. The variable 'i'
goes from 0 to n-1.The variable positionPtr also goes from 0 to max(n-1).Since each pointer visits each index at most
once, the total number of operations is proportional to 2n, which simplifies to (O(n)).
 */
public class MoveZeroes {
    private int[] moveZeroesToLast(int[] nums){
        //logic: use two pointers. Position pointer will be used to detect indexes that have the value zero. This
        // pointer keeps on moving until it finds zero and it stops there. It acts as the source for swapping of a
        // non-zero number. The second pointer 'i' will be a simple iterator over the array which stops when it comes
        // across a non-zero number. Then it checks if it is greater than position pointer and if nums[i] != 0 then a
        // swap is initiated

        //edge case: input array doesn't have any zero, so need to keep array length check for the position pointer to
        // avoid ArrayIndexOutOfBound errors
        int positionPtr = 0;
        for(int i = 0; i < nums.length; i++){
            //move position pointer till it lands on a zero value
            while(positionPtr < nums.length && nums[positionPtr] != 0){
                positionPtr++;
            }

            //'i' looks for non-zero numbers for swap
            if(nums[i] != 0 && i > positionPtr){
                //initiate swap
                nums[positionPtr] = nums[i];
                nums[i] = 0;
            }
        }
        return nums;
    }

    private void printIntArray(int[] nums){
        for(int i = 0; i < nums.length; i++){
            if(i == 0){
                System.out.print("[" + nums[i]);
            } else if(i == nums.length-1){
                System.out.print(", " + nums[i] + "]");
            } else{
                System.out.print(", " + nums[i]);
            }
        }
        System.out.println();
    }

    static void main(){
        MoveZeroes moveZeroes = new MoveZeroes();
        moveZeroes.printIntArray(moveZeroes.moveZeroesToLast(new int[]{2,0,4,0,9}));

        moveZeroes.printIntArray(moveZeroes.moveZeroesToLast(new int[]{0,1,0,3,12}));
    }
}
