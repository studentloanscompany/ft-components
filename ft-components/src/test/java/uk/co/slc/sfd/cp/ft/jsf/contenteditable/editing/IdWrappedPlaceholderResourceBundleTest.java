package uk.co.slc.sfd.cp.ft.jsf.contenteditable.editing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableUtils;

public class IdWrappedPlaceholderResourceBundleTest {

    @Mock
    private Environment mockEnv;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ContentEditableUtils.setSpringEnvironement(mockEnv);
    }

    @Test
    public void test_wrapped_notInDevelopmentMode_doesNothing() {
        doReturn(null).when(mockEnv).getProperty(anyString());
        IdWrappedPlaceholderResourceBundle resourceBundle = new IdWrappedPlaceholderResourceBundle();
        assertEquals("sample text", resourceBundle.wrapped("key", "sample text"));
    }

    @Test
    public void test_wrapped_inDevelopmentMode_addsASpan() {
        //Need to set the name of the property in case it has been changed elsewhere
        doReturn("aDirectory").when(mockEnv).getProperty(anyString());
        IdWrappedPlaceholderResourceBundle resourceBundle = new IdWrappedPlaceholderResourceBundle();
        assertEquals("<span data-messageid='key'>sample text</span>", resourceBundle.wrapped("key", "sample text"));
    }

}
