package android.support.transition;

import android.view.ViewGroup;

public abstract class TransitionPropagation {
   public abstract void captureValues(TransitionValues var1);

   public abstract String[] getPropagationProperties();

   public abstract long getStartDelay(ViewGroup var1, Transition var2, TransitionValues var3, TransitionValues var4);
}
