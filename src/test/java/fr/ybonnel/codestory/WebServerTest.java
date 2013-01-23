package fr.ybonnel.codestory;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import fr.ybonnel.codestory.util.LogUtil;
import net.sourceforge.jwebunit.html.Row;
import net.sourceforge.jwebunit.html.Table;
import org.junit.Test;
import org.xml.sax.SAXException;
import sun.misc.Signal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WebServerTest extends WebServerTestUtil {

    @Test
    public void should_answer_to_whatsyourmail() throws Exception {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/?q=Quelle+est+ton+adresse+email");
        assertEquals(200, response.getResponseCode());
        assertEquals("Response must be my mail", "ybonnel@gmail.com", response.getText());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_generate_logs() throws Exception {
        getTester().getTestingEngine().setIgnoreFailingStatusCodes(true);
        setBaseUrl(getURL());
        beginAt("/?q=test");
        assertResponseCode(404);
        assertEquals("Query test is unknown", getPageSource());

        gotoPage("/?q=log");
        assertResponseCode(200);
        assertTablePresent("log");
        Table table = getTable("log");
        ArrayList<Row> rows = table.getRows();
        // Three lines :
        // 1 for header
        // 1 for normal log of query
        assertEquals(2, rows.size());

        gotoPage("/?q=log(Q)");
        assertResponseCode(200);
        assertTablePresent("log");
        table = getTable("log");
        rows = table.getRows();
        assertEquals(2, rows.size());

        gotoPage("?q=logtutu");
        assertResponseCode(404);
        assertEquals("Query logtutu is unknown", getPageSource());
    }

    @Test
    public void should_answer_404_for_no_query() throws Exception {
        WebConversation wc = new WebConversation();
        wc.setExceptionsThrownOnErrorStatus(false);
        WebResponse response = wc.getResponse(getURL());
        assertEquals(404, response.getResponseCode());
    }

    @Test
    public void should_answer_to_ml() throws Exception {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/?q=Es+tu+abonne+a+la+mailing+list(OUI/NON)");
        assertEquals(200, response.getResponseCode());
        assertEquals("Response must be 'OUI'", "OUI", response.getText());
    }

    @Test
    public void should_answer_to_participate() throws Exception {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/?q=Es tu heureux de participer(OUI/NON)");
        assertEquals(200, response.getResponseCode());
        assertEquals("Response must be 'OUI'", "OUI", response.getText());
    }

    @Test
    public void should_answer_to_markdown_ready() throws Exception {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/?q=Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)");
        assertEquals(200, response.getResponseCode());
        assertEquals("Response must be 'OUI'", "OUI", response.getText());
    }

    @Test
    public void should_not_answer_always_yes() throws Exception {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/?q=Est ce que tu reponds toujours oui(OUI/NON)");
        assertEquals(200, response.getResponseCode());
        assertEquals("Response must be 'NON'", "NON", response.getText());
    }

    @Test
    public void can_insert_enonce() throws IOException, SAXException, SQLException {
        WebConversation wc = new WebConversation();
        wc.setExceptionsThrownOnErrorStatus(false);
        PostMethodWebRequest request = new PostMethodWebRequest(getURL() + "/enonce/1");
        request.setParameter("Titre", "=============\n\ntutu\n**tata**");
        WebResponse response = wc.getResponse(request);
        assertEquals(201, response.getResponseCode());
    }

    @Test
    public void should_return_enonce() throws Exception {
        can_insert_enonce();
        setBaseUrl(getURL());
        beginAt("/enonce");
        assertResponseCode(200);
        assertEquals("<table id=\"enonces\" border=\"1\"><tr><th>ID</th><th>Titre</th><th>Enonc&eacute;</th></tr><tr><td>1</td><td>Enonce 1</td><td><p>Titre=%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%0A%0Atutu%0A<strong>tata</strong></p></td></tr></table>", getPageSource());
    }

    @Test
    public void should_manage_update_of_enonce() throws Exception {
        can_insert_enonce();
        WebConversation wc = new WebConversation();
        wc.setExceptionsThrownOnErrorStatus(false);
        PostMethodWebRequest request = new PostMethodWebRequest(getURL() + "/enonce/1");
        request.setParameter("Titre2", "autreenonce");
        WebResponse response = wc.getResponse(request);
        assertEquals(201, response.getResponseCode());

        setBaseUrl(getURL());
        beginAt("/enonce");
        assertResponseCode(200);
        assertEquals("<table id=\"enonces\" border=\"1\"><tr><th>ID</th><th>Titre</th><th>Enonc&eacute;</th></tr><tr><td>1</td><td>Enonce 1</td><td><p>Titre2=autreenonce</p></td></tr></table>", getPageSource());

    }

    @Test
    public void should_know_first_enonce() throws Exception {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/?q=As tu bien recu le premier enonce(OUI/NON)");
        assertEquals(200, response.getResponseCode());
        assertEquals("Response must be 'OUI'", "OUI", response.getText());
    }

    @Test
    public void should_know_second_enonce() throws Exception {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/?q=As tu bien recu le second enonce(OUI/NON)");
        assertEquals(200, response.getResponseCode());
        assertEquals("Response must be 'OUI'", "OUI", response.getText());
    }



    @Test
    public void should_stop_with_sigTerm() throws Exception {
        int port = getRandomPort();
        final String[] params = {Integer.toString(port)};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WebServer.main(params);
                } catch (Exception e) {
                    e.printStackTrace();
                    fail(e.getMessage());
                }
            }
        });

        thread.start();

        Thread.sleep(100);

        // Thread must be alive
        assertTrue(thread.isAlive());

        // Serveur must respond.
        should_answer_to_whatsyourmail();

        // Sent of SIGTERM
        Signal.raise(new Signal("TERM"));

        // Waiting server stopped (if it's not stopped after 1 second, there is a problem).
        for (int i = 0; i < 10 && thread.isAlive(); i++) {
            Thread.sleep(100);
        }

        // Thread must be dead.
        assertFalse(thread.isAlive());
    }

    @Test
    public void should_have_good_night() throws IOException, SAXException {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/?q=As tu passe une bonne nuit malgre les bugs de l etape precedente(PAS_TOP/BOF/QUELS_BUGS)");
        assertEquals(200, response.getResponseCode());
        assertEquals("BOF", response.getText());
    }

    @Test
    public void should_not_copy() throws IOException, SAXException {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/?q=As tu copie le code de ndeloof(OUI/NON/JE_SUIS_NICOLAS)");
        assertEquals(200, response.getResponseCode());
        assertEquals("NON", response.getText());
    }





}
