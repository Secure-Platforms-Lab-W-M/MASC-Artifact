/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.ValueAnimator
 *  android.graphics.Paint
 *  android.view.View
 *  android.view.ViewParent
 */
package android.support.v4.view;

import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewParent;

class ViewCompatHC {
    ViewCompatHC() {
    }

    public static int combineMeasuredStates(int n, int n2) {
        return View.combineMeasuredStates((int)n, (int)n2);
    }

    public static float getAlpha(View view) {
        return view.getAlpha();
    }

    static long getFrameTime() {
        return ValueAnimator.getFrameDelay();
    }

    public static int getLayerType(View view) {
        return view.getLayerType();
    }

    public static int getMeasuredHeightAndState(View view) {
        return view.getMeasuredHeightAndState();
    }

    public static int getMeasuredState(View view) {
        return view.getMeasuredState();
    }

    public static int getMeasuredWidthAndState(View view) {
        return view.getMeasuredWidthAndState();
    }

    public static float getPivotX(View view) {
        return view.getPivotX();
    }

    public static float getPivotY(View view) {
        return view.getPivotY();
    }

    public static float getRotation(View view) {
        return view.getRotation();
    }

    public static float getRotationX(View view) {
        return view.getRotationX();
    }

    public static float getRotationY(View view) {
        return view.getRotationY();
    }

    public static float getScaleX(View view) {
        return view.getScaleX();
    }

    public static float getScaleY(View view) {
        return view.getScaleY();
    }

    public static float getTranslationX(View view) {
        return view.getTranslationX();
    }

    public static float getTranslationY(View view) {
        return view.getTranslationY();
    }

    public static float getX(View view) {
        return view.getX();
    }

    public static float getY(View view) {
        return view.getY();
    }

    public static void jumpDrawablesToCurrentState(View view) {
        view.jumpDrawablesToCurrentState();
    }

    static void offsetLeftAndRight(View view, int n) {
        view.offsetLeftAndRight(n);
        view = view.getParent();
        if (view instanceof View) {
            ViewCompatHC.tickleInvalidationFlag(view);
        }
    }

    static void offsetTopAndBottom(View view, int n) {
        view.offsetTopAndBottom(n);
        view = view.getParent();
        if (view instanceof View) {
            ViewCompatHC.tickleInvalidationFlag(view);
        }
    }

    public static int resolveSizeAndState(int n, int n2, int n3) {
        return View.resolveSizeAndState((int)n, (int)n2, (int)n3);
    }

    public static void setActivated(View view, boolean bl) {
        view.setActivated(bl);
    }

    public static void setAlpha(View view, float f) {
        view.setAlpha(f);
    }

    public static void setLayerType(View view, int n, Paint paint) {
        view.setLayerType(n, paint);
    }

    public static void setPivotX(View view, float f) {
        view.setPivotX(f);
    }

    public static void setPivotY(View view, float f) {
        view.setPivotY(f);
    }

    public static void setRotation(View view, float f) {
        view.setRotation(f);
    }

    public static void setRotationX(View view, float f) {
        view.setRotationX(f);
    }

    public static void setRotationY(View view, float f) {
        view.setRotationY(f);
    }

    public static void setSaveFromParentEnabled(View view, boolean bl) {
        view.setSaveFromParentEnabled(bl);
    }

    public static void setScaleX(View view, float f) {
        view.setScaleX(f);
    }

    public static void setScaleY(View view, float f) {
        view.setScaleY(f);
    }

    public static void setTranslationX(View view, float f) {
        view.setTranslationX(f);
    }

    public static void setTranslationY(View view, float f) {
        view.setTranslationY(f);
    }

    public static void setX(View view, float f) {
        view.setX(f);
    }

    public static void setY(View view, float f) {
        view.setY(f);
    }

    private static void tickleInvalidationFlag(View view) {
        float f = view.getTranslationY();
        view.setTranslationY(1.0f + f);
        view.setTranslationY(f);
    }
}

