package uk.co.slc.sfd.cp.ft.jsf.contenteditable;

import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.context.ContextLoader;

@Slf4j
public final class ContentEditableUtils {

    public static final String PROP_BASE_URL = "propertiesUrl";
    public static final String PROP_BASE_DIR = "propertiesDirectory";
    public static final String PROP_BASE_TTL = "resourceTimeToLive";

    private static String urlPropertyName = PROP_BASE_URL;
    private static String directoryPropertyName = PROP_BASE_DIR;
    private static String resourceTimeToLiveName = PROP_BASE_TTL;

    private static final long DEFAULT_TIME_TO_LIVE = 3600000L; //1 Hour

    private volatile static Environment env;

    private ContentEditableUtils() {
        assert false : "do not instantiate internally";
    }

    private static String lookupSpringProperty(String name) {
        if (null == env) {
            synchronized (ContentEditableUtils.class) {
                if (null == env) {
                    ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
                    env = context.getEnvironment();
                }
            }
        }

        return env.getProperty(name);
    }

    public static void setSpringEnvironement(Environment env) {
        ContentEditableUtils.env = env;
    }

    public static boolean inDevelopmentMode() {
        return lookupSpringProperty(directoryPropertyName) != null;
    }

    public static boolean inHttpMode() {
        return lookupSpringProperty(urlPropertyName) != null;
    }

    public static String getPropertiesDirectory() {
        return lookupSpringProperty(directoryPropertyName);
    }

    public static String getPropertiesUrl() {
        return lookupSpringProperty(urlPropertyName);
    }

    public static String facesLocale() {
        return FacesContext.getCurrentInstance().getViewRoot().getLocale().toString();
    }

    public static String filenameWithLocale(String locale) {
        return "/MessageResources_" + locale + ".properties";
    }

    public static String getUrlPropertyName() {
        return urlPropertyName;
    }

    public static void setUrlPropertyName(String urlPropertyName) {
        ContentEditableUtils.urlPropertyName = urlPropertyName;
    }

    public static String getDirectoryPropertyName() {
        return directoryPropertyName;
    }

    public static void setDirectoryPropertyName(String directoryPropertyName) {
        ContentEditableUtils.directoryPropertyName = directoryPropertyName;
    }

    public static String getResourceTimeToLiveName() {
        return resourceTimeToLiveName;
    }

    public static void setResourceTimeToLiveName(String resourceTimeToLiveName) {
        ContentEditableUtils.resourceTimeToLiveName = resourceTimeToLiveName;
    }

    public static long getResourceBundleTimeToLive() {
        String timeString = lookupSpringProperty(resourceTimeToLiveName);
        if (null != timeString) {
            try {
                return Long.parseLong(timeString);
            } catch (NumberFormatException e) {
                log.info("Exception handled...", e);
                return DEFAULT_TIME_TO_LIVE;
            }
        } else {
            return DEFAULT_TIME_TO_LIVE;
        }
    }
}
