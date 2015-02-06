package uk.co.slc.sfd.cp.ft.jsf.dropdown;

import java.io.Serializable;
import java.util.Locale;

public interface LocalizedTextEnum extends Serializable {

	String getLocalizedText();

	String getLocalizedText(Locale loc);

	String getKeyForText();

}
