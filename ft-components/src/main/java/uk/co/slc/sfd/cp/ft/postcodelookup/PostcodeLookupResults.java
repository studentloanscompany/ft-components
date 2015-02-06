package uk.co.slc.sfd.cp.ft.postcodelookup;

import java.util.List;

public interface PostcodeLookupResults {

    /** 
     * @return number of addresses listed.
     */
    int getCount();

    /** 
     * @return List of addresses.
     */
    List<PostcodeLookupAddress> getAddresses();

    /**
     * @return Returns the errorCode.
     */
    String getErrorCode();

    /**
     * @return Returns the errorMessage.
     */
    String getErrorMessage();

}