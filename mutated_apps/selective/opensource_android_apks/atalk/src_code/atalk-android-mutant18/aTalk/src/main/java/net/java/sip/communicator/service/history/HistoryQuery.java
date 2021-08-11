/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.history;

import net.java.sip.communicator.service.history.event.HistoryQueryListener;
import net.java.sip.communicator.service.history.records.HistoryRecord;

import java.util.Collection;

/**
 * The <tt>HistoryQuery</tt> corresponds to a query made through the
 * <tt>InteractiveHistoryReader</tt>. It allows to be canceled, to listen for changes in the
 * results and to obtain initial results if available.
 *
 * @author Yana Stamcheva
 */
public interface HistoryQuery
{
	/**
	 * Cancels this query.
	 */
	void cancel();

	/**
	 * Returns the query string, this query was created for.
	 *
	 * @return the query string, this query was created for
	 */
	String getQueryString();

	/**
	 * Returns a collection of the results for this query. It's up to the implementation to
	 * determine how and when to fill this list of results.
	 * <p>
	 * This method could be used in order to obtain first fast initial results and then obtain the
	 * additional results through the <tt>HistoryQueryListener</tt>, which should improve user
	 * experience when waiting for results.
	 *
	 * @return a collection of the initial results for this query
	 */
	Collection<HistoryRecord> getHistoryRecords();

	/**
	 * Adds the given <tt>HistoryQueryListener</tt> to the list of listeners interested in query
	 * result changes.
	 *
	 * @param l
	 * 		the <tt>HistoryQueryListener</tt> to add
	 */
	void addHistoryRecordsListener(HistoryQueryListener l);

	/**
	 * Removes the given <tt>HistoryQueryListener</tt> from the list of listeners interested in
	 * query result changes.
	 *
	 * @param l
	 * 		the <tt>HistoryQueryListener</tt> to remove
	 */
	void removeHistoryRecordsListener(HistoryQueryListener l);
}
