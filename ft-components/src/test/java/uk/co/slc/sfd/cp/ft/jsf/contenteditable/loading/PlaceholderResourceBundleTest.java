package uk.co.slc.sfd.cp.ft.jsf.contenteditable.loading;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.env.Environment;

import uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableUtils;

public class PlaceholderResourceBundleTest {

    @Mock
    private Environment mockEnv;
    @Mock
    FacesContext mockContext;
    @Mock
    Application mockApplication;
    static int placeholderNumber;

    private PlaceholderResourceBundleControl control;
    private PlaceholderResourceBundle bundle;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        FacesContextHelper.setFacesContext(mockContext);
        when(mockContext.getApplication()).thenReturn(mockApplication);
        placeholderNumber = 0;
        ContentEditableUtils.setSpringEnvironement(mockEnv);
        doReturn(null).when(mockEnv).getProperty(eq("propertiesDirectory"));
        control = new PlaceholderResourceBundleControl();
        control.setEnabled(true);
        when(mockApplication.evaluateExpressionGet(mockContext, "#{placeholderResourceBundleControl}", PlaceholderResourceBundleControl.class)).thenReturn(control);
        bundle = new PlaceholderResourceBundle();
    }

    @Test
    public void replacementIgnoresNormalPlaceholders() {
        when(mockApplication.evaluateExpressionGet(eq(mockContext), anyString(), eq(Object.class))).thenReturn("TEST_PLACEHOLDER_0");

        String actual = (String) bundle.changeAnswerHook("key", "testing {0} normal placeholder");
        assertEquals("testing {0} normal placeholder", actual);
    }

    @Test
    public void replacementForOnePlaceholder() {
        when(mockApplication.evaluateExpressionGet(eq(mockContext), anyString(), eq(Object.class))).thenAnswer(new Answer<String>() {

            public String answer(InvocationOnMock invocation) {
                return "TEST_PLACEHOLDER_" + PlaceholderResourceBundleTest.placeholderNumber++;
            }
        });
        PlaceholderResourceBundle bundle = new PlaceholderResourceBundle();
        String actual = (String) bundle.changeAnswerHook("key", "testing {{first}} one placeholder");
        assertEquals("testing TEST_PLACEHOLDER_0 one placeholder", actual);
    }

    @Test
    public void replacementForMultiplePlaceholders() {
        when(mockApplication.evaluateExpressionGet(eq(mockContext), anyString(), eq(Object.class))).thenAnswer(new Answer<String>() {

            public String answer(InvocationOnMock invocation) {
                return "TEST_PLACEHOLDER_" + PlaceholderResourceBundleTest.placeholderNumber++;
            }
        });
        PlaceholderResourceBundle bundle = new PlaceholderResourceBundle();
        String actual = (String) bundle.changeAnswerHook("key", "testing {{first}} multiple placeholders {{second}}");
        assertEquals("testing TEST_PLACEHOLDER_0 multiple placeholders TEST_PLACEHOLDER_1", actual);
    }

    @Test
    public void replacementForMultipleIdenticalPlaceholders() {
        when(mockApplication.evaluateExpressionGet(eq(mockContext), anyString(), eq(Object.class))).thenAnswer(new Answer<String>() {

            public String answer(InvocationOnMock invocation) {
                return "TEST_PLACEHOLDER_" + PlaceholderResourceBundleTest.placeholderNumber++;
            }
        });
        PlaceholderResourceBundle bundle = new PlaceholderResourceBundle();
        String actual = (String) bundle.changeAnswerHook("key", "testing {{first}} multiple placeholders {{first}}");
        assertEquals("testing TEST_PLACEHOLDER_0 multiple placeholders TEST_PLACEHOLDER_0", actual);
    }

    @Test
    public void replacementForUnfoundPlaceholder() {
        when(mockApplication.evaluateExpressionGet(eq(mockContext), anyString(), eq(Object.class))).thenAnswer(new Answer<String>() {

            public String answer(InvocationOnMock invocation) {
                return null;
            }
        });
        PlaceholderResourceBundle bundle = new PlaceholderResourceBundle();
        String actual = (String) bundle.changeAnswerHook("key", "testing {{first}} unfound placeholder");
        assertEquals("testing false unfound placeholder", actual);
    }
}
