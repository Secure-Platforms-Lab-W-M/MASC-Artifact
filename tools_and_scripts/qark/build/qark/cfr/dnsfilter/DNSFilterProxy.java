/*
 * Decompiled with CFR 0_124.
 */
package dnsfilter;

import dnsfilter.DNSFilterManager;
import dnsfilter.DNSResolver;
import dnsfilter.DNSServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Properties;
import util.ExecutionEnvironment;
import util.GroupedLogger;
import util.Logger;
import util.LoggerInterface;

public class DNSFilterProxy
implements Runnable {
    int port = 53;
    DatagramSocket receiver;
    boolean stopped = false;

    public DNSFilterProxy(int n) {
        this.port = n;
    }

    /*
     * Exception decompiling
     */
    private static void initDNS(DNSFilterManager var0) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 5[UNCONDITIONALDOLOOP]
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

    public static void main(String[] object) throws Exception {
        Logger.setLogger(new GroupedLogger(new LoggerInterface[]{new 1StandaloneLogger()}));
        ExecutionEnvironment.setEnvironment(new 1StandaloneEnvironment());
        object = DNSFilterManager.getInstance();
        object.init();
        DNSFilterProxy.initDNS((DNSFilterManager)object);
        new DNSFilterProxy(53).run();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void run() {
        int n;
        try {
            n = Integer.parseInt(DNSFilterManager.getInstance().getConfig().getProperty("maxResolverCount", "100"));
        }
        catch (Exception exception) {
            Logger.getLogger().logLine("Exception:Cannot get maxResolverCount configuration!");
            Logger.getLogger().logException(exception);
            return;
        }
        try {}
        catch (IOException iOException) {
            LoggerInterface loggerInterface = Logger.getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Exception:Cannot open DNS port ");
            stringBuilder.append(this.port);
            stringBuilder.append("!");
            stringBuilder.append(iOException.getMessage());
            loggerInterface.logLine(stringBuilder.toString());
            return;
        }
        this.receiver = new DatagramSocket(this.port);
        Object object = Logger.getLogger();
        Object object2 = new StringBuilder();
        object2.append("DNSFilterProxy running on port ");
        object2.append(this.port);
        object2.append("!");
        object.logLine(object2.toString());
        do {
            if (this.stopped) {
                Logger.getLogger().logLine("DNSFilterProxy stopped!");
                return;
            }
            try {
                object = new DatagramPacket(new byte[DNSServer.getBufSize()], 0, DNSServer.getBufSize());
                this.receiver.receive((DatagramPacket)object);
                if (DNSResolver.getResolverCount() > n) {
                    object = Logger.getLogger();
                    object2 = new StringBuilder();
                    object2.append("Max Resolver Count reached: ");
                    object2.append(n);
                    object.message(object2.toString());
                    continue;
                }
                new Thread(new DNSResolver((DatagramPacket)object, this.receiver)).start();
            }
            catch (IOException iOException) {
                if (this.stopped) continue;
                object2 = Logger.getLogger();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Exception:");
                stringBuilder.append(iOException.getMessage());
                object2.logLine(stringBuilder.toString());
                continue;
            }
            break;
        } while (true);
    }

    public void stop() {
        synchronized (this) {
            block4 : {
                this.stopped = true;
                DatagramSocket datagramSocket = this.receiver;
                if (datagramSocket != null) break block4;
                return;
            }
            this.receiver.close();
            return;
        }
    }

    class 1StandaloneEnvironment
    extends ExecutionEnvironment {
        boolean debug = false;
        boolean debugInit = false;

        1StandaloneEnvironment() {
        }

        @Override
        public boolean debug() {
            if (!this.debugInit) {
                try {
                    this.debug = Boolean.parseBoolean(DNSFilterManager.getInstance().getConfig().getProperty("debug", "false"));
                }
                catch (IOException iOException) {
                    Logger.getLogger().logException(iOException);
                }
                this.debugInit = true;
            }
            return this.debug;
        }

        @Override
        public InputStream getAsset(String string2) throws IOException {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(string2);
        }

        @Override
        public void onReload() {
            DNSFilterProxy.initDNS(DNSFilterManager.getInstance());
        }
    }

    class 1StandaloneLogger
    implements LoggerInterface {
        1StandaloneLogger() {
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
            this.logLine(string2);
        }
    }

}

