package com.bumptech.glide.signature;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import com.bumptech.glide.load.Key;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ApplicationVersionSignature {
   private static final ConcurrentMap PACKAGE_NAME_TO_KEY = new ConcurrentHashMap();
   private static final String TAG = "AppVersionSignature";

   private ApplicationVersionSignature() {
   }

   private static PackageInfo getPackageInfo(Context var0) {
      try {
         PackageInfo var1 = var0.getPackageManager().getPackageInfo(var0.getPackageName(), 0);
         return var1;
      } catch (NameNotFoundException var3) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Cannot resolve info for");
         var2.append(var0.getPackageName());
         Log.e("AppVersionSignature", var2.toString(), var3);
         return null;
      }
   }

   private static String getVersionCode(PackageInfo var0) {
      return var0 != null ? String.valueOf(var0.versionCode) : UUID.randomUUID().toString();
   }

   public static Key obtain(Context var0) {
      String var3 = var0.getPackageName();
      Key var2 = (Key)PACKAGE_NAME_TO_KEY.get(var3);
      Key var1 = var2;
      if (var2 == null) {
         Key var4 = obtainVersionSignature(var0);
         var2 = (Key)PACKAGE_NAME_TO_KEY.putIfAbsent(var3, var4);
         var1 = var2;
         if (var2 == null) {
            var1 = var4;
         }
      }

      return var1;
   }

   private static Key obtainVersionSignature(Context var0) {
      return new ObjectKey(getVersionCode(getPackageInfo(var0)));
   }

   static void reset() {
      PACKAGE_NAME_TO_KEY.clear();
   }
}
