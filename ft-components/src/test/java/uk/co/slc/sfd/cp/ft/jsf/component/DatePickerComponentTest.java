package uk.co.slc.sfd.cp.ft.jsf.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.slc.sfd.cp.ft.jsf.MockFacesContext;

public class DatePickerComponentTest {

    private DatePickerComponent testee;
    private FacesContext mockContext;

    @Mock
    private HtmlSelectOneMenu dayComponent;

    @Mock
    private HtmlSelectOneMenu monthComponent;

    @Mock
    private HtmlSelectOneMenu yearComponent;

    private UISelectItems dayItems;
    private UISelectItems monthItems;
    private UISelectItems yearItems;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockContext = MockFacesContext.mockFacesContext();
        testee = new DatePickerComponent();
        mockContext.getViewRoot().getChildren().add(testee);
        testee.setDayComponent(dayComponent);
        testee.setMonthComponent(monthComponent);
        testee.setYearComponent(yearComponent);

        //  create SelectItems
        dayItems = new UISelectItems();
        dayItems.setId("dayItems");
        monthItems = new UISelectItems();
        monthItems.setId("monthItems");
        yearItems = new UISelectItems();
        yearItems.setId("yearItems");

        Mockito.when(dayComponent.findComponent("dayItems")).thenReturn(dayItems);
        Mockito.when(monthComponent.findComponent("monthItems")).thenReturn(monthItems);
        Mockito.when(yearComponent.findComponent("yearItems")).thenReturn(yearItems);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEncodeBegin() throws IOException {
        testee.setShowDefault(true);
        testee.setFromInterval(10);
        testee.setToInterval(10);
        testee.setDescending(true);
        testee.encodeBegin(mockContext);

        //  General check to ensure selectItems are at least populated
        List<SelectItem> yearSelectItems = (List<SelectItem>) yearItems.getValue();
        assertTrue(yearSelectItems.size() > 0);
        List<SelectItem> monthSelectItems = (List<SelectItem>) monthItems.getValue();
        assertTrue(monthSelectItems.size() > 0);
        List<SelectItem> daySelectItems = (List<SelectItem>) dayItems.getValue();
        assertTrue(daySelectItems.size() > 0);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testYearDropdown() throws IOException {
        testee.setShowDefault(false);
        testee.setFromInterval(10);
        testee.setToInterval(10);
        testee.setDescending(true);
        testee.encodeBegin(mockContext);

        //  Check we have the correct items in year dropdown
        List<SelectItem> yearSelectItems = (List<SelectItem>) yearItems.getValue();
        //  Check 10 years prior to current year and 10 years after = 21
        assertTrue(yearSelectItems.size() == 21);
        //  Check first item is current year plus 10 years
        assertEquals((Integer) yearSelectItems.get(0).getValue(), getCurrentYearPlus(10));
        //  Check last item is current year minus 10 years
        assertEquals((Integer) yearSelectItems.get(20).getValue(), getCurrentYearPlus(-10));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMonthDropdown() throws IOException {
        testee.setShowDefault(false);
        testee.setFromInterval(10);
        testee.setToInterval(10);
        testee.setDescending(true);
        testee.encodeBegin(mockContext);

        //  Check we have the correct items in month dropdown
        List<SelectItem> monthSelectItems = (List<SelectItem>) monthItems.getValue();
        //  Check we have 12 months
        assertTrue(monthSelectItems.size() == 12);
        //  Check first item is January
        assertEquals((Integer) monthSelectItems.get(0).getValue(), new Integer(1));
        //  Check last item is December
        assertEquals((Integer) monthSelectItems.get(11).getValue(), new Integer(12));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDayDropdown() throws IOException {
        testee.setShowDefault(false);
        testee.setFromInterval(10);
        testee.setToInterval(10);
        testee.setDescending(true);
        testee.encodeBegin(mockContext);

        //  Check we have the correct items in month dropdown
        List<SelectItem> daySelectItems = (List<SelectItem>) dayItems.getValue();
        //  Check we have 31 days
        assertTrue(daySelectItems.size() == 31);
        //  Check first item is day 1
        assertEquals((Integer) daySelectItems.get(0).getValue(), new Integer(1));
        //  Check first item is day 31
        assertEquals((Integer) daySelectItems.get(30).getValue(), new Integer(31));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDefaultSelectItems() throws IOException {
        testee.setShowDefault(true);
        testee.setFromInterval(10);
        testee.setToInterval(10);
        testee.setDescending(true);
        testee.encodeBegin(mockContext);

        //  Check first item is null default item
        List<SelectItem> yearSelectItems = (List<SelectItem>) yearItems.getValue();
        assertNull(yearSelectItems.get(0).getValue());
        List<SelectItem> monthSelectItems = (List<SelectItem>) monthItems.getValue();
        assertNull(monthSelectItems.get(0).getValue());
        List<SelectItem> daySelectItems = (List<SelectItem>) dayItems.getValue();
        assertNull(daySelectItems.get(0).getValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAcendingSelectItems() throws IOException {
        testee.setShowDefault(true);
        testee.setFromInterval(5);
        testee.setToInterval(5);
        testee.setDescending(false);
        testee.encodeBegin(mockContext);

        List<SelectItem> yearSelectItems = (List<SelectItem>) yearItems.getValue();
        assertTrue(yearSelectItems.size() == 12);
        //  Check first item is null default item
        assertNull(yearSelectItems.get(0).getValue());
        //  Check second item is current year - 5
        assertEquals((Integer) yearSelectItems.get(1).getValue(), getCurrentYearPlus(-5));
        //  Check second item is current year + 5
        assertEquals((Integer) yearSelectItems.get(11).getValue(), getCurrentYearPlus(5));
    }

    @Test
    public void testGetSubmittedValue() {
        Mockito.when(dayComponent.getSubmittedValue()).thenReturn("6");
        Mockito.when(monthComponent.getSubmittedValue()).thenReturn("10");
        Mockito.when(yearComponent.getSubmittedValue()).thenReturn("2013");

        String submittedValue = (String) testee.getSubmittedValue();
        assertTrue(submittedValue.equals("6/10/2013"));
    }

    @Test
    public void testGetConvertedValue() {
        Date convertedValue = (Date) testee.getConvertedValue(mockContext, "6/10/2013");

        Calendar convertedValueCal = new GregorianCalendar();
        convertedValueCal.setTime(convertedValue);
        Calendar compareToCal = getTestDate(6, 10, 2013);
        assertEquals(convertedValueCal.get(Calendar.YEAR), compareToCal.get(Calendar.YEAR));
        assertEquals(convertedValueCal.get(Calendar.MONTH), compareToCal.get(Calendar.MONTH));
        assertEquals(convertedValueCal.get(Calendar.DAY_OF_MONTH), compareToCal.get(Calendar.DAY_OF_MONTH));
    }

    @Test(expected = ConverterException.class)
    public void testInvalidConvertedValue1() {
        testee.getConvertedValue(mockContext, "wibble");
    }

    @Test(expected = ConverterException.class)
    public void testInvalidConvertedValue2() {
        //  31st Feb
        testee.getConvertedValue(mockContext, "31/2/2013");
    }

    private Integer getCurrentYearPlus(int addToCurrentYear) {
        Calendar cal = new GregorianCalendar();
        int thisYear = cal.get(Calendar.YEAR);
        return new Integer(thisYear + addToCurrentYear);
    }

    private Calendar getTestDate(int day, int month, int year) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

}
