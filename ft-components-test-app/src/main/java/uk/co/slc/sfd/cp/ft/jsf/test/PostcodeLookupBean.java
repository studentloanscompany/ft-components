package uk.co.slc.sfd.cp.ft.jsf.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.extern.slf4j.Slf4j;
import uk.co.slc.sfd.cp.ft.postcodelookup.PostcodeLookupAddress;
import uk.co.slc.sfd.cp.ft.postcodelookup.PostcodeLookupProvider;
import uk.co.slc.sfd.cp.ft.postcodelookup.PostcodeLookupServiceException;

@ManagedBean
@ViewScoped
@Slf4j
public class PostcodeLookupBean implements Serializable, PostcodeLookupProvider {

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

    public PostcodeLookupAddress getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(PostcodeLookupAddress selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public void testAction() {
        log.info("<<<<<<<  SELECTED ADDRESS IS " + selectedAddress.getAddressLine1());
    }

}
