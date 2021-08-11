/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.android.gui.chat.conference;

import java.util.LinkedList;
import java.util.List;

import net.java.sip.communicator.service.protocol.AdHocChatRoom;
import net.java.sip.communicator.service.protocol.ProtocolIcon;
import net.java.sip.communicator.service.protocol.ProtocolProviderService;

/**
 * @author Valentin Martinet
 */
public class AdHocChatRoomProviderWrapper
{
	private final ProtocolProviderService protocolProvider;

	private final List<AdHocChatRoomWrapper> chatRoomsOrderedCopy = new LinkedList<AdHocChatRoomWrapper>();

	/**
	 * Creates an instance of <tt>AdHocChatRoomProviderWrapper</tt> by specifying the protocol provider, corresponding to the ad-hoc multi
	 * user chat account.
	 *
	 * @param protocolProvider
	 *        protocol provider, corresponding to the ad-hoc multi user chat account.
	 */
	public AdHocChatRoomProviderWrapper(ProtocolProviderService protocolProvider)
	{
		this.protocolProvider = protocolProvider;
	}

	/**
	 * Returns the name of this ad-hoc chat room provider.
	 * 
	 * @return the name of this ad-hoc chat room provider.
	 */
	public String getName()
	{
		return protocolProvider.getProtocolDisplayName();
	}

	public byte[] getIcon()
	{
		return protocolProvider.getProtocolIcon().getIcon(ProtocolIcon.ICON_SIZE_64x64);
	}

	public byte[] getImage()
	{
		byte[] logoImage = null;
		ProtocolIcon protocolIcon = protocolProvider.getProtocolIcon();

		if (protocolIcon.isSizeSupported(ProtocolIcon.ICON_SIZE_64x64))
			logoImage = protocolIcon.getIcon(ProtocolIcon.ICON_SIZE_64x64);
		else if (protocolIcon.isSizeSupported(ProtocolIcon.ICON_SIZE_48x48))
			logoImage = protocolIcon.getIcon(ProtocolIcon.ICON_SIZE_48x48);

		return logoImage;
	}

	/**
	 * Returns the protocol provider service corresponding to this server wrapper.
	 *
	 * @return the protocol provider service corresponding to this server wrapper.
	 */
	public ProtocolProviderService getProtocolProvider()
	{
		return protocolProvider;
	}

	/**
	 * Adds the given ad-hoc chat room to this chat room provider.
	 *
	 * @param adHocChatRoom
	 *        the ad-hoc chat room to add.
	 */
	public void addAdHocChatRoom(AdHocChatRoomWrapper adHocChatRoom)
	{
		this.chatRoomsOrderedCopy.add(adHocChatRoom);
	}

	/**
	 * Removes the given ad-hoc chat room from this provider.
	 *
	 * @param adHocChatRoom
	 *        the ad-hoc chat room to remove.
	 */
	public void removeChatRoom(AdHocChatRoomWrapper adHocChatRoom)
	{
		this.chatRoomsOrderedCopy.remove(adHocChatRoom);
	}

	/**
	 * Returns {@code true</code> if the given ad-hoc chat room is contained in this provider, otherwise - returns <code>false}.
	 *
	 * @param adHocChatRoom
	 *        the ad-hoc chat room to search for.
	 * @return {@code true</code> if the given ad-hoc chat room is contained in this provider, otherwise - returns <code>false}.
	 */
	public boolean containsAdHocChatRoom(AdHocChatRoomWrapper adHocChatRoom)
	{
		synchronized (chatRoomsOrderedCopy) {
			return chatRoomsOrderedCopy.contains(adHocChatRoom);
		}
	}

	/**
	 * Returns the ad-hoc chat room wrapper contained in this provider that corresponds to the given ad-hoc chat room.
	 *
	 * @param adHocChatRoom
	 *        the ad-hoc chat room we're looking for.
	 * @return the ad-hoc chat room wrapper contained in this provider that corresponds to the given ad-hoc chat room.
	 */
	public AdHocChatRoomWrapper findChatRoomWrapperForAdHocChatRoom(AdHocChatRoom adHocChatRoom)
	{
		// compare ids, cause saved ad-hoc chatrooms don't have AdHocChatRoom
		// object but Id's are the same
		for (AdHocChatRoomWrapper chatRoomWrapper : chatRoomsOrderedCopy) {
			if (chatRoomWrapper.getAdHocChatRoomID().equals(adHocChatRoom.getIdentifier())) {
				return chatRoomWrapper;
			}
		}

		return null;
	}

	/**
	 * Returns the number of ad-hoc chat rooms contained in this provider.
	 *
	 * @return the number of ad-hoc chat rooms contained in this provider.
	 */
	public int countAdHocChatRooms()
	{
		return chatRoomsOrderedCopy.size();
	}

	public AdHocChatRoomWrapper getAdHocChatRoom(int index)
	{
		return chatRoomsOrderedCopy.get(index);
	}

	/**
	 * Returns the index of the given chat room in this provider.
	 *
	 * @param chatRoomWrapper
	 *        the chat room to search for.
	 *
	 * @return the index of the given chat room in this provider.
	 */
	public int indexOf(AdHocChatRoomWrapper chatRoomWrapper)
	{
		return chatRoomsOrderedCopy.indexOf(chatRoomWrapper);
	}
}
