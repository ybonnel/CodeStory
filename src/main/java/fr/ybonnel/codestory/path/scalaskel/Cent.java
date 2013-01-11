package fr.ybonnel.codestory.path.scalaskel;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public enum Cent {
    FOO(1),
    BAR(7),
    QIX(11),
    BAZ(21);

    private int value;

    Cent(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean canPay(int cents) {
        return cents >= value;
    }

    private static List<Cent> valuesAsLists;

    public static List<Cent> valuesAsLists() {
        if (valuesAsLists == null) {
            valuesAsLists = newArrayList(values());
        }
        return valuesAsLists;
    }
}
