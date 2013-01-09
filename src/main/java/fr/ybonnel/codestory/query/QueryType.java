package fr.ybonnel.codestory.query;


import fr.ybonnel.codestory.logs.LogUtil;

public enum QueryType {

    EMAIL(new FixResponseQueryHandler("ybonnel@gmail.com")) {
        @Override
        protected boolean isThisQueryType(String query, String path) {
            return "Quelle est ton adresse email".equals(query);
        }
    },
    LOG(new LogQueryHandler()) {
        @Override
        protected boolean isThisQueryType(String query, String path) {
            return query != null && query.startsWith("log");
        }
    },
    MAILING_LIST(new FixResponseQueryHandler("OUI")) {
        @Override
        protected boolean isThisQueryType(String query, String path) {
            return query != null && query.equals("Es tu abonne a la mailing list(OUI/NON)");
        }
    },
    PARTICIAPATE(new FixResponseQueryHandler("OUI")) {
        @Override
        protected boolean isThisQueryType(String query, String path) {
            return query != null && query.equals("Es tu heureux de participer(OUI/NON)");
        }
    },
    MARKDWN_READY(new FixResponseQueryHandler("OUI")) {
        @Override
        protected boolean isThisQueryType(String query, String path) {
            return query != null && query.equals("Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)");
        }
    },
    NOT_ALWAYS_YES(new FixResponseQueryHandler("NON")) {
        @Override
        protected boolean isThisQueryType(String query, String path) {
            return query != null && query.equals("Est ce que tu reponds toujours oui(OUI/NON)");
        }
    },
    INSERT_ENONCE(new EnonceQueryHandler()) {
        @Override
        protected boolean isThisQueryType(String query, String path) {
            return path.startsWith("/enonce");
        }
    };


    private AbstractQueryHandler queryHandler;

    private QueryType(AbstractQueryHandler queryHandler) {
        this.queryHandler = queryHandler;
    }

    protected abstract boolean isThisQueryType(String query, String path);

    public static String getResponse(String query, String path, String requestBody) throws Exception {
        for (QueryType oneQuestion : values()) {
            if (oneQuestion.isThisQueryType(query, path)) {
                return oneQuestion.queryHandler.getResponse(query, path, requestBody);
            }
        }
        if (query != null) {
            LogUtil.logUnkownQuery(query);
        }
        return null;
    }

}
