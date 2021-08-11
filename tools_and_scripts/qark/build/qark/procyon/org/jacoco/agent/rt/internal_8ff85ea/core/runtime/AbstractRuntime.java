// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.runtime;

import java.util.Random;

public abstract class AbstractRuntime implements IRuntime
{
    private static final Random RANDOM;
    protected RuntimeData data;
    
    static {
        RANDOM = new Random();
    }
    
    public static String createRandomId() {
        return Integer.toHexString(AbstractRuntime.RANDOM.nextInt());
    }
    
    @Override
    public void startup(final RuntimeData data) throws Exception {
        this.data = data;
    }
}
