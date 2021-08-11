/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.ObjectAnimator
 *  android.animation.TypeEvaluator
 *  android.content.Context
 *  android.graphics.Matrix
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.util.Property
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.ImageView
 *  android.widget.ImageView$ScaleType
 */
package android.support.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.transition.ImageViewUtils;
import android.support.transition.MatrixUtils;
import android.support.transition.Transition;
import android.support.transition.TransitionUtils;
import android.support.transition.TransitionValues;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.Map;

public class ChangeImageTransform
extends Transition {
    private static final Property<ImageView, Matrix> ANIMATED_TRANSFORM_PROPERTY;
    private static final TypeEvaluator<Matrix> NULL_MATRIX_EVALUATOR;
    private static final String PROPNAME_BOUNDS = "android:changeImageTransform:bounds";
    private static final String PROPNAME_MATRIX = "android:changeImageTransform:matrix";
    private static final String[] sTransitionProperties;

    static {
        sTransitionProperties = new String[]{"android:changeImageTransform:matrix", "android:changeImageTransform:bounds"};
        NULL_MATRIX_EVALUATOR = new TypeEvaluator<Matrix>(){

            public Matrix evaluate(float f, Matrix matrix, Matrix matrix2) {
                return null;
            }
        };
        ANIMATED_TRANSFORM_PROPERTY = new Property<ImageView, Matrix>(Matrix.class, "animatedTransform"){

            public Matrix get(ImageView imageView) {
                return null;
            }

            public void set(ImageView imageView, Matrix matrix) {
                ImageViewUtils.animateTransform(imageView, matrix);
            }
        };
    }

    public ChangeImageTransform() {
    }

    public ChangeImageTransform(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void captureValues(TransitionValues object) {
        View view = object.view;
        if (view instanceof ImageView) {
            if (view.getVisibility() != 0) {
                return;
            }
            ImageView imageView = (ImageView)view;
            if (imageView.getDrawable() == null) {
                return;
            }
            object = object.values;
            object.put("android:changeImageTransform:bounds", new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
            object.put("android:changeImageTransform:matrix", ChangeImageTransform.copyImageMatrix(imageView));
            return;
        }
    }

    private static Matrix centerCropMatrix(ImageView imageView) {
        Drawable drawable2 = imageView.getDrawable();
        int n = drawable2.getIntrinsicWidth();
        int n2 = imageView.getWidth();
        float f = (float)n2 / (float)n;
        int n3 = drawable2.getIntrinsicHeight();
        int n4 = imageView.getHeight();
        f = Math.max(f, (float)n4 / (float)n3);
        float f2 = n;
        float f3 = n3;
        n = Math.round(((float)n2 - f2 * f) / 2.0f);
        n4 = Math.round(((float)n4 - f3 * f) / 2.0f);
        imageView = new Matrix();
        imageView.postScale(f, f);
        imageView.postTranslate((float)n, (float)n4);
        return imageView;
    }

    private static Matrix copyImageMatrix(ImageView imageView) {
        switch (.$SwitchMap$android$widget$ImageView$ScaleType[imageView.getScaleType().ordinal()]) {
            default: {
                return new Matrix(imageView.getImageMatrix());
            }
            case 2: {
                return ChangeImageTransform.centerCropMatrix(imageView);
            }
            case 1: 
        }
        return ChangeImageTransform.fitXYMatrix(imageView);
    }

    private ObjectAnimator createMatrixAnimator(ImageView imageView, Matrix matrix, Matrix matrix2) {
        return ObjectAnimator.ofObject((Object)imageView, ANIMATED_TRANSFORM_PROPERTY, (TypeEvaluator)new TransitionUtils.MatrixEvaluator(), (Object[])new Matrix[]{matrix, matrix2});
    }

    private ObjectAnimator createNullAnimator(ImageView imageView) {
        return ObjectAnimator.ofObject((Object)imageView, ANIMATED_TRANSFORM_PROPERTY, NULL_MATRIX_EVALUATOR, (Object[])new Matrix[]{null, null});
    }

    private static Matrix fitXYMatrix(ImageView imageView) {
        Drawable drawable2 = imageView.getDrawable();
        Matrix matrix = new Matrix();
        matrix.postScale((float)imageView.getWidth() / (float)drawable2.getIntrinsicWidth(), (float)imageView.getHeight() / (float)drawable2.getIntrinsicHeight());
        return matrix;
    }

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public Animator createAnimator(@NonNull ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues == null) {
            return null;
        }
        if (transitionValues2 == null) {
            return null;
        }
        Rect rect = (Rect)transitionValues.values.get("android:changeImageTransform:bounds");
        Rect rect2 = (Rect)transitionValues2.values.get("android:changeImageTransform:bounds");
        if (rect == null) {
            return null;
        }
        if (rect2 == null) {
            return null;
        }
        viewGroup = (Matrix)transitionValues.values.get("android:changeImageTransform:matrix");
        transitionValues = (Matrix)transitionValues2.values.get("android:changeImageTransform:matrix");
        int n = viewGroup == null && transitionValues == null || viewGroup != null && viewGroup.equals((Object)transitionValues) ? 1 : 0;
        if (rect.equals((Object)rect2) && n != 0) {
            return null;
        }
        transitionValues2 = (ImageView)transitionValues2.view;
        rect = transitionValues2.getDrawable();
        n = rect.getIntrinsicWidth();
        int n2 = rect.getIntrinsicHeight();
        ImageViewUtils.startAnimateTransform((ImageView)transitionValues2);
        if (n != 0 && n2 != 0) {
            if (viewGroup == null) {
                viewGroup = MatrixUtils.IDENTITY_MATRIX;
            }
            if (transitionValues == null) {
                transitionValues = MatrixUtils.IDENTITY_MATRIX;
            }
            ANIMATED_TRANSFORM_PROPERTY.set((Object)transitionValues2, (Object)viewGroup);
            viewGroup = this.createMatrixAnimator((ImageView)transitionValues2, (Matrix)viewGroup, (Matrix)transitionValues);
        } else {
            viewGroup = this.createNullAnimator((ImageView)transitionValues2);
        }
        ImageViewUtils.reserveEndAnimateTransform((ImageView)transitionValues2, (Animator)viewGroup);
        return viewGroup;
    }

    @Override
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

}

