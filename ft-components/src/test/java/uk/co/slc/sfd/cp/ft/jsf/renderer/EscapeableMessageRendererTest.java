package uk.co.slc.sfd.cp.ft.jsf.renderer;

import java.io.IOException;

import javax.faces.component.html.HtmlMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.slc.sfd.cp.ft.jsf.MockFacesContext;

public class EscapeableMessageRendererTest {

    private EscapableMessageRenderer testee;

    private FacesContext mockContext;

    @Mock
    private ResponseWriter mockWriter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockContext = MockFacesContext.mockFacesContext();
        Mockito.when(mockContext.getResponseWriter()).thenReturn(mockWriter);
        testee = new EscapableMessageRenderer();
    }

    @Test
    public void testEncodeEnd() throws IOException {
        HtmlMessage component = new HtmlMessage();
        component.setFor("test");
        testee.encodeEnd(mockContext, component);
        Mockito.verify(mockContext, Mockito.times(2)).setResponseWriter(Mockito.any(ResponseWriter.class));
    }

}
