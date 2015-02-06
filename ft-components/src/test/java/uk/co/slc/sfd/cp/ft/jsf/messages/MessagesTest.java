package uk.co.slc.sfd.cp.ft.jsf.messages;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MessagesTest {

    Messages testee = new Messages();

    @Test
    public void testMessages() {

        testee.add("someName", "Some Message", "some args");
        assertTrue(testee.getMessageMap().size() == 1);
        assertTrue(testee.getEntry("someName").getProperty().equals("Some Message"));
        testee.clear();
        assertTrue(testee.getMessageMap().isEmpty());
    }

}
