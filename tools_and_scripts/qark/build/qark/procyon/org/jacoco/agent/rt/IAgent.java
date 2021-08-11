// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt;

import java.io.IOException;

public interface IAgent
{
    void dump(final boolean p0) throws IOException;
    
    byte[] getExecutionData(final boolean p0);
    
    String getSessionId();
    
    String getVersion();
    
    void reset();
    
    void setSessionId(final String p0);
}
