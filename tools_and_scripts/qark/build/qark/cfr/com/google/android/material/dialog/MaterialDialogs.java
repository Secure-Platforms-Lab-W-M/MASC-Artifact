/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.InsetDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  com.google.android.material.R
 *  com.google.android.material.R$dimen
 *  com.google.android.material.R$styleable
 */
package com.google.android.material.dialog;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;

public class MaterialDialogs {
    private MaterialDialogs() {
    }

    public static Rect getDialogBackgroundInsets(Context context, int n, int n2) {
        TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(context, null, R.styleable.MaterialAlertDialog, n, n2, new int[0]);
        n = typedArray.getDimensionPixelSize(R.styleable.MaterialAlertDialog_backgroundInsetStart, context.getResources().getDimensionPixelSize(R.dimen.mtrl_alert_dialog_background_inset_start));
        int n3 = typedArray.getDimensionPixelSize(R.styleable.MaterialAlertDialog_backgroundInsetTop, context.getResources().getDimensionPixelSize(R.dimen.mtrl_alert_dialog_background_inset_top));
        int n4 = typedArray.getDimensionPixelSize(R.styleable.MaterialAlertDialog_backgroundInsetEnd, context.getResources().getDimensionPixelSize(R.dimen.mtrl_alert_dialog_background_inset_end));
        int n5 = typedArray.getDimensionPixelSize(R.styleable.MaterialAlertDialog_backgroundInsetBottom, context.getResources().getDimensionPixelSize(R.dimen.mtrl_alert_dialog_background_inset_bottom));
        typedArray.recycle();
        int n6 = n;
        int n7 = n4;
        int n8 = n6;
        n2 = n7;
        if (Build.VERSION.SDK_INT >= 17) {
            n8 = n6;
            n2 = n7;
            if (context.getResources().getConfiguration().getLayoutDirection() == 1) {
                n2 = n;
                n8 = n4;
            }
        }
        return new Rect(n8, n3, n2, n5);
    }

    public static InsetDrawable insetDrawable(Drawable drawable2, Rect rect) {
        return new InsetDrawable(drawable2, rect.left, rect.top, rect.right, rect.bottom);
    }
}

