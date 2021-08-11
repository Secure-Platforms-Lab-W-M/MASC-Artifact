package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

@TargetApi(14)
@RequiresApi(14)
class ChangeBoundsIcs extends TransitionIcs implements ChangeBoundsInterface {
   public ChangeBoundsIcs(TransitionInterface var1) {
      this.init(var1, new ChangeBoundsPort());
   }

   public void setResizeClip(boolean var1) {
      ((ChangeBoundsPort)this.mTransition).setResizeClip(var1);
   }
}
