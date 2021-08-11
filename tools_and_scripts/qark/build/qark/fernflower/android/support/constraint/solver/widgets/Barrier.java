package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;

public class Barrier extends Helper {
   public static final int BOTTOM = 3;
   public static final int LEFT = 0;
   public static final int RIGHT = 1;
   public static final int TOP = 2;
   private boolean mAllowsGoneWidget = true;
   private int mBarrierType = 0;
   private ArrayList mNodes = new ArrayList(4);

   public void addToSolver(LinearSystem var1) {
      this.mListAnchors[0] = this.mLeft;
      this.mListAnchors[2] = this.mTop;
      this.mListAnchors[1] = this.mRight;
      this.mListAnchors[3] = this.mBottom;

      int var2;
      for(var2 = 0; var2 < this.mListAnchors.length; ++var2) {
         this.mListAnchors[var2].mSolverVariable = var1.createObjectVariable(this.mListAnchors[var2]);
      }

      var2 = this.mBarrierType;
      if (var2 >= 0 && var2 < 4) {
         ConstraintAnchor var6 = this.mListAnchors[this.mBarrierType];
         boolean var5 = false;
         var2 = 0;

         int var3;
         boolean var4;
         while(true) {
            var4 = var5;
            if (var2 >= this.mWidgetsCount) {
               break;
            }

            ConstraintWidget var7 = this.mWidgets[var2];
            if (this.mAllowsGoneWidget || var7.allowedInBarrier()) {
               var3 = this.mBarrierType;
               if ((var3 == 0 || var3 == 1) && var7.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                  var4 = true;
                  break;
               }

               var3 = this.mBarrierType;
               if ((var3 == 2 || var3 == 3) && var7.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                  var4 = true;
                  break;
               }
            }

            ++var2;
         }

         var2 = this.mBarrierType;
         if (var2 != 0 && var2 != 1) {
            if (this.getParent().getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var4 = false;
            }
         } else if (this.getParent().getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            var4 = false;
         }

         for(var2 = 0; var2 < this.mWidgetsCount; ++var2) {
            ConstraintWidget var8 = this.mWidgets[var2];
            if (this.mAllowsGoneWidget || var8.allowedInBarrier()) {
               SolverVariable var9 = var1.createObjectVariable(var8.mListAnchors[this.mBarrierType]);
               ConstraintAnchor[] var10 = var8.mListAnchors;
               var3 = this.mBarrierType;
               var10[var3].mSolverVariable = var9;
               if (var3 != 0 && var3 != 2) {
                  var1.addGreaterBarrier(var6.mSolverVariable, var9, var4);
               } else {
                  var1.addLowerBarrier(var6.mSolverVariable, var9, var4);
               }
            }
         }

         var2 = this.mBarrierType;
         if (var2 == 0) {
            var1.addEquality(this.mRight.mSolverVariable, this.mLeft.mSolverVariable, 0, 6);
            if (!var4) {
               var1.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, 5);
            }
         } else if (var2 == 1) {
            var1.addEquality(this.mLeft.mSolverVariable, this.mRight.mSolverVariable, 0, 6);
            if (!var4) {
               var1.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, 5);
            }
         } else if (var2 == 2) {
            var1.addEquality(this.mBottom.mSolverVariable, this.mTop.mSolverVariable, 0, 6);
            if (!var4) {
               var1.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, 5);
            }
         } else if (var2 == 3) {
            var1.addEquality(this.mTop.mSolverVariable, this.mBottom.mSolverVariable, 0, 6);
            if (!var4) {
               var1.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, 5);
            }
         }
      }
   }

   public boolean allowedInBarrier() {
      return true;
   }

   public boolean allowsGoneWidget() {
      return this.mAllowsGoneWidget;
   }

   public void analyze(int var1) {
      if (this.mParent != null) {
         if (((ConstraintWidgetContainer)this.mParent).optimizeFor(2)) {
            ResolutionAnchor var3;
            switch(this.mBarrierType) {
            case 0:
               var3 = this.mLeft.getResolutionNode();
               break;
            case 1:
               var3 = this.mRight.getResolutionNode();
               break;
            case 2:
               var3 = this.mTop.getResolutionNode();
               break;
            case 3:
               var3 = this.mBottom.getResolutionNode();
               break;
            default:
               return;
            }

            var3.setType(5);
            var1 = this.mBarrierType;
            if (var1 != 0 && var1 != 1) {
               this.mLeft.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
               this.mRight.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
            } else {
               this.mTop.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
               this.mBottom.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
            }

            this.mNodes.clear();

            for(var1 = 0; var1 < this.mWidgetsCount; ++var1) {
               ConstraintWidget var4 = this.mWidgets[var1];
               if (this.mAllowsGoneWidget || var4.allowedInBarrier()) {
                  ResolutionAnchor var2 = null;
                  switch(this.mBarrierType) {
                  case 0:
                     var2 = var4.mLeft.getResolutionNode();
                     break;
                  case 1:
                     var2 = var4.mRight.getResolutionNode();
                     break;
                  case 2:
                     var2 = var4.mTop.getResolutionNode();
                     break;
                  case 3:
                     var2 = var4.mBottom.getResolutionNode();
                  }

                  if (var2 != null) {
                     this.mNodes.add(var2);
                     var2.addDependent(var3);
                  }
               }
            }

         }
      }
   }

   public void resetResolutionNodes() {
      super.resetResolutionNodes();
      this.mNodes.clear();
   }

   public void resolve() {
      float var1 = 0.0F;
      ResolutionAnchor var5;
      switch(this.mBarrierType) {
      case 0:
         var5 = this.mLeft.getResolutionNode();
         var1 = Float.MAX_VALUE;
         break;
      case 1:
         var5 = this.mRight.getResolutionNode();
         break;
      case 2:
         var5 = this.mTop.getResolutionNode();
         var1 = Float.MAX_VALUE;
         break;
      case 3:
         var5 = this.mBottom.getResolutionNode();
         break;
      default:
         return;
      }

      int var3 = this.mNodes.size();
      ResolutionAnchor var6 = null;

      for(int var2 = 0; var2 < var3; ++var2) {
         ResolutionAnchor var7 = (ResolutionAnchor)this.mNodes.get(var2);
         if (var7.state != 1) {
            return;
         }

         int var4 = this.mBarrierType;
         if (var4 != 0 && var4 != 2) {
            if (var7.resolvedOffset > var1) {
               var1 = var7.resolvedOffset;
               var6 = var7.resolvedTarget;
            }
         } else if (var7.resolvedOffset < var1) {
            var1 = var7.resolvedOffset;
            var6 = var7.resolvedTarget;
         }
      }

      if (LinearSystem.getMetrics() != null) {
         Metrics var8 = LinearSystem.getMetrics();
         ++var8.barrierConnectionResolved;
      }

      var5.resolvedTarget = var6;
      var5.resolvedOffset = var1;
      var5.didResolve();
      switch(this.mBarrierType) {
      case 0:
         this.mRight.getResolutionNode().resolve(var6, var1);
         return;
      case 1:
         this.mLeft.getResolutionNode().resolve(var6, var1);
         return;
      case 2:
         this.mBottom.getResolutionNode().resolve(var6, var1);
         return;
      case 3:
         this.mTop.getResolutionNode().resolve(var6, var1);
         return;
      default:
      }
   }

   public void setAllowsGoneWidget(boolean var1) {
      this.mAllowsGoneWidget = var1;
   }

   public void setBarrierType(int var1) {
      this.mBarrierType = var1;
   }
}
