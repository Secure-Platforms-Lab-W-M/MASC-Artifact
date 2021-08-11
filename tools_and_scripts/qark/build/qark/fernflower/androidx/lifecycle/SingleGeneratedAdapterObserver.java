package androidx.lifecycle;

class SingleGeneratedAdapterObserver implements LifecycleEventObserver {
   private final GeneratedAdapter mGeneratedAdapter;

   SingleGeneratedAdapterObserver(GeneratedAdapter var1) {
      this.mGeneratedAdapter = var1;
   }

   public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
      this.mGeneratedAdapter.callMethods(var1, var2, false, (MethodCallsLogger)null);
      this.mGeneratedAdapter.callMethods(var1, var2, true, (MethodCallsLogger)null);
   }
}
