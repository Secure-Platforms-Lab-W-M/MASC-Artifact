package com.bumptech.glide.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.bumptech.glide.util.Preconditions;

final class DefaultConnectivityMonitor implements ConnectivityMonitor {
   private static final String TAG = "ConnectivityMonitor";
   private final BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
      public void onReceive(Context var1, Intent var2) {
         boolean var3 = DefaultConnectivityMonitor.this.isConnected;
         DefaultConnectivityMonitor var5 = DefaultConnectivityMonitor.this;
         var5.isConnected = var5.isConnected(var1);
         if (var3 != DefaultConnectivityMonitor.this.isConnected) {
            if (Log.isLoggable("ConnectivityMonitor", 3)) {
               StringBuilder var4 = new StringBuilder();
               var4.append("connectivity changed, isConnected: ");
               var4.append(DefaultConnectivityMonitor.this.isConnected);
               Log.d("ConnectivityMonitor", var4.toString());
            }

            DefaultConnectivityMonitor.this.listener.onConnectivityChanged(DefaultConnectivityMonitor.this.isConnected);
         }

      }
   };
   private final Context context;
   boolean isConnected;
   private boolean isRegistered;
   final ConnectivityMonitor.ConnectivityListener listener;

   DefaultConnectivityMonitor(Context var1, ConnectivityMonitor.ConnectivityListener var2) {
      this.context = var1.getApplicationContext();
      this.listener = var2;
   }

   private void register() {
      if (!this.isRegistered) {
         this.isConnected = this.isConnected(this.context);

         try {
            this.context.registerReceiver(this.connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.isRegistered = true;
         } catch (SecurityException var2) {
            if (Log.isLoggable("ConnectivityMonitor", 5)) {
               Log.w("ConnectivityMonitor", "Failed to register", var2);
            }

         }
      }
   }

   private void unregister() {
      if (this.isRegistered) {
         this.context.unregisterReceiver(this.connectivityReceiver);
         this.isRegistered = false;
      }
   }

   boolean isConnected(Context var1) {
      ConnectivityManager var3 = (ConnectivityManager)Preconditions.checkNotNull((ConnectivityManager)var1.getSystemService("connectivity"));

      NetworkInfo var4;
      try {
         var4 = var3.getActiveNetworkInfo();
      } catch (RuntimeException var2) {
         if (Log.isLoggable("ConnectivityMonitor", 5)) {
            Log.w("ConnectivityMonitor", "Failed to determine connectivity status when connectivity changed", var2);
         }

         return true;
      }

      return var4 != null && var4.isConnected();
   }

   public void onDestroy() {
   }

   public void onStart() {
      this.register();
   }

   public void onStop() {
      this.unregister();
   }
}
