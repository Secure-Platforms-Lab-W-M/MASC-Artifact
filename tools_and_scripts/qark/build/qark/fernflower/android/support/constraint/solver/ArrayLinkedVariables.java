package android.support.constraint.solver;

import java.io.PrintStream;
import java.util.Arrays;

public class ArrayLinkedVariables {
   private static final boolean DEBUG = false;
   private static final int NONE = -1;
   private int ROW_SIZE = 8;
   private SolverVariable candidate = null;
   int currentSize = 0;
   private int[] mArrayIndices;
   private int[] mArrayNextIndices;
   private float[] mArrayValues;
   private final Cache mCache;
   private boolean mDidFillOnce;
   private int mHead;
   private int mLast;
   private final ArrayRow mRow;

   ArrayLinkedVariables(ArrayRow var1, Cache var2) {
      int var3 = this.ROW_SIZE;
      this.mArrayIndices = new int[var3];
      this.mArrayNextIndices = new int[var3];
      this.mArrayValues = new float[var3];
      this.mHead = -1;
      this.mLast = -1;
      this.mDidFillOnce = false;
      this.mRow = var1;
      this.mCache = var2;
   }

   public final void add(SolverVariable var1, float var2) {
      if (var2 != 0.0F) {
         int var3;
         if (this.mHead == -1) {
            this.mHead = 0;
            float[] var10 = this.mArrayValues;
            var3 = this.mHead;
            var10[var3] = var2;
            this.mArrayIndices[var3] = var1.field_41;
            this.mArrayNextIndices[this.mHead] = -1;
            ++this.currentSize;
            if (!this.mDidFillOnce) {
               ++this.mLast;
            }
         } else {
            var3 = this.mHead;
            int var5 = -1;

            int var4;
            int[] var9;
            for(var4 = 0; var3 != -1 && var4 < this.currentSize; ++var4) {
               int var6 = this.mArrayIndices[var3];
               if (var6 == var1.field_41) {
                  float[] var8 = this.mArrayValues;
                  var8[var3] += var2;
                  if (var8[var3] == 0.0F) {
                     if (var3 == this.mHead) {
                        this.mHead = this.mArrayNextIndices[var3];
                     } else {
                        var9 = this.mArrayNextIndices;
                        var9[var5] = var9[var3];
                     }

                     this.mCache.mIndexedVariables[var6].removeClientEquation(this.mRow);
                     if (this.mDidFillOnce) {
                        this.mLast = var3;
                     }

                     --this.currentSize;
                     return;
                  }

                  return;
               }

               if (this.mArrayIndices[var3] < var1.field_41) {
                  var5 = var3;
               }

               var3 = this.mArrayNextIndices[var3];
            }

            var4 = this.mLast;
            var3 = var4 + 1;
            int[] var7;
            if (this.mDidFillOnce) {
               var7 = this.mArrayIndices;
               if (var7[var4] == -1) {
                  var3 = this.mLast;
               } else {
                  var3 = var7.length;
               }
            }

            var7 = this.mArrayIndices;
            if (var3 >= var7.length && this.currentSize < var7.length) {
               var4 = 0;

               while(true) {
                  var7 = this.mArrayIndices;
                  if (var4 >= var7.length) {
                     break;
                  }

                  if (var7[var4] == -1) {
                     var3 = var4;
                     break;
                  }

                  ++var4;
               }
            }

            var7 = this.mArrayIndices;
            if (var3 >= var7.length) {
               var3 = var7.length;
               this.ROW_SIZE *= 2;
               this.mDidFillOnce = false;
               this.mLast = var3 - 1;
               this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
               this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
               this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
            }

            this.mArrayIndices[var3] = var1.field_41;
            this.mArrayValues[var3] = var2;
            if (var5 != -1) {
               var9 = this.mArrayNextIndices;
               var9[var3] = var9[var5];
               var9[var5] = var3;
            } else {
               this.mArrayNextIndices[var3] = this.mHead;
               this.mHead = var3;
            }

            ++this.currentSize;
            if (!this.mDidFillOnce) {
               ++this.mLast;
            }

            var3 = this.mLast;
            var9 = this.mArrayIndices;
            if (var3 >= var9.length) {
               this.mDidFillOnce = true;
               this.mLast = var9.length - 1;
            }
         }
      }
   }

   public final void clear() {
      this.mHead = -1;
      this.mLast = -1;
      this.mDidFillOnce = false;
      this.currentSize = 0;
   }

   final boolean containsKey(SolverVariable var1) {
      if (this.mHead == -1) {
         return false;
      } else {
         int var3 = this.mHead;

         for(int var2 = 0; var3 != -1 && var2 < this.currentSize; ++var2) {
            if (this.mArrayIndices[var3] == var1.field_41) {
               return true;
            }

            var3 = this.mArrayNextIndices[var3];
         }

         return false;
      }
   }

   public void display() {
      int var2 = this.currentSize;
      System.out.print("{ ");

      for(int var1 = 0; var1 < var2; ++var1) {
         SolverVariable var3 = this.getVariable(var1);
         if (var3 != null) {
            PrintStream var4 = System.out;
            StringBuilder var5 = new StringBuilder();
            var5.append(var3);
            var5.append(" = ");
            var5.append(this.getVariableValue(var1));
            var5.append(" ");
            var4.print(var5.toString());
         }
      }

      System.out.println(" }");
   }

   void divideByAmount(float var1) {
      int var3 = this.mHead;

      for(int var2 = 0; var3 != -1 && var2 < this.currentSize; ++var2) {
         float[] var4 = this.mArrayValues;
         var4[var3] /= var1;
         var3 = this.mArrayNextIndices[var3];
      }

   }

   public final float get(SolverVariable var1) {
      int var3 = this.mHead;

      for(int var2 = 0; var3 != -1 && var2 < this.currentSize; ++var2) {
         if (this.mArrayIndices[var3] == var1.field_41) {
            return this.mArrayValues[var3];
         }

         var3 = this.mArrayNextIndices[var3];
      }

      return 0.0F;
   }

   SolverVariable getPivotCandidate() {
      SolverVariable var3 = this.candidate;
      if (var3 != null) {
         return var3;
      } else {
         int var2 = this.mHead;
         int var1 = 0;

         for(var3 = null; var2 != -1 && var1 < this.currentSize; ++var1) {
            if (this.mArrayValues[var2] < 0.0F) {
               SolverVariable var4 = this.mCache.mIndexedVariables[this.mArrayIndices[var2]];
               if (var3 == null || var3.strength < var4.strength) {
                  var3 = var4;
               }
            }

            var2 = this.mArrayNextIndices[var2];
         }

         return var3;
      }
   }

   final SolverVariable getVariable(int var1) {
      int var3 = this.mHead;

      for(int var2 = 0; var3 != -1 && var2 < this.currentSize; ++var2) {
         if (var2 == var1) {
            return this.mCache.mIndexedVariables[this.mArrayIndices[var3]];
         }

         var3 = this.mArrayNextIndices[var3];
      }

      return null;
   }

   final float getVariableValue(int var1) {
      int var3 = this.mHead;

      for(int var2 = 0; var3 != -1 && var2 < this.currentSize; ++var2) {
         if (var2 == var1) {
            return this.mArrayValues[var3];
         }

         var3 = this.mArrayNextIndices[var3];
      }

      return 0.0F;
   }

   boolean hasAtLeastOnePositiveVariable() {
      int var2 = this.mHead;

      for(int var1 = 0; var2 != -1 && var1 < this.currentSize; ++var1) {
         if (this.mArrayValues[var2] > 0.0F) {
            return true;
         }

         var2 = this.mArrayNextIndices[var2];
      }

      return false;
   }

   void invert() {
      int var2 = this.mHead;

      for(int var1 = 0; var2 != -1 && var1 < this.currentSize; ++var1) {
         float[] var3 = this.mArrayValues;
         var3[var2] *= -1.0F;
         var2 = this.mArrayNextIndices[var2];
      }

   }

   SolverVariable pickPivotCandidate() {
      SolverVariable var5 = null;
      SolverVariable var4 = null;
      int var3 = this.mHead;

      for(int var2 = 0; var3 != -1 && var2 < this.currentSize; ++var2) {
         float[] var6 = this.mArrayValues;
         float var1 = var6[var3];
         if (var1 < 0.0F) {
            if (var1 > -0.001F) {
               var6[var3] = 0.0F;
               var1 = 0.0F;
            }
         } else if (var1 < 0.001F) {
            var6[var3] = 0.0F;
            var1 = 0.0F;
         }

         if (var1 != 0.0F) {
            SolverVariable var7 = this.mCache.mIndexedVariables[this.mArrayIndices[var3]];
            if (var7.mType == SolverVariable.Type.UNRESTRICTED) {
               if (var1 < 0.0F) {
                  return var7;
               }

               if (var4 == null) {
                  var4 = var7;
               }
            } else if (var1 < 0.0F && (var5 == null || var7.strength < var5.strength)) {
               var5 = var7;
            }
         }

         var3 = this.mArrayNextIndices[var3];
      }

      return var4 != null ? var4 : var5;
   }

   public final void put(SolverVariable var1, float var2) {
      if (var2 == 0.0F) {
         this.remove(var1);
      } else {
         int var3;
         if (this.mHead == -1) {
            this.mHead = 0;
            float[] var8 = this.mArrayValues;
            var3 = this.mHead;
            var8[var3] = var2;
            this.mArrayIndices[var3] = var1.field_41;
            this.mArrayNextIndices[this.mHead] = -1;
            ++this.currentSize;
            if (!this.mDidFillOnce) {
               ++this.mLast;
            }
         } else {
            var3 = this.mHead;
            int var5 = -1;

            int var4;
            for(var4 = 0; var3 != -1 && var4 < this.currentSize; ++var4) {
               if (this.mArrayIndices[var3] == var1.field_41) {
                  this.mArrayValues[var3] = var2;
                  return;
               }

               if (this.mArrayIndices[var3] < var1.field_41) {
                  var5 = var3;
               }

               var3 = this.mArrayNextIndices[var3];
            }

            var4 = this.mLast;
            var3 = var4 + 1;
            int[] var6;
            if (this.mDidFillOnce) {
               var6 = this.mArrayIndices;
               if (var6[var4] == -1) {
                  var3 = this.mLast;
               } else {
                  var3 = var6.length;
               }
            }

            var6 = this.mArrayIndices;
            if (var3 >= var6.length && this.currentSize < var6.length) {
               var4 = 0;

               while(true) {
                  var6 = this.mArrayIndices;
                  if (var4 >= var6.length) {
                     break;
                  }

                  if (var6[var4] == -1) {
                     var3 = var4;
                     break;
                  }

                  ++var4;
               }
            }

            var6 = this.mArrayIndices;
            if (var3 >= var6.length) {
               var3 = var6.length;
               this.ROW_SIZE *= 2;
               this.mDidFillOnce = false;
               this.mLast = var3 - 1;
               this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
               this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
               this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
            }

            this.mArrayIndices[var3] = var1.field_41;
            this.mArrayValues[var3] = var2;
            if (var5 != -1) {
               int[] var7 = this.mArrayNextIndices;
               var7[var3] = var7[var5];
               var7[var5] = var3;
            } else {
               this.mArrayNextIndices[var3] = this.mHead;
               this.mHead = var3;
            }

            ++this.currentSize;
            if (!this.mDidFillOnce) {
               ++this.mLast;
            }

            if (this.currentSize >= this.mArrayIndices.length) {
               this.mDidFillOnce = true;
            }
         }
      }
   }

   public final float remove(SolverVariable var1) {
      if (this.candidate == var1) {
         this.candidate = null;
      }

      if (this.mHead == -1) {
         return 0.0F;
      } else {
         int var2 = this.mHead;
         int var4 = -1;

         for(int var3 = 0; var2 != -1 && var3 < this.currentSize; ++var3) {
            int var5 = this.mArrayIndices[var2];
            if (var5 == var1.field_41) {
               if (var2 == this.mHead) {
                  this.mHead = this.mArrayNextIndices[var2];
               } else {
                  int[] var6 = this.mArrayNextIndices;
                  var6[var4] = var6[var2];
               }

               this.mCache.mIndexedVariables[var5].removeClientEquation(this.mRow);
               --this.currentSize;
               this.mArrayIndices[var2] = -1;
               if (this.mDidFillOnce) {
                  this.mLast = var2;
               }

               return this.mArrayValues[var2];
            }

            var4 = var2;
            var2 = this.mArrayNextIndices[var2];
         }

         return 0.0F;
      }
   }

   int sizeInBytes() {
      return 0 + this.mArrayIndices.length * 4 * 3 + 36;
   }

   public String toString() {
      String var3 = "";
      int var2 = this.mHead;

      for(int var1 = 0; var2 != -1 && var1 < this.currentSize; ++var1) {
         StringBuilder var4 = new StringBuilder();
         var4.append(var3);
         var4.append(" -> ");
         var3 = var4.toString();
         var4 = new StringBuilder();
         var4.append(var3);
         var4.append(this.mArrayValues[var2]);
         var4.append(" : ");
         var3 = var4.toString();
         var4 = new StringBuilder();
         var4.append(var3);
         var4.append(this.mCache.mIndexedVariables[this.mArrayIndices[var2]]);
         var3 = var4.toString();
         var2 = this.mArrayNextIndices[var2];
      }

      return var3;
   }

   void updateClientEquations(ArrayRow var1) {
      int var3 = this.mHead;

      for(int var2 = 0; var3 != -1 && var2 < this.currentSize; ++var2) {
         this.mCache.mIndexedVariables[this.mArrayIndices[var3]].addClientEquation(var1);
         var3 = this.mArrayNextIndices[var3];
      }

   }

   void updateFromRow(ArrayRow var1, ArrayRow var2) {
      int var4 = this.mHead;
      int var5 = 0;

      while(var4 != -1 && var5 < this.currentSize) {
         if (this.mArrayIndices[var4] != var2.variable.field_41) {
            var4 = this.mArrayNextIndices[var4];
            ++var5;
         } else {
            float var3 = this.mArrayValues[var4];
            this.remove(var2.variable);
            ArrayLinkedVariables var6 = var2.variables;
            var5 = var6.mHead;

            for(var4 = 0; var5 != -1 && var4 < var6.currentSize; ++var4) {
               this.add(this.mCache.mIndexedVariables[var6.mArrayIndices[var5]], var6.mArrayValues[var5] * var3);
               var5 = var6.mArrayNextIndices[var5];
            }

            var1.constantValue += var2.constantValue * var3;
            var2.variable.removeClientEquation(var1);
            var4 = this.mHead;
            var5 = 0;
         }
      }

   }

   void updateFromSystem(ArrayRow var1, ArrayRow[] var2) {
      int var4 = this.mHead;
      int var5 = 0;

      while(var4 != -1 && var5 < this.currentSize) {
         SolverVariable var6 = this.mCache.mIndexedVariables[this.mArrayIndices[var4]];
         if (var6.definitionId == -1) {
            var4 = this.mArrayNextIndices[var4];
            ++var5;
         } else {
            float var3 = this.mArrayValues[var4];
            this.remove(var6);
            ArrayRow var8 = var2[var6.definitionId];
            if (!var8.isSimpleDefinition) {
               ArrayLinkedVariables var7 = var8.variables;
               var5 = var7.mHead;

               for(var4 = 0; var5 != -1 && var4 < var7.currentSize; ++var4) {
                  this.add(this.mCache.mIndexedVariables[var7.mArrayIndices[var5]], var7.mArrayValues[var5] * var3);
                  var5 = var7.mArrayNextIndices[var5];
               }
            }

            var1.constantValue += var8.constantValue * var3;
            var8.variable.removeClientEquation(var1);
            var4 = this.mHead;
            var5 = 0;
         }
      }

   }
}
