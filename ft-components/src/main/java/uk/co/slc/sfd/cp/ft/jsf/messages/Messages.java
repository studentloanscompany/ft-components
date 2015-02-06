package uk.co.slc.sfd.cp.ft.jsf.messages;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Cross field validation messages - used by errorMessage.xhtml
@author slc
 */
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, MessageEntry> messageMap = new HashMap<String, MessageEntry>();

    public void add(String field, String property, Object... args) {
        messageMap.put(field, new MessageEntry(property, args));
    }

    public void remove(String name) {
        messageMap.remove(name);
    }

    public void clear() {
        messageMap.clear();
    }

    public String getMessage(String field) {
        String message = null;
        MessageEntry entry = messageMap.get(field);
        if (null != entry) {
            message = entry.getProperty();
        }
        return message;
    }

    public MessageEntry getEntry(String field) {
        return messageMap.get(field);
    }

    public Map<String, MessageEntry> getMessageMap() {
        return messageMap;
    }

    public boolean isEmpty() {
        return messageMap.isEmpty();
    }

    /**This class is added in order to support messages with arguments
    @author slc
     * */
    public static class MessageEntry implements Serializable {

        private static final long serialVersionUID = 2221980311035515367L;
        private String property;
        private Object[] args;

        public MessageEntry() {
        }

        public MessageEntry(String property, Object... args) {
            this.property = property;
            this.args = args;
        }

        public String getProperty() {
            return property;
        }

        public Object[] getArgs() {
            return args;
        }
    }
}
