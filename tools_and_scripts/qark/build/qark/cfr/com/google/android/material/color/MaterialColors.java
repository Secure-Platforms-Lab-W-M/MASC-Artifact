/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Color
 *  android.util.TypedValue
 *  android.view.View
 */
package com.google.android.material.color;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.resources.MaterialAttributes;

public class MaterialColors {
    public static final float ALPHA_DISABLED = 0.38f;
    public static final float ALPHA_DISABLED_LOW = 0.12f;
    public static final float ALPHA_FULL = 1.0f;
    public static final float ALPHA_LOW = 0.32f;
    public static final float ALPHA_MEDIUM = 0.54f;

    public static int getColor(Context context, int n, int n2) {
        if ((context = MaterialAttributes.resolve(context, n)) != null) {
            return context.data;
        }
        return n2;
    }

    public static int getColor(Context context, int n, String string2) {
        return MaterialAttributes.resolveOrThrow(context, n, string2);
    }

    public static int getColor(View view, int n) {
        return MaterialAttributes.resolveOrThrow(view, n);
    }

    public static int getColor(View view, int n, int n2) {
        return MaterialColors.getColor(view.getContext(), n, n2);
    }

    public static int layer(int n, int n2) {
        return ColorUtils.compositeColors(n2, n);
    }

    public static int layer(int n, int n2, float f) {
        return MaterialColors.layer(n, ColorUtils.setAlphaComponent(n2, Math.round((float)Color.alpha((int)n2) * f)));
    }

    public static int layer(View view, int n, int n2) {
        return MaterialColors.layer(view, n, n2, 1.0f);
    }

    public static int layer(View view, int n, int n2, float f) {
        return MaterialColors.layer(MaterialColors.getColor(view, n), MaterialColors.getColor(view, n2), f);
    }
}

