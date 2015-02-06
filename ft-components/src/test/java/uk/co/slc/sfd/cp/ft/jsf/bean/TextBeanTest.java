package uk.co.slc.sfd.cp.ft.jsf.bean;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PropertyResourceBundle;

import org.junit.Test;

/**
 */
public class TextBeanTest {

    private TextBean subject = new TextBean();

    @Test
    public void test() throws IOException {
        String props = "key=The {0} brown {1} jumped on {2, date,dd/MM/yyyy}";
        PropertyResourceBundle rb = new PropertyResourceBundle(new ByteArrayInputStream(props.getBytes()));
        String s = subject.render(rb, "key", "quick", "fox", getFormattedDate("25/02/2010"));
        assertEquals("The quick brown fox jumped on 25/02/2010", s);

        subject.toggleTokenReplace();
        s = subject.render(rb, "key", "quick", "fox", getFormattedDate("25/02/2010"));
        assertEquals("The {0} brown {1} jumped on {2, date,dd/MM/yyyy}", s);
    }

    public static Date getFormattedDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException(dateString, e);
        }
    }
}
