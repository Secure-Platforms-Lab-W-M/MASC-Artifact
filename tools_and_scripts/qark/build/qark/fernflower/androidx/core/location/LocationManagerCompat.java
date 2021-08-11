package androidx.core.location;

import android.location.LocationManager;
import android.os.Build.VERSION;

public final class LocationManagerCompat {
   private LocationManagerCompat() {
   }

   public static boolean isLocationEnabled(LocationManager var0) {
      if (VERSION.SDK_INT >= 28) {
         return var0.isLocationEnabled();
      } else {
         return var0.isProviderEnabled("network") || var0.isProviderEnabled("gps");
      }
   }
}
