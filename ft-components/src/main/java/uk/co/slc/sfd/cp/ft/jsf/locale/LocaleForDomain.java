package uk.co.slc.sfd.cp.ft.jsf.locale;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.slc.sfd.cp.ft.jsf.SpringPropertysource;

/**
 * File: LocaleForDomain.java
 * 
 *  A component to manipulate the user locale in case locale 
 *  not found in cookie or params it calculates
 *  locale using environment properties. Environment
 *  properties use domain calculate locale.
 * 
@author slc
 *
 */
@Component
public class LocaleForDomain {

    /** Environment properties **/
    @Autowired
    private SpringPropertysource propertySource;

    /** URL Property prefix for locale **/
    public static final String URL_LOOKUP_PREFIX = "url.locale.lookup.";

    /**
     * Get locale from protocol or environment properties.
     * 
     * @param request
     * @return
     */
    public Locale calculateLocale(HttpServletRequest request) {
        String locale = getLocaleFromEnvironmentProperties(request);

        return LocaleUtils.toLocale(locale);
    }

    /**
     * Check the system properties and spring properties using the servername, with a prefix.
     * @param request
     * 
     * @return
     */
    private String getLocaleFromEnvironmentProperties(HttpServletRequest request) {
        String key = URL_LOOKUP_PREFIX + request.getServerName();
        return propertySource.getProperty(key);
    }

    /**
     * @return the propertySource
     */
    public SpringPropertysource getPropertySource() {
        return propertySource;
    }

    /**
     * @param propertySource the propertySource to set
     */
    public void setPropertySource(SpringPropertysource propertySource) {
        this.propertySource = propertySource;
    }
}
