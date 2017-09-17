/*******************************************************
 * NCID Pop Main Display
 *******************************************************/
package com.lenderman.ncidpop;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.MenuItem;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import com.lenderman.ncidpop.about.NcidPopAboutDisplay;
import com.lenderman.ncidpop.compat.CallbackCapableTableSorter;
import com.lenderman.ncidpop.compat.CallbackCapableTableSorter.SortingStatusChangeListener;
import com.lenderman.ncidpop.compat.DesktopWrapper;
import com.lenderman.ncidpop.compat.SystemTrayWrapper;
import com.lenderman.ncidpop.compat.TableSorter;
import com.lenderman.ncidpop.compat.TableSorter.SortableHeaderRenderer;
import com.lenderman.ncidpop.compat.TrayIconWrapper;
import com.lenderman.ncidpop.data.LogDateTimeData;
import com.lenderman.ncidpop.data.LookupCandidate;
import com.lenderman.ncidpop.data.MessageData;
import com.lenderman.ncidpop.data.ServerData;
import com.lenderman.ncidpop.data.ServerDataManager;
import com.lenderman.ncidpop.jfd.NcidPopMain;
import com.lenderman.ncidpop.listmgmt.NcidPopAliasEditDisplay;
import com.lenderman.ncidpop.listmgmt.NcidPopWhiteBlackEditDisplay;
import com.lenderman.ncidpop.listmgmt.ServerRequestManager;
import com.lenderman.ncidpop.listmgmt.ServerRequestManager.InfoListType;
import com.lenderman.ncidpop.listmgmt.ServerRequestManager.ListRequestChangeType;
import com.lenderman.ncidpop.listmgmt.ServerRequestManager.ListType;
import com.lenderman.ncidpop.listmgmt.ServerRequestManager.ServerRequestType;
import com.lenderman.ncidpop.message.ServerMessageInputDisplay;
import com.lenderman.ncidpop.preferences.NcidPopPreferencesDisplay;
import com.lenderman.ncidpop.relay.RelayInputDisplay;
import com.lenderman.ncidpop.renderer.CustomTableCellRenderer;
import com.lenderman.ncidpop.renderer.TextAreaCellRenderer;
import com.lenderman.ncidpop.sms.SmsInputDisplay;
import com.lenderman.ncidpop.utils.DataDumpUtils;
import com.lenderman.ncidpop.utils.DataLookupUtils;
import com.lenderman.ncidpop.utils.LogParserUtils;
import com.lenderman.ncidpop.utils.NcidConstants;
import com.lenderman.ncidpop.utils.NotificationUtils;
import com.lenderman.ncidpop.utils.NumberFormatUtilities;
import com.lenderman.ncidpop.utils.OsUtils;
import com.lenderman.ncidpop.utils.PrefUtils;
import com.lenderman.ncidpop.utils.ServerCapabilityAssessmentUtils;
import com.lenderman.ncidpop.utils.ServerCapabilityAssessmentUtils.ServerCapability;
import com.lenderman.ncidpop.utils.TablePrefSizeHelper;
import com.lenderman.ncidpop.utils.VersionNumberUtils;

/**
 * The NCID Pop Main Display
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class NcidPopMainDisplay extends NcidPopMain
{
    /** The column in the call table which holds the type */
    public static final int CALL_TYPE_COL = 0;

    /** The column in the call table which holds the date */
    public static final int CALL_DATE_COL = 1;

    /** The column in the call table which holds the line */
    public static final int CALL_LINE_COL = 2;

    /** The column in the call table which holds the phone number */
    public static final int CALL_NUMBER_COL = 3;

    /** The column in the call table which holds the name */
    public static final int CALL_NAME_COL = 4;

    /** The column in the message table which holds the type */
    public static final int MESSAGE_TYPE_COL = 0;

    /** The column in the "plain" message table which holds the text */
    public static final int MESSAGE_TEXT_NO_EXTRA_COL = 1;

    /** The column in the "plain" message table which holds the phone number */
    public static final int MESSAGE_NUMBER_NO_EXTRA_COL = 2;

    /** The column in the "extra" message table which holds the date */
    public static final int MESSAGE_DATE_EXTRA_COL = 1;

    /** The column in the "extra" message table which holds the device */
    public static final int MESSAGE_DEVICE_EXTRA_COL = 2;

    /** The column in the "extra" message table which holds the from/to */
    public static final int MESSAGE_FROM_TO_EXTRA_COL = 3;

    /** The column in the "extra" message which holds the text */
    public static final int MESSAGE_TEXT_EXTRA_COL = 4;

    /** The column in the "extra" message table which holds the phone number */
    public static final int MESSAGE_NUMBER_EXTRA_COL = 5;

    /** Static lookup mapping call column numbers to preferences */
    public static HashMap<Integer, String> CALL_PREF_LOOKUP = new HashMap<Integer, String>();

    /** Static lookup mapping message column numbers to preferences */
    public static HashMap<Integer, String> MSG_PREF_EXTRA_COL_LOOKUP = new HashMap<Integer, String>();

    /** Static lookup mapping message column numbers to preferences */
    public static HashMap<Integer, String> MSG_PREF_NO_EXTRA_COL_LOOKUP = new HashMap<Integer, String>();

    static
    {
        CALL_PREF_LOOKUP.put(CALL_TYPE_COL, PrefUtils.PREF_CALL_TYPE_COL_WIDTH);
        CALL_PREF_LOOKUP.put(CALL_DATE_COL,
                PrefUtils.PREF_CALL_DATE_TIME_COL_WIDTH);
        CALL_PREF_LOOKUP.put(CALL_LINE_COL, PrefUtils.PREF_CALL_LINE_COL_WIDTH);
        CALL_PREF_LOOKUP.put(CALL_NUMBER_COL,
                PrefUtils.PREF_CALL_NUMBER_COL_WIDTH);

        MSG_PREF_NO_EXTRA_COL_LOOKUP.put(MESSAGE_TYPE_COL,
                PrefUtils.PREF_MSG_TYPE_COL_WIDTH);

        MSG_PREF_EXTRA_COL_LOOKUP.put(MESSAGE_TYPE_COL,
                PrefUtils.PREF_MSG_TYPE_COL_WIDTH);
        MSG_PREF_EXTRA_COL_LOOKUP.put(MESSAGE_DATE_EXTRA_COL,
                PrefUtils.PREF_MSG_DATE_TIME_COL_WIDTH);
        MSG_PREF_EXTRA_COL_LOOKUP.put(MESSAGE_DEVICE_EXTRA_COL,
                PrefUtils.PREF_MSG_DEVICE_COL_WIDTH);
        MSG_PREF_EXTRA_COL_LOOKUP.put(MESSAGE_FROM_TO_EXTRA_COL,
                PrefUtils.PREF_MSG_FROM_TO_COL_WIDTH);
    }

    /** The about display */
    private NcidPopAboutDisplay aboutDisplay = null;

    /** The preferences display */
    private NcidPopPreferencesDisplay prefDisplay = null;

    /** The SMS input display */
    private SmsInputDisplay smsInput = null;

    /** The Server Message input display */
    private ServerMessageInputDisplay messageInput = null;

    /** The Relay input display */
    private RelayInputDisplay relayInput = null;

    /** The SMS popup menu item. We save off a reference to allow enable/disable */
    private MenuItem smsPopupMenu = new MenuItem("Send SMS Text Message");

    /**
     * The refresh popup menu item. We save off a reference to allow
     * enable/disable
     */
    private MenuItem refreshPopupMenu = new MenuItem("Refresh");

    /**
     * The reload server list popup menu item. We save off a reference to allow
     * enable/disable
     */
    private MenuItem reloadServerListPopupMenu = new MenuItem(
            "Reload Server Lists");

    /** The popup menu for the messages table */
    private JPopupMenu messageMenu = new JPopupMenu();

    /** The message menu popup option for copying messages to the clipboard */
    private JMenuItem messageCopyToClipboard = new JMenuItem(
            "Copy Message to Clipboard");

    /** The message menu popup option for sending a text */
    private JMenuItem messageSendText = new JMenuItem("Send Text");

    /** The message launch address book menu item */
    private JMenuItem messageLaunchAddressBook = new JMenuItem(
            "Launch Address Book");

    /** The current selected call history name */
    private String currentSelectedName = null;

    /** The current selected call history number */
    private String currentSelectedNumber = null;

    /** JMenu item for adding aliases */
    private JMenuItem addAlias;

    /** JMenu item for updating aliases */
    private JMenuItem updateAlias;

    /** JMenu item for delete aliases */
    private JMenuItem deleteAlias;

    /** JMenu item for adding white list */
    private JMenuItem addWhitelist;

    /** JMenu item for removing white list */
    private JMenuItem removeWhiteList;

    /** JMenu item for adding black list */
    private JMenuItem addBlacklist;

    /** JMenu item for removing black list */
    private JMenuItem removeBlacklist;

    /** JMenu for calls */
    private JPopupMenu callMenu;

    /** Table preferences helper for calls table */
    private TablePrefSizeHelper callTableHelper = new TablePrefSizeHelper(
            CALL_PREF_LOOKUP, callTable);

    /** Table preferences helper for the "extra" messages table */
    private TablePrefSizeHelper messageTableExtraHelper = new TablePrefSizeHelper(
            MSG_PREF_EXTRA_COL_LOOKUP, messagesTable);

    /** Table preferences helper for the "plain" messages table */
    private TablePrefSizeHelper messageTableNoExtraHelper = new TablePrefSizeHelper(
            MSG_PREF_NO_EXTRA_COL_LOOKUP, messagesTable);

    /** Current table pref size helper assigned to the messages table */
    private TablePrefSizeHelper currentMessageHelper = null;

    /**
     * Data Dump Key Dispatcher class to support detection of CTRL + SHIFT + D
     */
    private class DataDumpKeyDispatcher implements KeyEventDispatcher
    {
        /** Keeps track of CTRL key press state */
        private boolean ctrlDown = false;

        /** Keeps track of SHIFT key press state */
        private boolean shiftDown = false;

        /** @inheritDoc */
        public boolean dispatchKeyEvent(KeyEvent e)
        {
            if (e.getID() == KeyEvent.KEY_PRESSED)
            {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL)
                {
                    ctrlDown = true;
                }
                else if (e.getKeyCode() == KeyEvent.VK_SHIFT)
                {
                    shiftDown = true;
                }
                else if ((e.getKeyCode() == KeyEvent.VK_D) && ctrlDown
                        && shiftDown)
                {
                    DataDumpUtils.dumpServerDataToFile(NcidPopMainDisplay.this,
                            ServerDataManager
                                    .getServerDataForCurrentDisplayId());
                }
            }
            else if (e.getID() == KeyEvent.KEY_RELEASED)
            {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL)
                {
                    ctrlDown = false;
                }
                else if (e.getKeyCode() == KeyEvent.VK_SHIFT)
                {
                    shiftDown = false;
                }
            }
            return false;
        }
    }

    /**
     * Called when amplifying alias/whitelist/blacklist info data is received
     * from the server.
     */
    public void onInfoUpdate()
    {
        if ((currentSelectedName != null) && (currentSelectedNumber != null))
        {
            if (ServerRequestManager.getInfoAliasType() != null)
            {
                if (ServerRequestManager.getInfoAliasType().equals("NOALIAS"))
                {
                    callMenu.add(addAlias);
                }
                else
                {
                    callMenu.add(updateAlias);
                    callMenu.add(deleteAlias);
                }
            }

            ServerData data = ServerDataManager
                    .getServerDataForCurrentDisplayId();

            if ((data != null)
                    && ServerCapabilityAssessmentUtils.isCapabilitySupported(
                            ServerCapability.HANGUP_OPTION,
                            data.supportedCapability)
                    && (ServerRequestManager.getInfoSelectedType() != null))
            {
                if ((ServerRequestManager.getInfoSelectedType() == InfoListType.BLACK_NAME)
                        || (ServerRequestManager.getInfoSelectedType() == InfoListType.BLACK_NUMBER))
                {
                    callMenu.add(removeBlacklist);
                    callMenu.add(addWhitelist);
                }
                else if ((ServerRequestManager.getInfoSelectedType() == InfoListType.WHITE_NAME)
                        || (ServerRequestManager.getInfoSelectedType() == InfoListType.WHITE_NUMBER))
                {
                    callMenu.add(removeWhiteList);
                }
                else if (ServerRequestManager.getInfoSelectedType() == InfoListType.NONE)
                {
                    callMenu.add(addBlacklist);
                }
            }
        }
        callMenu.pack();
    }

    /**
     * Displays a "plain" dialog popup message
     * 
     * @param String the text to display
     * @param boolean whether or not to wrap the text
     */
    public void showDialogPopupMessage(final String message,
            final boolean wrapText)
    {
        NotificationUtils.showDialogPopupMessage(message, wrapText);
    }

    /**
     * Displays a popup for calls/notifications
     * 
     * @param the text to display
     * @param the image file name, can be null
     */
    public void showCallsNotificationsPopup(String text, String imageFileName)
    {
        NotificationUtils.showCallsNotificationsPopup(text, imageFileName,
                PrefUtils.instance);
    }

    /**
     * Displays a popup for messages
     * 
     * @param the MessageData to display
     */
    public void showMessagesPopup(MessageData messageData)
    {
        NotificationUtils.showMessagesPopup(messageData, PrefUtils.instance);
    }

    /**
     * Displays an accept/reject dialog message
     * 
     * @param String the message to display
     */
    public void showAcceptReject(final String message)
    {
        NotificationUtils.enqueueDialog(new Runnable()
        {
            /** @inheritDoc */
            public void run()
            {
                // Custom button text
                Object[] options =
                { "Accept", "Reject" };

                int result = JOptionPane
                        .showOptionDialog(
                                null,
                                "Would you like to apply the alias change to "
                                        + (ServerRequestManager
                                                .getLastServerRequestType() == ServerRequestType.MULTIPLE_LOG_REPROCESS_ALIASES ? "ALL call logs"
                                                : "the current call log")
                                        + "?\n\n " + message, "Accept Changes",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options,
                                options[1]);

                final ServerData data = ServerDataManager
                        .getServerDataForCurrentDisplayId();

                if (result == 0)
                {
                    ServerRequestManager.requestLogAccept(data);

                    SwingUtilities.invokeLater(new Runnable()
                    {
                        /** @inheritDoc */
                        public void run()
                        {
                            reloadTableModels();
                        }
                    });

                    // Give the server some time to process the log accept
                    // before we request a call log reread
                    Timer oneShot = new Timer(200, new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            ServerRequestManager.requestCallLoadReread(data);
                        }
                    });
                    oneShot.setRepeats(false);
                    oneShot.start();
                }
                else
                {
                    ServerRequestManager.requestLogReject(data);
                }
            }
        });
    }

    /**
     * Loads the call table
     * 
     * @param int the server display ID for the call table to load
     */
    private void loadCallTable(int serverDisplayId)
    {
        if (serverDisplayId != ServerDataManager.getCurrentServerDisplayId())
        {
            return;
        }

        // Reference to Model
        DefaultTableModel model = (DefaultTableModel) ((CallbackCapableTableSorter) callTable
                .getModel()).getTableModel();

        model.setRowCount(0);
        ServerData data = ServerDataManager.getServerDataForCurrentDisplayId();
        if (data != null)
        {
            // Get the call log for the current server
            ArrayList<ArrayList<String>> callLog = data.callLog;

            boolean calculateMillisValue = !PrefUtils.instance
                    .getBoolean(PrefUtils.PREF_USE_RAW_DATE_TIME);

            HashMap<String, String> addressBook = OsUtils.getAddressBookList();

            // Populate the table
            for (int index = callLog.size() - 1; index >= 0; index--)
            {
                Vector<Object> rowData = new Vector<Object>();

                ArrayList<String> strings = callLog.get(index);

                rowData.add(LogParserUtils.getValueForKey(strings,
                        LogParserUtils.TYPE_KEY));

                rowData.add(new LogDateTimeData(LogParserUtils.getValueForKey(
                        strings, LogParserUtils.DATE_KEY), LogParserUtils
                        .getValueForKey(strings, LogParserUtils.TIME_KEY),
                        calculateMillisValue));

                rowData.add(LogParserUtils.formatLineString(LogParserUtils
                        .getValueForKey(strings, LogParserUtils.LINE_KEY)));

                String number = LogParserUtils.getValueForKey(strings,
                        LogParserUtils.NUMBER_KEY);

                rowData.add(number);

                String name = LogParserUtils.getValueForKey(strings,
                        LogParserUtils.NAME_KEY);

                String alias = null;

                if (addressBook != null)
                {
                    for (String key : addressBook.keySet())
                    {
                        if (number.contains(key))
                        {
                            alias = addressBook.get(key);
                            break;
                        }
                    }
                }

                rowData.add(alias != null ? alias : name);
                model.addRow(rowData);
            }
        }
    }

    /**
     * Called on a server refresh
     */
    public void onServerRefresh()
    {
        ServerDataManager.resetCurrentServerDisplayId();
        panelButtons.setVisible(ServerDataManager.getServerCount() > 1);
    }

    /**
     * Updates the message history message count
     * 
     * @param int the server display ID for the call history count
     */
    private void updateMessageHistoryMessageCount(int serverDisplayId)
    {
        if (serverDisplayId != ServerDataManager.getCurrentServerDisplayId())
        {
            return;
        }

        ServerData data = ServerDataManager.getServerDataForCurrentDisplayId();

        if (data != null)
        {
            totalMessagesLabel.setText(Integer.toString(data.msgLog.size()));
        }
    }

    /**
     * Updates the call history call count
     * 
     * @param int the server display ID for the call history count
     */
    private void updateCallHistoryCallCount(int serverDisplayId)
    {
        if (serverDisplayId != ServerDataManager.getCurrentServerDisplayId())
        {
            return;
        }

        ServerData data = ServerDataManager.getServerDataForCurrentDisplayId();

        if (data != null)
        {
            totalCallsLabel.setText(Integer.toString(data.callLog.size()));
        }
    }

    /**
     * Loads the message table
     * 
     * @param int the server display ID
     */
    public void loadMessageTable(int serverDisplayId)
    {
        if (serverDisplayId != ServerDataManager.getCurrentServerDisplayId())
        {
            return;
        }

        // Reference to Model
        DefaultTableModel model = null;

        if (messagesTable.getModel() instanceof CallbackCapableTableSorter)
        {
            model = (DefaultTableModel) ((CallbackCapableTableSorter) messagesTable
                    .getModel()).getTableModel();
        }
        else
        {
            model = (DefaultTableModel) messagesTable.getModel();
        }

        model.setRowCount(0);

        ServerData data = ServerDataManager.getServerDataForCurrentDisplayId();
        if (data != null)
        {
            // Get the message log for the current server
            ArrayList<MessageData> messageLog = data.msgLog;

            HashMap<String, String> addressBook = OsUtils.getAddressBookList();

            if (!PrefUtils.instance
                    .getBoolean(PrefUtils.PREF_DISPLAY_EXTRA_MESSAGE_LOG_FIELDS))
            {
                // Populate the table
                for (int index = messageLog.size() - 1; index >= 0; index--)
                {
                    Vector<Object> rowData = new Vector<Object>();

                    MessageData msgData = messageLog.get(index);

                    String type = msgData.lineType;
                    if (msgData.msgType.length() > 0)
                    {
                        type += "(" + msgData.msgType + ")";
                    }

                    rowData.add(type);
                    rowData.add(msgData.encodePrettyPrintWithAliasList(
                            PrefUtils.instance, true, addressBook));
                    rowData.add(msgData.number);
                    model.addRow(rowData);
                }
            }
            else
            {
                boolean calculateMillisValue = !PrefUtils.instance
                        .getBoolean(PrefUtils.PREF_USE_RAW_DATE_TIME);

                // Populate the table
                for (int index = messageLog.size() - 1; index >= 0; index--)
                {
                    Vector<Object> rowData = new Vector<Object>();

                    MessageData msgData = messageLog.get(index);

                    String type = msgData.lineType;
                    if (msgData.msgType.length() > 0)
                    {
                        type += "(" + msgData.msgType + ")";
                    }

                    rowData.add(type);
                    rowData.add(new LogDateTimeData(msgData.date, msgData.time,
                            calculateMillisValue));
                    rowData.add(msgData.device);
                    rowData.add(msgData.getFromTo(addressBook));
                    rowData.add(msgData.message);
                    rowData.add(msgData.number);
                    model.addRow(rowData);
                }
            }
        }
    }

    /**
     * Performs a GUI refresh
     * 
     * @param int the server display ID
     */
    public void refreshGuis(int serverDisplayId)
    {
        updateSearchField(serverDisplayId);
        refreshMsgGui(serverDisplayId);
        refreshCallGui(serverDisplayId);
        evaluateEnabledFields(serverDisplayId);
    }

    /**
     * Updates the search field with new suggestions
     * 
     * @param int the server display ID
     */
    private void updateSearchField(int serverDisplayId)
    {
        ServerData data = ServerDataManager.getServerDataForCurrentDisplayId();
        HashSet<LookupCandidate> candidates = DataLookupUtils
                .getAllLookupCandidates(data);
        suggestFieldSearch.setSuggestData(candidates);
    }

    /**
     * Performs a GUI refresh for the msg GUI
     * 
     * @param int the server display ID
     */
    public void refreshMsgGui(int serverDisplayId)
    {
        loadMessageTable(serverDisplayId);
        updateMessageHistoryMessageCount(serverDisplayId);
    }

    /**
     * Performs a GUI refresh for the call GUI
     * 
     * @param int the server display ID
     */
    public void refreshCallGui(int serverDisplayId)
    {
        loadCallTable(serverDisplayId);
        updateCallHistoryCallCount(serverDisplayId);
        updateCallHistoryServerName();
    }

    /**
     * Evaluates enabled fields based on the current server capabilities
     * 
     * @param int the server display ID
     */
    public void evaluateEnabledFields(int serverDisplayId)
    {
        if (serverDisplayId != ServerDataManager.getCurrentServerDisplayId())
        {
            return;
        }

        final ServerData data = ServerDataManager
                .getServerDataForCurrentDisplayId();

        boolean enableJobs = (data != null)
                && ServerCapabilityAssessmentUtils.isCapabilitySupported(
                        ServerCapability.BASELINE_JOB_SUPPORT,
                        data.supportedCapability);

        refreshPopupMenu.setEnabled(enableJobs);
        refreshMenu.setEnabled(enableJobs);

        reloadServerListPopupMenu.setEnabled(enableJobs);
        reloadListsMenu.setEnabled(enableJobs);
        sendServerMessageMenu.setEnabled((data != null)
                && data.isServerConnected);
    }

    /**
     * Responds to a list update
     * 
     * @param whether or not to present the option to apply list changes to the
     *        call log
     */
    public void respondToListUpdate(boolean applyToCallLogs)
    {
        final ServerData data = ServerDataManager
                .getServerDataForCurrentDisplayId();

        ServerRequestManager.requestServerListReload(data);
        ServerRequestManager.requestServerInfo(currentSelectedNumber,
                currentSelectedName, data);

        if (!applyToCallLogs)
        {
            return;
        }

        NotificationUtils.enqueueDialog(new Runnable()
        {
            public void run()
            {
                // Custom button text
                Object[] options =
                { "Just Current Log", "All Logs", "No Logs" };

                int result = JOptionPane
                        .showOptionDialog(
                                null,
                                "Would you like to apply aliases to all call logs?",
                                "Aliases", JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options,
                                options[2]);

                if (result == 0)
                {
                    ServerRequestManager.requestServerAliasReprocess(data,
                            false);
                }
                else if (result == 1)
                {
                    ServerRequestManager
                            .requestServerAliasReprocess(data, true);
                }
            }
        });
    }

    /**
     * Updates the connected icon
     */
    public void updateConnectedIcon()
    {
        ImageIcon icon = null;
        ImageIcon smallIcon = null;

        if (ServerDataManager.getCurrentSocketConnectionCount() == 0)
        {
            icon = NcidConstants.ICON_NOT_CONNECTED;
            smallIcon = NcidConstants.ICON_SMALL_NOT_CONNECTED;
        }
        else if (ServerDataManager.getCurrentSocketConnectionCount() == ServerDataManager
                .getServerCount())
        {
            icon = NcidConstants.ICON_CONNECTED;
            smallIcon = NcidConstants.ICON_SMALL_CONNECTED;
        }
        else
        {
            icon = NcidConstants.ICON_SOME_CONNECTED;
            smallIcon = NcidConstants.ICON_SMALL_SOME_CONNECTED;
        }

        NotificationUtils.setTrayIconImage(smallIcon.getImage());

        OsUtils.setMacDockImage(icon.getImage());
        iconLabel.setIcon(smallIcon);
    }

    /**
     * Updates the tray tip
     */
    public void updateTrayTip()
    {
        String tipText;
        if (ServerDataManager.getCurrentSocketConnectionCount() == 0)
        {
            tipText = "NCIDpop not connected - ";
        }
        else if (ServerDataManager.getCurrentSocketConnectionCount() == ServerDataManager
                .getServerCount())
        {
            tipText = "NCIDpop connected - ";
        }
        else
        {
            tipText = String.format("NCIDpop - %d of %d servers connected - ",
                    ServerDataManager.getCurrentSocketConnectionCount(),
                    ServerDataManager.getServerCount());
        }

        if (ServerDataManager.anyLogUpdateInProgress())
        {
            tipText = "Log Update In Progress";
        }
        else
        {
            tipText = tipText + ServerDataManager.getTotalCallCount()
                    + " calls, " + ServerDataManager.getTotalMessageCount()
                    + " messages";
        }

        NotificationUtils.setTrayIconTipText(tipText);
    }

    /**
     * Updates the call history server name
     */
    public void updateCallHistoryServerName()
    {
        ServerData data = ServerDataManager.getServerDataForCurrentDisplayId();

        if (data == null)
        {
            return;
        }

        String serverName = data.displayName;
        String serverList = "";

        if (ServerDataManager.getServerCount() > 1)
        {
            serverList = "Servers connected: "
                    + ServerDataManager.getCurrentSocketConnectionCount();

            serverName = serverName + " - " + data.hostName + ":"
                    + data.hostPort;
        }

        serverNameLabel.setText(serverName);
        serverListLabel.setText(serverList);
    }

    /**
     * Initializes the tray icon
     */
    public void initTrayIcon()
    {
        // Check to make sure that the tray icon is supported.
        if (SystemTrayWrapper.isSupported()
                && TrayIconWrapper.trayIconSupported())
        {
            PopupMenu popup = new PopupMenu();
            popup.add(new MenuItem("Call History..."));
            popup.addSeparator();
            popup.add(new MenuItem("Preferences..."));
            popup.add(refreshPopupMenu);
            popup.add(reloadServerListPopupMenu);
            popup.addSeparator();
            popup.add(smsPopupMenu);
            popup.add(new MenuItem("Send Server Message"));
            popup.addSeparator();
            popup.add(new MenuItem("NCIDpop Web Page"));
            popup.add(new MenuItem("About NCIDpop..."));
            popup.addSeparator();
            popup.add(new MenuItem("Exit"));

            NotificationUtils.setTrayIconPopupMenu(popup);
            iconLabel.setIcon(NcidConstants.ICON_SMALL_NOT_CONNECTED);

            NotificationUtils.addTrayIconMouseListener(new MouseAdapter()
            {
                /** @inheritDoc */
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    if ((e.getClickCount() == 2)
                            && (e.getButton() == MouseEvent.BUTTON1))
                    {
                        getTopLevelAncestor().setVisible(true);
                    }
                }
            });

            popup.addActionListener(new ActionListener()
            {
                /** @inheritDoc */
                public void actionPerformed(ActionEvent evt)
                {
                    if (evt.getActionCommand().equals("Call History..."))
                    {
                        getTopLevelAncestor().setVisible(true);
                    }
                    else if (evt.getActionCommand().equals("Preferences..."))
                    {
                        prefDisplay.setVisible(true);
                    }
                    else if (evt.getActionCommand().equals("Refresh"))
                    {
                        reloadTableModels();
                        ServerData data = ServerDataManager
                                .getServerDataForCurrentDisplayId();
                        ServerRequestManager.requestCallLoadReread(data);
                    }
                    else if (evt.getActionCommand().equals(
                            "Reload Server Lists"))
                    {
                        respondToListUpdate(true);
                    }
                    else if (evt.getActionCommand().equals(
                            "Send SMS Text Message"))
                    {
                        launchSendSmsDisplay("");
                    }
                    else if (evt.getActionCommand().equals(
                            "Send Server Message"))
                    {
                        launchSendServerMessageDisplay();
                    }
                    else if (evt.getActionCommand().equals("NCIDpop Web Page"))
                    {
                        DesktopWrapper
                                .launchWebsite(NcidConstants.NCIDPOP_WEB_PAGE);
                    }
                    else if (evt.getActionCommand().equals("About NCIDpop..."))
                    {
                        aboutDisplay.setVisible(true);
                    }
                    else if (evt.getActionCommand().equals("Exit"))
                    {
                        System.exit(0);
                    }
                }
            });
        }
    }

    /**
     * Initializes the main display
     */
    public void initialize()
    {
        Preferences.userNodeForPackage(PrefUtils.class)
                .addPreferenceChangeListener(new PreferenceChangeListener()
                {
                    /** @inheritDoc */
                    public void preferenceChange(final PreferenceChangeEvent evt)
                    {
                        SwingUtilities.invokeLater(new Runnable()
                        {
                            /** @inheritDoc */
                            public void run()
                            {
                                if (evt.getKey().equals(
                                        PrefUtils.PREF_USE_RAW_DATE_TIME))
                                {
                                    setTableSortingStatus(
                                            callTable,
                                            PrefUtils.instance
                                                    .getBoolean(PrefUtils.PREF_USE_RAW_DATE_TIME) ? -1
                                                    : PrefUtils.instance
                                                            .getInteger(PrefUtils.PREF_CALL_TABLE_SORT_COLUMN),
                                            PrefUtils.instance
                                                    .getInteger(PrefUtils.PREF_CALL_TABLE_SORT_DIRECTION));

                                    if (PrefUtils.instance
                                            .getBoolean(PrefUtils.PREF_DISPLAY_EXTRA_MESSAGE_LOG_FIELDS))
                                    {
                                        setTableSortingStatus(
                                                messagesTable,
                                                PrefUtils.instance
                                                        .getBoolean(PrefUtils.PREF_USE_RAW_DATE_TIME) ? -1
                                                        : PrefUtils.instance
                                                                .getInteger(PrefUtils.PREF_MESSAGE_TABLE_SORT_COLUMN),
                                                PrefUtils.instance
                                                        .getInteger(PrefUtils.PREF_MESSAGE_TABLE_SORT_DIRECTION));
                                    }

                                    refreshGuis(ServerDataManager
                                            .getCurrentServerDisplayId());
                                }

                                if (evt.getKey()
                                        .equals(PrefUtils.PREF_DISPLAY_EXTRA_MESSAGE_LOG_FIELDS))
                                {
                                    reloadMessageTableModel();
                                    refreshMsgGui(ServerDataManager
                                            .getCurrentServerDisplayId());
                                }

                                if (evt.getKey().equals(
                                        PrefUtils.PREF_PHONE_NUMBER_FORMAT))
                                {
                                    refreshCallGui(ServerDataManager
                                            .getCurrentServerDisplayId());
                                }

                                if (evt.getKey()
                                        .equals(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_EXTERNAL_PROGRAM)
                                        || evt.getKey()
                                                .equals(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_GATEWAY_RELAY)
                                        || evt.getKey()
                                                .equals(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_URL))
                                {
                                    setSmsMenusEnabled();
                                    rebuildMessageMenu();
                                }
                            }
                        });
                    }
                });

        aboutDisplay = new NcidPopAboutDisplay();
        prefDisplay = new NcidPopPreferencesDisplay();
        smsInput = new SmsInputDisplay();
        messageInput = new ServerMessageInputDisplay();
        relayInput = new RelayInputDisplay();
        initTrayIcon();

        String currentPreferencesCompatVersion = PrefUtils.instance
                .getString(PrefUtils.PREF_VERSION_COMPATIBILITY);

        int difference = VersionNumberUtils.versionNumberDifference(
                currentPreferencesCompatVersion,
                VersionNumber.NCIDPOP_PREFS_COMPATIBILITY_VERSION_NUMBER);

        if (difference < 0)
        {
            prefDisplay.setVisible(true);
        }

        setSmsMenusEnabled();
        refreshGuis(ServerDataManager.getCurrentServerDisplayId());
        updateConnectedIcon();
        updateTrayTip();
    }

    /**
     * Tests to see if any SMS send option is enabled
     * 
     * @return boolean
     */
    private boolean anySmsSendOptionEnabled()
    {
        return PrefUtils.instance
                .getBoolean(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_EXTERNAL_PROGRAM)
                || PrefUtils.instance
                        .getBoolean(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_GATEWAY_RELAY)
                || PrefUtils.instance
                        .getBoolean(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_URL);
    }

    /**
     * Sets the SMS menus enabled or disabled as a function of preferences
     */
    private void setSmsMenusEnabled()
    {
        smsPopupMenu.setEnabled(anySmsSendOptionEnabled());
        sendTextMenu.setEnabled(anySmsSendOptionEnabled());
    }

    /**
     * Sets the default table sorting status based on table formatting
     * parameters
     * 
     * @param JTable the table
     * @param integer the column to sort
     * @param integer the direction to sort
     */
    private void setTableSortingStatus(JTable table, int columnToSort,
            int directionToSort)
    {
        if (columnToSort == -1)
        {
            ((CallbackCapableTableSorter) table.getModel()).setSortingStatus(1,
                    TableSorter.NOT_SORTED);
        }
        else
        {
            ((CallbackCapableTableSorter) table.getModel()).setSortingStatus(
                    columnToSort, directionToSort);
        }
    }

    /**
     * Reloads the messages table model
     */
    private void reloadMessageTableModel()
    {
        // Remove old listener to prevent memory leaks
        if (currentMessageHelper != null)
        {
            messagesTable.getColumnModel().removeColumnModelListener(
                    currentMessageHelper);
            currentMessageHelper = null;
        }

        if (!PrefUtils.instance
                .getBoolean(PrefUtils.PREF_DISPLAY_EXTRA_MESSAGE_LOG_FIELDS))
        {

            if (messagesTable.getTableHeader().getDefaultRenderer() instanceof SortableHeaderRenderer)
            {
                messagesTable
                        .getTableHeader()
                        .setDefaultRenderer(
                                ((SortableHeaderRenderer) messagesTable
                                        .getTableHeader().getDefaultRenderer()).tableCellRenderer);
            }
            messagesTable.setDefaultRenderer(String.class,
                    new TextAreaCellRenderer());

            messagesTable.setModel(new DefaultTableModel(new Object[][]
            {}, new String[]
            { "Type", "Text", "Number" })
            {
                Class<?>[] columnTypes = new Class<?>[]
                { String.class, String.class, String.class };

                /** @inheritDoc */
                @Override
                public Class<?> getColumnClass(int columnIndex)
                {
                    return columnTypes[columnIndex];
                }

                /** @inheritDoc */
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex)
                {
                    return false;
                }
            });

            {
                TableColumnModel cm = messagesTable.getColumnModel();
                cm.getColumn(0).setPreferredWidth(
                        PrefUtils.instance
                                .getInteger(PrefUtils.PREF_MSG_TYPE_COL_WIDTH));
                cm.getColumn(0).setMinWidth(40);
                cm.getColumn(0).setMaxWidth(100);
                // Remove the number column from the table, but not the model
                messagesTable.removeColumn(messagesTable.getColumn("Number"));

                cm.addColumnModelListener(messageTableNoExtraHelper);
                currentMessageHelper = messageTableNoExtraHelper;
            }
        }
        else
        {
            messagesTable.setDefaultRenderer(String.class,
                    new CustomTableCellRenderer());

            messagesTable.setDefaultRenderer(LogDateTimeData.class,
                    new CustomTableCellRenderer());

            messagesTable.setModel(new DefaultTableModel(new Object[][]
            {}, new String[]
            { "Type", "Date/Time", "Device", "From/To", "Text", "Number" })
            {
                Class<?>[] columnTypes = new Class<?>[]
                { String.class, LogDateTimeData.class, String.class,
                        String.class, String.class };

                /** @inheritDoc */
                @Override
                public Class<?> getColumnClass(int columnIndex)
                {
                    return columnTypes[columnIndex];
                }

                /** @inheritDoc */
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex)
                {
                    return false;
                }
            });

            CallbackCapableTableSorter mySorter = new CallbackCapableTableSorter(
                    messagesTable.getModel(), messagesTable.getTableHeader());
            messagesTable.setModel(mySorter);

            mySorter.addSortingStatusChangeListener(new SortingStatusChangeListener()
            {
                /** @inheritDoc */
                public void onColumnSortChange(int column, int status)
                {
                    PrefUtils.instance.setInteger(
                            PrefUtils.PREF_MESSAGE_TABLE_SORT_COLUMN, column);
                    PrefUtils.instance
                            .setInteger(
                                    PrefUtils.PREF_MESSAGE_TABLE_SORT_DIRECTION,
                                    status);
                }
            });

            setTableSortingStatus(
                    messagesTable,
                    PrefUtils.instance
                            .getBoolean(PrefUtils.PREF_USE_RAW_DATE_TIME) ? -1
                            : PrefUtils.instance
                                    .getInteger(PrefUtils.PREF_MESSAGE_TABLE_SORT_COLUMN),
                    PrefUtils.instance
                            .getInteger(PrefUtils.PREF_MESSAGE_TABLE_SORT_DIRECTION));
            {
                TableColumnModel cm = messagesTable.getColumnModel();
                cm.getColumn(0).setPreferredWidth(
                        PrefUtils.instance
                                .getInteger(PrefUtils.PREF_MSG_TYPE_COL_WIDTH));
                cm.getColumn(0).setMaxWidth(100);
                cm.getColumn(0).setMinWidth(40);
                cm.getColumn(1)
                        .setPreferredWidth(
                                PrefUtils.instance
                                        .getInteger(PrefUtils.PREF_MSG_DATE_TIME_COL_WIDTH));
                cm.getColumn(1).setMaxWidth(185);
                cm.getColumn(1).setMinWidth(155);
                cm.getColumn(2)
                        .setPreferredWidth(
                                PrefUtils.instance
                                        .getInteger(PrefUtils.PREF_MSG_DEVICE_COL_WIDTH));
                cm.getColumn(2).setMaxWidth(100);
                cm.getColumn(2).setMinWidth(50);
                cm.getColumn(3)
                        .setPreferredWidth(
                                PrefUtils.instance
                                        .getInteger(PrefUtils.PREF_MSG_FROM_TO_COL_WIDTH));
                cm.getColumn(3).setMaxWidth(130);
                cm.getColumn(3).setMinWidth(60);

                cm.getColumn(4).setCellRenderer(new TextAreaCellRenderer());

                // Remove the number column from the table, but not the model
                messagesTable.removeColumn(messagesTable.getColumn("Number"));

                cm.addColumnModelListener(messageTableExtraHelper);
                currentMessageHelper = messageTableExtraHelper;
            }
        }
    }

    /**
     * Reloads the call table model
     */
    private void reloadCallTableModel()
    {
        // Remove old listener to prevent memory leaks
        callTable.getColumnModel().removeColumnModelListener(callTableHelper);

        // Initialize the model
        callTable.setModel(new DefaultTableModel(new Object[][]
        {}, new String[]
        { "Type", "Date/Time", "Line", "Number", "Name" })
        {
            Class<?>[] columnTypes = new Class<?>[]
            { String.class, LogDateTimeData.class, String.class, String.class,
                    String.class };
            boolean[] columnEditable = new boolean[]
            { false, false, false, false, false };

            /** @inheritDoc */
            @Override
            public Class<?> getColumnClass(int columnIndex)
            {
                return columnTypes[columnIndex];
            }

            /** @inheritDoc */
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return columnEditable[columnIndex];
            }
        });

        CallbackCapableTableSorter mySorter = new CallbackCapableTableSorter(
                callTable.getModel(), callTable.getTableHeader());
        callTable.setModel(mySorter);

        mySorter.addSortingStatusChangeListener(new SortingStatusChangeListener()
        {
            /** @inheritDoc */
            public void onColumnSortChange(int column, int status)
            {
                PrefUtils.instance.setInteger(
                        PrefUtils.PREF_CALL_TABLE_SORT_COLUMN, column);
                PrefUtils.instance.setInteger(
                        PrefUtils.PREF_CALL_TABLE_SORT_DIRECTION, status);
            }
        });

        setTableSortingStatus(
                callTable,
                PrefUtils.instance.getBoolean(PrefUtils.PREF_USE_RAW_DATE_TIME) ? -1
                        : PrefUtils.instance
                                .getInteger(PrefUtils.PREF_CALL_TABLE_SORT_COLUMN),
                PrefUtils.instance
                        .getInteger(PrefUtils.PREF_CALL_TABLE_SORT_DIRECTION));
        {
            TableColumnModel cm = callTable.getColumnModel();
            cm.getColumn(0).setPreferredWidth(
                    PrefUtils.instance
                            .getInteger(PrefUtils.PREF_CALL_TYPE_COL_WIDTH));
            cm.getColumn(0).setMaxWidth(105);
            cm.getColumn(0).setMinWidth(55);
            cm.getColumn(1)
                    .setPreferredWidth(
                            PrefUtils.instance
                                    .getInteger(PrefUtils.PREF_CALL_DATE_TIME_COL_WIDTH));
            cm.getColumn(1).setMaxWidth(185);
            cm.getColumn(1).setMinWidth(155);
            cm.getColumn(2).setPreferredWidth(
                    PrefUtils.instance
                            .getInteger(PrefUtils.PREF_CALL_LINE_COL_WIDTH));
            cm.getColumn(2).setMaxWidth(100);
            cm.getColumn(2).setMinWidth(50);
            cm.getColumn(3).setPreferredWidth(
                    PrefUtils.instance
                            .getInteger(PrefUtils.PREF_CALL_NUMBER_COL_WIDTH));
            cm.getColumn(3).setMaxWidth(130);
            cm.getColumn(3).setMinWidth(60);

            cm.addColumnModelListener(callTableHelper);
        }

    }

    /**
     * Reloads the table models
     */
    private void reloadTableModels()
    {
        reloadCallTableModel();
        reloadMessageTableModel();
    }

    /**
     * Removes a number from the black or white list.
     * 
     * @param InfoListType the info list type
     * @param ListType the list type
     * @param ServerData the server data
     * @param String the name to remove if listType is a name type
     * @param String the number to remove if listType is a number type
     */
    private void removeFromBlackOrWhiteList(final InfoListType type,
            final ListType listType, final ServerData data, final String name,
            final String number)
    {
        final String itemToDelete;

        if ((type == InfoListType.WHITE_NAME)
                || (type == InfoListType.BLACK_NAME))
        {
            itemToDelete = name;
        }
        else if ((type == InfoListType.WHITE_NUMBER)
                || (type == InfoListType.BLACK_NUMBER))
        {
            itemToDelete = number;
        }
        else
        {
            itemToDelete = null;
        }

        if (itemToDelete == null)
        {
            return;
        }

        NotificationUtils.enqueueDialog(new Runnable()
        {
            /** @inheritDoc */
            public void run()
            {
                int result = JOptionPane.showConfirmDialog(null,
                        "Do you really wish to delete " + itemToDelete
                                + " from the " + listType.type + " list?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION)
                {
                    if (itemToDelete.equals(name))
                    {
                        ServerRequestManager.requestServerBlackWhiteListChange(
                                null, name, null, ListRequestChangeType.REMOVE,
                                listType, data);
                    }
                    else if (itemToDelete.equals(number))
                    {
                        ServerRequestManager.requestServerBlackWhiteListChange(
                                number, null, null,
                                ListRequestChangeType.REMOVE, listType, data);
                    }
                }
            }
        });
    }

    /**
     * Tests to see whether the "Send Text" Menu should be enabled
     * 
     * @param String the number to test
     */
    private boolean isSendTextEnabled(String number)
    {
        return (anySmsSendOptionEnabled()
                && !number.equals(LogParserUtils.UNKNOWN_VALUE) && (!number
                    .equals("-")));
    }

    /**
     * Rebuilds the message menu
     */
    private void rebuildMessageMenu()
    {
        messageMenu.removeAll();
        messageMenu.add(messageCopyToClipboard);

        if (messagesTable.getSelectedRow() != -1)
        {
            int messageNumberColumn = 0;

            if (PrefUtils.instance
                    .getBoolean(PrefUtils.PREF_DISPLAY_EXTRA_MESSAGE_LOG_FIELDS))
            {
                messageNumberColumn = MESSAGE_NUMBER_EXTRA_COL;
            }
            else
            {
                messageNumberColumn = MESSAGE_NUMBER_NO_EXTRA_COL;
            }

            String number = (String) messagesTable.getModel().getValueAt(
                    messagesTable.getSelectedRow(), messageNumberColumn);

            if (OsUtils.hasAddressBookAlias(number))
            {
                messageMenu.add(messageLaunchAddressBook);
            }

            if (isSendTextEnabled(number))
            {
                messageMenu.add(messageSendText);
            }
        }
        messageMenu.pack();
    }

    /**
     * Initializes auxiliary popup menu items for the call table
     */
    private void initAuxCallTablePopupMenuItems()
    {
        addAlias = new JMenuItem("Add Alias");
        updateAlias = new JMenuItem("Update Alias");
        deleteAlias = new JMenuItem("Delete Alias");
        addBlacklist = new JMenuItem("Add to Black List");
        removeBlacklist = new JMenuItem("Remove From Black List");
        addWhitelist = new JMenuItem("Add to White List");
        removeWhiteList = new JMenuItem("Remove From White List");

        addAlias.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent evt)
            {
                ServerData data = ServerDataManager
                        .getServerDataForCurrentDisplayId();

                if (data == null)
                {
                    return;
                }

                NcidPopAliasEditDisplay display = new NcidPopAliasEditDisplay(
                        false, currentSelectedName, data, currentSelectedNumber);
                display.setVisible(true);
            }
        });

        updateAlias.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent evt)
            {
                ServerData data = ServerDataManager
                        .getServerDataForCurrentDisplayId();

                if (data == null)
                {
                    return;
                }

                NcidPopAliasEditDisplay display = new NcidPopAliasEditDisplay(
                        true, currentSelectedName, data, currentSelectedNumber);
                display.setVisible(true);
            }
        });

        deleteAlias.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent evt)
            {
                final ServerData data = ServerDataManager
                        .getServerDataForCurrentDisplayId();

                if (data == null)
                {
                    return;
                }

                final String aliasNumber = currentSelectedNumber;
                final String aliasName = currentSelectedName;
                NotificationUtils.enqueueDialog(new Runnable()
                {
                    /** @inheritDoc */
                    public void run()
                    {
                        int result = JOptionPane.showConfirmDialog(null,
                                "Do you really wish to delete the alias "
                                        + currentSelectedName + "?",
                                "Confirm Delete", JOptionPane.YES_NO_OPTION);

                        if (result == JOptionPane.YES_OPTION)
                        {
                            ServerRequestManager.requestServerAliasListChange(
                                    aliasNumber, aliasName, null,
                                    ListRequestChangeType.MODIFY, data);
                        }
                    }
                });
            }
        });

        addWhitelist.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent evt)
            {
                ServerData data = ServerDataManager
                        .getServerDataForCurrentDisplayId();

                if (data == null)
                {
                    return;
                }

                NcidPopWhiteBlackEditDisplay display = new NcidPopWhiteBlackEditDisplay(
                        true, currentSelectedName, currentSelectedNumber, data);
                display.setVisible(true);
            }
        });

        removeWhiteList.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent evt)
            {
                removeFromBlackOrWhiteList(
                        ServerRequestManager.getInfoSelectedType(),
                        ListType.WHITE,
                        ServerDataManager.getServerDataForCurrentDisplayId(),
                        currentSelectedName, currentSelectedNumber);
            }
        });

        addBlacklist.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent evt)
            {
                ServerData data = ServerDataManager
                        .getServerDataForCurrentDisplayId();

                if (data == null)
                {
                    return;
                }

                NcidPopWhiteBlackEditDisplay display = new NcidPopWhiteBlackEditDisplay(
                        false, currentSelectedName, currentSelectedNumber, data);
                display.setVisible(true);
            }
        });

        removeBlacklist.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent evt)
            {
                removeFromBlackOrWhiteList(
                        ServerRequestManager.getInfoSelectedType(),
                        ListType.BLACK,
                        ServerDataManager.getServerDataForCurrentDisplayId(),
                        currentSelectedName, currentSelectedNumber);
            }
        });
    }

    /**
     * Loads the Send Server Message display.
     */
    private void launchSendServerMessageDisplay()
    {
        ServerData data = ServerDataManager.getServerDataForCurrentDisplayId();

        if (data == null)
        {
            NotificationUtils.showDialogPopupMessage(
                    "Cannot send server message.  Server not initialized.",
                    true);
        }
        messageInput.setVisible(data);
    }

    /**
     * Loads the Send SMS display, which is either a website or an external
     * program.
     * 
     * @param String the number to which to send the SMS
     */
    private void launchSendSmsDisplay(String number)
    {
        String websitePref = PrefUtils.instance
                .getString(PrefUtils.PREF_TEXT_MESSAGE_URL);

        if (PrefUtils.instance
                .getBoolean(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_URL))
        {
            DesktopWrapper
                    .launchWebsite(websitePref.replace("$NUMBER", number));
        }

        if (PrefUtils.instance
                .getBoolean(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_EXTERNAL_PROGRAM)
                || PrefUtils.instance
                        .getBoolean(PrefUtils.PREF_ENABLE_TEXT_MESSAGE_GATEWAY_RELAY))
        {
            smsInput.setVisible(number);
        }
    }

    /**
     * Constructor
     */
    public NcidPopMainDisplay()
    {
        reloadTableModels();

        // Add a global key event dispatcher to support undocumented data dump
        // capability
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new DataDumpKeyDispatcher());

        // Set up the table parameters
        callTable.setDefaultRenderer(String.class,
                new CustomTableCellRenderer());
        callTable.setDefaultRenderer(LogDateTimeData.class,
                new CustomTableCellRenderer());
        callTable.setShowGrid(false);
        callTable.setRowSelectionAllowed(true);
        callTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        messagesTable.setShowGrid(false);
        messagesTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        buttonPrevious.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent event)
            {
                refreshGuis(ServerDataManager.decrementCurrentServerDisplayId());
            }
        });

        buttonNext.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent event)
            {
                refreshGuis(ServerDataManager.incrementCurrentServerDisplayId());
            }
        });

        refreshMenu.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                reloadTableModels();
                ServerData data = ServerDataManager
                        .getServerDataForCurrentDisplayId();
                ServerRequestManager.requestCallLoadReread(data);
            }
        });

        reloadListsMenu.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                respondToListUpdate(true);
            }
        });

        exitMenu.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        aboutMenu.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent arg0)
            {
                aboutDisplay.setVisible(true);
            }
        });

        linesLabelMenu.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent arg0)
            {
                NotificationUtils
                        .showDialogPopupMessage(
                                "Here are the list of the different label types supported by NCIDpop\n\n"
                                        + "Label\tType\t\tDescription\n"
                                        + "CID:\tCaller ID\t\t- incoming call\n"
                                        + "OUT:\tOut      \t\t- outgoing call\n"
                                        + "HUP:\tHangup   \t\t- blacklisted call hangup\n"
                                        + "BLK:\tBlocked  \t\t- blacklisted call blocked\n"
                                        + "PID:\tPhone ID \t\t- caller ID from a smart phone\n"
                                        + "WID:\tCall Waiting ID \t\t- caller ID from call waiting\n"
                                        + "MSG:\tMessage  \t\t- message from a user or NCID\n"
                                        + "NOT:\tNotice   \t\t- a smart phone message notice\n",
                                false);
            }
        });

        preferencesMenu.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                prefDisplay.setVisible(true);
            }
        });

        webpageMenu.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                DesktopWrapper.launchWebsite(NcidConstants.NCIDPOP_WEB_PAGE);
            }
        });

        sendTextMenu.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                launchSendSmsDisplay("");
            }
        });

        sendServerMessageMenu.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                launchSendServerMessageDisplay();
            }
        });

        relayOptionsMenu.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent e)
            {
                relayInput.setVisible();
            }
        });

        initAuxCallTablePopupMenuItems();

        callMenu = new JPopupMenu();

        final JMenuItem menuCopyToClipboard = new JMenuItem(
                "Copy Number to Clipboard");
        menuCopyToClipboard.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent evt)
            {
                // format the number first
                String value = NumberFormatUtilities.formatNumber(
                        (String) callTable.getValueAt(
                                callTable.getSelectedRow(), CALL_NUMBER_COL),
                        PrefUtils.instance
                                .getString(PrefUtils.PREF_PHONE_NUMBER_FORMAT));

                // copy it to clipboard
                StringSelection stringSelection = new StringSelection(value);
                Clipboard clpbrd = Toolkit.getDefaultToolkit()
                        .getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
            }
        });

        final JMenuItem menuLookupPhoneNumber = new JMenuItem(
                "Phone Number Lookup");
        menuLookupPhoneNumber.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent evt)
            {
                DesktopWrapper.launchWebsite(NumberFormatUtilities.formatNumber(
                        (String) callTable.getValueAt(
                                callTable.getSelectedRow(), CALL_NUMBER_COL),
                        PrefUtils.instance
                                .getString(PrefUtils.PREF_REVERSE_LOOKUP_URL)));
            }
        });

        final JMenuItem callLaunchAddressBook = new JMenuItem(
                "Launch Address Book");
        callLaunchAddressBook.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent evt)
            {
                String value = (String) callTable.getValueAt(
                        callTable.getSelectedRow(), CALL_NUMBER_COL);
                OsUtils.launchAddressBookFor(value);
            }
        });

        final JMenuItem callSendText = new JMenuItem("Send Text");

        callSendText.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent evt)
            {
                if (callTable.getSelectedRow() != -1)
                {
                    String number = (String) callTable.getModel().getValueAt(
                            callTable.getSelectedRow(), CALL_NUMBER_COL);
                    launchSendSmsDisplay(number);
                }
            }
        });

        callMenu.addPopupMenuListener(new PopupMenuListener()
        {
            /** @inheritDoc */
            public void popupMenuWillBecomeVisible(PopupMenuEvent e)
            {
                callMenu.removeAll();

                Point point = SwingUtilities.convertPoint(callMenu, MouseInfo
                        .getPointerInfo().getLocation(), callTable);
                // our intent here is to select the row on clicks other than
                // left click to support our popup menu for "copy to clipboard"
                int r = callTable.rowAtPoint(point);
                if ((r >= 0) && (r < callTable.getRowCount()))
                {
                    if (callTable.getSelectedRow() != r)
                    {
                        callTable.setRowSelectionInterval(r, r);
                    }
                }
                else
                {
                    callTable.clearSelection();
                }

                callMenu.add(menuCopyToClipboard);
                callMenu.add(menuLookupPhoneNumber);

                if (callTable.getSelectedRow() != -1)
                {
                    String number = (String) callTable.getModel().getValueAt(
                            callTable.getSelectedRow(), CALL_NUMBER_COL);

                    if (isSendTextEnabled(number))
                    {
                        callMenu.add(callSendText);
                    }

                    if (OsUtils.hasAddressBookAlias(number))
                    {
                        callMenu.add(callLaunchAddressBook);
                    }
                }
                onInfoUpdate();
            }

            /** @inheritDoc */
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
            {
            }

            /** @inheritDoc */
            public void popupMenuCanceled(PopupMenuEvent e)
            {
            }
        });

        callTable.setComponentPopupMenu(callMenu);

        callTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener()
                {
                    public void valueChanged(ListSelectionEvent e)
                    {
                        if (!e.getValueIsAdjusting()
                                && (callTable.getSelectedRow() != -1))
                        {
                            ServerData data = ServerDataManager
                                    .getServerDataForCurrentDisplayId();

                            currentSelectedNumber = (String) callTable
                                    .getValueAt(callTable.getSelectedRow(),
                                            CALL_NUMBER_COL);

                            currentSelectedName = (String) callTable
                                    .getValueAt(callTable.getSelectedRow(),
                                            CALL_NAME_COL);

                            if ((data != null)
                                    && ServerCapabilityAssessmentUtils
                                            .isCapabilitySupported(
                                                    ServerCapability.BASELINE_JOB_SUPPORT,
                                                    data.supportedCapability))
                            {
                                ServerRequestManager.requestServerInfo(
                                        currentSelectedNumber,
                                        currentSelectedName, data);
                            }
                        }
                    }
                });

        callTable.addMouseListener(new MouseAdapter()
        {
            /** @inheritDoc */
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if ((e.getClickCount() == 2)
                        && (e.getButton() == MouseEvent.BUTTON1))
                {
                    JTable target = (JTable) e.getSource();

                    DesktopWrapper.launchWebsite(NumberFormatUtilities.formatNumber(
                            (String) target.getValueAt(target.getSelectedRow(),
                                    CALL_NUMBER_COL),
                            PrefUtils.instance
                                    .getString(PrefUtils.PREF_REVERSE_LOOKUP_URL)));
                }
            }
        });

        messageCopyToClipboard.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent evt)
            {
                int messageTextColumn = 0;

                if (PrefUtils.instance
                        .getBoolean(PrefUtils.PREF_DISPLAY_EXTRA_MESSAGE_LOG_FIELDS))
                {
                    messageTextColumn = MESSAGE_TEXT_EXTRA_COL;
                }
                else
                {
                    messageTextColumn = MESSAGE_TEXT_NO_EXTRA_COL;
                }

                StringSelection stringSelection = new StringSelection(
                        (String) messagesTable.getValueAt(
                                messagesTable.getSelectedRow(),
                                messageTextColumn));
                Clipboard clpbrd = Toolkit.getDefaultToolkit()
                        .getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
            }
        });

        messageSendText.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent evt)
            {
                int messageNumberColumn = 0;

                if (PrefUtils.instance
                        .getBoolean(PrefUtils.PREF_DISPLAY_EXTRA_MESSAGE_LOG_FIELDS))
                {
                    messageNumberColumn = MESSAGE_NUMBER_EXTRA_COL;
                }
                else
                {
                    messageNumberColumn = MESSAGE_NUMBER_NO_EXTRA_COL;
                }

                String number = (String) messagesTable.getModel().getValueAt(
                        messagesTable.getSelectedRow(), messageNumberColumn);
                launchSendSmsDisplay(number);
            }
        });

        messageLaunchAddressBook.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent evt)
            {
                int messageNumberColumn = 0;

                if (PrefUtils.instance
                        .getBoolean(PrefUtils.PREF_DISPLAY_EXTRA_MESSAGE_LOG_FIELDS))
                {
                    messageNumberColumn = MESSAGE_NUMBER_EXTRA_COL;
                }
                else
                {
                    messageNumberColumn = MESSAGE_NUMBER_NO_EXTRA_COL;
                }

                String number = (String) messagesTable.getModel().getValueAt(
                        messagesTable.getSelectedRow(), messageNumberColumn);

                OsUtils.launchAddressBookFor(number);
            }
        });

        messagesTable.setComponentPopupMenu(messageMenu);

        messagesTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener()
                {
                    public void valueChanged(ListSelectionEvent e)
                    {
                        if (!e.getValueIsAdjusting()
                                && (messagesTable.getSelectedRow() != -1))
                        {
                            rebuildMessageMenu();
                        }
                    }
                });

        messageMenu.addPopupMenuListener(new PopupMenuListener()
        {
            /** @inheritDoc */
            public void popupMenuWillBecomeVisible(PopupMenuEvent e)
            {
                Point point = SwingUtilities
                        .convertPoint(messageMenu, MouseInfo.getPointerInfo()
                                .getLocation(), messagesTable);
                // our intent here is to select the row on clicks other than
                // left click to support our popup menu for "copy to clipboard"
                int r = messagesTable.rowAtPoint(point);
                if ((r >= 0) && (r < messagesTable.getRowCount()))
                {
                    messagesTable.setRowSelectionInterval(r, r);
                }
                else
                {
                    messagesTable.clearSelection();
                }
            }

            /** @inheritDoc */
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
            {
            }

            /** @inheritDoc */
            public void popupMenuCanceled(PopupMenuEvent e)
            {
            }
        });
    }
}