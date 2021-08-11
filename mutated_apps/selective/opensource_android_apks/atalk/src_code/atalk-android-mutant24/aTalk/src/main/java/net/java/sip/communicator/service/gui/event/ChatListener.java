/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.gui.event;

import net.java.sip.communicator.service.gui.Chat;

/**
 * Listens to the creation and closing of <tt>Chat</tt>s.
 *
 * @author Damian Johnson
 * @author Lyubomir Marinov
 * @author Eng Chong Meng
 */
public interface ChatListener
{
    /**
     * Notifies this instance that a <tt>Chat</tt> has been closed.
     *
     * @param chat the <tt>Chat</tt> which has been closed
     */
    void chatClosed(Chat chat);

    /**
     * Notifies this instance that a new <tt>Chat</tt> has been created.
     *
     * @param chat the new <tt>Chat</tt> which has been created
     */
    void chatCreated(Chat chat);
}
