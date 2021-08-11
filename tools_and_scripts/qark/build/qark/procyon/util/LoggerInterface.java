// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

public interface LoggerInterface
{
    void closeLogger();
    
    void log(final String p0);
    
    void logException(final Exception p0);
    
    void logLine(final String p0);
    
    void message(final String p0);
}
