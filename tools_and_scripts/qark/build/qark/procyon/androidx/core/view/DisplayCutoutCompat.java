// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.view;

import android.view.DisplayCutout;
import android.os.Build$VERSION;
import java.util.List;
import android.graphics.Rect;

public final class DisplayCutoutCompat
{
    private final Object mDisplayCutout;
    
    public DisplayCutoutCompat(final Rect rect, final List<Rect> list) {
        DisplayCutout displayCutout;
        if (Build$VERSION.SDK_INT >= 28) {
            displayCutout = new DisplayCutout(rect, (List)list);
        }
        else {
            displayCutout = null;
        }
        this(displayCutout);
    }
    
    private DisplayCutoutCompat(final Object mDisplayCutout) {
        this.mDisplayCutout = mDisplayCutout;
    }
    
    static DisplayCutoutCompat wrap(final Object o) {
        if (o == null) {
            return null;
        }
        return new DisplayCutoutCompat(o);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final DisplayCutoutCompat displayCutoutCompat = (DisplayCutoutCompat)o;
        final Object mDisplayCutout = this.mDisplayCutout;
        if (mDisplayCutout == null) {
            return displayCutoutCompat.mDisplayCutout == null;
        }
        return mDisplayCutout.equals(displayCutoutCompat.mDisplayCutout);
    }
    
    public List<Rect> getBoundingRects() {
        if (Build$VERSION.SDK_INT >= 28) {
            return (List<Rect>)((DisplayCutout)this.mDisplayCutout).getBoundingRects();
        }
        return null;
    }
    
    public int getSafeInsetBottom() {
        if (Build$VERSION.SDK_INT >= 28) {
            return ((DisplayCutout)this.mDisplayCutout).getSafeInsetBottom();
        }
        return 0;
    }
    
    public int getSafeInsetLeft() {
        if (Build$VERSION.SDK_INT >= 28) {
            return ((DisplayCutout)this.mDisplayCutout).getSafeInsetLeft();
        }
        return 0;
    }
    
    public int getSafeInsetRight() {
        if (Build$VERSION.SDK_INT >= 28) {
            return ((DisplayCutout)this.mDisplayCutout).getSafeInsetRight();
        }
        return 0;
    }
    
    public int getSafeInsetTop() {
        if (Build$VERSION.SDK_INT >= 28) {
            return ((DisplayCutout)this.mDisplayCutout).getSafeInsetTop();
        }
        return 0;
    }
    
    @Override
    public int hashCode() {
        final Object mDisplayCutout = this.mDisplayCutout;
        if (mDisplayCutout == null) {
            return 0;
        }
        return mDisplayCutout.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DisplayCutoutCompat{");
        sb.append(this.mDisplayCutout);
        sb.append("}");
        return sb.toString();
    }
}
