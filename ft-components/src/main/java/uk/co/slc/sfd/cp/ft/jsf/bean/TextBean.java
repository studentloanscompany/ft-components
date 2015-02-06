package uk.co.slc.sfd.cp.ft.jsf.bean;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.lang3.StringUtils;

import uk.co.slc.sfd.cp.ft.jsf.messages.Messages.MessageEntry;

/**
 * Backing bean to help with token replacement when outputting text:
 * <h:outputText value="#{textBean.render( msgs, 'theKey', 'aaaa', 'bbbb' ) }"/>
 */
@ManagedBean
@SessionScoped
public class TextBean implements Serializable {

    private static final long serialVersionUID = 1269554528713802033L;
    private boolean enable = true;

    private String doRender(ResourceBundle res, String key, Object... objs) {
        String sentence = res.getString(key);
        if (sentence == null) { //NOSONAR
            return "?? " + key + " ??";
        }

        if (!enable) {
            return sentence;
        }

        // format need to escape ' with ''
        sentence = sentence.replaceAll("'", "''");
        return MessageFormat.format(sentence, objs);
    }

    public void toggleTokenReplace() {
        enable = !enable;
    }

    public String render(ResourceBundle res, MessageEntry entry) {
        if (null != entry) {
            return doRender(res, entry.getProperty(), entry.getArgs());
        } else {
            return StringUtils.EMPTY;
        }
    }

    public String keyParams(ResourceBundle res, String key, String param) {
        String s = res.getString(key + "." + param);
        if (s == null) { //NOSONAR
            return "?? " + key + "." + param + " ??";
        }
        return s;
    }

    public String render(ResourceBundle res, String key, Object... objs) {
        return doRender(res, key, objs);
    }

}
