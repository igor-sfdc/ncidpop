/*******************************************************
 * SMS Input Dialog - Auto Generated
 *******************************************************/
/*
 * Created by JFormDesigner on Sun Feb 16 20:05:33 CST 2014
 */

package com.lenderman.ncidpop.sms.jfd;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import com.lenderman.ncidpop.utils.OsUtils;
import com.lenderman.ncidpop.utils.gui.jsuggest.JSuggestField;

/**
 * The NCID Pop SMS Input Dialog
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class SmsInput extends JFrame
{
    /**
     * Constructor
     */
    public SmsInput()
    {
        initComponents();
        setIconImage(OsUtils.getDialogIcon());
        numberTextField.setOwner(this);
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
        characterCountLabel = new JLabel();
        scrollPane1 = new JScrollPane();
        messageTextArea = new JTextArea();
        okButton = new JButton();
        cancelButton = new JButton();
        label3 = new JLabel();
        numberTextField = new JSuggestField();

        // ======== this ========
        setFont(UIManager.getFont("defaultFont"));
        setResizable(false);
        setTitle("Send Text Message");
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        // ---- label1 ----
        label1.setText("Message:");
        label1.setFont(UIManager.getFont("defaultFont"));
        contentPane.add(label1);
        label1.setBounds(5, 55, 365, label1.getPreferredSize().height);

        // ---- label2 ----
        label2.setText("Character Count:");
        label2.setFont(UIManager.getFont("defaultFont"));
        contentPane.add(label2);
        label2.setBounds(10, 160, 105, label2.getPreferredSize().height);

        // ---- characterCountLabel ----
        characterCountLabel.setText("0");
        characterCountLabel.setFont(UIManager.getFont("defaultFont"));
        contentPane.add(characterCountLabel);
        characterCountLabel.setBounds(125, 160, 30,
                characterCountLabel.getPreferredSize().height);

        // ======== scrollPane1 ========
        {

            // ---- messageTextArea ----
            messageTextArea.setFont(UIManager.getFont("defaultFont"));
            messageTextArea.setLineWrap(true);
            messageTextArea.setWrapStyleWord(true);
            scrollPane1.setViewportView(messageTextArea);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(5, 75, 370, 80);

        // ---- okButton ----
        okButton.setText("OK");
        contentPane.add(okButton);
        okButton.setBounds(115, 180, 80, okButton.getPreferredSize().height);

        // ---- cancelButton ----
        cancelButton.setText("Cancel");
        contentPane.add(cancelButton);
        cancelButton.setBounds(195, 180, 80,
                cancelButton.getPreferredSize().height);

        // ---- label3 ----
        label3.setText("Number:");
        label3.setFont(UIManager.getFont("defaultFont"));
        contentPane.add(label3);
        label3.setBounds(5, 5, 365, label3.getPreferredSize().height);
        contentPane.add(numberTextField);
        numberTextField.setBounds(5, 25, 260,
                numberTextField.getPreferredSize().height);

        contentPane.setPreferredSize(new Dimension(400, 250));
        setSize(400, 250);
        setLocationRelativeTo(getOwner());
        // //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY
    // //GEN-BEGIN:variables
    private JLabel label1;
    private JLabel label2;
    protected JLabel characterCountLabel;
    private JScrollPane scrollPane1;
    protected JTextArea messageTextArea;
    protected JButton okButton;
    protected JButton cancelButton;
    private JLabel label3;
    protected JSuggestField numberTextField;
    // JFormDesigner - End of variables declaration //GEN-END:variables
}
