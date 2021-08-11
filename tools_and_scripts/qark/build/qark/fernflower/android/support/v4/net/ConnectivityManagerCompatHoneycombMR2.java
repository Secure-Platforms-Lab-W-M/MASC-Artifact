package android.support.v4.net;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresApi;

@TargetApi(13)
@RequiresApi(13)
class ConnectivityManagerCompatHoneycombMR2 {
   public static boolean isActiveNetworkMetered(ConnectivityManager var0) {
      NetworkInfo var1 = var0.getActiveNetworkInfo();
      if (var1 != null) {
         switch(var1.getType()) {
         case 0:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
            break;
         case 1:
         case 7:
         case 9:
            return false;
         case 8:
         default:
            return true;
         }
      }

      return true;
   }
}
