package src.dynamic_programming;

/*
You are given an integer array coins representing coins of different denominations and an integer amount representing a
total amount of money. Return the fewest number of coins that you need to make up that amount. If that amount of money
cannot be made up by any combination of the coins, return -1. You may assume that you have an infinite number of each
kind of coin.

Example 1:
Input: coins = [1,2,5], amount = 11
Output: 3
Explanation: 11 = 5 + 5 + 1

Example 2:
Input: coins = [2], amount = 3
Output: -1

Example 3:
Input: coins = [1], amount = 0
Output: 0

Constraints:

1 <= coins.length <= 12
1 <= coins[i] <= 231 - 1
0 <= amount <= 104
 */

import java.util.Arrays;
/*
Time Complexity: O(A x C) where A is the target amount and C is the total number of distinct coin types. The nested
loops run exactly A × C times. Inside the inner block, everything runs in O(1) constant time, making this vastly
superior to an un-cached recursive execution path.

Space Complexity: O(A). The memory allocation is bounded strictly by our single primitive integer array minCoinsLedger
of size amount + 1, leaving zero garbage collection pressure on the JVM heap.
 */
public class CoinChange {
    public int coinChange(int[] coins, int amount) {
        if(coins == null || coins.length == 0 || amount < 0){
            return -1;
        }

        if(amount == 0){
            return 0;
        }

        // bottom up tabulation DP. We will maintain a ledger for amount starting from 1 to the target amount which
        // tells the minimum coins required to build that amount.

        // length is amount + 1, as we are treating the index of this ledger as the amount. Array
        // index are zero based. dp[i] stores the minimum number of coins needed to make up sub-amount 'i'.
        int[] minCoinsLedger = new int[amount + 1];

        // Fill the ledger with an impossible sentinel value representing "Unreachable State". We use 'amount + 1'
        // instead of Integer.MAX_VALUE to prevent arithmetic integer overflow bugs when we add 1 to a lookup cell
        // inside our loops.
        int unreachableSentinel = amount + 1;
        Arrays.fill(minCoinsLedger, unreachableSentinel);

        // base case: Making an amount of 0 always costs exactly 0 coins.
        minCoinsLedger[0] = 0;

        for(int activeAmount = 1; activeAmount <= amount; activeAmount++){
            // loop over coins
            for(int currentCoin: coins){
                // guardrail check
                if(currentCoin <= activeAmount){
                    int remainingBalanceLookupIndex = activeAmount - currentCoin;
                    // now what this means is for the current active amount we have used 1 coin. And the
                    // difference that we get, we will use that as the key to look in our minCoinsLedger
                    // how many minimum coins where needed to build that delta amount.
                    int historicalMinCoinsForDelta = minCoinsLedger[remainingBalanceLookupIndex];

                    // "+1" represents the current coin used to derive the delta
                    minCoinsLedger[activeAmount] = Math.min(minCoinsLedger[activeAmount], 1 + historicalMinCoinsForDelta);
                }
            }
        }

        return minCoinsLedger[amount] == unreachableSentinel ? -1 : minCoinsLedger[amount];
    }

    public static void main() {
        CoinChange coinChange = new CoinChange();
        System.out.println(coinChange.coinChange(new int[]{1,2,5}, 11));
    }
}
