/*******************************************************
 * Server Data
 *******************************************************/
package com.lenderman.ncidpop.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import com.lenderman.ncidpop.utils.ServerCapabilityAssessmentUtils.ServerCapability;

/**
 * A simple class for maintaining data associated with a server connection
 * instance
 * 
 * @author Chris Lenderman
 */
public class ServerData
{
    /** The default server display name */
    public static final String DEFAULT_DISPLAY_NAME = "Server: (unknown)";

    /** The server host name for the socket connection */
    public String hostName;

    /** The server host port for the socket connection */
    public int hostPort;

    /** The thread associated with this server instance */
    public Thread serverThread;

    /** An indication of whether a log update is in progress */
    public boolean isLogUpdateInProgress = false;

    /** An indication of whether this server is connected to a socket */
    public boolean isServerConnected = false;

    /** An indication of whether a restart is pending for this server */
    public boolean serverRestartRequested = false;

    /** The display name for this server as received from the server */
    public String displayName = DEFAULT_DISPLAY_NAME;

    /** The call log associated with this server */
    public ArrayList<ArrayList<String>> callLog = new ArrayList<ArrayList<String>>();

    /** The message log associated with this server */
    public ArrayList<MessageData> msgLog = new ArrayList<MessageData>();

    /** The time stamp of the last received log update */
    public long lastLogDataReceiveTime = 0;

    /** The time stamp of the last initiate log reload request time */
    public long lastInitiatedLogReloadRequestTime = 0;

    /** The request to initiate a log reload */
    public boolean initiateLogReloadRequestPending = false;

    /** List of server lines to enqueue */
    public LinkedBlockingQueue<String> serverEnqueuedLines = new LinkedBlockingQueue<String>();

    /** The unique ID associated with this server */
    public int uniqueInstanceId;

    /** The API version associated with this server */
    public String apiVersion = "0.0";

    /** List of supported server capabilities */
    public HashSet<ServerCapability> supportedCapability = new HashSet<ServerCapability>();

    /** List of server options */
    public HashSet<String> serverOptionList = new HashSet<String>();

    /**
     * Constructor
     */
    public ServerData(String hostName, int hostPort)
    {
        this.hostName = hostName;
        this.hostPort = hostPort;
    }
}