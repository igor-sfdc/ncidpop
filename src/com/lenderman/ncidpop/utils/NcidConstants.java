/*******************************************************
 * NCID Constants
 *******************************************************/
package com.lenderman.ncidpop.utils;

import java.awt.Image;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.ImageIcon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lenderman.ncidpop.NcidPopMainDisplay;

/**
 * Common constants used by the application components.
 * 
 * @author Chris Lenderman
 */
public class NcidConstants
{
    /** Server web page */
    public static final String NCIDPOP_WEB_PAGE = "http://ncid.sourceforge.net/ncidpop/ncidpop.html";

    /** The small connected icon */
    public static final ImageIcon ICON_CONNECTED_SMALL = new ImageIcon(
            NcidPopMainDisplay.class
                    .getResource("/com/lenderman/ncidpop/images/ncidpop_small.png"));

    /** The connected icon */
    public static final ImageIcon ICON_CONNECTED = new ImageIcon(
            NcidPopMainDisplay.class
                    .getResource("/com/lenderman/ncidpop/images/ncidpop.png"));

    /** The "some connected" icon */
    public static final ImageIcon ICON_SOME_CONNECTED = new ImageIcon(
            NcidPopMainDisplay.class
                    .getResource("/com/lenderman/ncidpop/images/ncidpop_some_connected.png"));

    /** The disconnected icon */
    public static final ImageIcon ICON_NOT_CONNECTED = new ImageIcon(
            NcidPopMainDisplay.class
                    .getResource("/com/lenderman/ncidpop/images/ncidpop_none_connected.png"));

    /** The small connected icon */
    public static final ImageIcon ICON_SMALL_CONNECTED = new ImageIcon(
            ICON_CONNECTED.getImage().getScaledInstance(16, 16,
                    Image.SCALE_SMOOTH));

    /** The small "some connected" icon */
    public static final ImageIcon ICON_SMALL_SOME_CONNECTED = new ImageIcon(
            ICON_SOME_CONNECTED.getImage().getScaledInstance(16, 16,
                    Image.SCALE_SMOOTH));

    /** The small disconnected icon */
    public static final ImageIcon ICON_SMALL_NOT_CONNECTED = new ImageIcon(
            ICON_NOT_CONNECTED.getImage().getScaledInstance(16, 16,
                    Image.SCALE_SMOOTH));

    /** Default server port */
    public static final int DEFAULT_SERVER_PORT = 3333;

    /** Call Info Header provided by the NCID server */
    public static final String CALL_INFO_HEADER = "CIDINFO: ";

    /** Server Info Message Header provided by the NCID server */
    public static final String SERVER_INFO_MSG_HEADER = "200 ";

    /** Server API Version line provided by the NCID server */
    public static final String SERVER_API_VERSION_LINE = "210 ";

    /** Call log not sent message line provided by the NCID server */
    public static final String CALL_LOG_NOT_SENT = "251 ";

    /** Call Log Message Header provided by the NCID server */
    private static final HashSet<String> CALL_LOG_MSG_HEADERS = new HashSet<String>();
    static
    {
        CALL_LOG_MSG_HEADERS.add("250 ");
        CALL_LOG_MSG_HEADERS.add(CALL_LOG_NOT_SENT);
        CALL_LOG_MSG_HEADERS.add("252 ");
        CALL_LOG_MSG_HEADERS.add("253 ");
    }

    /** End of Server Startup provided by the NCID server */
    public static final String END_OF_SERVER_STARTUP_MSG_HEADER = "300 ";

    /** Server OK Response Data Block begin header provided by the NCID server */
    public static final String SERVER_OK_BLOCK_BEGIN_HEADER = "400 ";

    /**
     * Server ACCEPT/REJECT Data Block begin header provided by the NCID server
     */
    public static final String SERVER_ACCEPT_REJECT_BEGIN_HEADER = "401 ";

    /** Server handled request response begin header provided by the NCID server */
    public static final String SERVER_HANDLED_REQUEST_RESPONSE_BEGIN_HEADER = "402 ";

    /** Server info selection response begin header provided by the NCID server */
    public static final String SERVER_INFO_SELECTION_RESPONSE_BEGIN_HEADER = "403 ";

    /** Server End of Data Block provided by the NCID server */
    public static final String SERVER_END_OF_DATA = "410 ";

    /** Server End of Response provided by the NCID server */
    public static final String SERVER_END_OF_RESPONSE = "411 ";

    /** Server Info Line provided by the NCID server */
    public static final String SERVER_INFO_LINE = "INFO: ";

    /** Server Response Line provided by the NCID server */
    public static final String SERVER_RESP_LINE = "RESP: ";

    /** Server Options line header provided by the NCID server */
    public static final String SERVER_OPTIONS_HEADER = "OPT: ";

    /** Descriptions for server options */
    public static final HashMap<String, String> SERVER_OPTIONS_DESCRIPTIONS;
    static
    {
        String resource = FileUtils
                .resourceFileToString("/com/lenderman/ncidpop/resources/ServerOptions.json");

        HashMap<String, String> temp = new Gson().fromJson(resource,
                new TypeToken<HashMap<String, String>>()
                {
                }.getType());

        if (temp != null)
        {
            SERVER_OPTIONS_DESCRIPTIONS = temp;
        }
        else
        {
            SERVER_OPTIONS_DESCRIPTIONS = new HashMap<String, String>();
        }
    }

    /** Constant for Smart Phone Message Type */
    public static final String SMART_PHONE_MESSAGE_TYPE = "NOT";

    /** CID Message Header provided by the NCID server */
    private static final HashSet<String> MSG_TYPE_BASE = new HashSet<String>();
    static
    {
        MSG_TYPE_BASE.add(SMART_PHONE_MESSAGE_TYPE);
        MSG_TYPE_BASE.add("MSG");
    }

    /*** Base string for incoming calls provided by the NCID server */
    private static final HashSet<String> INCOMING_BASE = new HashSet<String>();

    /*** Base string for outgoing calls provided by the NCID server */
    private static final HashSet<String> OUTGOING_BASE = new HashSet<String>();

    /*** Base string for status calls provided by the NCID server */
    private static final HashSet<String> STATUS_BASE = new HashSet<String>();

    /*** Base string for any call type provided by the NCID server */
    private static final HashSet<String> ANY_CALL_BASE = new HashSet<String>();

    static
    {
        INCOMING_BASE.add("CID");
        INCOMING_BASE.add("PID");
        INCOMING_BASE.add("WID");
        OUTGOING_BASE.add("OUT");
        STATUS_BASE.add("HUP");
        STATUS_BASE.add("BLK");

        ANY_CALL_BASE.addAll(INCOMING_BASE);
        ANY_CALL_BASE.addAll(OUTGOING_BASE);
        ANY_CALL_BASE.addAll(STATUS_BASE);
    }

    /**
     * Returns true if the given line is an "call log message" line
     * 
     * @param String the string text to test
     * @return true if this is a "call log message" line, false otherwise
     */
    public static boolean isCallLogMessageLine(String text)
    {
        return (text != null) && (text.length() >= 4)
                && CALL_LOG_MSG_HEADERS.contains(text.substring(0, 4));
    }

    /**
     * Returns true if the given line to test is an "incoming call" line
     * 
     * @param String the string text to test
     * @return true if this is an "incoming call" line, false otherwise
     */
    public static boolean isIncomingCallLine(String text)
    {
        return (text != null) && (text.length() >= 5)
                && INCOMING_BASE.contains(text.substring(0, 3))
                && text.substring(3, 5).equals(": ");
    }

    /**
     * Returns true if the given line to test is a "CID" call line
     * 
     * @param String the string text to test
     * @return true if this is a "CID" call line, false otherwise
     */
    public static boolean isCidCallLine(String text)
    {
        return (text != null) && (text.length() >= 5)
                && ANY_CALL_BASE.contains(text.substring(0, 3))
                && text.substring(3, 5).equals(": ");
    }

    /**
     * Returns true if the given line to test is a message call line
     * 
     * @param String the string text to test
     * @return true if this is a message call line, false otherwise
     */
    public static boolean isMsgLine(String text)
    {
        return (text != null) && (text.length() >= 5)
                && MSG_TYPE_BASE.contains(text.substring(0, 3))
                && text.substring(3, 5).equals(": ");
    }

    /**
     * Returns true if the given line to test is a message call line
     * 
     * @param String the string text to test
     * @return true if this is a message call line, false otherwise
     */
    public static boolean isMsgLogLine(String text)
    {
        return (text != null) && (text.length() >= 8)
                && MSG_TYPE_BASE.contains(text.substring(0, 3))
                && text.substring(3, 8).equals("LOG: ");
    }

    /**
     * Returns true if the given line to test is a "CID" call log line
     * 
     * @param String the string text to test
     * @return true if this is a "CID" call log line, false otherwise
     */
    public static boolean isCidLogLine(String text)
    {
        return (text != null) && (text.length() >= 8)
                && ANY_CALL_BASE.contains(text.substring(0, 3))
                && text.substring(3, 8).equals("LOG: ");
    }
}