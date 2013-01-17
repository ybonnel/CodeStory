package fr.ybonnel.codestory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import fr.ybonnel.codestory.logs.LogUtil;
import fr.ybonnel.codestory.path.jajascript.Commande;
import fr.ybonnel.codestory.path.jajascript.JajaScriptResponse;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void send_big_request() throws IOException, SAXException {
        String request = IOUtils.toString(JajaScriptsTest.class.getResource("/bigJajascript.json"));

        WebConversation wc = new WebConversation();

        PostMethodWebRequest postRequest = new PostMethodWebRequest(getURL() + "jajascript/optimize",
                new ByteArrayInputStream(request.getBytes()), "application/json");

        WebResponse response = wc.getResponse(postRequest);

        assertEquals(200, response.getResponseCode());
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getText().startsWith("{\"gain\":3991"));

    }

    private WebResponse sendPostRequest(WebConversation wc, String url, String request) throws IOException, SAXException {

        PostMethodWebRequest postRequest = new PostMethodWebRequest(url,
                new ByteArrayInputStream(request.getBytes()), "application/json");

        return wc.getResponse(postRequest);
    }

    private Random random = new SecureRandom();

    @Test
    public void should_answer_same_as_legacy() throws IOException, SAXException {
        LogUtil.disableLogs();
        ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        WebConversation wc = new WebConversation();


        for (int nbCommands = 1; nbCommands <= 500; nbCommands = nbCommands + 1) {
            List<Commande> commandes = generateRandomCommands(nbCommands);

            String request = mapper.writeValueAsString(commandes);

            long startTime = System.nanoTime();
            WebResponse response = sendPostRequest(wc, getURL() + "jajascript/optimize/legacy", request);
            long elapsedTimeLegacy = System.nanoTime() - startTime;

            assertEquals(200, response.getResponseCode());
            assertEquals("application/json", response.getContentType());
            String legacyResponse = response.getText();


            startTime = System.nanoTime();
            response = sendPostRequest(wc, getURL() + "jajascript/optimize", request);
            long elapsedTimeNew = System.nanoTime() - startTime;

            assertEquals(200, response.getResponseCode());
            assertEquals("application/json", response.getContentType());

            String newResponse = response.getText();


            int legacyGain = mapper.readValue(legacyResponse, JajaScriptResponse.class).getGain();
            int newGain = mapper.readValue(newResponse, JajaScriptResponse.class).getGain();

            System.out.println("Nb commands " + nbCommands + " : legacy=" + TimeUnit.NANOSECONDS.toMillis(elapsedTimeLegacy) + ",new=" + TimeUnit.NANOSECONDS.toMillis(elapsedTimeNew));

            if (legacyGain != newGain) {
                System.out.println("Request = " + request);
                System.out.println("Legacy = " + legacyResponse);
                System.out.println("NewResponse = " + newResponse);
            }

            assertEquals(request, legacyGain, newGain);
        }
    }

    private List<Commande> generateRandomCommands(int nbCommands) {
        List<Commande> commandes = new ArrayList<Commande>();
        for (int index = 1; index <= nbCommands; index++) {
            Commande commande = new Commande();
            commande.setNomVol(Integer.toString(index));
            commande.setHeureDepart(index);
            commande.setTempsVol(random.nextInt(10) + 1);
            commande.setPrix(random.nextInt(20) + 1);
            commandes.add(commande);
        }
        return commandes;
    }

    private static class ResponseByLevel {
        private int level;
        private long legacyTime;
        private long newTime;

        private ResponseByLevel(int level, long legacyTime, long newTime) {
            this.level = level;
            this.legacyTime = legacyTime;
            this.newTime = newTime;
        }

        public void print() {
            if (legacyTime != -1) {
                System.out.println(Joiner.on(",").join(level, TimeUnit.NANOSECONDS.toMillis(legacyTime), TimeUnit.NANOSECONDS.toMillis(newTime)));
            } else {
                System.out.println(level + ",," + TimeUnit.NANOSECONDS.toMillis(newTime));
            }
        }
    }

    @Test
    @Ignore
    public void should_found_max_level() throws IOException, SAXException {
        LogUtil.disableLogs();
        ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        WebConversation wc = new WebConversation();

        // Warm up
        String warmUpRequest = mapper.writeValueAsString(generateRandomCommands(5));
        sendPostRequest(wc, getURL() + "jajascript/optimize", warmUpRequest);
        sendPostRequest(wc, getURL() + "jajascript/optimize/legacy", warmUpRequest);


        boolean legacyRespond = true;
        boolean newRespond = true;
        long elapsedTimeLegacy = 0;

        List<ResponseByLevel> reponsesByLevel = new ArrayList<ResponseByLevel>();

        int level = 10;
        while (legacyRespond || newRespond) {
            elapsedTimeLegacy = -1;
            List<Commande> commandes = generateRandomCommands(level * 5);

            String request = mapper.writeValueAsString(commandes);

            if (legacyRespond) {
                long startTime = System.nanoTime();
                WebResponse response = sendPostRequest(wc, getURL() + "jajascript/optimize/legacy", request);
                elapsedTimeLegacy = System.nanoTime() - startTime;

                assertEquals(200, response.getResponseCode());
                assertEquals("application/json", response.getContentType());
            }

            long startTime = System.nanoTime();
            WebResponse response = sendPostRequest(wc, getURL() + "jajascript/optimize", request);
            long elapsedTimeNew = System.nanoTime() - startTime;

            assertEquals(200, response.getResponseCode());
            assertEquals("application/json", response.getContentType());

            reponsesByLevel.add(new ResponseByLevel(level, elapsedTimeLegacy, elapsedTimeNew));

            System.out.println("Level : " + level);
            System.out.println("LegacyTime : " + TimeUnit.NANOSECONDS.toMillis(elapsedTimeLegacy));
            System.out.println("NewTime : " + TimeUnit.NANOSECONDS.toMillis(elapsedTimeNew));


            if (elapsedTimeLegacy > TimeUnit.SECONDS.toNanos(5)) {
                legacyRespond = false;
            }
            if (elapsedTimeNew > TimeUnit.SECONDS.toNanos(5)) {
                newRespond = false;
            }
            int increment = level / 10 <= 10 ? 10 : level / 10;
            increment = increment - increment % 10;
            level = level + increment;
        }

        // Print Responses By Level
        System.out.println("Level,Legacy Time(ms),New Time(ms)");
        int lastLevel = 0;
        long lastLegacyTime = 0;
        long lastNewTime = 0;
        for (ResponseByLevel responseByLevel : reponsesByLevel) {

            long diffLegacyTime = responseByLevel.legacyTime - lastLegacyTime;
            long diffNewTime = responseByLevel.newTime - lastNewTime;
            int diffLevel = responseByLevel.level - lastLevel;
            for (int forgotLevel = lastLevel + 10; forgotLevel < responseByLevel.level; forgotLevel += 10) {
                long legacyTimeForgot = -1;
                if (responseByLevel.legacyTime != -1) {
                    legacyTimeForgot = lastLegacyTime + diffLegacyTime * (forgotLevel - lastLevel) / diffLevel;
                }
                long newTimeForgot = lastNewTime + diffNewTime * (forgotLevel - lastLevel) / diffLevel;
                new ResponseByLevel(forgotLevel, legacyTimeForgot, newTimeForgot).print();
            }
            responseByLevel.print();
            lastLevel = responseByLevel.level;
            lastLegacyTime = responseByLevel.legacyTime;
            lastNewTime = responseByLevel.newTime;
        }

    }


    @Test
    @Ignore
    public void should_be_very_very_fast() throws IOException, SAXException {
        LogUtil.disableLogs();
        ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        WebConversation wc = new WebConversation();

        // Warm up
        String warmUpRequest = mapper.writeValueAsString(generateRandomCommands(5));
        sendPostRequest(wc, getURL() + "jajascript/optimize", warmUpRequest);

        int level = 30000;
        List<Commande> commandes = generateRandomCommands(level * 5);
        String request = mapper.writeValueAsString(commandes);

        long startTime = System.nanoTime();

        WebResponse response = sendPostRequest(wc, getURL() + "jajascript/optimize", request);

        long elapsedTime = System.nanoTime() - startTime;

        assertEquals(200, response.getResponseCode());
        assertEquals("application/json", response.getContentType());
        System.out.println("Level " + (level) + " : " + TimeUnit.NANOSECONDS.toMillis(elapsedTime));

    }
}


