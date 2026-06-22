package src.two_pointers;

/*
Given a string s, return the longest palindromic substring in s.

Example 1:
Input: s = "babad"
Output: "bab"
Explanation: "aba" is also a valid answer.

Example 2:
Input: s = "cbbd"
Output: "bb"

Constraints:

1 <= s.length <= 1000
s consist of only digits and English letters.
 */

/*
Time Complexity: O(N ^ 2)
Space Complexity: O(1)
 */

public class LongestPalindromicSubstring {
    private String getLongestPalindromeSubstring(String s) {
        // edge case
        if(s == null || s.trim().isEmpty()){
            return s;
        }

        // expand around the center. handle odd and even length
        int start = 0;
        int end = 0;

        for(int center = 0; center < s.length(); center++){
            int oddLengthPalindrome = expandAroundCenter(s, center, center);
            int evenLengthPalindrome = expandAroundCenter(s, center, center + 1);
            int currentMax = Math.max(oddLengthPalindrome, evenLengthPalindrome);

            if(currentMax > (end - start)){
                /* Since we are expanding around center, the left part of the center, i.e, left half starting point will
                 give us the start. So, subtracting half-length from the center. Similarly, for the right half just add
                 the half-length to the right of center, which will give us the end. "-1" is for extracting the zero
                 index from String "str" for start. For the end we can use the entire length as it is.
                 */
                start = center - (currentMax - 1) / 2;
                end = center + (currentMax / 2);
            }
        }

        // in substring method start is inclusive, end is exclusive (hence the +1)
        return s.substring(start, end + 1);
    }

    private int expandAroundCenter(String str, int leftBoundary, int rightBoundary){
        while(leftBoundary >= 0 && rightBoundary < str.length() && str.charAt(leftBoundary) == str.charAt(rightBoundary)){
            leftBoundary--;
            rightBoundary++;
        }

        // now out of the while loop, either of the boundaries can be crossing the palindromic substring (while loop !)
        return rightBoundary - leftBoundary - 1;
    }

    public static void main(String[] args){
        LongestPalindromicSubstring longestPalindromeSubstring = new LongestPalindromicSubstring();
        System.out.println(longestPalindromeSubstring.getLongestPalindromeSubstring("babad"));
        System.out.println(longestPalindromeSubstring.getLongestPalindromeSubstring("cbbd"));
    }
}
