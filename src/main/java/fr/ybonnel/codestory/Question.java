package fr.ybonnel.codestory;


public enum Question {

    EMAIL(new EmailQuestion()) {
        @Override
        protected boolean isThisQuestion(String question) {
            return "Quelle est ton adresse email".equals(question);
        }
    };


    private AbstractQuestion abstractQuestion;

    private Question(AbstractQuestion abstractQuestion) {
        this.abstractQuestion = abstractQuestion;
    }

    protected abstract boolean isThisQuestion(String question);

    public static String getReponse(String question) {
        for (Question oneQuestion : values()) {
            if (oneQuestion.isThisQuestion(question)) {
                return oneQuestion.abstractQuestion.getReponse(question);
            }
        }
        return null;
    }

}
