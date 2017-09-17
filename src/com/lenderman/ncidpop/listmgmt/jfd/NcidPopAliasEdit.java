/*******************************************************
 * NCID Pop Alias Edit - Auto Generated
 *******************************************************/
/*
 * Created by JFormDesigner on Sat Jan 04 09:34:03 CST 2014
 */

package com.lenderman.ncidpop.listmgmt.jfd;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import com.lenderman.ncidpop.utils.OsUtils;

/**
 * The NCID Pop Alias Edit frame
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class NcidPopAliasEdit extends JFrame
{
    /**
     * Constructor
     */
    public NcidPopAliasEdit()
    {
        initComponents();
        setIconImage(OsUtils.getDialogIcon());
    }

    /**
     * Initializes Components
     */
    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY
        // //GEN-BEGIN:initComponents
        aliasLabel = new JLabel();
        nameTextField = new JTextField();
        okButton = new JButton();
        cancelButton = new JButton();
        aliasNumberLabel = new JLabel();

        // ======== this ========
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        // ---- aliasLabel ----
        aliasLabel.setText("Please Enter The Alias Name for Phone Number");
        aliasLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(aliasLabel);
        aliasLabel.setBounds(5, 15, 335, 20);
        contentPane.add(nameTextField);
        nameTextField.setBounds(40, 65, 260, 29);

        // ---- okButton ----
        okButton.setText("Ok");
        contentPane.add(okButton);
        okButton.setBounds(100, 105, 50, 27);

        // ---- cancelButton ----
        cancelButton.setText("Cancel");
        contentPane.add(cancelButton);
        cancelButton.setBounds(170, 105, 75, 27);

        // ---- aliasNumberLabel ----
        aliasNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(aliasNumberLabel);
        aliasNumberLabel.setBounds(5, 35, 335, 20);

        { // compute preferred size
            Dimension preferredSize = new Dimension();
            for (int i = 0; i < contentPane.getComponentCount(); i++)
            {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width,
                        preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height,
                        preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        setSize(360, 180);
        setLocationRelativeTo(getOwner());
        // //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY
    // //GEN-BEGIN:variables
    private JLabel aliasLabel;
    protected JTextField nameTextField;
    protected JButton okButton;
    protected JButton cancelButton;
    protected JLabel aliasNumberLabel;
    // JFormDesigner - End of variables declaration //GEN-END:variables
}
