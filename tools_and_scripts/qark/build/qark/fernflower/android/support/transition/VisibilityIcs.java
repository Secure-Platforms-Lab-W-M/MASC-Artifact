package android.support.transition;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
class VisibilityIcs extends TransitionIcs implements VisibilityImpl {
   public void init(TransitionInterface var1, Object var2) {
      this.mExternalTransition = var1;
      if (var2 == null) {
         this.mTransition = new VisibilityIcs.VisibilityWrapper((VisibilityInterface)var1);
      } else {
         this.mTransition = (VisibilityPort)var2;
      }
   }

   public boolean isVisible(TransitionValues var1) {
      return ((VisibilityPort)this.mTransition).isVisible(var1);
   }

   public Animator onAppear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
      return ((VisibilityPort)this.mTransition).onAppear(var1, var2, var3, var4, var5);
   }

   public Animator onDisappear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
      return ((VisibilityPort)this.mTransition).onDisappear(var1, var2, var3, var4, var5);
   }

   private static class VisibilityWrapper extends VisibilityPort {
      private VisibilityInterface mVisibility;

      VisibilityWrapper(VisibilityInterface var1) {
         this.mVisibility = var1;
      }

      public void captureEndValues(TransitionValues var1) {
         this.mVisibility.captureEndValues(var1);
      }

      public void captureStartValues(TransitionValues var1) {
         this.mVisibility.captureStartValues(var1);
      }

      public Animator createAnimator(ViewGroup var1, TransitionValues var2, TransitionValues var3) {
         return this.mVisibility.createAnimator(var1, var2, var3);
      }

      public boolean isVisible(TransitionValues var1) {
         return this.mVisibility.isVisible(var1);
      }

      public Animator onAppear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
         return this.mVisibility.onAppear(var1, var2, var3, var4, var5);
      }

      public Animator onDisappear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
         return this.mVisibility.onDisappear(var1, var2, var3, var4, var5);
      }
   }
}
