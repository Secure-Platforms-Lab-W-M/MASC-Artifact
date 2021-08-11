/*
 * Decompiled with CFR 0_124.
 */
package android.support.v4.widget;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public interface AutoSizeableTextView {
    public int getAutoSizeMaxTextSize();

    public int getAutoSizeMinTextSize();

    public int getAutoSizeStepGranularity();

    public int[] getAutoSizeTextAvailableSizes();

    public int getAutoSizeTextType();

    public void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException;

    public void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] var1, int var2) throws IllegalArgumentException;

    public void setAutoSizeTextTypeWithDefaults(int var1);
}

