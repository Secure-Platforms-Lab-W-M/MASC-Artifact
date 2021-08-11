package com.bumptech.glide.request.transition;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

public class ViewTransition implements Transition {
   private final ViewTransition.ViewTransitionAnimationFactory viewTransitionAnimationFactory;

   ViewTransition(ViewTransition.ViewTransitionAnimationFactory var1) {
      this.viewTransitionAnimationFactory = var1;
   }

   public boolean transition(Object var1, Transition.ViewAdapter var2) {
      View var3 = var2.getView();
      if (var3 != null) {
         var3.clearAnimation();
         var3.startAnimation(this.viewTransitionAnimationFactory.build(var3.getContext()));
      }

      return false;
   }

   interface ViewTransitionAnimationFactory {
      Animation build(Context var1);
   }
}
