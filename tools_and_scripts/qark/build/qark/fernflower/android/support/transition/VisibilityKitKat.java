package android.support.transition;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(19)
@RequiresApi(19)
class VisibilityKitKat extends TransitionKitKat implements VisibilityImpl {
   public void init(TransitionInterface var1, Object var2) {
      this.mExternalTransition = var1;
      if (var2 == null) {
         this.mTransition = new VisibilityKitKat.VisibilityWrapper((VisibilityInterface)var1);
      } else {
         this.mTransition = (android.transition.Visibility)var2;
      }
   }

   public boolean isVisible(TransitionValues var1) {
      return ((android.transition.Visibility)this.mTransition).isVisible(convertToPlatform(var1));
   }

   public Animator onAppear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
      return ((android.transition.Visibility)this.mTransition).onAppear(var1, convertToPlatform(var2), var3, convertToPlatform(var4), var5);
   }

   public Animator onDisappear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
      return ((android.transition.Visibility)this.mTransition).onDisappear(var1, convertToPlatform(var2), var3, convertToPlatform(var4), var5);
   }

   private static class VisibilityWrapper extends android.transition.Visibility {
      private final VisibilityInterface mVisibility;

      VisibilityWrapper(VisibilityInterface var1) {
         this.mVisibility = var1;
      }

      public void captureEndValues(android.transition.TransitionValues var1) {
         TransitionKitKat.wrapCaptureEndValues(this.mVisibility, var1);
      }

      public void captureStartValues(android.transition.TransitionValues var1) {
         TransitionKitKat.wrapCaptureStartValues(this.mVisibility, var1);
      }

      public Animator createAnimator(ViewGroup var1, android.transition.TransitionValues var2, android.transition.TransitionValues var3) {
         return this.mVisibility.createAnimator(var1, TransitionKitKat.convertToSupport(var2), TransitionKitKat.convertToSupport(var3));
      }

      public boolean isVisible(android.transition.TransitionValues var1) {
         if (var1 == null) {
            return false;
         } else {
            TransitionValues var2 = new TransitionValues();
            TransitionKitKat.copyValues(var1, var2);
            return this.mVisibility.isVisible(var2);
         }
      }

      public Animator onAppear(ViewGroup var1, android.transition.TransitionValues var2, int var3, android.transition.TransitionValues var4, int var5) {
         return this.mVisibility.onAppear(var1, TransitionKitKat.convertToSupport(var2), var3, TransitionKitKat.convertToSupport(var4), var5);
      }

      public Animator onDisappear(ViewGroup var1, android.transition.TransitionValues var2, int var3, android.transition.TransitionValues var4, int var5) {
         return this.mVisibility.onDisappear(var1, TransitionKitKat.convertToSupport(var2), var3, TransitionKitKat.convertToSupport(var4), var5);
      }
   }
}
