// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.widget.EdgeEffect;

class EdgeEffectCompatLollipop
{
    public static boolean onPull(final Object o, final float n, final float n2) {
        ((EdgeEffect)o).onPull(n, n2);
        return true;
    }
}
