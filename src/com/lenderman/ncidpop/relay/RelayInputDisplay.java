/*******************************************************
 * Relay Input Display
 *******************************************************/
package com.lenderman.ncidpop.relay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import com.lenderman.ncidpop.data.LookupCandidate;
import com.lenderman.ncidpop.data.ServerData;
import com.lenderman.ncidpop.data.ServerDataManager;
import com.lenderman.ncidpop.relay.jfd.RelayInput;
import com.lenderman.ncidpop.utils.DataLookupUtils;
import com.lenderman.ncidpop.utils.PrefUtils;
import com.lenderman.ncidpop.utils.ServerMessageUtils;

/**
 * The NCID Pop Relay Input Dialog Display
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class RelayInputDisplay extends RelayInput
{
    /**
     * Sets the dialog visible
     */
    public void setVisible()
    {
        ServerData data = ServerDataManager.getServerDataForCurrentDisplayId();
        HashSet<LookupCandidate> candidates = DataLookupUtils
                .getAllLookupCandidates(data);
        numberTextField.setSuggestData(candidates);

        setVisible(true);
    }

    /**
     * Constructor
     */
    public RelayInputDisplay()
    {
        super();

        gatewayDeviceNameTextField
                .setText(PrefUtils.instance
                        .getString(PrefUtils.PREF_TEXT_MESSAGE_GATEWAY_RELAY_DEVICE_NAME));

        closeButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                setVisible(false);
            }
        });

        callButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent arg0)
            {
                ServerMessageUtils.sendRelayMessage(
                        ServerDataManager.getServerDataForCurrentDisplayId(),
                        numberTextField.getText(), null,
                        gatewayDeviceNameTextField.getText(), "PLACECALL");
            }
        });

        batteryButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent arg0)
            {
                ServerMessageUtils.sendRelayMessage(
                        ServerDataManager.getServerDataForCurrentDisplayId(),
                        null, null, gatewayDeviceNameTextField.getText(),
                        "BATTERY");
            }
        });

        locationButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent arg0)
            {
                ServerMessageUtils.sendRelayMessage(
                        ServerDataManager.getServerDataForCurrentDisplayId(),
                        null, null, gatewayDeviceNameTextField.getText(),
                        "LOCATION");
            }
        });

        ringtoneButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent arg0)
            {
                ServerMessageUtils.sendRelayMessage(
                        ServerDataManager.getServerDataForCurrentDisplayId(),
                        null, null, gatewayDeviceNameTextField.getText(),
                        "RINGTONE");
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
            }
        });
    }
}