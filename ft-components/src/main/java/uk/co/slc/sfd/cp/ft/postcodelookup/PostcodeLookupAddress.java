package uk.co.slc.sfd.cp.ft.postcodelookup;

public interface PostcodeLookupAddress {

    /**
     * @return the addressLine1
     */
    String getAddressLine1();

    /**
     * @return the addressLine2
     */
    String getAddressLine2();

    /**
     * @return the addressLine3
     */
    String getAddressLine3();

    /**
     * @return the townCity
     */
    String getTownCity();

    /**
     * @return the county
     */
    String getCounty();

    /**
     * @return the country
     */
    String getCountry();

    /**
     * @return the postcode
     */
    String getPostcode();

}