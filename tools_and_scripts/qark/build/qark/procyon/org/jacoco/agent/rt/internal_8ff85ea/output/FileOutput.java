// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.output;

import org.jacoco.agent.rt.internal_8ff85ea.core.data.ISessionInfoVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.data.IExecutionDataVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.data.ExecutionDataWriter;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.AgentOptions;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.File;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.RuntimeData;

public class FileOutput implements IAgentOutput
{
    private boolean append;
    private RuntimeData data;
    private File destFile;
    
    private OutputStream openFile() throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(this.destFile, this.append);
        fileOutputStream.getChannel().lock();
        return fileOutputStream;
    }
    
    @Override
    public void shutdown() throws IOException {
    }
    
    @Override
    public final void startup(final AgentOptions agentOptions, final RuntimeData data) throws IOException {
        this.data = data;
        this.destFile = new File(agentOptions.getDestfile()).getAbsoluteFile();
        this.append = agentOptions.getAppend();
        final File parentFile = this.destFile.getParentFile();
        if (parentFile != null) {
            parentFile.mkdirs();
        }
        this.openFile().close();
    }
    
    @Override
    public void writeExecutionData(final boolean b) throws IOException {
        final OutputStream openFile = this.openFile();
        try {
            final ExecutionDataWriter executionDataWriter = new ExecutionDataWriter(openFile);
            this.data.collect(executionDataWriter, executionDataWriter, b);
        }
        finally {
            openFile.close();
        }
    }
}
