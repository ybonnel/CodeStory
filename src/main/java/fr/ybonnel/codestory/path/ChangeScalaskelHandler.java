package fr.ybonnel.codestory.path;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fr.ybonnel.codestory.path.scalaskel.Cent;
import fr.ybonnel.codestory.path.scalaskel.Change;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ChangeScalaskelHandler extends AbstractPathHandler {

    private ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private boolean verifParams(int centsToPay) {
        return centsToPay > 0 && centsToPay <= 100;
    }

    @Override
    public PathType.PathResponse getResponse(HttpServletRequest request, String... params) throws Exception {
        int centsToPay = Integer.parseInt(params[0]);
        if (!verifParams(centsToPay)) {
            return new PathType.PathResponse(HttpServletResponse.SC_FORBIDDEN, "Wrong parameters");
        }

        List<Change> changes = completeChanges(centsToPay, null, null);
        return new PathType.PathResponse(HttpServletResponse.SC_OK,
                objectMapper.writeValueAsString(changes));
    }

    public List<Change> completeChanges(int cents, Change currentChange, Cent lastMoney) {
        // Stop condition of recursivity
        if (cents == 0) {
            return Lists.newArrayList(currentChange);
        }
        List<Change> changes = Lists.newArrayList();
        for (Cent monay : Collections2.filter(
                Cent.valuesAsLists(),
                new FilterCent(lastMoney,cents))) {
            Change change = new Change(currentChange);
            change.pay(monay);
            changes.addAll(completeChanges(cents - monay.getValue(), change, monay));
        }
        return changes;
    }

    private static class FilterCent implements Predicate<Cent> {

        private int minValue;
        private int centsToPay;

        private FilterCent(Cent currentMoney, int centsToPay) {
            minValue = currentMoney == null ? 0 : currentMoney.getValue();
            this.centsToPay = centsToPay;
        }

        @Override
        public boolean apply(Cent input) {
            return minValue <= input.getValue() && input.canPay(centsToPay);
        }
    }

}
