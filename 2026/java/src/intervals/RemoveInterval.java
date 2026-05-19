package src.intervals;

import java.util.ArrayList;
import java.util.List;

/*
A set of real numbers can be represented as the union of several disjoint intervals, where each interval is in the form
[a, b). A real number x is in the set if one of its intervals [a, b) contains x (i.e. a <= x < b).

You are given a sorted list of disjoint intervals, "intervals" representing a set of real numbers as described above,
where intervals[i] = [ai, bi] represents the interval [ai, bi). You are also given another interval toBeRemoved.

Return the set of real numbers with the interval toBeRemoved removed from intervals. In other words, return the set of
real numbers such that every x in the set is in intervals but not in toBeRemoved. Your answer should be a sorted list of
disjoint intervals as described above.
 */

/*
Time Complexity: (O(n)) because it completes a single linear scan over the intervals array.
Space Complexity: (O(1)) auxiliary space beyond the memory needed to instantiate the final return list.
 */

public class RemoveInterval {
    private List<List<Integer>> removeInterval(int[][] intervals, int[] tobeRemoved){
        List<List<Integer>> result = new ArrayList<>();

        // sanity check
        if(intervals == null || intervals.length < 1){
            return result;
        }

        // sanity check
        if(tobeRemoved == null || tobeRemoved.length < 1){
            // return the input as it is
            for(int[] interval: intervals){
                result.add(List.of(interval[0], interval[1]));
            }
        }

        int removeStart = tobeRemoved[0];
        int removeEnd = tobeRemoved[1];

        // Logic: left chunk/right chunk model. Look at every interval and ask two independent questions:
        // 1) Is there a valid piece left behind before the removal window?
        // 2) Is there a valid piece left behind after the removal window?
        for(int[] interval: intervals){
            int currentLeft = interval[0];
            int currentRight = interval[1];

            // 1. Check if there's a valid left chunk before the removal window
            if(currentLeft < removeStart){
                // Keep everything up to either the end of the interval or where removal begins
                result.add(List.of(currentLeft, Math.min(currentRight, removeStart)));
            }

            // 2. Check if there's a valid right chunk after the removal window
            if(currentRight > removeEnd){
                // Keep everything from either where removal ends or the start of the interval
                result.add(List.of(Math.max(removeEnd, currentLeft) ,currentRight));
            }

            // Note: If an interval falls completely INSIDE the removal window,
            // BOTH conditions evaluate to false, and it is automatically dropped!
        }

        return result;
    }

    private List<List<Integer>> removeIntervalIfElseLogic(int[][] intervals, int[] toBeRemoved) {
        // check inclusive range using: start <= current < end

        List<List<Integer>> result = new ArrayList<>();

        // sanity check
        if(intervals == null || intervals.length < 1){
            return result;
        }

        if(toBeRemoved == null || toBeRemoved.length < 1){
            for (int[] interval : intervals) {
                result.add(List.of(interval[0], interval[1]));
            }
            return result;
        }

        int removeStart = toBeRemoved[0];
        int removeEnd = toBeRemoved[1];

        // Logic: given that the intervals are sorted, we plot out the possibilities of currentStart w.r.t to
        // removeStart and removeEnd while accounting for currentRight and plot out the complex nested if-else logic

        for (int[] interval : intervals) {
            int currentLeft = interval[0];
            int currentRight = interval[1];

            if (currentLeft < removeStart) {
                if (currentRight <= removeStart) {
                    // keep current
                    result.add(List.of(currentLeft, currentRight));
                } else if (currentRight <= removeEnd) {
                    result.add(List.of(currentLeft, removeStart));
                } else {
                    // two intervals needs to be formed
                    result.add(List.of(currentLeft, removeStart));
                    result.add(List.of(removeEnd, currentRight));
                }
            } else if (currentLeft < removeEnd) {
                if (currentRight > removeEnd) {
                    result.add(List.of(removeEnd, currentRight));
                }
                // else: currentRight <= removeEnd, drop the current interval
            } else {
                result.add(List.of(currentLeft, currentRight));
            }
        }

        return result;
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

    public static void main(String[] args){
        RemoveInterval removeInterval = new RemoveInterval();
        removeInterval.printResult(removeInterval.removeIntervalIfElseLogic(new int[][]{{0,2},{3,4},{5,7}}, new int[]{1,6}));

        removeInterval.printResult(removeInterval.removeInterval(new int[][]{{0,2},{3,4},{5,7}}, new int[]{1,6}));
        removeInterval.printResult(removeInterval.removeInterval(new int[][]{{0,5}}, new int[]{2,3}));
        removeInterval.printResult(removeInterval.removeInterval(new int[][]{{-5,-4}, {-3,-2}, {1,2}, {3,5}, {8,9}}, new int[]{-1,4}));
    }
}
