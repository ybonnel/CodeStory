package fr.ybonnel.codestory;

import org.junit.ClassRule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public class WebServerTest {

    @SuppressWarnings("UnusedDeclaration")
    public static @ClassRule WebServerRule server = new WebServerRule();


    @Test
    public void should_answer_to_whatsyourmail() throws Exception {
        beginAt("/?q=Quelle+est+ton+adresse+email");
        assertResponseCode(200);
        assertEquals("Response must be my mail", "ybonnel@gmail.com", getPageSource());
    }
}
