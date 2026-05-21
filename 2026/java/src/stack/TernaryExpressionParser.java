package src.stack;

import java.util.Stack;

/*
Given a string expression representing arbitrarily nested ternary expressions, evaluate the expression, and return the
result of it. You can always assume that the given expression is valid and only contains digits, '?', ':', 'T', and 'F'
where 'T' is true and 'F' is false. All the numbers in the expression are one-digit numbers (i.e., in the range [0, 9]).

The conditional expressions group right-to-left (as usual in most languages), and the result of the expression will
always evaluate to either a digit, 'T' or 'F'.

Example 1:

Input: expression = "T?2:3"
Output: "2"
Explanation: If true, then result is 2; otherwise result is 3.
Example 2:

Input: expression = "F?1:T?4:5"
Output: "4"
Explanation: The conditional expressions group right-to-left. Using parenthesis, it is read/evaluated as:
"(F ? 1 : (T ? 4 : 5))" --> "(F ? 1 : 4)" --> "4"
or "(F ? 1 : (T ? 4 : 5))" --> "(T ? 4 : 5)" --> "4"
Example 3:

Input: expression = "T?T?F:5:3"
Output: "F"
Explanation: The conditional expressions group right-to-left. Using parenthesis, it is read/evaluated as:
"(T ? (T ? F : 5) : 3)" --> "(T ? F : 3)" --> "F"
"(T ? (T ? F : 5) : 3)" --> "(T ? F : 5)" --> "F"


Constraints:

5 <= expression.length <= 104
expression consists of digits, 'T', 'F', '?', and ':'.
It is guaranteed that expression is a valid ternary expression and that each number is a one-digit number.
 */

/*
Time Complexity: (O(N)) where (N) is the length of the string. We look at each character exactly once during our
backward sweep, and heap/stack operations are all constant (O(1)).

Space Complexity: (O(N)) auxiliary stack memory space required to buffer the historical components before they get
processed by a conditional trigger.
 */

public class TernaryExpressionParser {
    // Logic: ternary operations are evaluated right to left. So scan the string from end to start. If you start from
    // left to right you will get stuck.

    // Push everything to stack other than '?'. Once you reach '?', get the next element from the string which should be
    // either true or false. Now get the next 3 elements from stack, throw away the ':'. push back the result to stack
    // basis true or false. Don't push the T(true) or (false), instead move to the next element
    private String getTernaryExpressionParserResult(String expression){
        Stack<Character> stack = new Stack<>();
        int i = expression.length() - 1;

        while(i >= 0){
            char current = expression.charAt(i);

            if(current != '?'){
                // push to stack
                stack.push(current);
                // decrement i
                i--;
            } else {
                boolean trueOrFalse = false;
                i--; // get the T or F left of '?'
                if(expression.charAt(i) == 'T'){
                    trueOrFalse = true;
                }
                // get the next 3 elements from stack
                char left = stack.pop();
                stack.pop(); // throw away the ':'
                char right = stack.pop();

                // push back the result to stack
                stack.push(trueOrFalse ? left : right);
                // Important: we need to skip current 'i' pointing to 'T'/'F', as we have already used it against a '?'
                i--;
            }
        }

        return stack.peek().toString();
    }

    public static void main(String[] args){
        TernaryExpressionParser tep = new TernaryExpressionParser();
        System.out.println(tep.getTernaryExpressionParserResult("T?2:3"));
        System.out.println(tep.getTernaryExpressionParserResult("F?1:T?4:5"));
        System.out.println(tep.getTernaryExpressionParserResult("T?T?F:5:3"));
    }
}
