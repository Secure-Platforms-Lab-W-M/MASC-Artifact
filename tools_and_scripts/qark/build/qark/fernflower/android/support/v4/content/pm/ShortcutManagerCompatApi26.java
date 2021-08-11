package android.support.v4.content.pm;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ShortcutManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

@RequiresApi(26)
class ShortcutManagerCompatApi26 {
   @Nullable
   public static Intent createShortcutResultIntent(Context var0, @NonNull ShortcutInfoCompat var1) {
      return ((ShortcutManager)var0.getSystemService(ShortcutManager.class)).createShortcutResultIntent(var1.toShortcutInfo());
   }

   public static boolean isRequestPinShortcutSupported(Context var0) {
      return ((ShortcutManager)var0.getSystemService(ShortcutManager.class)).isRequestPinShortcutSupported();
   }

   public static boolean requestPinShortcut(Context var0, @NonNull ShortcutInfoCompat var1, @Nullable IntentSender var2) {
      return ((ShortcutManager)var0.getSystemService(ShortcutManager.class)).requestPinShortcut(var1.toShortcutInfo(), var2);
   }
}
