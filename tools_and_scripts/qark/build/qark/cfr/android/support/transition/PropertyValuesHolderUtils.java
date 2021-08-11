/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.PropertyValuesHolder
 *  android.graphics.Path
 *  android.graphics.PointF
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Property
 */
package android.support.transition;

import android.animation.PropertyValuesHolder;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.support.transition.PropertyValuesHolderUtilsApi14;
import android.support.transition.PropertyValuesHolderUtilsApi21;
import android.support.transition.PropertyValuesHolderUtilsImpl;
import android.util.Property;

class PropertyValuesHolderUtils {
    private static final PropertyValuesHolderUtilsImpl IMPL = Build.VERSION.SDK_INT >= 21 ? new PropertyValuesHolderUtilsApi21() : new PropertyValuesHolderUtilsApi14();

    PropertyValuesHolderUtils() {
    }

    static PropertyValuesHolder ofPointF(Property<?, PointF> property, Path path) {
        return IMPL.ofPointF(property, path);
    }
}

