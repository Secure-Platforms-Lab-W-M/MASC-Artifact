// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view;

import android.view.ScaleGestureDetector;

class ScaleGestureDetectorCompatKitKat
{
    private ScaleGestureDetectorCompatKitKat() {
    }
    
    public static boolean isQuickScaleEnabled(final Object o) {
        return ((ScaleGestureDetector)o).isQuickScaleEnabled();
    }
    
    public static void setQuickScaleEnabled(final Object o, final boolean quickScaleEnabled) {
        ((ScaleGestureDetector)o).setQuickScaleEnabled(quickScaleEnabled);
    }
}
