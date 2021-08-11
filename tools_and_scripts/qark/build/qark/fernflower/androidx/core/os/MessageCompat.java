package androidx.core.os;

import android.os.Message;
import android.os.Build.VERSION;

public final class MessageCompat {
   private static boolean sTryIsAsynchronous = true;
   private static boolean sTrySetAsynchronous = true;

   private MessageCompat() {
   }

   public static boolean isAsynchronous(Message var0) {
      if (VERSION.SDK_INT >= 22) {
         return var0.isAsynchronous();
      } else {
         if (sTryIsAsynchronous && VERSION.SDK_INT >= 16) {
            try {
               boolean var1 = var0.isAsynchronous();
               return var1;
            } catch (NoSuchMethodError var2) {
               sTryIsAsynchronous = false;
            }
         }

         return false;
      }
   }

   public static void setAsynchronous(Message var0, boolean var1) {
      if (VERSION.SDK_INT >= 22) {
         var0.setAsynchronous(var1);
      } else {
         if (sTrySetAsynchronous && VERSION.SDK_INT >= 16) {
            try {
               var0.setAsynchronous(var1);
               return;
            } catch (NoSuchMethodError var2) {
               sTrySetAsynchronous = false;
            }
         }

      }
   }
}
