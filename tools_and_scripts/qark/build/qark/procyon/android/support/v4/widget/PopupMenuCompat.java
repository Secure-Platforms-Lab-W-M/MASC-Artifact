// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.widget.PopupMenu;
import android.os.Build$VERSION;
import android.view.View$OnTouchListener;

public final class PopupMenuCompat
{
    private PopupMenuCompat() {
    }
    
    public static View$OnTouchListener getDragToOpenListener(final Object o) {
        if (Build$VERSION.SDK_INT >= 19) {
            return ((PopupMenu)o).getDragToOpenListener();
        }
        return null;
    }
}
