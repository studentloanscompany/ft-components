package uk.co.slc.sfd.cp.ft.jsf.contenteditable.loading;

import static uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableUtils.facesLocale;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.lang3.LocaleUtils;

import uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableUtils;

public class LocaleAwareResourceBundle extends ResourceBundle {

    private long lastRefreshTime = 0;
    private long timeToLive = ContentEditableUtils.getResourceBundleTimeToLive();
    Map<String, PropertyResourceBundle> bundles = new HashMap<>();

    @Override
    protected Object handleGetObject(String key) {
        reloadIfExpired();

        Object result = localizedBundle(facesLocale()).handleGetObject(key);

        if (result == null) {
            result = handleMissingProperty(key);
        }

        result = changeAnswerHook(key, result);

        return result;
    }

    protected boolean isKeyNotNull(String key) {
        reloadIfExpired();

        Object result = localizedBundle(facesLocale()).handleGetObject(key);

        return result != null;
    }

    private void reloadIfExpired() {
        long currentTime = System.currentTimeMillis();
        long bundleAge = currentTime - lastRefreshTime;
        //If in content edit mode then expire all bundles after 10 seconds
        if (timeToLive > 0 && timeToLive < bundleAge || (ContentEditableUtils.inDevelopmentMode() && 10000 < bundleAge)) {
            synchronized (bundles) {
                //Need to re-check the expiration criteria in case another thread has already been through the sync block.
                if (timeToLive > 0 && timeToLive < bundleAge || (ContentEditableUtils.inDevelopmentMode() && 10000 < bundleAge)) {
                    bundles.clear();
                    lastRefreshTime = System.currentTimeMillis();
                }
            }
        }
    }

    /**
     * Load from parent bundle. Return key in case of missing property.
     * 
     * @param key
     * @return
     */
    private Object handleMissingProperty(String key) {
        PropertyResourceBundle resourceBundle = (PropertyResourceBundle) parent;
        Object result = resourceBundle.handleGetObject(key);

        if (result == null) {
            return "???" + key + "???";
        }
        return result;
    }

    // Allow subclasses to change the answer
    protected Object changeAnswerHook(String key, Object rawValue) {
        return rawValue;
    }

    @Override
    public Enumeration<String> getKeys() {
        return localizedBundle(facesLocale()).getKeys();
    }

    private PropertyResourceBundle localizedBundle(String locale) {
        PropertyResourceBundle bundle = bundles.get(locale);

        if (bundle == null) {
            bundle = new ResourceBundleLocationLoader(locale).loadBundle();
            bundles.put(locale, bundle);
        }

        localizedBaseResourceBundle(locale);

        return bundle;
    }

    /**
     * Get/Load base resource bundle, extracted language part from locale.
     * Only load parent if its empty or not yet loaded.
     * 
     * @param localeStr
     * @return
     */
    private void localizedBaseResourceBundle(String localeStr) {
        if (parent == null || parent.keySet().isEmpty()) {
            Locale locale = LocaleUtils.toLocale(localeStr);
            PropertyResourceBundle baseBundle = new ResourceBundleLocationLoader(locale.getLanguage()).loadBundle();
            setParent(baseBundle);
        }
    }
}
