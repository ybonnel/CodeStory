package fr.ybonnel.codestory;


import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class ScalaskelTest extends WebServerTestUtil {

    @Test
    public void should_answer_to_1cent() throws IOException, SAXException {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/scalaskel/change/1");
        assertEquals(200, response.getResponseCode());
        assertEquals("[{\"foo\":1}]", response.getText());
    }

    @Test
    public void should_answer_to_7cent() throws IOException, SAXException {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/scalaskel/change/7");
        assertEquals(200, response.getResponseCode());
        assertEquals("[{\"foo\":7},{\"bar\":1}]", response.getText());
    }

    @Test
    public void should_answer_to_11cent() throws IOException, SAXException {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/scalaskel/change/11");
        assertEquals(200, response.getResponseCode());
        assertEquals("[{\"foo\":11},{\"foo\":4,\"bar\":1},{\"qix\":1}]", response.getText());
    }

    @Test
    public void should_answer_to_21cent() throws IOException, SAXException {
        WebConversation wc = new WebConversation();
        WebResponse response = wc.getResponse(getURL() + "/scalaskel/change/21");
        assertEquals(200, response.getResponseCode());
        assertEquals("[{\"foo\":21},{\"foo\":14,\"bar\":1},{\"foo\":10,\"qix\":1},{\"foo\":7,\"bar\":2},{\"foo\":3,\"bar\":1,\"qix\":1},{\"bar\":3},{\"baz\":1}]", response.getText());
    }


}
