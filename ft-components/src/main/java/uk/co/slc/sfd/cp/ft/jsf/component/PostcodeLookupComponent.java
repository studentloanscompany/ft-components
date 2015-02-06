package uk.co.slc.sfd.cp.ft.jsf.component;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import uk.co.slc.sfd.cp.ft.jsf.messages.MessageProvider;
import uk.co.slc.sfd.cp.ft.postcodelookup.PostcodeLookupAddress;
import uk.co.slc.sfd.cp.ft.postcodelookup.PostcodeLookupProvider;
import uk.co.slc.sfd.cp.ft.postcodelookup.PostcodeLookupServiceException;

/**
 * Custom JSF component. This is a hybrid custom / composite component
 * referenced by the componentType of the compositeInterface of postcodelookup.xhtml.
 * 
@author slc
 * 
 */
@FacesComponent(value = "components.PostcodeLookupComponent")
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class PostcodeLookupComponent extends UIInput implements NamingContainer {

    private Object[] state;

    private PostcodeLookupMode mode = PostcodeLookupMode.SEARCH;

    private String postcodeValue;

    private List<PostcodeLookupAddress> addresses;

    private HtmlInputText postcode;

    private List<SelectItem> addressSelectItems;

    @Override
    public String getFamily() {
        // As we use a composite component for rendering the Component family
        // must be a Naming Container
        return UINamingContainer.COMPONENT_FAMILY;
    }

    @SuppressWarnings("unchecked")
    public void searchPostcodeAction() {
        PostcodeLookupProvider postcodeLookupProvider = (PostcodeLookupProvider) getAttributes().get("PostcodeLookupProvider");
        try {
            addresses = (List<PostcodeLookupAddress>) postcodeLookupProvider.getAddresses(postcodeValue);
        } catch (PostcodeLookupServiceException e) {
            log.error("Exception handled...", e);
            raiseLookupProblemError();
            return;
        }

        if (addresses == null || addresses.isEmpty()) {
            raiseNoAddressesFoundError();
            return;
        }

        clearSelection();
        mode = PostcodeLookupMode.RESULTS;
    }

    public void changePostcodeAction() {
        clearSelection();
        mode = PostcodeLookupMode.SEARCH;
    }

    public void manualEntryAction() {
        clearSelection();
        mode = PostcodeLookupMode.MANUAL;
    }

    protected void raiseNoAddressesFoundError() {
        FacesMessage message = new FacesMessage(MessageProvider.getMessageFromMsgs("postcodelookup_NoAddressFoundForPostCode"));
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(postcode.getClientId(), message);
    }

    protected void raiseLookupProblemError() {
        FacesMessage message = new FacesMessage(MessageProvider.getMessageFromMsgs("postcodelookup_ProblemWithAddressSearch"));
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(postcode.getClientId(), message);
    }

    public List<SelectItem> getAddressSelectItems() {
        addressSelectItems = new ArrayList<SelectItem>();

        if (addresses == null) {
            return addressSelectItems;
        }

        for (PostcodeLookupAddress address : addresses) {
            addressSelectItems.add(new SelectItem(address, getAddressLabel(address)));
        }

        return addressSelectItems;

    }

    private String getAddressLabel(PostcodeLookupAddress address) {
        return address.getAddressLine1() + ", " + address.getTownCity() + ", " + address.getPostcode();
    }

    private void clearSelection() {
    }

    public int getNumberAddresses() {
        return addresses.size();
    }

    @Override
    public Object saveState(FacesContext context) {

        if (state == null) {
            state = new Object[4];
        }

        state[0] = super.saveState(context);
        state[1] = mode;
        state[2] = postcodeValue;
        state[3] = addresses;

        return state;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void restoreState(FacesContext context, Object state) {
        this.state = (Object[]) state;
        super.restoreState(context, this.state[0]);
        mode = (PostcodeLookupMode) this.state[1];
        postcodeValue = (String) this.state[2];
        addresses = (List<PostcodeLookupAddress>) this.state[3];
    }

    // Convenience methods

    public boolean isSearchMode() {
        return PostcodeLookupMode.SEARCH.equals(mode);
    }

    public boolean isResultsMode() {
        return PostcodeLookupMode.RESULTS.equals(mode);
    }

    public boolean isManualMode() {
        return PostcodeLookupMode.MANUAL.equals(mode);
    }

    protected enum PostcodeLookupMode {
        SEARCH, MANUAL, RESULTS
    }

    public PostcodeLookupMode getMode() {
        return mode;
    }

    public void setMode(PostcodeLookupMode mode) {
        this.mode = mode;
    }

}
