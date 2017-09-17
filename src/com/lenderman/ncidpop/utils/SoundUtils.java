/*******************************************************
 * Sound Utilities
 *******************************************************/
package com.lenderman.ncidpop.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;
import com.lenderman.ncidpop.preferences.jfd.NcidPopPreferences;
import com.lenderman.ncidpop.speech.TextToSpeechManager;

/**
 * Sound utilities
 * 
 * @author Chris Lenderman
 */
public class SoundUtils
{
    /** Line info object */
    private static final Line.Info LINE_INFO = new Line.Info(Clip.class);

    /** A single thread for executing sound */
    private static Executor soundExecutor = Executors.newSingleThreadExecutor();

    /** A handle to the "playing" sound clip */
    private static Clip clip = null;

    /** A flag to indicate if a clip is running */
    private static boolean clipIsRunning = false;

    /**
     * Speaks the text string
     * 
     * @param String the text to read
     * @param PrefUtils the PrefUtils instance
     */
    public static void sayIt(final String text, final PrefUtils prefs)
    {
        String selection = prefs
                .getString(PrefUtils.PREF_THIRD_PARTY_NAME_READING_SELECTION);

        if (selection.equals(NcidPopPreferences.NAME_READING_NONE_LABEL))
        {
            return;
        }
        else if (selection
                .equals(NcidPopPreferences.NAME_READING_SYSTEM_DEFAULT_LABEL))
        {
            soundExecutor.execute(new Runnable()
            {
                /** @inheritDoc */
                public void run()
                {
                    if (TextToSpeechManager.isSupported())
                    {
                        TextToSpeechManager.sayIt(text);
                    }
                }
            });
        }
        else if (selection
                .equals(NcidPopPreferences.NAME_READING_THIRD_PARTY_LABEL))
        {
            soundExecutor.execute(new Runnable()
            {
                /** @inheritDoc */
                public void run()
                {
                    try
                    {
                        HashMap<String, String> substitutions = new HashMap<String, String>();
                        substitutions.put("$NAME", text);

                        OsUtils.executeCommandLineCommand(
                                prefs.getString(PrefUtils.PREF_THIRD_PARTY_NAME_READING_STRING),
                                substitutions, true);
                    }

                    catch (final Exception e)
                    {
                        NotificationUtils.enqueueDialog(new Runnable()
                        {
                            public void run()
                            {
                                NotificationUtils.showDialogPopupMessage(
                                        " Couldn't launch the third party name reader: "
                                                + e.toString(), true);
                            }
                        });
                    }
                }
            });
        }
    }

    /**
     * Returns a list of line mixers
     * 
     * @return mixers list
     */
    public static ArrayList<String> getLineMixerNames()
    {
        ArrayList<String> mixers = new ArrayList<String>();
        for (Mixer.Info mixer : AudioSystem.getMixerInfo())
        {
            if (AudioSystem.getMixer(mixer).isLineSupported(LINE_INFO))
            {
                mixers.add(mixer.getName());
            }
        }
        return mixers;
    }

    /**
     * Plays a sound clip
     * 
     * @param String indicates the sound clip
     * @param String system preferences used in helping to play clip
     */
    public static void playClip(final String name, final PrefUtils prefs)
    {
        System.out.println("Attempting to play clip: " + name);

        soundExecutor.execute(new Runnable()
        {
            /** @inheritDoc */
            public void run()
            {
                clipIsRunning = true;

                // If a sound clip is still playing, stop and flush it
                if ((clip != null) && clip.isRunning())
                {
                    clip.stop();
                    clip.flush();
                    clip = null;
                }

                // Grab a line if available
                Line line = null;
                try
                {
                    if (!prefs.getString(PrefUtils.PREF_MIXER_NAME).equals(
                            PrefUtils.DEFAULT_MIXER_NAME))
                    {
                        Mixer.Info[] mixers = AudioSystem.getMixerInfo();

                        for (Mixer.Info element : mixers)
                        {
                            if (AudioSystem.getMixer(element).isLineSupported(
                                    LINE_INFO)
                                    && element
                                            .getName()
                                            .contains(
                                                    prefs.getString(PrefUtils.PREF_MIXER_NAME)))
                            {
                                line = AudioSystem.getMixer(element).getLine(
                                        LINE_INFO);
                                break;
                            }
                        }
                    }

                    if (line == null)
                    {
                        line = AudioSystem.getLine(LINE_INFO);
                    }
                }
                catch (LineUnavailableException e)
                {
                    return;
                }

                // Get an audio input stream
                clip = (Clip) line;
                AudioInputStream ais = null;
                try
                {
                    InputStream stream = new BufferedInputStream(
                            new FileInputStream(name));
                    ais = AudioSystem.getAudioInputStream(stream);
                }
                catch (UnsupportedAudioFileException e)
                {
                    return;
                }
                catch (IOException e)
                {
                    return;
                }

                // Add a line listener to the clip and attempt to open it
                try
                {
                    clip.addLineListener(new LineListener()
                    {
                        public void update(LineEvent event)
                        {

                            if (event.getType() == Type.STOP)
                            {
                                event.getLine().close();
                                clipIsRunning = false;
                            }
                        }
                    });
                    clip.open(ais);

                }
                catch (LineUnavailableException e)
                {
                    return;
                }
                catch (IOException e)
                {
                    return;
                }

                // Start the clip
                clip.start();
                while (clipIsRunning)
                {
                    try
                    {
                        Thread.sleep(300);
                    }
                    catch (InterruptedException e)
                    {
                        break;
                    }
                }
            }
        });
    }
}