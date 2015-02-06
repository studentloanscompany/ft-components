package uk.co.slc.sfd.cp.ft.jsf.contenteditable.saving;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableUtils.filenameWithLocale;
import static uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableUtils.inDevelopmentMode;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableUtils;
import uk.co.slc.sfd.cp.ft.jsf.contenteditable.editing.AccessibleStyling;

public class PropertiesController {

    private final Lock lock = new ReentrantLock();

    private static final long LOCK_TIME = 50;

    public void updateProperty(String id, String locale, String value) throws InterruptedException {
        if (!inDevelopmentMode()) {
            return;
        }

        while (true) {
            if (lock.tryLock(LOCK_TIME, MILLISECONDS)) {
                try {
                    save(id, locale, value);
                    return;
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private void save(String id, String locale, String value) {
        try {
            String localeFile = getLocaleFile(locale);
            String styledValue = new AccessibleStyling().accessorize(value);
            PropertiesFileUpdater.update(id, styledValue, localeFile);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String getLocaleFile(String locale) throws IOException {
        String fullPath = ContentEditableUtils.getPropertiesDirectory() + filenameWithLocale(locale);
        makePropertiesFileIfMissing(fullPath);
        return fullPath;
    }

    private void makePropertiesFileIfMissing(String fullPath) throws IOException {
        if (new File(fullPath).exists()) {
            return;
        }

        if (!new File(fullPath).createNewFile()) {
            throw new IllegalArgumentException("Cannot create new properties file");
        }
    }

}
