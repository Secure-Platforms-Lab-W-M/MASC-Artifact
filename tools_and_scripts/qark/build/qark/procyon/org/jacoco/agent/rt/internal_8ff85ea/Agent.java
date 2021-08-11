// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea;

import org.jacoco.agent.rt.internal_8ff85ea.core.JaCoCo;
import org.jacoco.agent.rt.internal_8ff85ea.core.data.ISessionInfoVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.data.IExecutionDataVisitor;
import java.io.OutputStream;
import org.jacoco.agent.rt.internal_8ff85ea.core.data.ExecutionDataWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jacoco.agent.rt.internal_8ff85ea.output.FileOutput;
import org.jacoco.agent.rt.internal_8ff85ea.output.TcpServerOutput;
import org.jacoco.agent.rt.internal_8ff85ea.output.TcpClientOutput;
import org.jacoco.agent.rt.internal_8ff85ea.output.NoneOutput;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.AbstractRuntime;
import java.net.InetAddress;
import org.jacoco.agent.rt.internal_8ff85ea.output.IAgentOutput;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.AgentOptions;
import java.util.concurrent.Callable;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.RuntimeData;
import org.jacoco.agent.rt.IAgent;

public class Agent implements IAgent
{
    private static Agent singleton;
    private final RuntimeData data;
    private Callable<Void> jmxRegistration;
    private final IExceptionLogger logger;
    private final AgentOptions options;
    private IAgentOutput output;
    
    Agent(final AgentOptions options, final IExceptionLogger logger) {
        this.options = options;
        this.logger = logger;
        this.data = new RuntimeData();
    }
    
    private String createSessionId() {
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        }
        catch (Exception ex) {
            hostName = "unknownhost";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(hostName);
        sb.append("-");
        sb.append(AbstractRuntime.createRandomId());
        return sb.toString();
    }
    
    public static Agent getInstance() throws IllegalStateException {
        synchronized (Agent.class) {
            if (Agent.singleton != null) {
                return Agent.singleton;
            }
            throw new IllegalStateException("JaCoCo agent not started.");
        }
    }
    
    public static Agent getInstance(final AgentOptions agentOptions) {
        synchronized (Agent.class) {
            if (Agent.singleton == null) {
                final Agent singleton = new Agent(agentOptions, IExceptionLogger.SYSTEM_ERR);
                singleton.startup();
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        singleton.shutdown();
                    }
                });
                Agent.singleton = singleton;
            }
            return Agent.singleton;
        }
    }
    
    IAgentOutput createAgentOutput() {
        final AgentOptions.OutputMode output = this.options.getOutput();
        switch (output) {
            default: {
                throw new AssertionError(output);
            }
            case none: {
                return new NoneOutput();
            }
            case tcpclient: {
                return new TcpClientOutput(this.logger);
            }
            case tcpserver: {
                return new TcpServerOutput(this.logger);
            }
            case file: {
                return new FileOutput();
            }
        }
    }
    
    @Override
    public void dump(final boolean b) throws IOException {
        this.output.writeExecutionData(b);
    }
    
    public RuntimeData getData() {
        return this.data;
    }
    
    @Override
    public byte[] getExecutionData(final boolean b) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            final ExecutionDataWriter executionDataWriter = new ExecutionDataWriter(byteArrayOutputStream);
            this.data.collect(executionDataWriter, executionDataWriter, b);
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    @Override
    public String getSessionId() {
        return this.data.getSessionId();
    }
    
    @Override
    public String getVersion() {
        return JaCoCo.VERSION;
    }
    
    @Override
    public void reset() {
        this.data.reset();
    }
    
    @Override
    public void setSessionId(final String sessionId) {
        this.data.setSessionId(sessionId);
    }
    
    public void shutdown() {
        try {
            if (this.options.getDumpOnExit()) {
                this.output.writeExecutionData(false);
            }
            this.output.shutdown();
            if (this.jmxRegistration != null) {
                this.jmxRegistration.call();
            }
        }
        catch (Exception ex) {
            this.logger.logExeption(ex);
        }
    }
    
    public void startup() {
        try {
            String sessionId;
            if ((sessionId = this.options.getSessionId()) == null) {
                sessionId = this.createSessionId();
            }
            this.data.setSessionId(sessionId);
            (this.output = this.createAgentOutput()).startup(this.options, this.data);
            if (this.options.getJmx()) {
                this.jmxRegistration = new JmxRegistration(this);
            }
        }
        catch (Exception ex) {
            this.logger.logExeption(ex);
        }
    }
}
