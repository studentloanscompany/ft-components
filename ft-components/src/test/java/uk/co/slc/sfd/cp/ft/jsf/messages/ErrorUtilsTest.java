package uk.co.slc.sfd.cp.ft.jsf.messages;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@PrepareForTest(FacesContext.class)
@RunWith(PowerMockRunner.class)
public class ErrorUtilsTest {

    @Mock
    private FacesMessage message;
    @Mock
    private FacesContext mockFacesContext;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(FacesContext.class);
        initMocks(this);

        when(message.getDetail()).thenReturn("testMessage");
    }

    @Test
    public void testAddFacesMessageAddsToContext() {
        //hand back the mock(ito) instance of facesContext
        PowerMockito.when(FacesContext.getCurrentInstance()).thenReturn(mockFacesContext);

        //call the method on the class under test
        ErrorUtils.addFacesMessage("id", message);

        //verify (assert) that the ErrorUtils added the message (once) to the FacesContext 
        //with the parms we expect
        Mockito.verify(mockFacesContext, times(1)).addMessage("id", message);

    }

    @Test
    public void testCreateFacesErrorMessage() {
        String messageString = "testCreateErrorMessage";
        FacesMessage actual = ErrorUtils.createFacesErrorMessage(messageString);
        assertEquals(FacesMessage.SEVERITY_ERROR, actual.getSeverity());
        assertEquals(messageString, actual.getDetail());
        assertEquals(messageString, actual.getSummary());
    }

    @Test
    public void testCreateFacesMessage() {
        String messageString = "testCreateFacesMessage";
        Severity severity = FacesMessage.SEVERITY_WARN;
        FacesMessage actual = ErrorUtils.createFacesMessage(messageString, severity);
        assertEquals(severity, actual.getSeverity());
        assertEquals(messageString, actual.getDetail());
        assertEquals(messageString, actual.getSummary());
    }
}
