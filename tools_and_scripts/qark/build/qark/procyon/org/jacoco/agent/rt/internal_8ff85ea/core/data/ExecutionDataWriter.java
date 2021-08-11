// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.data.CompactDataOutput;

public class ExecutionDataWriter implements ISessionInfoVisitor, IExecutionDataVisitor
{
    public static final byte BLOCK_EXECUTIONDATA = 17;
    public static final byte BLOCK_HEADER = 1;
    public static final byte BLOCK_SESSIONINFO = 16;
    public static final char FORMAT_VERSION;
    public static final char MAGIC_NUMBER = '\uc0c0';
    protected final CompactDataOutput out;
    
    static {
        FORMAT_VERSION = '\u1007';
    }
    
    public ExecutionDataWriter(final OutputStream outputStream) throws IOException {
        this.out = new CompactDataOutput(outputStream);
        this.writeHeader();
    }
    
    public static final byte[] getFileHeader() {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new ExecutionDataWriter(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    private void writeHeader() throws IOException {
        this.out.writeByte(1);
        this.out.writeChar(49344);
        this.out.writeChar(ExecutionDataWriter.FORMAT_VERSION);
    }
    
    public void flush() throws IOException {
        this.out.flush();
    }
    
    @Override
    public void visitClassExecution(final ExecutionData executionData) {
        if (executionData.hasHits()) {
            try {
                this.out.writeByte(17);
                this.out.writeLong(executionData.getId());
                this.out.writeUTF(executionData.getName());
                this.out.writeBooleanArray(executionData.getProbes());
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    @Override
    public void visitSessionInfo(final SessionInfo sessionInfo) {
        try {
            this.out.writeByte(16);
            this.out.writeUTF(sessionInfo.getId());
            this.out.writeLong(sessionInfo.getStartTimeStamp());
            this.out.writeLong(sessionInfo.getDumpTimeStamp());
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
