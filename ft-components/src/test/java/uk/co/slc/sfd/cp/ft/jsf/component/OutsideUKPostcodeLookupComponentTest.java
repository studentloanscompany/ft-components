package uk.co.slc.sfd.cp.ft.jsf.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.co.slc.sfd.cp.ft.jsf.MockFacesContext;
import uk.co.slc.sfd.cp.ft.jsf.messages.MessageProvider;
import uk.co.slc.sfd.cp.ft.postcodelookup.OutsideUKPostcodeLookupProvider;
import uk.co.slc.sfd.cp.ft.postcodelookup.PostcodeLookupAddress;
import uk.co.slc.sfd.cp.ft.postcodelookup.PostcodeLookupServiceException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ FacesContext.class, MessageProvider.class })
public class OutsideUKPostcodeLookupComponentTest {

    private OutsideUKPostcodeLookupComponent testee;
    private FacesContext mockContext;
    @Mock
    private OutsideUKPostcodeLookupProvider mockPostcodeLookupProvider;

    @Mock
    private HtmlInputText mockPostCodeComponent;

    @Mock
    private TestAddress mockAddress;

    @Mock
    private ValueExpression mockValueExpression;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockContext = MockFacesContext.mockFacesContext();
        PowerMockito.mockStatic(FacesContext.class);
        PowerMockito.when(FacesContext.getCurrentInstance()).thenReturn(mockContext);
        PowerMockito.mockStatic(MessageProvider.class);
        testee = new OutsideUKPostcodeLookupComponent();
        testee.setPostcode(mockPostCodeComponent);
        testee.getAttributes().put("PostcodeLookupProvider", mockPostcodeLookupProvider);
        testee.setValueExpression("value", mockValueExpression);
        mockContext.getViewRoot().getChildren().add(testee);
        Mockito.when(mockPostCodeComponent.getClientId()).thenReturn("CLIENTID");
    }

    /**
     * Test that when we search for a postcode that zero results in showing 0 addresses in Result mode
     * @throws PostcodeLookupServiceException 
     */
    @Test
    public void testSearchPostcodeActionNoAddress() throws PostcodeLookupServiceException {
        //  Initial mode should be SEARCH
        assertEquals(PostcodeLookupComponent.PostcodeLookupMode.SEARCH, testee.getMode());
        testee.searchPostcodeAction();
        // Verify that a FacesMessage has been raised
        Mockito.verify(mockContext, Mockito.never()).addMessage(Mockito.anyString(), Mockito.any(FacesMessage.class));
        //  Assert that we are still in SEARCH mode
        assertEquals(PostcodeLookupComponent.PostcodeLookupMode.RESULTS, testee.getMode());
        //  Assert selection not cleared
        Mockito.verify(mockValueExpression, Mockito.never()).setValue(Mockito.any(ELContext.class), Mockito.any());
    }

    /**
     * Test that we can handle an address
     * 
     * @throws PostcodeLookupServiceException
     */
    @Test
    public void testSearchPostcodeActionWithAddress() throws PostcodeLookupServiceException {
        //  Initial mode should be SEARCH
        assertEquals(PostcodeLookupComponent.PostcodeLookupMode.SEARCH, testee.getMode());
        // One address returned by Provider
        List<TestAddress> addressList = new ArrayList<TestAddress>();
        addressList.add(mockAddress);
        Mockito.doReturn(addressList).when(mockPostcodeLookupProvider).getAddresses(Mockito.anyString());

        testee.searchPostcodeAction();
        // Verify that no FacesMessage has been raised
        Mockito.verify(mockContext, Mockito.never()).addMessage(Mockito.anyString(), Mockito.any(FacesMessage.class));
        //  Assert we have 1 address
        assertEquals(1, testee.getAddresses().size());
        //  Assert we have 1 select item 
        assertEquals(1, testee.getAddressSelectItems().size());
        //  Assert we are in results mode
        assertEquals(PostcodeLookupComponent.PostcodeLookupMode.RESULTS, testee.getMode());
        //  Assert that selection has been cleared
        //  TODO  Scott. We can take this out if component does not clear the selection
        //  Mockito.verify(mockValueExpression,Mockito.times(1)).setValue(Mockito.any(ELContext.class), Mockito.any());
    }

    @Test
    public void testSearchPostcodeActionException() throws PostcodeLookupServiceException {
        //  Initial mode should be SEARCH
        assertEquals(PostcodeLookupComponent.PostcodeLookupMode.SEARCH, testee.getMode());
        Mockito.when(mockPostcodeLookupProvider.getAddresses(Mockito.anyString())).thenThrow(new PostcodeLookupServiceException("WIBBLE"));
        testee.searchPostcodeAction();
        // Verify that a FacesMessage has been raised
        Mockito.verify(mockContext, Mockito.times(1)).addMessage(Mockito.anyString(), Mockito.any(FacesMessage.class));
        //  Assert that we are still in SEARCH mode
        assertEquals(PostcodeLookupComponent.PostcodeLookupMode.SEARCH, testee.getMode());
    }

    /**
     * Test that we switch to search mode when changing the postcode
     */
    @Test
    public void testChangePostcodeAction() {
        testee.changePostcodeAction();
        //  Assert that we are in SEARCH mode
        assertEquals(PostcodeLookupComponent.PostcodeLookupMode.SEARCH, testee.getMode());
        //  Assert that selection has been cleared
        //Mockito.verify(mockValueExpression,Mockito.times(1)).setValue(Mockito.any(ELContext.class), Mockito.any());		
    }

    /**
     * Test that we switch to Manual mode when user clicks manual entry
     */
    @Test
    public void testManualEntryAction() {
        testee.manualEntryAction();
        //  Assert that we are in MANUAL mode
        assertEquals(PostcodeLookupComponent.PostcodeLookupMode.MANUAL, testee.getMode());
        //  Assert that selection has been cleared
        //Mockito.verify(mockValueExpression,Mockito.times(1)).setValue(Mockito.any(ELContext.class), Mockito.any());		
    }

    /**
     * Test to check whether address list is being set to dropdown select Items
     */
    @Test
    public void testGetAddressSelectItems() {
        List<PostcodeLookupAddress> lookupAddresses = testee.getAddresses();
        List<SelectItem> addressSelectItems = testee.getAddressSelectItems();
        if (lookupAddresses == null || lookupAddresses.isEmpty()) {
            assertTrue(addressSelectItems.isEmpty());
        } else {
            assertFalse(addressSelectItems.isEmpty());
        }
    }

    /**
     * Test that checks save state
     */
    @Test
    public void testSaveState() {
        List<PostcodeLookupAddress> lookupAddresses = testee.getAddresses();
        testee.setMode(PostcodeLookupComponent.PostcodeLookupMode.MANUAL);
        testee.setPostcodeValue("N10 AH");
        testee.setAddresses(lookupAddresses);
        Object state = testee.saveState(mockContext);
        Object[] stateArray = (Object[]) state;
        assertTrue(stateArray.length == 5);
        assertTrue(stateArray[1].equals(PostcodeLookupComponent.PostcodeLookupMode.MANUAL));
        assertTrue(stateArray[2].equals("N10 AH"));
        assertTrue(stateArray[3] == lookupAddresses);
    }

    /**
     * Test that checks restore state 
     */
    @Test
    public void testRestoreState() {
        Object[] state = new Object[5];
        List<PostcodeLookupAddress> lookupAddresses = testee.getAddresses();
        state[1] = PostcodeLookupComponent.PostcodeLookupMode.MANUAL;
        state[2] = "N10 AH";
        state[3] = lookupAddresses;
        state[4] = false;
        testee.restoreState(mockContext, state);

        assertTrue(testee.getMode().equals(PostcodeLookupComponent.PostcodeLookupMode.MANUAL));
        assertTrue(testee.getPostcodeValue().equals("N10 AH"));
        assertTrue(testee.getAddresses() == lookupAddresses);
    }

    public class TestAddress implements PostcodeLookupAddress {

        private String addressLine1;
        private String addressLine2;
        private String addressLine3;
        private String townCity;

        private String postcode;

        private String county;

        private String country;

        /** Default Constructor. */
        public TestAddress() {
            // set default country
            country = "UK";
        }

        public TestAddress(String addressLine1, String addressLine2, String addressLine3, String townCity, String postCode, String county, String country) {
            super();
            this.addressLine1 = addressLine1;
            this.addressLine2 = addressLine2;
            this.addressLine3 = addressLine3;
            this.townCity = townCity;
            this.postcode = postCode;
            this.county = county;
            this.country = country;
        }

        public String getAddressLine1() {
            return addressLine1;
        }

        public void setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
        }

        public String getAddressLine2() {
            return addressLine2;
        }

        public void setAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
        }

        public String getAddressLine3() {
            return addressLine3;
        }

        public void setAddressLine3(String addressLine3) {
            this.addressLine3 = addressLine3;
        }

        public String getTownCity() {
            return townCity;
        }

        public void setTownCity(String townCity) {
            this.townCity = townCity;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

    }

}
