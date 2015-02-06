package uk.co.slc.sfd.cp.ft.jsf.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import uk.co.slc.sfd.cp.ft.jsf.MockFacesContext;

/**
@author slc
 */
public class LocaleBeanTest {

    private static final String LOC_EN_GB = "en_GB";
    private static final String LOC_EN_CY = "en_CY";
    private static final String LOC_EN_NI = "en_NI";
    private static final String LOC_CY_CY = "cy_CY";

    private FacesContext mockContext;

    LocaleBean localeBean;

    MockHttpServletRequest request;
    MockHttpServletResponse response;
    @Mock
    ExternalContext mockExtContext;
    Map<String, String> supportedLocales;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockContext = MockFacesContext.mockFacesContext();
        when(mockContext.getExternalContext()).thenReturn(mockExtContext);

        localeBean = new LocaleBean();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        when(mockExtContext.getRequest()).thenReturn(request);
        when(mockExtContext.getResponse()).thenReturn(response);

        // set postback to false;
        when(mockContext.isPostback()).thenReturn(false);

        initSupportedLocales();
        localeBean.setSupportedLocales(supportedLocales);

    }

    private void initSupportedLocales() {
        supportedLocales = new LinkedHashMap<String, String>();
        supportedLocales.put(LOC_EN_GB, LOC_EN_GB);
        supportedLocales.put(LOC_EN_NI, LOC_EN_NI);
        supportedLocales.put(LOC_EN_CY, LOC_EN_CY);
        supportedLocales.put(LOC_CY_CY, LOC_CY_CY);
    }

    @Test
    public void testPostback() {
        // set postback to false;
        when(mockContext.isPostback()).thenReturn(true);

        localeBean.resolveLocale(null);
        // the locale should NOT have been set on the ViewRoot
        verify(mockContext.getViewRoot(), never()).setLocale(LocaleUtils.toLocale(LocaleBean.DEFAULT_LOCALE));
    }

    @Test
    public void testResolveLocaleUrlParam() {
        String locale = "cy_CY";
        request.setParameter(LocaleBean.LOCALE_URL_PARAM, locale);

        localeBean.resolveLocale(null);
        // the locale should have been set on the ViewRoot
        verify(mockContext.getViewRoot(), times(1)).setLocale(LocaleUtils.toLocale(locale));

        assertEquals("The locale was incorrect (country) expected CY", localeBean.getLocale().getCountry(), "CY");
        assertEquals("The locale was incorrect (language)  expected cy", localeBean.getLocale().getLanguage(), "cy");
    }

    @Test
    public void testResolveLocaleCookie() {
        String locale = "en_CY";
        request.setCookies(new Cookie[] { new Cookie(LocaleBean.LOCALE_COOKIE_NAME, locale) });

        localeBean.resolveLocale(null);
        // the locale should have been set on the ViewRoot
        verify(mockContext.getViewRoot(), times(1)).setLocale(LocaleUtils.toLocale(locale));

        assertEquals("The locale was incorrect (country) expected CY", localeBean.getLocale().getCountry(), "CY");
        assertEquals("The locale was incorrect (language)  expected en", localeBean.getLocale().getLanguage(), "en");
    }

    @Test
    public void testSetLocaleCookie() {
        String locale = "en_CY";
        Locale locObject = new Locale(locale);

        localeBean.setLocale(locObject, false);
        assertEquals("The locale cookie should not have been set", 0, response.getCookies().length);

        localeBean.setLocale(locObject, true);
        assertEquals("The locale cookie should have been set", 1, response.getCookies().length);
    }

    @Test
    public void testResolveLocaleDefault() {
        localeBean.resolveLocale(null);

        Locale defaultLoc = LocaleUtils.toLocale(LocaleBean.DEFAULT_LOCALE);
        // the locale should have been set on the ViewRoot
        verify(mockContext.getViewRoot(), times(1)).setLocale(defaultLoc);

        assertEquals("The locale was incorrect (country) expected CY", localeBean.getLocale().getCountry(), defaultLoc.getCountry());
        assertEquals("The locale was incorrect (language)  expected en", localeBean.getLocale().getLanguage(), defaultLoc.getLanguage());
    }

    @Test
    public void testDetermineLocaleSupported() {
        String locale = "en_CY";
        Locale locObject = LocaleUtils.toLocale(locale);

        Locale result = localeBean.determineLocaleSupported(locObject);

        assertEquals("The locale was hould have been supported " + locale, locObject, result);
    }

    @Test
    public void testDetermineLocaleNotSupported() {
        String locale = "en_ZZ";
        Locale locObject = LocaleUtils.toLocale(locale);

        Locale result = localeBean.determineLocaleSupported(locObject);
        Locale expected = LocaleUtils.toLocale(LocaleBean.DEFAULT_LOCALE);

        assertEquals("The locale was hould have been supported " + locale, expected, result);
    }

    @Test
    public void testLanguageReturnedWhenLocaleNull() {

        String language = localeBean.getLanguage();

        assertNotNull(language);

        assertEquals("en-GB", language);

    }

    @Test
    public void testEnglishReturnedWhenLocaleEngland() {

        localeBean.setLocale(Locale.UK);

        String language = localeBean.getLanguage();

        assertNotNull(language);

        assertEquals("en-GB", language);

    }

    @Test
    public void testEnglishReturnedWhenLocaleNI() {

        Locale NI = new Locale("en", "NI");

        localeBean.setLocale(NI);

        String language = localeBean.getLanguage();

        assertNotNull(language);

        assertEquals("en-GB", language);

    }

    @Test
    public void testEnglishReturnedWhenLocaleWelshEnglish() {

        Locale NI = new Locale("en", "CY");

        localeBean.setLocale(NI);

        String language = localeBean.getLanguage();

        assertNotNull(language);

        assertEquals("en-GB", language);

    }

    @Test
    public void testWelshReturnedWhenLocaleWelshWelsh() {

        Locale NI = new Locale("cy", "CY");

        localeBean.setLocale(NI);

        String language = localeBean.getLanguage();

        assertNotNull(language);

        assertEquals("cy", language);

    }

    @Test
    public void testEnglishReturnedWhenLocaleNotSpecificallyHandled() {

        localeBean.setLocale(Locale.FRANCE);

        String language = localeBean.getLanguage();

        assertNotNull(language);

        assertEquals("en-GB", language);

    }

}
