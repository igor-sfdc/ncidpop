/*******************************************************
 * Socket Task Data Interface Definition
 *******************************************************/
package com.lenderman.ncidpop.socket;

import com.lenderman.ncidpop.socket.SocketTask.DataBlock;

/**
 * Call back used by the NcidService to receive status and data from the
 * SocketTask
 * 
 * @author Chris Lenderman
 */
public interface SocketTaskDataIF
{
    /**
     * Called back on server error
     * 
     * @param int the socket instance ID
     * @param String the error message
     */
    public void onServerError(int instanceId, String error);

    /**
     * Called back on server name update
     * 
     * @param int the socket instance ID
     * @param String the server name
     */
    public void onServerNameUpdate(int instanceId, String serverName);

    /**
     * Called back on call log update
     * 
     * @param int the socket instance ID
     * @param String the call log entry
     */
    public void onCallLogUpdate(int instanceId, String callLogEntry);

    /**
     * Called back on server connect
     * 
     * @param int the socket instance ID
     */
    public void onServerConnect(int instanceId);

    /**
     * Called back on server disconnect
     * 
     * @param int the socket instance ID
     */
    public void onServerDisconnect(int instanceId);

    /**
     * Called back on call info receipt
     * 
     * @param int the socket instance ID
     * @param String the call info
     */
    public void onCallInfo(int instanceId, String string);

    /**
     * Called back when a call log status update is provided by the server
     * 
     * @param int the socket instance ID
     * @param boolean indicates whether the call log was sent
     */
    public void onCallLogStatusUpdate(int instanceId, boolean callLogSent);

    /**
     * Called back when a message log update is available
     * 
     * @param int the socket instance ID
     * @param String the type of message
     * @param String the message
     */
    public void onMsgLogUpdate(int instanceId, String type, String msgLogEntry);

    /**
     * Called back when a message is available
     * 
     * @param int the socket instance ID
     * @param String the type of message
     * @param String the message
     */
    public void onNewMessage(int instanceId, String type, String message);

    /**
     * Called back when a new call is received
     * 
     * @param int the socket instance ID
     * @param String the call string
     */
    public void onNewCall(int instanceId, String call);

    /**
     * Called back when new server data is available
     * 
     * @param int the socket instance ID
     * @param DataBlock the data block from the server
     */
    public void onNewServerData(int instanceId, DataBlock block);

    /**
     * Called back when a new server option message is available
     * 
     * @param int the socket instance ID
     * @param String the option
     */
    public void onServerOption(int instanceId, String option);

    /**
     * Called back when a new server API message is available
     * 
     * @param int the socket instance ID
     * @param String the API message
     */
    public void onServerApiMessage(int instanceId, String message);
}
