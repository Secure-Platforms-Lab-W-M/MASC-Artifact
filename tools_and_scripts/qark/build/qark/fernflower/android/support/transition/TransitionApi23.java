package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

@TargetApi(23)
@RequiresApi(23)
class TransitionApi23 extends TransitionKitKat {
   public TransitionImpl removeTarget(int var1) {
      this.mTransition.removeTarget(var1);
      return this;
   }
}
