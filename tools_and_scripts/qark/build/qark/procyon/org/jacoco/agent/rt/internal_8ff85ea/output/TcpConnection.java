// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.output;

import org.jacoco.agent.rt.internal_8ff85ea.core.data.ISessionInfoVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.data.IExecutionDataVisitor;
import java.net.SocketException;
import java.io.IOException;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.RemoteControlWriter;
import java.net.Socket;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.RemoteControlReader;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.RuntimeData;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.IRemoteCommandVisitor;

class TcpConnection implements IRemoteCommandVisitor
{
    private final RuntimeData data;
    private boolean initialized;
    private RemoteControlReader reader;
    private final Socket socket;
    private RemoteControlWriter writer;
    
    public TcpConnection(final Socket socket, final RuntimeData data) {
        this.socket = socket;
        this.data = data;
        this.initialized = false;
    }
    
    public void close() throws IOException {
        if (!this.socket.isClosed()) {
            this.socket.close();
        }
    }
    
    public void init() throws IOException {
        this.writer = new RemoteControlWriter(this.socket.getOutputStream());
        (this.reader = new RemoteControlReader(this.socket.getInputStream())).setRemoteCommandVisitor(this);
        this.initialized = true;
    }
    
    public void run() throws IOException {
        while (true) {
            try {
                try {
                    while (this.reader.read()) {}
                    this.close();
                    return;
                }
                finally {}
            }
            catch (SocketException ex) {
                if (this.socket.isClosed()) {
                    continue;
                }
                throw ex;
            }
            break;
        }
        this.close();
    }
    
    @Override
    public void visitDumpCommand(final boolean b, final boolean b2) throws IOException {
        if (b) {
            this.data.collect(this.writer, this.writer, b2);
        }
        else if (b2) {
            this.data.reset();
        }
        this.writer.sendCmdOk();
    }
    
    public void writeExecutionData(final boolean b) throws IOException {
        if (this.initialized && !this.socket.isClosed()) {
            this.visitDumpCommand(true, b);
        }
    }
}
