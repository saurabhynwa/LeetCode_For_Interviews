package src.backtracking;

/*
Given an array of distinct integers candidates and a target integer target, return a list of all unique combinations of
candidates where the chosen numbers sum to target. You may return the combinations in any order. The same number may be
chosen from candidates an unlimited number of times. Two combinations are unique if the frequency of at least one of the
chosen numbers is different.

The test cases are generated such that the number of unique combinations that sum up to target is less than 150
combinations for the given input.

Example 1:
Input: candidates = [2,3,6,7], target = 7
Output: [[2,2,3],[7]]
Explanation:
2 and 3 are candidates, and 2 + 2 + 3 = 7. Note that 2 can be used multiple times.
7 is a candidate, and 7 = 7.
These are the only two combinations.

Example 2:
Input: candidates = [2,3,5], target = 8
Output: [[2,2,2,2],[2,3,3],[3,5]]

Example 3:
Input: candidates = [2], target = 1
Output: []

Constraints:

1 <= candidates.length <= 30
2 <= candidates[i] <= 40
All elements of candidates are distinct.
1 <= target <= 40
 */

import java.util.ArrayList;
import java.util.List;

/*
Time Complexity: O(S) where S is the total number of states explored across the execution tree. The tree depth can reach
a maximum of (Target / Minimum Value) layers if the code repeatedly picks the smallest available number. While
technically bounded exponentially by O(2 ^ M) where M is the number of recursive operations, the structural target
reductions and fail-fast constraints keep real-world execution highly linear and fast on standard inputs.

Space Complexity: O(T) where T is the maximum height of the recursive tree. The extra memory space on the heap is
bounded entirely by the depth of our currentCombinationPath list and the recursive JVM call stack depth, which both grow
strictly to a maximum capacity depth of (Target / Minimum Value)
 */

public class CombinationSum {
    private void backtrackAndSum(int[] candidates,
                                 int currentIndex,
                                 int remainingTarget,
                                 List<Integer> currentCombinationPath,
                                 List<List<Integer>> globalCombinationsRegistry) {

        // BASE CASE 1: SUCCESS TERMINATION
        // If the remaining target hits exactly zero, our current path is a perfect match!
        if (remainingTarget == 0) {
            globalCombinationsRegistry.add(new ArrayList<>(currentCombinationPath));
            return;
        }

        // BASE CASE 2: FAILURE BOUNDARY CHECKS
        // Fail fast if we overshoot the target (remainingTarget < 0) or if our pointer walks past the end of the array.
        if (remainingTarget < 0 || currentIndex == candidates.length) {
            return;
        }

        //  Branch A: STAY and TAKE. Stay on current index and recurse forward with reduced target. This simulates the
        //  repeated usage of the current index value.
        int activeCandidateValue = candidates[currentIndex];

        // Path Action: Append the active number to our scratchpad path
        currentCombinationPath.add(activeCandidateValue);

        // Recurse forward: Deduct the value from the target, but KEEP the currentIndex the same!
        backtrackAndSum(candidates, currentIndex, remainingTarget - activeCandidateValue,
                currentCombinationPath, globalCombinationsRegistry);

        // THE BACKTRACK STEP: Erase our footprint!
        // Pop the trailing item out to restore the state before exploring the "Leave" branch.
        currentCombinationPath.remove(currentCombinationPath.size() - 1);

        // Branch B: MOVE and LEAVE. Move to the next index value with the same target.
        // Recurse forward: Keep the target the same, but increment the index pointer by 1
        backtrackAndSum(candidates, currentIndex + 1, remainingTarget,
                currentCombinationPath, globalCombinationsRegistry);
    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> globalCombinationsRegistry = new ArrayList<>();

        // Edge Case Validation
        if (candidates == null || candidates.length == 0) {
            return globalCombinationsRegistry;
        }

        List<Integer> currentCombinationPath = new ArrayList<>();

        // Launch our infinite-reuse backtracking state machine starting at index 0
        backtrackAndSum(candidates, 0, target, currentCombinationPath, globalCombinationsRegistry);

        return globalCombinationsRegistry;
    }

    private void printResult(List<List<Integer>> resultList) {
        if (resultList == null) {
            System.out.println("[]");
            return;
        }

        System.out.print("[ ");

        // Loop over each individual inner subset list
        for (int i = 0; i < resultList.size(); i++) {
            List<Integer> innerList = resultList.get(i);

            System.out.print("[");

            // Format elements inside the active inner subset list
            for (int j = 0; j < innerList.size(); j++) {
                System.out.print(innerList.get(j));

                // Add a comma delimiter if we haven't reached the last element yet
                if (j < innerList.size() - 1) {
                    System.out.print(", ");
                }
            }

            System.out.print("]");

            // Add a separator space between different subset groups, but skip the last trailing one
            if (i < resultList.size() - 1) {
                System.out.print(", ");
            }
        }

        System.out.print(" ]");
        System.out.println(); // Flush output stream with a clean newline character
    }

    public static void main(String[] args) {
        CombinationSum combinationSum = new CombinationSum();
        combinationSum.printResult(combinationSum.combinationSum(new int[]{2,3,6,7}, 7));
    }
}

