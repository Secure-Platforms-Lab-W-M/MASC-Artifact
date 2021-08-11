/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver;

import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;

public class Goal {
    ArrayList<SolverVariable> variables = new ArrayList();

    private void initFromSystemErrors(LinearSystem linearSystem) {
        this.variables.clear();
        for (int i = 1; i < linearSystem.mNumColumns; ++i) {
            SolverVariable solverVariable = linearSystem.mCache.mIndexedVariables[i];
            for (int j = 0; j < 6; ++j) {
                solverVariable.strengthVector[j] = 0.0f;
            }
            solverVariable.strengthVector[solverVariable.strength] = 1.0f;
            if (solverVariable.mType != SolverVariable.Type.ERROR) continue;
            this.variables.add(solverVariable);
        }
    }

    SolverVariable getPivotCandidate() {
        int n = this.variables.size();
        SolverVariable solverVariable = null;
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            SolverVariable solverVariable2 = this.variables.get(i);
            for (int j = 5; j >= 0; --j) {
                float f = solverVariable2.strengthVector[j];
                if (solverVariable == null && f < 0.0f && j >= n2) {
                    n2 = j;
                    solverVariable = solverVariable2;
                }
                if (f <= 0.0f || j <= n2) continue;
                n2 = j;
                solverVariable = null;
            }
        }
        return solverVariable;
    }

    public String toString() {
        String string2 = "Goal: ";
        int n = this.variables.size();
        for (int i = 0; i < n; ++i) {
            SolverVariable solverVariable = this.variables.get(i);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(solverVariable.strengthsToString());
            string2 = stringBuilder.toString();
        }
        return string2;
    }

    void updateFromSystem(LinearSystem linearSystem) {
        this.initFromSystemErrors(linearSystem);
        int n = this.variables.size();
        for (int i = 0; i < n; ++i) {
            SolverVariable solverVariable = this.variables.get(i);
            if (solverVariable.definitionId == -1) continue;
            ArrayLinkedVariables arrayLinkedVariables = linearSystem.getRow((int)solverVariable.definitionId).variables;
            int n2 = arrayLinkedVariables.currentSize;
            for (int j = 0; j < n2; ++j) {
                SolverVariable solverVariable2 = arrayLinkedVariables.getVariable(j);
                if (solverVariable2 == null) continue;
                float f = arrayLinkedVariables.getVariableValue(j);
                for (int k = 0; k < 6; ++k) {
                    float[] arrf = solverVariable2.strengthVector;
                    arrf[k] = arrf[k] + solverVariable.strengthVector[k] * f;
                }
                if (this.variables.contains(solverVariable2)) continue;
                this.variables.add(solverVariable2);
            }
            solverVariable.clearStrengths();
        }
    }
}

