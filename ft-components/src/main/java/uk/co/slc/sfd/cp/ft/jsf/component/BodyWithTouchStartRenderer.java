/**
 * 
 */
package uk.co.slc.sfd.cp.ft.jsf.component;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.sun.faces.renderkit.html_basic.BodyRenderer;

public class BodyWithTouchStartRenderer extends BodyRenderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);

        if (component.getAttributes().containsKey("ontouchstart")) {
            context.getResponseWriter().writeAttribute("ontouchstart", component.getAttributes().get("ontouchstart"), "ontouchstart");
        }
    }

}