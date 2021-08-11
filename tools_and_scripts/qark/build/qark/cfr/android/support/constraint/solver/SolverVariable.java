/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver;

import android.support.constraint.solver.ArrayRow;
import java.util.Arrays;

public class SolverVariable {
    private static final boolean INTERNAL_DEBUG = false;
    static final int MAX_STRENGTH = 6;
    public static final int STRENGTH_EQUALITY = 5;
    public static final int STRENGTH_HIGH = 3;
    public static final int STRENGTH_HIGHEST = 4;
    public static final int STRENGTH_LOW = 1;
    public static final int STRENGTH_MEDIUM = 2;
    public static final int STRENGTH_NONE = 0;
    private static int uniqueId = 1;
    public float computedValue;
    int definitionId = -1;
    public int id = -1;
    ArrayRow[] mClientEquations = new ArrayRow[8];
    int mClientEquationsCount = 0;
    private String mName;
    Type mType;
    public int strength = 0;
    float[] strengthVector = new float[6];

    public SolverVariable(Type type) {
        this.mType = type;
    }

    public SolverVariable(String string2, Type type) {
        this.mName = string2;
        this.mType = type;
    }

    private static String getUniqueName(Type object) {
        ++uniqueId;
        switch (.$SwitchMap$android$support$constraint$solver$SolverVariable$Type[object.ordinal()]) {
            default: {
                object = new StringBuilder();
                object.append("V");
                object.append(uniqueId);
                return object.toString();
            }
            case 4: {
                object = new StringBuilder();
                object.append("e");
                object.append(uniqueId);
                return object.toString();
            }
            case 3: {
                object = new StringBuilder();
                object.append("S");
                object.append(uniqueId);
                return object.toString();
            }
            case 2: {
                object = new StringBuilder();
                object.append("C");
                object.append(uniqueId);
                return object.toString();
            }
            case 1: 
        }
        object = new StringBuilder();
        object.append("U");
        object.append(uniqueId);
        return object.toString();
    }

    void addClientEquation(ArrayRow arrayRow) {
        int n;
        int n2;
        for (n2 = 0; n2 < (n = this.mClientEquationsCount); ++n2) {
            if (this.mClientEquations[n2] != arrayRow) continue;
            return;
        }
        ArrayRow[] arrarrayRow = this.mClientEquations;
        if (n >= arrarrayRow.length) {
            this.mClientEquations = Arrays.copyOf(arrarrayRow, arrarrayRow.length * 2);
        }
        arrarrayRow = this.mClientEquations;
        n2 = this.mClientEquationsCount;
        arrarrayRow[n2] = arrayRow;
        this.mClientEquationsCount = n2 + 1;
    }

    void clearStrengths() {
        for (int i = 0; i < 6; ++i) {
            this.strengthVector[i] = 0.0f;
        }
    }

    public String getName() {
        return this.mName;
    }

    void removeClientEquation(ArrayRow arrarrayRow) {
        for (int i = 0; i < this.mClientEquationsCount; ++i) {
            int n;
            if (this.mClientEquations[i] != arrarrayRow) continue;
            for (int j = 0; j < (n = this.mClientEquationsCount) - i - 1; ++j) {
                arrarrayRow = this.mClientEquations;
                arrarrayRow[i + j] = arrarrayRow[i + j + 1];
            }
            this.mClientEquationsCount = n - 1;
            return;
        }
    }

    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.strength = 0;
        this.id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.mClientEquationsCount = 0;
    }

    public void setName(String string2) {
        this.mName = string2;
    }

    public void setType(Type type) {
        this.mType = type;
    }

    String strengthsToString() {
        CharSequence charSequence = new StringBuilder();
        charSequence.append(this);
        charSequence.append("[");
        charSequence = charSequence.toString();
        for (int i = 0; i < this.strengthVector.length; ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)charSequence);
            stringBuilder.append(this.strengthVector[i]);
            charSequence = stringBuilder.toString();
            if (i < this.strengthVector.length - 1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append((String)charSequence);
                stringBuilder.append(", ");
                charSequence = stringBuilder.toString();
                continue;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append((String)charSequence);
            stringBuilder.append("] ");
            charSequence = stringBuilder.toString();
        }
        return charSequence;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(this.mName);
        return stringBuilder.toString();
    }

    public static enum Type {
        UNRESTRICTED,
        CONSTANT,
        SLACK,
        ERROR,
        UNKNOWN;
        

        private Type() {
        }
    }

}

