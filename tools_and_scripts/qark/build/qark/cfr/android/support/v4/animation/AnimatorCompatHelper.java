/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 */
package android.support.v4.animation;

import android.os.Build;
import android.support.annotation.RestrictTo;
import android.support.v4.animation.AnimatorProvider;
import android.support.v4.animation.GingerbreadAnimatorCompatProvider;
import android.support.v4.animation.HoneycombMr1AnimatorCompatProvider;
import android.support.v4.animation.ValueAnimatorCompat;
import android.view.View;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public final class AnimatorCompatHelper {
    private static final AnimatorProvider IMPL = Build.VERSION.SDK_INT >= 12 ? new HoneycombMr1AnimatorCompatProvider() : new GingerbreadAnimatorCompatProvider();

    private AnimatorCompatHelper() {
    }

    public static void clearInterpolator(View view) {
        IMPL.clearInterpolator(view);
    }

    public static ValueAnimatorCompat emptyValueAnimator() {
        return IMPL.emptyValueAnimator();
    }
}

