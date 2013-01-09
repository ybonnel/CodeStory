package fr.ybonnel.codestory.query;


import fr.ybonnel.codestory.logs.LogUtil;

public enum QueryType {

    EMAIL(new FixResponseQueryHandler("ybonnel@gmail.com")) {
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
    },
    MAILING_LIST(new FixResponseQueryHandler("OUI")) {
        @Override
        protected boolean isThisQueryType(String query) {
            return query.equals("Es tu abonne a la mailing list(OUI/NON)");
        }
    },
    PARTICIAPATE(new FixResponseQueryHandler("OUI")) {
        @Override
        protected boolean isThisQueryType(String query) {
            return query.equals("Es tu heureux de participer(OUI/NON)");
        }
    },
    MARKDWN_READY(new FixResponseQueryHandler("OUI")) {
        @Override
        protected boolean isThisQueryType(String query) {
            return query.equals("Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)");
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
