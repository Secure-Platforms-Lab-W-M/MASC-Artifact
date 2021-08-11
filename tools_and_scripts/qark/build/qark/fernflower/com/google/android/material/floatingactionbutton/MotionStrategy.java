package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Animator.AnimatorListener;
import com.google.android.material.animation.MotionSpec;
import java.util.List;

interface MotionStrategy {
   void addAnimationListener(AnimatorListener var1);

   AnimatorSet createAnimator();

   MotionSpec getCurrentMotionSpec();

   int getDefaultMotionSpecResource();

   List getListeners();

   MotionSpec getMotionSpec();

   void onAnimationCancel();

   void onAnimationEnd();

   void onAnimationStart(Animator var1);

   void onChange(ExtendedFloatingActionButton.OnChangedCallback var1);

   void performNow();

   void removeAnimationListener(AnimatorListener var1);

   void setMotionSpec(MotionSpec var1);

   boolean shouldCancel();
}
