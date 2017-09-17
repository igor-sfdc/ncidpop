/*******************************************************
 * NCID Pop Preferences Dialog - Auto Generated
 *******************************************************/
/*
 * Created by JFormDesigner on Mon Dec 23 08:56:06 PST 2013
 */

package com.lenderman.ncidpop.preferences.jfd;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import com.lenderman.ncidpop.utils.OsUtils;

/**
 * The NCID Pop Preferences Dialog
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("all")
public class NcidPopPreferences extends JFrame
{
    /**
     * Constant labels that we apply to radio buttons so that we can uniquely
     * identify them in preferences, etc.
     */
    public static String NAME_READING_NONE_LABEL = "None";
    public static String NAME_READING_SYSTEM_DEFAULT_LABEL = "System Default";
    public static String NAME_READING_THIRD_PARTY_LABEL = "3rd Party";
    public static String MESSAGE_NOTIFY_DIALOG_LABEL = "Dialog";
    public static String MESSAGE_NOTIFY_SYSTEM_TRAY_LABEL = "System Tray";
    public static String MESSAGE_NOTIFY_THIRD_PARTY_LABEL = "3rd Party Notifier (such as growl or notify-send)";
    public static String MESSAGE_FORMAT_PRETTY_PRINT_LABEL = "Pretty Print";
    public static String MESSAGE_FORMAT_CUSTOM_LABEL = "Custom";

    /**
     * Constructor
     */
    public NcidPopPreferences()
    {
        initComponents();
        setIconImage(OsUtils.getDialogIcon());
        nameReadingDefaultRadioButton
                .setText(NAME_READING_SYSTEM_DEFAULT_LABEL);
        nameReadingNoneRadioButton.setText(NAME_READING_NONE_LABEL);
        nameReadingThirdPartyRadioButton
                .setText(NAME_READING_THIRD_PARTY_LABEL);

        messageNotifyDialogRadioButton.setText(MESSAGE_NOTIFY_DIALOG_LABEL);
        messageNotifySystemTrayRadioButton
                .setText(MESSAGE_NOTIFY_SYSTEM_TRAY_LABEL);
        messageNotifyThirdPartyRadioButton
                .setText(MESSAGE_NOTIFY_THIRD_PARTY_LABEL);

        messageFormatPrettyPrintRadioButton
                .setText(MESSAGE_FORMAT_PRETTY_PRINT_LABEL);
        messageFormatCustomRadioButton.setText(MESSAGE_FORMAT_CUSTOM_LABEL);
    }

    /**
     * Initializes components
     */
    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY
        // //GEN-BEGIN:initComponents
        setButton = new JButton();
        cancelButton = new JButton();
        defaultsButton = new JButton();
        preferencesTabbedPane = new JTabbedPane();
        panel6 = new JPanel();
        panel9 = new JPanel();
        serversTextField = new JTextField();
        label3 = new JLabel();
        label2 = new JLabel();
        label1 = new JLabel();
        requestCallLogFromServerCheckBox = new JCheckBox();
        panel10 = new JPanel();
        displayUnformattedDateTimeCheckBox = new JCheckBox();
        useEuropeanDateFormatCheckBox = new JCheckBox();
        useSystemAddressBookCheckBox = new JCheckBox();
        panel17 = new JPanel();
        notifyLowBatteryCheckBox = new JCheckBox();
        notifyFullChargeCheckBox = new JCheckBox();
        notifyLowBatteryThresholdSpinner = new JSpinner();
        panel1 = new JPanel();
        popupFieldsTabbedPane = new JTabbedPane();
        panel14 = new JPanel();
        panel12 = new JPanel();
        dateCheckBox = new JCheckBox();
        typeCheckBox = new JCheckBox();
        nameCheckBox = new JCheckBox();
        timeCheckBox = new JCheckBox();
        lineCheckBox = new JCheckBox();
        numberCheckBox = new JCheckBox();
        testCallsPopupButton = new JButton();
        panel16 = new JPanel();
        thirdPartyNotifyCheckBox = new JCheckBox();
        thirdPartyCallNotifyTextField = new JTextField();
        label10 = new JLabel();
        label17 = new JLabel();
        label18 = new JLabel();
        displayIncomingCallPopupsCheckBox = new JCheckBox();
        displayOutgoingAndStatusCallPopupsCheckBox = new JCheckBox();
        panel15 = new JPanel();
        panel4 = new JPanel();
        messageNotifyDialogRadioButton = new JRadioButton();
        messageNotifySystemTrayRadioButton = new JRadioButton();
        messageNotifyThirdPartyRadioButton = new JRadioButton();
        thirdPartyMessageNotifyTextField = new JTextField();
        label16 = new JLabel();
        label26 = new JLabel();
        label29 = new JLabel();
        testMessagesPopupButton = new JButton();
        displayIncomingMessagePopupsCheckBox = new JCheckBox();
        panel18 = new JPanel();
        messageDateCheckBox = new JCheckBox();
        messageTypeCheckBox = new JCheckBox();
        messageFromToCheckBox = new JCheckBox();
        messageTimeCheckBox = new JCheckBox();
        messageTextCheckBox = new JCheckBox();
        messageDeviceCheckBox = new JCheckBox();
        messageFormatPrettyPrintRadioButton = new JRadioButton();
        messageFormatCustomRadioButton = new JRadioButton();
        panel11 = new JPanel();
        panel13 = new JPanel();
        label19 = new JLabel();
        phoneNumberFormatTextField = new JTextField();
        label20 = new JLabel();
        reverseLookupTextField = new JTextField();
        label21 = new JLabel();
        label22 = new JLabel();
        label23 = new JLabel();
        label24 = new JLabel();
        label25 = new JLabel();
        label27 = new JLabel();
        label28 = new JLabel();
        panel3 = new JPanel();
        enableSmartPhoneTextNotifyCheckBox = new JCheckBox();
        panel8 = new JPanel();
        textMessageUrlCheckBox = new JCheckBox();
        label12 = new JLabel();
        textMessageUrlTextField = new JTextField();
        textMessageExternalProgramCheckBox = new JCheckBox();
        textMessageExternalProgramTextField = new JTextField();
        label13 = new JLabel();
        label15 = new JLabel();
        exampleProgramsLabel = new JLabel();
        displayOutputFromExternalProgramCheckBox = new JCheckBox();
        textMessageGatewayRelayDeviceCheckBox = new JCheckBox();
        textMessageGatewayRelayDeviceName = new JTextField();
        label4 = new JLabel();
        enableExtraLogFieldsCheckBox = new JCheckBox();
        panel2 = new JPanel();
        panel5 = new JPanel();
        nameReadingNoneRadioButton = new JRadioButton();
        nameReadingDefaultRadioButton = new JRadioButton();
        nameReadingThirdPartyRadioButton = new JRadioButton();
        thirdPartyNameReadTextField = new JTextField();
        testSoundButton = new JButton();
        label11 = new JLabel();
        panel7 = new JPanel();
        label9 = new JLabel();
        audioMixerComboBox = new JComboBox();
        ringtoneComboBox = new JComboBox();
        playButton = new JButton();
        browseButton = new JButton();
        label14 = new JLabel();
        readIncomingCallerNamesCheckBox = new JCheckBox();
        readIncomingMessagesCheckBox = new JCheckBox();
        readOutgoingAndStatusCallerNamesCheckBox = new JCheckBox();
        messageNotifySelectionButtonGroup = new ButtonGroup();
        messageFormatButtonGroup = new ButtonGroup();
        nameReadingButtonGroup = new ButtonGroup();

        // ======== this ========
        setTitle("Preferences");
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        // ---- setButton ----
        setButton.setText("Set");
        contentPane.add(setButton);
        setButton.setBounds(530, 5, 85, setButton.getPreferredSize().height);

        // ---- cancelButton ----
        cancelButton.setText("Cancel");
        contentPane.add(cancelButton);
        cancelButton.setBounds(530, 35, 85,
                cancelButton.getPreferredSize().height);

        // ---- defaultsButton ----
        defaultsButton.setText("Defaults");
        contentPane.add(defaultsButton);
        defaultsButton.setBounds(530, 65, 85,
                defaultsButton.getPreferredSize().height);

        // ======== preferencesTabbedPane ========
        {
            preferencesTabbedPane.setBorder(LineBorder.createBlackLineBorder());
            preferencesTabbedPane.setFont(UIManager.getFont("defaultFont"));

            // ======== panel6 ========
            {
                panel6.setFont(UIManager.getFont("defaultFont"));
                panel6.setLayout(null);

                // ======== panel9 ========
                {
                    panel9.setBorder(new TitledBorder("Server Settings"));
                    panel9.setLayout(null);

                    // ---- serversTextField ----
                    serversTextField.setFont(UIManager.getFont("defaultFont"));
                    panel9.add(serversTextField);
                    serversTextField.setBounds(10, 75, 465, 30);

                    // ---- label3 ----
                    label3.setText("(optional multi-server setting: separate entries with a comma)");
                    label3.setFont(UIManager.getFont("defaultFont"));
                    panel9.add(label3);
                    label3.setBounds(10, 55, 460, 14);

                    // ---- label2 ----
                    label2.setText("(optional port number setting: append a colon and port number)");
                    label2.setFont(UIManager.getFont("defaultFont"));
                    panel9.add(label2);
                    label2.setBounds(10, 40, 460, 14);

                    // ---- label1 ----
                    label1.setText("Hostname or Internet address of the ncidd Server:");
                    label1.setFont(UIManager.getFont("defaultFont"));
                    panel9.add(label1);
                    label1.setBounds(10, 25, 460, 16);

                    // ---- requestCallLogFromServerCheckBox ----
                    requestCallLogFromServerCheckBox
                            .setText("Request log from server if not provided on server startup");
                    requestCallLogFromServerCheckBox.setFont(UIManager
                            .getFont("defaultFont"));
                    panel9.add(requestCallLogFromServerCheckBox);
                    requestCallLogFromServerCheckBox
                            .setBounds(10, 110, 445, 20);
                }
                panel6.add(panel9);
                panel9.setBounds(5, 10, 485, 150);

                // ======== panel10 ========
                {
                    panel10.setBorder(new TitledBorder("Display Settings"));
                    panel10.setLayout(null);

                    // ---- displayUnformattedDateTimeCheckBox ----
                    displayUnformattedDateTimeCheckBox
                            .setText("Display dates and times as received from server (unformatted)");
                    displayUnformattedDateTimeCheckBox.setFont(UIManager
                            .getFont("defaultFont"));
                    panel10.add(displayUnformattedDateTimeCheckBox);
                    displayUnformattedDateTimeCheckBox.setBounds(10, 20, 445,
                            20);

                    // ---- useEuropeanDateFormatCheckBox ----
                    useEuropeanDateFormatCheckBox
                            .setText("Process dates as European format (DD-MM-YYYY)");
                    useEuropeanDateFormatCheckBox.setFont(UIManager
                            .getFont("defaultFont"));
                    panel10.add(useEuropeanDateFormatCheckBox);
                    useEuropeanDateFormatCheckBox.setBounds(10, 40, 445, 20);

                    // ---- useSystemAddressBookCheckBox ----
                    useSystemAddressBookCheckBox
                            .setText("Integrate with system address book for displaying aliases (if available)");
                    useSystemAddressBookCheckBox.setFont(UIManager
                            .getFont("defaultFont"));
                    panel10.add(useSystemAddressBookCheckBox);
                    useSystemAddressBookCheckBox.setBounds(10, 60, 445, 20);
                }
                panel6.add(panel10);
                panel10.setBounds(5, 165, 485, 95);

                // ======== panel17 ========
                {
                    panel17.setBorder(new TitledBorder("Power Settings"));
                    panel17.setLayout(null);

                    // ---- notifyLowBatteryCheckBox ----
                    notifyLowBatteryCheckBox
                            .setText("Notify server(s) on low battery percentage");
                    notifyLowBatteryCheckBox.setFont(UIManager
                            .getFont("defaultFont"));
                    panel17.add(notifyLowBatteryCheckBox);
                    notifyLowBatteryCheckBox.setBounds(10, 25, 275, 20);

                    // ---- notifyFullChargeCheckBox ----
                    notifyFullChargeCheckBox
                            .setText("Notify server(s) when battery reaches full charge");
                    notifyFullChargeCheckBox.setFont(UIManager
                            .getFont("defaultFont"));
                    panel17.add(notifyFullChargeCheckBox);
                    notifyFullChargeCheckBox.setBounds(10, 55, 445, 20);

                    // ---- notifyLowBatteryThresholdSpinner ----
                    notifyLowBatteryThresholdSpinner.setFont(UIManager
                            .getFont("defaultFont"));
                    notifyLowBatteryThresholdSpinner
                            .setModel(new SpinnerNumberModel(0, 0, 100, 5));
                    panel17.add(notifyLowBatteryThresholdSpinner);
                    notifyLowBatteryThresholdSpinner.setBounds(285, 20, 70, 30);
                }
                panel6.add(panel17);
                panel17.setBounds(5, 265, 485, 90);
            }
            preferencesTabbedPane.addTab("General", panel6);

            // ======== panel1 ========
            {
                panel1.setBorder(null);
                panel1.setFont(UIManager.getFont("defaultFont"));
                panel1.setLayout(null);

                // ======== popupFieldsTabbedPane ========
                {
                    popupFieldsTabbedPane.setFont(UIManager
                            .getFont("defaultFont"));

                    // ======== panel14 ========
                    {
                        panel14.setFont(UIManager.getFont("defaultFont"));
                        panel14.setLayout(null);

                        // ======== panel12 ========
                        {
                            panel12.setBorder(new TitledBorder(
                                    "Call Popup Settings"));
                            panel12.setLayout(null);

                            // ---- dateCheckBox ----
                            dateCheckBox.setText("Date");
                            dateCheckBox.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel12.add(dateCheckBox);
                            dateCheckBox.setBounds(10, 45, 65, 20);

                            // ---- typeCheckBox ----
                            typeCheckBox.setText("Type");
                            typeCheckBox.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel12.add(typeCheckBox);
                            typeCheckBox.setBounds(10, 25, 65, 20);

                            // ---- nameCheckBox ----
                            nameCheckBox.setText("Name");
                            nameCheckBox.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel12.add(nameCheckBox);
                            nameCheckBox.setBounds(90, 25, 65, 20);

                            // ---- timeCheckBox ----
                            timeCheckBox.setText("Time");
                            timeCheckBox.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel12.add(timeCheckBox);
                            timeCheckBox.setBounds(90, 45, 65, 20);

                            // ---- lineCheckBox ----
                            lineCheckBox.setText("Line");
                            lineCheckBox.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel12.add(lineCheckBox);
                            lineCheckBox.setBounds(170, 45, 65, 20);

                            // ---- numberCheckBox ----
                            numberCheckBox.setText("Number");
                            numberCheckBox.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel12.add(numberCheckBox);
                            numberCheckBox.setBounds(170, 25, 75, 20);
                        }
                        panel14.add(panel12);
                        panel12.setBounds(5, 60, 475, 80);

                        // ---- testCallsPopupButton ----
                        testCallsPopupButton.setText("Test Popup");
                        panel14.add(testCallsPopupButton);
                        testCallsPopupButton.setBounds(180, 295, 105, 28);

                        // ======== panel16 ========
                        {
                            panel16.setBorder(new TitledBorder(
                                    "Calls and Notifications Popup Display Selection"));
                            panel16.setFont(UIManager.getFont("defaultFont"));
                            panel16.setLayout(null);

                            // ---- thirdPartyNotifyCheckBox ----
                            thirdPartyNotifyCheckBox
                                    .setText("Use a 3rd Party Notifier (such as growl or notify-send)");
                            thirdPartyNotifyCheckBox.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel16.add(thirdPartyNotifyCheckBox);
                            thirdPartyNotifyCheckBox.setBounds(10, 25, 455, 20);

                            // ---- thirdPartyCallNotifyTextField ----
                            thirdPartyCallNotifyTextField.setFont(UIManager
                                    .getFont("defaultFont"));
                            thirdPartyCallNotifyTextField.setEnabled(false);
                            panel16.add(thirdPartyCallNotifyTextField);
                            thirdPartyCallNotifyTextField.setBounds(10, 45,
                                    450, 30);

                            // ---- label10 ----
                            label10.setText("Variable substitution:");
                            label10.setFont(UIManager.getFont("defaultFont"));
                            panel16.add(label10);
                            label10.setBounds(15, 75, 455, 14);

                            // ---- label17 ----
                            label17.setText("$POPUP will display the calls/notification message.");
                            label17.setFont(UIManager.getFont("defaultFont"));
                            panel16.add(label17);
                            label17.setBounds(15, 90, 455, 14);

                            // ---- label18 ----
                            label18.setText("$IMAGE will display the address book image.");
                            label18.setFont(UIManager.getFont("defaultFont"));
                            panel16.add(label18);
                            label18.setBounds(15, 105, 455, 14);
                        }
                        panel14.add(panel16);
                        panel16.setBounds(5, 155, 475, 130);

                        // ---- displayIncomingCallPopupsCheckBox ----
                        displayIncomingCallPopupsCheckBox
                                .setText("Display Incoming Call Popups");
                        displayIncomingCallPopupsCheckBox.setFont(UIManager
                                .getFont("defaultFont"));
                        panel14.add(displayIncomingCallPopupsCheckBox);
                        displayIncomingCallPopupsCheckBox.setBounds(10, 5, 480,
                                20);

                        // ---- displayOutgoingAndStatusCallPopupsCheckBox ----
                        displayOutgoingAndStatusCallPopupsCheckBox
                                .setText("Display Outgoing & Status Call Popups");
                        displayOutgoingAndStatusCallPopupsCheckBox
                                .setFont(UIManager.getFont("defaultFont"));
                        panel14.add(displayOutgoingAndStatusCallPopupsCheckBox);
                        displayOutgoingAndStatusCallPopupsCheckBox.setBounds(
                                10, 25, 480, 20);

                        { // compute preferred size
                            Dimension preferredSize = new Dimension();
                            for (int i = 0; i < panel14.getComponentCount(); i++)
                            {
                                Rectangle bounds = panel14.getComponent(i)
                                        .getBounds();
                                preferredSize.width = Math.max(bounds.x
                                        + bounds.width, preferredSize.width);
                                preferredSize.height = Math.max(bounds.y
                                        + bounds.height, preferredSize.height);
                            }
                            Insets insets = panel14.getInsets();
                            preferredSize.width += insets.right;
                            preferredSize.height += insets.bottom;
                            panel14.setMinimumSize(preferredSize);
                            panel14.setPreferredSize(preferredSize);
                        }
                    }
                    popupFieldsTabbedPane
                            .addTab("Calls/Notifications", panel14);

                    // ======== panel15 ========
                    {
                        panel15.setLayout(null);

                        // ======== panel4 ========
                        {
                            panel4.setBorder(new TitledBorder(
                                    "Message Popup Display Selection"));
                            panel4.setFont(UIManager.getFont("defaultFont"));
                            panel4.setLayout(null);

                            // ---- messageNotifyDialogRadioButton ----
                            messageNotifyDialogRadioButton.setText("Dialog");
                            messageNotifyDialogRadioButton.setSelected(true);
                            messageNotifyDialogRadioButton.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel4.add(messageNotifyDialogRadioButton);
                            messageNotifyDialogRadioButton.setBounds(15, 20,
                                    70, 18);

                            // ---- messageNotifySystemTrayRadioButton ----
                            messageNotifySystemTrayRadioButton
                                    .setText("System Tray");
                            messageNotifySystemTrayRadioButton
                                    .setFont(UIManager.getFont("defaultFont"));
                            panel4.add(messageNotifySystemTrayRadioButton);
                            messageNotifySystemTrayRadioButton.setBounds(15,
                                    40, 100, 18);

                            // ---- messageNotifyThirdPartyRadioButton ----
                            messageNotifyThirdPartyRadioButton
                                    .setText("3rd Party Notifier (such as growl or notify-send)");
                            messageNotifyThirdPartyRadioButton
                                    .setFont(UIManager.getFont("defaultFont"));
                            panel4.add(messageNotifyThirdPartyRadioButton);
                            messageNotifyThirdPartyRadioButton.setBounds(15,
                                    60, 345, 18);

                            // ---- thirdPartyMessageNotifyTextField ----
                            thirdPartyMessageNotifyTextField.setFont(UIManager
                                    .getFont("defaultFont"));
                            thirdPartyMessageNotifyTextField.setEnabled(false);
                            panel4.add(thirdPartyMessageNotifyTextField);
                            thirdPartyMessageNotifyTextField.setBounds(10, 80,
                                    450, 30);

                            // ---- label16 ----
                            label16.setText("Variable substitution: ");
                            label16.setFont(UIManager.getFont("defaultFont"));
                            panel4.add(label16);
                            label16.setBounds(10, 115, 455, 14);

                            // ---- label26 ----
                            label26.setText("$IMAGE will display the address book image.");
                            label26.setFont(UIManager.getFont("defaultFont"));
                            panel4.add(label26);
                            label26.setBounds(10, 145, 455, 14);

                            // ---- label29 ----
                            label29.setText("$MESSAGE will display the message.");
                            label29.setFont(UIManager.getFont("defaultFont"));
                            panel4.add(label29);
                            label29.setBounds(10, 130, 455, 14);
                        }
                        panel15.add(panel4);
                        panel4.setBounds(5, 130, 475, 170);

                        // ---- testMessagesPopupButton ----
                        testMessagesPopupButton.setText("Test Popup");
                        panel15.add(testMessagesPopupButton);
                        testMessagesPopupButton.setBounds(180, 300, 105, 28);

                        // ---- displayIncomingMessagePopupsCheckBox ----
                        displayIncomingMessagePopupsCheckBox
                                .setText("Display Incoming Message Popups");
                        displayIncomingMessagePopupsCheckBox.setFont(UIManager
                                .getFont("defaultFont"));
                        panel15.add(displayIncomingMessagePopupsCheckBox);
                        displayIncomingMessagePopupsCheckBox.setBounds(10, 5,
                                235, 20);

                        // ======== panel18 ========
                        {
                            panel18.setBorder(new TitledBorder(
                                    "Message Popup Settings"));
                            panel18.setLayout(null);

                            // ---- messageDateCheckBox ----
                            messageDateCheckBox.setText("Date");
                            messageDateCheckBox.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel18.add(messageDateCheckBox);
                            messageDateCheckBox.setBounds(235, 50, 65, 20);

                            // ---- messageTypeCheckBox ----
                            messageTypeCheckBox.setText("Type");
                            messageTypeCheckBox.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel18.add(messageTypeCheckBox);
                            messageTypeCheckBox.setBounds(130, 50, 65, 20);

                            // ---- messageFromToCheckBox ----
                            messageFromToCheckBox.setText("From/To");
                            messageFromToCheckBox.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel18.add(messageFromToCheckBox);
                            messageFromToCheckBox.setBounds(235, 70, 85, 20);

                            // ---- messageTimeCheckBox ----
                            messageTimeCheckBox.setText("Time");
                            messageTimeCheckBox.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel18.add(messageTimeCheckBox);
                            messageTimeCheckBox.setBounds(340, 50, 65, 20);

                            // ---- messageTextCheckBox ----
                            messageTextCheckBox.setText("Text");
                            messageTextCheckBox.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel18.add(messageTextCheckBox);
                            messageTextCheckBox.setBounds(340, 70, 65, 20);

                            // ---- messageDeviceCheckBox ----
                            messageDeviceCheckBox.setText("Device");
                            messageDeviceCheckBox.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel18.add(messageDeviceCheckBox);
                            messageDeviceCheckBox.setBounds(130, 70, 75, 20);

                            // ---- messageFormatPrettyPrintRadioButton ----
                            messageFormatPrettyPrintRadioButton
                                    .setText("Pretty Print");
                            messageFormatPrettyPrintRadioButton
                                    .setSelected(true);
                            messageFormatPrettyPrintRadioButton
                                    .setFont(UIManager.getFont("defaultFont"));
                            panel18.add(messageFormatPrettyPrintRadioButton);
                            messageFormatPrettyPrintRadioButton.setBounds(10,
                                    25, 115, 18);

                            // ---- messageFormatCustomRadioButton ----
                            messageFormatCustomRadioButton.setText("Custom");
                            messageFormatCustomRadioButton.setFont(UIManager
                                    .getFont("defaultFont"));
                            panel18.add(messageFormatCustomRadioButton);
                            messageFormatCustomRadioButton.setBounds(130, 25,
                                    100, 18);
                        }
                        panel15.add(panel18);
                        panel18.setBounds(5, 30, 475, 100);

                        { // compute preferred size
                            Dimension preferredSize = new Dimension();
                            for (int i = 0; i < panel15.getComponentCount(); i++)
                            {
                                Rectangle bounds = panel15.getComponent(i)
                                        .getBounds();
                                preferredSize.width = Math.max(bounds.x
                                        + bounds.width, preferredSize.width);
                                preferredSize.height = Math.max(bounds.y
                                        + bounds.height, preferredSize.height);
                            }
                            Insets insets = panel15.getInsets();
                            preferredSize.width += insets.right;
                            preferredSize.height += insets.bottom;
                            panel15.setMinimumSize(preferredSize);
                            panel15.setPreferredSize(preferredSize);
                        }
                    }
                    popupFieldsTabbedPane.addTab("Messages/Notifications",
                            panel15);
                }
                panel1.add(popupFieldsTabbedPane);
                popupFieldsTabbedPane.setBounds(5, 0, 500, 370);
            }
            preferencesTabbedPane.addTab("Popup Fields", panel1);

            // ======== panel11 ========
            {
                panel11.setLayout(null);

                // ======== panel13 ========
                {
                    panel13.setBorder(new TitledBorder("Phone Number Settings"));
                    panel13.setLayout(null);

                    // ---- label19 ----
                    label19.setText("Phone Number Format:");
                    label19.setFont(UIManager.getFont("defaultFont"));
                    panel13.add(label19);
                    label19.setBounds(10, 25, 244, 14);

                    // ---- phoneNumberFormatTextField ----
                    phoneNumberFormatTextField.setFont(UIManager
                            .getFont("defaultFont"));
                    panel13.add(phoneNumberFormatTextField);
                    phoneNumberFormatTextField.setBounds(10, 45, 465, 30);

                    // ---- label20 ----
                    label20.setText("Internet reverse-lookup URL:");
                    label20.setFont(UIManager.getFont("defaultFont"));
                    panel13.add(label20);
                    label20.setBounds(10, 75, 244, 14);

                    // ---- reverseLookupTextField ----
                    reverseLookupTextField.setFont(UIManager
                            .getFont("defaultFont"));
                    panel13.add(reverseLookupTextField);
                    reverseLookupTextField.setBounds(10, 95, 465, 30);

                    // ---- label21 ----
                    label21.setText("Variable substitution (Phone Number Format and reverse-lookup URL):");
                    label21.setFont(UIManager.getFont("defaultFont"));
                    panel13.add(label21);
                    label21.setBounds(10, 125, 465, 14);

                    // ---- label22 ----
                    label22.setText("2) $PRE, $AREA, $NPA, $NXX (for phone numbers 10 digits and above):");
                    label22.setFont(UIManager.getFont("defaultFont"));
                    panel13.add(label22);
                    label22.setBounds(10, 165, 465, 14);

                    // ---- label23 ----
                    label23.setText("If the phone number is 1800-555-1212, $PRE will be 1 (the \"prefix\"),");
                    label23.setFont(UIManager.getFont("defaultFont"));
                    panel13.add(label23);
                    label23.setBounds(25, 195, 450, 14);

                    // ---- label24 ----
                    label24.setText("$AREA will be 800, $NPA will be 555 and $NXX will be 1212.");
                    label24.setFont(UIManager.getFont("defaultFont"));
                    panel13.add(label24);
                    label24.setBounds(25, 210, 450, 14);

                    // ---- label25 ----
                    label25.setText("1) $ALL: Use phone number with no formatting.");
                    label25.setFont(UIManager.getFont("defaultFont"));
                    panel13.add(label25);
                    label25.setBounds(10, 145, 465, 14);

                    // ---- label27 ----
                    label27.setText("Apply North American Numbering Plan format to phone number.");
                    label27.setFont(UIManager.getFont("defaultFont"));
                    panel13.add(label27);
                    label27.setBounds(25, 180, 450, 14);

                    // ---- label28 ----
                    label28.setText("NOTE: Only one of the two above options is valid (options cannot be combined).");
                    label28.setFont(UIManager.getFont("defaultFont"));
                    panel13.add(label28);
                    label28.setBounds(10, 240, 465, 14);
                }
                panel11.add(panel13);
                panel13.setBounds(5, 10, 485, 275);
            }
            preferencesTabbedPane.addTab("Phone Number Options", panel11);

            // ======== panel3 ========
            {
                panel3.setBorder(null);
                panel3.setFont(UIManager.getFont("defaultFont"));
                panel3.setLayout(null);

                // ---- enableSmartPhoneTextNotifyCheckBox ----
                enableSmartPhoneTextNotifyCheckBox
                        .setText("Display text notifications from smart phones");
                enableSmartPhoneTextNotifyCheckBox.setFont(UIManager
                        .getFont("defaultFont"));
                panel3.add(enableSmartPhoneTextNotifyCheckBox);
                enableSmartPhoneTextNotifyCheckBox.setBounds(10, 10, 435, 20);

                // ======== panel8 ========
                {
                    panel8.setBorder(new TitledBorder(
                            "Send Text Message Options"));
                    panel8.setLayout(null);

                    // ---- textMessageUrlCheckBox ----
                    textMessageUrlCheckBox
                            .setText("Use Internet Text Message URL");
                    textMessageUrlCheckBox.setFont(UIManager
                            .getFont("defaultFont"));
                    panel8.add(textMessageUrlCheckBox);
                    textMessageUrlCheckBox.setBounds(15, 30, 244, 14);

                    // ---- label12 ----
                    label12.setText("Variable substitution: $NUMBER is the smart phone number.");
                    label12.setFont(UIManager.getFont("defaultFont"));
                    panel8.add(label12);
                    label12.setBounds(15, 80, 450, 14);

                    // ---- textMessageUrlTextField ----
                    textMessageUrlTextField.setFont(UIManager
                            .getFont("defaultFont"));
                    panel8.add(textMessageUrlTextField);
                    textMessageUrlTextField.setBounds(15, 50, 455, 30);

                    // ---- textMessageExternalProgramCheckBox ----
                    textMessageExternalProgramCheckBox
                            .setText("Use External Program (i.e. a perl script that uses Google Voice)");
                    textMessageExternalProgramCheckBox.setFont(UIManager
                            .getFont("defaultFont"));
                    panel8.add(textMessageExternalProgramCheckBox);
                    textMessageExternalProgramCheckBox.setBounds(15, 115, 450,
                            14);

                    // ---- textMessageExternalProgramTextField ----
                    textMessageExternalProgramTextField.setFont(UIManager
                            .getFont("defaultFont"));
                    panel8.add(textMessageExternalProgramTextField);
                    textMessageExternalProgramTextField.setBounds(15, 160, 455,
                            30);

                    // ---- label13 ----
                    label13.setText("Variable substitution: $NUMBER is the smart phone number. A separate");
                    label13.setFont(UIManager.getFont("defaultFont"));
                    panel8.add(label13);
                    label13.setBounds(15, 210, 450, 14);

                    // ---- label15 ----
                    label15.setText("form will pop up and prompt you for a $MESSAGE.");
                    label15.setFont(UIManager.getFont("defaultFont"));
                    panel8.add(label15);
                    label15.setBounds(15, 225, 450, 14);

                    // ---- exampleProgramsLabel ----
                    exampleProgramsLabel
                            .setText("<html><a href>Click Here for Example Programs</a></html>");
                    exampleProgramsLabel.setFont(UIManager
                            .getFont("defaultFont"));
                    panel8.add(exampleProgramsLabel);
                    exampleProgramsLabel.setBounds(15, 190, 450, 14);

                    // ---- displayOutputFromExternalProgramCheckBox ----
                    displayOutputFromExternalProgramCheckBox
                            .setText("Display Output Results from External Program");
                    displayOutputFromExternalProgramCheckBox.setFont(UIManager
                            .getFont("defaultFont"));
                    panel8.add(displayOutputFromExternalProgramCheckBox);
                    displayOutputFromExternalProgramCheckBox.setBounds(15, 140,
                            450, 14);

                    // ---- textMessageGatewayRelayDeviceCheckBox ----
                    textMessageGatewayRelayDeviceCheckBox
                            .setText("Send text message using a gateway relay device (i.e. NCID Android)");
                    textMessageGatewayRelayDeviceCheckBox.setFont(UIManager
                            .getFont("defaultFont"));
                    panel8.add(textMessageGatewayRelayDeviceCheckBox);
                    textMessageGatewayRelayDeviceCheckBox.setBounds(15, 260,
                            460, 14);

                    // ---- textMessageGatewayRelayDeviceName ----
                    textMessageGatewayRelayDeviceName.setFont(UIManager
                            .getFont("defaultFont"));
                    panel8.add(textMessageGatewayRelayDeviceName);
                    textMessageGatewayRelayDeviceName.setBounds(15, 305, 455,
                            30);

                    // ---- label4 ----
                    label4.setText("Gateway Device Name (or specify @all for all gateways):");
                    label4.setFont(UIManager.getFont("defaultFont"));
                    panel8.add(label4);
                    label4.setBounds(20, 285, 450, 20);
                }
                panel3.add(panel8);
                panel8.setBounds(5, 60, 485, 350);

                // ---- enableExtraLogFieldsCheckBox ----
                enableExtraLogFieldsCheckBox
                        .setText("Display extra fields on message log");
                enableExtraLogFieldsCheckBox.setFont(UIManager
                        .getFont("defaultFont"));
                panel3.add(enableExtraLogFieldsCheckBox);
                enableExtraLogFieldsCheckBox.setBounds(10, 30, 435, 20);
            }
            preferencesTabbedPane.addTab("Text Options", panel3);

            // ======== panel2 ========
            {
                panel2.setBorder(null);
                panel2.setFont(UIManager.getFont("defaultFont"));
                panel2.setLayout(null);

                // ======== panel5 ========
                {
                    panel5.setBorder(new TitledBorder("Text To Speech Reader"));
                    panel5.setLayout(null);

                    // ---- nameReadingNoneRadioButton ----
                    nameReadingNoneRadioButton.setText("None");
                    nameReadingNoneRadioButton.setFont(UIManager
                            .getFont("defaultFont"));
                    panel5.add(nameReadingNoneRadioButton);
                    nameReadingNoneRadioButton.setBounds(10, 20, 320, 18);

                    // ---- nameReadingDefaultRadioButton ----
                    nameReadingDefaultRadioButton.setText("System Default");
                    nameReadingDefaultRadioButton.setSelected(true);
                    nameReadingDefaultRadioButton.setFont(UIManager
                            .getFont("defaultFont"));
                    panel5.add(nameReadingDefaultRadioButton);
                    nameReadingDefaultRadioButton.setBounds(10, 45, 320, 18);

                    // ---- nameReadingThirdPartyRadioButton ----
                    nameReadingThirdPartyRadioButton.setText("3rd Party");
                    nameReadingThirdPartyRadioButton.setFont(UIManager
                            .getFont("defaultFont"));
                    panel5.add(nameReadingThirdPartyRadioButton);
                    nameReadingThirdPartyRadioButton.setBounds(10, 70, 85, 18);

                    // ---- thirdPartyNameReadTextField ----
                    thirdPartyNameReadTextField.setFont(UIManager
                            .getFont("defaultFont"));
                    thirdPartyNameReadTextField.setEnabled(false);
                    panel5.add(thirdPartyNameReadTextField);
                    thirdPartyNameReadTextField.setBounds(95, 65, 375, 30);

                    // ---- testSoundButton ----
                    testSoundButton.setText("Test");
                    panel5.add(testSoundButton);
                    testSoundButton.setBounds(195, 120, 105, 28);

                    // ---- label11 ----
                    label11.setText("Variable substitution: $NAME will read the name.");
                    label11.setFont(UIManager.getFont("defaultFont"));
                    panel5.add(label11);
                    label11.setBounds(90, 100, 365, 14);
                }
                panel2.add(panel5);
                panel5.setBounds(5, 180, 485, 160);

                // ======== panel7 ========
                {
                    panel7.setBorder(new TitledBorder("Ringtone Options"));
                    panel7.setLayout(null);

                    // ---- label9 ----
                    label9.setText("Audio Mixer");
                    label9.setFont(UIManager.getFont("defaultFont"));
                    panel7.add(label9);
                    label9.setBounds(20, 20, 150, 16);

                    // ---- audioMixerComboBox ----
                    audioMixerComboBox
                            .setFont(UIManager.getFont("defaultFont"));
                    panel7.add(audioMixerComboBox);
                    audioMixerComboBox.setBounds(15, 35, 460, 25);

                    // ---- ringtoneComboBox ----
                    ringtoneComboBox.setFont(UIManager.getFont("defaultFont"));
                    panel7.add(ringtoneComboBox);
                    ringtoneComboBox.setBounds(15, 80, 290, 25);

                    // ---- playButton ----
                    playButton.setText("Play");
                    panel7.add(playButton);
                    playButton.setBounds(310, 80, 65, 28);

                    // ---- browseButton ----
                    browseButton.setText("Browse...");
                    panel7.add(browseButton);
                    browseButton.setBounds(380, 80, 90, 28);

                    // ---- label14 ----
                    label14.setText("Ringtone");
                    label14.setFont(UIManager.getFont("defaultFont"));
                    panel7.add(label14);
                    label14.setBounds(20, 65, 150, 16);
                }
                panel2.add(panel7);
                panel7.setBounds(5, 10, 485, 120);

                // ---- readIncomingCallerNamesCheckBox ----
                readIncomingCallerNamesCheckBox
                        .setText("Read Incoming Caller Names");
                readIncomingCallerNamesCheckBox.setFont(UIManager
                        .getFont("defaultFont"));
                panel2.add(readIncomingCallerNamesCheckBox);
                readIncomingCallerNamesCheckBox.setBounds(10, 135, 270, 20);

                // ---- readIncomingMessagesCheckBox ----
                readIncomingMessagesCheckBox.setText("Read Incoming Messages");
                readIncomingMessagesCheckBox.setFont(UIManager
                        .getFont("defaultFont"));
                panel2.add(readIncomingMessagesCheckBox);
                readIncomingMessagesCheckBox.setBounds(280, 135, 225, 20);

                // ---- readOutgoingAndStatusCallerNamesCheckBox ----
                readOutgoingAndStatusCallerNamesCheckBox
                        .setText("Read Outgoing & Status Caller Names");
                readOutgoingAndStatusCallerNamesCheckBox.setFont(UIManager
                        .getFont("defaultFont"));
                panel2.add(readOutgoingAndStatusCallerNamesCheckBox);
                readOutgoingAndStatusCallerNamesCheckBox.setBounds(10, 155,
                        495, 20);
            }
            preferencesTabbedPane.addTab("Audio Options", panel2);
        }
        contentPane.add(preferencesTabbedPane);
        preferencesTabbedPane.setBounds(10, 10, 515, 445);

        contentPane.setPreferredSize(new Dimension(635, 500));
        setSize(635, 500);
        setLocationRelativeTo(getOwner());

        // ---- messageNotifySelectionButtonGroup ----
        messageNotifySelectionButtonGroup.add(messageNotifyDialogRadioButton);
        messageNotifySelectionButtonGroup
                .add(messageNotifySystemTrayRadioButton);
        messageNotifySelectionButtonGroup
                .add(messageNotifyThirdPartyRadioButton);

        // ---- messageFormatButtonGroup ----
        messageFormatButtonGroup.add(messageFormatPrettyPrintRadioButton);
        messageFormatButtonGroup.add(messageFormatCustomRadioButton);

        // ---- nameReadingButtonGroup ----
        nameReadingButtonGroup.add(nameReadingNoneRadioButton);
        nameReadingButtonGroup.add(nameReadingDefaultRadioButton);
        nameReadingButtonGroup.add(nameReadingThirdPartyRadioButton);
        // //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY
    // //GEN-BEGIN:variables
    protected JButton setButton;
    protected JButton cancelButton;
    protected JButton defaultsButton;
    protected JTabbedPane preferencesTabbedPane;
    private JPanel panel6;
    private JPanel panel9;
    protected JTextField serversTextField;
    private JLabel label3;
    private JLabel label2;
    private JLabel label1;
    protected JCheckBox requestCallLogFromServerCheckBox;
    private JPanel panel10;
    protected JCheckBox displayUnformattedDateTimeCheckBox;
    protected JCheckBox useEuropeanDateFormatCheckBox;
    protected JCheckBox useSystemAddressBookCheckBox;
    private JPanel panel17;
    protected JCheckBox notifyLowBatteryCheckBox;
    protected JCheckBox notifyFullChargeCheckBox;
    protected JSpinner notifyLowBatteryThresholdSpinner;
    private JPanel panel1;
    private JTabbedPane popupFieldsTabbedPane;
    private JPanel panel14;
    private JPanel panel12;
    protected JCheckBox dateCheckBox;
    protected JCheckBox typeCheckBox;
    protected JCheckBox nameCheckBox;
    protected JCheckBox timeCheckBox;
    protected JCheckBox lineCheckBox;
    protected JCheckBox numberCheckBox;
    protected JButton testCallsPopupButton;
    private JPanel panel16;
    protected JCheckBox thirdPartyNotifyCheckBox;
    protected JTextField thirdPartyCallNotifyTextField;
    private JLabel label10;
    private JLabel label17;
    private JLabel label18;
    protected JCheckBox displayIncomingCallPopupsCheckBox;
    protected JCheckBox displayOutgoingAndStatusCallPopupsCheckBox;
    private JPanel panel15;
    private JPanel panel4;
    protected JRadioButton messageNotifyDialogRadioButton;
    protected JRadioButton messageNotifySystemTrayRadioButton;
    protected JRadioButton messageNotifyThirdPartyRadioButton;
    protected JTextField thirdPartyMessageNotifyTextField;
    private JLabel label16;
    private JLabel label26;
    private JLabel label29;
    protected JButton testMessagesPopupButton;
    protected JCheckBox displayIncomingMessagePopupsCheckBox;
    private JPanel panel18;
    protected JCheckBox messageDateCheckBox;
    protected JCheckBox messageTypeCheckBox;
    protected JCheckBox messageFromToCheckBox;
    protected JCheckBox messageTimeCheckBox;
    protected JCheckBox messageTextCheckBox;
    protected JCheckBox messageDeviceCheckBox;
    protected JRadioButton messageFormatPrettyPrintRadioButton;
    protected JRadioButton messageFormatCustomRadioButton;
    private JPanel panel11;
    private JPanel panel13;
    private JLabel label19;
    protected JTextField phoneNumberFormatTextField;
    private JLabel label20;
    protected JTextField reverseLookupTextField;
    private JLabel label21;
    private JLabel label22;
    private JLabel label23;
    private JLabel label24;
    private JLabel label25;
    private JLabel label27;
    private JLabel label28;
    private JPanel panel3;
    protected JCheckBox enableSmartPhoneTextNotifyCheckBox;
    private JPanel panel8;
    protected JCheckBox textMessageUrlCheckBox;
    private JLabel label12;
    protected JTextField textMessageUrlTextField;
    protected JCheckBox textMessageExternalProgramCheckBox;
    protected JTextField textMessageExternalProgramTextField;
    private JLabel label13;
    private JLabel label15;
    protected JLabel exampleProgramsLabel;
    protected JCheckBox displayOutputFromExternalProgramCheckBox;
    protected JCheckBox textMessageGatewayRelayDeviceCheckBox;
    protected JTextField textMessageGatewayRelayDeviceName;
    private JLabel label4;
    protected JCheckBox enableExtraLogFieldsCheckBox;
    private JPanel panel2;
    private JPanel panel5;
    protected JRadioButton nameReadingNoneRadioButton;
    protected JRadioButton nameReadingDefaultRadioButton;
    protected JRadioButton nameReadingThirdPartyRadioButton;
    protected JTextField thirdPartyNameReadTextField;
    protected JButton testSoundButton;
    private JLabel label11;
    private JPanel panel7;
    private JLabel label9;
    protected JComboBox audioMixerComboBox;
    protected JComboBox ringtoneComboBox;
    protected JButton playButton;
    protected JButton browseButton;
    private JLabel label14;
    protected JCheckBox readIncomingCallerNamesCheckBox;
    protected JCheckBox readIncomingMessagesCheckBox;
    protected JCheckBox readOutgoingAndStatusCallerNamesCheckBox;
    protected ButtonGroup messageNotifySelectionButtonGroup;
    protected ButtonGroup messageFormatButtonGroup;
    protected ButtonGroup nameReadingButtonGroup;
    // JFormDesigner - End of variables declaration //GEN-END:variables
}
