/*******************************************************
 * NCID Pop Main Panel - Auto Generated
 *******************************************************/
/*
 * Created by JFormDesigner on Mon Dec 23 08:46:55 PST 2013
 */

package com.lenderman.ncidpop.jfd;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import com.lenderman.ncidpop.utils.gui.jsuggest.JSuggestField;

/**
 * The NCID Pop Main Panel
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class NcidPopMain extends JPanel
{
    /**
     * Constructor
     */
    public NcidPopMain()
    {
        initComponents();
    }

    /**
     * Sets the owner of this panel
     * 
     * @param Window owner
     */
    public void setOwner(Window owner)
    {
        suggestFieldSearch.setOwner(owner);
    }

    /**
     * Initializes components
     */
    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY
        // //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        iconLabel = new JLabel();
        menu2 = new JMenu();
        exitMenu = new JMenuItem();
        menu3 = new JMenu();
        preferencesMenu = new JMenuItem();
        menu1 = new JMenu();
        refreshMenu = new JMenuItem();
        reloadListsMenu = new JMenuItem();
        menu6 = new JMenu();
        relayOptionsMenu = new JMenuItem();
        menu5 = new JMenu();
        sendTextMenu = new JMenuItem();
        sendServerMessageMenu = new JMenuItem();
        menu4 = new JMenu();
        webpageMenu = new JMenuItem();
        linesLabelMenu = new JMenuItem();
        aboutMenu = new JMenuItem();
        panel7 = new JPanel();
        label3 = new JLabel();
        suggestFieldSearch = new JSuggestField();
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        callTable = new JTable();
        panel2 = new JPanel();
        scrollPane2 = new JScrollPane();
        messagesTable = new JTable();
        panel5 = new JPanel();
        label1 = new JLabel();
        totalCallsLabel = new JLabel();
        panel6 = new JPanel();
        label2 = new JLabel();
        totalMessagesLabel = new JLabel();
        panel3 = new JPanel();
        serverNameLabel = new JLabel();
        panel4 = new JPanel();
        serverListLabel = new JLabel();
        panelButtons = new JPanel();
        buttonPrevious = new JButton();
        buttonNext = new JButton();

        // ======== this ========
        setPreferredSize(new Dimension(560, 515));
        setMinimumSize(new Dimension(560, 515));
        setLayout(new FormLayout("default:grow",
                "2*(default, $lgap), fill:220dlu:grow, $lgap, 4*(default), $lgap, default"));

        // ======== menuBar1 ========
        {
            menuBar1.add(iconLabel);

            // ======== menu2 ========
            {
                menu2.setText("File");

                // ---- exitMenu ----
                exitMenu.setText("Exit");
                menu2.add(exitMenu);
            }
            menuBar1.add(menu2);

            // ======== menu3 ========
            {
                menu3.setText("Edit");

                // ---- preferencesMenu ----
                preferencesMenu.setText("Preferences");
                menu3.add(preferencesMenu);
            }
            menuBar1.add(menu3);

            // ======== menu1 ========
            {
                menu1.setText("Server");

                // ---- refreshMenu ----
                refreshMenu.setText("Refresh");
                menu1.add(refreshMenu);

                // ---- reloadListsMenu ----
                reloadListsMenu.setText("Reload Lists");
                menu1.add(reloadListsMenu);
            }
            menuBar1.add(menu1);

            // ======== menu6 ========
            {
                menu6.setText("Gateway");

                // ---- relayOptionsMenu ----
                relayOptionsMenu.setText("Gateway Relay Options");
                menu6.add(relayOptionsMenu);
            }
            menuBar1.add(menu6);

            // ======== menu5 ========
            {
                menu5.setText("Message");

                // ---- sendTextMenu ----
                sendTextMenu.setText("Send SMS Text Message");
                menu5.add(sendTextMenu);

                // ---- sendServerMessageMenu ----
                sendServerMessageMenu.setText("Send Server Message");
                menu5.add(sendServerMessageMenu);
            }
            menuBar1.add(menu5);

            // ======== menu4 ========
            {
                menu4.setText("Help");

                // ---- webpageMenu ----
                webpageMenu.setText("NCIDpop Web Page");
                menu4.add(webpageMenu);

                // ---- linesLabelMenu ----
                linesLabelMenu.setText("Line Labels");
                menu4.add(linesLabelMenu);

                // ---- aboutMenu ----
                aboutMenu.setText("About");
                menu4.add(aboutMenu);
            }
            menuBar1.add(menu4);
        }
        add(menuBar1, CC.xy(1, 1));

        // ======== panel7 ========
        {
            panel7.setLayout(new FormLayout(
                    "2*($lcgap), default, $lcgap, default:grow, 2*($lcgap)",
                    "default"));

            // ---- label3 ----
            label3.setText("Search:");
            panel7.add(label3, CC.xy(3, 1));
            panel7.add(suggestFieldSearch, CC.xy(5, 1));
        }
        add(panel7, CC.xy(1, 3));

        // ======== tabbedPane1 ========
        {
            tabbedPane1.setFont(new Font("SansSerif", Font.PLAIN, 12));

            // ======== panel1 ========
            {
                panel1.setLayout(new FormLayout("default:grow",
                        "fill:default:grow"));

                // ======== scrollPane1 ========
                {

                    // ---- callTable ----
                    callTable.setFont(new Font("SansSerif", callTable.getFont()
                            .getStyle(), callTable.getFont().getSize()));
                    callTable
                            .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    scrollPane1.setViewportView(callTable);
                }
                panel1.add(scrollPane1, CC.xy(1, 1));
            }
            tabbedPane1.addTab("Calls", panel1);

            // ======== panel2 ========
            {
                panel2.setLayout(new FormLayout("default:grow",
                        "fill:default:grow"));

                // ======== scrollPane2 ========
                {

                    // ---- messagesTable ----
                    messagesTable
                            .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    scrollPane2.setViewportView(messagesTable);
                }
                panel2.add(scrollPane2, CC.xy(1, 1));
            }
            tabbedPane1.addTab("Messages", panel2);
        }
        add(tabbedPane1, CC.xy(1, 5));

        // ======== panel5 ========
        {
            panel5.setLayout(new FormLayout(
                    "1dlu, $lcgap, 60dlu, $lcgap, default:grow", "fill:default"));

            // ---- label1 ----
            label1.setText("Total Calls:");
            label1.setFont(UIManager.getFont("defaultFont"));
            panel5.add(label1, CC.xy(3, 1));

            // ---- totalCallsLabel ----
            totalCallsLabel.setText("0");
            totalCallsLabel.setFont(UIManager.getFont("defaultFont"));
            panel5.add(totalCallsLabel, CC.xy(5, 1));
        }
        add(panel5, CC.xy(1, 7));

        // ======== panel6 ========
        {
            panel6.setLayout(new FormLayout(
                    "1dlu, $lcgap, 60dlu, $lcgap, default:grow", "default"));

            // ---- label2 ----
            label2.setText("Total Messages:");
            label2.setFont(UIManager.getFont("defaultFont"));
            panel6.add(label2, CC.xy(3, 1));

            // ---- totalMessagesLabel ----
            totalMessagesLabel.setText("0");
            totalMessagesLabel.setFont(UIManager.getFont("defaultFont"));
            panel6.add(totalMessagesLabel, CC.xy(5, 1));
        }
        add(panel6, CC.xy(1, 8));

        // ======== panel3 ========
        {
            panel3.setLayout(new FormLayout("1dlu, $lcgap, default", "default"));

            // ---- serverNameLabel ----
            serverNameLabel.setText("Server: Unknown");
            serverNameLabel.setFont(UIManager.getFont("defaultFont"));
            panel3.add(serverNameLabel, CC.xy(3, 1));
        }
        add(panel3, CC.xy(1, 9));

        // ======== panel4 ========
        {
            panel4.setLayout(new FormLayout("1dlu, $lcgap, default", "default"));

            // ---- serverListLabel ----
            serverListLabel.setFont(UIManager.getFont("defaultFont"));
            panel4.add(serverListLabel, CC.xy(3, 1));
        }
        add(panel4, CC.xy(1, 10));

        // ======== panelButtons ========
        {
            panelButtons
                    .setLayout(new FormLayout(
                            "default, $lcgap, default:grow, $lcgap, default",
                            "default"));

            // ---- buttonPrevious ----
            buttonPrevious.setText("<<");
            panelButtons.add(buttonPrevious, CC.xy(1, 1));

            // ---- buttonNext ----
            buttonNext.setText(">>");
            panelButtons.add(buttonNext, CC.xy(5, 1));
        }
        add(panelButtons, CC.xy(1, 12));
        // //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY
    // //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    protected JLabel iconLabel;
    private JMenu menu2;
    protected JMenuItem exitMenu;
    private JMenu menu3;
    protected JMenuItem preferencesMenu;
    private JMenu menu1;
    protected JMenuItem refreshMenu;
    protected JMenuItem reloadListsMenu;
    private JMenu menu6;
    protected JMenuItem relayOptionsMenu;
    private JMenu menu5;
    protected JMenuItem sendTextMenu;
    protected JMenuItem sendServerMessageMenu;
    private JMenu menu4;
    protected JMenuItem webpageMenu;
    protected JMenuItem linesLabelMenu;
    protected JMenuItem aboutMenu;
    private JPanel panel7;
    private JLabel label3;
    protected JSuggestField suggestFieldSearch;
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JScrollPane scrollPane1;
    protected JTable callTable;
    private JPanel panel2;
    private JScrollPane scrollPane2;
    protected JTable messagesTable;
    private JPanel panel5;
    private JLabel label1;
    protected JLabel totalCallsLabel;
    private JPanel panel6;
    private JLabel label2;
    protected JLabel totalMessagesLabel;
    private JPanel panel3;
    protected JLabel serverNameLabel;
    private JPanel panel4;
    protected JLabel serverListLabel;
    protected JPanel panelButtons;
    protected JButton buttonPrevious;
    protected JButton buttonNext;
    // JFormDesigner - End of variables declaration //GEN-END:variables
}
