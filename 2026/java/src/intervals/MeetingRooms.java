package src.intervals;

/*
Given an array of meeting time intervals where intervals[i] = [starti, endi], determine if a person could attend all
meetings.

Example 1:

Input: intervals = [[0,30],[5,10],[15,20]]
Output: false
Example 2:

Input: intervals = [[7,10],[2,4]]
Output: true


Constraints:

0 <= intervals.length <= 104
intervals[i].length == 2
0 <= starti < endi <= 106
*/

import java.util.Arrays;

/*
Time Complexity: (O(n log n)) where (n) is the number of intervals. This is because sorting the array takes (O(n log n))
time, while the subsequent linear scan takes (O(n)) time.

Space Complexity: (O(1)) or (O(n)) depending on the sorting implementation details of the programming language
(Java's Arrays.sort() for objects uses Timsort, which requires (O(n)) space in the worst case).
 */
public class MeetingRooms {
    private boolean canAttendAllMeetings(int[][] intervals){
        // sanity check. No meetings or only 1 meeting means no conflict. Everything can be attended.
        if(intervals == null || intervals.length <= 1){
            return true;
        }

        // Logic: We need to sort the given 2D input array basis the start time. If the current meeting's start time is
        // lesser than the previous meeting's end time, it means the meetings are overlapping and there is a conflict
        // which restricts the user from attending all of them.

        // 2D array, firs bracket [] => total numbers of rows, second bracket [] => total number of columns
        // To sort 2D array, we need to use Lambda expression, where we specify the logic of how the inner arrays are to
        // be used for sorting.

        Arrays.sort(intervals, (a,b) -> Integer.compare(a[0], b[0]));
        // a[0] < b[0], returns negative integer, means 'a' should be before 'b'
        // returns zero if equal
        // a[0] > b[0], returns positive integer, means 'a' should be after 'b'
        // Ex: {{7,10}, {2,4}} a = {7,10} b = {2,4}, after sorting {{2,4}, {7,10}}

        for(int current = 1; current < intervals.length; current++){
            // if the previous meeting's end time is greater than the current meeting's start time, there is a conflict
            if(intervals[current - 1][1] > intervals[current][0]){
                return false;
            }
        }

        return true;
    }

    static void main(){
        MeetingRooms meetingRooms = new MeetingRooms();
        System.out.println(meetingRooms.canAttendAllMeetings(new int[][]{{7,10}, {2,4}}));
        System.out.println(meetingRooms.canAttendAllMeetings(new int[][]{{0,30}, {5,10}, {15,20}}));
    }
}
