package uk.co.slc.sfd.cp.ft.jsf.contenteditable.loading;

import static uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableUtils.filenameWithLocale;
import static uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableUtils.inDevelopmentMode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableUtils;

public class ResourceBundleLocationLoader {

    @SuppressWarnings("serial")
    public static final class ResourceBundleLocationLoaderException extends RuntimeException {

        public ResourceBundleLocationLoaderException(Exception cause) {
            super(cause);
        }
    }

    private String locale;

    public ResourceBundleLocationLoader(String locale) {
        this.locale = locale;
    }

    public PropertyResourceBundle loadBundle() {
        try (InputStream propertiesStream = decideInputStream()) {
            return new PropertyResourceBundle(propertiesStream);
        } catch (IOException e) {
            throw new IllegalStateException("Problem loading the Message Resources for " + locale, e);
        }
    }

    private InputStream decideInputStream() throws FileNotFoundException {
        if (inDevelopmentMode()) {
            ResourceBundle.clearCache();
            return loadFromFilesystem();
        }

        if (ContentEditableUtils.inHttpMode()) {
            return loadFromHttp();
        }
        return loadFromClassPath();
    }

    private InputStream loadFromFilesystem() throws FileNotFoundException {
        return new FileInputStream(ContentEditableUtils.getPropertiesDirectory() + filenameWithLocale(locale));
    }

    private InputStream loadFromHttp() {
        try {
            return new URL(ContentEditableUtils.getPropertiesUrl() + filenameWithLocale(locale)).openStream();
        } catch (IOException e) {
            throw new ResourceBundleLocationLoaderException(e);
        }
    }

    private InputStream loadFromClassPath() {
        return getClass().getResourceAsStream("/messages" + filenameWithLocale(locale));
    }

}
