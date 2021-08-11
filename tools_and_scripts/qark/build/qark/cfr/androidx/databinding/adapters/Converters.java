/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.drawable.ColorDrawable
 */
package androidx.databinding.adapters;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;

public class Converters {
    public static ColorStateList convertColorToColorStateList(int n) {
        return ColorStateList.valueOf((int)n);
    }

    public static ColorDrawable convertColorToDrawable(int n) {
        return new ColorDrawable(n);
    }
}

