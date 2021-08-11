/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.notification;

import java.util.*;

/**
 * Represents an event notification.
 *
 * @author Yana Stamcheva
 */
public class Notification
{
	/**
	 * Indicates if this event notification is currently active. By default all notifications are active.
	 */
	private boolean isActive = true;

	/**
	 * Contains all actions which will be executed when this event notification is fired.
	 */
	private final Hashtable<String, NotificationAction> actionsTable = new Hashtable<>();

	/**
	 * Creates an instance of <tt>EventNotification</tt> by specifying the event type as declared by the bundle
	 * registering it.
	 *
	 * @param eventType
	 *        the name of the event
	 */
	public Notification(String eventType) {
	}

	/**
	 * Adds the given <tt>actionType</tt> to the list of actions for this event notifications.
	 * 
	 * @param action
	 *        the the handler that will process the given action type.
	 *
	 * @return the previous value of the actionHandler for the given actionType, if one existed, NULL if the actionType
	 *         is a new one
	 */
	public Object addAction(NotificationAction action)
	{
		return actionsTable.put(action.getActionType(), action);
	}

	/**
	 * Removes the action corresponding to the given <tt>actionType</tt>.
	 *
	 * @param actionType
	 *        one of NotificationService.ACTION_XXX constants
	 */
	public void removeAction(String actionType)
	{
		actionsTable.remove(actionType);
	}

	/**
	 * Returns the set of actions registered for this event notification.
	 *
	 * @return the set of actions registered for this event notification
	 */
	public Map<String, NotificationAction> getActions()
	{
		return actionsTable;
	}

	/**
	 * Returns the <tt>Action</tt> corresponding to the given <tt>actionType</tt>.
	 *
	 * @param actionType
	 *        one of NotificationService.ACTION_XXX constants
	 *
	 * @return the <tt>Action</tt> corresponding to the given <tt>actionType</tt>
	 */
	public NotificationAction getAction(String actionType)
	{
		return actionsTable.get(actionType);
	}

	/**
	 * Indicates if this event notification is currently active.
	 *
	 * @return true if this event notification is active, false otherwise.
	 */
	public boolean isActive()
	{
		return isActive;
	}

	/**
	 * Activates or deactivates this event notification.
	 *
	 * @param isActive
	 *        indicates if this event notification is active
	 */
	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}
}
