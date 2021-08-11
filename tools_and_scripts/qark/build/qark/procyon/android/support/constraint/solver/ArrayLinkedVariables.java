// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver;

import java.io.PrintStream;
import java.util.Arrays;

public class ArrayLinkedVariables
{
    private static final boolean DEBUG = false;
    private static final int NONE = -1;
    private int ROW_SIZE;
    private SolverVariable candidate;
    int currentSize;
    private int[] mArrayIndices;
    private int[] mArrayNextIndices;
    private float[] mArrayValues;
    private final Cache mCache;
    private boolean mDidFillOnce;
    private int mHead;
    private int mLast;
    private final ArrayRow mRow;
    
    ArrayLinkedVariables(final ArrayRow mRow, final Cache mCache) {
        this.currentSize = 0;
        this.ROW_SIZE = 8;
        this.candidate = null;
        final int row_SIZE = this.ROW_SIZE;
        this.mArrayIndices = new int[row_SIZE];
        this.mArrayNextIndices = new int[row_SIZE];
        this.mArrayValues = new float[row_SIZE];
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.mRow = mRow;
        this.mCache = mCache;
    }
    
    public final void add(final SolverVariable solverVariable, final float n) {
        if (n == 0.0f) {
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            final float[] mArrayValues = this.mArrayValues;
            final int mHead = this.mHead;
            mArrayValues[mHead] = n;
            this.mArrayIndices[mHead] = solverVariable.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                ++this.mLast;
            }
        }
        else {
            int mHead2 = this.mHead;
            int n2 = -1;
            int n3 = 0;
            while (mHead2 != -1 && n3 < this.currentSize) {
                final int n4 = this.mArrayIndices[mHead2];
                if (n4 == solverVariable.id) {
                    final float[] mArrayValues2 = this.mArrayValues;
                    mArrayValues2[mHead2] += n;
                    if (mArrayValues2[mHead2] == 0.0f) {
                        if (mHead2 == this.mHead) {
                            this.mHead = this.mArrayNextIndices[mHead2];
                        }
                        else {
                            final int[] mArrayNextIndices = this.mArrayNextIndices;
                            mArrayNextIndices[n2] = mArrayNextIndices[mHead2];
                        }
                        this.mCache.mIndexedVariables[n4].removeClientEquation(this.mRow);
                        if (this.mDidFillOnce) {
                            this.mLast = mHead2;
                        }
                        --this.currentSize;
                    }
                    return;
                }
                else {
                    if (this.mArrayIndices[mHead2] < solverVariable.id) {
                        n2 = mHead2;
                    }
                    mHead2 = this.mArrayNextIndices[mHead2];
                    ++n3;
                }
            }
            final int mLast = this.mLast;
            int mHead3 = mLast + 1;
            if (this.mDidFillOnce) {
                final int[] mArrayIndices = this.mArrayIndices;
                if (mArrayIndices[mLast] == -1) {
                    mHead3 = this.mLast;
                }
                else {
                    mHead3 = mArrayIndices.length;
                }
            }
            final int[] mArrayIndices2 = this.mArrayIndices;
            if (mHead3 >= mArrayIndices2.length) {
                if (this.currentSize < mArrayIndices2.length) {
                    int n5 = 0;
                    while (true) {
                        final int[] mArrayIndices3 = this.mArrayIndices;
                        if (n5 >= mArrayIndices3.length) {
                            break;
                        }
                        if (mArrayIndices3[n5] == -1) {
                            mHead3 = n5;
                            break;
                        }
                        ++n5;
                    }
                }
            }
            final int[] mArrayIndices4 = this.mArrayIndices;
            if (mHead3 >= mArrayIndices4.length) {
                mHead3 = mArrayIndices4.length;
                this.ROW_SIZE *= 2;
                this.mDidFillOnce = false;
                this.mLast = mHead3 - 1;
                this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
                this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
                this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
            }
            this.mArrayIndices[mHead3] = solverVariable.id;
            this.mArrayValues[mHead3] = n;
            if (n2 != -1) {
                final int[] mArrayNextIndices2 = this.mArrayNextIndices;
                mArrayNextIndices2[mHead3] = mArrayNextIndices2[n2];
                mArrayNextIndices2[n2] = mHead3;
            }
            else {
                this.mArrayNextIndices[mHead3] = this.mHead;
                this.mHead = mHead3;
            }
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                ++this.mLast;
            }
            final int mLast2 = this.mLast;
            final int[] mArrayIndices5 = this.mArrayIndices;
            if (mLast2 >= mArrayIndices5.length) {
                this.mDidFillOnce = true;
                this.mLast = mArrayIndices5.length - 1;
            }
        }
    }
    
    public final void clear() {
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.currentSize = 0;
    }
    
    final boolean containsKey(final SolverVariable solverVariable) {
        if (this.mHead == -1) {
            return false;
        }
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            if (this.mArrayIndices[mHead] == solverVariable.id) {
                return true;
            }
        }
        return false;
    }
    
    public void display() {
        final int currentSize = this.currentSize;
        System.out.print("{ ");
        for (int i = 0; i < currentSize; ++i) {
            final SolverVariable variable = this.getVariable(i);
            if (variable != null) {
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append(variable);
                sb.append(" = ");
                sb.append(this.getVariableValue(i));
                sb.append(" ");
                out.print(sb.toString());
            }
        }
        System.out.println(" }");
    }
    
    void divideByAmount(final float n) {
        for (int mHead = this.mHead, n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
            final float[] mArrayValues = this.mArrayValues;
            mArrayValues[mHead] /= n;
        }
    }
    
    public final float get(final SolverVariable solverVariable) {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            if (this.mArrayIndices[mHead] == solverVariable.id) {
                return this.mArrayValues[mHead];
            }
        }
        return 0.0f;
    }
    
    SolverVariable getPivotCandidate() {
        final SolverVariable candidate = this.candidate;
        if (candidate == null) {
            int mHead = this.mHead;
            int n = 0;
            SolverVariable solverVariable = null;
            while (mHead != -1 && n < this.currentSize) {
                if (this.mArrayValues[mHead] < 0.0f) {
                    final SolverVariable solverVariable2 = this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
                    if (solverVariable == null || solverVariable.strength < solverVariable2.strength) {
                        solverVariable = solverVariable2;
                    }
                }
                mHead = this.mArrayNextIndices[mHead];
                ++n;
            }
            return solverVariable;
        }
        return candidate;
    }
    
    final SolverVariable getVariable(final int n) {
        for (int mHead = this.mHead, n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
            if (n2 == n) {
                return this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
            }
        }
        return null;
    }
    
    final float getVariableValue(final int n) {
        for (int mHead = this.mHead, n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
            if (n2 == n) {
                return this.mArrayValues[mHead];
            }
        }
        return 0.0f;
    }
    
    boolean hasAtLeastOnePositiveVariable() {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            if (this.mArrayValues[mHead] > 0.0f) {
                return true;
            }
        }
        return false;
    }
    
    void invert() {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            final float[] mArrayValues = this.mArrayValues;
            mArrayValues[mHead] *= -1.0f;
        }
    }
    
    SolverVariable pickPivotCandidate() {
        SolverVariable solverVariable = null;
        SolverVariable solverVariable2 = null;
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            final float[] mArrayValues = this.mArrayValues;
            float n2 = mArrayValues[mHead];
            if (n2 < 0.0f) {
                if (n2 > -0.001f) {
                    mArrayValues[mHead] = 0.0f;
                    n2 = 0.0f;
                }
            }
            else if (n2 < 0.001f) {
                mArrayValues[mHead] = 0.0f;
                n2 = 0.0f;
            }
            if (n2 != 0.0f) {
                final SolverVariable solverVariable3 = this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
                if (solverVariable3.mType == SolverVariable.Type.UNRESTRICTED) {
                    if (n2 < 0.0f) {
                        return solverVariable3;
                    }
                    if (solverVariable2 == null) {
                        solverVariable2 = solverVariable3;
                    }
                }
                else if (n2 < 0.0f && (solverVariable == null || solverVariable3.strength < solverVariable.strength)) {
                    solverVariable = solverVariable3;
                }
            }
        }
        if (solverVariable2 != null) {
            return solverVariable2;
        }
        return solverVariable;
    }
    
    public final void put(final SolverVariable solverVariable, final float n) {
        if (n == 0.0f) {
            this.remove(solverVariable);
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            final float[] mArrayValues = this.mArrayValues;
            final int mHead = this.mHead;
            mArrayValues[mHead] = n;
            this.mArrayIndices[mHead] = solverVariable.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                ++this.mLast;
            }
        }
        else {
            int mHead2 = this.mHead;
            int n2 = -1;
            for (int n3 = 0; mHead2 != -1 && n3 < this.currentSize; mHead2 = this.mArrayNextIndices[mHead2], ++n3) {
                if (this.mArrayIndices[mHead2] == solverVariable.id) {
                    this.mArrayValues[mHead2] = n;
                    return;
                }
                if (this.mArrayIndices[mHead2] < solverVariable.id) {
                    n2 = mHead2;
                }
            }
            final int mLast = this.mLast;
            int mHead3 = mLast + 1;
            if (this.mDidFillOnce) {
                final int[] mArrayIndices = this.mArrayIndices;
                if (mArrayIndices[mLast] == -1) {
                    mHead3 = this.mLast;
                }
                else {
                    mHead3 = mArrayIndices.length;
                }
            }
            final int[] mArrayIndices2 = this.mArrayIndices;
            if (mHead3 >= mArrayIndices2.length) {
                if (this.currentSize < mArrayIndices2.length) {
                    int n4 = 0;
                    while (true) {
                        final int[] mArrayIndices3 = this.mArrayIndices;
                        if (n4 >= mArrayIndices3.length) {
                            break;
                        }
                        if (mArrayIndices3[n4] == -1) {
                            mHead3 = n4;
                            break;
                        }
                        ++n4;
                    }
                }
            }
            final int[] mArrayIndices4 = this.mArrayIndices;
            if (mHead3 >= mArrayIndices4.length) {
                mHead3 = mArrayIndices4.length;
                this.ROW_SIZE *= 2;
                this.mDidFillOnce = false;
                this.mLast = mHead3 - 1;
                this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
                this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
                this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
            }
            this.mArrayIndices[mHead3] = solverVariable.id;
            this.mArrayValues[mHead3] = n;
            if (n2 != -1) {
                final int[] mArrayNextIndices = this.mArrayNextIndices;
                mArrayNextIndices[mHead3] = mArrayNextIndices[n2];
                mArrayNextIndices[n2] = mHead3;
            }
            else {
                this.mArrayNextIndices[mHead3] = this.mHead;
                this.mHead = mHead3;
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
    
    public final float remove(final SolverVariable solverVariable) {
        if (this.candidate == solverVariable) {
            this.candidate = null;
        }
        if (this.mHead == -1) {
            return 0.0f;
        }
        int mHead = this.mHead;
        int n = -1;
        for (int n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
            final int n3 = this.mArrayIndices[mHead];
            if (n3 == solverVariable.id) {
                if (mHead == this.mHead) {
                    this.mHead = this.mArrayNextIndices[mHead];
                }
                else {
                    final int[] mArrayNextIndices = this.mArrayNextIndices;
                    mArrayNextIndices[n] = mArrayNextIndices[mHead];
                }
                this.mCache.mIndexedVariables[n3].removeClientEquation(this.mRow);
                --this.currentSize;
                this.mArrayIndices[mHead] = -1;
                if (this.mDidFillOnce) {
                    this.mLast = mHead;
                }
                return this.mArrayValues[mHead];
            }
            n = mHead;
        }
        return 0.0f;
    }
    
    int sizeInBytes() {
        return 0 + this.mArrayIndices.length * 4 * 3 + 36;
    }
    
    @Override
    public String toString() {
        String string = "";
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(" -> ");
            final String string2 = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string2);
            sb2.append(this.mArrayValues[mHead]);
            sb2.append(" : ");
            final String string3 = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string3);
            sb3.append(this.mCache.mIndexedVariables[this.mArrayIndices[mHead]]);
            string = sb3.toString();
        }
        return string;
    }
    
    void updateClientEquations(final ArrayRow arrayRow) {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            this.mCache.mIndexedVariables[this.mArrayIndices[mHead]].addClientEquation(arrayRow);
        }
    }
    
    void updateFromRow(final ArrayRow arrayRow, final ArrayRow arrayRow2) {
        int n = this.mHead;
        int n2 = 0;
        while (n != -1 && n2 < this.currentSize) {
            if (this.mArrayIndices[n] == arrayRow2.variable.id) {
                final float n3 = this.mArrayValues[n];
                this.remove(arrayRow2.variable);
                final ArrayLinkedVariables variables = arrayRow2.variables;
                for (int mHead = variables.mHead, n4 = 0; mHead != -1 && n4 < variables.currentSize; mHead = variables.mArrayNextIndices[mHead], ++n4) {
                    this.add(this.mCache.mIndexedVariables[variables.mArrayIndices[mHead]], variables.mArrayValues[mHead] * n3);
                }
                arrayRow.constantValue += arrayRow2.constantValue * n3;
                arrayRow2.variable.removeClientEquation(arrayRow);
                n = this.mHead;
                n2 = 0;
            }
            else {
                n = this.mArrayNextIndices[n];
                ++n2;
            }
        }
    }
    
    void updateFromSystem(final ArrayRow arrayRow, final ArrayRow[] array) {
        int n = this.mHead;
        int n2 = 0;
        while (n != -1 && n2 < this.currentSize) {
            final SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[n]];
            if (solverVariable.definitionId != -1) {
                final float n3 = this.mArrayValues[n];
                this.remove(solverVariable);
                final ArrayRow arrayRow2 = array[solverVariable.definitionId];
                if (!arrayRow2.isSimpleDefinition) {
                    final ArrayLinkedVariables variables = arrayRow2.variables;
                    for (int mHead = variables.mHead, n4 = 0; mHead != -1 && n4 < variables.currentSize; mHead = variables.mArrayNextIndices[mHead], ++n4) {
                        this.add(this.mCache.mIndexedVariables[variables.mArrayIndices[mHead]], variables.mArrayValues[mHead] * n3);
                    }
                }
                arrayRow.constantValue += arrayRow2.constantValue * n3;
                arrayRow2.variable.removeClientEquation(arrayRow);
                n = this.mHead;
                n2 = 0;
            }
            else {
                n = this.mArrayNextIndices[n];
                ++n2;
            }
        }
    }
}
