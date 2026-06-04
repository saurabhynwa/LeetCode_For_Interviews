package src.heap;

// LeetCode Hard
/*
The median is the middle value in an ordered integer list. If the size of the list is even, there is no middle value,
and the median is the mean of the two middle values. For example, for arr = [2,3,4], the median is 3. For example, for
arr = [2,3], the median is (2 + 3) / 2 = 2.5.

Implement the MedianFinder class:

1) MedianFinder() initializes the MedianFinder object.
2) void addNum(int num) adds the integer num from the data stream to the data structure.
3) double findMedian() returns the median of all elements so far. Answers within 10-5 of the actual answer will be accepted.


Example 1:
Input
["MedianFinder", "addNum", "addNum", "findMedian", "addNum", "findMedian"]
[[], [1], [2], [], [3], []]
Output
[null, null, null, 1.5, null, 2.0]

Explanation
MedianFinder medianFinder = new MedianFinder();
medianFinder.addNum(1);    // arr = [1]
medianFinder.addNum(2);    // arr = [1, 2]
medianFinder.findMedian(); // return 1.5 (i.e., (1 + 2) / 2)
medianFinder.addNum(3);    // arr[1, 2, 3]
medianFinder.findMedian(); // return 2.0

Constraints:

-105 <= num <= 105
There will be at least one element in the data structure before calling findMedian.
At most 5 * 104 calls will be made to addNum and findMedian.
 */
import java.util.PriorityQueue;

/*
1) addNum() Time: 𝒪(log𝑁) logarithmic entry balance. Pushing an element and executing the boundary pointer shifts takes
 at most a couple of node rebalances.

2) findMedian() Time: 𝒪(1) perfect constant lookup. The targets are always pinned precisely at the roots of both trees.

3) Space Complexity: 𝒪(𝑁) memory allocation to map the active stream items entirely within the dual array heap caches.
 */

/*
The Intuition: The "Two-Heap Equilibrium"
If you sort an array, the median is simply the element sitting right in the exact middle. Instead of re-sorting the
array every time a new number arrives, imagine cutting the sorted dataset exactly in half down the center line:

- The Lower Half: The smaller numbers.
- The Upper Half: The larger numbers.

We can capture these two halves using two separate heaps facing each other like mirrors

[ MAX-HEAP ]   <-- Lower Half (Holds smaller numbers)
    (Root: Largest of the small)
============   <-- The Median Line sits right here!
[ MIN-HEAP ]   <-- Upper Half (Holds larger numbers)
    (Root: Smallest of the large)

The Ultimate Trick:
1) The Lower Half is managed by a Max-Heap. Why? Because we want quick access to the largest number of the smaller group
 (the root sits right next to the median line).

2) The Upper Half is managed by a Min-Heap. Why? Because we want quick access to the smallest number of the larger group
 (the root sits right next to the median line).

By balancing these two heaps so their sizes never differ by more than 1 element, calculating the median becomes an
instant 𝑂(1) lookup:
- If both heaps have the same number of elements, the median is the average of both roots!
- If one heap is larger, its root is the exact median!
 */
public class MedianFinder {
    // Max-heap stores the lower/smaller half of numbers
    private final PriorityQueue<Integer> lowerHalfMaxHeap;
    // Min-heap stores the upper/larger half of numbers
    private final PriorityQueue<Integer> upperHalfMinHeap;

    public MedianFinder(){
        // Max-heap custom comparator (descending face values)
        this.lowerHalfMaxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        // Standard Min-heap (ascending face values)
        this.upperHalfMinHeap = new PriorityQueue<>();
    }

    private void addNum(int num) {
        // Step 1: Default to placing the new number into the lower half
        lowerHalfMaxHeap.add(num);

        // Step 2: Invariant Check 1 -> Every element in the lower half MUST be smaller than or equal to every element
        // in the upper half.
        if (!lowerHalfMaxHeap.isEmpty() && !upperHalfMinHeap.isEmpty()
                && lowerHalfMaxHeap.peek() > upperHalfMinHeap.peek()) {

            // Rebalance the boundary violation
            upperHalfMinHeap.add(lowerHalfMaxHeap.poll());
        }

        // Step 3: Invariant Check 2 -> Keep heap sizes balanced (equilibrium) Lower half max-heap is allowed to hold at
        // most ONE extra element
        if (lowerHalfMaxHeap.size() > upperHalfMinHeap.size() + 1) {
            upperHalfMinHeap.add(lowerHalfMaxHeap.poll());
        } else if (upperHalfMinHeap.size() > lowerHalfMaxHeap.size()) {
            lowerHalfMaxHeap.add(upperHalfMinHeap.poll());
        }
    }

    private double findMedian() {
        // If even total elements, average the two closest roots sitting on the median line
        if (lowerHalfMaxHeap.size() == upperHalfMinHeap.size()) {
            return (double) (lowerHalfMaxHeap.peek() + upperHalfMinHeap.peek()) / 2.0;
        }

        // If odd total elements, the larger heap's root is the absolute middle ground
        return (double) lowerHalfMaxHeap.peek();
    }

    public static void main(String[] args){
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.addNum(1);    // arr = [1]
        medianFinder.addNum(2);    // arr = [1, 2]
        System.out.println(medianFinder.findMedian()); // return 1.5 (i.e., (1 + 2) / 2)
        medianFinder.addNum(3);    // arr[1, 2, 3]
        System.out.println(medianFinder.findMedian()); // return 2.0
    }

    /*
    Follow up:
    1) If all integer numbers from the stream are in the range [0, 100], how would you optimize your solution?
    Ans: If the range of incoming numbers is bounded and extremely small, you can completely throw away the heaps.
    Replace them with a fixed-size frequency array.

    The Optimization Intuition:
    - Create an integer array int[] counts = new int[101] and a counter totalCount.
    - addNum(int num): Simply increment counts[num] and increment totalCount. This runs in 𝒪(1) perfect constant time,
    completely bypassing the 𝒪(log𝑁) heap-balancing overhead.
    - findMedian(): Calculate the target middle index (totalCount / 2). Walk through your 101 buckets, accumulating the
    counts until you cross that target index. Because the array size is permanently fixed at 101 elements, this scan
    takes at most 101 operations, meaning findMedian() also runs in 𝒪(1) constant time.

    2) If 99% of all integer numbers from the stream are in the range [0, 100], how would you optimize your solution?
    Ans: If a tiny 1% fraction of your data contains outliers (numbers < 0 or > 100), you can expand the bucket strategy
     by wrapping it with two outlier catch-containers.

     The Optimization Intuition:
     - Keep the int[] counts = new int[101] array for the core [0, 100] range.
     - Maintain a Max-Heap to store the rare values that come in < 0.
     - Maintain a Min-Heap to store the rare values that come in >100

     🛠️ How the Operations Scale Natively
     - addNum(int num) Execution:
        - If num < 0: Push to the lower Max-Heap 𝒪(log(outliers)).
        - If num > 100: Push to the upper Min-Heap 𝒪(log(outliers)).
        - Else: Increment counts[num] (𝒪(1) constant time).
        - Result: Since 99% of your data avoids the heaps, your streaming insertion speed remains at an effective 𝒪(1)
        operational constant on average.

     - findMedian() Execution:
        - Calculate your target median index based on the global totalCount.
        - Step 1: If the target index is less than or equal to the size of your lower Max-Heap, the median lives in the
        outliers. Pull it from the Max-Heap.
        - Step 2: If the target index lands past the lower heap but inside your main range, skip the lower heap's size
        offset and walk your 101-element bucket array to locate the value in 𝒪(1) constant time.
        - Step 3: If the target index is larger than the lower heap plus the total bucket sum, the median lives in the
        upper Min-Heap. Pull it from there.
        - Result: Because 99% of the data sits inside the bounded buckets, the median is mathematically guaranteed to
        land inside your 101-array frame for almost all streams, keeping the lookup cost at a highly efficient 𝒪(1)
        average runtime.
     */

}
