package androidx.lifecycle;

class CompositeGeneratedAdaptersObserver implements LifecycleEventObserver {
   private final GeneratedAdapter[] mGeneratedAdapters;

   CompositeGeneratedAdaptersObserver(GeneratedAdapter[] var1) {
      this.mGeneratedAdapters = var1;
   }

   public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
      MethodCallsLogger var6 = new MethodCallsLogger();
      GeneratedAdapter[] var7 = this.mGeneratedAdapters;
      int var5 = var7.length;
      byte var4 = 0;

      int var3;
      for(var3 = 0; var3 < var5; ++var3) {
         var7[var3].callMethods(var1, var2, false, var6);
      }

      var7 = this.mGeneratedAdapters;
      var5 = var7.length;

      for(var3 = var4; var3 < var5; ++var3) {
         var7[var3].callMethods(var1, var2, true, var6);
      }

   }
}
