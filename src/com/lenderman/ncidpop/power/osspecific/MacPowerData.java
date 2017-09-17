/*******************************************************
 * Macintosh Power Data
 *******************************************************/
package com.lenderman.ncidpop.power.osspecific;

import java.util.ArrayList;
import java.util.StringTokenizer;
import com.lenderman.ncidpop.power.PowerDataIF;
import com.lenderman.ncidpop.utils.OsUtils;

/**
 * Access methods for Macintosh Power Data
 * 
 * @author Chris Lenderman
 */
public class MacPowerData implements PowerDataIF
{
    /** An indication as to whether batteries are supported */
    private boolean batterySupported = false;

    /** An indication of whether AC power is connected */
    private boolean acConnected = false;

    /** An indication of the battery life percentage */
    private int batteryLifePercent = 0;

    /** @inheritDoc */
    public void refreshPowerInfo()
    {
        parsePmset();
    }

    /** @inheritDoc */
    public boolean isBatterySupported()
    {
        return batterySupported;
    }

    /** @inheritDoc */
    public boolean isAcConnected()
    {
        return acConnected;
    }

    /** @inheritDoc */
    public int getBatteryLifePercent()
    {
        return batteryLifePercent;
    }

    /**
     * Execute and parse the pmset command for power info
     * 
     * @return true if successful, false otherwise
     */
    private boolean parsePmset()
    {
        try
        {
            ArrayList<String> output = OsUtils.executeCommandLineCommand(
                    "pmset -g batt", null, true);

            if (output.size() >= 2)
            {
                acConnected = output.get(0).contains("AC Power");
                batterySupported = output.get(1).contains("Battery");

                StringTokenizer tokenizer = new StringTokenizer(output.get(1),
                        "\t");

                while (tokenizer.hasMoreElements())
                {
                    String token = tokenizer.nextToken();
                    if (token.contains("%"))
                    {
                        batteryLifePercent = Integer.parseInt(token.substring(
                                0, token.indexOf('%')));
                        return true;
                    }
                }
            }
        }
        catch (Exception e)
        {
            // Do nothing
        }
        return false;
    }
}