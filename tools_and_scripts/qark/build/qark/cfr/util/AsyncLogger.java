/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import util.LoggerInterface;
import util.Utils;

public class AsyncLogger
implements LoggerInterface,
Runnable {
    static final int LOG = 1;
    static final int LOG_EXC = 4;
    static final int LOG_LN = 2;
    static final int LOG_MSG = 3;
    private boolean closed = false;
    private LoggerInterface out = null;
    private PipedInputStream pin;
    private DataOutputStream pout;

    public AsyncLogger(LoggerInterface loggerInterface) throws IOException {
        this.out = loggerInterface;
        this.logOpen();
    }

    private void logOpen() throws IOException {
        this.pin = new PipedInputStream(10240);
        this.pout = new DataOutputStream(new PipedOutputStream(this.pin));
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    private void writeLog(int n, byte[] arrby) {
        try {
            this.pout.writeShort(n);
            this.pout.writeInt(arrby.length);
            this.pout.write(arrby);
            this.pout.flush();
            return;
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void closeLogger() {
        PipedInputStream pipedInputStream = this.pin;
        // MONITORENTER : pipedInputStream
        this.closed = true;
        this.pout.close();
        this.pin.notifyAll();
        return;
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        // MONITOREXIT : pipedInputStream
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void log(String string2) {
        DataOutputStream dataOutputStream = this.pout;
        synchronized (dataOutputStream) {
            PipedInputStream pipedInputStream = this.pin;
            synchronized (pipedInputStream) {
                this.writeLog(1, string2.getBytes());
                this.pin.notifyAll();
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void logException(Exception object) {
        byte[] arrby;
        try {
            arrby = Utils.serializeObject(object);
            object = this.pout;
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return;
        }
        synchronized (object) {
            PipedInputStream pipedInputStream = this.pin;
            synchronized (pipedInputStream) {
                this.writeLog(4, arrby);
                this.pin.notifyAll();
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void logLine(String string2) {
        DataOutputStream dataOutputStream = this.pout;
        synchronized (dataOutputStream) {
            PipedInputStream pipedInputStream = this.pin;
            synchronized (pipedInputStream) {
                this.writeLog(2, string2.getBytes());
                this.pin.notifyAll();
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void message(String string2) {
        DataOutputStream dataOutputStream = this.pout;
        synchronized (dataOutputStream) {
            PipedInputStream pipedInputStream = this.pin;
            synchronized (pipedInputStream) {
                this.writeLog(3, string2.getBytes());
                this.pin.notifyAll();
                return;
            }
        }
    }

    /*
     * Exception decompiling
     */
    @Override
    public void run() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[CASE]], but top level block is 3[TRYBLOCK]
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:420)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:472)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2880)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:838)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }
}

