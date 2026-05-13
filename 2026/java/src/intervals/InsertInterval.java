package src.intervals;

/*
You are given an array of non-overlapping intervals intervals where intervals[i] = [starti, endi] represent the start
and the end of the ith interval and intervals is sorted in ascending order by starti. You are also given an interval
newInterval = [start, end] that represents the start and end of another interval.

Insert newInterval into intervals such that intervals is still sorted in ascending order by starti and intervals still
does not have any overlapping intervals (merge overlapping intervals if necessary).

Return intervals after the insertion.

Note that you don't need to modify intervals in-place. You can make a new array and return it.

Example 1:

Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
Output: [[1,5],[6,9]]
Example 2:

Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
Output: [[1,2],[3,10],[12,16]]
Explanation: Because the new interval [4,8] overlaps with [3,5],[6,7],[8,10].


Constraints:

0 <= intervals.length <= 104
intervals[i].length == 2
0 <= starti <= endi <= 105
intervals is sorted by starti in ascending order.
newInterval.length == 2
0 <= start <= end <= 105
 */

import java.util.List;
import java.util.ArrayList;

/*
Time Complexity: (O(n)). Your loop sweeps the dataset exactly once. The inner pointer while loop triggers a break,
maintaining a strict linear processing pattern.

Space Complexity: (O(n)) required to assemble and construct the array payload representing the updated timetable output.
*/

public class InsertInterval {
    private int[][] insert(int[][] intervals, int[] newInterval){
        // sanity check
        if(intervals == null || intervals.length == 0){
            return new int[][]{newInterval};
        }

        // Logic: Given input is sorted and intervals are non-overlapping. We are told that newInterval insertion can
        // lead to interval merge. Imagine pointer is on a current interval of the input, and we have the newInterval.
        // There are 3 scenarios that are possible

        // scenario_1: the newInterval is shorter than the current interval. NewInterval_end < currentInterval_start
        // In this case, we just place the newInterval before the currentInterval and the rest of the output looks same

        // scenario_2: the newInterval is greater than the current interval. CurrentInterval_end < newInterval_start.
        // In this case, we copy the current interval into result list, keep the newInterval unchanged, move current to
        // the next element of the input and do the comparison. Now there are 2 possibilities, scenario_1 occurs or we
        // run out of input elements and we place the newInterval at the last. We will maintain a boolean flag to track
        // running out of input and placing the newInterval at the end

        // scenario_3: intervals are overlapping. We take min of starts and max of ends, update the newInterval and move
        // current.

        List<int[]> tempResult = new ArrayList<>();
        boolean isNewIntervalMerged = false;

        for(int current = 0; current < intervals.length; current++){
            int currentStart = intervals[current][0];
            int currentEnd = intervals[current][1];
            int newIntervalStart = newInterval[0];
            int newIntervalEnd = newInterval[1];

            // Case 1: newInterval is completely before the current interval
            if(newIntervalEnd < currentStart){
                tempResult.add(newInterval);
                isNewIntervalMerged = true;

                // Fast-copy all remaining intervals natively
                while(current < intervals.length){
                    tempResult.add(intervals[current]);
                    current++;
                }
                break;
            }
            // Case 2: current interval is completely before newInterval
            else if(currentEnd < newIntervalStart){
                tempResult.add(intervals[current]);
            }
            // Case 3: Intervals overlap, calculate the merged bounds
            else {
                newInterval[0] = Math.min(newIntervalStart, currentStart);
                newInterval[1] = Math.max(newIntervalEnd, currentEnd);
            }
        }

        // If we reached the end without triggering Case 1, append the merged interval
        if(!isNewIntervalMerged){
            tempResult.add(newInterval);
        }

        // Clean 2D array conversion shortcut
        return tempResult.toArray(new int[tempResult.size()][]);
        // The List interface in Java provides a native .toArray() method to dump all elements inside a list into an array.
        // tempResult.size(): Sets the exact number of rows in your new 2D array to match the number of items stored in
        // your list.
        // The empty trailing brackets []: Tells Java: "I am giving you the row dimension, but you will fill in the
        // exact size of the inner arrays (the columns) automatically when you copy the data."

        // What the shortcut line does behind the scenes:
        // int[][] result = new int[tempResult.size()][];
        // for (int i = 0; i < tempResult.size(); i++) {
        //  result[i] = tempResult.get(i);
        //  }
        //  return result;
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
        InsertInterval insertInterval = new InsertInterval();
        insertInterval.printResult(insertInterval.insert(new int[][]{{1,3}, {6,9}}, new int[]{2,5}));
        insertInterval.printResult(insertInterval.insert(new int[][]{{1,2}, {3,5}, {6,7}, {8,10}, {12,16}},
                new int[]{4,8}));
    }
}
