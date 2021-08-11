package androidx.lifecycle;

import java.util.concurrent.atomic.AtomicReference;

public abstract class Lifecycle {
   AtomicReference mInternalScopeRef = new AtomicReference();

   public abstract void addObserver(LifecycleObserver var1);

   public abstract Lifecycle.State getCurrentState();

   public abstract void removeObserver(LifecycleObserver var1);

   public static enum Event {
      ON_ANY,
      ON_CREATE,
      ON_DESTROY,
      ON_PAUSE,
      ON_RESUME,
      ON_START,
      ON_STOP;

      static {
         Lifecycle.Event var0 = new Lifecycle.Event("ON_ANY", 6);
         ON_ANY = var0;
      }
   }

   public static enum State {
      CREATED,
      DESTROYED,
      INITIALIZED,
      RESUMED,
      STARTED;

      static {
         Lifecycle.State var0 = new Lifecycle.State("RESUMED", 4);
         RESUMED = var0;
      }

      public boolean isAtLeast(Lifecycle.State var1) {
         return this.compareTo(var1) >= 0;
      }
   }
}
