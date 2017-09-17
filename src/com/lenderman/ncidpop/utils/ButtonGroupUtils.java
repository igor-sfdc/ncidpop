/*******************************************************
 * Button Group Utilities
 *******************************************************/
package com.lenderman.ncidpop.utils;

import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

/**
 * Utilities that support button group functions.
 * 
 * @author Chris Lenderman
 */
public class ButtonGroupUtils
{
    /**
     * Gets the text label for the selected button in the button group
     * 
     * @param ButtonGroup
     * @return String the text label
     */
    public static String getSelectedButtonText(ButtonGroup buttonGroup)
    {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons
                .hasMoreElements();)
        {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected())
            {
                return button.getText();
            }
        }
        return null;
    }

    /**
     * Given a button group and label, find the button
     * 
     * @param ButtonGroup
     * @param String the label
     * @return AbstractButton the found button
     */
    private static AbstractButton findButton(ButtonGroup group, String label)
    {
        if (label != null)
        {
            for (Enumeration<AbstractButton> buttons = group.getElements(); buttons
                    .hasMoreElements();)
            {
                AbstractButton button = buttons.nextElement();

                if (button.getText().equals(label))
                {
                    return button;
                }
            }
        }
        return null;
    }

    /**
     * Sets the given button with the specified label to be selected. If that
     * button is not enabled, then set the button with the default label to
     * selected.
     * 
     * @param ButtonGroup
     * @param String the label for the button to select
     * @param String the default label for the button to select if the "true"
     *        button is not enabled
     */
    public static void setButtonSelectedWithLabel(ButtonGroup group,
            String label, String defaultLabel)
    {
        AbstractButton button = findButton(group, label);
        AbstractButton defaultButton = findButton(group, defaultLabel);

        if (button != null)
        {
            if (button.isEnabled())
            {
                button.setSelected(true);
            }
            else if ((defaultButton != null) && defaultButton.isEnabled())
            {
                defaultButton.setSelected(true);
            }
        }
    }
}