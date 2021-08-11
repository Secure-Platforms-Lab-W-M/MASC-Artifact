package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;
import java.util.Arrays;

public class ConstraintWidgetContainer extends WidgetContainer {
   static boolean ALLOW_ROOT_GROUP = true;
   private static final int CHAIN_FIRST = 0;
   private static final int CHAIN_FIRST_VISIBLE = 2;
   private static final int CHAIN_LAST = 1;
   private static final int CHAIN_LAST_VISIBLE = 3;
   private static final boolean DEBUG = false;
   private static final boolean DEBUG_LAYOUT = false;
   private static final boolean DEBUG_OPTIMIZE = false;
   private static final int FLAG_CHAIN_DANGLING = 1;
   private static final int FLAG_CHAIN_OPTIMIZE = 0;
   private static final int FLAG_RECOMPUTE_BOUNDS = 2;
   private static final int MAX_ITERATIONS = 8;
   public static final int OPTIMIZATION_ALL = 2;
   public static final int OPTIMIZATION_BASIC = 4;
   public static final int OPTIMIZATION_CHAIN = 8;
   public static final int OPTIMIZATION_NONE = 1;
   private static final boolean USE_SNAPSHOT = true;
   private static final boolean USE_THREAD = false;
   private boolean[] flags = new boolean[3];
   protected LinearSystem mBackgroundSystem = null;
   private ConstraintWidget[] mChainEnds = new ConstraintWidget[4];
   private boolean mHeightMeasuredTooSmall = false;
   private ConstraintWidget[] mHorizontalChainsArray = new ConstraintWidget[4];
   private int mHorizontalChainsSize = 0;
   private ConstraintWidget[] mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
   private int mOptimizationLevel = 2;
   int mPaddingBottom;
   int mPaddingLeft;
   int mPaddingRight;
   int mPaddingTop;
   private Snapshot mSnapshot;
   protected LinearSystem mSystem = new LinearSystem();
   private ConstraintWidget[] mVerticalChainsArray = new ConstraintWidget[4];
   private int mVerticalChainsSize = 0;
   private boolean mWidthMeasuredTooSmall = false;
   int mWrapHeight;
   int mWrapWidth;

   public ConstraintWidgetContainer() {
   }

   public ConstraintWidgetContainer(int var1, int var2) {
      super(var1, var2);
   }

   public ConstraintWidgetContainer(int var1, int var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }

   private void addHorizontalChain(ConstraintWidget var1) {
      int var2 = 0;

      while(true) {
         int var3 = this.mHorizontalChainsSize;
         if (var2 >= var3) {
            ConstraintWidget[] var4 = this.mHorizontalChainsArray;
            if (var3 + 1 >= var4.length) {
               this.mHorizontalChainsArray = (ConstraintWidget[])Arrays.copyOf(var4, var4.length * 2);
            }

            var4 = this.mHorizontalChainsArray;
            var2 = this.mHorizontalChainsSize;
            var4[var2] = var1;
            this.mHorizontalChainsSize = var2 + 1;
            return;
         }

         if (this.mHorizontalChainsArray[var2] == var1) {
            return;
         }

         ++var2;
      }
   }

   private void addVerticalChain(ConstraintWidget var1) {
      int var2 = 0;

      while(true) {
         int var3 = this.mVerticalChainsSize;
         if (var2 >= var3) {
            ConstraintWidget[] var4 = this.mVerticalChainsArray;
            if (var3 + 1 >= var4.length) {
               this.mVerticalChainsArray = (ConstraintWidget[])Arrays.copyOf(var4, var4.length * 2);
            }

            var4 = this.mVerticalChainsArray;
            var2 = this.mVerticalChainsSize;
            var4[var2] = var1;
            this.mVerticalChainsSize = var2 + 1;
            return;
         }

         if (this.mVerticalChainsArray[var2] == var1) {
            return;
         }

         ++var2;
      }
   }

   private void applyHorizontalChain(LinearSystem var1) {
      LinearSystem var9 = var1;
      int var3 = 0;

      while(true) {
         ConstraintWidgetContainer var13 = this;
         if (var3 >= this.mHorizontalChainsSize) {
            return;
         }

         ConstraintWidget[] var11 = this.mHorizontalChainsArray;
         ConstraintWidget var10 = var11[var3];
         int var7 = this.countMatchConstraintsChainedWidgets(var1, this.mChainEnds, var11[var3], 0, this.flags);
         ConstraintWidget var30 = this.mChainEnds[2];
         if (var30 != null) {
            int var22;
            if (this.flags[1]) {
               for(var22 = var10.getDrawX(); var30 != null; var30 = var10) {
                  var9.addEquality(var30.mLeft.mSolverVariable, var22);
                  var10 = var30.mHorizontalNextWidget;
                  var22 += var30.mLeft.getMargin() + var30.getWidth() + var30.mRight.getMargin();
               }
            } else {
               boolean var4;
               if (var10.mHorizontalChainStyle == 0) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               boolean var5;
               if (var10.mHorizontalChainStyle == 2) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               ConstraintWidget var14 = var10;
               boolean var6;
               if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               int var8 = this.mOptimizationLevel;
               if ((var8 == 2 || var8 == 8) && this.flags[0] && var10.mHorizontalChainFixedPosition && !var5 && !var6 && var10.mHorizontalChainStyle == 0) {
                  Optimizer.applyDirectResolutionHorizontalChain(this, var9, var7, var10);
               } else {
                  ConstraintWidget var15;
                  ConstraintWidget var16;
                  SolverVariable var17;
                  SolverVariable var19;
                  int var23;
                  SolverVariable var31;
                  SolverVariable var42;
                  if (var7 != 0 && !var5) {
                     ConstraintWidget var34 = null;

                     float var2;
                     for(var2 = 0.0F; var30 != null; var30 = var30.mHorizontalNextWidget) {
                        if (var30.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                           var22 = var30.mLeft.getMargin();
                           if (var34 != null) {
                              var22 += var34.mRight.getMargin();
                           }

                           byte var24 = 3;
                           if (var30.mLeft.mTarget.mOwner.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                              var24 = 2;
                           }

                           var9.addGreaterThan(var30.mLeft.mSolverVariable, var30.mLeft.mTarget.mSolverVariable, var22, var24);
                           var22 = var30.mRight.getMargin();
                           if (var30.mRight.mTarget.mOwner.mLeft.mTarget != null && var30.mRight.mTarget.mOwner.mLeft.mTarget.mOwner == var30) {
                              var22 += var30.mRight.mTarget.mOwner.mLeft.getMargin();
                           }

                           var24 = 3;
                           if (var30.mRight.mTarget.mOwner.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                              var24 = 2;
                           }

                           var9.addLowerThan(var30.mRight.mSolverVariable, var30.mRight.mTarget.mSolverVariable, -var22, var24);
                        } else {
                           var2 += var30.mHorizontalWeight;
                           var22 = 0;
                           if (var30.mRight.mTarget != null) {
                              var22 = var30.mRight.getMargin();
                              if (var30 != var13.mChainEnds[3]) {
                                 var22 += var30.mRight.mTarget.mOwner.mLeft.getMargin();
                              }
                           }

                           var9.addGreaterThan(var30.mRight.mSolverVariable, var30.mLeft.mSolverVariable, 0, 1);
                           var9.addLowerThan(var30.mRight.mSolverVariable, var30.mRight.mTarget.mSolverVariable, -var22, 1);
                        }

                        var34 = var30;
                     }

                     ConstraintWidget[] var37;
                     if (var7 == 1) {
                        var30 = var13.mMatchConstraintsChainedWidgets[0];
                        var22 = var30.mLeft.getMargin();
                        if (var30.mLeft.mTarget != null) {
                           var22 += var30.mLeft.mTarget.getMargin();
                        }

                        var23 = var30.mRight.getMargin();
                        if (var30.mRight.mTarget != null) {
                           var23 += var30.mRight.mTarget.getMargin();
                        }

                        SolverVariable var29 = var10.mRight.mTarget.mSolverVariable;
                        var37 = var13.mChainEnds;
                        if (var30 == var37[3]) {
                           var29 = var37[1].mRight.mTarget.mSolverVariable;
                        }

                        if (var30.mMatchConstraintDefaultWidth == 1) {
                           var9.addGreaterThan(var10.mLeft.mSolverVariable, var10.mLeft.mTarget.mSolverVariable, var22, 1);
                           var9.addLowerThan(var10.mRight.mSolverVariable, var29, -var23, 1);
                           var9.addEquality(var10.mRight.mSolverVariable, var10.mLeft.mSolverVariable, var10.getWidth(), 2);
                        } else {
                           var9.addEquality(var30.mLeft.mSolverVariable, var30.mLeft.mTarget.mSolverVariable, var22, 1);
                           var9.addEquality(var30.mRight.mSolverVariable, var29, -var23, 1);
                        }
                     } else {
                        int var25 = 0;

                        for(var23 = var7; var25 < var23 - 1; ++var25) {
                           var11 = var13.mMatchConstraintsChainedWidgets;
                           var15 = var11[var25];
                           var16 = var11[var25 + 1];
                           var17 = var15.mLeft.mSolverVariable;
                           var42 = var15.mRight.mSolverVariable;
                           var19 = var16.mLeft.mSolverVariable;
                           var31 = var16.mRight.mSolverVariable;
                           var37 = var13.mChainEnds;
                           if (var16 == var37[3]) {
                              var31 = var37[1].mRight.mSolverVariable;
                           }

                           var22 = var15.mLeft.getMargin();
                           if (var15.mLeft.mTarget != null && var15.mLeft.mTarget.mOwner.mRight.mTarget != null && var15.mLeft.mTarget.mOwner.mRight.mTarget.mOwner == var15) {
                              var22 += var15.mLeft.mTarget.mOwner.mRight.getMargin();
                           }

                           var9.addGreaterThan(var17, var15.mLeft.mTarget.mSolverVariable, var22, 2);
                           var7 = var15.mRight.getMargin();
                           if (var15.mRight.mTarget != null && var15.mHorizontalNextWidget != null) {
                              if (var15.mHorizontalNextWidget.mLeft.mTarget != null) {
                                 var22 = var15.mHorizontalNextWidget.mLeft.getMargin();
                              } else {
                                 var22 = 0;
                              }

                              var22 += var7;
                           } else {
                              var22 = var7;
                           }

                           var9.addLowerThan(var42, var15.mRight.mTarget.mSolverVariable, -var22, 2);
                           if (var25 + 1 == var23 - 1) {
                              var22 = var16.mLeft.getMargin();
                              if (var16.mLeft.mTarget != null && var16.mLeft.mTarget.mOwner.mRight.mTarget != null && var16.mLeft.mTarget.mOwner.mRight.mTarget.mOwner == var16) {
                                 var22 += var16.mLeft.mTarget.mOwner.mRight.getMargin();
                              }

                              var9.addGreaterThan(var19, var16.mLeft.mTarget.mSolverVariable, var22, 2);
                              ConstraintAnchor var39 = var16.mRight;
                              ConstraintWidget[] var44 = var13.mChainEnds;
                              if (var16 == var44[3]) {
                                 var39 = var44[1].mRight;
                              }

                              var22 = var39.getMargin();
                              if (var39.mTarget != null && var39.mTarget.mOwner.mLeft.mTarget != null && var39.mTarget.mOwner.mLeft.mTarget.mOwner == var16) {
                                 var22 += var39.mTarget.mOwner.mLeft.getMargin();
                              }

                              var9.addLowerThan(var31, var39.mTarget.mSolverVariable, -var22, 2);
                           }

                           if (var14.mMatchConstraintMaxWidth > 0) {
                              var9.addLowerThan(var42, var17, var14.mMatchConstraintMaxWidth, 2);
                           }

                           ArrayRow var41 = var1.createRow();
                           var41.createRowEqualDimension(var15.mHorizontalWeight, var2, var16.mHorizontalWeight, var17, var15.mLeft.getMargin(), var42, var15.mRight.getMargin(), var19, var16.mLeft.getMargin(), var31, var16.mRight.getMargin());
                           var9.addConstraint(var41);
                        }
                     }
                  } else {
                     var6 = false;
                     var15 = null;
                     ConstraintWidget var35 = null;
                     var17 = null;
                     LinearSystem var12 = var9;
                     ConstraintWidget var27 = var30;

                     while(true) {
                        var16 = var27;
                        var17 = null;
                        var19 = null;
                        ConstraintAnchor var40;
                        if (var27 == null) {
                           var9 = var12;
                           if (var5) {
                              var40 = var30.mLeft;
                              ConstraintAnchor var38 = var15.mRight;
                              var22 = var40.getMargin();
                              var23 = var38.getMargin();
                              if (var10.mLeft.mTarget != null) {
                                 var31 = var10.mLeft.mTarget.mSolverVariable;
                              } else {
                                 var31 = null;
                              }

                              SolverVariable var32 = var17;
                              if (var15.mRight.mTarget != null) {
                                 var32 = var15.mRight.mTarget.mSolverVariable;
                              }

                              if (var31 != null && var32 != null) {
                                 var12.addLowerThan(var38.mSolverVariable, var32, -var23, 1);
                                 var1.addCentering(var40.mSolverVariable, var31, var22, var10.mHorizontalBiasPercent, var32, var38.mSolverVariable, var23, 4);
                              }
                           }
                           break;
                        }

                        ConstraintWidget var18 = var27.mHorizontalNextWidget;
                        if (var18 == null) {
                           var15 = this.mChainEnds[1];
                           var6 = true;
                        }

                        label327: {
                           if (var5) {
                              ConstraintAnchor var33 = var27.mLeft;
                              var7 = var33.getMargin();
                              if (var35 != null) {
                                 var7 += var35.mRight.getMargin();
                              }

                              byte var26 = 1;
                              if (var30 != var27) {
                                 var26 = 3;
                              }

                              var12.addGreaterThan(var33.mSolverVariable, var33.mTarget.mSolverVariable, var7, var26);
                              if (var27.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                 var40 = var27.mRight;
                                 if (var27.mMatchConstraintDefaultWidth == 1) {
                                    var7 = Math.max(var27.mMatchConstraintMinWidth, var27.getWidth());
                                    var12.addEquality(var40.mSolverVariable, var33.mSolverVariable, var7, 3);
                                 } else {
                                    var12.addGreaterThan(var33.mSolverVariable, var33.mTarget.mSolverVariable, var33.mMargin, 3);
                                    var12.addLowerThan(var40.mSolverVariable, var33.mSolverVariable, var27.mMatchConstraintMinWidth, 3);
                                 }
                              }
                           } else if (!var4 && var6 && var35 != null) {
                              if (var27.mRight.mTarget == null) {
                                 var12.addEquality(var27.mRight.mSolverVariable, var27.getDrawRight());
                              } else {
                                 var7 = var27.mRight.getMargin();
                                 var12.addEquality(var27.mRight.mSolverVariable, var15.mRight.mTarget.mSolverVariable, -var7, 5);
                              }
                           } else {
                              if (var4 || var6 || var35 != null) {
                                 ConstraintAnchor var21 = var27.mLeft;
                                 ConstraintAnchor var20 = var27.mRight;
                                 var7 = var21.getMargin();
                                 var8 = var20.getMargin();
                                 var12.addGreaterThan(var21.mSolverVariable, var21.mTarget.mSolverVariable, var7, 1);
                                 var12.addLowerThan(var20.mSolverVariable, var20.mTarget.mSolverVariable, -var8, 1);
                                 SolverVariable var28;
                                 if (var21.mTarget != null) {
                                    var28 = var21.mTarget.mSolverVariable;
                                 } else {
                                    var28 = null;
                                 }

                                 if (var35 == null) {
                                    if (var10.mLeft.mTarget != null) {
                                       var28 = var10.mLeft.mTarget.mSolverVariable;
                                    } else {
                                       var28 = null;
                                    }

                                    var17 = var28;
                                 } else {
                                    var17 = var28;
                                 }

                                 if (var18 == null) {
                                    if (var15.mRight.mTarget != null) {
                                       var27 = var15.mRight.mTarget.mOwner;
                                    } else {
                                       var27 = null;
                                    }
                                 } else {
                                    var27 = var18;
                                 }

                                 if (var27 != null) {
                                    SolverVariable var36 = var27.mLeft.mSolverVariable;
                                    if (var6) {
                                       if (var15.mRight.mTarget != null) {
                                          var36 = var15.mRight.mTarget.mSolverVariable;
                                       } else {
                                          var36 = null;
                                       }
                                    }

                                    if (var17 != null && var36 != null) {
                                       var42 = var21.mSolverVariable;
                                       SolverVariable var43 = var20.mSolverVariable;
                                       var1.addCentering(var42, var17, var7, 0.5F, var36, var43, var8, 4);
                                    }
                                 }
                                 break label327;
                              }

                              if (var27.mLeft.mTarget == null) {
                                 var12.addEquality(var27.mLeft.mSolverVariable, var27.getDrawX());
                              } else {
                                 var7 = var27.mLeft.getMargin();
                                 var12.addEquality(var27.mLeft.mSolverVariable, var10.mLeft.mTarget.mSolverVariable, var7, 5);
                              }
                           }

                           var27 = var18;
                        }

                        if (var6) {
                           var35 = var19;
                        } else {
                           var35 = var27;
                        }

                        var27 = var35;
                        var35 = var16;
                     }
                  }
               }
            }
         }

         ++var3;
      }
   }

   private void applyVerticalChain(LinearSystem var1) {
      LinearSystem var9 = var1;
      int var3 = 0;

      while(true) {
         ConstraintWidgetContainer var13 = this;
         if (var3 >= this.mVerticalChainsSize) {
            return;
         }

         ConstraintWidget[] var11 = this.mVerticalChainsArray;
         ConstraintWidget var10 = var11[var3];
         int var7 = this.countMatchConstraintsChainedWidgets(var1, this.mChainEnds, var11[var3], 1, this.flags);
         ConstraintWidget var31 = this.mChainEnds[2];
         if (var31 != null) {
            int var22;
            if (this.flags[1]) {
               for(var22 = var10.getDrawY(); var31 != null; var31 = var10) {
                  var9.addEquality(var31.mTop.mSolverVariable, var22);
                  var10 = var31.mVerticalNextWidget;
                  var22 += var31.mTop.getMargin() + var31.getHeight() + var31.mBottom.getMargin();
               }
            } else {
               boolean var4;
               if (var10.mVerticalChainStyle == 0) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               boolean var5;
               if (var10.mVerticalChainStyle == 2) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               ConstraintWidget var14 = var10;
               boolean var6;
               if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               int var8 = this.mOptimizationLevel;
               if ((var8 == 2 || var8 == 8) && this.flags[0] && var10.mVerticalChainFixedPosition && !var5 && !var6 && var10.mVerticalChainStyle == 0) {
                  Optimizer.applyDirectResolutionVerticalChain(this, var9, var7, var10);
               } else {
                  ConstraintWidget var15;
                  ConstraintWidget var16;
                  SolverVariable var18;
                  SolverVariable var19;
                  int var23;
                  SolverVariable var32;
                  SolverVariable var43;
                  if (var7 != 0 && !var5) {
                     ConstraintWidget var34 = null;

                     float var2;
                     for(var2 = 0.0F; var31 != null; var31 = var31.mVerticalNextWidget) {
                        if (var31.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                           var22 = var31.mTop.getMargin();
                           if (var34 != null) {
                              var22 += var34.mBottom.getMargin();
                           }

                           byte var26 = 3;
                           if (var31.mTop.mTarget.mOwner.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                              var26 = 2;
                           }

                           var9.addGreaterThan(var31.mTop.mSolverVariable, var31.mTop.mTarget.mSolverVariable, var22, var26);
                           var22 = var31.mBottom.getMargin();
                           if (var31.mBottom.mTarget.mOwner.mTop.mTarget != null && var31.mBottom.mTarget.mOwner.mTop.mTarget.mOwner == var31) {
                              var22 += var31.mBottom.mTarget.mOwner.mTop.getMargin();
                           }

                           var26 = 3;
                           if (var31.mBottom.mTarget.mOwner.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                              var26 = 2;
                           }

                           var9.addLowerThan(var31.mBottom.mSolverVariable, var31.mBottom.mTarget.mSolverVariable, -var22, var26);
                        } else {
                           var2 += var31.mVerticalWeight;
                           var22 = 0;
                           if (var31.mBottom.mTarget != null) {
                              var22 = var31.mBottom.getMargin();
                              if (var31 != var13.mChainEnds[3]) {
                                 var22 += var31.mBottom.mTarget.mOwner.mTop.getMargin();
                              }
                           }

                           var9.addGreaterThan(var31.mBottom.mSolverVariable, var31.mTop.mSolverVariable, 0, 1);
                           var9.addLowerThan(var31.mBottom.mSolverVariable, var31.mBottom.mTarget.mSolverVariable, -var22, 1);
                        }

                        var34 = var31;
                     }

                     ConstraintWidget[] var37;
                     if (var7 == 1) {
                        var31 = var13.mMatchConstraintsChainedWidgets[0];
                        var22 = var31.mTop.getMargin();
                        if (var31.mTop.mTarget != null) {
                           var22 += var31.mTop.mTarget.getMargin();
                        }

                        var23 = var31.mBottom.getMargin();
                        if (var31.mBottom.mTarget != null) {
                           var23 += var31.mBottom.mTarget.getMargin();
                        }

                        SolverVariable var30 = var10.mBottom.mTarget.mSolverVariable;
                        var37 = var13.mChainEnds;
                        if (var31 == var37[3]) {
                           var30 = var37[1].mBottom.mTarget.mSolverVariable;
                        }

                        if (var31.mMatchConstraintDefaultHeight == 1) {
                           var9.addGreaterThan(var10.mTop.mSolverVariable, var10.mTop.mTarget.mSolverVariable, var22, 1);
                           var9.addLowerThan(var10.mBottom.mSolverVariable, var30, -var23, 1);
                           var9.addEquality(var10.mBottom.mSolverVariable, var10.mTop.mSolverVariable, var10.getHeight(), 2);
                        } else {
                           var9.addEquality(var31.mTop.mSolverVariable, var31.mTop.mTarget.mSolverVariable, var22, 1);
                           var9.addEquality(var31.mBottom.mSolverVariable, var30, -var23, 1);
                        }
                     } else {
                        int var25 = 0;

                        for(var23 = var7; var25 < var23 - 1; ++var25) {
                           var11 = var13.mMatchConstraintsChainedWidgets;
                           var15 = var11[var25];
                           var16 = var11[var25 + 1];
                           var43 = var15.mTop.mSolverVariable;
                           var18 = var15.mBottom.mSolverVariable;
                           var19 = var16.mTop.mSolverVariable;
                           var32 = var16.mBottom.mSolverVariable;
                           var37 = var13.mChainEnds;
                           if (var16 == var37[3]) {
                              var32 = var37[1].mBottom.mSolverVariable;
                           }

                           var22 = var15.mTop.getMargin();
                           if (var15.mTop.mTarget != null && var15.mTop.mTarget.mOwner.mBottom.mTarget != null && var15.mTop.mTarget.mOwner.mBottom.mTarget.mOwner == var15) {
                              var22 += var15.mTop.mTarget.mOwner.mBottom.getMargin();
                           }

                           var9.addGreaterThan(var43, var15.mTop.mTarget.mSolverVariable, var22, 2);
                           var7 = var15.mBottom.getMargin();
                           if (var15.mBottom.mTarget != null && var15.mVerticalNextWidget != null) {
                              if (var15.mVerticalNextWidget.mTop.mTarget != null) {
                                 var22 = var15.mVerticalNextWidget.mTop.getMargin();
                              } else {
                                 var22 = 0;
                              }

                              var22 += var7;
                           } else {
                              var22 = var7;
                           }

                           var9.addLowerThan(var18, var15.mBottom.mTarget.mSolverVariable, -var22, 2);
                           if (var25 + 1 == var23 - 1) {
                              var22 = var16.mTop.getMargin();
                              if (var16.mTop.mTarget != null && var16.mTop.mTarget.mOwner.mBottom.mTarget != null && var16.mTop.mTarget.mOwner.mBottom.mTarget.mOwner == var16) {
                                 var22 += var16.mTop.mTarget.mOwner.mBottom.getMargin();
                              }

                              var9.addGreaterThan(var19, var16.mTop.mTarget.mSolverVariable, var22, 2);
                              ConstraintAnchor var40 = var16.mBottom;
                              ConstraintWidget[] var46 = var13.mChainEnds;
                              if (var16 == var46[3]) {
                                 var40 = var46[1].mBottom;
                              }

                              var22 = var40.getMargin();
                              if (var40.mTarget != null && var40.mTarget.mOwner.mTop.mTarget != null && var40.mTarget.mOwner.mTop.mTarget.mOwner == var16) {
                                 var22 += var40.mTarget.mOwner.mTop.getMargin();
                              }

                              var9.addLowerThan(var32, var40.mTarget.mSolverVariable, -var22, 2);
                           }

                           if (var14.mMatchConstraintMaxHeight > 0) {
                              var9.addLowerThan(var18, var43, var14.mMatchConstraintMaxHeight, 2);
                           }

                           ArrayRow var41 = var1.createRow();
                           var41.createRowEqualDimension(var15.mVerticalWeight, var2, var16.mVerticalWeight, var43, var15.mTop.getMargin(), var18, var15.mBottom.getMargin(), var19, var16.mTop.getMargin(), var32, var16.mBottom.getMargin());
                           var9.addConstraint(var41);
                        }
                     }
                  } else {
                     boolean var24 = false;
                     var15 = null;
                     ConstraintWidget var35 = null;
                     ConstraintWidget var17 = null;
                     LinearSystem var12 = var9;
                     var6 = var4;
                     ConstraintWidget var28 = var31;

                     while(true) {
                        var16 = var28;
                        var17 = null;
                        var19 = null;
                        if (var28 == null) {
                           var9 = var12;
                           if (var5) {
                              ConstraintAnchor var42 = var31.mTop;
                              ConstraintAnchor var38 = var15.mBottom;
                              var22 = var42.getMargin();
                              var23 = var38.getMargin();
                              if (var10.mTop.mTarget != null) {
                                 var32 = var10.mTop.mTarget.mSolverVariable;
                              } else {
                                 var32 = null;
                              }

                              SolverVariable var33 = var17;
                              if (var15.mBottom.mTarget != null) {
                                 var33 = var15.mBottom.mTarget.mSolverVariable;
                              }

                              if (var32 != null && var33 != null) {
                                 var12.addLowerThan(var38.mSolverVariable, var33, -var23, 1);
                                 var1.addCentering(var42.mSolverVariable, var32, var22, var10.mVerticalBiasPercent, var33, var38.mSolverVariable, var23, 4);
                              }
                           }
                           break;
                        }

                        var17 = var28.mVerticalNextWidget;
                        if (var17 == null) {
                           var15 = this.mChainEnds[1];
                           var24 = true;
                        }

                        label345: {
                           SolverVariable var29;
                           SolverVariable var36;
                           if (var5) {
                              ConstraintAnchor var44 = var28.mTop;
                              var22 = var44.getMargin();
                              if (var35 != null) {
                                 var22 += var35.mBottom.getMargin();
                              }

                              byte var27 = 1;
                              if (var31 != var28) {
                                 var27 = 3;
                              }

                              var29 = null;
                              var36 = null;
                              if (var44.mTarget != null) {
                                 var29 = var44.mSolverVariable;
                                 var36 = var44.mTarget.mSolverVariable;
                              } else if (var28.mBaseline.mTarget != null) {
                                 var29 = var28.mBaseline.mSolverVariable;
                                 var36 = var28.mBaseline.mTarget.mSolverVariable;
                                 var22 -= var44.getMargin();
                              }

                              if (var29 != null && var36 != null) {
                                 var12.addGreaterThan(var29, var36, var22, var27);
                              }

                              if (var28.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                 ConstraintAnchor var39 = var28.mBottom;
                                 if (var28.mMatchConstraintDefaultHeight == 1) {
                                    var22 = Math.max(var28.mMatchConstraintMinHeight, var28.getHeight());
                                    var12.addEquality(var39.mSolverVariable, var44.mSolverVariable, var22, 3);
                                 } else {
                                    var12.addGreaterThan(var44.mSolverVariable, var44.mTarget.mSolverVariable, var44.mMargin, 3);
                                    var12.addLowerThan(var39.mSolverVariable, var44.mSolverVariable, var28.mMatchConstraintMinHeight, 3);
                                 }
                              }
                           } else if (!var6 && var24 && var35 != null) {
                              if (var28.mBottom.mTarget == null) {
                                 var12.addEquality(var28.mBottom.mSolverVariable, var28.getDrawBottom());
                              } else {
                                 var22 = var28.mBottom.getMargin();
                                 var12.addEquality(var28.mBottom.mSolverVariable, var15.mBottom.mTarget.mSolverVariable, -var22, 5);
                              }
                           } else {
                              if (var6 || var24 || var35 != null) {
                                 ConstraintAnchor var21 = var28.mTop;
                                 ConstraintAnchor var20 = var28.mBottom;
                                 var22 = var21.getMargin();
                                 var8 = var20.getMargin();
                                 var12.addGreaterThan(var21.mSolverVariable, var21.mTarget.mSolverVariable, var22, 1);
                                 var12.addLowerThan(var20.mSolverVariable, var20.mTarget.mSolverVariable, -var8, 1);
                                 if (var21.mTarget != null) {
                                    var29 = var21.mTarget.mSolverVariable;
                                 } else {
                                    var29 = null;
                                 }

                                 if (var35 == null) {
                                    if (var10.mTop.mTarget != null) {
                                       var29 = var10.mTop.mTarget.mSolverVariable;
                                    } else {
                                       var29 = null;
                                    }

                                    var18 = var29;
                                 } else {
                                    var18 = var29;
                                 }

                                 if (var17 == null) {
                                    if (var15.mBottom.mTarget != null) {
                                       var28 = var15.mBottom.mTarget.mOwner;
                                    } else {
                                       var28 = null;
                                    }
                                 } else {
                                    var28 = var17;
                                 }

                                 if (var28 != null) {
                                    var36 = var28.mTop.mSolverVariable;
                                    if (var24) {
                                       if (var15.mBottom.mTarget != null) {
                                          var36 = var15.mBottom.mTarget.mSolverVariable;
                                       } else {
                                          var36 = null;
                                       }
                                    }

                                    if (var18 != null && var36 != null) {
                                       var43 = var21.mSolverVariable;
                                       SolverVariable var45 = var20.mSolverVariable;
                                       var1.addCentering(var43, var18, var22, 0.5F, var36, var45, var8, 4);
                                    }
                                 }
                                 break label345;
                              }

                              if (var28.mTop.mTarget == null) {
                                 var12.addEquality(var28.mTop.mSolverVariable, var28.getDrawY());
                              } else {
                                 var22 = var28.mTop.getMargin();
                                 var12.addEquality(var28.mTop.mSolverVariable, var10.mTop.mTarget.mSolverVariable, var22, 5);
                              }
                           }

                           var28 = var17;
                        }

                        if (var24) {
                           var35 = var19;
                        } else {
                           var35 = var28;
                        }

                        var28 = var35;
                        var35 = var16;
                     }
                  }
               }
            }
         }

         ++var3;
      }
   }

   private int countMatchConstraintsChainedWidgets(LinearSystem var1, ConstraintWidget[] var2, ConstraintWidget var3, int var4, boolean[] var5) {
      ConstraintWidget var9 = var3;
      byte var7 = 0;
      byte var6 = 0;
      var5[0] = true;
      var5[1] = false;
      var2[0] = null;
      var2[2] = null;
      var2[1] = null;
      var2[3] = null;
      boolean var8;
      ConstraintWidget var10;
      ConstraintWidget var11;
      ConstraintWidget var12;
      ConstraintWidget[] var13;
      if (var4 == 0) {
         var8 = true;
         var12 = null;
         if (var3.mLeft.mTarget != null && var3.mLeft.mTarget.mOwner != this) {
            var8 = false;
         }

         var3.mHorizontalNextWidget = null;
         var10 = null;
         if (var3.getVisibility() != 8) {
            var10 = var3;
         }

         var11 = var10;

         for(var4 = var6; var9.mRight.mTarget != null; var12 = var9) {
            var9.mHorizontalNextWidget = null;
            if (var9.getVisibility() != 8) {
               if (var10 == null) {
                  var10 = var9;
               }

               if (var11 != null && var11 != var9) {
                  var11.mHorizontalNextWidget = var9;
               }

               var11 = var9;
            } else {
               var1.addEquality(var9.mLeft.mSolverVariable, var9.mLeft.mTarget.mSolverVariable, 0, 5);
               var1.addEquality(var9.mRight.mSolverVariable, var9.mLeft.mSolverVariable, 0, 5);
            }

            if (var9.getVisibility() != 8 && var9.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
               if (var9.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                  var5[0] = false;
               }

               if (var9.mDimensionRatio <= 0.0F) {
                  var5[0] = false;
                  var13 = this.mMatchConstraintsChainedWidgets;
                  if (var4 + 1 >= var13.length) {
                     this.mMatchConstraintsChainedWidgets = (ConstraintWidget[])Arrays.copyOf(var13, var13.length * 2);
                  }

                  this.mMatchConstraintsChainedWidgets[var4] = var9;
                  ++var4;
               }
            }

            if (var9.mRight.mTarget.mOwner.mLeft.mTarget == null || var9.mRight.mTarget.mOwner.mLeft.mTarget.mOwner != var9 || var9.mRight.mTarget.mOwner == var9) {
               break;
            }

            var9 = var9.mRight.mTarget.mOwner;
         }

         if (var9.mRight.mTarget != null && var9.mRight.mTarget.mOwner != this) {
            var8 = false;
         }

         if (var3.mLeft.mTarget == null || var12.mRight.mTarget == null) {
            var5[1] = true;
         }

         var3.mHorizontalChainFixedPosition = var8;
         var12.mHorizontalNextWidget = null;
         var2[0] = var3;
         var2[2] = var10;
         var2[1] = var12;
         var2[3] = var11;
         return var4;
      } else {
         var8 = true;
         var12 = null;
         if (var3.mTop.mTarget != null && var3.mTop.mTarget.mOwner != this) {
            var8 = false;
         }

         var3.mVerticalNextWidget = null;
         var10 = null;
         if (var3.getVisibility() != 8) {
            var10 = var3;
         }

         var11 = var10;

         for(var4 = var7; var9.mBottom.mTarget != null; var12 = var9) {
            var9.mVerticalNextWidget = null;
            if (var9.getVisibility() != 8) {
               if (var10 == null) {
                  var10 = var9;
               }

               if (var11 != null && var11 != var9) {
                  var11.mVerticalNextWidget = var9;
               }

               var11 = var9;
            } else {
               var1.addEquality(var9.mTop.mSolverVariable, var9.mTop.mTarget.mSolverVariable, 0, 5);
               var1.addEquality(var9.mBottom.mSolverVariable, var9.mTop.mSolverVariable, 0, 5);
            }

            if (var9.getVisibility() != 8 && var9.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
               if (var9.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                  var5[0] = false;
               }

               if (var9.mDimensionRatio <= 0.0F) {
                  var5[0] = false;
                  var13 = this.mMatchConstraintsChainedWidgets;
                  if (var4 + 1 >= var13.length) {
                     this.mMatchConstraintsChainedWidgets = (ConstraintWidget[])Arrays.copyOf(var13, var13.length * 2);
                  }

                  this.mMatchConstraintsChainedWidgets[var4] = var9;
                  ++var4;
               }
            }

            if (var9.mBottom.mTarget.mOwner.mTop.mTarget == null || var9.mBottom.mTarget.mOwner.mTop.mTarget.mOwner != var9 || var9.mBottom.mTarget.mOwner == var9) {
               break;
            }

            var9 = var9.mBottom.mTarget.mOwner;
         }

         if (var9.mBottom.mTarget != null && var9.mBottom.mTarget.mOwner != this) {
            var8 = false;
         }

         if (var3.mTop.mTarget == null || var12.mBottom.mTarget == null) {
            var5[1] = true;
         }

         var3.mVerticalChainFixedPosition = var8;
         var12.mVerticalNextWidget = null;
         var2[0] = var3;
         var2[2] = var10;
         var2[1] = var12;
         var2[3] = var11;
         return var4;
      }
   }

   public static ConstraintWidgetContainer createContainer(ConstraintWidgetContainer var0, String var1, ArrayList var2, int var3) {
      Rectangle var5 = getBounds(var2);
      if (var5.width != 0 && var5.height != 0) {
         int var4;
         if (var3 > 0) {
            var4 = Math.min(var5.field_14, var5.field_15);
            if (var3 > var4) {
               var3 = var4;
            }

            var5.grow(var3, var3);
         }

         var0.setOrigin(var5.field_14, var5.field_15);
         var0.setDimension(var5.width, var5.height);
         var0.setDebugName(var1);
         ConstraintWidget var7 = ((ConstraintWidget)var2.get(0)).getParent();
         var3 = 0;

         for(var4 = var2.size(); var3 < var4; ++var3) {
            ConstraintWidget var6 = (ConstraintWidget)var2.get(var3);
            if (var6.getParent() == var7) {
               var0.add(var6);
               var6.setX(var6.getX() - var5.field_14);
               var6.setY(var6.getY() - var5.field_15);
            }
         }

         return var0;
      } else {
         return null;
      }
   }

   private boolean optimize(LinearSystem var1) {
      int var11 = this.mChildren.size();
      boolean var7 = false;
      byte var8 = 0;
      byte var9 = 0;
      byte var10 = 0;
      int var6 = 0;

      while(true) {
         boolean var4 = var7;
         int var3 = var8;
         int var2 = var9;
         int var5 = var10;
         ConstraintWidget var12;
         if (var6 >= var11) {
            while(true) {
               int var15 = var2;
               int var16 = var3;
               if (var4) {
                  int var14 = 0;
                  var3 = 0;

                  for(var2 = 0; var2 < var11; ++var2) {
                     ConstraintWidget var13 = (ConstraintWidget)this.mChildren.get(var2);
                     if (var13.mHorizontalResolution == 1 || var13.mHorizontalResolution == -1) {
                        ++var14;
                     }

                     if (var13.mVerticalResolution == 1 || var13.mVerticalResolution == -1) {
                        ++var3;
                     }
                  }

                  if (var14 == 0 && var3 == 0) {
                     return true;
                  }

                  return false;
               }

               var3 = 0;
               var2 = 0;
               var6 = var5 + 1;

               for(var5 = 0; var5 < var11; ++var5) {
                  var12 = (ConstraintWidget)this.mChildren.get(var5);
                  if (var12.mHorizontalResolution == -1) {
                     if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        var12.mHorizontalResolution = 1;
                     } else {
                        Optimizer.checkHorizontalSimpleDependency(this, var1, var12);
                     }
                  }

                  if (var12.mVerticalResolution == -1) {
                     if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        var12.mVerticalResolution = 1;
                     } else {
                        Optimizer.checkVerticalSimpleDependency(this, var1, var12);
                     }
                  }

                  if (var12.mVerticalResolution == -1) {
                     ++var3;
                  }

                  if (var12.mHorizontalResolution == -1) {
                     ++var2;
                  }
               }

               if (var3 == 0 && var2 == 0) {
                  var4 = true;
               } else if (var16 == var3 && var15 == var2) {
                  var4 = true;
               }

               var5 = var6;
            }
         }

         var12 = (ConstraintWidget)this.mChildren.get(var6);
         var12.mHorizontalResolution = -1;
         var12.mVerticalResolution = -1;
         if (var12.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || var12.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            var12.mHorizontalResolution = 1;
            var12.mVerticalResolution = 1;
         }

         ++var6;
      }
   }

   private void resetChains() {
      this.mHorizontalChainsSize = 0;
      this.mVerticalChainsSize = 0;
   }

   static int setGroup(ConstraintAnchor var0, int var1) {
      int var2 = var0.mGroup;
      if (var0.mOwner.getParent() == null) {
         return var1;
      } else if (var2 <= var1) {
         return var2;
      } else {
         var0.mGroup = var1;
         ConstraintAnchor var3 = var0.getOpposite();
         ConstraintAnchor var4 = var0.mTarget;
         if (var3 != null) {
            var1 = setGroup(var3, var1);
         }

         if (var4 != null) {
            var1 = setGroup(var4, var1);
         }

         if (var3 != null) {
            var1 = setGroup(var3, var1);
         }

         var0.mGroup = var1;
         return var1;
      }
   }

   void addChain(ConstraintWidget var1, int var2) {
      if (var2 == 0) {
         while(var1.mLeft.mTarget != null && var1.mLeft.mTarget.mOwner.mRight.mTarget != null && var1.mLeft.mTarget.mOwner.mRight.mTarget == var1.mLeft && var1.mLeft.mTarget.mOwner != var1) {
            var1 = var1.mLeft.mTarget.mOwner;
         }

         this.addHorizontalChain(var1);
      } else if (var2 == 1) {
         while(var1.mTop.mTarget != null && var1.mTop.mTarget.mOwner.mBottom.mTarget != null && var1.mTop.mTarget.mOwner.mBottom.mTarget == var1.mTop && var1.mTop.mTarget.mOwner != var1) {
            var1 = var1.mTop.mTarget.mOwner;
         }

         this.addVerticalChain(var1);
      }
   }

   public boolean addChildrenToSolver(LinearSystem var1, int var2) {
      this.addToSolver(var1, var2);
      int var5 = this.mChildren.size();
      boolean var3 = false;
      int var4 = this.mOptimizationLevel;
      if (var4 != 2 && var4 != 4) {
         var3 = true;
      } else if (this.optimize(var1)) {
         return false;
      }

      for(var4 = 0; var4 < var5; ++var4) {
         ConstraintWidget var6 = (ConstraintWidget)this.mChildren.get(var4);
         if (var6 instanceof ConstraintWidgetContainer) {
            ConstraintWidget.DimensionBehaviour var7 = var6.mHorizontalDimensionBehaviour;
            ConstraintWidget.DimensionBehaviour var8 = var6.mVerticalDimensionBehaviour;
            if (var7 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var6.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            }

            if (var8 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var6.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            }

            var6.addToSolver(var1, var2);
            if (var7 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var6.setHorizontalDimensionBehaviour(var7);
            }

            if (var8 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var6.setVerticalDimensionBehaviour(var8);
            }
         } else {
            if (var3) {
               Optimizer.checkMatchParent(this, var1, var6);
            }

            var6.addToSolver(var1, var2);
         }
      }

      if (this.mHorizontalChainsSize > 0) {
         this.applyHorizontalChain(var1);
      }

      if (this.mVerticalChainsSize > 0) {
         this.applyVerticalChain(var1);
      }

      return true;
   }

   public void findHorizontalWrapRecursive(ConstraintWidget var1, boolean[] var2) {
      ConstraintWidget.DimensionBehaviour var7 = var1.mHorizontalDimensionBehaviour;
      ConstraintWidget.DimensionBehaviour var8 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
      boolean var6 = false;
      if (var7 == var8 && var1.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var1.mDimensionRatio > 0.0F) {
         var2[0] = false;
      } else {
         int var3 = var1.getOptimizerWrapWidth();
         if (var1.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var1.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var1.mDimensionRatio > 0.0F) {
            var2[0] = false;
         } else {
            int var4 = var3;
            var8 = null;
            ConstraintWidget var11 = null;
            var1.mHorizontalWrapVisited = true;
            if (var1 instanceof Guideline) {
               Guideline var10 = (Guideline)var1;
               if (var10.getOrientation() == 1) {
                  var3 = 0;
                  var4 = 0;
                  if (var10.getRelativeBegin() != -1) {
                     var3 = var10.getRelativeBegin();
                  } else if (var10.getRelativeEnd() != -1) {
                     var4 = var10.getRelativeEnd();
                  }
               }
            } else if (!var1.mRight.isConnected() && !var1.mLeft.isConnected()) {
               var3 += var1.getX();
            } else {
               if (var1.mRight.mTarget != null && var1.mLeft.mTarget != null && (var1.mRight.mTarget == var1.mLeft.mTarget || var1.mRight.mTarget.mOwner == var1.mLeft.mTarget.mOwner && var1.mRight.mTarget.mOwner != var1.mParent)) {
                  var2[0] = false;
                  return;
               }

               if (var1.mRight.mTarget != null) {
                  var11 = var1.mRight.mTarget.mOwner;
                  var4 = var3 + var1.mRight.getMargin();
                  if (!var11.isRoot() && !var11.mHorizontalWrapVisited) {
                     this.findHorizontalWrapRecursive(var11, var2);
                  }
               }

               ConstraintWidget var9;
               if (var1.mLeft.mTarget != null) {
                  ConstraintWidget var12 = var1.mLeft.mTarget.mOwner;
                  var3 += var1.mLeft.getMargin();
                  if (!var12.isRoot() && !var12.mHorizontalWrapVisited) {
                     this.findHorizontalWrapRecursive(var12, var2);
                     var9 = var12;
                  } else {
                     var9 = var12;
                  }
               } else {
                  var9 = var8;
               }

               boolean var5;
               if (var1.mRight.mTarget != null && !var11.isRoot()) {
                  if (var1.mRight.mTarget.mType == ConstraintAnchor.Type.RIGHT) {
                     var4 += var11.mDistToRight - var11.getOptimizerWrapWidth();
                  } else if (var1.mRight.mTarget.getType() == ConstraintAnchor.Type.LEFT) {
                     var4 += var11.mDistToRight;
                  }

                  if (var11.mRightHasCentered || var11.mLeft.mTarget != null && var11.mRight.mTarget != null && var11.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                     var5 = true;
                  } else {
                     var5 = false;
                  }

                  var1.mRightHasCentered = var5;
                  if (var1.mRightHasCentered && (var11.mLeft.mTarget == null || var11.mLeft.mTarget.mOwner != var1)) {
                     var4 += var4 - var11.mDistToRight;
                  }
               }

               if (var1.mLeft.mTarget != null && !var9.isRoot()) {
                  if (var1.mLeft.mTarget.getType() == ConstraintAnchor.Type.LEFT) {
                     var3 += var9.mDistToLeft - var9.getOptimizerWrapWidth();
                  } else if (var1.mLeft.mTarget.getType() == ConstraintAnchor.Type.RIGHT) {
                     var3 += var9.mDistToLeft;
                  }

                  if (var9.mLeftHasCentered || var9.mLeft.mTarget != null && var9.mRight.mTarget != null && var9.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                     var5 = true;
                  } else {
                     var5 = var6;
                  }

                  var1.mLeftHasCentered = var5;
                  if (var1.mLeftHasCentered && (var9.mRight.mTarget == null || var9.mRight.mTarget.mOwner != var1)) {
                     var3 += var3 - var9.mDistToLeft;
                  }
               }
            }

            if (var1.getVisibility() == 8) {
               var3 -= var1.mWidth;
               var4 -= var1.mWidth;
            }

            var1.mDistToLeft = var3;
            var1.mDistToRight = var4;
         }
      }
   }

   public void findVerticalWrapRecursive(ConstraintWidget var1, boolean[] var2) {
      ConstraintWidget.DimensionBehaviour var8 = var1.mVerticalDimensionBehaviour;
      ConstraintWidget.DimensionBehaviour var9 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
      boolean var7 = false;
      if (var8 == var9 && var1.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var1.mDimensionRatio > 0.0F) {
         var2[0] = false;
      } else {
         int var5 = var1.getOptimizerWrapHeight();
         int var4 = var5;
         int var3 = var5;
         ConstraintWidget var12 = null;
         var9 = null;
         var1.mVerticalWrapVisited = true;
         if (var1 instanceof Guideline) {
            Guideline var11 = (Guideline)var1;
            if (var11.getOrientation() == 0) {
               var4 = 0;
               var3 = 0;
               if (var11.getRelativeBegin() != -1) {
                  var4 = var11.getRelativeBegin();
               } else if (var11.getRelativeEnd() != -1) {
                  var3 = var11.getRelativeEnd();
               }
            }
         } else if (var1.mBaseline.mTarget == null && var1.mTop.mTarget == null && var1.mBottom.mTarget == null) {
            var4 = var5 + var1.getY();
         } else {
            if (var1.mBottom.mTarget != null && var1.mTop.mTarget != null && (var1.mBottom.mTarget == var1.mTop.mTarget || var1.mBottom.mTarget.mOwner == var1.mTop.mTarget.mOwner && var1.mBottom.mTarget.mOwner != var1.mParent)) {
               var2[0] = false;
               return;
            }

            if (var1.mBaseline.isConnected()) {
               var12 = var1.mBaseline.mTarget.getOwner();
               if (!var12.mVerticalWrapVisited) {
                  this.findVerticalWrapRecursive(var12, var2);
               }

               var3 = Math.max(var12.mDistToTop - var12.mHeight + var5, var5);
               var4 = Math.max(var12.mDistToBottom - var12.mHeight + var5, var5);
               if (var1.getVisibility() == 8) {
                  var3 -= var1.mHeight;
                  var4 -= var1.mHeight;
               }

               var1.mDistToTop = var3;
               var1.mDistToBottom = var4;
               return;
            }

            if (var1.mTop.isConnected()) {
               var12 = var1.mTop.mTarget.getOwner();
               var4 = var5 + var1.mTop.getMargin();
               if (!var12.isRoot() && !var12.mVerticalWrapVisited) {
                  this.findVerticalWrapRecursive(var12, var2);
               }
            }

            ConstraintWidget var10;
            if (var1.mBottom.isConnected()) {
               ConstraintWidget var13 = var1.mBottom.mTarget.getOwner();
               var3 = var5 + var1.mBottom.getMargin();
               if (!var13.isRoot() && !var13.mVerticalWrapVisited) {
                  this.findVerticalWrapRecursive(var13, var2);
                  var10 = var13;
               } else {
                  var10 = var13;
               }
            } else {
               var10 = var9;
            }

            boolean var6;
            if (var1.mTop.mTarget != null && !var12.isRoot()) {
               if (var1.mTop.mTarget.getType() == ConstraintAnchor.Type.TOP) {
                  var4 += var12.mDistToTop - var12.getOptimizerWrapHeight();
               } else if (var1.mTop.mTarget.getType() == ConstraintAnchor.Type.BOTTOM) {
                  var4 += var12.mDistToTop;
               }

               if (var12.mTopHasCentered || var12.mTop.mTarget != null && var12.mTop.mTarget.mOwner != var1 && var12.mBottom.mTarget != null && var12.mBottom.mTarget.mOwner != var1 && var12.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               var1.mTopHasCentered = var6;
               if (var1.mTopHasCentered && (var12.mBottom.mTarget == null || var12.mBottom.mTarget.mOwner != var1)) {
                  var4 += var4 - var12.mDistToTop;
               }
            }

            if (var1.mBottom.mTarget != null && !var10.isRoot()) {
               if (var1.mBottom.mTarget.getType() == ConstraintAnchor.Type.BOTTOM) {
                  var3 += var10.mDistToBottom - var10.getOptimizerWrapHeight();
               } else if (var1.mBottom.mTarget.getType() == ConstraintAnchor.Type.TOP) {
                  var3 += var10.mDistToBottom;
               }

               if (var10.mBottomHasCentered || var10.mTop.mTarget != null && var10.mTop.mTarget.mOwner != var1 && var10.mBottom.mTarget != null && var10.mBottom.mTarget.mOwner != var1 && var10.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                  var6 = true;
               } else {
                  var6 = var7;
               }

               var1.mBottomHasCentered = var6;
               if (var1.mBottomHasCentered && (var10.mTop.mTarget == null || var10.mTop.mTarget.mOwner != var1)) {
                  var3 += var3 - var10.mDistToBottom;
               }
            }
         }

         if (var1.getVisibility() == 8) {
            var4 -= var1.mHeight;
            var3 -= var1.mHeight;
         }

         var1.mDistToTop = var4;
         var1.mDistToBottom = var3;
      }
   }

   public void findWrapSize(ArrayList var1, boolean[] var2) {
      int var9 = 0;
      int var11 = 0;
      int var10 = 0;
      int var8 = 0;
      int var7 = 0;
      int var5 = 0;
      int var12 = var1.size();
      var2[0] = true;

      int var3;
      for(int var6 = 0; var6 < var12; ++var6) {
         ConstraintWidget var13 = (ConstraintWidget)var1.get(var6);
         if (!var13.isRoot()) {
            if (!var13.mHorizontalWrapVisited) {
               this.findHorizontalWrapRecursive(var13, var2);
            }

            if (!var13.mVerticalWrapVisited) {
               this.findVerticalWrapRecursive(var13, var2);
            }

            if (!var2[0]) {
               return;
            }

            var3 = var13.mDistToLeft + var13.mDistToRight - var13.getWidth();
            int var4 = var13.mDistToTop + var13.mDistToBottom - var13.getHeight();
            if (var13.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
               var3 = var13.getWidth() + var13.mLeft.mMargin + var13.mRight.mMargin;
            }

            if (var13.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
               var4 = var13.getHeight() + var13.mTop.mMargin + var13.mBottom.mMargin;
            }

            if (var13.getVisibility() == 8) {
               var3 = 0;
               var4 = 0;
            }

            var11 = Math.max(var11, var13.mDistToLeft);
            var10 = Math.max(var10, var13.mDistToRight);
            var8 = Math.max(var8, var13.mDistToBottom);
            var9 = Math.max(var9, var13.mDistToTop);
            var7 = Math.max(var7, var3);
            var5 = Math.max(var5, var4);
         }
      }

      var3 = Math.max(var11, var10);
      this.mWrapWidth = Math.max(this.mMinWidth, Math.max(var3, var7));
      var3 = Math.max(var9, var8);
      this.mWrapHeight = Math.max(this.mMinHeight, Math.max(var3, var5));

      for(var3 = 0; var3 < var12; ++var3) {
         ConstraintWidget var14 = (ConstraintWidget)var1.get(var3);
         var14.mHorizontalWrapVisited = false;
         var14.mVerticalWrapVisited = false;
         var14.mLeftHasCentered = false;
         var14.mRightHasCentered = false;
         var14.mTopHasCentered = false;
         var14.mBottomHasCentered = false;
      }

   }

   public ArrayList getHorizontalGuidelines() {
      ArrayList var3 = new ArrayList();
      int var1 = 0;

      for(int var2 = this.mChildren.size(); var1 < var2; ++var1) {
         ConstraintWidget var4 = (ConstraintWidget)this.mChildren.get(var1);
         if (var4 instanceof Guideline) {
            Guideline var5 = (Guideline)var4;
            if (var5.getOrientation() == 0) {
               var3.add(var5);
            }
         }
      }

      return var3;
   }

   public LinearSystem getSystem() {
      return this.mSystem;
   }

   public String getType() {
      return "ConstraintLayout";
   }

   public ArrayList getVerticalGuidelines() {
      ArrayList var3 = new ArrayList();
      int var1 = 0;

      for(int var2 = this.mChildren.size(); var1 < var2; ++var1) {
         ConstraintWidget var4 = (ConstraintWidget)this.mChildren.get(var1);
         if (var4 instanceof Guideline) {
            Guideline var5 = (Guideline)var4;
            if (var5.getOrientation() == 1) {
               var3.add(var5);
            }
         }
      }

      return var3;
   }

   public boolean handlesInternalConstraints() {
      return false;
   }

   public boolean isHeightMeasuredTooSmall() {
      return this.mHeightMeasuredTooSmall;
   }

   public boolean isWidthMeasuredTooSmall() {
      return this.mWidthMeasuredTooSmall;
   }

   public void layout() {
      int var5 = this.mX;
      int var6 = this.mY;
      int var7 = Math.max(0, this.getWidth());
      int var8 = Math.max(0, this.getHeight());
      this.mWidthMeasuredTooSmall = false;
      this.mHeightMeasuredTooSmall = false;
      if (this.mParent != null) {
         if (this.mSnapshot == null) {
            this.mSnapshot = new Snapshot(this);
         }

         this.mSnapshot.updateFrom(this);
         this.setX(this.mPaddingLeft);
         this.setY(this.mPaddingTop);
         this.resetAnchors();
         this.resetSolverVariables(this.mSystem.getCache());
      } else {
         this.mX = 0;
         this.mY = 0;
      }

      boolean var10 = false;
      ConstraintWidget.DimensionBehaviour var13 = this.mVerticalDimensionBehaviour;
      ConstraintWidget.DimensionBehaviour var14 = this.mHorizontalDimensionBehaviour;
      if (this.mOptimizationLevel == 2 && (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)) {
         this.findWrapSize(this.mChildren, this.flags);
         var10 = this.flags[0];
         if (var7 > 0 && var8 > 0 && (this.mWrapWidth > var7 || this.mWrapHeight > var8)) {
            var10 = false;
         }

         if (var10) {
            if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
               if (var7 > 0 && var7 < this.mWrapWidth) {
                  this.mWidthMeasuredTooSmall = true;
                  this.setWidth(var7);
               } else {
                  this.setWidth(Math.max(this.mMinWidth, this.mWrapWidth));
               }
            }

            if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
               if (var8 > 0 && var8 < this.mWrapHeight) {
                  this.mHeightMeasuredTooSmall = true;
                  this.setHeight(var8);
               } else {
                  this.setHeight(Math.max(this.mMinHeight, this.mWrapHeight));
               }
            }
         }
      }

      this.resetChains();
      int var9 = this.mChildren.size();

      int var1;
      ConstraintWidget var15;
      for(var1 = 0; var1 < var9; ++var1) {
         var15 = (ConstraintWidget)this.mChildren.get(var1);
         if (var15 instanceof WidgetContainer) {
            ((WidgetContainer)var15).layout();
         }
      }

      boolean var12 = true;

      int var2;
      boolean var11;
      for(var1 = 0; var12; var10 = var11) {
         int var4 = var1 + 1;
         var11 = var12;

         label187: {
            label186: {
               Exception var10000;
               label234: {
                  boolean var10001;
                  try {
                     this.mSystem.reset();
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label234;
                  }

                  var11 = var12;

                  try {
                     var12 = this.addChildrenToSolver(this.mSystem, Integer.MAX_VALUE);
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label234;
                  }

                  if (!var12) {
                     break label186;
                  }

                  var11 = var12;

                  try {
                     this.mSystem.minimize();
                     break label186;
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                  }
               }

               Exception var19 = var10000;
               var19.printStackTrace();
               break label187;
            }

            var11 = var12;
         }

         if (var11) {
            this.updateChildrenFromSolver(this.mSystem, Integer.MAX_VALUE, this.flags);
         } else {
            this.updateFromSolver(this.mSystem, Integer.MAX_VALUE);

            for(var1 = 0; var1 < var9; ++var1) {
               var15 = (ConstraintWidget)this.mChildren.get(var1);
               if (var15.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var15.getWidth() < var15.getWrapWidth()) {
                  this.flags[2] = true;
                  break;
               }

               if (var15.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var15.getHeight() < var15.getWrapHeight()) {
                  this.flags[2] = true;
                  break;
               }
            }
         }

         var12 = false;
         var11 = false;
         if (var4 < 8 && this.flags[2]) {
            int var3 = 0;
            var1 = 0;

            for(var2 = 0; var2 < var9; ++var2) {
               var15 = (ConstraintWidget)this.mChildren.get(var2);
               var3 = Math.max(var3, var15.field_9 + var15.getWidth());
               var1 = Math.max(var1, var15.field_10 + var15.getHeight());
            }

            var2 = Math.max(this.mMinWidth, var3);
            var1 = Math.max(this.mMinHeight, var1);
            if (var14 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               if (this.getWidth() < var2) {
                  this.setWidth(var2);
                  this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                  var12 = true;
                  var11 = true;
               } else {
                  var12 = var10;
               }
            } else {
               var12 = var10;
            }

            if (var13 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               if (this.getHeight() < var1) {
                  this.setHeight(var1);
                  this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                  var11 = true;
                  var10 = true;
               } else {
                  var10 = var11;
                  var11 = var12;
               }
            } else {
               var10 = var11;
               var11 = var12;
            }
         } else {
            var11 = var10;
            var10 = var12;
         }

         var1 = Math.max(this.mMinWidth, this.getWidth());
         if (var1 > this.getWidth()) {
            this.setWidth(var1);
            this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
            var12 = true;
            var11 = true;
         } else {
            var12 = var11;
            var11 = var10;
         }

         var1 = Math.max(this.mMinHeight, this.getHeight());
         if (var1 > this.getHeight()) {
            this.setHeight(var1);
            this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
            var10 = true;
            var11 = true;
         } else {
            var10 = var12;
         }

         if (!var10) {
            if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && var7 > 0) {
               if (this.getWidth() > var7) {
                  this.mWidthMeasuredTooSmall = true;
                  var12 = true;
                  this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
                  this.setWidth(var7);
                  var11 = true;
               } else {
                  var12 = var10;
               }
            } else {
               var12 = var10;
            }

            if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && var8 > 0 && this.getHeight() > var8) {
               this.mHeightMeasuredTooSmall = true;
               var11 = true;
               this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
               this.setHeight(var8);
               var10 = true;
            } else {
               var10 = var11;
               var11 = var12;
            }
         } else {
            var12 = var11;
            var11 = var10;
            var10 = var12;
         }

         var12 = var10;
         var1 = var4;
      }

      if (this.mParent != null) {
         var1 = Math.max(this.mMinWidth, this.getWidth());
         var2 = Math.max(this.mMinHeight, this.getHeight());
         this.mSnapshot.applyTo(this);
         this.setWidth(this.mPaddingLeft + var1 + this.mPaddingRight);
         this.setHeight(this.mPaddingTop + var2 + this.mPaddingBottom);
      } else {
         this.mX = var5;
         this.mY = var6;
      }

      if (var10) {
         this.mHorizontalDimensionBehaviour = var14;
         this.mVerticalDimensionBehaviour = var13;
      }

      this.resetSolverVariables(this.mSystem.getCache());
      if (this == this.getRootConstraintContainer()) {
         this.updateDrawPosition();
      }
   }

   public int layoutFindGroups() {
      ConstraintAnchor.Type[] var8 = new ConstraintAnchor.Type[]{ConstraintAnchor.Type.LEFT, ConstraintAnchor.Type.RIGHT, ConstraintAnchor.Type.TOP, ConstraintAnchor.Type.BASELINE, ConstraintAnchor.Type.BOTTOM};
      int var1 = 1;
      int var6 = this.mChildren.size();

      int var2;
      ConstraintAnchor var9;
      ConstraintAnchor var14;
      for(var2 = 0; var2 < var6; ++var2) {
         ConstraintWidget var7 = (ConstraintWidget)this.mChildren.get(var2);
         var9 = var7.mLeft;
         if (var9.mTarget != null) {
            if (setGroup(var9, var1) == var1) {
               ++var1;
            }
         } else {
            var9.mGroup = Integer.MAX_VALUE;
         }

         var9 = var7.mTop;
         if (var9.mTarget != null) {
            if (setGroup(var9, var1) == var1) {
               ++var1;
            }
         } else {
            var9.mGroup = Integer.MAX_VALUE;
         }

         var9 = var7.mRight;
         if (var9.mTarget != null) {
            if (setGroup(var9, var1) == var1) {
               ++var1;
            }
         } else {
            var9.mGroup = Integer.MAX_VALUE;
         }

         var9 = var7.mBottom;
         if (var9.mTarget != null) {
            if (setGroup(var9, var1) == var1) {
               ++var1;
            }
         } else {
            var9.mGroup = Integer.MAX_VALUE;
         }

         var14 = var7.mBaseline;
         if (var14.mTarget != null) {
            if (setGroup(var14, var1) == var1) {
               ++var1;
            }
         } else {
            var14.mGroup = Integer.MAX_VALUE;
         }
      }

      boolean var3 = true;
      var2 = 0;

      int var5;
      int var12;
      int var13;
      for(var1 = 0; var3; var2 = var5) {
         boolean var4 = false;
         var5 = var2 + 1;
         var12 = 0;

         boolean var11;
         for(var11 = var4; var12 < var6; ++var12) {
            ConstraintWidget var18 = (ConstraintWidget)this.mChildren.get(var12);

            for(var13 = 0; var13 < var8.length; ++var13) {
               ConstraintAnchor.Type var10 = var8[var13];
               var14 = null;
               switch(var10) {
               case LEFT:
                  var14 = var18.mLeft;
                  break;
               case TOP:
                  var14 = var18.mTop;
                  break;
               case RIGHT:
                  var14 = var18.mRight;
                  break;
               case BOTTOM:
                  var14 = var18.mBottom;
                  break;
               case BASELINE:
                  var14 = var18.mBaseline;
               }

               ConstraintAnchor var19 = var14.mTarget;
               if (var19 != null) {
                  if (var19.mOwner.getParent() != null && var19.mGroup != var14.mGroup) {
                     if (var14.mGroup > var19.mGroup) {
                        var2 = var19.mGroup;
                     } else {
                        var2 = var14.mGroup;
                     }

                     var14.mGroup = var2;
                     var19.mGroup = var2;
                     ++var1;
                     var11 = true;
                  }

                  var19 = var19.getOpposite();
                  if (var19 != null && var19.mGroup != var14.mGroup) {
                     if (var14.mGroup > var19.mGroup) {
                        var2 = var19.mGroup;
                     } else {
                        var2 = var14.mGroup;
                     }

                     var14.mGroup = var2;
                     var19.mGroup = var2;
                     ++var1;
                     var11 = true;
                  }
               }
            }
         }

         var3 = var11;
      }

      var1 = 0;
      int[] var17 = new int[this.mChildren.size() * var8.length + 1];
      Arrays.fill(var17, -1);

      for(var2 = 0; var2 < var6; ++var2) {
         ConstraintWidget var15 = (ConstraintWidget)this.mChildren.get(var2);
         var9 = var15.mLeft;
         if (var9.mGroup != Integer.MAX_VALUE) {
            var12 = var9.mGroup;
            if (var17[var12] == -1) {
               var17[var12] = var1++;
            }

            var9.mGroup = var17[var12];
         }

         var9 = var15.mTop;
         if (var9.mGroup != Integer.MAX_VALUE) {
            var12 = var9.mGroup;
            if (var17[var12] == -1) {
               var17[var12] = var1++;
            }

            var9.mGroup = var17[var12];
         }

         var9 = var15.mRight;
         if (var9.mGroup != Integer.MAX_VALUE) {
            var12 = var9.mGroup;
            if (var17[var12] == -1) {
               var17[var12] = var1++;
            }

            var9.mGroup = var17[var12];
         }

         var9 = var15.mBottom;
         if (var9.mGroup != Integer.MAX_VALUE) {
            var12 = var9.mGroup;
            if (var17[var12] == -1) {
               var17[var12] = var1++;
            }

            var9.mGroup = var17[var12];
         }

         ConstraintAnchor var16 = var15.mBaseline;
         if (var16.mGroup != Integer.MAX_VALUE) {
            var13 = var16.mGroup;
            if (var17[var13] == -1) {
               var12 = var1 + 1;
               var17[var13] = var1;
               var1 = var12;
            }

            var16.mGroup = var17[var13];
         }
      }

      return var1;
   }

   public int layoutFindGroupsSimple() {
      int var2 = this.mChildren.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         ConstraintWidget var3 = (ConstraintWidget)this.mChildren.get(var1);
         var3.mLeft.mGroup = 0;
         var3.mRight.mGroup = 0;
         var3.mTop.mGroup = 1;
         var3.mBottom.mGroup = 1;
         var3.mBaseline.mGroup = 1;
      }

      return 2;
   }

   public void layoutWithGroup(int var1) {
      int var3 = this.mX;
      int var4 = this.mY;
      if (this.mParent != null) {
         if (this.mSnapshot == null) {
            this.mSnapshot = new Snapshot(this);
         }

         this.mSnapshot.updateFrom(this);
         this.mX = 0;
         this.mY = 0;
         this.resetAnchors();
         this.resetSolverVariables(this.mSystem.getCache());
      } else {
         this.mX = 0;
         this.mY = 0;
      }

      int var5 = this.mChildren.size();

      int var2;
      for(var2 = 0; var2 < var5; ++var2) {
         ConstraintWidget var6 = (ConstraintWidget)this.mChildren.get(var2);
         if (var6 instanceof WidgetContainer) {
            ((WidgetContainer)var6).layout();
         }
      }

      this.mLeft.mGroup = 0;
      this.mRight.mGroup = 0;
      this.mTop.mGroup = 1;
      this.mBottom.mGroup = 1;
      this.mSystem.reset();

      for(var2 = 0; var2 < var1; ++var2) {
         try {
            this.addToSolver(this.mSystem, var2);
            this.mSystem.minimize();
            this.updateFromSolver(this.mSystem, var2);
         } catch (Exception var7) {
            var7.printStackTrace();
         }

         this.updateFromSolver(this.mSystem, -2);
      }

      if (this.mParent != null) {
         var1 = this.getWidth();
         var2 = this.getHeight();
         this.mSnapshot.applyTo(this);
         this.setWidth(var1);
         this.setHeight(var2);
      } else {
         this.mX = var3;
         this.mY = var4;
      }

      if (this == this.getRootConstraintContainer()) {
         this.updateDrawPosition();
      }
   }

   public void reset() {
      this.mSystem.reset();
      this.mPaddingLeft = 0;
      this.mPaddingRight = 0;
      this.mPaddingTop = 0;
      this.mPaddingBottom = 0;
      super.reset();
   }

   public void setOptimizationLevel(int var1) {
      this.mOptimizationLevel = var1;
   }

   public void setPadding(int var1, int var2, int var3, int var4) {
      this.mPaddingLeft = var1;
      this.mPaddingTop = var2;
      this.mPaddingRight = var3;
      this.mPaddingBottom = var4;
   }

   public void updateChildrenFromSolver(LinearSystem var1, int var2, boolean[] var3) {
      var3[2] = false;
      this.updateFromSolver(var1, var2);
      int var5 = this.mChildren.size();

      for(int var4 = 0; var4 < var5; ++var4) {
         ConstraintWidget var6 = (ConstraintWidget)this.mChildren.get(var4);
         var6.updateFromSolver(var1, var2);
         if (var6.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var6.getWidth() < var6.getWrapWidth()) {
            var3[2] = true;
         }

         if (var6.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var6.getHeight() < var6.getWrapHeight()) {
            var3[2] = true;
         }
      }

   }
}
