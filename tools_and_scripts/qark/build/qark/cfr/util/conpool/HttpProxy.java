/*
 * Decompiled with CFR 0_124.
 */
package util.conpool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import util.http.HttpHeader;

public class HttpProxy
extends Proxy {
    private String authString;
    private InetSocketAddress proxyAdr;

    public HttpProxy(InetSocketAddress inetSocketAddress) {
        this(inetSocketAddress, null);
    }

    public HttpProxy(InetSocketAddress inetSocketAddress, String string2) {
        super(Proxy.Type.HTTP, inetSocketAddress);
        this.proxyAdr = inetSocketAddress;
        this.authString = string2;
    }

    public Socket openTunnel(InetSocketAddress object, int n) throws IOException {
        Object object2 = !object.getAddress().getHostAddress().equals("0.0.0.0") ? object.getAddress().getHostAddress() : object.getHostName();
        Object object3 = new HttpHeader(1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CONNECT ");
        stringBuilder.append((String)object2);
        stringBuilder.append(":");
        stringBuilder.append(object.getPort());
        stringBuilder.append(" HTTP/1.1");
        object3.setRequest(stringBuilder.toString());
        if (this.authString != null) {
            object3.setValue("Proxy-Authorization", this.authString);
        }
        object2 = object3.getServerRequestHeader();
        object3 = new InetSocketAddress(InetAddress.getByAddress(object.getHostName(), this.proxyAdr.getAddress().getAddress()), this.proxyAdr.getPort());
        object = new Socket();
        object.connect((SocketAddress)object3, n);
        object.setSoTimeout(n);
        object.getOutputStream().write(object2.getBytes());
        object.getOutputStream().flush();
        object2 = new HttpHeader(object.getInputStream(), 2);
        if (object2.responsecode != 200) {
            object.shutdownInput();
            object.shutdownOutput();
            object.close();
            object = new StringBuilder();
            object.append("Proxy refused Tunnel\n");
            object.append(object2.getResponseMessage());
            throw new IOException(object.toString());
        }
        object.setSoTimeout(0);
        return object;
    }

    public void setProxyAuth(String string2) {
        this.authString = string2;
    }
}

