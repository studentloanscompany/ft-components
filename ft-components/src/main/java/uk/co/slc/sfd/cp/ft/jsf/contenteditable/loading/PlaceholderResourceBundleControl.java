package uk.co.slc.sfd.cp.ft.jsf.contenteditable.loading;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "placeholderResourceBundleControl")
@SessionScoped
public class PlaceholderResourceBundleControl implements Serializable {

    private static final long serialVersionUID = -4405134750177139247L;

    // This is used to control {{el expression}} evaluation
    protected boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void toggleEnabled() {
        enabled = !enabled;
    }

    public boolean isContentEditModeEnabled() {
        return !enabled;
    }

    public void toggleContentEditMode() {
        toggleEnabled();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
