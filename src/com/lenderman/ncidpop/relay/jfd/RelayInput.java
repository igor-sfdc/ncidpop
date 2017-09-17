/*******************************************************
 * Relay Input Display Dialog - Auto Generated
 *******************************************************/
/*
 * Created by JFormDesigner on Sun Feb 16 20:05:33 CST 2014
 */

package com.lenderman.ncidpop.relay.jfd;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import com.lenderman.ncidpop.utils.OsUtils;
import com.lenderman.ncidpop.utils.gui.jsuggest.JSuggestField;

/**
 * The NCID Pop Relay Input Dialog
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class RelayInput extends JFrame
{
    /**
     * Constructor
     */
    public RelayInput()
    {
        initComponents();
        setIconImage(OsUtils.getDialogIcon());
        numberTextField.setOwner(this);
    }

    /**
     * Initializes components
     */
    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY
        // //GEN-BEGIN:initComponents
        closeButton = new JButton();
        panel1 = new JPanel();
        batteryButton = new JButton();
        locationButton = new JButton();
        ringtoneButton = new JButton();
        panel2 = new JPanel();
        numberTextField = new JSuggestField();
        callButton = new JButton();
        label19 = new JLabel();
        gatewayDeviceNameTextField = new JSuggestField();
        label20 = new JLabel();

        // ======== this ========
        setFont(UIManager.getFont("defaultFont"));
        setResizable(false);
        setTitle("Gateway Relay Options");
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        // ---- closeButton ----
        closeButton.setText("Close");
        contentPane.add(closeButton);
        closeButton.setBounds(160, 230, 80,
                closeButton.getPreferredSize().height);

        // ======== panel1 ========
        {
            panel1.setBorder(new TitledBorder("Gateway Device Status"));
            panel1.setLayout(null);

            // ---- batteryButton ----
            batteryButton.setText("Battery Level");
            panel1.add(batteryButton);
            batteryButton.setBounds(15, 25, 135, 28);

            // ---- locationButton ----
            locationButton.setText("Current Location");
            panel1.add(locationButton);
            locationButton.setBounds(15, 60, 135, 28);

            // ---- ringtoneButton ----
            ringtoneButton.setText("Play Ringtone");
            panel1.add(ringtoneButton);
            ringtoneButton.setBounds(15, 95, 135, 28);
        }
        contentPane.add(panel1);
        panel1.setBounds(10, 80, 170, 135);

        // ======== panel2 ========
        {
            panel2.setBorder(new TitledBorder("Gateway Remote Dialer"));
            panel2.setLayout(null);
            panel2.add(numberTextField);
            numberTextField.setBounds(10, 55, 195, 30);

            // ---- callButton ----
            callButton.setText("Call");
            panel2.add(callButton);
            callButton.setBounds(65, 95, 85,
                    callButton.getPreferredSize().height);

            // ---- label19 ----
            label19.setText("Number to Dial");
            label19.setFont(UIManager.getFont("defaultFont"));
            panel2.add(label19);
            label19.setBounds(15, 35, 145, 14);
        }
        contentPane.add(panel2);
        panel2.setBounds(190, 80, 215, 135);
        contentPane.add(gatewayDeviceNameTextField);
        gatewayDeviceNameTextField.setBounds(10, 35, 195, 30);

        // ---- label20 ----
        label20.setText("Gateway Device Name (specify \"@all\" for all gateway devices)");
        label20.setFont(UIManager.getFont("defaultFont"));
        contentPane.add(label20);
        label20.setBounds(15, 10, 405, 20);

        contentPane.setPreferredSize(new Dimension(435, 305));
        setSize(435, 305);
        setLocationRelativeTo(getOwner());
        // //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY
    // //GEN-BEGIN:variables
    protected JButton closeButton;
    private JPanel panel1;
    protected JButton batteryButton;
    protected JButton locationButton;
    protected JButton ringtoneButton;
    private JPanel panel2;
    protected JSuggestField numberTextField;
    protected JButton callButton;
    private JLabel label19;
    protected JSuggestField gatewayDeviceNameTextField;
    private JLabel label20;
    // JFormDesigner - End of variables declaration //GEN-END:variables
}
