package src.two_pointers;

/*
Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it
can trap after raining.

Example 1:
Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
Output: 6
Explanation: The above elevation map (black section) is represented by array [0,1,0,2,1,0,1,3,2,1,2,1]. In this case, 6 units of rain water (blue section) are being trapped.

Example 2:
Input: height = [4,2,0,3,2,5]
Output: 9

Constraints:

n == height.length
1 <= n <= 2 * 104
0 <= height[i] <= 105
 */

/*
Time complexity: O(n). Single iteration of O(n).
Space complexity: O(1) extra space. Only constant space required for currentLeft, currentRight, leftMax and rightMax.

Trick: 2 pointers and 2 iterators
 */
public class TrappingRainWater {
    public int trap(int[] height) {
        int waterTrapped = 0;
        // sanity check
        if(height == null || height.length <= 1){
            return waterTrapped; // no water can be trapped
        }

        // Logic: We will use 2 pointers and 2 iterators. Water can be trapped between two towers.
        // larger tower can be on the left, middle or right. So we keep left and right max pointers.
        // And we also keep two iterators for themselves. In the left and right max pointer, whichever is
        // the smaller, the iterator on that side moves. Once it moves then it evaluates the max of that side
        // if a new max is found we move the max, else we add to the sum the difference between the max of
        // that side and the current iterator. Same for the other side.

        int currentLeft = 0;
        int leftMax = height[currentLeft];
        int currentRight = height.length - 1;
        int rightMax = height[currentRight];

        while(currentLeft < currentRight){
            // check which side max is smaller
            if(leftMax < rightMax){
                //move the iterator on the left side
                currentLeft++;
                //check for new max
                if(height[currentLeft] >= leftMax){
                    //move leftMax to currentLeft
                    leftMax = height[currentLeft];
                } else {
                    //leftMax > currentLeft. So reduce the height[currentLeft] from leftMax and add to sum
                    waterTrapped += leftMax - height[currentLeft];
                }
            } else {
                //rightMax is equal or smaller to leftMax
                //move the iterator on right side
                currentRight--;
                //check for new rightMax
                if(height[currentRight] >= rightMax){
                    //move rightMax to currentRight
                    rightMax = height[currentRight];
                } else {
                    //rightMax > currentRight, so we reduce the height[currentRight] from rightMax and add
                    //it to the sum
                    waterTrapped += rightMax - height[currentRight];
                }
            }
        }
        return waterTrapped;
    }

    static void main(){
        TrappingRainWater trappingRainWater = new TrappingRainWater();
        System.out.println(trappingRainWater.trap(new int[]{0,1,0,2,1,0,1,3,2,1,2,1}));
        System.out.println(trappingRainWater.trap(new int[]{4,2,0,3,2,5}));
    }
}
