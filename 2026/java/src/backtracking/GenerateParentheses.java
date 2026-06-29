package src.backtracking;

/*
Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.

Example 1:
Input: n = 3
Output: ["((()))","(()())","(())()","()(())","()()()"]

Example 2:
Input: n = 1
Output: ["()"]

Constraints:

1 <= n <= 8
 */

import java.util.ArrayList;
import java.util.List;

/*
Time Complexity: O((4 ^ N) / N x sqrtN). The total number of valid combinations generated matches the Nth Catalan
Number, which scales tightly bound by this expression. For each valid sequence branch, building the final string out of
the StringBuilder buffer takes O(N) linear time. This predictive approach is vastly superior to a brute-force approach,
which forces an expensive O(2 ^ 2N) tree search.Space Complexity: O(N). We perform all character modifications in-place
inside our single string builder instance. The extra memory space on the heap is bounded strictly by the height of our
recursive JVM call stack depth, which reaches a maximum capacity depth of 2N.

Space Complexity: O(N). We perform all character modifications in-place inside our single string builder instance. The
extra memory space on the heap is bounded strictly by the height of our recursive JVM call stack depth, which reaches a
maximum capacity depth of 2N.

Catalan Number = it is just a special counting sequence in math used to find how many ways you can arrange shapes or
brackets without breaking structural rules.

Example: If you have 3 pairs of brackets, you have a total of 6 characters: three ( and three ).If you scrambled those 6
characters completely randomly, you could make 20 different strings. Some would be completely broken garbage like )))(((.
The Catalan Number for 3 is exactly 5. This tells you that out of those 20 scrambles, exactly 5 strings will be
perfectly balanced and valid.
 */

public class GenerateParentheses {
    private void backtrackAndBuild(int maxPairsCount,
                                   int activeOpenCount,
                                   int activeCloseCount,
                                   StringBuilder currentPathBuilder,
                                   List<String> globalResultsRegistry) {

        // BASE CASE / TERMINATION INVARIANT:
        // If our path length matches the exact length of all required characters (2 * N), our string is guaranteed to
        // be fully formed and valid. Snapshot it!
        if (currentPathBuilder.length() == maxPairsCount * 2) {
            globalResultsRegistry.add(currentPathBuilder.toString());
            return;
        }

        // PATH 1: Add an opening parenthesis if we haven't exhausted our allocation quota
        if (activeOpenCount < maxPairsCount) {
            // Action
            currentPathBuilder.append('(');

            // Recurse forward
            backtrackAndBuild(maxPairsCount, activeOpenCount + 1, activeCloseCount, currentPathBuilder, globalResultsRegistry);

            // Backtrack cleanup
            currentPathBuilder.deleteCharAt(currentPathBuilder.length() - 1);
        }

        // PATH 2: THE BALANCING GUARDRAIL
        // Only append a closing parenthesis if we have an active unmatched opening brace waiting
        if (activeCloseCount < activeOpenCount) {
            // Action
            currentPathBuilder.append(')');

            // Recurse forward
            backtrackAndBuild(maxPairsCount, activeOpenCount, activeCloseCount + 1, currentPathBuilder, globalResultsRegistry);

            // Backtrack cleanup
            currentPathBuilder.deleteCharAt(currentPathBuilder.length() - 1);
        }
    }

    public List<String> generateParenthesis(int n) {
        List<String> globalResultsRegistry = new ArrayList<>();

        // Edge Case Validation
        if (n <= 0) {
            return globalResultsRegistry;
        }

        StringBuilder currentPathBuilder = new StringBuilder();

        // Launch our predictive backtracking machine starting with 0 open and 0 close tags
        backtrackAndBuild(n, 0, 0, currentPathBuilder, globalResultsRegistry);

        return globalResultsRegistry;
    }

    private void printResult(List<String> result){
        System.out.print("[ ");

        for(int i = 0; i < result.size(); i++){
            if(i == result.size() - 1){
                System.out.print(result.get(i));
            } else {
                System.out.print(result.get(i) + ", ");
            }
        }

        System.out.print(" ]");
    }

    public static void main(String[] args) {
        GenerateParentheses generateParentheses = new GenerateParentheses();
        generateParentheses.printResult(generateParentheses.generateParenthesis(3));
    }
}

