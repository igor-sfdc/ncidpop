/*******************************************************
 * File Utilities
 *******************************************************/
package com.lenderman.ncidpop.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Utilities for working with file contents
 * 
 * @author Chris Lenderman
 */
public class FileUtils
{
    /**
     * Reads a resource file into a string
     * 
     * @param String the resource to read
     * @return String contents of the file
     */
    public static String resourceFileToString(String resourceName)
    {
        // Load the server options
        InputStream inputStream = NcidConstants.class
                .getResourceAsStream(resourceName);

        StringBuilder sb = new StringBuilder();

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    inputStream));
            String read;

            while ((read = br.readLine()) != null)
            {
                sb.append(read);
            }

            br.close();
        }
        catch (Exception ex)
        {
            // Fail silently
        }
        return sb.toString();
    }

    /**
     * Reads a file into an array
     * 
     * @param File the file to open and read
     * @return the ArrayList<String> containing the contents of the file
     */
    public static ArrayList<String> readFileIntoArray(File file)
    {
        ArrayList<File> fileArray = new ArrayList<File>();
        fileArray.add(file);
        return readFilesIntoArray(fileArray);
    }

    /**
     * Reads a list of files into an array
     * 
     * @param ArrayList<File> the list of files to open and read
     * @return the ArrayList<String> containing the contents of the files
     */
    public static ArrayList<String> readFilesIntoArray(ArrayList<File> fileList)
    {
        ArrayList<String> result = new ArrayList<String>();
        for (File file : fileList)
        {
            try
            {
                FileReader fileReader;
                fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = null;
                while ((line = bufferedReader.readLine()) != null)
                {
                    result.add(line);
                }
                bufferedReader.close();
            }
            catch (Exception ex)
            {
                // Fail silently
            }
        }
        return result;
    }
}
