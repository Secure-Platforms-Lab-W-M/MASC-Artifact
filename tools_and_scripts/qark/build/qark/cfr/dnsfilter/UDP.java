/*
 * Decompiled with CFR 0_124.
 */
package dnsfilter;

import dnsfilter.DNSServer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

class UDP
extends DNSServer {
    protected UDP(InetAddress inetAddress, int n, int n2) {
        super(inetAddress, n, n2);
    }

    @Override
    public String getProtocolName() {
        return "UDP";
    }

    /*
     * Loose catch block
     * Enabled aggressive exception aggregation
     */
    @Override
    public void resolve(DatagramPacket datagramPacket, DatagramPacket object) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();
        try {
            datagramPacket.setSocketAddress(this.address);
            datagramSocket.setSoTimeout(this.timeout);
            datagramSocket.send(datagramPacket);
            {
                catch (IOException iOException) {
                    object = new StringBuilder();
                    object.append("Cannot reach ");
                    object.append(this.address);
                    object.append("!");
                    object.append(iOException.getMessage());
                    throw new IOException(object.toString());
                }
            }
            try {
                datagramSocket.receive((DatagramPacket)object);
                return;
            }
            catch (IOException iOException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("No DNS Response from ");
                stringBuilder.append(this.address);
                throw new IOException(stringBuilder.toString());
            }
        }
        finally {
            datagramSocket.close();
        }
    }
}

