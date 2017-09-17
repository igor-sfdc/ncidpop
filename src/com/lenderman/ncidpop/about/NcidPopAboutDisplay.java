/*******************************************************
 * NCID Pop About Display
 *******************************************************/
package com.lenderman.ncidpop.about;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import com.lenderman.ncidpop.VersionNumber;
import com.lenderman.ncidpop.about.jfd.NcidPopAbout;
import com.lenderman.ncidpop.data.ServerData;
import com.lenderman.ncidpop.data.ServerDataManager;
import com.lenderman.ncidpop.utils.NcidConstants;

/**
 * The NCID Pop about display.
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class NcidPopAboutDisplay extends NcidPopAbout
{
    /**
     * Constructor
     */
    public NcidPopAboutDisplay()
    {
        versionLabel.setText(VersionNumber.NCIDPOP_VERSION_NUMBER);

        okButton.addActionListener(new ActionListener()
        {
            /** @inheritDoc */
            public void actionPerformed(ActionEvent arg0)
            {
                setVisible(false);
            }
        });
    }

    /** @inheritDoc */
    @Override
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);

        if (visible)
        {
            DefaultTableModel model = (DefaultTableModel) enabledServerOptionsTable
                    .getModel();

            if (model != null)
            {
                model.setRowCount(0);
                ServerData data = ServerDataManager
                        .getServerDataForCurrentDisplayId();

                if (data != null)
                {
                    for (String option : data.serverOptionList)
                    {
                        Vector<Object> rowData = new Vector<Object>();
                        rowData.add(option);

                        String optionDescription = NcidConstants.SERVER_OPTIONS_DESCRIPTIONS
                                .get(option);
                        rowData.add(optionDescription == null ? "Unknown"
                                : optionDescription);
                        model.addRow(rowData);
                    }
                }
            }
        }
    }
}