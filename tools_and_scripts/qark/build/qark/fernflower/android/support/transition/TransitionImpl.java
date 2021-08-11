package android.support.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

abstract class TransitionImpl {
   public abstract TransitionImpl addListener(TransitionInterfaceListener var1);

   public abstract TransitionImpl addTarget(int var1);

   public abstract TransitionImpl addTarget(View var1);

   public abstract void captureEndValues(TransitionValues var1);

   public abstract void captureStartValues(TransitionValues var1);

   public abstract Animator createAnimator(ViewGroup var1, TransitionValues var2, TransitionValues var3);

   public abstract TransitionImpl excludeChildren(int var1, boolean var2);

   public abstract TransitionImpl excludeChildren(View var1, boolean var2);

   public abstract TransitionImpl excludeChildren(Class var1, boolean var2);

   public abstract TransitionImpl excludeTarget(int var1, boolean var2);

   public abstract TransitionImpl excludeTarget(View var1, boolean var2);

   public abstract TransitionImpl excludeTarget(Class var1, boolean var2);

   public abstract long getDuration();

   public abstract TimeInterpolator getInterpolator();

   public abstract String getName();

   public abstract long getStartDelay();

   public abstract List getTargetIds();

   public abstract List getTargets();

   public abstract String[] getTransitionProperties();

   public abstract TransitionValues getTransitionValues(View var1, boolean var2);

   public void init(TransitionInterface var1) {
      this.init(var1, (Object)null);
   }

   public abstract void init(TransitionInterface var1, Object var2);

   public abstract TransitionImpl removeListener(TransitionInterfaceListener var1);

   public abstract TransitionImpl removeTarget(int var1);

   public abstract TransitionImpl removeTarget(View var1);

   public abstract TransitionImpl setDuration(long var1);

   public abstract TransitionImpl setInterpolator(TimeInterpolator var1);

   public abstract TransitionImpl setStartDelay(long var1);
}
