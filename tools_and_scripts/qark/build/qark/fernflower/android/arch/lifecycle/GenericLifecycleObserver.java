package android.arch.lifecycle;

public interface GenericLifecycleObserver extends LifecycleObserver {
   void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2);
}
