/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.jmfext.media.protocol.androidcamera;

import android.graphics.SurfaceTexture;
import android.view.Surface;

import org.atalk.impl.neomedia.device.util.*;
import org.atalk.service.osgi.OSGiActivity;

import java.io.IOException;

import javax.media.Buffer;
import javax.media.control.FormatControl;

import timber.log.Timber;

/**
 * Camera stream that uses <tt>Surface</tt> to capture video. First input <tt>Surface</tt> is
 * obtained from <tt>MediaCodec</tt>. Then it is passed as preview surface to the camera.
 * <tt>Surface</tt> instance is passed through buffer objects in read method - this stream won't
 * start until it's not provided. <br/>
 * <br/>
 * In order to display local camera preview in the app, <tt>TextureView</tt> is created in video
 * call <tt>Activity</tt>. It is used to create Open GL context that shares video texture and can
 * render it. Rendering is done here on camera capture <tt>Thread</tt>.
 *
 * @author Pawel Domas
 * @author Eng Chong Meng
 */
public class SurfaceStream extends CameraStreamBase
{
    /**
     * Codec input surface obtained from <tt>MediaCodec</tt>.
     */
    private CodecInputSurface inputSurface;

    /**
     * Surface texture manager that manages input surface.
     */
    private SurfaceTextureManager surfaceManager;

    /**
     * <tt>Surface</tt> object obtained from <tt>MediaCodec</tt>.
     */
    private Surface encoderSurface;

    /**
     * Flag used to stop capture thread.
     */
    private boolean run = false;

    /**
     * Capture thread.
     */
    private Thread captureThread;

    /**
     * <tt>OpenGlCtxProvider</tt> used by this instance.
     */
    private OpenGlCtxProvider myCtxProvider;

    /**
     * Object used to synchronize local preview painting.
     */
    private final Object paintLock = new Object();

    /**
     * Flag indicates that the local preview has been painted.
     */
    private boolean paintDone;

    /**
     * Creates new instance of <tt>SurfaceStream</tt>.
     *
     * @param parent parent <tt>DataSource</tt>.
     * @param formatControl format control used by this instance.
     */
    SurfaceStream(DataSource parent, FormatControl formatControl)
    {
        super(parent, formatControl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start()
            throws IOException
    {
        super.start();
        run = true;
        captureThread = new Thread()
        {
            @Override
            public void run()
            {
                captureLoop();
            }
        };
        captureThread.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitPreview()
            throws IOException
    {
        myCtxProvider = CameraUtils.localPreviewCtxProvider;
        OpenGLContext previewCtx = myCtxProvider.obtainObject();
        this.inputSurface = new CodecInputSurface(encoderSurface, previewCtx.getContext());
        // Make current
        inputSurface.ensureIsCurrentCtx();
        // Prepare preview texture
        surfaceManager = new SurfaceTextureManager();
        SurfaceTexture st = surfaceManager.getSurfaceTexture();
        mCamera.setPreviewTexture(st);
    }

    /**
     * Capture thread loop.
     */
    private void captureLoop()
    {
        // Wait for input surface
        while (run && mCamera == null && surfaceManager == null) {
            // Post empty frame to init encoder and get the surface (it will be provided in read() method
            transferHandler.transferData(this);
        }

        while (run) {
            SurfaceTexture st = surfaceManager.getSurfaceTexture();
            surfaceManager.awaitNewImage();

            // Renders the preview on main thread
            paintLocalPreview();

            // TODO: use frame rate supplied by format control
            long delay = calcStats();
            if (delay < 80) {
                try {
                    long wait = 80 - delay;
                    Timber.d("Delaying frame: %s", wait);
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // Pushes the frame to the encoder
            inputSurface.ensureIsCurrentCtx();
            surfaceManager.drawImage();
            inputSurface.setPresentationTime(st.getTimestamp());
            inputSurface.swapBuffers();
            transferHandler.transferData(this);
        }
    }

    /**
     * Paints the local preview on UI thread by posting paint job and waiting for the UI handler to
     * complete it's job.
     */
    private void paintLocalPreview()
    {
        paintDone = false;
        OSGiActivity.uiHandler.post(() -> {
            try {
                // assert AndroidUtils.isUIThread();

                OpenGLContext previewCtx = myCtxProvider.tryObtainObject();
                /*
                 * If we will not wait until local preview frame is posted to the
                 * TextureSurface((onSurfaceTextureUpdated) we will freeze on trying to set the
                 * current context. We skip the frame in this case.
                 */
                if (previewCtx == null || !myCtxProvider.textureUpdated) {
                    Timber.w("Skipped preview frame, ctx: %s textureUpdated: %s",
                            previewCtx, myCtxProvider.textureUpdated);
                }
                else {
                    previewCtx.ensureIsCurrentCtx();
                    surfaceManager.drawImage();
                    previewCtx.swapBuffers();
                    /*
                     * If current context is not unregistered the main thread will freeze at: at
                     * com.google.android.gles_jni.EGLImpl.eglMakeCurrent(EGLImpl.java:-1) at
                     * android.view.HardwareRenderer$GlRenderer.checkRenderContextUnsafe(HardwareRenderer.java:1767) at
                     * android.view.HardwareRenderer$GlRenderer.draw(HardwareRenderer.java:1438)
                     * at android.view.ViewRootImpl.draw(ViewRootImpl.java:2381) .... at
                     * com.android.internal.os.ZygoteInit.main(ZygoteInit.java:595) at
                     * dalvik.system.NativeStart.main(NativeStart.java:-1)
                     */
                    previewCtx.ensureIsNotCurrentCtx();
                    myCtxProvider.textureUpdated = false;
                }
            } finally {
                synchronized (paintLock) {
                    paintDone = true;
                    paintLock.notifyAll();
                }
            }
        });
        // Wait for the main thread to finish painting
        synchronized (paintLock) {
            if (!paintDone) {
                try {
                    paintLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(Buffer buffer)
            throws IOException
    {
        Surface surface = (Surface) buffer.getData();
        if (mCamera == null && surface != null) {
            this.encoderSurface = surface;
            startImpl();
        }
        if (surfaceManager != null) {
            buffer.setTimeStamp(surfaceManager.getSurfaceTexture().getTimestamp());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop()
            throws IOException
    {
        run = false;
        if (captureThread != null) {
            try {
                captureThread.join();
                captureThread = null;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        super.stop();
        if (surfaceManager != null) {
            surfaceManager.release();
            surfaceManager = null;
        }
        myCtxProvider.onObjectReleased();
    }
}
