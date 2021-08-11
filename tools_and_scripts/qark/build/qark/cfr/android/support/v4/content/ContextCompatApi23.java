/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 */
package android.support.v4.content;

import android.content.Context;
import android.content.res.ColorStateList;

class ContextCompatApi23 {
    ContextCompatApi23() {
    }

    public static int getColor(Context context, int n) {
        return context.getColor(n);
    }

    public static ColorStateList getColorStateList(Context context, int n) {
        return context.getColorStateList(n);
    }
}

