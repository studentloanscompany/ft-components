package uk.co.slc.sfd.cp.ft.postcodelookup;

/**
 * An exception that is thrown if the address search fails.
 * 
 * @author slc
 */
public class PostcodeLookupServiceException extends Exception {

    /**  <code>serialVersionUID</code>. */
    private static final long serialVersionUID = -1068560766567027899L;

    private final String msgBundleCode;

    public static final String DEFAULT_MSG_CODE = "postcodelookup_ServiceUnavailableMessage";

    /**
     * Exception that indicates that there is an issue with the
     * Address Search.
     *
     * @param message a message regarding this exception
     */
    public PostcodeLookupServiceException(String message) {
        super(message);
        this.msgBundleCode = DEFAULT_MSG_CODE;
    }

    /**
     * Exception that indicates that there is an issue with the
     * Address Search.
     *
     * @param message a message regarding this exception
     * @param msgBundleCode - the msg bundle code used to look up the message to display to the user
     */
    public PostcodeLookupServiceException(String message, String msgBundleCode) {
        super(message);
        this.msgBundleCode = msgBundleCode;
    }

    /**
     * Exception that indicates that there is an issue with the
     * Address Search.
     *
     * @param message a message regarding this exception
     * @param throwable  the chainable exception.
     */
    public PostcodeLookupServiceException(String message, Throwable throwable) {
        super(message, throwable);
        this.msgBundleCode = DEFAULT_MSG_CODE;
    }

    /**
     * Exception that indicates that there is an issue with the
     * Address Search.
     *
     * @param message a message regarding this exception
     * @param msgBundleCode - the msg bundle code used to look up the message to display to the user
     * @param throwable  the chainable exception.
     */
    public PostcodeLookupServiceException(String message, String msgBundleCode, Throwable throwable) {
        super(message, throwable);
        this.msgBundleCode = msgBundleCode;
    }

    /**
     * @return Returns the msgBundleCode.
     */
    public String getMsgBundleCode() {
        return msgBundleCode;
    }
}
