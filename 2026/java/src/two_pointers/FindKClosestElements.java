package src.two_pointers;

/*
Given a sorted integer array arr, two integers k and x, return the k closest integers to x in the array. The result
should also be sorted in ascending order. An integer 'a' is closer to 'x' than an integer 'b' if:

1. |a - x| < |b - x|, or
2. |a - x| == |b - x| and a < b

Example 1:
Input: arr = [1,2,3,4,5], k = 4, x = 3
Output: [1,2,3,4]

Example 2:
Input: arr = [1,1,2,3,4,5], k = 4, x = -1
Output: [1,1,2,3]

Constraints:

1 <= k <= arr.length
1 <= arr.length <= 104
arr is sorted in ascending order.
-104 <= arr[i], x <= 104
 */

import java.util.ArrayList;
import java.util.List;

/*
Time Complexity: 𝒪(𝑁−𝐾).
1. The window starts at size N and shrinks step-by-step down to size K. Each comparison runs in constant 𝑂(1) math time.
 This entirely outperforms your old heap solution.

Space Complexity: 𝒪(1)
1. perfect constant space auxiliary footprint as it strictly updates localized index markers directly on the execution
stack frame.
 */

public class FindKClosestElements {
    public List<Integer> getKClosestElements(int[] arr, int k, int x) {
        List<Integer> resultList = new ArrayList<>();
        if (arr == null || arr.length == 0) {
            return resultList;
        }

        /*
        Since the array is already sorted, we can look at the elements at left and right. Whichever element is farther
        away from x gets eliminated, and that pointer steps inward until the remaining window size equals exactly k.
         */

        /*
        Zero Sorting Overhead: Because you are squeezing an already sorted array from the outside in, the remaining
        elements between left and right are already guaranteed to be sorted. You completely eliminate the
        Collections.sort() step from the tail end of the query pipeline.
         */

        // Two Pointers anchoring the extreme ends of the array timeline
        int left = 0;
        int right = arr.length - 1;

        // Squeeze the window from both ends until exactly 'k' elements remain
        while (right - left >= k) {
            int distanceFromLeft = Math.abs(x - arr[left]);
            int distanceFromRight = Math.abs(x - arr[right]);

            // If the right element is farther away, or if they tie (prefer smaller index)
            // Eliminate the right element by stepping inward
            if (distanceFromLeft < distanceFromRight || distanceFromLeft == distanceFromRight) {
                right--; // Right is farther or loses tie-breaker, discard it
            } else {
                left++;  // Left is strictly farther away, discard it
            }
        }

        // Collect the final k survivors in order (No sorting overhead required!)
        for (int i = left; i <= right; i++) {
            resultList.add(arr[i]);
        }

        return resultList;
    }

    private void printResult(List<Integer> result){
        System.out.print("[");
        for(int num: result){
            System.out.print(num + ", ");
        }
        System.out.print("]");
        System.out.println();
    }

    public static void main(String[] args){
        FindKClosestElements obj = new FindKClosestElements();
        obj.printResult(obj.getKClosestElements(new int[]{1,2,3,4,5}, 4, 3));
        obj.printResult(obj.getKClosestElements(new int[]{1,1,2,3,4,5}, 4, -1));
    }
}
