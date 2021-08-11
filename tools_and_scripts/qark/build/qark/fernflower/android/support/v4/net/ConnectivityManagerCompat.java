package android.support.v4.net;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.annotation.RestrictTo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ConnectivityManagerCompat {
   private static final ConnectivityManagerCompat.ConnectivityManagerCompatImpl IMPL;
   public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
   public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
   public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;

   static {
      if (VERSION.SDK_INT >= 24) {
         IMPL = new ConnectivityManagerCompat.ConnectivityManagerCompatApi24Impl();
      } else if (VERSION.SDK_INT >= 16) {
         IMPL = new ConnectivityManagerCompat.ConnectivityManagerCompatApi16Impl();
      } else {
         IMPL = new ConnectivityManagerCompat.ConnectivityManagerCompatBaseImpl();
      }
   }

   private ConnectivityManagerCompat() {
   }

   public static NetworkInfo getNetworkInfoFromBroadcast(ConnectivityManager var0, Intent var1) {
      NetworkInfo var2 = (NetworkInfo)var1.getParcelableExtra("networkInfo");
      return var2 != null ? var0.getNetworkInfo(var2.getType()) : null;
   }

   public static int getRestrictBackgroundStatus(ConnectivityManager var0) {
      return IMPL.getRestrictBackgroundStatus(var0);
   }

   @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
   public static boolean isActiveNetworkMetered(ConnectivityManager var0) {
      return IMPL.isActiveNetworkMetered(var0);
   }

   @RequiresApi(16)
   static class ConnectivityManagerCompatApi16Impl extends ConnectivityManagerCompat.ConnectivityManagerCompatBaseImpl {
      public boolean isActiveNetworkMetered(ConnectivityManager var1) {
         return var1.isActiveNetworkMetered();
      }
   }

   @RequiresApi(24)
   static class ConnectivityManagerCompatApi24Impl extends ConnectivityManagerCompat.ConnectivityManagerCompatApi16Impl {
      public int getRestrictBackgroundStatus(ConnectivityManager var1) {
         return var1.getRestrictBackgroundStatus();
      }
   }

   static class ConnectivityManagerCompatBaseImpl implements ConnectivityManagerCompat.ConnectivityManagerCompatImpl {
      public int getRestrictBackgroundStatus(ConnectivityManager var1) {
         return 3;
      }

      public boolean isActiveNetworkMetered(ConnectivityManager var1) {
         NetworkInfo var2 = var1.getActiveNetworkInfo();
         if (var2 == null) {
            return true;
         } else {
            switch(var2.getType()) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
               return true;
            case 1:
            case 7:
            case 9:
               return false;
            case 8:
            default:
               return true;
            }
         }
      }
   }

   interface ConnectivityManagerCompatImpl {
      int getRestrictBackgroundStatus(ConnectivityManager var1);

      boolean isActiveNetworkMetered(ConnectivityManager var1);
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface RestrictBackgroundStatus {
   }
}
