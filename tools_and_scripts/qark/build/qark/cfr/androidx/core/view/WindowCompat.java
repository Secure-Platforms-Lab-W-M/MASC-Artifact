/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.Window
 */
package androidx.core.view;

import android.os.Build;
import android.view.View;
import android.view.Window;

public final class WindowCompat {
    public static final int FEATURE_ACTION_BAR = 8;
    public static final int FEATURE_ACTION_BAR_OVERLAY = 9;
    public static final int FEATURE_ACTION_MODE_OVERLAY = 10;

    private WindowCompat() {
    }

    public static <T extends View> T requireViewById(Window window, int n) {
        if (Build.VERSION.SDK_INT >= 28) {
            return (T)window.requireViewById(n);
        }
        if ((window = window.findViewById(n)) != null) {
            return (T)window;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this Window");
    }
}

