package fr.ybonnel.codestory.query;


import groovy.lang.GroovyShell;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class CalculateQueryHandler extends AbstractQueryHandler {
    private NumberFormat format = new DecimalFormat("#0.#", new DecimalFormatSymbols(Locale.FRANCE));
    private GroovyShell shell = new GroovyShell();

    public CalculateQueryHandler() {
        format.setMaximumFractionDigits(500);
    }

    @Override
    public String getResponse(String query) {

        Object object = shell.evaluate("return " + query.replace(' ', '+').replace(',', '.'));

        return formatGroovyReturn(object);
    }

    private String formatGroovyReturn(Object object) {
        if (object instanceof Integer) {
            return object.toString();
        } else if (object instanceof BigDecimal) {
            BigDecimal result = (BigDecimal) object;

            try {
                return format.format(result);
            } catch (NumberFormatException numberFormatException) {
                numberFormatException.printStackTrace();
                return null;
            }
        } else {
            throw new IllegalArgumentException("Type inconnu " + object.getClass());
        }
    }
}
