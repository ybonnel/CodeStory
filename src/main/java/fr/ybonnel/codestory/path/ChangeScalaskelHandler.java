package fr.ybonnel.codestory.path;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import fr.ybonnel.codestory.path.scalaskel.Coin;
import fr.ybonnel.codestory.path.scalaskel.Change;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ChangeScalaskelHandler extends AbstractPathHandler {

    private ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private boolean wrongParams(int centsToPay) {
        return centsToPay <= 0 || centsToPay > 100;
    }

    @Override
    public PathResponse getResponse(HttpServletRequest request, String payLoad, String... params) throws JsonProcessingException, NumberFormatException {
        int centsToPay = Integer.parseInt(params[0]);
        if (wrongParams(centsToPay)) {
            return new PathResponse(HttpServletResponse.SC_FORBIDDEN, "Wrong parameters");
        }

        List<Change> changes = completeChanges(centsToPay, null, null);
        return new PathResponse(HttpServletResponse.SC_OK,
                objectMapper.writeValueAsString(changes));
    }

    public List<Change> completeChanges(int cents, Change currentChange, Coin lastCoin) {
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
