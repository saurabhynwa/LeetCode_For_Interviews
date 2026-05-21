package src.stack;

import java.util.Stack;

/*
Given an encoded string, return its decoded string. The encoding rule is: k[encoded_string], where the encoded_string
inside the square brackets is being repeated exactly k times. Note that k is guaranteed to be a positive integer.

You may assume that the input string is always valid; there are no extra white spaces, square brackets are well-formed,
etc. Furthermore, you may assume that the original data does not contain any digits and that digits are only for those
repeat numbers, k. For example, there will not be input like 3a or 2[4].

The test cases are generated so that the length of the output will never exceed 105.

Example 1:

Input: s = "3[a]2[bc]"
Output: "aaabcbc"
Example 2:

Input: s = "3[a2[c]]"
Output: "accaccacc"
Example 3:

Input: s = "2[abc]3[cd]ef"
Output: "abcabccdcdcdef"

Constraints:

1 <= s.length <= 30
s consists of lowercase English letters, digits, and square brackets '[]'.
s is guaranteed to be a valid input.
All the integers in s are in the range [1, 300].
 */

/*
Time Complexity: O(Length of Output String)

1. Linear Scan: We iterate through the input string (S) of length (N) exactly once.
2. String Building: Every character that ends up in the final output string is appended to a StringBuilder or
concatenated. If the final decoded string has a length of (K) where (K >= N), the total time spent duplicating and
creating strings is proportional to (K).
3. Total Time: O(K), where (K) is the length of the final decoded output.

Space Complexity: O(Max Nesting Depth + K)
1. The Stacks: The maximum size of numStack and stringStack is determined by the maximum number of nested brackets.
For example, a[b[c[d]]] has a nesting depth of 3, so the stack holds at most 3 items. In the worst case, this is (O(N)).
2. The Heap Memory: The currentStr variable and the internal buffers grow to hold the final output string.
3. Total Space: O(Max Nesting Depth + K), where (K) is the length of the final decoded string.
*/

/*
Trick question: Learn the logic below:

1) currentStr: tracks your current string iteration progress

2) '[' : indicates that nesting is about to start. So we store the currentStr in a stack and the number that will decode or
 unnest the nesting is stored in other stack. Now since we are diving into a new string in a nested layer, we reset the
 trackers used to keep track of current string and the nesting/decode/mulitplier number

3) ']': indicates that the currentStr has encountered a closure. The question says that before a nesting start ('[')
there is always a number. Now the closure indicates a nesting, otherwise there is no sense of [ and ]. And we are
storing the nesting in num stack. So we take that number and multiply it with our currentStr (string result from [...]).

Now if you think, nesting result is the aftermath. There was some progress done before nesting started which we had
stored in the string stack. So you take that progress and append the nesting result and that becomes your current
progress. Hence, the formula currentStr = strBeforeNesting + (currentStr * nestingRepeatitions)

4) If it's just a character, we keep appending it to our currentStr to track progress
 */
public class DecodeString {
    private String getDecodedString(String s){
        // Logic: We have 4 type of characters. One is digits, One is opening square brace, one is closing square brace
        // and the last is lower cased English alphabets.

        // 4 scenarios to be handled with 2 stacks, one for string and one for numbers. Also, maintain 2 variables:
        // currentNum and currentStr

        // when it is digit, we need to update the currentNum. But be careful to construct the number from left to right
        // consider the example of 100. We see character by character. So first 1, then 0 and then 0

        // when it is an opening brace, push currentNum and currentStr to their stacks. Then reset both of the variables

        // when it is a closing brace, pop from both stacks. Update currentStr with this formula:
        // currentStr = strFromStack + currentStr * currentNum

        // when it is a character, append it to currentStr. Return currentStr
        Stack<Integer> numStack = new Stack<>();
        Stack<String> stringStack = new Stack<>();
        int currentNum = 0;
        // OPTIMIZATION: Use StringBuilder for the active workspace
        StringBuilder currentStr = new StringBuilder();

        for(int i = 0; i < s.length(); i++){
            char currentChar = s.charAt(i);

            if(Character.isDigit(currentChar)){
                // form the digit from left to right and store it in currentNum
                currentNum = currentNum * 10 + Character.getNumericValue(currentChar);
            } else if(currentChar == '['){
                // push currentNum and currentStr to their stacks
                numStack.push(currentNum);
                stringStack.push(currentStr.toString());
                // reset both current num and str
                currentNum = 0;
                currentStr.setLength(0); // Efficient way to clear a StringBuilder
            } else if(currentChar == ']') {
                // pop from both stacks
                int numFromStack = numStack.pop();
                String stringFromStack = stringStack.pop();
                // update currentStr using the formula
                String repeatedInner = getRepeatedString(currentStr.toString(), numFromStack);

                // Reconstruct workspace: clear it, add the prefix, then add the repeated inner text
                currentStr.setLength(0);
                currentStr.append(stringFromStack).append(repeatedInner);
            } else {
                // OPTIMIZATION: O(1) character append operation
                currentStr.append(currentChar);
            }
        }

        return currentStr.toString();
    }

    private String getRepeatedString(String str, int repeatition){
        StringBuilder sb = new StringBuilder();

        while(repeatition > 0){
            sb.append(str);
            repeatition--;
        }

        return sb.toString();
    }

    /*
    Why String Concatenation (+=) is a Hidden TrapIn Java ? String objects are immutable (they can never be changed in
    memory) [1]. Every single time your loop executes currentStr += currentChar, Java secretly performs the following
    heavy operations behind the scenes:
    1) It allocates a brand-new chunk of memory on the heap.
    2) It copies all the old characters from currentStr into that new chunk.
    3) It tacks on the currentChar.
    4) It throws away the old currentStr object, leaving it for the Garbage Collector to clean up [1].

    If a string grows to 10,000 characters, your code will perform thousands of copy operations and create thousands of
    temporary trash objects, which can drastically slow down your program or cause a Time Limit Exceeded (TLE) error on
    large inputs.

    The Fix: Use StringBuilderA StringBuilder uses a mutable, expandable char array internally. When you append
    characters, it just updates the array in place without creating new objects or copying the entire string every
    single time.

    Quick Performance Summary
    1. Original Code (+=): Takes (O(K^2)) time in the worst case (where (K) is the final string size) because of
    continuous data copying.
    2. Optimized Code (StringBuilder): Takes a clean (O(K)) time because appending characters happens in constant (O(1))
     time on average.

    (Note: StringBuilder is preferred over StringBuffer in single-threaded interview solutions because StringBuffer is
    synchronized, which adds unnecessary performance overhead for local variables).
     */

    public static void main(String[] args){
        DecodeString decodeString = new DecodeString();
        System.out.println(decodeString.getDecodedString("3[a]2[bc]"));
        System.out.println(decodeString.getDecodedString("3[a2[c]]"));
        System.out.println(decodeString.getDecodedString("2[abc]3[cd]ef"));
    }
}
