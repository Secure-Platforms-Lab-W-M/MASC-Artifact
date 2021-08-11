package com.bumptech.glide;

import com.bumptech.glide.request.transition.NoTransition;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.request.transition.ViewAnimationFactory;
import com.bumptech.glide.request.transition.ViewPropertyAnimationFactory;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.bumptech.glide.util.Preconditions;

public abstract class TransitionOptions implements Cloneable {
   private TransitionFactory transitionFactory = NoTransition.getFactory();

   private TransitionOptions self() {
      return this;
   }

   public final TransitionOptions clone() {
      try {
         TransitionOptions var1 = (TransitionOptions)super.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   public final TransitionOptions dontTransition() {
      return this.transition(NoTransition.getFactory());
   }

   final TransitionFactory getTransitionFactory() {
      return this.transitionFactory;
   }

   public final TransitionOptions transition(int var1) {
      return this.transition((TransitionFactory)(new ViewAnimationFactory(var1)));
   }

   public final TransitionOptions transition(TransitionFactory var1) {
      this.transitionFactory = (TransitionFactory)Preconditions.checkNotNull(var1);
      return this.self();
   }

   public final TransitionOptions transition(ViewPropertyTransition.Animator var1) {
      return this.transition((TransitionFactory)(new ViewPropertyAnimationFactory(var1)));
   }
}
