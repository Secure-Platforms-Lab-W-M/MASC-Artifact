// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.animation.Animator;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.graphics.Rect;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.animation.TimeInterpolator;

public class Explode extends Visibility
{
    private static final String PROPNAME_SCREEN_BOUNDS = "android:explode:screenBounds";
    private static final TimeInterpolator sAccelerate;
    private static final TimeInterpolator sDecelerate;
    private int[] mTempLoc;
    
    static {
        sDecelerate = (TimeInterpolator)new DecelerateInterpolator();
        sAccelerate = (TimeInterpolator)new AccelerateInterpolator();
    }
    
    public Explode() {
        this.mTempLoc = new int[2];
        this.setPropagation(new CircularPropagation());
    }
    
    public Explode(final Context context, final AttributeSet set) {
        super(context, set);
        this.mTempLoc = new int[2];
        this.setPropagation(new CircularPropagation());
    }
    
    private static float calculateDistance(final float n, final float n2) {
        return (float)Math.sqrt(n * n + n2 * n2);
    }
    
    private static float calculateMaxDistance(final View view, int max, int max2) {
        max = Math.max(max, view.getWidth() - max);
        max2 = Math.max(max2, view.getHeight() - max2);
        return calculateDistance((float)max, (float)max2);
    }
    
    private void calculateOut(final View view, final Rect rect, final int[] array) {
        view.getLocationOnScreen(this.mTempLoc);
        final int[] mTempLoc = this.mTempLoc;
        final int n = mTempLoc[0];
        final int n2 = mTempLoc[1];
        final Rect epicenter = this.getEpicenter();
        int centerX;
        int centerY;
        if (epicenter == null) {
            centerX = view.getWidth() / 2 + n + Math.round(view.getTranslationX());
            centerY = view.getHeight() / 2 + n2 + Math.round(view.getTranslationY());
        }
        else {
            centerX = epicenter.centerX();
            centerY = epicenter.centerY();
        }
        final int centerX2 = rect.centerX();
        final int centerY2 = rect.centerY();
        float n3 = (float)(centerX2 - centerX);
        float n4 = (float)(centerY2 - centerY);
        if (n3 == 0.0f && n4 == 0.0f) {
            n3 = (float)(Math.random() * 2.0) - 1.0f;
            n4 = (float)(Math.random() * 2.0) - 1.0f;
        }
        final float calculateDistance = calculateDistance(n3, n4);
        final float n5 = n3 / calculateDistance;
        final float n6 = n4 / calculateDistance;
        final float calculateMaxDistance = calculateMaxDistance(view, centerX - n, centerY - n2);
        array[0] = Math.round(calculateMaxDistance * n5);
        array[1] = Math.round(calculateMaxDistance * n6);
    }
    
    private void captureValues(final TransitionValues transitionValues) {
        final View view = transitionValues.view;
        view.getLocationOnScreen(this.mTempLoc);
        final int[] mTempLoc = this.mTempLoc;
        final int n = mTempLoc[0];
        final int n2 = mTempLoc[1];
        transitionValues.values.put("android:explode:screenBounds", new Rect(n, n2, view.getWidth() + n, view.getHeight() + n2));
    }
    
    @Override
    public void captureEndValues(@NonNull final TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        this.captureValues(transitionValues);
    }
    
    @Override
    public void captureStartValues(@NonNull final TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        this.captureValues(transitionValues);
    }
    
    @Override
    public Animator onAppear(final ViewGroup viewGroup, final View view, final TransitionValues transitionValues, final TransitionValues transitionValues2) {
        if (transitionValues2 == null) {
            return null;
        }
        final Rect rect = transitionValues2.values.get("android:explode:screenBounds");
        final float translationX = view.getTranslationX();
        final float translationY = view.getTranslationY();
        this.calculateOut((View)viewGroup, rect, this.mTempLoc);
        final int[] mTempLoc = this.mTempLoc;
        return TranslationAnimationCreator.createAnimation(view, transitionValues2, rect.left, rect.top, translationX + mTempLoc[0], translationY + mTempLoc[1], translationX, translationY, Explode.sDecelerate);
    }
    
    @Override
    public Animator onDisappear(final ViewGroup viewGroup, final View view, final TransitionValues transitionValues, final TransitionValues transitionValues2) {
        if (transitionValues == null) {
            return null;
        }
        final Rect rect = transitionValues.values.get("android:explode:screenBounds");
        final int left = rect.left;
        final int top = rect.top;
        final float translationX = view.getTranslationX();
        final float translationY = view.getTranslationY();
        float n = translationX;
        float n2 = translationY;
        final int[] array = (int[])transitionValues.view.getTag(R.id.transition_position);
        if (array != null) {
            n += array[0] - rect.left;
            n2 += array[1] - rect.top;
            rect.offsetTo(array[0], array[1]);
        }
        this.calculateOut((View)viewGroup, rect, this.mTempLoc);
        final int[] mTempLoc = this.mTempLoc;
        return TranslationAnimationCreator.createAnimation(view, transitionValues, left, top, translationX, translationY, n + mTempLoc[0], n2 + mTempLoc[1], Explode.sAccelerate);
    }
}
