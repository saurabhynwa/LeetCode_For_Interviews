package src.intervals;

import java.util.Arrays;

/*
Given an array of intervals intervals where intervals[i] = [starti, endi], return the minimum number of intervals you
need to remove to make the rest of the intervals non-overlapping. Note that intervals which only touch at a point are
non-overlapping. For example, [1, 2] and [2, 3] are non-overlapping.

Example 1:

Input: intervals = [[1,2],[2,3],[3,4],[1,3]]
Output: 1
Explanation: [1,3] can be removed and the rest of the intervals are non-overlapping.
Example 2:

Input: intervals = [[1,2],[1,2],[1,2]]
Output: 2
Explanation: You need to remove two [1,2] to make the rest of the intervals non-overlapping.
Example 3:

Input: intervals = [[1,2],[2,3]]
Output: 0
Explanation: You don't need to remove any of the intervals since they're already non-overlapping.

Constraints:

1 <= intervals.length <= 105
intervals[i].length == 2
-5 * 104 <= starti < endi <= 5 * 104
 */

/*
Time Complexity: (O(n log n)): The execution simplifies to a single, linear for loop pass after sorting.
Space Complexity: (O(1))
 */
public class NonOverLappingIntervals {
    private int eraseOverlapIntervals(int[][] intervals){
        // sanity check
        if(intervals == null || intervals.length <= 1){
            return 0; // no intervals need to be removed
        }

        int minimumIntervalsToBeRemoved = 0;

        // Logic: For non-overlapping intervals we need to sort them by end time first (Earliest Deadline First).
        Arrays.sort(intervals, (a,b) -> Integer.compare(a[1], b[1]));

        // Then we take two pointers (previousEnd and current). The condition for interval removal is
        // previousEnd > currentStart, then we skip the current interval and move current to the next interval. One
        // thing to keep in mind is that when our intervals are non-overlapping, that time make sure that previousEnd
        // moves to the current interval, and then you move your current interval to the next one.

        // Track the end time of the last successfully placed interval
        int previousEnd = intervals[0][1];

        // Iterate starting from the second interval
        for(int current = 1; current < intervals.length; current++){
            int currentStart = intervals[current][0];
            int currentEnd = intervals[current][1];

            // If it starts BEFORE the previous one ends, it's an overlap!
            if(previousEnd > currentStart){
                // skip the current interval
                minimumIntervalsToBeRemoved++; // "Remove" the current interval
                // Note: We do NOT update prevEnd because we are discarding this interval. Keeping the older, smaller
                // prevEnd is greedily safer.
            } else {
                // No overlap: We accept this interval and update our end marker
                previousEnd = currentEnd;
            }
        }

        return minimumIntervalsToBeRemoved;
    }

    static void main(){
        NonOverLappingIntervals nonOverLappingIntervals = new NonOverLappingIntervals();
        System.out.println(nonOverLappingIntervals.eraseOverlapIntervals(new int[][]{}));
    }
}
