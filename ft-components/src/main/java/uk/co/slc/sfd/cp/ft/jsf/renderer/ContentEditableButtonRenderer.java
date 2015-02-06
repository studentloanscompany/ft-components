/*
* GPL Classpath Exception:
* Oracle designates these particular files as subject to the "Classpath"
* exception as provided by Oracle in the GPL Version 2 section of the License
* file that accompanied this code - 
*		- com.sun.faces.renderkit.html_basic.HtmlBasicRenderer 
*		- com.sun.faces.renderkit.Attribute
*		- com.sun.faces.renderkit.AttributeManager
*		- com.sun.faces.renderkit.RenderKitUtils
 See https://glassfish.java.net/public/CDDL+GPL_1_1.html for more info
*/

package uk.co.slc.sfd.cp.ft.jsf.renderer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer;

/**
 * Extension of com.sun.faces.renderkit.html_basic.HtmlBasicRenderer with ContentEditor "shim";
 */
public class ContentEditableButtonRenderer extends HtmlBasicRenderer {

    public static final String SPAN_DATA_MESSAGEID = "<span data-messageid=['\"].+['\"]>";
    public static final String SPAN = "</span>";

    private static final Attribute[] ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.COMMANDBUTTON);

    // ---------------------------------------------------------- Public Methods

    @Override
    public void decode(FacesContext context, UIComponent component) {

        rendererParamsNotNull(context, component);

        if (!shouldDecode(component)) {
            return;
        }

        String clientId = decodeBehaviors(context, component);

        if (wasClicked(context, component, clientId) && !isReset(component)) {
            component.queueEvent(new ActionEvent(component));

            if (logger.isLoggable(Level.FINE)) {
                logger.fine("This command resulted in form submission " + " ActionEvent queued.");
                logger.log(Level.FINE, "End decoding component {0}", component.getId());
            }
        }

    }

    private String contentEditableShim(String label, ResponseWriter writer) throws IOException {
        Pattern p = Pattern.compile(SPAN_DATA_MESSAGEID);
        Matcher m = p.matcher(label);
        String messageId = null;
        if (m.find()) {
            label = label.replaceFirst(SPAN_DATA_MESSAGEID, "");
            label = label.replaceFirst(SPAN, "");
            messageId = getMessageId(m.group(0));
        }

        if (messageId != null) {
            writer.writeAttribute("data-messageid", messageId, "data-messageid");
        }
        return label;
    }

    private String getMessageId(String s) {
        Pattern p = Pattern.compile("data-messageid=['\"](.+)['\"]>");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            return null;
        }
        return m.group(1);
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        String type = getButtonType(component);

        ResponseWriter writer = context.getResponseWriter();
        assert writer != null;

        String label = "";
        Object value = ((UICommand) component).getValue();
        if (value != null) {
            label = value.toString();
        }

        Collection<ClientBehaviorContext.Parameter> params = getBehaviorParameters(component);
        if (!params.isEmpty() && "submit".equals(type) || "button".equals(type)) {
            RenderKitUtils.renderJsfJs(context);
        }

        String imageSrc = (String) component.getAttributes().get("image");
        writer.startElement("input", component);
        writeIdAttributeIfNecessary(context, writer, component);
        String clientId = component.getClientId(context);
        // --------------- CONTENT EDITABLE BEGIN
        label = contentEditableShim(label, writer);
        // --------------- CONTENT EDITABLE END
        if (imageSrc != null) {
            writer.writeAttribute("type", "image", "type");
            writer.writeURIAttribute("src", RenderKitUtils.getImageSource(context, component, "image"), "image");
            writer.writeAttribute("name", clientId, "clientId");
        } else {
            writer.writeAttribute("type", type, "type");
            writer.writeAttribute("name", clientId, "clientId");
            writer.writeAttribute("value", label, "value");
        }

        RenderKitUtils.renderPassThruAttributes(context, writer, component, ATTRIBUTES, getNonOnClickBehaviors(component));

        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        String styleClass = (String) component.getAttributes().get("styleClass");
        if (styleClass != null && styleClass.length() > 0) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

        RenderKitUtils.renderOnclick(context, component, params, null, false);

        writer.endElement("input");

    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

        rendererParamsNotNull(context, component);

    }

    // --------------------------------------------------------- Private Methods

    /**
     * <p>Determine if this component was activated on the client side.</p>
     *
     * @param context the <code>FacesContext</code> for the current request
     * @param component the component of interest
     * @param clientId the client id, if it has been retrieved, otherwise null
     * @return <code>true</code> if this component was in fact activated,
     *  otherwise <code>false</code>
     */
    private static boolean wasClicked(FacesContext context, UIComponent component, String clientId) {

        // Was our command the one that caused this submission?
        // we don' have to worry about getting the value from request parameter
        // because we just need to know if this command caused the submission. We
        // can get the command name by calling currentValue. This way we can
        // get around the IE bug.

        if (clientId == null) {
            clientId = component.getClientId(context);
        }

        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.get(clientId) == null) {

            // Check to see whether we've got an action event
            // as a result of a partial/behavior postback.
            if (RenderKitUtils.isPartialOrBehaviorAction(context, clientId)) {
                return true;
            }

            StringBuilder builder = new StringBuilder(clientId);
            String xValue = builder.append(".x").toString();
            builder.setLength(clientId.length());
            String yValue = builder.append(".y").toString();
            return requestParameterMap.get(xValue) != null && requestParameterMap.get(yValue) != null;
        }
        return true;

    }

    /**
     * @param component the component of interest
     * @return <code>true</code> if the button represents a <code>reset</code>
     *  button, otherwise <code>false</code>
     */
    private static boolean isReset(UIComponent component) {
        return "reset".equals(component.getAttributes().get("type"));
    }

    /**
     * <p>If the component's type attribute is null or not equal
     * to <code>reset</code>, <code>submit</code> or <code>button</code>,
     * default to <code>submit</code>.
     * @param component the component of interest
     * @return the type for this button
     */
    private static String getButtonType(UIComponent component) {
        String type = (String) component.getAttributes().get("type");
        if (type == null || (!"reset".equals(type) && !"submit".equals(type) && !"button".equals(type))) {
            type = "submit";
            // This is needed in the decode method
            component.getAttributes().put("type", type);
        }
        return type;
    }

	/**
     * Returns the Behaviors map, but only if it contains some entry other
	 * renderPassThruAttributes() in the very common case where the
     * button only contains an "action" (or "click") Behavior.  In that
     * we pass a null Behaviors map into renderPassThruAttributes(),
     * which allows us to take a more optimized code path.
     * @param component the component of interest
     * @return Map behaviours
     */
    private static Map<String, List<ClientBehavior>> getNonOnClickBehaviors(UIComponent component) {
        return getPassThruBehaviors(component, "click", "action");
    }

}
