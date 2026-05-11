package src.slidingWindow;

import java.util.Map;
import java.util.HashMap;

/*
You are given an integer array nums and an integer k. Find the maximum subarray sum of all the subarrays of nums that
meet the following conditions:

The length of the subarray is k, and All the elements of the subarray are distinct. Return the maximum subarray sum of
all the subarrays that meet the conditions. If no subarray meets the conditions, return 0.

A subarray is a contiguous non-empty sequence of elements within an array.

Example 1:

Input: nums = [1,5,4,2,9,9,9], k = 3
Output: 15
Explanation: The subarrays of nums with length 3 are:
- [1,5,4] which meets the requirements and has a sum of 10.
- [5,4,2] which meets the requirements and has a sum of 11.
- [4,2,9] which meets the requirements and has a sum of 15.
- [2,9,9] which does not meet the requirements because the element 9 is repeated.
- [9,9,9] which does not meet the requirements because the element 9 is repeated.
We return 15 because it is the maximum subarray sum of all the subarrays that meet the conditions
Example 2:

Input: nums = [4,4,4], k = 3
Output: 0
Explanation: The subarrays of nums with length 3 are:
- [4,4,4] which does not meet the requirements because the element 4 is repeated.
We return 0 because no subarrays meet the conditions.

Constraints:

1 <= k <= nums.length <= 105
1 <= nums[i] <= 105
 */

/*
Time Complexity: O(n) Linear scan; each element is processed twice (once by end, once by start).
Space Complexity: (O(k)) The HashMap scales with the size of the window, not the entire array.
 */

public class MaxSumOfDistinctSubArrayOfLengthK {
    private long maximumSubarraySum(int[] nums, int k){
        // sanity check
        if(nums == null || nums.length < k || k < 1){
            return 0;
        }

        // Logic: Sliding window. Key part is for duplicate detection in the window. Maintain a HashMap with array
        // element as the key and count as value. Whenever a window slides, update the HashMap as well. If the HashMap
        // has same number of keys as 'k' then all the elements in the window are distinct and we can do maxSum
        // comparison with windowSum
        long maxSum = 0;
        long windowSum = 0;
        Map<Integer, Integer> windowMap = new HashMap<>();

        for(int start = 0, end = 0; end < nums.length; end++){
            windowSum += nums[end];

            // add current array element to HashMap. If already exists increment the count else insert with value 1
            windowMap.put(nums[end], windowMap.getOrDefault(nums[end], 0) + 1);

            // window check
            if(end - start + 1 == k){
                // check if all elements in the window are unique
                if(windowMap.size() == k){
                    maxSum = Math.max(maxSum, windowSum);
                }
                // remove start element of the current window from the window sum in order for the window to slide
                windowSum -= nums[start];

                // update the windowMap as well. If value is zero after decrementing the array element, remove the key
                // itself from the windowMap
                int value = windowMap.get(nums[start]) - 1;

                if(value == 0){
                    // remove key from Map
                    windowMap.remove(nums[start]);
                } else {
                    // update the map by overwriting the key and value config
                    windowMap.put(nums[start], value);
                }

                // increment 'start' to next element for our window to slide
                start++;
            }
        }

        return maxSum;
    }

    static void main(){
        MaxSumOfDistinctSubArrayOfLengthK maxSumOfDistinctSubArrayOfLengthK = new MaxSumOfDistinctSubArrayOfLengthK();
        System.out.println(maxSumOfDistinctSubArrayOfLengthK.maximumSubarraySum(new int[]{1,1,1,7,8,9}, 3));
        System.out.println(maxSumOfDistinctSubArrayOfLengthK.maximumSubarraySum(new int[]{5,3,3,1,1}, 3));
        System.out.println(maxSumOfDistinctSubArrayOfLengthK.maximumSubarraySum(new int[]{1,2,2}, 2));
        System.out.println(maxSumOfDistinctSubArrayOfLengthK.maximumSubarraySum(new int[]{9,9,9,1,2,3}, 3));
        System.out.println(maxSumOfDistinctSubArrayOfLengthK.maximumSubarraySum(new int[]{1,2,2}, 2));
        System.out.println(maxSumOfDistinctSubArrayOfLengthK.maximumSubarraySum(new int[]{1,5,4,2,9,9,9}, 3));
        System.out.println(maxSumOfDistinctSubArrayOfLengthK.maximumSubarraySum(new int[]{4,4,4}, 3));
    }
}
