/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.os.IBinder
 *  android.view.View
 *  android.view.ViewParent
 */
package android.support.v4.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.IBinder;
import android.support.v4.view.TintableBackgroundView;
import android.view.View;
import android.view.ViewParent;
import java.lang.reflect.Field;

class ViewCompatBase {
    private static final String TAG = "ViewCompatBase";
    private static Field sMinHeightField;
    private static boolean sMinHeightFieldFetched;
    private static Field sMinWidthField;
    private static boolean sMinWidthFieldFetched;

    ViewCompatBase() {
    }

    static ColorStateList getBackgroundTintList(View view) {
        if (view instanceof TintableBackgroundView) {
            return ((TintableBackgroundView)view).getSupportBackgroundTintList();
        }
        return null;
    }

    static PorterDuff.Mode getBackgroundTintMode(View view) {
        if (view instanceof TintableBackgroundView) {
            return ((TintableBackgroundView)view).getSupportBackgroundTintMode();
        }
        return null;
    }

    static int getMinimumHeight(View view) {
        if (!sMinHeightFieldFetched) {
            try {
                sMinHeightField = View.class.getDeclaredField("mMinHeight");
                sMinHeightField.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                // empty catch block
            }
            sMinHeightFieldFetched = true;
        }
        if (sMinHeightField != null) {
            try {
                int n = (Integer)sMinHeightField.get((Object)view);
                return n;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return 0;
    }

    static int getMinimumWidth(View view) {
        if (!sMinWidthFieldFetched) {
            try {
                sMinWidthField = View.class.getDeclaredField("mMinWidth");
                sMinWidthField.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                // empty catch block
            }
            sMinWidthFieldFetched = true;
        }
        if (sMinWidthField != null) {
            try {
                int n = (Integer)sMinWidthField.get((Object)view);
                return n;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return 0;
    }

    static boolean isAttachedToWindow(View view) {
        if (view.getWindowToken() != null) {
            return true;
        }
        return false;
    }

    static boolean isLaidOut(View view) {
        if (view.getWidth() > 0 && view.getHeight() > 0) {
            return true;
        }
        return false;
    }

    static void offsetLeftAndRight(View view, int n) {
        int n2 = view.getLeft();
        view.offsetLeftAndRight(n);
        if (n != 0) {
            ViewParent viewParent = view.getParent();
            if (viewParent instanceof View) {
                n = Math.abs(n);
                ((View)viewParent).invalidate(n2 - n, view.getTop(), view.getWidth() + n2 + n, view.getBottom());
                return;
            }
            view.invalidate();
        }
    }

    static void offsetTopAndBottom(View view, int n) {
        int n2 = view.getTop();
        view.offsetTopAndBottom(n);
        if (n != 0) {
            ViewParent viewParent = view.getParent();
            if (viewParent instanceof View) {
                n = Math.abs(n);
                ((View)viewParent).invalidate(view.getLeft(), n2 - n, view.getRight(), view.getHeight() + n2 + n);
                return;
            }
            view.invalidate();
        }
    }

    static void setBackgroundTintList(View view, ColorStateList colorStateList) {
        if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView)view).setSupportBackgroundTintList(colorStateList);
        }
    }

    static void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
        if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView)view).setSupportBackgroundTintMode(mode);
        }
    }
}

