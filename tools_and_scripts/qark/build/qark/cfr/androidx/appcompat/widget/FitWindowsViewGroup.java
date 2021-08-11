/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 */
package androidx.appcompat.widget;

import android.graphics.Rect;

public interface FitWindowsViewGroup {
    public void setOnFitSystemWindowsListener(OnFitSystemWindowsListener var1);

    public static interface OnFitSystemWindowsListener {
        public void onFitSystemWindows(Rect var1);
    }

}

