package com.bumptech.glide.manager;

import com.bumptech.glide.util.Util;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

class ActivityFragmentLifecycle implements Lifecycle {
   private boolean isDestroyed;
   private boolean isStarted;
   private final Set lifecycleListeners = Collections.newSetFromMap(new WeakHashMap());

   public void addListener(LifecycleListener var1) {
      this.lifecycleListeners.add(var1);
      if (this.isDestroyed) {
         var1.onDestroy();
      } else if (this.isStarted) {
         var1.onStart();
      } else {
         var1.onStop();
      }
   }

   void onDestroy() {
      this.isDestroyed = true;
      Iterator var1 = Util.getSnapshot(this.lifecycleListeners).iterator();

      while(var1.hasNext()) {
         ((LifecycleListener)var1.next()).onDestroy();
      }

   }

   void onStart() {
      this.isStarted = true;
      Iterator var1 = Util.getSnapshot(this.lifecycleListeners).iterator();

      while(var1.hasNext()) {
         ((LifecycleListener)var1.next()).onStart();
      }

   }

   void onStop() {
      this.isStarted = false;
      Iterator var1 = Util.getSnapshot(this.lifecycleListeners).iterator();

      while(var1.hasNext()) {
         ((LifecycleListener)var1.next()).onStop();
      }

   }

   public void removeListener(LifecycleListener var1) {
      this.lifecycleListeners.remove(var1);
   }
}
