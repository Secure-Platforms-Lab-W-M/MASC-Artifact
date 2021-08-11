package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;

class Chain {
   private static final boolean DEBUG = false;

   static void applyChainConstraints(ConstraintWidgetContainer var0, LinearSystem var1, int var2) {
      byte var3;
      int var4;
      ChainHead[] var6;
      if (var2 == 0) {
         var3 = 0;
         var4 = var0.mHorizontalChainsSize;
         var6 = var0.mHorizontalChainsArray;
      } else {
         var3 = 2;
         var4 = var0.mVerticalChainsSize;
         var6 = var0.mVerticalChainsArray;
      }

      for(int var5 = 0; var5 < var4; ++var5) {
         ChainHead var7 = var6[var5];
         var7.define();
         if (var0.optimizeFor(4)) {
            if (!Optimizer.applyChainOptimized(var0, var1, var2, var3, var7)) {
               applyChainConstraints(var0, var1, var2, var3, var7);
            }
         } else {
            applyChainConstraints(var0, var1, var2, var3, var7);
         }
      }

   }

   static void applyChainConstraints(ConstraintWidgetContainer var0, LinearSystem var1, int var2, int var3, ChainHead var4) {
      ConstraintWidget var19 = var4.mFirst;
      ConstraintWidget var23 = var4.mLast;
      ConstraintWidget var16 = var4.mFirstVisibleWidget;
      ConstraintWidget var24 = var4.mLastVisibleWidget;
      ConstraintWidget var22 = var4.mHead;
      float var5 = var4.mTotalWeight;
      ConstraintWidget var20 = var4.mFirstMatchConstraintWidget;
      ConstraintWidget var18 = var4.mLastMatchConstraintWidget;
      boolean var12;
      if (var0.mListDimensionBehaviors[var2] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
         var12 = true;
      } else {
         var12 = false;
      }

      boolean var8;
      int var9;
      boolean var10;
      boolean var11;
      boolean var13;
      ConstraintWidget var15;
      boolean var36;
      if (var2 == 0) {
         if (var22.mHorizontalChainStyle == 0) {
            var8 = true;
         } else {
            var8 = false;
         }

         var9 = var22.mHorizontalChainStyle;
         var11 = var8;
         if (var9 == 1) {
            var8 = true;
         } else {
            var8 = false;
         }

         if (var22.mHorizontalChainStyle == 2) {
            var36 = true;
         } else {
            var36 = false;
         }

         var10 = var8;
         var15 = var19;
         var8 = false;
         var13 = var36;
      } else {
         if (var22.mVerticalChainStyle == 0) {
            var8 = true;
         } else {
            var8 = false;
         }

         var9 = var22.mVerticalChainStyle;
         var11 = var8;
         if (var9 == 1) {
            var8 = true;
         } else {
            var8 = false;
         }

         if (var22.mVerticalChainStyle == 2) {
            var36 = true;
         } else {
            var36 = false;
         }

         var15 = var19;
         boolean var14 = false;
         var10 = var8;
         var13 = var36;
         var8 = var14;
      }

      ConstraintAnchor var17;
      ConstraintWidget var44;
      while(!var8) {
         var17 = var15.mListAnchors[var3];
         byte var37 = 4;
         if (var12 || var13) {
            var37 = 1;
         }

         int var40 = var17.getMargin();
         if (var17.mTarget != null && var15 != var19) {
            var40 += var17.mTarget.getMargin();
         }

         if (var13 && var15 != var19 && var15 != var16) {
            var37 = 6;
         } else if (var11 && var12) {
            var37 = 4;
         }

         if (var17.mTarget != null) {
            if (var15 == var16) {
               var1.addGreaterThan(var17.mSolverVariable, var17.mTarget.mSolverVariable, var40, 5);
            } else {
               var1.addGreaterThan(var17.mSolverVariable, var17.mTarget.mSolverVariable, var40, 6);
            }

            var1.addEquality(var17.mSolverVariable, var17.mTarget.mSolverVariable, var40, var37);
         }

         if (var12) {
            if (var15.getVisibility() != 8 && var15.mListDimensionBehaviors[var2] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
               var1.addGreaterThan(var15.mListAnchors[var3 + 1].mSolverVariable, var15.mListAnchors[var3].mSolverVariable, 0, 5);
            }

            var1.addGreaterThan(var15.mListAnchors[var3].mSolverVariable, var0.mListAnchors[var3].mSolverVariable, 0, 6);
         }

         var17 = var15.mListAnchors[var3 + 1].mTarget;
         if (var17 != null) {
            var44 = var17.mOwner;
            if (var44.mListAnchors[var3].mTarget == null || var44.mListAnchors[var3].mTarget.mOwner != var15) {
               var44 = null;
            }
         } else {
            var44 = null;
         }

         if (var44 != null) {
            var15 = var44;
         } else {
            var8 = true;
         }
      }

      if (var24 != null && var23.mListAnchors[var3 + 1].mTarget != null) {
         var17 = var24.mListAnchors[var3 + 1];
         var1.addLowerThan(var17.mSolverVariable, var23.mListAnchors[var3 + 1].mTarget.mSolverVariable, -var17.getMargin(), 5);
      }

      if (var12) {
         var1.addGreaterThan(var0.mListAnchors[var3 + 1].mSolverVariable, var23.mListAnchors[var3 + 1].mSolverVariable, var23.mListAnchors[var3 + 1].getMargin(), 6);
      }

      ArrayList var29 = var4.mWeightedMatchConstraintsWidgets;
      SolverVariable var25;
      int var38;
      SolverVariable var48;
      if (var29 != null) {
         var38 = var29.size();
         if (var38 > 1) {
            float var6;
            if (var4.mHasUndefinedWeights && !var4.mHasComplexMatchWeights) {
               var6 = (float)var4.mWidgetsMatchCount;
            } else {
               var6 = var5;
            }

            ConstraintWidget var21 = null;
            var9 = 0;
            float var7 = 0.0F;

            for(var20 = var21; var9 < var38; var7 = var5) {
               label424: {
                  var21 = (ConstraintWidget)var29.get(var9);
                  var5 = var21.mWeight[var2];
                  if (var5 < 0.0F) {
                     if (var4.mHasComplexMatchWeights) {
                        var1.addEquality(var21.mListAnchors[var3 + 1].mSolverVariable, var21.mListAnchors[var3].mSolverVariable, 0, 4);
                        var5 = var7;
                        break label424;
                     }

                     var5 = 1.0F;
                  }

                  if (var5 == 0.0F) {
                     var1.addEquality(var21.mListAnchors[var3 + 1].mSolverVariable, var21.mListAnchors[var3].mSolverVariable, 0, 6);
                     var5 = var7;
                  } else {
                     if (var20 != null) {
                        var25 = var20.mListAnchors[var3].mSolverVariable;
                        var48 = var20.mListAnchors[var3 + 1].mSolverVariable;
                        SolverVariable var26 = var21.mListAnchors[var3].mSolverVariable;
                        SolverVariable var27 = var21.mListAnchors[var3 + 1].mSolverVariable;
                        ArrayRow var28 = var1.createRow();
                        var28.createRowEqualMatchDimensions(var7, var6, var5, var25, var48, var26, var27);
                        var1.addConstraint(var28);
                     }

                     var20 = var21;
                  }
               }

               ++var9;
            }
         }
      }

      SolverVariable var30;
      ConstraintAnchor var32;
      SolverVariable var33;
      ConstraintAnchor var42;
      if (var16 == null || var16 != var24 && !var13) {
         ConstraintWidget var31;
         ConstraintWidget var34;
         byte var41;
         ConstraintAnchor var45;
         SolverVariable var46;
         SolverVariable var47;
         ConstraintAnchor var49;
         SolverVariable var50;
         SolverVariable var51;
         if (var11 && var16 != null) {
            if (var4.mWidgetsMatchCount > 0 && var4.mWidgetsCount == var4.mWidgetsMatchCount) {
               var12 = true;
            } else {
               var12 = false;
            }

            var15 = var16;

            for(var31 = var16; var15 != null; var15 = var34) {
               for(var34 = var15.mNextChainWidget[var2]; var34 != null && var34.getVisibility() == 8; var34 = var34.mNextChainWidget[var2]) {
               }

               if (var34 != null || var15 == var24) {
                  var49 = var15.mListAnchors[var3];
                  var25 = var49.mSolverVariable;
                  if (var49.mTarget != null) {
                     var47 = var49.mTarget.mSolverVariable;
                  } else {
                     var47 = null;
                  }

                  if (var31 != var15) {
                     var47 = var31.mListAnchors[var3 + 1].mSolverVariable;
                  } else if (var15 == var16 && var31 == var15) {
                     if (var19.mListAnchors[var3].mTarget != null) {
                        var47 = var19.mListAnchors[var3].mTarget.mSolverVariable;
                     } else {
                        var47 = null;
                     }
                  }

                  var46 = null;
                  var9 = var49.getMargin();
                  var38 = var15.mListAnchors[var3 + 1].getMargin();
                  if (var34 != null) {
                     var45 = var34.mListAnchors[var3];
                     var48 = var45.mSolverVariable;
                     var50 = var15.mListAnchors[var3 + 1].mSolverVariable;
                     var51 = var48;
                  } else {
                     var49 = var23.mListAnchors[var3 + 1].mTarget;
                     if (var49 != null) {
                        var46 = var49.mSolverVariable;
                     }

                     var50 = var15.mListAnchors[var3 + 1].mSolverVariable;
                     var51 = var46;
                     var45 = var49;
                  }

                  if (var45 != null) {
                     var38 += var45.getMargin();
                  }

                  if (var31 != null) {
                     var9 += var31.mListAnchors[var3 + 1].getMargin();
                  }

                  if (var25 != null && var47 != null && var51 != null && var50 != null) {
                     if (var15 == var16) {
                        var9 = var16.mListAnchors[var3].getMargin();
                     }

                     if (var15 == var24) {
                        var38 = var24.mListAnchors[var3 + 1].getMargin();
                     }

                     if (var12) {
                        var41 = 6;
                     } else {
                        var41 = 4;
                     }

                     var1.addCentering(var25, var47, var9, 0.5F, var51, var50, var38, var41);
                  }
               }

               if (var15.getVisibility() != 8) {
                  var31 = var15;
               }
            }
         } else if (var10 && var16 != null) {
            if (var4.mWidgetsMatchCount > 0 && var4.mWidgetsCount == var4.mWidgetsMatchCount) {
               var8 = true;
            } else {
               var8 = false;
            }

            var15 = var16;

            for(var34 = var16; var15 != null; var15 = var31) {
               for(var31 = var15.mNextChainWidget[var2]; var31 != null && var31.getVisibility() == 8; var31 = var31.mNextChainWidget[var2]) {
               }

               if (var15 != var16 && var15 != var24 && var31 != null) {
                  if (var31 == var24) {
                     var31 = null;
                  }

                  var45 = var15.mListAnchors[var3];
                  var51 = var45.mSolverVariable;
                  if (var45.mTarget != null) {
                     var47 = var45.mTarget.mSolverVariable;
                  }

                  var25 = var34.mListAnchors[var3 + 1].mSolverVariable;
                  var47 = null;
                  int var39 = var45.getMargin();
                  var9 = var15.mListAnchors[var3 + 1].getMargin();
                  if (var31 != null) {
                     var49 = var31.mListAnchors[var3];
                     var46 = var49.mSolverVariable;
                     if (var49.mTarget != null) {
                        var47 = var49.mTarget.mSolverVariable;
                     } else {
                        var47 = null;
                     }

                     var50 = var46;
                  } else {
                     var45 = var15.mListAnchors[var3 + 1].mTarget;
                     if (var45 != null) {
                        var47 = var45.mSolverVariable;
                     }

                     var48 = var15.mListAnchors[var3 + 1].mSolverVariable;
                     var50 = var47;
                     var47 = var48;
                     var49 = var45;
                  }

                  if (var49 != null) {
                     var9 += var49.getMargin();
                  }

                  if (var34 != null) {
                     var39 += var34.mListAnchors[var3 + 1].getMargin();
                  }

                  if (var8) {
                     var41 = 6;
                  } else {
                     var41 = 4;
                  }

                  if (var51 != null && var25 != null && var50 != null && var47 != null) {
                     var1.addCentering(var51, var25, var39, 0.5F, var50, var47, var9, var41);
                  }
               }

               if (var15.getVisibility() != 8) {
                  var34 = var15;
               }
            }

            var42 = var16.mListAnchors[var3];
            var17 = var19.mListAnchors[var3].mTarget;
            ConstraintAnchor var35 = var24.mListAnchors[var3 + 1];
            var32 = var23.mListAnchors[var3 + 1].mTarget;
            if (var17 != null) {
               if (var16 != var24) {
                  var1.addEquality(var42.mSolverVariable, var17.mSolverVariable, var42.getMargin(), 5);
               } else if (var32 != null) {
                  var1.addCentering(var42.mSolverVariable, var17.mSolverVariable, var42.getMargin(), 0.5F, var35.mSolverVariable, var32.mSolverVariable, var35.getMargin(), 5);
               }
            }

            if (var32 != null && var16 != var24) {
               var1.addEquality(var35.mSolverVariable, var32.mSolverVariable, -var35.getMargin(), 5);
            }
         }
      } else {
         var42 = var19.mListAnchors[var3];
         var17 = var23.mListAnchors[var3 + 1];
         if (var19.mListAnchors[var3].mTarget != null) {
            var30 = var19.mListAnchors[var3].mTarget.mSolverVariable;
         } else {
            var30 = null;
         }

         if (var23.mListAnchors[var3 + 1].mTarget != null) {
            var33 = var23.mListAnchors[var3 + 1].mTarget.mSolverVariable;
         } else {
            var33 = null;
         }

         if (var16 == var24) {
            var42 = var16.mListAnchors[var3];
            var17 = var16.mListAnchors[var3 + 1];
         }

         if (var30 != null && var33 != null) {
            if (var2 == 0) {
               var5 = var22.mHorizontalBiasPercent;
            } else {
               var5 = var22.mVerticalBiasPercent;
            }

            var2 = var42.getMargin();
            var38 = var17.getMargin();
            var1.addCentering(var42.mSolverVariable, var30, var2, var5, var33, var17.mSolverVariable, var38, 5);
         }
      }

      if ((var11 || var10) && var16 != null) {
         var42 = var16.mListAnchors[var3];
         var17 = var24.mListAnchors[var3 + 1];
         if (var42.mTarget != null) {
            var33 = var42.mTarget.mSolverVariable;
         } else {
            var33 = null;
         }

         if (var17.mTarget != null) {
            var30 = var17.mTarget.mSolverVariable;
         } else {
            var30 = null;
         }

         if (var23 != var24) {
            var32 = var23.mListAnchors[var3 + 1];
            if (var32.mTarget != null) {
               var30 = var32.mTarget.mSolverVariable;
            } else {
               var30 = null;
            }
         }

         ConstraintAnchor var43;
         if (var16 == var24) {
            var42 = var16.mListAnchors[var3];
            var43 = var16.mListAnchors[var3 + 1];
         } else {
            var43 = var17;
         }

         if (var33 != null && var30 != null) {
            var2 = var42.getMargin();
            if (var24 == null) {
               var44 = var23;
            } else {
               var44 = var24;
            }

            var3 = var44.mListAnchors[var3 + 1].getMargin();
            var1.addCentering(var42.mSolverVariable, var33, var2, 0.5F, var30, var43.mSolverVariable, var3, 5);
         }
      }
   }
}
