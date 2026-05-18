package src.interviews.greedy;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/*
Company - AlphaSense

Mango orders. Each line represents one submission from buyer or seller. Each order has order_id, quantity, type and
price. Type is BUY or SELL. Ex: comma separated string: 1,100,SELL,240.

First line is always the seller order and the rest of the lines are buyer order. Seller order should try to match with
buyers orders. Match will happen if buy price > sell price. Difference between buy and sell price is profit for the
seller. A seller may match with multiple buyers if he has enough quantity to meet demand and multiple buyers. Seller can
have some quantities with him at end partially or fully. Output - total profit received by Seller.

If there is any mistake in the order line it should not be considered as valid order.
 */

/*
Time Complexity: (O(N log N)) where (N) is the number of rows. Inserting into the PriorityQueue takes (O(log N)) for
each element, and pulling them out to match takes (O(log N)).

Space Complexity: (O(N)) auxiliary memory space to build the PurchaseMetrics objects inside the max-heap timeline.
 */

public class OrderMangoes {
    static class PurchaseMetrics {
        double buyPrice;
        int buyQuantity;

        public PurchaseMetrics(){}

        public PurchaseMetrics(double buyPrice, int buyQuantity){
            this.buyPrice = buyPrice;
            this.buyQuantity = buyQuantity;
        }
    }

    private double getResult(List<String> orders){
        // input format: comma separated String. 4 values. First is id, second is quantity, third is order type
        // (BUY/SELL). Fourth is buy/sell price. It is given that there will be only one row of order type SELL and
        // others will be of type BUY. Sell happens only when the buy price is > than the sell price. Sell can happen
        // partially or fully. Seller will try to maximize the profit (this is implicit understanding).

        // sanity check
        if(orders == null || orders.size() <= 1){
            return -1.0;
        }

        String[] firstRow = orders.get(0).split(",");
        int sellQuantity = Integer.parseInt(firstRow[1].trim());
        String orderType = firstRow[2].trim();
        double sellPrice = Double.parseDouble(firstRow[3].trim());

        if(sellQuantity <= 0 || (!orderType.equalsIgnoreCase("SELL"))){
            return -1.0;
        }

        // Seller wants to maximize the profit. Profit calculation => (buyPrice - sellPrice) * (sellQuantity - buyQuantity)
        // So we have different buyers with different quantity requirement and buying price. So if you think as seller,
        // you will want to maximize you profit by catering to the buyer who gives you the best rate. So we will first
        // run through the BUY orders and store the highest buying price in a max heap. That way the seller always
        // caters to the highest profit first buy order. We need to store buyPrice and the buyQuantity in the max heap.

        // PriorityQueue by default works as min heap. With the lambda expression we have initiated this as max heap
        PriorityQueue<PurchaseMetrics> maxHeap = new PriorityQueue<>((a,b) -> Double.compare(b.buyPrice, a.buyPrice));

        // Step 1: Collect valid BUY orders
        insertInMaxHeap(orders, maxHeap, sellPrice);

        // Step 2: Execute transactions for valid BUY orders in the max heap
        double totalProfit = getTotalProfitFromSelling(maxHeap, sellQuantity, sellPrice);

        return totalProfit == 0.0 ? -1.0 : totalProfit;
    }

    private double getTotalProfitFromSelling(PriorityQueue<PurchaseMetrics> maxHeap, int sellQuantity, double sellPrice){
        double totalProfit = 0.0;
        while(!maxHeap.isEmpty()){
            // check if stock is available
            if(sellQuantity <= 0){
                break;
            } else {
                PurchaseMetrics purchaseMetrics = maxHeap.poll();
                int purchaseQuantity = Math.min(sellQuantity, purchaseMetrics.buyQuantity);
                totalProfit += purchaseQuantity * (purchaseMetrics.buyPrice - sellPrice);
                sellQuantity -= purchaseQuantity;
            }
        }

        return totalProfit;
    }

    private void insertInMaxHeap(List<String> orders, PriorityQueue<PurchaseMetrics> maxHeap, double sellPrice){
        for(int row = 1; row < orders.size(); row++){
            String[] buyOrder = orders.get(row).split(",");
            int buyQuantity = Integer.parseInt(buyOrder[1].trim());
            String buyType = buyOrder[2].trim();
            double buyPrice = Double.parseDouble(buyOrder[3].trim());

            // check for validity
            if(buyType.equalsIgnoreCase("BUY") && (buyPrice >= sellPrice)){
                // insert in max heap
                maxHeap.add(new PurchaseMetrics(buyPrice, buyQuantity));
            }
        }
    }

    public static void main(String[] args){
        OrderMangoes orderMangoes = new OrderMangoes();

        String row1 = "1,100,SELL,240";
        String row2 = "2,60,BUY,250";
        String row3 = "3,40,BUY,240";
        List<String> order1 = new ArrayList<>(List.of(row1, row2, row3));

        System.out.println(orderMangoes.getResult(order1));
    }
}
