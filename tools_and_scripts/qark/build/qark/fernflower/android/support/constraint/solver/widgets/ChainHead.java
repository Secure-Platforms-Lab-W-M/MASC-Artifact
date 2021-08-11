package android.support.constraint.solver.widgets;

import java.util.ArrayList;

public class ChainHead {
   private boolean mDefined;
   protected ConstraintWidget mFirst;
   protected ConstraintWidget mFirstMatchConstraintWidget;
   protected ConstraintWidget mFirstVisibleWidget;
   protected boolean mHasComplexMatchWeights;
   protected boolean mHasDefinedWeights;
   protected boolean mHasUndefinedWeights;
   protected ConstraintWidget mHead;
   private boolean mIsRtl = false;
   protected ConstraintWidget mLast;
   protected ConstraintWidget mLastMatchConstraintWidget;
   protected ConstraintWidget mLastVisibleWidget;
   private int mOrientation;
   protected float mTotalWeight = 0.0F;
   protected ArrayList mWeightedMatchConstraintsWidgets;
   protected int mWidgetsCount;
   protected int mWidgetsMatchCount;

   public ChainHead(ConstraintWidget var1, int var2, boolean var3) {
      this.mFirst = var1;
      this.mOrientation = var2;
      this.mIsRtl = var3;
   }

   private void defineChainProperties() {
      int var3 = this.mOrientation * 2;
      ConstraintWidget var7 = this.mFirst;
      ConstraintWidget var6 = this.mFirst;
      ConstraintWidget var5 = this.mFirst;
      boolean var2 = false;

      while(true) {
         boolean var4 = true;
         if (var2) {
            this.mLast = var6;
            if (this.mOrientation == 0 && this.mIsRtl) {
               this.mHead = this.mLast;
            } else {
               this.mHead = this.mFirst;
            }

            if (!this.mHasDefinedWeights || !this.mHasUndefinedWeights) {
               var4 = false;
            }

            this.mHasComplexMatchWeights = var4;
            return;
         }

         ++this.mWidgetsCount;
         var6.mNextChainWidget[this.mOrientation] = null;
         var6.mListNextMatchConstraintsWidget[this.mOrientation] = null;
         if (var6.getVisibility() != 8) {
            if (this.mFirstVisibleWidget == null) {
               this.mFirstVisibleWidget = var6;
            }

            this.mLastVisibleWidget = var6;
            if (var6.mListDimensionBehaviors[this.mOrientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (var6.mResolvedMatchConstraintDefault[this.mOrientation] == 0 || var6.mResolvedMatchConstraintDefault[this.mOrientation] == 3 || var6.mResolvedMatchConstraintDefault[this.mOrientation] == 2)) {
               ++this.mWidgetsMatchCount;
               float var1 = var6.mWeight[this.mOrientation];
               if (var1 > 0.0F) {
                  this.mTotalWeight += var6.mWeight[this.mOrientation];
               }

               if (isMatchConstraintEqualityCandidate(var6, this.mOrientation)) {
                  if (var1 < 0.0F) {
                     this.mHasUndefinedWeights = true;
                  } else {
                     this.mHasDefinedWeights = true;
                  }

                  if (this.mWeightedMatchConstraintsWidgets == null) {
                     this.mWeightedMatchConstraintsWidgets = new ArrayList();
                  }

                  this.mWeightedMatchConstraintsWidgets.add(var6);
               }

               if (this.mFirstMatchConstraintWidget == null) {
                  this.mFirstMatchConstraintWidget = var6;
               }

               var5 = this.mLastMatchConstraintWidget;
               if (var5 != null) {
                  var5.mListNextMatchConstraintsWidget[this.mOrientation] = var6;
               }

               this.mLastMatchConstraintWidget = var6;
            }
         }

         if (var7 != var6) {
            var7.mNextChainWidget[this.mOrientation] = var6;
         }

         var7 = var6;
         ConstraintAnchor var8 = var6.mListAnchors[var3 + 1].mTarget;
         if (var8 != null) {
            var5 = var8.mOwner;
            if (var5.mListAnchors[var3].mTarget == null || var5.mListAnchors[var3].mTarget.mOwner != var6) {
               var5 = null;
            }
         } else {
            var5 = null;
         }

         if (var5 != null) {
            var6 = var5;
         } else {
            var2 = true;
         }
      }
   }

   private static boolean isMatchConstraintEqualityCandidate(ConstraintWidget var0, int var1) {
      return var0.getVisibility() != 8 && var0.mListDimensionBehaviors[var1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (var0.mResolvedMatchConstraintDefault[var1] == 0 || var0.mResolvedMatchConstraintDefault[var1] == 3);
   }

   public void define() {
      if (!this.mDefined) {
         this.defineChainProperties();
      }

      this.mDefined = true;
   }

   public ConstraintWidget getFirst() {
      return this.mFirst;
   }

   public ConstraintWidget getFirstMatchConstraintWidget() {
      return this.mFirstMatchConstraintWidget;
   }

   public ConstraintWidget getFirstVisibleWidget() {
      return this.mFirstVisibleWidget;
   }

   public ConstraintWidget getHead() {
      return this.mHead;
   }

   public ConstraintWidget getLast() {
      return this.mLast;
   }

   public ConstraintWidget getLastMatchConstraintWidget() {
      return this.mLastMatchConstraintWidget;
   }

   public ConstraintWidget getLastVisibleWidget() {
      return this.mLastVisibleWidget;
   }

   public float getTotalWeight() {
      return this.mTotalWeight;
   }
}
