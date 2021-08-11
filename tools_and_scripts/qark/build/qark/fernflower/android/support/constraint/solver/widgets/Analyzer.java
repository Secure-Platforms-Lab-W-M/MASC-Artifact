package android.support.constraint.solver.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Analyzer {
   private Analyzer() {
   }

   public static void determineGroups(ConstraintWidgetContainer var0) {
      if ((var0.getOptimizationLevel() & 32) != 32) {
         singleGroup(var0);
      } else {
         var0.mSkipSolver = true;
         var0.mGroupsWrapOptimized = false;
         var0.mHorizontalWrapOptimized = false;
         var0.mVerticalWrapOptimized = false;
         ArrayList var7 = var0.mChildren;
         List var6 = var0.mWidgetGroups;
         boolean var1;
         if (var0.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            var1 = true;
         } else {
            var1 = false;
         }

         boolean var2;
         if (var0.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            var2 = true;
         } else {
            var2 = false;
         }

         boolean var5;
         if (!var1 && !var2) {
            var5 = false;
         } else {
            var5 = true;
         }

         var6.clear();
         Iterator var8 = var7.iterator();

         while(var8.hasNext()) {
            ConstraintWidget var9 = (ConstraintWidget)var8.next();
            var9.mBelongingGroup = null;
            var9.mGroupsToSolver = false;
            var9.resetResolutionNodes();
         }

         Iterator var10 = var7.iterator();

         while(var10.hasNext()) {
            ConstraintWidget var11 = (ConstraintWidget)var10.next();
            if (var11.mBelongingGroup == null && !determineGroups(var11, var6, var5)) {
               singleGroup(var0);
               var0.mSkipSolver = false;
               return;
            }
         }

         int var4 = 0;
         int var3 = 0;

         ConstraintWidgetGroup var12;
         for(var10 = var6.iterator(); var10.hasNext(); var3 = Math.max(var3, getMaxDimension(var12, 1))) {
            var12 = (ConstraintWidgetGroup)var10.next();
            var4 = Math.max(var4, getMaxDimension(var12, 0));
         }

         if (var1) {
            var0.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            var0.setWidth(var4);
            var0.mGroupsWrapOptimized = true;
            var0.mHorizontalWrapOptimized = true;
            var0.mWrapFixedWidth = var4;
         }

         if (var2) {
            var0.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            var0.setHeight(var3);
            var0.mGroupsWrapOptimized = true;
            var0.mVerticalWrapOptimized = true;
            var0.mWrapFixedHeight = var3;
         }

         setPosition(var6, 0, var0.getWidth());
         setPosition(var6, 1, var0.getHeight());
      }
   }

   private static boolean determineGroups(ConstraintWidget var0, List var1, boolean var2) {
      ConstraintWidgetGroup var3 = new ConstraintWidgetGroup(new ArrayList(), true);
      var1.add(var3);
      return traverse(var0, var3, var1, var2);
   }

   private static int getMaxDimension(ConstraintWidgetGroup var0, int var1) {
      int var3 = 0;
      int var4 = var1 * 2;
      List var7 = var0.getStartWidgets(var1);
      int var5 = var7.size();

      for(int var2 = 0; var2 < var5; ++var2) {
         ConstraintWidget var8 = (ConstraintWidget)var7.get(var2);
         boolean var6;
         if (var8.mListAnchors[var4 + 1].mTarget == null || var8.mListAnchors[var4].mTarget != null && var8.mListAnchors[var4 + 1].mTarget != null) {
            var6 = true;
         } else {
            var6 = false;
         }

         var3 = Math.max(var3, getMaxDimensionTraversal(var8, var1, var6, 0));
      }

      var0.mGroupDimensions[var1] = var3;
      return var3;
   }

   private static int getMaxDimensionTraversal(ConstraintWidget var0, int var1, boolean var2, int var3) {
      boolean var17 = var0.mOptimizerMeasurable;
      boolean var4 = false;
      if (!var17) {
         return 0;
      } else {
         byte var13 = 0;
         byte var14 = 0;
         boolean var7 = var4;
         if (var0.mBaseline.mTarget != null) {
            var7 = var4;
            if (var1 == 1) {
               var7 = true;
            }
         }

         int var5;
         int var9;
         int var10;
         int var20;
         if (var2) {
            var9 = var0.getBaselineDistance();
            var10 = var0.getHeight() - var0.getBaselineDistance();
            var20 = var1 * 2;
            var5 = var20 + 1;
         } else {
            var9 = var0.getHeight() - var0.getBaselineDistance();
            var10 = var0.getBaselineDistance();
            var5 = var1 * 2;
            var20 = var5 + 1;
         }

         int var6;
         byte var8;
         if (var0.mListAnchors[var5].mTarget != null && var0.mListAnchors[var20].mTarget == null) {
            var8 = -1;
            var6 = var20;
            var20 = var5;
            var5 = var6;
         } else {
            var8 = 1;
         }

         int var11;
         if (var7) {
            var11 = var3 - var9;
         } else {
            var11 = var3;
         }

         int var15 = var0.mListAnchors[var20].getMargin() * var8 + getParentBiasOffset(var0, var1);
         int var16 = var15 + var11;
         if (var1 == 0) {
            var3 = var0.getWidth();
         } else {
            var3 = var0.getHeight();
         }

         int var12 = var3 * var8;
         Iterator var18 = var0.mListAnchors[var20].getResolutionNode().dependents.iterator();

         for(var3 = var13; var18.hasNext(); var3 = Math.max(var3, getMaxDimensionTraversal(((ResolutionAnchor)((ResolutionNode)var18.next())).myAnchor.mOwner, var1, var2, var16))) {
         }

         var18 = var0.mListAnchors[var5].getResolutionNode().dependents.iterator();
         var6 = var5;

         for(var5 = var14; var18.hasNext(); var5 = Math.max(var5, getMaxDimensionTraversal(((ResolutionAnchor)((ResolutionNode)var18.next())).myAnchor.mOwner, var1, var2, var12 + var16))) {
         }

         int var22;
         int var23;
         if (var7) {
            var22 = var3 - var9;
            var23 = var5 + var10;
         } else {
            if (var1 == 0) {
               var22 = var0.getWidth();
            } else {
               var22 = var0.getHeight();
            }

            var23 = var5 + var22 * var8;
            var22 = var3;
         }

         var3 = 0;
         var5 = 0;
         if (var1 == 1) {
            var18 = var0.mBaseline.getResolutionNode().dependents.iterator();

            while(var18.hasNext()) {
               ResolutionAnchor var19 = (ResolutionAnchor)((ResolutionNode)var18.next());
               if (var8 == 1) {
                  var5 = Math.max(var5, getMaxDimensionTraversal(var19.myAnchor.mOwner, var1, var2, var9 + var16));
               } else {
                  var5 = Math.max(var5, getMaxDimensionTraversal(var19.myAnchor.mOwner, var1, var2, var10 * var8 + var16));
               }
            }

            var12 = var12;
            if (var0.mBaseline.getResolutionNode().dependents.size() > 0 && !var7) {
               if (var8 == 1) {
                  var3 = var5 + var9;
               } else {
                  var3 = var5 - var10;
               }
            } else {
               var3 = var5;
            }
         }

         var9 = var15 + Math.max(var22, Math.max(var23, var3));
         var3 = var11 + var15;
         var5 = var3 + var12;
         int var21;
         if (var8 == -1) {
            var21 = var3;
         } else {
            var21 = var5;
            var5 = var3;
         }

         if (var2) {
            Optimizer.setOptimizedWidget(var0, var1, var5);
            var0.setFrame(var5, var21, var1);
         } else {
            var0.mBelongingGroup.addWidgetsToSet(var0, var1);
            var0.setRelativePositioning(var5, var1);
         }

         if (var0.getDimensionBehaviour(var1) == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var0.mDimensionRatio != 0.0F) {
            var0.mBelongingGroup.addWidgetsToSet(var0, var1);
         }

         if (var0.mListAnchors[var20].mTarget != null && var0.mListAnchors[var6].mTarget != null) {
            ConstraintWidget var24 = var0.getParent();
            if (var0.mListAnchors[var20].mTarget.mOwner == var24 && var0.mListAnchors[var6].mTarget.mOwner == var24) {
               var0.mBelongingGroup.addWidgetsToSet(var0, var1);
               return var9;
            } else {
               return var9;
            }
         } else {
            return var9;
         }
      }
   }

   private static int getParentBiasOffset(ConstraintWidget var0, int var1) {
      int var3 = var1 * 2;
      ConstraintAnchor var4 = var0.mListAnchors[var3];
      ConstraintAnchor var5 = var0.mListAnchors[var3 + 1];
      if (var4.mTarget != null && var4.mTarget.mOwner == var0.mParent && var5.mTarget != null && var5.mTarget.mOwner == var0.mParent) {
         var3 = var0.mParent.getLength(var1);
         float var2;
         if (var1 == 0) {
            var2 = var0.mHorizontalBiasPercent;
         } else {
            var2 = var0.mVerticalBiasPercent;
         }

         var1 = var0.getLength(var1);
         return (int)((float)(var3 - var4.getMargin() - var5.getMargin() - var1) * var2);
      } else {
         return 0;
      }
   }

   private static void invalidate(ConstraintWidgetContainer var0, ConstraintWidget var1, ConstraintWidgetGroup var2) {
      var2.mSkipSolver = false;
      var0.mSkipSolver = false;
      var1.mOptimizerMeasurable = false;
   }

   private static int resolveDimensionRatio(ConstraintWidget var0) {
      int var1;
      if (var0.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
         if (var0.mDimensionRatioSide == 0) {
            var1 = (int)((float)var0.getHeight() * var0.mDimensionRatio);
         } else {
            var1 = (int)((float)var0.getHeight() / var0.mDimensionRatio);
         }

         var0.setWidth(var1);
         return var1;
      } else if (var0.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
         if (var0.mDimensionRatioSide == 1) {
            var1 = (int)((float)var0.getWidth() * var0.mDimensionRatio);
         } else {
            var1 = (int)((float)var0.getWidth() / var0.mDimensionRatio);
         }

         var0.setHeight(var1);
         return var1;
      } else {
         return -1;
      }
   }

   private static void setConnection(ConstraintAnchor var0) {
      ResolutionAnchor var1 = var0.getResolutionNode();
      if (var0.mTarget != null && var0.mTarget.mTarget != var0) {
         var0.mTarget.getResolutionNode().addDependent(var1);
      }
   }

   public static void setPosition(List var0, int var1, int var2) {
      int var4 = var0.size();

      for(int var3 = 0; var3 < var4; ++var3) {
         Iterator var5 = ((ConstraintWidgetGroup)var0.get(var3)).getWidgetsToSet(var1).iterator();

         while(var5.hasNext()) {
            ConstraintWidget var6 = (ConstraintWidget)var5.next();
            if (var6.mOptimizerMeasurable) {
               updateSizeDependentWidgets(var6, var1, var2);
            }
         }
      }

   }

   private static void singleGroup(ConstraintWidgetContainer var0) {
      var0.mWidgetGroups.clear();
      var0.mWidgetGroups.add(0, new ConstraintWidgetGroup(var0.mChildren));
   }

   private static boolean traverse(ConstraintWidget var0, ConstraintWidgetGroup var1, List var2, boolean var3) {
      if (var0 == null) {
         return true;
      } else {
         var0.mOptimizerMeasured = false;
         ConstraintWidgetContainer var6 = (ConstraintWidgetContainer)var0.getParent();
         if (var0.mBelongingGroup == null) {
            var0.mOptimizerMeasurable = true;
            var1.mConstrainedGroup.add(var0);
            var0.mBelongingGroup = var1;
            if (var0.mLeft.mTarget == null && var0.mRight.mTarget == null && var0.mTop.mTarget == null && var0.mBottom.mTarget == null && var0.mBaseline.mTarget == null && var0.mCenter.mTarget == null) {
               invalidate(var6, var0, var1);
               if (var3) {
                  return false;
               }
            }

            if (var0.mTop.mTarget != null && var0.mBottom.mTarget != null) {
               if (var6.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               }

               if (var3) {
                  invalidate(var6, var0, var1);
                  return false;
               }

               if (var0.mTop.mTarget.mOwner != var0.getParent() || var0.mBottom.mTarget.mOwner != var0.getParent()) {
                  invalidate(var6, var0, var1);
               }
            }

            if (var0.mLeft.mTarget != null && var0.mRight.mTarget != null) {
               if (var6.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               }

               if (var3) {
                  invalidate(var6, var0, var1);
                  return false;
               }

               if (var0.mLeft.mTarget.mOwner != var0.getParent() || var0.mRight.mTarget.mOwner != var0.getParent()) {
                  invalidate(var6, var0, var1);
               }
            }

            boolean var4;
            if (var0.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
               var4 = true;
            } else {
               var4 = false;
            }

            boolean var5;
            if (var0.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
               var5 = true;
            } else {
               var5 = false;
            }

            if (var4 ^ var5 && var0.mDimensionRatio != 0.0F) {
               resolveDimensionRatio(var0);
            } else if (var0.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || var0.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
               invalidate(var6, var0, var1);
               if (var3) {
                  return false;
               }
            }

            if ((var0.mLeft.mTarget == null && var0.mRight.mTarget == null || var0.mLeft.mTarget != null && var0.mLeft.mTarget.mOwner == var0.mParent && var0.mRight.mTarget == null || var0.mRight.mTarget != null && var0.mRight.mTarget.mOwner == var0.mParent && var0.mLeft.mTarget == null || var0.mLeft.mTarget != null && var0.mLeft.mTarget.mOwner == var0.mParent && var0.mRight.mTarget != null && var0.mRight.mTarget.mOwner == var0.mParent) && var0.mCenter.mTarget == null && !(var0 instanceof Guideline) && !(var0 instanceof Helper)) {
               var1.mStartHorizontalWidgets.add(var0);
            }

            if ((var0.mTop.mTarget == null && var0.mBottom.mTarget == null || var0.mTop.mTarget != null && var0.mTop.mTarget.mOwner == var0.mParent && var0.mBottom.mTarget == null || var0.mBottom.mTarget != null && var0.mBottom.mTarget.mOwner == var0.mParent && var0.mTop.mTarget == null || var0.mTop.mTarget != null && var0.mTop.mTarget.mOwner == var0.mParent && var0.mBottom.mTarget != null && var0.mBottom.mTarget.mOwner == var0.mParent) && var0.mCenter.mTarget == null && var0.mBaseline.mTarget == null && !(var0 instanceof Guideline) && !(var0 instanceof Helper)) {
               var1.mStartVerticalWidgets.add(var0);
            }

            int var9;
            if (var0 instanceof Helper) {
               invalidate(var6, var0, var1);
               if (var3) {
                  return false;
               }

               Helper var7 = (Helper)var0;

               for(var9 = 0; var9 < var7.mWidgetsCount; ++var9) {
                  if (!traverse(var7.mWidgets[var9], var1, var2, var3)) {
                     return false;
                  }
               }
            }

            int var10 = var0.mListAnchors.length;

            for(var9 = 0; var9 < var10; ++var9) {
               ConstraintAnchor var11 = var0.mListAnchors[var9];
               if (var11.mTarget != null && var11.mTarget.mOwner != var0.getParent()) {
                  if (var11.mType == ConstraintAnchor.Type.CENTER) {
                     invalidate(var6, var0, var1);
                     if (var3) {
                        return false;
                     }
                  } else {
                     setConnection(var11);
                  }

                  if (!traverse(var11.mTarget.mOwner, var1, var2, var3)) {
                     return false;
                  }
               }
            }

            return true;
         } else if (var0.mBelongingGroup == var1) {
            return true;
         } else {
            var1.mConstrainedGroup.addAll(var0.mBelongingGroup.mConstrainedGroup);
            var1.mStartHorizontalWidgets.addAll(var0.mBelongingGroup.mStartHorizontalWidgets);
            var1.mStartVerticalWidgets.addAll(var0.mBelongingGroup.mStartVerticalWidgets);
            if (!var0.mBelongingGroup.mSkipSolver) {
               var1.mSkipSolver = false;
            }

            var2.remove(var0.mBelongingGroup);

            for(Iterator var8 = var0.mBelongingGroup.mConstrainedGroup.iterator(); var8.hasNext(); ((ConstraintWidget)var8.next()).mBelongingGroup = var1) {
            }

            return true;
         }
      }
   }

   private static void updateSizeDependentWidgets(ConstraintWidget var0, int var1, int var2) {
      int var4 = var1 * 2;
      ConstraintAnchor var5 = var0.mListAnchors[var4];
      ConstraintAnchor var6 = var0.mListAnchors[var4 + 1];
      boolean var3;
      if (var5.mTarget != null && var6.mTarget != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (var3) {
         Optimizer.setOptimizedWidget(var0, var1, getParentBiasOffset(var0, var1) + var5.getMargin());
      } else {
         int var7;
         if (var0.mDimensionRatio != 0.0F && var0.getDimensionBehaviour(var1) == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            var2 = resolveDimensionRatio(var0);
            var7 = (int)var0.mListAnchors[var4].getResolutionNode().resolvedOffset;
            var6.getResolutionNode().resolvedTarget = var5.getResolutionNode();
            var6.getResolutionNode().resolvedOffset = (float)var2;
            var6.getResolutionNode().state = 1;
            var0.setFrame(var7, var7 + var2, var1);
         } else {
            var2 -= var0.getRelativePositioning(var1);
            var7 = var2 - var0.getLength(var1);
            var0.setFrame(var7, var2, var1);
            Optimizer.setOptimizedWidget(var0, var1, var7);
         }
      }
   }
}
