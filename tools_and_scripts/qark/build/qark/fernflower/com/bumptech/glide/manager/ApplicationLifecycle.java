package com.bumptech.glide.manager;

class ApplicationLifecycle implements Lifecycle {
   public void addListener(LifecycleListener var1) {
      var1.onStart();
   }

   public void removeListener(LifecycleListener var1) {
   }
}
