/*******************************************************
 * Text-To-Speech Manager
 *******************************************************/
package com.lenderman.ncidpop.speech;

import com.lenderman.ncidpop.utils.OsUtils;

/**
 * The text-to-speech manager for determining compatibility with various OSes
 * 
 * @author Chris Lenderman
 */
public class TextToSpeechManager
{
    /** Text-to-speech instance */
    private static TextToSpeechIF textToSpeechInstance = null;

    /** Static init block */
    static
    {
        if (OsUtils.isWindowsOS())
        {
            textToSpeechInstance = new WindowsTextToSpeech();
        }

        if (textToSpeechInstance != null)
        {
            try
            {
                textToSpeechInstance.initialize();
            }
            catch (Exception e)
            {
                textToSpeechInstance = null;
            }
        }
    }

    /**
     * Determines if text-to-speech is supported on this computer
     * 
     * @return boolean true if supported
     */
    public static boolean isSupported()
    {
        return textToSpeechInstance != null;
    }

    /**
     * Speaks the text string
     * 
     * @param String the text to read
     */
    public static void sayIt(String text)
    {
        if (textToSpeechInstance != null)
        {
            textToSpeechInstance.sayIt(text);
        }
    }
}