/*******************************************************
 * Power Manager Helper
 *******************************************************/
package com.lenderman.ncidpop.power;

import java.util.HashSet;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import com.lenderman.ncidpop.power.osspecific.LinuxPowerData;
import com.lenderman.ncidpop.power.osspecific.MacPowerData;
import com.lenderman.ncidpop.power.osspecific.WindowsPowerData;
import com.lenderman.ncidpop.utils.OsUtils;
import com.lenderman.ncidpop.utils.PrefUtils;

/**
 * A class that manages propagating information about power management.
 * Calculates the current power state and notifies listeners about current power
 * state.
 * 
 * @author Chris Lenderman
 */
public class PowerManagerHelper
{
    /** The power monitor interval in milliseconds */
    private static final int POWER_MONITOR_INTERVAL = 10000;

    /** The PowerDataIF instance */
    private static PowerDataIF powerData = null;

    /** The power thread instance that monitors power state information */
    private static Thread powerThread = null;

    /** A list of listeners interested in power state changes */
    private static HashSet<PowerNotifyIF> powerNotifyListeners = new HashSet<PowerNotifyIF>();

    /** A preferences change listener */
    private static PreferenceChangeListener preflistener = new PreferenceChangeListener()
    {
        /** @inheritDoc */
        public void preferenceChange(PreferenceChangeEvent evt)
        {
            if (evt.getKey().equals(PrefUtils.PREF_ENABLE_LOW_BATTERY_MESSAGE)
                    || evt.getKey().equals(
                            PrefUtils.PREF_ENABLE_BATTERY_CHARGED_MESSAGE))
            {
                // We should monitor power if either low battery or fully
                // charged notifications are enabled
                boolean monitorPower = PrefUtils.instance
                        .getBoolean(PrefUtils.PREF_ENABLE_LOW_BATTERY_MESSAGE)
                        || PrefUtils.instance
                                .getBoolean(PrefUtils.PREF_ENABLE_BATTERY_CHARGED_MESSAGE);

                if (monitorPower && (powerThread == null))
                {
                    startPowerMonitoring();
                }

                if (!monitorPower && (powerThread != null))
                {
                    stopPowerMonitoring();
                }
            }
        }
    };

    /** Static init block */
    static
    {
        initialize();
    }

    /**
     * Adds a power manager listener and starts the monitor thread if needed
     * 
     * @param PowerNotifyIF listener to add
     */
    public static synchronized void addListener(PowerNotifyIF listener)
    {
        powerNotifyListeners.add(listener);

        if (powerNotifyListeners.size() == 1)
        {
            startPowerMonitoring();
        }
    }

    /**
     * Removes a power manager listener and stops the monitor thread if needed
     * 
     * @param PowerNotifyIF listener to remove
     */
    public static synchronized void removeListener(PowerNotifyIF listener)
    {
        powerNotifyListeners.remove(listener);

        if (powerNotifyListeners.size() == 0)
        {
            stopPowerMonitoring();
        }
    }

    /**
     * Returns true if battery power is supported on this device, false
     * otherwise
     * 
     * @return boolean
     */
    public static boolean isBatteryPowerSupported()
    {
        powerData.refreshPowerInfo();
        return (powerData != null) && powerData.isBatterySupported();
    }

    /**
     * Starts the power monitoring thread
     */
    private static void startPowerMonitoring()
    {
        powerData.refreshPowerInfo();

        // Do not start the thread if power management is not supported or there
        // are no listeners
        if ((powerData == null) || !powerData.isBatterySupported()
                || (powerNotifyListeners.size() == 0))
        {
            return;
        }

        powerThread = new Thread(new Runnable()
        {
            boolean lastAcPowerConnected = powerData.isAcConnected();
            int lastLowBatteryThreshold = PrefUtils.instance
                    .getInteger(PrefUtils.PREF_LOW_BATTERY_MESSAGE_THRESHOLD);

            boolean lowBatteryWarned = false;
            boolean chargedNotified = lastAcPowerConnected;

            /** @inheritDoc */
            public void run()
            {
                while (true)
                {
                    try
                    {
                        int currentBatteryPercent = powerData
                                .getBatteryLifePercent();

                        boolean acPowerConnected = powerData.isAcConnected();

                        boolean monitorLowPower = PrefUtils.instance
                                .getBoolean(PrefUtils.PREF_ENABLE_LOW_BATTERY_MESSAGE);

                        boolean monitorCharged = PrefUtils.instance
                                .getBoolean(PrefUtils.PREF_ENABLE_BATTERY_CHARGED_MESSAGE);

                        int lowBatteryThreshold = PrefUtils.instance
                                .getInteger(PrefUtils.PREF_LOW_BATTERY_MESSAGE_THRESHOLD);

                        // If we have transitioned to AC power or if the low
                        // battery threshold changes, reset the low battery
                        // warning flag
                        if ((!lastAcPowerConnected && acPowerConnected)
                                || (lowBatteryThreshold != lastLowBatteryThreshold))
                        {
                            lowBatteryWarned = false;
                        }

                        // If we have transitioned to from AC power reset the
                        // charge notified flag
                        if (lastAcPowerConnected && !acPowerConnected)
                        {
                            chargedNotified = false;
                        }

                        // If we have hit the threshold and have not notified
                        // our listeners, call back listeners with low battery
                        // state
                        if (!acPowerConnected
                                && (currentBatteryPercent <= lowBatteryThreshold)
                                && !lowBatteryWarned)
                        {
                            lowBatteryWarned = true;

                            if (monitorLowPower)
                            {
                                for (PowerNotifyIF listener : powerNotifyListeners)
                                {
                                    listener.onBatteryLow(currentBatteryPercent);
                                }
                            }
                        }

                        // If we have charged the battery and have not notified
                        // our listeners, call back listeners with charged
                        // battery state
                        if (acPowerConnected && !chargedNotified
                                && (currentBatteryPercent == 100))
                        {
                            chargedNotified = true;

                            if (monitorCharged)
                            {
                                for (PowerNotifyIF listener : powerNotifyListeners)
                                {
                                    listener.onBatteryCharged();
                                }
                            }
                        }

                        lastAcPowerConnected = acPowerConnected;
                        lastLowBatteryThreshold = lowBatteryThreshold;

                        Thread.sleep(POWER_MONITOR_INTERVAL);
                        powerData.refreshPowerInfo();

                    }
                    catch (InterruptedException ex)
                    {
                        // Do nothing
                    }
                }
            }
        });

        powerThread.start();

        Preferences.userNodeForPackage(PrefUtils.class)
                .addPreferenceChangeListener(preflistener);
    }

    /**
     * Stops the power monitoring thread
     */
    private static void stopPowerMonitoring()
    {
        if (powerThread != null)
        {
            powerThread.interrupt();
            powerThread = null;

            Preferences.userNodeForPackage(PrefUtils.class)
                    .removePreferenceChangeListener(preflistener);
        }
    }

    /**
     * Initializes the class
     */
    private static void initialize()
    {
        if (OsUtils.isWindowsOS())
        {
            powerData = new WindowsPowerData();
        }
        else if (OsUtils.isMacOS())
        {
            powerData = new MacPowerData();
        }
        else
        {
            powerData = new LinuxPowerData();
        }
    }
}