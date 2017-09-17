/*******************************************************
 * Number Format Utilities
 *******************************************************/
package com.lenderman.ncidpop.utils;

/**
 * Utilities for formatting client-displayed numbers.
 * 
 * @author Chris Lenderman
 */
public class NumberFormatUtilities
{
    /**
     * Formats a number per the provided format.
     * 
     * @param String the number to format
     * @param String the format to use
     */
    public static String formatNumber(String number, String format)
    {
        if ((format != null) && (number != null))
        {
            if (format.contains("$ALL"))
            {
                return format.replace("$ALL", number);
            }

            boolean numberValid = true;

            // Ensure that the number is a valid numeric-only number
            try
            {
                Long.parseLong(number);
            }
            catch (NumberFormatException ex)
            {
                numberValid = false;
            }

            if (numberValid && (number.length() >= 10))
            {
                String prefix = number.substring(0, number.length() - 10);
                String suffix = new String(number.substring(
                        number.length() - 10, number.length()));

                String resultNumber = new String(format);
                resultNumber = resultNumber.replace("$PRE", prefix);
                resultNumber = resultNumber.replace("$AREA",
                        suffix.substring(0, 3));
                resultNumber = resultNumber.replace("$NPA",
                        suffix.substring(3, 6));
                resultNumber = resultNumber.replace("$NXX",
                        suffix.substring(6, 10));

                return resultNumber;
            }
        }

        return number;
    }
}