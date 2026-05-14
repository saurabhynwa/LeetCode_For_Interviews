package src.intervals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals, and return an array
of the non-overlapping intervals that cover all the intervals in the input.

Example 1:

Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
Output: [[1,6],[8,10],[15,18]]
Explanation: Since intervals [1,3] and [2,6] overlap, merge them into [1,6].
Example 2:

Input: intervals = [[1,4],[4,5]]
Output: [[1,5]]
Explanation: Intervals [1,4] and [4,5] are considered overlapping.
Example 3:

Input: intervals = [[4,7],[1,4]]
Output: [[1,7]]
Explanation: Intervals [1,4] and [4,7] are considered overlapping.


Constraints:

1 <= intervals.length <= 104
intervals[i].length == 2
0 <= starti <= endi <= 104
*/

/*
Time Complexity: (O(n log n)) due to the sorting step. The subsequent inspection loop remains linear (O(n)).
Space Complexity: (O(n)) required for the tempResult structure.
 */

public class MergeIntervals {
    private int[][] merge(int[][] intervals){
        // sanity check
        if(intervals == null || intervals.length <= 1){
            return intervals; // no merging needed
        }

        List<int[]> tempResult = new ArrayList<>();

        // Logic: for merging intervals we need to sort basis start time.
        Arrays.sort(intervals, (a,b) -> Integer.compare(a[0], b[0]));

        // Use 2 pointers. Previous initialized on the 0th interval and current on first interval. Merge condition:
        // previousEnd >= currentStart. If merge happens, update the result to prevStart and prevEnd, do not write
        // anything in this check. If previousEnd < currentStart then first write previous to result set and then move
        // previous to current. So once current loops out, previous points to the last element of the array, or it would
        // hold the merge result. So simply write the previous to result set after the loop ends.

        // Initialize our tracking window with the first interval reference
        int[] previous = intervals[0];
        // we are not tracking prevStart, because sorting ensures that prevStart will always be <= currentStart.

        for(int current = 1; current < intervals.length; current++){
            int currentStart = intervals[current][0];
            int currentEnd = intervals[current][1];

            // merge condition check. If current starts before/at previous ends -> Overlap detected
            if(previous[1] >= currentStart){
                // Math.min for start. But because of sorting on 'start' it is guaranteed that start will be prevStart
                // itself. Only the end time needs expanding due to sorting
                previous[1] = Math.max(previous[1], currentEnd);
                // we hold previous (int []) in memory and use that for checking with the next current
            } else {
                // No overlap. Write the in memory interval tracked by previous to the result
                tempResult.add(previous);
                // now move the tracker 'previous' to current
                previous = intervals[current];
            }
        }

        // we write to the result set content held by tracker 'previous'. When the for loop ends, 'previous' will point
        // to the last element, which we need to add to the result set.
        tempResult.add(previous);

        return tempResult.toArray(new int[tempResult.size()][]);
    }

    private void printResult(int[][] result){
        System.out.print("[ ");
        for(int[] temp: result){
            System.out.print("[" + temp[0] + ", " + temp[1] + "] ");
        }
        System.out.print("]");
        System.out.println();
    }

    static void main(){
        MergeIntervals mergeIntervals = new MergeIntervals();
        mergeIntervals.printResult(mergeIntervals.merge(new int[][]{{2,3},{2,2},{3,3},{1,3},{5,7},{2,2},{4,6}}));
        mergeIntervals.printResult(mergeIntervals.merge(new int[][]{{1,3},{2,6},{8,10},{15,18}}));
        mergeIntervals.printResult(mergeIntervals.merge(new int[][]{{1,4},{4,5}}));
        mergeIntervals.printResult(mergeIntervals.merge(new int[][]{{4,7},{1,4}}));
    }
}
