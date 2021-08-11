package android.support.transition;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(19)
@RequiresApi(19)
class FadeKitKat extends TransitionKitKat implements VisibilityImpl {
   public FadeKitKat(TransitionInterface var1) {
      this.init(var1, new android.transition.Fade());
   }

   public FadeKitKat(TransitionInterface var1, int var2) {
      this.init(var1, new android.transition.Fade(var2));
   }

   public boolean isVisible(TransitionValues var1) {
      return ((android.transition.Fade)this.mTransition).isVisible(convertToPlatform(var1));
   }

   public Animator onAppear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
      return ((android.transition.Fade)this.mTransition).onAppear(var1, convertToPlatform(var2), var3, convertToPlatform(var4), var5);
   }

   public Animator onDisappear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
      return ((android.transition.Fade)this.mTransition).onDisappear(var1, convertToPlatform(var2), var3, convertToPlatform(var4), var5);
   }
}
