package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public abstract class Transition implements Cloneable {
   static final boolean DBG = false;
   private static final int[] DEFAULT_MATCH_ORDER = new int[]{2, 1, 3, 4};
   private static final String LOG_TAG = "Transition";
   private static final int MATCH_FIRST = 1;
   public static final int MATCH_ID = 3;
   private static final String MATCH_ID_STR = "id";
   public static final int MATCH_INSTANCE = 1;
   private static final String MATCH_INSTANCE_STR = "instance";
   public static final int MATCH_ITEM_ID = 4;
   private static final String MATCH_ITEM_ID_STR = "itemId";
   private static final int MATCH_LAST = 4;
   public static final int MATCH_NAME = 2;
   private static final String MATCH_NAME_STR = "name";
   private static final PathMotion STRAIGHT_PATH_MOTION = new PathMotion() {
      public Path getPath(float var1, float var2, float var3, float var4) {
         Path var5 = new Path();
         var5.moveTo(var1, var2);
         var5.lineTo(var3, var4);
         return var5;
      }
   };
   private static ThreadLocal sRunningAnimators = new ThreadLocal();
   private ArrayList mAnimators;
   boolean mCanRemoveViews;
   private ArrayList mCurrentAnimators;
   long mDuration = -1L;
   private TransitionValuesMaps mEndValues = new TransitionValuesMaps();
   private ArrayList mEndValuesList;
   private boolean mEnded;
   private Transition.EpicenterCallback mEpicenterCallback;
   private TimeInterpolator mInterpolator = null;
   private ArrayList mListeners;
   private int[] mMatchOrder;
   private String mName = this.getClass().getName();
   private ArrayMap mNameOverrides;
   private int mNumInstances;
   TransitionSet mParent = null;
   private PathMotion mPathMotion;
   private boolean mPaused;
   TransitionPropagation mPropagation;
   private ViewGroup mSceneRoot;
   private long mStartDelay = -1L;
   private TransitionValuesMaps mStartValues = new TransitionValuesMaps();
   private ArrayList mStartValuesList;
   private ArrayList mTargetChildExcludes = null;
   private ArrayList mTargetExcludes = null;
   private ArrayList mTargetIdChildExcludes = null;
   private ArrayList mTargetIdExcludes = null;
   ArrayList mTargetIds = new ArrayList();
   private ArrayList mTargetNameExcludes = null;
   private ArrayList mTargetNames = null;
   private ArrayList mTargetTypeChildExcludes = null;
   private ArrayList mTargetTypeExcludes = null;
   private ArrayList mTargetTypes = null;
   ArrayList mTargets = new ArrayList();

   public Transition() {
      this.mMatchOrder = DEFAULT_MATCH_ORDER;
      this.mSceneRoot = null;
      this.mCanRemoveViews = false;
      this.mCurrentAnimators = new ArrayList();
      this.mNumInstances = 0;
      this.mPaused = false;
      this.mEnded = false;
      this.mListeners = null;
      this.mAnimators = new ArrayList();
      this.mPathMotion = STRAIGHT_PATH_MOTION;
   }

   public Transition(Context var1, AttributeSet var2) {
      this.mMatchOrder = DEFAULT_MATCH_ORDER;
      this.mSceneRoot = null;
      this.mCanRemoveViews = false;
      this.mCurrentAnimators = new ArrayList();
      this.mNumInstances = 0;
      this.mPaused = false;
      this.mEnded = false;
      this.mListeners = null;
      this.mAnimators = new ArrayList();
      this.mPathMotion = STRAIGHT_PATH_MOTION;
      TypedArray var6 = var1.obtainStyledAttributes(var2, Styleable.TRANSITION);
      XmlResourceParser var8 = (XmlResourceParser)var2;
      long var4 = (long)TypedArrayUtils.getNamedInt(var6, var8, "duration", 1, -1);
      if (var4 >= 0L) {
         this.setDuration(var4);
      }

      var4 = (long)TypedArrayUtils.getNamedInt(var6, var8, "startDelay", 2, -1);
      if (var4 > 0L) {
         this.setStartDelay(var4);
      }

      int var3 = TypedArrayUtils.getNamedResourceId(var6, var8, "interpolator", 0, 0);
      if (var3 > 0) {
         this.setInterpolator(AnimationUtils.loadInterpolator(var1, var3));
      }

      String var7 = TypedArrayUtils.getNamedString(var6, var8, "matchOrder", 3);
      if (var7 != null) {
         this.setMatchOrder(parseMatchOrder(var7));
      }

      var6.recycle();
   }

   private void addUnmatched(ArrayMap var1, ArrayMap var2) {
      int var3;
      for(var3 = 0; var3 < var1.size(); ++var3) {
         TransitionValues var4 = (TransitionValues)var1.valueAt(var3);
         if (this.isValidTarget(var4.view)) {
            this.mStartValuesList.add(var4);
            this.mEndValuesList.add((Object)null);
         }
      }

      for(var3 = 0; var3 < var2.size(); ++var3) {
         TransitionValues var5 = (TransitionValues)var2.valueAt(var3);
         if (this.isValidTarget(var5.view)) {
            this.mEndValuesList.add(var5);
            this.mStartValuesList.add((Object)null);
         }
      }

   }

   private static void addViewValues(TransitionValuesMaps var0, View var1, TransitionValues var2) {
      var0.mViewValues.put(var1, var2);
      int var3 = var1.getId();
      if (var3 >= 0) {
         if (var0.mIdValues.indexOfKey(var3) >= 0) {
            var0.mIdValues.put(var3, (Object)null);
         } else {
            var0.mIdValues.put(var3, var1);
         }
      }

      String var6 = ViewCompat.getTransitionName(var1);
      if (var6 != null) {
         if (var0.mNameValues.containsKey(var6)) {
            var0.mNameValues.put(var6, (Object)null);
         } else {
            var0.mNameValues.put(var6, var1);
         }
      }

      if (var1.getParent() instanceof ListView) {
         ListView var7 = (ListView)var1.getParent();
         if (var7.getAdapter().hasStableIds()) {
            long var4 = var7.getItemIdAtPosition(var7.getPositionForView(var1));
            if (var0.mItemIdValues.indexOfKey(var4) >= 0) {
               var1 = (View)var0.mItemIdValues.get(var4);
               if (var1 != null) {
                  ViewCompat.setHasTransientState(var1, false);
                  var0.mItemIdValues.put(var4, (Object)null);
               }

            } else {
               ViewCompat.setHasTransientState(var1, true);
               var0.mItemIdValues.put(var4, var1);
            }
         }
      }
   }

   private static boolean alreadyContains(int[] var0, int var1) {
      int var3 = var0[var1];

      for(int var2 = 0; var2 < var1; ++var2) {
         if (var0[var2] == var3) {
            return true;
         }
      }

      return false;
   }

   private void captureHierarchy(View var1, boolean var2) {
      if (var1 != null) {
         int var4 = var1.getId();
         ArrayList var6 = this.mTargetIdExcludes;
         if (var6 == null || !var6.contains(var4)) {
            var6 = this.mTargetExcludes;
            if (var6 == null || !var6.contains(var1)) {
               var6 = this.mTargetTypeExcludes;
               int var3;
               if (var6 != null) {
                  int var5 = var6.size();

                  for(var3 = 0; var3 < var5; ++var3) {
                     if (((Class)this.mTargetTypeExcludes.get(var3)).isInstance(var1)) {
                        return;
                     }
                  }
               }

               if (var1.getParent() instanceof ViewGroup) {
                  TransitionValues var8 = new TransitionValues();
                  var8.view = var1;
                  if (var2) {
                     this.captureStartValues(var8);
                  } else {
                     this.captureEndValues(var8);
                  }

                  var8.mTargetedTransitions.add(this);
                  this.capturePropagationValues(var8);
                  if (var2) {
                     addViewValues(this.mStartValues, var1, var8);
                  } else {
                     addViewValues(this.mEndValues, var1, var8);
                  }
               }

               if (var1 instanceof ViewGroup) {
                  var6 = this.mTargetIdChildExcludes;
                  if (var6 == null || !var6.contains(var4)) {
                     var6 = this.mTargetChildExcludes;
                     if (var6 == null || !var6.contains(var1)) {
                        var6 = this.mTargetTypeChildExcludes;
                        if (var6 != null) {
                           var4 = var6.size();

                           for(var3 = 0; var3 < var4; ++var3) {
                              if (((Class)this.mTargetTypeChildExcludes.get(var3)).isInstance(var1)) {
                                 return;
                              }
                           }
                        }

                        ViewGroup var7 = (ViewGroup)var1;

                        for(var3 = 0; var3 < var7.getChildCount(); ++var3) {
                           this.captureHierarchy(var7.getChildAt(var3), var2);
                        }

                     }
                  }
               }
            }
         }
      }
   }

   private ArrayList excludeId(ArrayList var1, int var2, boolean var3) {
      if (var2 > 0) {
         return var3 ? Transition.ArrayListManager.add(var1, var2) : Transition.ArrayListManager.remove(var1, var2);
      } else {
         return var1;
      }
   }

   private static ArrayList excludeObject(ArrayList var0, Object var1, boolean var2) {
      if (var1 != null) {
         return var2 ? Transition.ArrayListManager.add(var0, var1) : Transition.ArrayListManager.remove(var0, var1);
      } else {
         return var0;
      }
   }

   private ArrayList excludeType(ArrayList var1, Class var2, boolean var3) {
      if (var2 != null) {
         return var3 ? Transition.ArrayListManager.add(var1, var2) : Transition.ArrayListManager.remove(var1, var2);
      } else {
         return var1;
      }
   }

   private ArrayList excludeView(ArrayList var1, View var2, boolean var3) {
      if (var2 != null) {
         return var3 ? Transition.ArrayListManager.add(var1, var2) : Transition.ArrayListManager.remove(var1, var2);
      } else {
         return var1;
      }
   }

   private static ArrayMap getRunningAnimators() {
      ArrayMap var0 = (ArrayMap)sRunningAnimators.get();
      if (var0 == null) {
         var0 = new ArrayMap();
         sRunningAnimators.set(var0);
         return var0;
      } else {
         return var0;
      }
   }

   private static boolean isValidMatch(int var0) {
      return var0 >= 1 && var0 <= 4;
   }

   private static boolean isValueChanged(TransitionValues var0, TransitionValues var1, String var2) {
      Object var3 = var0.values.get(var2);
      Object var4 = var1.values.get(var2);
      if (var3 == null && var4 == null) {
         return false;
      } else {
         return var3 != null && var4 != null ? var3.equals(var4) ^ true : true;
      }
   }

   private void matchIds(ArrayMap var1, ArrayMap var2, SparseArray var3, SparseArray var4) {
      int var6 = var3.size();

      for(int var5 = 0; var5 < var6; ++var5) {
         View var7 = (View)var3.valueAt(var5);
         if (var7 != null && this.isValidTarget(var7)) {
            View var8 = (View)var4.get(var3.keyAt(var5));
            if (var8 != null && this.isValidTarget(var8)) {
               TransitionValues var9 = (TransitionValues)var1.get(var7);
               TransitionValues var10 = (TransitionValues)var2.get(var8);
               if (var9 != null && var10 != null) {
                  this.mStartValuesList.add(var9);
                  this.mEndValuesList.add(var10);
                  var1.remove(var7);
                  var2.remove(var8);
               }
            }
         }
      }

   }

   private void matchInstances(ArrayMap var1, ArrayMap var2) {
      for(int var3 = var1.size() - 1; var3 >= 0; --var3) {
         View var4 = (View)var1.keyAt(var3);
         if (var4 != null && this.isValidTarget(var4)) {
            TransitionValues var6 = (TransitionValues)var2.remove(var4);
            if (var6 != null && var6.view != null && this.isValidTarget(var6.view)) {
               TransitionValues var5 = (TransitionValues)var1.removeAt(var3);
               this.mStartValuesList.add(var5);
               this.mEndValuesList.add(var6);
            }
         }
      }

   }

   private void matchItemIds(ArrayMap var1, ArrayMap var2, LongSparseArray var3, LongSparseArray var4) {
      int var6 = var3.size();

      for(int var5 = 0; var5 < var6; ++var5) {
         View var7 = (View)var3.valueAt(var5);
         if (var7 != null && this.isValidTarget(var7)) {
            View var8 = (View)var4.get(var3.keyAt(var5));
            if (var8 != null && this.isValidTarget(var8)) {
               TransitionValues var9 = (TransitionValues)var1.get(var7);
               TransitionValues var10 = (TransitionValues)var2.get(var8);
               if (var9 != null && var10 != null) {
                  this.mStartValuesList.add(var9);
                  this.mEndValuesList.add(var10);
                  var1.remove(var7);
                  var2.remove(var8);
               }
            }
         }
      }

   }

   private void matchNames(ArrayMap var1, ArrayMap var2, ArrayMap var3, ArrayMap var4) {
      int var6 = var3.size();

      for(int var5 = 0; var5 < var6; ++var5) {
         View var7 = (View)var3.valueAt(var5);
         if (var7 != null && this.isValidTarget(var7)) {
            View var8 = (View)var4.get(var3.keyAt(var5));
            if (var8 != null && this.isValidTarget(var8)) {
               TransitionValues var9 = (TransitionValues)var1.get(var7);
               TransitionValues var10 = (TransitionValues)var2.get(var8);
               if (var9 != null && var10 != null) {
                  this.mStartValuesList.add(var9);
                  this.mEndValuesList.add(var10);
                  var1.remove(var7);
                  var2.remove(var8);
               }
            }
         }
      }

   }

   private void matchStartAndEnd(TransitionValuesMaps var1, TransitionValuesMaps var2) {
      ArrayMap var4 = new ArrayMap(var1.mViewValues);
      ArrayMap var5 = new ArrayMap(var2.mViewValues);
      int var3 = 0;

      while(true) {
         int[] var6 = this.mMatchOrder;
         if (var3 >= var6.length) {
            this.addUnmatched(var4, var5);
            return;
         }

         switch(var6[var3]) {
         case 1:
            this.matchInstances(var4, var5);
            break;
         case 2:
            this.matchNames(var4, var5, var1.mNameValues, var2.mNameValues);
            break;
         case 3:
            this.matchIds(var4, var5, var1.mIdValues, var2.mIdValues);
            break;
         case 4:
            this.matchItemIds(var4, var5, var1.mItemIdValues, var2.mItemIdValues);
         }

         ++var3;
      }
   }

   private static int[] parseMatchOrder(String var0) {
      StringTokenizer var3 = new StringTokenizer(var0, ",");
      int[] var4 = new int[var3.countTokens()];

      for(int var1 = 0; var3.hasMoreTokens(); ++var1) {
         String var2 = var3.nextToken().trim();
         if ("id".equalsIgnoreCase(var2)) {
            var4[var1] = 3;
         } else if ("instance".equalsIgnoreCase(var2)) {
            var4[var1] = 1;
         } else if ("name".equalsIgnoreCase(var2)) {
            var4[var1] = 2;
         } else if ("itemId".equalsIgnoreCase(var2)) {
            var4[var1] = 4;
         } else {
            if (!var2.isEmpty()) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Unknown match type in matchOrder: '");
               var5.append(var2);
               var5.append("'");
               throw new InflateException(var5.toString());
            }

            int[] var6 = new int[var4.length - 1];
            System.arraycopy(var4, 0, var6, 0, var1);
            var4 = var6;
            --var1;
         }
      }

      return var4;
   }

   private void runAnimator(Animator var1, final ArrayMap var2) {
      if (var1 != null) {
         var1.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               var2.remove(var1);
               Transition.this.mCurrentAnimators.remove(var1);
            }

            public void onAnimationStart(Animator var1) {
               Transition.this.mCurrentAnimators.add(var1);
            }
         });
         this.animate(var1);
      }
   }

   @NonNull
   public Transition addListener(@NonNull Transition.TransitionListener var1) {
      if (this.mListeners == null) {
         this.mListeners = new ArrayList();
      }

      this.mListeners.add(var1);
      return this;
   }

   @NonNull
   public Transition addTarget(@IdRes int var1) {
      if (var1 > 0) {
         this.mTargetIds.add(var1);
         return this;
      } else {
         return this;
      }
   }

   @NonNull
   public Transition addTarget(@NonNull View var1) {
      this.mTargets.add(var1);
      return this;
   }

   @NonNull
   public Transition addTarget(@NonNull Class var1) {
      if (this.mTargetTypes == null) {
         this.mTargetTypes = new ArrayList();
      }

      this.mTargetTypes.add(var1);
      return this;
   }

   @NonNull
   public Transition addTarget(@NonNull String var1) {
      if (this.mTargetNames == null) {
         this.mTargetNames = new ArrayList();
      }

      this.mTargetNames.add(var1);
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
               Transition.this.end();
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

      ArrayList var3 = this.mListeners;
      if (var3 != null && var3.size() > 0) {
         var3 = (ArrayList)this.mListeners.clone();
         int var2 = var3.size();

         for(var1 = 0; var1 < var2; ++var1) {
            ((Transition.TransitionListener)var3.get(var1)).onTransitionCancel(this);
         }

      }
   }

   public abstract void captureEndValues(@NonNull TransitionValues var1);

   void capturePropagationValues(TransitionValues var1) {
      if (this.mPropagation != null && !var1.values.isEmpty()) {
         String[] var5 = this.mPropagation.getPropagationProperties();
         if (var5 != null) {
            boolean var4 = true;
            int var2 = 0;

            boolean var3;
            while(true) {
               var3 = var4;
               if (var2 >= var5.length) {
                  break;
               }

               if (!var1.values.containsKey(var5[var2])) {
                  var3 = false;
                  break;
               }

               ++var2;
            }

            if (!var3) {
               this.mPropagation.captureValues(var1);
            }
         }
      }
   }

   public abstract void captureStartValues(@NonNull TransitionValues var1);

   void captureValues(ViewGroup var1, boolean var2) {
      int var3;
      View var10;
      label106: {
         this.clearValues(var2);
         if (this.mTargetIds.size() > 0 || this.mTargets.size() > 0) {
            ArrayList var5 = this.mTargetNames;
            if (var5 == null || var5.isEmpty()) {
               var5 = this.mTargetTypes;
               if (var5 == null || var5.isEmpty()) {
                  for(var3 = 0; var3 < this.mTargetIds.size(); ++var3) {
                     var10 = var1.findViewById((Integer)this.mTargetIds.get(var3));
                     if (var10 != null) {
                        TransitionValues var6 = new TransitionValues();
                        var6.view = var10;
                        if (var2) {
                           this.captureStartValues(var6);
                        } else {
                           this.captureEndValues(var6);
                        }

                        var6.mTargetedTransitions.add(this);
                        this.capturePropagationValues(var6);
                        if (var2) {
                           addViewValues(this.mStartValues, var10, var6);
                        } else {
                           addViewValues(this.mEndValues, var10, var6);
                        }
                     }
                  }

                  for(var3 = 0; var3 < this.mTargets.size(); ++var3) {
                     View var7 = (View)this.mTargets.get(var3);
                     TransitionValues var11 = new TransitionValues();
                     var11.view = var7;
                     if (var2) {
                        this.captureStartValues(var11);
                     } else {
                        this.captureEndValues(var11);
                     }

                     var11.mTargetedTransitions.add(this);
                     this.capturePropagationValues(var11);
                     if (var2) {
                        addViewValues(this.mStartValues, var7, var11);
                     } else {
                        addViewValues(this.mEndValues, var7, var11);
                     }
                  }
                  break label106;
               }
            }
         }

         this.captureHierarchy(var1, var2);
      }

      if (!var2) {
         ArrayMap var8 = this.mNameOverrides;
         if (var8 != null) {
            int var4 = var8.size();
            ArrayList var9 = new ArrayList(var4);

            for(var3 = 0; var3 < var4; ++var3) {
               String var12 = (String)this.mNameOverrides.keyAt(var3);
               var9.add(this.mStartValues.mNameValues.remove(var12));
            }

            for(var3 = 0; var3 < var4; ++var3) {
               var10 = (View)var9.get(var3);
               if (var10 != null) {
                  String var13 = (String)this.mNameOverrides.valueAt(var3);
                  this.mStartValues.mNameValues.put(var13, var10);
               }
            }

            return;
         }
      }

   }

   void clearValues(boolean var1) {
      if (var1) {
         this.mStartValues.mViewValues.clear();
         this.mStartValues.mIdValues.clear();
         this.mStartValues.mItemIdValues.clear();
      } else {
         this.mEndValues.mViewValues.clear();
         this.mEndValues.mIdValues.clear();
         this.mEndValues.mItemIdValues.clear();
      }
   }

   public Transition clone() {
      try {
         Transition var1 = (Transition)super.clone();
         var1.mAnimators = new ArrayList();
         var1.mStartValues = new TransitionValuesMaps();
         var1.mEndValues = new TransitionValuesMaps();
         var1.mStartValuesList = null;
         var1.mEndValuesList = null;
         return var1;
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   @Nullable
   public Animator createAnimator(@NonNull ViewGroup var1, @Nullable TransitionValues var2, @Nullable TransitionValues var3) {
      return null;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected void createAnimators(ViewGroup var1, TransitionValuesMaps var2, TransitionValuesMaps var3, ArrayList var4, ArrayList var5) {
      ArrayMap var20 = getRunningAnimators();
      long var10 = Long.MAX_VALUE;
      SparseIntArray var19 = new SparseIntArray();
      int var7 = var4.size();

      int var6;
      for(var6 = 0; var6 < var7; ++var6) {
         TransitionValues var15 = (TransitionValues)var4.get(var6);
         TransitionValues var16 = (TransitionValues)var5.get(var6);
         if (var15 != null && !var15.mTargetedTransitions.contains(this)) {
            var15 = null;
         }

         if (var16 != null && !var16.mTargetedTransitions.contains(this)) {
            var16 = null;
         }

         if (var15 != null || var16 != null) {
            boolean var8;
            if (var15 != null && var16 != null && !this.isTransitionRequired(var15, var16)) {
               var8 = false;
            } else {
               var8 = true;
            }

            if (var8) {
               Animator var23 = this.createAnimator(var1, var15, var16);
               if (var23 != null) {
                  TransitionValues var14 = null;
                  View var17;
                  if (var16 != null) {
                     var17 = var16.view;
                     String[] var21 = this.getTransitionProperties();
                     if (var17 != null && var21 != null && var21.length > 0) {
                        TransitionValues var18 = new TransitionValues();
                        var18.view = var17;
                        var14 = (TransitionValues)var3.mViewValues.get(var17);
                        int var24;
                        if (var14 != null) {
                           for(var24 = 0; var24 < var21.length; ++var24) {
                              var18.values.put(var21[var24], var14.values.get(var21[var24]));
                           }
                        }

                        var24 = var20.size();
                        int var9 = 0;

                        while(true) {
                           if (var9 >= var24) {
                              var14 = var18;
                              break;
                           }

                           Transition.AnimationInfo var25 = (Transition.AnimationInfo)var20.get((Animator)var20.keyAt(var9));
                           if (var25.mValues != null && var25.mView == var17 && var25.mName.equals(this.getName()) && var25.mValues.equals(var18)) {
                              var23 = null;
                              var14 = var18;
                              break;
                           }

                           ++var9;
                        }
                     }
                  } else {
                     var17 = var15.view;
                     var14 = null;
                  }

                  if (var23 != null) {
                     TransitionPropagation var26 = this.mPropagation;
                     if (var26 != null) {
                        long var12 = var26.getStartDelay(var1, this, var15, var16);
                        var19.put(this.mAnimators.size(), (int)var12);
                        var10 = Math.min(var12, var10);
                     }

                     var20.put(var23, new Transition.AnimationInfo(var17, this.getName(), this, ViewUtils.getWindowId(var1), var14));
                     this.mAnimators.add(var23);
                  }
               }
            }
         }
      }

      if (var10 != 0L) {
         for(var6 = 0; var6 < var19.size(); ++var6) {
            var7 = var19.keyAt(var6);
            Animator var22 = (Animator)this.mAnimators.get(var7);
            var22.setStartDelay((long)var19.valueAt(var6) - var10 + var22.getStartDelay());
         }

      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected void end() {
      --this.mNumInstances;
      if (this.mNumInstances == 0) {
         ArrayList var3 = this.mListeners;
         int var1;
         if (var3 != null && var3.size() > 0) {
            var3 = (ArrayList)this.mListeners.clone();
            int var2 = var3.size();

            for(var1 = 0; var1 < var2; ++var1) {
               ((Transition.TransitionListener)var3.get(var1)).onTransitionEnd(this);
            }
         }

         View var4;
         for(var1 = 0; var1 < this.mStartValues.mItemIdValues.size(); ++var1) {
            var4 = (View)this.mStartValues.mItemIdValues.valueAt(var1);
            if (var4 != null) {
               ViewCompat.setHasTransientState(var4, false);
            }
         }

         for(var1 = 0; var1 < this.mEndValues.mItemIdValues.size(); ++var1) {
            var4 = (View)this.mEndValues.mItemIdValues.valueAt(var1);
            if (var4 != null) {
               ViewCompat.setHasTransientState(var4, false);
            }
         }

         this.mEnded = true;
      }
   }

   @NonNull
   public Transition excludeChildren(@IdRes int var1, boolean var2) {
      this.mTargetIdChildExcludes = this.excludeId(this.mTargetIdChildExcludes, var1, var2);
      return this;
   }

   @NonNull
   public Transition excludeChildren(@NonNull View var1, boolean var2) {
      this.mTargetChildExcludes = this.excludeView(this.mTargetChildExcludes, var1, var2);
      return this;
   }

   @NonNull
   public Transition excludeChildren(@NonNull Class var1, boolean var2) {
      this.mTargetTypeChildExcludes = this.excludeType(this.mTargetTypeChildExcludes, var1, var2);
      return this;
   }

   @NonNull
   public Transition excludeTarget(@IdRes int var1, boolean var2) {
      this.mTargetIdExcludes = this.excludeId(this.mTargetIdExcludes, var1, var2);
      return this;
   }

   @NonNull
   public Transition excludeTarget(@NonNull View var1, boolean var2) {
      this.mTargetExcludes = this.excludeView(this.mTargetExcludes, var1, var2);
      return this;
   }

   @NonNull
   public Transition excludeTarget(@NonNull Class var1, boolean var2) {
      this.mTargetTypeExcludes = this.excludeType(this.mTargetTypeExcludes, var1, var2);
      return this;
   }

   @NonNull
   public Transition excludeTarget(@NonNull String var1, boolean var2) {
      this.mTargetNameExcludes = excludeObject(this.mTargetNameExcludes, var1, var2);
      return this;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void forceToEnd(ViewGroup var1) {
      ArrayMap var3 = getRunningAnimators();
      int var2 = var3.size();
      if (var1 != null) {
         WindowIdImpl var5 = ViewUtils.getWindowId(var1);
         --var2;

         for(; var2 >= 0; --var2) {
            Transition.AnimationInfo var4 = (Transition.AnimationInfo)var3.valueAt(var2);
            if (var4.mView != null && var5 != null && var5.equals(var4.mWindowId)) {
               ((Animator)var3.keyAt(var2)).end();
            }
         }

      }
   }

   public long getDuration() {
      return this.mDuration;
   }

   @Nullable
   public Rect getEpicenter() {
      Transition.EpicenterCallback var1 = this.mEpicenterCallback;
      return var1 == null ? null : var1.onGetEpicenter(this);
   }

   @Nullable
   public Transition.EpicenterCallback getEpicenterCallback() {
      return this.mEpicenterCallback;
   }

   @Nullable
   public TimeInterpolator getInterpolator() {
      return this.mInterpolator;
   }

   TransitionValues getMatchedTransitionValues(View var1, boolean var2) {
      TransitionSet var7 = this.mParent;
      if (var7 != null) {
         return var7.getMatchedTransitionValues(var1, var2);
      } else {
         ArrayList var10;
         if (var2) {
            var10 = this.mStartValuesList;
         } else {
            var10 = this.mEndValuesList;
         }

         if (var10 == null) {
            return null;
         } else {
            int var6 = var10.size();
            byte var5 = -1;
            int var3 = 0;

            int var4;
            while(true) {
               var4 = var5;
               if (var3 >= var6) {
                  break;
               }

               TransitionValues var8 = (TransitionValues)var10.get(var3);
               if (var8 == null) {
                  return null;
               }

               if (var8.view == var1) {
                  var4 = var3;
                  break;
               }

               ++var3;
            }

            if (var4 >= 0) {
               ArrayList var9;
               if (var2) {
                  var9 = this.mEndValuesList;
               } else {
                  var9 = this.mStartValuesList;
               }

               return (TransitionValues)var9.get(var4);
            } else {
               return null;
            }
         }
      }
   }

   @NonNull
   public String getName() {
      return this.mName;
   }

   @NonNull
   public PathMotion getPathMotion() {
      return this.mPathMotion;
   }

   @Nullable
   public TransitionPropagation getPropagation() {
      return this.mPropagation;
   }

   public long getStartDelay() {
      return this.mStartDelay;
   }

   @NonNull
   public List getTargetIds() {
      return this.mTargetIds;
   }

   @Nullable
   public List getTargetNames() {
      return this.mTargetNames;
   }

   @Nullable
   public List getTargetTypes() {
      return this.mTargetTypes;
   }

   @NonNull
   public List getTargets() {
      return this.mTargets;
   }

   @Nullable
   public String[] getTransitionProperties() {
      return null;
   }

   @Nullable
   public TransitionValues getTransitionValues(@NonNull View var1, boolean var2) {
      TransitionSet var3 = this.mParent;
      if (var3 != null) {
         return var3.getTransitionValues(var1, var2);
      } else {
         TransitionValuesMaps var4;
         if (var2) {
            var4 = this.mStartValues;
         } else {
            var4 = this.mEndValues;
         }

         return (TransitionValues)var4.mViewValues.get(var1);
      }
   }

   public boolean isTransitionRequired(@Nullable TransitionValues var1, @Nullable TransitionValues var2) {
      boolean var6 = false;
      if (var1 != null && var2 != null) {
         String[] var7 = this.getTransitionProperties();
         if (var7 == null) {
            Iterator var8 = var1.values.keySet().iterator();

            do {
               if (!var8.hasNext()) {
                  return false;
               }
            } while(!isValueChanged(var1, var2, (String)var8.next()));

            return true;
         } else {
            int var4 = var7.length;
            int var3 = 0;

            boolean var5;
            while(true) {
               var5 = var6;
               if (var3 >= var4) {
                  break;
               }

               if (isValueChanged(var1, var2, var7[var3])) {
                  var5 = true;
                  break;
               }

               ++var3;
            }

            return var5;
         }
      } else {
         return false;
      }
   }

   boolean isValidTarget(View var1) {
      int var3 = var1.getId();
      ArrayList var5 = this.mTargetIdExcludes;
      if (var5 != null && var5.contains(var3)) {
         return false;
      } else {
         var5 = this.mTargetExcludes;
         if (var5 != null && var5.contains(var1)) {
            return false;
         } else {
            var5 = this.mTargetTypeExcludes;
            int var2;
            if (var5 != null) {
               int var4 = var5.size();

               for(var2 = 0; var2 < var4; ++var2) {
                  if (((Class)this.mTargetTypeExcludes.get(var2)).isInstance(var1)) {
                     return false;
                  }
               }
            }

            if (this.mTargetNameExcludes != null && ViewCompat.getTransitionName(var1) != null && this.mTargetNameExcludes.contains(ViewCompat.getTransitionName(var1))) {
               return false;
            } else {
               if (this.mTargetIds.size() == 0 && this.mTargets.size() == 0) {
                  var5 = this.mTargetTypes;
                  if (var5 == null || var5.isEmpty()) {
                     var5 = this.mTargetNames;
                     if (var5 == null) {
                        return true;
                     }

                     if (var5.isEmpty()) {
                        return true;
                     }
                  }
               }

               if (!this.mTargetIds.contains(var3)) {
                  if (this.mTargets.contains(var1)) {
                     return true;
                  } else {
                     var5 = this.mTargetNames;
                     if (var5 != null && var5.contains(ViewCompat.getTransitionName(var1))) {
                        return true;
                     } else if (this.mTargetTypes != null) {
                        for(var2 = 0; var2 < this.mTargetTypes.size(); ++var2) {
                           if (((Class)this.mTargetTypes.get(var2)).isInstance(var1)) {
                              return true;
                           }
                        }

                        return false;
                     } else {
                        return false;
                     }
                  }
               } else {
                  return true;
               }
            }
         }
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void pause(View var1) {
      if (!this.mEnded) {
         ArrayMap var4 = getRunningAnimators();
         int var2 = var4.size();
         WindowIdImpl var6 = ViewUtils.getWindowId(var1);
         --var2;

         for(; var2 >= 0; --var2) {
            Transition.AnimationInfo var5 = (Transition.AnimationInfo)var4.valueAt(var2);
            if (var5.mView != null && var6.equals(var5.mWindowId)) {
               AnimatorUtils.pause((Animator)var4.keyAt(var2));
            }
         }

         ArrayList var7 = this.mListeners;
         if (var7 != null && var7.size() > 0) {
            var7 = (ArrayList)this.mListeners.clone();
            int var3 = var7.size();

            for(var2 = 0; var2 < var3; ++var2) {
               ((Transition.TransitionListener)var7.get(var2)).onTransitionPause(this);
            }
         }

         this.mPaused = true;
      }
   }

   void playTransition(ViewGroup var1) {
      this.mStartValuesList = new ArrayList();
      this.mEndValuesList = new ArrayList();
      this.matchStartAndEnd(this.mStartValues, this.mEndValues);
      ArrayMap var4 = getRunningAnimators();
      int var2 = var4.size();
      WindowIdImpl var5 = ViewUtils.getWindowId(var1);
      --var2;

      for(; var2 >= 0; --var2) {
         Animator var6 = (Animator)var4.keyAt(var2);
         if (var6 != null) {
            Transition.AnimationInfo var7 = (Transition.AnimationInfo)var4.get(var6);
            if (var7 != null && var7.mView != null && var5.equals(var7.mWindowId)) {
               TransitionValues var8 = var7.mValues;
               View var10 = var7.mView;
               boolean var3 = true;
               TransitionValues var9 = this.getTransitionValues(var10, true);
               TransitionValues var11 = this.getMatchedTransitionValues(var10, true);
               if (var9 == null && var11 == null || !var7.mTransition.isTransitionRequired(var8, var11)) {
                  var3 = false;
               }

               if (var3) {
                  if (!var6.isRunning() && !var6.isStarted()) {
                     var4.remove(var6);
                  } else {
                     var6.cancel();
                  }
               }
            }
         }
      }

      this.createAnimators(var1, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
      this.runAnimators();
   }

   @NonNull
   public Transition removeListener(@NonNull Transition.TransitionListener var1) {
      ArrayList var2 = this.mListeners;
      if (var2 == null) {
         return this;
      } else {
         var2.remove(var1);
         if (this.mListeners.size() == 0) {
            this.mListeners = null;
            return this;
         } else {
            return this;
         }
      }
   }

   @NonNull
   public Transition removeTarget(@IdRes int var1) {
      if (var1 > 0) {
         this.mTargetIds.remove(var1);
         return this;
      } else {
         return this;
      }
   }

   @NonNull
   public Transition removeTarget(@NonNull View var1) {
      this.mTargets.remove(var1);
      return this;
   }

   @NonNull
   public Transition removeTarget(@NonNull Class var1) {
      ArrayList var2 = this.mTargetTypes;
      if (var2 != null) {
         var2.remove(var1);
         return this;
      } else {
         return this;
      }
   }

   @NonNull
   public Transition removeTarget(@NonNull String var1) {
      ArrayList var2 = this.mTargetNames;
      if (var2 != null) {
         var2.remove(var1);
         return this;
      } else {
         return this;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void resume(View var1) {
      if (this.mPaused) {
         if (!this.mEnded) {
            ArrayMap var4 = getRunningAnimators();
            int var2 = var4.size();
            WindowIdImpl var6 = ViewUtils.getWindowId(var1);
            --var2;

            for(; var2 >= 0; --var2) {
               Transition.AnimationInfo var5 = (Transition.AnimationInfo)var4.valueAt(var2);
               if (var5.mView != null && var6.equals(var5.mWindowId)) {
                  AnimatorUtils.resume((Animator)var4.keyAt(var2));
               }
            }

            ArrayList var7 = this.mListeners;
            if (var7 != null && var7.size() > 0) {
               var7 = (ArrayList)this.mListeners.clone();
               int var3 = var7.size();

               for(var2 = 0; var2 < var3; ++var2) {
                  ((Transition.TransitionListener)var7.get(var2)).onTransitionResume(this);
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

   @NonNull
   public Transition setDuration(long var1) {
      this.mDuration = var1;
      return this;
   }

   public void setEpicenterCallback(@Nullable Transition.EpicenterCallback var1) {
      this.mEpicenterCallback = var1;
   }

   @NonNull
   public Transition setInterpolator(@Nullable TimeInterpolator var1) {
      this.mInterpolator = var1;
      return this;
   }

   public void setMatchOrder(int... var1) {
      if (var1 != null && var1.length != 0) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (!isValidMatch(var1[var2])) {
               throw new IllegalArgumentException("matches contains invalid value");
            }

            if (alreadyContains(var1, var2)) {
               throw new IllegalArgumentException("matches contains a duplicate value");
            }
         }

         this.mMatchOrder = (int[])var1.clone();
      } else {
         this.mMatchOrder = DEFAULT_MATCH_ORDER;
      }
   }

   public void setPathMotion(@Nullable PathMotion var1) {
      if (var1 == null) {
         this.mPathMotion = STRAIGHT_PATH_MOTION;
      } else {
         this.mPathMotion = var1;
      }
   }

   public void setPropagation(@Nullable TransitionPropagation var1) {
      this.mPropagation = var1;
   }

   Transition setSceneRoot(ViewGroup var1) {
      this.mSceneRoot = var1;
      return this;
   }

   @NonNull
   public Transition setStartDelay(long var1) {
      this.mStartDelay = var1;
      return this;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected void start() {
      if (this.mNumInstances == 0) {
         ArrayList var3 = this.mListeners;
         if (var3 != null && var3.size() > 0) {
            var3 = (ArrayList)this.mListeners.clone();
            int var2 = var3.size();

            for(int var1 = 0; var1 < var2; ++var1) {
               ((Transition.TransitionListener)var3.get(var1)).onTransitionStart(this);
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
      StringBuilder var3 = new StringBuilder();
      var3.append(var1);
      var3.append(this.getClass().getSimpleName());
      var3.append("@");
      var3.append(Integer.toHexString(this.hashCode()));
      var3.append(": ");
      var1 = var3.toString();
      if (this.mDuration != -1L) {
         var3 = new StringBuilder();
         var3.append(var1);
         var3.append("dur(");
         var3.append(this.mDuration);
         var3.append(") ");
         var1 = var3.toString();
      }

      if (this.mStartDelay != -1L) {
         var3 = new StringBuilder();
         var3.append(var1);
         var3.append("dly(");
         var3.append(this.mStartDelay);
         var3.append(") ");
         var1 = var3.toString();
      }

      if (this.mInterpolator != null) {
         var3 = new StringBuilder();
         var3.append(var1);
         var3.append("interp(");
         var3.append(this.mInterpolator);
         var3.append(") ");
         var1 = var3.toString();
      }

      if (this.mTargetIds.size() <= 0 && this.mTargets.size() <= 0) {
         return var1;
      } else {
         var3 = new StringBuilder();
         var3.append(var1);
         var3.append("tgts(");
         var1 = var3.toString();
         int var2;
         if (this.mTargetIds.size() > 0) {
            for(var2 = 0; var2 < this.mTargetIds.size(); ++var2) {
               if (var2 > 0) {
                  var3 = new StringBuilder();
                  var3.append(var1);
                  var3.append(", ");
                  var1 = var3.toString();
               }

               var3 = new StringBuilder();
               var3.append(var1);
               var3.append(this.mTargetIds.get(var2));
               var1 = var3.toString();
            }
         }

         if (this.mTargets.size() > 0) {
            for(var2 = 0; var2 < this.mTargets.size(); ++var2) {
               if (var2 > 0) {
                  var3 = new StringBuilder();
                  var3.append(var1);
                  var3.append(", ");
                  var1 = var3.toString();
               }

               var3 = new StringBuilder();
               var3.append(var1);
               var3.append(this.mTargets.get(var2));
               var1 = var3.toString();
            }
         }

         var3 = new StringBuilder();
         var3.append(var1);
         var3.append(")");
         return var3.toString();
      }
   }

   private static class AnimationInfo {
      String mName;
      Transition mTransition;
      TransitionValues mValues;
      View mView;
      WindowIdImpl mWindowId;

      AnimationInfo(View var1, String var2, Transition var3, WindowIdImpl var4, TransitionValues var5) {
         this.mView = var1;
         this.mName = var2;
         this.mValues = var5;
         this.mWindowId = var4;
         this.mTransition = var3;
      }
   }

   private static class ArrayListManager {
      static ArrayList add(ArrayList var0, Object var1) {
         if (var0 == null) {
            var0 = new ArrayList();
         }

         if (!var0.contains(var1)) {
            var0.add(var1);
            return var0;
         } else {
            return var0;
         }
      }

      static ArrayList remove(ArrayList var0, Object var1) {
         if (var0 != null) {
            var0.remove(var1);
            return var0.isEmpty() ? null : var0;
         } else {
            return var0;
         }
      }
   }

   public abstract static class EpicenterCallback {
      public abstract Rect onGetEpicenter(@NonNull Transition var1);
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface MatchOrder {
   }

   public interface TransitionListener {
      void onTransitionCancel(@NonNull Transition var1);

      void onTransitionEnd(@NonNull Transition var1);

      void onTransitionPause(@NonNull Transition var1);

      void onTransitionResume(@NonNull Transition var1);

      void onTransitionStart(@NonNull Transition var1);
   }
}
