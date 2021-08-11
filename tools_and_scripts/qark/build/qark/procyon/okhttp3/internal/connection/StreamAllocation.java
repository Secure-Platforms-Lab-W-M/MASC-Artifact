// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.connection;

import java.util.List;
import java.lang.ref.WeakReference;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.StreamResetException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import java.lang.ref.Reference;
import okhttp3.Connection;
import okhttp3.internal.Util;
import java.io.IOException;
import okhttp3.internal.Internal;
import java.net.Socket;
import okhttp3.Route;
import okhttp3.EventListener;
import okhttp3.ConnectionPool;
import okhttp3.internal.http.HttpCodec;
import okhttp3.Call;
import okhttp3.Address;

public final class StreamAllocation
{
    public final Address address;
    public final Call call;
    private final Object callStackTrace;
    private boolean canceled;
    private HttpCodec codec;
    private RealConnection connection;
    private final ConnectionPool connectionPool;
    public final EventListener eventListener;
    private int refusedStreamCount;
    private boolean released;
    private boolean reportedAcquired;
    private Route route;
    private RouteSelector.Selection routeSelection;
    private final RouteSelector routeSelector;
    
    public StreamAllocation(final ConnectionPool connectionPool, final Address address, final Call call, final EventListener eventListener, final Object callStackTrace) {
        this.connectionPool = connectionPool;
        this.address = address;
        this.call = call;
        this.eventListener = eventListener;
        this.routeSelector = new RouteSelector(address, this.routeDatabase(), call, eventListener);
        this.callStackTrace = callStackTrace;
    }
    
    private Socket deallocate(final boolean b, final boolean b2, final boolean b3) {
        assert Thread.holdsLock(this.connectionPool);
        if (b3) {
            this.codec = null;
        }
        if (b2) {
            this.released = true;
        }
        final Socket socket = null;
        final Socket socket2 = null;
        Socket socket3 = socket;
        if (this.connection != null) {
            if (b) {
                this.connection.noNewStreams = true;
            }
            socket3 = socket;
            if (this.codec == null) {
                if (!this.released) {
                    socket3 = socket;
                    if (!this.connection.noNewStreams) {
                        return socket3;
                    }
                }
                this.release(this.connection);
                socket3 = socket2;
                if (this.connection.allocations.isEmpty()) {
                    this.connection.idleAtNanos = System.nanoTime();
                    socket3 = socket2;
                    if (Internal.instance.connectionBecameIdle(this.connectionPool, this.connection)) {
                        socket3 = this.connection.socket();
                    }
                }
                this.connection = null;
            }
        }
        return socket3;
    }
    
    private RealConnection findConnection(final int n, final int n2, final int n3, final int n4, final boolean b) throws IOException {
        final boolean b2 = false;
        Object connection = null;
        final Route route = null;
        synchronized (this.connectionPool) {
            if (this.released) {
                throw new IllegalStateException("released");
            }
        }
        if (this.codec != null) {
            throw new IllegalStateException("codec != null");
        }
        if (this.canceled) {
            throw new IOException("Canceled");
        }
        RealConnection connection2 = this.connection;
        final Socket releaseIfNoNewStreams = this.releaseIfNoNewStreams();
        if (this.connection != null) {
            connection = this.connection;
            connection2 = null;
        }
        RealConnection realConnection = connection2;
        if (!this.reportedAcquired) {
            realConnection = null;
        }
        Object o = connection;
        int n5 = b2 ? 1 : 0;
        Object route2 = route;
        if (connection == null) {
            Internal.instance.get(this.connectionPool, this.address, this, null);
            if (this.connection != null) {
                n5 = 1;
                o = this.connection;
                route2 = route;
            }
            else {
                route2 = this.route;
                o = connection;
                n5 = (b2 ? 1 : 0);
            }
        }
        // monitorexit(connectionPool)
        Util.closeQuietly(releaseIfNoNewStreams);
        if (realConnection != null) {
            this.eventListener.connectionReleased(this.call, realConnection);
        }
        if (n5 != 0) {
            this.eventListener.connectionAcquired(this.call, (Connection)o);
        }
        if (o != null) {
            return (RealConnection)o;
        }
        int n6;
        int size = n6 = 0;
        Label_0289: {
            if (route2 == null) {
                if (this.routeSelection != null) {
                    n6 = size;
                    if (this.routeSelection.hasNext()) {
                        break Label_0289;
                    }
                }
                n6 = 1;
                this.routeSelection = this.routeSelector.next();
            }
        }
        final ConnectionPool connectionPool2 = this.connectionPool;
        // monitorenter(connectionPool2)
        while (true) {
            try {
                if (this.canceled) {
                    throw new IOException("Canceled");
                }
                // monitorexit(connectionPool2)
            Label_0464_Outer:
                while (true) {
                    Label_0637: {
                        if (n6 == 0) {
                            break Label_0637;
                        }
                        Object route3 = this.routeSelection.getAll();
                        n6 = 0;
                        size = ((List)route3).size();
                        while (true) {
                            Label_0630: {
                                while (true) {
                                    if (n6 >= size) {
                                        break Label_0637;
                                    }
                                    final Route route4 = ((List<Route>)route3).get(n6);
                                    Internal.instance.get(this.connectionPool, this.address, this, route4);
                                    Label_0488: {
                                        if (this.connection == null) {
                                            break Label_0488;
                                        }
                                        n5 = 1;
                                        route3 = this.connection;
                                        this.route = route4;
                                        if (n5 != 0) {
                                            break Label_0630;
                                        }
                                        Label_0431: {
                                            if ((route3 = route2) != null) {
                                                break Label_0431;
                                            }
                                            try {
                                                route3 = this.routeSelection.next();
                                                this.route = (Route)route3;
                                                this.refusedStreamCount = 0;
                                                final Connection connection3 = new RealConnection(this.connectionPool, (Route)route3);
                                                this.acquire((RealConnection)connection3, false);
                                                // monitorexit(connectionPool2)
                                                if (n5 != 0) {
                                                    this.eventListener.connectionAcquired(this.call, connection3);
                                                    return (RealConnection)connection3;
                                                }
                                                ((RealConnection)connection3).connect(n, n2, n3, n4, b, this.call, this.eventListener);
                                                this.routeDatabase().connected(((RealConnection)connection3).route());
                                                o = null;
                                                synchronized (this.connectionPool) {
                                                    this.reportedAcquired = true;
                                                    Internal.instance.put(this.connectionPool, (RealConnection)connection3);
                                                    route3 = connection3;
                                                    if (((RealConnection)connection3).isMultiplexed()) {
                                                        o = Internal.instance.deduplicate(this.connectionPool, this.address, this);
                                                        route3 = this.connection;
                                                    }
                                                    // monitorexit(this.connectionPool)
                                                    Util.closeQuietly((Socket)o);
                                                    this.eventListener.connectionAcquired(this.call, (Connection)route3);
                                                    return (RealConnection)route3;
                                                }
                                                ++n6;
                                                continue Label_0464_Outer;
                                            }
                                            finally {}
                                        }
                                    }
                                    break;
                                }
                                break Label_0630;
                                throw route2;
                            }
                            final Connection connection3 = (Connection)route3;
                            continue;
                        }
                    }
                    Object route3 = o;
                    continue;
                }
            }
            finally {
                continue;
            }
            break;
        }
    }
    
    private RealConnection findHealthyConnection(final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2) throws IOException {
        while (true) {
            final RealConnection connection = this.findConnection(n, n2, n3, n4, b);
            synchronized (this.connectionPool) {
                if (connection.successCount == 0) {
                    return connection;
                }
                // monitorexit(this.connectionPool)
                if (!connection.isHealthy(b2)) {
                    this.noNewStreams();
                    continue;
                }
            }
            break;
        }
        return;
    }
    
    private void release(final RealConnection realConnection) {
        for (int i = 0; i < realConnection.allocations.size(); ++i) {
            if (realConnection.allocations.get(i).get() == this) {
                realConnection.allocations.remove(i);
                return;
            }
        }
        throw new IllegalStateException();
    }
    
    private Socket releaseIfNoNewStreams() {
        assert Thread.holdsLock(this.connectionPool);
        final RealConnection connection = this.connection;
        if (connection != null && connection.noNewStreams) {
            return this.deallocate(false, false, true);
        }
        return null;
    }
    
    private RouteDatabase routeDatabase() {
        return Internal.instance.routeDatabase(this.connectionPool);
    }
    
    public void acquire(final RealConnection connection, final boolean reportedAcquired) {
        assert Thread.holdsLock(this.connectionPool);
        if (this.connection != null) {
            throw new IllegalStateException();
        }
        this.connection = connection;
        this.reportedAcquired = reportedAcquired;
        connection.allocations.add(new StreamAllocationReference(this, this.callStackTrace));
    }
    
    public void cancel() {
        RealConnection connection;
        while (true) {
            synchronized (this.connectionPool) {
                this.canceled = true;
                final HttpCodec codec = this.codec;
                connection = this.connection;
                // monitorexit(this.connectionPool)
                if (codec != null) {
                    codec.cancel();
                    return;
                }
            }
            if (connection != null) {
                break;
            }
            return;
        }
        connection.cancel();
    }
    
    public HttpCodec codec() {
        synchronized (this.connectionPool) {
            return this.codec;
        }
    }
    
    public RealConnection connection() {
        synchronized (this) {
            return this.connection;
        }
    }
    
    public boolean hasMoreRoutes() {
        return this.route != null || (this.routeSelection != null && this.routeSelection.hasNext()) || this.routeSelector.hasNext();
    }
    
    public HttpCodec newStream(final OkHttpClient okHttpClient, final Interceptor.Chain chain, final boolean b) {
        final int connectTimeoutMillis = chain.connectTimeoutMillis();
        final int timeoutMillis = chain.readTimeoutMillis();
        final int writeTimeoutMillis = chain.writeTimeoutMillis();
        final int pingIntervalMillis = okHttpClient.pingIntervalMillis();
        final boolean retryOnConnectionFailure = okHttpClient.retryOnConnectionFailure();
        try {
            final HttpCodec codec = this.findHealthyConnection(connectTimeoutMillis, timeoutMillis, writeTimeoutMillis, pingIntervalMillis, retryOnConnectionFailure, b).newCodec(okHttpClient, chain, this);
            synchronized (this.connectionPool) {
                return this.codec = codec;
            }
        }
        catch (IOException ex) {
            throw new RouteException(ex);
        }
    }
    
    public void noNewStreams() {
        synchronized (this.connectionPool) {
            RealConnection connection = this.connection;
            final Socket deallocate = this.deallocate(true, false, false);
            if (this.connection != null) {
                connection = null;
            }
            // monitorexit(this.connectionPool)
            Util.closeQuietly(deallocate);
            if (connection != null) {
                this.eventListener.connectionReleased(this.call, connection);
            }
        }
    }
    
    public void release() {
        synchronized (this.connectionPool) {
            RealConnection connection = this.connection;
            final Socket deallocate = this.deallocate(false, true, false);
            if (this.connection != null) {
                connection = null;
            }
            // monitorexit(this.connectionPool)
            Util.closeQuietly(deallocate);
            if (connection != null) {
                this.eventListener.connectionReleased(this.call, connection);
            }
        }
    }
    
    public Socket releaseAndAcquire(final RealConnection connection) {
        assert Thread.holdsLock(this.connectionPool);
        if (this.codec != null || this.connection.allocations.size() != 1) {
            throw new IllegalStateException();
        }
        final Reference<StreamAllocation> reference = this.connection.allocations.get(0);
        final Socket deallocate = this.deallocate(true, false, false);
        this.connection = connection;
        connection.allocations.add(reference);
        return deallocate;
    }
    
    public Route route() {
        return this.route;
    }
    
    public void streamFailed(final IOException ex) {
        while (true) {
            final boolean b = false;
            while (true) {
                synchronized (this.connectionPool) {
                    boolean b2 = false;
                    Label_0070: {
                        if (ex instanceof StreamResetException) {
                            final StreamResetException ex2 = (StreamResetException)ex;
                            if (ex2.errorCode == ErrorCode.REFUSED_STREAM) {
                                ++this.refusedStreamCount;
                            }
                            if (ex2.errorCode == ErrorCode.REFUSED_STREAM) {
                                b2 = b;
                                if (this.refusedStreamCount <= 1) {
                                    break Label_0070;
                                }
                            }
                            b2 = true;
                            this.route = null;
                        }
                        else {
                            b2 = b;
                            if (this.connection != null) {
                                if (this.connection.isMultiplexed()) {
                                    b2 = b;
                                    if (!(ex instanceof ConnectionShutdownException)) {
                                        break Label_0070;
                                    }
                                }
                                b2 = true;
                                if (this.connection.successCount == 0) {
                                    if (this.route != null && ex != null) {
                                        this.routeSelector.connectFailed(this.route, ex);
                                    }
                                    this.route = null;
                                    b2 = b2;
                                }
                            }
                        }
                    }
                    final RealConnection connection = this.connection;
                    final Socket deallocate = this.deallocate(b2, false, true);
                    if (this.connection == null) {
                        if (this.reportedAcquired) {
                            // monitorexit(this.connectionPool)
                            Util.closeQuietly(deallocate);
                            if (connection != null) {
                                this.eventListener.connectionReleased(this.call, connection);
                            }
                            return;
                        }
                    }
                }
                final RealConnection connection = null;
                continue;
            }
        }
    }
    
    public void streamFinished(final boolean b, final HttpCodec httpCodec, final long n, final IOException ex) {
        this.eventListener.responseBodyEnd(this.call, n);
        final ConnectionPool connectionPool = this.connectionPool;
        // monitorenter(connectionPool)
        while (true) {
            if (httpCodec != null) {
                try {
                    if (httpCodec != this.codec) {
                        throw new IllegalStateException("expected " + this.codec + " but was " + httpCodec);
                    }
                }
                finally {
                }
                // monitorexit(connectionPool)
                if (!b) {
                    final RealConnection connection = this.connection;
                    ++connection.successCount;
                }
                RealConnection connection2 = this.connection;
                final Socket deallocate = this.deallocate(b, false, true);
                if (this.connection != null) {
                    connection2 = null;
                }
                final boolean released = this.released;
                // monitorexit(connectionPool)
                Util.closeQuietly(deallocate);
                if (connection2 != null) {
                    this.eventListener.connectionReleased(this.call, connection2);
                }
                if (ex != null) {
                    this.eventListener.callFailed(this.call, ex);
                }
                else if (released) {
                    this.eventListener.callEnd(this.call);
                }
                return;
            }
            continue;
        }
    }
    
    @Override
    public String toString() {
        final RealConnection connection = this.connection();
        if (connection != null) {
            return connection.toString();
        }
        return this.address.toString();
    }
    
    public static final class StreamAllocationReference extends WeakReference<StreamAllocation>
    {
        public final Object callStackTrace;
        
        StreamAllocationReference(final StreamAllocation streamAllocation, final Object callStackTrace) {
            super(streamAllocation);
            this.callStackTrace = callStackTrace;
        }
    }
}
