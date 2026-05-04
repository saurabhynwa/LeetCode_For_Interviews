package src.hashmap;

/* Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.
You may assume that each input would have exactly one solution, and you may not use the same element twice.

You can return the answer in any order.

Example 1:

Input: nums = [2,7,11,15], target = 9
Output: [0,1]
Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].
Example 2:

Input: nums = [3,2,4], target = 6
Output: [1,2]
Example 3:

Input: nums = [3,3], target = 6
Output: [0,1]

Constraints:

2 <= nums.length <= 104
-109 <= nums[i] <= 109
-109 <= target <= 109
Only one valid answer exists.
*
* */
import java.util.Map;
import java.util.HashMap;

// Input = Non-sorted array. So you cannot use binary search or two pointers
public class TwoSum {
    private int[] twoSum(int[] nums, int target){
        //sanity cases
        if(nums == null || nums.length <= 1){
            //throw IllegalArgumentException
            throw new IllegalArgumentException("Input array cannot be null and should have minimum of 2 elements");
            // for LeetCode you can return an empty array: return new int[0];
        }

        /*Logic: Input array in not sorted. But it is given that all the elements are integers and positive. So we will
        * walk over the array from start and using the a + b = target formula figure out the indices of a & b. We will
        * use HashMap to store the array[index] as key and the index itself as the value. While looping we check whether
        * target - array[index] exists in our HashMap. If yes, we have the answer, else we insert into HashMap.
        *
        * Time Complexity = O(n) | Space Complexity = O(n)*/
        int[] indices = new int[2];
        Map<Integer, Integer> targetMap = new HashMap<>();

        // Loop over the input array
        for(int index = 0; index < nums.length; index++){
            int searchValue = target - nums[index];

            //check in HashMap for presence
            if(targetMap.containsKey(searchValue)){
                indices[0] = targetMap.get(searchValue);
                indices[1] = index;
                return indices; //because exactly one solution exists
            }
            //insert entry into HashMap
            targetMap.put(nums[index], index);
        }
        return indices;
    }

    private void printResult(int[] result){
        if(result == null || result.length != 2){
            throw new IllegalArgumentException("result array cannot be null, empty, have less than 2 elements or more than 2 elements !");
        }

        System.out.println("Indices = {" + result[0] + ", " + result[1] + "}");
    }

    public static void main(String[] args){
        TwoSum obj = new TwoSum();

        int[] nums1 = {2,7,11,15};
        int target1 = 9;
        int[] result1 = obj.twoSum(nums1, target1);
        obj.printResult(result1);

        int[] nums2 = {3,2,4};
        int target2 = 6;
        int[] result2 = obj.twoSum(nums2, target2);
        obj.printResult(result2);

        int[] nums3 = {3,3};
        int target3 = 6;
        int[] result3 = obj.twoSum(nums3, target3);
        obj.printResult(result3);
    }
}
