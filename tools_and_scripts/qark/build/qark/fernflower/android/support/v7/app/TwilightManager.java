package android.support.v7.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import java.util.Calendar;

class TwilightManager {
   private static final int SUNRISE = 6;
   private static final int SUNSET = 22;
   private static final String TAG = "TwilightManager";
   private static TwilightManager sInstance;
   private final Context mContext;
   private final LocationManager mLocationManager;
   private final TwilightManager.TwilightState mTwilightState = new TwilightManager.TwilightState();

   @VisibleForTesting
   TwilightManager(@NonNull Context var1, @NonNull LocationManager var2) {
      this.mContext = var1;
      this.mLocationManager = var2;
   }

   static TwilightManager getInstance(@NonNull Context var0) {
      if (sInstance == null) {
         var0 = var0.getApplicationContext();
         sInstance = new TwilightManager(var0, (LocationManager)var0.getSystemService("location"));
      }

      return sInstance;
   }

   private Location getLastKnownLocation() {
      Location var1 = null;
      Location var2 = null;
      if (PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
         var1 = this.getLastKnownLocationForProvider("network");
      }

      if (PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION") == 0) {
         var2 = this.getLastKnownLocationForProvider("gps");
      }

      if (var2 != null && var1 != null) {
         return var2.getTime() > var1.getTime() ? var2 : var1;
      } else {
         return var2 != null ? var2 : var1;
      }
   }

   private Location getLastKnownLocationForProvider(String var1) {
      LocationManager var2 = this.mLocationManager;
      if (var2 != null) {
         try {
            if (var2.isProviderEnabled(var1)) {
               Location var4 = this.mLocationManager.getLastKnownLocation(var1);
               return var4;
            }
         } catch (Exception var3) {
            Log.d("TwilightManager", "Failed to get last known location", var3);
         }
      }

      return null;
   }

   private boolean isStateValid() {
      TwilightManager.TwilightState var1 = this.mTwilightState;
      return var1 != null && var1.nextUpdate > System.currentTimeMillis();
   }

   @VisibleForTesting
   static void setInstance(TwilightManager var0) {
      sInstance = var0;
   }

   private void updateState(@NonNull Location var1) {
      TwilightManager.TwilightState var14 = this.mTwilightState;
      long var3 = System.currentTimeMillis();
      TwilightCalculator var15 = TwilightCalculator.getInstance();
      var15.calculateTwilight(var3 - 86400000L, var1.getLatitude(), var1.getLongitude());
      long var5 = var15.sunset;
      var15.calculateTwilight(var3, var1.getLatitude(), var1.getLongitude());
      int var2 = var15.state;
      boolean var13 = true;
      if (var2 != 1) {
         var13 = false;
      }

      long var7 = var15.sunrise;
      long var9 = var15.sunset;
      var15.calculateTwilight(86400000L + var3, var1.getLatitude(), var1.getLongitude());
      long var11 = var15.sunrise;
      if (var7 != -1L && var9 != -1L) {
         if (var3 > var9) {
            var3 = 0L + var11;
         } else if (var3 > var7) {
            var3 = 0L + var9;
         } else {
            var3 = 0L + var7;
         }

         var3 += 60000L;
      } else {
         var3 += 43200000L;
      }

      var14.isNight = var13;
      var14.yesterdaySunset = var5;
      var14.todaySunrise = var7;
      var14.todaySunset = var9;
      var14.tomorrowSunrise = var11;
      var14.nextUpdate = var3;
   }

   boolean isNight() {
      TwilightManager.TwilightState var2 = this.mTwilightState;
      if (this.isStateValid()) {
         return var2.isNight;
      } else {
         Location var3 = this.getLastKnownLocation();
         if (var3 != null) {
            this.updateState(var3);
            return var2.isNight;
         } else {
            Log.i("TwilightManager", "Could not get last known location. This is probably because the app does not have any location permissions. Falling back to hardcoded sunrise/sunset values.");
            int var1 = Calendar.getInstance().get(11);
            return var1 < 6 || var1 >= 22;
         }
      }
   }

   private static class TwilightState {
      boolean isNight;
      long nextUpdate;
      long todaySunrise;
      long todaySunset;
      long tomorrowSunrise;
      long yesterdaySunset;

      TwilightState() {
      }
   }
}
