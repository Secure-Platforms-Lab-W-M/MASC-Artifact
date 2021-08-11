package com.bumptech.glide;

import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.request.transition.ViewPropertyTransition;

public final class GenericTransitionOptions extends TransitionOptions {
   public static GenericTransitionOptions with(int var0) {
      return (GenericTransitionOptions)(new GenericTransitionOptions()).transition(var0);
   }

   public static GenericTransitionOptions with(TransitionFactory var0) {
      return (GenericTransitionOptions)(new GenericTransitionOptions()).transition(var0);
   }

   public static GenericTransitionOptions with(ViewPropertyTransition.Animator var0) {
      return (GenericTransitionOptions)(new GenericTransitionOptions()).transition(var0);
   }

   public static GenericTransitionOptions withNoTransition() {
      return (GenericTransitionOptions)(new GenericTransitionOptions()).dontTransition();
   }
}
