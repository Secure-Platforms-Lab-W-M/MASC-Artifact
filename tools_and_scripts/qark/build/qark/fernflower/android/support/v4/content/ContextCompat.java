package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Process;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import java.io.File;

public class ContextCompat {
   private static final String TAG = "ContextCompat";
   private static final Object sLock = new Object();
   private static TypedValue sTempValue;

   protected ContextCompat() {
   }

   private static File buildPath(File var0, String... var1) {
      int var3 = var1.length;
      int var2 = 0;

      File var4;
      for(var4 = var0; var2 < var3; var4 = var0) {
         String var5 = var1[var2];
         if (var4 == null) {
            var0 = new File(var5);
         } else {
            var0 = var4;
            if (var5 != null) {
               var0 = new File(var4, var5);
            }
         }

         ++var2;
      }

      return var4;
   }

   public static int checkSelfPermission(@NonNull Context var0, @NonNull String var1) {
      if (var1 != null) {
         return var0.checkPermission(var1, Process.myPid(), Process.myUid());
      } else {
         throw new IllegalArgumentException("permission is null");
      }
   }

   public static Context createDeviceProtectedStorageContext(Context var0) {
      return VERSION.SDK_INT >= 24 ? var0.createDeviceProtectedStorageContext() : null;
   }

   private static File createFilesDir(File var0) {
      synchronized(ContextCompat.class){}

      label103: {
         Throwable var10000;
         label107: {
            boolean var1;
            boolean var10001;
            try {
               if (var0.exists() || var0.mkdirs()) {
                  break label103;
               }

               var1 = var0.exists();
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label107;
            }

            if (var1) {
               return var0;
            }

            try {
               StringBuilder var2 = new StringBuilder();
               var2.append("Unable to create files subdir ");
               var2.append(var0.getPath());
               Log.w("ContextCompat", var2.toString());
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label107;
            }

            return null;
         }

         Throwable var9 = var10000;
         throw var9;
      }

      return var0;
   }

   public static File getCodeCacheDir(Context var0) {
      return VERSION.SDK_INT >= 21 ? var0.getCodeCacheDir() : createFilesDir(new File(var0.getApplicationInfo().dataDir, "code_cache"));
   }

   @ColorInt
   public static final int getColor(Context var0, @ColorRes int var1) {
      return VERSION.SDK_INT >= 23 ? var0.getColor(var1) : var0.getResources().getColor(var1);
   }

   public static final ColorStateList getColorStateList(Context var0, @ColorRes int var1) {
      return VERSION.SDK_INT >= 23 ? var0.getColorStateList(var1) : var0.getResources().getColorStateList(var1);
   }

   public static File getDataDir(Context var0) {
      if (VERSION.SDK_INT >= 24) {
         return var0.getDataDir();
      } else {
         String var1 = var0.getApplicationInfo().dataDir;
         return var1 != null ? new File(var1) : null;
      }
   }

   public static final Drawable getDrawable(Context param0, @DrawableRes int param1) {
      // $FF: Couldn't be decompiled
   }

   public static File[] getExternalCacheDirs(Context var0) {
      return VERSION.SDK_INT >= 19 ? var0.getExternalCacheDirs() : new File[]{var0.getExternalCacheDir()};
   }

   public static File[] getExternalFilesDirs(Context var0, String var1) {
      return VERSION.SDK_INT >= 19 ? var0.getExternalFilesDirs(var1) : new File[]{var0.getExternalFilesDir(var1)};
   }

   public static final File getNoBackupFilesDir(Context var0) {
      return VERSION.SDK_INT >= 21 ? var0.getNoBackupFilesDir() : createFilesDir(new File(var0.getApplicationInfo().dataDir, "no_backup"));
   }

   public static File[] getObbDirs(Context var0) {
      return VERSION.SDK_INT >= 19 ? var0.getObbDirs() : new File[]{var0.getObbDir()};
   }

   public static boolean isDeviceProtectedStorage(Context var0) {
      return VERSION.SDK_INT >= 24 ? var0.isDeviceProtectedStorage() : false;
   }

   public static boolean startActivities(Context var0, Intent[] var1) {
      return startActivities(var0, var1, (Bundle)null);
   }

   public static boolean startActivities(Context var0, Intent[] var1, Bundle var2) {
      if (VERSION.SDK_INT >= 16) {
         var0.startActivities(var1, var2);
      } else {
         var0.startActivities(var1);
      }

      return true;
   }

   public static void startActivity(Context var0, Intent var1, @Nullable Bundle var2) {
      if (VERSION.SDK_INT >= 16) {
         var0.startActivity(var1, var2);
      } else {
         var0.startActivity(var1);
      }
   }

   public static void startForegroundService(Context var0, Intent var1) {
      if (VERSION.SDK_INT >= 26) {
         var0.startForegroundService(var1);
      } else {
         var0.startService(var1);
      }
   }
}
