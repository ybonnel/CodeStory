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
