/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol;

import net.java.sip.communicator.service.protocol.event.*;

import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.List;
import java.util.Map;

/**
 * Allows creating, configuring, joining and administering of individual text-based conference
 * rooms.
 *
 * @author Emil Ivov
 * @author Eng Chong Meng
 */
public interface OperationSetMultiUserChat extends OperationSet
{
    /**
     * Returns the <tt>List</tt> of <tt>String</tt>s indicating chat rooms currently available on
     * the server that this protocol provider is connected to.
     *
     * @return a <tt>java.util.List</tt> of the name <tt>String</tt>s for chat rooms that are
     * currently available on the server that this protocol provider is connected to.
     * @throws OperationFailedException if we failed retrieving this list from the server.
     * @throws OperationNotSupportedException if the server does not support multi-user chat
     */
    List<EntityBareJid> getExistingChatRooms()
            throws OperationFailedException, OperationNotSupportedException;

    /**
     * Returns a list of the chat rooms that we have joined and are currently active in.
     *
     * @return a <tt>List</tt> of the rooms where the user has joined using a given connection.
     */
    List<ChatRoom> getCurrentlyJoinedChatRooms();

    /**
     * Returns a list of the chat rooms that <tt>chatRoomMember</tt> has joined and is currently active in.
     *
     * @param chatRoomMember the chatRoomMember whose current ChatRooms we will be querying.
     * @return a list of the chat rooms that <tt>chatRoomMember</tt> has joined and is currently
     * active in.
     * @throws OperationFailedException if an error occurs while trying to discover the room on the server.
     * @throws OperationNotSupportedException if the server does not support multi-user chat
     */
    List<String> getCurrentlyJoinedChatRooms(ChatRoomMember chatRoomMember)
            throws OperationFailedException, OperationNotSupportedException;

    /**
     * Creates a room with the named <tt>roomName</tt> and according to the specified
     * <tt>roomProperties</tt> on the server that this protocol provider is currently connected to.
     * When the method returns the room the local user will not have joined it and thus will not
     * receive messages on it until the <tt>ChatRoom.join()</tt> method is called.
     *
     * @param roomName the name of the <tt>ChatRoom</tt> to create.
     * @param roomProperties properties specifying how the room should be created; <tt>null</tt> for no properties
     * just like an empty <code>Map</code>
     * @return the newly created <tt>ChatRoom</tt> named <tt>roomName</tt>.
     * @throws OperationFailedException if the room couldn't be created for some reason (e.g. room already exists; user
     * already joined to an existent room or user has no permissions to create a chat room).
     * @throws OperationNotSupportedException if chat room creation is not supported by this server
     */
    ChatRoom createChatRoom(String roomName, Map<String, Object> roomProperties)
            throws OperationFailedException, OperationNotSupportedException, XmppStringprepException;

    /**
     * Returns a reference to a chatRoom named <tt>roomName</tt> or null if no room with the given
     * name exist on the server.
     *
     * @param roomName the name of the <tt>ChatRoom</tt> that we're looking for.
     * @return the <tt>ChatRoom</tt> named <tt>roomName</tt> if it exists, null otherwise.
     * @throws OperationFailedException if an error occurs while trying to discover the room on the server.
     * @throws OperationNotSupportedException if the server does not support multi-user chat
     */
    ChatRoom findRoom(String roomName)
            throws OperationFailedException, OperationNotSupportedException, XmppStringprepException;

    /**
     * @param entityBareJid ChatRoom EntityBareJid
     * @return ChatRoom
     */
    ChatRoom findRoom(EntityBareJid entityBareJid);

    /**
     * Informs the sender of an invitation that we decline their invitation.
     *
     * @param invitation the invitation we are rejecting.
     * @param rejectReason the reason to reject the invitation (optional)
     */
    void rejectInvitation(ChatRoomInvitation invitation, String rejectReason)
            throws OperationFailedException;

    /**
     * Adds a listener to invitation notifications. The listener will be fired anytime an invitation is received.
     *
     * @param listener an invitation listener.
     */
    void addInvitationListener(ChatRoomInvitationListener listener);

    /**
     * Removes <tt>listener</tt> from the list of invitation listeners registered to receive invitation events.
     *
     * @param listener the invitation listener to remove.
     */
    void removeInvitationListener(ChatRoomInvitationListener listener);

    /**
     * Adds a listener to invitation notifications. The listener will be fired anytime an invitation is received.
     *
     * @param listener an invitation listener.
     */
    void addInvitationRejectionListener(ChatRoomInvitationRejectionListener listener);

    /**
     * Removes the given listener from the list of invitation listeners registered to receive events
     * every time an invitation has been rejected.
     *
     * @param listener the invitation listener to remove.
     */
    void removeInvitationRejectionListener(ChatRoomInvitationRejectionListener listener);

    /**
     * Returns true if <tt>contact</tt> supports multi-user chat sessions.
     *
     * @param contact reference to the contact whose support for chat rooms we are currently querying.
     * @return a boolean indicating whether <tt>contact</tt> supports chat rooms.
     */
    boolean isMultiChatSupportedByContact(Contact contact);

    /**
     * Checks if the contact address is associated with private messaging contact or not.
     *
     * @return <tt>true</tt> if the contact address is associated with private messaging contact and <tt>false</tt> if not.
     */
    boolean isPrivateMessagingContact(String contactAddress);

    /**
     * Adds a listener that will be notified of changes in our participation in a chat room such as
     * us being kicked, joined, left.
     *
     * @param listener a local user participation listener.
     */
    void addPresenceListener(LocalUserChatRoomPresenceListener listener);

    /**
     * Removes a listener that was being notified of changes in our participation in a room such as
     * us being kicked, joined, left.
     *
     * @param listener a local user participation listener.
     */
    void removePresenceListener(LocalUserChatRoomPresenceListener listener);
}
