package com.bumptech.glide.manager;

import android.content.Context;
import android.util.Log;
import androidx.core.content.ContextCompat;

public class DefaultConnectivityMonitorFactory implements ConnectivityMonitorFactory {
   private static final String NETWORK_PERMISSION = "android.permission.ACCESS_NETWORK_STATE";
   private static final String TAG = "ConnectivityMonitor";

   public ConnectivityMonitor build(Context var1, ConnectivityMonitor.ConnectivityListener var2) {
      boolean var3;
      if (ContextCompat.checkSelfPermission(var1, "android.permission.ACCESS_NETWORK_STATE") == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (Log.isLoggable("ConnectivityMonitor", 3)) {
         String var4;
         if (var3) {
            var4 = "ACCESS_NETWORK_STATE permission granted, registering connectivity monitor";
         } else {
            var4 = "ACCESS_NETWORK_STATE permission missing, cannot register connectivity monitor";
         }

         Log.d("ConnectivityMonitor", var4);
      }

      return (ConnectivityMonitor)(var3 ? new DefaultConnectivityMonitor(var1, var2) : new NullConnectivityMonitor());
   }
}
