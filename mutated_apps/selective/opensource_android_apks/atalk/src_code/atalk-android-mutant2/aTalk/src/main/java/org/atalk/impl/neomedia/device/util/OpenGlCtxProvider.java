/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.device.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.os.Build;
import android.view.*;

import net.java.sip.communicator.util.Logger;

import org.atalk.android.plugin.timberlog.TimberLog;

import timber.log.Timber;

/**
 * Provider of Open GL context. Currently used to provide shared context for recorded video that
 * will be used to draw local preview.
 *
 * @author Pawel Domas
 * @author Eng Chong Meng
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class OpenGlCtxProvider extends ViewDependentProvider<OpenGLContext>
        implements TextureView.SurfaceTextureListener
{
    /**
     * The <tt>OpenGLContext</tt>.
     */
    OpenGLContext context;

    /**
     * Flag used to inform the <tt>SurfaceStream</tt> that the <tt>onSurfaceTextureUpdated</tt>
     * event has occurred.
     */
    public boolean textureUpdated = true;

    /**
     * Creates new instance of <tt>OpenGlCtxProvider</tt>.
     *
     * @param activity parent <tt>Activity</tt>.
     * @param container the container that will hold maintained <tt>View</tt>.
     */
    public OpenGlCtxProvider(Activity activity, ViewGroup container)
    {
        super(activity, container);
    }

    @Override
    protected View createViewInstance()
    {
        TextureView textureView = new TextureView(activity);
        textureView.setSurfaceTextureListener(this);
        return textureView;
    }

    @Override
    synchronized public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height)
    {
        context = new OpenGLContext(false, surface, EGL14.EGL_NO_CONTEXT);
        onObjectCreated(context);
    }

    @Override
    synchronized public boolean onSurfaceTextureDestroyed(SurfaceTexture surface)
    {
        onObjectDestroyed();
        if (context != null) {
            // Release context only when the View is destroyed
            context.release();
            context = null;
        }
        return false;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height)
    {
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface)
    {
        Timber.log(TimberLog.FINER, "onSurfaceTextureUpdated");
        this.textureUpdated = true;
    }
}
