// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.util.Hashtable;

public class Logger implements LoggerInterface
{
    private static Hashtable _loggers;
    private static LoggerInterface m_default;
    private static LoggerInterface m_logger;
    
    static {
        Logger._loggers = new Hashtable();
        Logger.m_default = new Logger();
    }
    
    public static LoggerInterface getLogger() {
        if (Logger.m_logger != null) {
            return Logger.m_logger;
        }
        return Logger.m_default;
    }
    
    public static LoggerInterface getLogger(final String s) {
        final LoggerInterface loggerInterface = Logger._loggers.get(s);
        if (loggerInterface != null) {
            return loggerInterface;
        }
        return getLogger();
    }
    
    public static void removeLogger(final String s) {
        Logger._loggers.remove(s);
    }
    
    public static void setLogger(final LoggerInterface logger) {
        Logger.m_logger = logger;
    }
    
    public static void setLogger(final LoggerInterface loggerInterface, final String s) {
        Logger._loggers.put(s, loggerInterface);
    }
    
    @Override
    public void closeLogger() {
    }
    
    @Override
    public void log(final String s) {
        System.out.print(s);
    }
    
    @Override
    public void logException(final Exception ex) {
        ex.printStackTrace();
    }
    
    @Override
    public void logLine(final String s) {
        System.out.println(s);
    }
    
    @Override
    public void message(final String s) {
        System.out.println(s);
    }
}
