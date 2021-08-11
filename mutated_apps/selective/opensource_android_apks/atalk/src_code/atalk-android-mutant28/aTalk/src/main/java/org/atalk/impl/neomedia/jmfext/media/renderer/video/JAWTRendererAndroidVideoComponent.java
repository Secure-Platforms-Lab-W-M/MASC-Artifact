/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.jmfext.media.renderer.video;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.View;

import org.atalk.android.plugin.timberlog.TimberLog;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.java.awt.Graphics;
import org.atalk.service.neomedia.ViewAccessor;

import javax.microedition.khronos.opengles.GL10;

/**
 * Implements <tt>java.awt.Component</tt> for <tt>JAWTRenderer</tt> on Android using a
 * {@link GLSurfaceView}.
 *
 * @author Lyubomir Marinov
 * @author Eng Chong Meng
 */
public class JAWTRendererAndroidVideoComponent extends Component implements ViewAccessor
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * The <tt>JAWTRenderer</tt> which is to use or is using this instance as its visual
     * <tt>Component</tt>.
     */
    private final JAWTRenderer renderer;

    /**
     * The <tt>GLSurfaceView</tt> is the actual visual counterpart of this
     * <tt>java.awt.Component</tt>.
     */
    private GLSurfaceView view;

    /**
     * Initializes a new <tt>JAWTRendererAndroidVideoComponent</tt> which is to be the visual
     * <tt>Component</tt> of a specific <tt>JAWTRenderer</tt>.
     *
     * @param renderer the <tt>JAWTRenderer</tt> which is to use the new instance as its visual
     * <tt>Component</tt>
     */
    public JAWTRendererAndroidVideoComponent(JAWTRenderer renderer)
    {
        this.renderer = renderer;
    }

    /**
     * Implements {@link ViewAccessor#getView(Context)}. Gets the {@link View} provided by this
     * instance which is to be used in a specific {@link Context}.
     *
     * @param context the <tt>Context</tt> in which the provided <tt>View</tt> will be used
     * @return the <tt>View</tt> provided by this instance which is to be used in a specific
     * <tt>Context</tt>
     * @see ViewAccessor#getView(Context)
     */
    public synchronized GLSurfaceView getView(Context context)
    {
        if ((view == null) && (context != null)) {
            view = new GLSurfaceView(context);
            if (TimberLog.isTraceEnable)
                view.setDebugFlags(GLSurfaceView.DEBUG_LOG_GL_CALLS);
            view.setRenderer(new GLSurfaceView.Renderer()
            {
                /**
                 * Implements {@link GLSurfaceView.Renderer#onDrawFrame(GL10)}. Draws the current
                 * frame.
                 *
                 * @param gl
                 *        the <tt>GL10</tt> interface with which the drawing is to be performed
                 */
                public void onDrawFrame(GL10 gl)
                {
                    JAWTRendererAndroidVideoComponent.this.onDrawFrame(gl);
                }

                public void onSurfaceChanged(GL10 gl, int width, int height)
                {
                    // TODO Auto-generated method stub
                }

                public void onSurfaceCreated(GL10 gl,
                        javax.microedition.khronos.egl.EGLConfig config)
                {
                    // TODO Auto-generated method stub
                }
            });
            view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
        return view;
    }

    /**
     * Called by the <tt>GLSurfaceView</tt> which is the actual visual counterpart of this
     * <tt>java.awt.Component</tt> to draw the current frame.
     *
     * @param gl the <tt>GL10</tt> interface with which the drawing is to be performed
     */
    protected void onDrawFrame(GL10 gl)
    {
        synchronized (renderer.getHandleLock()) {
            long handle = renderer.getHandle();

            if (handle != 0) {
                Graphics g = null;
                int zOrder = -1;

                JAWTRenderer.paint(handle, this, g, zOrder);
            }
        }
    }

    @Override
    public synchronized void repaint()
    {
        if (view != null)
            view.requestRender();
    }
}
