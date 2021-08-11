/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.device;

import androidx.annotation.NonNull;

/**
 * Implements an <tt>AudioSystem</tt> without any devices which allows the user to select to use no
 * audio capture, notification and playback.
 *
 * @author Lyubomir Marinov
 */
public class NoneAudioSystem extends AudioSystem
{
    public static final String LOCATOR_PROTOCOL = "none";

    public NoneAudioSystem()
            throws Exception
    {
        super(LOCATOR_PROTOCOL);
    }

    @Override
    protected void doInitialize()
            throws Exception
    {
    }

    @NonNull
    @Override
    public String toString()
    {
        return "None";
    }
}
