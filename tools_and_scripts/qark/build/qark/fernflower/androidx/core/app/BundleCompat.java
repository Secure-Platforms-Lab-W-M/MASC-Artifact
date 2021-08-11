package androidx.core.app;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Build.VERSION;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BundleCompat {
   private BundleCompat() {
   }

   public static IBinder getBinder(Bundle var0, String var1) {
      return VERSION.SDK_INT >= 18 ? var0.getBinder(var1) : BundleCompat.BundleCompatBaseImpl.getBinder(var0, var1);
   }

   public static void putBinder(Bundle var0, String var1, IBinder var2) {
      if (VERSION.SDK_INT >= 18) {
         var0.putBinder(var1, var2);
      } else {
         BundleCompat.BundleCompatBaseImpl.putBinder(var0, var1, var2);
      }
   }

   static class BundleCompatBaseImpl {
      private static final String TAG = "BundleCompatBaseImpl";
      private static Method sGetIBinderMethod;
      private static boolean sGetIBinderMethodFetched;
      private static Method sPutIBinderMethod;
      private static boolean sPutIBinderMethodFetched;

      private BundleCompatBaseImpl() {
      }

      public static IBinder getBinder(Bundle var0, String var1) {
         Method var2;
         if (!sGetIBinderMethodFetched) {
            try {
               var2 = Bundle.class.getMethod("getIBinder", String.class);
               sGetIBinderMethod = var2;
               var2.setAccessible(true);
            } catch (NoSuchMethodException var6) {
               Log.i("BundleCompatBaseImpl", "Failed to retrieve getIBinder method", var6);
            }

            sGetIBinderMethodFetched = true;
         }

         var2 = sGetIBinderMethod;
         if (var2 != null) {
            Object var7;
            try {
               IBinder var8 = (IBinder)var2.invoke(var0, var1);
               return var8;
            } catch (InvocationTargetException var3) {
               var7 = var3;
            } catch (IllegalAccessException var4) {
               var7 = var4;
            } catch (IllegalArgumentException var5) {
               var7 = var5;
            }

            Log.i("BundleCompatBaseImpl", "Failed to invoke getIBinder via reflection", (Throwable)var7);
            sGetIBinderMethod = null;
         }

         return null;
      }

      public static void putBinder(Bundle var0, String var1, IBinder var2) {
         Method var3;
         if (!sPutIBinderMethodFetched) {
            try {
               var3 = Bundle.class.getMethod("putIBinder", String.class, IBinder.class);
               sPutIBinderMethod = var3;
               var3.setAccessible(true);
            } catch (NoSuchMethodException var7) {
               Log.i("BundleCompatBaseImpl", "Failed to retrieve putIBinder method", var7);
            }

            sPutIBinderMethodFetched = true;
         }

         var3 = sPutIBinderMethod;
         if (var3 != null) {
            Object var8;
            try {
               var3.invoke(var0, var1, var2);
               return;
            } catch (InvocationTargetException var4) {
               var8 = var4;
            } catch (IllegalAccessException var5) {
               var8 = var5;
            } catch (IllegalArgumentException var6) {
               var8 = var6;
            }

            Log.i("BundleCompatBaseImpl", "Failed to invoke putIBinder via reflection", (Throwable)var8);
            sPutIBinderMethod = null;
         }

      }
   }
}
