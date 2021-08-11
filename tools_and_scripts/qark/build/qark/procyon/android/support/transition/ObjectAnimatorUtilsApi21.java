// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.animation.TypeConverter;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Property;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class ObjectAnimatorUtilsApi21 implements ObjectAnimatorUtilsImpl
{
    @Override
    public <T> ObjectAnimator ofPointF(final T t, final Property<T, PointF> property, final Path path) {
        return ObjectAnimator.ofObject((Object)t, (Property)property, (TypeConverter)null, path);
    }
}
