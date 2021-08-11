// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content.pm;

import android.content.IntentSender;
import android.support.annotation.Nullable;
import android.content.pm.ShortcutManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(26)
class ShortcutManagerCompatApi26
{
    @Nullable
    public static Intent createShortcutResultIntent(final Context context, @NonNull final ShortcutInfoCompat shortcutInfoCompat) {
        return ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).createShortcutResultIntent(shortcutInfoCompat.toShortcutInfo());
    }
    
    public static boolean isRequestPinShortcutSupported(final Context context) {
        return ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).isRequestPinShortcutSupported();
    }
    
    public static boolean requestPinShortcut(final Context context, @NonNull final ShortcutInfoCompat shortcutInfoCompat, @Nullable final IntentSender intentSender) {
        return ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).requestPinShortcut(shortcutInfoCompat.toShortcutInfo(), intentSender);
    }
}
