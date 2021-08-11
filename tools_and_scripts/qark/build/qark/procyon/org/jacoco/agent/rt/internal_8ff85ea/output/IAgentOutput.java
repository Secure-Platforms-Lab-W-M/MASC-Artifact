// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.output;

import java.io.IOException;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.RuntimeData;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.AgentOptions;

public interface IAgentOutput
{
    void shutdown() throws Exception;
    
    void startup(final AgentOptions p0, final RuntimeData p1) throws Exception;
    
    void writeExecutionData(final boolean p0) throws IOException;
}
