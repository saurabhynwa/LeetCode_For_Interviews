package src.easy;

/*
The next greater element of some element x in an array is the first greater element that is to the right of x in the
same array. You are given two distinct 0-indexed integer arrays nums1 and nums2, where nums1 is a subset of nums2.

For each 0 <= i < nums1.length, find the index j such that nums1[i] == nums2[j] and determine the next greater element
of nums2[j] in nums2. If there is no next greater element, then the answer for this query is -1. Return an array ans of
length nums1.length such that ans[i] is the next greater element as described above.

Example 1:

Input: nums1 = [4,1,2], nums2 = [1,3,4,2]
Output: [-1,3,-1]
Explanation: The next greater element for each value of nums1 is as follows:
- 4 is underlined in nums2 = [1,3,4,2]. There is no next greater element, so the answer is -1.
- 1 is underlined in nums2 = [1,3,4,2]. The next greater element is 3.
- 2 is underlined in nums2 = [1,3,4,2]. There is no next greater element, so the answer is -1.
Example 2:

Input: nums1 = [2,4], nums2 = [1,2,3,4]
Output: [3,-1]
Explanation: The next greater element for each value of nums1 is as follows:
- 2 is underlined in nums2 = [1,2,3,4]. The next greater element is 3.
- 4 is underlined in nums2 = [1,2,3,4]. There is no next greater element, so the answer is -1.


Constraints:

1 <= nums1.length <= nums2.length <= 1000
0 <= nums1[i], nums2[i] <= 104
All integers in nums1 and nums2 are unique.
All the integers of nums1 also appear in nums2.
 */

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
/*
Time Complexity: (O(N1 + N2)) — A perfect linear sweep.
Space Complexity: (O(N2)) — To hold the tracking map and the boundary index stack.
 */
class NextGreaterElement {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int[] result = new int[nums1.length];
        // initialize to -1
        Arrays.fill(result, -1);

        // loop over nums 2, maintain a HashMap (given that numbers are unique).
        // Key = nums2 value, value (nextGreaterElement) = -1
        Map<Integer, Integer> mapOfNums2 = getArrayMapped(nums2);
        Stack<Integer> nums2IndexStack = new Stack<>();

        // Monotonic stack build up
        for(int currentIndex = 0; currentIndex < nums2.length; currentIndex++){
            int currentValue = nums2[currentIndex];

            if(nums2IndexStack.isEmpty()){
                // push current index and move on
                nums2IndexStack.push(currentIndex);
            } else {
                // check if current value > stack top index
                while(!nums2IndexStack.isEmpty() && currentValue > nums2[nums2IndexStack.peek()]){
                    // pop stack top and replace current with the value at the popped index in the Map
                    int nums2ValueToBeReplaced = nums2[nums2IndexStack.pop()];
                    mapOfNums2.put(nums2ValueToBeReplaced, currentValue);
                }
                nums2IndexStack.push(currentIndex);
            }
        }

        // now loop through nums1, look in the map what is value for it and store that at result index
        for(int currentIndex = 0; currentIndex < nums1.length; currentIndex++){
            int currentNums1Value = nums1[currentIndex];
            result[currentIndex] = mapOfNums2.get(currentNums1Value);
        }

        return result;
    }

    private Map<Integer, Integer> getArrayMapped(int[] nums){
        Map<Integer, Integer> map = new HashMap<>();

        for(int num: nums){
            map.put(num, -1);
        }

        return map;
    }

    private void printResultArray(int[] result){
        System.out.print("[");
        for(int i = 0; i < result.length; i++){
            if(i == result.length - 1){
                System.out.print(result[i]);
            } else {
            System.out.print(result[i] + ", ");
            }
        }
        System.out.print("]");
        System.out.println();
    }

    public static void main(String[] args){
        NextGreaterElement nge = new NextGreaterElement();
        nge.printResultArray(nge.nextGreaterElement(new int[]{4,1,2}, new int[]{1,3,4,2}));
        nge.printResultArray(nge.nextGreaterElement(new int[]{2,4}, new int[]{1,2,3,4}));
    }
}
