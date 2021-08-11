/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 *  android.view.View
 *  com.google.android.material.R
 *  com.google.android.material.R$attr
 *  com.google.android.material.R$dimen
 */
package com.google.android.material.resources;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import com.google.android.material.R;

public class MaterialAttributes {
    public static TypedValue resolve(Context context, int n) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(n, typedValue, true)) {
            return typedValue;
        }
        return null;
    }

    public static boolean resolveBoolean(Context context, int n, boolean bl) {
        if ((context = MaterialAttributes.resolve(context, n)) != null && context.type == 18) {
            if (context.data != 0) {
                return true;
            }
            return false;
        }
        return bl;
    }

    public static boolean resolveBooleanOrThrow(Context context, int n, String string2) {
        if (MaterialAttributes.resolveOrThrow(context, n, string2) != 0) {
            return true;
        }
        return false;
    }

    public static int resolveDimension(Context context, int n, int n2) {
        TypedValue typedValue = MaterialAttributes.resolve(context, n);
        if (typedValue != null && typedValue.type == 5) {
            return (int)typedValue.getDimension(context.getResources().getDisplayMetrics());
        }
        return (int)context.getResources().getDimension(n2);
    }

    public static int resolveMinimumAccessibleTouchTarget(Context context) {
        return MaterialAttributes.resolveDimension(context, R.attr.minTouchTargetSize, R.dimen.mtrl_min_touch_target_size);
    }

    public static int resolveOrThrow(Context context, int n, String string2) {
        TypedValue typedValue = MaterialAttributes.resolve(context, n);
        if (typedValue != null) {
            return typedValue.data;
        }
        throw new IllegalArgumentException(String.format("%1$s requires a value for the %2$s attribute to be set in your app theme. You can either set the attribute in your theme or update your theme to inherit from Theme.MaterialComponents (or a descendant).", string2, context.getResources().getResourceName(n)));
    }

    public static int resolveOrThrow(View view, int n) {
        return MaterialAttributes.resolveOrThrow(view.getContext(), n, view.getClass().getCanonicalName());
    }
}

