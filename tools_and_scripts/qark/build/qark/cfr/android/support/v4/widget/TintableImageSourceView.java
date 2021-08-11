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
import android.support.annotation.RestrictTo;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public interface TintableImageSourceView {
    @Nullable
    public ColorStateList getSupportImageTintList();

    @Nullable
    public PorterDuff.Mode getSupportImageTintMode();

    public void setSupportImageTintList(@Nullable ColorStateList var1);

    public void setSupportImageTintMode(@Nullable PorterDuff.Mode var1);
}

