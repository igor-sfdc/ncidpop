/*******************************************************
 * Windows Power Data
 *******************************************************/
package com.lenderman.ncidpop.power.osspecific;

import java.util.ArrayList;
import java.util.List;
import com.lenderman.ncidpop.power.PowerDataIF;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

/**
 * Access methods for Windows Power Data
 * 
 * @author Chris Lenderman
 */
public class WindowsPowerData implements PowerDataIF
{
    /** Kernel32 SYSTEM_POWER_STATUS instance for retrieving battery status */
    private static Kernel32.SYSTEM_POWER_STATUS batteryStatus = null;

    /** Static init block */
    static
    {
        try
        {
            batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();
        }
        catch (Exception ex)
        {
            // Do nothing. Unsupported in this version of java.
        }
    }

    /** @inheritDoc */
    public void refreshPowerInfo()
    {
        if (batteryStatus != null)
        {
            Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);
        }
    }

    /** @inheritDoc */
    public boolean isBatterySupported()
    {
        return (batteryStatus != null) && batteryStatus.isBatteryPresent();
    }

    /** @inheritDoc */
    public boolean isAcConnected()
    {
        return (batteryStatus != null) && batteryStatus.isAcConnected();
    }

    /** @inheritDoc */
    public int getBatteryLifePercent()
    {
        if ((batteryStatus != null) && batteryStatus.isBatteryPercentValid())
        {
            return batteryStatus.getBatteryLifePercent();
        }
        else
        {
            return -1;
        }
    }

    /**
     * An interface class for tapping into the Kernel32 Windows library. We use
     * JNA libraries to access this library.
     */
    public interface Kernel32 extends StdCallLibrary
    {
        /** Kernel32 instance */
        public Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("Kernel32",
                Kernel32.class);

        /**
         * @see http://msdn2.microsoft.com/en-us/library/aa373232.aspx
         */
        public class SYSTEM_POWER_STATUS extends Structure
        {
            public byte ACLineStatus;
            public byte BatteryFlag;
            public byte BatteryLifePercent;
            public byte Reserved1;
            public int BatteryLifeTime;
            public int BatteryFullLifeTime;

            /** @inheritDoc */
            @Override
            protected List<String> getFieldOrder()
            {
                ArrayList<String> fields = new ArrayList<String>();
                fields.add("ACLineStatus");
                fields.add("BatteryFlag");
                fields.add("BatteryLifePercent");
                fields.add("Reserved1");
                fields.add("BatteryLifeTime");
                fields.add("BatteryFullLifeTime");
                return fields;
            }

            /**
             * Returns true if AC power is connected, false otherwise
             * 
             * @return boolean
             */
            public boolean isAcConnected()
            {
                return ACLineStatus == 1;
            }

            /**
             * Returns true if battery is present, false otherwise
             * 
             * @return boolean
             */
            public boolean isBatteryPresent()
            {
                return (BatteryFlag & (byte) 128) == 0;
            }

            /**
             * Returns true if battery percent is valid, false otherwise
             * 
             * @return boolean
             */
            public boolean isBatteryPercentValid()
            {
                return BatteryLifePercent != (byte) 255;
            }

            /**
             * Returns the battery life percent
             * 
             * @return integer
             */
            public int getBatteryLifePercent()
            {
                return BatteryLifePercent;
            }
        }

        /**
         * This is the function prototype for the call that will be made to the
         * kernel32.dll. The call will fill in the passed in SYSTEM_POWER_STATUS
         * 
         * @param SYSTEM_POWER_STATUS instance that will be set within the
         *        method call
         */
        public int GetSystemPowerStatus(SYSTEM_POWER_STATUS result);
    }
}