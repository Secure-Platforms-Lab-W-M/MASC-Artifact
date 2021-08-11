/*
 * Copyright @ 2015 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.atalk.util.event;

import org.atalk.android.util.java.awt.Component;

import java.util.EventObject;

/**
 * Represents an event fired by providers of visual <tt>Component</tt>s depicting video to notify
 * about changes in the availability of such <tt>Component</tt>s.
 *
 * @author Lyubomir Marinov
 */
public class VideoEvent extends EventObject
{
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * The video origin of a <tt>VideoEvent</tt> which is local to the executing
	 * client such as a local video capture device.
	 */
	public static final int LOCAL = 1;

	/**
	 * The video origin of a <tt>VideoEvent</tt> which is remote to the
	 * executing client such as a video being remotely streamed from a
	 * <tt>CallPeer</tt>.
	 */
	public static final int REMOTE = 2;

	/**
	 * The type of a <tt>VideoEvent</tt> which notifies about a specific visual
	 * <tt>Component</tt> depicting video being made available by the firing
	 * provider.
	 */
	public static final int VIDEO_ADDED = 1;

	/**
	 * The type of a <tt>VideoEvent</tt> which notifies about a specific visual
	 * <tt>Component</tt> depicting video no longer being made available by the
	 * firing provider.
	 */
	public static final int VIDEO_REMOVED = 2;

	/**
	 * The type of a <tt>VideoEvent</tt> which notifies about an update to the
	 * size of a specific visual <tt>Component</tt> depicting video.
	 */
	public static final int VIDEO_SIZE_CHANGE = 3;

	/**
	 * The indicator which determines whether this event and, more specifically,
	 * the visual <tt>Component</tt> it describes have been consumed and should
	 * be considered owned, referenced (which is important because
	 * <tt>Component</tt>s belong to a single <tt>Container</tt> at a time).
	 */
	private boolean consumed;

	/**
	 * The origin of the video this <tt>VideoEvent</tt> notifies about which is
	 * one of {@link #LOCAL} and {@link #REMOTE}.
	 */
	private final int origin;

	/**
	 * The type of availability change this <tt>VideoEvent</tt> notifies about
	 * which is one of {@link #VIDEO_ADDED} and {@link #VIDEO_REMOVED}.
	 */
	private final int type;

	/**
	 * The visual <tt>Component</tt> depicting video which had its availability
	 * changed and which this <tt>VideoEvent</tt> notifies about.
	 */
	private final Component visualComponent;

	/**
	 * Initializes a new <tt>VideoEvent</tt> which is to notify about a specific
	 * change in the availability of a specific visual <tt>Component</tt>
	 * depicting video and being provided by a specific source.
	 *
	 * @param source
	 * 		the source of the new <tt>VideoEvent</tt> and the provider
	 * 		of the visual <tt>Component</tt> depicting video
	 * @param type
	 * 		the type of the availability change which has caused the new
	 * 		<tt>VideoEvent</tt> to be fired
	 * @param visualComponent
	 * 		the visual <tt>Component</tt> depicting video
	 * 		which had its availability in the <tt>source</tt> provider changed
	 * @param origin
	 * 		the origin of the video the new <tt>VideoEvent</tt> is to
	 * 		notify about
	 */
	public VideoEvent(Object source, int type,
			Component visualComponent, int origin)
	{
		super(source);
		this.type = type;
		this.visualComponent = visualComponent;
		this.origin = origin;
	}

	/**
	 * Initializes a new instance of the run-time type of this instance which
	 * has the same property values as this instance except for the source which
	 * is set on the new instance to a specific value.
	 *
	 * @param source
	 * 		the <tt>Object</tt> which is to be reported as the source
	 * 		of the new instance
	 * @return a new instance of the run-time type of this instance which has
	 * the same property values as this instance except for the source which is
	 * set on the new instance to the specified <tt>source</tt>
	 */
	public VideoEvent clone(Object source)
	{
		return new VideoEvent(source,
				getType(), getVisualComponent(), getOrigin());
	}

	/**
	 * Consumes this event and, more specifically, marks the <tt>Component</tt>
	 * it describes as owned, referenced in order to let other potential
	 * consumers know about its current ownership status (which is important
	 * because <tt>Component</tt>s belong to a single <tt>Container</tt> at a time).
	 */
	public void consume()
	{
		consumed = true;
	}

	/**
	 * Gets the origin of the video this <tt>VideoEvent</tt> notifies about
	 * which is one of {@link #LOCAL} and {@link #REMOTE}.
	 *
	 * @return one of {@link #LOCAL} and {@link #REMOTE} which specifies the
	 * origin of the video this <tt>VideoEvent</tt> notifies about
	 */
	public int getOrigin()
	{
		return origin;
	}

	/**
	 * Gets the type of availability change this <tt>VideoEvent</tt> notifies
	 * about which is one of {@link #VIDEO_ADDED} and {@link #VIDEO_REMOVED}.
	 *
	 * @return one of {@link #VIDEO_ADDED} and {@link #VIDEO_REMOVED} which
	 * describes the type of availability change this <tt>VideoEvent</tt>
	 * notifies about
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * Gets the visual <tt>Component</tt> depicting video which had its
	 * availability changed and which this <tt>VideoEvent</tt> notifies about.
	 *
	 * @return the visual <tt>Component</tt> depicting video which had its
	 * availability changed and which this <tt>VideoEvent</tt> notifies about
	 */
	public Component getVisualComponent()
	{
		return visualComponent;
	}

	/**
	 * Determines whether this event and, more specifically, the visual
	 * <tt>Component</tt> it describes have been consumed and should be
	 * considered owned, referenced (which is important because
	 * <tt>Component</tt>s belong to a single <tt>Container</tt> at a time).
	 *
	 * @return <tt>true</tt> if this event and, more specifically, the visual
	 * <tt>Component</tt> it describes have been consumed and should be
	 * considered owned, referenced (which is important because
	 * <tt>Component</tt>s belong to a single <tt>Container</tt> at a time);
	 * otherwise, <tt>false</tt>
	 */
	public boolean isConsumed()
	{
		return consumed;
	}

	/**
	 * Returns a human-readable representation of a specific <tt>VideoEvent</tt>
	 * origin constant in the form of a <tt>String</tt> value.
	 *
	 * @param origin
	 * 		one of the <tt>VideoEvent</tt> origin constants such as
	 * 		{@link #LOCAL} or {@link #REMOTE}
	 * @return a <tt>String</tt> value which gives a human-readable
	 * representation of the specified <tt>VideoEvent</tt> <tt>origin</tt>
	 * constant
	 */
	public static String originToString(int origin)
	{
		switch (origin) {
			case VideoEvent.LOCAL:
				return "LOCAL";
			case VideoEvent.REMOTE:
				return "REMOTE";
			default:
				throw new IllegalArgumentException("origin");
		}
	}

	/**
	 * Returns a human-readable representation of a specific <tt>VideoEvent</tt>
	 * type constant in the form of a <tt>String</tt> value.
	 *
	 * @param type
	 * 		one of the <tt>VideoEvent</tt> type constants such as
	 * 		{@link #VIDEO_ADDED} or {@link #VIDEO_REMOVED}
	 * @return a <tt>String</tt> value which gives a human-readable
	 * representation of the specified <tt>VideoEvent</tt> <tt>type</tt> constant
	 */
	public static String typeToString(int type)
	{
		switch (type) {
			case VideoEvent.VIDEO_ADDED:
				return "VIDEO_ADDED";
			case VideoEvent.VIDEO_REMOVED:
				return "VIDEO_REMOVED";
			default:
				throw new IllegalArgumentException("type");
		}
	}
}
