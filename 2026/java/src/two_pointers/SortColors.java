package src.two_pointers;

/*
Given an array nums with n objects colored red, white, or blue, sort them in-place so that objects of the same color
are adjacent, with the colors in the order red, white, and blue. We will use the integers 0, 1, and 2 to represent the
color red, white, and blue, respectively.

You must solve this problem without using the library's sort function.

 Example 1:

Input: nums = [2,0,2,1,1,0]
Output: [0,0,1,1,2,2]
Example 2:

Input: nums = [2,0,1]
Output: [0,1,2]

Constraints:

n == nums.length
1 <= n <= 300
nums[i] is either 0, 1, or 2.

Follow up: Could you come up with a one-pass algorithm using only constant extra space?
 */

/*
Time complexity : O(N) since it's one pass along the array of length N.

Space complexity : O(1) since it's a constant space solution.

O(N) can still be done with two pass brute force and constant space by counting how many zeros, ones and twos are there
in the array in the first pass and then in the second pass we simply overwrite the given input array.
 */

public class SortColors {
    private int[] getSortedColors(int[] nums){
        /* Logic: Dutch National Flag problem. We have 3 colors (0,1,2). We will use 2 pointers. Left to indicate zero,
        right to indicate 2. We will also use an iterator 'i' which defines the swap trigger and our iteration stop when
        'i' crosses the right pointer. Result we want is all the two's at the end. So our right pointer will start from
        the end. And similarly, left pointer which represents zero starts from zero index. Iterator 'i' also starts from
        zero.
        - When nums[i] == 2 swap with nums[right] and decrement the right pointer.
        - When nums[i] == 0 then there are two cases
            (i) if nums[left] is already zero, then the swap could have come from right pointer or it was already
        zero. In this case we need to increment both left and 'i' pointer (otherwise the while loop break condition of
        'i' being larger never succeeds).
            (ii) if nums[left] != 0, simple swap with nums[i] and move the left pointer
        - if nums[i] == 1 simply increment 'i'
         */

        //initialize the pointers
        int i = 0;
        int left = 0;
        int right = nums.length - 1;

        while(i <= right){
            if(nums[i] == 1){
                i++;
            } else if(nums[i] == 2){
                //swap with right
                nums[i] = nums[right];
                nums[right] = 2;
                //decrement the right pointer
                right--;
            } else { //nums[i] == 0
                if(nums[left] == 0){
                    //move both pointers
                    i++;
                    left++;
                } else { //nums[left] != 0
                    //swap with left
                    nums[i] = nums[left];
                    nums[left] = 0;
                    //increment the left pointer
                    left++;
                }
            }
        }

        return nums;
    }

    private int[] twoPassOverWriteSolution(int[] nums){
        // Logic: first pass keep count of 0, 1 and 2. Second pass re-write the input array accordingly.

        int zeroCount = 0;
        int oneCount = 0;
        int twoCount = 0;

        //first pass
        for(int num: nums){
            if(num == 0){
                zeroCount++;
            } else if(num == 1){
                oneCount++;
            } else {
                twoCount++;
            }
        }

        //second pass
        for(int i = 0; i < nums.length; i++){
            if(zeroCount > 0){
                nums[i] = 0;
                zeroCount--;
            } else if (oneCount > 0) {
                nums[i] = 1;
                oneCount--;
            } else {
                nums[i] = 2;
                twoCount--;
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
        SortColors sortColors = new SortColors();

        // O(N) one pass with constant space
        sortColors.printIntArray(sortColors.getSortedColors(new int[]{2,0,2,1,1,0}));
        sortColors.printIntArray(sortColors.getSortedColors(new int[]{2,0,1}));

        // O(N) two pass with constant space
        sortColors.printIntArray(sortColors.twoPassOverWriteSolution(new int[]{2,0,2,1,1,0}));
        sortColors.printIntArray(sortColors.twoPassOverWriteSolution(new int[]{2,0,1}));
    }
}
