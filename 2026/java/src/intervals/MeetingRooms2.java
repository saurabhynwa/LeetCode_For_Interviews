package src.intervals;

/*
Given an array of meeting time intervals intervals where intervals[i] = [starti, endi], return the minimum number of
conference rooms required.

Example 1:

Input: intervals = [[0,30],[5,10],[15,20]]
Output: 2
Example 2:

Input: intervals = [[7,10],[2,4]]
Output: 1

Constraints:

1 <= intervals.length <= 104
0 <= starti < endi <= 106
 */

import java.util.Arrays;
import java.util.PriorityQueue;

/*
Time Complexity: (O(n log n))

1) Sorting: Sorting the initial 2D array of (n) elements by start time takes (O(n log n)) time.
2) Heap Operations: The for loop runs (n-1) times. Inside the loop, inserting into a heap (add) and removing from a heap
 (poll) both take (O(log n)) time. Doing this for (n) elements takes (O(n log n)) time.
3) Total Time: (O(n log n) + O(n log n)) which simplifies to a clean (O(n log n))

Space Complexity: (O(n))
1) Heap Storage: In the worst-case scenario (where all meetings overlap perfectly, like a star-shaped schedule), the
roomScheduleHeap will have to store the end times of every single meeting simultaneously.
2) Total Space: (O(n)) to hold up to (n) integers in the priority queue.
 */

public class MeetingRooms2 {
    private int getMinimumNumberOfRooms(int[][] intervals){
        // Sanity check
        if(intervals == null || intervals.length == 0){
            return 0; // don't need any rooms
        }

        // Logic: We need to keep track of when a room is get freed up at the earliest so that the current meeting can
        // use it. If that's not possible we add one more meeting room to our capacity and record its ending time.

        // First we need to sort the input array basis 'start time'. Why ? because that will help us find overlapping
        // meetings.

        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        // Now to keep track of which room is getting freed up first, we will maintain a min heap with Priority Queue.
        // This keeps track of the meeting end time.
        PriorityQueue<Integer> roomAvailableHeap = new PriorityQueue<>();

        // Since the input is sorted by start time, let's add the ending time of the first meeting to the min heap
        roomAvailableHeap.add(intervals[0][1]);

        // now start from the second element in the input
        for(int current = 1; current < intervals.length; current++){
            int currentStart = intervals[current][0];
            int currentEnd = intervals[current][1];

            // check if the currentStart >= the earliest room ending time. If yes, use that room
            if(currentStart >= roomAvailableHeap.peek()){
                // claim the room
                roomAvailableHeap.poll(); // removing that end time from the top of the min heap
            }

            // update the ending time of the current meeting in the room availability heap
            roomAvailableHeap.add(currentEnd);
        }

        // now the size of the min heap is our answer for the minimum number of rooms that are required to schedule all
        // the given meetings without any conflict.
        return roomAvailableHeap.size();
    }
}
