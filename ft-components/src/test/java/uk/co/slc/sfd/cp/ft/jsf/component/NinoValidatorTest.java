package uk.co.slc.sfd.cp.ft.jsf.component;

import javax.faces.validator.ValidatorException;

import org.junit.Test;

public class NinoValidatorTest {

    NinoValidator validator = new NinoValidator();

    @Test
    public void testValid() {
        //None of these should throw an exception
        validator.validate(null, null, "NR334455A");
        validator.validate(null, null, "");
    }

    @Test(expected = ValidatorException.class)
    public void testInvalid() {
        validator.validate(null, null, "XXXXX");
    }

    @Test(expected = ValidatorException.class)
    public void testInvalidPrefix() {
        validator.validate(null, null, "GB334455A");
    }
}
