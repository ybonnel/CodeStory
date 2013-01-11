package fr.ybonnel.codestory.path.scalaskel;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Change {
    @JsonProperty
    private Integer foo;
    @JsonProperty
    private Integer bar;
    @JsonProperty
    private Integer qix;
    @JsonProperty
    private Integer baz;

    public Change(Change change) {
        if (change != null) {
            foo = change.foo;
            bar = change.bar;
            qix = change.qix;
            baz = change.baz;
        }
    }

    public void pay(Coin coin) {
        switch (coin) {
            case FOO:
                foo = foo == null ? 1 : foo + 1;
                break;
            case BAR:
                bar = bar == null ? 1 : bar + 1;
                break;
            case QIX:
                qix = qix == null ? 1 : qix + 1;
                break;
            case BAZ:
                baz = baz == null ? 1 : baz + 1;
                break;
        }
    }
}
