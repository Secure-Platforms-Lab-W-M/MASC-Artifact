package androidx.lifecycle;

class FullLifecycleObserverAdapter implements LifecycleEventObserver {
   private final FullLifecycleObserver mFullLifecycleObserver;
   private final LifecycleEventObserver mLifecycleEventObserver;

   FullLifecycleObserverAdapter(FullLifecycleObserver var1, LifecycleEventObserver var2) {
      this.mFullLifecycleObserver = var1;
      this.mLifecycleEventObserver = var2;
   }

   public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
      switch(null.$SwitchMap$androidx$lifecycle$Lifecycle$Event[var2.ordinal()]) {
      case 1:
         this.mFullLifecycleObserver.onCreate(var1);
         break;
      case 2:
         this.mFullLifecycleObserver.onStart(var1);
         break;
      case 3:
         this.mFullLifecycleObserver.onResume(var1);
         break;
      case 4:
         this.mFullLifecycleObserver.onPause(var1);
         break;
      case 5:
         this.mFullLifecycleObserver.onStop(var1);
         break;
      case 6:
         this.mFullLifecycleObserver.onDestroy(var1);
         break;
      case 7:
         throw new IllegalArgumentException("ON_ANY must not been send by anybody");
      }

      LifecycleEventObserver var3 = this.mLifecycleEventObserver;
      if (var3 != null) {
         var3.onStateChanged(var1, var2);
      }

   }
}
