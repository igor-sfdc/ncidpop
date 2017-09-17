/*******************************************************
 * Linux Power Data
 *******************************************************/
package com.lenderman.ncidpop.power.osspecific;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import com.lenderman.ncidpop.power.PowerDataIF;
import com.lenderman.ncidpop.utils.FileUtils;

/**
 * Access methods for Linux Power Data
 * 
 * @author Chris Lenderman
 */
public class LinuxPowerData implements PowerDataIF
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
        boolean success = readFromProc();

        if (!success)
        {
            readFromSys();
        }
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
     * Parses proc data
     * 
     * @param list of proc strings
     * @return HashMap with proc keys and values
     */
    private HashMap<String, ArrayList<String>> parseProcData(
            ArrayList<String> data)
    {
        HashMap<String, ArrayList<String>> parsed = new HashMap<String, ArrayList<String>>();
        for (String string : data)
        {
            ArrayList<String> tokens = new ArrayList<String>();
            StringTokenizer tokenizer = new StringTokenizer(string, ":");

            String index = null;
            index = tokenizer.hasMoreElements() ? tokenizer.nextToken() : null;
            tokenizer.nextToken(" ");

            if (index == null)
            {
                continue;
            }

            while (tokenizer.hasMoreElements())
            {
                tokens.add(tokenizer.nextToken(" "));
            }

            if (tokens.size() > 0)
            {
                parsed.put(index, tokens);
            }
        }
        return parsed;
    }

    /**
     * Reads proc data and parses power state data
     * 
     * @return boolean true if successful, false otherwise
     */
    private boolean readFromProc()
    {
        File fileState = new File("/proc/acpi/battery/BAT0/state");
        File fileInfo = new File("/proc/acpi/battery/BAT0/info");

        float remainingCapacity = 0;
        float lastFullCapacity = 0;

        if (fileState.exists() && fileInfo.exists())
        {
            ArrayList<File> list = new ArrayList<File>();
            list.add(fileState);
            list.add(fileInfo);

            HashMap<String, ArrayList<String>> parsed = parseProcData(FileUtils
                    .readFilesIntoArray(list));

            try
            {
                batterySupported = parsed.get("present").get(0).equals("yes");
                acConnected = parsed.get("charging state").get(0)
                        .equals("charging");
                remainingCapacity = Integer.parseInt(parsed.get(
                        "remaining capacity").get(0));
                lastFullCapacity = Integer.parseInt(parsed.get(
                        "last full capacity").get(0));
                batteryLifePercent = (int) ((remainingCapacity / lastFullCapacity) * 100.0f);
                return true;
            }
            catch (Exception ex)
            {
                // Do nothing
            }
        }
        return false;
    }

    /**
     * Reads sys data and parses power state data
     * 
     * @return boolean true if successful, false otherwise
     */
    private boolean readFromSys()
    {
        try
        {
            File batteryDirectory = new File("/sys/class/power_supply/BAT0");
            if (!batteryDirectory.exists())
            {
                batteryDirectory = new File("/sys/class/power_supply/BAT1");
            }
            batterySupported = batteryDirectory.exists();

            acConnected = FileUtils
                    .readFileIntoArray(
                            new File("/sys/class/power_supply/AC/online"))
                    .get(0).equals("1");

            float remainingCapacity = Integer
                    .parseInt(FileUtils
                            .readFileIntoArray(
                                    new File(batteryDirectory.getPath()
                                            + "/energy_now")).get(0));

            float lastFullCapacity = Integer.parseInt(FileUtils
                    .readFileIntoArray(
                            new File(batteryDirectory.getPath()
                                    + "/energy_full")).get(0));

            batteryLifePercent = (int) ((remainingCapacity / lastFullCapacity) * 100.0f);

            return true;
        }
        catch (Exception ex)
        {
            // Do nothing
        }
        return false;
    }
}