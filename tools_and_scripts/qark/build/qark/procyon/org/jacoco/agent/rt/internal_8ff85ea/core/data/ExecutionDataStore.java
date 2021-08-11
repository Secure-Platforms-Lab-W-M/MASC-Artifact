// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

public final class ExecutionDataStore implements IExecutionDataVisitor
{
    private final Map<Long, ExecutionData> entries;
    private final Set<String> names;
    
    public ExecutionDataStore() {
        this.entries = new HashMap<Long, ExecutionData>();
        this.names = new HashSet<String>();
    }
    
    public void accept(final IExecutionDataVisitor executionDataVisitor) {
        final Iterator<ExecutionData> iterator = this.getContents().iterator();
        while (iterator.hasNext()) {
            executionDataVisitor.visitClassExecution(iterator.next());
        }
    }
    
    public boolean contains(final String s) {
        return this.names.contains(s);
    }
    
    public ExecutionData get(final long n) {
        return this.entries.get(n);
    }
    
    public ExecutionData get(final Long n, final String s, final int n2) {
        final ExecutionData executionData = this.entries.get(n);
        if (executionData == null) {
            final ExecutionData executionData2 = new ExecutionData(n, s, n2);
            this.entries.put(n, executionData2);
            this.names.add(s);
            return executionData2;
        }
        executionData.assertCompatibility(n, s, n2);
        return executionData;
    }
    
    public Collection<ExecutionData> getContents() {
        return new ArrayList<ExecutionData>(this.entries.values());
    }
    
    public void put(final ExecutionData executionData) throws IllegalStateException {
        final Long value = executionData.getId();
        final ExecutionData executionData2 = this.entries.get(value);
        if (executionData2 == null) {
            this.entries.put(value, executionData);
            this.names.add(executionData.getName());
            return;
        }
        executionData2.merge(executionData);
    }
    
    public void reset() {
        final Iterator<ExecutionData> iterator = this.entries.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().reset();
        }
    }
    
    public void subtract(final ExecutionData executionData) throws IllegalStateException {
        final ExecutionData executionData2 = this.entries.get(executionData.getId());
        if (executionData2 != null) {
            executionData2.merge(executionData, false);
        }
    }
    
    public void subtract(final ExecutionDataStore executionDataStore) {
        final Iterator<ExecutionData> iterator = executionDataStore.getContents().iterator();
        while (iterator.hasNext()) {
            this.subtract(iterator.next());
        }
    }
    
    @Override
    public void visitClassExecution(final ExecutionData executionData) {
        this.put(executionData);
    }
}
