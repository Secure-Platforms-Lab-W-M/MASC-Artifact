/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.WindowInsets
 */
package android.support.v4.view;

import android.graphics.Rect;
import android.os.Build;
import android.view.WindowInsets;

public class WindowInsetsCompat {
    private final Object mInsets;

    public WindowInsetsCompat(WindowInsetsCompat windowInsetsCompat) {
        int n = Build.VERSION.SDK_INT;
        Object var3_3 = null;
        if (n >= 20) {
            windowInsetsCompat = windowInsetsCompat == null ? var3_3 : new WindowInsets((WindowInsets)windowInsetsCompat.mInsets);
            this.mInsets = windowInsetsCompat;
            return;
        }
        this.mInsets = null;
    }

    private WindowInsetsCompat(Object object) {
        this.mInsets = object;
    }

    static Object unwrap(WindowInsetsCompat windowInsetsCompat) {
        if (windowInsetsCompat == null) {
            return null;
        }
        return windowInsetsCompat.mInsets;
    }

    static WindowInsetsCompat wrap(Object object) {
        if (object == null) {
            return null;
        }
        return new WindowInsetsCompat(object);
    }

    public WindowInsetsCompat consumeStableInsets() {
        if (Build.VERSION.SDK_INT >= 21) {
            return new WindowInsetsCompat((Object)((WindowInsets)this.mInsets).consumeStableInsets());
        }
        return null;
    }

    public WindowInsetsCompat consumeSystemWindowInsets() {
        if (Build.VERSION.SDK_INT >= 20) {
            return new WindowInsetsCompat((Object)((WindowInsets)this.mInsets).consumeSystemWindowInsets());
        }
        return null;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null) {
            if (this.getClass() != object.getClass()) {
                return false;
            }
            object = (WindowInsetsCompat)object;
            Object object2 = this.mInsets;
            if (object2 == null) {
                if (object.mInsets == null) {
                    return true;
                }
                return false;
            }
            return object2.equals(object.mInsets);
        }
        return false;
    }

    public int getStableInsetBottom() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((WindowInsets)this.mInsets).getStableInsetBottom();
        }
        return 0;
    }

    public int getStableInsetLeft() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((WindowInsets)this.mInsets).getStableInsetLeft();
        }
        return 0;
    }

    public int getStableInsetRight() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((WindowInsets)this.mInsets).getStableInsetRight();
        }
        return 0;
    }

    public int getStableInsetTop() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((WindowInsets)this.mInsets).getStableInsetTop();
        }
        return 0;
    }

    public int getSystemWindowInsetBottom() {
        if (Build.VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).getSystemWindowInsetBottom();
        }
        return 0;
    }

    public int getSystemWindowInsetLeft() {
        if (Build.VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).getSystemWindowInsetLeft();
        }
        return 0;
    }

    public int getSystemWindowInsetRight() {
        if (Build.VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).getSystemWindowInsetRight();
        }
        return 0;
    }

    public int getSystemWindowInsetTop() {
        if (Build.VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).getSystemWindowInsetTop();
        }
        return 0;
    }

    public boolean hasInsets() {
        if (Build.VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).hasInsets();
        }
        return false;
    }

    public boolean hasStableInsets() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((WindowInsets)this.mInsets).hasStableInsets();
        }
        return false;
    }

    public boolean hasSystemWindowInsets() {
        if (Build.VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).hasSystemWindowInsets();
        }
        return false;
    }

    public int hashCode() {
        Object object = this.mInsets;
        if (object == null) {
            return 0;
        }
        return object.hashCode();
    }

    public boolean isConsumed() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((WindowInsets)this.mInsets).isConsumed();
        }
        return false;
    }

    public boolean isRound() {
        if (Build.VERSION.SDK_INT >= 20) {
            return ((WindowInsets)this.mInsets).isRound();
        }
        return false;
    }

    public WindowInsetsCompat replaceSystemWindowInsets(int n, int n2, int n3, int n4) {
        if (Build.VERSION.SDK_INT >= 20) {
            return new WindowInsetsCompat((Object)((WindowInsets)this.mInsets).replaceSystemWindowInsets(n, n2, n3, n4));
        }
        return null;
    }

    public WindowInsetsCompat replaceSystemWindowInsets(Rect rect) {
        if (Build.VERSION.SDK_INT >= 21) {
            return new WindowInsetsCompat((Object)((WindowInsets)this.mInsets).replaceSystemWindowInsets(rect));
        }
        return null;
    }
}

