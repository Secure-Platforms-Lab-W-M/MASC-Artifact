// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter;

import util.LoggerInterface;
import util.Logger;
import util.ExecutionEnvironment;
import java.io.IOException;
import ip.IPPacket;
import java.net.SocketAddress;
import ip.UDPPacket;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

public class DNSResolver implements Runnable
{
    private static Object CNT_SYNC;
    private static boolean IO_ERROR;
    private static int THR_COUNT;
    private DatagramPacket dataGramRequest;
    private boolean datagramPacketMode;
    private DatagramSocket replySocket;
    private OutputStream responseOut;
    private UDPPacket udpRequestPacket;
    
    static {
        DNSResolver.THR_COUNT = 0;
        DNSResolver.CNT_SYNC = new Object();
        DNSResolver.IO_ERROR = false;
    }
    
    public DNSResolver(final UDPPacket udpRequestPacket, final OutputStream responseOut) {
        this.datagramPacketMode = false;
        this.udpRequestPacket = udpRequestPacket;
        this.responseOut = responseOut;
    }
    
    public DNSResolver(final DatagramPacket dataGramRequest, final DatagramSocket replySocket) {
        this.datagramPacketMode = false;
        this.datagramPacketMode = true;
        this.dataGramRequest = dataGramRequest;
        this.replySocket = replySocket;
    }
    
    public static int getResolverCount() {
        return DNSResolver.THR_COUNT;
    }
    
    private void processDatagramPackageMode() throws Exception {
        final SocketAddress socketAddress = this.dataGramRequest.getSocketAddress();
        final byte[] data = this.dataGramRequest.getData();
        final DatagramPacket datagramPacket = new DatagramPacket(data, this.dataGramRequest.getOffset(), data.length - this.dataGramRequest.getOffset());
        DNSCommunicator.getInstance().requestDNS(this.dataGramRequest, datagramPacket);
        DNSResponsePatcher.patchResponse(socketAddress.toString(), datagramPacket.getData(), datagramPacket.getOffset());
        datagramPacket.setSocketAddress(socketAddress);
        this.replySocket.send(datagramPacket);
    }
    
    private void processIPPackageMode() throws Exception {
        final int ttl = this.udpRequestPacket.getTTL();
        final int[] sourceIP = this.udpRequestPacket.getSourceIP();
        final int[] destIP = this.udpRequestPacket.getDestIP();
        final int sourcePort = this.udpRequestPacket.getSourcePort();
        final int destPort = this.udpRequestPacket.getDestPort();
        final int version = this.udpRequestPacket.getVersion();
        final StringBuilder sb = new StringBuilder();
        sb.append(IPPacket.int2ip(sourceIP).getHostAddress());
        sb.append(":");
        sb.append(sourcePort);
        final String string = sb.toString();
        final int headerLength = this.udpRequestPacket.getHeaderLength();
        final byte[] data = this.udpRequestPacket.getData();
        final int ipPacketOffset = this.udpRequestPacket.getIPPacketOffset();
        final int n = ipPacketOffset + headerLength;
        final DatagramPacket datagramPacket = new DatagramPacket(data, n, this.udpRequestPacket.getIPPacketLength() - headerLength);
        final DatagramPacket datagramPacket2 = new DatagramPacket(data, n, data.length - n);
        DNSCommunicator.getInstance().requestDNS(datagramPacket, datagramPacket2);
        final UDPPacket udpPacket = UDPPacket.createUDPPacket(DNSResponsePatcher.patchResponse(string, datagramPacket2.getData(), n), ipPacketOffset, headerLength + datagramPacket2.getLength(), version);
        udpPacket.updateHeader(ttl, 17, destIP, sourceIP);
        udpPacket.updateHeader(destPort, sourcePort);
        final OutputStream responseOut = this.responseOut;
        // monitorenter(responseOut)
        while (true) {
            try {
                final OutputStream responseOut2 = this.responseOut;
                try {
                    final byte[] data2 = udpPacket.getData();
                    try {
                        final int ipPacketOffset2 = udpPacket.getIPPacketOffset();
                        try {
                            responseOut2.write(data2, ipPacketOffset2, udpPacket.getIPPacketLength());
                            this.responseOut.flush();
                            // monitorexit(responseOut)
                            return;
                            // monitorexit(responseOut)
                            throw;
                        }
                        finally {}
                    }
                    finally {}
                }
                finally {}
            }
            finally {
                continue;
            }
            break;
        }
    }
    
    @Override
    public void run() {
        try {
            synchronized (DNSResolver.CNT_SYNC) {
                ++DNSResolver.THR_COUNT;
                // monitorexit(DNSResolver.CNT_SYNC)
                if (this.datagramPacketMode) {
                    this.processDatagramPackageMode();
                }
                else {
                    this.processIPPackageMode();
                }
                DNSResolver.IO_ERROR = false;
                final Object cnt_SYNC = DNSResolver.CNT_SYNC;
                synchronized (DNSResolver.CNT_SYNC) {
                    --DNSResolver.THR_COUNT;
                }
            }
        }
        catch (Exception ex3) {}
        catch (IOException ex2) {}
        finally {
            synchronized (DNSResolver.CNT_SYNC) {
                --DNSResolver.THR_COUNT;
            }
            // monitorexit(DNSResolver.CNT_SYNC)
            // iftrue(Label_0169:, !ExecutionEnvironment.getEnvironment().debug())
            // iftrue(Label_0146:, ex.getMessage() != null)
            // iftrue(Label_0222:, DNSResolver.IO_ERROR || !hasNetwork)
            while (true) {
                while (true) {
                    final IOException ex;
                Label_0222_Outer:
                    while (true) {
                        while (true) {
                            final String s;
                            Block_16: {
                                break Block_16;
                                s = ex.getMessage();
                                break Label_0222_Outer;
                                final LoggerInterface logger = Logger.getLogger();
                                final StringBuilder sb = new StringBuilder();
                                sb.append(s);
                                sb.append("\nIO Error occured! Check network or DNS Config!");
                                logger.logLine(sb.toString());
                                DNSResolver.IO_ERROR = true;
                                synchronized (DNSResolver.CNT_SYNC) {
                                    --DNSResolver.THR_COUNT;
                                    return;
                                }
                            }
                            Logger.getLogger().logLine(s);
                            continue;
                        }
                        Label_0169: {
                            continue Label_0222_Outer;
                        }
                    }
                    String s = ex.toString();
                    continue;
                }
                try {
                    --DNSResolver.THR_COUNT;
                    return;
                }
                finally {
                }
                // monitorexit(DNSResolver.CNT_SYNC)
                final IOException ex2;
                final IOException ex = ex2;
                final boolean hasNetwork = ExecutionEnvironment.getEnvironment().hasNetwork();
                Logger.getLogger().message("No Network!");
                continue;
            }
        }
        // iftrue(Label_0129:, hasNetwork)
    }
}
