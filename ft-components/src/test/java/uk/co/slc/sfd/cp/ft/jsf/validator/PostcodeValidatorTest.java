package uk.co.slc.sfd.cp.ft.jsf.validator;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.co.slc.sfd.cp.ft.jsf.messages.MessageProvider;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ FacesContext.class, MessageProvider.class })
public class PostcodeValidatorTest {

    /**
     * Class under test
     */
    private PostcodeValidator validator;

    @Mock
    private UIInput postcodeUiInput;

    @Mock
    private FacesContext mockFacesContext;

    @Mock
    HttpServletResponse mockHttpServletResponse;

    @Mock
    HttpServletResponse mockHttpServletRequest;

    @Mock
    ExternalContext mockExternalContext;

    @Mock
    HttpSession mockHttpSession;

    @Before
    public void setUp() throws Exception {
        validator = new PostcodeValidator();
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(FacesContext.class);
        PowerMockito.mockStatic(MessageProvider.class);
        PowerMockito.when(FacesContext.getCurrentInstance()).thenReturn(mockFacesContext);
        Mockito.when(mockFacesContext.getExternalContext()).thenReturn(mockExternalContext);
        Mockito.when(mockExternalContext.getResponse()).thenReturn(mockHttpServletResponse);
        Mockito.when(mockExternalContext.getSession(false)).thenReturn(mockHttpSession);

    }

    @Test
    public void testValidUKPostcode() {
        validator.validate(mockFacesContext, postcodeUiInput, "G1 1DW");
    }

    @Test(expected = ValidatorException.class)
    public void testInvalidUKPostcode() {
        validator.validate(mockFacesContext, postcodeUiInput, "12345");
    }

    @Test
    public void testValidNonUKPostcode() {
        Map<String, Object> dummyMap = new HashMap<String, Object>();
        dummyMap.put("country", "INDIA");
        Mockito.when(postcodeUiInput.getAttributes()).thenReturn(dummyMap);
        validator.validate(mockFacesContext, postcodeUiInput, "12345");
    }

    @Test(expected = ValidatorException.class)
    public void testInValidNonUKPostcode() {
        Map<String, Object> dummyMap = new HashMap<String, Object>();
        dummyMap.put("country", "INDIA");
        Mockito.when(postcodeUiInput.getAttributes()).thenReturn(dummyMap);
        validator.validate(mockFacesContext, postcodeUiInput, "£$%£%£");
    }

}