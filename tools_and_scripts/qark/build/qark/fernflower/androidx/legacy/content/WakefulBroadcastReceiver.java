package androidx.legacy.content;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.util.SparseArray;

@Deprecated
public abstract class WakefulBroadcastReceiver extends BroadcastReceiver {
   private static final String EXTRA_WAKE_LOCK_ID = "androidx.contentpager.content.wakelockid";
   private static int mNextId = 1;
   private static final SparseArray sActiveWakeLocks = new SparseArray();

   public static boolean completeWakefulIntent(Intent var0) {
      int var1 = var0.getIntExtra("androidx.contentpager.content.wakelockid", 0);
      if (var1 == 0) {
         return false;
      } else {
         SparseArray var23 = sActiveWakeLocks;
         synchronized(var23){}

         Throwable var10000;
         boolean var10001;
         label182: {
            WakeLock var2;
            try {
               var2 = (WakeLock)sActiveWakeLocks.get(var1);
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label182;
            }

            if (var2 != null) {
               label176:
               try {
                  var2.release();
                  sActiveWakeLocks.remove(var1);
                  return true;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label176;
               }
            } else {
               label178:
               try {
                  StringBuilder var25 = new StringBuilder();
                  var25.append("No active wake lock id #");
                  var25.append(var1);
                  Log.w("WakefulBroadcastReceiv.", var25.toString());
                  return true;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label178;
               }
            }
         }

         while(true) {
            Throwable var24 = var10000;

            try {
               throw var24;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public static ComponentName startWakefulService(Context var0, Intent var1) {
      SparseArray var4 = sActiveWakeLocks;
      synchronized(var4){}

      Throwable var10000;
      boolean var10001;
      label324: {
         int var2;
         int var3;
         try {
            var2 = mNextId;
            var3 = mNextId + 1;
            mNextId = var3;
         } catch (Throwable var47) {
            var10000 = var47;
            var10001 = false;
            break label324;
         }

         if (var3 <= 0) {
            try {
               mNextId = 1;
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label324;
            }
         }

         ComponentName var51;
         try {
            var1.putExtra("androidx.contentpager.content.wakelockid", var2);
            var51 = var0.startService(var1);
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label324;
         }

         if (var51 == null) {
            label307:
            try {
               return null;
            } catch (Throwable var43) {
               var10000 = var43;
               var10001 = false;
               break label307;
            }
         } else {
            label309:
            try {
               PowerManager var49 = (PowerManager)var0.getSystemService("power");
               StringBuilder var5 = new StringBuilder();
               var5.append("androidx.core:wake:");
               var5.append(var51.flattenToShortString());
               WakeLock var50 = var49.newWakeLock(1, var5.toString());
               var50.setReferenceCounted(false);
               var50.acquire(60000L);
               sActiveWakeLocks.put(var2, var50);
               return var51;
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label309;
            }
         }
      }

      while(true) {
         Throwable var48 = var10000;

         try {
            throw var48;
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            continue;
         }
      }
   }
}
