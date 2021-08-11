/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.TypedValue
 */
package com.google.android.material.resources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.TintTypedArray;
import com.google.android.material.resources.TextAppearance;

public class MaterialResources {
    private MaterialResources() {
    }

    public static ColorStateList getColorStateList(Context context, TypedArray typedArray, int n) {
        int n2;
        if (typedArray.hasValue(n) && (n2 = typedArray.getResourceId(n, 0)) != 0 && (context = AppCompatResources.getColorStateList(context, n2)) != null) {
            return context;
        }
        if (Build.VERSION.SDK_INT <= 15 && (n2 = typedArray.getColor(n, -1)) != -1) {
            return ColorStateList.valueOf((int)n2);
        }
        return typedArray.getColorStateList(n);
    }

    public static ColorStateList getColorStateList(Context context, TintTypedArray tintTypedArray, int n) {
        int n2;
        if (tintTypedArray.hasValue(n) && (n2 = tintTypedArray.getResourceId(n, 0)) != 0 && (context = AppCompatResources.getColorStateList(context, n2)) != null) {
            return context;
        }
        if (Build.VERSION.SDK_INT <= 15 && (n2 = tintTypedArray.getColor(n, -1)) != -1) {
            return ColorStateList.valueOf((int)n2);
        }
        return tintTypedArray.getColorStateList(n);
    }

    public static int getDimensionPixelSize(Context context, TypedArray typedArray, int n, int n2) {
        TypedValue typedValue = new TypedValue();
        if (typedArray.getValue(n, typedValue) && typedValue.type == 2) {
            context = context.getTheme().obtainStyledAttributes(new int[]{typedValue.data});
            n = context.getDimensionPixelSize(0, n2);
            context.recycle();
            return n;
        }
        return typedArray.getDimensionPixelSize(n, n2);
    }

    public static Drawable getDrawable(Context context, TypedArray typedArray, int n) {
        int n2;
        if (typedArray.hasValue(n) && (n2 = typedArray.getResourceId(n, 0)) != 0 && (context = AppCompatResources.getDrawable(context, n2)) != null) {
            return context;
        }
        return typedArray.getDrawable(n);
    }

    static int getIndexWithValue(TypedArray typedArray, int n, int n2) {
        if (typedArray.hasValue(n)) {
            return n;
        }
        return n2;
    }

    public static TextAppearance getTextAppearance(Context context, TypedArray typedArray, int n) {
        if (typedArray.hasValue(n) && (n = typedArray.getResourceId(n, 0)) != 0) {
            return new TextAppearance(context, n);
        }
        return null;
    }
}

