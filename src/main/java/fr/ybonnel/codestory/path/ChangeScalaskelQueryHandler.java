package fr.ybonnel.codestory.path;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class ChangeScalaskelQueryHandler extends AbstractPathHandler {

    private ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Override
    public PathType.PathResponse getResponse(HttpServletRequest request, String... params) throws Exception {
        List<Change> changes = completeChanges(Integer.parseInt(params[0]), null, null);
        return new PathType.PathResponse(HttpServletResponse.SC_OK,
                objectMapper.writeValueAsString(changes));
    }

    public List<Change> completeChanges(int cents, Change currentChange, Cent lastMoney) {

        if (cents > 500) {
            return Lists.newArrayList();
        }
        if (cents == 0) {
            if (currentChange == null) {
                return Lists.newArrayList();
            }
            return Lists.newArrayList(currentChange);
        }
        List<Change> changes = Lists.newArrayList();
        for (Cent monay : Cent.values()) {
            if (lastMoney == null ||
                    monay.getValue() >= lastMoney.getValue()) {
                if (monay.canPay(cents)) {
                    Change change = new Change(currentChange);
                    change.pay(monay);
                    changes.addAll(completeChanges(cents - monay.getValue(), change, monay));
                }
            }
        }
        return changes;
    }

}
