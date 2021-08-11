/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import org.jacoco.agent.rt.internal_8ff85ea.Agent;
import org.jacoco.agent.rt.internal_8ff85ea.CoverageTransformer;
import org.jacoco.agent.rt.internal_8ff85ea.IExceptionLogger;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.AgentOptions;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.IRuntime;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.ModifiedSystemClassRuntime;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.RuntimeData;

public final class PreMain {
    private PreMain() {
    }

    private static IRuntime createRuntime(Instrumentation instrumentation) throws Exception {
        return ModifiedSystemClassRuntime.createFor(instrumentation, "java/util/UUID");
    }

    public static void premain(String object, Instrumentation instrumentation) throws Exception {
        object = new AgentOptions((String)object);
        Agent agent = Agent.getInstance((AgentOptions)object);
        IRuntime iRuntime = PreMain.createRuntime(instrumentation);
        iRuntime.startup(agent.getData());
        instrumentation.addTransformer(new CoverageTransformer(iRuntime, (AgentOptions)object, IExceptionLogger.SYSTEM_ERR));
    }
}

