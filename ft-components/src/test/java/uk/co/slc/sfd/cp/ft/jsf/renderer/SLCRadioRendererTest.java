package uk.co.slc.sfd.cp.ft.jsf.renderer;

import java.io.IOException;
import java.util.Arrays;

import javax.el.ExpressionFactory;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.slc.sfd.cp.ft.jsf.MockFacesContext;

import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer.OptionComponentInfo;

public class SLCRadioRendererTest {

    private SLCRadioRenderer testee;

    private FacesContext mockContext;

    @Mock
    private ResponseWriter mockWriter;

    @Mock
    UISelectOne component;

    @Mock
    SelectItem mockSelectItem;

    @Mock
    OptionComponentInfo mockOptionComponentInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockContext = MockFacesContext.mockFacesContext();
        Mockito.when(mockContext.getResponseWriter()).thenReturn(mockWriter);
        Mockito.when(mockContext.getApplication().getExpressionFactory()).thenReturn(Mockito.mock(ExpressionFactory.class));
        testee = new SLCRadioRenderer();
    }

    @Test
    public void testRenderOption() throws IOException {

        //  Try with non null submitted SelectItem value
        Mockito.when(component.getSubmittedValue()).thenReturn("test");
        testee.renderOption(mockContext, component, null, mockSelectItem, null, null, true, 0, mockOptionComponentInfo);
        Mockito.verify(component, Mockito.never()).getValue();
    }

    @Test
    public void testRenderOptionWithNull() throws IOException {

        testee.renderOption(mockContext, component, null, mockSelectItem, null, null, false, 0, mockOptionComponentInfo);
        //  Check DIV rendered
        Mockito.verify(mockWriter, Mockito.times(1)).startElement("div", component);
        Mockito.verify(mockOptionComponentInfo, Mockito.times(1)).getEnabledClass();

        //  Try with disabled option
        Mockito.when(mockSelectItem.isDisabled()).thenReturn(true);
        Mockito.when(mockOptionComponentInfo.isDisabled()).thenReturn(true);
        testee.renderOption(mockContext, component, null, mockSelectItem, null, null, false, 0, mockOptionComponentInfo);
        Mockito.verify(mockOptionComponentInfo, Mockito.times(1)).getDisabledClass();
    }

    @Test
    public void testRenderOptionWithLabel() throws IOException {

        //  Try with non null submitted SelectItem value
        Mockito.when(component.getSubmittedValue()).thenReturn("test");
        Mockito.when(mockSelectItem.getLabel()).thenReturn("testLabel");
        testee.renderOption(mockContext, component, null, mockSelectItem, null, null, true, 0, mockOptionComponentInfo);
        Mockito.verify(component, Mockito.never()).getValue();
        Mockito.verify(mockWriter, Mockito.times(1)).writeText(" ", component, null);
    }

    @Test
    public void testRenderOptionWithArrayValue() throws IOException {

        //  Try with array submitted SelectItem value
        Mockito.when(component.getSubmittedValue()).thenReturn(new String[] { "test" });
        Mockito.when(mockSelectItem.getLabel()).thenReturn("testLabel");
        testee.renderOption(mockContext, component, null, mockSelectItem, null, null, true, 0, mockOptionComponentInfo);
        Mockito.verify(component, Mockito.never()).getValue();
        Mockito.verify(mockWriter, Mockito.times(1)).writeText(" ", component, null);
    }

    @Test
    public void testRenderOptionWithCollectionValue() throws IOException {

        //  Try with array submitted SelectItem value
        Mockito.when(component.getSubmittedValue()).thenReturn(Arrays.asList((new String[] { "test" })));
        Mockito.when(mockSelectItem.getLabel()).thenReturn("testLabel");
        testee.renderOption(mockContext, component, null, mockSelectItem, null, null, true, 0, mockOptionComponentInfo);
        Mockito.verify(component, Mockito.never()).getValue();
        Mockito.verify(mockWriter, Mockito.times(1)).writeText(" ", component, null);
    }

    @Test
    public void testRenderBeginText() throws IOException {
        testee.renderBeginText(component, 0, true, mockContext, false);
        //  Ensure we start a DIV
        Mockito.verify(mockWriter, Mockito.times(1)).startElement("div", component);
    }

    @Test
    public void testRenderEndText() throws IOException {
        testee.renderEndText(component, false, mockContext);
        //  Ensure we end the DIV
        Mockito.verify(mockWriter, Mockito.times(1)).endElement("div");
    }

}
