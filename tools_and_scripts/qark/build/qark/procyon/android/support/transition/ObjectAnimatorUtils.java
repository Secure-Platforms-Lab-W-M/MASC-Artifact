// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Property;
import android.os.Build$VERSION;

class ObjectAnimatorUtils
{
    private static final ObjectAnimatorUtilsImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = new ObjectAnimatorUtilsApi21();
            return;
        }
        IMPL = new ObjectAnimatorUtilsApi14();
    }
    
    static <T> ObjectAnimator ofPointF(final T t, final Property<T, PointF> property, final Path path) {
        return ObjectAnimatorUtils.IMPL.ofPointF(t, property, path);
    }
}
