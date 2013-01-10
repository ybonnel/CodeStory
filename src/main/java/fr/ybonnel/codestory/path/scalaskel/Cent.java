package fr.ybonnel.codestory.path.scalaskel;

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
}
