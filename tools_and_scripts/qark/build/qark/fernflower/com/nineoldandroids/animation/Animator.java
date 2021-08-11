package com.nineoldandroids.animation;

import android.view.animation.Interpolator;
import java.util.ArrayList;

public abstract class Animator implements Cloneable {
   ArrayList mListeners = null;

   public void addListener(Animator.AnimatorListener var1) {
      if (this.mListeners == null) {
         this.mListeners = new ArrayList();
      }

      this.mListeners.add(var1);
   }

   public void cancel() {
   }

   public Animator clone() {
      // $FF: Couldn't be decompiled
   }

   public void end() {
   }

   public abstract long getDuration();

   public ArrayList getListeners() {
      return this.mListeners;
   }

   public abstract long getStartDelay();

   public abstract boolean isRunning();

   public boolean isStarted() {
      return this.isRunning();
   }

   public void removeAllListeners() {
      ArrayList var1 = this.mListeners;
      if (var1 != null) {
         var1.clear();
         this.mListeners = null;
      }

   }

   public void removeListener(Animator.AnimatorListener var1) {
      ArrayList var2 = this.mListeners;
      if (var2 != null) {
         var2.remove(var1);
         if (this.mListeners.size() == 0) {
            this.mListeners = null;
         }

      }
   }

   public abstract Animator setDuration(long var1);

   public abstract void setInterpolator(Interpolator var1);

   public abstract void setStartDelay(long var1);

   public void setTarget(Object var1) {
   }

   public void setupEndValues() {
   }

   public void setupStartValues() {
   }

   public void start() {
   }

   public interface AnimatorListener {
      void onAnimationCancel(Animator var1);

      void onAnimationEnd(Animator var1);

      void onAnimationRepeat(Animator var1);

      void onAnimationStart(Animator var1);
   }
}
