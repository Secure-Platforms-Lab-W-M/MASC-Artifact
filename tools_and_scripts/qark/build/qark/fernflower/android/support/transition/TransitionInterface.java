package android.support.transition;

import android.animation.Animator;
import android.view.ViewGroup;

interface TransitionInterface {
   void captureEndValues(TransitionValues var1);

   void captureStartValues(TransitionValues var1);

   Animator createAnimator(ViewGroup var1, TransitionValues var2, TransitionValues var3);
}
