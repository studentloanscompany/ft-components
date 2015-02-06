package uk.co.slc.sfd.cp.ft.jsf.messages;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

/**
 * The Class ErrorUtils.
 */
public final class ErrorUtils {

    private ErrorUtils() {
    }

    public static FacesMessage createFacesErrorMessage(final String text) {
        return createFacesMessage(text, FacesMessage.SEVERITY_ERROR);
    }

    public static FacesMessage createFacesMessage(final String text, final Severity severity) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(severity);
        message.setDetail(text);
        message.setSummary(message.getDetail());
        return message;
    }

    public static void addFacesMessage(final String componentId, final FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(componentId, message);
    }

}
