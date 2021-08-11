// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.support.annotation.RequiresApi;

@RequiresApi(14)
interface ViewGroupUtilsImpl
{
    ViewGroupOverlayImpl getOverlay(@NonNull final ViewGroup p0);
    
    void suppressLayout(@NonNull final ViewGroup p0, final boolean p1);
}
