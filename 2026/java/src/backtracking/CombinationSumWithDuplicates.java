package src.backtracking;

/*
Given a collection of candidate numbers (candidates) and a target number (target), find all unique combinations in
candidates where the candidate numbers sum to target. Each number in candidates may only be used once in the combination.

Note: The solution set must not contain duplicate combinations.

Example 1:
Input: candidates = [10,1,2,7,6,1,5], target = 8
Output:
[
[1,1,6],
[1,2,5],
[1,7],
[2,6]
]

Example 2:
Input: candidates = [2,5,2,1,2], target = 5
Output:
[
[1,2,2],
[5]
]

Constraints:

1 <= candidates.length <= 100
1 <= candidates[i] <= 50
1 <= target <= 30
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Time Complexity: O(2 ^ N) in the worst-case scenario where all elements are unique. The decision tree forks at most
(2 ^ N) times. For each valid path, executing the new ArrayList<>(currentCombinationPath) copy operation takes O(N)
linear time. However, sorting takes an initial (O(N log N) time, and our two pruning constraints
(sortedCandidates[i] > remainingTarget and the sibling check) cut off massive subtrees, keeping the real-world execution
incredibly fast compared to an unmanaged brute-force search.

Space Complexity: O(N). The extra memory space on the heap is bounded strictly by the height of our
currentCombinationPath list and the maximum height of the recursive JVM execution call stack, which both reach a maximum
capacity depth of N.
 */
public class CombinationSumWithDuplicates {
    private void backtrackAndSumUnique(int[] sortedCandidates,
                                       int startingIndex,
                                       int remainingTarget,
                                       List<Integer> currentCombinationPath,
                                       List<List<Integer>> globalCombinationsRegistry) {

        // BASE CASE 1: SUCCESS TERMINATION
        // If the remaining target hits exactly zero, our current path is a perfect unique match!
        if (remainingTarget == 0) {
            globalCombinationsRegistry.add(new ArrayList<>(currentCombinationPath));
            return;
        }

        // Horizontal branch expansion loop starting from the current available index
        for (int i = startingIndex; i < sortedCandidates.length; i++) {
            // BASE CASE 2 / PRUNING: If the active candidate value is strictly greater than our remaining target, all
            // subsequent sorted candidates will also overshoot. We can break early, saving millions of operations!
            if (sortedCandidates[i] > remainingTarget) {
                break;
            }

            // PATH GUARDRAIL: THE CRITICAL HORIZONTAL SIBLING PRUNING RULE
            // If 'i' is greater than startingIndex, it means we are evaluating horizontal sibling branches at the exact
            // same level of recursion. If this sibling matches the previous sibling, skip it to prevent duplicate path
            // skews!
            if (i > startingIndex && sortedCandidates[i] == sortedCandidates[i - 1]) {
                continue;
            }

            // Path Action: Append the active number to our scratchpad path
            currentCombinationPath.add(sortedCandidates[i]);

            // Recurse forward: Deduct value from target, and increment index to 'i + 1', so this specific array cell
            // cannot be reused in the same path.
            backtrackAndSumUnique(
                    sortedCandidates,
                    i + 1,
                    remainingTarget - sortedCandidates[i],
                    currentCombinationPath,
                    globalCombinationsRegistry);

            // The Backtrack Step: Erase our footprint to restore state for horizontal siblings
            currentCombinationPath.remove(currentCombinationPath.size() - 1);
        }
    }

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> globalCombinationsRegistry = new ArrayList<>();

        // Edge Case Validation
        if (candidates == null || candidates.length == 0) {
            return globalCombinationsRegistry;
        }

        // STEP 1: Mandatory sort to group identical elements side-by-side
        Arrays.sort(candidates);

        List<Integer> currentCombinationPath = new ArrayList<>();

        // STEP 2: Launch our pruned unique backtracking state machine starting at index 0
        backtrackAndSumUnique(
                candidates,
                0,
                target,
                currentCombinationPath,
                globalCombinationsRegistry
        );

        return globalCombinationsRegistry;
    }

    private void printResult(List<List<Integer>> resultList){
        if(resultList.isEmpty()){
            System.out.println("[]");
            return;
        }

        System.out.print("[");

        for(int i = 0; i < resultList.size(); i++){
            List<Integer> innerList = resultList.get(i);

            for(int j = 0; j < innerList.size(); j++){
                if(j == 0){
                    System.out.print("[" + innerList.get(j) + ", ");
                } else if(j == innerList.size() - 1){
                    System.out.print(innerList.get(j) + "]");
                } else {
                    System.out.print(innerList.get(j) + ", ");
                }
            }

            if(i < resultList.size() - 1){
                System.out.print(", ");
            }
        }
        System.out.print("]");
        System.out.println();
    }

    public static void main() {
        CombinationSumWithDuplicates combinationSumWithDuplicates = new CombinationSumWithDuplicates();
        combinationSumWithDuplicates.printResult(combinationSumWithDuplicates.combinationSum2(new int[]{10,1,2,7,6,1,5}, 8));
    }
}

