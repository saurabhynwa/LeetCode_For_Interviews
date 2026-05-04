package src.two_pointers;

/*
Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]] such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.

Notice that the solution set must not contain duplicate triplets.

Example 1:

Input: nums = [-1,0,1,2,-1,-4]
Output: [[-1,-1,2],[-1,0,1]]
Explanation:
nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0.
nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0.
nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0.
The distinct triplets are [-1,0,1] and [-1,-1,2].
Notice that the order of the output and the order of the triplets does not matter.
Example 2:

Input: nums = [0,1,1]
Output: []
Explanation: The only possible triplet does not sum up to 0.
Example 3:

Input: nums = [0,0,0]
Output: [[0,0,0]]
Explanation: The only possible triplet sums up to 0.


Constraints:

3 <= nums.length <= 3000
-105 <= nums[i] <= 105
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/*
Time Complexity = O(n ^ 2)
    - outer loop O(n)
    - Two-pointer scan per 'i' O(n)
    - Sorting O(n log n)

 Space Complexity (for output storing triplets) = O(n ^ 2)
 */
public class ThreeSum {
    private List<List<Integer>> threeSum(int[] nums){
        //sanity check
        if(nums == null || nums.length <= 2){
            return new ArrayList<>();
        }

        //initialize return data type
        List<List<Integer>> uniqueTriplets = new ArrayList<>();

        //logic: first we need to sort the array, only then it's possible to solve O(n ^ 2)
        Arrays.sort(nums); //important step

        //post sorting use two pointer approach
        for(int i = 0; i < nums.length - 2; i++){
            int left = i + 1;
            int right = nums.length - 1;

            if(nums[i] > 0){
                break;//no zero sum possible, stop processing
            }

            if(i > 0 && nums[i] == nums[i-1]){
                continue; //we have already found the triplets
            }

            while(left < right){
                int sum = nums[i] + nums[left] + nums[right];
                if(sum == 0){
                    //add triple
                    List<Integer> triplet = new ArrayList<>();
                    triplet.add(nums[i]);
                    triplet.add(nums[left]);
                    triplet.add(nums[right]);
                    uniqueTriplets.add(triplet);

                    //keep i stable, and check whether the next element for left and right are same or not.
                    //if same we need to move them
                    while(left < right && nums[left] == nums[left+1]){
                        left++;
                    }

                    while(left < right && nums[right] == nums[right-1]){
                        right--;
                    }

                    left++; //new value for left pointer which it hasn't seen for the current 'i'
                    right--; //new value for right pointer which it hasn't seen for the current 'i'
                } else if(sum < 0){
                    left++; //for existing 'i' move to the next value
                } else {
                    right--; //for existing 'i' move to the next value
                }
            }
        }

        return uniqueTriplets;
    }

    static void main(){
        ThreeSum threeSum = new ThreeSum();
        List<List<Integer>> result = threeSum.threeSum(new int[]{-1,0,1,2,-1,-4});

        for(List<Integer> triplet: result){
            System.out.println(triplet);
        }
    }
}
