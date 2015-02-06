package uk.co.slc.sfd.cp.ft.postcodelookup;

import java.util.List;

public interface PostcodeLookupProvider {

    /**{@inheritDoc} */
    List<? extends PostcodeLookupAddress> getAddresses(String postcode) throws PostcodeLookupServiceException;

}