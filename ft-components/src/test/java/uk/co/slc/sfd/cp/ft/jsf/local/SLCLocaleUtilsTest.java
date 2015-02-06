package uk.co.slc.sfd.cp.ft.jsf.local;

import static org.junit.Assert.assertEquals;
import static uk.co.slc.sfd.cp.ft.jsf.locale.SLCLocaleUtils.equalsCountry;
import static uk.co.slc.sfd.cp.ft.jsf.locale.SLCLocaleUtils.equalsLanguage;
import static uk.co.slc.sfd.cp.ft.jsf.locale.SLCLocaleUtils.getLocaleByCountry;
import static uk.co.slc.sfd.cp.ft.jsf.locale.SLCLocaleUtils.switchLocale;

import java.util.Locale;

import org.junit.Test;

/**
 * File: SLCLocaleUtilsTest.java
 * 
 * SLCLocaleUtils tests
 * 
@author slc
 *
 */
public class SLCLocaleUtilsTest {

    @Test
    public void testGetLocaleByCountry() {
        Locale locale = getLocaleByCountry("ENGLAND");
        assertEquals("en_GB", locale.toString());

        locale = getLocaleByCountry("NORTHERN IRELAND");
        assertEquals("en_NI", locale.toString());

        locale = getLocaleByCountry("WALES");
        assertEquals("en_CY", locale.toString());

        locale = getLocaleByCountry("SCOTLAND");
        assertEquals("en_GB", locale.toString());

        locale = getLocaleByCountry("FRANCE");
        assertEquals("en_GB", locale.toString());
    }

    @Test
    public void testGetLocaleByCountryIfNullorNotNull() {
        Locale locale = getLocaleByCountry("SCOTLAND", true);
        assertEquals(null, locale);

        locale = getLocaleByCountry("FRANCE", true);
        assertEquals(null, locale);

        locale = getLocaleByCountry("FRANCE", false);
        assertEquals("en_GB", locale.toString());

        locale = getLocaleByCountry("ENGLAND", true);
        assertEquals("en_GB", locale.toString());
    }

    @Test
    public void testEqualsLanguage() {
        assertEquals(true, equalsLanguage(new Locale("en", "GB"), new Locale("en", "NI")));
        assertEquals(false, equalsLanguage(new Locale("en", "GB"), new Locale("cy", "CY")));
    }

    @Test
    public void testEqualsCountry() {
        assertEquals(false, equalsCountry(new Locale("en", "GB"), new Locale("en", "NI")));
        assertEquals(true, equalsCountry(new Locale("cy", "CY"), new Locale("en", "CY")));
    }

    @Test
    public void testEqualsCountrywithCountry() {
        assertEquals(true, equalsCountry(new Locale("en", "GB"), "ENGLAND"));
        assertEquals(true, equalsCountry(new Locale("en", "NI"), "NORTHERN IRELAND"));
        assertEquals(true, equalsCountry(new Locale("en", "CY"), "WALES"));
        assertEquals(true, equalsCountry(new Locale("cy", "CY"), "WALES"));

        assertEquals(false, equalsCountry(new Locale("cy", "CY"), "SCOTLAND"));
        assertEquals(false, equalsCountry(new Locale("en", "GB"), "FRANCE"));
    }

    @Test
    public void testSwitchLocale() {
        Locale locale = switchLocale(new Locale("en", "CY"), new Locale("cy", "CY"), new Locale("en", "CY"));
        assertEquals("cy_CY", locale.toString());
    }
}
