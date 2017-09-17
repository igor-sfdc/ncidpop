/*******************************************************
 * Notification Utils
 *******************************************************/
package com.lenderman.ncidpop.utils;

import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.lenderman.ncidpop.compat.SystemTrayWrapper;
import com.lenderman.ncidpop.compat.TrayIconWrapper;
import com.lenderman.ncidpop.data.CallDisplayPrefsMapData;
import com.lenderman.ncidpop.data.MessageData;
import com.lenderman.ncidpop.jfd.MessageDialog;
import com.lenderman.ncidpop.preferences.jfd.NcidPopPreferences;

/**
 * Utilities to support notifications.
 * 
 * @author Chris Lenderman
 */
public class NotificationUtils
{
    /** A sample call array */
    public static final ArrayList<String> SAMPLE_CALL = new ArrayList<String>();
    static
    {
        SAMPLE_CALL.add("CID");
        SAMPLE_CALL.add("NAME");
        SAMPLE_CALL.add("TEST NAME");
        SAMPLE_CALL.add("NMBR");
        SAMPLE_CALL.add("1234567890");
        SAMPLE_CALL.add("DATE");
        SAMPLE_CALL.add("01022014");
        SAMPLE_CALL.add("TIME");
        SAMPLE_CALL.add("1234");
        SAMPLE_CALL.add("LINE");
        SAMPLE_CALL.add("LINE");
    }

    /** A sample message */
    public static final MessageData SAMPLE_MESSAGE = new MessageData(
            "NOT",
            "TEST MESSAGE ***DATE*01022014*TIME*1234*NAME*TEST NAME*NMBR*1234567890*LINE*DEVICE*MTYPE*IN*");

    /** Width of the dialog for wrapping characters */
    private static final int DIALOG_CHARACTER_WRAP = 100;

    /** Single threaded executor for popups */
    private static Executor popupExecutor = Executors.newSingleThreadExecutor();

    /** Single threaded executor for third party popups */
    private static Executor thirdPartyPopupExecutor = Executors
            .newSingleThreadScheduledExecutor();

    /** The tray icon */
    private static TrayIconWrapper trayIcon = null;

    /** The message box */
    private static MessageDialog messageBox = new MessageDialog();

    /** Static init block */
    static
    {
        if (TrayIconWrapper.trayIconSupported())
        {
            trayIcon = new TrayIconWrapper(
                    NcidConstants.ICON_NOT_CONNECTED.getImage());

            trayIcon.setImageAutoSize(true);
            SystemTrayWrapper.add(trayIcon);
        }
    }

    /** A test to see if an extra space delim should be applied to notifications */
    private static boolean isExtraSpaceDelim(PrefUtils prefUtils)
    {
        return (!prefUtils.getBoolean(PrefUtils.PREF_ENABLE_THIRD_PARTY_NOTIFY) && OsUtils
                .isMacOS());
    }

    /**
     * Adds a tray icon mouse listener
     * 
     * @param MouseListener
     */
    public static void addTrayIconMouseListener(MouseListener listener)
    {
        if (trayIcon != null)
        {
            trayIcon.addMouseListener(listener);
        }
    }

    /**
     * Sets the tray icon tip text
     * 
     * @param String
     */
    public static void setTrayIconTipText(String text)
    {
        if (trayIcon != null)
        {
            trayIcon.setToolTip(text);
        }
    }

    /**
     * Sets the tray icon popup menu
     * 
     * @param PopupMenu
     */
    public static void setTrayIconPopupMenu(PopupMenu menu)
    {
        trayIcon.setPopupMenu(menu);
    }

    /**
     * Sets the tray icon image
     * 
     * @param Image
     */
    public static void setTrayIconImage(Image image)
    {
        if (trayIcon != null)
        {
            trayIcon.setImage(image);
        }
    }

    /**
     * Enqueues a dialog for display by the popup executor
     * 
     * @param Runnable
     */
    public static void enqueueDialog(Runnable runnable)
    {
        popupExecutor.execute(runnable);
    }

    /**
     * Displays a "plain dialog" popup message
     * 
     * @param String the message to display
     * @param boolean whether or not to wrap the text
     */
    public static void showDialogPopupMessage(final String message,
            final boolean wrapText)
    {
        enqueueDialog(new Runnable()
        {
            /** @inheritDoc */
            public void run()
            {
                NotificationUtils.messageTextDialog(message, wrapText);
            }
        });
    }

    /**
     * Shows the messages popup
     * 
     * @param the message data to display
     * @param PrefUtils the pref utils instance associated with the popup
     */
    public static void showMessagesPopup(final MessageData messageData,
            final PrefUtils prefUtils)
    {
        boolean notificationDisplayed = false;

        String messageDisplaySelection = prefUtils
                .getString(PrefUtils.PREF_MESSAGE_POPUP_DISPLAY_SELECTION);

        final String text;
        final String alias = OsUtils.determineAddressBookAlias(
                messageData.name, messageData.number);

        if (prefUtils
                .getString(PrefUtils.PREF_MESSAGE_FORMAT_OPTIONS_SELECTION)
                .equals(NcidPopPreferences.MESSAGE_FORMAT_PRETTY_PRINT_LABEL))
        {
            text = messageData.encodePrettyPrintWithAlias(prefUtils, true,
                    alias);
        }
        else
        {
            text = messageData.encodeCustom(prefUtils, alias);
        }

        if (messageDisplaySelection == null)
        {
            return;
        }

        if (messageDisplaySelection
                .equals(NcidPopPreferences.MESSAGE_NOTIFY_THIRD_PARTY_LABEL))
        {
            thirdPartyPopupExecutor.execute(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        final String imageFileName = OsUtils
                                .getAddressBookImage(messageData.number);

                        HashMap<String, String> substitutions = new HashMap<String, String>();
                        substitutions.put("$MESSAGE", text);
                        if (imageFileName != null)
                        {
                            substitutions.put("$IMAGE", imageFileName);
                        }

                        OsUtils.executeCommandLineCommand(
                                prefUtils
                                        .getString(PrefUtils.PREF_THIRD_PARTY_MESSAGE_NOTIFIER_STRING),
                                substitutions, true);

                        if (imageFileName != null)
                        {
                            new File(imageFileName).delete();
                        }
                    }
                    catch (final Exception e)
                    {
                        enqueueDialog(new Runnable()
                        {
                            public void run()
                            {
                                messageTextDialog(
                                        " Couldn't launch the third party notifier: "
                                                + e.toString(), true);
                            }
                        });
                    }
                }
            });
            notificationDisplayed = true;
        }
        else if (messageDisplaySelection
                .equals(NcidPopPreferences.MESSAGE_NOTIFY_SYSTEM_TRAY_LABEL)
                && (trayIcon != null))
        {
            notificationDisplayed = trayIcon.displayMessage("Message", text);
        }

        if (!notificationDisplayed)
        {
            showDialogPopupMessage(text, true);
        }
    }

    /**
     * Shows the calls/notifications popup
     * 
     * @param the text to display
     * @param the image name to display, can be null
     * @param PrefUtils the pref utils instance associated with the popup
     */
    public static void showCallsNotificationsPopup(final String text,
            final String imageFileName, final PrefUtils prefUtils)
    {
        boolean notificationDisplayed = false;

        if (prefUtils.getBoolean(PrefUtils.PREF_ENABLE_THIRD_PARTY_NOTIFY))
        {
            thirdPartyPopupExecutor.execute(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        HashMap<String, String> substitutions = new HashMap<String, String>();
                        substitutions.put("$POPUP", text);

                        if (imageFileName != null)
                        {
                            substitutions.put("$IMAGE", imageFileName);
                        }

                        OsUtils.executeCommandLineCommand(
                                prefUtils
                                        .getString(PrefUtils.PREF_THIRD_PARTY_NOTIFIER_STRING),
                                substitutions, true);

                        if (imageFileName != null)
                        {
                            new File(imageFileName).delete();
                        }
                    }
                    catch (final Exception e)
                    {
                        enqueueDialog(new Runnable()
                        {
                            public void run()
                            {
                                messageTextDialog(
                                        " Couldn't launch the third party notifier: "
                                                + e.toString(), true);
                            }
                        });
                    }
                }
            });
            notificationDisplayed = true;
        }
        else if (trayIcon != null)
        {
            notificationDisplayed = trayIcon
                    .displayMessage(isExtraSpaceDelim(prefUtils) ? " " : ""
                            + "Caller ID", text);
        }

        if (!notificationDisplayed)
        {
            showDialogPopupMessage("Caller ID:\n" + text, false);
        }
    }

    /**
     * Populates call display preferences in preparation for display
     * 
     * @param PrefUtils the pref utils instance associated with the popup
     * @return ArrayList<CallDisplayPrefsMapData>
     */
    private static ArrayList<CallDisplayPrefsMapData> populateCallDisplayPrefs(
            PrefUtils prefUtils)
    {
        ArrayList<CallDisplayPrefsMapData> callDisplayPrefsLookup = new ArrayList<CallDisplayPrefsMapData>();
        String extraSpaceAfterNewLine = isExtraSpaceDelim(prefUtils) ? " " : "";

        callDisplayPrefsLookup.clear();

        callDisplayPrefsLookup.add(new CallDisplayPrefsMapData(
                PrefUtils.PREF_TYPE_SELECTED, LogParserUtils.TYPE_KEY, " "));
        callDisplayPrefsLookup.add(new CallDisplayPrefsMapData(
                PrefUtils.PREF_DATE_SELECTED, LogParserUtils.DATE_KEY, " "));
        callDisplayPrefsLookup.add(new CallDisplayPrefsMapData(
                PrefUtils.PREF_TIME_SELECTED, LogParserUtils.TIME_KEY, " "));
        callDisplayPrefsLookup.add(new CallDisplayPrefsMapData(
                PrefUtils.PREF_NAME_SELECTED, LogParserUtils.NAME_KEY, "\n"
                        + extraSpaceAfterNewLine));
        callDisplayPrefsLookup.add(new CallDisplayPrefsMapData(
                PrefUtils.PREF_NUMBER_SELECTED, LogParserUtils.NUMBER_KEY, "\n"
                        + extraSpaceAfterNewLine));
        callDisplayPrefsLookup.add(new CallDisplayPrefsMapData(
                PrefUtils.PREF_LINE_SELECTED, LogParserUtils.LINE_KEY, "\n"
                        + extraSpaceAfterNewLine));

        return callDisplayPrefsLookup;
    }

    /**
     * Builds the call notification text
     * 
     * @param String list of tokenized strings
     * @param PrefUtils the pref utils instance associated with the popup
     */
    public static String buildCallNotificationText(ArrayList<String> strings,
            PrefUtils prefUtils)
    {
        String number = LogParserUtils.getValueForKey(strings,
                LogParserUtils.NUMBER_KEY);
        String formattedName = OsUtils
                .determineAddressBookAlias(LogParserUtils.getValueForKey(
                        strings, LogParserUtils.NAME_KEY), number);
        String formattedNumber = NumberFormatUtilities.formatNumber(number,
                prefUtils.getString(PrefUtils.PREF_PHONE_NUMBER_FORMAT));

        String date = LogParserUtils.getValueForKey(strings,
                LogParserUtils.DATE_KEY);

        String time = LogParserUtils.getValueForKey(strings,
                LogParserUtils.TIME_KEY);

        String formattedDate;

        String formattedTime;

        if (prefUtils.getBoolean(PrefUtils.PREF_USE_RAW_DATE_TIME))
        {
            formattedDate = date;
            formattedTime = time;
        }
        else
        {
            formattedDate = LogParserUtils.convertToDateTimeString(date, time,
                    LogParserUtils.getPreferredDateFormat());
            formattedTime = LogParserUtils.convertToDateTimeString(date, time,
                    LogParserUtils.TIME_FORMAT);
        }

        String formattedLine = LogParserUtils.formatLineString(LogParserUtils
                .getValueForKey(strings, LogParserUtils.LINE_KEY));

        ArrayList<CallDisplayPrefsMapData> callDisplayPrefsLookup = populateCallDisplayPrefs(prefUtils);

        String callDisplayText = "";
        for (CallDisplayPrefsMapData item : callDisplayPrefsLookup)
        {
            if (prefUtils.getBoolean(item.prefType))
            {
                String displayDelim = isExtraSpaceDelim(prefUtils) ? " " : "";
                if (callDisplayText.length() > 0)
                {
                    displayDelim = item.displayDelim;
                }

                if (item.dataDisplayKey.equals(LogParserUtils.NAME_KEY))
                {
                    callDisplayText += displayDelim + formattedName;
                }
                else if (item.dataDisplayKey.equals(LogParserUtils.NUMBER_KEY))
                {
                    callDisplayText += displayDelim + formattedNumber;
                }
                else if (item.dataDisplayKey.equals(LogParserUtils.DATE_KEY))
                {
                    callDisplayText += displayDelim + formattedDate;
                }
                else if (item.dataDisplayKey.equals(LogParserUtils.TIME_KEY))
                {
                    callDisplayText += displayDelim + formattedTime;
                }
                else if (item.dataDisplayKey.equals(LogParserUtils.LINE_KEY))
                {
                    if (formattedLine.length() > 0)
                    {
                        callDisplayText += displayDelim + formattedLine;
                    }
                }
                else
                {
                    callDisplayText += displayDelim
                            + LogParserUtils.getValueForKey(strings,
                                    item.dataDisplayKey);
                }
            }
        }
        return callDisplayText;
    }

    /**
     * Calculates the delimeter used for messages
     * 
     * @param int the length of the "base string" for formulating the message
     * @param boolean whether or not an extra space is desired for the delimeter
     * @param String the default delimeter used for this message segment
     * @return String
     */
    private static String calculateMessageDelimeter(int baseStringSize,
            boolean extraSpace, String delimeter)
    {
        if (baseStringSize == 0)
        {
            return extraSpace ? " " : "";
        }
        else
        {
            return delimeter;
        }
    }

    /**
     * Builds the message notification text
     * 
     * @param MessageData the message data object
     * @param PrefUtils the pref utils instance associated with the popup
     * @param String the from/to text field
     */
    public static String buildMessageNotificationText(MessageData messageData,
            PrefUtils prefUtils, String fromTo)
    {
        String formattedFromTo = NumberFormatUtilities.formatNumber(fromTo,
                prefUtils.getString(PrefUtils.PREF_PHONE_NUMBER_FORMAT));

        String formattedDate;

        String formattedTime;

        if (prefUtils.getBoolean(PrefUtils.PREF_USE_RAW_DATE_TIME))
        {
            formattedDate = messageData.date;
            formattedTime = messageData.time;
        }
        else
        {
            formattedDate = LogParserUtils.convertToDateTimeString(
                    messageData.date, messageData.time,
                    LogParserUtils.getPreferredDateFormat());
            formattedTime = LogParserUtils.convertToDateTimeString(
                    messageData.date, messageData.time,
                    LogParserUtils.TIME_FORMAT);
        }

        String messageDisplayText = "";
        boolean extraSpace = isExtraSpaceDelim(prefUtils);

        if (prefUtils.getBoolean(PrefUtils.PREF_MESSAGE_TYPE_SELECTED))
        {
            messageDisplayText += calculateMessageDelimeter(
                    messageDisplayText.length(), extraSpace, " ")
                    + messageData.lineType;

            if (messageData.msgType.length() > 0)
            {
                messageDisplayText += calculateMessageDelimeter(
                        messageDisplayText.length(), extraSpace, " ")
                        + "("
                        + messageData.msgType + ")";
            }
        }

        if (prefUtils.getBoolean(PrefUtils.PREF_MESSAGE_DATE_SELECTED))
        {
            messageDisplayText += calculateMessageDelimeter(
                    messageDisplayText.length(), extraSpace, " ")
                    + formattedDate;
        }

        if (prefUtils.getBoolean(PrefUtils.PREF_MESSAGE_TIME_SELECTED))
        {
            messageDisplayText += calculateMessageDelimeter(
                    messageDisplayText.length(), extraSpace, " ")
                    + formattedTime;
        }

        if (prefUtils.getBoolean(PrefUtils.PREF_MESSAGE_DEVICE_SELECTED))
        {
            messageDisplayText += calculateMessageDelimeter(
                    messageDisplayText.length(), extraSpace, "\n")
                    + messageData.device;
        }

        if (prefUtils.getBoolean(PrefUtils.PREF_MESSAGE_FROM_TO_SELECTED)
                && (fromTo.length() > 0))
        {
            messageDisplayText += calculateMessageDelimeter(
                    messageDisplayText.length(), extraSpace, "\n")
                    + formattedFromTo;
        }

        if (prefUtils.getBoolean(PrefUtils.PREF_MESSAGE_TEXT_SELECTED))
        {
            messageDisplayText += calculateMessageDelimeter(
                    messageDisplayText.length(), extraSpace, "\n")
                    + messageData.message;
        }
        return messageDisplayText;
    }

    /**
     * Creates a message text dialog
     * 
     * @param String the message
     * @param boolean whether or not to wrap text
     */
    private static void messageTextDialog(String message, boolean wrapText)
    {
        StringBuilder builder = new StringBuilder(message);
        if (wrapText)
        {
            int i = 0;
            while (((i + DIALOG_CHARACTER_WRAP) < builder.length())
                    && ((i = builder
                            .lastIndexOf(" ", i + DIALOG_CHARACTER_WRAP)) != -1))
            {
                builder.replace(i, i + 1, "\n");
            }
        }

        messageBox.infoLabel.setText(builder.toString());
        messageBox.infoLabel.invalidate();
        messageBox.pack();
        messageBox.setLocationRelativeTo(null);
        messageBox.infoLabel.setEditable(false);
        messageBox.infoLabel.setHighlighter(null);
        messageBox.setModal(true);
        messageBox.setVisible(true);
    }
}
