/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 */
package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;

public interface TintableCompoundDrawablesView {
    public ColorStateList getSupportCompoundDrawablesTintList();

    public PorterDuff.Mode getSupportCompoundDrawablesTintMode();

    public void setSupportCompoundDrawablesTintList(ColorStateList var1);

    public void setSupportCompoundDrawablesTintMode(PorterDuff.Mode var1);
}

