package androidx.lifecycle;

class ReflectiveGenericLifecycleObserver implements LifecycleEventObserver {
   private final ClassesInfoCache.CallbackInfo mInfo;
   private final Object mWrapped;

   ReflectiveGenericLifecycleObserver(Object var1) {
      this.mWrapped = var1;
      this.mInfo = ClassesInfoCache.sInstance.getInfo(this.mWrapped.getClass());
   }

   public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
      this.mInfo.invokeCallbacks(var1, var2, this.mWrapped);
   }
}
