/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.widget.ImageView
 */
package androidx.transition;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;
import java.lang.reflect.Field;

class ImageViewUtils {
    private static Field sDrawMatrixField;
    private static boolean sDrawMatrixFieldFetched;
    private static boolean sTryHiddenAnimateTransform;

    static {
        sTryHiddenAnimateTransform = true;
    }

    private ImageViewUtils() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static void animateTransform(ImageView imageView, Matrix matrix) {
        if (Build.VERSION.SDK_INT >= 29) {
            imageView.animateTransform(matrix);
            return;
        }
        if (matrix == null) {
            matrix = imageView.getDrawable();
            if (matrix != null) {
                matrix.setBounds(0, 0, imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight(), imageView.getHeight() - imageView.getPaddingTop() - imageView.getPaddingBottom());
                imageView.invalidate();
            }
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            ImageViewUtils.hiddenAnimateTransform(imageView, matrix);
            return;
        }
        Drawable drawable2 = imageView.getDrawable();
        if (drawable2 != null) {
            drawable2.setBounds(0, 0, drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
            drawable2 = null;
            Matrix matrix2 = null;
            ImageViewUtils.fetchDrawMatrixField();
            Field field = sDrawMatrixField;
            if (field != null) {
                drawable2 = matrix2;
                try {
                    matrix2 = (Matrix)field.get((Object)imageView);
                    drawable2 = matrix2;
                    if (matrix2 == null) {
                        drawable2 = matrix2;
                        matrix2 = new Matrix();
                        drawable2 = matrix2;
                        sDrawMatrixField.set((Object)imageView, (Object)matrix2);
                        drawable2 = matrix2;
                    }
                }
                catch (IllegalAccessException illegalAccessException) {
                    // empty catch block
                }
            }
            if (drawable2 != null) {
                drawable2.set(matrix);
            }
            imageView.invalidate();
        }
    }

    private static void fetchDrawMatrixField() {
        if (!sDrawMatrixFieldFetched) {
            try {
                Field field;
                sDrawMatrixField = field = ImageView.class.getDeclaredField("mDrawMatrix");
                field.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                // empty catch block
            }
            sDrawMatrixFieldFetched = true;
        }
    }

    private static void hiddenAnimateTransform(ImageView imageView, Matrix matrix) {
        if (sTryHiddenAnimateTransform) {
            try {
                imageView.animateTransform(matrix);
                return;
            }
            catch (NoSuchMethodError noSuchMethodError) {
                sTryHiddenAnimateTransform = false;
            }
        }
    }
}

