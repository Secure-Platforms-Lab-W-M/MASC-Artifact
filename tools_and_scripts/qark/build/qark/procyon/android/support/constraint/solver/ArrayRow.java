// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver;

public class ArrayRow
{
    private static final boolean DEBUG = false;
    float constantValue;
    boolean isSimpleDefinition;
    boolean used;
    SolverVariable variable;
    final ArrayLinkedVariables variables;
    
    public ArrayRow(final Cache cache) {
        this.variable = null;
        this.constantValue = 0.0f;
        this.used = false;
        this.isSimpleDefinition = false;
        this.variables = new ArrayLinkedVariables(this, cache);
    }
    
    public ArrayRow addError(final SolverVariable solverVariable, final SolverVariable solverVariable2) {
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        return this;
    }
    
    ArrayRow addSingleError(final SolverVariable solverVariable, final int n) {
        this.variables.put(solverVariable, (float)n);
        return this;
    }
    
    ArrayRow createRowCentering(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final float n2, final SolverVariable solverVariable3, final SolverVariable solverVariable4, final int n3) {
        if (solverVariable2 == solverVariable3) {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.variables.put(solverVariable2, -2.0f);
            return this;
        }
        if (n2 == 0.5f) {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable3, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            if (n <= 0 && n3 <= 0) {
                return this;
            }
            this.constantValue = (float)(-n + n3);
            return this;
        }
        else {
            if (n2 <= 0.0f) {
                this.variables.put(solverVariable, -1.0f);
                this.variables.put(solverVariable2, 1.0f);
                this.constantValue = (float)n;
                return this;
            }
            if (n2 >= 1.0f) {
                this.variables.put(solverVariable3, -1.0f);
                this.variables.put(solverVariable4, 1.0f);
                this.constantValue = (float)n3;
                return this;
            }
            this.variables.put(solverVariable, (1.0f - n2) * 1.0f);
            this.variables.put(solverVariable2, (1.0f - n2) * -1.0f);
            this.variables.put(solverVariable3, -1.0f * n2);
            this.variables.put(solverVariable4, n2 * 1.0f);
            if (n <= 0 && n3 <= 0) {
                return this;
            }
            this.constantValue = -n * (1.0f - n2) + n3 * n2;
            return this;
        }
    }
    
    ArrayRow createRowDefinition(final SolverVariable variable, final int n) {
        this.variable = variable;
        variable.computedValue = (float)n;
        this.constantValue = (float)n;
        this.isSimpleDefinition = true;
        return this;
    }
    
    ArrayRow createRowDimensionPercent(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final float n) {
        this.variables.put(solverVariable, -1.0f);
        this.variables.put(solverVariable2, 1.0f - n);
        this.variables.put(solverVariable3, n);
        return this;
    }
    
    public ArrayRow createRowDimensionRatio(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final SolverVariable solverVariable4, final float n) {
        this.variables.put(solverVariable, -1.0f);
        this.variables.put(solverVariable2, 1.0f);
        this.variables.put(solverVariable3, n);
        this.variables.put(solverVariable4, -n);
        return this;
    }
    
    public ArrayRow createRowEqualDimension(float n, final float n2, final float n3, final SolverVariable solverVariable, final int n4, final SolverVariable solverVariable2, final int n5, final SolverVariable solverVariable3, final int n6, final SolverVariable solverVariable4, final int n7) {
        if (n2 != 0.0f && n != n3) {
            n = n / n2 / (n3 / n2);
            this.constantValue = -n4 - n5 + n6 * n + n7 * n;
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable4, n);
            this.variables.put(solverVariable3, -n);
            return this;
        }
        this.constantValue = (float)(-n4 - n5 + n6 + n7);
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        this.variables.put(solverVariable4, 1.0f);
        this.variables.put(solverVariable3, -1.0f);
        return this;
    }
    
    public ArrayRow createRowEquals(final SolverVariable solverVariable, final int n) {
        if (n < 0) {
            this.constantValue = (float)(n * -1);
            this.variables.put(solverVariable, 1.0f);
            return this;
        }
        this.constantValue = (float)n;
        this.variables.put(solverVariable, -1.0f);
        return this;
    }
    
    public ArrayRow createRowEquals(final SolverVariable solverVariable, final SolverVariable solverVariable2, int n) {
        final int n2 = 0;
        final int n3 = 0;
        if (n != 0) {
            int n4;
            if (n < 0) {
                n4 = n * -1;
                n = 1;
            }
            else {
                n4 = n;
                n = n3;
            }
            this.constantValue = (float)n4;
        }
        else {
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
    
    public ArrayRow createRowGreaterThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, int n) {
        final int n2 = 0;
        final int n3 = 0;
        if (n != 0) {
            int n4;
            if (n < 0) {
                n4 = n * -1;
                n = 1;
            }
            else {
                n4 = n;
                n = n3;
            }
            this.constantValue = (float)n4;
        }
        else {
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
    
    public ArrayRow createRowLowerThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, int n) {
        final int n2 = 0;
        final int n3 = 0;
        if (n != 0) {
            int n4;
            if (n < 0) {
                n4 = n * -1;
                n = 1;
            }
            else {
                n4 = n;
                n = n3;
            }
            this.constantValue = (float)n4;
        }
        else {
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
        final float constantValue = this.constantValue;
        if (constantValue < 0.0f) {
            this.constantValue = constantValue * -1.0f;
            this.variables.invert();
        }
    }
    
    boolean hasAtLeastOnePositiveVariable() {
        return this.variables.hasAtLeastOnePositiveVariable();
    }
    
    boolean hasKeyVariable() {
        final SolverVariable variable = this.variable;
        return variable != null && (variable.mType == SolverVariable.Type.UNRESTRICTED || this.constantValue >= 0.0f);
    }
    
    boolean hasVariable(final SolverVariable solverVariable) {
        return this.variables.containsKey(solverVariable);
    }
    
    void pickRowVariable() {
        final SolverVariable pickPivotCandidate = this.variables.pickPivotCandidate();
        if (pickPivotCandidate != null) {
            this.pivot(pickPivotCandidate);
        }
        if (this.variables.currentSize == 0) {
            this.isSimpleDefinition = true;
        }
    }
    
    void pivot(final SolverVariable variable) {
        final SolverVariable variable2 = this.variable;
        if (variable2 != null) {
            this.variables.put(variable2, -1.0f);
            this.variable = null;
        }
        final float n = this.variables.remove(variable) * -1.0f;
        this.variable = variable;
        if (n == 1.0f) {
            return;
        }
        this.constantValue /= n;
        this.variables.divideByAmount(n);
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
        String s;
        if (this.variable == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append("0");
            s = sb.toString();
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(this.variable);
            s = sb2.toString();
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append(" = ");
        String s2 = sb3.toString();
        int n = 0;
        if (this.constantValue != 0.0f) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(s2);
            sb4.append(this.constantValue);
            s2 = sb4.toString();
            n = 1;
        }
        for (int currentSize = this.variables.currentSize, i = 0; i < currentSize; ++i) {
            final SolverVariable variable = this.variables.getVariable(i);
            if (variable != null) {
                float variableValue = this.variables.getVariableValue(i);
                final String string = variable.toString();
                if (n == 0) {
                    if (variableValue < 0.0f) {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append(s2);
                        sb5.append("- ");
                        s2 = sb5.toString();
                        variableValue *= -1.0f;
                    }
                }
                else if (variableValue > 0.0f) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append(s2);
                    sb6.append(" + ");
                    s2 = sb6.toString();
                }
                else {
                    final StringBuilder sb7 = new StringBuilder();
                    sb7.append(s2);
                    sb7.append(" - ");
                    s2 = sb7.toString();
                    variableValue *= -1.0f;
                }
                if (variableValue == 1.0f) {
                    final StringBuilder sb8 = new StringBuilder();
                    sb8.append(s2);
                    sb8.append(string);
                    s2 = sb8.toString();
                }
                else {
                    final StringBuilder sb9 = new StringBuilder();
                    sb9.append(s2);
                    sb9.append(variableValue);
                    sb9.append(" ");
                    sb9.append(string);
                    s2 = sb9.toString();
                }
                n = 1;
            }
        }
        if (n == 0) {
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(s2);
            sb10.append("0.0");
            return sb10.toString();
        }
        return s2;
    }
    
    @Override
    public String toString() {
        return this.toReadableString();
    }
    
    void updateClientEquations() {
        this.variables.updateClientEquations(this);
    }
    
    boolean updateRowWithEquation(final ArrayRow arrayRow) {
        this.variables.updateFromRow(this, arrayRow);
        return true;
    }
}
