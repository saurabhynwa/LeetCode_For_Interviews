package src.dynamic_programming;

/*
Given an integer array nums, return the length of the longest strictly increasing subsequence.

Example 1:
Input: nums = [10,9,2,5,3,7,101,18]
Output: 4

Explanation: The longest increasing subsequence is [2,3,7,101], therefore the length is 4.
Example 2:
Input: nums = [0,1,0,3,2,3]
Output: 4

Example 3:
Input: nums = [7,7,7,7,7,7,7]
Output: 1

Constraints:

1 <= nums.length <= 2500
-104 <= nums[i] <= 104

 */

/*
Time Complexity = O(N log N)
Space Complexity = O(N)
 */


public class LongestIncreasingSubsequence {
    private int lengthOfLIS(int[] nums) {
        if(nums == null || nums.length == 0){
            return 0;
        }

        // card decks analogy. 2 rules. Bigger card, straight away append to the deck in hand
        // smaller card, detect the first position where it can be inserted. This is a
        // replace operation. Before replace the length of sequence in hand is maintained in
        // a variable.

        // declare a array of size nums which will act as the increasing sequence. Worst case
        // nums is sorted, then this array will have all elements from nums. Initially it will
        // have zero (garbage value)
        int[] deckInHand = new int[nums.length];
        int activeSequenceLength = 0;

        for (int num : nums) {
            // as per the second rule, the deckInHand is sorted, so we can run a binary search
            // to locate the index position where the current num needs to be inserted
            int insertIndex = getInsertIndex(deckInHand, activeSequenceLength, num);

            deckInHand[insertIndex] = num;

            // if the value is inserted in the result array at the end, then we need to increase
            // the activeSequence by 1 (index is zero based)
            if (insertIndex == activeSequenceLength) {
                activeSequenceLength++;
            }
        }

        return activeSequenceLength;
    }

    private int getInsertIndex(int[] targetArr, int activeSequenceLength, int target){
        int leftPointer = 0; // sorted array, we always start from left most
        int rightPointer = activeSequenceLength - 1; // we are binary searching on index, hence -1
        // so that we don't include the garbage zero values initiated at the start

        while(leftPointer <= rightPointer){
            int mid = leftPointer + (rightPointer - leftPointer) / 2; // to avoid integer overflow

            if(targetArr[mid] == target){
                return mid;
            } else if(targetArr[mid] < target){
                // search right of mid
                leftPointer = mid + 1;
            } else {
                // search left of mid
                rightPointer = mid - 1;
            }
        }

        return leftPointer;
    }

    public static void main(String[] args) {
        LongestIncreasingSubsequence obj = new LongestIncreasingSubsequence();
        System.out.println(obj.lengthOfLIS(new int[]{10,9,2,5,3,7,101,18}));
    }
}
