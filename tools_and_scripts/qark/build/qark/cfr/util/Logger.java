/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.io.PrintStream;
import java.util.Hashtable;
import util.LoggerInterface;

public class Logger
implements LoggerInterface {
    private static Hashtable _loggers = new Hashtable();
    private static LoggerInterface m_default = new Logger();
    private static LoggerInterface m_logger;

    public static LoggerInterface getLogger() {
        if (m_logger != null) {
            return m_logger;
        }
        return m_default;
    }

    public static LoggerInterface getLogger(String object) {
        if ((object = (LoggerInterface)_loggers.get(object)) != null) {
            return object;
        }
        return Logger.getLogger();
    }

    public static void removeLogger(String string2) {
        _loggers.remove(string2);
    }

    public static void setLogger(LoggerInterface loggerInterface) {
        m_logger = loggerInterface;
    }

    public static void setLogger(LoggerInterface loggerInterface, String string2) {
        _loggers.put(string2, loggerInterface);
    }

    @Override
    public void closeLogger() {
    }

    @Override
    public void log(String string2) {
        System.out.print(string2);
    }

    @Override
    public void logException(Exception exception) {
        exception.printStackTrace();
    }

    @Override
    public void logLine(String string2) {
        System.out.println(string2);
    }

    @Override
    public void message(String string2) {
        System.out.println(string2);
    }
}

