// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.connection;

import java.util.Collection;
import java.util.NoSuchElementException;
import okhttp3.internal.Util;
import okhttp3.HttpUrl;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.net.SocketException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.net.Proxy;
import okhttp3.Route;
import java.net.InetSocketAddress;
import java.util.List;
import okhttp3.EventListener;
import okhttp3.Call;
import okhttp3.Address;

public final class RouteSelector
{
    private final Address address;
    private final Call call;
    private final EventListener eventListener;
    private List<InetSocketAddress> inetSocketAddresses;
    private int nextProxyIndex;
    private final List<Route> postponedRoutes;
    private List<Proxy> proxies;
    private final RouteDatabase routeDatabase;
    
    public RouteSelector(final Address address, final RouteDatabase routeDatabase, final Call call, final EventListener eventListener) {
        this.proxies = Collections.emptyList();
        this.inetSocketAddresses = Collections.emptyList();
        this.postponedRoutes = new ArrayList<Route>();
        this.address = address;
        this.routeDatabase = routeDatabase;
        this.call = call;
        this.eventListener = eventListener;
        this.resetNextProxy(address.url(), address.proxy());
    }
    
    static String getHostString(final InetSocketAddress inetSocketAddress) {
        final InetAddress address = inetSocketAddress.getAddress();
        if (address == null) {
            return inetSocketAddress.getHostName();
        }
        return address.getHostAddress();
    }
    
    private boolean hasNextProxy() {
        return this.nextProxyIndex < this.proxies.size();
    }
    
    private Proxy nextProxy() throws IOException {
        if (!this.hasNextProxy()) {
            throw new SocketException("No route to " + this.address.url().host() + "; exhausted proxy configurations: " + this.proxies);
        }
        final Proxy proxy = this.proxies.get(this.nextProxyIndex++);
        this.resetNextInetSocketAddress(proxy);
        return proxy;
    }
    
    private void resetNextInetSocketAddress(final Proxy proxy) throws IOException {
        this.inetSocketAddresses = new ArrayList<InetSocketAddress>();
        String s;
        int n;
        if (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.SOCKS) {
            s = this.address.url().host();
            n = this.address.url().port();
        }
        else {
            final SocketAddress address = proxy.address();
            if (!(address instanceof InetSocketAddress)) {
                throw new IllegalArgumentException("Proxy.address() is not an InetSocketAddress: " + ((InetSocketAddress)address).getClass());
            }
            final InetSocketAddress inetSocketAddress = (InetSocketAddress)address;
            s = getHostString(inetSocketAddress);
            n = inetSocketAddress.getPort();
        }
        if (n < 1 || n > 65535) {
            throw new SocketException("No route to " + s + ":" + n + "; port is out of range");
        }
        if (proxy.type() == Proxy.Type.SOCKS) {
            this.inetSocketAddresses.add(InetSocketAddress.createUnresolved(s, n));
        }
        else {
            this.eventListener.dnsStart(this.call, s);
            final List<InetAddress> lookup = this.address.dns().lookup(s);
            if (lookup.isEmpty()) {
                throw new UnknownHostException(this.address.dns() + " returned no addresses for " + s);
            }
            this.eventListener.dnsEnd(this.call, s, lookup);
            for (int i = 0; i < lookup.size(); ++i) {
                this.inetSocketAddresses.add(new InetSocketAddress(lookup.get(i), n));
            }
        }
    }
    
    private void resetNextProxy(final HttpUrl httpUrl, final Proxy proxy) {
        if (proxy != null) {
            this.proxies = Collections.singletonList(proxy);
        }
        else {
            final List<Proxy> select = this.address.proxySelector().select(httpUrl.uri());
            List<Proxy> proxies;
            if (select != null && !select.isEmpty()) {
                proxies = Util.immutableList(select);
            }
            else {
                proxies = Util.immutableList(Proxy.NO_PROXY);
            }
            this.proxies = proxies;
        }
        this.nextProxyIndex = 0;
    }
    
    public void connectFailed(final Route route, final IOException ex) {
        if (route.proxy().type() != Proxy.Type.DIRECT && this.address.proxySelector() != null) {
            this.address.proxySelector().connectFailed(this.address.url().uri(), route.proxy().address(), ex);
        }
        this.routeDatabase.failed(route);
    }
    
    public boolean hasNext() {
        return this.hasNextProxy() || !this.postponedRoutes.isEmpty();
    }
    
    public Selection next() throws IOException {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        final ArrayList list = new ArrayList<Object>();
        while (this.hasNextProxy()) {
            final Proxy nextProxy = this.nextProxy();
            for (int i = 0; i < this.inetSocketAddresses.size(); ++i) {
                final Route route = new Route(this.address, nextProxy, this.inetSocketAddresses.get(i));
                if (this.routeDatabase.shouldPostpone(route)) {
                    this.postponedRoutes.add(route);
                }
                else {
                    list.add(route);
                }
            }
            if (!list.isEmpty()) {
                break;
            }
        }
        if (list.isEmpty()) {
            list.addAll(this.postponedRoutes);
            this.postponedRoutes.clear();
        }
        return new Selection(list);
    }
    
    public static final class Selection
    {
        private int nextRouteIndex;
        private final List<Route> routes;
        
        Selection(final List<Route> routes) {
            this.nextRouteIndex = 0;
            this.routes = routes;
        }
        
        public List<Route> getAll() {
            return new ArrayList<Route>(this.routes);
        }
        
        public boolean hasNext() {
            return this.nextRouteIndex < this.routes.size();
        }
        
        public Route next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.routes.get(this.nextRouteIndex++);
        }
    }
}
