package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;
import java.util.HashSet;

public class ConstraintAnchor {
   private static final boolean ALLOW_BINARY = false;
   public static final int ANY_GROUP = Integer.MAX_VALUE;
   public static final int APPLY_GROUP_RESULTS = -2;
   public static final int AUTO_CONSTRAINT_CREATOR = 2;
   public static final int SCOUT_CREATOR = 1;
   private static final int UNSET_GONE_MARGIN = -1;
   public static final int USER_CREATOR = 0;
   public static final boolean USE_CENTER_ANCHOR = false;
   private int mConnectionCreator;
   private ConstraintAnchor.ConnectionType mConnectionType;
   int mGoneMargin = -1;
   int mGroup;
   public int mMargin = 0;
   final ConstraintWidget mOwner;
   SolverVariable mSolverVariable;
   private ConstraintAnchor.Strength mStrength;
   ConstraintAnchor mTarget;
   final ConstraintAnchor.Type mType;

   public ConstraintAnchor(ConstraintWidget var1, ConstraintAnchor.Type var2) {
      this.mStrength = ConstraintAnchor.Strength.NONE;
      this.mConnectionType = ConstraintAnchor.ConnectionType.RELAXED;
      this.mConnectionCreator = 0;
      this.mGroup = Integer.MAX_VALUE;
      this.mOwner = var1;
      this.mType = var2;
   }

   private boolean isConnectionToMe(ConstraintWidget var1, HashSet var2) {
      if (var2.contains(var1)) {
         return false;
      } else {
         var2.add(var1);
         if (var1 == this.getOwner()) {
            return true;
         } else {
            ArrayList var6 = var1.getAnchors();
            int var3 = 0;

            for(int var4 = var6.size(); var3 < var4; ++var3) {
               ConstraintAnchor var5 = (ConstraintAnchor)var6.get(var3);
               if (var5.isSimilarDimensionConnection(this) && var5.isConnected() && this.isConnectionToMe(var5.getTarget().getOwner(), var2)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   private String toString(HashSet var1) {
      if (var1.add(this)) {
         StringBuilder var2 = new StringBuilder();
         var2.append(this.mOwner.getDebugName());
         var2.append(":");
         var2.append(this.mType.toString());
         String var4;
         if (this.mTarget != null) {
            StringBuilder var3 = new StringBuilder();
            var3.append(" connected to ");
            var3.append(this.mTarget.toString(var1));
            var4 = var3.toString();
         } else {
            var4 = "";
         }

         var2.append(var4);
         return var2.toString();
      } else {
         return "<-";
      }
   }

   public boolean connect(ConstraintAnchor var1, int var2) {
      return this.connect(var1, var2, -1, ConstraintAnchor.Strength.STRONG, 0, false);
   }

   public boolean connect(ConstraintAnchor var1, int var2, int var3) {
      return this.connect(var1, var2, -1, ConstraintAnchor.Strength.STRONG, var3, false);
   }

   public boolean connect(ConstraintAnchor var1, int var2, int var3, ConstraintAnchor.Strength var4, int var5, boolean var6) {
      if (var1 == null) {
         this.mTarget = null;
         this.mMargin = 0;
         this.mGoneMargin = -1;
         this.mStrength = ConstraintAnchor.Strength.NONE;
         this.mConnectionCreator = 2;
         return true;
      } else if (!var6 && !this.isValidConnection(var1)) {
         return false;
      } else {
         this.mTarget = var1;
         if (var2 > 0) {
            this.mMargin = var2;
         } else {
            this.mMargin = 0;
         }

         this.mGoneMargin = var3;
         this.mStrength = var4;
         this.mConnectionCreator = var5;
         return true;
      }
   }

   public boolean connect(ConstraintAnchor var1, int var2, ConstraintAnchor.Strength var3, int var4) {
      return this.connect(var1, var2, -1, var3, var4, false);
   }

   public int getConnectionCreator() {
      return this.mConnectionCreator;
   }

   public ConstraintAnchor.ConnectionType getConnectionType() {
      return this.mConnectionType;
   }

   public int getGroup() {
      return this.mGroup;
   }

   public int getMargin() {
      if (this.mOwner.getVisibility() == 8) {
         return 0;
      } else {
         if (this.mGoneMargin > -1) {
            ConstraintAnchor var1 = this.mTarget;
            if (var1 != null && var1.mOwner.getVisibility() == 8) {
               return this.mGoneMargin;
            }
         }

         return this.mMargin;
      }
   }

   public final ConstraintAnchor getOpposite() {
      switch(this.mType) {
      case LEFT:
         return this.mOwner.mRight;
      case RIGHT:
         return this.mOwner.mLeft;
      case TOP:
         return this.mOwner.mBottom;
      case BOTTOM:
         return this.mOwner.mTop;
      default:
         return null;
      }
   }

   public ConstraintWidget getOwner() {
      return this.mOwner;
   }

   public int getPriorityLevel() {
      switch(this.mType) {
      case CENTER:
         return 2;
      case LEFT:
         return 2;
      case RIGHT:
         return 2;
      case TOP:
         return 2;
      case BOTTOM:
         return 2;
      case CENTER_X:
         return 0;
      case CENTER_Y:
         return 0;
      case BASELINE:
         return 1;
      default:
         return 0;
      }
   }

   public int getSnapPriorityLevel() {
      switch(this.mType) {
      case CENTER:
         return 3;
      case LEFT:
         return 1;
      case RIGHT:
         return 1;
      case TOP:
         return 0;
      case BOTTOM:
         return 0;
      case CENTER_X:
         return 0;
      case CENTER_Y:
         return 1;
      case BASELINE:
         return 2;
      default:
         return 0;
      }
   }

   public SolverVariable getSolverVariable() {
      return this.mSolverVariable;
   }

   public ConstraintAnchor.Strength getStrength() {
      return this.mStrength;
   }

   public ConstraintAnchor getTarget() {
      return this.mTarget;
   }

   public ConstraintAnchor.Type getType() {
      return this.mType;
   }

   public boolean isConnected() {
      return this.mTarget != null;
   }

   public boolean isConnectionAllowed(ConstraintWidget var1) {
      if (this.isConnectionToMe(var1, new HashSet())) {
         return false;
      } else {
         ConstraintWidget var2 = this.getOwner().getParent();
         if (var2 == var1) {
            return true;
         } else {
            return var1.getParent() == var2;
         }
      }
   }

   public boolean isConnectionAllowed(ConstraintWidget var1, ConstraintAnchor var2) {
      return this.isConnectionAllowed(var1);
   }

   public boolean isSideAnchor() {
      switch(this.mType) {
      case LEFT:
      case RIGHT:
      case TOP:
      case BOTTOM:
         return true;
      default:
         return false;
      }
   }

   public boolean isSimilarDimensionConnection(ConstraintAnchor var1) {
      ConstraintAnchor.Type var6 = var1.getType();
      ConstraintAnchor.Type var5 = this.mType;
      boolean var4 = true;
      boolean var3 = true;
      if (var6 == var5) {
         return true;
      } else {
         boolean var2;
         switch(this.mType) {
         case CENTER:
            if (var6 != ConstraintAnchor.Type.BASELINE) {
               return true;
            }

            return false;
         case LEFT:
         case RIGHT:
         case CENTER_X:
            var2 = var4;
            if (var6 != ConstraintAnchor.Type.LEFT) {
               var2 = var4;
               if (var6 != ConstraintAnchor.Type.RIGHT) {
                  if (var6 == ConstraintAnchor.Type.CENTER_X) {
                     return true;
                  }

                  var2 = false;
               }
            }

            return var2;
         case TOP:
         case BOTTOM:
         case CENTER_Y:
         case BASELINE:
            var2 = var3;
            if (var6 != ConstraintAnchor.Type.TOP) {
               var2 = var3;
               if (var6 != ConstraintAnchor.Type.BOTTOM) {
                  var2 = var3;
                  if (var6 != ConstraintAnchor.Type.CENTER_Y) {
                     if (var6 == ConstraintAnchor.Type.BASELINE) {
                        return true;
                     }

                     var2 = false;
                  }
               }
            }

            return var2;
         default:
            return false;
         }
      }
   }

   public boolean isSnapCompatibleWith(ConstraintAnchor var1) {
      if (this.mType == ConstraintAnchor.Type.CENTER) {
         return false;
      } else if (this.mType == var1.getType()) {
         return true;
      } else {
         int var2;
         switch(this.mType) {
         case LEFT:
            var2 = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[var1.getType().ordinal()];
            if (var2 != 3) {
               if (var2 != 6) {
                  return false;
               }

               return true;
            }

            return true;
         case RIGHT:
            var2 = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[var1.getType().ordinal()];
            if (var2 != 2) {
               if (var2 != 6) {
                  return false;
               }

               return true;
            }

            return true;
         case TOP:
            var2 = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[var1.getType().ordinal()];
            if (var2 != 5) {
               if (var2 != 7) {
                  return false;
               }

               return true;
            }

            return true;
         case BOTTOM:
            var2 = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[var1.getType().ordinal()];
            if (var2 != 4) {
               if (var2 != 7) {
                  return false;
               }

               return true;
            }

            return true;
         case CENTER_X:
            switch(var1.getType()) {
            case LEFT:
               return true;
            case RIGHT:
               return true;
            default:
               return false;
            }
         case CENTER_Y:
            switch(var1.getType()) {
            case TOP:
               return true;
            case BOTTOM:
               return true;
            default:
               return false;
            }
         default:
            return false;
         }
      }
   }

   public boolean isValidConnection(ConstraintAnchor var1) {
      boolean var4 = false;
      boolean var5 = false;
      boolean var3 = false;
      if (var1 == null) {
         return false;
      } else {
         ConstraintAnchor.Type var6 = var1.getType();
         ConstraintAnchor.Type var7 = this.mType;
         if (var6 == var7) {
            if (var7 == ConstraintAnchor.Type.CENTER) {
               return false;
            } else if (this.mType == ConstraintAnchor.Type.BASELINE) {
               if (var1.getOwner().hasBaseline()) {
                  return this.getOwner().hasBaseline();
               } else {
                  return false;
               }
            } else {
               return true;
            }
         } else {
            boolean var2;
            switch(this.mType) {
            case CENTER:
               var2 = var5;
               if (var6 != ConstraintAnchor.Type.BASELINE) {
                  var2 = var5;
                  if (var6 != ConstraintAnchor.Type.CENTER_X) {
                     var2 = var5;
                     if (var6 != ConstraintAnchor.Type.CENTER_Y) {
                        var2 = true;
                     }
                  }
               }

               return var2;
            case LEFT:
            case RIGHT:
               if (var6 != ConstraintAnchor.Type.LEFT && var6 != ConstraintAnchor.Type.RIGHT) {
                  var2 = false;
               } else {
                  var2 = true;
               }

               if (!(var1.getOwner() instanceof Guideline)) {
                  return var2;
               } else {
                  if (!var2 && var6 != ConstraintAnchor.Type.CENTER_X) {
                     var2 = var4;
                  } else {
                     var2 = true;
                  }

                  return var2;
               }
            case TOP:
            case BOTTOM:
               if (var6 != ConstraintAnchor.Type.TOP && var6 != ConstraintAnchor.Type.BOTTOM) {
                  var2 = false;
               } else {
                  var2 = true;
               }

               if (!(var1.getOwner() instanceof Guideline)) {
                  return var2;
               }

               if (!var2 && var6 != ConstraintAnchor.Type.CENTER_Y) {
                  var2 = var3;
               } else {
                  var2 = true;
               }

               return var2;
            default:
               return false;
            }
         }
      }
   }

   public boolean isVerticalAnchor() {
      int var1 = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()];
      if (var1 != 6) {
         switch(var1) {
         case 1:
         case 2:
         case 3:
            break;
         default:
            return true;
         }
      }

      return false;
   }

   public void reset() {
      this.mTarget = null;
      this.mMargin = 0;
      this.mGoneMargin = -1;
      this.mStrength = ConstraintAnchor.Strength.STRONG;
      this.mConnectionCreator = 0;
      this.mConnectionType = ConstraintAnchor.ConnectionType.RELAXED;
   }

   public void resetSolverVariable(Cache var1) {
      SolverVariable var2 = this.mSolverVariable;
      if (var2 == null) {
         this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED);
      } else {
         var2.reset();
      }
   }

   public void setConnectionCreator(int var1) {
      this.mConnectionCreator = var1;
   }

   public void setConnectionType(ConstraintAnchor.ConnectionType var1) {
      this.mConnectionType = var1;
   }

   public void setGoneMargin(int var1) {
      if (this.isConnected()) {
         this.mGoneMargin = var1;
      }
   }

   public void setGroup(int var1) {
      this.mGroup = var1;
   }

   public void setMargin(int var1) {
      if (this.isConnected()) {
         this.mMargin = var1;
      }
   }

   public void setStrength(ConstraintAnchor.Strength var1) {
      if (this.isConnected()) {
         this.mStrength = var1;
      }
   }

   public String toString() {
      HashSet var1 = new HashSet();
      StringBuilder var2 = new StringBuilder();
      var2.append(this.mOwner.getDebugName());
      var2.append(":");
      var2.append(this.mType.toString());
      String var4;
      if (this.mTarget != null) {
         StringBuilder var3 = new StringBuilder();
         var3.append(" connected to ");
         var3.append(this.mTarget.toString(var1));
         var4 = var3.toString();
      } else {
         var4 = "";
      }

      var2.append(var4);
      return var2.toString();
   }

   public static enum ConnectionType {
      RELAXED,
      STRICT;
   }

   public static enum Strength {
      NONE,
      STRONG,
      WEAK;
   }

   public static enum Type {
      BASELINE,
      BOTTOM,
      CENTER,
      CENTER_X,
      CENTER_Y,
      LEFT,
      NONE,
      RIGHT,
      TOP;
   }
}
