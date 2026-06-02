package src.binarySearch;

/*
You're a logistics manager preparing to ship products from a warehouse. Each product type has both a quantity and a
weight per item. Shipping boxes have TWO constraints:

1) Capacity limit: Maximum number of items per box
2) Weight limit: Maximum weight (kg) per box

Rules:

1. Each product type must be packed separately
2. All boxes have the same capacity and weight limits
3. A box can hold at most capacity items OR maxWeightPerBox kg, whichever comes first
4. Items must be whole numbers (can't pack fractional items)

Given arrays of quantities and weights per item, plus box and weight constraints, find the minimum box capacity needed
to ship all products.

Example 1:

Input:
quantities = [8, 12, 5]
weights = [2, 3, 1]  # kg per item
maxBoxes = 6
maxWeightPerBox = 20  # kg
Output: 5

Explanation: With capacity 5:

Product 0 (8 items @ 2kg): min(5, 10, 8) = 5 items/box → needs 2 boxes (5 + 3 items)
Product 1 (12 items @ 3kg): min(5, 6, 12) = 5 items/box → needs 3 boxes (5 + 5 + 2 items)
Product 2 (5 items @ 1kg): min(5, 20, 5) = 5 items/box → needs 1 box Total: 6 boxes ≤ 6 ✓
Example 2:

Input:

quantities = [10, 15, 8]
weights = [5, 2, 3]
maxBoxes = 10
maxWeightPerBox = 15
Output: 4

Explanation: With capacity 4:

Product 0 (10 items @ 5kg): min(4, ⌊15/5⌋, remaining) = 3 items/box → needs 4 boxes
Product 1 (15 items @ 2kg): min(4, ⌊15/2⌋, remaining) = 4 items/box → needs 4 boxes
Product 2 (8 items @ 3kg): min(4, ⌊15/3⌋, remaining) = 4 items/box → needs 2 boxes Total: 10 boxes ≤ 10 ✓
 */

/*
Time Complexity: O(N log(MaxQuantity)) -> O(N). The outer value-space binary search splits our quantity range in half
at most 31 to 32 times (since maximum quantities conform to standard integer space limits). Inside each round, our
helper method executes a clean, linear pass over the N product types. This calculates the optimal multi-constraint
bottleneck in real-world linear time.

Space Complexity: O(1) perfect constant space. All boundary markers and accumulation tokens live cleanly within
localized stack activation frames.
 */

// Hard problem: 2 constraints and the 'Math.min' formula to remember.
// Insight: Binary Search Forces Even Load Distribution. When you run this binary search, you are treating the box
// capacity mid as a uniform standard rule that applies to every single product type equally.
public class MinimumShippingCapacity {

    private int getMinimumShippingCapacity(int[] quantities, int[] weights, int maxBoxes, int maxWeightPerBox){
        // sanity check
        if (quantities == null || weights == null || quantities.length == 0) {
            return 0;
        }

        // The problem statement asks us to find minimum shipping capacity (of items) which can be applied to all the
        // quantities and still satisfy the maxBoxes limit. Some quantity * weight will be smaller and can be fitted in
        // a single box as well. But the goal here is to find a common minimum number, which can be used uniformly.

        long low = 1; // we can fit at least 1 item in the box;
        long largestQuantity = Integer.MIN_VALUE;

        for(int quantity: quantities){
            largestQuantity = Math.max(largestQuantity, quantity);
        }

        long high = largestQuantity; // maximum quantity that can be packed in a single box, given the weight criteria
        // is not violated.
        long optimalCapacity = high;

        while(low <= high){
            long mid = low + (high - low) / 2; // avoid integer overflow

            // now use this target and check how many boxes are needed across all quantities
            long boxesNeeded = calculateBoxesNeededForTarget(quantities, weights, mid, maxWeightPerBox);

            // If boxesNeeded is -1, it means the weight constraint made shipping impossible at this speed
            if (boxesNeeded != -1 && boxesNeeded <= maxBoxes) {
                optimalCapacity = mid; // Candidate capacity found! Save it.
                high = mid - 1;        // Tighten the ceiling, try to find a smaller capacity
            } else {
                low = mid + 1;         // Too small, or over box limit! Raise the floor
            }
        }

        return (int) optimalCapacity;
    }

    private long calculateBoxesNeededForTarget(int[] quantities, int[] weights, long target, int maxWeightPerBox){
        long totalBoxes = 0;

        // Quantity and weights array are of same length. Loop on any one.
        for(int currentIndex = 0; currentIndex < quantities.length; currentIndex++){
            long currentQuantity = quantities[currentIndex];
            long itemWeight = weights[currentIndex];

            // Core Bottleneck Logic: How many items can we ACTUALLY fit due to weight limits?
            long itemsAllowedByWeight = maxWeightPerBox / itemWeight;

            // Edge Case Guard: If a single item is heavier than the whole box limit, it can never be shipped
            if(itemsAllowedByWeight == 0) {
                return -1;
            }

            // our target 'mid' is a number which can be applied across quantity and weights. So let's see which number
            // is minimal ? our target or the current capacity for current weight. Imagine our target is "4",
            // maxWeightPerBox is 10, currentQuantity is 5, itemWeight is 2. So ideally in a box we can fit 5 items
            // each weighing 2 Kg. So anything less than 5 quantity for 2 kg each, fits in the bag. So let's test that
            // number. Maybe this number satisfies other weight and quantity. Goal is to have minimums items in a bag
            // while ensuring that all the items get packed within the bag limit. So a single bag can have different
            // weights. The actual capacity of the box is the tighter of the two constraints
            long commonQuantity = Math.min(target, itemsAllowedByWeight);

            // now let's figure out for this common quantity how many bags will be needed ? Use the integer division
            // buffer trick to account for the remainder.
            totalBoxes += ((currentQuantity + (commonQuantity - 1)) / commonQuantity);
        }

        return totalBoxes;
    }

    public static void main(String[] args){
        MinimumShippingCapacity minimumShippingCapacity = new MinimumShippingCapacity();
        System.out.println(minimumShippingCapacity.getMinimumShippingCapacity(
                new int[]{8, 12, 5},
                new int[]{2, 3, 1},
                6,
                20
        ));

        System.out.println(minimumShippingCapacity.getMinimumShippingCapacity(
                new int[]{10, 15, 8},
                new int[]{5, 2, 3},
                10,
                15
        ));
    }
}
