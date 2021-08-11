/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.util.AttributeSet
 *  android.util.TypedValue
 *  com.google.android.material.R
 *  com.google.android.material.R$attr
 *  com.google.android.material.R$styleable
 */
package com.google.android.material.internal;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.TintTypedArray;
import com.google.android.material.R;

public final class ThemeEnforcement {
    private static final int[] ANDROID_THEME_OVERLAY_ATTRS;
    private static final int[] APPCOMPAT_CHECK_ATTRS;
    private static final String APPCOMPAT_THEME_NAME = "Theme.AppCompat";
    private static final int[] MATERIAL_CHECK_ATTRS;
    private static final String MATERIAL_THEME_NAME = "Theme.MaterialComponents";
    private static final int[] MATERIAL_THEME_OVERLAY_ATTR;

    static {
        APPCOMPAT_CHECK_ATTRS = new int[]{R.attr.colorPrimary};
        MATERIAL_CHECK_ATTRS = new int[]{R.attr.colorPrimaryVariant};
        ANDROID_THEME_OVERLAY_ATTRS = new int[]{16842752, R.attr.theme};
        MATERIAL_THEME_OVERLAY_ATTR = new int[]{R.attr.materialThemeOverlay};
    }

    private ThemeEnforcement() {
    }

    public static void checkAppCompatTheme(Context context) {
        ThemeEnforcement.checkTheme(context, APPCOMPAT_CHECK_ATTRS, "Theme.AppCompat");
    }

    private static void checkCompatibleTheme(Context context, AttributeSet attributeSet, int n, int n2) {
        attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.ThemeEnforcement, n, n2);
        boolean bl = attributeSet.getBoolean(R.styleable.ThemeEnforcement_enforceMaterialTheme, false);
        attributeSet.recycle();
        if (bl) {
            attributeSet = new TypedValue();
            if (!context.getTheme().resolveAttribute(R.attr.isMaterialTheme, (TypedValue)attributeSet, true) || attributeSet.type == 18 && attributeSet.data == 0) {
                ThemeEnforcement.checkMaterialTheme(context);
            }
        }
        ThemeEnforcement.checkAppCompatTheme(context);
    }

    public static void checkMaterialTheme(Context context) {
        ThemeEnforcement.checkTheme(context, MATERIAL_CHECK_ATTRS, "Theme.MaterialComponents");
    }

    private static /* varargs */ void checkTextAppearance(Context context, AttributeSet attributeSet, int[] arrn, int n, int n2, int ... arrn2) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ThemeEnforcement, n, n2);
        int n3 = R.styleable.ThemeEnforcement_enforceTextAppearance;
        boolean bl = false;
        if (!typedArray.getBoolean(n3, false)) {
            typedArray.recycle();
            return;
        }
        if (arrn2 != null && arrn2.length != 0) {
            bl = ThemeEnforcement.isCustomTextAppearanceValid(context, attributeSet, arrn, n, n2, arrn2);
        } else if (typedArray.getResourceId(R.styleable.ThemeEnforcement_android_textAppearance, -1) != -1) {
            bl = true;
        }
        typedArray.recycle();
        if (bl) {
            return;
        }
        throw new IllegalArgumentException("This component requires that you specify a valid TextAppearance attribute. Update your app theme to inherit from Theme.MaterialComponents (or a descendant).");
    }

    private static void checkTheme(Context object, int[] arrn, String string2) {
        if (ThemeEnforcement.isTheme((Context)object, arrn)) {
            return;
        }
        object = new StringBuilder();
        object.append("The style on this component requires your app theme to be ");
        object.append(string2);
        object.append(" (or a descendant).");
        throw new IllegalArgumentException(object.toString());
    }

    public static Context createThemedContext(Context object, AttributeSet attributeSet, int n, int n2) {
        Object object2;
        block4 : {
            block5 : {
                n = ThemeEnforcement.obtainMaterialThemeOverlayId((Context)object, attributeSet, n, n2);
                object2 = object;
                if (n == 0) break block4;
                if (!(object instanceof ContextThemeWrapper)) break block5;
                object2 = object;
                if (((ContextThemeWrapper)((Object)object)).getThemeResId() == n) break block4;
            }
            object = new ContextThemeWrapper((Context)object, n);
            n = ThemeEnforcement.obtainAndroidThemeOverlayId((Context)object, attributeSet);
            object2 = object;
            if (n != 0) {
                object2 = new ContextThemeWrapper((Context)object, n);
            }
        }
        return object2;
    }

    public static boolean isAppCompatTheme(Context context) {
        return ThemeEnforcement.isTheme(context, APPCOMPAT_CHECK_ATTRS);
    }

    private static /* varargs */ boolean isCustomTextAppearanceValid(Context context, AttributeSet attributeSet, int[] arrn, int n, int n2, int ... arrn2) {
        context = context.obtainStyledAttributes(attributeSet, arrn, n, n2);
        n2 = arrn2.length;
        for (n = 0; n < n2; ++n) {
            if (context.getResourceId(arrn2[n], -1) != -1) continue;
            context.recycle();
            return false;
        }
        context.recycle();
        return true;
    }

    public static boolean isMaterialTheme(Context context) {
        return ThemeEnforcement.isTheme(context, MATERIAL_CHECK_ATTRS);
    }

    private static boolean isTheme(Context context, int[] arrn) {
        context = context.obtainStyledAttributes(arrn);
        for (int i = 0; i < arrn.length; ++i) {
            if (context.hasValue(i)) continue;
            context.recycle();
            return false;
        }
        context.recycle();
        return true;
    }

    private static int obtainAndroidThemeOverlayId(Context context, AttributeSet attributeSet) {
        context = context.obtainStyledAttributes(attributeSet, ANDROID_THEME_OVERLAY_ATTRS);
        int n = context.getResourceId(0, 0);
        int n2 = context.getResourceId(1, 0);
        context.recycle();
        if (n != 0) {
            return n;
        }
        return n2;
    }

    private static int obtainMaterialThemeOverlayId(Context context, AttributeSet attributeSet, int n, int n2) {
        context = context.obtainStyledAttributes(attributeSet, MATERIAL_THEME_OVERLAY_ATTR, n, n2);
        n = context.getResourceId(0, 0);
        context.recycle();
        return n;
    }

    public static /* varargs */ TypedArray obtainStyledAttributes(Context context, AttributeSet attributeSet, int[] arrn, int n, int n2, int ... arrn2) {
        ThemeEnforcement.checkCompatibleTheme(context, attributeSet, n, n2);
        ThemeEnforcement.checkTextAppearance(context, attributeSet, arrn, n, n2, arrn2);
        return context.obtainStyledAttributes(attributeSet, arrn, n, n2);
    }

    public static /* varargs */ TintTypedArray obtainTintedStyledAttributes(Context context, AttributeSet attributeSet, int[] arrn, int n, int n2, int ... arrn2) {
        ThemeEnforcement.checkCompatibleTheme(context, attributeSet, n, n2);
        ThemeEnforcement.checkTextAppearance(context, attributeSet, arrn, n, n2, arrn2);
        return TintTypedArray.obtainStyledAttributes(context, attributeSet, arrn, n, n2);
    }
}

