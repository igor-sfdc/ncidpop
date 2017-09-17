/*******************************************************
 * Log Date/Time Data
 *******************************************************/
package com.lenderman.ncidpop.data;

import com.lenderman.ncidpop.utils.LogParserUtils;

/**
 * Log date/time data, used to encapsulate log date and time and for table
 * comparisons.
 * 
 * @author Chris Lenderman
 */
public class LogDateTimeData implements Comparable<LogDateTimeData>
{
    /** The date */
    public String date;

    /** The time */
    public String time;

    /**
     * The calculated value in millis. Only set if requested to be set in
     * constructor.
     */
    public Long valueInMillis;

    /**
     * Constructor
     */
    public LogDateTimeData(String date, String time,
            boolean calculateMillisValue)
    {
        this.date = date;
        this.time = time;

        if (calculateMillisValue)
        {
            valueInMillis = LogParserUtils.convertLogDateAndTimeToMillis(date,
                    time);
        }
    }

    /** @inheritDoc */
    public int compareTo(LogDateTimeData dateTime)
    {
        // Always try and match on millis first; otherwise, do a more "raw"
        // comparison
        if ((valueInMillis != null) && (dateTime.valueInMillis != null))
        {
            return valueInMillis.compareTo(dateTime.valueInMillis);
        }
        else
        {
            return ((date + " " + time).compareTo(dateTime.date + " "
                    + dateTime.time));
        }
    }
}