package fr.ybonnel.codestory.query.calculate;

/** Search deeper parenthesis */
public class SearchParanthesis {
    private String calculateQuery;
    private int start;
    private int end;

    public SearchParanthesis(String calculateQuery) {
        this.calculateQuery = calculateQuery;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public SearchParanthesis invoke() {
        start = -1;
        end = -1;
        int currentProf = 0;
        int currentStart = -1;
        int maxProf = -1;
        for (int index = 0; index < calculateQuery.length(); index++) {
            char car = calculateQuery.charAt(index);
            if (car == '(') {
                currentStart = index;
                currentProf++;
            }

            if (car == ')') {
                if (currentProf > maxProf) {
                    start = currentStart;
                    end = index + 1;
                    maxProf = currentProf;
                }
                currentProf--;
            }
        }
        return this;
    }
}
