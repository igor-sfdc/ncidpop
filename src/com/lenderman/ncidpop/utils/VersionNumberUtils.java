/*******************************************************
 * Version Number Utils
 *******************************************************/
package com.lenderman.ncidpop.utils;

/**
 * Utilities to support version number assessment
 * 
 * @author Chris Lenderman
 */
public class VersionNumberUtils
{
    /**
     * Checks to see if there is a version number difference
     * 
     * @param String first version number to check
     * @param String second version number to check
     * @return nonzero if there is a difference
     */
    public static int versionNumberDifference(String str1, String str2)
    {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        while ((i < vals1.length) && (i < vals2.length)
                && vals1[i].equals(vals2[i]))
        {
            i++;
        }

        if ((i < vals1.length) && (i < vals2.length))
        {
            int diff = Integer.valueOf(vals1[i]).compareTo(
                    Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }

        return Integer.signum(vals1.length - vals2.length);
    }
}