package uk.co.slc.sfd.cp.ft.jsf.contenteditable.loading;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;
import uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableException;

@Slf4j
public class PlaceholderResourceBundle extends LocaleAwareResourceBundle {

    private static final Pattern EL_PATTERN = Pattern.compile("\\{\\{([^{}]*)\\}\\}");
    private static final Pattern MSG_PATTERN = Pattern.compile("\\[\\[([\\w.]+)\\]\\]");

    /**
     * A Set of the keys contained only in this ResourceBundle.
     */

    public PlaceholderResourceBundle() {
    }

    @Override
    protected Object changeAnswerHook(String key, Object rawValue) {
        // this may be slow to run so might consider taking it out
        FacesContext context = FacesContext.getCurrentInstance();
        PlaceholderResourceBundleControl control = (PlaceholderResourceBundleControl) context.getApplication().evaluateExpressionGet(context, "#{placeholderResourceBundleControl}",
                PlaceholderResourceBundleControl.class);
        if (!control.enabled) {
            return rawValue;
        }

        String val = rawValue.toString();
        try {
            PropertyRecorder.getInstance().store(key, val);
        } catch (ContentEditableException cee) {
            log.error("Caught ContentEditableException, value will not be written", cee);
            return null;
        }
        String oldVal = "";
        // call as many times as needed until no replacement occurs
        while (!val.equals(oldVal)) {
            oldVal = val;
            val = applyMessageFilter(applyElFilter(val));
        }
        return val;
    }

    private String applyElFilter(String value) {
        Matcher matcher = EL_PATTERN.matcher(value);
        while (matcher.find()) {
            String matched = matcher.group();
            String placeholderName = matcher.group(1);
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                Object obj = context.getApplication().evaluateExpressionGet(context, "#{" + placeholderName + "}", Object.class);
                value = obj == null ? value.replace(matched, "false") : value.replace(matched, obj.toString());
            } catch (Exception e) {
                // swallow exception.
                log.info("Exception handled...", e);
            }
        }
        return value;
    }

    private String applyMessageFilter(String value) {
        Matcher matcher = MSG_PATTERN.matcher(value);
        while (matcher.find()) {
            String matched = matcher.group();
            String placeholderName = matcher.group(1);
            try {
                value = value.replace(matched, getString(placeholderName));
            } catch (Exception e) {
                // swallow exception.
                log.info("Exception handled...", e);
            }
        }
        return value;
    }

}
