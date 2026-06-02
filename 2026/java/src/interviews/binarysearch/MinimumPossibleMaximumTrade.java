package src.interviews.binarysearch;

/*
ou are a trading algorithm developer at a fintech firm. You’re given the below scenarios and you have to develop an
algorithm that runs in the minimum time possible. You have to do a set of n trades for a given day and each of them
incurs some cost i.e. cost[i]. There is also an option where you are given a budget D discount which you can use to
reduce the cost of trades. This D is for a whole day and can be used for any trades. You need to come up with an
algorithm that determines the minimum possible maximum trade cost that you can achieve for the given set of trades
using the discount budget.

For example, you are given 4 trades for a day with each costing [10,20,29,40] and a discount budget of 35. Now your
algorithm must give the output as 18.

Cost for First trade : 10
Cost for Second trade : 20 becomes 18 (2 of 35 used here. Remaining 33)
Cost for third Trade: 29 becomes 18 (11 of 33 units used here. Remaining 22)
Cost for the last trade : 40 becomes 18 (22 of 22 used. Remaining 0)

Input Format :
	Costs : An array of length n where costs[i] is the cost of ith trade.
D: The discount budget that you are given

Output:
M: the minimum possible max trade value that we can get.

Sample Test Cases
#1
costs = [1, 10, 100, 1000, 10000]
D = 10800
Answer: 100


#2
costs = [100, 200, 300, 400, 500, 600]
D = 1000
Answer: 200

#3
costs = [100,...... 1000 times]  # 1000 trades, each costing 100
D = 1000
Answer: 99
 */

/*
Time Complexity: O(N log(MaxElement)) -> O(N). The outer binary search loop splits our value space in half at most 31
to 32 times (since trade values fit within standard integer bit boundaries). Inside each of those 32 rounds, our helper
validation method completes a clean, single linear scan over the array O(N). This effortlessly computes the optimal
budget cap in real-world linear time.

Space Complexity: O(1) perfect constant space. All execution boundaries and accumulation markers are managed entirely
within localized stack frames without allocating any heap array footprints.
 */

import java.util.Arrays;

// The problem statement is hard to interpret/understand due to vague wordings.
public class MinimumPossibleMaximumTrade {
    private int getMinimumPossibleMaximumTrade(int[] nums, int d){
        // In this question, input array elements are "trade costs". We are given a budget 'd' which we can use to
        // minimize the trade cost. So if you have unlimited budget, then you can give as much discount as you want. So
        // all the trades will be zero. This forms our 'low'. Now let's say we have zero budget, then the maximum trade
        // in the input array becomes the answer. This is our 'high'.

        // Now we need to find a trade cost which is maximum across all the input elements post using the budget
        // allocated. The trick here is that number needs to be as small as possible. So this evens the ground or makes
        // it fair or all the trades to use the budget as per a standard rule. Else we would exhaust the entire budget
        // on a one single trade and would land nowhere since other numbers would be larger.

        // Now what is this standard rule here ? If the trade cost is >= our target, then we reduce the target from our
        // cost. The difference drains the budget. So finally we check whether the budget that we used is <= allocated
        // budget or not.

        int largestTrade = Integer.MIN_VALUE; // zero budget, then the largest trade in the input will be the answer

        for(int num: nums){
            largestTrade = Math.max(largestTrade, num);
        }

        // OPTIMIZATION: Track value-space coordinates using long references
        long low = 0; // unlimited budget, all trades are given full discounts.
        long high = largestTrade;
        long optimalTrade = high;

        // start binary search
        while(low <= high){
            long mid = low + (high - low) / 2; // avoid integer overflow

            // use this 'mid' as target and test on each trade how much discount it needs. Then we check the used
            // discount against allocated one
            if(canUseTargetWithinGivenBudget(nums, mid, d)){
                optimalTrade = mid;
                // try a smaller value, which is left of 'mid'
                high = mid - 1;
            } else {
                // we are over budget, try a larger target, which will be right of 'mid'
                low = mid + 1;
            }
        }

        return (int) optimalTrade;
    }

    private boolean canUseTargetWithinGivenBudget(int[] nums, long target, int allocatedBudget){
        // Use long to guard against integer sum accumulation overflows
        long budgetUsed = 0; // we haven't started yet

        for(int num: nums){
            if(num > target){
                // Reduce the target from num
                long discountUsed = num - target;
                budgetUsed += discountUsed;
            }
            // we don't do anything for trades that are smaller than our target, as there is no point of using discount
        }

        return budgetUsed <= allocatedBudget;
    }

    public static void main(String[] args){
        MinimumPossibleMaximumTrade obj = new MinimumPossibleMaximumTrade();
        System.out.println(obj.getMinimumPossibleMaximumTrade(new int[]{10,20,29,40}, 35));
        System.out.println(obj.getMinimumPossibleMaximumTrade(new int[]{1, 10, 100, 1000, 10000}, 10800));
        System.out.println(obj.getMinimumPossibleMaximumTrade(new int[]{100, 200, 300, 400, 500, 600}, 1000));

        int[] input4 = new int[1000];
        Arrays.fill(input4, 100);

        System.out.println(obj.getMinimumPossibleMaximumTrade(input4, 1000));
    }
}
