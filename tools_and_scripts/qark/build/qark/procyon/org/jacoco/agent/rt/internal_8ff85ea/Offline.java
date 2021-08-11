// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea;

import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.AgentOptions;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.RuntimeData;

public final class Offline
{
    private static final String CONFIG_RESOURCE = "/jacoco-agent.properties";
    private static final RuntimeData DATA;
    
    static {
        DATA = Agent.getInstance(new AgentOptions(ConfigLoader.load("/jacoco-agent.properties", System.getProperties()))).getData();
    }
    
    private Offline() {
    }
    
    public static boolean[] getProbes(final long n, final String s, final int n2) {
        return Offline.DATA.getExecutionData(n, s, n2).getProbes();
    }
}
