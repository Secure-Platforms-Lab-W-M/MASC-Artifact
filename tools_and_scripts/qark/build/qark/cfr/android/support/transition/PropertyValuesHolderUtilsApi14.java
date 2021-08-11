/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.PropertyValuesHolder
 *  android.graphics.Path
 *  android.graphics.PointF
 *  android.util.Property
 */
package android.support.transition;

import android.animation.PropertyValuesHolder;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.RequiresApi;
import android.support.transition.PathProperty;
import android.support.transition.PropertyValuesHolderUtilsImpl;
import android.util.Property;

@RequiresApi(value=14)
class PropertyValuesHolderUtilsApi14
implements PropertyValuesHolderUtilsImpl {
    PropertyValuesHolderUtilsApi14() {
    }

    @Override
    public PropertyValuesHolder ofPointF(Property<?, PointF> property, Path path) {
        return PropertyValuesHolder.ofFloat(new PathProperty(property, path), (float[])new float[]{0.0f, 1.0f});
    }
}

