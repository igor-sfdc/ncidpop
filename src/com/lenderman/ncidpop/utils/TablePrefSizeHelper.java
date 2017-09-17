/*******************************************************
 * Table Pref Size Helper
 *******************************************************/
package com.lenderman.ncidpop.utils;

import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;

/**
 * A helper for saving off preferences when a table column size changes
 * 
 * @author Chris Lenderman
 */
public class TablePrefSizeHelper implements TableColumnModelListener
{
    /** Lookup map associated with this helper */
    private HashMap<Integer, String> lookupMap = null;

    /** Table associated with this helper */
    private JTable table;

    /**
     * Constructor
     */
    public TablePrefSizeHelper(HashMap<Integer, String> lookupMap, JTable table)
    {
        this.lookupMap = lookupMap;
        this.table = table;
    }

    /** @inheritDoc */
    public void columnMarginChanged(ChangeEvent e)
    {
        TableColumn col = table.getTableHeader().getResizingColumn();
        if (col != null)
        {
            int index = col.getModelIndex();
            if (lookupMap.get(index) != null)
            {
                PrefUtils.instance.setInteger(lookupMap.get(index),
                        col.getWidth());
            }
        }
    }

    /** @inheritDoc */
    public void columnMoved(TableColumnModelEvent e)
    {
    }

    /** @inheritDoc */
    public void columnAdded(TableColumnModelEvent e)
    {
    }

    /** @inheritDoc */
    public void columnRemoved(TableColumnModelEvent e)
    {
    }

    /** @inheritDoc */
    public void columnSelectionChanged(ListSelectionEvent e)
    {
    }
};
