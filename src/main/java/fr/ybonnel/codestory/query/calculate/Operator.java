package fr.ybonnel.codestory.query.calculate;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class Operator {
    private Pattern pattern;

    public Operator(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public Matcher matcher(String query) {
        return pattern.matcher(query);
    }

    public abstract BigDecimal operate(BigDecimal a, BigDecimal b);

}
