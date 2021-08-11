// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

class UDP extends DNSServer
{
    protected UDP(final InetAddress inetAddress, final int n, final int n2) {
        super(inetAddress, n, n2);
    }
    
    @Override
    public String getProtocolName() {
        return "UDP";
    }
    
    @Override
    public void resolve(final DatagramPacket datagramPacket, final DatagramPacket datagramPacket2) throws IOException {
        final DatagramSocket datagramSocket = new DatagramSocket();
        try {
            datagramPacket.setSocketAddress(this.address);
            datagramSocket.setSoTimeout(this.timeout);
            try {
                datagramSocket.send(datagramPacket);
                try {
                    datagramSocket.receive(datagramPacket2);
                }
                catch (IOException ex2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("No DNS Response from ");
                    sb.append(this.address);
                    throw new IOException(sb.toString());
                }
            }
            catch (IOException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Cannot reach ");
                sb2.append(this.address);
                sb2.append("!");
                sb2.append(ex.getMessage());
                throw new IOException(sb2.toString());
            }
        }
        finally {
            datagramSocket.close();
        }
    }
}
