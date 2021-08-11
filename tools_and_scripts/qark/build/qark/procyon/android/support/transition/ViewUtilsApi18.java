// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.support.annotation.NonNull;
import android.view.View;
import android.support.annotation.RequiresApi;

@RequiresApi(18)
class ViewUtilsApi18 extends ViewUtilsApi14
{
    @Override
    public ViewOverlayImpl getOverlay(@NonNull final View view) {
        return new ViewOverlayApi18(view);
    }
    
    @Override
    public WindowIdImpl getWindowId(@NonNull final View view) {
        return new WindowIdApi18(view);
    }
}
