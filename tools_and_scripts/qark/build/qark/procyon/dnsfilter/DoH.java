// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter;

import java.io.OutputStream;
import java.io.EOFException;
import java.net.SocketAddress;
import java.io.InputStream;
import java.io.DataInputStream;
import javax.net.ssl.SSLSocketFactory;
import util.conpool.Connection;
import java.net.Proxy;
import java.net.DatagramPacket;
import util.http.HttpHeader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

class DoH extends DNSServer
{
    String reqTemplate;
    String url;
    String urlHost;
    InetSocketAddress urlHostAddress;
    
    protected DoH(final InetAddress inetAddress, final int n, final int n2, final String url) throws IOException {
        super(inetAddress, n, n2);
        if (url == null) {
            throw new IOException("Endpoint URL not defined for DNS over HTTPS (DoH)!");
        }
        this.url = url;
        this.buildTemplate();
        this.urlHostAddress = new InetSocketAddress(InetAddress.getByAddress(this.urlHost, inetAddress.getAddress()), n);
    }
    
    private byte[] buildRequestHeader(final int n) throws IOException {
        final String reqTemplate = this.reqTemplate;
        final StringBuilder sb = new StringBuilder();
        sb.append("\nContent-Length: ");
        sb.append(n);
        return reqTemplate.replace("\nContent-Length: 999", sb.toString()).getBytes();
    }
    
    private void buildTemplate() throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("Mozilla/5.0 (");
        sb.append(System.getProperty("os.name"));
        sb.append("; ");
        sb.append(System.getProperty("os.version"));
        sb.append(")");
        final String string = sb.toString();
        final HttpHeader httpHeader = new HttpHeader(1);
        httpHeader.setValue("User-Agent", string);
        httpHeader.setValue("Accept", "application/dns-message");
        httpHeader.setValue("content-type", "application/dns-message");
        httpHeader.setValue("Connection", "keep-alive");
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("POST ");
        sb2.append(this.url);
        sb2.append(" HTTP/1.1");
        httpHeader.setRequest(sb2.toString());
        httpHeader.setValue("Content-Length", "999");
        this.reqTemplate = httpHeader.getServerRequestHeader(false);
        this.urlHost = httpHeader.remote_host_name;
    }
    
    @Override
    public String getProtocolName() {
        return "DOH";
    }
    
    @Override
    public void resolve(final DatagramPacket datagramPacket, final DatagramPacket datagramPacket2) throws IOException {
        final byte[] buildRequestHeader = this.buildRequestHeader(datagramPacket.getLength());
        int i = 0;
        while (i < 2) {
            final Connection connect = Connection.connect(this.urlHostAddress, this.timeout, true, null, Proxy.NO_PROXY);
            try {
                final OutputStream outputStream = connect.getOutputStream();
                final DataInputStream dataInputStream = new DataInputStream(connect.getInputStream());
                outputStream.write(buildRequestHeader);
                outputStream.write(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength());
                outputStream.flush();
                final HttpHeader httpHeader = new HttpHeader(dataInputStream, 2);
                if (httpHeader.getResponseCode() != 200) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("DoH failed for ");
                    sb.append(this.url);
                    sb.append("! ");
                    sb.append(httpHeader.getResponseCode());
                    sb.append(" - ");
                    sb.append(httpHeader.getResponseMessage());
                    throw new IOException(sb.toString());
                }
                this.readResponseFromStream(dataInputStream, (int)httpHeader.getContentLength(), datagramPacket2);
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
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("EOF when reading from ");
                    sb2.append(this.toString());
                    throw new IOException(sb2.toString(), ex2);
                }
                ++i;
                continue;
            }
            break;
        }
    }
}
