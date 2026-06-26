package src.array_questions;

/*
Given an unsorted array of integers nums, return the length of the longest consecutive elements sequence. You must write
an algorithm that runs in O(n) time.

Example 1:

Input: nums = [100,4,200,1,3,2]
Output: 4
Explanation: The longest consecutive elements sequence is [1, 2, 3, 4]. Therefore its length is 4.
Example 2:

Input: nums = [0,3,7,2,5,8,4,6,0,1]
Output: 9
Example 3:

Input: nums = [1,0,1,2]
Output: 3


Constraints:

0 <= nums.length <= 105
-109 <= nums[i] <= 109
 */

import java.util.HashSet;
import java.util.Set;

/*
Time Complexity: O(N). 2 pass, but still amortized O(N)

Space Complexity: O(N), worst case all the input elements are unique.
 */

public class LongestConsecutiveSequence {
    private int getLongestConsecutiveSequenceLength(int[] nums){
        int longestConsecutiveSequenceLength = 0;

        // edge case
        if(nums == null || nums.length == 0){
            return longestConsecutiveSequenceLength;
        }

        //Logic: loop over nums and build hashset. Now we loop over this hashset. For our current num, we check whether
        // there exists a previous element in the hashset. If it does, then we move on from the current num as we are
        // anyway going to come across that smaller num through set iteration. This way for any potential sequence, we
        // only scan once and that too forward. Now let's say if the current num doesn't have any previous number, then
        // this current num is the start of a potential sequence, you scan forward, update current sequence length, and
        // update the current num for next search. When it breaks out of while loop you compare with max length.

        Set<Integer> numSet = new HashSet<>();

        for(int num: nums){
            numSet.add(num);
        }

        // loop over set
        for(int num: numSet){
            // check if the current num has a previous or not in the set. Only when it doesn't we start a forward scan.
            if(!numSet.contains(num - 1)){
                int currentSequenceLength = 1; // the num itself
                int currentNum = num; // don't manipulate the input num. It breaks the loop iteration logic and you will
                // end up with wrong answer

                // start forward scan
                while(numSet.contains(currentNum + 1)){
                    currentSequenceLength++;
                    currentNum += 1;
                }

                longestConsecutiveSequenceLength = Math.max(longestConsecutiveSequenceLength, currentSequenceLength);
            }
        }

        return longestConsecutiveSequenceLength;
    }

    public static void main(String[] args) {
        LongestConsecutiveSequence longestConsecutiveSequence = new LongestConsecutiveSequence();
        System.out.println(longestConsecutiveSequence.getLongestConsecutiveSequenceLength(new int[]{100,4,200,1,3,2}));
        System.out.println(longestConsecutiveSequence.getLongestConsecutiveSequenceLength(new int[]{0,3,7,2,5,8,4,6,0,1}));
        System.out.println(longestConsecutiveSequence.getLongestConsecutiveSequenceLength(new int[]{1,0,1,2}));
    }
}
