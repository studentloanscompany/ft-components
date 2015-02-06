package uk.co.slc.sfd.cp.ft.jsf.contenteditable.editing;

import static uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableUtils.inDevelopmentMode;
import uk.co.slc.sfd.cp.ft.jsf.contenteditable.loading.PlaceholderResourceBundle;

public class IdWrappedPlaceholderResourceBundle extends PlaceholderResourceBundle {

    public Object wrapped(String key, Object rawValue) {
        if (inDevelopmentMode()) {
            return "<span data-messageid='" + key + "'>" + rawValue + "</span>";
        }
        return rawValue;
    }

    @Override
    protected Object changeAnswerHook(String key, Object rawValue) {
        return wrapped(key, super.changeAnswerHook(key, rawValue));
    }
}
