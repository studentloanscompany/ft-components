package uk.co.slc.sfd.cp.ft.jsf.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@ViewScoped
public class TestBean implements Serializable {

    private static final long serialVersionUID = 633847118359576224L;

    private String select1;
    private String select2;
    private String select3;
    private String select4;

    private Date date1;
    private Date date2;
    private Date date3;
    private Date date4;
    private Date date5;
    private Date date7;

    private String field1;
    private String field2;
    private String field3;
    private String field4;
    private String field5;

    private String textArea1;
    private String textArea2;
    private String textArea3;
    private String textArea4;
    private String textArea5;

    private String nino1;
    private String nino2;
    private String nino3;
    private String nino4;

    private boolean renderTooltip = true;

    private boolean renderInfoBox = true;

    private boolean ninoErrorLinkClicked = false;

    private String currency1;

    private transient List<TestObject> testListObject;

    @PostConstruct
    public void init() {
        testListObject = new ArrayList<TestObject>();
        testListObject.add(new TestObject());
        testListObject.add(new TestObject());
        testListObject.add(new TestObject());
    }

    public void testAction() {
    }

    public void ninoErrorAction() {
        ninoErrorLinkClicked = true;
    }

    public void toggleTooltip() {
        renderTooltip = !renderTooltip;
    }

    public void toggleInfoBox() {
        renderInfoBox = !renderInfoBox;
    }

    public List<SelectItem> getSelectItems() {

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();

        selectItemList.add(new SelectItem(null, "Please select something"));
        selectItemList.add(new SelectItem("SELECT_ITEM_A", "Select Item A"));
        selectItemList.add(new SelectItem("SELECT_ITEM_B", "Select Item B"));

        return selectItemList;
    }

    public List<SelectItem> getSelectItemsNoDefault() {

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();

        selectItemList.add(new SelectItem("SELECT_ITEM_A", "Select Item A"));
        selectItemList.add(new SelectItem("SELECT_ITEM_B", "Select Item B"));

        return selectItemList;
    }

    public String getSelect1() {
        return select1;
    }

    public void setSelect1(String select1) {
        this.select1 = select1;
    }

    public String getSelect2() {
        return select2;
    }

    public void setSelect2(String select2) {
        this.select2 = select2;
    }

    public String getSelect3() {
        return select3;
    }

    public void setSelect3(String select3) {
        this.select3 = select3;
    }

    public String getSelect4() {
        return select4;
    }

    public void setSelect4(String select4) {
        this.select4 = select4;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Date getDate3() {
        return date3;
    }

    public void setDate3(Date date3) {
        this.date3 = date3;
    }

    public String getCurrency1() {
        return currency1;
    }

    public void setCurrency1(String currency1) {
        this.currency1 = currency1;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public Date getDate4() {
        return date4;
    }

    public void setDate4(Date date4) {
        this.date4 = date4;
    }

    public String getTextArea1() {
        return textArea1;
    }

    public void setTextArea1(String textArea1) {
        this.textArea1 = textArea1;
    }

    public String getTextArea2() {
        return textArea2;
    }

    public void setTextArea2(String textArea2) {
        this.textArea2 = textArea2;
    }

    public String getTextArea3() {
        return textArea3;
    }

    public void setTextArea3(String textArea3) {
        this.textArea3 = textArea3;
    }

    public String getTextArea4() {
        return textArea4;
    }

    public void setTextArea4(String textArea4) {
        this.textArea4 = textArea4;
    }

    public String getTextArea5() {
        return textArea5;
    }

    public void setTextArea5(String textArea5) {
        this.textArea5 = textArea5;
    }

    public boolean isRenderTooltip() {
        return renderTooltip;
    }

    public void setRenderTooltip(boolean renderTooltip) {
        this.renderTooltip = renderTooltip;
    }

    public boolean isRenderInfoBox() {
        return renderInfoBox;
    }

    public void setRenderInfoBox(boolean renderInfoBox) {
        this.renderInfoBox = renderInfoBox;
    }

    public Date getDate5() {
        return date5;
    }

    public void setDate5(Date date5) {
        this.date5 = date5;
    }

    public List<TestObject> getTestListObject() {
        return testListObject;
    }

    public void setTestListObject(List<TestObject> testListObject) {
        this.testListObject = testListObject;
    }

    public static class TestObject {

        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    public Date getDate7() {
        return date7;
    }

    public void setDate7(Date date7) {
        this.date7 = date7;
    }

    public String getNino1() {
        return nino1;
    }

    public void setNino1(String nino1) {
        this.nino1 = nino1;
    }

    public String getNino2() {
        return nino2;
    }

    public void setNino2(String nino2) {
        this.nino2 = nino2;
    }

    public String getNino3() {
        return nino3;
    }

    public void setNino3(String nino3) {
        this.nino3 = nino3;
    }

    public String getNino4() {
        return nino4;
    }

    public void setNino4(String nino4) {
        this.nino4 = nino4;
    }

    public boolean isNinoErrorLinkClicked() {
        return ninoErrorLinkClicked;
    }

    public void setNinoErrorLinkClicked(boolean ninoErrorLinkClicked) {
        this.ninoErrorLinkClicked = ninoErrorLinkClicked;
    }

}
