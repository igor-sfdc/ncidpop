/*******************************************************
 * SMS Input Display
 *******************************************************/
package com.lenderman.ncidpop.sms;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.lenderman.ncidpop.data.LookupCandidate;
import com.lenderman.ncidpop.data.ServerData;
import com.lenderman.ncidpop.data.ServerDataManager;
import com.lenderman.ncidpop.sms.jfd.SmsInput;
import com.lenderman.ncidpop.utils.DataLookupUtils;
import com.lenderman.ncidpop.utils.NotificationUtils;
import com.lenderman.ncidpop.utils.OsUtils;
import com.lenderman.ncidpop.utils.PrefUtils;
import com.lenderman.ncidpop.utils.ServerMessageUtils;

/**
 * The NCID Pop SMS Input Dialog Display
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class SmsInputDisplay extends SmsInput
{
    /** Initial foreground color for character count label */
    private Color initialCharCountLabelForeground = characterCountLabel
            .getForeground();

    /** Single threaded executor for SMS sender */
    private Executor sendSmsExecutor = Executors
            .newSingleThreadScheduledExecutor();

    /**
     * Sets the dialog visible
     * 
     * @param String the number to use for texting
     */
    public void setVisible(String initialNumber)
    {
        ServerData data = ServerDataManager.getServerDataForCurrentDisplayId();

        HashSet<LookupCandidate> candidates = DataLookupUtils
                .getAllLookupCandidates(data);
        numberTextField.setSuggestData(candidates);

        numberTextField.setText(initialNumber);
        messageTextArea.setText("");
        characterCountLabel.setText("0");
        characterCountLabel.setForeground(initialCharCountLabelForeground);
        assessWidgetEnabled();

        if (numberTextField.getText().length() == 0)
        {
            numberTextField.requestFocus();
        }
        else
        {
            messageTextArea.requestFocus();
        }
        setVisible(true);
    }

    /**
     * Constructor
     */
    public SmsInputDisplay()
    {
        super();

        messageTextArea.addKeyListener(new KeyAdapter()
        {
            /** @inheritDoc */
            @Override
            public void keyReleased(KeyEvent e)
            {
                int messageLength = messageTextArea.getText().length();
                characterCountLabel.setText(Integer.toString(messageLength));
                characterCountLabel
                        .setForeground(messageLength > 160 ? Color.RED
                                : initialCharCountLabelForeground);
                assessWidgetEnabled();
            }
        });

        numberTextField.addKeyListener(new KeyAdapter()
        {
            /** @inheritDoc */
            @Override
            public void keyReleased(KeyEvent e)
            {
                assessWidgetEnabled();
            }
        });

        numberTextField.addSelectionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                LookupCandidate candidate = (LookupCandidate) numberTextField
                        .getLastChosenExistingVariable();
                numberTextField.setText(candidate.number);
                messageTextArea.requestFocus();
            }
        });

        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (PrefUtils.instance
                        .getBoolean(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_GATEWAY_RELAY))
                {
                    ServerData data = ServerDataManager
                            .getServerDataForCurrentDisplayId();
                    String gatewayName = PrefUtils.instance
                            .getString(PrefUtils.PREF_TEXT_MESSAGE_GATEWAY_RELAY_DEVICE_NAME);
                    String number = numberTextField.getText().replace("+", "");
                    String message = messageTextArea.getText()
                            .replace('\n', ' ').replace('\r', ' ')
                            .replace("*", "<asterick>");

                    ServerMessageUtils.sendRelayMessage(data, number, message,
                            gatewayName, "TEXTMSG");
                }

                if (PrefUtils.instance
                        .getBoolean(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_EXTERNAL_PROGRAM))
                {
                    final String command = PrefUtils.instance
                            .getString(PrefUtils.PREF_TEXT_MESSAGE_EXTERNAL_PROGRAM);

                    final HashMap<String, String> substitutions = new HashMap<String, String>();
                    substitutions.put("$NUMBER", numberTextField.getText());
                    substitutions.put("$MESSAGE", messageTextArea.getText());

                    sendSmsExecutor.execute(new Runnable()
                    {
                        public void run()
                        {
                            try
                            {
                                ArrayList<String> output = OsUtils
                                        .executeCommandLineCommand(command,
                                                substitutions, true);

                                if (PrefUtils.instance
                                        .getBoolean(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_EXTERNAL_PROGRAM_OUTPUT))
                                {
                                    if (output.size() > 0)
                                    {
                                        StringBuilder sb = new StringBuilder();
                                        for (String s : output)
                                        {
                                            sb.append(s);
                                            sb.append("\t");
                                        }

                                        NotificationUtils.showDialogPopupMessage(
                                                "Send SMS: " + sb.toString(),
                                                true);
                                    }
                                }
                            }
                            catch (final Exception ex)
                            {
                                NotificationUtils.enqueueDialog(new Runnable()
                                {
                                    public void run()
                                    {
                                        NotificationUtils.showDialogPopupMessage(
                                                " Couldn't launch text message sender program: "
                                                        + ex.toString(), true);
                                    }
                                });
                            }
                        }
                    });
                }
                setVisible(false);
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
     * Assesses whether or not widgets should be enabled
     */
    private void assessWidgetEnabled()
    {
        okButton.setEnabled((messageTextArea.getText().length() > 0)
                && (numberTextField.getText().length() > 0));
    }
}