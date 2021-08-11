package android.support.constraint.solver;

public class Cache {
   Pools.Pool arrayRowPool = new Pools.SimplePool(256);
   SolverVariable[] mIndexedVariables = new SolverVariable[32];
   Pools.Pool solverVariablePool = new Pools.SimplePool(256);
}
