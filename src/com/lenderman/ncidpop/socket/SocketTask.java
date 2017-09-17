/*******************************************************
 * Socket Task
 *******************************************************/
package com.lenderman.ncidpop.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
import com.lenderman.ncidpop.data.ServerData;
import com.lenderman.ncidpop.utils.NcidConstants;

/**
 * The SocketTask is an asynchronous background task that communicates with the
 * NCID server.
 * 
 * @author Chris Lenderman
 */
public class SocketTask
{
    /**
     * Enum class which specifies the type of server response required
     */
    public enum ServerResponseRequired
    {
        OK, ACCEPT_REJECT, HANDLED_REQUEST_RESPONSE, INFO_SELECTION_RESPONSE
    }

    /**
     * Class for holding server data blocks
     */
    public class DataBlock
    {
        /** List of server lines */
        public ArrayList<String> lines = new ArrayList<String>();

        /** Type of server response required */
        public ServerResponseRequired serverResponseRequired = null;

        /**
         * Constructor
         */
        public DataBlock(ServerResponseRequired response)
        {
            this.serverResponseRequired = response;
        }
    }

    /** Class logger */
    private Logger log = Logger.getLogger(SocketTask.class);

    /** A socket object for talking to the server */
    private Socket socket;

    /** Reader for reading data from the socket */
    private BufferedReader reader;

    /** Writer for writing data to the socket */
    private PrintWriter writer;

    /** The callback for socket task data */
    private SocketTaskDataIF dataCallback;

    /** The instance ID associated with this socket */
    private int instanceId = 0;

    /** The server enqueued lines associated with this socket */
    private LinkedBlockingQueue<String> enqueuedLines = null;

    /** Whether or not server startup is complete */
    private boolean serverStartupComplete = false;

    /** The current server data block response */
    private DataBlock currentServerDataBlock = null;

    /** The main thread loop */
    private Thread mainThread;

    /**
     * This routine is called when we want to close the socket
     */
    private void closeSocket()
    {
        log.debug("SocketTask - closeSocket");

        // Close the socket if not null
        if (this.socket != null)
        {
            try
            {
                this.socket.close();
            }
            catch (IOException e)
            {
                log.error("Could not close socket: ", e);
            }

            this.socket = null;
        }

        if (this.reader != null)
        {
            try
            {
                this.reader.close();
            }
            catch (IOException e)
            {
                log.error("Could not close reader: ", e);
            }
            this.reader = null;
        }

        if (this.writer != null)
        {
            this.writer.close();
            this.writer = null;
        }
    }

    /**
     * Processes a given line of the server data
     * 
     * @param String the line to process
     */
    private void processLine(String line)
    {
        if (line != null)
        {
            if (NcidConstants.isMsgLogLine(line))
            {
                dataCallback.onMsgLogUpdate(instanceId, line.substring(0, 3),
                        line.substring(8, line.length()));
            }
            else if (NcidConstants.isMsgLine(line))
            {
                dataCallback.onNewMessage(instanceId, line.substring(0, 3),
                        line.substring(5, line.length()));
            }
            else if (line.indexOf(NcidConstants.SERVER_INFO_MSG_HEADER) == 0)
            {
                String ncidServerName = line.substring(
                        NcidConstants.SERVER_INFO_MSG_HEADER.length(),
                        line.length());

                this.dataCallback
                        .onServerNameUpdate(instanceId, ncidServerName);
            }
            else if (line.indexOf(NcidConstants.CALL_INFO_HEADER) == 0)
            {
                this.dataCallback.onCallInfo(instanceId, line);
            }
            else if (NcidConstants.isCidLogLine(line))
            {
                this.dataCallback.onCallLogUpdate(instanceId, line);
            }
            else if (line.indexOf(NcidConstants.SERVER_INFO_LINE) == 0)
            {
                if (currentServerDataBlock != null)
                {
                    currentServerDataBlock.lines.add(line.substring(
                            NcidConstants.SERVER_INFO_LINE.length(),
                            line.length()));
                }
            }
            else if (line.indexOf(NcidConstants.SERVER_RESP_LINE) == 0)
            {
                if (currentServerDataBlock != null)
                {
                    currentServerDataBlock.lines.add(line.substring(
                            NcidConstants.SERVER_RESP_LINE.length(),
                            line.length()));
                }
            }
            else if (line.indexOf(NcidConstants.SERVER_OK_BLOCK_BEGIN_HEADER) == 0)
            {
                currentServerDataBlock = new DataBlock(
                        ServerResponseRequired.OK);
            }
            else if (line.indexOf(NcidConstants.SERVER_OPTIONS_HEADER) == 0)
            {
                dataCallback.onServerOption(instanceId, line.substring(
                        NcidConstants.SERVER_OPTIONS_HEADER.length(),
                        line.length()));
            }
            else if (line
                    .indexOf(NcidConstants.SERVER_ACCEPT_REJECT_BEGIN_HEADER) == 0)
            {
                currentServerDataBlock = new DataBlock(
                        ServerResponseRequired.ACCEPT_REJECT);
            }
            else if (line
                    .indexOf(NcidConstants.SERVER_HANDLED_REQUEST_RESPONSE_BEGIN_HEADER) == 0)
            {
                currentServerDataBlock = new DataBlock(
                        ServerResponseRequired.HANDLED_REQUEST_RESPONSE);
            }
            else if (line
                    .indexOf(NcidConstants.SERVER_INFO_SELECTION_RESPONSE_BEGIN_HEADER) == 0)
            {
                currentServerDataBlock = new DataBlock(
                        ServerResponseRequired.INFO_SELECTION_RESPONSE);
            }
            else if ((line.indexOf(NcidConstants.SERVER_END_OF_DATA) == 0)
                    || (line.indexOf(NcidConstants.SERVER_END_OF_RESPONSE) == 0))
            {
                dataCallback
                        .onNewServerData(instanceId, currentServerDataBlock);
                currentServerDataBlock = null;
            }
            else if (line
                    .indexOf(NcidConstants.END_OF_SERVER_STARTUP_MSG_HEADER) == 0)
            {
                serverStartupComplete = true;
            }
            else if (NcidConstants.isCallLogMessageLine(line))
            {
                this.dataCallback.onCallLogStatusUpdate(instanceId,
                        !(line.indexOf(NcidConstants.CALL_LOG_NOT_SENT) == 0));
            }
            else if (NcidConstants.isCidCallLine(line))
            {
                this.dataCallback.onNewCall(instanceId, line);
            }
            else if (line.indexOf(NcidConstants.SERVER_API_VERSION_LINE) == 0)
            {
                dataCallback.onServerApiMessage(instanceId, line.substring(
                        NcidConstants.SERVER_API_VERSION_LINE.length(),
                        line.length()));
            }
        }
    }

    /**
     * Starts the socket task
     * 
     * @param ServerData server data associated with this socket instance
     * @param SocketTaskDataIF data callback associated with this socket
     *        instance
     */
    public void start(ServerData serverData, SocketTaskDataIF dataCallback)
    {
        this.instanceId = serverData.uniqueInstanceId;
        this.dataCallback = dataCallback;
        this.enqueuedLines = serverData.serverEnqueuedLines;

        log.debug("SocketTask - start.  Connection params: "
                + serverData.hostName + ":" + serverData.hostPort);

        // A thread for reading incoming server data
        Thread readThread = new Thread(new Runnable()
        {
            /** @inheritDoc */
            public void run()
            {
                SocketTask.this.receiveData();
            }
        });

        try
        {
            this.socket = new Socket(serverData.hostName, serverData.hostPort);

            this.socket.setKeepAlive(true);

            this.writer = new PrintWriter(this.socket.getOutputStream(), true);

            this.reader = new BufferedReader(new InputStreamReader(
                    this.socket.getInputStream(), "UTF-8"));

            this.dataCallback.onServerConnect(instanceId);

            // Start a thread to receive data
            readThread.start();

            mainThread = Thread.currentThread();

            // Process enqueued lines and send them to the server
            while (!mainThread.isInterrupted())
            {
                while (!serverStartupComplete && !mainThread.isInterrupted())
                {
                    Thread.sleep(500);
                }

                if (mainThread.isInterrupted())
                {
                    break;
                }

                Thread.sleep(20);

                String element = enqueuedLines.take();
                if (element != null)
                {
                    this.writer.println(element);
                    this.writer.flush();
                }
            }
        }
        catch (Exception ex)
        {
            if (!(ex instanceof InterruptedException))
            {
                log.error("SocketTask start terminating - call handleServerError: "
                        + ex.toString());
                this.handleServerClose(ex.toString());
            }
        }

        // Cancel our read thread
        readThread.interrupt();

        // Attempt to close the connection, which will do nothing if already
        // closed.
        this.handleServerClose(null);

        log.debug("SocketTask - end start.");
    }

    /**
     * Method for receiving data from the socket
     */
    private void receiveData()
    {
        try
        {
            String str = null;
            while ((str = this.reader.readLine()) != null)
            {
                this.processLine(str);
            }
            this.handleServerClose("Server terminated connection");
        }
        catch (Exception ex)
        {
            if (!(ex instanceof InterruptedException))
            {
                log.error("SocketTask ReceiveData terminating- receiveData call handleServerError: "
                        + ex.toString());
                this.handleServerClose(ex.toString());
            }
            else
            {
                log.debug("SocketTask ReceiveData terminating - interrupted exception caught");
            }
        }
    }

    /**
     * Handles closing of a server connection
     * 
     * @param String the error string used to close the server (null if no
     *        error, i.e. normal close)
     */
    private synchronized void handleServerClose(String error)
    {
        // If we have already attempted a close, our callback will be null.
        // Return and do nothing.
        if (this.dataCallback == null)
        {
            return;
        }

        log.debug("SocketTask - handleServerClose: ");

        if (error != null)
        {
            this.dataCallback.onServerError(instanceId, error);
        }
        else
        {
            this.dataCallback.onServerDisconnect(instanceId);
        }
        this.dataCallback = null;

        if (mainThread != null)
        {
            mainThread.interrupt();
        }

        this.closeSocket();
    }
}