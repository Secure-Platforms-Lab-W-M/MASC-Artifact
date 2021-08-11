// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.util.Vector;

public class GroupedLogger implements LoggerInterface
{
    LoggerInterface[] nestedLoggers;
    
    public GroupedLogger(final LoggerInterface[] nestedLoggers) {
        this.nestedLoggers = nestedLoggers;
    }
    
    public void attachLogger(final LoggerInterface loggerInterface) {
        final LoggerInterface[] nestedLoggers = this.nestedLoggers;
        int i = 0;
        final LoggerInterface[] nestedLoggers2 = new LoggerInterface[nestedLoggers.length + 1];
        while (i < this.nestedLoggers.length) {
            nestedLoggers2[i] = this.nestedLoggers[i];
            ++i;
        }
        nestedLoggers2[this.nestedLoggers.length] = loggerInterface;
        this.nestedLoggers = nestedLoggers2;
    }
    
    @Override
    public void closeLogger() {
        for (int i = 0; i < this.nestedLoggers.length; ++i) {
            this.nestedLoggers[i].closeLogger();
        }
    }
    
    public void detachLogger(final LoggerInterface loggerInterface) {
        final LoggerInterface[] nestedLoggers = this.nestedLoggers;
        int i = 0;
        final Vector vector = new Vector<LoggerInterface>(nestedLoggers.length);
        while (i < this.nestedLoggers.length) {
            if (this.nestedLoggers[i] != loggerInterface) {
                vector.add(this.nestedLoggers[i]);
            }
            ++i;
        }
        this.nestedLoggers = vector.toArray(new LoggerInterface[vector.size()]);
    }
    
    @Override
    public void log(final String s) {
        for (int i = 0; i < this.nestedLoggers.length; ++i) {
            this.nestedLoggers[i].log(s);
        }
    }
    
    @Override
    public void logException(final Exception ex) {
        for (int i = 0; i < this.nestedLoggers.length; ++i) {
            this.nestedLoggers[i].logException(ex);
        }
    }
    
    @Override
    public void logLine(final String s) {
        for (int i = 0; i < this.nestedLoggers.length; ++i) {
            this.nestedLoggers[i].logLine(s);
        }
    }
    
    @Override
    public void message(final String s) {
        for (int i = 0; i < this.nestedLoggers.length; ++i) {
            this.nestedLoggers[i].message(s);
        }
    }
}
