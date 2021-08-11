/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver;

import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.Cache;
import android.support.constraint.solver.Goal;
import android.support.constraint.solver.Pools;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem {
    private static final boolean DEBUG = false;
    private static int POOL_SIZE = 1000;
    private int TABLE_SIZE;
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
    private HashMap<String, SolverVariable> mVariables = null;
    int mVariablesID = 0;
    private ArrayRow[] tempClientsCopy;

    public LinearSystem() {
        int n;
        this.mMaxColumns = n = (this.TABLE_SIZE = 32);
        this.mRows = null;
        this.mAlreadyTestedCandidates = new boolean[n];
        this.mNumColumns = 1;
        this.mNumRows = 0;
        this.mMaxRows = n;
        this.mPoolVariables = new SolverVariable[POOL_SIZE];
        this.mPoolVariablesCount = 0;
        this.tempClientsCopy = new ArrayRow[n];
        this.mRows = new ArrayRow[n];
        this.releaseRows();
        this.mCache = new Cache();
    }

    private SolverVariable acquireSolverVariable(SolverVariable.Type object) {
        SolverVariable[] arrsolverVariable = this.mCache.solverVariablePool.acquire();
        if (arrsolverVariable == null) {
            object = new SolverVariable((SolverVariable.Type)((Object)object));
        } else {
            arrsolverVariable.reset();
            arrsolverVariable.setType((SolverVariable.Type)((Object)object));
            object = arrsolverVariable;
        }
        int n = this.mPoolVariablesCount;
        int n2 = POOL_SIZE;
        if (n >= n2) {
            POOL_SIZE = n2 * 2;
            this.mPoolVariables = Arrays.copyOf(this.mPoolVariables, POOL_SIZE);
        }
        arrsolverVariable = this.mPoolVariables;
        n = this.mPoolVariablesCount;
        this.mPoolVariablesCount = n + 1;
        arrsolverVariable[n] = object;
        return object;
    }

    private void addError(ArrayRow arrayRow) {
        arrayRow.addError(this.createErrorVariable(), this.createErrorVariable());
    }

    private void addSingleError(ArrayRow arrayRow, int n) {
        arrayRow.addSingleError(this.createErrorVariable(), n);
    }

    private void computeValues() {
        for (int i = 0; i < this.mNumRows; ++i) {
            ArrayRow arrayRow = this.mRows[i];
            arrayRow.variable.computedValue = arrayRow.constantValue;
        }
    }

    public static ArrayRow createRowCentering(LinearSystem object, SolverVariable solverVariable, SolverVariable solverVariable2, int n, float f, SolverVariable solverVariable3, SolverVariable solverVariable4, int n2, boolean bl) {
        ArrayRow arrayRow = object.createRow();
        arrayRow.createRowCentering(solverVariable, solverVariable2, n, f, solverVariable3, solverVariable4, n2);
        if (bl) {
            solverVariable = object.createErrorVariable();
            object = object.createErrorVariable();
            solverVariable.strength = 4;
            object.strength = 4;
            arrayRow.addError(solverVariable, (SolverVariable)object);
            return arrayRow;
        }
        return arrayRow;
    }

    public static ArrayRow createRowDimensionPercent(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, float f, boolean bl) {
        ArrayRow arrayRow = linearSystem.createRow();
        if (bl) {
            linearSystem.addError(arrayRow);
        }
        return arrayRow.createRowDimensionPercent(solverVariable, solverVariable2, solverVariable3, f);
    }

    public static ArrayRow createRowEquals(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, int n, boolean bl) {
        ArrayRow arrayRow = linearSystem.createRow();
        arrayRow.createRowEquals(solverVariable, solverVariable2, n);
        if (bl) {
            linearSystem.addSingleError(arrayRow, 1);
            return arrayRow;
        }
        return arrayRow;
    }

    public static ArrayRow createRowGreaterThan(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, int n, boolean bl) {
        SolverVariable solverVariable3 = linearSystem.createSlackVariable();
        ArrayRow arrayRow = linearSystem.createRow();
        arrayRow.createRowGreaterThan(solverVariable, solverVariable2, solverVariable3, n);
        if (bl) {
            linearSystem.addSingleError(arrayRow, (int)(-1.0f * arrayRow.variables.get(solverVariable3)));
            return arrayRow;
        }
        return arrayRow;
    }

    public static ArrayRow createRowLowerThan(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, int n, boolean bl) {
        SolverVariable solverVariable3 = linearSystem.createSlackVariable();
        ArrayRow arrayRow = linearSystem.createRow();
        arrayRow.createRowLowerThan(solverVariable, solverVariable2, solverVariable3, n);
        if (bl) {
            linearSystem.addSingleError(arrayRow, (int)(-1.0f * arrayRow.variables.get(solverVariable3)));
            return arrayRow;
        }
        return arrayRow;
    }

    private SolverVariable createVariable(String string2, SolverVariable.Type object) {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        object = this.acquireSolverVariable((SolverVariable.Type)((Object)object));
        object.setName(string2);
        ++this.mVariablesID;
        ++this.mNumColumns;
        object.id = this.mVariablesID;
        if (this.mVariables == null) {
            this.mVariables = new HashMap();
        }
        this.mVariables.put(string2, (SolverVariable)object);
        this.mCache.mIndexedVariables[this.mVariablesID] = object;
        return object;
    }

    private void displayRows() {
        StringBuilder stringBuilder;
        this.displaySolverVariables();
        String string2 = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(this.mRows[i]);
            string2 = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("\n");
            string2 = stringBuilder.toString();
        }
        if (this.mGoal.variables.size() != 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(this.mGoal);
            stringBuilder.append("\n");
            string2 = stringBuilder.toString();
        }
        System.out.println(string2);
    }

    private void displaySolverVariables() {
        Object object;
        CharSequence charSequence = new StringBuilder();
        charSequence.append("Display Rows (");
        charSequence.append(this.mNumRows);
        charSequence.append("x");
        charSequence.append(this.mNumColumns);
        charSequence.append(") :\n\t | C | ");
        charSequence = charSequence.toString();
        for (int i = 1; i <= this.mNumColumns; ++i) {
            object = this.mCache.mIndexedVariables[i];
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)charSequence);
            stringBuilder.append(object);
            charSequence = stringBuilder.toString();
            object = new StringBuilder();
            object.append((String)charSequence);
            object.append(" | ");
            charSequence = object.toString();
        }
        object = new StringBuilder();
        object.append((String)charSequence);
        object.append("\n");
        charSequence = object.toString();
        System.out.println((String)charSequence);
    }

    private int enforceBFS(Goal goal) throws Exception {
        int n;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        do {
            n = n3;
            if (n4 >= this.mNumRows) break;
            if (this.mRows[n4].variable.mType != SolverVariable.Type.UNRESTRICTED && this.mRows[n4].constantValue < 0.0f) {
                n = 1;
                break;
            }
            ++n4;
        } while (true);
        if (n != 0) {
            boolean bl = false;
            n4 = 0;
            while (!bl) {
                ArrayRow arrayRow;
                int n5 = n4 + 1;
                float f = Float.MAX_VALUE;
                n2 = 0;
                n = -1;
                n4 = -1;
                for (n3 = 0; n3 < this.mNumRows; ++n3) {
                    arrayRow = this.mRows[n3];
                    if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED || arrayRow.constantValue >= 0.0f) continue;
                    for (int i = 1; i < this.mNumColumns; ++i) {
                        int n6;
                        float f2;
                        int n7;
                        int n8;
                        SolverVariable solverVariable = this.mCache.mIndexedVariables[i];
                        float f3 = arrayRow.variables.get(solverVariable);
                        if (f3 <= 0.0f) {
                            f2 = f;
                            n8 = n2;
                            n7 = n;
                            n6 = n4;
                        } else {
                            int n9 = 0;
                            do {
                                f2 = f;
                                n8 = n2;
                                n7 = n;
                                n6 = n4;
                                if (n9 >= 6) break;
                                f2 = solverVariable.strengthVector[n9] / f3;
                                if (f2 < f && n9 == n2 || n9 > n2) {
                                    f = f2;
                                    n = n3;
                                    n4 = i;
                                    n2 = n9;
                                }
                                ++n9;
                            } while (true);
                        }
                        f = f2;
                        n2 = n8;
                        n = n7;
                        n4 = n6;
                    }
                }
                if (n != -1) {
                    arrayRow = this.mRows[n];
                    arrayRow.variable.definitionId = -1;
                    arrayRow.pivot(this.mCache.mIndexedVariables[n4]);
                    arrayRow.variable.definitionId = n;
                    for (n4 = 0; n4 < this.mNumRows; ++n4) {
                        this.mRows[n4].updateRowWithEquation(arrayRow);
                    }
                    goal.updateFromSystem(this);
                } else {
                    bl = true;
                }
                n4 = n5;
            }
        } else {
            n4 = n2;
        }
        for (n = 0; n < this.mNumRows; ++n) {
            if (this.mRows[n].variable.mType == SolverVariable.Type.UNRESTRICTED || this.mRows[n].constantValue >= 0.0f) continue;
            return n4;
        }
        return n4;
    }

    private String getDisplaySize(int n) {
        int n2 = n * 4 / 1024 / 1024;
        if (n2 > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(n2);
            stringBuilder.append(" Mb");
            return stringBuilder.toString();
        }
        n2 = n * 4 / 1024;
        if (n2 > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(n2);
            stringBuilder.append(" Kb");
            return stringBuilder.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(n * 4);
        stringBuilder.append(" bytes");
        return stringBuilder.toString();
    }

    private void increaseTableSize() {
        this.TABLE_SIZE *= 2;
        this.mRows = Arrays.copyOf(this.mRows, this.TABLE_SIZE);
        Cache cache = this.mCache;
        cache.mIndexedVariables = Arrays.copyOf(cache.mIndexedVariables, this.TABLE_SIZE);
        int n = this.TABLE_SIZE;
        this.mAlreadyTestedCandidates = new boolean[n];
        this.mMaxColumns = n;
        this.mMaxRows = n;
        this.mGoal.variables.clear();
    }

    private int optimize(Goal goal) {
        int n;
        int n2 = 0;
        int n3 = 0;
        for (n = 0; n < this.mNumColumns; ++n) {
            this.mAlreadyTestedCandidates[n] = false;
        }
        int n4 = 0;
        n = n2;
        while (n == 0) {
            int n5 = n3 + 1;
            SolverVariable solverVariable = goal.getPivotCandidate();
            if (solverVariable != null) {
                if (this.mAlreadyTestedCandidates[solverVariable.id]) {
                    solverVariable = null;
                } else {
                    this.mAlreadyTestedCandidates[solverVariable.id] = true;
                    if (++n4 >= this.mNumColumns) {
                        n = 1;
                    }
                }
            }
            if (solverVariable != null) {
                ArrayRow arrayRow;
                float f = Float.MAX_VALUE;
                n2 = -1;
                for (n3 = 0; n3 < this.mNumRows; ++n3) {
                    float f2;
                    arrayRow = this.mRows[n3];
                    if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED || !arrayRow.hasVariable(solverVariable) || (f2 = arrayRow.variables.get(solverVariable)) >= 0.0f || (f2 = (- arrayRow.constantValue) / f2) >= f) continue;
                    f = f2;
                    n2 = n3;
                }
                if (n2 > -1) {
                    arrayRow = this.mRows[n2];
                    arrayRow.variable.definitionId = -1;
                    arrayRow.pivot(solverVariable);
                    arrayRow.variable.definitionId = n2;
                    for (n3 = 0; n3 < this.mNumRows; ++n3) {
                        this.mRows[n3].updateRowWithEquation(arrayRow);
                    }
                    goal.updateFromSystem(this);
                    try {
                        this.enforceBFS(goal);
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else {
                    n = 1;
                }
            } else {
                n = 1;
            }
            n3 = n5;
        }
        return n3;
    }

    private void releaseRows() {
        Object object;
        for (int i = 0; i < (object = this.mRows).length; ++i) {
            if ((object = object[i]) != null) {
                this.mCache.arrayRowPool.release((ArrayRow)object);
            }
            this.mRows[i] = null;
        }
    }

    private void updateRowFromVariables(ArrayRow arrayRow) {
        if (this.mNumRows > 0) {
            arrayRow.variables.updateFromSystem(arrayRow, this.mRows);
            if (arrayRow.variables.currentSize == 0) {
                arrayRow.isSimpleDefinition = true;
                return;
            }
            return;
        }
    }

    public void addCentering(SolverVariable solverVariable, SolverVariable solverVariable2, int n, float f, SolverVariable solverVariable3, SolverVariable solverVariable4, int n2, int n3) {
        ArrayRow arrayRow = this.createRow();
        arrayRow.createRowCentering(solverVariable, solverVariable2, n, f, solverVariable3, solverVariable4, n2);
        solverVariable = this.createErrorVariable();
        solverVariable2 = this.createErrorVariable();
        solverVariable.strength = n3;
        solverVariable2.strength = n3;
        arrayRow.addError(solverVariable, solverVariable2);
        this.addConstraint(arrayRow);
    }

    public void addConstraint(ArrayRow arrayRow) {
        int n;
        if (arrayRow == null) {
            return;
        }
        if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        if (!arrayRow.isSimpleDefinition) {
            this.updateRowFromVariables(arrayRow);
            arrayRow.ensurePositiveConstant();
            arrayRow.pickRowVariable();
            if (!arrayRow.hasKeyVariable()) {
                return;
            }
        }
        if (this.mRows[this.mNumRows] != null) {
            this.mCache.arrayRowPool.release(this.mRows[this.mNumRows]);
        }
        if (!arrayRow.isSimpleDefinition) {
            arrayRow.updateClientEquations();
        }
        this.mRows[this.mNumRows] = arrayRow;
        ArrayRow[] arrarrayRow = arrayRow.variable;
        arrarrayRow.definitionId = n = this.mNumRows;
        this.mNumRows = n + 1;
        int n2 = arrayRow.variable.mClientEquationsCount;
        if (n2 > 0) {
            while ((arrarrayRow = this.tempClientsCopy).length < n2) {
                this.tempClientsCopy = new ArrayRow[arrarrayRow.length * 2];
            }
            arrarrayRow = this.tempClientsCopy;
            for (n = 0; n < n2; ++n) {
                arrarrayRow[n] = arrayRow.variable.mClientEquations[n];
            }
            for (n = 0; n < n2; ++n) {
                ArrayRow arrayRow2 = arrarrayRow[n];
                if (arrayRow2 == arrayRow) continue;
                arrayRow2.variables.updateFromRow(arrayRow2, arrayRow);
                arrayRow2.updateClientEquations();
            }
            return;
        }
    }

    public ArrayRow addEquality(SolverVariable solverVariable, SolverVariable solverVariable2, int n, int n2) {
        ArrayRow arrayRow = this.createRow();
        arrayRow.createRowEquals(solverVariable, solverVariable2, n);
        solverVariable = this.createErrorVariable();
        solverVariable2 = this.createErrorVariable();
        solverVariable.strength = n2;
        solverVariable2.strength = n2;
        arrayRow.addError(solverVariable, solverVariable2);
        this.addConstraint(arrayRow);
        return arrayRow;
    }

    public void addEquality(SolverVariable solverVariable, int n) {
        int n2 = solverVariable.definitionId;
        if (solverVariable.definitionId != -1) {
            ArrayRow arrayRow = this.mRows[n2];
            if (arrayRow.isSimpleDefinition) {
                arrayRow.constantValue = n;
            } else {
                arrayRow = this.createRow();
                arrayRow.createRowEquals(solverVariable, n);
                this.addConstraint(arrayRow);
            }
            return;
        }
        ArrayRow arrayRow = this.createRow();
        arrayRow.createRowDefinition(solverVariable, n);
        this.addConstraint(arrayRow);
    }

    public void addGreaterThan(SolverVariable solverVariable, SolverVariable solverVariable2, int n, int n2) {
        ArrayRow arrayRow = this.createRow();
        SolverVariable solverVariable3 = this.createSlackVariable();
        solverVariable3.strength = n2;
        arrayRow.createRowGreaterThan(solverVariable, solverVariable2, solverVariable3, n);
        this.addConstraint(arrayRow);
    }

    public void addLowerThan(SolverVariable solverVariable, SolverVariable solverVariable2, int n, int n2) {
        ArrayRow arrayRow = this.createRow();
        SolverVariable solverVariable3 = this.createSlackVariable();
        solverVariable3.strength = n2;
        arrayRow.createRowLowerThan(solverVariable, solverVariable2, solverVariable3, n);
        this.addConstraint(arrayRow);
    }

    public SolverVariable createErrorVariable() {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        SolverVariable solverVariable = this.acquireSolverVariable(SolverVariable.Type.ERROR);
        ++this.mVariablesID;
        ++this.mNumColumns;
        solverVariable.id = this.mVariablesID;
        this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
        return solverVariable;
    }

    public SolverVariable createObjectVariable(Object object) {
        if (object == null) {
            return null;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        if (object instanceof ConstraintAnchor) {
            SolverVariable solverVariable = ((ConstraintAnchor)object).getSolverVariable();
            if (solverVariable == null) {
                ((ConstraintAnchor)object).resetSolverVariable(this.mCache);
                object = ((ConstraintAnchor)object).getSolverVariable();
            } else {
                object = solverVariable;
            }
            if (object.id != -1 && object.id <= this.mVariablesID && this.mCache.mIndexedVariables[object.id] != null) {
                return object;
            }
            if (object.id != -1) {
                object.reset();
            }
            ++this.mVariablesID;
            ++this.mNumColumns;
            object.id = this.mVariablesID;
            object.mType = SolverVariable.Type.UNRESTRICTED;
            this.mCache.mIndexedVariables[this.mVariablesID] = object;
            return object;
        }
        return null;
    }

    public ArrayRow createRow() {
        ArrayRow arrayRow = this.mCache.arrayRowPool.acquire();
        if (arrayRow == null) {
            return new ArrayRow(this.mCache);
        }
        arrayRow.reset();
        return arrayRow;
    }

    public SolverVariable createSlackVariable() {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        SolverVariable solverVariable = this.acquireSolverVariable(SolverVariable.Type.SLACK);
        ++this.mVariablesID;
        ++this.mNumColumns;
        solverVariable.id = this.mVariablesID;
        this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
        return solverVariable;
    }

    void displayReadableRows() {
        StringBuilder stringBuilder;
        this.displaySolverVariables();
        String string2 = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(this.mRows[i].toReadableString());
            string2 = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("\n");
            string2 = stringBuilder.toString();
        }
        if (this.mGoal != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(this.mGoal);
            stringBuilder.append("\n");
            string2 = stringBuilder.toString();
        }
        System.out.println(string2);
    }

    void displaySystemInformations() {
        ArrayRow[] arrarrayRow;
        int n;
        int n2 = 0;
        for (n = 0; n < this.TABLE_SIZE; ++n) {
            arrarrayRow = this.mRows;
            if (arrarrayRow[n] == null) continue;
            n2 += arrarrayRow[n].sizeInBytes();
        }
        int n3 = 0;
        for (n = 0; n < this.mNumRows; ++n) {
            arrarrayRow = this.mRows;
            if (arrarrayRow[n] == null) continue;
            n3 += arrarrayRow[n].sizeInBytes();
        }
        arrarrayRow = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Linear System -> Table size: ");
        stringBuilder.append(this.TABLE_SIZE);
        stringBuilder.append(" (");
        n = this.TABLE_SIZE;
        stringBuilder.append(this.getDisplaySize(n * n));
        stringBuilder.append(") -- row sizes: ");
        stringBuilder.append(this.getDisplaySize(n2));
        stringBuilder.append(", actual size: ");
        stringBuilder.append(this.getDisplaySize(n3));
        stringBuilder.append(" rows: ");
        stringBuilder.append(this.mNumRows);
        stringBuilder.append("/");
        stringBuilder.append(this.mMaxRows);
        stringBuilder.append(" cols: ");
        stringBuilder.append(this.mNumColumns);
        stringBuilder.append("/");
        stringBuilder.append(this.mMaxColumns);
        stringBuilder.append(" ");
        stringBuilder.append(0);
        stringBuilder.append(" occupied cells, ");
        stringBuilder.append(this.getDisplaySize(0));
        arrarrayRow.println(stringBuilder.toString());
    }

    public void displayVariablesReadableRows() {
        StringBuilder stringBuilder;
        this.displaySolverVariables();
        String string2 = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            if (this.mRows[i].variable.mType != SolverVariable.Type.UNRESTRICTED) continue;
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(this.mRows[i].toReadableString());
            string2 = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("\n");
            string2 = stringBuilder.toString();
        }
        if (this.mGoal.variables.size() != 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(this.mGoal);
            stringBuilder.append("\n");
            string2 = stringBuilder.toString();
        }
        System.out.println(string2);
    }

    public Cache getCache() {
        return this.mCache;
    }

    Goal getGoal() {
        return this.mGoal;
    }

    public int getMemoryUsed() {
        int n = 0;
        for (int i = 0; i < this.mNumRows; ++i) {
            ArrayRow[] arrarrayRow = this.mRows;
            if (arrarrayRow[i] == null) continue;
            n += arrarrayRow[i].sizeInBytes();
        }
        return n;
    }

    public int getNumEquations() {
        return this.mNumRows;
    }

    public int getNumVariables() {
        return this.mVariablesID;
    }

    public int getObjectVariableValue(Object object) {
        if ((object = ((ConstraintAnchor)object).getSolverVariable()) != null) {
            return (int)(object.computedValue + 0.5f);
        }
        return 0;
    }

    ArrayRow getRow(int n) {
        return this.mRows[n];
    }

    float getValueFor(String object) {
        if ((object = this.getVariable((String)object, SolverVariable.Type.UNRESTRICTED)) == null) {
            return 0.0f;
        }
        return object.computedValue;
    }

    SolverVariable getVariable(String string2, SolverVariable.Type type) {
        SolverVariable solverVariable;
        if (this.mVariables == null) {
            this.mVariables = new HashMap();
        }
        if ((solverVariable = this.mVariables.get(string2)) == null) {
            return this.createVariable(string2, type);
        }
        return solverVariable;
    }

    public void minimize() throws Exception {
        this.minimizeGoal(this.mGoal);
    }

    void minimizeGoal(Goal goal) throws Exception {
        goal.updateFromSystem(this);
        this.enforceBFS(goal);
        this.optimize(goal);
        this.computeValues();
    }

    void rebuildGoalFromErrors() {
        this.mGoal.updateFromSystem(this);
    }

    public void reset() {
        Object object;
        int n;
        for (n = 0; n < this.mCache.mIndexedVariables.length; ++n) {
            object = this.mCache.mIndexedVariables[n];
            if (object == null) continue;
            object.reset();
        }
        this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
        this.mPoolVariablesCount = 0;
        Arrays.fill(this.mCache.mIndexedVariables, null);
        object = this.mVariables;
        if (object != null) {
            object.clear();
        }
        this.mVariablesID = 0;
        this.mGoal.variables.clear();
        this.mNumColumns = 1;
        for (n = 0; n < this.mNumRows; ++n) {
            this.mRows[n].used = false;
        }
        this.releaseRows();
        this.mNumRows = 0;
    }
}

