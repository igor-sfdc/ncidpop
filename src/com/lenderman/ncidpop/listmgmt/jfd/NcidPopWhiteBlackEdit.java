/*******************************************************
 * NCID Pop White/Black Edit - Auto Generated
 *******************************************************/
/*
 * Created by JFormDesigner on Sat Jan 04 21:42:39 CST 2014
 */

package com.lenderman.ncidpop.listmgmt.jfd;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import com.lenderman.ncidpop.utils.OsUtils;

/**
 * The NCID Pop Alias Whitelist/Blacklist edit frame
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class NcidPopWhiteBlackEdit extends JFrame
{
    /**
     * Constructor
     */
    public NcidPopWhiteBlackEdit()
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
        titleLabel = new JLabel();
        panel1 = new JPanel();
        nameRadioButton = new JRadioButton();
        numberRadioButton = new JRadioButton();
        applyButton = new JButton();
        cancelButton = new JButton();
        label1 = new JLabel();
        commentField = new JTextField();

        // ======== this ========
        setTitle("Add Item To List");
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        // ---- titleLabel ----
        titleLabel.setText("Which item would you like to add to the ");
        contentPane.add(titleLabel);
        titleLabel.setBounds(20, 15, 330, titleLabel.getPreferredSize().height);

        // ======== panel1 ========
        {
            panel1.setBorder(new TitledBorder("Item"));
            panel1.setLayout(null);

            // ---- nameRadioButton ----
            nameRadioButton.setText("text");
            nameRadioButton.setSelected(true);
            panel1.add(nameRadioButton);
            nameRadioButton.setBounds(10, 20, 300, 18);

            // ---- numberRadioButton ----
            numberRadioButton.setText("text");
            panel1.add(numberRadioButton);
            numberRadioButton.setBounds(10, 40, 305, 18);
        }
        contentPane.add(panel1);
        panel1.setBounds(20, 40, 325, 70);

        // ---- applyButton ----
        applyButton.setText("Apply");
        contentPane.add(applyButton);
        applyButton.setBounds(95, 160, 65, 26);

        // ---- cancelButton ----
        cancelButton.setText("Cancel");
        contentPane.add(cancelButton);
        cancelButton.setBounds(170, 160, 80, 26);

        // ---- label1 ----
        label1.setText("Comment");
        contentPane.add(label1);
        label1.setBounds(new Rectangle(new Point(25, 130), label1
                .getPreferredSize()));
        contentPane.add(commentField);
        commentField.setBounds(90, 130, 250,
                commentField.getPreferredSize().height);

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
        setSize(375, 235);
        setLocationRelativeTo(getOwner());

        // ---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(nameRadioButton);
        buttonGroup1.add(numberRadioButton);
        // //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY
    // //GEN-BEGIN:variables
    protected JLabel titleLabel;
    private JPanel panel1;
    protected JRadioButton nameRadioButton;
    protected JRadioButton numberRadioButton;
    protected JButton applyButton;
    protected JButton cancelButton;
    private JLabel label1;
    protected JTextField commentField;
    // JFormDesigner - End of variables declaration //GEN-END:variables
}
