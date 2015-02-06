package uk.co.slc.sfd.cp.ft.jsf.component;

import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ArrayUtils;

import uk.co.slc.sfd.cp.ft.postcodelookup.OutsideUKPostcodeLookupProvider;
import uk.co.slc.sfd.cp.ft.postcodelookup.PostcodeLookupAddress;
import uk.co.slc.sfd.cp.ft.postcodelookup.PostcodeLookupProvider;
import uk.co.slc.sfd.cp.ft.postcodelookup.PostcodeLookupServiceException;

/**
 * Custom JSF postcodelookup component. This is a hybrid custom / composite component
 * referenced by the componentType of the compositeInterface of outsideukpostcodelookup.xhtml.
 * 
 * @author slc
 * 
 */
@FacesComponent(value = "components.OutsideUKPostcodeLookupComponent")
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class OutsideUKPostcodeLookupComponent extends PostcodeLookupComponent {

    private Object[] state;
    private boolean outsideUkAddress;

    @SuppressWarnings("unchecked")
    public void searchPostcodeAction() {
        PostcodeLookupProvider postcodeLookupProvider = (PostcodeLookupProvider) getAttributes().get("PostcodeLookupProvider");
        setPostcodeValue((String) getPostcode().getValue());
        try {
            setAddresses((List<PostcodeLookupAddress>) postcodeLookupProvider.getAddresses(getPostcodeValue()));
        } catch (PostcodeLookupServiceException e) {
            log.error("Exception handled...", e);
            raiseLookupProblemError();
            return;
        }
        if (getAddresses() == null) {
            raiseNoAddressesFoundError();
            return;
        }
        setMode(PostcodeLookupMode.RESULTS);
    }

    @Override
    public Object saveState(FacesContext context) {
        state = (Object[]) super.saveState(context);
        if (state.length < 5) {
            state = ArrayUtils.add(state, outsideUkAddress);
        } else {
            state[4] = outsideUkAddress;
        }
        return state;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        this.state = (Object[]) state;
        super.restoreState(context, this.state);
        outsideUkAddress = (boolean) this.state[4];
    }

    public Object getCountries() {
        OutsideUKPostcodeLookupProvider lookupProvider = (OutsideUKPostcodeLookupProvider) getAttributes().get("PostcodeLookupProvider");
        if (outsideUkAddress) {
            return lookupProvider.allNonUKCountries();
        }
        return lookupProvider.allCountries();
    }
}
