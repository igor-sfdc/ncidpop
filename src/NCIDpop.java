/*******************************************************
 * Main - NCID Pop Main Entry Point
 *******************************************************/

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import org.apache.log4j.Logger;
import com.lenderman.ncidpop.NcidPopMainDisplay;
import com.lenderman.ncidpop.NcidPopMainDisplayController;
import com.lenderman.ncidpop.compat.SystemTrayWrapper;
import com.lenderman.ncidpop.jfd.NcidPopMainDialog;
import com.lenderman.ncidpop.jfd.NcidPopMainFrame;
import com.lenderman.ncidpop.utils.OsUtils;

/**
 * The NCID Pop Main entry point
 * 
 * @author Chris Lenderman
 */
public class NCIDpop
{
    /** Class logger */
    private static Logger log = Logger.getLogger(NCIDpop.class);

    /**
     * Installs resource file
     * 
     * @param String the file to install
     */
    private static void installResourcFile(String resourceName)
    {
        try
        {
            File file = new File(OsUtils.getCurrentPath() + "/" + resourceName);

            if (!file.exists())
            {
                File parent = new File(file.getParent());
                parent.mkdirs();
                file.createNewFile();

                InputStream inputStream = NCIDpop.class
                        .getResourceAsStream("/com/lenderman/ncidpop/resources/"
                                + resourceName);

                OutputStream oStream = new FileOutputStream(file);
                int len;
                byte[] buf = new byte[4096];

                while ((len = inputStream.read(buf)) > 0)
                {
                    oStream.write(buf, 0, len);
                }
                oStream.close();
                inputStream.close();
            }
        }
        catch (Exception e)
        {
            log.error("Couldn't install resource file:" + e);
        }
    }

    /**
     * The main entry point
     * 
     * @param String[] args
     */
    public static void main(String[] args)
    {
        System.out.println("Running on OS " + System.getProperty("os.name"));
        System.out.println("The path of the JAR is allegedly "
                + OsUtils.getCurrentPath());

        installResourcFile("CIDRing.wav");

        if (OsUtils.isWindowsOS())
        {
            installResourcFile("com4j-amd64.dll");
            installResourcFile("com4j.dll");
            installResourcFile("com4j-x86.dll");
        }
        else if (OsUtils.isMacOS())
        {
            installResourcFile("libaddressbooklookup.dylib");
            try
            {
                System.load(OsUtils.getCurrentPath()
                        + "/libaddressbooklookup.dylib");
            }
            catch (UnsatisfiedLinkError ex)
            {
                // Do Nothing. Will not use this library if unavailable.
            }
            catch (Exception ex)
            {
                // Do Nothing. Will not use this library if unable.
            }
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    if (!OsUtils.isMacOS())
                    {
                        for (LookAndFeelInfo info : UIManager
                                .getInstalledLookAndFeels())
                        {
                            if ("Nimbus".equals(info.getName()))
                            {
                                UIManager.setLookAndFeel(info.getClassName());
                                break;
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    log.error("Couldn't set look and feel: " + e);
                }

                if (OsUtils.isLinuxOS() || OsUtils.isMacOS())
                {
                    Font defaultFont = new Font("SansSerif", Font.PLAIN, 11);
                    UIManager.put("defaultFont", defaultFont);
                }

                NcidPopMainDisplay display = null;
                if (OsUtils.isLinuxOS() || !SystemTrayWrapper.isSupported())
                {
                    NcidPopMainFrame frame = new NcidPopMainFrame();
                    display = frame.mainDisplay;
                    display.setOwner(frame);
                    frame.setVisible(true);
                }
                else
                {
                    NcidPopMainDialog dialog = new NcidPopMainDialog();
                    display = dialog.mainDisplay;
                    display.setOwner(dialog);
                }
                new NcidPopMainDisplayController(display);
            }
        });
    }
}
