/*******************************************************
 * Message Data
 *******************************************************/
package com.lenderman.ncidpop.data;

import java.util.ArrayList;
import java.util.HashMap;
import com.lenderman.ncidpop.utils.LogParserUtils;
import com.lenderman.ncidpop.utils.NotificationUtils;
import com.lenderman.ncidpop.utils.PrefUtils;

/**
 * Message data class instance
 * 
 * @author Chris Lenderman
 */
public class MessageData
{
    /** Whether or not this is a simple string which only contains a message */
    private boolean isSimpleString = false;

    /** The message contents */
    public String message = "";

    /** The message line type */
    public String lineType;

    /** The message date in raw string form */
    public String date;

    /** The message time in raw string form */
    public String time;

    /** The message device name */
    public String device = "";

    /** The message type field, if present (i.e. IN/OUT) */
    public String msgType = "";

    /** The message name */
    public String name = "";

    /** The message number in string form */
    public String number;

    /**
     * Returns the date and time based on "raw date/time" selection
     * 
     * @param PrefUtils
     * @return String
     */
    public String getDateTime(PrefUtils prefUtils)
    {
        if (prefUtils.getBoolean(PrefUtils.PREF_USE_RAW_DATE_TIME))
        {
            return date + " " + time;
        }
        else
        {
            return LogParserUtils.convertToDateTimeString(date, time,
                    "EEE MMM dd, yyyy hh:mm a");
        }
    }

    /**
     * Constructor
     */
    public MessageData(String type, String messageToParse)
    {
        this.lineType = type;
        ArrayList<String> strings = LogParserUtils
                .tokenizeMsgLine(messageToParse);

        if (strings.size() == 1)
        {
            message = strings.get(0);
            isSimpleString = true;
        }
        else
        {
            message = LogParserUtils.getValueForKey(strings,
                    LogParserUtils.MSG_KEY);
            date = LogParserUtils.getValueForKey(strings,
                    LogParserUtils.DATE_KEY);
            time = LogParserUtils.getValueForKey(strings,
                    LogParserUtils.TIME_KEY);
            number = LogParserUtils.getValueForKey(strings,
                    LogParserUtils.NUMBER_KEY);
            name = LogParserUtils.getValueForKey(strings,
                    LogParserUtils.NAME_KEY);

            String line = LogParserUtils.getValueForKey(strings,
                    LogParserUtils.LINE_KEY);

            msgType = LogParserUtils.getValueForKey(strings,
                    LogParserUtils.MTYPE_KEY);

            if (msgType.equals(LogParserUtils.UNKNOWN_VALUE))
            {
                msgType = "";
            }

            if (!line.equals("-") && !line.equals(LogParserUtils.UNKNOWN_VALUE))
            {
                device = line;
            }
        }
    }

    /**
     * Calculates the from/to field using provided alias
     * 
     * @param the alias to use, can be null
     * @return from/to field
     */
    private String getFromTo(String alias)
    {
        String calculatedName = null;
        if (alias == null)
        {
            calculatedName = name;
        }
        else
        {
            calculatedName = alias;
        }

        if ((calculatedName != null) && !calculatedName.equals("-")
                && !calculatedName.equals("UNKNOWN")
                && !calculatedName.equals(LogParserUtils.UNKNOWN_VALUE))
        {
            return calculatedName;
        }
        else if ((number != null) && !number.equals("-")
                && !number.equals(LogParserUtils.UNKNOWN_VALUE))
        {
            return number;
        }

        return "";
    }

    /**
     * Calculates the from/to field using the provided list of aliases
     * 
     * @param the alias list to use, can be null
     * @return from/to field
     */
    public String getFromTo(HashMap<String, String> addressBookLookupArray)
    {
        String alias = null;

        if ((addressBookLookupArray != null) && (number != null))
        {
            for (String key : addressBookLookupArray.keySet())
            {
                if (number.contains(key))
                {
                    alias = addressBookLookupArray.get(key);
                    break;
                }
            }
        }

        return getFromTo(alias);
    }

    /**
     * Encode the message in a custom format
     * 
     * @param PrefUtils
     * @param the alias to use, can be null
     * @return String
     */
    public String encodeCustom(PrefUtils prefUtils, String alias)
    {
        if (isSimpleString)
        {
            return message;
        }
        else
        {
            return NotificationUtils.buildMessageNotificationText(this,
                    prefUtils, getFromTo(alias));
        }
    }

    /**
     * Encode the message in "pretty print" format using the supplied alias list
     * 
     * @param PrefUtils
     * @param boolean whether or not to include the date and time
     * @param the alias list, can be null
     * @return String
     */
    public String encodePrettyPrintWithAliasList(PrefUtils prefUtils,
            boolean includeDateTime, HashMap<String, String> aliasList)
    {
        return encodePrettyPrintLocal(prefUtils, includeDateTime,
                getFromTo(aliasList));
    }

    /**
     * Encode the message in "pretty print" format using the supplied alias
     * 
     * @param PrefUtils
     * @param boolean whether or not to include the date and time
     * @param the alias, can be null
     * @return String
     */
    public String encodePrettyPrintWithAlias(PrefUtils prefUtils,
            boolean includeDateTime, String alias)
    {
        return encodePrettyPrintLocal(prefUtils, includeDateTime,
                getFromTo(alias));
    }

    /**
     * Encode the message in "pretty print" format
     * 
     * @param PrefUtils
     * @param boolean whether or not to include the date and time
     * @param the calculated from/to field
     * @return String
     */
    private String encodePrettyPrintLocal(PrefUtils prefUtils,
            boolean includeDateTime, String fromTo)
    {
        if (isSimpleString)
        {
            return message;
        }
        else
        {
            String dateTime = "";
            if (includeDateTime)
            {
                dateTime = getDateTime(prefUtils) + " ";
            }

            StringBuilder builder = new StringBuilder();

            if (device.length() > 0)
            {
                builder.append("(Device " + device + ") ");
            }

            if (fromTo.length() > 0)
            {
                if (msgType.equals("OUT"))
                {
                    builder.append("To: " + fromTo + " ");
                }
                else
                {
                    builder.append("From: " + fromTo + " ");
                }
            }

            builder.append(dateTime);
            builder.append("- " + message);

            return builder.toString();
        }
    }
}