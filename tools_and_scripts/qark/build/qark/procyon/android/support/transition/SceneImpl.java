// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.view.View;
import android.view.ViewGroup;

abstract class SceneImpl
{
    public abstract void enter();
    
    public abstract void exit();
    
    public abstract ViewGroup getSceneRoot();
    
    public abstract void init(final ViewGroup p0);
    
    public abstract void init(final ViewGroup p0, final View p1);
    
    public abstract void setEnterAction(final Runnable p0);
    
    public abstract void setExitAction(final Runnable p0);
}
