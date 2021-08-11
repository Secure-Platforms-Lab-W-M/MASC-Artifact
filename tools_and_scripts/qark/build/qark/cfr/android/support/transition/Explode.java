/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.DecelerateInterpolator
 */
package android.support.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.transition.CircularPropagation;
import android.support.transition.R;
import android.support.transition.TransitionPropagation;
import android.support.transition.TransitionValues;
import android.support.transition.TranslationAnimationCreator;
import android.support.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import java.util.Map;

public class Explode
extends Visibility {
    private static final String PROPNAME_SCREEN_BOUNDS = "android:explode:screenBounds";
    private static final TimeInterpolator sAccelerate;
    private static final TimeInterpolator sDecelerate;
    private int[] mTempLoc = new int[2];

    static {
        sDecelerate = new DecelerateInterpolator();
        sAccelerate = new AccelerateInterpolator();
    }

    public Explode() {
        this.setPropagation(new CircularPropagation());
    }

    public Explode(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setPropagation(new CircularPropagation());
    }

    private static float calculateDistance(float f, float f2) {
        return (float)Math.sqrt(f * f + f2 * f2);
    }

    private static float calculateMaxDistance(View view, int n, int n2) {
        n = Math.max(n, view.getWidth() - n);
        n2 = Math.max(n2, view.getHeight() - n2);
        return Explode.calculateDistance(n, n2);
    }

    private void calculateOut(View view, Rect rect, int[] arrn) {
        int n;
        int n2;
        view.getLocationOnScreen(this.mTempLoc);
        Rect rect2 = this.mTempLoc;
        int n3 = rect2[0];
        int n4 = rect2[1];
        rect2 = this.getEpicenter();
        if (rect2 == null) {
            n = view.getWidth() / 2 + n3 + Math.round(view.getTranslationX());
            n2 = view.getHeight() / 2 + n4 + Math.round(view.getTranslationY());
        } else {
            n = rect2.centerX();
            n2 = rect2.centerY();
        }
        int n5 = rect.centerX();
        int n6 = rect.centerY();
        float f = n5 - n;
        float f2 = n6 - n2;
        if (f == 0.0f && f2 == 0.0f) {
            f = (float)(Math.random() * 2.0) - 1.0f;
            f2 = (float)(Math.random() * 2.0) - 1.0f;
        }
        float f3 = Explode.calculateDistance(f, f2);
        f3 = Explode.calculateMaxDistance(view, n - n3, n2 - n4);
        arrn[0] = Math.round(f3 * (f /= f3));
        arrn[1] = Math.round(f3 * (f2 /= f3));
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        view.getLocationOnScreen(this.mTempLoc);
        int[] arrn = this.mTempLoc;
        int n = arrn[0];
        int n2 = arrn[1];
        int n3 = view.getWidth();
        int n4 = view.getHeight();
        transitionValues.values.put("android:explode:screenBounds", (Object)new Rect(n, n2, n3 + n, n4 + n2));
    }

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        this.captureValues(transitionValues);
    }

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        this.captureValues(transitionValues);
    }

    @Override
    public Animator onAppear(ViewGroup arrn, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues2 == null) {
            return null;
        }
        transitionValues = (Rect)transitionValues2.values.get("android:explode:screenBounds");
        float f = view.getTranslationX();
        float f2 = view.getTranslationY();
        this.calculateOut((View)arrn, (Rect)transitionValues, this.mTempLoc);
        arrn = this.mTempLoc;
        float f3 = arrn[0];
        float f4 = arrn[1];
        return TranslationAnimationCreator.createAnimation(view, transitionValues2, transitionValues.left, transitionValues.top, f + f3, f2 + f4, f, f2, sDecelerate);
    }

    @Override
    public Animator onDisappear(ViewGroup arrn, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues == null) {
            return null;
        }
        transitionValues2 = (Rect)transitionValues.values.get("android:explode:screenBounds");
        int n = transitionValues2.left;
        int n2 = transitionValues2.top;
        float f = view.getTranslationX();
        float f2 = view.getTranslationY();
        float f3 = f;
        float f4 = f2;
        int[] arrn2 = (int[])transitionValues.view.getTag(R.id.transition_position);
        if (arrn2 != null) {
            f3 += (float)(arrn2[0] - transitionValues2.left);
            f4 += (float)(arrn2[1] - transitionValues2.top);
            transitionValues2.offsetTo(arrn2[0], arrn2[1]);
        }
        this.calculateOut((View)arrn, (Rect)transitionValues2, this.mTempLoc);
        arrn = this.mTempLoc;
        return TranslationAnimationCreator.createAnimation(view, transitionValues, n, n2, f, f2, f3 + (float)arrn[0], f4 + (float)arrn[1], sAccelerate);
    }
}

