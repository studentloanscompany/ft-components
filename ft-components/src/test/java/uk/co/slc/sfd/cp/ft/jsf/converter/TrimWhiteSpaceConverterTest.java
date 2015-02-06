package uk.co.slc.sfd.cp.ft.jsf.converter;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;

public class TrimWhiteSpaceConverterTest {

    //class under test
    private TrimWhiteSpaceConverter trimWhiteSpaceConverter;

    @Mock
    FacesContext mockFacesContext;
    @Mock
    UIComponent mockUIComponent;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(FacesContext.class);
        trimWhiteSpaceConverter = new TrimWhiteSpaceConverter();
    }

    @Test
    public void testGetAsObject() throws IOException {

        String stringWithWhitespace = (String) trimWhiteSpaceConverter.getAsObject(mockFacesContext, mockUIComponent, " 123 ");
        assertTrue(stringWithWhitespace.equals("123"));

        String stringWithoutWhitespace = (String) trimWhiteSpaceConverter.getAsObject(mockFacesContext, mockUIComponent, "123");
        assertTrue(stringWithoutWhitespace.equals("123"));

        String emptySpace = (String) trimWhiteSpaceConverter.getAsObject(mockFacesContext, mockUIComponent, "    ");
        assertTrue(StringUtils.isEmpty(emptySpace));

        Object nullObject = trimWhiteSpaceConverter.getAsObject(mockFacesContext, mockUIComponent, null);
        assertTrue(nullObject == null);
    }

    @Test
    public void testGetAsString() throws IOException {

        String stringWithWhitespace = trimWhiteSpaceConverter.getAsString(mockFacesContext, mockUIComponent, " 123 ");
        assertTrue(stringWithWhitespace.equals(" 123 "));

        String nullString = trimWhiteSpaceConverter.getAsString(mockFacesContext, mockUIComponent, null);
        assertTrue(nullString == null);
    }

}
