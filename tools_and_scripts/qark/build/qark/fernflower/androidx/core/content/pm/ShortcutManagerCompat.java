package androidx.core.content.pm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.OnFinished;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build.VERSION;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShortcutManagerCompat {
   static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
   public static final String EXTRA_SHORTCUT_ID = "android.intent.extra.shortcut.ID";
   static final String INSTALL_SHORTCUT_PERMISSION = "com.android.launcher.permission.INSTALL_SHORTCUT";
   private static volatile ShortcutInfoCompatSaver sShortcutInfoCompatSaver = null;

   private ShortcutManagerCompat() {
   }

   public static boolean addDynamicShortcuts(Context var0, List var1) {
      if (VERSION.SDK_INT >= 25) {
         ArrayList var2 = new ArrayList();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            var2.add(((ShortcutInfoCompat)var3.next()).toShortcutInfo());
         }

         if (!((ShortcutManager)var0.getSystemService(ShortcutManager.class)).addDynamicShortcuts(var2)) {
            return false;
         }
      }

      getShortcutInfoSaverInstance(var0).addShortcuts(var1);
      return true;
   }

   public static Intent createShortcutResultIntent(Context var0, ShortcutInfoCompat var1) {
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

   public static List getDynamicShortcuts(Context var0) {
      if (VERSION.SDK_INT < 25) {
         try {
            List var4 = getShortcutInfoSaverInstance(var0).getShortcuts();
            return var4;
         } catch (Exception var3) {
            return new ArrayList();
         }
      } else {
         List var2 = ((ShortcutManager)var0.getSystemService(ShortcutManager.class)).getDynamicShortcuts();
         ArrayList var1 = new ArrayList(var2.size());
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            var1.add((new ShortcutInfoCompat.Builder(var0, (ShortcutInfo)var5.next())).build());
         }

         return var1;
      }
   }

   public static int getMaxShortcutCountPerActivity(Context var0) {
      return VERSION.SDK_INT >= 25 ? ((ShortcutManager)var0.getSystemService(ShortcutManager.class)).getMaxShortcutCountPerActivity() : 0;
   }

   private static ShortcutInfoCompatSaver getShortcutInfoSaverInstance(Context var0) {
      if (sShortcutInfoCompatSaver == null) {
         if (VERSION.SDK_INT >= 23) {
            try {
               sShortcutInfoCompatSaver = (ShortcutInfoCompatSaver)Class.forName("androidx.sharetarget.ShortcutInfoCompatSaverImpl", false, ShortcutManagerCompat.class.getClassLoader()).getMethod("getInstance", Context.class).invoke((Object)null, var0);
            } catch (Exception var1) {
            }
         }

         if (sShortcutInfoCompatSaver == null) {
            sShortcutInfoCompatSaver = new ShortcutInfoCompatSaver.NoopImpl();
         }
      }

      return sShortcutInfoCompatSaver;
   }

   public static boolean isRequestPinShortcutSupported(Context var0) {
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

   public static void removeAllDynamicShortcuts(Context var0) {
      if (VERSION.SDK_INT >= 25) {
         ((ShortcutManager)var0.getSystemService(ShortcutManager.class)).removeAllDynamicShortcuts();
      }

      getShortcutInfoSaverInstance(var0).removeAllShortcuts();
   }

   public static boolean requestPinShortcut(Context var0, ShortcutInfoCompat var1, final IntentSender var2) {
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

   public static boolean updateShortcuts(Context var0, List var1) {
      if (VERSION.SDK_INT >= 25) {
         ArrayList var2 = new ArrayList();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            var2.add(((ShortcutInfoCompat)var3.next()).toShortcutInfo());
         }

         if (!((ShortcutManager)var0.getSystemService(ShortcutManager.class)).updateShortcuts(var2)) {
            return false;
         }
      }

      getShortcutInfoSaverInstance(var0).addShortcuts(var1);
      return true;
   }

   public void removeDynamicShortcuts(Context var1, List var2) {
      if (VERSION.SDK_INT >= 25) {
         ((ShortcutManager)var1.getSystemService(ShortcutManager.class)).removeDynamicShortcuts(var2);
      }

      getShortcutInfoSaverInstance(var1).removeShortcuts(var2);
   }
}
