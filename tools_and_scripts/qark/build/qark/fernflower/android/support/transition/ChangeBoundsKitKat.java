package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

@TargetApi(19)
@RequiresApi(19)
class ChangeBoundsKitKat extends TransitionKitKat implements ChangeBoundsInterface {
   public ChangeBoundsKitKat(TransitionInterface var1) {
      this.init(var1, new android.transition.ChangeBounds());
   }

   public void setResizeClip(boolean var1) {
      ((android.transition.ChangeBounds)this.mTransition).setResizeClip(var1);
   }
}
