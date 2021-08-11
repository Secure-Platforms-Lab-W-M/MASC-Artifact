/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.TextView
 */
package android.support.design.internal;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Map;

@RequiresApi(value=14)
@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class TextScale
extends Transition {
    private static final String PROPNAME_SCALE = "android:textscale:scale";

    private void captureValues(TransitionValues transitionValues) {
        if (transitionValues.view instanceof TextView) {
            TextView textView = (TextView)transitionValues.view;
            transitionValues.values.put("android:textscale:scale", Float.valueOf(textView.getScaleX()));
            return;
        }
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    @Override
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues object, TransitionValues object2) {
        if (object != null && object2 != null && object.view instanceof TextView) {
            if (!(object2.view instanceof TextView)) {
                return null;
            }
            viewGroup = (TextView)object2.view;
            object = object.values;
            object2 = object2.values;
            Object v = object.get("android:textscale:scale");
            float f = 1.0f;
            float f2 = v != null ? ((Float)object.get("android:textscale:scale")).floatValue() : 1.0f;
            if (object2.get("android:textscale:scale") != null) {
                f = ((Float)object2.get("android:textscale:scale")).floatValue();
            }
            if (f2 == f) {
                return null;
            }
            object = ValueAnimator.ofFloat((float[])new float[]{f2, f});
            object.addUpdateListener(new ValueAnimator.AnimatorUpdateListener((TextView)viewGroup){
                final /* synthetic */ TextView val$view;
                {
                    this.val$view = textView;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float f = ((Float)valueAnimator.getAnimatedValue()).floatValue();
                    this.val$view.setScaleX(f);
                    this.val$view.setScaleY(f);
                }
            });
            return object;
        }
        return null;
    }

}

