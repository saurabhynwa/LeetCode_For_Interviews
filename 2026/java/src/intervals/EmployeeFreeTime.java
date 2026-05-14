package src.intervals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
We are given a list schedule of employees, which represents the working time for each employee.

Each employee has a list of non-overlapping Intervals, and these intervals are in sorted order.

Return the list of finite intervals representing common, positive-length free time for all employees, also in sorted order.

(Even though we are representing Intervals in the form [x, y], the objects inside are Intervals, not lists or arrays.
For example, schedule[0][0].start = 1, schedule[0][0].end = 2, and schedule[0][0][0] is not defined).  Also, we
wouldn't include intervals like [5, 5] in our answer, as they have zero length.


Example 1:

Input: schedule = [[[1,2],[5,6]],[[1,3]],[[4,10]]]
Output: [[3,4]]
Explanation: There are a total of three employees, and all common
free time intervals would be [-inf, 1], [3, 4], [10, inf].
We discard any intervals that contain inf as they aren't finite.
Example 2:

Input: schedule = [[[1,3],[6,7]],[[2,4]],[[2,5],[9,12]]]
Output: [[5,6],[7,9]]

Constraints:

1 <= schedule.length , schedule[i].length <= 50
0 <= schedule[i].start < schedule[i].end <= 10^8
 */

/*
Time Complexity: (O(N log N)) where (N) is the total number of working intervals across all employees. Sorting is the
dominant factor; the extraction and evaluation remain strictly (O(N)) linear.

Space Complexity: (O(N)) to maintain the flat timeline inventory.
 */

class Interval {
    int start;
    int end;

    public Interval(){} // empty constructor

    public Interval(int start, int end){
        this.start = start;
        this.end = end;
    }
}

public class EmployeeFreeTime {
    private List<Interval> employeeFreeTime(List<List<Interval>> schedule){
        List<Interval> resultList = new ArrayList<>();

        // sanity check
        if(schedule == null || schedule.isEmpty()){
            return resultList;
        }

        // Given: on employee level intervals are sorted and non overlapping.
        // Logic: If we plot these intervals as horizontal bars on a graph it becomes clear that there will be a gap
        // where none of the employees have meetings scheduled. To find those gaps we need to do the following:
        // step_1: first flatten the list. This will make the available gap common for all.
        // step_2: sort this flattened list by start time. This will help us understand the overlapping meetings.
        // step_3: we loop through this sorted list, merged overlapping schedule and write merged and non-overlapping
        // schedules into a new list.
        // step_4: this new list has schedules which are strictly non overlapping. So the answer schedule looks like
        // [end of (current - 1), start of current]

        // step_1: flatten the list
        List<Interval> timelines = new ArrayList<>();
        for(List<Interval> timeline: schedule){
            timelines.addAll(timeline);
        }

        if(timelines.size() <= 1){
            return resultList;
        }

        // step_2: sort the list by start time
        Collections.sort(timelines, (a,b) -> Integer.compare(a.start, b.start));

        // step_3: merged overlapping intervals and write them with non-overlapping ones into result set
        Interval previous = timelines.get(0);
        List<Interval> mergedResult = new ArrayList<>();

        for(int current = 1; current < timelines.size(); current++){
            int currentStart = timelines.get(current).start;
            int currentEnd = timelines.get(current).end;

            // merge check: prevEnd >= currentStart
            if(previous.end >= currentStart){
                // merge
                previous.end = Math.max(previous.end, currentEnd);
                // we are not tracking previous start, because sorted on start time guaranteed us that previous.start
                // will always be <= currentStart
            } else {
                // no overlap. Write previous to intermediate result set
                mergedResult.add(previous);
                // update previous to current
                previous = timelines.get(current);
            }
        }

        // add the last value of timelines held by previous to merged set
        mergedResult.add(previous);

        // step_4: iterate over the merged result and write in the final result set
        if(mergedResult.size() == 1){
            // no free time between the schedules
            return resultList;
        }

        for(int current = 1; current < mergedResult.size(); current++){
            int start = mergedResult.get(current - 1).end;
            int end = mergedResult.get(current).start;
            resultList.add(new Interval(start, end));
        }

        return resultList;
    }

    private void printResult(List<Interval> result){
        System.out.print("[");
        for(Interval temp: result){
            System.out.print(" [" + temp.start + "," + temp.end + "] ");
        }
        System.out.print("]");
        System.out.println();
    }

    static void main(){
        Interval interval1 = new Interval(1,2);
        Interval interval2 = new Interval(5,6);
        Interval interval3 = new Interval(1,3);
        Interval interval4 = new Interval(4,10);

        List<Interval> employee1 = List.of(interval1, interval2);
        List<Interval> employee2 = List.of(interval3);
        List<Interval> employee3 = List.of(interval4);

        List<List<Interval>> schedule1 = List.of(employee1, employee2, employee3);

        EmployeeFreeTime empFreeTime = new EmployeeFreeTime();
        List<Interval> result1 = empFreeTime.employeeFreeTime(schedule1);
        empFreeTime.printResult(result1);

        Interval interval5 = new Interval(1,3);
        Interval interval6 = new Interval(6,7);
        Interval interval7 = new Interval(2,4);
        Interval interval8 = new Interval(2,5);
        Interval interval9 = new Interval(9,12);

        List<Interval> employee4 = List.of(interval5, interval6);
        List<Interval> employee5 = List.of(interval7);
        List<Interval> employee6 = List.of(interval8, interval9);

        List<List<Interval>> schedule2 = List.of(employee4, employee5, employee6);
        List<Interval> result2 = empFreeTime.employeeFreeTime(schedule2);
        empFreeTime.printResult(result2);
    }
}
