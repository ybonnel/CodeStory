package fr.ybonnel.codestory;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import fr.ybonnel.codestory.logs.DatabaseManager;
import net.sourceforge.jwebunit.html.Cell;
import net.sourceforge.jwebunit.html.Row;
import net.sourceforge.jwebunit.html.Table;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
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

    @SuppressWarnings("unchecked")
    @Test
    public void should_log_a_post_request() throws IOException, SAXException {
        WebConversation wc = new WebConversation();
        wc.setExceptionsThrownOnErrorStatus(false);
        PostMethodWebRequest request = new PostMethodWebRequest(getURL(), new ByteArrayInputStream("testpost".getBytes("UTF-8")),
                "plain/text");
        WebResponse response = wc.getResponse(request);
        assertEquals(404, response.getResponseCode());

        setBaseUrl(getURL());
        beginAt("/?q=log(Q)");
        assertResponseCode(200);
        assertTablePresent("log");
        Table table = getTable("log");
        ArrayList<Row> rows = table.getRows();
        // Three lines :
        // 1 for header
        // 1 for the log
        assertEquals(2, rows.size());
        Row logRow = rows.get(1);
        List<Cell> cells = logRow.getCells();
        assertEquals("Q", cells.get(1).getValue());
        assertTrue(cells.get(2).getValue().contains("plain/text"));
        assertTrue(cells.get(2).getValue().contains("testpost"));

    }

    @Test
    public void can_insert_enonce() throws IOException, SAXException, SQLException {
        Connection conn = DatabaseManager.INSTANCE.getDs().getConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("DROP TABLE ENONCE");
        conn.close();
        WebConversation wc = new WebConversation();
        wc.setExceptionsThrownOnErrorStatus(false);
        PostMethodWebRequest request = new PostMethodWebRequest(getURL() + "/enonce/1", WebServerTest.class.getResourceAsStream("/enonce1.markdown"),
                "plain/text");
        WebResponse response = wc.getResponse(request);
        assertEquals(200, response.getResponseCode());
    }

    @Test
    public void should_return_enonce() throws IOException, SAXException, SQLException {
        can_insert_enonce();
        setBaseUrl(getURL());
        beginAt("/enonce");
        assertResponseCode(200);
        assertEquals("<table id=\"enonces\" border=\"1\"><tr><th>ID</th><th>Enonc&eacute;</th></tr><tr><td>1</td><td><h1>Titre</h1><p>tutu <strong>tata</strong></p></td></tr></table>", getPageSource());
    }
}
