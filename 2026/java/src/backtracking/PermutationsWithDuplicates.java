package src.backtracking;

/*
Given a collection of numbers, nums, that might contain duplicates, return all possible unique permutations in any order.

Example 1:
Input: nums = [1,1,2]
Output:
[[1,1,2],
 [1,2,1],
 [2,1,1]]

Example 2:
Input: nums = [1,2,3]
Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]

Constraints:

1 <= nums.length <= 8
-10 <= nums[i] <= 10
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Time Complexity: O(N x N!) in the worst-case scenario where all elements are unique. However, when duplicates are
present, our pruning condition !elementIsUsedInPath[i - 1] cuts off entire subtrees, keeping the real-world execution
time much lower than the standard factorial curve. Sorting takes an initial O(N log N) time, which is completely
absorbed by the dominant permutation growth curve.

Space Complexity: O(N). The boolean tracking array operates completely in-place. The working memory space on the heap is
bounded strictly by the recursive JVM execution call stack depth and the max width of our path list, which both reach a
maximum scale depth of N.
 */

public class PermutationsWithDuplicates {
    private void backtrackAndPermuteUnique(int[] sortedNums, boolean[] elementIsUsedInPath,
                                           List<Integer> currentPermutationPath,
                                           List<List<Integer>> globalPermutationsRegistry) {

        // BASE CASE / TERMINATION CONDITION:
        // If our path length matches the size of the array, a unique permutation is complete!
        if (currentPermutationPath.size() == sortedNums.length) {
            globalPermutationsRegistry.add(new ArrayList<>(currentPermutationPath));
            return;
        }

        // Always loop over EVERY element from index 0 to build out choices
        for (int i = 0; i < sortedNums.length; i++) {

            // PATH GUARDRAIL 1: Skip if this specific index slot is already active in our path
            if (elementIsUsedInPath[i]) {
                continue;
            }

            // PATH GUARDRAIL 2: THE CRITICAL HORIZONTAL SIBLING PRUNING RULE
            // If this element matches the previous element, we can ONLY explore it if the previous element is currently
            // active in our vertical child path. If the previous element is FALSE, it means it was just popped out, so
            // skip it!
            if (i > 0 && sortedNums[i] == sortedNums[i - 1] && !elementIsUsedInPath[i - 1]) {
                continue;
            }

            // Path Action: Lock the index and append the element
            elementIsUsedInPath[i] = true;
            currentPermutationPath.add(sortedNums[i]);

            // Recurse forward: Deep dive vertically to fill the remaining slots
            backtrackAndPermuteUnique(sortedNums, elementIsUsedInPath, currentPermutationPath, globalPermutationsRegistry);

            // The Backtrack Step: Erase our footprint to restore state for horizontal siblings
            currentPermutationPath.remove(currentPermutationPath.size() - 1);
            elementIsUsedInPath[i] = false;
        }
    }

    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> globalPermutationsRegistry = new ArrayList<>();

        if (nums == null || nums.length == 0) {
            return globalPermutationsRegistry;
        }

        // STEP 1: Mandatory sort to group identical elements side-by-side
        Arrays.sort(nums);

        List<Integer> currentPermutationPath = new ArrayList<>();
        boolean[] elementIsUsedInPath = new boolean[nums.length];

        // STEP 2: Launch our pruned backtracking state machine
        backtrackAndPermuteUnique(nums, elementIsUsedInPath, currentPermutationPath, globalPermutationsRegistry);

        return globalPermutationsRegistry;
    }

    private void printResult(List<List<Integer>> resultList){
        System.out.print("[");

        for(List<Integer> innerList: resultList){
            System.out.print("[");
            for(int i = 0; i < innerList.size(); i++){
                if(i == innerList.size() - 1){
                    System.out.print(innerList.get(i));
                } else {
                    System.out.print(innerList.get(i) + ", ");
                }
            }
            System.out.print("], ");
        }

        System.out.print("]");
    }

    public static void main() {
        PermutationsWithDuplicates permutationsWithDuplicates = new PermutationsWithDuplicates();
        permutationsWithDuplicates.printResult(permutationsWithDuplicates.permuteUnique(new int[]{1,1,2}));
    }
}

