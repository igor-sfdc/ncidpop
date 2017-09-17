/*******************************************************
 * Data Lookup Utilities
 *******************************************************/
package com.lenderman.ncidpop.utils;

import java.util.ArrayList;
import java.util.HashSet;
import com.lenderman.ncidpop.data.LookupCandidate;
import com.lenderman.ncidpop.data.MessageData;
import com.lenderman.ncidpop.data.ServerData;

/**
 * Utilities for supporting data lookup
 * 
 * @author Chris Lenderman
 */
public class DataLookupUtils
{
    /**
     * Assesses whether or not a String is a number
     * 
     * @param String
     * @return true if a number, false otherwise
     */
    private static boolean isNumber(String string)
    {
        try
        {
            Long.parseLong(string);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * Gets all available lookup candidates
     * 
     * @param ServerData data
     * @return list of lookup candidates
     */
    public static HashSet<LookupCandidate> getAllLookupCandidates(
            ServerData serverData)
    {
        HashSet<LookupCandidate> candidates = new HashSet<LookupCandidate>();

        if (serverData != null)
        {
            if (!serverData.isLogUpdateInProgress
                    && serverData.isServerConnected)
            {
                for (MessageData messageData : serverData.msgLog)
                {
                    if ((messageData.name != null)
                            && (messageData.number != null)
                            && isNumber(messageData.number))
                    {
                        candidates.add(new LookupCandidate(messageData.name,
                                messageData.number));
                    }
                }

                for (ArrayList<String> callData : serverData.callLog)
                {
                    String name = LogParserUtils.getValueForKey(callData,
                            LogParserUtils.NAME_KEY);

                    String number = LogParserUtils.getValueForKey(callData,
                            LogParserUtils.NUMBER_KEY);

                    if ((name != null) && (number != null) && isNumber(number))
                    {
                        candidates.add(new LookupCandidate(name, number));
                    }
                }
            }
        }
        return candidates;
    }
}