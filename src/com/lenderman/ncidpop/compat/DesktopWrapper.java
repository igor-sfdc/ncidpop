/*******************************************************
 * Desktop Wrapper
 *******************************************************/
package com.lenderman.ncidpop.compat;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import org.apache.log4j.Logger;
import com.lenderman.ncidpop.utils.OsUtils;

/**
 * This class supports launching websites. It is a compat class since this
 * capability does not exist in Java 1.5. If the Desktop class doesn't exist, we
 * go with a "best effort" method for launching desktops.
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("all")
public class DesktopWrapper
{
    /** Class logger */
    private static Logger log = Logger.getLogger(DesktopWrapper.class);

    /**
     * Launches a website
     * 
     * @param the website to launch
     */
    public static void launchWebsite(String site)
    {
        boolean successful = false;
        try
        {
            // First, try and see if our java version has the Desktop class.
            Class<?> c = Class.forName("java.awt.Desktop");
            Method supportedMethod = c.getMethod("isDesktopSupported");
            supportedMethod.setAccessible(true);
            Object supported = supportedMethod.invoke(null, null);

            if (((Boolean) supported) == true)
            {
                Method getDesktop = c.getMethod("getDesktop");
                getDesktop.setAccessible(true);
                Object desktop = getDesktop.invoke(null, null);

                if (desktop != null)
                {
                    Method browse = c.getMethod("browse", URI.class);
                    browse.setAccessible(true);
                    browse.invoke(desktop, (new URL(site)).toURI());
                }
            }
            successful = true;
        }
        catch (Exception ex)
        {
            log.warn("Couldn't open web browser : " + ex);
        }

        // We failed to open a browser with conventional methods. Fall back!
        if (!successful)
        {
            openBrowserFallback(site);
        }
    }

    /**
     * Fallback method for opening browsers
     * 
     * @param the website URL to launch
     */
    private static void openBrowserFallback(String url)
    {
        Runtime rt = Runtime.getRuntime();
        try
        {

            if (OsUtils.isWindowsOS())
            {
                // this doesn't support showing urls in the form of
                // "page.html#nameLink"
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);

            }
            else if (OsUtils.isMacOS())
            {
                rt.exec("open " + url);
            }
            else if (OsUtils.isLinuxOS())
            {
                // Do a best guess on Linux until we get a platform independent
                // way. Build a list of browsers to try, in this order.
                String[] browsers =
                { "epiphany", "firefox", "mozilla", "konqueror", "netscape",
                        "opera", "links", "lynx" };

                // Build a command string which looks like
                // "browser1 "url" || browser2 "url" ||..."
                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < browsers.length; i++)
                {
                    cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \""
                            + url + "\" ");
                }

                rt.exec(new String[]
                { "sh", "-c", cmd.toString() });
            }
            else
            {
                return;
            }
        }
        catch (Exception e)
        {
            log.error("Couldn't open web browser via fallback method: " + e);
            return;
        }
        return;
    }
}