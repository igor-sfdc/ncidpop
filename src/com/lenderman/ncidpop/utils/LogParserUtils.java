/*******************************************************
 * Log Parse Utilities
 *******************************************************/
package com.lenderman.ncidpop.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/**
 * Utilities that support log parsing.
 * 
 * @author Chris Lenderman
 */
public class LogParserUtils
{
    /** Class logger */
    private static Logger log = Logger.getLogger(LogParserUtils.class);

    /** Key for finding line type in the raw data */
    public static final String TYPE_KEY = "TYPE";

    /** Key for finding MTYPE in the raw data */
    public static final String MTYPE_KEY = "MTYPE";

    /** Key for finding MSG in the raw data */
    public static final String MSG_KEY = "MSG";

    /** Key for finding NAME in the raw data */
    public static final String NAME_KEY = "NAME";

    /** Key for finding RING in the raw data */
    public static final String RING_KEY = "RING";

    /** Key for finding NMBR in the raw data */
    public static final String NUMBER_KEY = "NMBR";

    /** Key for finding DATE in the raw data */
    public static final String DATE_KEY = "DATE";

    /** Key for finding TIME in the raw data */
    public static final String TIME_KEY = "TIME";

    /** Key for finding LINE in the raw data */
    public static final String LINE_KEY = "LINE";

    /** A constant for representing unknown values */
    public static final String UNKNOWN_VALUE = "*****";

    /** Constant string time format */
    public static String TIME_FORMAT = "hh:mm a";

    /** A constant for start of message line metadata delimeter */
    private static String MESSAGE_START_METADATA_DELIM = "***";

    /**
     * Determines the preferred date format
     * 
     * @return String
     */
    public static String getPreferredDateFormat()
    {
        if (PrefUtils.instance
                .getBoolean(PrefUtils.PREF_USE_EUROPEAN_DATE_TIME))
        {
            return "dd-MM-yyyy";
        }
        else
        {
            return "MM-dd-yyyy";
        }
    }

    /**
     * Determines the preferred date/time format
     * 
     * @return String
     */
    public static String getPreferredDateTimeFormat()
    {
        return getPreferredDateFormat() + " " + TIME_FORMAT;
    }

    /**
     * Determines the proper date/time parser format
     * 
     * @return String
     */
    private static String getDateTimeParserFormat()
    {
        if (PrefUtils.instance
                .getBoolean(PrefUtils.PREF_USE_EUROPEAN_DATE_TIME))
        {
            return "ddMMyyyy HHmm";
        }
        else
        {
            return "MMddyyyy HHmm";
        }
    }

    /**
     * Given a raw string, convert that string to a time in millis
     * 
     * @param String the date
     * @param String the time
     * @return long
     */
    public static long convertLogDateAndTimeToMillis(String date, String time)
    {
        // Define the "source" date format.
        SimpleDateFormat sdf = new SimpleDateFormat(getDateTimeParserFormat());

        // Define the date object
        Date d = null;

        // Define the return object
        long result = 0;

        try
        {
            // Create our "source" date format and create a date object out of
            // it
            String concat = date + " " + time;
            d = sdf.parse(concat);
        }
        catch (ParseException e)
        {
            log.error("Could not parse date format: ", e);
        }

        if (d != null)
        {
            result = d.getTime();
        }
        return result;
    }

    /**
     * Given a raw string, extract and format line string
     * 
     * @param String the line string
     * @return String
     */
    public static String formatLineString(String lineString)
    {
        return lineString.equals("-") ? "" : lineString;
    }

    /**
     * Given a raw string, convert that string to a date/time string
     * 
     * @param String the date string
     * @param String the time string
     * @param String the desired string format
     * @return String
     */
    public static String convertToDateTimeString(String date, String time,
            String format)
    {
        long value = convertLogDateAndTimeToMillis(date, time);
        return convertToDateTimeString(value, format);
    }

    /**
     * Given a time in millis, convert that string to a date/time string
     * 
     * @param long the time in millis
     * @param String the desired string format
     * @return String (unknown if the seconds since epoch is zero)
     */
    public static String convertToDateTimeString(long secondsSinceEpoch,
            String format)
    {
        // Define the date object
        Date d = new Date(secondsSinceEpoch);
        String result = "UNKNOWN";

        if ((d != null) && (secondsSinceEpoch != 0))
        {
            SimpleDateFormat theDate = new SimpleDateFormat(format);
            result = theDate.format(d);
        }
        return result;
    }

    /**
     * Given a key and an array of parsed strings, find the value of interest
     * 
     * @param ArrayList <String> the array of parsed strings from the NCID
     *        server
     * @param String the key of interest
     * @return String
     */
    public static String getValueForKey(ArrayList<String> strings, String key)
    {
        if (key.equals(LogParserUtils.TYPE_KEY)
                || key.equals(LogParserUtils.MSG_KEY))
        {
            return strings.get(0);
        }

        int index = strings.indexOf(key);
        if ((index != -1) && ((index + 1) < strings.size()))
        {
            return strings.get(index + 1);
        }
        return UNKNOWN_VALUE;
    }

    /**
     * Creates an array of parsed strings from an NCID server message line
     * 
     * @param ArrayList <String> the NCID server message string
     * @return ArrayList <String> the list of strings generate
     */
    public static ArrayList<String> tokenizeMsgLine(String str)
    {
        ArrayList<String> strings = new ArrayList<String>();

        if (!str.contains(MESSAGE_START_METADATA_DELIM))
        {
            strings.add(str);
        }
        else
        {
            strings.add(str.substring(0,
                    str.lastIndexOf(MESSAGE_START_METADATA_DELIM)));

            StringTokenizer tk = new StringTokenizer(str.substring(
                    str.lastIndexOf(MESSAGE_START_METADATA_DELIM)
                            + MESSAGE_START_METADATA_DELIM.length(),
                    str.length()), "*");

            while ((tk.hasMoreElements()))
            {
                strings.add(tk.nextToken());
            }
        }
        return strings;
    }

    /**
     * Creates an array of parsed strings from an NCID server call line
     * 
     * @param ArrayList <String> the NCID server call string
     * @return ArrayList <String> the list of strings generate
     */
    public static ArrayList<String> tokenizeCallLine(String str)
    {
        ArrayList<String> strings = new ArrayList<String>();

        // Add the first three characters of the TYPE to the array
        strings.add(str.substring(0, 3));

        StringTokenizer tk = new StringTokenizer(str.substring(
                str.indexOf(":") + 2, str.length()), "*");

        while ((tk.hasMoreElements()))
        {
            strings.add(tk.nextToken());
        }

        return strings;
    }
}