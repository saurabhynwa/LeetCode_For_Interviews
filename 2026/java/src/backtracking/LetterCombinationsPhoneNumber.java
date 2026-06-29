package src.backtracking;

/*
Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the number could
represent. Return the answer in any order. A mapping of digits to letters (just like on the telephone buttons) is given
below. Note that 1 does not map to any letters.

Example 1:
Input: digits = "23"
Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]

Example 2:
Input: digits = "2"
Output: ["a","b","c"]

Constraints:

1 <= digits.length <= 4
digits[i] is a digit in the range ['2', '9'].
 */

import java.util.ArrayList;
import java.util.List;

/*
Time Complexity: O(4 ^ N x N) where N is the total number of digits in the input string. In the worst-case scenario
(pressing buttons like 7 or 9 which have 4 letters each), the decision tree splits 4 times at every level, creating up
to 4 ^ N combinations. For each combination, building the final string payload out of the StringBuilder takes O(N)
linear time.

Space Complexity: O(N). We perform character mutations in-place inside our StringBuilder container. The working memory
space is bounded strictly by the recursive JVM call stack depth and the max length of the string builder buffer, which
both grow to a maximum height of N.
 */

public class LetterCombinationsPhoneNumber  {
    // Keypad layout mapping digits cleanly to their respective character blocks
    private static final String[] KEYPAD_MAPPING = {
            "",     // 0
            "",     // 1
            "abc",  // 2
            "def",  // 3
            "ghi",  // 4
            "jkl",  // 5
            "mno",  // 6
            "pqrs", // 7
            "tuv",  // 8
            "wxyz"  // 9
    };

    private void backtrackAndCombine(String digits, int currentDigitIndex,
                                     StringBuilder currentPathBuilder,
                                     List<String> globalCombinationsList) {

        // BASE CASE / TERMINATION CONDITIONS:
        // If our pointer reaches the end of the digits string, we have fully formed a valid combination path. Snapshot
        // it to our results list!
        if (currentDigitIndex == digits.length()) {
            globalCombinationsList.add(currentPathBuilder.toString());
            return;
        }

        // Fetch the active digit character and resolve its mapped letters from our registry
        char activeDigit = digits.charAt(currentDigitIndex);
        String mappedLetters = KEYPAD_MAPPING[activeDigit - '0'];

        // Fan out horizontally by looping over every available letter option for this digit
        for (int i = 0; i < mappedLetters.length(); i++) {
            char activeLetterOption = mappedLetters.charAt(i);

            // Step Action: Append the character to our active path builder
            currentPathBuilder.append(activeLetterOption);

            // Recurse forward: Move to the next digit index slot vertically down the tree
            backtrackAndCombine(digits, currentDigitIndex + 1, currentPathBuilder, globalCombinationsList);

            // The Backtrack Step: Delete our last footprint!
            // Truncate the trailing character to clean the buffer for the next loop sibling
            currentPathBuilder.deleteCharAt(currentPathBuilder.length() - 1);
        }
    }

    private List<String> letterCombinations(String digits) {
        List<String> globalCombinationsList = new ArrayList<>();

        // Edge Case Validation: Empty inputs return empty list layers immediately
        if (digits == null || digits.isEmpty()) {
            return globalCombinationsList;
        }

        // StringBuilder acts as an optimal in-place stack buffer to avoid heap string churn
        StringBuilder currentPathBuilder = new StringBuilder();

        // Launch our backtracking state machine starting at digit index 0
        backtrackAndCombine(digits, 0, currentPathBuilder, globalCombinationsList);

        return globalCombinationsList;
    }

    private void printResult(List<String> resultList){
        System.out.print("[");

        for(int i = 0; i < resultList.size(); i++){
            if(i == resultList.size() - 1){
                System.out.print(resultList.get(i));
            } else {
                System.out.print(resultList.get(i) + ", ");
            }
        }

        System.out.print("]");
    }

    public static void main(String[] args) {
        LetterCombinationsPhoneNumber letterCombinationsPhoneNumber = new LetterCombinationsPhoneNumber();
        letterCombinationsPhoneNumber.printResult(letterCombinationsPhoneNumber.letterCombinations("23"));
    }
}

