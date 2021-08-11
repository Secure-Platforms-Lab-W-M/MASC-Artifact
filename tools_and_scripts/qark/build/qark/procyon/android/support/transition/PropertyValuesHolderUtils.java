// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.animation.PropertyValuesHolder;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Property;
import android.os.Build$VERSION;

class PropertyValuesHolderUtils
{
    private static final PropertyValuesHolderUtilsImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = new PropertyValuesHolderUtilsApi21();
            return;
        }
        IMPL = new PropertyValuesHolderUtilsApi14();
    }
    
    static PropertyValuesHolder ofPointF(final Property<?, PointF> property, final Path path) {
        return PropertyValuesHolderUtils.IMPL.ofPointF(property, path);
    }
}
