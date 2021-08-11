// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.data;

import java.io.IOException;

public class IncompatibleExecDataVersionException extends IOException
{
    private static final long serialVersionUID = 1L;
    private final int actualVersion;
    
    public IncompatibleExecDataVersionException(final int actualVersion) {
        super(String.format("Cannot read execution data version 0x%x. This version of JaCoCo uses execution data version 0x%x.", actualVersion, (int)ExecutionDataWriter.FORMAT_VERSION));
        this.actualVersion = actualVersion;
    }
    
    public int getActualVersion() {
        return this.actualVersion;
    }
    
    public int getExpectedVersion() {
        return ExecutionDataWriter.FORMAT_VERSION;
    }
}
