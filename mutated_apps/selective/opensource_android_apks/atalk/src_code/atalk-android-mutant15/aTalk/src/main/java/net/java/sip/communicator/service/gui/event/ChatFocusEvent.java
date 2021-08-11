/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.gui.event;

import net.java.sip.communicator.service.gui.Chat;

import java.util.EventObject;

/**
 * The <tt>ChatFocusEvent</tt> indicates that a <tt>Chat</tt> has gained or lost the current focus.
 *
 * @author Yana Stamcheva
 * @author Eng Chong Meng
 */
public class ChatFocusEvent extends EventObject
{
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 0L;

    /**
     * ID of the event.
     */
    private int eventID;

    /**
     * Indicates that the ChatFocusEvent instance was triggered by <tt>Chat</tt> gaining the focus.
     */
    public static final int FOCUS_GAINED = 1;

    /**
     * Indicates that the ChatFocusEvent instance was triggered by <tt>Chat</tt> losing the focus.
     */
    public static final int FOCUS_LOST = 2;

    /**
     * Creates a new <tt>ChatFocusEvent</tt> according to the specified parameters.
     *
     * @param source The <tt>Chat</tt> that triggers the event.
     * @param eventID one of the FOCUS_XXX static fields indicating the nature of the event.
     */
    public ChatFocusEvent(Object source, int eventID)
    {
        super(source);
        this.eventID = eventID;
    }

    /**
     * Returns an event id specifying what is the type of this event (FOCUS_GAINED or FOCUS_LOST)
     *
     * @return one of the REGISTRATION_XXX int fields of this class.
     */
    public int getEventID()
    {
        return eventID;
    }

    /**
     * Returns the <tt>Chat</tt> object that corresponds to this event.
     *
     * @return the <tt>Chat</tt> object that corresponds to this event
     */
    public Chat getChat()
    {
        return (Chat) source;
    }
}
