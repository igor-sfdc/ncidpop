/*******************************************************
 * NCID Pop Main Display Controller
 *******************************************************/
package com.lenderman.ncidpop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;
import com.lenderman.ncidpop.data.MessageData;
import com.lenderman.ncidpop.data.ServerData;
import com.lenderman.ncidpop.data.ServerDataManager;
import com.lenderman.ncidpop.listmgmt.ServerRequestManager;
import com.lenderman.ncidpop.listmgmt.ServerRequestManager.ServerRequestType;
import com.lenderman.ncidpop.power.PowerManagerHelper;
import com.lenderman.ncidpop.power.PowerNotifyIF;
import com.lenderman.ncidpop.socket.SocketTask;
import com.lenderman.ncidpop.socket.SocketTask.DataBlock;
import com.lenderman.ncidpop.socket.SocketTask.ServerResponseRequired;
import com.lenderman.ncidpop.socket.SocketTaskDataIF;
import com.lenderman.ncidpop.utils.LogParserUtils;
import com.lenderman.ncidpop.utils.NcidConstants;
import com.lenderman.ncidpop.utils.NotificationUtils;
import com.lenderman.ncidpop.utils.OsUtils;
import com.lenderman.ncidpop.utils.PrefUtils;
import com.lenderman.ncidpop.utils.ServerApiUtils;
import com.lenderman.ncidpop.utils.ServerCapabilityAssessmentUtils;
import com.lenderman.ncidpop.utils.ServerMessageUtils;
import com.lenderman.ncidpop.utils.SoundUtils;

/**
 * Controls the NCID Pop Main Display
 * 
 * @author Chris Lenderman
 */
public class NcidPopMainDisplayController implements SocketTaskDataIF
{
    /**
     * A buffered call history which allows for correlation between call info
     * and call information
     */
    private HashMap<Integer, HashMap<String, String>> bufferCallHistory = null;

    /**
     * An info received history which helps determine if we have received an
     * info line for the call in question
     */
    private HashMap<Integer, HashSet<String>> infoReceivedHistory = null;

    /** The server restart interval in millis */
    private static final int SERVER_RESTART_INTERVAL = 10000;

    /** The server keep alive interval in millis */
    private static final int SERVER_KEEP_ALIVE_INTERVAL = 60000;

    /** The log update timeout interval in millis */
    private static final int LOG_UPDATE_TIMEOUT_INTERVAL = 2500;

    /** The log request interval in millis */
    private static final int LOG_REQUEST_INTERVAL = 5000;

    /** Reconnect timer */
    private Timer reconnectTimer = null;

    /** Log update timeout timer */
    private Timer logUpdateTimeoutTimer = null;

    /** Log reload request timer */
    private Timer logReloadRequestTimer = null;

    /** Reference to the NCID Pop Main Display */
    private NcidPopMainDisplay mainDisplay;

    /**
     * Log update timer task used to determine if a log update time threshold
     * has passed.
     */
    private class LogUpdateTimerTask extends TimerTask
    {
        /** @inheritDoc */
        @Override
        public void run()
        {
            boolean rescheduleTimer = false;
            long nextTimerInterval = LOG_UPDATE_TIMEOUT_INTERVAL;

            for (ServerData data : ServerDataManager.getAllServerData())
            {
                long interval = System.currentTimeMillis()
                        - data.lastLogDataReceiveTime;

                if (interval <= LOG_UPDATE_TIMEOUT_INTERVAL)
                {
                    rescheduleTimer |= data.isLogUpdateInProgress;
                }
                else if (data.isLogUpdateInProgress)
                {
                    onCallLogStatusUpdate(data.uniqueInstanceId, true);
                }

                if ((interval > 0) && (interval < nextTimerInterval))
                {
                    nextTimerInterval = interval;
                }
            }

            if (rescheduleTimer)
            {
                logUpdateTimeoutTimer = new Timer();
                logUpdateTimeoutTimer.schedule(new LogUpdateTimerTask(),
                        nextTimerInterval);
            }
            else
            {
                logUpdateTimeoutTimer = null;
            }
        }
    }

    /**
     * Log reload timer task used to determine if a log update time threshold
     * has passed.
     */
    private class LogReloadTimerTask extends TimerTask
    {
        /** @inheritDoc */
        @Override
        public void run()
        {
            boolean rescheduleTimer = false;
            long nextTimerInterval = LOG_REQUEST_INTERVAL;

            for (ServerData data : ServerDataManager.getAllServerData())
            {
                long interval = System.currentTimeMillis()
                        - data.lastInitiatedLogReloadRequestTime;

                if (interval <= LOG_REQUEST_INTERVAL)
                {
                    rescheduleTimer |= data.initiateLogReloadRequestPending;
                }
                else if (data.initiateLogReloadRequestPending)
                {
                    data.initiateLogReloadRequestPending = false;
                    ServerRequestManager.requestCallLoadReread(data);
                }

                if ((interval > 0) && (interval < nextTimerInterval))
                {
                    nextTimerInterval = interval;
                }
            }

            if (rescheduleTimer)
            {
                logReloadRequestTimer = new Timer();
                logReloadRequestTimer.schedule(new LogReloadTimerTask(),
                        nextTimerInterval);
            }
            else
            {
                logReloadRequestTimer = null;
            }
        }
    }

    /**
     * Constructor
     */
    public NcidPopMainDisplayController(NcidPopMainDisplay mainDisplay)
    {
        bufferCallHistory = new HashMap<Integer, HashMap<String, String>>();
        infoReceivedHistory = new HashMap<Integer, HashSet<String>>();

        this.mainDisplay = mainDisplay;
        mainDisplay.initialize();

        Preferences.userNodeForPackage(PrefUtils.class)
                .addPreferenceChangeListener(new PreferenceChangeListener()
                {
                    /** @inheritDoc */
                    public void preferenceChange(PreferenceChangeEvent evt)
                    {
                        if (evt.getKey().equals(PrefUtils.PREF_SERVERS)
                                || evt.getKey()
                                        .equals(PrefUtils.PREF_ENABLE_SMART_PHONE_TEXT_NOTIFY)
                                || evt.getKey().equals(
                                        PrefUtils.PREF_USE_EUROPEAN_DATE_TIME)
                                || evt.getKey().equals(
                                        PrefUtils.PREF_USE_SYSTEM_ADDRESS_BOOK)
                                || evt.getKey()
                                        .equals(PrefUtils.PREF_REQUEST_CALL_LOG_FROM_SERVER))
                        {
                            refreshServers();

                            if (evt.getKey().equals(PrefUtils.PREF_SERVERS))
                            {
                                PrefUtils.instance.setString(
                                        PrefUtils.PREF_TOTAL_CALL_COUNT,
                                        PrefUtils.DEFAULT_TOTAL_CALL_COUNT);
                                PrefUtils.instance.setString(
                                        PrefUtils.PREF_TOTAL_MSG_COUNT,
                                        PrefUtils.DEFAULT_TOTAL_MSG_COUNT);
                            }
                        }
                    }
                });

        refreshServers();

        PowerManagerHelper.addListener(new PowerNotifyIF()
        {
            /** @inheritDoc */
            public void onBatteryLow(int percentage)
            {
                for (ServerData data : ServerDataManager.getAllServerData())
                {
                    ServerMessageUtils.sendMessageData(data, "Battery low: "
                            + percentage + "%");
                }
            }

            /** @inheritDoc */
            public void onBatteryCharged()
            {
                for (ServerData data : ServerDataManager.getAllServerData())
                {
                    ServerMessageUtils.sendMessageData(data, "Battery charged");
                }
            }
        });

        scheduleServerKeepAliveTimer();
    }

    /**
     * Refreshes the server connections
     */
    private void refreshServers()
    {
        ServerDataManager.killAllServers();

        StringTokenizer tk = new StringTokenizer(
                PrefUtils.instance.getString(PrefUtils.PREF_SERVERS), ",");
        ArrayList<String> strings = new ArrayList<String>();

        while ((tk.hasMoreElements()))
        {
            strings.add(tk.nextToken());
        }

        for (String serverNamePort : strings)
        {
            tk = new StringTokenizer(serverNamePort, ":");

            String name = tk.nextToken();
            int port = NcidConstants.DEFAULT_SERVER_PORT;

            if (tk.hasMoreTokens())
            {
                try
                {
                    port = Integer.parseInt(tk.nextToken());
                }
                catch (NumberFormatException ex)
                {
                    // Do nothing. We will use the default port.
                }
            }

            final ServerData data = new ServerData(name.trim(), port);

            data.serverThread = new Thread(new Runnable()
            {
                /** @inheritDoc */
                public void run()
                {
                    new SocketTask().start(data,
                            NcidPopMainDisplayController.this);
                }
            });

            ServerDataManager.addServerData(data);
            data.serverThread.start();
        }
        mainDisplay.onServerRefresh();
    }

    /**
     * Prepares for a log update by initiating variables and updating displays
     * 
     * @param ServerData data
     */
    private void initializeLogUpdate(ServerData data)
    {
        data.isLogUpdateInProgress = true;
        data.callLog.clear();
        data.msgLog.clear();

        Runnable runnable = new Runnable()
        {
            /** @inheritDoc */
            public void run()
            {
                mainDisplay.updateTrayTip();
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    /**
     * Responds to a log update for a given instance
     * 
     * @param int server unique instance ID
     */
    private void respondToLogUpdate(final int instanceId)
    {
        ServerData data = ServerDataManager
                .getServerDataForUniqueId(instanceId);

        if (data != null)
        {
            data.lastLogDataReceiveTime = System.currentTimeMillis();
        }

        // If a log update timeout timer is not scheduled, create and schedule
        // one
        if (logUpdateTimeoutTimer == null)
        {
            logUpdateTimeoutTimer = new Timer();
            logUpdateTimeoutTimer.schedule(new LogUpdateTimerTask(),
                    LOG_UPDATE_TIMEOUT_INTERVAL);
        }
    }

    /** @inheritDoc */
    public void onServerError(final int instanceId, String error)
    {
        onServerDisconnect(instanceId);

        ServerData data = ServerDataManager
                .getServerDataForUniqueId(instanceId);
        if (data == null)
        {
            return;
        }

        data.serverRestartRequested = true;
        scheduleServerRestartTimer();
    }

    /** @inheritDoc */
    public void onServerNameUpdate(final int instanceId, final String serverName)
    {
        ServerData data = ServerDataManager
                .getServerDataForUniqueId(instanceId);

        if (data != null)
        {
            data.displayName = serverName;
        }

        Runnable runnable = new Runnable()
        {
            public void run()
            {
                mainDisplay.updateCallHistoryServerName();
                mainDisplay.updateTrayTip();
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    /** @inheritDoc */
    public void onCallLogUpdate(final int instanceId, final String callLogEntry)
    {
        respondToLogUpdate(instanceId);

        ServerData data = ServerDataManager
                .getServerDataForUniqueId(instanceId);

        if (data != null)
        {
            if (!data.isLogUpdateInProgress)
            {
                initializeLogUpdate(data);
            }
            data.callLog.add(LogParserUtils.tokenizeCallLine(callLogEntry));
        }
    }

    /** @inheritDoc */
    public void onServerConnect(final int instanceId)
    {
        ServerData data = ServerDataManager
                .getServerDataForUniqueId(instanceId);

        if (data != null)
        {
            data.isServerConnected = true;
            data.callLog.clear();
            data.msgLog.clear();
            data.serverOptionList.clear();
            ServerCapabilityAssessmentUtils
                    .setDefaultCapabilities(data.supportedCapability);
        }

        Runnable runnable = new Runnable()
        {
            /** @inheritDoc */
            public void run()
            {
                mainDisplay.refreshGuis(ServerDataManager
                        .getDisplayIdForUnique(instanceId));
                mainDisplay.updateConnectedIcon();
                mainDisplay.updateTrayTip();
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    /** @inheritDoc */
    public void onServerDisconnect(final int instanceId)
    {
        ServerData data = ServerDataManager
                .getServerDataForUniqueId(instanceId);

        if (data != null)
        {
            data.isServerConnected = false;
            data.displayName = ServerData.DEFAULT_DISPLAY_NAME;
        }

        Runnable runnable = new Runnable()
        {
            /** @inheritDoc */
            public void run()
            {
                mainDisplay.refreshGuis(ServerDataManager
                        .getDisplayIdForUnique(instanceId));
                mainDisplay.updateConnectedIcon();
                mainDisplay.updateTrayTip();
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    /** @inheritDoc */
    public void onCallLogStatusUpdate(final int instanceId, boolean callLogSent)
    {
        ServerData data = ServerDataManager
                .getServerDataForUniqueId(instanceId);

        if (!callLogSent
                && PrefUtils.instance
                        .getBoolean(PrefUtils.PREF_REQUEST_CALL_LOG_FROM_SERVER))
        {
            data.lastInitiatedLogReloadRequestTime = System.currentTimeMillis();
            data.initiateLogReloadRequestPending = true;

            // If a log reload request timer is not scheduled, create and
            // schedule one
            if (logReloadRequestTimer == null)
            {
                logReloadRequestTimer = new Timer();
                logReloadRequestTimer.schedule(new LogReloadTimerTask(),
                        LOG_REQUEST_INTERVAL);
            }

            return;
        }

        if (data != null)
        {
            data.isLogUpdateInProgress = false;
        }

        Runnable runnable = new Runnable()
        {
            /** @inheritDoc */
            public void run()
            {
                mainDisplay.refreshGuis(ServerDataManager
                        .getDisplayIdForUnique(instanceId));
                mainDisplay.updateTrayTip();

                if (ServerDataManager.allLogUpdatesComplete())
                {
                    String popupMessage = null;

                    int lastCallCount = Integer.parseInt(PrefUtils.instance
                            .getString(PrefUtils.PREF_TOTAL_CALL_COUNT));
                    int lastMsgCount = Integer.parseInt(PrefUtils.instance
                            .getString(PrefUtils.PREF_TOTAL_MSG_COUNT));

                    if (lastCallCount < ServerDataManager.getTotalCallCount())
                    {
                        popupMessage = (ServerDataManager.getTotalCallCount() - lastCallCount)
                                + " New Calls";
                    }

                    if (lastMsgCount < ServerDataManager.getTotalMessageCount())
                    {
                        popupMessage = popupMessage != null ? (popupMessage + "\n")
                                : "";

                        popupMessage += (ServerDataManager
                                .getTotalMessageCount() - lastMsgCount)
                                + " New Messages";
                    }

                    PrefUtils.instance.setString(
                            PrefUtils.PREF_TOTAL_CALL_COUNT, Integer
                                    .toString(ServerDataManager
                                            .getTotalCallCount()));

                    PrefUtils.instance.setString(
                            PrefUtils.PREF_TOTAL_MSG_COUNT, Integer
                                    .toString(ServerDataManager
                                            .getTotalMessageCount()));

                    if (popupMessage != null)
                    {
                        mainDisplay.showCallsNotificationsPopup(popupMessage,
                                null);
                    }
                }
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    /** @inheritDoc */
    public void onNewMessage(final int instanceId, final String type,
            final String message)
    {
        if (type.equals(NcidConstants.SMART_PHONE_MESSAGE_TYPE)
                && !PrefUtils.instance
                        .getBoolean(PrefUtils.PREF_ENABLE_SMART_PHONE_TEXT_NOTIFY))
        {
            return;
        }

        ServerData data = ServerDataManager
                .getServerDataForUniqueId(instanceId);

        final MessageData messageData = new MessageData(type, message);
        if (data != null)
        {
            data.msgLog.add(messageData);

            PrefUtils.instance.setString(PrefUtils.PREF_TOTAL_MSG_COUNT,
                    Integer.toString(ServerDataManager.getTotalMessageCount()));
        }

        Runnable runnable = new Runnable()
        {
            /** @inheritDoc */
            public void run()
            {
                mainDisplay.refreshMsgGui(ServerDataManager
                        .getDisplayIdForUnique(instanceId));
                mainDisplay.updateTrayTip();

                if (!messageData.msgType.equals("OUT"))
                {
                    if (PrefUtils.instance
                            .getBoolean(PrefUtils.PREF_DISPLAY_INCOMING_MESSAGE_POPUPS))
                    {
                        mainDisplay.showMessagesPopup(messageData);
                    }

                    if (PrefUtils.instance
                            .getBoolean(PrefUtils.PREF_READ_MESSAGES))
                    {
                        SoundUtils.sayIt(messageData
                                .encodePrettyPrintWithAlias(PrefUtils.instance,
                                        false, OsUtils
                                                .determineAddressBookAlias(
                                                        messageData.name,
                                                        messageData.number)),
                                PrefUtils.instance);
                    }
                }
            }
        };
        SwingUtilities.invokeLater(runnable);

    }

    /** @inheritDoc */
    public void onNewCall(final int instanceId, final String call)
    {
        ServerData data = ServerDataManager
                .getServerDataForUniqueId(instanceId);

        if (data != null)
        {
            data.callLog.add(LogParserUtils.tokenizeCallLine(call));
            PrefUtils.instance.setString(PrefUtils.PREF_TOTAL_CALL_COUNT,
                    Integer.toString(ServerDataManager.getTotalCallCount()));
        }

        final ArrayList<String> strings = LogParserUtils.tokenizeCallLine(call);

        // Buffer off this call for purposes of name reading on subsequent
        // CIDINFO lines.
        if (bufferCallHistory.get(instanceId) == null)
        {
            bufferCallHistory.put(instanceId, new HashMap<String, String>());
        }

        bufferCallHistory.get(instanceId)
                .put(LogParserUtils.getValueForKey(strings,
                        LogParserUtils.LINE_KEY), call);

        String line = LogParserUtils.getValueForKey(strings,
                LogParserUtils.LINE_KEY);

        final boolean isIncomingCall = NcidConstants.isIncomingCallLine(call);

        // If there is no info received history for this line, go ahead and play
        // the ringtone clip.
        if ((infoReceivedHistory.get(instanceId) == null)
                || !infoReceivedHistory.get(instanceId).contains(line))
        {
            if (isIncomingCall)
            {
                SoundUtils.playClip(
                        PrefUtils.instance.getRingtonePathForPreference(),
                        PrefUtils.instance);
            }
        }
        else
        {
            infoReceivedHistory.get(instanceId).remove(line);
        }

        boolean readCallerName = (isIncomingCall && PrefUtils.instance
                .getBoolean(PrefUtils.PREF_READ_INCOMING_CALLER_NAMES))
                || (!isIncomingCall && PrefUtils.instance
                        .getBoolean(PrefUtils.PREF_READ_OUTGOING_AND_STATUS_CALLER_NAMES));

        if (readCallerName)
        {
            SoundUtils.sayIt(OsUtils.determineAddressBookAlias(LogParserUtils
                    .getValueForKey(strings, LogParserUtils.NAME_KEY),
                    LogParserUtils.getValueForKey(strings,
                            LogParserUtils.NUMBER_KEY)), PrefUtils.instance);
        }

        Runnable runnable = new Runnable()
        {
            /** @inheritDoc */
            public void run()
            {
                final boolean displayPopup = (isIncomingCall && PrefUtils.instance
                        .getBoolean(PrefUtils.PREF_DISPLAY_INCOMING_CALL_POPUPS))
                        || (!isIncomingCall && PrefUtils.instance
                                .getBoolean(PrefUtils.PREF_DISPLAY_OUTGOING_AND_STATUS_CALL_POPUPS));

                if (displayPopup)
                {
                    mainDisplay.showCallsNotificationsPopup(NotificationUtils
                            .buildCallNotificationText(strings,
                                    PrefUtils.instance), OsUtils
                            .getAddressBookImage(LogParserUtils.getValueForKey(
                                    strings, LogParserUtils.NUMBER_KEY)));
                }
                mainDisplay.refreshCallGui(ServerDataManager
                        .getDisplayIdForUnique(instanceId));
                mainDisplay.updateTrayTip();
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    /** @inheritDoc */
    public void onCallInfo(final int instanceId, final String string)
    {
        ArrayList<String> callInfoStrings = LogParserUtils
                .tokenizeCallLine(string);

        int ring = Integer.parseInt(LogParserUtils.getValueForKey(
                callInfoStrings, LogParserUtils.RING_KEY));

        if (ring > 0)
        {
            String infoLine = LogParserUtils.getValueForKey(callInfoStrings,
                    LogParserUtils.LINE_KEY);

            // Save off the fact that we received an info line.
            if (infoReceivedHistory.get(instanceId) == null)
            {
                infoReceivedHistory.put(instanceId, new HashSet<String>());
            }

            infoReceivedHistory.get(instanceId).add(infoLine);

            SoundUtils.playClip(
                    PrefUtils.instance.getRingtonePathForPreference(),
                    PrefUtils.instance);

            String callLine = null;

            if (bufferCallHistory.get(instanceId) != null)
            {
                callLine = bufferCallHistory.get(instanceId).get(infoLine);
            }

            String nameToRead = LogParserUtils.UNKNOWN_VALUE;

            if (callLine != null)
            {
                ArrayList<String> strings = LogParserUtils
                        .tokenizeCallLine(callLine);
                nameToRead = OsUtils.determineAddressBookAlias(LogParserUtils
                        .getValueForKey(strings, LogParserUtils.NAME_KEY),
                        LogParserUtils.getValueForKey(strings,
                                LogParserUtils.NUMBER_KEY));
            }

            if (PrefUtils.instance
                    .getBoolean(PrefUtils.PREF_READ_INCOMING_CALLER_NAMES)
                    && (!nameToRead.equals(LogParserUtils.UNKNOWN_VALUE)))
            {
                SoundUtils.sayIt(nameToRead, PrefUtils.instance);
            }
        }
        else
        {
            purgeHistoryDataForLine(instanceId, LogParserUtils.getValueForKey(
                    callInfoStrings, LogParserUtils.LINE_KEY));
        }
    }

    /**
     * Purges history data for a given line
     * 
     * @param int the instance ID
     * @param Sring the line to purge
     */
    private void purgeHistoryDataForLine(int instanceId, String line)
    {
        if (bufferCallHistory.get(instanceId) != null)
        {
            bufferCallHistory.get(instanceId).remove(line);
        }
    }

    /** @inheritDoc */
    public void onMsgLogUpdate(int instanceId, String type, String msgLogEntry)
    {
        respondToLogUpdate(instanceId);

        ServerData data = ServerDataManager
                .getServerDataForUniqueId(instanceId);

        if (data != null)
        {
            if (!data.isLogUpdateInProgress)
            {
                initializeLogUpdate(data);
            }

            if (type.equals(NcidConstants.SMART_PHONE_MESSAGE_TYPE)
                    && !PrefUtils.instance
                            .getBoolean(PrefUtils.PREF_ENABLE_SMART_PHONE_TEXT_NOTIFY))
            {
                return;
            }
            data.msgLog.add(new MessageData(type, msgLogEntry));
        }
    }

    /** @inheritDoc */
    public void onNewServerData(int instanceId, final DataBlock block)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            /** @inheritDoc */
            public void run()
            {
                if (block.serverResponseRequired == ServerResponseRequired.OK)
                {
                    String message = new String();
                    for (String str : block.lines)
                    {
                        message += str + "\n";
                    }

                    mainDisplay.showDialogPopupMessage(message, false);
                }
                else if (block.serverResponseRequired == ServerResponseRequired.INFO_SELECTION_RESPONSE)
                {
                    if (ServerRequestManager.getLastServerRequestType() == ServerRequestType.INFO)
                    {
                        ServerRequestManager.processInfoResponse(block.lines);
                        mainDisplay.onInfoUpdate();
                    }
                }
                else if (block.serverResponseRequired == ServerResponseRequired.HANDLED_REQUEST_RESPONSE)
                {
                    if ((ServerRequestManager.getLastServerRequestType() == ServerRequestType.UPDATE_ALIASES)
                            || (ServerRequestManager.getLastServerRequestType() == ServerRequestType.UPDATE_BLACK_WHITE))
                    {

                        if (ServerRequestManager
                                .serverResponseIsSuccessful(block.lines))
                        {
                            mainDisplay.respondToListUpdate(ServerRequestManager
                                    .getLastServerRequestType() == ServerRequestType.UPDATE_ALIASES);
                        }
                        else
                        {
                            String message = new String();
                            for (String str : block.lines)
                            {
                                message += str + "\n";
                            }

                            mainDisplay.showDialogPopupMessage(message, false);
                        }
                    }
                }
                else if (block.serverResponseRequired == ServerResponseRequired.ACCEPT_REJECT)
                {
                    String message = new String();
                    for (String str : block.lines)
                    {
                        message += str + "\n";
                    }
                    mainDisplay.showAcceptReject(message);
                }
            }
        });
    }

    /** @inheritDoc */
    public void onServerOption(int instanceId, String option)
    {
        ServerData data = ServerDataManager
                .getServerDataForUniqueId(instanceId);

        data.serverOptionList.add(option);

        if (data != null)
        {
            if (option.contains("hangup"))
            {
                ServerCapabilityAssessmentUtils
                        .setHangupOptionSupported(data.supportedCapability);
            }
        }
    }

    /** @inheritDoc */
    public void onServerApiMessage(int instanceId, String message)
    {
        ServerData data = ServerDataManager
                .getServerDataForUniqueId(instanceId);

        if (data != null)
        {
            ServerApiUtils.processServerApiVersion(message, data);
            mainDisplay.evaluateEnabledFields(instanceId);
        }
    }

    /**
     * Schedules the server keep alive timer
     */
    private void scheduleServerKeepAliveTimer()
    {
        (new Timer()).scheduleAtFixedRate(new TimerTask()
        {
            /** @inheritDoc */
            @Override
            public void run()
            {
                ServerDataManager.enqueueKeepAliveForAllServers();
            }
        }, SERVER_KEEP_ALIVE_INTERVAL, SERVER_KEEP_ALIVE_INTERVAL);
    }

    /**
     * Schedules the server restart timer
     */
    private void scheduleServerRestartTimer()
    {
        // Only schedule timer if one is not already scheduled
        if (reconnectTimer == null)
        {
            reconnectTimer = new Timer();
            reconnectTimer.schedule(new TimerTask()
            {
                /** @inheritDoc */
                @Override
                public void run()
                {
                    for (final ServerData data : ServerDataManager
                            .getAllServerData())
                    {
                        if (data.serverRestartRequested)
                        {
                            data.serverRestartRequested = false;

                            if (!data.isServerConnected)
                            {
                                data.serverThread = new Thread(new Runnable()
                                {
                                    /** @inheritDoc */
                                    public void run()
                                    {
                                        new SocketTask()
                                                .start(data,
                                                        NcidPopMainDisplayController.this);
                                    }
                                });
                                data.serverThread.start();
                            }
                        }
                    }
                    reconnectTimer = null;
                }
            }, SERVER_RESTART_INTERVAL);
        }
    }
}