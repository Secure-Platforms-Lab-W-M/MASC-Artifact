package android.support.constraint.solver;

import android.support.constraint.solver.widgets.ConstraintAnchor;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem {
   private static final boolean DEBUG = false;
   private static int POOL_SIZE = 1000;
   private int TABLE_SIZE = 32;
   private boolean[] mAlreadyTestedCandidates;
   final Cache mCache;
   private Goal mGoal = new Goal();
   private int mMaxColumns;
   private int mMaxRows;
   int mNumColumns;
   private int mNumRows;
   private SolverVariable[] mPoolVariables;
   private int mPoolVariablesCount;
   private ArrayRow[] mRows;
   private HashMap mVariables = null;
   int mVariablesID = 0;
   private ArrayRow[] tempClientsCopy;

   public LinearSystem() {
      int var1 = this.TABLE_SIZE;
      this.mMaxColumns = var1;
      this.mRows = null;
      this.mAlreadyTestedCandidates = new boolean[var1];
      this.mNumColumns = 1;
      this.mNumRows = 0;
      this.mMaxRows = var1;
      this.mPoolVariables = new SolverVariable[POOL_SIZE];
      this.mPoolVariablesCount = 0;
      this.tempClientsCopy = new ArrayRow[var1];
      this.mRows = new ArrayRow[var1];
      this.releaseRows();
      this.mCache = new Cache();
   }

   private SolverVariable acquireSolverVariable(SolverVariable.Type var1) {
      SolverVariable var4 = (SolverVariable)this.mCache.solverVariablePool.acquire();
      SolverVariable var5;
      if (var4 == null) {
         var5 = new SolverVariable(var1);
      } else {
         var4.reset();
         var4.setType(var1);
         var5 = var4;
      }

      int var2 = this.mPoolVariablesCount;
      int var3 = POOL_SIZE;
      if (var2 >= var3) {
         POOL_SIZE = var3 * 2;
         this.mPoolVariables = (SolverVariable[])Arrays.copyOf(this.mPoolVariables, POOL_SIZE);
      }

      SolverVariable[] var6 = this.mPoolVariables;
      var2 = this.mPoolVariablesCount++;
      var6[var2] = var5;
      return var5;
   }

   private void addError(ArrayRow var1) {
      var1.addError(this.createErrorVariable(), this.createErrorVariable());
   }

   private void addSingleError(ArrayRow var1, int var2) {
      var1.addSingleError(this.createErrorVariable(), var2);
   }

   private void computeValues() {
      for(int var1 = 0; var1 < this.mNumRows; ++var1) {
         ArrayRow var2 = this.mRows[var1];
         var2.variable.computedValue = var2.constantValue;
      }

   }

   public static ArrayRow createRowCentering(LinearSystem var0, SolverVariable var1, SolverVariable var2, int var3, float var4, SolverVariable var5, SolverVariable var6, int var7, boolean var8) {
      ArrayRow var9 = var0.createRow();
      var9.createRowCentering(var1, var2, var3, var4, var5, var6, var7);
      if (var8) {
         var1 = var0.createErrorVariable();
         SolverVariable var10 = var0.createErrorVariable();
         var1.strength = 4;
         var10.strength = 4;
         var9.addError(var1, var10);
         return var9;
      } else {
         return var9;
      }
   }

   public static ArrayRow createRowDimensionPercent(LinearSystem var0, SolverVariable var1, SolverVariable var2, SolverVariable var3, float var4, boolean var5) {
      ArrayRow var6 = var0.createRow();
      if (var5) {
         var0.addError(var6);
      }

      return var6.createRowDimensionPercent(var1, var2, var3, var4);
   }

   public static ArrayRow createRowEquals(LinearSystem var0, SolverVariable var1, SolverVariable var2, int var3, boolean var4) {
      ArrayRow var5 = var0.createRow();
      var5.createRowEquals(var1, var2, var3);
      if (var4) {
         var0.addSingleError(var5, 1);
         return var5;
      } else {
         return var5;
      }
   }

   public static ArrayRow createRowGreaterThan(LinearSystem var0, SolverVariable var1, SolverVariable var2, int var3, boolean var4) {
      SolverVariable var5 = var0.createSlackVariable();
      ArrayRow var6 = var0.createRow();
      var6.createRowGreaterThan(var1, var2, var5, var3);
      if (var4) {
         var0.addSingleError(var6, (int)(-1.0F * var6.variables.get(var5)));
         return var6;
      } else {
         return var6;
      }
   }

   public static ArrayRow createRowLowerThan(LinearSystem var0, SolverVariable var1, SolverVariable var2, int var3, boolean var4) {
      SolverVariable var5 = var0.createSlackVariable();
      ArrayRow var6 = var0.createRow();
      var6.createRowLowerThan(var1, var2, var5, var3);
      if (var4) {
         var0.addSingleError(var6, (int)(-1.0F * var6.variables.get(var5)));
         return var6;
      } else {
         return var6;
      }
   }

   private SolverVariable createVariable(String var1, SolverVariable.Type var2) {
      if (this.mNumColumns + 1 >= this.mMaxColumns) {
         this.increaseTableSize();
      }

      SolverVariable var3 = this.acquireSolverVariable(var2);
      var3.setName(var1);
      ++this.mVariablesID;
      ++this.mNumColumns;
      var3.field_41 = this.mVariablesID;
      if (this.mVariables == null) {
         this.mVariables = new HashMap();
      }

      this.mVariables.put(var1, var3);
      this.mCache.mIndexedVariables[this.mVariablesID] = var3;
      return var3;
   }

   private void displayRows() {
      this.displaySolverVariables();
      String var2 = "";

      StringBuilder var3;
      for(int var1 = 0; var1 < this.mNumRows; ++var1) {
         var3 = new StringBuilder();
         var3.append(var2);
         var3.append(this.mRows[var1]);
         var2 = var3.toString();
         var3 = new StringBuilder();
         var3.append(var2);
         var3.append("\n");
         var2 = var3.toString();
      }

      if (this.mGoal.variables.size() != 0) {
         var3 = new StringBuilder();
         var3.append(var2);
         var3.append(this.mGoal);
         var3.append("\n");
         var2 = var3.toString();
      }

      System.out.println(var2);
   }

   private void displaySolverVariables() {
      StringBuilder var2 = new StringBuilder();
      var2.append("Display Rows (");
      var2.append(this.mNumRows);
      var2.append("x");
      var2.append(this.mNumColumns);
      var2.append(") :\n\t | C | ");
      String var5 = var2.toString();

      StringBuilder var6;
      for(int var1 = 1; var1 <= this.mNumColumns; ++var1) {
         SolverVariable var3 = this.mCache.mIndexedVariables[var1];
         StringBuilder var4 = new StringBuilder();
         var4.append(var5);
         var4.append(var3);
         var5 = var4.toString();
         var6 = new StringBuilder();
         var6.append(var5);
         var6.append(" | ");
         var5 = var6.toString();
      }

      var6 = new StringBuilder();
      var6.append(var5);
      var6.append("\n");
      var5 = var6.toString();
      System.out.println(var5);
   }

   private int enforceBFS(Goal var1) throws Exception {
      byte var7 = 0;
      boolean var8 = false;
      int var5 = 0;

      boolean var6;
      while(true) {
         var6 = var8;
         if (var5 >= this.mNumRows) {
            break;
         }

         if (this.mRows[var5].variable.mType != SolverVariable.Type.UNRESTRICTED && this.mRows[var5].constantValue < 0.0F) {
            var6 = true;
            break;
         }

         ++var5;
      }

      int var18;
      if (var6) {
         boolean var11 = false;

         int var15;
         for(var5 = 0; !var11; var5 = var15) {
            var15 = var5 + 1;
            float var2 = Float.MAX_VALUE;
            int var19 = 0;
            var18 = -1;
            var5 = -1;

            ArrayRow var16;
            for(int var20 = 0; var20 < this.mNumRows; ++var20) {
               var16 = this.mRows[var20];
               int var14;
               if (var16.variable.mType != SolverVariable.Type.UNRESTRICTED && var16.constantValue < 0.0F) {
                  for(int var9 = 1; var9 < this.mNumColumns; var5 = var14) {
                     SolverVariable var17 = this.mCache.mIndexedVariables[var9];
                     float var4 = var16.variables.get(var17);
                     float var3;
                     int var12;
                     int var13;
                     if (var4 <= 0.0F) {
                        var3 = var2;
                        var12 = var19;
                        var13 = var18;
                        var14 = var5;
                     } else {
                        int var10 = 0;

                        while(true) {
                           var3 = var2;
                           var12 = var19;
                           var13 = var18;
                           var14 = var5;
                           if (var10 >= 6) {
                              break;
                           }

                           var3 = var17.strengthVector[var10] / var4;
                           if (var3 < var2 && var10 == var19 || var10 > var19) {
                              var2 = var3;
                              var18 = var20;
                              var5 = var9;
                              var19 = var10;
                           }

                           ++var10;
                        }
                     }

                     ++var9;
                     var2 = var3;
                     var19 = var12;
                     var18 = var13;
                  }
               }
            }

            if (var18 == -1) {
               var11 = true;
            } else {
               var16 = this.mRows[var18];
               var16.variable.definitionId = -1;
               var16.pivot(this.mCache.mIndexedVariables[var5]);
               var16.variable.definitionId = var18;

               for(var5 = 0; var5 < this.mNumRows; ++var5) {
                  this.mRows[var5].updateRowWithEquation(var16);
               }

               var1.updateFromSystem(this);
            }
         }
      } else {
         var5 = var7;
      }

      for(var18 = 0; var18 < this.mNumRows; ++var18) {
         if (this.mRows[var18].variable.mType != SolverVariable.Type.UNRESTRICTED && this.mRows[var18].constantValue < 0.0F) {
            return var5;
         }
      }

      return var5;
   }

   private String getDisplaySize(int var1) {
      int var2 = var1 * 4 / 1024 / 1024;
      StringBuilder var3;
      if (var2 > 0) {
         var3 = new StringBuilder();
         var3.append("");
         var3.append(var2);
         var3.append(" Mb");
         return var3.toString();
      } else {
         var2 = var1 * 4 / 1024;
         if (var2 > 0) {
            var3 = new StringBuilder();
            var3.append("");
            var3.append(var2);
            var3.append(" Kb");
            return var3.toString();
         } else {
            var3 = new StringBuilder();
            var3.append("");
            var3.append(var1 * 4);
            var3.append(" bytes");
            return var3.toString();
         }
      }
   }

   private void increaseTableSize() {
      this.TABLE_SIZE *= 2;
      this.mRows = (ArrayRow[])Arrays.copyOf(this.mRows, this.TABLE_SIZE);
      Cache var2 = this.mCache;
      var2.mIndexedVariables = (SolverVariable[])Arrays.copyOf(var2.mIndexedVariables, this.TABLE_SIZE);
      int var1 = this.TABLE_SIZE;
      this.mAlreadyTestedCandidates = new boolean[var1];
      this.mMaxColumns = var1;
      this.mMaxRows = var1;
      this.mGoal.variables.clear();
   }

   private int optimize(Goal var1) {
      boolean var7 = false;
      int var5 = 0;

      for(int var4 = 0; var4 < this.mNumColumns; ++var4) {
         this.mAlreadyTestedCandidates[var4] = false;
      }

      int var6 = 0;

      int var8;
      for(boolean var12 = var7; !var12; var5 = var8) {
         var8 = var5 + 1;
         SolverVariable var9 = var1.getPivotCandidate();
         if (var9 != null) {
            if (this.mAlreadyTestedCandidates[var9.field_41]) {
               var9 = null;
            } else {
               this.mAlreadyTestedCandidates[var9.field_41] = true;
               ++var6;
               if (var6 >= this.mNumColumns) {
                  var12 = true;
               }
            }
         }

         if (var9 == null) {
            var12 = true;
         } else {
            float var2 = Float.MAX_VALUE;
            int var13 = -1;

            ArrayRow var10;
            for(var5 = 0; var5 < this.mNumRows; ++var5) {
               var10 = this.mRows[var5];
               if (var10.variable.mType != SolverVariable.Type.UNRESTRICTED && var10.hasVariable(var9)) {
                  float var3 = var10.variables.get(var9);
                  if (var3 < 0.0F) {
                     var3 = -var10.constantValue / var3;
                     if (var3 < var2) {
                        var2 = var3;
                        var13 = var5;
                     }
                  }
               }
            }

            if (var13 <= -1) {
               var12 = true;
            } else {
               var10 = this.mRows[var13];
               var10.variable.definitionId = -1;
               var10.pivot(var9);
               var10.variable.definitionId = var13;

               for(var5 = 0; var5 < this.mNumRows; ++var5) {
                  this.mRows[var5].updateRowWithEquation(var10);
               }

               var1.updateFromSystem(this);

               try {
                  this.enforceBFS(var1);
               } catch (Exception var11) {
                  var11.printStackTrace();
               }
            }
         }
      }

      return var5;
   }

   private void releaseRows() {
      int var1 = 0;

      while(true) {
         ArrayRow[] var2 = this.mRows;
         if (var1 >= var2.length) {
            return;
         }

         ArrayRow var3 = var2[var1];
         if (var3 != null) {
            this.mCache.arrayRowPool.release(var3);
         }

         this.mRows[var1] = null;
         ++var1;
      }
   }

   private void updateRowFromVariables(ArrayRow var1) {
      if (this.mNumRows > 0) {
         var1.variables.updateFromSystem(var1, this.mRows);
         if (var1.variables.currentSize == 0) {
            var1.isSimpleDefinition = true;
         }
      }
   }

   public void addCentering(SolverVariable var1, SolverVariable var2, int var3, float var4, SolverVariable var5, SolverVariable var6, int var7, int var8) {
      ArrayRow var9 = this.createRow();
      var9.createRowCentering(var1, var2, var3, var4, var5, var6, var7);
      var1 = this.createErrorVariable();
      var2 = this.createErrorVariable();
      var1.strength = var8;
      var2.strength = var8;
      var9.addError(var1, var2);
      this.addConstraint(var9);
   }

   public void addConstraint(ArrayRow var1) {
      if (var1 != null) {
         if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
         }

         if (!var1.isSimpleDefinition) {
            this.updateRowFromVariables(var1);
            var1.ensurePositiveConstant();
            var1.pickRowVariable();
            if (!var1.hasKeyVariable()) {
               return;
            }
         }

         if (this.mRows[this.mNumRows] != null) {
            this.mCache.arrayRowPool.release(this.mRows[this.mNumRows]);
         }

         if (!var1.isSimpleDefinition) {
            var1.updateClientEquations();
         }

         this.mRows[this.mNumRows] = var1;
         SolverVariable var4 = var1.variable;
         int var2 = this.mNumRows;
         var4.definitionId = var2;
         this.mNumRows = var2 + 1;
         int var3 = var1.variable.mClientEquationsCount;
         if (var3 > 0) {
            while(true) {
               ArrayRow[] var6 = this.tempClientsCopy;
               if (var6.length >= var3) {
                  var6 = this.tempClientsCopy;

                  for(var2 = 0; var2 < var3; ++var2) {
                     var6[var2] = var1.variable.mClientEquations[var2];
                  }

                  for(var2 = 0; var2 < var3; ++var2) {
                     ArrayRow var5 = var6[var2];
                     if (var5 != var1) {
                        var5.variables.updateFromRow(var5, var1);
                        var5.updateClientEquations();
                     }
                  }

                  return;
               }

               this.tempClientsCopy = new ArrayRow[var6.length * 2];
            }
         }
      }
   }

   public ArrayRow addEquality(SolverVariable var1, SolverVariable var2, int var3, int var4) {
      ArrayRow var5 = this.createRow();
      var5.createRowEquals(var1, var2, var3);
      var1 = this.createErrorVariable();
      var2 = this.createErrorVariable();
      var1.strength = var4;
      var2.strength = var4;
      var5.addError(var1, var2);
      this.addConstraint(var5);
      return var5;
   }

   public void addEquality(SolverVariable var1, int var2) {
      int var3 = var1.definitionId;
      ArrayRow var4;
      if (var1.definitionId != -1) {
         var4 = this.mRows[var3];
         if (var4.isSimpleDefinition) {
            var4.constantValue = (float)var2;
         } else {
            var4 = this.createRow();
            var4.createRowEquals(var1, var2);
            this.addConstraint(var4);
         }

      } else {
         var4 = this.createRow();
         var4.createRowDefinition(var1, var2);
         this.addConstraint(var4);
      }
   }

   public void addGreaterThan(SolverVariable var1, SolverVariable var2, int var3, int var4) {
      ArrayRow var5 = this.createRow();
      SolverVariable var6 = this.createSlackVariable();
      var6.strength = var4;
      var5.createRowGreaterThan(var1, var2, var6, var3);
      this.addConstraint(var5);
   }

   public void addLowerThan(SolverVariable var1, SolverVariable var2, int var3, int var4) {
      ArrayRow var5 = this.createRow();
      SolverVariable var6 = this.createSlackVariable();
      var6.strength = var4;
      var5.createRowLowerThan(var1, var2, var6, var3);
      this.addConstraint(var5);
   }

   public SolverVariable createErrorVariable() {
      if (this.mNumColumns + 1 >= this.mMaxColumns) {
         this.increaseTableSize();
      }

      SolverVariable var1 = this.acquireSolverVariable(SolverVariable.Type.ERROR);
      ++this.mVariablesID;
      ++this.mNumColumns;
      var1.field_41 = this.mVariablesID;
      this.mCache.mIndexedVariables[this.mVariablesID] = var1;
      return var1;
   }

   public SolverVariable createObjectVariable(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
         }

         if (var1 instanceof ConstraintAnchor) {
            SolverVariable var2 = ((ConstraintAnchor)var1).getSolverVariable();
            SolverVariable var3;
            if (var2 == null) {
               ((ConstraintAnchor)var1).resetSolverVariable(this.mCache);
               var3 = ((ConstraintAnchor)var1).getSolverVariable();
            } else {
               var3 = var2;
            }

            if (var3.field_41 != -1 && var3.field_41 <= this.mVariablesID && this.mCache.mIndexedVariables[var3.field_41] != null) {
               return var3;
            } else {
               if (var3.field_41 != -1) {
                  var3.reset();
               }

               ++this.mVariablesID;
               ++this.mNumColumns;
               var3.field_41 = this.mVariablesID;
               var3.mType = SolverVariable.Type.UNRESTRICTED;
               this.mCache.mIndexedVariables[this.mVariablesID] = var3;
               return var3;
            }
         } else {
            return null;
         }
      }
   }

   public ArrayRow createRow() {
      ArrayRow var1 = (ArrayRow)this.mCache.arrayRowPool.acquire();
      if (var1 == null) {
         return new ArrayRow(this.mCache);
      } else {
         var1.reset();
         return var1;
      }
   }

   public SolverVariable createSlackVariable() {
      if (this.mNumColumns + 1 >= this.mMaxColumns) {
         this.increaseTableSize();
      }

      SolverVariable var1 = this.acquireSolverVariable(SolverVariable.Type.SLACK);
      ++this.mVariablesID;
      ++this.mNumColumns;
      var1.field_41 = this.mVariablesID;
      this.mCache.mIndexedVariables[this.mVariablesID] = var1;
      return var1;
   }

   void displayReadableRows() {
      this.displaySolverVariables();
      String var2 = "";

      StringBuilder var3;
      for(int var1 = 0; var1 < this.mNumRows; ++var1) {
         var3 = new StringBuilder();
         var3.append(var2);
         var3.append(this.mRows[var1].toReadableString());
         var2 = var3.toString();
         var3 = new StringBuilder();
         var3.append(var2);
         var3.append("\n");
         var2 = var3.toString();
      }

      if (this.mGoal != null) {
         var3 = new StringBuilder();
         var3.append(var2);
         var3.append(this.mGoal);
         var3.append("\n");
         var2 = var3.toString();
      }

      System.out.println(var2);
   }

   void displaySystemInformations() {
      int var1 = 0;

      int var2;
      ArrayRow[] var4;
      for(var2 = 0; var2 < this.TABLE_SIZE; ++var2) {
         var4 = this.mRows;
         if (var4[var2] != null) {
            var1 += var4[var2].sizeInBytes();
         }
      }

      int var3 = 0;

      for(var2 = 0; var2 < this.mNumRows; ++var2) {
         var4 = this.mRows;
         if (var4[var2] != null) {
            var3 += var4[var2].sizeInBytes();
         }
      }

      PrintStream var6 = System.out;
      StringBuilder var5 = new StringBuilder();
      var5.append("Linear System -> Table size: ");
      var5.append(this.TABLE_SIZE);
      var5.append(" (");
      var2 = this.TABLE_SIZE;
      var5.append(this.getDisplaySize(var2 * var2));
      var5.append(") -- row sizes: ");
      var5.append(this.getDisplaySize(var1));
      var5.append(", actual size: ");
      var5.append(this.getDisplaySize(var3));
      var5.append(" rows: ");
      var5.append(this.mNumRows);
      var5.append("/");
      var5.append(this.mMaxRows);
      var5.append(" cols: ");
      var5.append(this.mNumColumns);
      var5.append("/");
      var5.append(this.mMaxColumns);
      var5.append(" ");
      var5.append(0);
      var5.append(" occupied cells, ");
      var5.append(this.getDisplaySize(0));
      var6.println(var5.toString());
   }

   public void displayVariablesReadableRows() {
      this.displaySolverVariables();
      String var2 = "";

      StringBuilder var3;
      for(int var1 = 0; var1 < this.mNumRows; ++var1) {
         if (this.mRows[var1].variable.mType == SolverVariable.Type.UNRESTRICTED) {
            var3 = new StringBuilder();
            var3.append(var2);
            var3.append(this.mRows[var1].toReadableString());
            var2 = var3.toString();
            var3 = new StringBuilder();
            var3.append(var2);
            var3.append("\n");
            var2 = var3.toString();
         }
      }

      if (this.mGoal.variables.size() != 0) {
         var3 = new StringBuilder();
         var3.append(var2);
         var3.append(this.mGoal);
         var3.append("\n");
         var2 = var3.toString();
      }

      System.out.println(var2);
   }

   public Cache getCache() {
      return this.mCache;
   }

   Goal getGoal() {
      return this.mGoal;
   }

   public int getMemoryUsed() {
      int var2 = 0;

      for(int var1 = 0; var1 < this.mNumRows; ++var1) {
         ArrayRow[] var3 = this.mRows;
         if (var3[var1] != null) {
            var2 += var3[var1].sizeInBytes();
         }
      }

      return var2;
   }

   public int getNumEquations() {
      return this.mNumRows;
   }

   public int getNumVariables() {
      return this.mVariablesID;
   }

   public int getObjectVariableValue(Object var1) {
      SolverVariable var2 = ((ConstraintAnchor)var1).getSolverVariable();
      return var2 != null ? (int)(var2.computedValue + 0.5F) : 0;
   }

   ArrayRow getRow(int var1) {
      return this.mRows[var1];
   }

   float getValueFor(String var1) {
      SolverVariable var2 = this.getVariable(var1, SolverVariable.Type.UNRESTRICTED);
      return var2 == null ? 0.0F : var2.computedValue;
   }

   SolverVariable getVariable(String var1, SolverVariable.Type var2) {
      if (this.mVariables == null) {
         this.mVariables = new HashMap();
      }

      SolverVariable var3 = (SolverVariable)this.mVariables.get(var1);
      return var3 == null ? this.createVariable(var1, var2) : var3;
   }

   public void minimize() throws Exception {
      this.minimizeGoal(this.mGoal);
   }

   void minimizeGoal(Goal var1) throws Exception {
      var1.updateFromSystem(this);
      this.enforceBFS(var1);
      this.optimize(var1);
      this.computeValues();
   }

   void rebuildGoalFromErrors() {
      this.mGoal.updateFromSystem(this);
   }

   public void reset() {
      int var1;
      for(var1 = 0; var1 < this.mCache.mIndexedVariables.length; ++var1) {
         SolverVariable var2 = this.mCache.mIndexedVariables[var1];
         if (var2 != null) {
            var2.reset();
         }
      }

      this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
      this.mPoolVariablesCount = 0;
      Arrays.fill(this.mCache.mIndexedVariables, (Object)null);
      HashMap var3 = this.mVariables;
      if (var3 != null) {
         var3.clear();
      }

      this.mVariablesID = 0;
      this.mGoal.variables.clear();
      this.mNumColumns = 1;

      for(var1 = 0; var1 < this.mNumRows; ++var1) {
         this.mRows[var1].used = false;
      }

      this.releaseRows();
      this.mNumRows = 0;
   }
}
