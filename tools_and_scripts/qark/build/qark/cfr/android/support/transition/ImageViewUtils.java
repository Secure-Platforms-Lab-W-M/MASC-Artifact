/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.graphics.Matrix
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.widget.ImageView
 */
package android.support.transition;

import android.animation.Animator;
import android.graphics.Matrix;
import android.os.Build;
import android.support.transition.ImageViewUtilsApi14;
import android.support.transition.ImageViewUtilsApi21;
import android.support.transition.ImageViewUtilsImpl;
import android.widget.ImageView;

class ImageViewUtils {
    private static final ImageViewUtilsImpl IMPL = Build.VERSION.SDK_INT >= 21 ? new ImageViewUtilsApi21() : new ImageViewUtilsApi14();

    ImageViewUtils() {
    }

    static void animateTransform(ImageView imageView, Matrix matrix) {
        IMPL.animateTransform(imageView, matrix);
    }

    static void reserveEndAnimateTransform(ImageView imageView, Animator animator2) {
        IMPL.reserveEndAnimateTransform(imageView, animator2);
    }

    static void startAnimateTransform(ImageView imageView) {
        IMPL.startAnimateTransform(imageView);
    }
}

