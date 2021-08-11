// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import javax.annotation.Nullable;
import java.net.Proxy;
import java.net.InetSocketAddress;

public final class Route
{
    final Address address;
    final InetSocketAddress inetSocketAddress;
    final Proxy proxy;
    
    public Route(final Address address, final Proxy proxy, final InetSocketAddress inetSocketAddress) {
        if (address == null) {
            throw new NullPointerException("address == null");
        }
        if (proxy == null) {
            throw new NullPointerException("proxy == null");
        }
        if (inetSocketAddress == null) {
            throw new NullPointerException("inetSocketAddress == null");
        }
        this.address = address;
        this.proxy = proxy;
        this.inetSocketAddress = inetSocketAddress;
    }
    
    public Address address() {
        return this.address;
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        return o instanceof Route && ((Route)o).address.equals(this.address) && ((Route)o).proxy.equals(this.proxy) && ((Route)o).inetSocketAddress.equals(this.inetSocketAddress);
    }
    
    @Override
    public int hashCode() {
        return ((this.address.hashCode() + 527) * 31 + this.proxy.hashCode()) * 31 + this.inetSocketAddress.hashCode();
    }
    
    public Proxy proxy() {
        return this.proxy;
    }
    
    public boolean requiresTunnel() {
        return this.address.sslSocketFactory != null && this.proxy.type() == Proxy.Type.HTTP;
    }
    
    public InetSocketAddress socketAddress() {
        return this.inetSocketAddress;
    }
    
    @Override
    public String toString() {
        return "Route{" + this.inetSocketAddress + "}";
    }
}
