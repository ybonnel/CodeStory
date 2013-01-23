package fr.ybonnel.codestory.path.scalaskel;


import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.util.List;

public enum ScalaskelChangeService {
    INSTANCE;

    public static ScalaskelChangeService getInstance() {
        return INSTANCE;
    }

    public List<Change> calculateChanges(int cents) {
        return completeChanges(cents, null, null);
    }

    private List<Change> completeChanges(int cents, Change currentChange, Coin lastCoin) {
        // Stop condition of recursivity
        if (cents == 0) {
            return Lists.newArrayList(currentChange);
        }
        List<Change> changes = Lists.newArrayList();
        for (Coin coin : Collections2.filter(
                Coin.valuesAsLists(),
                new FilterCoins(lastCoin, cents))) {
            Change change = new Change(currentChange);
            change.pay(coin);
            changes.addAll(completeChanges(cents - coin.getValue(), change, coin));
        }
        return changes;
    }

    /**
     * Filter coins with this rule :
     * coin is keeped only if :
     * <ul>
     * <li>its value is bigger or equals thant lastCoin</li>
     * <li>we can pay with the coin.</li>
     * </ul>
     */
    private static class FilterCoins implements Predicate<Coin> {

        private int minValue;
        private int centsToPay;

        private FilterCoins(Coin lastCoin, int centsToPay) {
            minValue = lastCoin == null ? 0 : lastCoin.getValue();
            this.centsToPay = centsToPay;
        }

        @Override
        public boolean apply(Coin input) {
            return minValue <= input.getValue() && input.canPay(centsToPay);
        }
    }
}
