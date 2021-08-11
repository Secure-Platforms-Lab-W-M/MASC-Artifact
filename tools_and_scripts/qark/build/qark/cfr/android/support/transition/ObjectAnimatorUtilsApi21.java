/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.ObjectAnimator
 *  android.animation.TypeConverter
 *  android.graphics.Path
 *  android.graphics.PointF
 *  android.util.Property
 */
package android.support.transition;

import android.animation.ObjectAnimator;
import android.animation.TypeConverter;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.RequiresApi;
import android.support.transition.ObjectAnimatorUtilsImpl;
import android.util.Property;

@RequiresApi(value=21)
class ObjectAnimatorUtilsApi21
implements ObjectAnimatorUtilsImpl {
    ObjectAnimatorUtilsApi21() {
    }

    @Override
    public <T> ObjectAnimator ofPointF(T t, Property<T, PointF> property, Path path) {
        return ObjectAnimator.ofObject(t, property, (TypeConverter)null, (Path)path);
    }
}

