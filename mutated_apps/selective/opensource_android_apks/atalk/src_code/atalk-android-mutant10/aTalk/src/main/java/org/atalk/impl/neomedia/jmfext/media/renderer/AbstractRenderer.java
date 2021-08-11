/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.jmfext.media.renderer;

import org.atalk.impl.neomedia.control.ControlsAdapter;

import javax.media.Format;
import javax.media.Renderer;

import timber.log.Timber;

/**
 * Provides an abstract base implementation of <tt>Renderer</tt> in order to facilitate extenders.
 *
 * @param <T> the type of <tt>Format</tt> of the media data processed as input by <tt>AbstractRenderer</tt>
 * @author Lyubomir Marinov
 * @author Eng Chong Meng
 */
public abstract class AbstractRenderer<T extends Format> extends ControlsAdapter
        implements Renderer
{
    /**
     * The <tt>Format</tt> of the media data processed as input by this <tt>Renderer</tt>.
     */
    protected T inputFormat;

    /**
     * Resets the state of this <tt>PlugIn</tt>.
     */
    public void reset()
    {
        // TODO Auto-generated method stub
    }

    /**
     * Sets the <tt>Format</tt> of the media data to be rendered by this <tt>Renderer</tt>.
     *
     * @param format the <tt>Format</tt> of the media data to be rendered by this <tt>Renderer</tt>
     * @return <tt>null</tt> if the specified <tt>format</tt> is not compatible with this
     * <tt>Renderer</tt>; otherwise, the <tt>Format</tt> which has been successfully set
     */
    public Format setInputFormat(Format format)
    {
        Format matchingFormat = null;
        for (Format supportedInputFormat : getSupportedInputFormats()) {
            if (supportedInputFormat.matches(format)) {
                matchingFormat = supportedInputFormat.intersects(format);
                break;
            }
        }
        if (matchingFormat == null)
            return null;

        @SuppressWarnings("unchecked")
        T t = (T) matchingFormat;

        inputFormat = t;
        return inputFormat;
    }

    /**
     * Changes the priority of the current thread to a specific value.
     *
     * @param threadPriority the priority to set the current thread to
     */
    public static void useThreadPriority(int threadPriority)
    {
        Throwable throwable = null;
        try {
            Thread.currentThread().setPriority(threadPriority);
        } catch (IllegalArgumentException | SecurityException iae) {
            throwable = iae;
        }
        if (throwable != null) {
            Timber.w(throwable, "Failed to use thread priority: %s", threadPriority);
        }
    }
}
