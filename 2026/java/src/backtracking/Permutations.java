package src.backtracking;

/*
Given an array nums of distinct integers, return all the possible permutations. You can return the answer in any order.

Example 1:
Input: nums = [1,2,3]
Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]

Example 2:
Input: nums = [0,1]
Output: [[0,1],[1,0]]

Example 3:
Input: nums = [1]
Output: [[1]]

Constraints:

1 <= nums.length <= 6
-10 <= nums[i] <= 10
All the integers of nums are unique.
 */

import java.util.ArrayList;
import java.util.List;

/*
Time Complexity: O(N x N!) where N is the length of the input array. For the first slot, you have N choices, for the
second slot you have N-1 choices, and so on, creating a total of N! full permutation paths. For each of the (N!) leaf
solutions, copying the path values into the final deep-copy list takes O(N) linear time, resulting in O(N x N!).

Space Complexity: O(N). The boolean flag tracking map handles updates in-place. The footprint is bounded entirely by
the height of our currentPermutationPath list and the recursive JVM call stack depth, which both grow strictly to a
maximum capacity depth of N before returning.
 */

public class Permutations {
    private void backtrackAndPermute(int[] nums,
                                     boolean[] elementIsUsedInPath,
                                     List<Integer> currentPermutationPath,
                                     List<List<Integer>> globalPermutationsRegistry) {
        // BASE CASE / TERMINATION INVARIANT:
        // If our active path length matches the size of the original array, we have successfully created a complete
        // ordering permutation. Deep copy it!
        if (currentPermutationPath.size() == nums.length) {
            globalPermutationsRegistry.add(new ArrayList<>(currentPermutationPath));
            return;
        }

        // Loop over EVERY element in the array to evaluate placement options
        for (int i = 0; i < nums.length; i++) {
            // THE GUARDRAIL CHECK: If this element is already sitting in our
            // active permutation path tracking window, skip it immediately!
            if (elementIsUsedInPath[i]) {
                continue;
            }

            // Path Action: Mark the item as used and append it to our path list
            elementIsUsedInPath[i] = true;
            currentPermutationPath.add(nums[i]);

            // Recurse forward: Deep dive vertically to pick the next unused element
            backtrackAndPermute(nums, elementIsUsedInPath, currentPermutationPath, globalPermutationsRegistry);

            // THE BACKTRACK STEP: Erase our footprint!
            // Revert our boolean flag and pop the trailing item out to restore state for siblings
            currentPermutationPath.remove(currentPermutationPath.size() - 1);
            elementIsUsedInPath[i] = false;
        }
    }

    private List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> globalPermutationsRegistry = new ArrayList<>();

        // Edge Case Validation
        if (nums == null || nums.length == 0) {
            return globalPermutationsRegistry;
        }

        List<Integer> currentPermutationPath = new ArrayList<>();

        // boolean array acts as an O(1) tracking map registry to check element availability
        boolean[] elementIsUsedInPath = new boolean[nums.length];

        // Launch our backtracking permutation machine
        backtrackAndPermute(nums, elementIsUsedInPath, currentPermutationPath, globalPermutationsRegistry);

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

    public static void main(String[] args) {
        Permutations permutations = new Permutations();
        permutations.printResult(permutations.permute(new int[]{1,2,3}));
    }
}

