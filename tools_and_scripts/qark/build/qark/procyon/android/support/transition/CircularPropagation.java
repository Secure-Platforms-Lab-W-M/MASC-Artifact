// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.graphics.Rect;
import android.view.ViewGroup;

public class CircularPropagation extends VisibilityPropagation
{
    private float mPropagationSpeed;
    
    public CircularPropagation() {
        this.mPropagationSpeed = 3.0f;
    }
    
    private static float distance(float n, float n2, final float n3, final float n4) {
        n = n3 - n;
        n2 = n4 - n2;
        return (float)Math.sqrt(n * n + n2 * n2);
    }
    
    @Override
    public long getStartDelay(final ViewGroup viewGroup, final Transition transition, TransitionValues transitionValues, final TransitionValues transitionValues2) {
        if (transitionValues == null && transitionValues2 == null) {
            return 0L;
        }
        int n = 1;
        if (transitionValues2 != null && this.getViewVisibility(transitionValues) != 0) {
            transitionValues = transitionValues2;
        }
        else {
            n = -1;
        }
        final int viewX = this.getViewX(transitionValues);
        final int viewY = this.getViewY(transitionValues);
        final Rect epicenter = transition.getEpicenter();
        int n2;
        int n3;
        if (epicenter != null) {
            n2 = epicenter.centerX();
            n3 = epicenter.centerY();
        }
        else {
            final int[] array = new int[2];
            viewGroup.getLocationOnScreen(array);
            n2 = Math.round(array[0] + viewGroup.getWidth() / 2 + viewGroup.getTranslationX());
            n3 = Math.round(array[1] + viewGroup.getHeight() / 2 + viewGroup.getTranslationY());
        }
        final float n4 = distance((float)viewX, (float)viewY, (float)n2, (float)n3) / distance(0.0f, 0.0f, (float)viewGroup.getWidth(), (float)viewGroup.getHeight());
        long duration = transition.getDuration();
        if (duration < 0L) {
            duration = 300L;
        }
        return Math.round(n * duration / this.mPropagationSpeed * n4);
    }
    
    public void setPropagationSpeed(final float mPropagationSpeed) {
        if (mPropagationSpeed != 0.0f) {
            this.mPropagationSpeed = mPropagationSpeed;
            return;
        }
        throw new IllegalArgumentException("propagationSpeed may not be 0");
    }
}
