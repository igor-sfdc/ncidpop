/*******************************************************
 * Call Display Preferences Map Data
 *******************************************************/
package com.lenderman.ncidpop.data;

/**
 * A simple class that allows for mapping call display preferences to a call
 * data display key and associated delimeter. Utilizing this class allows for
 * programmatic call data display formation.
 * 
 * @author Chris Lenderman
 */
public class CallDisplayPrefsMapData
{
    /** The preference type for this particular call display line */
    public String prefType;

    /**
     * The data display key to specify which data to use for this call display
     * preference
     */
    public String dataDisplayKey;

    /** The data delimeter associated with displaying this call display data */
    public String displayDelim;

    /**
     * Constructor
     */
    public CallDisplayPrefsMapData(String prefType, String dataDisplayKey,
            String displayDelim)
    {
        this.prefType = prefType;
        this.dataDisplayKey = dataDisplayKey;
        this.displayDelim = displayDelim;
    }
}