/*******************************************************
 * Server Request Manager
 *******************************************************/
package com.lenderman.ncidpop.listmgmt;

import java.util.ArrayList;
import java.util.StringTokenizer;
import com.lenderman.ncidpop.data.ServerData;

/**
 * The Server Request Manager. Facilitates making client requests.
 * 
 * @author Chris Lenderman
 */
public class ServerRequestManager
{
    /**
     * The info list types
     */
    public enum InfoListType
    {
        WHITE_NAME("white name"), WHITE_NUMBER("white number"), BLACK_NAME(
                "black name"), BLACK_NUMBER("black number"), NONE("neither");

        /** The type received from the server */
        public String infoStringType;

        /**
         * Constructor
         */
        InfoListType(String type)
        {
            this.infoStringType = type;
        }
    }

    /**
     * The server request types
     */
    public enum ServerRequestType
    {
        INFO,
        UPDATE_ALIASES,
        UPDATE_BLACK_WHITE,
        RELOAD_LISTS,
        SINGLE_LOG_REPROCESS_ALIASES,
        MULTIPLE_LOG_REPROCESS_ALIASES,
        REREAD_CALL_LOG;
    }

    /**
     * The server list types
     */
    public enum ListType
    {
        ALIAS("alias"), WHITE("white"), BLACK("black");

        /** The type command which will be provided to the server */
        public String type = null;

        /**
         * Constructor
         */
        ListType(String type)
        {
            this.type = type;
        }
    }

    /**
     * The list change type being requested
     */
    public enum ListRequestChangeType
    {
        ADD("add"), MODIFY("modify"), REMOVE("remove");

        /** The server command for the change type */
        public String command;

        /**
         * Constructor
         */
        ListRequestChangeType(String command)
        {
            this.command = command;
        }
    }

    /** The current alias type for the given name/number info request */
    private static String infoAliasType = null;

    /** The current info selected type for the given name/number info request */
    private static InfoListType infoSelectedType = null;

    /** The last server request type */
    private static ServerRequestType lastServerRequestType = null;

    /**
     * Request a log accept
     * 
     * @param ServerData
     */
    public static void requestLogAccept(ServerData data)
    {
        if (data == null)
        {
            return;
        }

        String accept = "WRK: ACCEPT LOG";

        if (lastServerRequestType == ServerRequestType.MULTIPLE_LOG_REPROCESS_ALIASES)
        {
            accept += "S";
            data.serverEnqueuedLines.add(accept);
        }
        else if (lastServerRequestType == ServerRequestType.SINGLE_LOG_REPROCESS_ALIASES)
        {
            data.serverEnqueuedLines.add(accept);
        }
    }

    /**
     * Request a log reject
     * 
     * @param ServerData
     */
    public static void requestLogReject(ServerData data)
    {
        if (data == null)
        {
            return;
        }

        String reject = "WRK: REJECT LOG";

        if (lastServerRequestType == ServerRequestType.MULTIPLE_LOG_REPROCESS_ALIASES)
        {
            reject += "S";
            data.serverEnqueuedLines.add(reject);
        }
        else if (lastServerRequestType == ServerRequestType.SINGLE_LOG_REPROCESS_ALIASES)
        {
            data.serverEnqueuedLines.add(reject);
        }
    }

    /**
     * Request a call log reread
     * 
     * @param ServerData
     */
    public static void requestCallLoadReread(ServerData data)
    {
        if (data == null)
        {
            return;
        }

        lastServerRequestType = ServerRequestType.REREAD_CALL_LOG;
        data.serverEnqueuedLines.add("REQ: REREAD");
    }

    /**
     * Requests server info for the given name and number
     * 
     * @param String the name for which we want server info
     * @param String the number for which we want server info
     * @param ServerData
     */
    public static void requestServerInfo(String number, String name,
            ServerData data)
    {
        // Reset any old cached information
        infoAliasType = null;
        infoSelectedType = null;

        if (data == null)
        {
            return;
        }

        lastServerRequestType = ServerRequestType.INFO;

        data.serverEnqueuedLines.add("REQ: INFO " + number + "&&" + name);
    }

    /**
     * Requests a server alias list change
     * 
     * @param String the number for which we are requesting the change. Cannot
     *        be null.
     * @param String the old name for which we are requesting the change. Cannot
     *        be null.
     * @param String the new name associated with the change. Can be null.
     * @param ListRequestChangeType the type of list change we are requesting
     * @param ServerData
     */
    public static void requestServerAliasListChange(String number,
            String oldName, String newName, ListRequestChangeType request,
            ServerData data)
    {
        if ((oldName == null) || (number == null) || (data == null))
        {
            return;
        }

        lastServerRequestType = ServerRequestType.UPDATE_ALIASES;

        String formatOldName = "";
        String formatNewName = "";

        if (newName != null)
        {
            formatNewName = newName;
        }

        if (oldName != null)
        {
            formatOldName = oldName;
        }

        data.serverEnqueuedLines.add("REQ: " + ListType.ALIAS.type + " "
                + request.command + " \"" + number + "&&" + formatNewName
                + "\"" + " " + "\"" + "NAMEDEP&&" + formatOldName + "\"");
    }

    /**
     * Requests a server black/white list change
     * 
     * @param String the number for which we are requesting the change. Can be
     *        null.
     * @param String the name for which we are requesting the change. Can be
     *        null.
     * @param String the comment associated with the change. Can be null.
     * @param ListRequestChangeType the type of list change we are requesting
     * @param ListType the type of list we wish to modify
     * @param ServerData
     */
    public static void requestServerBlackWhiteListChange(String number,
            String name, String comment, ListRequestChangeType request,
            ListType list, ServerData data)
    {
        if (data == null)
        {
            return;
        }

        lastServerRequestType = ServerRequestType.UPDATE_BLACK_WHITE;

        String formatNumber = "";
        String formatName = "";
        String formatComment = "";

        if (comment != null)
        {
            formatComment = comment;
        }

        if (number != null)
        {
            formatNumber = number;
        }

        if (name != null)
        {
            formatName = name;
        }

        data.serverEnqueuedLines
                .add("REQ: "
                        + list.type
                        + " "
                        + request.command
                        + " \""
                        + formatNumber
                        + (((formatNumber.length() > 0) && (formatName.length() > 0)) ? " "
                                : "") + formatName + "\" \"" + formatComment
                        + "\"");
    }

    /**
     * Requests a server list reload
     * 
     * @param ServerData
     */
    public static void requestServerListReload(ServerData data)
    {
        if (data == null)
        {
            return;
        }

        lastServerRequestType = ServerRequestType.RELOAD_LISTS;
        data.serverEnqueuedLines.add("REQ: RELOAD");
    }

    /**
     * Requests reprocessing of server alias list(s)
     * 
     * @param ServerData
     * @param boolean whether or not to reprocess all server logs
     */
    public static void requestServerAliasReprocess(ServerData data,
            boolean allLogs)
    {
        if (data == null)
        {
            return;
        }

        if (allLogs)
        {
            lastServerRequestType = ServerRequestType.MULTIPLE_LOG_REPROCESS_ALIASES;
            data.serverEnqueuedLines.add("REQ: UPDATES");
        }
        else
        {
            lastServerRequestType = ServerRequestType.SINGLE_LOG_REPROCESS_ALIASES;
            data.serverEnqueuedLines.add("REQ: UPDATE");
        }
    }

    /**
     * Gets the last server request type
     * 
     * @return ServerRequestType
     */
    public static ServerRequestType getLastServerRequestType()
    {
        return lastServerRequestType;
    }

    /**
     * Gets the last received info alias type
     * 
     * @return String
     */
    public static String getInfoAliasType()
    {
        return infoAliasType;
    }

    /**
     * Gets the last received info selected type
     * 
     * @return InfoListType
     */
    public static InfoListType getInfoSelectedType()
    {
        return infoSelectedType;
    }

    /**
     * Assess whether or not a server response was successful
     * 
     * @param ArrayList<String> list of strings received from the server in
     *        response to a server job
     * 
     */
    public static boolean serverResponseIsSuccessful(ArrayList<String> lines)
    {
        for (String str : lines)
        {
            if (str.toLowerCase().contains("done"))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Processes an info response
     * 
     * @param ArrayList<String> list of strings as received by the server in
     *        response to an info request
     */
    public static void processInfoResponse(ArrayList<String> info)
    {
        infoAliasType = "NOALIAS";

        String aliasStringToParse = null;

        for (String str : info)
        {
            if (str.contains("alias"))
            {
                aliasStringToParse = str;
                continue;
            }

            for (InfoListType type : InfoListType.values())
            {
                if (str.startsWith(type.infoStringType))
                {
                    infoSelectedType = type;
                    continue;
                }
            }
        }

        if (aliasStringToParse != null)
        {
            StringTokenizer tokenizer = new StringTokenizer(aliasStringToParse,
                    " ");

            if (tokenizer.countTokens() == 2)
            {
                tokenizer.nextToken();
                infoAliasType = tokenizer.nextToken();
            }
        }
    }
}
