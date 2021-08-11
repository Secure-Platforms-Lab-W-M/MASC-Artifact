package com.bumptech.glide.manager;

public interface ConnectivityMonitor extends LifecycleListener {
   public interface ConnectivityListener {
      void onConnectivityChanged(boolean var1);
   }
}
