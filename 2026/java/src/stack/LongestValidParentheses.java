package src.stack;

/*
LeetCode "Hard": Given a string containing just the characters '(' and ')', return the length of the longest valid
(well-formed) parentheses substring.

Example 1:

Input: s = "(()"
Output: 2
Explanation: The longest valid parentheses substring is "()".
Example 2:

Input: s = ")()())"
Output: 4
Explanation: The longest valid parentheses substring is "()()".
Example 3:

Input: s = ""
Output: 0

Constraints:

0 <= s.length <= 3 * 104
s[i] is '(', or ')'.
 */

import java.util.Stack;

/*
Time Complexity = O(N). We scan the string only once.

Space Complexity = O(N). Stack can grow up to string length (imagine a string with all open parentheses)
 */

public class LongestValidParentheses {
    private int getLongestValidParenthesesLength(String s){
        int maxLength = 0;

        // sanity check
        if(s == null || s.trim().length() <= 1){
            return maxLength;
        }

        //Logic: we are given open and close brackets. Valid parentheses is one when an open bracket is followed by a
        // closed bracket. We take into account the length of open bracket as well. So if you think of a wall or a start
        // point before the open bracket, then the index of closed bracket - that of wall/start is our answer.

        // Now let's say open bracket '(' appears at the 0th index. Then the wall/start would be behind it, as right to
        // the open bracket can be a closed bracket ')'. Hence, we seed our stack with -1.

        // Another case could be that our stack is empty, and we are on a closed bracket index and there are yet
        // characters to be scanned. So the current closed bracket becomes our wall/start/boundary as it will not have
        // any match going forward. So we push its index to stack to mark the current boundary.

        // push indices instead of characters to the stack
        Stack<Integer> indexStack = new Stack<>();
        indexStack.push(-1); // 0th index open '(' case

        for(int index = 0; index < s.length(); index++){
            char current = s.charAt(index);

            if(current == '('){
                // push the current index to stack
                indexStack.push(index);
            } else {
                // pop and then process
                indexStack.pop(); // can't be empty as we have seeded it with -1

                // check for emptiness now
                if(indexStack.isEmpty()){
                    // update the current boundary
                    indexStack.push(index);
                } else {
                    // we found a match for the closed ')', whose index was popped at the start of this else block.
                    // Now stack top will be the index of the boundary. Subtract that from the current index and compare
                    // for maxLength
                    maxLength = Math.max(maxLength, index - indexStack.peek());
                }
            }
        }

        return maxLength;
    }

    public static void main(String[] args){
        LongestValidParentheses lvp = new LongestValidParentheses();
        System.out.println(lvp.getLongestValidParenthesesLength(")()"));
        System.out.println(lvp.getLongestValidParenthesesLength("(()"));
        System.out.println(lvp.getLongestValidParenthesesLength(")()())"));
        System.out.println(lvp.getLongestValidParenthesesLength(""));
    }
}
