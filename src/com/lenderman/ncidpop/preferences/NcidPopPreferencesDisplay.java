/*******************************************************
 * NCID Pop Preferences Display
 *******************************************************/
package com.lenderman.ncidpop.preferences;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import com.lenderman.ncidpop.VersionNumber;
import com.lenderman.ncidpop.compat.DesktopWrapper;
import com.lenderman.ncidpop.compat.FileNameExtensionFilter;
import com.lenderman.ncidpop.compat.SystemTrayWrapper;
import com.lenderman.ncidpop.compat.TrayIconWrapper;
import com.lenderman.ncidpop.power.PowerManagerHelper;
import com.lenderman.ncidpop.preferences.jfd.NcidPopPreferences;
import com.lenderman.ncidpop.speech.TextToSpeechManager;
import com.lenderman.ncidpop.utils.ButtonGroupUtils;
import com.lenderman.ncidpop.utils.LogParserUtils;
import com.lenderman.ncidpop.utils.NotificationUtils;
import com.lenderman.ncidpop.utils.OsUtils;
import com.lenderman.ncidpop.utils.PrefUtils;
import com.lenderman.ncidpop.utils.SoundUtils;

/**
 * The NCID Pop Preferences display. Facilitates displaying and manipulating the
 * preferences.
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("all")
public class NcidPopPreferencesDisplay extends NcidPopPreferences
{
    /** Samples web page */
    public static final String NCIDPOP_SAMPLES_WEB_PAGE = "http://ncid.sourceforge.net/ncidpop/samples.html";

    /** A mapping between preferences and checkboxes */
    private HashMap<String, JCheckBox> prefToCheckBox = new HashMap<String, JCheckBox>();

    /** A mapping between preferences and text fields */
    private HashMap<String, JTextField> prefToTextField = new HashMap<String, JTextField>();

    /**
     * Temporary test preferences to support testing items such as the test 3rd
     * party notification
     */
    private PrefUtils testPrefs = new PrefUtils(null);

    /** Key listener to detect enter and click the "set" button */
    private KeyListener enterListener = new KeyAdapter()
    {
        /** @inheritDoc */
        @Override
        public void keyReleased(KeyEvent e)
        {
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
            {
                setButton.doClick();
            }
        }
    };

    /**
     * Initializes preference mappings for checkboxes and text fields to allow
     * for programmatic access to them for preferences manipulation
     */
    private void initializePreferencesMappings()
    {
        prefToCheckBox.put(PrefUtils.PREF_TYPE_SELECTED, typeCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_NAME_SELECTED, nameCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_NUMBER_SELECTED, numberCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_DATE_SELECTED, dateCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_TIME_SELECTED, timeCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_LINE_SELECTED, lineCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_MESSAGE_TYPE_SELECTED,
                messageTypeCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_MESSAGE_DATE_SELECTED,
                messageDateCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_MESSAGE_TIME_SELECTED,
                messageTimeCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_MESSAGE_DEVICE_SELECTED,
                messageDeviceCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_MESSAGE_FROM_TO_SELECTED,
                messageFromToCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_MESSAGE_TEXT_SELECTED,
                messageTextCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_ENABLE_SMART_PHONE_TEXT_NOTIFY,
                enableSmartPhoneTextNotifyCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_ENABLE_THIRD_PARTY_NOTIFY,
                thirdPartyNotifyCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_URL,
                textMessageUrlCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_EXTERNAL_PROGRAM,
                textMessageExternalProgramCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_GATEWAY_RELAY,
                textMessageGatewayRelayDeviceCheckBox);
        prefToCheckBox.put(
                PrefUtils.PREF_ENABLE_TEXT_MESSAGE_EXTERNAL_PROGRAM_OUTPUT,
                displayOutputFromExternalProgramCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_READ_MESSAGES,
                readIncomingMessagesCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_READ_INCOMING_CALLER_NAMES,
                readIncomingCallerNamesCheckBox);
        prefToCheckBox.put(
                PrefUtils.PREF_READ_OUTGOING_AND_STATUS_CALLER_NAMES,
                readOutgoingAndStatusCallerNamesCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_USE_RAW_DATE_TIME,
                displayUnformattedDateTimeCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_USE_EUROPEAN_DATE_TIME,
                useEuropeanDateFormatCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_DISPLAY_EXTRA_MESSAGE_LOG_FIELDS,
                enableExtraLogFieldsCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_DISPLAY_INCOMING_CALL_POPUPS,
                displayIncomingCallPopupsCheckBox);
        prefToCheckBox.put(
                PrefUtils.PREF_DISPLAY_OUTGOING_AND_STATUS_CALL_POPUPS,
                displayOutgoingAndStatusCallPopupsCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_DISPLAY_INCOMING_MESSAGE_POPUPS,
                displayIncomingMessagePopupsCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_ENABLE_BATTERY_CHARGED_MESSAGE,
                notifyFullChargeCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_ENABLE_LOW_BATTERY_MESSAGE,
                notifyLowBatteryCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_USE_SYSTEM_ADDRESS_BOOK,
                useSystemAddressBookCheckBox);
        prefToCheckBox.put(PrefUtils.PREF_REQUEST_CALL_LOG_FROM_SERVER,
                requestCallLogFromServerCheckBox);

        prefToTextField.put(PrefUtils.PREF_SERVERS, serversTextField);
        prefToTextField.put(PrefUtils.PREF_PHONE_NUMBER_FORMAT,
                phoneNumberFormatTextField);
        prefToTextField.put(PrefUtils.PREF_REVERSE_LOOKUP_URL,
                reverseLookupTextField);
        prefToTextField.put(PrefUtils.PREF_TEXT_MESSAGE_URL,
                textMessageUrlTextField);
        prefToTextField.put(PrefUtils.PREF_TEXT_MESSAGE_EXTERNAL_PROGRAM,
                textMessageExternalProgramTextField);
        prefToTextField.put(
                PrefUtils.PREF_TEXT_MESSAGE_GATEWAY_RELAY_DEVICE_NAME,
                textMessageGatewayRelayDeviceName);
        prefToTextField.put(PrefUtils.PREF_THIRD_PARTY_NOTIFIER_STRING,
                thirdPartyCallNotifyTextField);
        prefToTextField.put(PrefUtils.PREF_THIRD_PARTY_NAME_READING_STRING,
                thirdPartyNameReadTextField);
        prefToTextField.put(PrefUtils.PREF_THIRD_PARTY_MESSAGE_NOTIFIER_STRING,
                thirdPartyMessageNotifyTextField);
    }

    /**
     * Initializes controls based on current preference settings
     * 
     * @param PrefUtils the pref utils to use to initialize
     */
    private void initializeControls(PrefUtils prefUtils)
    {
        ringtoneComboBox.removeAllItems();
        audioMixerComboBox.removeAllItems();

        // Initialize ringtone combo box
        ringtoneComboBox.addItem("(none)");

        for (String file : PrefUtils.relativeWavFileToLocationMap.keySet())
        {
            ringtoneComboBox.addItem(file);
        }

        DefaultComboBoxModel model = (DefaultComboBoxModel) ringtoneComboBox
                .getModel();
        if (model.getIndexOf(prefUtils.getString(PrefUtils.PREF_RINGTONE)) == -1)
        {
            ringtoneComboBox.addItem(prefUtils
                    .getString(PrefUtils.PREF_RINGTONE));
        }

        ringtoneComboBox.setSelectedItem(prefUtils
                .getString(PrefUtils.PREF_RINGTONE));

        // Initialize mixer combo box
        audioMixerComboBox.addItem(PrefUtils.DEFAULT_MIXER_NAME);

        for (String mixer : SoundUtils.getLineMixerNames())
        {
            audioMixerComboBox.addItem(mixer);
        }

        model = (DefaultComboBoxModel) audioMixerComboBox.getModel();
        if (model.getIndexOf(prefUtils.getString(PrefUtils.PREF_MIXER_NAME)) == -1)
        {
            audioMixerComboBox.setSelectedItem(PrefUtils.DEFAULT_MIXER_NAME);
        }
        else
        {
            audioMixerComboBox.setSelectedItem(prefUtils
                    .getString(PrefUtils.PREF_MIXER_NAME));
        }

        ButtonGroupUtils
                .setButtonSelectedWithLabel(
                        nameReadingButtonGroup,
                        prefUtils
                                .getString(PrefUtils.PREF_THIRD_PARTY_NAME_READING_SELECTION),
                        PrefUtils.DEFAULT_NAME_READING_SELECTION());

        ButtonGroupUtils
                .setButtonSelectedWithLabel(
                        messageNotifySelectionButtonGroup,
                        prefUtils
                                .getString(PrefUtils.PREF_MESSAGE_POPUP_DISPLAY_SELECTION),
                        NcidPopPreferences.MESSAGE_NOTIFY_DIALOG_LABEL);

        ButtonGroupUtils
                .setButtonSelectedWithLabel(
                        messageFormatButtonGroup,
                        prefUtils
                                .getString(PrefUtils.PREF_MESSAGE_FORMAT_OPTIONS_SELECTION),
                        NcidPopPreferences.MESSAGE_FORMAT_PRETTY_PRINT_LABEL);

        // Initialize preference checkboxes
        for (String pref : prefToCheckBox.keySet())
        {
            prefToCheckBox.get(pref).setSelected(prefUtils.getBoolean(pref));
        }

        // Initialize preference textfields
        for (String pref : prefToTextField.keySet())
        {
            prefToTextField.get(pref).setText(prefUtils.getString(pref));
        }

        // Initialize preference spinners
        notifyLowBatteryThresholdSpinner.setValue(prefUtils
                .getInteger(PrefUtils.PREF_LOW_BATTERY_MESSAGE_THRESHOLD));

        notifyLowBatteryThresholdSpinner.setEditor(new JSpinner.NumberEditor(
                notifyLowBatteryThresholdSpinner, "0'%'"));

        assessWidgetEnabledState();
    }

    /**
     * Saves preferences
     * 
     * @param PrefUtils the PrefUtils instance to use
     */
    private void savePrefs(PrefUtils prefUtils)
    {
        for (String pref : prefToCheckBox.keySet())
        {
            prefUtils.setBoolean(pref, prefToCheckBox.get(pref).isSelected());
        }

        for (String pref : prefToTextField.keySet())
        {
            prefUtils.setString(pref, prefToTextField.get(pref).getText());
        }

        prefUtils.setString(PrefUtils.PREF_THIRD_PARTY_NAME_READING_SELECTION,
                ButtonGroupUtils.getSelectedButtonText(nameReadingButtonGroup));

        prefUtils
                .setString(
                        PrefUtils.PREF_MESSAGE_POPUP_DISPLAY_SELECTION,
                        ButtonGroupUtils
                                .getSelectedButtonText(messageNotifySelectionButtonGroup));

        prefUtils.setString(PrefUtils.PREF_MESSAGE_FORMAT_OPTIONS_SELECTION,
                ButtonGroupUtils
                        .getSelectedButtonText(messageFormatButtonGroup));

        prefUtils.setString(PrefUtils.PREF_RINGTONE,
                (String) ringtoneComboBox.getSelectedItem());

        prefUtils.setString(PrefUtils.PREF_MIXER_NAME,
                (String) audioMixerComboBox.getSelectedItem());

        prefUtils.setInteger(PrefUtils.PREF_LOW_BATTERY_MESSAGE_THRESHOLD,
                (Integer) notifyLowBatteryThresholdSpinner.getValue());

        // Saving settings, so update the version compliance preference.
        prefUtils.setString(PrefUtils.PREF_VERSION_COMPATIBILITY,
                VersionNumber.NCIDPOP_PREFS_COMPATIBILITY_VERSION_NUMBER);
    }

    /** @inheritDoc */
    @Override
    public void setVisible(boolean visible)
    {
        if (visible)
        {
            initializeControls(PrefUtils.instance);
        }
        super.setVisible(visible);
    }

    /**
     * Constructor
     */
    public NcidPopPreferencesDisplay()
    {
        nameReadingDefaultRadioButton.setEnabled(TextToSpeechManager
                .isSupported());

        messageNotifySystemTrayRadioButton.setEnabled(SystemTrayWrapper
                .isSupported() && TrayIconWrapper.trayIconSupported());

        notifyFullChargeCheckBox.setEnabled(PowerManagerHelper
                .isBatteryPowerSupported());

        notifyLowBatteryCheckBox.setEnabled(PowerManagerHelper
                .isBatteryPowerSupported());

        useSystemAddressBookCheckBox.setEnabled(OsUtils
                .isSystemAddressBookSupported());

        initializePreferencesMappings();

        KeyAdapter assessEnableStateKeyListener = new KeyAdapter()
        {
            /** @inheritDoc */
            @Override
            public void keyReleased(KeyEvent e)
            {
                assessWidgetEnabledState();
            }
        };

        ActionListener assessEnableStateListener = new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                assessWidgetEnabledState();
            }
        };

        // Add action and key listeners
        thirdPartyCallNotifyTextField
                .addKeyListener(assessEnableStateKeyListener);

        thirdPartyMessageNotifyTextField
                .addKeyListener(assessEnableStateKeyListener);

        exampleProgramsLabel.addMouseListener(new MouseAdapter()
        {
            /** @inheritDoc */
            @Override
            public void mouseClicked(MouseEvent e)
            {
                DesktopWrapper.launchWebsite(NCIDPOP_SAMPLES_WEB_PAGE);
            }
        });

        testCallsPopupButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                savePrefs(testPrefs);
                NotificationUtils.showCallsNotificationsPopup(NotificationUtils
                        .buildCallNotificationText(
                                NotificationUtils.SAMPLE_CALL, testPrefs),
                        OsUtils.getAddressBookImage(LogParserUtils
                                .getValueForKey(NotificationUtils.SAMPLE_CALL,
                                        LogParserUtils.NUMBER_KEY)), testPrefs);
            }
        });

        testMessagesPopupButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                savePrefs(testPrefs);
                NotificationUtils.showMessagesPopup(
                        NotificationUtils.SAMPLE_MESSAGE, testPrefs);
            }
        });

        defaultsButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent event)
            {
                for (String pref : prefToCheckBox.keySet())
                {
                    prefToCheckBox.get(pref).setSelected(
                            PrefUtils.getDefaultBoolean(pref));
                }

                for (String pref : prefToTextField.keySet())
                {
                    prefToTextField.get(pref).setText(
                            PrefUtils.getDefaultString(pref));
                }

                ringtoneComboBox.setSelectedItem(PrefUtils.DEFAULT_RINGTONE);

                audioMixerComboBox
                        .setSelectedItem(PrefUtils.DEFAULT_MIXER_NAME);

                ButtonGroupUtils.setButtonSelectedWithLabel(
                        nameReadingButtonGroup,
                        PrefUtils.DEFAULT_NAME_READING_SELECTION(),
                        PrefUtils.DEFAULT_NAME_READING_SELECTION());

                ButtonGroupUtils.setButtonSelectedWithLabel(
                        messageNotifySelectionButtonGroup,
                        NcidPopPreferences.MESSAGE_NOTIFY_DIALOG_LABEL,
                        NcidPopPreferences.MESSAGE_NOTIFY_DIALOG_LABEL);

                ButtonGroupUtils.setButtonSelectedWithLabel(
                        messageFormatButtonGroup,
                        NcidPopPreferences.MESSAGE_FORMAT_PRETTY_PRINT_LABEL,
                        NcidPopPreferences.MESSAGE_FORMAT_PRETTY_PRINT_LABEL);

                notifyLowBatteryThresholdSpinner.setValue(PrefUtils
                        .getDefaultInteger(PrefUtils.PREF_LOW_BATTERY_MESSAGE_THRESHOLD));

                assessWidgetEnabledState();
            }
        });

        cancelButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent event)
            {
                setVisible(false);
            }
        });

        setButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent event)
            {
                savePrefs(PrefUtils.instance);
                setVisible(false);
            }
        });

        playButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                savePrefs(testPrefs);
                SoundUtils.playClip(PrefUtils
                        .getRingtonePathFor((String) ringtoneComboBox
                                .getSelectedItem()), testPrefs);
            }
        });

        browseButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                final JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter(
                        "Wav files(*.wav)", "wav"));
                fc.showOpenDialog(NcidPopPreferencesDisplay.this);
                File file = fc.getSelectedFile();

                if (file != null)
                {
                    DefaultComboBoxModel model = (DefaultComboBoxModel) ringtoneComboBox
                            .getModel();
                    if (model.getIndexOf(fc.getSelectedFile().getName()) == -1)
                    {
                        ringtoneComboBox.addItem(file.getAbsolutePath());
                        ringtoneComboBox.setSelectedItem(file.getAbsolutePath());
                    }
                }
            }
        });

        ActionListener textMessageCheckBoxListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                if (!event.getSource().equals(
                        textMessageExternalProgramCheckBox)
                        && textMessageExternalProgramCheckBox.isSelected())
                {
                    textMessageExternalProgramCheckBox.setSelected(false);
                }
                if (!event.getSource().equals(textMessageUrlCheckBox)
                        && textMessageUrlCheckBox.isSelected())
                {
                    textMessageUrlCheckBox.setSelected(false);
                }

                if (!event.getSource().equals(
                        textMessageGatewayRelayDeviceCheckBox)
                        && textMessageGatewayRelayDeviceCheckBox.isSelected())
                {
                    textMessageGatewayRelayDeviceCheckBox.setSelected(false);
                }
                assessWidgetEnabledState();
            }
        };

        textMessageExternalProgramCheckBox
                .addActionListener(textMessageCheckBoxListener);
        textMessageUrlCheckBox.addActionListener(textMessageCheckBoxListener);
        textMessageGatewayRelayDeviceCheckBox
                .addActionListener(textMessageCheckBoxListener);

        serversTextField.addKeyListener(enterListener);
        reverseLookupTextField.addKeyListener(enterListener);
        phoneNumberFormatTextField.addKeyListener(enterListener);
        thirdPartyNotifyCheckBox.addKeyListener(enterListener);

        thirdPartyNotifyCheckBox.addActionListener(assessEnableStateListener);
        nameReadingThirdPartyRadioButton
                .addActionListener(assessEnableStateListener);
        nameReadingDefaultRadioButton
                .addActionListener(assessEnableStateListener);
        nameReadingNoneRadioButton.addActionListener(assessEnableStateListener);

        messageNotifyDialogRadioButton
                .addActionListener(assessEnableStateListener);
        messageNotifySystemTrayRadioButton
                .addActionListener(assessEnableStateListener);
        messageNotifyThirdPartyRadioButton
                .addActionListener(assessEnableStateListener);

        messageFormatPrettyPrintRadioButton
                .addActionListener(assessEnableStateListener);
        messageFormatCustomRadioButton
                .addActionListener(assessEnableStateListener);
        thirdPartyNameReadTextField
                .addKeyListener(assessEnableStateKeyListener);
        textMessageExternalProgramCheckBox
                .addActionListener(assessEnableStateListener);
        textMessageUrlCheckBox.addActionListener(assessEnableStateListener);
        notifyLowBatteryCheckBox.addActionListener(assessEnableStateListener);

        testSoundButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                savePrefs(testPrefs);
                SoundUtils.sayIt("Sound Test", testPrefs);
            }
        });
    }

    /**
     * Determines if widgets should be enabled or not
     */
    private void assessWidgetEnabledState()
    {
        notifyLowBatteryThresholdSpinner.setEnabled(notifyLowBatteryCheckBox
                .isSelected());

        textMessageExternalProgramTextField
                .setEnabled(textMessageExternalProgramCheckBox.isSelected());

        displayOutputFromExternalProgramCheckBox
                .setEnabled(textMessageExternalProgramCheckBox.isSelected());

        textMessageUrlTextField.setEnabled(textMessageUrlCheckBox.isSelected());

        textMessageGatewayRelayDeviceName
                .setEnabled(textMessageGatewayRelayDeviceCheckBox.isSelected());

        thirdPartyCallNotifyTextField.setEnabled(thirdPartyNotifyCheckBox
                .isSelected());

        thirdPartyMessageNotifyTextField
                .setEnabled(messageNotifyThirdPartyRadioButton.isSelected());

        testCallsPopupButton.setEnabled(!thirdPartyNotifyCheckBox.isSelected()
                || (thirdPartyCallNotifyTextField.getText().length() > 0));

        testMessagesPopupButton.setEnabled(!messageNotifyThirdPartyRadioButton
                .isSelected()
                || (thirdPartyMessageNotifyTextField.getText().length() > 0));

        thirdPartyNameReadTextField.setEnabled(nameReadingThirdPartyRadioButton
                .isSelected());
        testSoundButton
                .setEnabled((!nameReadingNoneRadioButton.isSelected() && (nameReadingDefaultRadioButton
                        .isSelected() && nameReadingDefaultRadioButton
                        .isEnabled()))
                        || (nameReadingThirdPartyRadioButton.isSelected() && (thirdPartyNameReadTextField
                                .getText().length() > 0)));

        messageTypeCheckBox.setEnabled(messageFormatCustomRadioButton
                .isSelected());
        messageDateCheckBox.setEnabled(messageFormatCustomRadioButton
                .isSelected());
        messageTimeCheckBox.setEnabled(messageFormatCustomRadioButton
                .isSelected());
        messageDeviceCheckBox.setEnabled(messageFormatCustomRadioButton
                .isSelected());
        messageFromToCheckBox.setEnabled(messageFormatCustomRadioButton
                .isSelected());
        messageTextCheckBox.setEnabled(messageFormatCustomRadioButton
                .isSelected());
    }
}