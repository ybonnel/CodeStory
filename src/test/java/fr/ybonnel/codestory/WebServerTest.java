package fr.ybonnel.codestory;

import net.sourceforge.jwebunit.html.Row;
import net.sourceforge.jwebunit.html.Table;
import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public class WebServerTest extends WebServerTestUtil {

    @Test
    public void should_answer_to_whatsyourmail() throws Exception {
        beginAt("/?q=Quelle+est+ton+adresse+email");
        assertResponseCode(200);
        assertEquals("Response must be my mail", "ybonnel@gmail.com", getPageSource());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_generate_logs() throws Exception {
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
    public void should_answer_404_for_no_query() {
        beginAt("/");
        assertResponseCode(404);
    }

    @Test
    public void should_answer_to_ml() {
        beginAt("/?q=Es+tu+abonne+a+la+mailing+list(OUI/NON)");
        assertResponseCode(200);
        assertEquals("Response must be 'OUI'", "OUI", getPageSource());
    }

    @Test
    public void should_answer_to_participate() {
        beginAt("/?q=Es tu heureux de participer(OUI/NON)");
        assertResponseCode(200);
        assertEquals("Response must be 'OUI'", "OUI", getPageSource());
    }
}
