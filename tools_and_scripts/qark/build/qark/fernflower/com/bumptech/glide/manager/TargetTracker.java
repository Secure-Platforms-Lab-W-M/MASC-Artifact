package com.bumptech.glide.manager;

import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public final class TargetTracker implements LifecycleListener {
   private final Set targets = Collections.newSetFromMap(new WeakHashMap());

   public void clear() {
      this.targets.clear();
   }

   public List getAll() {
      return Util.getSnapshot(this.targets);
   }

   public void onDestroy() {
      Iterator var1 = Util.getSnapshot(this.targets).iterator();

      while(var1.hasNext()) {
         ((Target)var1.next()).onDestroy();
      }

   }

   public void onStart() {
      Iterator var1 = Util.getSnapshot(this.targets).iterator();

      while(var1.hasNext()) {
         ((Target)var1.next()).onStart();
      }

   }

   public void onStop() {
      Iterator var1 = Util.getSnapshot(this.targets).iterator();

      while(var1.hasNext()) {
         ((Target)var1.next()).onStop();
      }

   }

   public void track(Target var1) {
      this.targets.add(var1);
   }

   public void untrack(Target var1) {
      this.targets.remove(var1);
   }
}
