package uk.co.slc.sfd.cp.ft.jsf.dropdown;

import java.util.Locale;

public interface Dropdownable {
	
	Object getKey();
	
	String getDropdownValue();

	String getDropdownValueForLocale(Locale locale);
}
