package androidx.core.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.util.Log;

public final class NavUtils {
   public static final String PARENT_ACTIVITY = "android.support.PARENT_ACTIVITY";
   private static final String TAG = "NavUtils";

   private NavUtils() {
   }

   public static Intent getParentActivityIntent(Activity var0) {
      if (VERSION.SDK_INT >= 16) {
         Intent var1 = var0.getParentActivityIntent();
         if (var1 != null) {
            return var1;
         }
      }

      String var6 = getParentActivityName(var0);
      if (var6 == null) {
         return null;
      } else {
         ComponentName var2 = new ComponentName(var0, var6);

         try {
            if (getParentActivityName(var0, var2) == null) {
               return Intent.makeMainActivity(var2);
            } else {
               Intent var5 = (new Intent()).setComponent(var2);
               return var5;
            }
         } catch (NameNotFoundException var3) {
            StringBuilder var4 = new StringBuilder();
            var4.append("getParentActivityIntent: bad parentActivityName '");
            var4.append(var6);
            var4.append("' in manifest");
            Log.e("NavUtils", var4.toString());
            return null;
         }
      }
   }

   public static Intent getParentActivityIntent(Context var0, ComponentName var1) throws NameNotFoundException {
      String var2 = getParentActivityName(var0, var1);
      if (var2 == null) {
         return null;
      } else {
         var1 = new ComponentName(var1.getPackageName(), var2);
         return getParentActivityName(var0, var1) == null ? Intent.makeMainActivity(var1) : (new Intent()).setComponent(var1);
      }
   }

   public static Intent getParentActivityIntent(Context var0, Class var1) throws NameNotFoundException {
      String var2 = getParentActivityName(var0, new ComponentName(var0, var1));
      if (var2 == null) {
         return null;
      } else {
         ComponentName var3 = new ComponentName(var0, var2);
         return getParentActivityName(var0, var3) == null ? Intent.makeMainActivity(var3) : (new Intent()).setComponent(var3);
      }
   }

   public static String getParentActivityName(Activity var0) {
      try {
         String var2 = getParentActivityName(var0, var0.getComponentName());
         return var2;
      } catch (NameNotFoundException var1) {
         throw new IllegalArgumentException(var1);
      }
   }

   public static String getParentActivityName(Context var0, ComponentName var1) throws NameNotFoundException {
      PackageManager var3 = var0.getPackageManager();
      int var2;
      if (VERSION.SDK_INT >= 24) {
         var2 = 128 | 512;
      } else {
         var2 = 128 | 512;
      }

      ActivityInfo var4 = var3.getActivityInfo(var1, var2);
      String var7;
      if (VERSION.SDK_INT >= 16) {
         var7 = var4.parentActivityName;
         if (var7 != null) {
            return var7;
         }
      }

      if (var4.metaData == null) {
         return null;
      } else {
         var7 = var4.metaData.getString("android.support.PARENT_ACTIVITY");
         if (var7 == null) {
            return null;
         } else {
            String var5 = var7;
            if (var7.charAt(0) == '.') {
               StringBuilder var6 = new StringBuilder();
               var6.append(var0.getPackageName());
               var6.append(var7);
               var5 = var6.toString();
            }

            return var5;
         }
      }
   }

   public static void navigateUpFromSameTask(Activity var0) {
      Intent var1 = getParentActivityIntent(var0);
      if (var1 != null) {
         navigateUpTo(var0, var1);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Activity ");
         var2.append(var0.getClass().getSimpleName());
         var2.append(" does not have a parent activity name specified. (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data>  element in your manifest?)");
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public static void navigateUpTo(Activity var0, Intent var1) {
      if (VERSION.SDK_INT >= 16) {
         var0.navigateUpTo(var1);
      } else {
         var1.addFlags(67108864);
         var0.startActivity(var1);
         var0.finish();
      }
   }

   public static boolean shouldUpRecreateTask(Activity var0, Intent var1) {
      if (VERSION.SDK_INT >= 16) {
         return var0.shouldUpRecreateTask(var1);
      } else {
         String var2 = var0.getIntent().getAction();
         return var2 != null && !var2.equals("android.intent.action.MAIN");
      }
   }
}
