package uk.co.slc.sfd.cp.ft.jsf.dropdown;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class LocalizedTextEnumHelper {

    public static final String BUNDLE_LOOKUP_FAILURE_FORMAT = "??? %s ??? not found";

    private LocalizedTextEnumHelper() {
        //utility class constructor
    }

    public static String getLocalizedText(LocalizedTextEnum localizedTextEnum) {
        return getLocalizedText(localizedTextEnum, null);
    }

    public static String getLocalizedText(LocalizedTextEnum localizedTextEnum, Locale loc) {

        String result;
        try {
            ResourceBundle rb = null == loc ? ResourceBundle.getBundle(localizedTextEnum.getClass().getName()) : ResourceBundle.getBundle(localizedTextEnum.getClass().getName(), loc);
            result = rb.getString(localizedTextEnum.getKeyForText());
        } catch (MissingResourceException e) {
            log.info("Caught MissingResourceException...", e);
            result = String.format(BUNDLE_LOOKUP_FAILURE_FORMAT, localizedTextEnum.getKeyForText());
        }

        return result;
    }

}
