/*******************************************************
 * NCID Pop About Dialog - Auto Generated
 *******************************************************/
/*
 * Created by JFormDesigner on Mon Dec 23 16:36:07 PST 2013
 */

package com.lenderman.ncidpop.about.jfd;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import com.lenderman.ncidpop.utils.OsUtils;

/**
 * The NCID Pop About Dialog
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class NcidPopAbout extends JFrame
{
    /**
     * Constructor
     */
    public NcidPopAbout()
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
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        okButton = new JButton();
        versionLabel = new JLabel();
        scrollPane1 = new JScrollPane();
        enabledServerOptionsTable = new JTable();
        label6 = new JLabel();

        // ======== this ========
        setTitle("About NCIDpop");
        setFont(UIManager.getFont("defaultFont"));
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        // ---- label1 ----
        label1.setText("NCIDpop Version:");
        label1.setFont(UIManager.getFont("defaultFont"));
        contentPane.add(label1);
        label1.setBounds(110, 15, 105, label1.getPreferredSize().height);

        // ---- label2 ----
        label2.setText("Copyright (C) 2017 Chris Lenderman");
        label2.setFont(UIManager.getFont("defaultFont"));
        contentPane.add(label2);
        label2.setBounds(110, 35, 235, 16);

        // ---- label3 ----
        label3.setText("Portions Copyright (C) 2003 Alexi Kosut");
        label3.setFont(UIManager.getFont("defaultFont"));
        contentPane.add(label3);
        label3.setBounds(110, 55, 235, 16);

        // ---- label4 ----
        label4.setText("Portions Copyright (C) 2004-2006 Lyman Epp");
        label4.setFont(UIManager.getFont("defaultFont"));
        contentPane.add(label4);
        label4.setBounds(110, 75, 270, 16);

        // ---- label5 ----
        label5.setIcon(new ImageIcon(getClass().getResource(
                "/com/lenderman/ncidpop/images/ncidpop_medium.png")));
        contentPane.add(label5);
        label5.setBounds(new Rectangle(new Point(50, 30), label5
                .getPreferredSize()));

        // ---- okButton ----
        okButton.setText("OK");
        okButton.setFont(UIManager.getFont("defaultFont"));
        contentPane.add(okButton);
        okButton.setBounds(new Rectangle(new Point(190, 245), okButton
                .getPreferredSize()));

        // ---- versionLabel ----
        versionLabel.setText("Version#");
        versionLabel.setFont(UIManager.getFont("defaultFont"));
        contentPane.add(versionLabel);
        versionLabel.setBounds(215, 15, 150,
                versionLabel.getPreferredSize().height);

        // ======== scrollPane1 ========
        {

            // ---- enabledServerOptionsTable ----
            enabledServerOptionsTable.setModel(new DefaultTableModel(
                    new Object[][]
                    {}, new String[]
                    { "Option", "Description" })
            {
                Class<?>[] columnTypes = new Class<?>[]
                { String.class, String.class };
                boolean[] columnEditable = new boolean[]
                { false, false };

                @Override
                public Class<?> getColumnClass(int columnIndex)
                {
                    return columnTypes[columnIndex];
                }

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex)
                {
                    return columnEditable[columnIndex];
                }
            });
            {
                TableColumnModel cm = enabledServerOptionsTable
                        .getColumnModel();
                cm.getColumn(0).setPreferredWidth(100);
                cm.getColumn(1).setPreferredWidth(325);
            }
            scrollPane1.setViewportView(enabledServerOptionsTable);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(15, 145, 425, 80);

        // ---- label6 ----
        label6.setText("Enabled Server Options:");
        label6.setFont(UIManager.getFont("defaultFont"));
        contentPane.add(label6);
        label6.setBounds(20, 125, 165, 16);

        contentPane.setPreferredSize(new Dimension(470, 330));
        setSize(470, 330);
        setLocationRelativeTo(getOwner());
        // //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY
    // //GEN-BEGIN:variables
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    protected JButton okButton;
    protected JLabel versionLabel;
    private JScrollPane scrollPane1;
    protected JTable enabledServerOptionsTable;
    private JLabel label6;
    // JFormDesigner - End of variables declaration //GEN-END:variables
}
