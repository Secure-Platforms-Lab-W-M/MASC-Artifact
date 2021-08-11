/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.androidauthwindow;

import net.java.sip.communicator.service.gui.AuthenticationWindowService;
import net.java.sip.communicator.util.SimpleServiceActivator;

/**
 * Bundle activator for Android <tt>AuthenticationWindowService</tt> impl.
 *
 * @author Pawel Domas
 */
public class AuthWindowActivator extends SimpleServiceActivator<AuthenticationWindowService>
{

	/**
	 * Creates new instance of <tt>AuthenticationWindowService</tt>.
	 */
	public AuthWindowActivator() {
		super(AuthenticationWindowService.class, "AuthenticationWindowService");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AuthenticationWindowService createServiceImpl()
	{
		return new AuthWindowServiceImpl();
	}
}
