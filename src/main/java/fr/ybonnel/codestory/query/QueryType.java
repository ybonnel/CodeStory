package fr.ybonnel.codestory.query;


import fr.ybonnel.codestory.WebServerResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

public enum QueryType {

    EMAIL(new FixResponseQueryHandler("ybonnel@gmail.com")) {
        @Override
        protected boolean isThisQueryType(String query) {
            return "Quelle est ton adresse email".equals(query);
        }
    },
    LOG(new LogQueryHandler()) {

        Pattern pattern = Pattern.compile("log(\\([QN]\\))*");

        @Override
        protected boolean isThisQueryType(String query) {
            return pattern.matcher(query).matches();
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
    },
    NOT_ALWAYS_YES(new FixResponseQueryHandler("NON")) {
        @Override
        protected boolean isThisQueryType(String query) {
            return query.equals("Est ce que tu reponds toujours oui(OUI/NON)");
        }
    },
    KNOW_ENONCE1(new FixResponseQueryHandler("OUI")) {
        @Override
        protected boolean isThisQueryType(String query) {
            return query.equals("As tu bien recu le premier enonce(OUI/NON)");
        }
    },
    KNOW_ENONCE2(new FixResponseQueryHandler("OUI")) {
        @Override
        protected boolean isThisQueryType(String query) {
            return query.equals("As tu bien recu le second enonce(OUI/NON)");
        }
    },
    GOOD_NIGHT(new FixResponseQueryHandler("BOF")) {
        @Override
        protected boolean isThisQueryType(String query) {
            return query.equals("As tu passe une bonne nuit malgre les bugs de l etape precedente(PAS_TOP/BOF/QUELS_BUGS)");
        }
    },
    NOT_COPY(new FixResponseQueryHandler("NON")) {
        @Override
        protected boolean isThisQueryType(String query) {
            return query.equals("As tu copie le code de ndeloof(OUI/NON/JE_SUIS_NICOLAS)");
        }
    },
    CALCULATE(new CalculateQueryHandler()) {

        private Pattern pattern = Pattern.compile("[\\(\\)0-9\\+/\\* ,\\-]+");

        @Override
        protected boolean isThisQueryType(String query) {
            return pattern.matcher(query).matches();
        }
    };


    private AbstractQueryHandler queryHandler;

    QueryType(AbstractQueryHandler queryHandler) {
        this.queryHandler = queryHandler;
    }

    protected abstract boolean isThisQueryType(String query);

    public static WebServerResponse getResponse(String query) {
        for (QueryType oneQuestion : values()) {
            if (oneQuestion.isThisQueryType(query)) {
                return new WebServerResponse(HttpServletResponse.SC_OK, oneQuestion.queryHandler.getResponse(query));
            }
        }
        return new WebServerResponse(HttpServletResponse.SC_NOT_FOUND, "Query " + query + " is unknown");
    }

}
