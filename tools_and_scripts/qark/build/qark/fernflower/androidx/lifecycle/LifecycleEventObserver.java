package androidx.lifecycle;

public interface LifecycleEventObserver extends LifecycleObserver {
   void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2);
}
