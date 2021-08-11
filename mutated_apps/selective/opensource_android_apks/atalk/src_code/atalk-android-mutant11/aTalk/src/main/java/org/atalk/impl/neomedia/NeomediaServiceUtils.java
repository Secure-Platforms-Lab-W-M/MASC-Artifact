/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia;

import org.atalk.service.libjitsi.LibJitsi;

/**
 *
 * @author Lyubomir Marinov
 */
public class NeomediaServiceUtils
{
	public static MediaServiceImpl getMediaServiceImpl()
	{
		return (MediaServiceImpl) LibJitsi.getMediaService();
	}

	/**
	 * Prevents the initialization of <tt>NeomediaServiceUtils</tt> instances.
	 */
	private NeomediaServiceUtils()
	{
	}
}
