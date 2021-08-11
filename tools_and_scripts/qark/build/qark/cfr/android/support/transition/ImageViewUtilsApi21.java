/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.graphics.Matrix
 *  android.util.Log
 *  android.widget.ImageView
 */
package android.support.transition;

import android.animation.Animator;
import android.graphics.Matrix;
import android.support.annotation.RequiresApi;
import android.support.transition.ImageViewUtilsImpl;
import android.util.Log;
import android.widget.ImageView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiresApi(value=21)
class ImageViewUtilsApi21
implements ImageViewUtilsImpl {
    private static final String TAG = "ImageViewUtilsApi21";
    private static Method sAnimateTransformMethod;
    private static boolean sAnimateTransformMethodFetched;

    ImageViewUtilsApi21() {
    }

    private void fetchAnimateTransformMethod() {
        if (!sAnimateTransformMethodFetched) {
            try {
                sAnimateTransformMethod = ImageView.class.getDeclaredMethod("animateTransform", Matrix.class);
                sAnimateTransformMethod.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)"ImageViewUtilsApi21", (String)"Failed to retrieve animateTransform method", (Throwable)noSuchMethodException);
            }
            sAnimateTransformMethodFetched = true;
            return;
        }
    }

    @Override
    public void animateTransform(ImageView imageView, Matrix matrix) {
        this.fetchAnimateTransformMethod();
        Method method = sAnimateTransformMethod;
        if (method != null) {
            try {
                method.invoke((Object)imageView, new Object[]{matrix});
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException(invocationTargetException.getCause());
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
            return;
        }
    }

    @Override
    public void reserveEndAnimateTransform(ImageView imageView, Animator animator2) {
    }

    @Override
    public void startAnimateTransform(ImageView imageView) {
    }
}

