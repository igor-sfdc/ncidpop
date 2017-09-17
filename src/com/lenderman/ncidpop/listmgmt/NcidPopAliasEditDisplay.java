/*******************************************************
 * NCID Pop Alias Edit Display
 *******************************************************/
package com.lenderman.ncidpop.listmgmt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.lenderman.ncidpop.data.ServerData;
import com.lenderman.ncidpop.listmgmt.ServerRequestManager.ListRequestChangeType;
import com.lenderman.ncidpop.listmgmt.jfd.NcidPopAliasEdit;

/**
 * The NCID Pop Alias Edit display
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class NcidPopAliasEditDisplay extends NcidPopAliasEdit
{
    /**
     * Constructor
     */
    public NcidPopAliasEditDisplay(final boolean isUpdate,
            final String initialValue, final ServerData data,
            final String currentSelectedNumber)
    {
        setTitle(isUpdate ? "Update Alias" : "Add Alias");
        aliasNumberLabel.setText(currentSelectedNumber);
        nameTextField.setText(initialValue);

        nameTextField.addKeyListener(new KeyAdapter()
        {
            /** @inheritDoc */
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    okButton.doClick();
                }
                assessWidgetEnableState();
            }
        });

        okButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                if (isUpdate)
                {
                    ServerRequestManager.requestServerAliasListChange(
                            currentSelectedNumber, initialValue,
                            nameTextField.getText(),
                            ListRequestChangeType.MODIFY, data);
                }
                else if (nameTextField.getText().length() != 0)
                {
                    ServerRequestManager.requestServerAliasListChange(
                            currentSelectedNumber, initialValue,
                            nameTextField.getText(), ListRequestChangeType.ADD,
                            data);
                }
                setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                setVisible(false);
            }
        });

        assessWidgetEnableState();
    }

    /**
     * Assesses enabled state for widgets
     */
    private void assessWidgetEnableState()
    {
        okButton.setEnabled(nameTextField.getText().length() > 0);
    }
}