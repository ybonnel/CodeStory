package fr.ybonnel.codestory;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JajaScriptsTest extends WebServerTestUtil {

    @Test
    public void should_answer_to_jajascripts() throws IOException, SAXException {
        String request = "[{ \"MONAD42\",0,5,10 }," +
                "{ \"META18\",3,7,14 }," +
                "{ \"LEGACY01\",5,9,7}," +
                "{ \"YAGNI17\",6,9,8}" +
                "]";

        String resultExpected = "{\"gain\":18,\"path\":[\"MONAD42\",\"YAGNI17\"]}";

        WebConversation wc = new WebConversation();

        PostMethodWebRequest postRequest = new PostMethodWebRequest(getURL() + "jajascript/optimize",
                new ByteArrayInputStream(request.getBytes()), "application/json");

        WebResponse response = wc.getResponse(postRequest);

        assertEquals(200, response.getResponseCode());
        assertEquals(resultExpected, response.getText());
    }
}


