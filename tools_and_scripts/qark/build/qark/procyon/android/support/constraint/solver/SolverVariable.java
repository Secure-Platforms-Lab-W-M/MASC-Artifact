// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver;

import java.util.Arrays;

public class SolverVariable
{
    private static final boolean INTERNAL_DEBUG = false;
    static final int MAX_STRENGTH = 6;
    public static final int STRENGTH_EQUALITY = 5;
    public static final int STRENGTH_HIGH = 3;
    public static final int STRENGTH_HIGHEST = 4;
    public static final int STRENGTH_LOW = 1;
    public static final int STRENGTH_MEDIUM = 2;
    public static final int STRENGTH_NONE = 0;
    private static int uniqueId;
    public float computedValue;
    int definitionId;
    public int id;
    ArrayRow[] mClientEquations;
    int mClientEquationsCount;
    private String mName;
    Type mType;
    public int strength;
    float[] strengthVector;
    
    static {
        SolverVariable.uniqueId = 1;
    }
    
    public SolverVariable(final Type mType) {
        this.id = -1;
        this.definitionId = -1;
        this.strength = 0;
        this.strengthVector = new float[6];
        this.mClientEquations = new ArrayRow[8];
        this.mClientEquationsCount = 0;
        this.mType = mType;
    }
    
    public SolverVariable(final String mName, final Type mType) {
        this.id = -1;
        this.definitionId = -1;
        this.strength = 0;
        this.strengthVector = new float[6];
        this.mClientEquations = new ArrayRow[8];
        this.mClientEquationsCount = 0;
        this.mName = mName;
        this.mType = mType;
    }
    
    private static String getUniqueName(final Type type) {
        ++SolverVariable.uniqueId;
        switch (type) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("V");
                sb.append(SolverVariable.uniqueId);
                return sb.toString();
            }
            case ERROR: {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("e");
                sb2.append(SolverVariable.uniqueId);
                return sb2.toString();
            }
            case SLACK: {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("S");
                sb3.append(SolverVariable.uniqueId);
                return sb3.toString();
            }
            case CONSTANT: {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("C");
                sb4.append(SolverVariable.uniqueId);
                return sb4.toString();
            }
            case UNRESTRICTED: {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("U");
                sb5.append(SolverVariable.uniqueId);
                return sb5.toString();
            }
        }
    }
    
    void addClientEquation(final ArrayRow arrayRow) {
        int n = 0;
        while (true) {
            final int mClientEquationsCount = this.mClientEquationsCount;
            if (n >= mClientEquationsCount) {
                final ArrayRow[] mClientEquations = this.mClientEquations;
                if (mClientEquationsCount >= mClientEquations.length) {
                    this.mClientEquations = Arrays.copyOf(mClientEquations, mClientEquations.length * 2);
                }
                final ArrayRow[] mClientEquations2 = this.mClientEquations;
                final int mClientEquationsCount2 = this.mClientEquationsCount;
                mClientEquations2[mClientEquationsCount2] = arrayRow;
                this.mClientEquationsCount = mClientEquationsCount2 + 1;
                return;
            }
            if (this.mClientEquations[n] == arrayRow) {
                return;
            }
            ++n;
        }
    }
    
    void clearStrengths() {
        for (int i = 0; i < 6; ++i) {
            this.strengthVector[i] = 0.0f;
        }
    }
    
    public String getName() {
        return this.mName;
    }
    
    void removeClientEquation(final ArrayRow arrayRow) {
        for (int i = 0; i < this.mClientEquationsCount; ++i) {
            if (this.mClientEquations[i] == arrayRow) {
                int n = 0;
                int mClientEquationsCount;
                while (true) {
                    mClientEquationsCount = this.mClientEquationsCount;
                    if (n >= mClientEquationsCount - i - 1) {
                        break;
                    }
                    final ArrayRow[] mClientEquations = this.mClientEquations;
                    mClientEquations[i + n] = mClientEquations[i + n + 1];
                    ++n;
                }
                this.mClientEquationsCount = mClientEquationsCount - 1;
                return;
            }
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
    
    public void setName(final String mName) {
        this.mName = mName;
    }
    
    public void setType(final Type mType) {
        this.mType = mType;
    }
    
    String strengthsToString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this);
        sb.append("[");
        String s = sb.toString();
        for (int i = 0; i < this.strengthVector.length; ++i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(this.strengthVector[i]);
            final String string = sb2.toString();
            if (i < this.strengthVector.length - 1) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(string);
                sb3.append(", ");
                s = sb3.toString();
            }
            else {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(string);
                sb4.append("] ");
                s = sb4.toString();
            }
        }
        return s;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.mName);
        return sb.toString();
    }
    
    public enum Type
    {
        CONSTANT, 
        ERROR, 
        SLACK, 
        UNKNOWN, 
        UNRESTRICTED;
    }
}
