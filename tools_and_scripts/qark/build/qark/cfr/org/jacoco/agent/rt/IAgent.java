/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt;

import java.io.IOException;

public interface IAgent {
    public void dump(boolean var1) throws IOException;

    public byte[] getExecutionData(boolean var1);

    public String getSessionId();

    public String getVersion();

    public void reset();

    public void setSessionId(String var1);
}

