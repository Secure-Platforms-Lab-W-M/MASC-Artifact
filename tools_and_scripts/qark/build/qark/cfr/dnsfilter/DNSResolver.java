/*
 * Decompiled with CFR 0_124.
 */
package dnsfilter;

import dnsfilter.DNSCommunicator;
import dnsfilter.DNSResponsePatcher;
import ip.IPPacket;
import ip.UDPPacket;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import util.ExecutionEnvironment;
import util.Logger;
import util.LoggerInterface;

public class DNSResolver
implements Runnable {
    private static Object CNT_SYNC;
    private static boolean IO_ERROR;
    private static int THR_COUNT;
    private DatagramPacket dataGramRequest;
    private boolean datagramPacketMode = false;
    private DatagramSocket replySocket;
    private OutputStream responseOut;
    private UDPPacket udpRequestPacket;

    static {
        THR_COUNT = 0;
        CNT_SYNC = new Object();
        IO_ERROR = false;
    }

    public DNSResolver(UDPPacket uDPPacket, OutputStream outputStream) {
        this.udpRequestPacket = uDPPacket;
        this.responseOut = outputStream;
    }

    public DNSResolver(DatagramPacket datagramPacket, DatagramSocket datagramSocket) {
        this.datagramPacketMode = true;
        this.dataGramRequest = datagramPacket;
        this.replySocket = datagramSocket;
    }

    public static int getResolverCount() {
        return THR_COUNT;
    }

    private void processDatagramPackageMode() throws Exception {
        SocketAddress socketAddress = this.dataGramRequest.getSocketAddress();
        Object object = this.dataGramRequest.getData();
        object = new DatagramPacket((byte[])object, this.dataGramRequest.getOffset(), object.length - this.dataGramRequest.getOffset());
        DNSCommunicator.getInstance().requestDNS(this.dataGramRequest, (DatagramPacket)object);
        DNSResponsePatcher.patchResponse(socketAddress.toString(), object.getData(), object.getOffset());
        object.setSocketAddress(socketAddress);
        this.replySocket.send((DatagramPacket)object);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void processIPPackageMode() throws Exception {
        int n = this.udpRequestPacket.getTTL();
        Object object = this.udpRequestPacket.getSourceIP();
        Object object2 = this.udpRequestPacket.getDestIP();
        int n2 = this.udpRequestPacket.getSourcePort();
        int n3 = this.udpRequestPacket.getDestPort();
        int n4 = this.udpRequestPacket.getVersion();
        Object object3 = new StringBuilder();
        object3.append(IPPacket.int2ip((int[])object).getHostAddress());
        object3.append(":");
        object3.append(n2);
        object3 = object3.toString();
        int n5 = this.udpRequestPacket.getHeaderLength();
        Object object4 = this.udpRequestPacket.getData();
        int n6 = this.udpRequestPacket.getIPPacketOffset();
        int n7 = n6 + n5;
        byte[] arrby = new byte[]((byte[])object4, n7, this.udpRequestPacket.getIPPacketLength() - n5);
        object4 = new DatagramPacket((byte[])object4, n7, object4.length - n7);
        DNSCommunicator.getInstance().requestDNS((DatagramPacket)arrby, (DatagramPacket)object4);
        object3 = UDPPacket.createUDPPacket(DNSResponsePatcher.patchResponse((String)object3, object4.getData(), n7), n6, n5 + object4.getLength(), n4);
        object3.updateHeader(n, 17, (int[])object2, (int[])object);
        object3.updateHeader(n3, n2);
        object2 = this.responseOut;
        synchronized (object2) {
            void var8_3;
            block9 : {
                try {
                    object = this.responseOut;
                }
                catch (Throwable throwable) {}
                try {
                    arrby = object3.getData();
                }
                catch (Throwable throwable) {
                    break block9;
                }
                try {
                    n = object3.getIPPacketOffset();
                }
                catch (Throwable throwable) {
                    break block9;
                }
                try {
                    object.write(arrby, n, object3.getIPPacketLength());
                    this.responseOut.flush();
                    return;
                }
                catch (Throwable throwable) {}
            }
            throw var8_3;
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void run() {
        Throwable throwable222;
        Object object;
        block27 : {
            object = CNT_SYNC;
            // MONITORENTER : object
            ++THR_COUNT;
            // MONITOREXIT : object
            if (this.datagramPacketMode) {
                this.processDatagramPackageMode();
            } else {
                this.processIPPackageMode();
            }
            IO_ERROR = false;
            object = CNT_SYNC;
            --THR_COUNT;
            // MONITOREXIT : object
            return;
            {
                catch (Throwable throwable222) {
                    break block27;
                }
                catch (Exception exception) {
                    Logger.getLogger().logException(exception);
                    Object object2 = CNT_SYNC;
                    // MONITORENTER : object2
                    --THR_COUNT;
                    // MONITOREXIT : object2
                    return;
                }
                catch (IOException iOException) {}
                {
                    boolean bl = ExecutionEnvironment.getEnvironment().hasNetwork();
                    if (!bl) {
                        Logger.getLogger().message("No Network!");
                    }
                    object = iOException.getMessage();
                    if (iOException.getMessage() == null) {
                        object = iOException.toString();
                    }
                    if (ExecutionEnvironment.getEnvironment().debug()) {
                        Logger.getLogger().logLine((String)object);
                    } else if (!IO_ERROR && bl) {
                        LoggerInterface loggerInterface = Logger.getLogger();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append((String)object);
                        stringBuilder.append("\nIO Error occured! Check network or DNS Config!");
                        loggerInterface.logLine(stringBuilder.toString());
                        IO_ERROR = true;
                    }
                    object = CNT_SYNC;
                }
                // MONITORENTER : object
                --THR_COUNT;
                // MONITOREXIT : object
                return;
            }
        }
        object = CNT_SYNC;
        // MONITORENTER : object
        --THR_COUNT;
        // MONITOREXIT : object
        throw throwable222;
    }
}

