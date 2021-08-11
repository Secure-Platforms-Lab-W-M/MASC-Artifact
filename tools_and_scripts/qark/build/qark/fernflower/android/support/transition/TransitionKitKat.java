package android.support.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.transition.Transition.TransitionListener;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TargetApi(19)
@RequiresApi(19)
class TransitionKitKat extends TransitionImpl {
   private TransitionKitKat.CompatListener mCompatListener;
   TransitionInterface mExternalTransition;
   android.transition.Transition mTransition;

   static android.transition.TransitionValues convertToPlatform(TransitionValues var0) {
      if (var0 == null) {
         return null;
      } else {
         android.transition.TransitionValues var1 = new android.transition.TransitionValues();
         copyValues(var0, var1);
         return var1;
      }
   }

   static TransitionValues convertToSupport(android.transition.TransitionValues var0) {
      if (var0 == null) {
         return null;
      } else {
         TransitionValues var1 = new TransitionValues();
         copyValues(var0, var1);
         return var1;
      }
   }

   static void copyValues(TransitionValues var0, android.transition.TransitionValues var1) {
      if (var0 != null) {
         var1.view = var0.view;
         if (var0.values.size() > 0) {
            var1.values.putAll(var0.values);
            return;
         }
      }

   }

   static void copyValues(android.transition.TransitionValues var0, TransitionValues var1) {
      if (var0 != null) {
         var1.view = var0.view;
         if (var0.values.size() > 0) {
            var1.values.putAll(var0.values);
            return;
         }
      }

   }

   static void wrapCaptureEndValues(TransitionInterface var0, android.transition.TransitionValues var1) {
      TransitionValues var2 = new TransitionValues();
      copyValues(var1, var2);
      var0.captureEndValues(var2);
      copyValues(var2, var1);
   }

   static void wrapCaptureStartValues(TransitionInterface var0, android.transition.TransitionValues var1) {
      TransitionValues var2 = new TransitionValues();
      copyValues(var1, var2);
      var0.captureStartValues(var2);
      copyValues(var2, var1);
   }

   public TransitionImpl addListener(TransitionInterfaceListener var1) {
      if (this.mCompatListener == null) {
         this.mCompatListener = new TransitionKitKat.CompatListener();
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
      android.transition.TransitionValues var2 = new android.transition.TransitionValues();
      copyValues(var1, var2);
      this.mTransition.captureEndValues(var2);
      copyValues(var2, var1);
   }

   public void captureStartValues(TransitionValues var1) {
      android.transition.TransitionValues var2 = new android.transition.TransitionValues();
      copyValues(var1, var2);
      this.mTransition.captureStartValues(var2);
      copyValues(var2, var1);
   }

   public Animator createAnimator(ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      android.transition.TransitionValues var4;
      android.transition.TransitionValues var5;
      if (var2 != null) {
         var4 = new android.transition.TransitionValues();
         copyValues(var2, var4);
         var5 = var4;
      } else {
         var5 = null;
      }

      android.transition.TransitionValues var6;
      if (var3 != null) {
         var4 = new android.transition.TransitionValues();
         copyValues(var3, var4);
         var6 = var4;
      } else {
         var6 = null;
      }

      return this.mTransition.createAnimator(var1, var5, var6);
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
      TransitionValues var3 = new TransitionValues();
      copyValues(this.mTransition.getTransitionValues(var1, var2), var3);
      return var3;
   }

   public void init(TransitionInterface var1, Object var2) {
      this.mExternalTransition = var1;
      if (var2 == null) {
         this.mTransition = new TransitionKitKat.TransitionWrapper(var1);
      } else {
         this.mTransition = (android.transition.Transition)var2;
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
      if (var1 > 0) {
         this.getTargetIds().remove(var1);
      }

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

   private class CompatListener implements TransitionListener {
      private final ArrayList mListeners = new ArrayList();

      CompatListener() {
      }

      void addListener(TransitionInterfaceListener var1) {
         this.mListeners.add(var1);
      }

      boolean isEmpty() {
         return this.mListeners.isEmpty();
      }

      public void onTransitionCancel(android.transition.Transition var1) {
         Iterator var2 = this.mListeners.iterator();

         while(var2.hasNext()) {
            ((TransitionInterfaceListener)var2.next()).onTransitionCancel(TransitionKitKat.this.mExternalTransition);
         }

      }

      public void onTransitionEnd(android.transition.Transition var1) {
         Iterator var2 = this.mListeners.iterator();

         while(var2.hasNext()) {
            ((TransitionInterfaceListener)var2.next()).onTransitionEnd(TransitionKitKat.this.mExternalTransition);
         }

      }

      public void onTransitionPause(android.transition.Transition var1) {
         Iterator var2 = this.mListeners.iterator();

         while(var2.hasNext()) {
            ((TransitionInterfaceListener)var2.next()).onTransitionPause(TransitionKitKat.this.mExternalTransition);
         }

      }

      public void onTransitionResume(android.transition.Transition var1) {
         Iterator var2 = this.mListeners.iterator();

         while(var2.hasNext()) {
            ((TransitionInterfaceListener)var2.next()).onTransitionResume(TransitionKitKat.this.mExternalTransition);
         }

      }

      public void onTransitionStart(android.transition.Transition var1) {
         Iterator var2 = this.mListeners.iterator();

         while(var2.hasNext()) {
            ((TransitionInterfaceListener)var2.next()).onTransitionStart(TransitionKitKat.this.mExternalTransition);
         }

      }

      void removeListener(TransitionInterfaceListener var1) {
         this.mListeners.remove(var1);
      }
   }

   private static class TransitionWrapper extends android.transition.Transition {
      private TransitionInterface mTransition;

      public TransitionWrapper(TransitionInterface var1) {
         this.mTransition = var1;
      }

      public void captureEndValues(android.transition.TransitionValues var1) {
         TransitionKitKat.wrapCaptureEndValues(this.mTransition, var1);
      }

      public void captureStartValues(android.transition.TransitionValues var1) {
         TransitionKitKat.wrapCaptureStartValues(this.mTransition, var1);
      }

      public Animator createAnimator(ViewGroup var1, android.transition.TransitionValues var2, android.transition.TransitionValues var3) {
         return this.mTransition.createAnimator(var1, TransitionKitKat.convertToSupport(var2), TransitionKitKat.convertToSupport(var3));
      }
   }
}
