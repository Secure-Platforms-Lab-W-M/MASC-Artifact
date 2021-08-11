/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.gui.event;

import net.java.sip.communicator.service.contactlist.MetaContact;
import net.java.sip.communicator.service.contactlist.MetaContactGroup;

import java.util.*;

/**
 * The <tt>MetaContactQuery</tt> corresponds to a particular query made through
 * the <tt>MetaContactListSource</tt>. Each query once started could be
 * canceled. One could also register a listener in order to be notified for
 * changes in query status and query contact results.
 *
 * @author Yana Stamcheva
 * @author Eng Chong Meng
 */
public class MetaContactQuery
{
    private boolean isCanceled = false;

    private int resultCount = 0;

    /**
     * A list of all registered query listeners.
     */
    private final List<MetaContactQueryListener> queryListeners = new LinkedList<>();

    /**
     * Cancels this query.
     */
    public void cancel()
    {
        isCanceled = true;
        queryListeners.clear();
    }

    /**
     * Returns <tt>true</tt> if this query has been canceled, otherwise returns <tt>false</tt>.
     *
     * @return <tt>true</tt> if this query has been canceled, otherwise returns <tt>false</tt>.
     */
    public boolean isCanceled()
    {
        return isCanceled;
    }

    /**
     * Returns the current number of results received for this query.
     *
     * @return the current number of results received for this query
     */
    public int getResultCount()
    {
        return resultCount;
    }

    /**
     * Sets the result count of this query. This method is meant to be used to
     * set the initial result count which is before firing any events. The
     * result count would be then augmented each time the fireQueryEvent is called.
     *
     * @param resultCount the initial result count to set
     */
    public void setInitialResultCount(int resultCount)
    {
        this.resultCount = resultCount;
    }

    /**
     * Adds the given <tt>MetaContactQueryListener</tt> to the list of
     * registered listeners. The <tt>MetaContactQueryListener</tt> would be
     * notified each time a new <tt>MetaContactQuery</tt> result has been
     * received or if the query has been completed or has been canceled by user
     * or for any other reason.
     *
     * @param l the <tt>MetaContactQueryListener</tt> to add
     */
    public void addContactQueryListener(MetaContactQueryListener l)
    {
        synchronized (queryListeners) {
            queryListeners.add(l);
        }
    }

    /**
     * Removes the given <tt>MetaContactQueryListener</tt> to the list of
     * registered listeners. The <tt>MetaContactQueryListener</tt> would be
     * notified each time a new <tt>MetaContactQuery</tt> result has been
     * received or if the query has been completed or has been canceled by user
     * or for any other reason.
     *
     * @param l the <tt>MetaContactQueryListener</tt> to remove
     */
    public void removeContactQueryListener(MetaContactQueryListener l)
    {
        synchronized (queryListeners) {
            queryListeners.remove(l);
        }
    }

    /**
     * Notifies the <tt>MetaContactQueryListener</tt> that a new
     * <tt>MetaContact</tt> has been received as a result of a search.
     *
     * @param metaContact the received <tt>MetaContact</tt>
     */
    public void fireQueryEvent(MetaContact metaContact)
    {
        resultCount++;
        MetaContactQueryEvent event = new MetaContactQueryEvent(this, metaContact);
        List<MetaContactQueryListener> listeners;
        synchronized (queryListeners) {
            listeners = new LinkedList<>(queryListeners);
        }

        for (MetaContactQueryListener listener : listeners) {
            listener.metaContactReceived(event);
        }
    }

    /**
     * Notifies the <tt>MetaContactQueryListener</tt> that a new
     * <tt>MetaGroup</tt> has been received as a result of a search.
     *
     * @param metaGroup the received <tt>MetaGroup</tt>
     */
    public void fireQueryEvent(MetaContactGroup metaGroup)
    {
        MetaGroupQueryEvent event = new MetaGroupQueryEvent(this, metaGroup);

        List<MetaContactQueryListener> listeners;
        synchronized (queryListeners) {
            listeners = new LinkedList<>(queryListeners);
        }

        for (MetaContactQueryListener listener : listeners) {
            listener.metaGroupReceived(event);
        }
    }

    /**
     * Notifies the <tt>MetaContactQueryListener</tt> that this query has changed its status.
     *
     * @param queryStatus the new query status
     */
    public void fireQueryEvent(int queryStatus)
    {
        MetaContactQueryStatusEvent event  = new MetaContactQueryStatusEvent(this, queryStatus);

        List<MetaContactQueryListener> listeners;
        synchronized (queryListeners) {
            listeners = new LinkedList<>(queryListeners);
        }

        for (MetaContactQueryListener listener : listeners) {
            listener.metaContactQueryStatusChanged(event);
        }
    }
}
