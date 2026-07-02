package src.backtracking;

/*
Given an integer array nums that may contain duplicates, return all possible subsets (the power set). The solution set
must not contain duplicate subsets. Return the solution in any order.

Example 1:
Input: nums = [1,2,2]
Output: [[],[1],[1,2],[1,2,2],[2],[2,2]]

Example 2:
Input: nums = [0]
Output: [[],[0]]

Constraints:

1 <= nums.length <= 10
-10 <= nums[i] <= 10
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Visualize the example: nums [1,2,3,2], after sorting [1,2,2,3]. Now imagine at every level how the tree forms
d = duplicate. We skip the duplicate. This is very important to understand the code.

                                            []

                            [1]          [2]        [2](d)      [3]

            [1,2]   [1,2](d)    [1,3]    [2,2] [2,3]

        [1,2,2] [1,2,3]                 [2,2,3]

    [1,2,2,3]

 */

/*
Time Complexity: O(N x 2 ^ N). In the worst-case scenario with an array containing all unique elements, the decision tree
 forks 2 ^ N times. For each generated path, executing the new ArrayList<>(currentSubsetPath) copy operation takes O(N)
 linear time. Sorting takes O(N log N), which is completely absorbed by the dominant exponential runtime.

Space Complexity: O(N). Excluding the final output storage memory, space usage is bounded entirely by the height of our
currentSubsetPath scratchpad and the maximum depth of the recursive JVM call stack, which both reach a maximum scale
depth of N.
 */

public class SubsetsWithDuplicates {
    private void backtrackAndGenerate(int[] sortedNums, int startingIndex,
                                      List<Integer> currentSubsetPath,
                                      List<List<Integer>> globalPowerSet) {

        // Every single stop along the decision tree paths is a mathematically valid subset combination. Snapshot a
        // deep copy immediately!
        globalPowerSet.add(new ArrayList<>(currentSubsetPath));

        // Loop over the remaining elements to branch forward down the tree
        for (int i = startingIndex; i < sortedNums.length; i++) {
            // THE CRITICAL PRUNING GUARDRAIL:
            // If 'i' is greater than startingIndex, it means we are evaluating horizontal sibling branches at the exact
            // same level of recursion. If this sibling matches the previous sibling, skip it to prevent duplicate path
            // skews!
            if (i > startingIndex && sortedNums[i] == sortedNums[i - 1]) {
                continue;
            }

            // Path Action: Include the active sibling element
            currentSubsetPath.add(sortedNums[i]);

            // Recurse forward: Increment the index to explore deeper vertical child branches
            backtrackAndGenerate(sortedNums, i + 1, currentSubsetPath, globalPowerSet);

            // Backtrack Action: Erase our footprint before moving to the next sibling option
            currentSubsetPath.remove(currentSubsetPath.size() - 1);
        }
    }

    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> globalPowerSet = new ArrayList<>();
        if (nums == null) {
            return globalPowerSet;
        }

        // STEP 1: Sort the array to ensure duplicate values sit side-by-side
        Arrays.sort(nums);

        List<Integer> currentSubsetPath = new ArrayList<>();

        // STEP 2: Launch our pruned backtracking state machine starting at index 0
        backtrackAndGenerate(nums, 0, currentSubsetPath, globalPowerSet);

        return globalPowerSet;
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
        SubsetsWithDuplicates subsetsWithDuplicates = new SubsetsWithDuplicates();
        subsetsWithDuplicates.printResult(subsetsWithDuplicates.subsetsWithDup(new int[]{1,2,2}));
        subsetsWithDuplicates.printResult(subsetsWithDuplicates.subsetsWithDup(new int[]{0}));
    }
}

