package src.binarySearch;

/*
Given an array of integers nums and an integer threshold, we will choose a positive integer divisor, divide all the
array by it, and sum the division's result. Find the smallest divisor such that the result mentioned above is less than
or equal to threshold. Each result of the division is rounded to the nearest integer greater than or equal to that
element. (For example: 7/3 = 3 and 10/2 = 5). The test cases are generated so that there will be an answer.

Example 1:
Input: nums = [1,2,5,9], threshold = 6
Output: 5
Explanation: We can get a sum to 17 (1+2+5+9) if the divisor is 1.
If the divisor is 4 we can get a sum of 7 (1+1+2+3) and if the divisor is 5 the sum will be 5 (1+1+1+2).

Example 2:
Input: nums = [44,22,33,11,1], threshold = 5
Output: 44

Constraints:

1 <= nums.length <= 5 * 104
1 <= nums[i] <= 106
nums.length <= threshold <= 106
 */

/*
Time Complexity: O(N log(LargestNum)) -> O(N)
The outer binary search window is halved exactly 31 to 32 times based on integer bit space limitations. Inside each
round, your helper method runs a linear pass over the N items in the array, completing the search in real-world linear
time.

Space Complexity: O(1) perfect constant space. All boundary markers and loop references live cleanly within localized
activation frames on the stack.
 */
public class SmallestDivisorForGivenThreshold {
    private int getSmallestDivisorWithinThreshold(int[] nums, int threshold){
        // problem asks us to find a divisor for all the input elements, such that the sum of their individual divisions
        // will be smaller or equal to the threshold. The smallest divisor would be 1 and the largest divisor would be
        // the largest element in the array. One trick here is to use the buffer division technique we learnt in Koko
        // eating banana problem: (numerator + (denominator - 1)) / denominator, which takes into account the remainder
        // as well.

        // so if your threshold is really high then the divisor will be towards the lower side. If threshold is small,
        // then the divisor will be on higher side as the sum of the division would need to be <= threshold

        int largestNum = Integer.MIN_VALUE;

        for(int num: nums){
            largestNum = Math.max(largestNum, num);
        }

        int low = 1; // smallest positive divisor possible
        int high = largestNum;
        int optimalSmallestDivisorWithinThreshold = high;

        // start binary search
        while(low <= high){
            int mid = low + (high - low) / 2;

            // check this 'mid' as the target
            if(canDivideUsingTargetWithinThreshold(nums, mid, threshold)){
                optimalSmallestDivisorWithinThreshold = mid;
                // now look for even a smaller value, which is to the left of 'mid'
                high = mid - 1;
            } else {
                // look for a larger value, which is to the right of 'mid'
                low = mid + 1;
            }
        }

        return optimalSmallestDivisorWithinThreshold;
    }

    private boolean canDivideUsingTargetWithinThreshold(int[] nums, int target, int threshold){
        long sumOfDivisions = 0; // haven't started dividing yet. Also, use long to avoid integer sum overflows

        for(int num: nums){
            // use buffer integer division technique that counts for the remainder as well in the division result
            // formula: (numerator + (denominator - 1)) / denominator
            sumOfDivisions += (num + (target - 1)) / target;
        }

        return sumOfDivisions <= threshold;
    }

    public static void main(String[] args){
        SmallestDivisorForGivenThreshold obj = new SmallestDivisorForGivenThreshold();
        System.out.println(obj.getSmallestDivisorWithinThreshold(new int[]{1,2,5,9}, 6));
        System.out.println(obj.getSmallestDivisorWithinThreshold(new int[]{44,22,33,11,1}, 5));
    }
}
