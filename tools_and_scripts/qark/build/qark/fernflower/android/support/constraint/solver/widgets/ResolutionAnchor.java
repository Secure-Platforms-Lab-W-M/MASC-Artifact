package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.SolverVariable;

public class ResolutionAnchor extends ResolutionNode {
   public static final int BARRIER_CONNECTION = 5;
   public static final int CENTER_CONNECTION = 2;
   public static final int CHAIN_CONNECTION = 4;
   public static final int DIRECT_CONNECTION = 1;
   public static final int MATCH_CONNECTION = 3;
   public static final int UNCONNECTED = 0;
   float computedValue;
   private ResolutionDimension dimension = null;
   private int dimensionMultiplier = 1;
   ConstraintAnchor myAnchor;
   float offset;
   private ResolutionAnchor opposite;
   private ResolutionDimension oppositeDimension = null;
   private int oppositeDimensionMultiplier = 1;
   private float oppositeOffset;
   float resolvedOffset;
   ResolutionAnchor resolvedTarget;
   ResolutionAnchor target;
   int type = 0;

   public ResolutionAnchor(ConstraintAnchor var1) {
      this.myAnchor = var1;
   }

   void addResolvedValue(LinearSystem var1) {
      SolverVariable var2 = this.myAnchor.getSolverVariable();
      ResolutionAnchor var3 = this.resolvedTarget;
      if (var3 == null) {
         var1.addEquality(var2, (int)(this.resolvedOffset + 0.5F));
      } else {
         var1.addEquality(var2, var1.createObjectVariable(var3.myAnchor), (int)(this.resolvedOffset + 0.5F), 6);
      }
   }

   public void dependsOn(int var1, ResolutionAnchor var2, int var3) {
      this.type = var1;
      this.target = var2;
      this.offset = (float)var3;
      this.target.addDependent(this);
   }

   public void dependsOn(ResolutionAnchor var1, int var2) {
      this.target = var1;
      this.offset = (float)var2;
      this.target.addDependent(this);
   }

   public void dependsOn(ResolutionAnchor var1, int var2, ResolutionDimension var3) {
      this.target = var1;
      this.target.addDependent(this);
      this.dimension = var3;
      this.dimensionMultiplier = var2;
      this.dimension.addDependent(this);
   }

   public float getResolvedValue() {
      return this.resolvedOffset;
   }

   public void remove(ResolutionDimension var1) {
      ResolutionDimension var2 = this.dimension;
      if (var2 == var1) {
         this.dimension = null;
         this.offset = (float)this.dimensionMultiplier;
      } else if (var2 == this.oppositeDimension) {
         this.oppositeDimension = null;
         this.oppositeOffset = (float)this.oppositeDimensionMultiplier;
      }

      this.resolve();
   }

   public void reset() {
      super.reset();
      this.target = null;
      this.offset = 0.0F;
      this.dimension = null;
      this.dimensionMultiplier = 1;
      this.oppositeDimension = null;
      this.oppositeDimensionMultiplier = 1;
      this.resolvedTarget = null;
      this.resolvedOffset = 0.0F;
      this.computedValue = 0.0F;
      this.opposite = null;
      this.oppositeOffset = 0.0F;
      this.type = 0;
   }

   public void resolve() {
      int var3 = this.state;
      boolean var4 = true;
      if (var3 != 1) {
         if (this.type != 4) {
            ResolutionDimension var6 = this.dimension;
            if (var6 != null) {
               if (var6.state != 1) {
                  return;
               }

               this.offset = (float)this.dimensionMultiplier * this.dimension.value;
            }

            var6 = this.oppositeDimension;
            if (var6 != null) {
               if (var6.state != 1) {
                  return;
               }

               this.oppositeOffset = (float)this.oppositeDimensionMultiplier * this.oppositeDimension.value;
            }

            ResolutionAnchor var11;
            if (this.type == 1) {
               var11 = this.target;
               if (var11 == null || var11.state == 1) {
                  var11 = this.target;
                  if (var11 == null) {
                     this.resolvedTarget = this;
                     this.resolvedOffset = this.offset;
                  } else {
                     this.resolvedTarget = var11.resolvedTarget;
                     this.resolvedOffset = var11.resolvedOffset + this.offset;
                  }

                  this.didResolve();
                  return;
               }
            }

            Metrics var12;
            if (this.type == 2) {
               var11 = this.target;
               if (var11 != null && var11.state == 1) {
                  var11 = this.opposite;
                  if (var11 != null) {
                     var11 = var11.target;
                     if (var11 != null && var11.state == 1) {
                        if (LinearSystem.getMetrics() != null) {
                           var12 = LinearSystem.getMetrics();
                           ++var12.centerConnectionResolved;
                        }

                        this.resolvedTarget = this.target.resolvedTarget;
                        var11 = this.opposite;
                        var11.resolvedTarget = var11.target.resolvedTarget;
                        boolean var9 = var4;
                        if (this.myAnchor.mType != ConstraintAnchor.Type.RIGHT) {
                           if (this.myAnchor.mType == ConstraintAnchor.Type.BOTTOM) {
                              var9 = var4;
                           } else {
                              var9 = false;
                           }
                        }

                        float var1;
                        if (var9) {
                           var1 = this.target.resolvedOffset - this.opposite.target.resolvedOffset;
                        } else {
                           var1 = this.opposite.target.resolvedOffset - this.target.resolvedOffset;
                        }

                        float var2;
                        if (this.myAnchor.mType != ConstraintAnchor.Type.LEFT && this.myAnchor.mType != ConstraintAnchor.Type.RIGHT) {
                           var2 = var1 - (float)this.myAnchor.mOwner.getHeight();
                           var1 = this.myAnchor.mOwner.mVerticalBiasPercent;
                        } else {
                           var2 = var1 - (float)this.myAnchor.mOwner.getWidth();
                           var1 = this.myAnchor.mOwner.mHorizontalBiasPercent;
                        }

                        int var10 = this.myAnchor.getMargin();
                        int var5 = this.opposite.myAnchor.getMargin();
                        if (this.myAnchor.getTarget() == this.opposite.myAnchor.getTarget()) {
                           var1 = 0.5F;
                           var10 = 0;
                           var5 = 0;
                        }

                        var2 = var2 - (float)var10 - (float)var5;
                        if (var9) {
                           var11 = this.opposite;
                           var11.resolvedOffset = var11.target.resolvedOffset + (float)var5 + var2 * var1;
                           this.resolvedOffset = this.target.resolvedOffset - (float)var10 - (1.0F - var1) * var2;
                        } else {
                           this.resolvedOffset = this.target.resolvedOffset + (float)var10 + var2 * var1;
                           var11 = this.opposite;
                           var11.resolvedOffset = var11.target.resolvedOffset - (float)var5 - (1.0F - var1) * var2;
                        }

                        this.didResolve();
                        this.opposite.didResolve();
                        return;
                     }
                  }
               }
            }

            if (this.type == 3) {
               var11 = this.target;
               if (var11 != null && var11.state == 1) {
                  var11 = this.opposite;
                  if (var11 != null) {
                     var11 = var11.target;
                     if (var11 != null && var11.state == 1) {
                        if (LinearSystem.getMetrics() != null) {
                           var12 = LinearSystem.getMetrics();
                           ++var12.matchConnectionResolved;
                        }

                        var11 = this.target;
                        this.resolvedTarget = var11.resolvedTarget;
                        ResolutionAnchor var7 = this.opposite;
                        ResolutionAnchor var8 = var7.target;
                        var7.resolvedTarget = var8.resolvedTarget;
                        this.resolvedOffset = var11.resolvedOffset + this.offset;
                        var7.resolvedOffset = var8.resolvedOffset + var7.offset;
                        this.didResolve();
                        this.opposite.didResolve();
                        return;
                     }
                  }
               }
            }

            if (this.type == 5) {
               this.myAnchor.mOwner.resolve();
            }
         }
      }
   }

   public void resolve(ResolutionAnchor var1, float var2) {
      if (this.state == 0 || this.resolvedTarget != var1 && this.resolvedOffset != var2) {
         this.resolvedTarget = var1;
         this.resolvedOffset = var2;
         if (this.state == 1) {
            this.invalidate();
         }

         this.didResolve();
      }
   }

   String sType(int var1) {
      if (var1 == 1) {
         return "DIRECT";
      } else if (var1 == 2) {
         return "CENTER";
      } else if (var1 == 3) {
         return "MATCH";
      } else if (var1 == 4) {
         return "CHAIN";
      } else {
         return var1 == 5 ? "BARRIER" : "UNCONNECTED";
      }
   }

   public void setOpposite(ResolutionAnchor var1, float var2) {
      this.opposite = var1;
      this.oppositeOffset = var2;
   }

   public void setOpposite(ResolutionAnchor var1, int var2, ResolutionDimension var3) {
      this.opposite = var1;
      this.oppositeDimension = var3;
      this.oppositeDimensionMultiplier = var2;
   }

   public void setType(int var1) {
      this.type = var1;
   }

   public String toString() {
      StringBuilder var1;
      if (this.state == 1) {
         if (this.resolvedTarget == this) {
            var1 = new StringBuilder();
            var1.append("[");
            var1.append(this.myAnchor);
            var1.append(", RESOLVED: ");
            var1.append(this.resolvedOffset);
            var1.append("]  type: ");
            var1.append(this.sType(this.type));
            return var1.toString();
         } else {
            var1 = new StringBuilder();
            var1.append("[");
            var1.append(this.myAnchor);
            var1.append(", RESOLVED: ");
            var1.append(this.resolvedTarget);
            var1.append(":");
            var1.append(this.resolvedOffset);
            var1.append("] type: ");
            var1.append(this.sType(this.type));
            return var1.toString();
         }
      } else {
         var1 = new StringBuilder();
         var1.append("{ ");
         var1.append(this.myAnchor);
         var1.append(" UNRESOLVED} type: ");
         var1.append(this.sType(this.type));
         return var1.toString();
      }
   }

   public void update() {
      ConstraintAnchor var2 = this.myAnchor.getTarget();
      if (var2 != null) {
         if (var2.getTarget() == this.myAnchor) {
            this.type = 4;
            var2.getResolutionNode().type = 4;
         }

         int var1 = this.myAnchor.getMargin();
         if (this.myAnchor.mType == ConstraintAnchor.Type.RIGHT || this.myAnchor.mType == ConstraintAnchor.Type.BOTTOM) {
            var1 = -var1;
         }

         this.dependsOn(var2.getResolutionNode(), var1);
      }
   }
}
