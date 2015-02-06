package uk.co.slc.sfd.cp.ft.postcodelookup;

import java.util.List;

public interface OutsideUKPostcodeLookupProvider extends PostcodeLookupProvider {

    List<?> allCountries();
    List<?> allNonUKCountries();

}
