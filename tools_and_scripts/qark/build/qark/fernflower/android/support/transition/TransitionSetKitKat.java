package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

@TargetApi(19)
@RequiresApi(19)
class TransitionSetKitKat extends TransitionKitKat implements TransitionSetImpl {
   private android.transition.TransitionSet mTransitionSet = new android.transition.TransitionSet();

   public TransitionSetKitKat(TransitionInterface var1) {
      this.init(var1, this.mTransitionSet);
   }

   public TransitionSetKitKat addTransition(TransitionImpl var1) {
      this.mTransitionSet.addTransition(((TransitionKitKat)var1).mTransition);
      return this;
   }

   public int getOrdering() {
      return this.mTransitionSet.getOrdering();
   }

   public TransitionSetKitKat removeTransition(TransitionImpl var1) {
      this.mTransitionSet.removeTransition(((TransitionKitKat)var1).mTransition);
      return this;
   }

   public TransitionSetKitKat setOrdering(int var1) {
      this.mTransitionSet.setOrdering(var1);
      return this;
   }
}
