package src.array_questions;

/*
You are given an integer array nums of even length. You have to split the array into two parts nums1 and nums2 such that:

- nums1.length == nums2.length == nums.length / 2.
- nums1 should contain distinct elements.
- nums2 should also contain distinct elements.

Return true if it is possible to split the array, and false otherwise.

Example 1:

Input: nums = [1,1,2,2,3,4]
Output: true
Explanation: One of the possible ways to split nums is nums1 = [1,2,3] and nums2 = [1,2,4].

Example 2:
Input: nums = [1,1,1,1]
Output: false

Explanation: The only possible way to split nums is nums1 = [1,1] and nums2 = [1,1]. Both nums1 and nums2 do not contain
distinct elements. Therefore, we return false.

Constraints:

1 <= nums.length <= 100
nums.length % 2 == 0
1 <= nums[i] <= 100
*/

/*
Time Complexity: O(N)
Space Complexity: O(N)
 */

import java.util.HashMap;
import java.util.Map;

public class SplitTheArray {
    private boolean isPossibleToSplit(int[] nums) {
        // Logic: Read the constraints carefully and go for defensive programming. First one is that both arrays need to
        // be of equal length. That eliminates odd length input altogether. So now we are only scanning even length
        // input. Second, if any of the input num crosses occurrence count of 2, it's not possible to have 2 unique
        // arrays. Only these 2 conditions are important. It is not important how the input nums are distributed, they
        // just need to be unique in their own sub array

        if(nums == null || nums.length == 0 || (nums.length % 2 != 0)){
            return false;
        }

        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for(int num: nums){
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
            int countOccurrence = frequencyMap.get(num);

            if(countOccurrence > 2){
                // duplicate will land in either sub array
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        SplitTheArray splitTheArray = new SplitTheArray();
        System.out.println(splitTheArray.isPossibleToSplit(new int[]{1,1,2,2,3,4}));
        System.out.println(splitTheArray.isPossibleToSplit(new int[]{1,1,1,1}));
    }
}
