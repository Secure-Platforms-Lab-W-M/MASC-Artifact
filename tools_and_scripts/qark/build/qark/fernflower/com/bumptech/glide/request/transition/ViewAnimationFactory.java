package com.bumptech.glide.request.transition;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.bumptech.glide.load.DataSource;

public class ViewAnimationFactory implements TransitionFactory {
   private Transition transition;
   private final ViewTransition.ViewTransitionAnimationFactory viewTransitionAnimationFactory;

   public ViewAnimationFactory(int var1) {
      this((ViewTransition.ViewTransitionAnimationFactory)(new ViewAnimationFactory.ResourceViewTransitionAnimationFactory(var1)));
   }

   public ViewAnimationFactory(Animation var1) {
      this((ViewTransition.ViewTransitionAnimationFactory)(new ViewAnimationFactory.ConcreteViewTransitionAnimationFactory(var1)));
   }

   ViewAnimationFactory(ViewTransition.ViewTransitionAnimationFactory var1) {
      this.viewTransitionAnimationFactory = var1;
   }

   public Transition build(DataSource var1, boolean var2) {
      if (var1 != DataSource.MEMORY_CACHE && var2) {
         if (this.transition == null) {
            this.transition = new ViewTransition(this.viewTransitionAnimationFactory);
         }

         return this.transition;
      } else {
         return NoTransition.get();
      }
   }

   private static class ConcreteViewTransitionAnimationFactory implements ViewTransition.ViewTransitionAnimationFactory {
      private final Animation animation;

      ConcreteViewTransitionAnimationFactory(Animation var1) {
         this.animation = var1;
      }

      public Animation build(Context var1) {
         return this.animation;
      }
   }

   private static class ResourceViewTransitionAnimationFactory implements ViewTransition.ViewTransitionAnimationFactory {
      private final int animationId;

      ResourceViewTransitionAnimationFactory(int var1) {
         this.animationId = var1;
      }

      public Animation build(Context var1) {
         return AnimationUtils.loadAnimation(var1, this.animationId);
      }
   }
}
