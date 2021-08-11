package android.support.v4.content.pm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.OnFinished;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import java.util.Iterator;

public class ShortcutManagerCompat {
   @VisibleForTesting
   static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
   @VisibleForTesting
   static final String INSTALL_SHORTCUT_PERMISSION = "com.android.launcher.permission.INSTALL_SHORTCUT";

   private ShortcutManagerCompat() {
   }

   @NonNull
   public static Intent createShortcutResultIntent(@NonNull Context var0, @NonNull ShortcutInfoCompat var1) {
      Intent var2 = null;
      if (VERSION.SDK_INT >= 26) {
         var2 = ((ShortcutManager)var0.getSystemService(ShortcutManager.class)).createShortcutResultIntent(var1.toShortcutInfo());
      }

      Intent var3 = var2;
      if (var2 == null) {
         var3 = new Intent();
      }

      return var1.addToIntent(var3);
   }

   public static boolean isRequestPinShortcutSupported(@NonNull Context var0) {
      if (VERSION.SDK_INT >= 26) {
         return ((ShortcutManager)var0.getSystemService(ShortcutManager.class)).isRequestPinShortcutSupported();
      } else if (ContextCompat.checkSelfPermission(var0, "com.android.launcher.permission.INSTALL_SHORTCUT") != 0) {
         return false;
      } else {
         Iterator var2 = var0.getPackageManager().queryBroadcastReceivers(new Intent("com.android.launcher.action.INSTALL_SHORTCUT"), 0).iterator();

         String var1;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var1 = ((ResolveInfo)var2.next()).activityInfo.permission;
         } while(!TextUtils.isEmpty(var1) && !"com.android.launcher.permission.INSTALL_SHORTCUT".equals(var1));

         return true;
      }
   }

   public static boolean requestPinShortcut(@NonNull Context var0, @NonNull ShortcutInfoCompat var1, @Nullable final IntentSender var2) {
      if (VERSION.SDK_INT >= 26) {
         return ((ShortcutManager)var0.getSystemService(ShortcutManager.class)).requestPinShortcut(var1.toShortcutInfo(), var2);
      } else if (!isRequestPinShortcutSupported(var0)) {
         return false;
      } else {
         Intent var3 = var1.addToIntent(new Intent("com.android.launcher.action.INSTALL_SHORTCUT"));
         if (var2 == null) {
            var0.sendBroadcast(var3);
            return true;
         } else {
            var0.sendOrderedBroadcast(var3, (String)null, new BroadcastReceiver() {
               public void onReceive(Context var1, Intent var2x) {
                  try {
                     var2.sendIntent(var1, 0, (Intent)null, (OnFinished)null, (Handler)null);
                  } catch (SendIntentException var3) {
                  }
               }
            }, (Handler)null, -1, (String)null, (Bundle)null);
            return true;
         }
      }
   }
}
