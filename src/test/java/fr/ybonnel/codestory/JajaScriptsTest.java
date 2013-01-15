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
        String request = "[{ \"VOL\": \"MONAD42\", \"DEPART\": 0, \"DUREE\": 5, \"PRIX\": 10 }," +
                "{ \"VOL\": \"META18\", \"DEPART\": 3, \"DUREE\": 7, \"PRIX\": 14 }," +
                "{ \"VOL\": \"LEGACY01\", \"DEPART\": 5, \"DUREE\": 9, \"PRIX\": 8 }," +
                "{ \"VOL\": \"YAGNI17\", \"DEPART\": 5, \"DUREE\": 9, \"PRIX\": 7 }]";

        String resultExpected = "{\"gain\":18,\"path\":[\"MONAD42\",\"LEGACY01\"]}";

        WebConversation wc = new WebConversation();

        PostMethodWebRequest postRequest = new PostMethodWebRequest(getURL() + "jajascript/optimize",
                new ByteArrayInputStream(request.getBytes()), "application/json");

        WebResponse response = wc.getResponse(postRequest);

        assertEquals(200, response.getResponseCode());
        assertEquals("application/json", response.getContentType());
        assertEquals(resultExpected, response.getText());
    }

    @Test
    public void should_be_faster() throws IOException, SAXException {
        String request = "[{\"VOL\":\"cooing-garbage-26\",\"DEPART\":0,\"DUREE\":4,\"PRIX\":10},{\"VOL\":\"bright-watercress-92\",\"DEPART\":1,\"DUREE\":2,\"PRIX\":5},{\"VOL\":\"thankful-watercress-10\",\"DEPART\":2,\"DUREE\":6,\"PRIX\":6},{\"VOL\":\"disgusted-sparkler-4\",\"DEPART\":4,\"DUREE\":5,\"PRIX\":9},{\"VOL\":\"screeching-driftwood-70\",\"DEPART\":5,\"DUREE\":2,\"PRIX\":9},{\"VOL\":\"important-dwarf-88\",\"DEPART\":5,\"DUREE\":4,\"PRIX\":9},{\"VOL\":\"fast-pretzel-82\",\"DEPART\":6,\"DUREE\":2,\"PRIX\":8},{\"VOL\":\"super-huntsman-48\",\"DEPART\":7,\"DUREE\":6,\"PRIX\":3},{\"VOL\":\"mammoth-topographer-83\",\"DEPART\":9,\"DUREE\":5,\"PRIX\":4},{\"VOL\":\"blue-wiper-14\",\"DEPART\":10,\"DUREE\":2,\"PRIX\":26},{\"VOL\":\"inquisitive-nation-70\",\"DEPART\":10,\"DUREE\":4,\"PRIX\":15},{\"VOL\":\"curved-dinosaur-85\",\"DEPART\":11,\"DUREE\":2,\"PRIX\":9},{\"VOL\":\"short-conga-87\",\"DEPART\":12,\"DUREE\":6,\"PRIX\":1},{\"VOL\":\"puzzled-hatchet-58\",\"DEPART\":14,\"DUREE\":5,\"PRIX\":19},{\"VOL\":\"depressed-firefighter-11\",\"DEPART\":15,\"DUREE\":2,\"PRIX\":10},{\"VOL\":\"super-dolt-88\",\"DEPART\":15,\"DUREE\":4,\"PRIX\":6},{\"VOL\":\"adventurous-sidebar-15\",\"DEPART\":16,\"DUREE\":2,\"PRIX\":9},{\"VOL\":\"cooing-mate-76\",\"DEPART\":17,\"DUREE\":6,\"PRIX\":6},{\"VOL\":\"super-smartass-58\",\"DEPART\":19,\"DUREE\":5,\"PRIX\":16},{\"VOL\":\"helpless-cage-84\",\"DEPART\":20,\"DUREE\":2,\"PRIX\":13},{\"VOL\":\"good-sin-11\",\"DEPART\":20,\"DUREE\":4,\"PRIX\":12},{\"VOL\":\"grumpy-ballpoint-21\",\"DEPART\":21,\"DUREE\":2,\"PRIX\":9},{\"VOL\":\"cloudy-buffalo-12\",\"DEPART\":22,\"DUREE\":6,\"PRIX\":7},{\"VOL\":\"ashamed-taxi-33\",\"DEPART\":24,\"DUREE\":5,\"PRIX\":4},{\"VOL\":\"adventurous-pastry-44\",\"DEPART\":25,\"DUREE\":2,\"PRIX\":3}]";

        String resultExpected = "{\"gain\":84,\"path\":[\"cooing-garbage-26\",\"screeching-driftwood-70\",\"blue-wiper-14\",\"puzzled-hatchet-58\",\"helpless-cage-84\",\"cloudy-buffalo-12\"]}";

        WebConversation wc = new WebConversation();

        PostMethodWebRequest postRequest = new PostMethodWebRequest(getURL() + "jajascript/optimize",
                new ByteArrayInputStream(request.getBytes()), "application/json");

        WebResponse response = wc.getResponse(postRequest);

        assertEquals(200, response.getResponseCode());
        assertEquals("application/json", response.getContentType());
        assertEquals(resultExpected, response.getText());


    }
}


