// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.runtime;

public interface IRuntime extends IExecutionDataAccessorGenerator
{
    void shutdown();
    
    void startup(final RuntimeData p0) throws Exception;
}
