package src.two_pointers;

/*
You are given an integer array height of length n. There are n vertical lines drawn such that the two endpoints of the ith line are (i, 0) and (i, height[i]).

Find two lines that together with the x-axis form a container, such that the container contains the most water.

Return the maximum amount of water a container can store.

Notice that you may not slant the container.
Example 1:

Input: height = [1,8,6,2,5,4,8,3,7]
Output: 49
Explanation: The above vertical lines are represented by array [1,8,6,2,5,4,8,3,7]. In this case, the max area of water (blue section) the container can contain is 49.
Example 2:

Input: height = [1,1]
Output: 1

Constraints:

n == height.length
2 <= n <= 105
0 <= height[i] <= 104
 */

// Time Complexity = O(n) single pass. Space Complexity = O(1)
public class ContainerWithMostWater {
    private int maxArea(int[] height) {
        // sanity check
        if (height == null || height.length <= 1) {
            return 0;
        }

        //Logic: length * breadth. Use 2 pointers. Calculate the difference between the two towers. Reduce the delta
        // from the larger one. Use this new value as length and multiply it with the difference of index which is the
        // breadth. Move the smaller pointer.

        //initialize the pointers
        int leftPtr = 0;
        int rightPtr = height.length - 1;
        //initialize the result to be returned
        int maxWater = 0;

        while (leftPtr < rightPtr) {
            int leftTower = height[leftPtr];
            int rightTower = height[rightPtr];
            int breadth = rightPtr - leftPtr;

            if (leftTower < rightTower) {
                //calculate water trapped
                maxWater = Math.max(maxWater, getMaxWaterTrapped(leftTower, rightTower, breadth, false));
                //move the smaller pointer
                leftPtr++;
            } else if (leftTower > rightTower) {
                //calculate water trapped
                maxWater = Math.max(maxWater, getMaxWaterTrapped(rightTower, leftTower, breadth, false));
                rightPtr--;
            } else {
                maxWater = Math.max(maxWater, getMaxWaterTrapped(rightTower, leftTower, breadth, true));
                // move the leftPtr
                leftPtr++;
            }
        }
        return maxWater;
    }

    private int getMaxWaterTrapped(int smallerTower, int largerTower, int breadth, boolean equalTowers) {
        int length;

        if (equalTowers) {
            length = largerTower; // can be smallerTower as well
        } else {
            //reduce the smaller from bigger
            int towerDelta = largerTower - smallerTower;
            //reduce the delta from the bigger
            length = largerTower - towerDelta;
        }

        return length * breadth;
    }

    static void main(){
        ContainerWithMostWater containerWithMostWater = new ContainerWithMostWater();
        System.out.println(containerWithMostWater.maxArea(new int[]{1,8,6,2,5,4,8,3,7}));

        System.out.println(containerWithMostWater.maxArea(new int[]{1,1}));
    }
}
