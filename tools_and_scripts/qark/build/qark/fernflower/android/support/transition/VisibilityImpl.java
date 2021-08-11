package android.support.transition;

import android.animation.Animator;
import android.view.ViewGroup;

interface VisibilityImpl {
   boolean isVisible(TransitionValues var1);

   Animator onAppear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5);

   Animator onDisappear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5);
}
