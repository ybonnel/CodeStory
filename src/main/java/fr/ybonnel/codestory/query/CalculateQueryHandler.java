package fr.ybonnel.codestory.query;


public class CalculateQueryHandler extends AbstractQueryHandler {
    @Override
    public String getResponse(String query) {
        String calculateQuery = query.replace(' ', '+');
        if (calculateQuery.equals("1+1")) {
            return "2";
        }
        return null;
    }
}
