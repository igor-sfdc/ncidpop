/*******************************************************
 * Microsoft Windows Text-To-Speech
 *******************************************************/
package com.lenderman.ncidpop.speech;

import java.math.BigDecimal;
import speech.types.ClassFactory;
import speech.types.ISpeechVoice;
import speech.types.SpeechVoiceSpeakFlags;

/**
 * The Windows text-to-speech interface. Utilizes COM for text-to-speech.
 * 
 * @author Chris Lenderman
 */
public class WindowsTextToSpeech implements TextToSpeechIF
{
    /** The speech voice instance */
    private ISpeechVoice voice = null;

    /** @inheritDoc */
    public void initialize() throws Exception
    {
        BigDecimal version = new BigDecimal(
                System.getProperty("java.specification.version"));

        if (version.compareTo(new BigDecimal("1.5")) > 0)
        {
            voice = ClassFactory.createSpVoice();
        }
        else
        {
            throw new Exception("Not supported on this version of Java");
        }
    }

    /** @inheritDoc */
    public void sayIt(String text)
    {
        if (voice != null)
        {
            voice.speak(text, SpeechVoiceSpeakFlags.SVSFDefault);
        }
    }
}