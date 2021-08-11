package android.support.v4.app;

import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class BundleCompatDonut {
   private static final String TAG = "BundleCompatDonut";
   private static Method sGetIBinderMethod;
   private static boolean sGetIBinderMethodFetched;
   private static Method sPutIBinderMethod;
   private static boolean sPutIBinderMethodFetched;

   public static IBinder getBinder(Bundle var0, String var1) {
      if (!sGetIBinderMethodFetched) {
         try {
            sGetIBinderMethod = Bundle.class.getMethod("getIBinder", String.class);
            sGetIBinderMethod.setAccessible(true);
         } catch (NoSuchMethodException var3) {
            Log.i("BundleCompatDonut", "Failed to retrieve getIBinder method", var3);
         }

         sGetIBinderMethodFetched = true;
      }

      if (sGetIBinderMethod != null) {
         try {
            IBinder var5 = (IBinder)sGetIBinderMethod.invoke(var0, var1);
            return var5;
         } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException var4) {
            Log.i("BundleCompatDonut", "Failed to invoke getIBinder via reflection", var4);
            sGetIBinderMethod = null;
         }
      }

      return null;
   }

   public static void putBinder(Bundle var0, String var1, IBinder var2) {
      if (!sPutIBinderMethodFetched) {
         try {
            sPutIBinderMethod = Bundle.class.getMethod("putIBinder", String.class, IBinder.class);
            sPutIBinderMethod.setAccessible(true);
         } catch (NoSuchMethodException var4) {
            Log.i("BundleCompatDonut", "Failed to retrieve putIBinder method", var4);
         }

         sPutIBinderMethodFetched = true;
      }

      if (sPutIBinderMethod != null) {
         try {
            sPutIBinderMethod.invoke(var0, var1, var2);
            return;
         } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException var5) {
            Log.i("BundleCompatDonut", "Failed to invoke putIBinder via reflection", var5);
            sPutIBinderMethod = null;
         }
      }

   }
}
