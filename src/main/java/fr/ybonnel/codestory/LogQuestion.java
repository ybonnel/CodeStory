package fr.ybonnel.codestory;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogQuestion extends AbstractQuestion {

    private Gson gson = new GsonBuilder().create();

    @Override
    public String getReponse(String question) throws IOException {
        if ("log".equals(question)) {
            return gson.toJson(DatabaseManager.INSTANCE.getLogs());
        }

        Matcher matcher = Pattern.compile("log\\(([a-zA-Z])\\)").matcher(question);
        if (matcher.matches()) {
            return gson.toJson(DatabaseManager.INSTANCE.getLogsByType(matcher.group(1)));
        }
        return null;
    }
}
