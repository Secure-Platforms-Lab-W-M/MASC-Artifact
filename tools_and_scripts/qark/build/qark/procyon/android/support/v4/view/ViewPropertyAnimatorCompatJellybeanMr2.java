// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view;

import android.view.animation.Interpolator;
import android.view.View;

class ViewPropertyAnimatorCompatJellybeanMr2
{
    public static Interpolator getInterpolator(final View view) {
        return (Interpolator)view.animate().getInterpolator();
    }
}
