/*******************************************************
 * Data Dump Utilities
 *******************************************************/
package com.lenderman.ncidpop.utils;

import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFileChooser;
import com.lenderman.ncidpop.compat.FileNameExtensionFilter;
import com.lenderman.ncidpop.data.MessageData;
import com.lenderman.ncidpop.data.ServerData;

/**
 * Utilities that support dumping log data to a file
 * 
 * @author Chris Lenderman
 */
public class DataDumpUtils
{

    /**
     * Dumps server caller and message data to a file
     * 
     * @param Component Swing component
     * @param ServerData the server data to dump
     */
    public static void dumpServerDataToFile(Component parent, ServerData data)
    {
        JFileChooser ch = new JFileChooser();

        ch.setFileFilter(new FileNameExtensionFilter(
                "Text Files (*.txt, *.text)", "txt", "text"));

        if (ch.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            PrintWriter pw = null;
            try
            {
                pw = new PrintWriter(ch.getSelectedFile());
                for (ArrayList<String> callLine : data.callLog)
                {
                    pw.print("**");
                    for (String element : callLine)
                    {
                        pw.print(element + "*");
                    }
                    pw.println("*");
                }

                HashMap<String, String> addressBook = OsUtils
                        .getAddressBookList();

                // Populate the table
                for (MessageData msgData : data.msgLog)
                {
                    String type = msgData.lineType;
                    if (msgData.msgType.length() > 0)
                    {
                        type += "(" + msgData.msgType + ")";
                    }

                    StringBuilder lineBuilder = new StringBuilder();
                    lineBuilder.append(type);
                    lineBuilder.append(": *");
                    lineBuilder.append(msgData.encodePrettyPrintWithAliasList(
                            PrefUtils.instance, true, addressBook));
                    lineBuilder.append("*");
                    lineBuilder.append(msgData.number);
                    lineBuilder.append("*");
                    pw.println(lineBuilder);
                }
                pw.close();
            }
            catch (FileNotFoundException ex)
            {
                // Fail silently
            }
            finally
            {
                if (pw != null)
                {
                    pw.close();
                }
            }
        }
    }
}