/*******************************************************
 * OS Utilities
 *******************************************************/
package com.lenderman.ncidpop.utils;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * Utilities for cross-platform OS support.
 * 
 * @author Chris Lenderman
 */
public class OsUtils
{
    /** Class logger */
    private static Logger log = Logger.getLogger(OsUtils.class);

    /** The Default Linux notify executable */
    private static final String DEFAULT_LINUX_NOTIFY_EXECUTABLE = "/usr/bin/notify-send";

    /** The Default Mac name read executable */
    private static final String DEFAULT_MAC_NAME_READ_EXECUTABLE = "/usr/bin/say";

    /** The Default Linux name read executable */
    private static final String DEFAULT_LINUX_NAME_READ_EXECUTABLE = "./thirdPartyNameScript.sh";

    /** The Default Linux/Mac name read parameters */
    private static final String DEFAULT_LINUX_MAC_NAME_READ_PARAMS = " \"$NAME\"";

    /** The Default Linux notify parameters prefix */
    private static final String DEFAULT_LINUX_NOTIFY_PARAMS_PREFIX = " \"NCIDpop - ";

    /** The Default Mac notify executable */
    private static final String DEFAULT_MAC_NOTIFY_EXECUTABLE = "/usr/local/bin/growlnotify";

    /** The Default Mac notify parameters prefix */
    private static final String DEFAULT_MAC_NOTIFY_PARAMS_PREFIX = " --title \"NCIDpop\" -n NCIDpop --image $IMAGE -m \"";

    /** The Default Windows notify executable (installed under "program files") */
    private static final String DEFAULT_WINDOWS_NOTIFY_EXECUTABLE = "\\growl for windows\\growlnotify.com";

    /** The Default Windows notify parameters prefix */
    private static final String DEFAULT_WINDOWS_NOTIFY_PARAMS_PREFIX = " /t:\"NCIDpop\" /r:\"NCIDpopNotify\" /n:\"NCIDpopNotify\" /a:NCIDpop \"";

    /** The Default notify parameters suffix */
    private static final String DEFAULT_NOTIFY_SUFFIX = "\"";

    /**
     * Returns the dialog icon for use on this operating system. Mac OS X needs
     * a higher resolution to ensure that we have a good icon in the dock.
     * 
     * @return Image
     */
    public static Image getDialogIcon()
    {
        if (isMacOS())
        {
            return NcidConstants.ICON_CONNECTED.getImage();
        }
        else
        {
            return NcidConstants.ICON_CONNECTED_SMALL.getImage();
        }
    }

    /**
     * A JNI method for calling native Objective-C code in the
     * libaddressbooklookup library
     * 
     * Gets the Macintosh address lookup string
     * 
     * @return String
     */
    private static native String getMacintoshAddressBookAlias(String number);

    /**
     * A JNI method for calling native Objective-C code in the
     * libaddressbooklookup library
     * 
     * Gets the Macintosh address book image
     * 
     * @return String
     */
    private static native String getMacintoshAddressBookImage(String number);

    /**
     * A JNI method for calling native Objective-C code in the
     * libaddressbooklookup library
     * 
     * Gets the Macintosh address book list in Number, Name format
     * 
     * @return HashMap<String, String>
     */
    private static native HashMap<String, String> getMacintoshAddressBookList();

    /**
     * A JNI method for calling native Objective-C code in the
     * libaddressbooklookup library
     * 
     * Launches the Macintosh address book for the given number
     * 
     * @param String
     */
    private static native void launchMacintoshAddressBook(String number);

    /**
     * Tests to see if system address book is supported
     * 
     * @return true if supported, false otherwise
     */
    public static boolean isSystemAddressBookSupported()
    {
        return OsUtils.isMacOS();
    }

    /**
     * For a given number, launch the OS specific address book
     * 
     * @param the number
     */
    public static void launchAddressBookFor(String number)
    {
        try
        {
            if (OsUtils.isMacOS())
            {
                OsUtils.launchMacintoshAddressBook(number);
            }
        }
        catch (final UnsatisfiedLinkError ex)
        {
            NotificationUtils.enqueueDialog(new Runnable()
            {
                public void run()
                {
                    NotificationUtils.showDialogPopupMessage(
                            " Couldn't launch address book display"
                                    + ex.toString(), true);
                }
            });
        }
    }

    /**
     * For a given number, check to see if the OS specific address book has an
     * alias for it
     * 
     * @param the number
     * @return true if found in address book, false otherwise
     */
    public static boolean hasAddressBookAlias(String number)
    {
        String alias = null;
        if (PrefUtils.instance
                .getBoolean(PrefUtils.PREF_USE_SYSTEM_ADDRESS_BOOK))
        {
            try
            {
                if (OsUtils.isMacOS())
                {
                    alias = OsUtils.getMacintoshAddressBookAlias(number);
                }
            }
            catch (UnsatisfiedLinkError ex)
            {
                // do nothing, we will return false
            }
        }

        return alias != null;
    }

    /**
     * Returns an address book list in Name, Number format.
     * 
     * @return HashMap<String, String>
     */
    public static HashMap<String, String> getAddressBookList()
    {
        if (PrefUtils.instance
                .getBoolean(PrefUtils.PREF_USE_SYSTEM_ADDRESS_BOOK))
        {
            try
            {
                if (OsUtils.isMacOS())
                {
                    return OsUtils.getMacintoshAddressBookList();
                }
            }
            catch (UnsatisfiedLinkError ex)
            {
                // do nothing, we will return null
            }
        }
        return null;
    }

    /**
     * For a given name and number, determine if an address book alias exists.
     * If it exists, apply it.
     * 
     * @param the name
     * @param the number
     * @return the original name if no alias found; otherwise, the alias
     */
    public static String determineAddressBookAlias(String name, String number)
    {
        String alias = null;

        if (PrefUtils.instance
                .getBoolean(PrefUtils.PREF_USE_SYSTEM_ADDRESS_BOOK))
        {
            try
            {
                if (OsUtils.isMacOS())
                {
                    alias = OsUtils.getMacintoshAddressBookAlias(number);
                }
            }
            catch (UnsatisfiedLinkError ex)
            {
                // do nothing, we will use native alias
            }
        }

        if (alias != null)
        {
            return alias;
        }

        return name;
    }

    /**
     * For a given name and number, determine if an address book image exists.
     * If it exists, apply it.
     * 
     * @param the number
     * @return the image name, can be null
     */
    public static String getAddressBookImage(String number)
    {
        String imageName = null;

        if (PrefUtils.instance
                .getBoolean(PrefUtils.PREF_USE_SYSTEM_ADDRESS_BOOK))
        {
            try
            {
                if (OsUtils.isMacOS())
                {
                    imageName = OsUtils.getMacintoshAddressBookImage(number);
                }
            }
            catch (UnsatisfiedLinkError ex)
            {
                // do nothing, we will use native alias
            }
        }

        return imageName;
    }

    /**
     * Returns the default third party notifier string based on operating system
     * 
     * @param the substitution phrase to apply to the string
     * @return String default notifier string
     */
    public static String getDefaultThirdPartyNotifierString(String substitution)
    {
        File file = null;
        if (isLinuxOS())
        {
            file = new File(DEFAULT_LINUX_NOTIFY_EXECUTABLE);
            if (file.exists())
            {
                return DEFAULT_LINUX_NOTIFY_EXECUTABLE
                        + DEFAULT_LINUX_NOTIFY_PARAMS_PREFIX + substitution
                        + DEFAULT_NOTIFY_SUFFIX;
            }
        }
        else if (isMacOS())
        {
            file = new File(DEFAULT_MAC_NOTIFY_EXECUTABLE);
            if (file.exists())
            {
                return DEFAULT_MAC_NOTIFY_EXECUTABLE
                        + DEFAULT_MAC_NOTIFY_PARAMS_PREFIX + substitution
                        + DEFAULT_NOTIFY_SUFFIX;
            }
        }
        else if (isWindowsOS())
        {
            String basePath;
            String notifierPath = null;
            try
            {
                basePath = System.getenv("ProgramFiles(X86)");
                file = new File(basePath + DEFAULT_WINDOWS_NOTIFY_EXECUTABLE);
                if (file.exists())
                {
                    notifierPath = file.getAbsolutePath();
                }

                if (notifierPath == null)
                {
                    basePath = System.getenv("ProgramFiles");
                    file = new File(basePath
                            + DEFAULT_WINDOWS_NOTIFY_EXECUTABLE);

                    if (file.exists())
                    {
                        notifierPath = file.getAbsolutePath();
                    }
                }
            }
            catch (Exception e)
            {
                log.warn("Couldn't get default notifier executable: " + e);
            }

            if (notifierPath != null)
            {
                return notifierPath + DEFAULT_WINDOWS_NOTIFY_PARAMS_PREFIX
                        + substitution + DEFAULT_NOTIFY_SUFFIX;
            }

        }
        return "";
    }

    /**
     * Returns the default third party name reading string based on operating
     * system
     * 
     * @return String default third party name reading string
     */
    public static String getDefaultThirdPartyNameReadingString()
    {
        File file = null;
        if (isLinuxOS())
        {
            file = new File(DEFAULT_LINUX_NAME_READ_EXECUTABLE);
            if (file.exists())
            {
                return DEFAULT_LINUX_NAME_READ_EXECUTABLE
                        + DEFAULT_LINUX_MAC_NAME_READ_PARAMS;
            }
        }
        else if (isMacOS())
        {
            file = new File(DEFAULT_MAC_NAME_READ_EXECUTABLE);
            if (file.exists())
            {
                return DEFAULT_MAC_NAME_READ_EXECUTABLE
                        + DEFAULT_LINUX_MAC_NAME_READ_PARAMS;
            }
        }
        return "";
    }

    /**
     * Determines if this computer is running a Linux OS
     * 
     * @return boolean true if running Linux OS
     */
    public static boolean isLinuxOS()
    {
        String os = System.getProperty("os.name");
        return ((os != null) && os.toLowerCase().startsWith("linux"));
    }

    /**
     * Determines if this computer is running a windows OS
     * 
     * @return boolean true if running Windows OS
     */
    public static boolean isWindowsOS()
    {
        String os = System.getProperty("os.name");
        return ((os != null) && os.toLowerCase().startsWith("win"));
    }

    /**
     * Determines if this computer is running a mac OS
     * 
     * @return boolean true if running mac OS
     */
    public static boolean isMacOS()
    {
        String os = System.getProperty("os.name");
        return ((os != null) && os.toLowerCase().startsWith("mac"));
    }

    /**
     * Gets the directory which contains the running JAR file
     * 
     * @param Class the class to assess
     * @return File representing the JAR directory
     */
    public static File getJarDir(Class<?> aclass)
    {
        URL url;
        String extURL;

        // get an url
        try
        {
            url = aclass.getProtectionDomain().getCodeSource().getLocation();
        }
        catch (SecurityException ex)
        {
            url = aclass.getResource(aclass.getSimpleName() + ".class");
        }

        // convert to external form
        extURL = url.toExternalForm();

        // prune for various cases
        if (extURL.endsWith(".jar"))
        {
            extURL = extURL.substring(0, extURL.lastIndexOf("/"));
        }
        else
        { // from getResource
            String suffix = "/" + (aclass.getName()).replace(".", "/")
                    + ".class";
            extURL = extURL.replace(suffix, "");
            if (extURL.startsWith("jar:") && extURL.endsWith(".jar!"))
            {
                extURL = extURL.substring(4, extURL.lastIndexOf("/"));
            }
        }

        // convert back to url
        try
        {
            url = new URL(extURL);
        }
        catch (MalformedURLException mux)
        {
            // leave url unchanged; probably does not happen
        }

        // convert url to File
        try
        {
            return new File(url.toURI());
        }
        catch (URISyntaxException ex)
        {
            return new File(url.getPath());
        }
    }

    /**
     * Gets the current path
     * 
     * @return String the current path
     */
    public static String getCurrentPath()
    {
        String path = null;
        URL url = ClassLoader.getSystemClassLoader().getResource(".");

        if (url != null)
        {
            try
            {
                path = (URLDecoder.decode(url.getPath(), "UTF-8"));
            }
            catch (UnsupportedEncodingException e)
            {
                log.warn("Couldn't decode current path: " + e);
            }
        }

        if ((path == null) && (getJarDir(OsUtils.class) != null))
        {
            path = getJarDir(OsUtils.class).getAbsolutePath();
        }

        if (path == null)
        {
            path = System.getenv("user.dir");
        }
        return path;
    }

    /**
     * Executes a command line command
     * 
     * @param String the command
     * @param HashMap <String, String> a list of substitutions to process
     * @param boolean whether or not to wait for termination before continuing
     *        execution
     * @return if waiting for termination, a list of output strings from the
     *         process.
     * @throws IOException
     */
    public static ArrayList<String> executeCommandLineCommand(String command,
            HashMap<String, String> substitutes, boolean waitForTermination)
            throws IOException
    {
        String regex = "(\"[^\"]*\")|(\\S+)";

        List<String> matchList = new ArrayList<String>();

        Matcher m = Pattern.compile(regex).matcher(command);
        while (m.find())
        {
            if (m.group(1) != null)
            {
                matchList.add(m.group(1));
            }
            else if (m.group(2) != null)
            {
                matchList.add(m.group(2));
            }
        }

        if (substitutes != null)
        {
            for (int index = 0; index < matchList.size(); index++)
            {
                for (String key : substitutes.keySet())
                {
                    if (matchList.get(index).contains(key))
                    {
                        matchList.set(
                                index,
                                matchList.get(index).replace(key,
                                        substitutes.get(key)));
                    }
                }
            }
        }

        ProcessBuilder builder = new ProcessBuilder(matchList);
        builder.directory(new File(OsUtils.getCurrentPath()));

        Process p = builder.start();
        if (waitForTermination)
        {
            try
            {
                p.waitFor();

                BufferedReader is = new BufferedReader(new InputStreamReader(
                        p.getInputStream()));

                String line;
                ArrayList<String> results = new ArrayList<String>();
                while ((line = is.readLine()) != null)
                {
                    results.add(line);
                }

                is = new BufferedReader(new InputStreamReader(
                        p.getErrorStream()));
                while ((line = is.readLine()) != null)
                {
                    results.add(line);
                }

                return results;
            }
            catch (InterruptedException e)
            {
                log.info("Command line execution terminated: " + e);
            }
        }
        return null;
    }

    /**
     * Using Java reflection, sets the mac dock image
     * 
     * @param Image the image to set
     */
    @SuppressWarnings("all")
    public static void setMacDockImage(Image image)
    {
        if (OsUtils.isMacOS())
        {
            System.setProperty(
                    "com.apple.mrj.application.apple.menu.about.name",
                    "NCIDpop");
            try
            {
                Class<?> c = Class.forName("com.apple.eawt.Application");
                Method method = c.getMethod("getApplication");
                method.setAccessible(true);
                Object o = method.invoke(null, null);

                Method newMethod = o.getClass().getMethod("setDockIconImage",
                        Image.class);
                newMethod.invoke(o, image);
            }
            catch (Exception ex)
            {
                log.error("Couldn't set Mac dock image: " + ex);
            }
        }
    }
}