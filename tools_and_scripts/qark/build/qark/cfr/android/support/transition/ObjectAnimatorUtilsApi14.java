/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.ObjectAnimator
 *  android.graphics.Path
 *  android.graphics.PointF
 *  android.util.Property
 */
package android.support.transition;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.RequiresApi;
import android.support.transition.ObjectAnimatorUtilsImpl;
import android.support.transition.PathProperty;
import android.util.Property;

@RequiresApi(value=14)
class ObjectAnimatorUtilsApi14
implements ObjectAnimatorUtilsImpl {
    ObjectAnimatorUtilsApi14() {
    }

    @Override
    public <T> ObjectAnimator ofPointF(T t, Property<T, PointF> property, Path path) {
        return ObjectAnimator.ofFloat(t, new PathProperty<T>(property, path), (float[])new float[]{0.0f, 1.0f});
    }
}

