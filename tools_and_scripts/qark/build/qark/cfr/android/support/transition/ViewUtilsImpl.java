/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.view.View
 */
package android.support.transition;

import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.ViewOverlayImpl;
import android.support.transition.WindowIdImpl;
import android.view.View;

@RequiresApi(value=14)
interface ViewUtilsImpl {
    public void clearNonTransitionAlpha(@NonNull View var1);

    public ViewOverlayImpl getOverlay(@NonNull View var1);

    public float getTransitionAlpha(@NonNull View var1);

    public WindowIdImpl getWindowId(@NonNull View var1);

    public void saveNonTransitionAlpha(@NonNull View var1);

    public void setAnimationMatrix(@NonNull View var1, Matrix var2);

    public void setLeftTopRightBottom(View var1, int var2, int var3, int var4, int var5);

    public void setTransitionAlpha(@NonNull View var1, float var2);

    public void transformMatrixToGlobal(@NonNull View var1, @NonNull Matrix var2);

    public void transformMatrixToLocal(@NonNull View var1, @NonNull Matrix var2);
}

