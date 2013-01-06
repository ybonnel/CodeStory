package fr.ybonnel.codestory;


import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class LogQuestion extends AbstractQuestion {
    @Override
    public String getReponse(String question) throws IOException {
        return Files.toString(new File("./serveur.log"), Charset.forName("utf-8")).replaceAll("\\n", "<br/>");
    }
}
