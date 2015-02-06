package uk.co.slc.sfd.cp.ft.jsf.component;

import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang3.StringUtils;

@FacesValidator("NinoValidator")
public class NinoValidator implements Validator {

    private static final String NINO_REGEX = "^[A-CEGHJ-PR-TW-Z][A-CEGHJ-NPR-TW-Z] ?[0-9]{2} ?[0-9]{2} ?[0-9]{2} ?[ABCD]?";
    private static final String NINO_GROUP_REGEX = "^(GB|BG|NK|KN|TN|NT|ZZ).*$";

    private static final String DEFAULT_ERROR_MSG = "Invalid NINO number.";

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
        String stringValue = value.toString();

        if (StringUtils.isBlank(stringValue)) {
            return;
        }

        if (Pattern.matches(NINO_GROUP_REGEX, stringValue)) {
            FacesMessage msg = new FacesMessage(DEFAULT_ERROR_MSG);
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }

        if (!Pattern.matches(NINO_REGEX, stringValue)) {
            FacesMessage msg = new FacesMessage(DEFAULT_ERROR_MSG);
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

}
