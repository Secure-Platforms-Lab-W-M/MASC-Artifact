package androidx.core.net;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ConnectivityManagerCompat {
   public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
   public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
   public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;

   private ConnectivityManagerCompat() {
   }

   public static NetworkInfo getNetworkInfoFromBroadcast(ConnectivityManager var0, Intent var1) {
      NetworkInfo var2 = (NetworkInfo)var1.getParcelableExtra("networkInfo");
      return var2 != null ? var0.getNetworkInfo(var2.getType()) : null;
   }

   public static int getRestrictBackgroundStatus(ConnectivityManager var0) {
      return VERSION.SDK_INT >= 24 ? var0.getRestrictBackgroundStatus() : 3;
   }

   public static boolean isActiveNetworkMetered(ConnectivityManager var0) {
      if (VERSION.SDK_INT >= 16) {
         return var0.isActiveNetworkMetered();
      } else {
         NetworkInfo var2 = var0.getActiveNetworkInfo();
         if (var2 == null) {
            return true;
         } else {
            int var1 = var2.getType();
            return var1 != 1 && var1 != 7 && var1 != 9;
         }
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface RestrictBackgroundStatus {
   }
}
