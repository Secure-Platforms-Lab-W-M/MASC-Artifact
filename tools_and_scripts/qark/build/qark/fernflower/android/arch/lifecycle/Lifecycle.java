package android.arch.lifecycle;

import android.support.annotation.MainThread;

public abstract class Lifecycle {
   @MainThread
   public abstract void addObserver(LifecycleObserver var1);

   @MainThread
   public abstract Lifecycle.State getCurrentState();

   @MainThread
   public abstract void removeObserver(LifecycleObserver var1);

   public static enum Event {
      ON_ANY,
      ON_CREATE,
      ON_DESTROY,
      ON_PAUSE,
      ON_RESUME,
      ON_START,
      ON_STOP;
   }

   public static enum State {
      CREATED,
      DESTROYED,
      INITIALIZED,
      RESUMED,
      STARTED;

      public boolean isAtLeast(Lifecycle.State var1) {
         return this.compareTo(var1) >= 0;
      }
   }
}
