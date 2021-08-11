// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.output;

import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.RuntimeData;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.io.IOException;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.AgentOptions;
import java.net.ServerSocket;
import org.jacoco.agent.rt.internal_8ff85ea.IExceptionLogger;

public class TcpServerOutput implements IAgentOutput
{
    private TcpConnection connection;
    private final IExceptionLogger logger;
    private ServerSocket serverSocket;
    private Thread worker;
    
    public TcpServerOutput(final IExceptionLogger logger) {
        this.logger = logger;
    }
    
    protected ServerSocket createServerSocket(final AgentOptions agentOptions) throws IOException {
        return new ServerSocket(agentOptions.getPort(), 1, this.getInetAddress(agentOptions.getAddress()));
    }
    
    protected InetAddress getInetAddress(final String s) throws UnknownHostException {
        if ("*".equals(s)) {
            return null;
        }
        return InetAddress.getByName(s);
    }
    
    @Override
    public void shutdown() throws Exception {
        this.serverSocket.close();
        synchronized (this.serverSocket) {
            if (this.connection != null) {
                this.connection.close();
            }
            // monitorexit(this.serverSocket)
            this.worker.join();
        }
    }
    
    @Override
    public void startup(final AgentOptions agentOptions, final RuntimeData runtimeData) throws IOException {
        this.serverSocket = this.createServerSocket(agentOptions);
        (this.worker = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!TcpServerOutput.this.serverSocket.isClosed()) {
                    try {
                        synchronized (TcpServerOutput.this.serverSocket) {
                            TcpServerOutput.this.connection = new TcpConnection(TcpServerOutput.this.serverSocket.accept(), runtimeData);
                            // monitorexit(TcpServerOutput.access$000(this.this$0))
                            TcpServerOutput.this.connection.init();
                            TcpServerOutput.this.connection.run();
                        }
                    }
                    catch (IOException ex) {
                        if (TcpServerOutput.this.serverSocket.isClosed()) {
                            continue;
                        }
                        TcpServerOutput.this.logger.logExeption(ex);
                    }
                }
            }
        })).setName(this.getClass().getName());
        this.worker.setDaemon(true);
        this.worker.start();
    }
    
    @Override
    public void writeExecutionData(final boolean b) throws IOException {
        if (this.connection != null) {
            this.connection.writeExecutionData(b);
        }
    }
}
