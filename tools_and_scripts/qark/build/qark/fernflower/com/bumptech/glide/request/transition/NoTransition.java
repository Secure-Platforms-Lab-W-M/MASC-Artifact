package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;

public class NoTransition implements Transition {
   static final NoTransition NO_ANIMATION = new NoTransition();
   private static final TransitionFactory NO_ANIMATION_FACTORY = new NoTransition.NoAnimationFactory();

   public static Transition get() {
      return NO_ANIMATION;
   }

   public static TransitionFactory getFactory() {
      return NO_ANIMATION_FACTORY;
   }

   public boolean transition(Object var1, Transition.ViewAdapter var2) {
      return false;
   }

   public static class NoAnimationFactory implements TransitionFactory {
      public Transition build(DataSource var1, boolean var2) {
         return NoTransition.NO_ANIMATION;
      }
   }
}
