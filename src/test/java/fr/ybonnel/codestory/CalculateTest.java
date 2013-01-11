package fr.ybonnel.codestory;


import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CalculateTest extends WebServerTestUtil {

    @Test
    public void should_answer_to_1plus1() throws IOException, SAXException {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/?q=1+1");
        assertEquals(200, response.getResponseCode());
        assertEquals("2", response.getText());
    }



}
