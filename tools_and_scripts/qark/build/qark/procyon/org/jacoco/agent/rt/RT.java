// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt;

import org.jacoco.agent.rt.internal_8ff85ea.Agent;

public final class RT
{
    private RT() {
    }
    
    public static IAgent getAgent() throws IllegalStateException {
        return Agent.getInstance();
    }
}
