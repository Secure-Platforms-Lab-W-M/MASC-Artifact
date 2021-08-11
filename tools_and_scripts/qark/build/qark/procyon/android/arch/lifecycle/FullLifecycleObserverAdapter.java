// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.lifecycle;

class FullLifecycleObserverAdapter implements GenericLifecycleObserver
{
    private final FullLifecycleObserver mObserver;
    
    FullLifecycleObserverAdapter(final FullLifecycleObserver mObserver) {
        this.mObserver = mObserver;
    }
    
    @Override
    public void onStateChanged(final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
        switch (event) {
            default: {}
            case ON_CREATE: {
                this.mObserver.onCreate(lifecycleOwner);
            }
            case ON_START: {
                this.mObserver.onStart(lifecycleOwner);
            }
            case ON_RESUME: {
                this.mObserver.onResume(lifecycleOwner);
            }
            case ON_PAUSE: {
                this.mObserver.onPause(lifecycleOwner);
            }
            case ON_STOP: {
                this.mObserver.onStop(lifecycleOwner);
            }
            case ON_DESTROY: {
                this.mObserver.onDestroy(lifecycleOwner);
            }
            case ON_ANY: {
                throw new IllegalArgumentException("ON_ANY must not been send by anybody");
            }
        }
    }
}
