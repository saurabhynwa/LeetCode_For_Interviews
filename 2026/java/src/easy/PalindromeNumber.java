package src.easy;

/*
Given an integer x, return true if x is a palindrome, and false otherwise.

Example 1:
Input: x = 121
Output: true
Explanation: 121 reads as 121 from left to right and from right to left.

Example 2:
Input: x = -121
Output: false
Explanation: From left to right, it reads -121. From right to left, it becomes 121-. Therefore it is not a palindrome.

Example 3:
Input: x = 10
Output: false
Explanation: Reads 01 from right to left. Therefore it is not a palindrome.

Constraints:

-231 <= x <= 231 - 1
 */

/*
Time Complexity: O(log_{10} x) because we divide the input value by 10 during every single loop pass. For a maximum
32-bit integer, this loop is mathematically guaranteed to run at most 10 times, making it perform at an effective
constant speed.

Space Complexity: O(1) perfect constant space auxiliary footprint as it manipulates primitive stack registers directly
without allocating any string heap memory footprints.
 */

public class PalindromeNumber {
    /*
    - The Trap: Java 'int' variables have a hard boundary ceiling of exactly 2,147,483,647 (Integer.MAX_VALUE).
    - The Failure Case: If the input integer x is a non-palindrome number that is close to the limit—for example,
     x = 1,463,847,412 — look what happens when your code tries to reverse it:
        - The mathematical reverse of x is 2,147,483,641.
        - During the very last loop iteration, sum * 10 + rightMostNumber attempts to compute 2,147,483,641.
        - Because 2,147,483,641 is strictly greater than 2,147,483,647, the 32-bit integer register overflows, warps
        into a negative number, and corrupted data is returned.

     - The Fix: Upgrade your tracking variables to long inside the mathematical processing pipeline to shield the
     multiplier sequence from data truncation traps.
     */

    private boolean isPalindrome(int x){
        // single positive digits are always palindrome
        if(x >= 0 && x <= 9){
            return true;
        }

        // -ve integers can never be palindrome as per the question due to the negative sign taken into consideration
        // as well.
        if(x < 0){
            return false;
        }

        // reconstruct the number from right to left and compare with the originalNumber
        int originalNumber = x;
        long reversedSum = 0; // to avoid integer overflow trap

        while(x > 0){
            // get the right most digit
            int rightMostDigit = x % 10;
            reversedSum = reversedSum * 10 + rightMostDigit;
            // drop the right most digit
            x = x / 10;
        }

        return originalNumber == reversedSum;
    }

    public static void main(String[] args){
        PalindromeNumber palindromeNumber = new PalindromeNumber();
        System.out.println(palindromeNumber.isPalindrome(121));
        System.out.println(palindromeNumber.isPalindrome(-121));
        System.out.println(palindromeNumber.isPalindrome(10));
    }
}
