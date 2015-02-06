package uk.co.slc.sfd.cp.ft.jsf.contenteditable.saving;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import uk.co.slc.sfd.cp.ft.jsf.contenteditable.ContentEditableUtils;

@ManagedBean
@SessionScoped
public class ContentEditableManagedBean implements Serializable {

    private static final long serialVersionUID = -4970638035661387395L;

    public String getFacesLocale() {
        return ContentEditableUtils.facesLocale();
    }

    public boolean isInDevelopmentMode() {
        return ContentEditableUtils.inDevelopmentMode();
    }
}
