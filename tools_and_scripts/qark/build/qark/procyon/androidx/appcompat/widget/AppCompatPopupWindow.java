// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import android.view.View;
import androidx.core.widget.PopupWindowCompat;
import androidx.appcompat.R$styleable;
import android.util.AttributeSet;
import android.content.Context;
import android.os.Build$VERSION;
import android.widget.PopupWindow;

class AppCompatPopupWindow extends PopupWindow
{
    private static final boolean COMPAT_OVERLAP_ANCHOR;
    private boolean mOverlapAnchor;
    
    static {
        COMPAT_OVERLAP_ANCHOR = (Build$VERSION.SDK_INT < 21);
    }
    
    public AppCompatPopupWindow(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.init(context, set, n, 0);
    }
    
    public AppCompatPopupWindow(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
        this.init(context, set, n, n2);
    }
    
    private void init(final Context context, final AttributeSet set, final int n, final int n2) {
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R$styleable.PopupWindow, n, n2);
        if (obtainStyledAttributes.hasValue(R$styleable.PopupWindow_overlapAnchor)) {
            this.setSupportOverlapAnchor(obtainStyledAttributes.getBoolean(R$styleable.PopupWindow_overlapAnchor, false));
        }
        this.setBackgroundDrawable(obtainStyledAttributes.getDrawable(R$styleable.PopupWindow_android_popupBackground));
        obtainStyledAttributes.recycle();
    }
    
    private void setSupportOverlapAnchor(final boolean mOverlapAnchor) {
        if (AppCompatPopupWindow.COMPAT_OVERLAP_ANCHOR) {
            this.mOverlapAnchor = mOverlapAnchor;
            return;
        }
        PopupWindowCompat.setOverlapAnchor(this, mOverlapAnchor);
    }
    
    public void showAsDropDown(final View view, final int n, final int n2) {
        int n3 = n2;
        if (AppCompatPopupWindow.COMPAT_OVERLAP_ANCHOR) {
            n3 = n2;
            if (this.mOverlapAnchor) {
                n3 = n2 - view.getHeight();
            }
        }
        super.showAsDropDown(view, n, n3);
    }
    
    public void showAsDropDown(final View view, final int n, final int n2, final int n3) {
        int n4 = n2;
        if (AppCompatPopupWindow.COMPAT_OVERLAP_ANCHOR) {
            n4 = n2;
            if (this.mOverlapAnchor) {
                n4 = n2 - view.getHeight();
            }
        }
        super.showAsDropDown(view, n, n4, n3);
    }
    
    public void update(final View view, final int n, final int n2, final int n3, final int n4) {
        int n5 = n2;
        if (AppCompatPopupWindow.COMPAT_OVERLAP_ANCHOR) {
            n5 = n2;
            if (this.mOverlapAnchor) {
                n5 = n2 - view.getHeight();
            }
        }
        super.update(view, n, n5, n3, n4);
    }
}
