package fr.ybonnel.codestory.path.scalaskel;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public enum Coin {
    FOO(1),
    BAR(7),
    QIX(11),
    BAZ(21);

    private int value;

    Coin(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean canPay(int cents) {
        return cents >= value;
    }

    private static class ListHolder {
        private static final List<Coin> valuesAsLists = newArrayList(values());
    }

    public static List<Coin> valuesAsLists() {
        return ListHolder.valuesAsLists;
    }
}
