// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.cardview.widget;

import android.view.View;
import android.graphics.drawable.Drawable;

interface CardViewDelegate
{
    Drawable getCardBackground();
    
    View getCardView();
    
    boolean getPreventCornerOverlap();
    
    boolean getUseCompatPadding();
    
    void setCardBackground(final Drawable p0);
    
    void setMinWidthHeightInternal(final int p0, final int p1);
    
    void setShadowPadding(final int p0, final int p1, final int p2, final int p3);
}
