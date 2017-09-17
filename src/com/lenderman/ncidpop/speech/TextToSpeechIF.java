/*******************************************************
 * Text-To-Speech Interface Definition
 *******************************************************/
package com.lenderman.ncidpop.speech;

/**
 * The interface that each interested OS should implement
 * 
 * @author Chris Lenderman
 */
public interface TextToSpeechIF
{
    /**
     * Called to initialize
     * 
     * @throws exception
     */
    public void initialize() throws Exception;

    /**
     * Speaks the text string
     * 
     * @param String the text to read
     */
    public void sayIt(String text);
}