// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea;

import java.lang.instrument.ClassFileTransformer;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.AgentOptions;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.ModifiedSystemClassRuntime;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.IRuntime;
import java.lang.instrument.Instrumentation;

public final class PreMain
{
    private PreMain() {
    }
    
    private static IRuntime createRuntime(final Instrumentation instrumentation) throws Exception {
        return ModifiedSystemClassRuntime.createFor(instrumentation, "java/util/UUID");
    }
    
    public static void premain(final String s, final Instrumentation instrumentation) throws Exception {
        final AgentOptions agentOptions = new AgentOptions(s);
        final Agent instance = Agent.getInstance(agentOptions);
        final IRuntime runtime = createRuntime(instrumentation);
        runtime.startup(instance.getData());
        instrumentation.addTransformer(new CoverageTransformer(runtime, agentOptions, IExceptionLogger.SYSTEM_ERR));
    }
}
