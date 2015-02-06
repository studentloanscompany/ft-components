/*
* GPL Classpath Exception:
* Oracle designates these particular files as subject to the "Classpath"
* exception as provided by Oracle in the GPL Version 2 section of the License
* file that accompanied this code - 
*		- com.sun.faces.renderkit.html_basic.RadioRenderer 
*		- com.sun.faces.renderkit.Attribute
*		- com.sun.faces.renderkit.RenderKitUtils
*		- com.sun.faces.util.RequestStateManager
 See https://glassfish.java.net/public/CDDL+GPL_1_1.html for more info
*/

package uk.co.slc.sfd.cp.ft.jsf.renderer;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import lombok.extern.slf4j.Slf4j;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.RadioRenderer;
import com.sun.faces.util.RequestStateManager;

@Slf4j
public class SLCRadioRenderer extends RadioRenderer {

    private static final Attribute[] ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.SELECTONERADIO);

    @Override
    @SuppressWarnings("rawtypes")
    /**
     * Method to render the radio option.
     * @param context
     * @param component
     * @param converter
     * @param curItem
     * @param currentSelections
     * @param submittedValues
     * @param alignVertical
     * @param itemNumber
     * @param optionInfo
     * @throws IOException
     */
    protected void renderOption(FacesContext context, UIComponent component, Converter converter, SelectItem curItem, Object currentSelections, Object[] submittedValues, boolean alignVertical,
            int itemNumber, OptionComponentInfo optionInfo) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        assert writer != null;

        UISelectOne selectOne = (UISelectOne) component;
        Object curValue = selectOne.getSubmittedValue();
        if (curValue == null) {
            curValue = selectOne.getValue();
        }

        Class type = determineTypeToUse(curValue);

        Object itemValue = curItem.getValue();
        RequestStateManager.set(context, RequestStateManager.TARGET_COMPONENT_ATTRIBUTE_NAME, component);
        Object newValue;
        try {
            newValue = context.getApplication().getExpressionFactory().coerceToType(itemValue, type);
        } catch (ELException ele) {
            log.info("Exception handled: ", ele);
            newValue = itemValue;
        } catch (IllegalArgumentException iae) {
            log.info("Exception handled: ", iae);
            newValue = itemValue;
        }

        boolean checked = null != newValue && newValue.equals(curValue);

        if (optionInfo.isHideNoSelection() && curItem.isNoSelectionOption() && curValue != null && !checked) {
            return;
        }
        renderRadioOptionAttributes(context, component, converter, curItem, optionInfo, writer, checked);
    }

    @SuppressWarnings("rawtypes")
    /**
     * Method to determine the type for the value of 
     * radio option to be rendered
     * @param curValue
     * @return Class
     */
    private Class determineTypeToUse(Object curValue) {
        Object newValue = null;
        Class type = String.class;
        if (curValue != null) {
            type = curValue.getClass();
            if (type.isArray()) {
                newValue = ((Object[]) curValue)[0];
                if (null != newValue) {
                    type = newValue.getClass();
                }
            } else if (Collection.class.isAssignableFrom(type)) {
                Iterator valueIter = ((Collection) curValue).iterator();
                if (null != valueIter && valueIter.hasNext()) {
                    newValue = valueIter.next();
                    if (null != newValue) {
                        type = newValue.getClass();
                    }
                }
            }
        }
        return type;
    }

    /**
     * Method to render radio option attributes
     * @param context
     * @param component
     * @param converter
     * @param curItem
     * @param optionInfo
     * @param writer
     * @param checked
     * @throws IOException
     */
    private void renderRadioOptionAttributes(FacesContext context, UIComponent component, Converter converter, SelectItem curItem, OptionComponentInfo optionInfo, ResponseWriter writer,
            boolean checked) throws IOException {
        String labelClass;
        String idString = component.getClientId();
        if (optionInfo.isDisabled() || curItem.isDisabled()) {
            labelClass = optionInfo.getDisabledClass();
        } else {
            labelClass = optionInfo.getEnabledClass();
        }
        writer.writeText("\n", component, null);
        writer.startElement("div", component);
        writer.writeAttribute("class", "gds-radio", "class");

        writer.startElement("input", component);
        writer.writeAttribute("type", "radio", "type");
        if (checked) {
            writer.writeAttribute("checked", Boolean.TRUE, null);
        }
        writer.writeAttribute("name", idString, "clientId");

        String itemName = curItem.getValue() == null ? "null" : curItem.getValue().toString().replaceAll("\\s", "");

        writer.writeAttribute("id", itemName, "id");
        writer.writeAttribute("value", getFormattedValue(context, component, curItem.getValue(), converter), "value");
        if (!optionInfo.isDisabled() && curItem.isDisabled()) {
            writer.writeAttribute("disabled", true, "disabled");
        }

        RenderKitUtils.renderPassThruAttributes(context, writer, component, ATTRIBUTES, getNonOnClickSelectBehaviors(component));
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        RenderKitUtils.renderSelectOnclick(context, component, false);
        writer.endElement("input");

        writer.startElement("label", component);
        writer.writeAttribute("for", itemName, "for");
        if (labelClass != null) {
            writer.writeAttribute("class", "block-label, " + labelClass, "labelClass");
        } else {
            writer.writeAttribute("class", "block-label", "labelClass");
        }
        String itemLabel = curItem.getLabel();
        if (itemLabel != null) {
            writer.writeText(" ", component, null);
            if (!curItem.isEscape()) {
                writer.write(itemLabel);
            } else {
                writer.writeText(itemLabel, component, "label");
            }
        }
        writer.endElement("label");

        writer.endElement("div");
        writer.writeText("\n", component, null);
    }

    @Override
    /**
     * Method overridden to render begin text which wraps the radio component
     * @param component
     * @param border
     * @param alignVertical
     * @param context
     * @param outerTable
     * @throws IOException 
     */
    protected void renderBeginText(UIComponent component, int border, boolean alignVertical, FacesContext context, boolean outerTable) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        assert writer != null;
        writer.startElement("div", component);
        writer.writeText("\n", component, null);
    }

    @Override
    /**
     * Method overridden to render end text which culminates the radio component
     * @param component
     * @param alignVertical
     * @param context
     * @throws IOException 
     */
    protected void renderEndText(UIComponent component, boolean alignVertical, FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        assert writer != null;
        writer.endElement("div");
    }
}
