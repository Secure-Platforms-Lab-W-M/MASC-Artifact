package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TargetApi(14)
@RequiresApi(14)
abstract class TransitionPort implements Cloneable {
   static final boolean DBG = false;
   private static final String LOG_TAG = "Transition";
   private static ThreadLocal sRunningAnimators = new ThreadLocal();
   ArrayList mAnimators = new ArrayList();
   boolean mCanRemoveViews = false;
   ArrayList mCurrentAnimators = new ArrayList();
   long mDuration = -1L;
   private TransitionValuesMaps mEndValues = new TransitionValuesMaps();
   private boolean mEnded = false;
   TimeInterpolator mInterpolator = null;
   ArrayList mListeners = null;
   private String mName = this.getClass().getName();
   int mNumInstances = 0;
   TransitionSetPort mParent = null;
   boolean mPaused = false;
   ViewGroup mSceneRoot = null;
   long mStartDelay = -1L;
   private TransitionValuesMaps mStartValues = new TransitionValuesMaps();
   ArrayList mTargetChildExcludes = null;
   ArrayList mTargetExcludes = null;
   ArrayList mTargetIdChildExcludes = null;
   ArrayList mTargetIdExcludes = null;
   ArrayList mTargetIds = new ArrayList();
   ArrayList mTargetTypeChildExcludes = null;
   ArrayList mTargetTypeExcludes = null;
   ArrayList mTargets = new ArrayList();

   public TransitionPort() {
   }

   private void captureHierarchy(View var1, boolean var2) {
      if (var1 != null) {
         boolean var3 = false;
         if (var1.getParent() instanceof ListView) {
            var3 = true;
         }

         if (!var3 || ((ListView)var1.getParent()).getAdapter().hasStableIds()) {
            int var4 = -1;
            long var7 = -1L;
            if (!var3) {
               var4 = var1.getId();
            } else {
               ListView var9 = (ListView)var1.getParent();
               var7 = var9.getItemIdAtPosition(var9.getPositionForView(var1));
            }

            if ((this.mTargetIdExcludes == null || !this.mTargetIdExcludes.contains(var4)) && (this.mTargetExcludes == null || !this.mTargetExcludes.contains(var1))) {
               if (this.mTargetTypeExcludes != null && var1 != null) {
                  int var6 = this.mTargetTypeExcludes.size();

                  for(int var5 = 0; var5 < var6; ++var5) {
                     if (((Class)this.mTargetTypeExcludes.get(var5)).isInstance(var1)) {
                        return;
                     }
                  }
               }

               TransitionValues var12 = new TransitionValues();
               var12.view = var1;
               if (var2) {
                  this.captureStartValues(var12);
               } else {
                  this.captureEndValues(var12);
               }

               if (var2) {
                  if (!var3) {
                     this.mStartValues.viewValues.put(var1, var12);
                     if (var4 >= 0) {
                        this.mStartValues.idValues.put(var4, var12);
                     }
                  } else {
                     this.mStartValues.itemIdValues.put(var7, var12);
                  }
               } else if (!var3) {
                  this.mEndValues.viewValues.put(var1, var12);
                  if (var4 >= 0) {
                     this.mEndValues.idValues.put(var4, var12);
                  }
               } else {
                  this.mEndValues.itemIdValues.put(var7, var12);
               }

               if (var1 instanceof ViewGroup && (this.mTargetIdChildExcludes == null || !this.mTargetIdChildExcludes.contains(var4)) && (this.mTargetChildExcludes == null || !this.mTargetChildExcludes.contains(var1))) {
                  int var11;
                  if (this.mTargetTypeChildExcludes != null && var1 != null) {
                     var4 = this.mTargetTypeChildExcludes.size();

                     for(var11 = 0; var11 < var4; ++var11) {
                        if (((Class)this.mTargetTypeChildExcludes.get(var11)).isInstance(var1)) {
                           return;
                        }
                     }
                  }

                  ViewGroup var10 = (ViewGroup)var1;

                  for(var11 = 0; var11 < var10.getChildCount(); ++var11) {
                     this.captureHierarchy(var10.getChildAt(var11), var2);
                  }
               }
            }
         }
      }

   }

   private ArrayList excludeId(ArrayList var1, int var2, boolean var3) {
      ArrayList var4 = var1;
      if (var2 > 0) {
         if (!var3) {
            return TransitionPort.ArrayListManager.remove(var1, var2);
         }

         var4 = TransitionPort.ArrayListManager.add(var1, var2);
      }

      return var4;
   }

   private ArrayList excludeType(ArrayList var1, Class var2, boolean var3) {
      ArrayList var4 = var1;
      if (var2 != null) {
         if (!var3) {
            return TransitionPort.ArrayListManager.remove(var1, var2);
         }

         var4 = TransitionPort.ArrayListManager.add(var1, var2);
      }

      return var4;
   }

   private ArrayList excludeView(ArrayList var1, View var2, boolean var3) {
      ArrayList var4 = var1;
      if (var2 != null) {
         if (!var3) {
            return TransitionPort.ArrayListManager.remove(var1, var2);
         }

         var4 = TransitionPort.ArrayListManager.add(var1, var2);
      }

      return var4;
   }

   private static ArrayMap getRunningAnimators() {
      ArrayMap var1 = (ArrayMap)sRunningAnimators.get();
      ArrayMap var0 = var1;
      if (var1 == null) {
         var0 = new ArrayMap();
         sRunningAnimators.set(var0);
      }

      return var0;
   }

   private void runAnimator(Animator var1, final ArrayMap var2) {
      if (var1 != null) {
         var1.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               var2.remove(var1);
               TransitionPort.this.mCurrentAnimators.remove(var1);
            }

            public void onAnimationStart(Animator var1) {
               TransitionPort.this.mCurrentAnimators.add(var1);
            }
         });
         this.animate(var1);
      }

   }

   public TransitionPort addListener(TransitionPort.TransitionListener var1) {
      if (this.mListeners == null) {
         this.mListeners = new ArrayList();
      }

      this.mListeners.add(var1);
      return this;
   }

   public TransitionPort addTarget(int var1) {
      if (var1 > 0) {
         this.mTargetIds.add(var1);
      }

      return this;
   }

   public TransitionPort addTarget(View var1) {
      this.mTargets.add(var1);
      return this;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected void animate(Animator var1) {
      if (var1 == null) {
         this.end();
      } else {
         if (this.getDuration() >= 0L) {
            var1.setDuration(this.getDuration());
         }

         if (this.getStartDelay() >= 0L) {
            var1.setStartDelay(this.getStartDelay());
         }

         if (this.getInterpolator() != null) {
            var1.setInterpolator(this.getInterpolator());
         }

         var1.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               TransitionPort.this.end();
               var1.removeListener(this);
            }
         });
         var1.start();
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected void cancel() {
      int var1;
      for(var1 = this.mCurrentAnimators.size() - 1; var1 >= 0; --var1) {
         ((Animator)this.mCurrentAnimators.get(var1)).cancel();
      }

      if (this.mListeners != null && this.mListeners.size() > 0) {
         ArrayList var3 = (ArrayList)this.mListeners.clone();
         int var2 = var3.size();

         for(var1 = 0; var1 < var2; ++var1) {
            ((TransitionPort.TransitionListener)var3.get(var1)).onTransitionCancel(this);
         }
      }

   }

   public abstract void captureEndValues(TransitionValues var1);

   public abstract void captureStartValues(TransitionValues var1);

   void captureValues(ViewGroup var1, boolean var2) {
      this.clearValues(var2);
      if (this.mTargetIds.size() <= 0 && this.mTargets.size() <= 0) {
         this.captureHierarchy(var1, var2);
      } else {
         int var3;
         if (this.mTargetIds.size() > 0) {
            for(var3 = 0; var3 < this.mTargetIds.size(); ++var3) {
               int var4 = (Integer)this.mTargetIds.get(var3);
               View var5 = var1.findViewById(var4);
               if (var5 != null) {
                  TransitionValues var6 = new TransitionValues();
                  var6.view = var5;
                  if (var2) {
                     this.captureStartValues(var6);
                  } else {
                     this.captureEndValues(var6);
                  }

                  if (var2) {
                     this.mStartValues.viewValues.put(var5, var6);
                     if (var4 >= 0) {
                        this.mStartValues.idValues.put(var4, var6);
                     }
                  } else {
                     this.mEndValues.viewValues.put(var5, var6);
                     if (var4 >= 0) {
                        this.mEndValues.idValues.put(var4, var6);
                     }
                  }
               }
            }
         }

         if (this.mTargets.size() > 0) {
            for(var3 = 0; var3 < this.mTargets.size(); ++var3) {
               View var7 = (View)this.mTargets.get(var3);
               if (var7 != null) {
                  TransitionValues var8 = new TransitionValues();
                  var8.view = var7;
                  if (var2) {
                     this.captureStartValues(var8);
                  } else {
                     this.captureEndValues(var8);
                  }

                  if (var2) {
                     this.mStartValues.viewValues.put(var7, var8);
                  } else {
                     this.mEndValues.viewValues.put(var7, var8);
                  }
               }
            }
         }
      }

   }

   void clearValues(boolean var1) {
      if (var1) {
         this.mStartValues.viewValues.clear();
         this.mStartValues.idValues.clear();
         this.mStartValues.itemIdValues.clear();
      } else {
         this.mEndValues.viewValues.clear();
         this.mEndValues.idValues.clear();
         this.mEndValues.itemIdValues.clear();
      }
   }

   public TransitionPort clone() {
      TransitionPort var1 = null;

      boolean var10001;
      TransitionPort var2;
      try {
         var2 = (TransitionPort)super.clone();
      } catch (CloneNotSupportedException var6) {
         var10001 = false;
         return var1;
      }

      var1 = var2;

      try {
         var2.mAnimators = new ArrayList();
      } catch (CloneNotSupportedException var5) {
         var10001 = false;
         return var1;
      }

      var1 = var2;

      try {
         var2.mStartValues = new TransitionValuesMaps();
      } catch (CloneNotSupportedException var4) {
         var10001 = false;
         return var1;
      }

      var1 = var2;

      try {
         var2.mEndValues = new TransitionValuesMaps();
         return var2;
      } catch (CloneNotSupportedException var3) {
         var10001 = false;
         return var1;
      }
   }

   public Animator createAnimator(ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      return null;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected void createAnimators(ViewGroup var1, TransitionValuesMaps var2, TransitionValuesMaps var3) {
      ArrayMap var17 = new ArrayMap(var3.viewValues);
      SparseArray var16 = new SparseArray(var3.idValues.size());

      int var4;
      for(var4 = 0; var4 < var3.idValues.size(); ++var4) {
         var16.put(var3.idValues.keyAt(var4), var3.idValues.valueAt(var4));
      }

      LongSparseArray var13 = new LongSparseArray(var3.itemIdValues.size());

      for(var4 = 0; var4 < var3.itemIdValues.size(); ++var4) {
         var13.put(var3.itemIdValues.keyAt(var4), var3.itemIdValues.valueAt(var4));
      }

      ArrayList var14 = new ArrayList();
      ArrayList var15 = new ArrayList();
      Iterator var18 = var2.viewValues.keySet().iterator();

      long var7;
      TransitionValues var9;
      TransitionValues var10;
      View var11;
      while(var18.hasNext()) {
         View var19 = (View)var18.next();
         var9 = null;
         boolean var23 = false;
         if (var19.getParent() instanceof ListView) {
            var23 = true;
         }

         if (!var23) {
            var4 = var19.getId();
            if (var2.viewValues.get(var19) != null) {
               var10 = (TransitionValues)var2.viewValues.get(var19);
            } else {
               var10 = (TransitionValues)var2.idValues.get(var4);
            }

            if (var3.viewValues.get(var19) != null) {
               var9 = (TransitionValues)var3.viewValues.get(var19);
               var17.remove(var19);
            } else if (var4 != -1) {
               TransitionValues var12 = (TransitionValues)var3.idValues.get(var4);
               var11 = null;
               Iterator var20 = var17.keySet().iterator();

               while(var20.hasNext()) {
                  View var25 = (View)var20.next();
                  if (var25.getId() == var4) {
                     var11 = var25;
                  }
               }

               var9 = var12;
               if (var11 != null) {
                  var17.remove(var11);
                  var9 = var12;
               }
            }

            var16.remove(var4);
            if (this.isValidTarget(var19, (long)var4)) {
               var14.add(var10);
               var15.add(var9);
            }
         } else {
            ListView var24 = (ListView)var19.getParent();
            if (var24.getAdapter().hasStableIds()) {
               var7 = var24.getItemIdAtPosition(var24.getPositionForView(var19));
               var9 = (TransitionValues)var2.itemIdValues.get(var7);
               var13.remove(var7);
               var14.add(var9);
               var15.add((Object)null);
            }
         }
      }

      int var5 = var2.itemIdValues.size();

      for(var4 = 0; var4 < var5; ++var4) {
         var7 = var2.itemIdValues.keyAt(var4);
         if (this.isValidTarget((View)null, var7)) {
            var9 = (TransitionValues)var2.itemIdValues.get(var7);
            var10 = (TransitionValues)var3.itemIdValues.get(var7);
            var13.remove(var7);
            var14.add(var9);
            var15.add(var10);
         }
      }

      Iterator var26 = var17.keySet().iterator();

      while(var26.hasNext()) {
         var11 = (View)var26.next();
         var4 = var11.getId();
         if (this.isValidTarget(var11, (long)var4)) {
            if (var2.viewValues.get(var11) != null) {
               var9 = (TransitionValues)var2.viewValues.get(var11);
            } else {
               var9 = (TransitionValues)var2.idValues.get(var4);
            }

            TransitionValues var27 = (TransitionValues)var17.get(var11);
            var16.remove(var4);
            var14.add(var9);
            var15.add(var27);
         }
      }

      var5 = var16.size();

      int var6;
      for(var4 = 0; var4 < var5; ++var4) {
         var6 = var16.keyAt(var4);
         if (this.isValidTarget((View)null, (long)var6)) {
            var9 = (TransitionValues)var2.idValues.get(var6);
            var10 = (TransitionValues)var16.get(var6);
            var14.add(var9);
            var15.add(var10);
         }
      }

      var5 = var13.size();

      for(var4 = 0; var4 < var5; ++var4) {
         var7 = var13.keyAt(var4);
         var9 = (TransitionValues)var2.itemIdValues.get(var7);
         var10 = (TransitionValues)var13.get(var7);
         var14.add(var9);
         var15.add(var10);
      }

      ArrayMap var33 = getRunningAnimators();

      for(var4 = 0; var4 < var14.size(); ++var4) {
         TransitionValues var21 = (TransitionValues)var14.get(var4);
         var9 = (TransitionValues)var15.get(var4);
         if ((var21 != null || var9 != null) && (var21 == null || !var21.equals(var9))) {
            Animator var28 = this.createAnimator(var1, var21, var9);
            if (var28 != null) {
               var13 = null;
               View var29;
               Animator var32;
               if (var9 != null) {
                  View var30 = var9.view;
                  String[] var34 = this.getTransitionProperties();
                  var32 = var28;
                  var21 = var13;
                  var29 = var30;
                  if (var30 != null) {
                     var32 = var28;
                     var21 = var13;
                     var29 = var30;
                     if (var34 != null) {
                        var32 = var28;
                        var21 = var13;
                        var29 = var30;
                        if (var34.length > 0) {
                           TransitionValues var31 = new TransitionValues();
                           var31.view = var30;
                           var21 = (TransitionValues)var3.viewValues.get(var30);
                           if (var21 != null) {
                              for(var5 = 0; var5 < var34.length; ++var5) {
                                 var31.values.put(var34[var5], var21.values.get(var34[var5]));
                              }
                           }

                           var6 = var33.size();
                           var5 = 0;

                           while(true) {
                              var32 = var28;
                              var21 = var31;
                              var29 = var30;
                              if (var5 >= var6) {
                                 break;
                              }

                              TransitionPort.AnimationInfo var22 = (TransitionPort.AnimationInfo)var33.get((Animator)var33.keyAt(var5));
                              if (var22.values != null && var22.view == var30 && (var22.name == null && this.getName() == null || var22.name.equals(this.getName())) && var22.values.equals(var31)) {
                                 var32 = null;
                                 var29 = var30;
                                 var21 = var31;
                                 break;
                              }

                              ++var5;
                           }
                        }
                     }
                  }
               } else {
                  var29 = var21.view;
                  var32 = var28;
                  var21 = var13;
               }

               if (var32 != null) {
                  var33.put(var32, new TransitionPort.AnimationInfo(var29, this.getName(), WindowIdPort.getWindowId(var1), var21));
                  this.mAnimators.add(var32);
               }
            }
         }
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected void end() {
      --this.mNumInstances;
      if (this.mNumInstances == 0) {
         int var1;
         if (this.mListeners != null && this.mListeners.size() > 0) {
            ArrayList var3 = (ArrayList)this.mListeners.clone();
            int var2 = var3.size();

            for(var1 = 0; var1 < var2; ++var1) {
               ((TransitionPort.TransitionListener)var3.get(var1)).onTransitionEnd(this);
            }
         }

         View var4;
         for(var1 = 0; var1 < this.mStartValues.itemIdValues.size(); ++var1) {
            var4 = ((TransitionValues)this.mStartValues.itemIdValues.valueAt(var1)).view;
         }

         for(var1 = 0; var1 < this.mEndValues.itemIdValues.size(); ++var1) {
            var4 = ((TransitionValues)this.mEndValues.itemIdValues.valueAt(var1)).view;
         }

         this.mEnded = true;
      }

   }

   public TransitionPort excludeChildren(int var1, boolean var2) {
      this.mTargetIdChildExcludes = this.excludeId(this.mTargetIdChildExcludes, var1, var2);
      return this;
   }

   public TransitionPort excludeChildren(View var1, boolean var2) {
      this.mTargetChildExcludes = this.excludeView(this.mTargetChildExcludes, var1, var2);
      return this;
   }

   public TransitionPort excludeChildren(Class var1, boolean var2) {
      this.mTargetTypeChildExcludes = this.excludeType(this.mTargetTypeChildExcludes, var1, var2);
      return this;
   }

   public TransitionPort excludeTarget(int var1, boolean var2) {
      this.mTargetIdExcludes = this.excludeId(this.mTargetIdExcludes, var1, var2);
      return this;
   }

   public TransitionPort excludeTarget(View var1, boolean var2) {
      this.mTargetExcludes = this.excludeView(this.mTargetExcludes, var1, var2);
      return this;
   }

   public TransitionPort excludeTarget(Class var1, boolean var2) {
      this.mTargetTypeExcludes = this.excludeType(this.mTargetTypeExcludes, var1, var2);
      return this;
   }

   public long getDuration() {
      return this.mDuration;
   }

   public TimeInterpolator getInterpolator() {
      return this.mInterpolator;
   }

   public String getName() {
      return this.mName;
   }

   public long getStartDelay() {
      return this.mStartDelay;
   }

   public List getTargetIds() {
      return this.mTargetIds;
   }

   public List getTargets() {
      return this.mTargets;
   }

   public String[] getTransitionProperties() {
      return null;
   }

   public TransitionValues getTransitionValues(View var1, boolean var2) {
      TransitionValues var6;
      if (this.mParent != null) {
         var6 = this.mParent.getTransitionValues(var1, var2);
      } else {
         TransitionValuesMaps var8;
         if (var2) {
            var8 = this.mStartValues;
         } else {
            var8 = this.mEndValues;
         }

         TransitionValues var7 = (TransitionValues)var8.viewValues.get(var1);
         var6 = var7;
         if (var7 == null) {
            int var3 = var1.getId();
            if (var3 >= 0) {
               var7 = (TransitionValues)var8.idValues.get(var3);
            }

            var6 = var7;
            if (var7 == null) {
               var6 = var7;
               if (var1.getParent() instanceof ListView) {
                  ListView var9 = (ListView)var1.getParent();
                  long var4 = var9.getItemIdAtPosition(var9.getPositionForView(var1));
                  return (TransitionValues)var8.itemIdValues.get(var4);
               }
            }
         }
      }

      return var6;
   }

   boolean isValidTarget(View var1, long var2) {
      if (this.mTargetIdExcludes != null && this.mTargetIdExcludes.contains((int)var2)) {
         return false;
      } else if (this.mTargetExcludes != null && this.mTargetExcludes.contains(var1)) {
         return false;
      } else {
         int var4;
         if (this.mTargetTypeExcludes != null && var1 != null) {
            int var5 = this.mTargetTypeExcludes.size();

            for(var4 = 0; var4 < var5; ++var4) {
               if (((Class)this.mTargetTypeExcludes.get(var4)).isInstance(var1)) {
                  return false;
               }
            }
         }

         if (this.mTargetIds.size() == 0 && this.mTargets.size() == 0) {
            return true;
         } else {
            if (this.mTargetIds.size() > 0) {
               for(var4 = 0; var4 < this.mTargetIds.size(); ++var4) {
                  if ((long)(Integer)this.mTargetIds.get(var4) == var2) {
                     return true;
                  }
               }
            }

            if (var1 != null && this.mTargets.size() > 0) {
               for(var4 = 0; var4 < this.mTargets.size(); ++var4) {
                  if (this.mTargets.get(var4) == var1) {
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void pause(View var1) {
      if (!this.mEnded) {
         ArrayMap var4 = getRunningAnimators();
         int var2 = var4.size();
         WindowIdPort var6 = WindowIdPort.getWindowId(var1);
         --var2;

         for(; var2 >= 0; --var2) {
            TransitionPort.AnimationInfo var5 = (TransitionPort.AnimationInfo)var4.valueAt(var2);
            if (var5.view != null && var6.equals(var5.windowId)) {
               ((Animator)var4.keyAt(var2)).cancel();
            }
         }

         if (this.mListeners != null && this.mListeners.size() > 0) {
            ArrayList var7 = (ArrayList)this.mListeners.clone();
            int var3 = var7.size();

            for(var2 = 0; var2 < var3; ++var2) {
               ((TransitionPort.TransitionListener)var7.get(var2)).onTransitionPause(this);
            }
         }

         this.mPaused = true;
      }

   }

   void playTransition(ViewGroup var1) {
      ArrayMap var7 = getRunningAnimators();

      for(int var2 = var7.size() - 1; var2 >= 0; --var2) {
         Animator var8 = (Animator)var7.keyAt(var2);
         if (var8 != null) {
            TransitionPort.AnimationInfo var5 = (TransitionPort.AnimationInfo)var7.get(var8);
            if (var5 != null && var5.view != null && var5.view.getContext() == var1.getContext()) {
               boolean var4 = false;
               TransitionValues var9 = var5.values;
               View var10 = var5.view;
               TransitionValues var12;
               if (this.mEndValues.viewValues != null) {
                  var12 = (TransitionValues)this.mEndValues.viewValues.get(var10);
               } else {
                  var12 = null;
               }

               TransitionValues var6 = var12;
               if (var12 == null) {
                  var6 = (TransitionValues)this.mEndValues.idValues.get(var10.getId());
               }

               boolean var3 = var4;
               if (var9 != null) {
                  var3 = var4;
                  if (var6 != null) {
                     Iterator var13 = var9.values.keySet().iterator();

                     while(true) {
                        var3 = var4;
                        if (!var13.hasNext()) {
                           break;
                        }

                        String var11 = (String)var13.next();
                        Object var14 = var9.values.get(var11);
                        Object var15 = var6.values.get(var11);
                        if (var14 != null && var15 != null && !var14.equals(var15)) {
                           var3 = true;
                           break;
                        }
                     }
                  }
               }

               if (var3) {
                  if (!var8.isRunning() && !var8.isStarted()) {
                     var7.remove(var8);
                  } else {
                     var8.cancel();
                  }
               }
            }
         }
      }

      this.createAnimators(var1, this.mStartValues, this.mEndValues);
      this.runAnimators();
   }

   public TransitionPort removeListener(TransitionPort.TransitionListener var1) {
      if (this.mListeners != null) {
         this.mListeners.remove(var1);
         if (this.mListeners.size() == 0) {
            this.mListeners = null;
            return this;
         }
      }

      return this;
   }

   public TransitionPort removeTarget(int var1) {
      if (var1 > 0) {
         this.mTargetIds.remove(var1);
      }

      return this;
   }

   public TransitionPort removeTarget(View var1) {
      if (var1 != null) {
         this.mTargets.remove(var1);
      }

      return this;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void resume(View var1) {
      if (this.mPaused) {
         if (!this.mEnded) {
            ArrayMap var4 = getRunningAnimators();
            int var2 = var4.size();
            WindowIdPort var6 = WindowIdPort.getWindowId(var1);
            --var2;

            for(; var2 >= 0; --var2) {
               TransitionPort.AnimationInfo var5 = (TransitionPort.AnimationInfo)var4.valueAt(var2);
               if (var5.view != null && var6.equals(var5.windowId)) {
                  ((Animator)var4.keyAt(var2)).end();
               }
            }

            if (this.mListeners != null && this.mListeners.size() > 0) {
               ArrayList var7 = (ArrayList)this.mListeners.clone();
               int var3 = var7.size();

               for(var2 = 0; var2 < var3; ++var2) {
                  ((TransitionPort.TransitionListener)var7.get(var2)).onTransitionResume(this);
               }
            }
         }

         this.mPaused = false;
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected void runAnimators() {
      this.start();
      ArrayMap var1 = getRunningAnimators();
      Iterator var2 = this.mAnimators.iterator();

      while(var2.hasNext()) {
         Animator var3 = (Animator)var2.next();
         if (var1.containsKey(var3)) {
            this.start();
            this.runAnimator(var3, var1);
         }
      }

      this.mAnimators.clear();
      this.end();
   }

   void setCanRemoveViews(boolean var1) {
      this.mCanRemoveViews = var1;
   }

   public TransitionPort setDuration(long var1) {
      this.mDuration = var1;
      return this;
   }

   public TransitionPort setInterpolator(TimeInterpolator var1) {
      this.mInterpolator = var1;
      return this;
   }

   TransitionPort setSceneRoot(ViewGroup var1) {
      this.mSceneRoot = var1;
      return this;
   }

   public TransitionPort setStartDelay(long var1) {
      this.mStartDelay = var1;
      return this;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected void start() {
      if (this.mNumInstances == 0) {
         if (this.mListeners != null && this.mListeners.size() > 0) {
            ArrayList var3 = (ArrayList)this.mListeners.clone();
            int var2 = var3.size();

            for(int var1 = 0; var1 < var2; ++var1) {
               ((TransitionPort.TransitionListener)var3.get(var1)).onTransitionStart(this);
            }
         }

         this.mEnded = false;
      }

      ++this.mNumInstances;
   }

   public String toString() {
      return this.toString("");
   }

   String toString(String var1) {
      String var3 = var1 + this.getClass().getSimpleName() + "@" + Integer.toHexString(this.hashCode()) + ": ";
      var1 = var3;
      if (this.mDuration != -1L) {
         var1 = var3 + "dur(" + this.mDuration + ") ";
      }

      var3 = var1;
      if (this.mStartDelay != -1L) {
         var3 = var1 + "dly(" + this.mStartDelay + ") ";
      }

      var1 = var3;
      if (this.mInterpolator != null) {
         var1 = var3 + "interp(" + this.mInterpolator + ") ";
      }

      if (this.mTargetIds.size() <= 0) {
         var3 = var1;
         if (this.mTargets.size() <= 0) {
            return var3;
         }
      }

      var3 = var1 + "tgts(";
      var1 = var3;
      int var2;
      if (this.mTargetIds.size() > 0) {
         var2 = 0;

         while(true) {
            var1 = var3;
            if (var2 >= this.mTargetIds.size()) {
               break;
            }

            var1 = var3;
            if (var2 > 0) {
               var1 = var3 + ", ";
            }

            var3 = var1 + this.mTargetIds.get(var2);
            ++var2;
         }
      }

      var3 = var1;
      if (this.mTargets.size() > 0) {
         var2 = 0;

         while(true) {
            var3 = var1;
            if (var2 >= this.mTargets.size()) {
               break;
            }

            var3 = var1;
            if (var2 > 0) {
               var3 = var1 + ", ";
            }

            var1 = var3 + this.mTargets.get(var2);
            ++var2;
         }
      }

      var3 = var3 + ")";
      return var3;
   }

   private static class AnimationInfo {
      String name;
      TransitionValues values;
      View view;
      WindowIdPort windowId;

      AnimationInfo(View var1, String var2, WindowIdPort var3, TransitionValues var4) {
         this.view = var1;
         this.name = var2;
         this.values = var4;
         this.windowId = var3;
      }
   }

   private static class ArrayListManager {
      static ArrayList add(ArrayList var0, Object var1) {
         ArrayList var2 = var0;
         if (var0 == null) {
            var2 = new ArrayList();
         }

         if (!var2.contains(var1)) {
            var2.add(var1);
         }

         return var2;
      }

      static ArrayList remove(ArrayList var0, Object var1) {
         ArrayList var2 = var0;
         if (var0 != null) {
            var0.remove(var1);
            var2 = var0;
            if (var0.isEmpty()) {
               var2 = null;
            }
         }

         return var2;
      }
   }

   public interface TransitionListener {
      void onTransitionCancel(TransitionPort var1);

      void onTransitionEnd(TransitionPort var1);

      void onTransitionPause(TransitionPort var1);

      void onTransitionResume(TransitionPort var1);

      void onTransitionStart(TransitionPort var1);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static class TransitionListenerAdapter implements TransitionPort.TransitionListener {
      public void onTransitionCancel(TransitionPort var1) {
      }

      public void onTransitionEnd(TransitionPort var1) {
      }

      public void onTransitionPause(TransitionPort var1) {
      }

      public void onTransitionResume(TransitionPort var1) {
      }

      public void onTransitionStart(TransitionPort var1) {
      }
   }
}
