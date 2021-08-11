package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.view.View;
import androidx.core.util.Preconditions;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.animation.MotionSpec;
import java.util.ArrayList;
import java.util.List;

abstract class BaseMotionStrategy implements MotionStrategy {
   private final Context context;
   private MotionSpec defaultMotionSpec;
   private final ExtendedFloatingActionButton fab;
   private final ArrayList listeners = new ArrayList();
   private MotionSpec motionSpec;
   private final AnimatorTracker tracker;

   BaseMotionStrategy(ExtendedFloatingActionButton var1, AnimatorTracker var2) {
      this.fab = var1;
      this.context = var1.getContext();
      this.tracker = var2;
   }

   public final void addAnimationListener(AnimatorListener var1) {
      this.listeners.add(var1);
   }

   public AnimatorSet createAnimator() {
      return this.createAnimator(this.getCurrentMotionSpec());
   }

   AnimatorSet createAnimator(MotionSpec var1) {
      ArrayList var2 = new ArrayList();
      if (var1.hasPropertyValues("opacity")) {
         var2.add(var1.getAnimator("opacity", this.fab, View.ALPHA));
      }

      if (var1.hasPropertyValues("scale")) {
         var2.add(var1.getAnimator("scale", this.fab, View.SCALE_Y));
         var2.add(var1.getAnimator("scale", this.fab, View.SCALE_X));
      }

      if (var1.hasPropertyValues("width")) {
         var2.add(var1.getAnimator("width", this.fab, ExtendedFloatingActionButton.WIDTH));
      }

      if (var1.hasPropertyValues("height")) {
         var2.add(var1.getAnimator("height", this.fab, ExtendedFloatingActionButton.HEIGHT));
      }

      AnimatorSet var3 = new AnimatorSet();
      AnimatorSetCompat.playTogether(var3, var2);
      return var3;
   }

   public final MotionSpec getCurrentMotionSpec() {
      MotionSpec var1 = this.motionSpec;
      if (var1 != null) {
         return var1;
      } else {
         if (this.defaultMotionSpec == null) {
            this.defaultMotionSpec = MotionSpec.createFromResource(this.context, this.getDefaultMotionSpecResource());
         }

         return (MotionSpec)Preconditions.checkNotNull(this.defaultMotionSpec);
      }
   }

   public final List getListeners() {
      return this.listeners;
   }

   public MotionSpec getMotionSpec() {
      return this.motionSpec;
   }

   public void onAnimationCancel() {
      this.tracker.clear();
   }

   public void onAnimationEnd() {
      this.tracker.clear();
   }

   public void onAnimationStart(Animator var1) {
      this.tracker.onNextAnimationStart(var1);
   }

   public final void removeAnimationListener(AnimatorListener var1) {
      this.listeners.remove(var1);
   }

   public final void setMotionSpec(MotionSpec var1) {
      this.motionSpec = var1;
   }
}
