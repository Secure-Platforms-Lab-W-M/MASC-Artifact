package android.support.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TargetApi(14)
@RequiresApi(14)
class TransitionIcs extends TransitionImpl {
   private TransitionIcs.CompatListener mCompatListener;
   TransitionInterface mExternalTransition;
   TransitionPort mTransition;

   public TransitionImpl addListener(TransitionInterfaceListener var1) {
      if (this.mCompatListener == null) {
         this.mCompatListener = new TransitionIcs.CompatListener();
         this.mTransition.addListener(this.mCompatListener);
      }

      this.mCompatListener.addListener(var1);
      return this;
   }

   public TransitionImpl addTarget(int var1) {
      this.mTransition.addTarget(var1);
      return this;
   }

   public TransitionImpl addTarget(View var1) {
      this.mTransition.addTarget(var1);
      return this;
   }

   public void captureEndValues(TransitionValues var1) {
      this.mTransition.captureEndValues(var1);
   }

   public void captureStartValues(TransitionValues var1) {
      this.mTransition.captureStartValues(var1);
   }

   public Animator createAnimator(ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      return this.mTransition.createAnimator(var1, var2, var3);
   }

   public TransitionImpl excludeChildren(int var1, boolean var2) {
      this.mTransition.excludeChildren(var1, var2);
      return this;
   }

   public TransitionImpl excludeChildren(View var1, boolean var2) {
      this.mTransition.excludeChildren(var1, var2);
      return this;
   }

   public TransitionImpl excludeChildren(Class var1, boolean var2) {
      this.mTransition.excludeChildren(var1, var2);
      return this;
   }

   public TransitionImpl excludeTarget(int var1, boolean var2) {
      this.mTransition.excludeTarget(var1, var2);
      return this;
   }

   public TransitionImpl excludeTarget(View var1, boolean var2) {
      this.mTransition.excludeTarget(var1, var2);
      return this;
   }

   public TransitionImpl excludeTarget(Class var1, boolean var2) {
      this.mTransition.excludeTarget(var1, var2);
      return this;
   }

   public long getDuration() {
      return this.mTransition.getDuration();
   }

   public TimeInterpolator getInterpolator() {
      return this.mTransition.getInterpolator();
   }

   public String getName() {
      return this.mTransition.getName();
   }

   public long getStartDelay() {
      return this.mTransition.getStartDelay();
   }

   public List getTargetIds() {
      return this.mTransition.getTargetIds();
   }

   public List getTargets() {
      return this.mTransition.getTargets();
   }

   public String[] getTransitionProperties() {
      return this.mTransition.getTransitionProperties();
   }

   public TransitionValues getTransitionValues(View var1, boolean var2) {
      return this.mTransition.getTransitionValues(var1, var2);
   }

   public void init(TransitionInterface var1, Object var2) {
      this.mExternalTransition = var1;
      if (var2 == null) {
         this.mTransition = new TransitionIcs.TransitionWrapper(var1);
      } else {
         this.mTransition = (TransitionPort)var2;
      }
   }

   public TransitionImpl removeListener(TransitionInterfaceListener var1) {
      if (this.mCompatListener != null) {
         this.mCompatListener.removeListener(var1);
         if (this.mCompatListener.isEmpty()) {
            this.mTransition.removeListener(this.mCompatListener);
            this.mCompatListener = null;
            return this;
         }
      }

      return this;
   }

   public TransitionImpl removeTarget(int var1) {
      this.mTransition.removeTarget(var1);
      return this;
   }

   public TransitionImpl removeTarget(View var1) {
      this.mTransition.removeTarget(var1);
      return this;
   }

   public TransitionImpl setDuration(long var1) {
      this.mTransition.setDuration(var1);
      return this;
   }

   public TransitionImpl setInterpolator(TimeInterpolator var1) {
      this.mTransition.setInterpolator(var1);
      return this;
   }

   public TransitionImpl setStartDelay(long var1) {
      this.mTransition.setStartDelay(var1);
      return this;
   }

   public String toString() {
      return this.mTransition.toString();
   }

   private class CompatListener implements TransitionPort.TransitionListener {
      private final ArrayList mListeners = new ArrayList();

      CompatListener() {
      }

      public void addListener(TransitionInterfaceListener var1) {
         this.mListeners.add(var1);
      }

      public boolean isEmpty() {
         return this.mListeners.isEmpty();
      }

      public void onTransitionCancel(TransitionPort var1) {
         Iterator var2 = this.mListeners.iterator();

         while(var2.hasNext()) {
            ((TransitionInterfaceListener)var2.next()).onTransitionCancel(TransitionIcs.this.mExternalTransition);
         }

      }

      public void onTransitionEnd(TransitionPort var1) {
         Iterator var2 = this.mListeners.iterator();

         while(var2.hasNext()) {
            ((TransitionInterfaceListener)var2.next()).onTransitionEnd(TransitionIcs.this.mExternalTransition);
         }

      }

      public void onTransitionPause(TransitionPort var1) {
         Iterator var2 = this.mListeners.iterator();

         while(var2.hasNext()) {
            ((TransitionInterfaceListener)var2.next()).onTransitionPause(TransitionIcs.this.mExternalTransition);
         }

      }

      public void onTransitionResume(TransitionPort var1) {
         Iterator var2 = this.mListeners.iterator();

         while(var2.hasNext()) {
            ((TransitionInterfaceListener)var2.next()).onTransitionResume(TransitionIcs.this.mExternalTransition);
         }

      }

      public void onTransitionStart(TransitionPort var1) {
         Iterator var2 = this.mListeners.iterator();

         while(var2.hasNext()) {
            ((TransitionInterfaceListener)var2.next()).onTransitionStart(TransitionIcs.this.mExternalTransition);
         }

      }

      public void removeListener(TransitionInterfaceListener var1) {
         this.mListeners.remove(var1);
      }
   }

   private static class TransitionWrapper extends TransitionPort {
      private TransitionInterface mTransition;

      public TransitionWrapper(TransitionInterface var1) {
         this.mTransition = var1;
      }

      public void captureEndValues(TransitionValues var1) {
         this.mTransition.captureEndValues(var1);
      }

      public void captureStartValues(TransitionValues var1) {
         this.mTransition.captureStartValues(var1);
      }

      public Animator createAnimator(ViewGroup var1, TransitionValues var2, TransitionValues var3) {
         return this.mTransition.createAnimator(var1, var2, var3);
      }
   }
}
