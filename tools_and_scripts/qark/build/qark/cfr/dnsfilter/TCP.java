/*
 * Decompiled with CFR 0_124.
 */
package dnsfilter;

import dnsfilter.DNSServer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import util.conpool.Connection;

class TCP
extends DNSServer {
    boolean ssl;

    protected TCP(InetAddress inetAddress, int n, int n2, boolean bl, String string2) throws IOException {
        super(inetAddress, n, n2);
        this.ssl = bl;
        if (string2 != null) {
            if (string2.indexOf("://") != -1) {
                inetAddress = new StringBuilder();
                inetAddress.append("Invalid hostname specified for ");
                inetAddress.append(this.getProtocolName());
                inetAddress.append(": ");
                inetAddress.append(string2);
                throw new IOException(inetAddress.toString());
            }
            this.address = new InetSocketAddress(InetAddress.getByAddress(string2, inetAddress.getAddress()), n);
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
    public void resolve(DatagramPacket object, DatagramPacket datagramPacket) throws IOException {
        for (int i = 0; i < 2; ++i) {
            Connection connection = Connection.connect(this.address, this.timeout, this.ssl, null, Proxy.NO_PROXY);
            connection.setSoTimeout(this.timeout);
            try {
                DataInputStream dataInputStream = new DataInputStream(connection.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeShort(object.getLength());
                dataOutputStream.write(object.getData(), object.getOffset(), object.getLength());
                dataOutputStream.flush();
                this.readResponseFromStream(dataInputStream, dataInputStream.readShort(), datagramPacket);
                datagramPacket.setSocketAddress(this.address);
                connection.release(true);
                return;
            }
            catch (IOException iOException) {
                connection.release(false);
                throw iOException;
            }
            catch (EOFException eOFException) {
                connection.release(false);
                if (i != 1) continue;
                object = new StringBuilder();
                object.append("EOF when reading from ");
                object.append(this.toString());
                throw new IOException(object.toString(), eOFException);
            }
        }
    }
}

