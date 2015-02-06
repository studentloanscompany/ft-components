package uk.co.slc.sfd.cp.ft.jsf.test;

import java.io.Serializable;

import uk.co.slc.sfd.cp.ft.postcodelookup.PostcodeLookupAddress;

public class Address implements Serializable, PostcodeLookupAddress {

	private static final long serialVersionUID = -149281201071845778L;

	/** House or Street. **/
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String townCity;

	private String postcode;

	private String county;

	private String country;

	/** Default Constructor. */
	public Address() {
		// set default country
		country = "UK";
	}

	public Address(String addressLine1, String addressLine2,
			String addressLine3, String townCity, String postCode,
			String county, String country) {
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
