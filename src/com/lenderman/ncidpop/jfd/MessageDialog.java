/*******************************************************
 * Message Dialog - Auto Generated
 *******************************************************/
/*
 * Created by JFormDesigner on Tue Jan 14 22:06:48 CST 2014
 */

package com.lenderman.ncidpop.jfd;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Method;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.apache.log4j.Logger;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import com.lenderman.ncidpop.utils.OsUtils;

/**
 * The NCID Pop Message Dialog
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class MessageDialog extends JDialog
{
    /** Class logger */
    private static Logger log = Logger.getLogger(MessageDialog.class);

    /**
     * Constructor
     */
    public MessageDialog()
    {
        super();

        try
        {
            Method setIconImage = this.getClass().getMethod("setIconImage",
                    Image.class);
            setIconImage.setAccessible(true);
            setIconImage.invoke(this, new ImageIcon(OsUtils.getDialogIcon()));
        }
        catch (Exception ex)
        {
            log.warn("Couldn't set icon image in message dialog: " + ex);
        }

        initComponents();

        // Remove any default mouse listeners from the infoLabel component. We
        // don't want to allow selection.
        for (MouseListener listener : infoLabel
                .getListeners(MouseListener.class))
        {
            infoLabel.removeMouseListener(listener);
        }
        for (MouseMotionListener listener : infoLabel
                .getListeners(MouseMotionListener.class))
        {
            infoLabel.removeMouseMotionListener(listener);
        }
    }

    /**
     * Action Performed
     */
    private void buttonOkActionPerformed(ActionEvent e)
    {
        setVisible(false);
    }

    /**
     * Init Components
     */
    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY
        // //GEN-BEGIN:initComponents
        infoLabel = new JTextArea();
        panel1 = new JPanel();
        buttonOk = new JButton();

        // ======== this ========
        setAlwaysOnTop(true);
        setResizable(false);
        setTitle("Message");
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout("8dlu, [100dlu,min]:grow, 8dlu",
                "fill:6dlu, $lgap, default:grow, 8dlu, default, $lgap, 2dlu"));

        // ---- infoLabel ----
        infoLabel.setText("Message");
        infoLabel.setEditable(false);
        infoLabel.setFocusable(false);
        infoLabel.setFont(infoLabel.getFont().deriveFont(
                infoLabel.getFont().getStyle() | Font.BOLD));
        infoLabel.setBackground(new Color(214, 217, 223, 0));
        infoLabel.setBorder(null);
        contentPane.add(infoLabel, CC.xy(2, 3, CC.CENTER, CC.DEFAULT));

        // ======== panel1 ========
        {
            panel1.setLayout(new FormLayout("center:default:grow",
                    "fill:default"));

            // ---- buttonOk ----
            buttonOk.setText("Ok");
            buttonOk.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    buttonOkActionPerformed(e);
                }
            });
            panel1.add(buttonOk, CC.xy(1, 1));
        }
        contentPane.add(panel1, CC.xy(2, 5));
        pack();
        setLocationRelativeTo(null);
        // //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY
    // //GEN-BEGIN:variables
    public JTextArea infoLabel;
    private JPanel panel1;
    private JButton buttonOk;
    // JFormDesigner - End of variables declaration //GEN-END:variables
}
