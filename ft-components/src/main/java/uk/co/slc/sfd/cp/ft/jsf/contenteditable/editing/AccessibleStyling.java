package uk.co.slc.sfd.cp.ft.jsf.contenteditable.editing;

import java.util.HashMap;
import java.util.Map;

public class AccessibleStyling {

    private static final Map<String, String> REPLACEMENTS = new HashMap<>();

    static {
        REPLACEMENTS.put("<i>", "<em>");
        REPLACEMENTS.put("</i>", "</em>");
        REPLACEMENTS.put("<b>", "<strong>");
        REPLACEMENTS.put("</b>", "</strong>");
        REPLACEMENTS.put("£", "&pound;");
        REPLACEMENTS.put(Character.toString('\u2014'), "&mdash;");
    }

    /*
    In Chrome you can press <ctrl>+B or <ctrl>+I to make the text bold or italic, we convert these to <strong> and <em>
    */
    public String accessorize(String value) {
        //convert all &amp; to &, they are escaped by default
        value = value.replace("&amp;", "&");
        for (String target : REPLACEMENTS.keySet()) {
            value = value.replace(target, REPLACEMENTS.get(target));
        }
        return value.trim();
    }
}
