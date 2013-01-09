package fr.ybonnel.codestory;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import net.sourceforge.jwebunit.html.Row;
import net.sourceforge.jwebunit.html.Table;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;

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
        // 1 for special log of unknown query
        assertEquals(3, rows.size());

        gotoPage("/?q=log(Q)");
        assertResponseCode(200);
        assertTablePresent("log");
        table = getTable("log");
        rows = table.getRows();
        assertEquals(2, rows.size());

        gotoPage("/?q=log(N)");
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
    public void should_answer_404_for_no_query() throws IOException, SAXException {
        WebConversation wc = new WebConversation();
        wc.setExceptionsThrownOnErrorStatus(false);
        WebResponse response = wc.getResponse(getURL());
        assertEquals(404, response.getResponseCode());
    }

    @Test
    public void should_answer_to_ml() throws IOException, SAXException {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/?q=Es+tu+abonne+a+la+mailing+list(OUI/NON)");
        assertEquals(200, response.getResponseCode());
        assertEquals("Response must be 'OUI'", "OUI", response.getText());
    }

    @Test
    public void should_answer_to_participate() throws IOException, SAXException {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/?q=Es tu heureux de participer(OUI/NON)");
        assertEquals(200, response.getResponseCode());
        assertEquals("Response must be 'OUI'", "OUI", response.getText());
    }

    @Test
    public void should_answer_to_markdown_ready() throws IOException, SAXException {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/?q=Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)");
        assertEquals(200, response.getResponseCode());
        assertEquals("Response must be 'OUI'", "OUI", response.getText());
    }

    @Test
    public void should_not_answer_always_yes() throws IOException, SAXException {
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
    public void should_return_enonce() throws IOException, SAXException, SQLException {
        can_insert_enonce();
        setBaseUrl(getURL());
        beginAt("/enonce");
        assertResponseCode(200);
        assertEquals("<table id=\"enonces\" border=\"1\"><tr><th>ID</th><th>Titre</th><th>Enonc&eacute;</th></tr><tr><td>1</td><td>Titre</td><td><p>=============</p><p>tutu <strong>tata</strong></p></td></tr></table>", getPageSource());
    }

    @Test
    public void should_manage_update_of_enonce() throws IOException, SAXException, SQLException {
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
        assertEquals("<table id=\"enonces\" border=\"1\"><tr><th>ID</th><th>Titre</th><th>Enonc&eacute;</th></tr><tr><td>1</td><td>Titre2</td><td><p>autreenonce</p></td></tr></table>", getPageSource());

    }

    @Test
    public void should_know_first_enonce() throws IOException, SAXException {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/?q=As tu bien recu le premier enonce(OUI/NON)");
        assertEquals(200, response.getResponseCode());
        assertEquals("Response must be 'NON'", "OUI", response.getText());
    }
}
