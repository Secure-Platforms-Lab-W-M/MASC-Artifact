package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class ConnectivityManagerCompatGingerbread {
   public static boolean isActiveNetworkMetered(ConnectivityManager var0) {
      NetworkInfo var1 = var0.getActiveNetworkInfo();
      if (var1 == null) {
         return true;
      } else {
         switch(var1.getType()) {
         case 0:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
            return true;
         case 1:
            return false;
         default:
            return true;
         }
      }
   }
}
