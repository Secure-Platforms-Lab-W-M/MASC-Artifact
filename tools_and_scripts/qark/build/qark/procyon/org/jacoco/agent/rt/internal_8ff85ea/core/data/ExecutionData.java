// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.data;

import java.util.Arrays;

public final class ExecutionData
{
    private final long id;
    private final String name;
    private final boolean[] probes;
    
    public ExecutionData(final long id, final String name, final int n) {
        this.id = id;
        this.name = name;
        this.probes = new boolean[n];
    }
    
    public ExecutionData(final long id, final String name, final boolean[] probes) {
        this.id = id;
        this.name = name;
        this.probes = probes;
    }
    
    public void assertCompatibility(final long n, final String s, final int n2) throws IllegalStateException {
        if (this.id != n) {
            throw new IllegalStateException(String.format("Different ids (%016x and %016x).", this.id, n));
        }
        if (!this.name.equals(s)) {
            throw new IllegalStateException(String.format("Different class names %s and %s for id %016x.", this.name, s, n));
        }
        if (this.probes.length == n2) {
            return;
        }
        throw new IllegalStateException(String.format("Incompatible execution data for class %s with id %016x.", s, n));
    }
    
    public long getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean[] getProbes() {
        return this.probes;
    }
    
    public boolean hasHits() {
        final boolean[] probes = this.probes;
        for (int length = probes.length, i = 0; i < length; ++i) {
            if (probes[i]) {
                return true;
            }
        }
        return false;
    }
    
    public void merge(final ExecutionData executionData) {
        this.merge(executionData, true);
    }
    
    public void merge(final ExecutionData executionData, final boolean b) {
        this.assertCompatibility(executionData.getId(), executionData.getName(), executionData.getProbes().length);
        final boolean[] probes = executionData.getProbes();
        for (int i = 0; i < this.probes.length; ++i) {
            if (probes[i]) {
                this.probes[i] = b;
            }
        }
    }
    
    public void reset() {
        Arrays.fill(this.probes, false);
    }
    
    @Override
    public String toString() {
        return String.format("ExecutionData[name=%s, id=%016x]", this.name, this.id);
    }
}
