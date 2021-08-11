// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Property;

interface ObjectAnimatorUtilsImpl
{
     <T> ObjectAnimator ofPointF(final T p0, final Property<T, PointF> p1, final Path p2);
}
