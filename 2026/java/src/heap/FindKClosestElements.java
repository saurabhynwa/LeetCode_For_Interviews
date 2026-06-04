package src.heap;

/*
Given a sorted integer array arr, two integers k and x, return the k closest integers to x in the array. The result
should also be sorted in ascending order. An integer a is closer to x than an integer b if:

1) |a - x| < |b - x|, or
2) |a - x| == |b - x| and a < b


Example 1:
Input: arr = [1,2,3,4,5], k = 4, x = 3
Output: [1,2,3,4]

Example 2:
Input: arr = [1,1,2,3,4,5], k = 4, x = -1
Output: [1,1,2,3]

Constraints:

1 <= k <= arr.length
1 <= arr.length <= 104
arr is sorted in ascending order.
-104 <= arr[i], x <= 104
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/*
Time Complexity: 𝒪(𝑁 log 𝐾 + 𝐾 log 𝐾)
Let 'N' be the total number of items in the array arr, and 'K' be the number of closest elements requested.

1) The Sifting Loop (𝒪(𝑁log𝐾)):
a. You iterate over every single item in the array exactly once via the for(int num: arr) loop (N rounds).
b. Inside the loop, you perform a maxHeap.add(num). Because you immediately trigger a maxHeap.poll() the moment the
tree size exceeds 'K', the height of your PriorityQueue is strictly capped at log 𝐾.
c. Each insertion and structural rebalance costs 𝒪(log 𝐾) time. Across N items, this takes 𝒪(𝑁log𝐾) execution time.

2) The Heap Unloading Loop (𝒪(𝐾log𝐾)):
a. The while(!maxHeap.isEmpty()) loop runs exactly K times to empty the remaining survivors out of the priority queue.
b. Each poll() removal costs a logarithmic tree adjustment time of 𝒪(log𝐾), resulting in 𝒪(𝐾log𝐾).

3) The Final Sort step (𝒪(𝐾log𝐾)):
a. Calling Collections.sort(resultList) runs the highly optimized Dual-Pivot Timsort algorithm over a collection
containing exactly K elements. This scales at 𝒪(𝐾log𝐾).
b. Total Combined Time: 𝒪(𝑁log𝐾) + 𝒪(𝐾log𝐾) + 𝒪(𝐾log𝐾) → 𝒪(𝐍log𝐊+𝐊log𝐊). Since 𝐾≤𝑁, the dominant bottleneck factor
is the linear-logarithmic sifting pass.

Space Complexity: O(K)
1. Capped Heap Buffer (𝒪(𝐾)): Your active memory overhead is determined exclusively by your PriorityQueue size. Because
 your structural if(maxHeap.size() > k) rule caps it dynamically, the heap never holds more than 𝐾+1 elements at any
 single point in flight.

2. Heap Collections Allocation: Managing this internal tree structure consumes linear memory relative to its size,
yielding a constant-bounded 𝒪(𝐾) auxiliary memory footprint that scales completely independently of the size of a
massive input array N.
(Note: The resultList allocation requires 𝒪(𝐾) space, but this is often considered part of the expected output return
signature rather than extra workspace overhead in core interviews).
 */

// There is a more efficient approach using two-pointers. Prefer that.
public class FindKClosestElements {
    private List<Integer> getKClosestElements(int[] arr, int k, int x){
        // sanity check
        if(arr == null || arr.length == 0){
            return new ArrayList<Integer>();
        }

        // conditions given for closer element to 'x': |x - a| < |x - b| OR |x - a| == |x - b| then a < b

        // we will maintain a max heap of size 'K' which will store elements based on their distance from 'x'.
        // Since maxHeap, root will be the largest distance. We add to the heap and remove the top the moment our heap
        // crosses size 'k'. That way the heap will contain the smallest 'k' distances.

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a,b) -> {
            int distanceFromA = Math.abs(x - a);
            int distanceFromB = Math.abs(x - b);

            // defines maxHeap building
            int primaryCompareForMaxHeap = Integer.compare(distanceFromB, distanceFromA);

            // but when distance is same, then we need to decide on the actual face value and select the larger value
            // for maxHeap
            return primaryCompareForMaxHeap == 0 ? Integer.compare(b, a) : primaryCompareForMaxHeap;
        });

        for(int num: arr){
            maxHeap.add(num);

            if(maxHeap.size() > k){
                // remove the largest distance at root
                maxHeap.poll();
            }
        }

        List<Integer> resultList = new ArrayList<>();

        while(!maxHeap.isEmpty()){
            resultList.add(maxHeap.poll());
        }

        // expected output requires sorted elements
        Collections.sort(resultList);

        return resultList;
    }

    private void printResult(List<Integer> result){
        System.out.print("[");
        for(int num: result){
            System.out.print(num + ", ");
        }
        System.out.print("]");
        System.out.println();
    }

    public static void main(String[] args){
        FindKClosestElements obj = new FindKClosestElements();
        obj.printResult(obj.getKClosestElements(new int[]{1,2,3,4,5}, 4, 3));
        obj.printResult(obj.getKClosestElements(new int[]{1,1,2,3,4,5}, 4, -1));
    }
}
