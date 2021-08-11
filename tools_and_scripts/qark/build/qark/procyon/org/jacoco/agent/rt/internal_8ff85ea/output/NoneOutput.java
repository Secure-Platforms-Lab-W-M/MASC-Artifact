// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.output;

import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.RuntimeData;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.AgentOptions;

public class NoneOutput implements IAgentOutput
{
    @Override
    public void shutdown() {
    }
    
    @Override
    public final void startup(final AgentOptions agentOptions, final RuntimeData runtimeData) {
    }
    
    @Override
    public void writeExecutionData(final boolean b) {
    }
}
