package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

@TargetApi(14)
@RequiresApi(14)
class TransitionSetIcs extends TransitionIcs implements TransitionSetImpl {
   private TransitionSetPort mTransitionSet = new TransitionSetPort();

   public TransitionSetIcs(TransitionInterface var1) {
      this.init(var1, this.mTransitionSet);
   }

   public TransitionSetIcs addTransition(TransitionImpl var1) {
      this.mTransitionSet.addTransition(((TransitionIcs)var1).mTransition);
      return this;
   }

   public int getOrdering() {
      return this.mTransitionSet.getOrdering();
   }

   public TransitionSetIcs removeTransition(TransitionImpl var1) {
      this.mTransitionSet.removeTransition(((TransitionIcs)var1).mTransition);
      return this;
   }

   public TransitionSetIcs setOrdering(int var1) {
      this.mTransitionSet.setOrdering(var1);
      return this;
   }
}
