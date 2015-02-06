package uk.co.slc.sfd.cp.ft.jsf.contenteditable.editing;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AccessibleStylingTest {

    public static final String SAMPLE_TEXT = "sample text";
    private AccessibleStyling accessibleStyling = new AccessibleStyling();

    @Test
    public void test_normalTextIsUnchanged() {
        assertEquals(SAMPLE_TEXT, accessibleStyling.accessorize(SAMPLE_TEXT));
    }

    @Test
    public void test_boldChangedToStrong() {
        assertEquals("<strong>" + SAMPLE_TEXT + "</strong>", accessibleStyling.accessorize("<b>" + SAMPLE_TEXT + "</b>"));
    }

    @Test
    public void test_italicChangedToEm() {
        assertEquals("<em>" + SAMPLE_TEXT + "</em>", accessibleStyling.accessorize("<i>" + SAMPLE_TEXT + "</i>"));
    }

    @Test
    public void test_ampersandNotEscaped() {
        assertEquals("&", accessibleStyling.accessorize("&amp;"));
    }
}
