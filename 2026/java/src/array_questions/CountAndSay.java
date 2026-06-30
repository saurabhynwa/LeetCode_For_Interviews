package src.array_questions;

/*
The count-and-say sequence is a sequence of digit strings defined by the recursive formula: countAndSay(1) = "1"
countAndSay(n) is the run-length encoding of countAndSay(n - 1). Run-length encoding (RLE) is a string compression
method that works by replacing each maximal group of consecutive identical characters with the concatenation of the
length of the group followed by the character itself. For example, to compress the string "3322251" we replace "33" with
"23", replace "222" with "32", replace "5" with "15", and replace "1" with "11". Thus, the compressed string becomes
"23321511".

Given a positive integer n, return the nth element of the count-and-say sequence.

Example 1:
Input: n = 4
Output: "1211"

Explanation:

countAndSay(1) = "1"
countAndSay(2) = RLE of "1" = "11"
countAndSay(3) = RLE of "11" = "21"
countAndSay(4) = RLE of "21" = "1211"

Example 2:
Input: n = 1
Output: "1"

Explanation:

This is the base case.

Constraints:

1 <= n <= 30
 */

/*
Time Complexity: O(M) where M is the total number of characters processed across all generated terms. Each string
generation executes a strict single, linear pass over the previous string length.

Space Complexity: O(1) Auxiliary Extra Space. Excluding the storage space occupied by the string payload output buffers,
the internal processing execution footprint utilizes only primitive iteration tracking counters, leaving zero recursive
call stack pressure on the engine.
 */

public class CountAndSay {
    public String countAndSay(int n) {
        if (n <= 1) {
            return "1";
        }

        // Initialize the base case string payload
        String currentTerm = "1";

        // Iteratively compute every sequence term from 2 up to n
        for (int termIndex = 2; termIndex <= n; termIndex++) {
            StringBuilder nextTermBuilder = new StringBuilder();
            int consecutiveCharacterCount = 1;
            int termLength = currentTerm.length();

            // Run-Length Encoding Pass over the active string state
            for (int i = 0; i < termLength; i++) {

                // Safe lookahead: If the next character is identical, extend the group count
                if (i + 1 < termLength && currentTerm.charAt(i) == currentTerm.charAt(i + 1)) {
                    consecutiveCharacterCount++;
                } else {
                    // Group complete or array edge hit: Serialize [Count][Digit]
                    nextTermBuilder.append(consecutiveCharacterCount);
                    nextTermBuilder.append(currentTerm.charAt(i));

                    // Reset character tracking bucket back to 1 for the next sequence block
                    consecutiveCharacterCount = 1;
                }
            }

            // Update our tracking state variable with the freshly serialized text payload
            currentTerm = nextTermBuilder.toString();
        }

        return currentTerm;
    }

    public static void main(String[] args) {
        CountAndSay countAndSay = new CountAndSay();
        System.out.println(countAndSay.countAndSay(4));
    }
}

