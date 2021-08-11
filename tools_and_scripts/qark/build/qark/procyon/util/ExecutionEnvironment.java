// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.io.IOException;
import java.io.InputStream;

public class ExecutionEnvironment implements ExecutionEnvironmentInterface
{
    private static ExecutionEnvironmentInterface m_Env;
    private static ExecutionEnvironmentInterface m_default;
    
    static {
        ExecutionEnvironment.m_default = new ExecutionEnvironment();
    }
    
    public static ExecutionEnvironmentInterface getEnvironment() {
        if (ExecutionEnvironment.m_Env != null) {
            return ExecutionEnvironment.m_Env;
        }
        return ExecutionEnvironment.m_default;
    }
    
    public static void setEnvironment(final ExecutionEnvironmentInterface env) {
        ExecutionEnvironment.m_Env = env;
    }
    
    @Override
    public boolean debug() {
        return false;
    }
    
    @Override
    public InputStream getAsset(final String s) throws IOException {
        throw new IOException("Not supported!");
    }
    
    @Override
    public String getWorkDir() {
        return "./";
    }
    
    @Override
    public boolean hasNetwork() {
        return true;
    }
    
    @Override
    public void onReload() throws IOException {
    }
    
    @Override
    public void releaseAllWakeLocks() {
    }
    
    @Override
    public void releaseWakeLock() {
    }
    
    @Override
    public void wakeLock() {
    }
}
