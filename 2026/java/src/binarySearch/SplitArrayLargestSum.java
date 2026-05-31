package src.binarySearch;

/*
Given an integer array nums and an integer k, split nums into k non-empty subarrays such that the largest sum of any
subarray is minimized. Return the minimized largest sum of the split. A subarray is a contiguous part of the array.

Example 1:
Input: nums = [7,2,5,10,8], k = 2
Output: 18
Explanation: There are four ways to split nums into two subarrays.
The best way is to split it into [7,2,5] and [10,8], where the largest sum among the two subarrays is only 18.

Example 2:
Input: nums = [1,2,3,4,5], k = 2
Output: 9
Explanation: There are four ways to split nums into two subarrays.
The best way is to split it into [1,2,3] and [4,5], where the largest sum among the two subarrays is only 9.

Constraints:

1 <= nums.length <= 1000
0 <= nums[i] <= 106
1 <= k <= min(50, nums.length)
 */

/*
Time Complexity: O(N log(Sum - Max)) where N is the length of nums. The term log(Sum - Max) represents the mathematical
size of our binary search range. 'N' is nums length which we iterate once to find max and sum. And the rest part is the
binary search range complexity. Usually we start our binary search normally on the sorted array or it's indexes, hence
there it is log N. But here, to start binary search we need to first get the low and high range. Hence, the Time
complexity of the binary search in this solution is log(Sum - Max) => log (high - low).

The value range space is split in half at most 32 times (32 comes directly from the maximum number of bits used to store
a standard integer in computer memory, which directly sets a hard ceiling on how many times a binary search loop can
possibly run). In Java, an int is a 32-bit signed integer. This means it can hold a maximum value of exactly
2,147,483,647 roughly ((2 ^ 31) - 1). Our greedy packing helper processes the array in a clean linear pass per iteration.

Space Complexity: O(1) perfect constant space. All variables are held directly within localized activation frames on the
stack.
 */

// Recall the item packing and container analogy !
public class SplitArrayLargestSum {
    private int splitArray(int[] nums, int k){
        // the question asks us to find a minimum largest sum of the split. What it means is we have to split the given
        // input array into 'k' "contiguous parts" such that the sum of either of split is large enough to cover sum of
        // all the individual contiguous parts but at the same time that number should be as small as possible.

        // In simple words, try to visualize the situation as item packing. We are give a bunch of items that are on a
        // conveyor belt (simulates Array analogy, we pick first and then go to second). Now we are given 'k' number of
        // containers or bags to pack these items. We need to find a minimum weight of the bag that can accommodate all
        // the items in 'k' containers.

        // Now if you look closely, the minimum weight of the bag has to be equal to the heaviest/largest item on the
        // belt. If not you cannot pack that item even in an empty container or bag. Now the maximum weight of a
        // bag/container can be the sum of all the array elements. In that way all the items on the belt can be packed
        // into a single container.

        // So we have a lower value and a higher value. And we are supposed to find a minimum value between this range
        // which allows us to stay within the 'k' number of containers and yet get all the items packed. Binary search !

        // Logic: We have range, we take mid and use it as our target. Now we take a bag and start packing it until it
        // breaches our target limit. If that happens, we seal that bag/container, get a new container and start packing
        // from the item we are on. At the end we see if the number of containers we used are within the limit given, it
        // means that our 'target (mid)' is a potential answer. So we tighten the space and start looking for even a
        // smaller target. And we will have our answer.

        long largestNum = Integer.MIN_VALUE;
        long sumOfNums = 0; // using long to avoid integer overflows

        for(int num: nums){
            largestNum = Math.max(largestNum, num);
            sumOfNums += num;
        }

        long low = largestNum;
        long high = sumOfNums;
        long optimalWeight = high;

        // binary search
        while(low <= high){
            long mid = low + (high - low) / 2; // to avoid integer overflow

            // now let's use this 'mid' as target and see if we can accommodate the 'k' contiguous split's individual
            // sum inside the 'k' containers
            if(canFitAllContainersUsingTargetWithinKContainers(nums, mid, k)){
                // we have a potential answer. Record it !
                optimalWeight = mid;
                // let's find a smaller weight, which will be left of 'mid'
                high = mid - 1;
            } else {
                // we need a larger container weight, which means to the right of 'mid'
                low = mid + 1;
            }
        }

        return (int) optimalWeight;
    }

    private boolean canFitAllContainersUsingTargetWithinKContainers(int[] nums, long target, int allowedContainers){
        int containersUsed = 1; // we need at least 1 container to start packing
        long containerWeight = 0; // unless we start packing, the container is empty

        // Let's start packing
        for(int num: nums){
            if(containerWeight + num <= target){
                // keep packing since we haven't breached the container weight limit
                containerWeight += num;
            } else {
                // container weight limit breached
                // pack the current container and move it aside.
                containersUsed++;
                // take a new container and resume packing
                containerWeight = num;
            }
        }

        return containersUsed <= allowedContainers;
    }

    public static void main(String[] args){
        SplitArrayLargestSum obj = new SplitArrayLargestSum();
        System.out.println(obj.splitArray(new int[]{7,2,5,10,8}, 2));
        System.out.println(obj.splitArray(new int[]{1,2,3,4,5}, 2));
    }
}
