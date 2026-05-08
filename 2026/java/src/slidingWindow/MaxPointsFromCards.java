package src.slidingWindow;

/*
There are several cards arranged in a row, and each card has an associated number of points. The points are given in
the integer array cardPoints. In one step, you can take one card from the beginning or from the end of the row. You
have to take exactly k cards.

Your score is the sum of the points of the cards you have taken.

Given the integer array cardPoints and the integer k, return the maximum score you can obtain.



Example 1:

Input: cardPoints = [1,2,3,4,5,6,1], k = 3
Output: 12
Explanation: After the first step, your score will always be 1. However, choosing the rightmost card first will maximize
your total score. The optimal strategy is to take the three cards on the right, giving a final score of 1 + 6 + 5 = 12.
Example 2:

Input: cardPoints = [2,2,2], k = 2
Output: 4
Explanation: Regardless of which two cards you take, your score will always be 4.
Example 3:

Input: cardPoints = [9,7,7,9,7,7,9], k = 7
Output: 55
Explanation: You have to take all the cards. Your score is the sum of points of all cards.


Constraints:

1 <= cardPoints.length <= 105
1 <= cardPoints[i] <= 104
1 <= k <= cardPoints.length
 */

import java.util.ArrayList;
import java.util.List;

/*
Time Complexity = 0(n)
Space Complexity = 0(1)
 */
public class MaxPointsFromCards {
    public int[] getMaxPointsFromCards(int[] cardPoints, int k){
        // sanity check
        if(cardPoints == null || cardPoints.length < k || k < 1){
            return new int[]{};
        }

        // Logic: Sliding window. We can pick only one card from start or end in one turn. So if you look carefully
        // there will be a continuous group of cards that don't get picked. This group is our sliding window. We take
        // the sum of all the cards and calculate for each combination of 'k' cards to be picked what will the max sum

        // If we reduce the sum of our nonPickedCardsWindow from the totalSum that will give us the maxPoints we can get
        // from the cards picked. So lesser the nonPickedCardsWindow sum, greater will be the score we want of the
        // picked combination.

        // BONUS: We can also maintain the start and end indexes of the nonPickedCardsWindow and use them with that
        // indexes of the given cards. The indexes that are not in the range of the start and end index are actually the
        // ones that have been picked.

        int totalSum = 0;

        for(int card: cardPoints){
            totalSum += card;
        }

        if(k == cardPoints.length){
            // it means all the cards have to be picked, so simply return the sum
            return new int[]{totalSum, 0, 0};
        }

        int start = 0;
        int end = 0;
        int nonPickedWindowLength = cardPoints.length - k;
        int nonPickedWindowSum = 0;
        int maxPickedSum = 0;
        int finalNonPickedStart = 0;
        int finalNonPickedEnd = 0;

        while(end < cardPoints.length){
            nonPickedWindowSum += cardPoints[end];

            // check for window
            if(end - start + 1 == nonPickedWindowLength){
               int runningPickedSum = totalSum - nonPickedWindowSum;

               // check if runningPickedSum is max
                if(runningPickedSum >= maxPickedSum){
                    maxPickedSum = runningPickedSum;
                    finalNonPickedStart = start;
                    finalNonPickedEnd = end;
                }

                // remove the start value from the window so that we can slide it
                nonPickedWindowSum -= cardPoints[start];

                // move start for the new window
                start++;
            }

            end++;
        }

        return new int[]{maxPickedSum, finalNonPickedStart, finalNonPickedEnd};
    }

    private List<Integer> getPickedCardsIndexes(int[] result, int cardPointsLength){
        // first element of the result array is the maxPickedSum
        // second and third element are the start and end index of the non-picked window;
        List<Integer> pickedCardIndices = new ArrayList<>();
        int nonPickedStart = result[1];
        int nonPickedEnd = result[2];

        for(int i = 0; i < cardPointsLength; i++){
            if(i < nonPickedStart || i > nonPickedEnd){
                pickedCardIndices.add(i);
            }
        }
        return pickedCardIndices;
    }

    private void printResult(List<Integer> pickedCardIndices, int[] cardPoints){
        System.out.print("Picked Card Indices: ");

        for(int pickedCardIndex: pickedCardIndices){
            System.out.print(pickedCardIndex + ", ");
        }

        System.out.println();
        System.out.print("Picked Card Values: ");

        for(int pickedCardIndex: pickedCardIndices){
            System.out.print(cardPoints[pickedCardIndex] + ", ");
        }
        System.out.println();
    }

    private void printMaxScore(int maxScore){
        System.out.println("Max Score = " + maxScore);
    }

    static void main() {
        MaxPointsFromCards maxPointsFromCards = new MaxPointsFromCards();
        int[] cardPoints1 = new int[]{1,2,3,4,5,6,1};
        int[] result1 = maxPointsFromCards.getMaxPointsFromCards(cardPoints1, 3);
        maxPointsFromCards.printMaxScore(result1[0]);
        maxPointsFromCards.printResult(maxPointsFromCards.getPickedCardsIndexes(result1, cardPoints1.length), cardPoints1);
        System.out.println();

        int[] cardPoints2 = new int[]{2,2,2};
        int[] result2 = maxPointsFromCards.getMaxPointsFromCards(cardPoints2, 2);
        maxPointsFromCards.printMaxScore(result2[0]);
        maxPointsFromCards.printResult(maxPointsFromCards.getPickedCardsIndexes(result2, cardPoints2.length), cardPoints2);
        System.out.println();

        int[] cardPoints3 = new int[]{9,7,7,9,7,7,9};
        int[] result3 = maxPointsFromCards.getMaxPointsFromCards(cardPoints3, 7);
        maxPointsFromCards.printMaxScore(result3[0]);
        maxPointsFromCards.printResult(maxPointsFromCards.getPickedCardsIndexes(result3, cardPoints3.length), cardPoints3);
        System.out.println();

        int[] cardPoints4 = new int[]{11,49,100,20,86,29,72};
        int[] result4 = maxPointsFromCards.getMaxPointsFromCards(cardPoints4, 4);
        maxPointsFromCards.printMaxScore(result4[0]);
        maxPointsFromCards.printResult(maxPointsFromCards.getPickedCardsIndexes(result4, cardPoints4.length), cardPoints4);
        System.out.println();
    }
}
