import java.util.*;

/**
 * P020. Best Time Buy Sell Stock. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P020BestTimeBuySellStock {

    private P020BestTimeBuySellStock() {
    }

    public int maxProfit(int[] prices) {
        int min = Integer.MAX_VALUE, profit = 0;
        for (int price : prices) {
    min = Math.min(min, price);
    profit = Math.max(profit, price - min);
        }
        return profit;
    }

}
