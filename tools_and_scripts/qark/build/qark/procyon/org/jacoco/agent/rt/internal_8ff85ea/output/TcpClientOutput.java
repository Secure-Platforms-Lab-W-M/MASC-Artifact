// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.output;

import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.RuntimeData;
import java.io.IOException;
import java.net.Socket;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.AgentOptions;
import org.jacoco.agent.rt.internal_8ff85ea.IExceptionLogger;

public class TcpClientOutput implements IAgentOutput
{
    private TcpConnection connection;
    private final IExceptionLogger logger;
    private Thread worker;
    
    public TcpClientOutput(final IExceptionLogger logger) {
        this.logger = logger;
    }
    
    protected Socket createSocket(final AgentOptions agentOptions) throws IOException {
        return new Socket(agentOptions.getAddress(), agentOptions.getPort());
    }
    
    @Override
    public void shutdown() throws Exception {
        this.connection.close();
        this.worker.join();
    }
    
    @Override
    public void startup(final AgentOptions agentOptions, final RuntimeData runtimeData) throws IOException {
        (this.connection = new TcpConnection(this.createSocket(agentOptions), runtimeData)).init();
        (this.worker = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TcpClientOutput.this.connection.run();
                }
                catch (IOException ex) {
                    TcpClientOutput.this.logger.logExeption(ex);
                }
            }
        })).setName(this.getClass().getName());
        this.worker.setDaemon(true);
        this.worker.start();
    }
    
    @Override
    public void writeExecutionData(final boolean b) throws IOException {
        this.connection.writeExecutionData(b);
    }
}
