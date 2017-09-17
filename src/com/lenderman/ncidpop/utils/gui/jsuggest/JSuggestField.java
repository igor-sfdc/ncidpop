/*******************************************************
 * JSuggest Field
 *******************************************************/
package com.lenderman.ncidpop.utils.gui.jsuggest;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

/**
 * Provides a text-field that makes suggestions using a provided data
 * collection. You might have seen this on Google (tm), this is the Java
 * implementation.
 * 
 * @beaninfo attribute: isContainer false description: A component which allows
 *           for the editing of a single line of text.
 * 
 * @author Chris Lenderman (adopted from work performed by David von Ah)
 */
@SuppressWarnings(
{ "serial", "rawtypes", "unchecked" })
public class JSuggestField extends JTextField
{
    /** Dialog used as the drop-down list. */
    private JDialog d;

    /** Location of said drop-down list. */
    private Point location;

    /** List contained in the drop-down dialog. */
    private JList list;

    /**
     * Collections containing the original data and the filtered data for the
     * suggestions.
     */
    private AbstractCollection<Object> data, suggestions;

    /**
     * Separate matcher-thread, prevents the text-field from hanging while the
     * suggestions are being prepared.
     */
    private InterruptableMatcher matcher;

    /**
     * Fonts used to indicate that the text-field is processing the request,
     * i.e. looking for matches
     */
    private Font busy, regular;

    /** Needed for the new narrowing search, so we know when to reset the list */
    private String lastWord = "";

    /** The last chosen variable which exists. */
    private Object lastChosenExistingVariable;

    /** Listeners, fire event when a selection as occurred */
    private LinkedList<ActionListener> listeners;

    /** Suggestion matcher */
    private ContainsMatcher<Object> suggestMatcher = new ContainsMatcher<Object>();

    /** Indication of whether or not case sensitivity matters */
    private boolean caseSensitive = false;

    /**
     * Create a new JSuggestField.
     */
    public JSuggestField()
    {
        this((Window) null);
    }

    /**
     * Create a new JSuggestField.
     * 
     * @param owner Window containing this JSuggestField
     */
    public JSuggestField(Window owner)
    {
        super();
        data = new Vector<Object>();
        suggestions = new Vector<Object>();
        listeners = new LinkedList<ActionListener>();
        list = new JList();

        setOwner(owner);

        regular = getFont();
        busy = new Font(getFont().getName(), Font.ITALIC, getFont().getSize());
    }

    /**
     * Create a new JSuggestField.
     * 
     * @param owner Window containing this JSuggestField
     * @param data Available suggestions
     */
    public JSuggestField(Window owner, AbstractCollection<Object> data)
    {
        this(owner);
        setSuggestData(data);
    }

    /**
     * Sets the owner of the JSuggestField
     * 
     * @param Window owner
     */
    public void setOwner(Window owner)
    {
        if (owner != null)
        {
            owner.addComponentListener(new ComponentListener()
            {
                /** @inheritDoc */
                public void componentShown(ComponentEvent e)
                {
                    relocate();
                }

                /** @inheritDoc */
                public void componentResized(ComponentEvent e)
                {
                    relocate();
                }

                /** @inheritDoc */
                public void componentMoved(ComponentEvent e)
                {
                    relocate();
                }

                /** @inheritDoc */
                public void componentHidden(ComponentEvent e)
                {
                    relocate();
                }
            });
            addFocusListener(new FocusAdapter()
            {
                /** @inheritDoc */
                @Override
                public void focusLost(FocusEvent e)
                {
                    if ((d.isVisible() && (list.getSelectedIndex() != -1))
                            & (suggestions.size() > 0))
                    {
                        setText(list.getSelectedValue().toString());
                        lastChosenExistingVariable = list.getSelectedValue();
                        fireActionEvent();
                    }
                    d.setVisible(false);
                }
            });

            if (owner instanceof Frame)
            {
                d = new JDialog((Frame) owner);
            }
            else if (owner instanceof Dialog)
            {
                d = new JDialog((Dialog) owner);
            }
            else
            {
                d = new JDialog();
            }
            d.setUndecorated(true);
            d.setFocusableWindowState(false);
            d.setFocusable(false);
            list.addMouseListener(new MouseAdapter()
            {
                private int selected;

                /** @inheritDoc */
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    if (selected == list.getSelectedIndex())
                    {
                        // provide double-click for selecting a suggestion
                        setText(list.getSelectedValue().toString());

                        lastChosenExistingVariable = list.getSelectedValue();
                        d.setVisible(false);
                        fireActionEvent();
                    }
                    selected = list.getSelectedIndex();
                }
            });
            d.add(new JScrollPane(list,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
            d.pack();
            addKeyListener(new KeyAdapter()
            {
                /** @inheritDoc */
                @Override
                public void keyPressed(KeyEvent e)
                {
                    relocate();
                }

                /** @inheritDoc */
                @Override
                public void keyReleased(KeyEvent e)
                {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    {
                        d.setVisible(false);
                        return;
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    {
                        if (d.isVisible())
                        {
                            list.setSelectedIndex(list.getSelectedIndex() + 1);
                            list.ensureIndexIsVisible(list.getSelectedIndex() + 1);
                            return;
                        }
                        else
                        {
                            showSuggest();
                        }
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_UP)
                    {
                        list.setSelectedIndex(list.getSelectedIndex() - 1);
                        list.ensureIndexIsVisible(list.getSelectedIndex() - 1);
                        return;
                    }
                    else if ((e.getKeyCode() == KeyEvent.VK_ENTER)
                            & (list.getSelectedIndex() != -1)
                            & (suggestions.size() > 0))
                    {
                        setText(list.getSelectedValue().toString());
                        lastChosenExistingVariable = list.getSelectedValue();
                        d.setVisible(false);
                        fireActionEvent();
                        return;
                    }
                    showSuggest();
                }
            });
            owner.addWindowListener(new WindowAdapter()
            {
                /** @inheritDoc */
                @Override
                public void windowIconified(WindowEvent e)
                {
                    d.setVisible(false);
                }

                /** @inheritDoc */
                @Override
                public void windowClosing(WindowEvent e)
                {
                    d.dispose();
                }

                /** @inheritDoc */
                @Override
                public void windowClosed(WindowEvent e)
                {
                    d.dispose();
                }
            });
        }
    }

    /**
     * Sets new data used to suggest similar words.
     * 
     * @param data AbstractCollection containing available words
     * @return success, true unless the collection was null
     */
    public boolean setSuggestData(AbstractCollection<? extends Object> data)
    {
        if (data == null)
        {
            return false;
        }

        this.data.clear();
        for (Object obj : data)
        {
            this.data.add(obj);
        }
        list.setListData(this.data.toArray());
        return true;
    }

    /**
     * Set preferred size for the drop-down that will appear.
     * 
     * @param size Preferred size of the drop-down list
     */
    public void setPreferredSuggestSize(Dimension size)
    {
        d.setPreferredSize(size);
    }

    /**
     * Set minimum size for the drop-down that will appear.
     * 
     * @param size Minimum size of the drop-down list
     */
    public void setMinimumSuggestSize(Dimension size)
    {
        d.setMinimumSize(size);
    }

    /**
     * Set maximum size for the drop-down that will appear.
     * 
     * @param size Maximum size of the drop-down list
     */
    public void setMaximumSuggestSize(Dimension size)
    {
        d.setMaximumSize(size);
    }

    /**
     * Force the suggestions to be displayed (Useful for buttons e.g. for using
     * JSuggestionField like a ComboBox)
     */
    public void showSuggest()
    {
        if (!getText().toLowerCase().contains(lastWord.toLowerCase()))
        {
            suggestions.clear();
        }
        if (suggestions.isEmpty())
        {
            suggestions.addAll(data);
        }
        if (matcher != null)
        {
            matcher.stop = true;
        }
        matcher = new InterruptableMatcher();
        SwingUtilities.invokeLater(matcher);
        lastWord = getText();
        relocate();
    }

    /**
     * Force the suggestions to be hidden (Useful for buttons, e.g. to use
     * JSuggestionField like a ComboBox)
     */
    public void hideSuggest()
    {
        d.setVisible(false);
    }

    /**
     * @return boolean Visibility of the suggestion window
     */
    public boolean isSuggestVisible()
    {
        return d.isVisible();
    }

    /**
     * Place the suggestion window under the JTextField.
     */
    private void relocate()
    {
        try
        {
            location = getLocationOnScreen();
            location.y += getHeight();
            d.setLocation(location);
        }
        catch (IllegalComponentStateException e)
        {
            return; // might happen on window creation
        }
    }

    /**
     * Inner class providing the independent matcher-thread. This thread can be
     * interrupted, so it won't process older requests while there's already a
     * new one.
     */
    private class InterruptableMatcher extends Thread
    {
        /** flag used to stop the thread */
        private volatile boolean stop;

        /**
         * Standard run method used in threads responsible for the actual search
         */

        /** @inheritDoc */
        @Override
        public void run()
        {
            try
            {
                setFont(busy);
                Iterator<? extends Object> it = suggestions.iterator();
                String word = getText();
                while (it.hasNext())
                {
                    if (stop)
                    {
                        return;
                    }
                    // rather than using the entire list, let's rather remove
                    // the words that don't match, thus narrowing
                    // the search and making it faster
                    if (!suggestMatcher
                            .matches(it.next(), word, !caseSensitive))
                    {
                        it.remove();
                    }
                }
                setFont(regular);
                if (suggestions.size() > 0)
                {
                    list.setListData(suggestions.toArray());
                    list.setSelectedIndex(0);
                    list.ensureIndexIsVisible(0);
                    d.setVisible(true);
                }
                else
                {
                    d.setVisible(false);
                }
            }
            catch (Exception e)
            {
                // Despite all precautions, external changes have occurred.
                // Let the new thread handle it...
                return;
            }
        }
    }

    /**
     * Adds a listener that notifies when a selection has occured
     * 
     * @param listener ActionListener to use
     */
    public void addSelectionListener(ActionListener listener)
    {
        if (listener != null)
        {
            listeners.add(listener);
        }
    }

    /**
     * Removes the Listener
     * 
     * @param listener ActionListener to remove
     */
    public void removeSelectionListener(ActionListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Use ActionListener to notify on changes so we don't have to create an
     * extra event
     */
    private void fireActionEvent()
    {
        ActionEvent event = new ActionEvent(this, 0, getText());
        for (ActionListener listener : listeners)
        {
            listener.actionPerformed(event);
        }
    }

    /**
     * Returns the selected value in the drop down list
     * 
     * @return selected value from the user or null if the entered value does
     *         not exist
     */
    public Object getLastChosenExistingVariable()
    {
        return lastChosenExistingVariable;
    }

    /**
     * Sets case sensitivity for the JSuggestField
     * 
     * @param boolean
     */
    public void setCaseSensitive(boolean caseSensitive)
    {
        this.caseSensitive = caseSensitive;
    }
}