package fr.ybonnel.codestory.query;


import fr.ybonnel.codestory.logs.LogUtil;

public enum QueryType {

    EMAIL(new EmailQueryHandler()) {
        @Override
        protected boolean isThisQueryType(String query) {
            return "Quelle est ton adresse email".equals(query);
        }
    },
    LOG(new LogQueryHandler()) {
        @Override
        protected boolean isThisQueryType(String query) {
            return query.startsWith("log");
        }
    };


    private AbstractQueryHandler queryHandler;

    private QueryType(AbstractQueryHandler queryHandler) {
        this.queryHandler = queryHandler;
    }

    protected abstract boolean isThisQueryType(String query);

    public static String getResponse(String query) throws Exception {
        if (query ==null) {
            return null;
        }
        for (QueryType oneQuestion : values()) {
            if (oneQuestion.isThisQueryType(query)) {
                return oneQuestion.queryHandler.getResponse(query);
            }
        }
        LogUtil.logUnkownQuery(query);
        return null;
    }

}
