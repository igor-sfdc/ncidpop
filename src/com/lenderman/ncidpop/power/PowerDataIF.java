/*******************************************************
 * Power Data Interface
 *******************************************************/
package com.lenderman.ncidpop.power;

/**
 * An interface used to retrieve power information. Typically, implementors of
 * this interface will be OS-specific implementations of power management
 * control.
 * 
 * @author Chris Lenderman
 */
public interface PowerDataIF
{
    /**
     * Call this method to "refresh" the power data owned by the implementor of
     * this class.
     * 
     * For example, implementors may need to re-read a file, call an external
     * executable, and/or make an OS-level function call to get the data needed
     * for power management.
     */
    public void refreshPowerInfo();

    /**
     * Returns true if battery supports exists (i.e. the computer has a battery
     * in it), false otherwise
     * 
     * @return boolean
     */
    public boolean isBatterySupported();

    /**
     * Returns true if AC power is currently connected, false otherwise
     * 
     * @return boolean
     */
    public boolean isAcConnected();

    /**
     * Returns the battery life percent ranging from 0% to 100%
     * 
     * @return an integer representation of battery life percent
     */
    public int getBatteryLifePercent();
}