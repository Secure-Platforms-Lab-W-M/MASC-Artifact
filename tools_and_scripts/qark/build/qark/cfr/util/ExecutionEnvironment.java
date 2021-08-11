/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.io.IOException;
import java.io.InputStream;
import util.ExecutionEnvironmentInterface;

public class ExecutionEnvironment
implements ExecutionEnvironmentInterface {
    private static ExecutionEnvironmentInterface m_Env;
    private static ExecutionEnvironmentInterface m_default;

    static {
        m_default = new ExecutionEnvironment();
    }

    public static ExecutionEnvironmentInterface getEnvironment() {
        if (m_Env != null) {
            return m_Env;
        }
        return m_default;
    }

    public static void setEnvironment(ExecutionEnvironmentInterface executionEnvironmentInterface) {
        m_Env = executionEnvironmentInterface;
    }

    @Override
    public boolean debug() {
        return false;
    }

    @Override
    public InputStream getAsset(String string2) throws IOException {
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

