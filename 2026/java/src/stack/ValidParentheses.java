package src.stack;

import java.util.Stack;
import java.util.Set;
import java.util.HashSet;

/*
Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.

An input string is valid if:

Open brackets must be closed by the same type of brackets.
Open brackets must be closed in the correct order.
Every close bracket has a corresponding open bracket of the same type.


Example 1:

Input: s = "()"

Output: true

Example 2:

Input: s = "()[]{}"

Output: true

Example 3:

Input: s = "(]"

Output: false

Example 4:

Input: s = "([])"

Output: true

Example 5:

Input: s = "([)]"

Output: false



Constraints:

1 <= s.length <= 104
s consists of parentheses only '()[]{}'.
 */

/*
Time Complexity: (O(n)) because it completes a single linear scan over the string.
Space Complexity: (O(n)) in the worst case (e.g., input is ((((((), where the stack grows up to the size of the input
length.
 */

public class ValidParentheses {
    private boolean isValidParentheses(String s){
        // Sanity check
        if(s == null || s.trim().isEmpty()){
            return true;
        }

        // Logic: use stack. Push open parentheses on the stack, pop when matching closing parentheses found. Mismatch
        // returns false
        Stack<Character> stack = new Stack<>();
        Set<Character> openParentheses = new HashSet<>();
        openParentheses.add('(');
        openParentheses.add('{');
        openParentheses.add('[');

        for(int i = 0; i < s.length(); i++){
            char current = s.charAt(i);

            if(openParentheses.contains(current)){
                // push to stack
                stack.push(current);
            } else {
                if(stack.isEmpty()){
                    return false;
                } else {
                    char openParenthesesFromStack = stack.peek();

                    if(current == ')' && openParenthesesFromStack == '('){
                        stack.pop();
                    } else if(current == '}' && openParenthesesFromStack == '{') {
                        stack.pop();
                    } else if(current == ']' && openParenthesesFromStack == '[') {
                        stack.pop();
                    } else {
                        return false;
                    }
                }
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        ValidParentheses validParentheses = new ValidParentheses();
        System.out.println(validParentheses.isValidParentheses("()[]{}"));
        System.out.println(validParentheses.isValidParentheses("()"));
        System.out.println(validParentheses.isValidParentheses("(]"));
        System.out.println(validParentheses.isValidParentheses("([])"));
        System.out.println(validParentheses.isValidParentheses("([)]"));
    }
}
