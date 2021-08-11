/*
 * Decompiled with CFR 0_124.
 */
package dnsfilter;

import dnsfilter.DNSFilterManager;
import dnsfilter.remote.RemoteAccessClient;
import java.io.IOException;
import java.util.Properties;
import util.LoggerInterface;

public abstract class ConfigurationAccess {
    public static ConfigurationAccess getLocal() {
        return DNSFilterManager.getInstance();
    }

    public static ConfigurationAccess getRemote(LoggerInterface loggerInterface, String string2, int n, String string3) throws IOException {
        return new RemoteAccessClient(loggerInterface, string2, n, string3);
    }

    public abstract void doBackup(String var1) throws IOException;

    public abstract void doRestore(String var1) throws IOException;

    public abstract void doRestoreDefaults() throws IOException;

    public abstract byte[] getAdditionalHosts(int var1) throws IOException;

    public abstract String[] getAvailableBackups() throws IOException;

    public abstract Properties getConfig() throws IOException;

    public abstract long[] getFilterStatistics() throws IOException;

    public abstract String getLastDNSAddress() throws IOException;

    public abstract String getVersion() throws IOException;

    public boolean isLocal() {
        return true;
    }

    public abstract int openConnectionsCount() throws IOException;

    public abstract byte[] readConfig() throws IOException;

    public abstract void releaseConfiguration();

    public abstract void releaseWakeLock() throws IOException;

    public abstract void restart() throws IOException;

    public abstract void stop() throws IOException;

    public String toString() {
        return "LOCAL";
    }

    public abstract void triggerUpdateFilter() throws IOException;

    public abstract void updateAdditionalHosts(byte[] var1) throws IOException;

    public abstract void updateConfig(byte[] var1) throws IOException;

    public abstract void updateFilter(String var1, boolean var2) throws IOException;

    public abstract void wakeLock() throws IOException;

    public static class ConfigurationAccessException
    extends IOException {
        public ConfigurationAccessException(String string2) {
            super(string2);
        }

        public ConfigurationAccessException(String string2, IOException iOException) {
            super(string2, iOException);
        }
    }

}

