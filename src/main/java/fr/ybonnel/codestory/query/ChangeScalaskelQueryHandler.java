package fr.ybonnel.codestory.query;


import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeScalaskelQueryHandler extends AbstractQueryHandler {
    @Override
    public String getResponse(String query, String path, HttpServletRequest request) throws Exception {

        Matcher matcher = Pattern.compile("/scalaskel/change/(\\d+)").matcher(path);
        if (matcher.matches()) {
            return new GsonBuilder().create().toJson(getChanges(Integer.parseInt(matcher.group(1))));
        }
        return null;
    }

    private List<Change> getChanges(int cents) {

        List<Change> changes = new ArrayList<Change>();
        completeChanges(cents, null, changes, null);
        return changes;
    }

    private void completeChanges(int cents, Change currentChange, List<Change> changes, Cent lastMoney) {
        if (cents == 0) {
            changes.add(currentChange);
        }
        for (Cent monay : Cent.values()) {
            if (lastMoney == null ||
                    monay.getValue() >= lastMoney.getValue()) {
                if (monay.canPay(cents)) {
                    Change change = new Change(currentChange);
                    change.pay(monay);
                    completeChanges(cents - monay.getValue(), change, changes, monay);
                }
            }
        }
    }

    private enum Cent {
        FOO(1),
        BAR(7),
        QIX(11),
        BAZ(21);

        private int value;

        private Cent(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public boolean canPay(int cents) {
            return cents >= value;
        }
    }

    private class Change {
        private Integer foo;
        private Integer bar;
        private Integer qix;
        private Integer baz;

        public Change(Integer foo, Integer bar, Integer qix, Integer baz) {
            this.foo = foo;
            this.bar = bar;
            this.qix = qix;
            this.baz = baz;
        }

        public Change(Change change) {
            if (change != null) {
                this.foo = change.foo;
                this.bar = change.bar;
                this.qix = change.qix;
                this.baz = change.baz;
            }
        }

        public void pay(Cent cent) {
            switch (cent) {
                case FOO:
                    this.foo = this.foo == null ? 1 : this.foo + 1;
                    break;
                case BAR:
                    this.bar = this.bar == null ? 1 : this.bar + 1;
                    break;
                case QIX:
                    this.qix = this.qix == null ? 1 : this.qix + 1;
                    break;
                case BAZ:
                    this.baz = this.baz == null ? 1 : this.baz + 1;
                    break;
            }
        }
    }
}
