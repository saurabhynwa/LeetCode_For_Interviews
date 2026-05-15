package src.intervals;

/*
You are given an inclusive range [lower, upper] and a sorted unique integer array nums, where all elements are within
the inclusive range. A number x is considered missing if x is in the range [lower, upper] and x is not in nums. Return
the shortest sorted list of ranges that exactly covers all the missing numbers. That is, no element of nums is included
in any of the ranges, and each missing number is covered by one of the ranges.

Example 1:

Input: nums = [0,1,3,50,75], lower = 0, upper = 99
Output: [[2,2],[4,49],[51,74],[76,99]]
Explanation: The ranges are:
[2,2]
[4,49]
[51,74]
[76,99]
Example 2:

Input: nums = [-1], lower = -1, upper = -1
Output: []
Explanation: There are no missing ranges since there are no missing numbers.


Constraints:

-109 <= lower <= upper <= 109
0 <= nums.length <= 100
lower <= nums[i] <= upper
All the values of nums are unique.
 */

import java.util.ArrayList;
import java.util.List;

/*
Time Complexity: (O(n)) because it makes a single clean linear pass through the array.

Space Complexity: (O(1)) auxiliary space beyond the memory required to build the output result payload list.
 */

public class MissingRanges {
    private List<List<Integer>> getMissingRanges(int[] nums, int lower, int upper){
        // We have to find missing ranges between lower and upper. Some numbers are available in nums int array

        // Logic: Try to visualize this problem as a runner who needs to paint the roadblocks. Runner starts at lower.
        // Then he checks whether the next road block is further down the road. If yes, starting from its position till
        // the roadblock runner paints the road. This becomes a range. Roadblock given in the nums array doesn't need to
        // be painted. The runner jumps over the roadblock and immediately from the next position repeats the same
        // exercise.

        // Once all the roadblocks are done in the nums array the runner has either reached its destination or the
        // destination is still further away from it. In that case the runner paints from its current position till
        // destination inclusive.

        List<List<Integer>> missingRanges = new ArrayList<>();

        int runner = lower; // runner's starting point

        for (int currentRoadBlock : nums) {
            // check if the current roadblock is ahead of the runner
            if (currentRoadBlock > runner) {
                // runner paints from its location till current roadblock (exclusive)
                missingRanges.add(List.of(runner, currentRoadBlock - 1));
            }

            // runner has 2 situations: 1) when it's done painting till the current roadblock it needs to start from the
            // immediate next or if the current roadblock wsa behind runner it still starts from the immediate right of
            // the next road block.
            runner = currentRoadBlock + 1;
        }

        // check whether the runner is at the destination or is the destination still far away
        if(runner <= upper){
            missingRanges.add(List.of(runner, upper));
        }

        return missingRanges;
    }

    private void printResult(List<List<Integer>> result){
        System.out.print("[");
        for(int i = 0; i < result.size(); i++){
            int start = result.get(i).get(0);
            int end = result.get(i).get(1);
            if(i == result.size() - 1){
                System.out.print(" [" + start + ", " + end + "]");
            } else {
                System.out.print("[" + start + ", " + end + "], ");
            }
        }
        System.out.print("]");
        System.out.println();
    }

    static void main(){
        MissingRanges missingRanges = new MissingRanges();
        missingRanges.printResult(missingRanges.getMissingRanges(new int[]{0,1,3,50,75}, 0, 99));
        missingRanges.printResult(missingRanges.getMissingRanges(new int[]{-1}, -1, -1));
    }
}
