package fr.ybonnel.codestory;


public enum Question {

    EMAIL(new EmailQuestion()) {
        @Override
        protected boolean isThisQuestion(String question) {
            return "Quelle est ton adresse email".equals(question);
        }
    },
    LOG(new LogQuestion()) {
        @Override
        protected boolean isThisQuestion(String question) {
            return question.startsWith("log");
        }
    };


    private AbstractQuestion abstractQuestion;

    private Question(AbstractQuestion abstractQuestion) {
        this.abstractQuestion = abstractQuestion;
    }

    protected abstract boolean isThisQuestion(String question);

    public static String getReponse(String question) throws Exception {
        if (question ==null) {
            return null;
        }
        for (Question oneQuestion : values()) {
            if (oneQuestion.isThisQuestion(question)) {
                return oneQuestion.abstractQuestion.getReponse(question);
            }
        }
        LogUtil.logQuestionUnkown(question);
        return null;
    }

}
