package com.nineoldandroids.animation;

import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class AnimatorSet extends Animator {
   private ValueAnimator mDelayAnim = null;
   private long mDuration = -1L;
   private boolean mNeedsSort = true;
   private HashMap mNodeMap = new HashMap();
   private ArrayList mNodes = new ArrayList();
   private ArrayList mPlayingSet = new ArrayList();
   private AnimatorSet.AnimatorSetListener mSetListener = null;
   private ArrayList mSortedNodes = new ArrayList();
   private long mStartDelay = 0L;
   private boolean mStarted = false;
   boolean mTerminated = false;

   private void sortNodes() {
      int var1;
      int var2;
      int var3;
      int var4;
      if (this.mNeedsSort) {
         this.mSortedNodes.clear();
         ArrayList var9 = new ArrayList();
         var2 = this.mNodes.size();

         for(var1 = 0; var1 < var2; ++var1) {
            AnimatorSet.Node var10 = (AnimatorSet.Node)this.mNodes.get(var1);
            if (var10.dependencies == null || var10.dependencies.size() == 0) {
               var9.add(var10);
            }
         }

         ArrayList var11 = new ArrayList();

         while(var9.size() > 0) {
            var3 = var9.size();

            for(var1 = 0; var1 < var3; ++var1) {
               AnimatorSet.Node var7 = (AnimatorSet.Node)var9.get(var1);
               this.mSortedNodes.add(var7);
               if (var7.nodeDependents != null) {
                  var4 = var7.nodeDependents.size();

                  for(var2 = 0; var2 < var4; ++var2) {
                     AnimatorSet.Node var8 = (AnimatorSet.Node)var7.nodeDependents.get(var2);
                     var8.nodeDependencies.remove(var7);
                     if (var8.nodeDependencies.size() == 0) {
                        var11.add(var8);
                     }
                  }
               }
            }

            var9.clear();
            var9.addAll(var11);
            var11.clear();
         }

         this.mNeedsSort = false;
         if (this.mSortedNodes.size() != this.mNodes.size()) {
            throw new IllegalStateException("Circular dependencies cannot exist in AnimatorSet");
         }
      } else {
         var3 = this.mNodes.size();

         for(var1 = 0; var1 < var3; ++var1) {
            AnimatorSet.Node var5 = (AnimatorSet.Node)this.mNodes.get(var1);
            if (var5.dependencies != null && var5.dependencies.size() > 0) {
               var4 = var5.dependencies.size();

               for(var2 = 0; var2 < var4; ++var2) {
                  AnimatorSet.Dependency var6 = (AnimatorSet.Dependency)var5.dependencies.get(var2);
                  if (var5.nodeDependencies == null) {
                     var5.nodeDependencies = new ArrayList();
                  }

                  if (!var5.nodeDependencies.contains(var6.node)) {
                     var5.nodeDependencies.add(var6.node);
                  }
               }
            }

            var5.done = false;
         }

      }
   }

   public void cancel() {
      this.mTerminated = true;
      if (this.isStarted()) {
         ArrayList var1 = null;
         if (this.mListeners != null) {
            ArrayList var2 = (ArrayList)this.mListeners.clone();
            Iterator var3 = var2.iterator();

            while(true) {
               var1 = var2;
               if (!var3.hasNext()) {
                  break;
               }

               ((Animator.AnimatorListener)var3.next()).onAnimationCancel(this);
            }
         }

         ValueAnimator var5 = this.mDelayAnim;
         if (var5 != null && var5.isRunning()) {
            this.mDelayAnim.cancel();
         } else if (this.mSortedNodes.size() > 0) {
            Iterator var6 = this.mSortedNodes.iterator();

            while(var6.hasNext()) {
               ((AnimatorSet.Node)var6.next()).animation.cancel();
            }
         }

         if (var1 != null) {
            Iterator var4 = var1.iterator();

            while(var4.hasNext()) {
               ((Animator.AnimatorListener)var4.next()).onAnimationEnd(this);
            }
         }

         this.mStarted = false;
      }

   }

   public AnimatorSet clone() {
      AnimatorSet var3 = (AnimatorSet)super.clone();
      var3.mNeedsSort = true;
      var3.mTerminated = false;
      var3.mStarted = false;
      var3.mPlayingSet = new ArrayList();
      var3.mNodeMap = new HashMap();
      var3.mNodes = new ArrayList();
      var3.mSortedNodes = new ArrayList();
      HashMap var4 = new HashMap();
      Iterator var5 = this.mNodes.iterator();

      while(true) {
         ArrayList var6;
         ArrayList var9;
         Iterator var10;
         do {
            do {
               AnimatorSet.Node var2;
               if (!var5.hasNext()) {
                  var10 = this.mNodes.iterator();

                  while(true) {
                     AnimatorSet.Node var12;
                     do {
                        if (!var10.hasNext()) {
                           return var3;
                        }

                        var12 = (AnimatorSet.Node)var10.next();
                        var2 = (AnimatorSet.Node)var4.get(var12);
                     } while(var12.dependencies == null);

                     var5 = var12.dependencies.iterator();

                     while(var5.hasNext()) {
                        AnimatorSet.Dependency var13 = (AnimatorSet.Dependency)var5.next();
                        var2.addDependency(new AnimatorSet.Dependency((AnimatorSet.Node)var4.get(var13.node), var13.rule));
                     }
                  }
               }

               AnimatorSet.Node var1 = (AnimatorSet.Node)var5.next();
               var2 = var1.clone();
               var4.put(var1, var2);
               var3.mNodes.add(var2);
               var3.mNodeMap.put(var2.animation, var2);
               var2.dependencies = null;
               var2.tmpDependencies = null;
               var2.nodeDependents = null;
               var2.nodeDependencies = null;
               var6 = var2.animation.getListeners();
            } while(var6 == null);

            var9 = null;

            ArrayList var11;
            for(Iterator var7 = var6.iterator(); var7.hasNext(); var9 = var11) {
               Animator.AnimatorListener var8 = (Animator.AnimatorListener)var7.next();
               var11 = var9;
               if (var8 instanceof AnimatorSet.AnimatorSetListener) {
                  var11 = var9;
                  if (var9 == null) {
                     var11 = new ArrayList();
                  }

                  var11.add(var8);
               }
            }
         } while(var9 == null);

         var10 = var9.iterator();

         while(var10.hasNext()) {
            var6.remove((Animator.AnimatorListener)var10.next());
         }
      }
   }

   public void end() {
      this.mTerminated = true;
      if (this.isStarted()) {
         Iterator var1;
         if (this.mSortedNodes.size() != this.mNodes.size()) {
            this.sortNodes();

            AnimatorSet.Node var2;
            for(var1 = this.mSortedNodes.iterator(); var1.hasNext(); var2.animation.addListener(this.mSetListener)) {
               var2 = (AnimatorSet.Node)var1.next();
               if (this.mSetListener == null) {
                  this.mSetListener = new AnimatorSet.AnimatorSetListener(this);
               }
            }
         }

         ValueAnimator var3 = this.mDelayAnim;
         if (var3 != null) {
            var3.cancel();
         }

         if (this.mSortedNodes.size() > 0) {
            var1 = this.mSortedNodes.iterator();

            while(var1.hasNext()) {
               ((AnimatorSet.Node)var1.next()).animation.end();
            }
         }

         if (this.mListeners != null) {
            var1 = ((ArrayList)this.mListeners.clone()).iterator();

            while(var1.hasNext()) {
               ((Animator.AnimatorListener)var1.next()).onAnimationEnd(this);
            }
         }

         this.mStarted = false;
      }

   }

   public ArrayList getChildAnimations() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.mNodes.iterator();

      while(var2.hasNext()) {
         var1.add(((AnimatorSet.Node)var2.next()).animation);
      }

      return var1;
   }

   public long getDuration() {
      return this.mDuration;
   }

   public long getStartDelay() {
      return this.mStartDelay;
   }

   public boolean isRunning() {
      Iterator var1 = this.mNodes.iterator();

      do {
         if (!var1.hasNext()) {
            return false;
         }
      } while(!((AnimatorSet.Node)var1.next()).animation.isRunning());

      return true;
   }

   public boolean isStarted() {
      return this.mStarted;
   }

   public AnimatorSet.Builder play(Animator var1) {
      if (var1 != null) {
         this.mNeedsSort = true;
         return new AnimatorSet.Builder(var1);
      } else {
         return null;
      }
   }

   public void playSequentially(List var1) {
      if (var1 != null && var1.size() > 0) {
         this.mNeedsSort = true;
         if (var1.size() == 1) {
            this.play((Animator)var1.get(0));
            return;
         }

         for(int var2 = 0; var2 < var1.size() - 1; ++var2) {
            this.play((Animator)var1.get(var2)).before((Animator)var1.get(var2 + 1));
         }
      }

   }

   public void playSequentially(Animator... var1) {
      if (var1 != null) {
         this.mNeedsSort = true;
         if (var1.length == 1) {
            this.play(var1[0]);
            return;
         }

         for(int var2 = 0; var2 < var1.length - 1; ++var2) {
            this.play(var1[var2]).before(var1[var2 + 1]);
         }
      }

   }

   public void playTogether(Collection var1) {
      if (var1 != null && var1.size() > 0) {
         this.mNeedsSort = true;
         Animator var2 = null;
         Iterator var3 = var1.iterator();
         AnimatorSet.Builder var4 = var2;

         while(var3.hasNext()) {
            var2 = (Animator)var3.next();
            if (var4 == null) {
               var4 = this.play(var2);
            } else {
               var4.with(var2);
            }
         }
      }

   }

   public void playTogether(Animator... var1) {
      if (var1 != null) {
         this.mNeedsSort = true;
         AnimatorSet.Builder var3 = this.play(var1[0]);

         for(int var2 = 1; var2 < var1.length; ++var2) {
            var3.with(var1[var2]);
         }
      }

   }

   public AnimatorSet setDuration(long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("duration must be a value of zero or greater");
      } else {
         Iterator var3 = this.mNodes.iterator();

         while(var3.hasNext()) {
            ((AnimatorSet.Node)var3.next()).animation.setDuration(var1);
         }

         this.mDuration = var1;
         return this;
      }
   }

   public void setInterpolator(Interpolator var1) {
      Iterator var2 = this.mNodes.iterator();

      while(var2.hasNext()) {
         ((AnimatorSet.Node)var2.next()).animation.setInterpolator(var1);
      }

   }

   public void setStartDelay(long var1) {
      this.mStartDelay = var1;
   }

   public void setTarget(Object var1) {
      Iterator var2 = this.mNodes.iterator();

      while(var2.hasNext()) {
         Animator var3 = ((AnimatorSet.Node)var2.next()).animation;
         if (var3 instanceof AnimatorSet) {
            ((AnimatorSet)var3).setTarget(var1);
         } else if (var3 instanceof ObjectAnimator) {
            ((ObjectAnimator)var3).setTarget(var1);
         }
      }

   }

   public void setupEndValues() {
      Iterator var1 = this.mNodes.iterator();

      while(var1.hasNext()) {
         ((AnimatorSet.Node)var1.next()).animation.setupEndValues();
      }

   }

   public void setupStartValues() {
      Iterator var1 = this.mNodes.iterator();

      while(var1.hasNext()) {
         ((AnimatorSet.Node)var1.next()).animation.setupStartValues();
      }

   }

   public void start() {
      this.mTerminated = false;
      this.mStarted = true;
      this.sortNodes();
      int var3 = this.mSortedNodes.size();

      int var1;
      label105:
      for(var1 = 0; var1 < var3; ++var1) {
         AnimatorSet.Node var5 = (AnimatorSet.Node)this.mSortedNodes.get(var1);
         ArrayList var6 = var5.animation.getListeners();
         if (var6 != null && var6.size() > 0) {
            Iterator var10 = (new ArrayList(var6)).iterator();

            while(true) {
               Animator.AnimatorListener var7;
               do {
                  if (!var10.hasNext()) {
                     continue label105;
                  }

                  var7 = (Animator.AnimatorListener)var10.next();
               } while(!(var7 instanceof AnimatorSet.DependencyListener) && !(var7 instanceof AnimatorSet.AnimatorSetListener));

               var5.animation.removeListener(var7);
            }
         }
      }

      final ArrayList var8 = new ArrayList();

      int var2;
      AnimatorSet.Node var11;
      for(var1 = 0; var1 < var3; ++var1) {
         var11 = (AnimatorSet.Node)this.mSortedNodes.get(var1);
         if (this.mSetListener == null) {
            this.mSetListener = new AnimatorSet.AnimatorSetListener(this);
         }

         if (var11.dependencies != null && var11.dependencies.size() != 0) {
            int var4 = var11.dependencies.size();

            for(var2 = 0; var2 < var4; ++var2) {
               AnimatorSet.Dependency var13 = (AnimatorSet.Dependency)var11.dependencies.get(var2);
               var13.node.animation.addListener(new AnimatorSet.DependencyListener(this, var11, var13.rule));
            }

            var11.tmpDependencies = (ArrayList)var11.dependencies.clone();
         } else {
            var8.add(var11);
         }

         var11.animation.addListener(this.mSetListener);
      }

      if (this.mStartDelay <= 0L) {
         Iterator var9 = var8.iterator();

         while(var9.hasNext()) {
            var11 = (AnimatorSet.Node)var9.next();
            var11.animation.start();
            this.mPlayingSet.add(var11.animation);
         }
      } else {
         ValueAnimator var12 = ValueAnimator.ofFloat(0.0F, 1.0F);
         this.mDelayAnim = var12;
         var12.setDuration(this.mStartDelay);
         this.mDelayAnim.addListener(new AnimatorListenerAdapter() {
            boolean canceled = false;

            public void onAnimationCancel(Animator var1) {
               this.canceled = true;
            }

            public void onAnimationEnd(Animator var1) {
               if (!this.canceled) {
                  int var3 = var8.size();

                  for(int var2 = 0; var2 < var3; ++var2) {
                     AnimatorSet.Node var4 = (AnimatorSet.Node)var8.get(var2);
                     var4.animation.start();
                     AnimatorSet.this.mPlayingSet.add(var4.animation);
                  }
               }

            }
         });
         this.mDelayAnim.start();
      }

      if (this.mListeners != null) {
         var8 = (ArrayList)this.mListeners.clone();
         var2 = var8.size();

         for(var1 = 0; var1 < var2; ++var1) {
            ((Animator.AnimatorListener)var8.get(var1)).onAnimationStart(this);
         }
      }

      if (this.mNodes.size() == 0 && this.mStartDelay == 0L) {
         this.mStarted = false;
         if (this.mListeners != null) {
            var8 = (ArrayList)this.mListeners.clone();
            var2 = var8.size();

            for(var1 = 0; var1 < var2; ++var1) {
               ((Animator.AnimatorListener)var8.get(var1)).onAnimationEnd(this);
            }
         }
      }

   }

   private class AnimatorSetListener implements Animator.AnimatorListener {
      private AnimatorSet mAnimatorSet;

      AnimatorSetListener(AnimatorSet var2) {
         this.mAnimatorSet = var2;
      }

      public void onAnimationCancel(Animator var1) {
         if (!AnimatorSet.this.mTerminated && AnimatorSet.this.mPlayingSet.size() == 0 && AnimatorSet.this.mListeners != null) {
            int var3 = AnimatorSet.this.mListeners.size();

            for(int var2 = 0; var2 < var3; ++var2) {
               ((Animator.AnimatorListener)AnimatorSet.this.mListeners.get(var2)).onAnimationCancel(this.mAnimatorSet);
            }
         }

      }

      public void onAnimationEnd(Animator var1) {
         var1.removeListener(this);
         AnimatorSet.this.mPlayingSet.remove(var1);
         ((AnimatorSet.Node)this.mAnimatorSet.mNodeMap.get(var1)).done = true;
         if (!AnimatorSet.this.mTerminated) {
            ArrayList var6 = this.mAnimatorSet.mSortedNodes;
            boolean var4 = true;
            int var5 = var6.size();
            int var2 = 0;

            boolean var3;
            while(true) {
               var3 = var4;
               if (var2 >= var5) {
                  break;
               }

               if (!((AnimatorSet.Node)var6.get(var2)).done) {
                  var3 = false;
                  break;
               }

               ++var2;
            }

            if (var3) {
               if (AnimatorSet.this.mListeners != null) {
                  var6 = (ArrayList)AnimatorSet.this.mListeners.clone();
                  int var7 = var6.size();

                  for(var2 = 0; var2 < var7; ++var2) {
                     ((Animator.AnimatorListener)var6.get(var2)).onAnimationEnd(this.mAnimatorSet);
                  }
               }

               this.mAnimatorSet.mStarted = false;
            }
         }

      }

      public void onAnimationRepeat(Animator var1) {
      }

      public void onAnimationStart(Animator var1) {
      }
   }

   public class Builder {
      private AnimatorSet.Node mCurrentNode;

      Builder(Animator var2) {
         AnimatorSet.Node var3 = (AnimatorSet.Node)AnimatorSet.this.mNodeMap.get(var2);
         this.mCurrentNode = var3;
         if (var3 == null) {
            this.mCurrentNode = new AnimatorSet.Node(var2);
            AnimatorSet.this.mNodeMap.put(var2, this.mCurrentNode);
            AnimatorSet.this.mNodes.add(this.mCurrentNode);
         }

      }

      public AnimatorSet.Builder after(long var1) {
         ValueAnimator var3 = ValueAnimator.ofFloat(0.0F, 1.0F);
         var3.setDuration(var1);
         this.after(var3);
         return this;
      }

      public AnimatorSet.Builder after(Animator var1) {
         AnimatorSet.Node var3 = (AnimatorSet.Node)AnimatorSet.this.mNodeMap.get(var1);
         AnimatorSet.Node var2 = var3;
         if (var3 == null) {
            var2 = new AnimatorSet.Node(var1);
            AnimatorSet.this.mNodeMap.put(var1, var2);
            AnimatorSet.this.mNodes.add(var2);
         }

         AnimatorSet.Dependency var4 = new AnimatorSet.Dependency(var2, 1);
         this.mCurrentNode.addDependency(var4);
         return this;
      }

      public AnimatorSet.Builder before(Animator var1) {
         AnimatorSet.Node var3 = (AnimatorSet.Node)AnimatorSet.this.mNodeMap.get(var1);
         AnimatorSet.Node var2 = var3;
         if (var3 == null) {
            var2 = new AnimatorSet.Node(var1);
            AnimatorSet.this.mNodeMap.put(var1, var2);
            AnimatorSet.this.mNodes.add(var2);
         }

         var2.addDependency(new AnimatorSet.Dependency(this.mCurrentNode, 1));
         return this;
      }

      public AnimatorSet.Builder with(Animator var1) {
         AnimatorSet.Node var3 = (AnimatorSet.Node)AnimatorSet.this.mNodeMap.get(var1);
         AnimatorSet.Node var2 = var3;
         if (var3 == null) {
            var2 = new AnimatorSet.Node(var1);
            AnimatorSet.this.mNodeMap.put(var1, var2);
            AnimatorSet.this.mNodes.add(var2);
         }

         var2.addDependency(new AnimatorSet.Dependency(this.mCurrentNode, 0));
         return this;
      }
   }

   private static class Dependency {
      static final int AFTER = 1;
      static final int WITH = 0;
      public AnimatorSet.Node node;
      public int rule;

      public Dependency(AnimatorSet.Node var1, int var2) {
         this.node = var1;
         this.rule = var2;
      }
   }

   private static class DependencyListener implements Animator.AnimatorListener {
      private AnimatorSet mAnimatorSet;
      private AnimatorSet.Node mNode;
      private int mRule;

      public DependencyListener(AnimatorSet var1, AnimatorSet.Node var2, int var3) {
         this.mAnimatorSet = var1;
         this.mNode = var2;
         this.mRule = var3;
      }

      private void startIfReady(Animator var1) {
         if (!this.mAnimatorSet.mTerminated) {
            Object var5 = null;
            int var3 = this.mNode.tmpDependencies.size();
            int var2 = 0;

            AnimatorSet.Dependency var4;
            while(true) {
               var4 = (AnimatorSet.Dependency)var5;
               if (var2 >= var3) {
                  break;
               }

               var4 = (AnimatorSet.Dependency)this.mNode.tmpDependencies.get(var2);
               if (var4.rule == this.mRule && var4.node.animation == var1) {
                  var1.removeListener(this);
                  break;
               }

               ++var2;
            }

            this.mNode.tmpDependencies.remove(var4);
            if (this.mNode.tmpDependencies.size() == 0) {
               this.mNode.animation.start();
               this.mAnimatorSet.mPlayingSet.add(this.mNode.animation);
            }

         }
      }

      public void onAnimationCancel(Animator var1) {
      }

      public void onAnimationEnd(Animator var1) {
         if (this.mRule == 1) {
            this.startIfReady(var1);
         }

      }

      public void onAnimationRepeat(Animator var1) {
      }

      public void onAnimationStart(Animator var1) {
         if (this.mRule == 0) {
            this.startIfReady(var1);
         }

      }
   }

   private static class Node implements Cloneable {
      public Animator animation;
      public ArrayList dependencies = null;
      public boolean done = false;
      public ArrayList nodeDependencies = null;
      public ArrayList nodeDependents = null;
      public ArrayList tmpDependencies = null;

      public Node(Animator var1) {
         this.animation = var1;
      }

      public void addDependency(AnimatorSet.Dependency var1) {
         if (this.dependencies == null) {
            this.dependencies = new ArrayList();
            this.nodeDependencies = new ArrayList();
         }

         this.dependencies.add(var1);
         if (!this.nodeDependencies.contains(var1.node)) {
            this.nodeDependencies.add(var1.node);
         }

         AnimatorSet.Node var2 = var1.node;
         if (var2.nodeDependents == null) {
            var2.nodeDependents = new ArrayList();
         }

         var2.nodeDependents.add(this);
      }

      public AnimatorSet.Node clone() {
         try {
            AnimatorSet.Node var1 = (AnimatorSet.Node)super.clone();
            var1.animation = this.animation.clone();
            return var1;
         } catch (CloneNotSupportedException var2) {
            throw new AssertionError();
         }
      }
   }
}
