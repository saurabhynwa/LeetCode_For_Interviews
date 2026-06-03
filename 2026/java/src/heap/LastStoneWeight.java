package src.heap;

/*
You are given an array of integers stones where stones[i] is the weight of the ith stone. We are playing a game with the
 stones. On each turn, we choose the heaviest two stones and smash them together. Suppose the heaviest two stones have
 weights x and y with x <= y. The result of this smash is:

1) If x == y, both stones are destroyed, and
2) If x != y, the stone of weight x is destroyed, and the stone of weight y has new weight y - x.

At the end of the game, there is at most one stone left. Return the weight of the last remaining stone. If there are no
stones left, return 0.

Example 1:
Input: stones = [2,7,4,1,8,1]
Output: 1
Explanation:
We combine 7 and 8 to get 1 so the array converts to [2,4,1,1,1] then,
we combine 2 and 4 to get 2 so the array converts to [2,1,1,1] then,
we combine 2 and 1 to get 1 so the array converts to [1,1,1] then,
we combine 1 and 1 to get 0 so the array converts to [1] then that's the value of the last stone.

Example 2:
Input: stones = [1]
Output: 1

Constraints:

1 <= stones.length <= 30
1 <= stones[i] <= 1000
 */

import java.util.PriorityQueue;

/*
Time Complexity: O(n log n) where (n) is the number of stones.
1. Initializing the heap by pushing all elements takes O(n log n) time.
2. In the worst-case scenario, the while loop runs (n - 1) times. During each iteration, your poll() and add()
operations take O(log n) logarithmic heap-adjustment adjustments.

Space Complexity: O(n) auxiliary workspace memory on the heap to host the elements within the PriorityQueue buffer.
 */
public class LastStoneWeight {
    private int getLastStoneWeight(int[] stones){
        // sanity check
        if(stones == null || stones.length == 0){
            return 0;
        }

        if(stones.length == 1){
            return stones[0];
        }

        // max heap
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b, a));

        // insert into max heap
        for(int stone: stones){
            maxHeap.add(stone);
        }

        while(maxHeap.size() > 1){
            // get the 2 heaviest stones
            int heaviestStone = maxHeap.poll();
            int secondHeaviestStone = maxHeap.poll();

            int delta = heaviestStone - secondHeaviestStone;

            if(delta > 0){
                // add back to maxHeap
                maxHeap.add(delta);
            }
        }

        return maxHeap.isEmpty() ? 0 : maxHeap.peek();
    }

    public static void main(String[] args){
        LastStoneWeight lastStoneWeight = new LastStoneWeight();
        System.out.println(lastStoneWeight.getLastStoneWeight(new int[]{2,7,4,1,8,1}));
        System.out.println(lastStoneWeight.getLastStoneWeight(new int[]{1}));
    }
}
