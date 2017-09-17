/*******************************************************
 * Server Data Manager
 *******************************************************/
package com.lenderman.ncidpop.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class handles all interactions with server data
 * 
 * @author Chris Lenderman
 */
public class ServerDataManager
{
    /** The current server display ID */
    private static int currentServerDisplayId = 0;

    /** Data associated with the server. Indexed by server unique ID */
    private static ConcurrentHashMap<Integer, ServerData> serverData = new ConcurrentHashMap<Integer, ServerData>();;

    /** Maps between Server Display and Server Unique ID */
    private static HashMap<Integer, Integer> serverDisplayIdToUniqueId = new HashMap<Integer, Integer>();
    private static HashMap<Integer, Integer> serverUniqueIdToDisplayId = new HashMap<Integer, Integer>();

    /** The next available server unique ID */
    private static int nextUniqueId = 0;

    /**
     * Decrements and returns the current server display ID
     * 
     * @return int
     */
    public static int decrementCurrentServerDisplayId()
    {
        currentServerDisplayId = currentServerDisplayId - 1;
        if (currentServerDisplayId < 0)
        {
            currentServerDisplayId = getServerCount() - 1;
        }
        return currentServerDisplayId;
    }

    /**
     * Increments and returns the current server display ID
     * 
     * @return int
     */
    public static int incrementCurrentServerDisplayId()
    {
        currentServerDisplayId = currentServerDisplayId + 1;
        if (currentServerDisplayId >= ServerDataManager.getServerCount())
        {
            currentServerDisplayId = 0;
        }
        return currentServerDisplayId;
    }

    /**
     * Returns the current server display ID
     * 
     * @return int
     */
    public static int getCurrentServerDisplayId()
    {
        return currentServerDisplayId;
    }

    /**
     * Resets the current server display ID
     */
    public static void resetCurrentServerDisplayId()
    {
        currentServerDisplayId = 0;
    }

    /**
     * Adds new server data
     * 
     * @param ServerData the server data to add
     */
    public static void addServerData(ServerData data)
    {
        data.uniqueInstanceId = nextUniqueId++;
        serverDisplayIdToUniqueId.put(serverData.size(), data.uniqueInstanceId);
        serverUniqueIdToDisplayId.put(data.uniqueInstanceId, serverData.size());
        serverData.put(data.uniqueInstanceId, data);
    }

    /**
     * Given a unique ID, returns a display ID
     * 
     * @param int unique ID
     * @return int display ID, or -1 if invalid
     */
    public static int getDisplayIdForUnique(int uniqueId)
    {
        Integer result = serverUniqueIdToDisplayId.get(uniqueId);

        if (result == null)
        {
            result = -1;
        }
        return result;
    }

    /**
     * Kills all existing server connections
     */
    public static void killAllServers()
    {
        // Kill any lingering threads
        for (ServerData data : serverData.values())
        {
            data.serverThread.interrupt();
        }

        // Clear metadata
        serverData.clear();
        serverDisplayIdToUniqueId.clear();
        serverUniqueIdToDisplayId.clear();
    }

    /**
     * Enqueues a keep alive for all servers
     */
    public static void enqueueKeepAliveForAllServers()
    {
        for (ServerData data : serverData.values())
        {
            data.serverEnqueuedLines.add("\n");
        }
    }

    /**
     * Returns the server count
     * 
     * @return int the server count
     */
    public static int getServerCount()
    {
        return serverData.size();
    }

    /**
     * Determines if any log updates are in progress
     * 
     * @return true if any log updates are in progress
     */
    public static boolean anyLogUpdateInProgress()
    {
        for (ServerData data : serverData.values())
        {
            if (data.isLogUpdateInProgress)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if all log updates are complete
     * 
     * @return true if any log updates are in progress
     */
    public static boolean allLogUpdatesComplete()
    {
        for (ServerData data : serverData.values())
        {
            if (data.isLogUpdateInProgress)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the total call count
     * 
     * @return int total call count
     */
    public static int getTotalCallCount()
    {
        int count = 0;
        for (ServerData data : serverData.values())
        {
            count += data.callLog.size();
        }
        return count;
    }

    /**
     * Gets the total message count
     * 
     * @return int total message count
     */
    public static int getTotalMessageCount()
    {
        int count = 0;
        for (ServerData data : serverData.values())
        {
            count += data.msgLog.size();
        }
        return count;
    }

    /**
     * Returns server data for all servers
     * 
     * @return a collection of server data
     */
    public static Collection<ServerData> getAllServerData()
    {
        return serverData.values();
    }

    /**
     * Given a unique ID, returns the server data
     * 
     * @param int the unique ID
     * @return the server data
     */
    public static ServerData getServerDataForUniqueId(int uniqueId)
    {
        return serverData.get(uniqueId);
    }

    /**
     * Gets the socket connection count
     * 
     * @return int number of connected sockets
     */
    public static int getCurrentSocketConnectionCount()
    {
        int count = 0;
        for (ServerData data : serverData.values())
        {
            if (data.isServerConnected)
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the server data for the current display ID
     * 
     * @return the server data
     */
    public static ServerData getServerDataForCurrentDisplayId()
    {
        Integer serverUniqueId = serverDisplayIdToUniqueId
                .get(currentServerDisplayId);

        if (serverUniqueId != null)
        {
            return serverData.get(serverUniqueId);
        }
        return null;
    }
}