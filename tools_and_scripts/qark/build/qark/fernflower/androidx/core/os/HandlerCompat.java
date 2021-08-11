package androidx.core.os;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;

public final class HandlerCompat {
   private static final String TAG = "HandlerCompat";

   private HandlerCompat() {
   }

   public static Handler createAsync(Looper var0) {
      if (VERSION.SDK_INT >= 28) {
         return Handler.createAsync(var0);
      } else {
         if (VERSION.SDK_INT >= 16) {
            try {
               Handler var1 = (Handler)Handler.class.getDeclaredConstructor(Looper.class, Callback.class, Boolean.TYPE).newInstance(var0, null, true);
               return var1;
            } catch (IllegalAccessException var2) {
            } catch (InstantiationException var3) {
            } catch (NoSuchMethodException var4) {
            } catch (InvocationTargetException var5) {
               Throwable var6 = var5.getCause();
               if (!(var6 instanceof RuntimeException)) {
                  if (var6 instanceof Error) {
                     throw (Error)var6;
                  }

                  throw new RuntimeException(var6);
               }

               throw (RuntimeException)var6;
            }

            Log.v("HandlerCompat", "Unable to invoke Handler(Looper, Callback, boolean) constructor");
         }

         return new Handler(var0);
      }
   }

   public static Handler createAsync(Looper var0, Callback var1) {
      if (VERSION.SDK_INT >= 28) {
         return Handler.createAsync(var0, var1);
      } else {
         if (VERSION.SDK_INT >= 16) {
            try {
               Handler var2 = (Handler)Handler.class.getDeclaredConstructor(Looper.class, Callback.class, Boolean.TYPE).newInstance(var0, var1, true);
               return var2;
            } catch (IllegalAccessException var3) {
            } catch (InstantiationException var4) {
            } catch (NoSuchMethodException var5) {
            } catch (InvocationTargetException var6) {
               Throwable var7 = var6.getCause();
               if (!(var7 instanceof RuntimeException)) {
                  if (var7 instanceof Error) {
                     throw (Error)var7;
                  }

                  throw new RuntimeException(var7);
               }

               throw (RuntimeException)var7;
            }

            Log.v("HandlerCompat", "Unable to invoke Handler(Looper, Callback, boolean) constructor");
         }

         return new Handler(var0, var1);
      }
   }

   public static boolean postDelayed(Handler var0, Runnable var1, Object var2, long var3) {
      if (VERSION.SDK_INT >= 28) {
         return var0.postDelayed(var1, var2, var3);
      } else {
         Message var5 = Message.obtain(var0, var1);
         var5.obj = var2;
         return var0.sendMessageDelayed(var5, var3);
      }
   }
}
