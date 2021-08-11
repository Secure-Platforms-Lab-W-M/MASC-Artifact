/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 */
package android.support.v7.widget;

import android.graphics.Rect;
import android.support.annotation.RestrictTo;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public interface FitWindowsViewGroup {
    public void setOnFitSystemWindowsListener(OnFitSystemWindowsListener var1);

    public static interface OnFitSystemWindowsListener {
        public void onFitSystemWindows(Rect var1);
    }

}

