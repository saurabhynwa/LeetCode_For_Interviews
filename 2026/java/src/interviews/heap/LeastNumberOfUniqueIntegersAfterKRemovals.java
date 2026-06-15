package src.interviews.heap;

/*
Given an array of integers arr and an integer k. Find the least number of unique integers after removing exactly k
elements.

Example 1:
Input: arr = [5,5,4], k = 1
Output: 1
Explanation: Remove the single 4, only 5 is left.

Example 2:
Input: arr = [4,3,1,1,3,3,2], k = 3
Output: 2
Explanation: Remove 4, 2 and either one of the two 1s or three 3s. 1 and 3 will be left.

Constraints:

1 <= arr.length <= 10^5
1 <= arr[i] <= 10^9
0 <= k <= arr.length
 */

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/*
Time Complexity: O(N log N)

Space Complexity: O(N)
 */

public class LeastNumberOfUniqueIntegersAfterKRemovals {
    private int findLeastNumOfUniqueInts(int[] arr, int k) {
        // Logic: first remove the integers with smaller counts.

        Map<Integer, Integer> valueCountMap = new HashMap<>();

        for(int num: arr){
            valueCountMap.put(num, valueCountMap.getOrDefault(num, 0) + 1);
        }

        // create a min heap based on the HashMap value count. Heap will contain num's face value
        PriorityQueue<Integer> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(valueCountMap.get(a), valueCountMap.get(b)));

        // build heap
        for(Integer num: valueCountMap.keySet()){
            minHeap.add(num);
        }

        while(k > 0){
            int lowestCountNum = minHeap.peek();
            // reduce from map
            int updatedValue = valueCountMap.get(lowestCountNum) - 1;
            k--;

            if(updatedValue == 0){
                // remove from Map
                valueCountMap.remove(lowestCountNum);
                // remove from heap
                minHeap.poll();
            } else {
                // update the map
                valueCountMap.put(lowestCountNum, updatedValue);
            }
        }

        return minHeap.isEmpty() ? 0 : minHeap.size();
    }

    public static void main(String[] args){
        LeastNumberOfUniqueIntegersAfterKRemovals obj = new LeastNumberOfUniqueIntegersAfterKRemovals();
        System.out.println(obj.findLeastNumOfUniqueInts(new int[]{5,5,4}, 1));
        System.out.println(obj.findLeastNumOfUniqueInts(new int[]{4,3,1,1,3,3,2}, 3));
    }
}
