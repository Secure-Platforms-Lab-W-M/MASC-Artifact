package androidx.lifecycle;

interface FullLifecycleObserver extends LifecycleObserver {
   void onCreate(LifecycleOwner var1);

   void onDestroy(LifecycleOwner var1);

   void onPause(LifecycleOwner var1);

   void onResume(LifecycleOwner var1);

   void onStart(LifecycleOwner var1);

   void onStop(LifecycleOwner var1);
}
