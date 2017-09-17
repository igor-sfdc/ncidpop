/*******************************************************
 * Server API Utils
 *******************************************************/
package com.lenderman.ncidpop.utils;

import java.util.HashSet;
import com.lenderman.ncidpop.data.ServerData;

/**
 * The NCID Server API utilities, used to translate which capabilities are
 * supported in which API.
 * 
 * @author Chris Lenderman
 */
public class ServerApiUtils
{
    /** Feature set value for job support */
    private static int FEATURE_SET_JOB_SUPPORT = 3;

    /** API Version for baseline job support */
    private static String BASELINE_JOB_API_VERSION = "1.0";

    /**
     * API Version for server hangup support (which doesn't require OPT: hangup)
     */
    private static String SERVER_HANGUP_SUPPORT_API_VERSION = "1.3";

    /**
     * Process the server API version
     * 
     * @param the name
     * @param ServerData
     */
    public static void processServerApiVersion(String version,
            ServerData serverData)
    {
        HashSet<Integer> featureSetSupported = new HashSet<Integer>();

        String[] tokens = version.split(" ");

        if (tokens != null)
        {
            for (int index = 0; index < tokens.length; index++)
            {
                if (tokens[index].equals("API:"))
                {
                    if (tokens.length >= (index + 1))
                    {
                        serverData.apiVersion = tokens[index + 1];
                    }
                    index++;
                }
                if (tokens[index].equals("Feature")
                        && (tokens.length >= (index + 1))
                        && tokens[index + 1].equals("Set"))
                {
                    index += 2;

                    while (index < tokens.length)
                    {
                        try
                        {
                            featureSetSupported.add(Integer
                                    .parseInt(tokens[index]));
                        }
                        catch (NumberFormatException ex)
                        {
                            // This token isn't an feature set number, keep
                            // going
                        }
                        index++;
                    }
                }
            }
        }

        if ((VersionNumberUtils.versionNumberDifference(serverData.apiVersion,
                BASELINE_JOB_API_VERSION) < 0)
                || !featureSetSupported.contains(FEATURE_SET_JOB_SUPPORT))
        {
            ServerCapabilityAssessmentUtils
                    .clearJobSupport(serverData.supportedCapability);
        }

        // NOTE: NCID servers with API SERVER_HANGUP_SUPPORT_API_VERSION and
        // above support hangup without having to send OPT: hangup
        if ((VersionNumberUtils.versionNumberDifference(serverData.apiVersion,
                SERVER_HANGUP_SUPPORT_API_VERSION) >= 0)
                && featureSetSupported.contains(FEATURE_SET_JOB_SUPPORT))
        {
            ServerCapabilityAssessmentUtils
                    .setHangupOptionSupported(serverData.supportedCapability);
        }
    }
}