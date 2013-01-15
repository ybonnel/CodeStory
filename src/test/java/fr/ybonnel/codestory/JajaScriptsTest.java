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

    @Test
    public void should_be_wery_faster() throws IOException, SAXException {
        String request = "[{\"VOL\":\"energetic-thumbtack-72\",\"DEPART\":0,\"DUREE\":4,\"PRIX\":12},{\"VOL\":\"confused-joint-99\",\"DEPART\":1,\"DUREE\":2,\"PRIX\":7},{\"VOL\":\"fancy-publisher-39\",\"DEPART\":2,\"DUREE\":6,\"PRIX\":3},{\"VOL\":\"victorious-tenor-78\",\"DEPART\":4,\"DUREE\":5,\"PRIX\":9},{\"VOL\":\"slow-cocoa-66\",\"DEPART\":5,\"DUREE\":2,\"PRIX\":11},{\"VOL\":\"raspy-bike-42\",\"DEPART\":5,\"DUREE\":4,\"PRIX\":8},{\"VOL\":\"powerful-ocean-10\",\"DEPART\":6,\"DUREE\":2,\"PRIX\":7},{\"VOL\":\"high-alternator-4\",\"DEPART\":7,\"DUREE\":6,\"PRIX\":6},{\"VOL\":\"wide-gloom-53\",\"DEPART\":9,\"DUREE\":5,\"PRIX\":19},{\"VOL\":\"hushed-slope-57\",\"DEPART\":10,\"DUREE\":2,\"PRIX\":5},{\"VOL\":\"foolish-bandana-45\",\"DEPART\":10,\"DUREE\":4,\"PRIX\":10},{\"VOL\":\"teeny-mouth-75\",\"DEPART\":11,\"DUREE\":2,\"PRIX\":5},{\"VOL\":\"loud-pie-32\",\"DEPART\":12,\"DUREE\":6,\"PRIX\":2},{\"VOL\":\"successful-storage-75\",\"DEPART\":14,\"DUREE\":5,\"PRIX\":7},{\"VOL\":\"curious-shampoo-2\",\"DEPART\":15,\"DUREE\":2,\"PRIX\":9},{\"VOL\":\"soft-software-15\",\"DEPART\":15,\"DUREE\":4,\"PRIX\":6},{\"VOL\":\"itchy-specs-18\",\"DEPART\":16,\"DUREE\":2,\"PRIX\":5},{\"VOL\":\"unsightly-gasoline-14\",\"DEPART\":17,\"DUREE\":6,\"PRIX\":3},{\"VOL\":\"precious-jawbone-35\",\"DEPART\":19,\"DUREE\":5,\"PRIX\":13},{\"VOL\":\"shiny-map-17\",\"DEPART\":20,\"DUREE\":2,\"PRIX\":6},{\"VOL\":\"homeless-walnut-51\",\"DEPART\":20,\"DUREE\":4,\"PRIX\":12},{\"VOL\":\"agreeable-pedestal-36\",\"DEPART\":21,\"DUREE\":2,\"PRIX\":5},{\"VOL\":\"fast-pasteboard-23\",\"DEPART\":22,\"DUREE\":6,\"PRIX\":7},{\"VOL\":\"gigantic-tulip-84\",\"DEPART\":24,\"DUREE\":5,\"PRIX\":6},{\"VOL\":\"testy-voter-31\",\"DEPART\":25,\"DUREE\":2,\"PRIX\":16},{\"VOL\":\"wonderful-snubnosed-56\",\"DEPART\":25,\"DUREE\":4,\"PRIX\":15},{\"VOL\":\"little-metal-22\",\"DEPART\":26,\"DUREE\":2,\"PRIX\":5},{\"VOL\":\"strange-password-43\",\"DEPART\":27,\"DUREE\":6,\"PRIX\":2},{\"VOL\":\"resonant-signboard-19\",\"DEPART\":29,\"DUREE\":5,\"PRIX\":4},{\"VOL\":\"husky-earth-38\",\"DEPART\":30,\"DUREE\":2,\"PRIX\":8},{\"VOL\":\"lonely-absinthe-94\",\"DEPART\":30,\"DUREE\":4,\"PRIX\":6},{\"VOL\":\"motionless-bean-72\",\"DEPART\":31,\"DUREE\":2,\"PRIX\":4},{\"VOL\":\"scary-idiot-13\",\"DEPART\":32,\"DUREE\":6,\"PRIX\":5},{\"VOL\":\"hissing-scandal-10\",\"DEPART\":34,\"DUREE\":5,\"PRIX\":22},{\"VOL\":\"gentle-anorexic-76\",\"DEPART\":35,\"DUREE\":2,\"PRIX\":3},{\"VOL\":\"bewildered-scraper-72\",\"DEPART\":35,\"DUREE\":4,\"PRIX\":11},{\"VOL\":\"tense-bear-88\",\"DEPART\":36,\"DUREE\":2,\"PRIX\":10},{\"VOL\":\"teeny-supervisor-40\",\"DEPART\":37,\"DUREE\":6,\"PRIX\":3},{\"VOL\":\"hissing-turpentine-47\",\"DEPART\":39,\"DUREE\":5,\"PRIX\":16},{\"VOL\":\"frightened-vitamin-20\",\"DEPART\":40,\"DUREE\":2,\"PRIX\":30},{\"VOL\":\"huge-cherry-18\",\"DEPART\":40,\"DUREE\":4,\"PRIX\":8},{\"VOL\":\"impossible-perfumery-52\",\"DEPART\":41,\"DUREE\":2,\"PRIX\":2},{\"VOL\":\"bright-slavery-85\",\"DEPART\":42,\"DUREE\":6,\"PRIX\":3},{\"VOL\":\"sore-landscape-83\",\"DEPART\":44,\"DUREE\":5,\"PRIX\":23},{\"VOL\":\"dull-pup-14\",\"DEPART\":45,\"DUREE\":2,\"PRIX\":16},{\"VOL\":\"pleasant-cobbler-26\",\"DEPART\":45,\"DUREE\":4,\"PRIX\":15},{\"VOL\":\"wrong-cow-45\",\"DEPART\":46,\"DUREE\":2,\"PRIX\":4},{\"VOL\":\"thundering-eyeglasses-82\",\"DEPART\":47,\"DUREE\":6,\"PRIX\":6},{\"VOL\":\"funny-seaside-90\",\"DEPART\":49,\"DUREE\":5,\"PRIX\":11},{\"VOL\":\"zealous-ringer-99\",\"DEPART\":50,\"DUREE\":2,\"PRIX\":11}]";

        String resultExpected = "{\"gain\":174,\"path\":[\"energetic-thumbtack-72\",\"slow-cocoa-66\",\"wide-gloom-53\",\"curious-shampoo-2\",\"precious-jawbone-35\",\"testy-voter-31\",\"husky-earth-38\",\"hissing-scandal-10\",\"frightened-vitamin-20\",\"sore-landscape-83\",\"zealous-ringer-99\"]}";

        WebConversation wc = new WebConversation();

        PostMethodWebRequest postRequest = new PostMethodWebRequest(getURL() + "jajascript/optimize",
                new ByteArrayInputStream(request.getBytes()), "application/json");

        WebResponse response = wc.getResponse(postRequest);

        assertEquals(200, response.getResponseCode());
        assertEquals("application/json", response.getContentType());
        assertEquals(resultExpected, response.getText());
    }
}


