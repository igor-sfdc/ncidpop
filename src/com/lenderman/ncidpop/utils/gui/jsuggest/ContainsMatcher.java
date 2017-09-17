/*******************************************************
 * Contains Matcher
 *******************************************************/
package com.lenderman.ncidpop.utils.gui.jsuggest;

/**
 * Template class to support assessing the matching of a phrase.
 * 
 * @author Chris Lenderman
 */
public class ContainsMatcher<C>
{
    /**
     * Assesses match based on a provided data object and search word
     * 
     * @param C the object for which matching shall be assessed
     * @param String the search word to match
     * @param boolean whether or not to ignore case in the matching
     */
    public boolean matches(C dataWord, String searchWord, boolean ignoreCase)
    {
        if (ignoreCase)
        {
            return dataWord.toString().toLowerCase()
                    .contains(searchWord.toLowerCase());
        }
        else
        {
            return dataWord.toString().contains(searchWord);
        }
    }
}