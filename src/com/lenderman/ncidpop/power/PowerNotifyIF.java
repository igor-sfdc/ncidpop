/*******************************************************
 * Power Notify Interface
 *******************************************************/
package com.lenderman.ncidpop.power;

/**
 * An interface used to notify listeners of changes in power state
 * 
 * @author Chris Lenderman
 */
public interface PowerNotifyIF
{
    /**
     * Called back when the battery is fully charged
     */
    public void onBatteryCharged();

    /**
     * Called back when the battery is low
     * 
     * @param integer current battery level percentage
     */
    public void onBatteryLow(int percentage);
}