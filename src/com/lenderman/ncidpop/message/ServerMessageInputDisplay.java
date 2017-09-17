/*******************************************************
 * Server Message Input Display
 *******************************************************/
package com.lenderman.ncidpop.message;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import com.lenderman.ncidpop.data.ServerData;
import com.lenderman.ncidpop.message.jfd.ServerMessageInput;
import com.lenderman.ncidpop.utils.NotificationUtils;
import com.lenderman.ncidpop.utils.ServerMessageUtils;

/**
 * The NCID Pop Server Message Input Dialog Display
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class ServerMessageInputDisplay extends ServerMessageInput
{
    /** Abstract action class, used to override the enter key and send data */
    private class Actioner extends AbstractAction
    {
        /** @inheritDoc */
        public void actionPerformed(ActionEvent arg0)
        {
            if (okButton.isEnabled())
            {
                sendMessageData();
            }
        }
    }

    /** ServerData instance */
    private ServerData data;

    /**
     * Sets the dialog visible
     * 
     * @param ServerData the server data object
     */
    public void setVisible(ServerData data)
    {
        messageTextArea.setText("");
        assessWidgetEnabled();
        messageTextArea.requestFocus();
        this.data = data;
        setVisible(true);
    }

    /**
     * Constructor
     */
    public ServerMessageInputDisplay()
    {
        super();

        messageTextArea.getActionMap().put(
                messageTextArea.getInputMap().get(
                        KeyStroke.getKeyStroke("ENTER")), new Actioner());

        messageTextArea.addKeyListener(new KeyAdapter()
        {
            /** @inheritDoc */
            @Override
            public void keyReleased(KeyEvent e)
            {
                assessWidgetEnabled();
            }
        });

        okButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                sendMessageData();
            }
        });

        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setVisible(false);
            }
        });
    }

    /**
     * Enqueues the message data for the server
     */
    private void sendMessageData()
    {
        if (!ServerMessageUtils
                .sendMessageData(data, messageTextArea.getText()))
        {
            NotificationUtils.showDialogPopupMessage(
                    "Unable to send server message", false);
        }
        setVisible(false);
    }

    /**
     * Assesses whether or not widgets should be enabled
     */
    private void assessWidgetEnabled()
    {
        okButton.setEnabled((messageTextArea.getText().length() > 0));
    }
}