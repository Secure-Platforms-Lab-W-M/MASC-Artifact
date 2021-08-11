/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 */
package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;

public interface TintableCompoundButton {
    @Nullable
    public ColorStateList getSupportButtonTintList();

    @Nullable
    public PorterDuff.Mode getSupportButtonTintMode();

    public void setSupportButtonTintList(@Nullable ColorStateList var1);

    public void setSupportButtonTintMode(@Nullable PorterDuff.Mode var1);
}

