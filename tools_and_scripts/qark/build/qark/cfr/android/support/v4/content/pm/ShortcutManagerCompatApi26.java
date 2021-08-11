/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.content.pm.ShortcutInfo
 *  android.content.pm.ShortcutManager
 */
package android.support.v4.content.pm;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.pm.ShortcutInfoCompat;

@RequiresApi(value=26)
class ShortcutManagerCompatApi26 {
    ShortcutManagerCompatApi26() {
    }

    @Nullable
    public static Intent createShortcutResultIntent(Context context, @NonNull ShortcutInfoCompat shortcutInfoCompat) {
        return ((ShortcutManager)context.getSystemService(ShortcutManager.class)).createShortcutResultIntent(shortcutInfoCompat.toShortcutInfo());
    }

    public static boolean isRequestPinShortcutSupported(Context context) {
        return ((ShortcutManager)context.getSystemService(ShortcutManager.class)).isRequestPinShortcutSupported();
    }

    public static boolean requestPinShortcut(Context context, @NonNull ShortcutInfoCompat shortcutInfoCompat, @Nullable IntentSender intentSender) {
        return ((ShortcutManager)context.getSystemService(ShortcutManager.class)).requestPinShortcut(shortcutInfoCompat.toShortcutInfo(), intentSender);
    }
}

