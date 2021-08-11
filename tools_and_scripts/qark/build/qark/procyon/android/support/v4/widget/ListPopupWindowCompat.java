// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.os.Build$VERSION;
import android.view.View$OnTouchListener;
import android.view.View;
import android.widget.ListPopupWindow;

public final class ListPopupWindowCompat
{
    private ListPopupWindowCompat() {
    }
    
    public static View$OnTouchListener createDragToOpenListener(final ListPopupWindow listPopupWindow, final View view) {
        if (Build$VERSION.SDK_INT >= 19) {
            return listPopupWindow.createDragToOpenListener(view);
        }
        return null;
    }
    
    @Deprecated
    public static View$OnTouchListener createDragToOpenListener(final Object o, final View view) {
        return createDragToOpenListener((ListPopupWindow)o, view);
    }
}
