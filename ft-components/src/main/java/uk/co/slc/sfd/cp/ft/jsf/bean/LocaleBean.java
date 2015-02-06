package uk.co.slc.sfd.cp.ft.jsf.bean;

import static uk.co.slc.sfd.cp.ft.jsf.locale.SLCLocaleUtils.ENGLISH_ENGLISH;
import static uk.co.slc.sfd.cp.ft.jsf.locale.SLCLocaleUtils.ENGLISH_WELSH;
import static uk.co.slc.sfd.cp.ft.jsf.locale.SLCLocaleUtils.WELSH_WELSH;
import static uk.co.slc.sfd.cp.ft.jsf.locale.SLCLocaleUtils.equalsCountry;
import static uk.co.slc.sfd.cp.ft.jsf.locale.SLCLocaleUtils.switchLocale;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.slc.sfd.cp.ft.jsf.locale.LocaleForDomain;
import uk.co.slc.sfd.cp.ft.jsf.locale.WelshLocaleToggleable;

/**
 * A session scoped bean used to store and manipulate the user locale.
 * 
@author slc
 */
@ManagedBean
@SessionScoped
@Slf4j
public class LocaleBean implements Serializable, WelshLocaleToggleable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocaleBean.class);

    /** Generated Serialisation id. */
    private static final long serialVersionUID = 8830446622532268371L;

    /** The name of the URI property that contains the users locale. */
    public static final String LOCALE_URL_PARAM = "_locale";

    /** The name of the cookie that contains the users locale. */
    public static final String LOCALE_COOKIE_NAME = "slcLocale";

    /** The default locale. */
    public static final String DEFAULT_LOCALE = "en_GB";

    /** The list of supported locales. */
    private Map<String, String> supportedLocales;

    /** The current lcoale **/
    private Locale locale;

    /** The LocaleForDomain component **/
    private transient LocaleForDomain localeForDomain;

    /**
     * This method is called after the view has been validated.
     * @param event -
     */
    public void resolveLocale(ComponentSystemEvent event) {
        if (FacesContext.getCurrentInstance().isPostback() && !isError()) {
            return;
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        // First check the URL to determine the local
        String param = request.getParameter(LOCALE_URL_PARAM);
        if (StringUtils.isNotEmpty(param)) {
            try {
                this.setLocale(LocaleUtils.toLocale(param));
            } catch (IllegalArgumentException e) {
                log.info("Exception handled...", e);
                LOGGER.warn(String.format("Invalid locale format: %s", param));
            }

            return;
        }

        // Next check the cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (LOCALE_COOKIE_NAME.equals(cookie.getName())) {
                    this.setLocale(LocaleUtils.toLocale(cookie.getValue()), false);
                    return;
                }
            }
        }

        /*
         * Scenario is a least scenario (might not happen most of the time),
         * in case of locale not found in cookie or url params, 
         * calculate and set it. Invoked at most once.  
         */
        if (localeForDomain != null) {
            Locale domainLocale = localeForDomain.calculateLocale(request);
            if (domainLocale != null) {
                this.setLocale(domainLocale);
                return;
            }
        }

        //At the end all scenario fails to set default locale.
        this.setLocale(LocaleUtils.toLocale(DEFAULT_LOCALE), false);
    }

    private boolean isError() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Map<String, Object> req = ctx.getExternalContext().getRequestMap();
        Throwable t = (Throwable) req.get("javax.servlet.error.exception");

        return t != null;
    }

    /**
     * Check that the locale the client is attempting to use is supported.
     * @param locale - the locale the client would like to use
     * @return the locale that most closely matches the requested locale.
     */
    protected Locale determineLocaleSupported(Locale locale) {
        if (getSupportedLocales().containsKey(locale.toString())) {
            return locale;
        }
        return LocaleUtils.toLocale(DEFAULT_LOCALE);
    }

    /**
     * Get the current locale.
     * @return locale
     */
    public Locale getLocale() {
        if (locale == null) {
            locale = LocaleUtils.toLocale(DEFAULT_LOCALE);
        }
        return locale;
    }

    /**
     * Set the locale for this session, also adds a cookie on the client.
     *
     * @param locale - the locale to use
     */
    public void setLocale(Locale locale) {
        setLocale(locale, true);
    }

    /**
     * Set the locale for this session, optionally adding a cookie on the client.
     *
     * @param locale - the locale to use
     * @param setCookie -  a flag to indicate whether the cookie should be set or not.
     */
    public void setLocale(Locale locale, boolean setCookie) {
        this.locale = determineLocaleSupported(locale);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);

        if (!setCookie) {
            return;
        }

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        if (null != response) {
            Cookie cookie = new Cookie(LOCALE_COOKIE_NAME, this.locale.toString());
            //Sync with other application (my account) - If toggled between languages - selected language remains same
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    /**
     * Return the language associated with the
     * current locale, default en-GB.
     * @return a String representing the language of the current locale.
     */
    public String getLanguage() {

        //Get the current locale
        Locale theLocale = getLocale();

        //Default language (en-GB)
        String language = Locale.UK.getLanguage() + "-" + Locale.UK.getCountry();

        if (theLocale != null && theLocale.equals(WELSH_WELSH)) {
            language = theLocale.getLanguage();
        }

        return language;
    }

    public String getCountry() {
        return locale.getCountry();
    }

    /*
     * (non-Javadoc)
     * @see uk.co.slc.sfd.cp.ft.jsf.locale.WelshLocale#isWelsh()
     */
    @Override
    public boolean isWelsh() {
        return equalsCountry(WELSH_WELSH, locale);
    }

    /**
     * return true if locale is locale for England
     * 
     **/
    public boolean isEnglish() {
        return equalsCountry(ENGLISH_ENGLISH, locale);
    }

    /*
     * (non-Javadoc)
     * @see uk.co.slc.sfd.cp.ft.jsf.locale.WelshLocale#getWelshLocaleToggleLink()
     */
    @Override
    public String getWelshLocaleToggleLink() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        Locale newLocale = switchLocale(locale, ENGLISH_WELSH, WELSH_WELSH);

        StringBuilder link = new StringBuilder(request.getRequestURI());
        link.append("?");
        if (!request.getParameterMap().isEmpty()) {
            link.append(buildParameters(request));
        }
        link.append("_locale=");
        link.append(newLocale);

        return link.toString();
    }

    /**
     * Build a single string to append all paramerters from parameterMap.
     * 
     * @see HttpServletRequest#getParameterMap()
     * 
     * @param request
     * @return
     */
    private String buildParameters(HttpServletRequest request) {
        StringBuilder parameters = new StringBuilder();

        for (Entry<String, String[]> param : request.getParameterMap().entrySet()) {
            if (!LOCALE_URL_PARAM.equalsIgnoreCase(param.getKey())) {
                parameters.append(param.getKey());
                parameters.append("=");
                String paramValue = StringUtils.join(param.getValue(), ",");
                parameters.append(paramValue);
                parameters.append("&");
            }
        }

        return parameters.toString();
    }

    /**
     * @return Returns the supportedLocales.
     */
    public Map<String, String> getSupportedLocales() {
        return supportedLocales;
    }

    /**
     * @param supportedLocales The supportedLocales to set.
     */
    public void setSupportedLocales(Map<String, String> supportedLocales) {
        this.supportedLocales = supportedLocales;
    }

    /**
     * @return the localeForDomain
     */
    public LocaleForDomain getLocaleForDomain() {
        return localeForDomain;
    }

    /**
     * @param localeForDomain the localeForDomain to set
     */
    public void setLocaleForDomain(LocaleForDomain localeForDomain) {
        this.localeForDomain = localeForDomain;
    }
}
