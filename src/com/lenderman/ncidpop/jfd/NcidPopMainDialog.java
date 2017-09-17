/*******************************************************
 * NCID Pop Main Dialog - Auto Generated
 *******************************************************/
/*
 * Created by JFormDesigner on Wed Dec 25 10:10:56 PST 2013
 */

/**
 * The NCID Pop Main Dialog
 *
 * @author Chris Lenderman
 */
package com.lenderman.ncidpop.jfd;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.lang.reflect.Method;
import javax.swing.JDialog;
import org.apache.log4j.Logger;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import com.lenderman.ncidpop.NcidPopMainDisplay;
import com.lenderman.ncidpop.utils.OsUtils;

/**
 * The NCID Pop Main Frame
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class NcidPopMainDialog extends JDialog
{
    /** Class logger */
    private static Logger log = Logger.getLogger(NcidPopMainDialog.class);

    /**
     * Constructor
     */
    public NcidPopMainDialog()
    {
        try
        {
            Method setIconImage = this.getClass().getMethod("setIconImage",
                    Image.class);
            setIconImage.setAccessible(true);
            setIconImage.invoke(this, OsUtils.getDialogIcon());
        }
        catch (Exception ex)
        {
            log.warn("Couldn't set icon image in message dialog: " + ex);
        }

        initComponents();
    }

    /**
     * Initializes components
     */
    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY
        // //GEN-BEGIN:initComponents
        mainDisplay = new NcidPopMainDisplay();

        // ======== this ========
        setTitle("NCIDpop Call Log");
        setMinimumSize(new Dimension(595, 555));
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout("default:grow",
                "fill:default:grow"));
        contentPane.add(mainDisplay, CC.xy(1, 1));
        pack();
        setLocationRelativeTo(getOwner());
        // //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY
    // //GEN-BEGIN:variables
    public NcidPopMainDisplay mainDisplay;
    // JFormDesigner - End of variables declaration //GEN-END:variables
}
