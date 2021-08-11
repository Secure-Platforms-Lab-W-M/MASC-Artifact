/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.ObjectAnimator
 *  android.graphics.Path
 *  android.graphics.PointF
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Property
 */
package android.support.transition;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.support.transition.ObjectAnimatorUtilsApi14;
import android.support.transition.ObjectAnimatorUtilsApi21;
import android.support.transition.ObjectAnimatorUtilsImpl;
import android.util.Property;

class ObjectAnimatorUtils {
    private static final ObjectAnimatorUtilsImpl IMPL = Build.VERSION.SDK_INT >= 21 ? new ObjectAnimatorUtilsApi21() : new ObjectAnimatorUtilsApi14();

    ObjectAnimatorUtils() {
    }

    static <T> ObjectAnimator ofPointF(T t, Property<T, PointF> property, Path path) {
        return IMPL.ofPointF(t, property, path);
    }
}

