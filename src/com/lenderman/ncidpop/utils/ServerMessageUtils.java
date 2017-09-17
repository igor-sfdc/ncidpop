/*******************************************************
 * Server Message Utils
 *******************************************************/
package com.lenderman.ncidpop.utils;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import com.lenderman.ncidpop.data.ServerData;

/**
 * Utilities for sending server messages
 * 
 * @author Chris Lenderman
 */
public class ServerMessageUtils
{
    /** Class logger */
    private static Logger log = Logger.getLogger(ServerMessageUtils.class);

    /** The date format */
    private static final String DATE_FORMAT = "MMddyyyy";

    /** The European date format */
    private static final String EUROPEAN_DATE_FORMAT = "ddMMyyyy";

    /** The time format */
    private static final String TIME_FORMAT = "HHmm";

    /** Static instance of date format */
    private static final SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);

    /** Static instance of European date format */
    private static final SimpleDateFormat edf = new SimpleDateFormat(
            EUROPEAN_DATE_FORMAT);

    /** Static instance of time format */
    private static final SimpleDateFormat tf = new SimpleDateFormat(TIME_FORMAT);

    /**
     * Gets the host name
     * 
     * @return String
     */
    private static String getHostName()
    {
        String hostname = "NCIDpop";

        try
        {
            hostname = InetAddress.getLocalHost().getHostName();
        }
        catch (Exception e)
        {
            log.warn("Couldn't get hostname: " + e);
        }
        return hostname;
    }

    /**
     * Enqueues the relay message for the server
     * 
     * @param data server data instance
     * @param argument the argument (can be null)
     * @param message the message (can be null)
     * @param gatewayName the gateway name
     * @param command the command for the relay message
     */
    public static void sendRelayMessage(ServerData data, String argument,
            String message, String gatewayName, String command)
    {
        if (data != null)
        {
            Date date = new Date();
            boolean useEuropeanDateFormat = PrefUtils.instance
                    .getBoolean(PrefUtils.PREF_USE_EUROPEAN_DATE_TIME);

            data.serverEnqueuedLines.add("RLY: "
                    + (message != null ? message + " " : "")
                    + "###DATE*"
                    + (useEuropeanDateFormat ? edf.format(date) : df
                            .format(date)) + "*TIME*" + tf.format(date)
                    + "*TO*" + gatewayName + "*FROM*" + getHostName() + "*CMD*"
                    + command + "*"
                    + (argument != null ? "ARG1*" + argument + "*" : ""));
        }
    }

    /**
     * Enqueues the message data for the server
     * 
     * @param ServerData server data instance
     * @param String the message to send
     */
    public static boolean sendMessageData(ServerData data, String message)
    {
        if (data != null)
        {
            data.serverEnqueuedLines.add("MSG: " + message + " ###NAME*"
                    + System.getProperty("user.name") + "*LINE*"
                    + getHostName() + "*MTYPE*IN*");

            return true;
        }

        return false;
    }
}