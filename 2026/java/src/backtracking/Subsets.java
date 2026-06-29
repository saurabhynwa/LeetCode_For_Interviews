package src.backtracking;

import java.util.ArrayList;
import java.util.List;

/*
Time Complexity = O(N x 2 ^ N). For an array of size N, there are exactly 2 ^ N possible subset combinations
(leaf nodes). For each leaf node, executing the deep copy operation new ArrayList<>(currentSubsetPath) takes O(N) linear
 time to copy the bytes.

Space Complexity: O(N). Excluding the memory container used to hold the final results, the working space is bounded by
the maximum size of our currentSubsetPath scratchpad 'N' and the maximum height of the recursive JVM execution stack.
 */

public class Subsets {
    private List<List<Integer>> subsets(int[] nums){
        List<List<Integer>> globalPowerSet = new ArrayList<>();

        if(nums == null || nums.length == 0){
            return globalPowerSet;
        }

        // maintain a list which will store the state for our backtracking
        List<Integer> currentSubsetPath = new ArrayList<>();

        // Logic: For every element there are 2 paths: Include self or Exclude Self, and move to the next using backtracking

        generatePowerSet(nums, 0, currentSubsetPath, globalPowerSet);

        return globalPowerSet;
    }

    private void generatePowerSet(int[] nums, int currentIndex, List<Integer> currentSubsetPath, List<List<Integer>> globalPowerSet) {
        // BASE CASE / TERMINATION GUARDRAIL:
        // If our pointer reaches the end of the array, we have fully evaluated a complete path from the top of the
        // decision tree to a leaf node.
        if (currentIndex == nums.length) {
            // CRITICAL STEP: Create a DEEP COPY of the active subset path.
            // Passing just 'currentSubsetPath' adds a reference pointer, which changes during future backtracking steps
            // and ruins our historical records.
            globalPowerSet.add(new ArrayList<>(currentSubsetPath));
            return;
        }

        // branch A: take the current element
        int activeElement = nums[currentIndex];
        currentSubsetPath.add(activeElement);

        // backtrack with this path
        generatePowerSet(nums, currentIndex + 1, currentSubsetPath, globalPowerSet);

        // now before going to 2nd path, we are returning from recursion, so we need to clear the state for backtrack
        // remove last element from the currentSubset path
        currentSubsetPath.remove(currentSubsetPath.size() - 1);

        // branch B: exclude the current element and backtrack
        generatePowerSet(nums, currentIndex + 1, currentSubsetPath, globalPowerSet);
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
        Subsets powerSet = new Subsets();
        powerSet.printResult(powerSet.subsets(new int[]{1,2,3}));
    }
}
