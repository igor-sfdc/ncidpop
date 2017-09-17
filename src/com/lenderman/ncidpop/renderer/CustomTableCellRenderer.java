/*******************************************************
 * Custom Table Cell Renderer
 *******************************************************/
package com.lenderman.ncidpop.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import com.lenderman.ncidpop.NcidPopMainDisplay;
import com.lenderman.ncidpop.data.LogDateTimeData;
import com.lenderman.ncidpop.utils.LogParserUtils;
import com.lenderman.ncidpop.utils.NumberFormatUtilities;
import com.lenderman.ncidpop.utils.PrefUtils;

/**
 * Custom table cell renderer
 * 
 * @author Chris Lenderman
 */
public class CustomTableCellRenderer extends DefaultTableCellRenderer
{
    /** The alternating color to use */
    private Color light = new Color(240, 240, 240);

    /** Serial version ID */
    private static final long serialVersionUID = 6703872492730589499L;

    /** @inheritDoc */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column)
    {

        if (value != null)
        {
            // Perform special rendering for the date column
            if (table.convertColumnIndexToModel(column) == NcidPopMainDisplay.CALL_DATE_COL)
            {
                if (((LogDateTimeData) value).valueInMillis == null)
                {
                    value = ((LogDateTimeData) value).date + " "
                            + ((LogDateTimeData) value).time;
                }
                else
                {
                    value = LogParserUtils.convertToDateTimeString(
                            ((LogDateTimeData) value).valueInMillis,
                            LogParserUtils.getPreferredDateTimeFormat());
                }
            }

            // Perform special rendering for the number column
            if (table.convertColumnIndexToModel(column) == NcidPopMainDisplay.CALL_NUMBER_COL)
            {
                value = NumberFormatUtilities.formatNumber((String) value,
                        PrefUtils.instance
                                .getString(PrefUtils.PREF_PHONE_NUMBER_FORMAT));
            }
        }

        Component cellComponent = super.getTableCellRendererComponent(table,
                value, isSelected, hasFocus, row, column);

        if (!isSelected)
        {
            if ((row % 2) == 0)
            {
                cellComponent.setBackground(light);
            }
            else
            {
                cellComponent.setBackground(Color.WHITE);
            }
        }
        return cellComponent;
    }
}