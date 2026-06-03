package src.heap;

/*
Given an integer array nums and an integer k, return the kth largest element in the array. Note that it is the kth
largest element in the sorted order, not the kth distinct element. Can you solve it without sorting?

Example 1:
Input: nums = [3,2,1,5,6,4], k = 2
Output: 5

Example 2:
Input: nums = [3,2,3,1,2,4,5,5,6], k = 4
Output: 4

Constraints:

1 <= k <= nums.length <= 105
-104 <= nums[i] <= 104
 */
import java.util.PriorityQueue;

// Note: Quick Select technique can do this in O(N) on avg, but worse case is O(N ^ 2) and it also mutates your input.
// Hence, minHeap option is preferred.

/*
Time Complexity: O(N log K). K is the size of heap

Space Complexity: O(K)
 */
public class KthLargestElementInArray {
    private int getKthLargestElement(int[] nums, int k){
        // we will use 'min' heap to find the Kth largest element. Why 'min' and not 'max' heap ? We are maintaining the
        // heap size up to 'K'. So the smallest element gets popped out. At the end will have 'K' elements in the heap,
        // which are greater than all the other elements in the input. Out of these elements, the root would be the
        // smallest w.r.t current heap elements. But w.r.t all the array elements, it would be the 'K' largest element.

        // sanity check
        if(nums == null || nums.length == 0){
            return 0;
        }

        // min heap
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for(int num: nums){
            // add current element to heap
            minHeap.add(num);

            // now check for minHeap size
            if(minHeap.size() > k){
                // remove the smallest element, which is at the root
                minHeap.poll();
            }
        }

        return minHeap.peek();
    }

    public static void main(String[] args){
        KthLargestElementInArray obj = new KthLargestElementInArray();
        System.out.println(obj.getKthLargestElement(new int[]{3,2,1,5,6,4}, 2));
        System.out.println(obj.getKthLargestElement(new int[]{3,2,3,1,2,4,5,5,6}, 4));
    }
}
