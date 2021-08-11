// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.graphics.PorterDuff$Mode;
import android.support.annotation.Nullable;
import android.content.res.ColorStateList;

public interface TintableCompoundButton
{
    @Nullable
    ColorStateList getSupportButtonTintList();
    
    @Nullable
    PorterDuff$Mode getSupportButtonTintMode();
    
    void setSupportButtonTintList(@Nullable final ColorStateList p0);
    
    void setSupportButtonTintMode(@Nullable final PorterDuff$Mode p0);
}
