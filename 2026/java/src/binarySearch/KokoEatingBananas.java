package src.binarySearch;

/*
Koko loves to eat bananas. There are n piles of bananas, the ith pile has piles[i] bananas. The guards have gone and
will come back in h hours. Koko can decide her bananas-per-hour eating speed of k. Each hour, she chooses some pile of
bananas and eats k bananas from that pile. If the pile has less than k bananas, she eats all of them instead and will
not eat any more bananas during this hour. Koko likes to eat slowly but still wants to finish eating all the bananas
before the guards return. Return the minimum integer k such that she can eat all the bananas within h hours.

Example 1:
Input: piles = [3,6,7,11], h = 8
Output: 4

Example 2:
Input: piles = [30,11,23,4,20], h = 5
Output: 30

Example 3:
Input: piles = [30,11,23,4,20], h = 6
Output: 23

Constraints:

1 <= piles.length <= 104
piles.length <= h <= 109
1 <= piles[i] <= 109
 */

/*
Time Complexity: O(N log(max_pile)) where N is the number of boxes/piles. The binary search takes log(max_pile) rounds,
and inside each round, we do a linear scan over the N piles.

Space Complexity = O(1)
 */
public class KokoEatingBananas {
    private int minEatingSpeed(int[] piles, int h){
        // we have been asked to calculate the minimum speed at which KoKo can eat all the piles of bananas and still
        // stay within the limit of 'h' hours.

        // Per hour Koko is eating at last 1 banana. Value at array index = number of bananas in a pile. So by theory,
        // Koko can choose the biggest pile, and if Koko eats at that speed per hour, Koko can finish all the bananas in
        // record low time. But the question for us is to find out a speed which doesn't necessarily needs to be the
        // record speed. It actually needs to be the slowest to finish all bananas within 'h' hours.

        // So we have a range from 1 to biggest number is an array. Binary search doesn't necessarily need sorted array
        // as input. What it actually needs is a range, and a range is always sorted.
        int low = 1;
        int high = getLargestBananaPile(piles);
        int optimalSpeed = high;

        // start binary search
        while(low <= high){
            int mid = low + (high - low) / 2; // avoid integer overflow trap

            // now test this mid
            if(canFinishAllBananaAtThisSpeedWithinTheLimit(piles, mid, h)){
                optimalSpeed = mid;
                // check for a slower speed, i.e, check to the left of the 'mid'
                high = mid - 1;
            } else {
                // check for a higher speed, i.e, check to the right of the 'mid'
                low = mid + 1;
            }
        }

        return optimalSpeed;
    }

    private boolean canFinishAllBananaAtThisSpeedWithinTheLimit(int[] piles, int speed, int limit){
        // divide the pile by speed. If there is any left over that adds as '+ 1' to the quotient/answer of division

        long timeTaken = 0; // use long to avoid integer overflow due to addition

        for(int pile: piles){
            // use integer buffer division trick to append the remainder as an additional batch to the result of normal
            // division Example: pile = 5, speed = 3, timeTaken would be 2. 5 / 3 = 1, Remainder 2 which can be covered
            // in next batch. Now using the buffer trick => (5 + (3 - 1)) / 3 => (5 + 2) / 3 = 7 / 3 => 2, the answer we
            // desire.
            timeTaken += (pile + (speed - 1)) / speed;
        }

        return timeTaken <= limit;
    }

    private int getLargestBananaPile(int[] piles){
        int largestBananaPile = 0;

        for(int pile: piles){
            largestBananaPile = Math.max(largestBananaPile, pile);
        }

        return largestBananaPile;
    }

    public static void main(String[] args) {
        KokoEatingBananas kokoEatingBananas = new KokoEatingBananas();
        System.out.println(kokoEatingBananas.minEatingSpeed(new int[]{3,6,7,11}, 8));
        System.out.println(kokoEatingBananas.minEatingSpeed(new int[]{30,11,23,4,20}, 5));
        System.out.println(kokoEatingBananas.minEatingSpeed(new int[]{30,11,23,4,20}, 6));
    }
}
