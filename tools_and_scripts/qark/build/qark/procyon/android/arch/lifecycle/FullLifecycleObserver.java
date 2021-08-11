// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.lifecycle;

interface FullLifecycleObserver extends LifecycleObserver
{
    void onCreate(final LifecycleOwner p0);
    
    void onDestroy(final LifecycleOwner p0);
    
    void onPause(final LifecycleOwner p0);
    
    void onResume(final LifecycleOwner p0);
    
    void onStart(final LifecycleOwner p0);
    
    void onStop(final LifecycleOwner p0);
}
