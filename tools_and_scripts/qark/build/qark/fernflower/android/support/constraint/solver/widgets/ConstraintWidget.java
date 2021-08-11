package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;

public class ConstraintWidget {
   private static final boolean AUTOTAG_CENTER = false;
   public static final int CHAIN_PACKED = 2;
   public static final int CHAIN_SPREAD = 0;
   public static final int CHAIN_SPREAD_INSIDE = 1;
   public static float DEFAULT_BIAS = 0.5F;
   protected static final int DIRECT = 2;
   public static final int GONE = 8;
   public static final int HORIZONTAL = 0;
   public static final int INVISIBLE = 4;
   public static final int MATCH_CONSTRAINT_SPREAD = 0;
   public static final int MATCH_CONSTRAINT_WRAP = 1;
   protected static final int SOLVER = 1;
   public static final int UNKNOWN = -1;
   public static final int VERTICAL = 1;
   public static final int VISIBLE = 0;
   protected ArrayList mAnchors;
   ConstraintAnchor mBaseline;
   int mBaselineDistance;
   ConstraintAnchor mBottom;
   boolean mBottomHasCentered;
   ConstraintAnchor mCenter;
   ConstraintAnchor mCenterX;
   ConstraintAnchor mCenterY;
   private Object mCompanionWidget;
   private int mContainerItemSkip;
   private String mDebugName;
   protected float mDimensionRatio;
   protected int mDimensionRatioSide;
   int mDistToBottom;
   int mDistToLeft;
   int mDistToRight;
   int mDistToTop;
   private int mDrawHeight;
   private int mDrawWidth;
   private int mDrawX;
   private int mDrawY;
   int mHeight;
   float mHorizontalBiasPercent;
   boolean mHorizontalChainFixedPosition;
   int mHorizontalChainStyle;
   ConstraintWidget.DimensionBehaviour mHorizontalDimensionBehaviour;
   ConstraintWidget mHorizontalNextWidget;
   public int mHorizontalResolution;
   float mHorizontalWeight;
   boolean mHorizontalWrapVisited;
   ConstraintAnchor mLeft;
   boolean mLeftHasCentered;
   int mMatchConstraintDefaultHeight;
   int mMatchConstraintDefaultWidth;
   int mMatchConstraintMaxHeight;
   int mMatchConstraintMaxWidth;
   int mMatchConstraintMinHeight;
   int mMatchConstraintMinWidth;
   protected int mMinHeight;
   protected int mMinWidth;
   protected int mOffsetX;
   protected int mOffsetY;
   ConstraintWidget mParent;
   ConstraintAnchor mRight;
   boolean mRightHasCentered;
   private int mSolverBottom;
   private int mSolverLeft;
   private int mSolverRight;
   private int mSolverTop;
   ConstraintAnchor mTop;
   boolean mTopHasCentered;
   private String mType;
   float mVerticalBiasPercent;
   boolean mVerticalChainFixedPosition;
   int mVerticalChainStyle;
   ConstraintWidget.DimensionBehaviour mVerticalDimensionBehaviour;
   ConstraintWidget mVerticalNextWidget;
   public int mVerticalResolution;
   float mVerticalWeight;
   boolean mVerticalWrapVisited;
   private int mVisibility;
   int mWidth;
   private int mWrapHeight;
   private int mWrapWidth;
   // $FF: renamed from: mX int
   protected int field_9;
   // $FF: renamed from: mY int
   protected int field_10;

   public ConstraintWidget() {
      this.mHorizontalResolution = -1;
      this.mVerticalResolution = -1;
      this.mMatchConstraintDefaultWidth = 0;
      this.mMatchConstraintDefaultHeight = 0;
      this.mMatchConstraintMinWidth = 0;
      this.mMatchConstraintMaxWidth = 0;
      this.mMatchConstraintMinHeight = 0;
      this.mMatchConstraintMaxHeight = 0;
      this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
      this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
      this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
      this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
      this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
      this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
      this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
      this.mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
      this.mAnchors = new ArrayList();
      this.mParent = null;
      this.mWidth = 0;
      this.mHeight = 0;
      this.mDimensionRatio = 0.0F;
      this.mDimensionRatioSide = -1;
      this.mSolverLeft = 0;
      this.mSolverTop = 0;
      this.mSolverRight = 0;
      this.mSolverBottom = 0;
      this.field_9 = 0;
      this.field_10 = 0;
      this.mDrawX = 0;
      this.mDrawY = 0;
      this.mDrawWidth = 0;
      this.mDrawHeight = 0;
      this.mOffsetX = 0;
      this.mOffsetY = 0;
      this.mBaselineDistance = 0;
      float var1 = DEFAULT_BIAS;
      this.mHorizontalBiasPercent = var1;
      this.mVerticalBiasPercent = var1;
      this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
      this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
      this.mContainerItemSkip = 0;
      this.mVisibility = 0;
      this.mDebugName = null;
      this.mType = null;
      this.mHorizontalChainStyle = 0;
      this.mVerticalChainStyle = 0;
      this.mHorizontalWeight = 0.0F;
      this.mVerticalWeight = 0.0F;
      this.mHorizontalNextWidget = null;
      this.mVerticalNextWidget = null;
      this.addAnchors();
   }

   public ConstraintWidget(int var1, int var2) {
      this(0, 0, var1, var2);
   }

   public ConstraintWidget(int var1, int var2, int var3, int var4) {
      this.mHorizontalResolution = -1;
      this.mVerticalResolution = -1;
      this.mMatchConstraintDefaultWidth = 0;
      this.mMatchConstraintDefaultHeight = 0;
      this.mMatchConstraintMinWidth = 0;
      this.mMatchConstraintMaxWidth = 0;
      this.mMatchConstraintMinHeight = 0;
      this.mMatchConstraintMaxHeight = 0;
      this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
      this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
      this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
      this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
      this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
      this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
      this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
      this.mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
      this.mAnchors = new ArrayList();
      this.mParent = null;
      this.mWidth = 0;
      this.mHeight = 0;
      this.mDimensionRatio = 0.0F;
      this.mDimensionRatioSide = -1;
      this.mSolverLeft = 0;
      this.mSolverTop = 0;
      this.mSolverRight = 0;
      this.mSolverBottom = 0;
      this.field_9 = 0;
      this.field_10 = 0;
      this.mDrawX = 0;
      this.mDrawY = 0;
      this.mDrawWidth = 0;
      this.mDrawHeight = 0;
      this.mOffsetX = 0;
      this.mOffsetY = 0;
      this.mBaselineDistance = 0;
      float var5 = DEFAULT_BIAS;
      this.mHorizontalBiasPercent = var5;
      this.mVerticalBiasPercent = var5;
      this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
      this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
      this.mContainerItemSkip = 0;
      this.mVisibility = 0;
      this.mDebugName = null;
      this.mType = null;
      this.mHorizontalChainStyle = 0;
      this.mVerticalChainStyle = 0;
      this.mHorizontalWeight = 0.0F;
      this.mVerticalWeight = 0.0F;
      this.mHorizontalNextWidget = null;
      this.mVerticalNextWidget = null;
      this.field_9 = var1;
      this.field_10 = var2;
      this.mWidth = var3;
      this.mHeight = var4;
      this.addAnchors();
      this.forceUpdateDrawPosition();
   }

   private void addAnchors() {
      this.mAnchors.add(this.mLeft);
      this.mAnchors.add(this.mTop);
      this.mAnchors.add(this.mRight);
      this.mAnchors.add(this.mBottom);
      this.mAnchors.add(this.mCenterX);
      this.mAnchors.add(this.mCenterY);
      this.mAnchors.add(this.mBaseline);
   }

   private void applyConstraints(LinearSystem var1, boolean var2, boolean var3, ConstraintAnchor var4, ConstraintAnchor var5, int var6, int var7, int var8, int var9, float var10, boolean var11, boolean var12, int var13, int var14, int var15) {
      SolverVariable var20 = var1.createObjectVariable(var4);
      SolverVariable var18 = var1.createObjectVariable(var5);
      SolverVariable var21 = var1.createObjectVariable(var4.getTarget());
      SolverVariable var19 = var1.createObjectVariable(var5.getTarget());
      int var16 = var4.getMargin();
      int var17 = var5.getMargin();
      if (this.mVisibility == 8) {
         var3 = true;
         var8 = 0;
      }

      if (var21 == null && var19 == null) {
         var1.addConstraint(var1.createRow().createRowEquals(var20, var6));
         if (!var11) {
            if (var2) {
               var1.addConstraint(LinearSystem.createRowEquals(var1, var18, var20, var9, true));
            } else if (var3) {
               var1.addConstraint(LinearSystem.createRowEquals(var1, var18, var20, var8, false));
            } else {
               var1.addConstraint(var1.createRow().createRowEquals(var18, var7));
            }
         }
      } else if (var21 != null && var19 == null) {
         var1.addConstraint(var1.createRow().createRowEquals(var20, var21, var16));
         if (var2) {
            var1.addConstraint(LinearSystem.createRowEquals(var1, var18, var20, var9, true));
         } else if (!var11) {
            if (var3) {
               var1.addConstraint(var1.createRow().createRowEquals(var18, var20, var8));
            } else {
               var1.addConstraint(var1.createRow().createRowEquals(var18, var7));
            }
         }
      } else if (var21 == null && var19 != null) {
         var1.addConstraint(var1.createRow().createRowEquals(var18, var19, var17 * -1));
         if (var2) {
            var1.addConstraint(LinearSystem.createRowEquals(var1, var18, var20, var9, true));
         } else if (!var11) {
            if (var3) {
               var1.addConstraint(var1.createRow().createRowEquals(var18, var20, var8));
            } else {
               var1.addConstraint(var1.createRow().createRowEquals(var20, var6));
            }
         }
      } else if (var3) {
         if (var2) {
            var1.addConstraint(LinearSystem.createRowEquals(var1, var18, var20, var9, true));
         } else {
            var1.addConstraint(var1.createRow().createRowEquals(var18, var20, var8));
         }

         if (var4.getStrength() != var5.getStrength()) {
            SolverVariable var22;
            ArrayRow var23;
            if (var4.getStrength() == ConstraintAnchor.Strength.STRONG) {
               var1.addConstraint(var1.createRow().createRowEquals(var20, var21, var16));
               var22 = var1.createSlackVariable();
               var23 = var1.createRow();
               var23.createRowLowerThan(var18, var19, var22, var17 * -1);
               var1.addConstraint(var23);
            } else {
               var22 = var1.createSlackVariable();
               var23 = var1.createRow();
               var23.createRowGreaterThan(var20, var21, var22, var16);
               var1.addConstraint(var23);
               var1.addConstraint(var1.createRow().createRowEquals(var18, var19, var17 * -1));
            }
         } else if (var21 == var19) {
            var1.addConstraint(LinearSystem.createRowCentering(var1, var20, var21, 0, 0.5F, var19, var18, 0, true));
         } else if (!var12) {
            if (var4.getConnectionType() != ConstraintAnchor.ConnectionType.STRICT) {
               var2 = true;
            } else {
               var2 = false;
            }

            var1.addConstraint(LinearSystem.createRowGreaterThan(var1, var20, var21, var16, var2));
            if (var5.getConnectionType() != ConstraintAnchor.ConnectionType.STRICT) {
               var2 = true;
            } else {
               var2 = false;
            }

            var1.addConstraint(LinearSystem.createRowLowerThan(var1, var18, var19, var17 * -1, var2));
            var1.addConstraint(LinearSystem.createRowCentering(var1, var20, var21, var16, var10, var19, var18, var17, false));
         }
      } else if (var11) {
         var1.addGreaterThan(var20, var21, var16, 3);
         var1.addLowerThan(var18, var19, var17 * -1, 3);
         var1.addConstraint(LinearSystem.createRowCentering(var1, var20, var21, var16, var10, var19, var18, var17, true));
      } else if (!var12) {
         if (var13 == 1) {
            if (var14 > var8) {
               var6 = var14;
            } else {
               var6 = var8;
            }

            if (var15 > 0) {
               if (var15 < var6) {
                  var6 = var15;
               } else {
                  var1.addLowerThan(var18, var20, var15, 3);
               }
            }

            var1.addEquality(var18, var20, var6, 3);
            var1.addGreaterThan(var20, var21, var16, 2);
            var1.addLowerThan(var18, var19, -var17, 2);
            var1.addCentering(var20, var21, var16, var10, var19, var18, var17, 4);
         } else if (var14 == 0 && var15 == 0) {
            var1.addConstraint(var1.createRow().createRowEquals(var20, var21, var16));
            var1.addConstraint(var1.createRow().createRowEquals(var18, var19, var17 * -1));
         } else {
            if (var15 > 0) {
               var1.addLowerThan(var18, var20, var15, 3);
            }

            var1.addGreaterThan(var20, var21, var16, 2);
            var1.addLowerThan(var18, var19, -var17, 2);
            var1.addCentering(var20, var21, var16, var10, var19, var18, var17, 4);
         }
      }
   }

   public void addToSolver(LinearSystem var1) {
      this.addToSolver(var1, Integer.MAX_VALUE);
   }

   public void addToSolver(LinearSystem var1, int var2) {
      SolverVariable var22 = null;
      SolverVariable var23 = null;
      if (var2 == Integer.MAX_VALUE || this.mLeft.mGroup == var2) {
         var22 = var1.createObjectVariable(this.mLeft);
      }

      if (var2 == Integer.MAX_VALUE || this.mRight.mGroup == var2) {
         var23 = var1.createObjectVariable(this.mRight);
      }

      SolverVariable var21;
      if (var2 != Integer.MAX_VALUE && this.mTop.mGroup != var2) {
         var21 = null;
      } else {
         var21 = var1.createObjectVariable(this.mTop);
      }

      SolverVariable var20;
      if (var2 != Integer.MAX_VALUE && this.mBottom.mGroup != var2) {
         var20 = null;
      } else {
         var20 = var1.createObjectVariable(this.mBottom);
      }

      SolverVariable var24;
      if (var2 != Integer.MAX_VALUE && this.mBaseline.mGroup != var2) {
         var24 = null;
      } else {
         var24 = var1.createObjectVariable(this.mBaseline);
      }

      boolean var14 = false;
      boolean var15 = false;
      boolean var16;
      boolean var17;
      SolverVariable var25;
      if (this.mParent != null) {
         if (this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft || this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight) {
            ((ConstraintWidgetContainer)this.mParent).addChain(this, 0);
            var14 = true;
         }

         if (this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop || this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom) {
            ((ConstraintWidgetContainer)this.mParent).addChain(this, 1);
            var15 = true;
         }

         ArrayRow var26;
         if (this.mParent.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && !var14) {
            if (this.mLeft.mTarget != null && this.mLeft.mTarget.mOwner == this.mParent) {
               if (this.mLeft.mTarget != null && this.mLeft.mTarget.mOwner == this.mParent) {
                  this.mLeft.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
               }
            } else {
               var25 = var1.createObjectVariable(this.mParent.mLeft);
               var26 = var1.createRow();
               var26.createRowGreaterThan(var22, var25, var1.createSlackVariable(), 0);
               var1.addConstraint(var26);
            }

            if (this.mRight.mTarget != null && this.mRight.mTarget.mOwner == this.mParent) {
               if (this.mRight.mTarget != null && this.mRight.mTarget.mOwner == this.mParent) {
                  this.mRight.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
               }
            } else {
               var25 = var1.createObjectVariable(this.mParent.mRight);
               var26 = var1.createRow();
               var26.createRowGreaterThan(var25, var23, var1.createSlackVariable(), 0);
               var1.addConstraint(var26);
            }
         }

         if (this.mParent.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && !var15) {
            if (this.mTop.mTarget != null && this.mTop.mTarget.mOwner == this.mParent) {
               if (this.mTop.mTarget != null && this.mTop.mTarget.mOwner == this.mParent) {
                  this.mTop.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
               }
            } else {
               var25 = var1.createObjectVariable(this.mParent.mTop);
               var26 = var1.createRow();
               var26.createRowGreaterThan(var21, var25, var1.createSlackVariable(), 0);
               var1.addConstraint(var26);
            }

            if (this.mBottom.mTarget != null && this.mBottom.mTarget.mOwner == this.mParent) {
               if (this.mBottom.mTarget != null && this.mBottom.mTarget.mOwner == this.mParent) {
                  this.mBottom.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
               }
            } else {
               var25 = var1.createObjectVariable(this.mParent.mBottom);
               var26 = var1.createRow();
               var26.createRowGreaterThan(var25, var20, var1.createSlackVariable(), 0);
               var1.addConstraint(var26);
            }
         }

         var17 = var14;
         var16 = var15;
      } else {
         var17 = false;
         var16 = false;
      }

      int var6 = this.mWidth;
      if (var6 < this.mMinWidth) {
         var6 = this.mMinWidth;
      }

      int var5 = this.mHeight;
      if (var5 < this.mMinHeight) {
         var5 = this.mMinHeight;
      }

      if (this.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
         var15 = true;
      } else {
         var15 = false;
      }

      if (this.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
         var14 = true;
      } else {
         var14 = false;
      }

      ConstraintAnchor var37;
      if (!var15) {
         var37 = this.mLeft;
         if (var37 != null && this.mRight != null && (var37.mTarget == null || this.mRight.mTarget == null)) {
            var15 = true;
         }
      }

      if (!var14) {
         var37 = this.mTop;
         if (var37 != null && this.mBottom != null && (var37.mTarget == null || this.mBottom.mTarget == null) && (this.mBaselineDistance == 0 || this.mBaseline != null && (this.mTop.mTarget == null || this.mBaseline.mTarget == null))) {
            var14 = true;
         }
      }

      float var3;
      boolean var7;
      int var8;
      int var9;
      label347: {
         var7 = false;
         var9 = this.mDimensionRatioSide;
         var3 = this.mDimensionRatio;
         if (this.mDimensionRatio > 0.0F && this.mVisibility != 8) {
            if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
               var7 = true;
               if (var15 && !var14) {
                  var8 = var6;
                  var7 = true;
                  var6 = 0;
                  break label347;
               }

               if (!var15 && var14) {
                  if (this.mDimensionRatioSide == -1) {
                     var3 = 1.0F / var3;
                     var8 = var6;
                     var6 = 1;
                     var7 = true;
                  } else {
                     var8 = var6;
                     var7 = true;
                     var6 = 1;
                  }
                  break label347;
               }
            } else {
               if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                  var8 = (int)((float)this.mHeight * var3);
                  var15 = true;
                  var7 = false;
                  var6 = 0;
                  break label347;
               }

               if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                  if (this.mDimensionRatioSide == -1) {
                     var3 = 1.0F / var3;
                  }

                  var5 = (int)((float)this.mWidth * var3);
                  var8 = var6;
                  var14 = true;
                  var7 = false;
                  var6 = 1;
                  break label347;
               }
            }
         }

         var8 = var6;
         var6 = var9;
      }

      boolean var18;
      if (!var7 || var6 != 0 && var6 != -1) {
         var18 = false;
      } else {
         var18 = true;
      }

      boolean var19;
      if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer) {
         var19 = true;
      } else {
         var19 = false;
      }

      SolverVariable var27;
      SolverVariable var28;
      SolverVariable var39;
      if (this.mHorizontalResolution != 2 && (var2 == Integer.MAX_VALUE || this.mLeft.mGroup == var2 && this.mRight.mGroup == var2)) {
         if (var18 && this.mLeft.mTarget != null && this.mRight.mTarget != null) {
            var25 = var1.createObjectVariable(this.mLeft);
            var39 = var1.createObjectVariable(this.mRight);
            var27 = var1.createObjectVariable(this.mLeft.getTarget());
            var28 = var1.createObjectVariable(this.mRight.getTarget());
            var1.addGreaterThan(var25, var27, this.mLeft.getMargin(), 3);
            var1.addLowerThan(var39, var28, this.mRight.getMargin() * -1, 3);
            if (!var17) {
               var1.addCentering(var25, var27, this.mLeft.getMargin(), this.mHorizontalBiasPercent, var28, var39, this.mRight.getMargin(), 4);
            }
         } else {
            var37 = this.mLeft;
            ConstraintAnchor var38 = this.mRight;
            var9 = this.field_9;
            this.applyConstraints(var1, var19, var15, var37, var38, var9, var9 + var8, var8, this.mMinWidth, this.mHorizontalBiasPercent, var18, var17, this.mMatchConstraintDefaultWidth, this.mMatchConstraintMinWidth, this.mMatchConstraintMaxWidth);
         }
      }

      var39 = var23;
      var27 = var22;
      var17 = false;
      if (this.mVerticalResolution != 2) {
         if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer) {
            var15 = true;
         } else {
            var15 = false;
         }

         if (var7 && (var6 == 1 || var6 == -1)) {
            var17 = true;
         }

         float var4;
         int var10;
         int var11;
         int var12;
         ConstraintWidget var31;
         ConstraintAnchor var33;
         LinearSystem var36;
         if (this.mBaselineDistance > 0) {
            ConstraintAnchor var34 = this.mBottom;
            if (var2 == Integer.MAX_VALUE || this.mBottom.mGroup == var2 && this.mBaseline.mGroup == var2) {
               var1.addEquality(var24, var21, this.getBaselineDistance(), 5);
            }

            ConstraintAnchor var35;
            if (this.mBaseline.mTarget != null) {
               var6 = this.mBaselineDistance;
               var35 = this.mBaseline;
            } else {
               var6 = var5;
               var35 = var34;
            }

            LinearSystem var32;
            if (var2 == Integer.MAX_VALUE || this.mTop.mGroup == var2 && var35.mGroup == var2) {
               if (var17 && this.mTop.mTarget != null && this.mBottom.mTarget != null) {
                  var23 = var1.createObjectVariable(this.mTop);
                  var24 = var1.createObjectVariable(this.mBottom);
                  var25 = var1.createObjectVariable(this.mTop.getTarget());
                  var28 = var1.createObjectVariable(this.mBottom.getTarget());
                  var1.addGreaterThan(var23, var25, this.mTop.getMargin(), 3);
                  var1.addLowerThan(var24, var28, this.mBottom.getMargin() * -1, 3);
                  if (!var16) {
                     var1.addCentering(var23, var25, this.mTop.getMargin(), this.mVerticalBiasPercent, var28, var24, this.mBottom.getMargin(), 4);
                  }

                  var22 = var21;
                  var32 = var1;
               } else {
                  var33 = this.mTop;
                  var9 = this.field_10;
                  var10 = this.mMinHeight;
                  var4 = this.mVerticalBiasPercent;
                  var11 = this.mMatchConstraintDefaultHeight;
                  var12 = this.mMatchConstraintMinHeight;
                  int var13 = this.mMatchConstraintMaxHeight;
                  this.applyConstraints(var1, var15, var14, var33, var35, var9, var9 + var6, var6, var10, var4, var17, var16, var11, var12, var13);
                  var1.addEquality(var20, var21, var5, 5);
                  var22 = var21;
                  var32 = var1;
               }
            } else {
               var23 = var21;
               var32 = var1;
               var22 = var23;
            }

            var24 = var20;
            var31 = this;
            var36 = var32;
            var21 = var24;
         } else {
            var36 = var1;
            if (var2 != Integer.MAX_VALUE && (this.mTop.mGroup != var2 || this.mBottom.mGroup != var2)) {
               var22 = var20;
               var24 = var21;
               var31 = this;
               var21 = var22;
               var22 = var24;
            } else if (var17 && this.mTop.mTarget != null && this.mBottom.mTarget != null) {
               var24 = var1.createObjectVariable(this.mTop);
               var25 = var1.createObjectVariable(this.mBottom);
               var28 = var1.createObjectVariable(this.mTop.getTarget());
               SolverVariable var29 = var1.createObjectVariable(this.mBottom.getTarget());
               var1.addGreaterThan(var24, var28, this.mTop.getMargin(), 3);
               var1.addLowerThan(var25, var29, this.mBottom.getMargin() * -1, 3);
               if (!var16) {
                  var1.addCentering(var24, var28, this.mTop.getMargin(), this.mVerticalBiasPercent, var29, var25, this.mBottom.getMargin(), 4);
               }

               var22 = var20;
               var24 = var21;
               var31 = this;
               var21 = var22;
               var22 = var24;
            } else {
               var33 = this.mTop;
               var37 = this.mBottom;
               var6 = this.field_10;
               var9 = this.mMinHeight;
               var4 = this.mVerticalBiasPercent;
               var10 = this.mMatchConstraintDefaultHeight;
               var11 = this.mMatchConstraintMinHeight;
               var12 = this.mMatchConstraintMaxHeight;
               this.applyConstraints(var1, var15, var14, var33, var37, var6, var6 + var5, var5, var9, var4, var17, var16, var10, var11, var12);
               var22 = var21;
               var21 = var20;
               var31 = this;
            }
         }

         if (var7) {
            ArrayRow var40 = var1.createRow();
            if (var2 == Integer.MAX_VALUE || var31.mLeft.mGroup == var2 && var31.mRight.mGroup == var2) {
               if (var6 == 0) {
                  var36.addConstraint(var40.createRowDimensionRatio(var39, var27, var21, var22, var3));
               } else if (var6 == 1) {
                  var36.addConstraint(var40.createRowDimensionRatio(var21, var22, var39, var27, var3));
               } else {
                  var2 = var31.mMatchConstraintMinWidth;
                  if (var2 > 0) {
                     var36.addGreaterThan(var39, var27, var2, 3);
                  }

                  var2 = var31.mMatchConstraintMinHeight;
                  if (var2 > 0) {
                     var36.addGreaterThan(var21, var22, var2, 3);
                  }

                  var40.createRowDimensionRatio(var39, var27, var21, var22, var3);
                  var20 = var1.createErrorVariable();
                  SolverVariable var30 = var1.createErrorVariable();
                  var20.strength = 4;
                  var30.strength = 4;
                  var40.addError(var20, var30);
                  var36.addConstraint(var40);
               }
            }
         }
      }
   }

   public void connect(ConstraintAnchor.Type var1, ConstraintWidget var2, ConstraintAnchor.Type var3) {
      this.connect(var1, var2, var3, 0, ConstraintAnchor.Strength.STRONG);
   }

   public void connect(ConstraintAnchor.Type var1, ConstraintWidget var2, ConstraintAnchor.Type var3, int var4) {
      this.connect(var1, var2, var3, var4, ConstraintAnchor.Strength.STRONG);
   }

   public void connect(ConstraintAnchor.Type var1, ConstraintWidget var2, ConstraintAnchor.Type var3, int var4, ConstraintAnchor.Strength var5) {
      this.connect(var1, var2, var3, var4, var5, 0);
   }

   public void connect(ConstraintAnchor.Type var1, ConstraintWidget var2, ConstraintAnchor.Type var3, int var4, ConstraintAnchor.Strength var5, int var6) {
      ConstraintAnchor var8;
      ConstraintAnchor var11;
      ConstraintAnchor var13;
      if (var1 == ConstraintAnchor.Type.CENTER) {
         if (var3 == ConstraintAnchor.Type.CENTER) {
            var11 = this.getAnchor(ConstraintAnchor.Type.LEFT);
            var13 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
            var8 = this.getAnchor(ConstraintAnchor.Type.TOP);
            ConstraintAnchor var9 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
            boolean var14 = false;
            boolean var7 = false;
            if ((var11 == null || !var11.isConnected()) && (var13 == null || !var13.isConnected())) {
               this.connect(ConstraintAnchor.Type.LEFT, var2, ConstraintAnchor.Type.LEFT, 0, var5, var6);
               this.connect(ConstraintAnchor.Type.RIGHT, var2, ConstraintAnchor.Type.RIGHT, 0, var5, var6);
               var14 = true;
            }

            if ((var8 == null || !var8.isConnected()) && (var9 == null || !var9.isConnected())) {
               this.connect(ConstraintAnchor.Type.TOP, var2, ConstraintAnchor.Type.TOP, 0, var5, var6);
               this.connect(ConstraintAnchor.Type.BOTTOM, var2, ConstraintAnchor.Type.BOTTOM, 0, var5, var6);
               var7 = true;
            }

            if (var14 && var7) {
               this.getAnchor(ConstraintAnchor.Type.CENTER).connect(var2.getAnchor(ConstraintAnchor.Type.CENTER), 0, var6);
            } else if (var14) {
               this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(var2.getAnchor(ConstraintAnchor.Type.CENTER_X), 0, var6);
            } else if (var7) {
               this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(var2.getAnchor(ConstraintAnchor.Type.CENTER_Y), 0, var6);
            }
         } else if (var3 != ConstraintAnchor.Type.LEFT && var3 != ConstraintAnchor.Type.RIGHT) {
            if (var3 == ConstraintAnchor.Type.TOP || var3 == ConstraintAnchor.Type.BOTTOM) {
               this.connect(ConstraintAnchor.Type.TOP, var2, var3, 0, var5, var6);
               this.connect(ConstraintAnchor.Type.BOTTOM, var2, var3, 0, var5, var6);
               this.getAnchor(ConstraintAnchor.Type.CENTER).connect(var2.getAnchor(var3), 0, var6);
            }
         } else {
            this.connect(ConstraintAnchor.Type.LEFT, var2, var3, 0, var5, var6);
            var1 = ConstraintAnchor.Type.RIGHT;

            try {
               this.connect(var1, var2, var3, 0, var5, var6);
            } catch (Throwable var10) {
               throw var10;
            }

            this.getAnchor(ConstraintAnchor.Type.CENTER).connect(var2.getAnchor(var3), 0, var6);
         }
      } else {
         ConstraintAnchor var12;
         if (var1 == ConstraintAnchor.Type.CENTER_X && (var3 == ConstraintAnchor.Type.LEFT || var3 == ConstraintAnchor.Type.RIGHT)) {
            var11 = this.getAnchor(ConstraintAnchor.Type.LEFT);
            var12 = var2.getAnchor(var3);
            var13 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
            var11.connect(var12, 0, var6);
            var13.connect(var12, 0, var6);
            this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(var12, 0, var6);
         } else if (var1 == ConstraintAnchor.Type.CENTER_Y && (var3 == ConstraintAnchor.Type.TOP || var3 == ConstraintAnchor.Type.BOTTOM)) {
            var11 = var2.getAnchor(var3);
            this.getAnchor(ConstraintAnchor.Type.TOP).connect(var11, 0, var6);
            this.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(var11, 0, var6);
            this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(var11, 0, var6);
         } else if (var1 == ConstraintAnchor.Type.CENTER_X && var3 == ConstraintAnchor.Type.CENTER_X) {
            this.getAnchor(ConstraintAnchor.Type.LEFT).connect(var2.getAnchor(ConstraintAnchor.Type.LEFT), 0, var6);
            this.getAnchor(ConstraintAnchor.Type.RIGHT).connect(var2.getAnchor(ConstraintAnchor.Type.RIGHT), 0, var6);
            this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(var2.getAnchor(var3), 0, var6);
         } else if (var1 == ConstraintAnchor.Type.CENTER_Y && var3 == ConstraintAnchor.Type.CENTER_Y) {
            this.getAnchor(ConstraintAnchor.Type.TOP).connect(var2.getAnchor(ConstraintAnchor.Type.TOP), 0, var6);
            this.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(var2.getAnchor(ConstraintAnchor.Type.BOTTOM), 0, var6);
            this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(var2.getAnchor(var3), 0, var6);
         } else {
            var8 = this.getAnchor(var1);
            var12 = var2.getAnchor(var3);
            if (var8.isValidConnection(var12)) {
               if (var1 == ConstraintAnchor.Type.BASELINE) {
                  var11 = this.getAnchor(ConstraintAnchor.Type.TOP);
                  var13 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
                  if (var11 != null) {
                     var11.reset();
                  }

                  if (var13 != null) {
                     var13.reset();
                  }

                  var4 = 0;
               } else if (var1 != ConstraintAnchor.Type.TOP && var1 != ConstraintAnchor.Type.BOTTOM) {
                  if (var1 == ConstraintAnchor.Type.LEFT || var1 == ConstraintAnchor.Type.RIGHT) {
                     var13 = this.getAnchor(ConstraintAnchor.Type.CENTER);
                     if (var13.getTarget() != var12) {
                        var13.reset();
                     }

                     var11 = this.getAnchor(var1).getOpposite();
                     var13 = this.getAnchor(ConstraintAnchor.Type.CENTER_X);
                     if (var13.isConnected()) {
                        var11.reset();
                        var13.reset();
                     }
                  }
               } else {
                  var13 = this.getAnchor(ConstraintAnchor.Type.BASELINE);
                  if (var13 != null) {
                     var13.reset();
                  }

                  var13 = this.getAnchor(ConstraintAnchor.Type.CENTER);
                  if (var13.getTarget() != var12) {
                     var13.reset();
                  }

                  var11 = this.getAnchor(var1).getOpposite();
                  var13 = this.getAnchor(ConstraintAnchor.Type.CENTER_Y);
                  if (var13.isConnected()) {
                     var11.reset();
                     var13.reset();
                  }
               }

               var8.connect(var12, var4, var5, var6);
               var12.getOwner().connectedTo(var8.getOwner());
               return;
            }
         }
      }

   }

   public void connect(ConstraintAnchor var1, ConstraintAnchor var2, int var3) {
      this.connect(var1, var2, var3, ConstraintAnchor.Strength.STRONG, 0);
   }

   public void connect(ConstraintAnchor var1, ConstraintAnchor var2, int var3, int var4) {
      this.connect(var1, var2, var3, ConstraintAnchor.Strength.STRONG, var4);
   }

   public void connect(ConstraintAnchor var1, ConstraintAnchor var2, int var3, ConstraintAnchor.Strength var4, int var5) {
      if (var1.getOwner() == this) {
         this.connect(var1.getType(), var2.getOwner(), var2.getType(), var3, var4, var5);
      }
   }

   public void connectedTo(ConstraintWidget var1) {
   }

   public void disconnectUnlockedWidget(ConstraintWidget var1) {
      ArrayList var4 = this.getAnchors();
      int var2 = 0;

      for(int var3 = var4.size(); var2 < var3; ++var2) {
         ConstraintAnchor var5 = (ConstraintAnchor)var4.get(var2);
         if (var5.isConnected() && var5.getTarget().getOwner() == var1 && var5.getConnectionCreator() == 2) {
            var5.reset();
         }
      }

   }

   public void disconnectWidget(ConstraintWidget var1) {
      ArrayList var4 = this.getAnchors();
      int var2 = 0;

      for(int var3 = var4.size(); var2 < var3; ++var2) {
         ConstraintAnchor var5 = (ConstraintAnchor)var4.get(var2);
         if (var5.isConnected() && var5.getTarget().getOwner() == var1) {
            var5.reset();
         }
      }

   }

   public void forceUpdateDrawPosition() {
      int var1 = this.field_9;
      int var2 = this.field_10;
      int var3 = this.field_9;
      int var4 = this.mWidth;
      int var5 = this.field_10;
      int var6 = this.mHeight;
      this.mDrawX = var1;
      this.mDrawY = var2;
      this.mDrawWidth = var3 + var4 - var1;
      this.mDrawHeight = var5 + var6 - var2;
   }

   public ConstraintAnchor getAnchor(ConstraintAnchor.Type var1) {
      switch(var1) {
      case LEFT:
         return this.mLeft;
      case TOP:
         return this.mTop;
      case RIGHT:
         return this.mRight;
      case BOTTOM:
         return this.mBottom;
      case BASELINE:
         return this.mBaseline;
      case CENTER_X:
         return this.mCenterX;
      case CENTER_Y:
         return this.mCenterY;
      case CENTER:
         return this.mCenter;
      default:
         return null;
      }
   }

   public ArrayList getAnchors() {
      return this.mAnchors;
   }

   public int getBaselineDistance() {
      return this.mBaselineDistance;
   }

   public int getBottom() {
      return this.getY() + this.mHeight;
   }

   public Object getCompanionWidget() {
      return this.mCompanionWidget;
   }

   public int getContainerItemSkip() {
      return this.mContainerItemSkip;
   }

   public String getDebugName() {
      return this.mDebugName;
   }

   public float getDimensionRatio() {
      return this.mDimensionRatio;
   }

   public int getDimensionRatioSide() {
      return this.mDimensionRatioSide;
   }

   public int getDrawBottom() {
      return this.getDrawY() + this.mDrawHeight;
   }

   public int getDrawHeight() {
      return this.mDrawHeight;
   }

   public int getDrawRight() {
      return this.getDrawX() + this.mDrawWidth;
   }

   public int getDrawWidth() {
      return this.mDrawWidth;
   }

   public int getDrawX() {
      return this.mDrawX + this.mOffsetX;
   }

   public int getDrawY() {
      return this.mDrawY + this.mOffsetY;
   }

   public int getHeight() {
      return this.mVisibility == 8 ? 0 : this.mHeight;
   }

   public float getHorizontalBiasPercent() {
      return this.mHorizontalBiasPercent;
   }

   public ConstraintWidget getHorizontalChainControlWidget() {
      ConstraintWidget var3 = null;
      if (!this.isInHorizontalChain()) {
         return null;
      } else {
         ConstraintWidget var1 = this;

         while(var3 == null && var1 != null) {
            ConstraintAnchor var2 = var1.getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor var4 = null;
            if (var2 == null) {
               var2 = null;
            } else {
               var2 = var2.getTarget();
            }

            ConstraintWidget var5;
            if (var2 == null) {
               var5 = null;
            } else {
               var5 = var2.getOwner();
            }

            if (var5 == this.getParent()) {
               return var1;
            }

            if (var5 != null) {
               var4 = var5.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget();
            }

            if (var4 != null && var4.getOwner() != var1) {
               var3 = var1;
            } else {
               var1 = var5;
            }
         }

         return var3;
      }
   }

   public int getHorizontalChainStyle() {
      return this.mHorizontalChainStyle;
   }

   public ConstraintWidget.DimensionBehaviour getHorizontalDimensionBehaviour() {
      return this.mHorizontalDimensionBehaviour;
   }

   public int getInternalDrawBottom() {
      return this.mDrawY + this.mDrawHeight;
   }

   public int getInternalDrawRight() {
      return this.mDrawX + this.mDrawWidth;
   }

   int getInternalDrawX() {
      return this.mDrawX;
   }

   int getInternalDrawY() {
      return this.mDrawY;
   }

   public int getLeft() {
      return this.getX();
   }

   public int getMinHeight() {
      return this.mMinHeight;
   }

   public int getMinWidth() {
      return this.mMinWidth;
   }

   public int getOptimizerWrapHeight() {
      int var1 = this.mHeight;
      if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
         if (this.mMatchConstraintDefaultHeight == 1) {
            var1 = Math.max(this.mMatchConstraintMinHeight, var1);
         } else if (this.mMatchConstraintMinHeight > 0) {
            var1 = this.mMatchConstraintMinHeight;
            this.mHeight = var1;
         } else {
            var1 = 0;
         }

         int var2 = this.mMatchConstraintMaxHeight;
         return var2 > 0 && var2 < var1 ? this.mMatchConstraintMaxHeight : var1;
      } else {
         return var1;
      }
   }

   public int getOptimizerWrapWidth() {
      int var1 = this.mWidth;
      if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
         if (this.mMatchConstraintDefaultWidth == 1) {
            var1 = Math.max(this.mMatchConstraintMinWidth, var1);
         } else if (this.mMatchConstraintMinWidth > 0) {
            var1 = this.mMatchConstraintMinWidth;
            this.mWidth = var1;
         } else {
            var1 = 0;
         }

         int var2 = this.mMatchConstraintMaxWidth;
         return var2 > 0 && var2 < var1 ? this.mMatchConstraintMaxWidth : var1;
      } else {
         return var1;
      }
   }

   public ConstraintWidget getParent() {
      return this.mParent;
   }

   public int getRight() {
      return this.getX() + this.mWidth;
   }

   public WidgetContainer getRootWidgetContainer() {
      ConstraintWidget var1;
      for(var1 = this; var1.getParent() != null; var1 = var1.getParent()) {
      }

      return var1 instanceof WidgetContainer ? (WidgetContainer)var1 : null;
   }

   protected int getRootX() {
      return this.field_9 + this.mOffsetX;
   }

   protected int getRootY() {
      return this.field_10 + this.mOffsetY;
   }

   public int getTop() {
      return this.getY();
   }

   public String getType() {
      return this.mType;
   }

   public float getVerticalBiasPercent() {
      return this.mVerticalBiasPercent;
   }

   public ConstraintWidget getVerticalChainControlWidget() {
      ConstraintWidget var3 = null;
      if (!this.isInVerticalChain()) {
         return null;
      } else {
         ConstraintWidget var1 = this;

         while(var3 == null && var1 != null) {
            ConstraintAnchor var2 = var1.getAnchor(ConstraintAnchor.Type.TOP);
            ConstraintAnchor var4 = null;
            if (var2 == null) {
               var2 = null;
            } else {
               var2 = var2.getTarget();
            }

            ConstraintWidget var5;
            if (var2 == null) {
               var5 = null;
            } else {
               var5 = var2.getOwner();
            }

            if (var5 == this.getParent()) {
               return var1;
            }

            if (var5 != null) {
               var4 = var5.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget();
            }

            if (var4 != null && var4.getOwner() != var1) {
               var3 = var1;
            } else {
               var1 = var5;
            }
         }

         return var3;
      }
   }

   public int getVerticalChainStyle() {
      return this.mVerticalChainStyle;
   }

   public ConstraintWidget.DimensionBehaviour getVerticalDimensionBehaviour() {
      return this.mVerticalDimensionBehaviour;
   }

   public int getVisibility() {
      return this.mVisibility;
   }

   public int getWidth() {
      return this.mVisibility == 8 ? 0 : this.mWidth;
   }

   public int getWrapHeight() {
      return this.mWrapHeight;
   }

   public int getWrapWidth() {
      return this.mWrapWidth;
   }

   public int getX() {
      return this.field_9;
   }

   public int getY() {
      return this.field_10;
   }

   public boolean hasAncestor(ConstraintWidget var1) {
      ConstraintWidget var2 = this.getParent();
      if (var2 == var1) {
         return true;
      } else if (var2 == var1.getParent()) {
         return false;
      } else {
         while(var2 != null) {
            if (var2 == var1) {
               return true;
            }

            if (var2 == var1.getParent()) {
               return true;
            }

            var2 = var2.getParent();
         }

         return false;
      }
   }

   public boolean hasBaseline() {
      return this.mBaselineDistance > 0;
   }

   public void immediateConnect(ConstraintAnchor.Type var1, ConstraintWidget var2, ConstraintAnchor.Type var3, int var4, int var5) {
      this.getAnchor(var1).connect(var2.getAnchor(var3), var4, var5, ConstraintAnchor.Strength.STRONG, 0, true);
   }

   public boolean isInHorizontalChain() {
      return this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft || this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight;
   }

   public boolean isInVerticalChain() {
      return this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop || this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom;
   }

   public boolean isInsideConstraintLayout() {
      ConstraintWidget var1 = this.getParent();
      if (var1 == null) {
         return false;
      } else {
         while(var1 != null) {
            if (var1 instanceof ConstraintWidgetContainer) {
               return true;
            }

            var1 = var1.getParent();
         }

         return false;
      }
   }

   public boolean isRoot() {
      return this.mParent == null;
   }

   public boolean isRootContainer() {
      if (this instanceof ConstraintWidgetContainer) {
         ConstraintWidget var1 = this.mParent;
         if (var1 == null || !(var1 instanceof ConstraintWidgetContainer)) {
            return true;
         }
      }

      return false;
   }

   public void reset() {
      this.mLeft.reset();
      this.mTop.reset();
      this.mRight.reset();
      this.mBottom.reset();
      this.mBaseline.reset();
      this.mCenterX.reset();
      this.mCenterY.reset();
      this.mCenter.reset();
      this.mParent = null;
      this.mWidth = 0;
      this.mHeight = 0;
      this.mDimensionRatio = 0.0F;
      this.mDimensionRatioSide = -1;
      this.field_9 = 0;
      this.field_10 = 0;
      this.mDrawX = 0;
      this.mDrawY = 0;
      this.mDrawWidth = 0;
      this.mDrawHeight = 0;
      this.mOffsetX = 0;
      this.mOffsetY = 0;
      this.mBaselineDistance = 0;
      this.mMinWidth = 0;
      this.mMinHeight = 0;
      this.mWrapWidth = 0;
      this.mWrapHeight = 0;
      float var1 = DEFAULT_BIAS;
      this.mHorizontalBiasPercent = var1;
      this.mVerticalBiasPercent = var1;
      this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
      this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
      this.mCompanionWidget = null;
      this.mContainerItemSkip = 0;
      this.mVisibility = 0;
      this.mDebugName = null;
      this.mType = null;
      this.mHorizontalWrapVisited = false;
      this.mVerticalWrapVisited = false;
      this.mHorizontalChainStyle = 0;
      this.mVerticalChainStyle = 0;
      this.mHorizontalChainFixedPosition = false;
      this.mVerticalChainFixedPosition = false;
      this.mHorizontalWeight = 0.0F;
      this.mVerticalWeight = 0.0F;
      this.mHorizontalResolution = -1;
      this.mVerticalResolution = -1;
   }

   public void resetAllConstraints() {
      this.resetAnchors();
      this.setVerticalBiasPercent(DEFAULT_BIAS);
      this.setHorizontalBiasPercent(DEFAULT_BIAS);
      if (!(this instanceof ConstraintWidgetContainer)) {
         if (this.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.getWidth() == this.getWrapWidth()) {
               this.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
            } else if (this.getWidth() > this.getMinWidth()) {
               this.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            }
         }

         if (this.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.getHeight() == this.getWrapHeight()) {
               this.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
            } else if (this.getHeight() > this.getMinHeight()) {
               this.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            }
         }
      }
   }

   public void resetAnchor(ConstraintAnchor var1) {
      if (this.getParent() == null || !(this.getParent() instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
         ConstraintAnchor var2 = this.getAnchor(ConstraintAnchor.Type.LEFT);
         ConstraintAnchor var3 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
         ConstraintAnchor var4 = this.getAnchor(ConstraintAnchor.Type.TOP);
         ConstraintAnchor var5 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
         ConstraintAnchor var6 = this.getAnchor(ConstraintAnchor.Type.CENTER);
         ConstraintAnchor var7 = this.getAnchor(ConstraintAnchor.Type.CENTER_X);
         ConstraintAnchor var8 = this.getAnchor(ConstraintAnchor.Type.CENTER_Y);
         if (var1 == var6) {
            if (var2.isConnected() && var3.isConnected() && var2.getTarget() == var3.getTarget()) {
               var2.reset();
               var3.reset();
            }

            if (var4.isConnected() && var5.isConnected() && var4.getTarget() == var5.getTarget()) {
               var4.reset();
               var5.reset();
            }

            this.mHorizontalBiasPercent = 0.5F;
            this.mVerticalBiasPercent = 0.5F;
         } else if (var1 == var7) {
            if (var2.isConnected() && var3.isConnected() && var2.getTarget().getOwner() == var3.getTarget().getOwner()) {
               var2.reset();
               var3.reset();
            }

            this.mHorizontalBiasPercent = 0.5F;
         } else if (var1 == var8) {
            if (var4.isConnected() && var5.isConnected() && var4.getTarget().getOwner() == var5.getTarget().getOwner()) {
               var4.reset();
               var5.reset();
            }

            this.mVerticalBiasPercent = 0.5F;
         } else if (var1 != var2 && var1 != var3) {
            if ((var1 == var4 || var1 == var5) && var4.isConnected() && var4.getTarget() == var5.getTarget()) {
               var6.reset();
            }
         } else if (var2.isConnected() && var2.getTarget() == var3.getTarget()) {
            var6.reset();
         }

         var1.reset();
      }
   }

   public void resetAnchors() {
      ConstraintWidget var3 = this.getParent();
      if (var3 == null || !(var3 instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
         int var1 = 0;

         for(int var2 = this.mAnchors.size(); var1 < var2; ++var1) {
            ((ConstraintAnchor)this.mAnchors.get(var1)).reset();
         }

      }
   }

   public void resetAnchors(int var1) {
      ConstraintWidget var4 = this.getParent();
      if (var4 == null || !(var4 instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
         int var2 = 0;

         for(int var3 = this.mAnchors.size(); var2 < var3; ++var2) {
            ConstraintAnchor var5 = (ConstraintAnchor)this.mAnchors.get(var2);
            if (var1 == var5.getConnectionCreator()) {
               if (var5.isVerticalAnchor()) {
                  this.setVerticalBiasPercent(DEFAULT_BIAS);
               } else {
                  this.setHorizontalBiasPercent(DEFAULT_BIAS);
               }

               var5.reset();
            }
         }

      }
   }

   public void resetGroups() {
      int var2 = this.mAnchors.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         ((ConstraintAnchor)this.mAnchors.get(var1)).mGroup = Integer.MAX_VALUE;
      }

   }

   public void resetSolverVariables(Cache var1) {
      this.mLeft.resetSolverVariable(var1);
      this.mTop.resetSolverVariable(var1);
      this.mRight.resetSolverVariable(var1);
      this.mBottom.resetSolverVariable(var1);
      this.mBaseline.resetSolverVariable(var1);
      this.mCenter.resetSolverVariable(var1);
      this.mCenterX.resetSolverVariable(var1);
      this.mCenterY.resetSolverVariable(var1);
   }

   public void setBaselineDistance(int var1) {
      this.mBaselineDistance = var1;
   }

   public void setCompanionWidget(Object var1) {
      this.mCompanionWidget = var1;
   }

   public void setContainerItemSkip(int var1) {
      if (var1 >= 0) {
         this.mContainerItemSkip = var1;
      } else {
         this.mContainerItemSkip = 0;
      }
   }

   public void setDebugName(String var1) {
      this.mDebugName = var1;
   }

   public void setDebugSolverName(LinearSystem var1, String var2) {
      this.mDebugName = var2;
      SolverVariable var6 = var1.createObjectVariable(this.mLeft);
      SolverVariable var5 = var1.createObjectVariable(this.mTop);
      SolverVariable var4 = var1.createObjectVariable(this.mRight);
      SolverVariable var3 = var1.createObjectVariable(this.mBottom);
      StringBuilder var7 = new StringBuilder();
      var7.append(var2);
      var7.append(".left");
      var6.setName(var7.toString());
      StringBuilder var12 = new StringBuilder();
      var12.append(var2);
      var12.append(".top");
      var5.setName(var12.toString());
      StringBuilder var11 = new StringBuilder();
      var11.append(var2);
      var11.append(".right");
      var4.setName(var11.toString());
      StringBuilder var10 = new StringBuilder();
      var10.append(var2);
      var10.append(".bottom");
      var3.setName(var10.toString());
      if (this.mBaselineDistance > 0) {
         SolverVariable var8 = var1.createObjectVariable(this.mBaseline);
         StringBuilder var9 = new StringBuilder();
         var9.append(var2);
         var9.append(".baseline");
         var8.setName(var9.toString());
      }
   }

   public void setDimension(int var1, int var2) {
      this.mWidth = var1;
      var1 = this.mWidth;
      int var3 = this.mMinWidth;
      if (var1 < var3) {
         this.mWidth = var3;
      }

      this.mHeight = var2;
      var1 = this.mHeight;
      var2 = this.mMinHeight;
      if (var1 < var2) {
         this.mHeight = var2;
      }
   }

   public void setDimensionRatio(float var1, int var2) {
      this.mDimensionRatio = var1;
      this.mDimensionRatioSide = var2;
   }

   public void setDimensionRatio(String var1) {
      if (var1 != null && var1.length() != 0) {
         byte var6 = -1;
         float var2 = 0.0F;
         float var4 = 0.0F;
         float var3 = 0.0F;
         int var8 = var1.length();
         int var7 = var1.indexOf(44);
         String var10;
         if (var7 > 0 && var7 < var8 - 1) {
            var10 = var1.substring(0, var7);
            if (var10.equalsIgnoreCase("W")) {
               var6 = 0;
            } else if (var10.equalsIgnoreCase("H")) {
               var6 = 1;
            }

            ++var7;
         } else {
            var7 = 0;
         }

         int var9 = var1.indexOf(58);
         if (var9 >= 0 && var9 < var8 - 1) {
            var10 = var1.substring(var7, var9);
            var1 = var1.substring(var9 + 1);
            if (var10.length() > 0 && var1.length() > 0) {
               label92: {
                  boolean var10001;
                  float var5;
                  try {
                     var4 = Float.parseFloat(var10);
                     var5 = Float.parseFloat(var1);
                  } catch (NumberFormatException var14) {
                     var10001 = false;
                     break label92;
                  }

                  if (var4 > 0.0F && var5 > 0.0F) {
                     if (var6 == 1) {
                        label58: {
                           try {
                              var3 = Math.abs(var5 / var4);
                           } catch (NumberFormatException var12) {
                              var10001 = false;
                              break label58;
                           }

                           var2 = var3;
                        }
                     } else {
                        label61: {
                           try {
                              var3 = Math.abs(var4 / var5);
                           } catch (NumberFormatException var13) {
                              var10001 = false;
                              break label61;
                           }

                           var2 = var3;
                        }
                     }
                  } else {
                     var2 = var3;
                  }
               }
            }
         } else {
            var1 = var1.substring(var7);
            if (var1.length() > 0) {
               try {
                  var2 = Float.parseFloat(var1);
               } catch (NumberFormatException var11) {
                  var2 = var4;
               }
            } else {
               var2 = var4;
            }
         }

         if (var2 > 0.0F) {
            this.mDimensionRatio = var2;
            this.mDimensionRatioSide = var6;
         }
      } else {
         this.mDimensionRatio = 0.0F;
      }
   }

   public void setDrawHeight(int var1) {
      this.mDrawHeight = var1;
   }

   public void setDrawOrigin(int var1, int var2) {
      this.mDrawX = var1 - this.mOffsetX;
      this.mDrawY = var2 - this.mOffsetY;
      this.field_9 = this.mDrawX;
      this.field_10 = this.mDrawY;
   }

   public void setDrawWidth(int var1) {
      this.mDrawWidth = var1;
   }

   public void setDrawX(int var1) {
      this.mDrawX = var1 - this.mOffsetX;
      this.field_9 = this.mDrawX;
   }

   public void setDrawY(int var1) {
      this.mDrawY = var1 - this.mOffsetY;
      this.field_10 = this.mDrawY;
   }

   public void setFrame(int var1, int var2, int var3, int var4) {
      int var5 = var3 - var1;
      var3 = var4 - var2;
      this.field_9 = var1;
      this.field_10 = var2;
      if (this.mVisibility == 8) {
         this.mWidth = 0;
         this.mHeight = 0;
      } else {
         if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED && var5 < this.mWidth) {
            var1 = this.mWidth;
         } else {
            var1 = var5;
         }

         if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED && var3 < this.mHeight) {
            var2 = this.mHeight;
         } else {
            var2 = var3;
         }

         this.mWidth = var1;
         this.mHeight = var2;
         var1 = this.mHeight;
         var2 = this.mMinHeight;
         if (var1 < var2) {
            this.mHeight = var2;
         }

         var1 = this.mWidth;
         var2 = this.mMinWidth;
         if (var1 < var2) {
            this.mWidth = var2;
         }
      }
   }

   public void setGoneMargin(ConstraintAnchor.Type var1, int var2) {
      switch(var1) {
      case LEFT:
         this.mLeft.mGoneMargin = var2;
         return;
      case TOP:
         this.mTop.mGoneMargin = var2;
         return;
      case RIGHT:
         this.mRight.mGoneMargin = var2;
         return;
      case BOTTOM:
         this.mBottom.mGoneMargin = var2;
         return;
      default:
      }
   }

   public void setHeight(int var1) {
      this.mHeight = var1;
      var1 = this.mHeight;
      int var2 = this.mMinHeight;
      if (var1 < var2) {
         this.mHeight = var2;
      }
   }

   public void setHorizontalBiasPercent(float var1) {
      this.mHorizontalBiasPercent = var1;
   }

   public void setHorizontalChainStyle(int var1) {
      this.mHorizontalChainStyle = var1;
   }

   public void setHorizontalDimension(int var1, int var2) {
      this.field_9 = var1;
      this.mWidth = var2 - var1;
      var1 = this.mWidth;
      var2 = this.mMinWidth;
      if (var1 < var2) {
         this.mWidth = var2;
      }
   }

   public void setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour var1) {
      this.mHorizontalDimensionBehaviour = var1;
      if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
         this.setWidth(this.mWrapWidth);
      }
   }

   public void setHorizontalMatchStyle(int var1, int var2, int var3) {
      this.mMatchConstraintDefaultWidth = var1;
      this.mMatchConstraintMinWidth = var2;
      this.mMatchConstraintMaxWidth = var3;
   }

   public void setHorizontalWeight(float var1) {
      this.mHorizontalWeight = var1;
   }

   public void setMinHeight(int var1) {
      if (var1 < 0) {
         this.mMinHeight = 0;
      } else {
         this.mMinHeight = var1;
      }
   }

   public void setMinWidth(int var1) {
      if (var1 < 0) {
         this.mMinWidth = 0;
      } else {
         this.mMinWidth = var1;
      }
   }

   public void setOffset(int var1, int var2) {
      this.mOffsetX = var1;
      this.mOffsetY = var2;
   }

   public void setOrigin(int var1, int var2) {
      this.field_9 = var1;
      this.field_10 = var2;
   }

   public void setParent(ConstraintWidget var1) {
      this.mParent = var1;
   }

   public void setType(String var1) {
      this.mType = var1;
   }

   public void setVerticalBiasPercent(float var1) {
      this.mVerticalBiasPercent = var1;
   }

   public void setVerticalChainStyle(int var1) {
      this.mVerticalChainStyle = var1;
   }

   public void setVerticalDimension(int var1, int var2) {
      this.field_10 = var1;
      this.mHeight = var2 - var1;
      var1 = this.mHeight;
      var2 = this.mMinHeight;
      if (var1 < var2) {
         this.mHeight = var2;
      }
   }

   public void setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour var1) {
      this.mVerticalDimensionBehaviour = var1;
      if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
         this.setHeight(this.mWrapHeight);
      }
   }

   public void setVerticalMatchStyle(int var1, int var2, int var3) {
      this.mMatchConstraintDefaultHeight = var1;
      this.mMatchConstraintMinHeight = var2;
      this.mMatchConstraintMaxHeight = var3;
   }

   public void setVerticalWeight(float var1) {
      this.mVerticalWeight = var1;
   }

   public void setVisibility(int var1) {
      this.mVisibility = var1;
   }

   public void setWidth(int var1) {
      this.mWidth = var1;
      var1 = this.mWidth;
      int var2 = this.mMinWidth;
      if (var1 < var2) {
         this.mWidth = var2;
      }
   }

   public void setWrapHeight(int var1) {
      this.mWrapHeight = var1;
   }

   public void setWrapWidth(int var1) {
      this.mWrapWidth = var1;
   }

   public void setX(int var1) {
      this.field_9 = var1;
   }

   public void setY(int var1) {
      this.field_10 = var1;
   }

   public String toString() {
      StringBuilder var2 = new StringBuilder();
      StringBuilder var1;
      String var3;
      if (this.mType != null) {
         var1 = new StringBuilder();
         var1.append("type: ");
         var1.append(this.mType);
         var1.append(" ");
         var3 = var1.toString();
      } else {
         var3 = "";
      }

      var2.append(var3);
      if (this.mDebugName != null) {
         var1 = new StringBuilder();
         var1.append("id: ");
         var1.append(this.mDebugName);
         var1.append(" ");
         var3 = var1.toString();
      } else {
         var3 = "";
      }

      var2.append(var3);
      var2.append("(");
      var2.append(this.field_9);
      var2.append(", ");
      var2.append(this.field_10);
      var2.append(") - (");
      var2.append(this.mWidth);
      var2.append(" x ");
      var2.append(this.mHeight);
      var2.append(")");
      var2.append(" wrap: (");
      var2.append(this.mWrapWidth);
      var2.append(" x ");
      var2.append(this.mWrapHeight);
      var2.append(")");
      return var2.toString();
   }

   public void updateDrawPosition() {
      int var1 = this.field_9;
      int var2 = this.field_10;
      int var3 = this.field_9;
      int var4 = this.mWidth;
      int var5 = this.field_10;
      int var6 = this.mHeight;
      this.mDrawX = var1;
      this.mDrawY = var2;
      this.mDrawWidth = var3 + var4 - var1;
      this.mDrawHeight = var5 + var6 - var2;
   }

   public void updateFromSolver(LinearSystem var1) {
      this.updateFromSolver(var1, Integer.MAX_VALUE);
   }

   public void updateFromSolver(LinearSystem var1, int var2) {
      if (var2 == Integer.MAX_VALUE) {
         this.setFrame(var1.getObjectVariableValue(this.mLeft), var1.getObjectVariableValue(this.mTop), var1.getObjectVariableValue(this.mRight), var1.getObjectVariableValue(this.mBottom));
      } else if (var2 == -2) {
         this.setFrame(this.mSolverLeft, this.mSolverTop, this.mSolverRight, this.mSolverBottom);
      } else {
         if (this.mLeft.mGroup == var2) {
            this.mSolverLeft = var1.getObjectVariableValue(this.mLeft);
         }

         if (this.mTop.mGroup == var2) {
            this.mSolverTop = var1.getObjectVariableValue(this.mTop);
         }

         if (this.mRight.mGroup == var2) {
            this.mSolverRight = var1.getObjectVariableValue(this.mRight);
         }

         if (this.mBottom.mGroup == var2) {
            this.mSolverBottom = var1.getObjectVariableValue(this.mBottom);
         }
      }
   }

   public static enum ContentAlignment {
      BEGIN,
      BOTTOM,
      END,
      LEFT,
      MIDDLE,
      RIGHT,
      TOP,
      VERTICAL_MIDDLE;
   }

   public static enum DimensionBehaviour {
      FIXED,
      MATCH_CONSTRAINT,
      MATCH_PARENT,
      WRAP_CONTENT;
   }
}
