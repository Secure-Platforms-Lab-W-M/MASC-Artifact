package android.support.transition;

import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;

@TargetApi(14)
@RequiresApi(14)
class TransitionSetPort extends TransitionPort {
   public static final int ORDERING_SEQUENTIAL = 1;
   public static final int ORDERING_TOGETHER = 0;
   int mCurrentListeners;
   private boolean mPlayTogether = true;
   boolean mStarted = false;
   ArrayList mTransitions = new ArrayList();

   public TransitionSetPort() {
   }

   private void setupStartEndListeners() {
      TransitionSetPort.TransitionSetListener var1 = new TransitionSetPort.TransitionSetListener(this);
      Iterator var2 = this.mTransitions.iterator();

      while(var2.hasNext()) {
         ((TransitionPort)var2.next()).addListener(var1);
      }

      this.mCurrentListeners = this.mTransitions.size();
   }

   public TransitionSetPort addListener(TransitionPort.TransitionListener var1) {
      return (TransitionSetPort)super.addListener(var1);
   }

   public TransitionSetPort addTarget(int var1) {
      return (TransitionSetPort)super.addTarget(var1);
   }

   public TransitionSetPort addTarget(View var1) {
      return (TransitionSetPort)super.addTarget(var1);
   }

   public TransitionSetPort addTransition(TransitionPort var1) {
      if (var1 != null) {
         this.mTransitions.add(var1);
         var1.mParent = this;
         if (this.mDuration >= 0L) {
            var1.setDuration(this.mDuration);
         }
      }

      return this;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected void cancel() {
      super.cancel();
      int var2 = this.mTransitions.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         ((TransitionPort)this.mTransitions.get(var1)).cancel();
      }

   }

   public void captureEndValues(TransitionValues var1) {
      int var2 = var1.view.getId();
      if (this.isValidTarget(var1.view, (long)var2)) {
         Iterator var3 = this.mTransitions.iterator();

         while(var3.hasNext()) {
            TransitionPort var4 = (TransitionPort)var3.next();
            if (var4.isValidTarget(var1.view, (long)var2)) {
               var4.captureEndValues(var1);
            }
         }
      }

   }

   public void captureStartValues(TransitionValues var1) {
      int var2 = var1.view.getId();
      if (this.isValidTarget(var1.view, (long)var2)) {
         Iterator var3 = this.mTransitions.iterator();

         while(var3.hasNext()) {
            TransitionPort var4 = (TransitionPort)var3.next();
            if (var4.isValidTarget(var1.view, (long)var2)) {
               var4.captureStartValues(var1);
            }
         }
      }

   }

   public TransitionSetPort clone() {
      TransitionSetPort var3 = (TransitionSetPort)super.clone();
      var3.mTransitions = new ArrayList();
      int var2 = this.mTransitions.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         var3.addTransition(((TransitionPort)this.mTransitions.get(var1)).clone());
      }

      return var3;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected void createAnimators(ViewGroup var1, TransitionValuesMaps var2, TransitionValuesMaps var3) {
      Iterator var4 = this.mTransitions.iterator();

      while(var4.hasNext()) {
         ((TransitionPort)var4.next()).createAnimators(var1, var2, var3);
      }

   }

   public int getOrdering() {
      return this.mPlayTogether ? 0 : 1;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void pause(View var1) {
      super.pause(var1);
      int var3 = this.mTransitions.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((TransitionPort)this.mTransitions.get(var2)).pause(var1);
      }

   }

   public TransitionSetPort removeListener(TransitionPort.TransitionListener var1) {
      return (TransitionSetPort)super.removeListener(var1);
   }

   public TransitionSetPort removeTarget(int var1) {
      return (TransitionSetPort)super.removeTarget(var1);
   }

   public TransitionSetPort removeTarget(View var1) {
      return (TransitionSetPort)super.removeTarget(var1);
   }

   public TransitionSetPort removeTransition(TransitionPort var1) {
      this.mTransitions.remove(var1);
      var1.mParent = null;
      return this;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void resume(View var1) {
      super.resume(var1);
      int var3 = this.mTransitions.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((TransitionPort)this.mTransitions.get(var2)).resume(var1);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected void runAnimators() {
      if (this.mTransitions.isEmpty()) {
         this.start();
         this.end();
      } else {
         this.setupStartEndListeners();
         if (!this.mPlayTogether) {
            for(int var1 = 1; var1 < this.mTransitions.size(); ++var1) {
               ((TransitionPort)this.mTransitions.get(var1 - 1)).addListener(new TransitionPort.TransitionListenerAdapter((TransitionPort)this.mTransitions.get(var1)) {
                  // $FF: synthetic field
                  final TransitionPort val$nextTransition;

                  {
                     this.val$nextTransition = var2;
                  }

                  public void onTransitionEnd(TransitionPort var1) {
                     this.val$nextTransition.runAnimators();
                     var1.removeListener(this);
                  }
               });
            }

            TransitionPort var2 = (TransitionPort)this.mTransitions.get(0);
            if (var2 != null) {
               var2.runAnimators();
               return;
            }
         } else {
            Iterator var3 = this.mTransitions.iterator();

            while(var3.hasNext()) {
               ((TransitionPort)var3.next()).runAnimators();
            }
         }
      }

   }

   void setCanRemoveViews(boolean var1) {
      super.setCanRemoveViews(var1);
      int var3 = this.mTransitions.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((TransitionPort)this.mTransitions.get(var2)).setCanRemoveViews(var1);
      }

   }

   public TransitionSetPort setDuration(long var1) {
      super.setDuration(var1);
      if (this.mDuration >= 0L) {
         int var4 = this.mTransitions.size();

         for(int var3 = 0; var3 < var4; ++var3) {
            ((TransitionPort)this.mTransitions.get(var3)).setDuration(var1);
         }
      }

      return this;
   }

   public TransitionSetPort setInterpolator(TimeInterpolator var1) {
      return (TransitionSetPort)super.setInterpolator(var1);
   }

   public TransitionSetPort setOrdering(int var1) {
      switch(var1) {
      case 0:
         this.mPlayTogether = true;
         return this;
      case 1:
         this.mPlayTogether = false;
         return this;
      default:
         throw new AndroidRuntimeException("Invalid parameter for TransitionSet ordering: " + var1);
      }
   }

   TransitionSetPort setSceneRoot(ViewGroup var1) {
      super.setSceneRoot(var1);
      int var3 = this.mTransitions.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((TransitionPort)this.mTransitions.get(var2)).setSceneRoot(var1);
      }

      return this;
   }

   public TransitionSetPort setStartDelay(long var1) {
      return (TransitionSetPort)super.setStartDelay(var1);
   }

   String toString(String var1) {
      String var3 = super.toString(var1);

      for(int var2 = 0; var2 < this.mTransitions.size(); ++var2) {
         var3 = var3 + "\n" + ((TransitionPort)this.mTransitions.get(var2)).toString(var1 + "  ");
      }

      return var3;
   }

   static class TransitionSetListener extends TransitionPort.TransitionListenerAdapter {
      TransitionSetPort mTransitionSet;

      TransitionSetListener(TransitionSetPort var1) {
         this.mTransitionSet = var1;
      }

      public void onTransitionEnd(TransitionPort var1) {
         TransitionSetPort var2 = this.mTransitionSet;
         --var2.mCurrentListeners;
         if (this.mTransitionSet.mCurrentListeners == 0) {
            this.mTransitionSet.mStarted = false;
            this.mTransitionSet.end();
         }

         var1.removeListener(this);
      }

      public void onTransitionStart(TransitionPort var1) {
         if (!this.mTransitionSet.mStarted) {
            this.mTransitionSet.start();
            this.mTransitionSet.mStarted = true;
         }

      }
   }
}
