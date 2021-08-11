/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.util.Vector;
import util.LoggerInterface;

public class GroupedLogger
implements LoggerInterface {
    LoggerInterface[] nestedLoggers;

    public GroupedLogger(LoggerInterface[] arrloggerInterface) {
        this.nestedLoggers = arrloggerInterface;
    }

    public void attachLogger(LoggerInterface loggerInterface) {
        LoggerInterface[] arrloggerInterface = this.nestedLoggers;
        arrloggerInterface = new LoggerInterface[arrloggerInterface.length + 1];
        for (int i = 0; i < this.nestedLoggers.length; ++i) {
            arrloggerInterface[i] = this.nestedLoggers[i];
        }
        arrloggerInterface[this.nestedLoggers.length] = loggerInterface;
        this.nestedLoggers = arrloggerInterface;
    }

    @Override
    public void closeLogger() {
        for (int i = 0; i < this.nestedLoggers.length; ++i) {
            this.nestedLoggers[i].closeLogger();
        }
    }

    public void detachLogger(LoggerInterface loggerInterface) {
        Object object = this.nestedLoggers;
        object = new Vector(object.length);
        for (int i = 0; i < this.nestedLoggers.length; ++i) {
            if (this.nestedLoggers[i] == loggerInterface) continue;
            object.add(this.nestedLoggers[i]);
        }
        this.nestedLoggers = object.toArray(new LoggerInterface[object.size()]);
    }

    @Override
    public void log(String string2) {
        for (int i = 0; i < this.nestedLoggers.length; ++i) {
            this.nestedLoggers[i].log(string2);
        }
    }

    @Override
    public void logException(Exception exception) {
        for (int i = 0; i < this.nestedLoggers.length; ++i) {
            this.nestedLoggers[i].logException(exception);
        }
    }

    @Override
    public void logLine(String string2) {
        for (int i = 0; i < this.nestedLoggers.length; ++i) {
            this.nestedLoggers[i].logLine(string2);
        }
    }

    @Override
    public void message(String string2) {
        for (int i = 0; i < this.nestedLoggers.length; ++i) {
            this.nestedLoggers[i].message(string2);
        }
    }
}

