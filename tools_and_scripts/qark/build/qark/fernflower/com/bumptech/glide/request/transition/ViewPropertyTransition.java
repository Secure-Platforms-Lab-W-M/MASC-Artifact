package com.bumptech.glide.request.transition;

import android.view.View;

public class ViewPropertyTransition implements Transition {
   private final ViewPropertyTransition.Animator animator;

   public ViewPropertyTransition(ViewPropertyTransition.Animator var1) {
      this.animator = var1;
   }

   public boolean transition(Object var1, Transition.ViewAdapter var2) {
      if (var2.getView() != null) {
         this.animator.animate(var2.getView());
      }

      return false;
   }

   public interface Animator {
      void animate(View var1);
   }
}
