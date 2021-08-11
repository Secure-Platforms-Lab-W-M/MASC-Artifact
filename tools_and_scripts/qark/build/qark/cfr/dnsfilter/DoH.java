/*
 * Decompiled with CFR 0_124.
 */
package dnsfilter;

import dnsfilter.DNSServer;
import java.io.DataInputStream;
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
import util.http.HttpHeader;

class DoH
extends DNSServer {
    String reqTemplate;
    String url;
    String urlHost;
    InetSocketAddress urlHostAddress;

    protected DoH(InetAddress inetAddress, int n, int n2, String string2) throws IOException {
        super(inetAddress, n, n2);
        if (string2 == null) {
            throw new IOException("Endpoint URL not defined for DNS over HTTPS (DoH)!");
        }
        this.url = string2;
        this.buildTemplate();
        this.urlHostAddress = new InetSocketAddress(InetAddress.getByAddress(this.urlHost, inetAddress.getAddress()), n);
    }

    private byte[] buildRequestHeader(int n) throws IOException {
        String string2 = this.reqTemplate;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nContent-Length: ");
        stringBuilder.append(n);
        return string2.replace("\nContent-Length: 999", stringBuilder.toString()).getBytes();
    }

    private void buildTemplate() throws IOException {
        Object object = new StringBuilder();
        object.append("Mozilla/5.0 (");
        object.append(System.getProperty("os.name"));
        object.append("; ");
        object.append(System.getProperty("os.version"));
        object.append(")");
        CharSequence charSequence = object.toString();
        object = new HttpHeader(1);
        object.setValue("User-Agent", (String)charSequence);
        object.setValue("Accept", "application/dns-message");
        object.setValue("content-type", "application/dns-message");
        object.setValue("Connection", "keep-alive");
        charSequence = new StringBuilder();
        charSequence.append("POST ");
        charSequence.append(this.url);
        charSequence.append(" HTTP/1.1");
        object.setRequest(charSequence.toString());
        object.setValue("Content-Length", "999");
        this.reqTemplate = object.getServerRequestHeader(false);
        this.urlHost = object.remote_host_name;
    }

    @Override
    public String getProtocolName() {
        return "DOH";
    }

    @Override
    public void resolve(DatagramPacket object, DatagramPacket datagramPacket) throws IOException {
        byte[] arrby = this.buildRequestHeader(object.getLength());
        for (int i = 0; i < 2; ++i) {
            Connection connection = Connection.connect(this.urlHostAddress, this.timeout, true, null, Proxy.NO_PROXY);
            try {
                Object object2 = connection.getOutputStream();
                Object object3 = new DataInputStream(connection.getInputStream());
                object2.write(arrby);
                object2.write(object.getData(), object.getOffset(), object.getLength());
                object2.flush();
                object2 = new HttpHeader((InputStream)object3, 2);
                if (object2.getResponseCode() != 200) {
                    object3 = new StringBuilder();
                    object3.append("DoH failed for ");
                    object3.append(this.url);
                    object3.append("! ");
                    object3.append(object2.getResponseCode());
                    object3.append(" - ");
                    object3.append(object2.getResponseMessage());
                    throw new IOException(object3.toString());
                }
                this.readResponseFromStream((DataInputStream)object3, (int)object2.getContentLength(), datagramPacket);
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

