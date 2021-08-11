/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.calendar;

import net.java.sip.communicator.service.protocol.*;

import java.util.Map;

/**
 * A service for calendar. It defines for accessing the current free busy status and add / remove listeners for the free
 * busy status.
 * 
 * @author Hristo Terezov
 */
public interface CalendarService
{
	/**
	 * Defines the possible free busy statuses.
	 * 
	 * @author Hristo Terezov
	 */
	public enum BusyStatusEnum {
		/**
		 * The Free status.
		 */
		FREE((long) 0x00000000),

		/**
		 * The In meeting status.
		 */
		IN_MEETING((long) 0x00000001),

		/**
		 * The busy status.
		 */
		BUSY((long) 0x00000002),

		/**
		 * The out of office status.
		 */
		OUT_OF_OFFICE((long) 0x00000003);

		/**
		 * The value of the status.
		 */
		private final long value;

		/**
		 * The priority of the status
		 */
		private final Integer priority;

		/**
		 * Constructs new status.
		 * 
		 * @param value
		 *        the value of the status
		 */
		BusyStatusEnum(Long value) {
			this.value = value;
			this.priority = null;
		}

		/**
		 * Returns the value of the status.
		 * 
		 * @return the value of the status.
		 */
		public long getValue()
		{
			return value;
		}

		/**
		 * Finds <tt>BusyStatusEnum</tt> instance by given value of the status.
		 * 
		 * @param value
		 *        the value of the status we are searching for.
		 * @return the status or <tt>FREE</tt> if no status is found.
		 */
		public static BusyStatusEnum getFromLong(Long value)
		{
			for (BusyStatusEnum state : values()) {
				if (state.getValue() == value) {
					return state;
				}
			}
			return FREE;
		}

		/**
		 * Returns the priority of the status
		 * 
		 * @return the priority of the status
		 */
		public int getPriority()
		{
			if (priority != null) {
				return priority;
			}
			return ordinal();
		}
	};

	/**
	 * The name of the configuration property which specifies whether free busy status is disabled i.e. whether it
	 * should set the presence statuses of online accounts to &quot;In Meeting&quot;.
	 */
	public static final String PNAME_FREE_BUSY_STATUS_DISABLED = "calendar.FreeBusyStatus.disabled";

	/**
	 * Returns the current value of the free busy status.
	 * 
	 * @return the current value of the free busy status.
	 */
	public BusyStatusEnum getStatus();

	/**
	 * Adds free busy listener.
	 * 
	 * @param listener
	 *        the listener to be added.
	 */
	public void addFreeBusySateListener(FreeBusySateListener listener);

	/**
	 * Removes free busy listener.
	 * 
	 * @param listener
	 *        the listener to be removed.
	 */
	public void removeFreeBusySateListener(FreeBusySateListener listener);

	/**
	 * Handles presence status changed from "On the Phone"
	 * 
	 * @param presenceStatuses
	 *        the remembered presence statuses
	 * @return <tt>true</tt> if the status is changed.
	 */
	public boolean onThePhoneStatusChanged(Map<ProtocolProviderService, PresenceStatus> presenceStatuses);

	/**
	 * Returns the remembered presence statuses
	 * 
	 * @return the remembered presence statuses
	 */
	public Map<ProtocolProviderService, PresenceStatus> getRememberedStatuses();
}
