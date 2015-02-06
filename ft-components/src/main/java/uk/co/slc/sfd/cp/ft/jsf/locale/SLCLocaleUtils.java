package uk.co.slc.sfd.cp.ft.jsf.locale;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.LocaleUtils;

import com.google.common.collect.ImmutableMap;

/**
 * File: SLCLocaleUtils.java
 * 
@author slc
 * 
 */
public final class SLCLocaleUtils {

    /** Locale for England(default) **/
    public static final Locale ENGLISH_ENGLISH = new Locale("en", "GB");

    /** Locale for Northern Ireland **/
    public static final Locale ENGLISH_NORTHERN_IRISH = new Locale("en", "NI");

    /** Locale for Wales **/
    public static final Locale ENGLISH_WELSH = new Locale("en", "CY");

    /** Locale for Wales - welsh **/
    public static final Locale WELSH_WELSH = new Locale("cy", "CY");

    /** Mapping holder countryCode to Country Name **/
    public static final Map<String, String> LOCALE_TO_COUNTRY_MAP = ImmutableMap.of("en_GB", "ENGLAND", "en_NI", "NORTHERN IRELAND", "en_CY", "WALES", "cy_CY", "WALES");

    //Hidden constructor for utility class.
    private SLCLocaleUtils() {
        throw new AssertionError("full static class");
    }

    /**
     * Return locale for corresponding country, else return 
     * default locale if country not exists.
     * 
     * @param country
     * @return
     */
    public static Locale getLocaleByCountry(String country) {
        return getLocaleByCountry(country, false);
    }

    /**
     * Return locale for corresponding country, return 
     * default locale if country not exists. If <code>returnNullNotExists</code>
     * is true return null if country not exists.
     * 
     * @param country
     * @return
     */
    public static Locale getLocaleByCountry(String country, boolean returnNullNotExists) {
        Locale locale = getLocale(country);

        if (locale == null && !returnNullNotExists) {
            locale = ENGLISH_ENGLISH;
        }

        return locale;
    }

    /**
     * Perform comparison of locale language.
     * 
     * @param locale1
     * @param locale2
     * @return
     */
    public static boolean equalsLanguage(Locale locale1, Locale locale2) {
        return locale1.getLanguage().equals(locale2.getLanguage());
    }

    /**
     * Performs comparison of locale country.
     * 
     * @param locale1
     * @param locale2
     * @return
     */
    public static boolean equalsCountry(Locale locale1, Locale locale2) {
        return locale1.getCountry().equals(locale2.getCountry());
    }

    /**
     * Pull full country from locale country and match.
     * 
     * @param locale
     * @param countryDesc
     * @return
     */
    public static boolean equalsCountry(Locale locale, String countryDesc) {
        if (locale != null && countryDesc != null) {
            String country = LOCALE_TO_COUNTRY_MAP.get(locale.toString());
            return country.equalsIgnoreCase(countryDesc);
        }

        return false;
    }

    /**
     * Performs comparison between current locale and locales to be switched,
     * and return which in not matched with current one.
     * 
     * @param currentLocale
     * @param localeTobeSwitched1
     * @param localeToBsSiwtched2
     * @return
     */
    public static Locale switchLocale(Locale currentLocale, Locale localeTobeSwitched1, Locale localeToBsSiwtched2) {
        return currentLocale.equals(localeTobeSwitched1) ? localeToBsSiwtched2 : localeTobeSwitched1;
    }

    /**
     * Lookup to find the country and return corresponding locale.
     * 
     * @param country
     * @return
     */
    private static Locale getLocale(String country) {
        for (Entry<String, String> localeByCountry : LOCALE_TO_COUNTRY_MAP.entrySet()) {
            if (localeByCountry.getValue().equalsIgnoreCase(country)) {
                return LocaleUtils.toLocale(localeByCountry.getKey());
            }
        }
        return null;
    }

}
