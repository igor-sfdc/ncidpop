/*******************************************************
 * Callback Capable Table Sorter
 *******************************************************/
package com.lenderman.ncidpop.compat;

import java.util.HashSet;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

/**
 * This class takes a compatible TableSorter and extends it to add callbacks on
 * table sorting.
 * 
 * @author Chris Lenderman
 */
@SuppressWarnings("serial")
public class CallbackCapableTableSorter extends TableSorter
{
    /**
     * Interface which callees will implement to be called back on sorting
     * status change
     */
    public interface SortingStatusChangeListener
    {
        /**
         * Called back on column sort change
         * 
         * @param the column that change
         * @param the status of the column that changed. See TableSorter.java
         *        for the various statuses
         */
        public void onColumnSortChange(int column, int status);
    }

    /** List of sorting listeners */
    private HashSet<SortingStatusChangeListener> sortingListeners = new HashSet<SortingStatusChangeListener>();

    /**
     * Constructor
     */
    public CallbackCapableTableSorter(TableModel tableModel,
            JTableHeader tableHeader)
    {
        super(tableModel, tableHeader);
    }

    /**
     * Adds a sorting status change listener
     * 
     * @param SortingStatusChangeListener
     */
    public synchronized void addSortingStatusChangeListener(
            SortingStatusChangeListener listener)
    {
        sortingListeners.add(listener);
    }

    /**
     * Removes a sorting status change listener
     * 
     * @param SortingStatusChangeListener
     */
    public synchronized void removeSortingStatusChangeListener(
            SortingStatusChangeListener listener)
    {
        sortingListeners.remove(listener);
    }

    /**
     * Overrides the base sorting status and adds callback capability
     * 
     * @param int the column to sort
     * @param the status of the column that changed. See TableSorter.java for
     *        the various statuses
     */
    @Override
    public void setSortingStatus(int column, int status)
    {
        super.setSortingStatus(column, status);

        for (SortingStatusChangeListener listener : sortingListeners)
        {
            listener.onColumnSortChange(column, status);
        }
    }
}