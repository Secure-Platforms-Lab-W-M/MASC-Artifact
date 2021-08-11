package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

@TargetApi(14)
@RequiresApi(14)
class AutoTransitionPort extends TransitionSetPort {
   public AutoTransitionPort() {
      this.setOrdering(1);
      this.addTransition(new FadePort(2)).addTransition(new ChangeBoundsPort()).addTransition(new FadePort(1));
   }
}
