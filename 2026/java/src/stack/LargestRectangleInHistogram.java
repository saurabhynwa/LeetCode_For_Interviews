package src.stack;

/*
LeetCode Hard: Given an array of integers heights representing the histogram's bar height where the width of each bar
is 1, return the area of the largest rectangle in the histogram.

Example 1:
Input: heights = [2,1,5,6,2,3]
Output: 10

Explanation: The above is a histogram where width of each bar is 1.
The largest rectangle is shown in the red area, which has an area = 10 units.
Example 2:

Input: heights = [2,4]
Output: 4

Constraints:

1 <= heights.length <= 105
0 <= heights[i] <= 104
*/

import java.util.Stack;
/*
TTime Complexity: (O(N))
1. Linear Sweep: The two sequential while loops split the work back-to-back ((O(n) + O(n))).
2. Constant Operations: Every bar index is pushed onto the stack exactly once and popped exactly once.
3. Total: (O(n)) where (n) is the number of bars.

Space Complexity: (O(N))
1. Stack Size: In the worst-case scenario (strictly increasing heights), the stack stores every index simultaneously.
2. In-Place: No copy arrays are created, minimizing extra heap memory allocations.
3. Total: (O(N)) auxiliary space.

 */
public class LargestRectangleInHistogram {
    private int getLargestRectangleArea(int[] heights){
        int maxArea = 0;

        // sanity check
        if(heights == null || heights.length < 1){
            return maxArea;
        }

        // Logic: Use Monotonic stack which keeps track of indexes that represent strictly growing bars.
        // If the stack is empty or the height of current bar >= tallest bar we have seen (stack top), then we push
        // the current index to stack and move our current index. If we come across a smaller bar, we pause the
        // increment of currentIndex. We get the tallest bar from stack top index and use that as length. Now for width,
        // we need right and left boundaries. RightBoundary = index of the tallest bar we have seen so far for the
        // current bar. It is the index just before we saw the smaller bar. This boundary is fixed. Now LeftBoundary
        // indicates the index of the next tallest bar. Which should be available on the stack top. If stack is empty,
        // them it means the index of the next tallest stretches till start, at index 0. We need to account for index 0
        // in our width calculation (right - left). Hence, we return -1 for that case. Now we calculate our maxArea. Now
        // again we keep looping until the bar at currentIndex is not the smaller bar anymore.

        // after the currentIndex breaks out of the while loop due to length constraint, we check whether our stack is
        // empty or not. If stack is not empty it means it contains bars who have not come across a smaller bar yet. For
        // them the right boundary index becomes the last index and, it is fixed as well because that is the tallest bar
        // as well. Now we expand the left boundary index same as above.

        Stack<Integer> indexStack = new Stack<>();
        int currentIndex = 0;

        while(currentIndex < heights.length){
            int currentHeight = heights[currentIndex];

            // check whether the current height >= the tallest height we have seen so far
            if(indexStack.isEmpty() || currentHeight >= heights[indexStack.peek()]){
                // push current index to stack
                indexStack.push(currentIndex);
                // move current index
                currentIndex++;
            } else {
                // we came across a shorter bar. Pause current index movement
                // use the tallest bar at stack top index value as length
                int length = heights[indexStack.pop()];
                // right boundary is the index of the tallest bar, which was just before our shorter current index
                int rightBoundary = currentIndex - 1;
                // left boundary is index of next taller bar. Stack top gives that value. If stack empty, then the next
                // taller bar is at index 0, return -1 to cover zero index in width calculation of (right - left)
                int leftBoundary = indexStack.isEmpty() ? -1 : indexStack.peek();
                int width = rightBoundary - leftBoundary;
                // evaluate maxArea
                maxArea = Math.max(maxArea, length * width);
            }
        }

        // check whether stack is empty. If not empty it means all the bars in stack are the tallest bars which have not
        // come across a smaller ones. They are in descending order. Right boundary becomes the last index of input
        // array and left boundary follows the same logic as above

        int rightBoundary = currentIndex - 1; // last index of input array

        while(!indexStack.isEmpty()){
            int length = heights[indexStack.pop()];
            int leftBoundary = indexStack.isEmpty() ? -1 : indexStack.peek();
            int width = rightBoundary - leftBoundary;
            maxArea = Math.max(maxArea, length * width);
        }

        return maxArea;
    }

    public static void main(String[] args){
        LargestRectangleInHistogram lrh = new LargestRectangleInHistogram();
        System.out.println(lrh.getLargestRectangleArea(new int[]{2,1,5,6,2,3}));
        System.out.println(lrh.getLargestRectangleArea(new int[]{2,4}));
    }
}
