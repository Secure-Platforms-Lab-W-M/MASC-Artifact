// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util.conpool;

import java.io.IOException;
import java.net.InetAddress;
import util.http.HttpHeader;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class HttpProxy extends Proxy
{
    private String authString;
    private InetSocketAddress proxyAdr;
    
    public HttpProxy(final InetSocketAddress inetSocketAddress) {
        this(inetSocketAddress, null);
    }
    
    public HttpProxy(final InetSocketAddress proxyAdr, final String authString) {
        super(Type.HTTP, proxyAdr);
        this.proxyAdr = proxyAdr;
        this.authString = authString;
    }
    
    public Socket openTunnel(final InetSocketAddress inetSocketAddress, final int soTimeout) throws IOException {
        String s;
        if (!inetSocketAddress.getAddress().getHostAddress().equals("0.0.0.0")) {
            s = inetSocketAddress.getAddress().getHostAddress();
        }
        else {
            s = inetSocketAddress.getHostName();
        }
        final HttpHeader httpHeader = new HttpHeader(1);
        final StringBuilder sb = new StringBuilder();
        sb.append("CONNECT ");
        sb.append(s);
        sb.append(":");
        sb.append(inetSocketAddress.getPort());
        sb.append(" HTTP/1.1");
        httpHeader.setRequest(sb.toString());
        if (this.authString != null) {
            httpHeader.setValue("Proxy-Authorization", this.authString);
        }
        final String serverRequestHeader = httpHeader.getServerRequestHeader();
        final InetSocketAddress inetSocketAddress2 = new InetSocketAddress(InetAddress.getByAddress(inetSocketAddress.getHostName(), this.proxyAdr.getAddress().getAddress()), this.proxyAdr.getPort());
        final Socket socket = new Socket();
        socket.connect(inetSocketAddress2, soTimeout);
        socket.setSoTimeout(soTimeout);
        socket.getOutputStream().write(serverRequestHeader.getBytes());
        socket.getOutputStream().flush();
        final HttpHeader httpHeader2 = new HttpHeader(socket.getInputStream(), 2);
        if (httpHeader2.responsecode != 200) {
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Proxy refused Tunnel\n");
            sb2.append(httpHeader2.getResponseMessage());
            throw new IOException(sb2.toString());
        }
        socket.setSoTimeout(0);
        return socket;
    }
    
    public void setProxyAuth(final String authString) {
        this.authString = authString;
    }
}
