/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.Cache;
import android.support.constraint.solver.SolverVariable;
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

    ArrayLinkedVariables(ArrayRow arrayRow, Cache cache) {
        int n = this.ROW_SIZE;
        this.mArrayIndices = new int[n];
        this.mArrayNextIndices = new int[n];
        this.mArrayValues = new float[n];
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.mRow = arrayRow;
        this.mCache = cache;
    }

    public final void add(SolverVariable arrf, float f) {
        int[] arrn;
        int n;
        if (f == 0.0f) {
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            float[] arrf2 = this.mArrayValues;
            int n2 = this.mHead;
            arrf2[n2] = f;
            this.mArrayIndices[n2] = arrf.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                ++this.mLast;
                return;
            }
            return;
        }
        int n3 = this.mHead;
        int n4 = -1;
        for (n = 0; n3 != -1 && n < this.currentSize; ++n) {
            int n5 = this.mArrayIndices[n3];
            if (n5 == arrf.id) {
                arrf = this.mArrayValues;
                arrf[n3] = arrf[n3] + f;
                if (arrf[n3] == 0.0f) {
                    if (n3 == this.mHead) {
                        this.mHead = this.mArrayNextIndices[n3];
                    } else {
                        arrf = this.mArrayNextIndices;
                        arrf[n4] = arrf[n3];
                    }
                    this.mCache.mIndexedVariables[n5].removeClientEquation(this.mRow);
                    if (this.mDidFillOnce) {
                        this.mLast = n3;
                    }
                    --this.currentSize;
                    return;
                }
                return;
            }
            if (this.mArrayIndices[n3] < arrf.id) {
                n4 = n3;
            }
            n3 = this.mArrayNextIndices[n3];
        }
        n = this.mLast;
        n3 = n + 1;
        if (this.mDidFillOnce) {
            arrn = this.mArrayIndices;
            n3 = arrn[n] == -1 ? this.mLast : arrn.length;
        }
        if (n3 >= (arrn = this.mArrayIndices).length && this.currentSize < arrn.length) {
            for (n = 0; n < (arrn = this.mArrayIndices).length; ++n) {
                if (arrn[n] != -1) continue;
                n3 = n;
                break;
            }
        }
        if (n3 >= (arrn = this.mArrayIndices).length) {
            n3 = arrn.length;
            this.ROW_SIZE *= 2;
            this.mDidFillOnce = false;
            this.mLast = n3 - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[n3] = arrf.id;
        this.mArrayValues[n3] = f;
        if (n4 != -1) {
            arrf = this.mArrayNextIndices;
            arrf[n3] = arrf[n4];
            arrf[n4] = n3;
        } else {
            this.mArrayNextIndices[n3] = this.mHead;
            this.mHead = n3;
        }
        ++this.currentSize;
        if (!this.mDidFillOnce) {
            ++this.mLast;
        }
        if ((n3 = this.mLast) >= (arrf = this.mArrayIndices).length) {
            this.mDidFillOnce = true;
            this.mLast = arrf.length - 1;
            return;
        }
    }

    public final void clear() {
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.currentSize = 0;
    }

    final boolean containsKey(SolverVariable solverVariable) {
        if (this.mHead == -1) {
            return false;
        }
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            if (this.mArrayIndices[n] == solverVariable.id) {
                return true;
            }
            n = this.mArrayNextIndices[n];
        }
        return false;
    }

    public void display() {
        int n = this.currentSize;
        System.out.print("{ ");
        for (int i = 0; i < n; ++i) {
            SolverVariable solverVariable = this.getVariable(i);
            if (solverVariable == null) continue;
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(solverVariable);
            stringBuilder.append(" = ");
            stringBuilder.append(this.getVariableValue(i));
            stringBuilder.append(" ");
            printStream.print(stringBuilder.toString());
        }
        System.out.println(" }");
    }

    void divideByAmount(float f) {
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            float[] arrf = this.mArrayValues;
            arrf[n] = arrf[n] / f;
            n = this.mArrayNextIndices[n];
        }
    }

    public final float get(SolverVariable solverVariable) {
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            if (this.mArrayIndices[n] == solverVariable.id) {
                return this.mArrayValues[n];
            }
            n = this.mArrayNextIndices[n];
        }
        return 0.0f;
    }

    SolverVariable getPivotCandidate() {
        SolverVariable solverVariable = this.candidate;
        if (solverVariable == null) {
            int n = this.mHead;
            solverVariable = null;
            for (int i = 0; n != -1 && i < this.currentSize; ++i) {
                if (this.mArrayValues[n] < 0.0f) {
                    SolverVariable solverVariable2 = this.mCache.mIndexedVariables[this.mArrayIndices[n]];
                    if (solverVariable == null || solverVariable.strength < solverVariable2.strength) {
                        solverVariable = solverVariable2;
                    }
                }
                n = this.mArrayNextIndices[n];
            }
            return solverVariable;
        }
        return solverVariable;
    }

    final SolverVariable getVariable(int n) {
        int n2 = this.mHead;
        for (int i = 0; n2 != -1 && i < this.currentSize; ++i) {
            if (i == n) {
                return this.mCache.mIndexedVariables[this.mArrayIndices[n2]];
            }
            n2 = this.mArrayNextIndices[n2];
        }
        return null;
    }

    final float getVariableValue(int n) {
        int n2 = this.mHead;
        for (int i = 0; n2 != -1 && i < this.currentSize; ++i) {
            if (i == n) {
                return this.mArrayValues[n2];
            }
            n2 = this.mArrayNextIndices[n2];
        }
        return 0.0f;
    }

    boolean hasAtLeastOnePositiveVariable() {
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            if (this.mArrayValues[n] > 0.0f) {
                return true;
            }
            n = this.mArrayNextIndices[n];
        }
        return false;
    }

    void invert() {
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            float[] arrf = this.mArrayValues;
            arrf[n] = arrf[n] * -1.0f;
            n = this.mArrayNextIndices[n];
        }
    }

    SolverVariable pickPivotCandidate() {
        float[] arrf = null;
        float[] arrf2 = null;
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            float[] arrf3 = this.mArrayValues;
            float f = arrf3[n];
            if (f < 0.0f) {
                if (f > - 0.001f) {
                    arrf3[n] = 0.0f;
                    f = 0.0f;
                }
            } else if (f < 0.001f) {
                arrf3[n] = 0.0f;
                f = 0.0f;
            }
            if (f != 0.0f) {
                arrf3 = this.mCache.mIndexedVariables[this.mArrayIndices[n]];
                if (arrf3.mType == SolverVariable.Type.UNRESTRICTED) {
                    if (f < 0.0f) {
                        return arrf3;
                    }
                    if (arrf2 == null) {
                        arrf2 = arrf3;
                    }
                } else if (f < 0.0f && (arrf == null || arrf3.strength < arrf.strength)) {
                    arrf = arrf3;
                }
            }
            n = this.mArrayNextIndices[n];
        }
        if (arrf2 != null) {
            return arrf2;
        }
        return arrf;
    }

    public final void put(SolverVariable arrn, float f) {
        int[] arrn2;
        int n;
        if (f == 0.0f) {
            this.remove((SolverVariable)arrn);
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            float[] arrf = this.mArrayValues;
            int n2 = this.mHead;
            arrf[n2] = f;
            this.mArrayIndices[n2] = arrn.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                ++this.mLast;
                return;
            }
            return;
        }
        int n3 = this.mHead;
        int n4 = -1;
        for (n = 0; n3 != -1 && n < this.currentSize; ++n) {
            if (this.mArrayIndices[n3] == arrn.id) {
                this.mArrayValues[n3] = f;
                return;
            }
            if (this.mArrayIndices[n3] < arrn.id) {
                n4 = n3;
            }
            n3 = this.mArrayNextIndices[n3];
        }
        n = this.mLast;
        n3 = n + 1;
        if (this.mDidFillOnce) {
            arrn2 = this.mArrayIndices;
            n3 = arrn2[n] == -1 ? this.mLast : arrn2.length;
        }
        if (n3 >= (arrn2 = this.mArrayIndices).length && this.currentSize < arrn2.length) {
            for (n = 0; n < (arrn2 = this.mArrayIndices).length; ++n) {
                if (arrn2[n] != -1) continue;
                n3 = n;
                break;
            }
        }
        if (n3 >= (arrn2 = this.mArrayIndices).length) {
            n3 = arrn2.length;
            this.ROW_SIZE *= 2;
            this.mDidFillOnce = false;
            this.mLast = n3 - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[n3] = arrn.id;
        this.mArrayValues[n3] = f;
        if (n4 != -1) {
            arrn = this.mArrayNextIndices;
            arrn[n3] = arrn[n4];
            arrn[n4] = n3;
        } else {
            this.mArrayNextIndices[n3] = this.mHead;
            this.mHead = n3;
        }
        ++this.currentSize;
        if (!this.mDidFillOnce) {
            ++this.mLast;
        }
        if (this.currentSize >= this.mArrayIndices.length) {
            this.mDidFillOnce = true;
            return;
        }
    }

    public final float remove(SolverVariable arrn) {
        if (this.candidate == arrn) {
            this.candidate = null;
        }
        if (this.mHead == -1) {
            return 0.0f;
        }
        int n = this.mHead;
        int n2 = -1;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            int n3 = this.mArrayIndices[n];
            if (n3 == arrn.id) {
                if (n == this.mHead) {
                    this.mHead = this.mArrayNextIndices[n];
                } else {
                    arrn = this.mArrayNextIndices;
                    arrn[n2] = arrn[n];
                }
                this.mCache.mIndexedVariables[n3].removeClientEquation(this.mRow);
                --this.currentSize;
                this.mArrayIndices[n] = -1;
                if (this.mDidFillOnce) {
                    this.mLast = n;
                }
                return this.mArrayValues[n];
            }
            n2 = n;
            n = this.mArrayNextIndices[n];
        }
        return 0.0f;
    }

    int sizeInBytes() {
        return 0 + this.mArrayIndices.length * 4 * 3 + 36;
    }

    public String toString() {
        String string2 = "";
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(" -> ");
            string2 = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(this.mArrayValues[n]);
            stringBuilder.append(" : ");
            string2 = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(this.mCache.mIndexedVariables[this.mArrayIndices[n]]);
            string2 = stringBuilder.toString();
            n = this.mArrayNextIndices[n];
        }
        return string2;
    }

    void updateClientEquations(ArrayRow arrayRow) {
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            this.mCache.mIndexedVariables[this.mArrayIndices[n]].addClientEquation(arrayRow);
            n = this.mArrayNextIndices[n];
        }
    }

    void updateFromRow(ArrayRow arrayRow, ArrayRow arrayRow2) {
        int n = this.mHead;
        int n2 = 0;
        while (n != -1 && n2 < this.currentSize) {
            if (this.mArrayIndices[n] == arrayRow2.variable.id) {
                float f = this.mArrayValues[n];
                this.remove(arrayRow2.variable);
                ArrayLinkedVariables arrayLinkedVariables = arrayRow2.variables;
                n2 = arrayLinkedVariables.mHead;
                for (n = 0; n2 != -1 && n < arrayLinkedVariables.currentSize; ++n) {
                    this.add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[n2]], arrayLinkedVariables.mArrayValues[n2] * f);
                    n2 = arrayLinkedVariables.mArrayNextIndices[n2];
                }
                arrayRow.constantValue += arrayRow2.constantValue * f;
                arrayRow2.variable.removeClientEquation(arrayRow);
                n = this.mHead;
                n2 = 0;
                continue;
            }
            n = this.mArrayNextIndices[n];
            ++n2;
        }
    }

    void updateFromSystem(ArrayRow arrayRow, ArrayRow[] arrarrayRow) {
        int n = this.mHead;
        int n2 = 0;
        while (n != -1 && n2 < this.currentSize) {
            Object object = this.mCache.mIndexedVariables[this.mArrayIndices[n]];
            if (object.definitionId != -1) {
                float f = this.mArrayValues[n];
                this.remove((SolverVariable)object);
                object = arrarrayRow[object.definitionId];
                if (!object.isSimpleDefinition) {
                    ArrayLinkedVariables arrayLinkedVariables = object.variables;
                    n2 = arrayLinkedVariables.mHead;
                    for (n = 0; n2 != -1 && n < arrayLinkedVariables.currentSize; ++n) {
                        this.add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[n2]], arrayLinkedVariables.mArrayValues[n2] * f);
                        n2 = arrayLinkedVariables.mArrayNextIndices[n2];
                    }
                }
                arrayRow.constantValue += object.constantValue * f;
                object.variable.removeClientEquation(arrayRow);
                n = this.mHead;
                n2 = 0;
                continue;
            }
            n = this.mArrayNextIndices[n];
            ++n2;
        }
    }
}

