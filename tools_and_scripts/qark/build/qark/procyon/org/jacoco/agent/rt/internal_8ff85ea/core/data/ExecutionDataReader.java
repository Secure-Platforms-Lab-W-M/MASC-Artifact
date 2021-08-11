// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.data;

import java.io.IOException;
import java.io.InputStream;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.data.CompactDataInput;

public class ExecutionDataReader
{
    private IExecutionDataVisitor executionDataVisitor;
    private boolean firstBlock;
    protected final CompactDataInput in;
    private ISessionInfoVisitor sessionInfoVisitor;
    
    public ExecutionDataReader(final InputStream inputStream) {
        this.sessionInfoVisitor = null;
        this.executionDataVisitor = null;
        this.firstBlock = true;
        this.in = new CompactDataInput(inputStream);
    }
    
    private void readExecutionData() throws IOException {
        if (this.executionDataVisitor != null) {
            this.executionDataVisitor.visitClassExecution(new ExecutionData(this.in.readLong(), this.in.readUTF(), this.in.readBooleanArray()));
            return;
        }
        throw new IOException("No execution data visitor.");
    }
    
    private void readHeader() throws IOException {
        if (this.in.readChar() != '\uc0c0') {
            throw new IOException("Invalid execution data file.");
        }
        final char char1 = this.in.readChar();
        if (char1 == ExecutionDataWriter.FORMAT_VERSION) {
            return;
        }
        throw new IncompatibleExecDataVersionException(char1);
    }
    
    private void readSessionInfo() throws IOException {
        if (this.sessionInfoVisitor != null) {
            this.sessionInfoVisitor.visitSessionInfo(new SessionInfo(this.in.readUTF(), this.in.readLong(), this.in.readLong()));
            return;
        }
        throw new IOException("No session info visitor.");
    }
    
    public boolean read() throws IOException, IncompatibleExecDataVersionException {
        byte b;
        do {
            final int read = this.in.read();
            if (read == -1) {
                return false;
            }
            b = (byte)read;
            if (this.firstBlock && b != 1) {
                throw new IOException("Invalid execution data file.");
            }
            this.firstBlock = false;
        } while (this.readBlock(b));
        return true;
    }
    
    protected boolean readBlock(final byte b) throws IOException {
        if (b == 1) {
            this.readHeader();
            return true;
        }
        switch (b) {
            default: {
                throw new IOException(String.format("Unknown block type %x.", b));
            }
            case 17: {
                this.readExecutionData();
                return true;
            }
            case 16: {
                this.readSessionInfo();
                return true;
            }
        }
    }
    
    public void setExecutionDataVisitor(final IExecutionDataVisitor executionDataVisitor) {
        this.executionDataVisitor = executionDataVisitor;
    }
    
    public void setSessionInfoVisitor(final ISessionInfoVisitor sessionInfoVisitor) {
        this.sessionInfoVisitor = sessionInfoVisitor;
    }
}
