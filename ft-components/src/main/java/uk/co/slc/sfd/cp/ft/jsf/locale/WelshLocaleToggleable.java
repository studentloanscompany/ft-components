package uk.co.slc.sfd.cp.ft.jsf.locale;

/**
 * File: WelshLocaleToggleable.java
 * 
@author slc
 *
 */
public interface WelshLocaleToggleable {

    /**
     * Check is selected locale country is wales
     * 
     * @return
     */
    boolean isWelsh();

    /**
     * Return a toggle link to switch between
     * english-welsh and welsh locale.
     * 
     * @return
     */
    String getWelshLocaleToggleLink();
}
