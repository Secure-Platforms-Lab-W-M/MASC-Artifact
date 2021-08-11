package android.support.transition;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
class FadeIcs extends TransitionIcs implements VisibilityImpl {
   public FadeIcs(TransitionInterface var1) {
      this.init(var1, new FadePort());
   }

   public FadeIcs(TransitionInterface var1, int var2) {
      this.init(var1, new FadePort(var2));
   }

   public boolean isVisible(TransitionValues var1) {
      return ((FadePort)this.mTransition).isVisible(var1);
   }

   public Animator onAppear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
      return ((FadePort)this.mTransition).onAppear(var1, var2, var3, var4, var5);
   }

   public Animator onDisappear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
      return ((FadePort)this.mTransition).onDisappear(var1, var2, var3, var2, var3);
   }
}
