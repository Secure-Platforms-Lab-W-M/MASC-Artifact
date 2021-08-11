// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow;

public class Instruction
{
    private int branches;
    private int coveredBranches;
    private final int line;
    private Instruction predecessor;
    
    public Instruction(final int line) {
        this.line = line;
        this.branches = 0;
        this.coveredBranches = 0;
    }
    
    public void addBranch() {
        ++this.branches;
    }
    
    public int getBranches() {
        return this.branches;
    }
    
    public int getCoveredBranches() {
        return this.coveredBranches;
    }
    
    public int getLine() {
        return this.line;
    }
    
    public void setCovered() {
        for (Instruction predecessor = this; predecessor != null && predecessor.coveredBranches++ == 0; predecessor = predecessor.predecessor) {}
    }
    
    public void setPredecessor(final Instruction predecessor) {
        (this.predecessor = predecessor).addBranch();
    }
}
