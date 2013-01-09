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


}
