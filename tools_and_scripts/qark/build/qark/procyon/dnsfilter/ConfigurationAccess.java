// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter;

import java.util.Properties;
import java.io.IOException;
import dnsfilter.remote.RemoteAccessClient;
import util.LoggerInterface;

public abstract class ConfigurationAccess
{
    public static ConfigurationAccess getLocal() {
        return DNSFilterManager.getInstance();
    }
    
    public static ConfigurationAccess getRemote(final LoggerInterface loggerInterface, final String s, final int n, final String s2) throws IOException {
        return new RemoteAccessClient(loggerInterface, s, n, s2);
    }
    
    public abstract void doBackup(final String p0) throws IOException;
    
    public abstract void doRestore(final String p0) throws IOException;
    
    public abstract void doRestoreDefaults() throws IOException;
    
    public abstract byte[] getAdditionalHosts(final int p0) throws IOException;
    
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
    
    @Override
    public String toString() {
        return "LOCAL";
    }
    
    public abstract void triggerUpdateFilter() throws IOException;
    
    public abstract void updateAdditionalHosts(final byte[] p0) throws IOException;
    
    public abstract void updateConfig(final byte[] p0) throws IOException;
    
    public abstract void updateFilter(final String p0, final boolean p1) throws IOException;
    
    public abstract void wakeLock() throws IOException;
    
    public static class ConfigurationAccessException extends IOException
    {
        public ConfigurationAccessException(final String s) {
            super(s);
        }
        
        public ConfigurationAccessException(final String s, final IOException ex) {
            super(s, ex);
        }
    }
}
