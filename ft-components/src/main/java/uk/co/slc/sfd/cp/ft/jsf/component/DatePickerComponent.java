package uk.co.slc.sfd.cp.ft.jsf.component;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import uk.co.slc.sfd.cp.ft.jsf.messages.MessageProvider;

/**
 * Custom JSF date picker component. This is a hybrid custom / composite
 * component referenced by the componentType attribute of the compositeInterface
 * of datePicker.xhtml. Doing it this way removes the need for a custom
 * renderer, simplifies the Java component code and removes the need for nasty
 * hacks to support ui:repeat
 * 
@author slc
 * 
 */
@FacesComponent(value = "components.DatePickerComponent")
public class DatePickerComponent extends UIInput implements NamingContainer {

    private static final String SELECT_ITEMS_YEAR_ID = "yearItems";
    private static final String SELECT_ITEMS_MONTH_ID = "monthItems";
    private static final String SELECT_ITEMS_DAY_ID = "dayItems";
    private static final int MAX_NUMBER_OF_DAYS_IN_ANY_MONTH = 31;
    private static final int TOTAL_NUMBER_OF_MONTHS = 12;
    //  Bug in JSF prevents us adding default value in composite component
    private static final Integer DEFAULT_TO_INTERVAL = 10;
    private static final Integer DEFAULT_FROM_INTERVAL = 10;
    private static final Boolean DEFAULT_SHOW_DEFAULT = true;
    private static final Boolean DEFAULT_DESCENDING = true;

    /**
     * Component binding for day dropdown
     */
    private HtmlSelectOneMenu dayComponent;

    /**
     * Component binding for month dropdown
     */
    private HtmlSelectOneMenu monthComponent;

    /**
     * Component binding for year dropdown
     */
    private HtmlSelectOneMenu yearComponent;

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.UIInput#getFamily()
     */
    @Override
    public String getFamily() {
        // As we use a composite component for rendering the Component family
        // must be a Naming Container
        return UINamingContainer.COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {

        // Dynamically generate dropdown selectItems based on component
        // attributes
        UISelectItems dayItems = (UISelectItems) dayComponent.findComponent(SELECT_ITEMS_DAY_ID);
        dayItems.setValue(getDays());

        UISelectItems monthItems = (UISelectItems) monthComponent.findComponent(SELECT_ITEMS_MONTH_ID);
        monthItems.setValue(getMonths());

        UISelectItems yearItems = (UISelectItems) yearComponent.findComponent(SELECT_ITEMS_YEAR_ID);
        yearItems.setValue(getYears());

        // set dropdowns to values in bound date in model
        Date dateValue = (Date) getValue();
        if (dateValue != null) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(dateValue);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            dayComponent.setValue(day);
            int month = cal.get(Calendar.MONTH);
            monthComponent.setValue(month + 1);
            int year = cal.get(Calendar.YEAR);
            yearComponent.setValue(year);
        } else {
            dayComponent.setValue(null);
            monthComponent.setValue(null);
            yearComponent.setValue(null);
        }
        super.encodeBegin(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.UIInput#getSubmittedValue()
     */
    @Override
    public Object getSubmittedValue() {
        // Take the parts of the date from the dropdowns and join them up into a
        // String
        String dateAsString = dayComponent.getSubmittedValue() + "/" + monthComponent.getSubmittedValue() + "/" + yearComponent.getSubmittedValue();

        return dateAsString;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.UIInput#getConvertedValue(javax.faces.context.
     * FacesContext, java.lang.Object)
     */
    @Override
    protected Object getConvertedValue(FacesContext context, Object newSubmittedValue) {
        // Try and convert the String date to a real date and throw conversion error if it doesn't work
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            return sdf.parse((String) newSubmittedValue);
        } catch (ParseException e) {
            throw new ConverterException(e);
        }

    }

    //  Getters and setters for attributes requiring state saving
    public Boolean getShowDefault() {
        Boolean showDefault = (Boolean) getStateHelper().eval("showDefault");
        return showDefault == null ? DEFAULT_SHOW_DEFAULT : showDefault;
    }

    public void setShowDefault(Boolean showDefault) {
        getStateHelper().put("showDefault", showDefault);
    }

    public Boolean getDescending() {
        Boolean descending = (Boolean) getStateHelper().eval("descending");
        return descending == null ? DEFAULT_DESCENDING : descending;
    }

    public void setDescending(Boolean descending) {
        getStateHelper().put("descending", descending);
    }

    public Integer getReferenceYear() {
        return (Integer) getStateHelper().eval("referenceYear");
    }

    public void setReferenceYear(Integer referenceYear) {
        getStateHelper().put("referenceYear", referenceYear);
    }

    public Integer getToInterval() {
        Integer toInterval = (Integer) getStateHelper().eval("toInterval");
        return toInterval == null ? DEFAULT_TO_INTERVAL : toInterval;
    }

    public void setToInterval(Integer toInterval) {
        getStateHelper().put("toInterval", toInterval);
    }

    public Integer getFromInterval() {
        Integer fromInterval = (Integer) getStateHelper().eval("fromInterval");
        return fromInterval == null ? DEFAULT_FROM_INTERVAL : fromInterval;
    }

    public void setFromInterval(Integer fromInterval) {
        getStateHelper().put("fromInterval", fromInterval);
    }

    //  Component binding getters and setters

    public HtmlSelectOneMenu getDayComponent() {
        return dayComponent;
    }

    public void setDayComponent(HtmlSelectOneMenu dayComponent) {
        this.dayComponent = dayComponent;
    }

    public HtmlSelectOneMenu getMonthComponent() {
        return monthComponent;
    }

    public void setMonthComponent(HtmlSelectOneMenu monthComponent) {
        this.monthComponent = monthComponent;
    }

    public HtmlSelectOneMenu getYearComponent() {
        return yearComponent;
    }

    public void setYearComponent(HtmlSelectOneMenu yearComponent) {
        this.yearComponent = yearComponent;
    }

    /**
     * Gets the days.
     * 
     * @return the days
     */
    private List<SelectItem> getDays(int from, int to) {
        List<SelectItem> selectItems = new ArrayList<SelectItem>(MAX_NUMBER_OF_DAYS_IN_ANY_MONTH);
        Integer[] dayArray = getAsArray(from, to);

        for (Integer day : dayArray) {
            selectItems.add(new SelectItem(day, day.toString()));
        }

        if (getShowDefault() != null && getShowDefault()) {
            addDefaultSelectItem(selectItems, MessageProvider.getMessageValue("Day"));
        }

        return selectItems;
    }

    /**
     * Gets the days.
     * 
     * @param from
     *            the from
     * @param to
     *            the to
     * @return the days
     */
    private List<SelectItem> getDays() {
        return getDays(1, MAX_NUMBER_OF_DAYS_IN_ANY_MONTH);
    }

    /**
     * Gets the months.
     * 
     * @return the months
     */
    private List<SelectItem> getMonths() {
        List<SelectItem> selectItems = new ArrayList<SelectItem>(TOTAL_NUMBER_OF_MONTHS);

        String[] months = new DateFormatSymbols().getMonths();

        for (int i = 0; i < TOTAL_NUMBER_OF_MONTHS; i++) {
            selectItems.add(new SelectItem(i + 1, MessageProvider.getMessageValue(months[i])));
        }

        if (getShowDefault() != null && getShowDefault()) {
            addDefaultSelectItem(selectItems, MessageProvider.getMessageValue("Month"));
        }

        return selectItems;
    }

    /**
     * Gets the years.
     * 
     * @param toInterval
     *            the to interval
     * @param fromInterval
     *            the from interval
     * @return the years
     */
    private List<SelectItem> getYears() {

        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        List<SelectItem> selectItems = new ArrayList<SelectItem>();

        int reference = getReferenceYear() == null || getReferenceYear().equals(Integer.valueOf(0)) ? currentYear : getReferenceYear();

        Integer[] years = getAsArray(reference - getToInterval(), reference + getFromInterval());

        for (Integer year : years) {
            selectItems.add(new SelectItem(year, year.toString()));
        }

        if (getDescending()) {
            Collections.reverse(selectItems);
        }

        if (getShowDefault() != null && getShowDefault()) {
            addDefaultSelectItem(selectItems, MessageProvider.getMessageValue("Year"));
        }
        return selectItems;
    }

    /**
     * Int array.
     * 
     * @param from
     *            the from
     * @param to
     *            the to
     * @return the int[]
     */
    private Integer[] getAsArray(int from, int to) {
        Integer[] result = new Integer[to - from + 1];
        for (int i = from; i <= to; i++) {
            result[i - from] = i;
        }
        return result;
    }

    /**
     * Adds the default {@link SelectItem} to the dropdown
     * 
     * @param selectItems
     * @param key
     */
    private void addDefaultSelectItem(List<SelectItem> selectItems, String key) {
        selectItems.add(0, new SelectItem(null, key));
    }

}
