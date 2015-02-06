package uk.co.slc.sfd.cp.ft.jsf.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/** 
 * Class to test Message Resource keys.
 * Do not write tests for specific text from the resource bundle as this is subject to change
 * 
@author slc
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ FacesContext.class, ResourceBundle.class })
public class MessageProviderTest {

    @Mock
    ResourceBundle mockResourceBundle;
    @Mock
    Application mockApplication;
    @Mock
    FacesContext mockFacesContext;

    @Before
    public void setup() {

        //creates a Powermock object for the static calls. 
        PowerMockito.mockStatic(FacesContext.class);
        PowerMockito.mockStatic(ResourceBundle.class);

        //creates Mockito instances of the @Mock annotated members above.
        initMocks(this);

        //get the static method call to FacesContext sorted with PowerMockito to make sure it returns a mock instance of the Faces application.	
        PowerMockito.when(FacesContext.getCurrentInstance()).thenReturn(mockFacesContext);
        PowerMockito.when(FacesContext.getCurrentInstance().getApplication()).thenReturn(mockApplication);

        //back to using Mockito for the behaviour of mocked ResouceBundle (NB! this will not support the getString(key) method since getString(..) is final
        Mockito.when(mockApplication.getResourceBundle((FacesContext) any(), anyString())).thenReturn(mockResourceBundle);
    }

    @Test
    public void testContainsKey() {
        String validKey = "key";
        Mockito.when(mockResourceBundle.containsKey(validKey)).thenReturn(true);
        assertTrue(MessageProvider.containsKey(validKey));
    }

    @Test
    public void testDoesNotContainKey() {
        String invalidKey = "_invali_dKey";
        Mockito.when(mockResourceBundle.containsKey(invalidKey)).thenReturn(false);
        assertFalse(MessageProvider.containsKey(invalidKey));
    }

    @Test
    public void testInvalidKeyWithFormatArgs() {
        String invalidKey = "key";
        String actual = MessageProvider.getValue(invalidKey, MessageProvider.BUNDLE_LOOKUP_FAILURE_FORMAT);
        Mockito.when(mockResourceBundle.containsKey(invalidKey)).thenReturn(false);
        String expected = String.format(MessageProvider.BUNDLE_LOOKUP_FAILURE_FORMAT, invalidKey);
        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidKey() {
        String value = MessageProvider.getValue("_invali_dKey");
        assertEquals(String.format(MessageProvider.BUNDLE_LOOKUP_FAILURE_FORMAT, "_invali_dKey"), value);
    }

}
