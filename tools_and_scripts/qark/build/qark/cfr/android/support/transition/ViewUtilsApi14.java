/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.os.IBinder
 *  android.view.View
 *  android.view.ViewParent
 */
package android.support.transition;

import android.graphics.Matrix;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.R;
import android.support.transition.ViewOverlayApi14;
import android.support.transition.ViewOverlayImpl;
import android.support.transition.ViewUtilsImpl;
import android.support.transition.WindowIdApi14;
import android.support.transition.WindowIdImpl;
import android.view.View;
import android.view.ViewParent;

@RequiresApi(value=14)
class ViewUtilsApi14
implements ViewUtilsImpl {
    private float[] mMatrixValues;

    ViewUtilsApi14() {
    }

    @Override
    public void clearNonTransitionAlpha(@NonNull View view) {
        if (view.getVisibility() == 0) {
            view.setTag(R.id.save_non_transition_alpha, (Object)null);
            return;
        }
    }

    @Override
    public ViewOverlayImpl getOverlay(@NonNull View view) {
        return ViewOverlayApi14.createFrom(view);
    }

    @Override
    public float getTransitionAlpha(@NonNull View view) {
        Float f = (Float)view.getTag(R.id.save_non_transition_alpha);
        if (f != null) {
            return view.getAlpha() / f.floatValue();
        }
        return view.getAlpha();
    }

    @Override
    public WindowIdImpl getWindowId(@NonNull View view) {
        return new WindowIdApi14(view.getWindowToken());
    }

    @Override
    public void saveNonTransitionAlpha(@NonNull View view) {
        if (view.getTag(R.id.save_non_transition_alpha) == null) {
            view.setTag(R.id.save_non_transition_alpha, (Object)Float.valueOf(view.getAlpha()));
            return;
        }
    }

    @Override
    public void setAnimationMatrix(@NonNull View view, Matrix matrix) {
        if (matrix != null && !matrix.isIdentity()) {
            float[] arrf = this.mMatrixValues;
            if (arrf == null) {
                float[] arrf2 = new float[9];
                arrf = arrf2;
                this.mMatrixValues = arrf2;
            }
            matrix.getValues(arrf);
            float f = arrf[3];
            float f2 = (float)Math.sqrt(1.0f - f * f);
            int n = arrf[0] < 0.0f ? -1 : 1;
            float f3 = f2 * (float)n;
            f = (float)Math.toDegrees(Math.atan2(f, f3));
            f2 = arrf[0] / f3;
            f3 = arrf[4] / f3;
            float f4 = arrf[2];
            float f5 = arrf[5];
            view.setPivotX(0.0f);
            view.setPivotY(0.0f);
            view.setTranslationX(f4);
            view.setTranslationY(f5);
            view.setRotation(f);
            view.setScaleX(f2);
            view.setScaleY(f3);
            return;
        }
        view.setPivotX((float)(view.getWidth() / 2));
        view.setPivotY((float)(view.getHeight() / 2));
        view.setTranslationX(0.0f);
        view.setTranslationY(0.0f);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
        view.setRotation(0.0f);
    }

    @Override
    public void setLeftTopRightBottom(View view, int n, int n2, int n3, int n4) {
        view.setLeft(n);
        view.setTop(n2);
        view.setRight(n3);
        view.setBottom(n4);
    }

    @Override
    public void setTransitionAlpha(@NonNull View view, float f) {
        Float f2 = (Float)view.getTag(R.id.save_non_transition_alpha);
        if (f2 != null) {
            view.setAlpha(f2.floatValue() * f);
            return;
        }
        view.setAlpha(f);
    }

    @Override
    public void transformMatrixToGlobal(@NonNull View view, @NonNull Matrix matrix) {
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof View) {
            viewParent = (View)viewParent;
            this.transformMatrixToGlobal((View)viewParent, matrix);
            matrix.preTranslate((float)(- viewParent.getScrollX()), (float)(- viewParent.getScrollY()));
        }
        matrix.preTranslate((float)view.getLeft(), (float)view.getTop());
        view = view.getMatrix();
        if (!view.isIdentity()) {
            matrix.preConcat((Matrix)view);
            return;
        }
    }

    @Override
    public void transformMatrixToLocal(@NonNull View view, @NonNull Matrix matrix) {
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof View) {
            viewParent = (View)viewParent;
            this.transformMatrixToLocal((View)viewParent, matrix);
            matrix.postTranslate((float)viewParent.getScrollX(), (float)viewParent.getScrollY());
        }
        matrix.postTranslate((float)view.getLeft(), (float)view.getTop());
        view = view.getMatrix();
        if (!view.isIdentity()) {
            viewParent = new Matrix();
            if (view.invert((Matrix)viewParent)) {
                matrix.postConcat((Matrix)viewParent);
                return;
            }
            return;
        }
    }
}

