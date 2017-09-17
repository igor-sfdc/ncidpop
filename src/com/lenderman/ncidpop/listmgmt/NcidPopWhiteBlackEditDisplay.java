/*******************************************************
 * NCID Pop White/Black Edit Display
 *******************************************************/
package com.lenderman.ncidpop.listmgmt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.lenderman.ncidpop.data.ServerData;
import com.lenderman.ncidpop.listmgmt.ServerRequestManager.ListRequestChangeType;
import com.lenderman.ncidpop.listmgmt.ServerRequestManager.ListType;
import com.lenderman.ncidpop.listmgmt.jfd.NcidPopWhiteBlackEdit;

/**
 * The NCID Pop White/Black Edit display
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class NcidPopWhiteBlackEditDisplay extends NcidPopWhiteBlackEdit
{
    /**
     * Constructor
     */
    public NcidPopWhiteBlackEditDisplay(final boolean isWhiteList,
            final String name, final String number, final ServerData data)
    {
        titleLabel.setText(titleLabel.getText() + " "
                + (isWhiteList ? "white list?" : "black list?"));
        nameRadioButton.setText(name);
        numberRadioButton.setText(number);

        commentField.addKeyListener(new KeyAdapter()
        {
            /** @inheritDoc */
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    applyButton.doClick();
                }
            }
        });

        applyButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                String resultName = nameRadioButton.isSelected() ? name : null;
                String resultNumber = numberRadioButton.isSelected() ? number
                        : null;

                ServerRequestManager.requestServerBlackWhiteListChange(
                        resultNumber, resultName, commentField.getText(),
                        ListRequestChangeType.ADD, isWhiteList ? ListType.WHITE
                                : ListType.BLACK, data);

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
    }
}