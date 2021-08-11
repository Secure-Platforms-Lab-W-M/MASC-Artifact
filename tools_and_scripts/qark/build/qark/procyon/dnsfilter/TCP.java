// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter;

import java.io.EOFException;
import java.net.SocketAddress;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import javax.net.ssl.SSLSocketFactory;
import util.conpool.Connection;
import java.net.Proxy;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.InetAddress;

class TCP extends DNSServer
{
    boolean ssl;
    
    protected TCP(final InetAddress inetAddress, final int n, final int n2, final boolean ssl, final String s) throws IOException {
        super(inetAddress, n, n2);
        this.ssl = ssl;
        if (s != null) {
            if (s.indexOf("://") != -1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid hostname specified for ");
                sb.append(this.getProtocolName());
                sb.append(": ");
                sb.append(s);
                throw new IOException(sb.toString());
            }
            this.address = new InetSocketAddress(InetAddress.getByAddress(s, inetAddress.getAddress()), n);
        }
    }
    
    @Override
    public String getProtocolName() {
        if (this.ssl) {
            return "DOT";
        }
        return "TCP";
    }
    
    @Override
    public void resolve(final DatagramPacket datagramPacket, final DatagramPacket datagramPacket2) throws IOException {
        int i = 0;
        while (i < 2) {
            final Connection connect = Connection.connect(this.address, this.timeout, this.ssl, null, Proxy.NO_PROXY);
            connect.setSoTimeout(this.timeout);
            try {
                final DataInputStream dataInputStream = new DataInputStream(connect.getInputStream());
                final DataOutputStream dataOutputStream = new DataOutputStream(connect.getOutputStream());
                dataOutputStream.writeShort(datagramPacket.getLength());
                dataOutputStream.write(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength());
                dataOutputStream.flush();
                this.readResponseFromStream(dataInputStream, dataInputStream.readShort(), datagramPacket2);
                datagramPacket2.setSocketAddress(this.address);
                connect.release(true);
                return;
            }
            catch (IOException ex) {
                connect.release(false);
                throw ex;
            }
            catch (EOFException ex2) {
                connect.release(false);
                if (i == 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("EOF when reading from ");
                    sb.append(this.toString());
                    throw new IOException(sb.toString(), ex2);
                }
                ++i;
                continue;
            }
            break;
        }
    }
}
