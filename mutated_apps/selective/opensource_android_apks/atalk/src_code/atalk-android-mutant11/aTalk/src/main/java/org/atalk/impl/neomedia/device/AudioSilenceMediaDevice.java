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
package org.atalk.impl.neomedia.device;

import org.atalk.service.neomedia.MediaDirection;

import javax.media.*;
import javax.media.protocol.*;

/**
 * Implements a <tt>MediaDevice</tt> which provides silence in the form of audio
 * media and does not play back any (audio) media (because Jitsi Videobridge is
 * a server-side technology).
 *
 * @author Lyubomir Marinov
 * @author Boris Grozev
 */
public class AudioSilenceMediaDevice
    extends AudioMediaDeviceImpl
{
    /**
     * The value to pass as the {@code clockOnly} flag to {@link
     * AudioSilenceCaptureDevice} when creating a {@link CaptureDevice}.
     * See {@link AudioSilenceCaptureDevice#clockOnly}.
     */
    private boolean clockOnly = true;

    /**
     * Initializes a new {@link AudioSilenceMediaDevice} instance.
     */
    public AudioSilenceMediaDevice()
    {
    }

    /**
     * Initializes a new {@link AudioSilenceMediaDevice} instance.
     * @param clockOnly the value to set to {@link #clockOnly}.
     */
    public AudioSilenceMediaDevice(boolean clockOnly)
    {
        this.clockOnly = clockOnly;
    }

    /**
     * {@inheritDoc}
     *
     * Overrides the super implementation to initialize a <tt>CaptureDevice</tt>
     * without asking FMJ to initialize one for a <tt>CaptureDeviceInfo</tt>.
     */
    @Override
    protected CaptureDevice createCaptureDevice()
    {
        return new AudioSilenceCaptureDevice(clockOnly);
    }

    /**
     * {@inheritDoc}
     *
     * Overrides the super implementation to disable the very playback because
     * Jitsi Videobridge is a server-side technology.
     */
    @Override
    protected Processor createPlayer(DataSource dataSource)
    {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * Overrides the super implementation to initialize a
     * <tt>MediaDeviceSession</tt> which disables the very playback because
     * Jitsi Videobridge is a server-side technology.
     */
    @Override
    public MediaDeviceSession createSession()
    {
        return
            new AudioMediaDeviceSession(this)
                    {
                        /**
                         * {@inheritDoc}
                         *
                         * Overrides the super implementation to disable the
                         * very playback because Jitsi Videobridge is a
                         * server-side technology.
                         */
                        @Override
                        protected Player createPlayer(DataSource dataSource)
                        {
                            return null;
                        }
                    };
    }

    /**
     * {@inheritDoc}
     *
     * Overrides the super implementation to always return
     * {@link MediaDirection#SENDRECV} because this instance stands for a relay
     * and because the super bases the <tt>MediaDirection</tt> on the
     * <tt>CaptureDeviceInfo</tt> which this instance does not have.
     */
    @Override
    public MediaDirection getDirection()
    {
        return MediaDirection.SENDRECV;
    }
}
