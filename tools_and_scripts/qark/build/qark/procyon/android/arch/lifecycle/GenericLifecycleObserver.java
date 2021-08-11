// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.lifecycle;

public interface GenericLifecycleObserver extends LifecycleObserver
{
    void onStateChanged(final LifecycleOwner p0, final Lifecycle.Event p1);
}
