/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.device.util;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.os.Build;

import org.atalk.android.plugin.timberlog.TimberLog;

import timber.log.Timber;

/**
 * Manages a SurfaceTexture. Creates SurfaceTexture and CameraTextureRender objects, and provides
 * functions that wait for frames and render them to the current EGL surface.
 * <p/>
 * The SurfaceTexture can be passed to Camera.setPreviewTexture() to receive camera output.
 */
public class SurfaceTextureManager implements SurfaceTexture.OnFrameAvailableListener
{
    private SurfaceTexture surfaceTexture;

    private CameraTextureRender textureRender;

    /**
     * guards frameAvailable
     */
    private final Object frameSyncObject = new Object();

    private boolean frameAvailable;

    /**
     * Creates instances of CameraTextureRender and SurfaceTexture.
     */
    public SurfaceTextureManager()
    {

        textureRender = new CameraTextureRender();
        textureRender.surfaceCreated();

        Timber.d("textureID = %s", textureRender.getTextureId());
        surfaceTexture = new SurfaceTexture(textureRender.getTextureId());

        surfaceTexture.setOnFrameAvailableListener(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void release()
    {
        if (textureRender != null) {
            textureRender.release();
            textureRender = null;
        }
        if (surfaceTexture != null) {
            surfaceTexture.release();
            surfaceTexture = null;
        }
    }

    /**
     * Returns the SurfaceTexture.
     */
    public SurfaceTexture getSurfaceTexture()
    {
        return surfaceTexture;
    }

    /**
     * Latches the next buffer into the texture. Must be called from the thread that created the
     * OutputSurface object.
     */
    public void awaitNewImage()
    {
        final int TIMEOUT_MS = 2500;

        synchronized (frameSyncObject) {
            while (!frameAvailable) {
                try {
                    // Wait for onFrameAvailable() to signal us. Use a timeout
                    // to avoid stalling the test if it doesn't arrive.
                    frameSyncObject.wait(TIMEOUT_MS);
                    if (!frameAvailable) {
                        // TODO: if "spurious wakeup", continue while loop
                        throw new RuntimeException("Camera frame wait timed out");
                    }
                } catch (InterruptedException ie) {
                    // shouldn't happen
                    throw new RuntimeException(ie);
                }
            }
            frameAvailable = false;
        }

        // Latch the data.
        textureRender.checkGlError("before updateTexImage");
        surfaceTexture.updateTexImage();
    }

    /**
     * Draws the data from SurfaceTexture onto the current EGL surface.
     */
    public void drawImage()
    {
        textureRender.drawFrame(surfaceTexture);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture st)
    {
        Timber.log(TimberLog.FINER, "new frame available");
        synchronized (frameSyncObject) {
            if (frameAvailable) {
                throw new RuntimeException("frameAvailable already set, frame could be dropped");
            }
            frameAvailable = true;
            frameSyncObject.notifyAll();
        }
    }
}
