/*******************************************************
 * Server Capability Assessment Utils
 *******************************************************/
package com.lenderman.ncidpop.utils;

import java.util.HashSet;

/**
 * A common utilities class used to assess whether the server instance possesses
 * a certain capability.
 * 
 * @author Chris Lenderman
 */
public class ServerCapabilityAssessmentUtils
{
    /**
     * Server capability enum
     */
    public enum ServerCapability
    {
        BASELINE_JOB_SUPPORT(null), HANGUP_OPTION(null);

        /** The dependency associated with this capability. For now, singular. */
        private ServerCapability dependency;

        /**
         * Constructor
         */
        private ServerCapability(ServerCapability dependency)
        {
            this.dependency = dependency;
        }
    }

    /**
     * Returns true if this capability and all dependent capabilities are
     * supported
     * 
     * @param ServerCapability
     * @param HashSet<ServerCapability>
     * @return boolean
     */
    public static boolean isCapabilitySupported(ServerCapability capability,
            HashSet<ServerCapability> supportedCapability)
    {
        if (capability == null)
        {
            return false;
        }
        else if ((capability.dependency != null)
                && !isCapabilitySupported(capability.dependency,
                        supportedCapability))
        {
            return false;
        }
        return supportedCapability.contains(capability);
    }

    /**
     * Adds the hangup option supported to the list of capabilities
     * 
     * @param HashSet<ServerCapability>
     */
    public static void setHangupOptionSupported(
            HashSet<ServerCapability> supportedCapability)
    {
        supportedCapability.add(ServerCapability.HANGUP_OPTION);
    }

    /**
     * Resets the list of capabilities to default settings
     * 
     * @param HashSet<ServerCapability>
     */
    public static void setDefaultCapabilities(
            HashSet<ServerCapability> supportedCapability)
    {
        supportedCapability.clear();
        supportedCapability.add(ServerCapability.BASELINE_JOB_SUPPORT);
    }

    /**
     * Removes job support from the list of capabilities
     * 
     * @param HashSet<ServerCapability>
     */
    public static void clearJobSupport(
            HashSet<ServerCapability> supportedCapability)
    {
        supportedCapability.remove(ServerCapability.BASELINE_JOB_SUPPORT);
    }
}