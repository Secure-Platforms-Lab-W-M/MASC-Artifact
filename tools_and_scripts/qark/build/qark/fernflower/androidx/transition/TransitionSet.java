package androidx.transition;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import java.util.ArrayList;
import java.util.Iterator;

public class TransitionSet extends Transition {
   private static final int FLAG_CHANGE_EPICENTER = 8;
   private static final int FLAG_CHANGE_INTERPOLATOR = 1;
   private static final int FLAG_CHANGE_PATH_MOTION = 4;
   private static final int FLAG_CHANGE_PROPAGATION = 2;
   public static final int ORDERING_SEQUENTIAL = 1;
   public static final int ORDERING_TOGETHER = 0;
   private int mChangeFlags = 0;
   int mCurrentListeners;
   private boolean mPlayTogether = true;
   boolean mStarted = false;
   private ArrayList mTransitions = new ArrayList();

   public TransitionSet() {
   }

   public TransitionSet(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var3 = var1.obtainStyledAttributes(var2, Styleable.TRANSITION_SET);
      this.setOrdering(TypedArrayUtils.getNamedInt(var3, (XmlResourceParser)var2, "transitionOrdering", 0, 0));
      var3.recycle();
   }

   private void addTransitionInternal(Transition var1) {
      this.mTransitions.add(var1);
      var1.mParent = this;
   }

   private void setupStartEndListeners() {
      TransitionSet.TransitionSetListener var1 = new TransitionSet.TransitionSetListener(this);
      Iterator var2 = this.mTransitions.iterator();

      while(var2.hasNext()) {
         ((Transition)var2.next()).addListener(var1);
      }

      this.mCurrentListeners = this.mTransitions.size();
   }

   public TransitionSet addListener(Transition.TransitionListener var1) {
      return (TransitionSet)super.addListener(var1);
   }

   public TransitionSet addTarget(int var1) {
      for(int var2 = 0; var2 < this.mTransitions.size(); ++var2) {
         ((Transition)this.mTransitions.get(var2)).addTarget(var1);
      }

      return (TransitionSet)super.addTarget(var1);
   }

   public TransitionSet addTarget(View var1) {
      for(int var2 = 0; var2 < this.mTransitions.size(); ++var2) {
         ((Transition)this.mTransitions.get(var2)).addTarget(var1);
      }

      return (TransitionSet)super.addTarget(var1);
   }

   public TransitionSet addTarget(Class var1) {
      for(int var2 = 0; var2 < this.mTransitions.size(); ++var2) {
         ((Transition)this.mTransitions.get(var2)).addTarget(var1);
      }

      return (TransitionSet)super.addTarget(var1);
   }

   public TransitionSet addTarget(String var1) {
      for(int var2 = 0; var2 < this.mTransitions.size(); ++var2) {
         ((Transition)this.mTransitions.get(var2)).addTarget(var1);
      }

      return (TransitionSet)super.addTarget(var1);
   }

   public TransitionSet addTransition(Transition var1) {
      this.addTransitionInternal(var1);
      if (this.mDuration >= 0L) {
         var1.setDuration(this.mDuration);
      }

      if ((this.mChangeFlags & 1) != 0) {
         var1.setInterpolator(this.getInterpolator());
      }

      if ((this.mChangeFlags & 2) != 0) {
         var1.setPropagation(this.getPropagation());
      }

      if ((this.mChangeFlags & 4) != 0) {
         var1.setPathMotion(this.getPathMotion());
      }

      if ((this.mChangeFlags & 8) != 0) {
         var1.setEpicenterCallback(this.getEpicenterCallback());
      }

      return this;
   }

   protected void cancel() {
      super.cancel();
      int var2 = this.mTransitions.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         ((Transition)this.mTransitions.get(var1)).cancel();
      }

   }

   public void captureEndValues(TransitionValues var1) {
      if (this.isValidTarget(var1.view)) {
         Iterator var2 = this.mTransitions.iterator();

         while(var2.hasNext()) {
            Transition var3 = (Transition)var2.next();
            if (var3.isValidTarget(var1.view)) {
               var3.captureEndValues(var1);
               var1.mTargetedTransitions.add(var3);
            }
         }
      }

   }

   void capturePropagationValues(TransitionValues var1) {
      super.capturePropagationValues(var1);
      int var3 = this.mTransitions.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((Transition)this.mTransitions.get(var2)).capturePropagationValues(var1);
      }

   }

   public void captureStartValues(TransitionValues var1) {
      if (this.isValidTarget(var1.view)) {
         Iterator var2 = this.mTransitions.iterator();

         while(var2.hasNext()) {
            Transition var3 = (Transition)var2.next();
            if (var3.isValidTarget(var1.view)) {
               var3.captureStartValues(var1);
               var1.mTargetedTransitions.add(var3);
            }
         }
      }

   }

   public Transition clone() {
      TransitionSet var3 = (TransitionSet)super.clone();
      var3.mTransitions = new ArrayList();
      int var2 = this.mTransitions.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         var3.addTransitionInternal(((Transition)this.mTransitions.get(var1)).clone());
      }

      return var3;
   }

   protected void createAnimators(ViewGroup var1, TransitionValuesMaps var2, TransitionValuesMaps var3, ArrayList var4, ArrayList var5) {
      long var8 = this.getStartDelay();
      int var7 = this.mTransitions.size();

      for(int var6 = 0; var6 < var7; ++var6) {
         Transition var12 = (Transition)this.mTransitions.get(var6);
         if (var8 > 0L && (this.mPlayTogether || var6 == 0)) {
            long var10 = var12.getStartDelay();
            if (var10 > 0L) {
               var12.setStartDelay(var8 + var10);
            } else {
               var12.setStartDelay(var8);
            }
         }

         var12.createAnimators(var1, var2, var3, var4, var5);
      }

   }

   public Transition excludeTarget(int var1, boolean var2) {
      for(int var3 = 0; var3 < this.mTransitions.size(); ++var3) {
         ((Transition)this.mTransitions.get(var3)).excludeTarget(var1, var2);
      }

      return super.excludeTarget(var1, var2);
   }

   public Transition excludeTarget(View var1, boolean var2) {
      for(int var3 = 0; var3 < this.mTransitions.size(); ++var3) {
         ((Transition)this.mTransitions.get(var3)).excludeTarget(var1, var2);
      }

      return super.excludeTarget(var1, var2);
   }

   public Transition excludeTarget(Class var1, boolean var2) {
      for(int var3 = 0; var3 < this.mTransitions.size(); ++var3) {
         ((Transition)this.mTransitions.get(var3)).excludeTarget(var1, var2);
      }

      return super.excludeTarget(var1, var2);
   }

   public Transition excludeTarget(String var1, boolean var2) {
      for(int var3 = 0; var3 < this.mTransitions.size(); ++var3) {
         ((Transition)this.mTransitions.get(var3)).excludeTarget(var1, var2);
      }

      return super.excludeTarget(var1, var2);
   }

   void forceToEnd(ViewGroup var1) {
      super.forceToEnd(var1);
      int var3 = this.mTransitions.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((Transition)this.mTransitions.get(var2)).forceToEnd(var1);
      }

   }

   public int getOrdering() {
      return this.mPlayTogether ^ 1;
   }

   public Transition getTransitionAt(int var1) {
      return var1 >= 0 && var1 < this.mTransitions.size() ? (Transition)this.mTransitions.get(var1) : null;
   }

   public int getTransitionCount() {
      return this.mTransitions.size();
   }

   public void pause(View var1) {
      super.pause(var1);
      int var3 = this.mTransitions.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((Transition)this.mTransitions.get(var2)).pause(var1);
      }

   }

   public TransitionSet removeListener(Transition.TransitionListener var1) {
      return (TransitionSet)super.removeListener(var1);
   }

   public TransitionSet removeTarget(int var1) {
      for(int var2 = 0; var2 < this.mTransitions.size(); ++var2) {
         ((Transition)this.mTransitions.get(var2)).removeTarget(var1);
      }

      return (TransitionSet)super.removeTarget(var1);
   }

   public TransitionSet removeTarget(View var1) {
      for(int var2 = 0; var2 < this.mTransitions.size(); ++var2) {
         ((Transition)this.mTransitions.get(var2)).removeTarget(var1);
      }

      return (TransitionSet)super.removeTarget(var1);
   }

   public TransitionSet removeTarget(Class var1) {
      for(int var2 = 0; var2 < this.mTransitions.size(); ++var2) {
         ((Transition)this.mTransitions.get(var2)).removeTarget(var1);
      }

      return (TransitionSet)super.removeTarget(var1);
   }

   public TransitionSet removeTarget(String var1) {
      for(int var2 = 0; var2 < this.mTransitions.size(); ++var2) {
         ((Transition)this.mTransitions.get(var2)).removeTarget(var1);
      }

      return (TransitionSet)super.removeTarget(var1);
   }

   public TransitionSet removeTransition(Transition var1) {
      this.mTransitions.remove(var1);
      var1.mParent = null;
      return this;
   }

   public void resume(View var1) {
      super.resume(var1);
      int var3 = this.mTransitions.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((Transition)this.mTransitions.get(var2)).resume(var1);
      }

   }

   protected void runAnimators() {
      if (this.mTransitions.isEmpty()) {
         this.start();
         this.end();
      } else {
         this.setupStartEndListeners();
         if (!this.mPlayTogether) {
            for(int var1 = 1; var1 < this.mTransitions.size(); ++var1) {
               ((Transition)this.mTransitions.get(var1 - 1)).addListener(new TransitionListenerAdapter((Transition)this.mTransitions.get(var1)) {
                  // $FF: synthetic field
                  final Transition val$nextTransition;

                  {
                     this.val$nextTransition = var2;
                  }

                  public void onTransitionEnd(Transition var1) {
                     this.val$nextTransition.runAnimators();
                     var1.removeListener(this);
                  }
               });
            }

            Transition var3 = (Transition)this.mTransitions.get(0);
            if (var3 != null) {
               var3.runAnimators();
            }

         } else {
            Iterator var2 = this.mTransitions.iterator();

            while(var2.hasNext()) {
               ((Transition)var2.next()).runAnimators();
            }

         }
      }
   }

   void setCanRemoveViews(boolean var1) {
      super.setCanRemoveViews(var1);
      int var3 = this.mTransitions.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((Transition)this.mTransitions.get(var2)).setCanRemoveViews(var1);
      }

   }

   public TransitionSet setDuration(long var1) {
      super.setDuration(var1);
      if (this.mDuration >= 0L) {
         ArrayList var5 = this.mTransitions;
         if (var5 != null) {
            int var4 = var5.size();

            for(int var3 = 0; var3 < var4; ++var3) {
               ((Transition)this.mTransitions.get(var3)).setDuration(var1);
            }
         }
      }

      return this;
   }

   public void setEpicenterCallback(Transition.EpicenterCallback var1) {
      super.setEpicenterCallback(var1);
      this.mChangeFlags |= 8;
      int var3 = this.mTransitions.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((Transition)this.mTransitions.get(var2)).setEpicenterCallback(var1);
      }

   }

   public TransitionSet setInterpolator(TimeInterpolator var1) {
      this.mChangeFlags |= 1;
      ArrayList var4 = this.mTransitions;
      if (var4 != null) {
         int var3 = var4.size();

         for(int var2 = 0; var2 < var3; ++var2) {
            ((Transition)this.mTransitions.get(var2)).setInterpolator(var1);
         }
      }

      return (TransitionSet)super.setInterpolator(var1);
   }

   public TransitionSet setOrdering(int var1) {
      if (var1 != 0) {
         if (var1 == 1) {
            this.mPlayTogether = false;
            return this;
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Invalid parameter for TransitionSet ordering: ");
            var2.append(var1);
            throw new AndroidRuntimeException(var2.toString());
         }
      } else {
         this.mPlayTogether = true;
         return this;
      }
   }

   public void setPathMotion(PathMotion var1) {
      super.setPathMotion(var1);
      this.mChangeFlags |= 4;
      if (this.mTransitions != null) {
         for(int var2 = 0; var2 < this.mTransitions.size(); ++var2) {
            ((Transition)this.mTransitions.get(var2)).setPathMotion(var1);
         }
      }

   }

   public void setPropagation(TransitionPropagation var1) {
      super.setPropagation(var1);
      this.mChangeFlags |= 2;
      int var3 = this.mTransitions.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((Transition)this.mTransitions.get(var2)).setPropagation(var1);
      }

   }

   TransitionSet setSceneRoot(ViewGroup var1) {
      super.setSceneRoot(var1);
      int var3 = this.mTransitions.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((Transition)this.mTransitions.get(var2)).setSceneRoot(var1);
      }

      return this;
   }

   public TransitionSet setStartDelay(long var1) {
      return (TransitionSet)super.setStartDelay(var1);
   }

   String toString(String var1) {
      String var3 = super.toString(var1);

      for(int var2 = 0; var2 < this.mTransitions.size(); ++var2) {
         StringBuilder var4 = new StringBuilder();
         var4.append(var3);
         var4.append("\n");
         Transition var6 = (Transition)this.mTransitions.get(var2);
         StringBuilder var5 = new StringBuilder();
         var5.append(var1);
         var5.append("  ");
         var4.append(var6.toString(var5.toString()));
         var3 = var4.toString();
      }

      return var3;
   }

   static class TransitionSetListener extends TransitionListenerAdapter {
      TransitionSet mTransitionSet;

      TransitionSetListener(TransitionSet var1) {
         this.mTransitionSet = var1;
      }

      public void onTransitionEnd(Transition var1) {
         TransitionSet var2 = this.mTransitionSet;
         --var2.mCurrentListeners;
         if (this.mTransitionSet.mCurrentListeners == 0) {
            this.mTransitionSet.mStarted = false;
            this.mTransitionSet.end();
         }

         var1.removeListener(this);
      }

      public void onTransitionStart(Transition var1) {
         if (!this.mTransitionSet.mStarted) {
            this.mTransitionSet.start();
            this.mTransitionSet.mStarted = true;
         }

      }
   }
}
