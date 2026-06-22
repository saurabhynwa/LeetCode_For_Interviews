package src.dynamic_programming;

/*
Given two strings text1 and text2, return the length of their longest common subsequence. If there is no common
subsequence, return 0. A subsequence of a string is a new string generated from the original string with some characters
 (can be none) deleted without changing the relative order of the remaining characters.

For example, "ace" is a subsequence of "abcde".
A common subsequence of two strings is a subsequence that is common to both strings.

Example 1:

Input: text1 = "abcde", text2 = "ace"
Output: 3
Explanation: The longest common subsequence is "ace" and its length is 3.
Example 2:

Input: text1 = "abc", text2 = "abc"
Output: 3
Explanation: The longest common subsequence is "abc" and its length is 3.
Example 3:

Input: text1 = "abc", text2 = "def"
Output: 0
Explanation: There is no such common subsequence, so the result is 0.


Constraints:

1 <= text1.length, text2.length <= 1000
text1 and text2 consist of only lowercase English characters.
 */

/*
Time Complexity: O(text1 Length x text2 Length)
Space Complexity: O(text1 Length x text2 Length)
 */

public class LongestCommonSubsequence {
    private int getLongestCommonSubsequenceLength(String text1, String text2){
        // Logic: we are asked to find longest common subsequence, not continuous but strictly in increasing order.
        // Visualize a matrix or 2D grid, where text1 is vertical and text2 is horizontal. Then only the diagonal
        // elements will be matching. There are 2 rules. Rule_1: when match, then we take the value from diagonal
        // up-and-left and add 1 to it. Rule_2: mismatch, take max of left and up cells. This way our answer is running
        // in a cumulative manner from starting cell [left most] to ending cell [bottom right].

        // we declare the matrix for an extra length. This is for the first match. Since we need diagonal values to
        // carry forward the result state.

        if(text1 == null || text2 == null || text1.isEmpty() || text2.isEmpty()){
            return 0;
        }

        // declare 2D grid, with extra column for zero padding
        int[][] gridMatch = new int[text1.length() + 1][text2.length() + 1];

        for(int row = 1; row <= text1.length(); row++){
            char rowChar = text1.charAt(row - 1);

            for(int col = 1; col <= text2.length(); col++){
                char colChar = text2.charAt(col - 1);

                if(rowChar == colChar){
                    // apply match rule
                    gridMatch[row][col] = gridMatch[row - 1][col - 1] + 1;
                } else {
                    // max of left and top
                    gridMatch[row][col] = Math.max(gridMatch[row][col - 1], gridMatch[row - 1][col]);
                }
            }
        }

        return gridMatch[text1.length()][text2.length()];
    }

    public static void main(String[] args){
        LongestCommonSubsequence longestCommonSubsequence = new LongestCommonSubsequence();
        System.out.println(longestCommonSubsequence.getLongestCommonSubsequenceLength("abcde", "ace"));
        System.out.println(longestCommonSubsequence.getLongestCommonSubsequenceLength("abc", "abc"));
        System.out.println(longestCommonSubsequence.getLongestCommonSubsequenceLength("abc", "def"));
    }
}
