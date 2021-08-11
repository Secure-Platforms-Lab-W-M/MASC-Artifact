/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.msghistory.event;

import java.util.*;

/**
 * A "ProgressEvent" event gets delivered through the search process of MessageHistoryService Service. The event is
 * wrapper around the generated event from the History Service
 *
 * @author Damian Minkov
 */
public class ProgressEvent extends java.util.EventObject
{
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 0L;

	private net.java.sip.communicator.service.history.event.ProgressEvent evt;

	/**
	 * The current progress that we will pass.
	 */
	private int progress = 0;

	/**
	 * Constructor.
	 *
	 * @param source
	 *        source <tt>Object</tt>
	 * @param evt
	 *        the event
	 * @param progress
	 *        initial progress
	 */
	public ProgressEvent(Object source, net.java.sip.communicator.service.history.event.ProgressEvent evt, int progress) {
		super(source);

		this.evt = evt;
		this.progress = progress;
	}

	/**
	 * Gets the current progress that will be fired.
	 * 
	 * @return int the progress value
	 */
	public int getProgress()
	{
		return progress;
	}

	/**
	 * The end date in the search condition.
	 * 
	 * @return Date end date value
	 */
	public Date getEndDate()
	{
		return evt.getEndDate();
	}

	/**
	 * The keywords in the search condition.
	 * 
	 * @return String[] array of keywords fo searching
	 */
	public String[] getKeywords()
	{
		return evt.getKeywords();
	}

	/**
	 * The start date in the search condition.
	 * 
	 * @return Date start date value
	 */
	public Date getStartDate()
	{
		return evt.getStartDate();
	}

	/**
	 * Sets the progress that will be fired
	 * 
	 * @param progress
	 *        int progress value
	 */
	public void setProgress(int progress)
	{
		this.progress = progress;
	}

}
