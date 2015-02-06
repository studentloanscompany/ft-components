package uk.co.slc.sfd.cp.ft.jsf.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import uk.co.slc.sfd.cp.ft.postcodelookup.OutsideUKPostcodeLookupProvider;
import uk.co.slc.sfd.cp.ft.postcodelookup.PostcodeLookupAddress;
import uk.co.slc.sfd.cp.ft.postcodelookup.PostcodeLookupServiceException;

@ManagedBean
@ViewScoped
@Slf4j
@Data
public class OutsideUKPostcodeLookupBean implements Serializable, OutsideUKPostcodeLookupProvider {

    private static final long serialVersionUID = 633847118359576224L;

    private PostcodeLookupAddress selectedAddress = new Address();

    @Override
    public List<PostcodeLookupAddress> getAddresses(String postcode) throws PostcodeLookupServiceException {

        List<PostcodeLookupAddress> addressList = new ArrayList<PostcodeLookupAddress>();

        if ("G1 1DW".equalsIgnoreCase(postcode)) {
            addressList.add(new Address("LINE 1", null, null, null, null, null, null));
            addressList.add(new Address("LINE 2", null, null, null, null, null, null));
            addressList.add(new Address("LINE 3", null, null, null, null, null, null));
        }

        return addressList;
    }

    @Override
    public List<?> allCountries() {
        return Arrays.asList("UK", "Outside UK");
    }

    @Override
    public List<?> allNonUKCountries() {
        return Arrays.asList("Outside UK");
    }

    public void testAction() {
        log.info("<<<<<<<  SELECTED ADDRESS IS " + selectedAddress.getAddressLine1());
    }

}
