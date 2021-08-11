// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver;

import java.io.PrintStream;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem
{
    private static final boolean DEBUG = false;
    private static int POOL_SIZE;
    private int TABLE_SIZE;
    private boolean[] mAlreadyTestedCandidates;
    final Cache mCache;
    private Goal mGoal;
    private int mMaxColumns;
    private int mMaxRows;
    int mNumColumns;
    private int mNumRows;
    private SolverVariable[] mPoolVariables;
    private int mPoolVariablesCount;
    private ArrayRow[] mRows;
    private HashMap<String, SolverVariable> mVariables;
    int mVariablesID;
    private ArrayRow[] tempClientsCopy;
    
    static {
        LinearSystem.POOL_SIZE = 1000;
    }
    
    public LinearSystem() {
        this.mVariablesID = 0;
        this.mVariables = null;
        this.mGoal = new Goal();
        this.TABLE_SIZE = 32;
        final int table_SIZE = this.TABLE_SIZE;
        this.mMaxColumns = table_SIZE;
        this.mRows = null;
        this.mAlreadyTestedCandidates = new boolean[table_SIZE];
        this.mNumColumns = 1;
        this.mNumRows = 0;
        this.mMaxRows = table_SIZE;
        this.mPoolVariables = new SolverVariable[LinearSystem.POOL_SIZE];
        this.mPoolVariablesCount = 0;
        this.tempClientsCopy = new ArrayRow[table_SIZE];
        this.mRows = new ArrayRow[table_SIZE];
        this.releaseRows();
        this.mCache = new Cache();
    }
    
    private SolverVariable acquireSolverVariable(final SolverVariable.Type type) {
        final SolverVariable solverVariable = this.mCache.solverVariablePool.acquire();
        SolverVariable solverVariable2;
        if (solverVariable == null) {
            solverVariable2 = new SolverVariable(type);
        }
        else {
            solverVariable.reset();
            solverVariable.setType(type);
            solverVariable2 = solverVariable;
        }
        final int mPoolVariablesCount = this.mPoolVariablesCount;
        final int pool_SIZE = LinearSystem.POOL_SIZE;
        if (mPoolVariablesCount >= pool_SIZE) {
            LinearSystem.POOL_SIZE = pool_SIZE * 2;
            this.mPoolVariables = Arrays.copyOf(this.mPoolVariables, LinearSystem.POOL_SIZE);
        }
        return this.mPoolVariables[this.mPoolVariablesCount++] = solverVariable2;
    }
    
    private void addError(final ArrayRow arrayRow) {
        arrayRow.addError(this.createErrorVariable(), this.createErrorVariable());
    }
    
    private void addSingleError(final ArrayRow arrayRow, final int n) {
        arrayRow.addSingleError(this.createErrorVariable(), n);
    }
    
    private void computeValues() {
        for (int i = 0; i < this.mNumRows; ++i) {
            final ArrayRow arrayRow = this.mRows[i];
            arrayRow.variable.computedValue = arrayRow.constantValue;
        }
    }
    
    public static ArrayRow createRowCentering(final LinearSystem linearSystem, SolverVariable errorVariable, final SolverVariable solverVariable, final int n, final float n2, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final int n3, final boolean b) {
        final ArrayRow row = linearSystem.createRow();
        row.createRowCentering(errorVariable, solverVariable, n, n2, solverVariable2, solverVariable3, n3);
        if (b) {
            errorVariable = linearSystem.createErrorVariable();
            final SolverVariable errorVariable2 = linearSystem.createErrorVariable();
            errorVariable.strength = 4;
            errorVariable2.strength = 4;
            row.addError(errorVariable, errorVariable2);
            return row;
        }
        return row;
    }
    
    public static ArrayRow createRowDimensionPercent(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final float n, final boolean b) {
        final ArrayRow row = linearSystem.createRow();
        if (b) {
            linearSystem.addError(row);
        }
        return row.createRowDimensionPercent(solverVariable, solverVariable2, solverVariable3, n);
    }
    
    public static ArrayRow createRowEquals(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final boolean b) {
        final ArrayRow row = linearSystem.createRow();
        row.createRowEquals(solverVariable, solverVariable2, n);
        if (b) {
            linearSystem.addSingleError(row, 1);
            return row;
        }
        return row;
    }
    
    public static ArrayRow createRowGreaterThan(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final boolean b) {
        final SolverVariable slackVariable = linearSystem.createSlackVariable();
        final ArrayRow row = linearSystem.createRow();
        row.createRowGreaterThan(solverVariable, solverVariable2, slackVariable, n);
        if (b) {
            linearSystem.addSingleError(row, (int)(-1.0f * row.variables.get(slackVariable)));
            return row;
        }
        return row;
    }
    
    public static ArrayRow createRowLowerThan(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final boolean b) {
        final SolverVariable slackVariable = linearSystem.createSlackVariable();
        final ArrayRow row = linearSystem.createRow();
        row.createRowLowerThan(solverVariable, solverVariable2, slackVariable, n);
        if (b) {
            linearSystem.addSingleError(row, (int)(-1.0f * row.variables.get(slackVariable)));
            return row;
        }
        return row;
    }
    
    private SolverVariable createVariable(final String name, final SolverVariable.Type type) {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(type);
        acquireSolverVariable.setName(name);
        ++this.mVariablesID;
        ++this.mNumColumns;
        acquireSolverVariable.id = this.mVariablesID;
        if (this.mVariables == null) {
            this.mVariables = new HashMap<String, SolverVariable>();
        }
        this.mVariables.put(name, acquireSolverVariable);
        return this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
    }
    
    private void displayRows() {
        this.displaySolverVariables();
        String s = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(this.mRows[i]);
            final String string = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string);
            sb2.append("\n");
            s = sb2.toString();
        }
        if (this.mGoal.variables.size() != 0) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(this.mGoal);
            sb3.append("\n");
            s = sb3.toString();
        }
        System.out.println(s);
    }
    
    private void displaySolverVariables() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Display Rows (");
        sb.append(this.mNumRows);
        sb.append("x");
        sb.append(this.mNumColumns);
        sb.append(") :\n\t | C | ");
        String s = sb.toString();
        for (int i = 1; i <= this.mNumColumns; ++i) {
            final SolverVariable solverVariable = this.mCache.mIndexedVariables[i];
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(solverVariable);
            final String string = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string);
            sb3.append(" | ");
            s = sb3.toString();
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(s);
        sb4.append("\n");
        System.out.println(sb4.toString());
    }
    
    private int enforceBFS(final Goal goal) throws Exception {
        final int n = 0;
        final boolean b = false;
        int n2 = 0;
        boolean b2;
        while (true) {
            b2 = b;
            if (n2 >= this.mNumRows) {
                break;
            }
            if (this.mRows[n2].variable.mType != SolverVariable.Type.UNRESTRICTED) {
                if (this.mRows[n2].constantValue < 0.0f) {
                    b2 = true;
                    break;
                }
            }
            ++n2;
        }
        int n3;
        if (b2) {
            int i = 0;
            n3 = 0;
            while (i == 0) {
                final int n4 = n3 + 1;
                float n5 = Float.MAX_VALUE;
                int n6 = 0;
                int definitionId = -1;
                int n7 = -1;
                for (int j = 0; j < this.mNumRows; ++j) {
                    final ArrayRow arrayRow = this.mRows[j];
                    if (arrayRow.variable.mType != SolverVariable.Type.UNRESTRICTED) {
                        if (arrayRow.constantValue < 0.0f) {
                            float n8;
                            int n9;
                            int n10;
                            int n11;
                            for (int k = 1; k < this.mNumColumns; ++k, n5 = n8, n6 = n9, definitionId = n10, n7 = n11) {
                                final SolverVariable solverVariable = this.mCache.mIndexedVariables[k];
                                final float value = arrayRow.variables.get(solverVariable);
                                if (value <= 0.0f) {
                                    n8 = n5;
                                    n9 = n6;
                                    n10 = definitionId;
                                    n11 = n7;
                                }
                                else {
                                    int n12 = 0;
                                    while (true) {
                                        n8 = n5;
                                        n9 = n6;
                                        n10 = definitionId;
                                        n11 = n7;
                                        if (n12 >= 6) {
                                            break;
                                        }
                                        final float n13 = solverVariable.strengthVector[n12] / value;
                                        if ((n13 < n5 && n12 == n6) || n12 > n6) {
                                            n5 = n13;
                                            definitionId = j;
                                            n7 = k;
                                            n6 = n12;
                                        }
                                        ++n12;
                                    }
                                }
                            }
                        }
                    }
                }
                if (definitionId != -1) {
                    final ArrayRow arrayRow2 = this.mRows[definitionId];
                    arrayRow2.variable.definitionId = -1;
                    arrayRow2.pivot(this.mCache.mIndexedVariables[n7]);
                    arrayRow2.variable.definitionId = definitionId;
                    for (int l = 0; l < this.mNumRows; ++l) {
                        this.mRows[l].updateRowWithEquation(arrayRow2);
                    }
                    goal.updateFromSystem(this);
                }
                else {
                    i = 1;
                }
                n3 = n4;
            }
        }
        else {
            n3 = n;
        }
        for (int n14 = 0; n14 < this.mNumRows; ++n14) {
            if (this.mRows[n14].variable.mType != SolverVariable.Type.UNRESTRICTED) {
                if (this.mRows[n14].constantValue < 0.0f) {
                    return n3;
                }
            }
        }
        return n3;
    }
    
    private String getDisplaySize(final int n) {
        final int n2 = n * 4 / 1024 / 1024;
        if (n2 > 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(n2);
            sb.append(" Mb");
            return sb.toString();
        }
        final int n3 = n * 4 / 1024;
        if (n3 > 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(n3);
            sb2.append(" Kb");
            return sb2.toString();
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("");
        sb3.append(n * 4);
        sb3.append(" bytes");
        return sb3.toString();
    }
    
    private void increaseTableSize() {
        this.TABLE_SIZE *= 2;
        this.mRows = Arrays.copyOf(this.mRows, this.TABLE_SIZE);
        final Cache mCache = this.mCache;
        mCache.mIndexedVariables = Arrays.copyOf(mCache.mIndexedVariables, this.TABLE_SIZE);
        final int table_SIZE = this.TABLE_SIZE;
        this.mAlreadyTestedCandidates = new boolean[table_SIZE];
        this.mMaxColumns = table_SIZE;
        this.mMaxRows = table_SIZE;
        this.mGoal.variables.clear();
    }
    
    private int optimize(final Goal goal) {
        final boolean b = false;
        int n = 0;
        for (int i = 0; i < this.mNumColumns; ++i) {
            this.mAlreadyTestedCandidates[i] = false;
        }
        int n2 = 0;
        int j = b ? 1 : 0;
        while (j == 0) {
            final int n3 = n + 1;
            SolverVariable pivotCandidate = goal.getPivotCandidate();
            if (pivotCandidate != null) {
                if (this.mAlreadyTestedCandidates[pivotCandidate.id]) {
                    pivotCandidate = null;
                }
                else {
                    this.mAlreadyTestedCandidates[pivotCandidate.id] = true;
                    ++n2;
                    if (n2 >= this.mNumColumns) {
                        j = 1;
                    }
                }
            }
            if (pivotCandidate != null) {
                float n4 = Float.MAX_VALUE;
                int definitionId = -1;
                for (int k = 0; k < this.mNumRows; ++k) {
                    final ArrayRow arrayRow = this.mRows[k];
                    if (arrayRow.variable.mType != SolverVariable.Type.UNRESTRICTED) {
                        if (arrayRow.hasVariable(pivotCandidate)) {
                            final float value = arrayRow.variables.get(pivotCandidate);
                            if (value < 0.0f) {
                                final float n5 = -arrayRow.constantValue / value;
                                if (n5 < n4) {
                                    n4 = n5;
                                    definitionId = k;
                                }
                            }
                        }
                    }
                }
                if (definitionId > -1) {
                    final ArrayRow arrayRow2 = this.mRows[definitionId];
                    arrayRow2.variable.definitionId = -1;
                    arrayRow2.pivot(pivotCandidate);
                    arrayRow2.variable.definitionId = definitionId;
                    for (int l = 0; l < this.mNumRows; ++l) {
                        this.mRows[l].updateRowWithEquation(arrayRow2);
                    }
                    goal.updateFromSystem(this);
                    try {
                        this.enforceBFS(goal);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    j = 1;
                }
            }
            else {
                j = 1;
            }
            n = n3;
        }
        return n;
    }
    
    private void releaseRows() {
        int n = 0;
        while (true) {
            final ArrayRow[] mRows = this.mRows;
            if (n >= mRows.length) {
                break;
            }
            final ArrayRow arrayRow = mRows[n];
            if (arrayRow != null) {
                this.mCache.arrayRowPool.release(arrayRow);
            }
            this.mRows[n] = null;
            ++n;
        }
    }
    
    private void updateRowFromVariables(final ArrayRow arrayRow) {
        if (this.mNumRows <= 0) {
            return;
        }
        arrayRow.variables.updateFromSystem(arrayRow, this.mRows);
        if (arrayRow.variables.currentSize == 0) {
            arrayRow.isSimpleDefinition = true;
        }
    }
    
    public void addCentering(SolverVariable errorVariable, SolverVariable errorVariable2, final int n, final float n2, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n3, final int n4) {
        final ArrayRow row = this.createRow();
        row.createRowCentering(errorVariable, errorVariable2, n, n2, solverVariable, solverVariable2, n3);
        errorVariable = this.createErrorVariable();
        errorVariable2 = this.createErrorVariable();
        errorVariable.strength = n4;
        errorVariable2.strength = n4;
        row.addError(errorVariable, errorVariable2);
        this.addConstraint(row);
    }
    
    public void addConstraint(final ArrayRow arrayRow) {
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
        final SolverVariable variable = arrayRow.variable;
        final int mNumRows = this.mNumRows;
        variable.definitionId = mNumRows;
        this.mNumRows = mNumRows + 1;
        final int mClientEquationsCount = arrayRow.variable.mClientEquationsCount;
        if (mClientEquationsCount > 0) {
            while (true) {
                final ArrayRow[] tempClientsCopy = this.tempClientsCopy;
                if (tempClientsCopy.length >= mClientEquationsCount) {
                    break;
                }
                this.tempClientsCopy = new ArrayRow[tempClientsCopy.length * 2];
            }
            final ArrayRow[] tempClientsCopy2 = this.tempClientsCopy;
            for (int i = 0; i < mClientEquationsCount; ++i) {
                tempClientsCopy2[i] = arrayRow.variable.mClientEquations[i];
            }
            for (int j = 0; j < mClientEquationsCount; ++j) {
                final ArrayRow arrayRow2 = tempClientsCopy2[j];
                if (arrayRow2 != arrayRow) {
                    arrayRow2.variables.updateFromRow(arrayRow2, arrayRow);
                    arrayRow2.updateClientEquations();
                }
            }
        }
    }
    
    public ArrayRow addEquality(SolverVariable errorVariable, SolverVariable errorVariable2, final int n, final int n2) {
        final ArrayRow row = this.createRow();
        row.createRowEquals(errorVariable, errorVariable2, n);
        errorVariable = this.createErrorVariable();
        errorVariable2 = this.createErrorVariable();
        errorVariable.strength = n2;
        errorVariable2.strength = n2;
        row.addError(errorVariable, errorVariable2);
        this.addConstraint(row);
        return row;
    }
    
    public void addEquality(final SolverVariable solverVariable, final int n) {
        final int definitionId = solverVariable.definitionId;
        if (solverVariable.definitionId != -1) {
            final ArrayRow arrayRow = this.mRows[definitionId];
            if (arrayRow.isSimpleDefinition) {
                arrayRow.constantValue = (float)n;
            }
            else {
                final ArrayRow row = this.createRow();
                row.createRowEquals(solverVariable, n);
                this.addConstraint(row);
            }
            return;
        }
        final ArrayRow row2 = this.createRow();
        row2.createRowDefinition(solverVariable, n);
        this.addConstraint(row2);
    }
    
    public void addGreaterThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final int strength) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        slackVariable.strength = strength;
        row.createRowGreaterThan(solverVariable, solverVariable2, slackVariable, n);
        this.addConstraint(row);
    }
    
    public void addLowerThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final int strength) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        slackVariable.strength = strength;
        row.createRowLowerThan(solverVariable, solverVariable2, slackVariable, n);
        this.addConstraint(row);
    }
    
    public SolverVariable createErrorVariable() {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(SolverVariable.Type.ERROR);
        ++this.mVariablesID;
        ++this.mNumColumns;
        acquireSolverVariable.id = this.mVariablesID;
        return this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
    }
    
    public SolverVariable createObjectVariable(final Object o) {
        if (o == null) {
            return null;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        if (!(o instanceof ConstraintAnchor)) {
            return null;
        }
        final SolverVariable solverVariable = ((ConstraintAnchor)o).getSolverVariable();
        SolverVariable solverVariable2;
        if (solverVariable == null) {
            ((ConstraintAnchor)o).resetSolverVariable(this.mCache);
            solverVariable2 = ((ConstraintAnchor)o).getSolverVariable();
        }
        else {
            solverVariable2 = solverVariable;
        }
        if (solverVariable2.id != -1 && solverVariable2.id <= this.mVariablesID && this.mCache.mIndexedVariables[solverVariable2.id] != null) {
            return solverVariable2;
        }
        if (solverVariable2.id != -1) {
            solverVariable2.reset();
        }
        ++this.mVariablesID;
        ++this.mNumColumns;
        solverVariable2.id = this.mVariablesID;
        solverVariable2.mType = SolverVariable.Type.UNRESTRICTED;
        return this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable2;
    }
    
    public ArrayRow createRow() {
        final ArrayRow arrayRow = this.mCache.arrayRowPool.acquire();
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
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(SolverVariable.Type.SLACK);
        ++this.mVariablesID;
        ++this.mNumColumns;
        acquireSolverVariable.id = this.mVariablesID;
        return this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
    }
    
    void displayReadableRows() {
        this.displaySolverVariables();
        String s = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(this.mRows[i].toReadableString());
            final String string = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string);
            sb2.append("\n");
            s = sb2.toString();
        }
        if (this.mGoal != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(this.mGoal);
            sb3.append("\n");
            s = sb3.toString();
        }
        System.out.println(s);
    }
    
    void displaySystemInformations() {
        int n = 0;
        for (int i = 0; i < this.TABLE_SIZE; ++i) {
            final ArrayRow[] mRows = this.mRows;
            if (mRows[i] != null) {
                n += mRows[i].sizeInBytes();
            }
        }
        int n2 = 0;
        for (int j = 0; j < this.mNumRows; ++j) {
            final ArrayRow[] mRows2 = this.mRows;
            if (mRows2[j] != null) {
                n2 += mRows2[j].sizeInBytes();
            }
        }
        final PrintStream out = System.out;
        final StringBuilder sb = new StringBuilder();
        sb.append("Linear System -> Table size: ");
        sb.append(this.TABLE_SIZE);
        sb.append(" (");
        final int table_SIZE = this.TABLE_SIZE;
        sb.append(this.getDisplaySize(table_SIZE * table_SIZE));
        sb.append(") -- row sizes: ");
        sb.append(this.getDisplaySize(n));
        sb.append(", actual size: ");
        sb.append(this.getDisplaySize(n2));
        sb.append(" rows: ");
        sb.append(this.mNumRows);
        sb.append("/");
        sb.append(this.mMaxRows);
        sb.append(" cols: ");
        sb.append(this.mNumColumns);
        sb.append("/");
        sb.append(this.mMaxColumns);
        sb.append(" ");
        sb.append(0);
        sb.append(" occupied cells, ");
        sb.append(this.getDisplaySize(0));
        out.println(sb.toString());
    }
    
    public void displayVariablesReadableRows() {
        this.displaySolverVariables();
        String s = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            if (this.mRows[i].variable.mType == SolverVariable.Type.UNRESTRICTED) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(this.mRows[i].toReadableString());
                final String string = sb.toString();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string);
                sb2.append("\n");
                s = sb2.toString();
            }
        }
        if (this.mGoal.variables.size() != 0) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(this.mGoal);
            sb3.append("\n");
            s = sb3.toString();
        }
        System.out.println(s);
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
            final ArrayRow[] mRows = this.mRows;
            if (mRows[i] != null) {
                n += mRows[i].sizeInBytes();
            }
        }
        return n;
    }
    
    public int getNumEquations() {
        return this.mNumRows;
    }
    
    public int getNumVariables() {
        return this.mVariablesID;
    }
    
    public int getObjectVariableValue(final Object o) {
        final SolverVariable solverVariable = ((ConstraintAnchor)o).getSolverVariable();
        if (solverVariable != null) {
            return (int)(solverVariable.computedValue + 0.5f);
        }
        return 0;
    }
    
    ArrayRow getRow(final int n) {
        return this.mRows[n];
    }
    
    float getValueFor(final String s) {
        final SolverVariable variable = this.getVariable(s, SolverVariable.Type.UNRESTRICTED);
        if (variable == null) {
            return 0.0f;
        }
        return variable.computedValue;
    }
    
    SolverVariable getVariable(final String s, final SolverVariable.Type type) {
        if (this.mVariables == null) {
            this.mVariables = new HashMap<String, SolverVariable>();
        }
        final SolverVariable solverVariable = this.mVariables.get(s);
        if (solverVariable == null) {
            return this.createVariable(s, type);
        }
        return solverVariable;
    }
    
    public void minimize() throws Exception {
        this.minimizeGoal(this.mGoal);
    }
    
    void minimizeGoal(final Goal goal) throws Exception {
        goal.updateFromSystem(this);
        this.enforceBFS(goal);
        this.optimize(goal);
        this.computeValues();
    }
    
    void rebuildGoalFromErrors() {
        this.mGoal.updateFromSystem(this);
    }
    
    public void reset() {
        for (int i = 0; i < this.mCache.mIndexedVariables.length; ++i) {
            final SolverVariable solverVariable = this.mCache.mIndexedVariables[i];
            if (solverVariable != null) {
                solverVariable.reset();
            }
        }
        this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
        this.mPoolVariablesCount = 0;
        Arrays.fill(this.mCache.mIndexedVariables, null);
        final HashMap<String, SolverVariable> mVariables = this.mVariables;
        if (mVariables != null) {
            mVariables.clear();
        }
        this.mVariablesID = 0;
        this.mGoal.variables.clear();
        this.mNumColumns = 1;
        for (int j = 0; j < this.mNumRows; ++j) {
            this.mRows[j].used = false;
        }
        this.releaseRows();
        this.mNumRows = 0;
    }
}
