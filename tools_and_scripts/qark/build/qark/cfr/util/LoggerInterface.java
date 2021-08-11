/*
 * Decompiled with CFR 0_124.
 */
package util;

public interface LoggerInterface {
    public void closeLogger();

    public void log(String var1);

    public void logException(Exception var1);

    public void logLine(String var1);

    public void message(String var1);
}

