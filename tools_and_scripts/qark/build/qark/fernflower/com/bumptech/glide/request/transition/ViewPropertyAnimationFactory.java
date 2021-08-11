package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;

public class ViewPropertyAnimationFactory implements TransitionFactory {
   private ViewPropertyTransition animation;
   private final ViewPropertyTransition.Animator animator;

   public ViewPropertyAnimationFactory(ViewPropertyTransition.Animator var1) {
      this.animator = var1;
   }

   public Transition build(DataSource var1, boolean var2) {
      if (var1 != DataSource.MEMORY_CACHE && var2) {
         if (this.animation == null) {
            this.animation = new ViewPropertyTransition(this.animator);
         }

         return this.animation;
      } else {
         return NoTransition.get();
      }
   }
}
