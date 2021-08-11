package android.support.constraint.solver;

public class ArrayRow {
   private static final boolean DEBUG = false;
   float constantValue = 0.0F;
   boolean isSimpleDefinition = false;
   boolean used = false;
   SolverVariable variable = null;
   final ArrayLinkedVariables variables;

   public ArrayRow(Cache var1) {
      this.variables = new ArrayLinkedVariables(this, var1);
   }

   public ArrayRow addError(SolverVariable var1, SolverVariable var2) {
      this.variables.put(var1, 1.0F);
      this.variables.put(var2, -1.0F);
      return this;
   }

   ArrayRow addSingleError(SolverVariable var1, int var2) {
      this.variables.put(var1, (float)var2);
      return this;
   }

   ArrayRow createRowCentering(SolverVariable var1, SolverVariable var2, int var3, float var4, SolverVariable var5, SolverVariable var6, int var7) {
      if (var2 == var5) {
         this.variables.put(var1, 1.0F);
         this.variables.put(var6, 1.0F);
         this.variables.put(var2, -2.0F);
         return this;
      } else if (var4 == 0.5F) {
         this.variables.put(var1, 1.0F);
         this.variables.put(var2, -1.0F);
         this.variables.put(var5, -1.0F);
         this.variables.put(var6, 1.0F);
         if (var3 <= 0 && var7 <= 0) {
            return this;
         } else {
            this.constantValue = (float)(-var3 + var7);
            return this;
         }
      } else if (var4 <= 0.0F) {
         this.variables.put(var1, -1.0F);
         this.variables.put(var2, 1.0F);
         this.constantValue = (float)var3;
         return this;
      } else if (var4 >= 1.0F) {
         this.variables.put(var5, -1.0F);
         this.variables.put(var6, 1.0F);
         this.constantValue = (float)var7;
         return this;
      } else {
         this.variables.put(var1, (1.0F - var4) * 1.0F);
         this.variables.put(var2, (1.0F - var4) * -1.0F);
         this.variables.put(var5, -1.0F * var4);
         this.variables.put(var6, var4 * 1.0F);
         if (var3 <= 0 && var7 <= 0) {
            return this;
         } else {
            this.constantValue = (float)(-var3) * (1.0F - var4) + (float)var7 * var4;
            return this;
         }
      }
   }

   ArrayRow createRowDefinition(SolverVariable var1, int var2) {
      this.variable = var1;
      var1.computedValue = (float)var2;
      this.constantValue = (float)var2;
      this.isSimpleDefinition = true;
      return this;
   }

   ArrayRow createRowDimensionPercent(SolverVariable var1, SolverVariable var2, SolverVariable var3, float var4) {
      this.variables.put(var1, -1.0F);
      this.variables.put(var2, 1.0F - var4);
      this.variables.put(var3, var4);
      return this;
   }

   public ArrayRow createRowDimensionRatio(SolverVariable var1, SolverVariable var2, SolverVariable var3, SolverVariable var4, float var5) {
      this.variables.put(var1, -1.0F);
      this.variables.put(var2, 1.0F);
      this.variables.put(var3, var5);
      this.variables.put(var4, -var5);
      return this;
   }

   public ArrayRow createRowEqualDimension(float var1, float var2, float var3, SolverVariable var4, int var5, SolverVariable var6, int var7, SolverVariable var8, int var9, SolverVariable var10, int var11) {
      if (var2 != 0.0F && var1 != var3) {
         var1 = var1 / var2 / (var3 / var2);
         this.constantValue = (float)(-var5 - var7) + (float)var9 * var1 + (float)var11 * var1;
         this.variables.put(var4, 1.0F);
         this.variables.put(var6, -1.0F);
         this.variables.put(var10, var1);
         this.variables.put(var8, -var1);
         return this;
      } else {
         this.constantValue = (float)(-var5 - var7 + var9 + var11);
         this.variables.put(var4, 1.0F);
         this.variables.put(var6, -1.0F);
         this.variables.put(var10, 1.0F);
         this.variables.put(var8, -1.0F);
         return this;
      }
   }

   public ArrayRow createRowEquals(SolverVariable var1, int var2) {
      if (var2 < 0) {
         this.constantValue = (float)(var2 * -1);
         this.variables.put(var1, 1.0F);
         return this;
      } else {
         this.constantValue = (float)var2;
         this.variables.put(var1, -1.0F);
         return this;
      }
   }

   public ArrayRow createRowEquals(SolverVariable var1, SolverVariable var2, int var3) {
      boolean var4 = false;
      boolean var5 = false;
      boolean var6;
      if (var3 != 0) {
         int var7;
         if (var3 < 0) {
            var7 = var3 * -1;
            var6 = true;
         } else {
            var7 = var3;
            var6 = var5;
         }

         this.constantValue = (float)var7;
      } else {
         var6 = var4;
      }

      if (!var6) {
         this.variables.put(var1, -1.0F);
         this.variables.put(var2, 1.0F);
         return this;
      } else {
         this.variables.put(var1, 1.0F);
         this.variables.put(var2, -1.0F);
         return this;
      }
   }

   public ArrayRow createRowGreaterThan(SolverVariable var1, SolverVariable var2, SolverVariable var3, int var4) {
      boolean var5 = false;
      boolean var6 = false;
      boolean var7;
      if (var4 != 0) {
         int var8;
         if (var4 < 0) {
            var8 = var4 * -1;
            var7 = true;
         } else {
            var8 = var4;
            var7 = var6;
         }

         this.constantValue = (float)var8;
      } else {
         var7 = var5;
      }

      if (!var7) {
         this.variables.put(var1, -1.0F);
         this.variables.put(var2, 1.0F);
         this.variables.put(var3, 1.0F);
         return this;
      } else {
         this.variables.put(var1, 1.0F);
         this.variables.put(var2, -1.0F);
         this.variables.put(var3, -1.0F);
         return this;
      }
   }

   public ArrayRow createRowLowerThan(SolverVariable var1, SolverVariable var2, SolverVariable var3, int var4) {
      boolean var5 = false;
      boolean var6 = false;
      boolean var7;
      if (var4 != 0) {
         int var8;
         if (var4 < 0) {
            var8 = var4 * -1;
            var7 = true;
         } else {
            var8 = var4;
            var7 = var6;
         }

         this.constantValue = (float)var8;
      } else {
         var7 = var5;
      }

      if (!var7) {
         this.variables.put(var1, -1.0F);
         this.variables.put(var2, 1.0F);
         this.variables.put(var3, -1.0F);
         return this;
      } else {
         this.variables.put(var1, 1.0F);
         this.variables.put(var2, -1.0F);
         this.variables.put(var3, 1.0F);
         return this;
      }
   }

   void ensurePositiveConstant() {
      float var1 = this.constantValue;
      if (var1 < 0.0F) {
         this.constantValue = var1 * -1.0F;
         this.variables.invert();
      }
   }

   boolean hasAtLeastOnePositiveVariable() {
      return this.variables.hasAtLeastOnePositiveVariable();
   }

   boolean hasKeyVariable() {
      SolverVariable var1 = this.variable;
      return var1 != null && (var1.mType == SolverVariable.Type.UNRESTRICTED || this.constantValue >= 0.0F);
   }

   boolean hasVariable(SolverVariable var1) {
      return this.variables.containsKey(var1);
   }

   void pickRowVariable() {
      SolverVariable var1 = this.variables.pickPivotCandidate();
      if (var1 != null) {
         this.pivot(var1);
      }

      if (this.variables.currentSize == 0) {
         this.isSimpleDefinition = true;
      }
   }

   void pivot(SolverVariable var1) {
      SolverVariable var3 = this.variable;
      if (var3 != null) {
         this.variables.put(var3, -1.0F);
         this.variable = null;
      }

      float var2 = this.variables.remove(var1) * -1.0F;
      this.variable = var1;
      if (var2 != 1.0F) {
         this.constantValue /= var2;
         this.variables.divideByAmount(var2);
      }
   }

   public void reset() {
      this.variable = null;
      this.variables.clear();
      this.constantValue = 0.0F;
      this.isSimpleDefinition = false;
   }

   int sizeInBytes() {
      int var1 = 0;
      if (this.variable != null) {
         var1 = 0 + 4;
      }

      return var1 + 4 + 4 + this.variables.sizeInBytes();
   }

   String toReadableString() {
      StringBuilder var5;
      String var8;
      if (this.variable == null) {
         var5 = new StringBuilder();
         var5.append("");
         var5.append("0");
         var8 = var5.toString();
      } else {
         var5 = new StringBuilder();
         var5.append("");
         var5.append(this.variable);
         var8 = var5.toString();
      }

      StringBuilder var6 = new StringBuilder();
      var6.append(var8);
      var6.append(" = ");
      var8 = var6.toString();
      boolean var2 = false;
      if (this.constantValue != 0.0F) {
         var6 = new StringBuilder();
         var6.append(var8);
         var6.append(this.constantValue);
         var8 = var6.toString();
         var2 = true;
      }

      int var4 = this.variables.currentSize;

      for(int var3 = 0; var3 < var4; ++var3) {
         SolverVariable var9 = this.variables.getVariable(var3);
         if (var9 != null) {
            float var1 = this.variables.getVariableValue(var3);
            String var10 = var9.toString();
            StringBuilder var7;
            if (!var2) {
               if (var1 < 0.0F) {
                  var7 = new StringBuilder();
                  var7.append(var8);
                  var7.append("- ");
                  var8 = var7.toString();
                  var1 *= -1.0F;
               }
            } else if (var1 > 0.0F) {
               var7 = new StringBuilder();
               var7.append(var8);
               var7.append(" + ");
               var8 = var7.toString();
            } else {
               var7 = new StringBuilder();
               var7.append(var8);
               var7.append(" - ");
               var8 = var7.toString();
               var1 *= -1.0F;
            }

            if (var1 == 1.0F) {
               var7 = new StringBuilder();
               var7.append(var8);
               var7.append(var10);
               var8 = var7.toString();
            } else {
               var7 = new StringBuilder();
               var7.append(var8);
               var7.append(var1);
               var7.append(" ");
               var7.append(var10);
               var8 = var7.toString();
            }

            var2 = true;
         }
      }

      if (!var2) {
         var6 = new StringBuilder();
         var6.append(var8);
         var6.append("0.0");
         return var6.toString();
      } else {
         return var8;
      }
   }

   public String toString() {
      return this.toReadableString();
   }

   void updateClientEquations() {
      this.variables.updateClientEquations(this);
   }

   boolean updateRowWithEquation(ArrayRow var1) {
      this.variables.updateFromRow(this, var1);
      return true;
   }
}
