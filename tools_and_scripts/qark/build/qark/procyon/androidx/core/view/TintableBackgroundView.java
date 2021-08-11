// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.view;

import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;

public interface TintableBackgroundView
{
    ColorStateList getSupportBackgroundTintList();
    
    PorterDuff$Mode getSupportBackgroundTintMode();
    
    void setSupportBackgroundTintList(final ColorStateList p0);
    
    void setSupportBackgroundTintMode(final PorterDuff$Mode p0);
}
