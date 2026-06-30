package src.array_questions;

/*
Given an integer array nums, return true if there exists a triple of indices (i, j, k) such that i < j < k and
nums[i] < nums[j] < nums[k]. If no such indices exists, return false.

Example 1:
Input: nums = [1,2,3,4,5]
Output: true
Explanation: Any triplet where i < j < k is valid.

Example 2:
Input: nums = [5,4,3,2,1]
Output: false
Explanation: No triplet exists.

Example 3:
Input: nums = [2,1,5,0,4,6]
Output: true
Explanation: One of the valid triplet is (1, 4, 5), because nums[1] == 1 < nums[4] == 4 < nums[5] == 6.

Constraints:

1 <= nums.length <= 5 * 105
-231 <= nums[i] <= 231 - 1
 */

/*
Time Complexity = O(N)
Space Complexity = O(1)
 */
public class IncreasingTripletSubsequence {
    public boolean increasingTriplet(int[] nums) {
        // Edge Case Validation: A triplet requires at least 3 elements to exist
        if (nums == null || nums.length < 3) {
            return false;
        }

        // Initialize variables to the absolute maximum bounds.
        // These act as sentinel values waiting for real array numbers.
        int smallestFirstCard = Integer.MAX_VALUE;
        int midwaySecondCard = Integer.MAX_VALUE;

        // Perform a single, linear pass across the text data stream
        for (int currentNum : nums) {

            if (currentNum <= smallestFirstCard) {
                // Rule 1: We found a new global minimum card. Update our bottom floor baseline.
                smallestFirstCard = currentNum;

            } else if (currentNum <= midwaySecondCard) {
                // Rule 2: The number beats our baseline, but is smaller than or equal to our middle card.
                // Tighten our mid-range trap window to optimize future checks.
                midwaySecondCard = currentNum;

            } else {
                // Rule 3: The number is strictly greater than BOTH milestones!
                // This means: smallestFirstCard < midwaySecondCard < currentNum. Invariant satisfied!
                return true;
            }
        }

        // Entire array scanned without hitting Rule 3, no triplet exists.
        return false;
    }

    public static void main(String[] args) {
        IncreasingTripletSubsequence increasingTripletSubsequence = new IncreasingTripletSubsequence();
        System.out.println(increasingTripletSubsequence.increasingTriplet(new int[]{5,4,3,2,1}));
        System.out.println(increasingTripletSubsequence.increasingTriplet(new int[]{2,1,5,0,4,6}));
    }
}

