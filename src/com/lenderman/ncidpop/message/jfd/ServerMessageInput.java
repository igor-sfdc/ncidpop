/*******************************************************
 * Server Message Input Dialog - Auto Generated
 *******************************************************/
/*
 * Created by JFormDesigner on Sun Feb 16 20:05:33 CST 2014
 */

package com.lenderman.ncidpop.message.jfd;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import com.lenderman.ncidpop.utils.OsUtils;

/**
 * The NCID Pop Server Message Input Dialog
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class ServerMessageInput extends JFrame
{
    /**
     * Constructor
     */
    public ServerMessageInput()
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
        scrollPane1 = new JScrollPane();
        messageTextArea = new JTextArea();
        okButton = new JButton();
        cancelButton = new JButton();

        // ======== this ========
        setFont(UIManager.getFont("defaultFont"));
        setResizable(false);
        setTitle("Send Server Message");
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        // ---- label1 ----
        label1.setText("Message:");
        label1.setFont(UIManager.getFont("defaultFont"));
        contentPane.add(label1);
        label1.setBounds(5, 10, 365, label1.getPreferredSize().height);

        // ======== scrollPane1 ========
        {

            // ---- messageTextArea ----
            messageTextArea.setFont(UIManager.getFont("defaultFont"));
            messageTextArea.setLineWrap(true);
            messageTextArea.setWrapStyleWord(true);
            scrollPane1.setViewportView(messageTextArea);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(5, 30, 370, 80);

        // ---- okButton ----
        okButton.setText("OK");
        contentPane.add(okButton);
        okButton.setBounds(115, 115, 80, okButton.getPreferredSize().height);

        // ---- cancelButton ----
        cancelButton.setText("Cancel");
        contentPane.add(cancelButton);
        cancelButton.setBounds(195, 115, 80,
                cancelButton.getPreferredSize().height);

        contentPane.setPreferredSize(new Dimension(400, 195));
        setSize(400, 195);
        setLocationRelativeTo(getOwner());
        // //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY
    // //GEN-BEGIN:variables
    private JLabel label1;
    private JScrollPane scrollPane1;
    protected JTextArea messageTextArea;
    protected JButton okButton;
    protected JButton cancelButton;
    // JFormDesigner - End of variables declaration //GEN-END:variables
}
