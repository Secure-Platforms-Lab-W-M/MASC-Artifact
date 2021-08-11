package android.support.v4.content;

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
   private static final String EXTRA_WAKE_LOCK_ID = "android.support.content.wakelockid";
   private static int mNextId = 1;
   private static final SparseArray sActiveWakeLocks = new SparseArray();

   public static boolean completeWakefulIntent(Intent var0) {
      int var1 = var0.getIntExtra("android.support.content.wakelockid", 0);
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
      SparseArray var3 = sActiveWakeLocks;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label260: {
         int var2;
         try {
            var2 = mNextId++;
            if (mNextId <= 0) {
               mNextId = 1;
            }
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label260;
         }

         ComponentName var38;
         try {
            var1.putExtra("android.support.content.wakelockid", var2);
            var38 = var0.startService(var1);
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label260;
         }

         if (var38 == null) {
            label247:
            try {
               return null;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label247;
            }
         } else {
            label249:
            try {
               PowerManager var36 = (PowerManager)var0.getSystemService("power");
               StringBuilder var4 = new StringBuilder();
               var4.append("wake:");
               var4.append(var38.flattenToShortString());
               WakeLock var37 = var36.newWakeLock(1, var4.toString());
               var37.setReferenceCounted(false);
               var37.acquire(60000L);
               sActiveWakeLocks.put(var2, var37);
               return var38;
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label249;
            }
         }
      }

      while(true) {
         Throwable var35 = var10000;

         try {
            throw var35;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }
}
