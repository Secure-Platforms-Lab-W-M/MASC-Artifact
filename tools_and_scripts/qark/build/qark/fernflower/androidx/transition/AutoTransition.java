package androidx.transition;

import android.content.Context;
import android.util.AttributeSet;

public class AutoTransition extends TransitionSet {
   public AutoTransition() {
      this.init();
   }

   public AutoTransition(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init();
   }

   private void init() {
      this.setOrdering(1);
      this.addTransition(new Fade(2)).addTransition(new ChangeBounds()).addTransition(new Fade(1));
   }
}
