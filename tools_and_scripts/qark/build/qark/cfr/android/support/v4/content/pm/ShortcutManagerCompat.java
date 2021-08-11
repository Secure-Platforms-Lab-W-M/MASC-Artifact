/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.content.IntentSender$OnFinished
 *  android.content.IntentSender$SendIntentException
 *  android.content.pm.ActivityInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.content.pm.ShortcutInfo
 *  android.content.pm.ShortcutManager
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.text.TextUtils
 */
package android.support.v4.content.pm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.text.TextUtils;
import java.util.Iterator;
import java.util.List;

public class ShortcutManagerCompat {
    @VisibleForTesting
    static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    @VisibleForTesting
    static final String INSTALL_SHORTCUT_PERMISSION = "com.android.launcher.permission.INSTALL_SHORTCUT";

    private ShortcutManagerCompat() {
    }

    @NonNull
    public static Intent createShortcutResultIntent(@NonNull Context context, @NonNull ShortcutInfoCompat shortcutInfoCompat) {
        Intent intent = null;
        context = Build.VERSION.SDK_INT >= 26 ? ((ShortcutManager)context.getSystemService(ShortcutManager.class)).createShortcutResultIntent(shortcutInfoCompat.toShortcutInfo()) : intent;
        if (context == null) {
            context = new Intent();
        }
        return shortcutInfoCompat.addToIntent((Intent)context);
    }

    public static boolean isRequestPinShortcutSupported(@NonNull Context object) {
        if (Build.VERSION.SDK_INT >= 26) {
            return ((ShortcutManager)object.getSystemService(ShortcutManager.class)).isRequestPinShortcutSupported();
        }
        if (ContextCompat.checkSelfPermission((Context)object, "com.android.launcher.permission.INSTALL_SHORTCUT") != 0) {
            return false;
        }
        object = object.getPackageManager().queryBroadcastReceivers(new Intent("com.android.launcher.action.INSTALL_SHORTCUT"), 0).iterator();
        while (object.hasNext()) {
            String string2 = ((ResolveInfo)object.next()).activityInfo.permission;
            if (!TextUtils.isEmpty((CharSequence)string2) && !"com.android.launcher.permission.INSTALL_SHORTCUT".equals(string2)) continue;
            return true;
        }
        return false;
    }

    public static boolean requestPinShortcut(@NonNull Context context, @NonNull ShortcutInfoCompat shortcutInfoCompat, final @Nullable IntentSender intentSender) {
        if (Build.VERSION.SDK_INT >= 26) {
            return ((ShortcutManager)context.getSystemService(ShortcutManager.class)).requestPinShortcut(shortcutInfoCompat.toShortcutInfo(), intentSender);
        }
        if (!ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            return false;
        }
        shortcutInfoCompat = shortcutInfoCompat.addToIntent(new Intent("com.android.launcher.action.INSTALL_SHORTCUT"));
        if (intentSender == null) {
            context.sendBroadcast((Intent)shortcutInfoCompat);
            return true;
        }
        context.sendOrderedBroadcast((Intent)shortcutInfoCompat, null, new BroadcastReceiver(){

            public void onReceive(Context context, Intent intent) {
                try {
                    intentSender.sendIntent(context, 0, null, null, null);
                    return;
                }
                catch (IntentSender.SendIntentException sendIntentException) {
                    return;
                }
            }
        }, null, -1, null, null);
        return true;
    }

}

