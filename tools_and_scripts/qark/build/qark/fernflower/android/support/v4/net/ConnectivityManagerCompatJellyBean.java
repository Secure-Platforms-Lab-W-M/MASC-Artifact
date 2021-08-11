package android.support.v4.net;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.support.annotation.RequiresApi;

@TargetApi(16)
@RequiresApi(16)
class ConnectivityManagerCompatJellyBean {
   public static boolean isActiveNetworkMetered(ConnectivityManager var0) {
      return var0.isActiveNetworkMetered();
   }
}
