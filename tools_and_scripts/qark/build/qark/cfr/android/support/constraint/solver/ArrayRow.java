/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver;

import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.constraint.solver.Cache;
import android.support.constraint.solver.SolverVariable;

public class ArrayRow {
    private static final boolean DEBUG = false;
    float constantValue = 0.0f;
    boolean isSimpleDefinition = false;
    boolean used = false;
    SolverVariable variable = null;
    final ArrayLinkedVariables variables;

    public ArrayRow(Cache cache) {
        this.variables = new ArrayLinkedVariables(this, cache);
    }

    public ArrayRow addError(SolverVariable solverVariable, SolverVariable solverVariable2) {
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        return this;
    }

    ArrayRow addSingleError(SolverVariable solverVariable, int n) {
        this.variables.put(solverVariable, n);
        return this;
    }

    ArrayRow createRowCentering(SolverVariable solverVariable, SolverVariable solverVariable2, int n, float f, SolverVariable solverVariable3, SolverVariable solverVariable4, int n2) {
        if (solverVariable2 == solverVariable3) {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.variables.put(solverVariable2, -2.0f);
            return this;
        }
        if (f == 0.5f) {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable3, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            if (n <= 0 && n2 <= 0) {
                return this;
            }
            this.constantValue = - n + n2;
            return this;
        }
        if (f <= 0.0f) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.constantValue = n;
            return this;
        }
        if (f >= 1.0f) {
            this.variables.put(solverVariable3, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.constantValue = n2;
            return this;
        }
        this.variables.put(solverVariable, (1.0f - f) * 1.0f);
        this.variables.put(solverVariable2, (1.0f - f) * -1.0f);
        this.variables.put(solverVariable3, -1.0f * f);
        this.variables.put(solverVariable4, f * 1.0f);
        if (n <= 0 && n2 <= 0) {
            return this;
        }
        this.constantValue = (float)(- n) * (1.0f - f) + (float)n2 * f;
        return this;
    }

    ArrayRow createRowDefinition(SolverVariable solverVariable, int n) {
        this.variable = solverVariable;
        solverVariable.computedValue = n;
        this.constantValue = n;
        this.isSimpleDefinition = true;
        return this;
    }

    ArrayRow createRowDimensionPercent(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, float f) {
        this.variables.put(solverVariable, -1.0f);
        this.variables.put(solverVariable2, 1.0f - f);
        this.variables.put(solverVariable3, f);
        return this;
    }

    public ArrayRow createRowDimensionRatio(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f) {
        this.variables.put(solverVariable, -1.0f);
        this.variables.put(solverVariable2, 1.0f);
        this.variables.put(solverVariable3, f);
        this.variables.put(solverVariable4, - f);
        return this;
    }

    public ArrayRow createRowEqualDimension(float f, float f2, float f3, SolverVariable solverVariable, int n, SolverVariable solverVariable2, int n2, SolverVariable solverVariable3, int n3, SolverVariable solverVariable4, int n4) {
        if (f2 != 0.0f && f != f3) {
            f = f / f2 / (f3 / f2);
            this.constantValue = (float)(- n - n2) + (float)n3 * f + (float)n4 * f;
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable4, f);
            this.variables.put(solverVariable3, - f);
            return this;
        }
        this.constantValue = - n - n2 + n3 + n4;
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        this.variables.put(solverVariable4, 1.0f);
        this.variables.put(solverVariable3, -1.0f);
        return this;
    }

    public ArrayRow createRowEquals(SolverVariable solverVariable, int n) {
        if (n < 0) {
            this.constantValue = n * -1;
            this.variables.put(solverVariable, 1.0f);
            return this;
        }
        this.constantValue = n;
        this.variables.put(solverVariable, -1.0f);
        return this;
    }

    public ArrayRow createRowEquals(SolverVariable solverVariable, SolverVariable solverVariable2, int n) {
        int n2 = 0;
        int n3 = 0;
        if (n != 0) {
            if (n < 0) {
                n2 = n * -1;
                n = 1;
            } else {
                n2 = n;
                n = n3;
            }
            this.constantValue = n2;
        } else {
            n = n2;
        }
        if (n == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            return this;
        }
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        return this;
    }

    public ArrayRow createRowGreaterThan(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, int n) {
        int n2 = 0;
        int n3 = 0;
        if (n != 0) {
            if (n < 0) {
                n2 = n * -1;
                n = 1;
            } else {
                n2 = n;
                n = n3;
            }
            this.constantValue = n2;
        } else {
            n = n2;
        }
        if (n == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.variables.put(solverVariable3, 1.0f);
            return this;
        }
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        this.variables.put(solverVariable3, -1.0f);
        return this;
    }

    public ArrayRow createRowLowerThan(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, int n) {
        int n2 = 0;
        int n3 = 0;
        if (n != 0) {
            if (n < 0) {
                n2 = n * -1;
                n = 1;
            } else {
                n2 = n;
                n = n3;
            }
            this.constantValue = n2;
        } else {
            n = n2;
        }
        if (n == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.variables.put(solverVariable3, -1.0f);
            return this;
        }
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        this.variables.put(solverVariable3, 1.0f);
        return this;
    }

    void ensurePositiveConstant() {
        float f = this.constantValue;
        if (f < 0.0f) {
            this.constantValue = f * -1.0f;
            this.variables.invert();
            return;
        }
    }

    boolean hasAtLeastOnePositiveVariable() {
        return this.variables.hasAtLeastOnePositiveVariable();
    }

    boolean hasKeyVariable() {
        SolverVariable solverVariable = this.variable;
        if (solverVariable != null && (solverVariable.mType == SolverVariable.Type.UNRESTRICTED || this.constantValue >= 0.0f)) {
            return true;
        }
        return false;
    }

    boolean hasVariable(SolverVariable solverVariable) {
        return this.variables.containsKey(solverVariable);
    }

    void pickRowVariable() {
        SolverVariable solverVariable = this.variables.pickPivotCandidate();
        if (solverVariable != null) {
            this.pivot(solverVariable);
        }
        if (this.variables.currentSize == 0) {
            this.isSimpleDefinition = true;
            return;
        }
    }

    void pivot(SolverVariable solverVariable) {
        SolverVariable solverVariable2 = this.variable;
        if (solverVariable2 != null) {
            this.variables.put(solverVariable2, -1.0f);
            this.variable = null;
        }
        float f = this.variables.remove(solverVariable) * -1.0f;
        this.variable = solverVariable;
        if (f == 1.0f) {
            return;
        }
        this.constantValue /= f;
        this.variables.divideByAmount(f);
    }

    public void reset() {
        this.variable = null;
        this.variables.clear();
        this.constantValue = 0.0f;
        this.isSimpleDefinition = false;
    }

    int sizeInBytes() {
        int n = 0;
        if (this.variable != null) {
            n = 0 + 4;
        }
        return n + 4 + 4 + this.variables.sizeInBytes();
    }

    String toReadableString() {
        CharSequence charSequence2;
        CharSequence charSequence2;
        if (this.variable == null) {
            charSequence2 = new StringBuilder();
            charSequence2.append("");
            charSequence2.append("0");
            charSequence2 = charSequence2.toString();
        } else {
            charSequence2 = new StringBuilder();
            charSequence2.append("");
            charSequence2.append(this.variable);
            charSequence2 = charSequence2.toString();
        }
        Object object = new StringBuilder();
        object.append((String)charSequence2);
        object.append(" = ");
        charSequence2 = object.toString();
        boolean bl = false;
        if (this.constantValue != 0.0f) {
            object = new StringBuilder();
            object.append((String)charSequence2);
            object.append(this.constantValue);
            charSequence2 = object.toString();
            bl = true;
        }
        int n = this.variables.currentSize;
        for (int i = 0; i < n; ++i) {
            StringBuilder stringBuilder;
            object = this.variables.getVariable(i);
            if (object == null) continue;
            float f = this.variables.getVariableValue(i);
            object = object.toString();
            if (!bl) {
                if (f < 0.0f) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append((String)charSequence2);
                    stringBuilder.append("- ");
                    charSequence2 = stringBuilder.toString();
                    f *= -1.0f;
                }
            } else if (f > 0.0f) {
                stringBuilder = new StringBuilder();
                stringBuilder.append((String)charSequence2);
                stringBuilder.append(" + ");
                charSequence2 = stringBuilder.toString();
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append((String)charSequence2);
                stringBuilder.append(" - ");
                charSequence2 = stringBuilder.toString();
                f *= -1.0f;
            }
            if (f == 1.0f) {
                stringBuilder = new StringBuilder();
                stringBuilder.append((String)charSequence2);
                stringBuilder.append((String)object);
                charSequence2 = stringBuilder.toString();
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append((String)charSequence2);
                stringBuilder.append(f);
                stringBuilder.append(" ");
                stringBuilder.append((String)object);
                charSequence2 = stringBuilder.toString();
            }
            bl = true;
        }
        if (!bl) {
            object = new StringBuilder();
            object.append((String)charSequence2);
            object.append("0.0");
            return object.toString();
        }
        return charSequence2;
    }

    public String toString() {
        return this.toReadableString();
    }

    void updateClientEquations() {
        this.variables.updateClientEquations(this);
    }

    boolean updateRowWithEquation(ArrayRow arrayRow) {
        this.variables.updateFromRow(this, arrayRow);
        return true;
    }
}

