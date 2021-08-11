/*
 * Decompiled with CFR 0_124.
 */
package dnsfilter;

import dnsfilter.DNSServer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import util.ExecutionEnvironment;
import util.Logger;

public class DNSCommunicator {
    private static DNSCommunicator INSTANCE = new DNSCommunicator();
    private static int TIMEOUT = 12000;
    int curDNS = -1;
    DNSServer[] dnsServers = new DNSServer[0];
    String lastDNS = "";

    public static DNSCommunicator getInstance() {
        return INSTANCE;
    }

    private boolean hasChanged(DNSServer[] arrdNSServer, DNSServer[] arrdNSServer2) {
        if (arrdNSServer.length != arrdNSServer2.length) {
            return true;
        }
        for (int i = 0; i < arrdNSServer.length; ++i) {
            if (arrdNSServer[i].equals(arrdNSServer2[i])) continue;
            return true;
        }
        return false;
    }

    private void switchDNSServer(DNSServer object) throws IOException {
        synchronized (this) {
            if (object == this.getCurrentDNS()) {
                this.curDNS = (this.curDNS + 1) % this.dnsServers.length;
                if (ExecutionEnvironment.getEnvironment().debug()) {
                    object = Logger.getLogger();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Switched DNS Server to:");
                    stringBuilder.append(this.getCurrentDNS().getAddress().getHostAddress());
                    object.logLine(stringBuilder.toString());
                }
            }
            return;
        }
    }

    public DNSServer getCurrentDNS() throws IOException {
        synchronized (this) {
            if (this.dnsServers.length == 0) {
                throw new IOException("No DNS Server initialized!");
            }
            this.lastDNS = this.dnsServers[this.curDNS].toString();
            DNSServer dNSServer = this.dnsServers[this.curDNS];
            return dNSServer;
        }
    }

    public String getLastDNSAddress() {
        return this.lastDNS;
    }

    public void requestDNS(DatagramPacket datagramPacket, DatagramPacket datagramPacket2) throws IOException {
        DNSServer dNSServer = this.getCurrentDNS();
        try {
            dNSServer.resolve(datagramPacket, datagramPacket2);
            return;
        }
        catch (IOException iOException) {
            if (ExecutionEnvironment.getEnvironment().hasNetwork()) {
                this.switchDNSServer(dNSServer);
            }
            throw iOException;
        }
    }

    public void setDNSServers(DNSServer[] arrdNSServer) {
        synchronized (this) {
            if (this.hasChanged(arrdNSServer, this.dnsServers)) {
                this.dnsServers = arrdNSServer;
                if (this.dnsServers.length > 0) {
                    this.lastDNS = this.dnsServers[0].toString();
                    this.curDNS = 0;
                } else {
                    this.lastDNS = "";
                    this.curDNS = -1;
                }
                if (ExecutionEnvironment.getEnvironment().debug()) {
                    Logger.getLogger().logLine("Using updated DNS Servers!");
                }
            }
            return;
        }
    }
}

