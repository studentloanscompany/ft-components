package uk.co.slc.sfd.cp.ft.jsf.validator;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import uk.co.slc.sfd.cp.ft.jsf.messages.MessageProvider;

/**
 * The class PostcodeValidator validates UK post codes. Configured in
 * faces-config.xml. <validator> <validator-id>postcodeValidator</validator-id>
 * <validator-class>uk.co.slc.sfd.cp.can.integration.validator.PostcodeValidator
 * </validator-class> </validator> Although validators in JSF 2.0 are
 * configured. using @FacesValidator annotation, but these annotation not get
 * scanned in non Glassfish container(s).
 * 
@author slc
 * 
 */
@FacesValidator("postcodeValidatorFT")
public class PostcodeValidator implements Validator {

    /** REGEX to validate UK postcodes. **/
    private static final String POSTCODE_REGEX = "(GIR 0AA)|((([A-Z-[QVX]][0-9][0-9]?)|(([A-Z-[QVX]][A-Z-[IJZ]][0-9][0-9]?)|(([A-Z-[QVX]][0-9][A-HJKSTUW])|([A-Z-[QVX]][A-Z-[IJZ]][0-9][ABEHMNPRVWXY])))) [0-9][A-Z-[CIKMOV]]{2})";
    private static final String POSTCODE_REGEX_NON_UK = "^[A-Za-z0-9\\s]+$";
    private Pattern pattern;
    private Matcher matcher;

    private static final String DEFAULT_COUNTRY_GB = "GB";
    private static final String DEFAULT_COUNTRY_UK = "UK";

    public PostcodeValidator() {
        pattern = Pattern.compile(POSTCODE_REGEX);
    }

    /**
     * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
     * @throws ValidatorException ( if their is a validation exception )
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {

        // Postcode text input value part 1
        UIInput postcodeInput = (UIInput) component;

        // Country text input value
        String country = (String) postcodeInput.getAttributes().get("country");
        String postcode = value.toString().toUpperCase(Locale.getDefault());

        if (country == null) {
            country = DEFAULT_COUNTRY_GB;
        }

        if (DEFAULT_COUNTRY_GB.equals(country) || DEFAULT_COUNTRY_UK.equals(country)) {
            //  If postcode doesn't include space then add one 4 from end to ensure correct validation
            if (!postcode.contains(" ") && postcode.length() > 4) {
                postcode = new StringBuilder(postcode).insert(postcode.length() - 3, " ").toString();
            }

            //If no post-code was entered do not fail. Let "optional" validation check for this.
            if (1 <= postcode.length()) {
                // All post codes must be in upper case as configured in
                // POSTCODE_REGEX.
                matcher = pattern.matcher(postcode);

                if (!matcher.matches()) {
                    String errorMessage = MessageProvider.getMsgsValue("postcodelookup_InvalidPostcodeMessage");
                    FacesMessage message = new FacesMessage(errorMessage, errorMessage);
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);

                    context.addMessage(postcodeInput.getClientId(), message);

                    // Throwing exception stops invoking address search service.
                    throw new ValidatorException(message);
                }
            }
        } else {
            pattern = Pattern.compile(POSTCODE_REGEX_NON_UK);
            if (1 <= postcode.length()) {
                matcher = pattern.matcher(postcode);

                if (!matcher.matches()) {
                    String errorMessage = MessageProvider.getMsgsValue("postcodelookup_NonUKInvalidPostcodeMessage");
                    FacesMessage message = new FacesMessage(errorMessage, errorMessage);
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    context.addMessage(postcodeInput.getClientId(), message);
                    // Throwing exception stops invoking address search service.
                    throw new ValidatorException(message);
                }
            }
        }
    }

}
