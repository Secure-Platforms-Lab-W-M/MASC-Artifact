// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter;

import java.io.InputStream;
import java.net.DatagramPacket;
import util.ExecutionEnvironmentInterface;
import util.ExecutionEnvironment;
import util.GroupedLogger;
import util.LoggerInterface;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import util.Logger;
import java.net.DatagramSocket;

public class DNSFilterProxy implements Runnable
{
    int port;
    DatagramSocket receiver;
    boolean stopped;
    
    public DNSFilterProxy(final int port) {
        this.stopped = false;
        this.port = 53;
        this.port = port;
    }
    
    private static void initDNS(DNSFilterManager dnsFilterManager) {
        while (true) {
            while (true) {
                int n;
                try {
                    if (Boolean.parseBoolean(dnsFilterManager.getConfig().getProperty("detectDNS", "true"))) {
                        Logger.getLogger().logLine("DNS detection not supported for this device");
                        Logger.getLogger().message("DNS detection not supported - Using fallback!");
                    }
                    final Vector<DNSServer> vector = new Vector<DNSServer>();
                    final int int1 = Integer.parseInt(dnsFilterManager.getConfig().getProperty("dnsRequestTimeout", "15000"));
                    dnsFilterManager = (DNSFilterManager)new StringTokenizer(dnsFilterManager.getConfig().getProperty("fallbackDNS", ""), ";");
                    final int countTokens = ((StringTokenizer)dnsFilterManager).countTokens();
                    n = 0;
                    if (n >= countTokens) {
                        DNSCommunicator.getInstance().setDNSServers(vector.toArray(new DNSServer[vector.size()]));
                        return;
                    }
                    final String trim = ((StringTokenizer)dnsFilterManager).nextToken().trim();
                    final LoggerInterface logger = Logger.getLogger();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("DNS:");
                    sb.append(trim);
                    logger.logLine(sb.toString());
                    try {
                        vector.add(DNSServer.getInstance().createDNSServer(trim, int1));
                    }
                    catch (IOException ex) {
                        Logger.getLogger().logException(ex);
                    }
                }
                catch (IOException ex2) {
                    Logger.getLogger().logException(ex2);
                    return;
                }
                ++n;
                continue;
            }
        }
    }
    
    public static void main(final String[] array) throws Exception {
        Logger.setLogger(new GroupedLogger(new LoggerInterface[] { new StandaloneLogger() }));
        ExecutionEnvironment.setEnvironment(new StandaloneEnvironment());
        final DNSFilterManager instance = DNSFilterManager.getInstance();
        instance.init();
        initDNS(instance);
        new DNSFilterProxy(53).run();
    }
    
    @Override
    public void run() {
        try {
            final int int1 = Integer.parseInt(DNSFilterManager.getInstance().getConfig().getProperty("maxResolverCount", "100"));
            try {
                this.receiver = new DatagramSocket(this.port);
                final LoggerInterface logger = Logger.getLogger();
                final StringBuilder sb = new StringBuilder();
                sb.append("DNSFilterProxy running on port ");
                sb.append(this.port);
                sb.append("!");
                logger.logLine(sb.toString());
                while (!this.stopped) {
                    try {
                        final DatagramPacket datagramPacket = new DatagramPacket(new byte[DNSServer.getBufSize()], 0, DNSServer.getBufSize());
                        this.receiver.receive(datagramPacket);
                        if (DNSResolver.getResolverCount() > int1) {
                            final LoggerInterface logger2 = Logger.getLogger();
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Max Resolver Count reached: ");
                            sb2.append(int1);
                            logger2.message(sb2.toString());
                        }
                        else {
                            new Thread(new DNSResolver(datagramPacket, this.receiver)).start();
                        }
                    }
                    catch (IOException ex) {
                        if (this.stopped) {
                            continue;
                        }
                        final LoggerInterface logger3 = Logger.getLogger();
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Exception:");
                        sb3.append(ex.getMessage());
                        logger3.logLine(sb3.toString());
                    }
                }
                Logger.getLogger().logLine("DNSFilterProxy stopped!");
            }
            catch (IOException ex2) {
                final LoggerInterface logger4 = Logger.getLogger();
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Exception:Cannot open DNS port ");
                sb4.append(this.port);
                sb4.append("!");
                sb4.append(ex2.getMessage());
                logger4.logLine(sb4.toString());
            }
        }
        catch (Exception ex3) {
            Logger.getLogger().logLine("Exception:Cannot get maxResolverCount configuration!");
            Logger.getLogger().logException(ex3);
        }
    }
    
    public void stop() {
        synchronized (this) {
            this.stopped = true;
            if (this.receiver == null) {
                return;
            }
            this.receiver.close();
        }
    }
    
    class StandaloneEnvironment extends ExecutionEnvironment
    {
        boolean debug;
        boolean debugInit;
        
        StandaloneEnvironment() {
            this.debug = false;
            this.debugInit = false;
        }
        
        @Override
        public boolean debug() {
            if (!this.debugInit) {
                try {
                    this.debug = Boolean.parseBoolean(DNSFilterManager.getInstance().getConfig().getProperty("debug", "false"));
                }
                catch (IOException ex) {
                    Logger.getLogger().logException(ex);
                }
                this.debugInit = true;
            }
            return this.debug;
        }
        
        @Override
        public InputStream getAsset(final String s) throws IOException {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(s);
        }
        
        @Override
        public void onReload() {
            initDNS(DNSFilterManager.getInstance());
        }
    }
    
    class StandaloneLogger implements LoggerInterface
    {
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
            this.logLine(s);
        }
    }
}
