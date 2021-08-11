// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.runtime;

import java.io.IOException;
import java.io.InputStream;
import org.jacoco.agent.rt.internal_8ff85ea.core.data.ExecutionDataReader;

public class RemoteControlReader extends ExecutionDataReader
{
    private IRemoteCommandVisitor remoteCommandVisitor;
    
    public RemoteControlReader(final InputStream inputStream) throws IOException {
        super(inputStream);
    }
    
    private void readDumpCommand() throws IOException {
        if (this.remoteCommandVisitor != null) {
            this.remoteCommandVisitor.visitDumpCommand(this.in.readBoolean(), this.in.readBoolean());
            return;
        }
        throw new IOException("No remote command visitor.");
    }
    
    @Override
    protected boolean readBlock(final byte b) throws IOException {
        if (b == 32) {
            return false;
        }
        if (b != 64) {
            return super.readBlock(b);
        }
        this.readDumpCommand();
        return true;
    }
    
    public void setRemoteCommandVisitor(final IRemoteCommandVisitor remoteCommandVisitor) {
        this.remoteCommandVisitor = remoteCommandVisitor;
    }
}
