// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter;

import java.net.DatagramPacket;
import java.io.IOException;
import util.LoggerInterface;
import util.Logger;
import util.ExecutionEnvironment;

public class DNSCommunicator
{
    private static DNSCommunicator INSTANCE;
    private static int TIMEOUT;
    int curDNS;
    DNSServer[] dnsServers;
    String lastDNS;
    
    static {
        DNSCommunicator.INSTANCE = new DNSCommunicator();
        DNSCommunicator.TIMEOUT = 12000;
    }
    
    public DNSCommunicator() {
        this.dnsServers = new DNSServer[0];
        this.curDNS = -1;
        this.lastDNS = "";
    }
    
    public static DNSCommunicator getInstance() {
        return DNSCommunicator.INSTANCE;
    }
    
    private boolean hasChanged(final DNSServer[] array, final DNSServer[] array2) {
        if (array.length != array2.length) {
            return true;
        }
        for (int i = 0; i < array.length; ++i) {
            if (!array[i].equals(array2[i])) {
                return true;
            }
        }
        return false;
    }
    
    private void switchDNSServer(final DNSServer dnsServer) throws IOException {
        synchronized (this) {
            if (dnsServer == this.getCurrentDNS()) {
                this.curDNS = (this.curDNS + 1) % this.dnsServers.length;
                if (ExecutionEnvironment.getEnvironment().debug()) {
                    final LoggerInterface logger = Logger.getLogger();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Switched DNS Server to:");
                    sb.append(this.getCurrentDNS().getAddress().getHostAddress());
                    logger.logLine(sb.toString());
                }
            }
        }
    }
    
    public DNSServer getCurrentDNS() throws IOException {
        synchronized (this) {
            if (this.dnsServers.length == 0) {
                throw new IOException("No DNS Server initialized!");
            }
            this.lastDNS = this.dnsServers[this.curDNS].toString();
            return this.dnsServers[this.curDNS];
        }
    }
    
    public String getLastDNSAddress() {
        return this.lastDNS;
    }
    
    public void requestDNS(final DatagramPacket datagramPacket, final DatagramPacket datagramPacket2) throws IOException {
        final DNSServer currentDNS = this.getCurrentDNS();
        try {
            currentDNS.resolve(datagramPacket, datagramPacket2);
        }
        catch (IOException ex) {
            if (ExecutionEnvironment.getEnvironment().hasNetwork()) {
                this.switchDNSServer(currentDNS);
            }
            throw ex;
        }
    }
    
    public void setDNSServers(final DNSServer[] dnsServers) {
        synchronized (this) {
            if (this.hasChanged(dnsServers, this.dnsServers)) {
                this.dnsServers = dnsServers;
                if (this.dnsServers.length > 0) {
                    this.lastDNS = this.dnsServers[0].toString();
                    this.curDNS = 0;
                }
                else {
                    this.lastDNS = "";
                    this.curDNS = -1;
                }
                if (ExecutionEnvironment.getEnvironment().debug()) {
                    Logger.getLogger().logLine("Using updated DNS Servers!");
                }
            }
        }
    }
}
