package androidx.fragment.app;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

class FragmentViewLifecycleOwner implements LifecycleOwner {
   private LifecycleRegistry mLifecycleRegistry = null;

   public Lifecycle getLifecycle() {
      this.initialize();
      return this.mLifecycleRegistry;
   }

   void handleLifecycleEvent(Lifecycle.Event var1) {
      this.mLifecycleRegistry.handleLifecycleEvent(var1);
   }

   void initialize() {
      if (this.mLifecycleRegistry == null) {
         this.mLifecycleRegistry = new LifecycleRegistry(this);
      }

   }

   boolean isInitialized() {
      return this.mLifecycleRegistry != null;
   }
}
