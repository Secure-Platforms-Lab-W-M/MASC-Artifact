// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.os.Build$VERSION;

class ViewGroupUtils
{
    private static final ViewGroupUtilsImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 18) {
            IMPL = new ViewGroupUtilsApi18();
            return;
        }
        IMPL = new ViewGroupUtilsApi14();
    }
    
    static ViewGroupOverlayImpl getOverlay(@NonNull final ViewGroup viewGroup) {
        return ViewGroupUtils.IMPL.getOverlay(viewGroup);
    }
    
    static void suppressLayout(@NonNull final ViewGroup viewGroup, final boolean b) {
        ViewGroupUtils.IMPL.suppressLayout(viewGroup, b);
    }
}
