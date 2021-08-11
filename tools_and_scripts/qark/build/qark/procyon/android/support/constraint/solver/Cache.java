// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver;

public class Cache
{
    Pools.Pool<ArrayRow> arrayRowPool;
    SolverVariable[] mIndexedVariables;
    Pools.Pool<SolverVariable> solverVariablePool;
    
    public Cache() {
        this.arrayRowPool = new Pools.SimplePool<ArrayRow>(256);
        this.solverVariablePool = new Pools.SimplePool<SolverVariable>(256);
        this.mIndexedVariables = new SolverVariable[32];
    }
}
