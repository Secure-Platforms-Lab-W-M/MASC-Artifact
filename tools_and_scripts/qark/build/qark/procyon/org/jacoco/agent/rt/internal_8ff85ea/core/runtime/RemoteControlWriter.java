// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.runtime;

import java.io.IOException;
import java.io.OutputStream;
import org.jacoco.agent.rt.internal_8ff85ea.core.data.ExecutionDataWriter;

public class RemoteControlWriter extends ExecutionDataWriter implements IRemoteCommandVisitor
{
    public static final byte BLOCK_CMDDUMP = 64;
    public static final byte BLOCK_CMDOK = 32;
    
    public RemoteControlWriter(final OutputStream outputStream) throws IOException {
        super(outputStream);
    }
    
    public void sendCmdOk() throws IOException {
        this.out.writeByte(32);
    }
    
    @Override
    public void visitDumpCommand(final boolean b, final boolean b2) throws IOException {
        this.out.writeByte(64);
        this.out.writeBoolean(b);
        this.out.writeBoolean(b2);
    }
}
