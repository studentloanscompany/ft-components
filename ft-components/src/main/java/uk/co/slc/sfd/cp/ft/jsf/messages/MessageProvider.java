package uk.co.slc.sfd.cp.ft.jsf.messages;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class MessageProvider.
 */
@Slf4j
public class MessageProvider {

    /** The Constant CUSTOMER_KEY. */
    /** This key is used for customer data from session */
    public static final String BUNDLE_KEY = "literal";
    public static final String MSGS_BUNDLE_KEY = "msgs";
    public static final String BUNDLE_LOOKUP_FAILURE_FORMAT = "??? %s ??? not found";

    private MessageProvider() {
    }

    /**
     * Gets the bundle.
     * 
     * @return the bundle
     */
    private static ResourceBundle getBundle() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getResourceBundle(context, BUNDLE_KEY);
    }

    private static ResourceBundle getMsgsBundle() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getResourceBundle(context, MSGS_BUNDLE_KEY);
    }

    /**
     * Contains key.
     * 
     * @param key
     *            the key
     * @return true, if successful
     */
    public static boolean containsKey(final String key) {
        return getBundle().containsKey(key);
    }

    public static String getMessageValue(String key) {
        return getValueFromBundle(key, getBundle());
    }

    public static String getMessageFromMsgs(String key) {
        return getValueFromBundle(key, getMsgsBundle());
    }

    private static String getValueFromBundle(String key, ResourceBundle bundle) {
        String result = null;
        try {
            result = bundle.getString(key);
        } catch (MissingResourceException e) {
            log.info("Exception handled...", e);
            result = String.format(BUNDLE_LOOKUP_FAILURE_FORMAT, key);
        }
        return result;
    }

    /**
     * Gets the value by formatting message with given arguments
     * 
     * @param key
     *            the key
     * @param args
     *            the args
     * @return the value from the properties if the value found
     */
    public static String getValue(String key, Object... args) {
        String sentence = getValueFromBundle(key, getBundle());
        // format need to escape ' with ''
        String readySentence = sentence.replaceAll("'", "''");
        return MessageFormat.format(readySentence, args);
    }

    public static String getMsgsValue(String key) {
        return getValueFromBundle(key, getMsgsBundle());
    }
}