/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.AnimatorSet
 *  android.animation.TypeEvaluator
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.Matrix
 *  android.graphics.Picture
 *  android.graphics.RectF
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroupOverlay
 *  android.view.ViewParent
 *  android.widget.ImageView
 *  android.widget.ImageView$ScaleType
 */
package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewParent;
import android.widget.ImageView;
import androidx.transition.ViewUtils;

class TransitionUtils {
    private static final boolean HAS_IS_ATTACHED_TO_WINDOW;
    private static final boolean HAS_OVERLAY;
    private static final boolean HAS_PICTURE_BITMAP;
    private static final int MAX_IMAGE_SIZE = 1048576;

    static {
        int n = Build.VERSION.SDK_INT;
        boolean bl = true;
        boolean bl2 = n >= 19;
        HAS_IS_ATTACHED_TO_WINDOW = bl2;
        bl2 = Build.VERSION.SDK_INT >= 18;
        HAS_OVERLAY = bl2;
        bl2 = Build.VERSION.SDK_INT >= 28 ? bl : false;
        HAS_PICTURE_BITMAP = bl2;
    }

    private TransitionUtils() {
    }

    static View copyViewImage(ViewGroup viewGroup, View view, View view2) {
        Matrix matrix = new Matrix();
        matrix.setTranslate((float)(- view2.getScrollX()), (float)(- view2.getScrollY()));
        ViewUtils.transformMatrixToGlobal(view, matrix);
        ViewUtils.transformMatrixToLocal((View)viewGroup, matrix);
        RectF rectF = new RectF(0.0f, 0.0f, (float)view.getWidth(), (float)view.getHeight());
        matrix.mapRect(rectF);
        int n = Math.round(rectF.left);
        int n2 = Math.round(rectF.top);
        int n3 = Math.round(rectF.right);
        int n4 = Math.round(rectF.bottom);
        view2 = new ImageView(view.getContext());
        view2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewGroup = TransitionUtils.createViewBitmap(view, matrix, rectF, viewGroup);
        if (viewGroup != null) {
            view2.setImageBitmap((Bitmap)viewGroup);
        }
        view2.measure(View.MeasureSpec.makeMeasureSpec((int)(n3 - n), (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)(n4 - n2), (int)1073741824));
        view2.layout(n, n2, n3, n4);
        return view2;
    }

    private static Bitmap createViewBitmap(View view, Matrix matrix, RectF rectF, ViewGroup viewGroup) {
        boolean bl;
        boolean bl2;
        if (HAS_IS_ATTACHED_TO_WINDOW) {
            bl2 = view.isAttachedToWindow() ^ true;
            bl = viewGroup == null ? false : viewGroup.isAttachedToWindow();
        } else {
            bl2 = false;
            bl = false;
        }
        ViewGroup viewGroup2 = null;
        int n = 0;
        ViewGroup viewGroup3 = viewGroup2;
        int n2 = n;
        if (HAS_OVERLAY) {
            viewGroup3 = viewGroup2;
            n2 = n;
            if (bl2) {
                if (!bl) {
                    return null;
                }
                viewGroup3 = (ViewGroup)view.getParent();
                n2 = viewGroup3.indexOfChild(view);
                viewGroup.getOverlay().add(view);
            }
        }
        Object var12_10 = null;
        int n3 = Math.round(rectF.width());
        n = Math.round(rectF.height());
        viewGroup2 = var12_10;
        if (n3 > 0) {
            viewGroup2 = var12_10;
            if (n > 0) {
                float f = Math.min(1.0f, 1048576.0f / (float)(n3 * n));
                n3 = Math.round((float)n3 * f);
                n = Math.round((float)n * f);
                matrix.postTranslate(- rectF.left, - rectF.top);
                matrix.postScale(f, f);
                if (HAS_PICTURE_BITMAP) {
                    rectF = new Picture();
                    viewGroup2 = rectF.beginRecording(n3, n);
                    viewGroup2.concat(matrix);
                    view.draw((Canvas)viewGroup2);
                    rectF.endRecording();
                    viewGroup2 = Bitmap.createBitmap((Picture)rectF);
                } else {
                    viewGroup2 = Bitmap.createBitmap((int)n3, (int)n, (Bitmap.Config)Bitmap.Config.ARGB_8888);
                    rectF = new Canvas((Bitmap)viewGroup2);
                    rectF.concat(matrix);
                    view.draw((Canvas)rectF);
                }
            }
        }
        if (HAS_OVERLAY && bl2) {
            viewGroup.getOverlay().remove(view);
            viewGroup3.addView(view, n2);
        }
        return viewGroup2;
    }

    static Animator mergeAnimators(Animator animator, Animator animator2) {
        if (animator == null) {
            return animator2;
        }
        if (animator2 == null) {
            return animator;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{animator, animator2});
        return animatorSet;
    }

    static class MatrixEvaluator
    implements TypeEvaluator<Matrix> {
        final float[] mTempEndValues = new float[9];
        final Matrix mTempMatrix = new Matrix();
        final float[] mTempStartValues = new float[9];

        MatrixEvaluator() {
        }

        public Matrix evaluate(float f, Matrix arrf, Matrix arrf2) {
            arrf.getValues(this.mTempStartValues);
            arrf2.getValues(this.mTempEndValues);
            for (int i = 0; i < 9; ++i) {
                arrf = this.mTempEndValues;
                float f2 = arrf[i];
                arrf2 = this.mTempStartValues;
                float f3 = arrf2[i];
                arrf[i] = arrf2[i] + f * (f2 - f3);
            }
            this.mTempMatrix.setValues(this.mTempEndValues);
            return this.mTempMatrix;
        }
    }

}

