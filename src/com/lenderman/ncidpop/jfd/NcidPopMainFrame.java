/*******************************************************
 * NCID Pop Main Frame - Auto Generated
 *******************************************************/
/*
 * Created by JFormDesigner on Wed Dec 25 09:53:40 PST 2013
 */

package com.lenderman.ncidpop.jfd;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
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
public class NcidPopMainFrame extends JFrame
{
    /**
     * Constructor
     */
    public NcidPopMainFrame()
    {
        initComponents();
        setIconImage(OsUtils.getDialogIcon());
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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
