package src.heap;

/*
Given an integer array nums and an integer k, return the k most frequent elements. You may return the answer in any
order.

Example 1:
Input: nums = [1,1,1,2,2,3], k = 2
Output: [1,2]

Example 2:
Input: nums = [1], k = 1
Output: [1]

Example 3:
Input: nums = [1,2,1,2,1,2,3,1,3,2], k = 2
Output: [1,2]

Constraints:

1 <= nums.length <= 105
-104 <= nums[i] <= 104
k is in the range [1, the number of unique elements in the array].
It is guaranteed that the answer is unique.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/*
Time Complexity: O(N log k)
1. Building the map takes O(N) linear time.
2. Sifting unique elements through the heap takes O(U log k), where (U) is the number of unique elements (U <= N). Each
insertion/deletion in a heap of size (k) costs (log k).
3. This completely beats sorting the frequencies (O(N \log N)).

Space Complexity: (O(N + k))
1. The HashMap stores up to (N) elements in the worst-case scenario.
2. The PriorityQueue stores exactly (k) elements inside its active buffer footprint.
 */

/*
Think of the heap as a VIP lounge that only has room for k people. The numbers (e.g., 3, 99, 12) are the people standing
 in line. Their count/frequency is their wealth.

The comparator (a, b) -> Integer.compare(frequencyMap.get(a), frequencyMap.get(b)) acts as the security guard at the
door. Every time a new number tries to enter, the guard checks their bank account (frequencyMap.get()).If the lounge
gets too full (size > k), the guard ruthlessly kicks out the poorest person at the root (the number with the absolute
lowest frequency count). This guarantees that when the loop finishes, only the k absolute richest (most frequent)
numbers are left inside the room.
 */
public class TopKFrequentElements {
    private int[] getTopKFrequentElements(int[] nums, int k){
        // sanity check
        if(nums == null || nums.length == 0){
            return new int[0];
        }

        // loop over input and create a map of value, and it's count. Now, initialize a min heap with comparison to be
        // made for array element's count value in frequency map we have populated. That way our minHeap stores the
        // actual face value of the number basis the count.

        Map<Integer, Integer> frequencyMap = new HashMap<>();

        // populate map
        for(int num: nums){
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        // Important - initialize min heap and define lambda expression for comparison
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(
                (a,b) -> Integer.compare(frequencyMap.get(a), frequencyMap.get(b))
        );

        // we will maintain heap of size 'K'. That way our heap will contain the top 'K' elements by itself
        // let's walk the HashMap keySet
        for(int num: frequencyMap.keySet()){
            // add the current num to minHeap. Lambda expression looks up the value for the given num from frequency
            // map and use that value for comparison. Face value i.e, num gets stored in minHeap but sorting depends on
            // its count and not it's face value. This is very important here.
            minHeap.add(num);

            if(minHeap.size() > k){
                // remove the smallest element from the heap which is the root
                minHeap.poll();
            }
        }

        int[] result = new int[k];
        int currentIndex = 0;

        while(!minHeap.isEmpty()){
            result[currentIndex] = minHeap.poll();
            currentIndex++;
        }

        return result;
    }

    private void printResult(int[] result){
        for(int num: result){
            System.out.print(num + " ");
        }
        System.out.println();
    }

    public static void main(String[] args){
        TopKFrequentElements obj = new TopKFrequentElements();
        int[] result1 = obj.getTopKFrequentElements(new int[]{1,1,1,2,2,3}, 2);
        obj.printResult(result1);

        int[] result2 = obj.getTopKFrequentElements(new int[]{1}, 1);
        obj.printResult(result2);

        int[] result3 = obj.getTopKFrequentElements(new int[]{1,2,1,2,1,2,3,1,3,2}, 2);
        obj.printResult(result3);
    }
}
