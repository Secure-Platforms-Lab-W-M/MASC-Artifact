/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea;

import java.util.Properties;
import org.jacoco.agent.rt.internal_8ff85ea.Agent;
import org.jacoco.agent.rt.internal_8ff85ea.ConfigLoader;
import org.jacoco.agent.rt.internal_8ff85ea.core.data.ExecutionData;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.AgentOptions;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.RuntimeData;

public final class Offline {
    private static final String CONFIG_RESOURCE = "/jacoco-agent.properties";
    private static final RuntimeData DATA = Agent.getInstance(new AgentOptions(ConfigLoader.load("/jacoco-agent.properties", System.getProperties()))).getData();

    private Offline() {
    }

    public static boolean[] getProbes(long l, String string2, int n) {
        return DATA.getExecutionData(l, string2, n).getProbes();
    }
}

