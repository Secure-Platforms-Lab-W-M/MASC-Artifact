// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import java.util.ArrayList;
import javax.annotation.Nullable;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.connection.StreamAllocation;
import java.lang.ref.Reference;
import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import okhttp3.internal.Util;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import okhttp3.internal.connection.RouteDatabase;
import okhttp3.internal.connection.RealConnection;
import java.util.Deque;
import java.util.concurrent.Executor;

public final class ConnectionPool
{
    private static final Executor executor;
    private final Runnable cleanupRunnable;
    boolean cleanupRunning;
    private final Deque<RealConnection> connections;
    private final long keepAliveDurationNs;
    private final int maxIdleConnections;
    final RouteDatabase routeDatabase;
    
    static {
        executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp ConnectionPool", true));
    }
    
    public ConnectionPool() {
        this(5, 5L, TimeUnit.MINUTES);
    }
    
    public ConnectionPool(final int maxIdleConnections, final long n, final TimeUnit timeUnit) {
        this.cleanupRunnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final long cleanup = ConnectionPool.this.cleanup(System.nanoTime());
                    if (cleanup == -1L) {
                        break;
                    }
                    if (cleanup <= 0L) {
                        continue;
                    }
                    final long n = cleanup / 1000000L;
                    final ConnectionPool this$0 = ConnectionPool.this;
                    // monitorenter(this$0)
                    while (true) {
                        try {
                            try {
                                ConnectionPool.this.wait(n, (int)(cleanup - n * 1000000L));
                            }
                            finally {
                            }
                            // monitorexit(this$0)
                        }
                        catch (InterruptedException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        };
        this.connections = new ArrayDeque<RealConnection>();
        this.routeDatabase = new RouteDatabase();
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDurationNs = timeUnit.toNanos(n);
        if (n <= 0L) {
            throw new IllegalArgumentException("keepAliveDuration <= 0: " + n);
        }
    }
    
    private int pruneAndGetAllocationCount(final RealConnection realConnection, final long n) {
        final List<Reference<StreamAllocation>> allocations = realConnection.allocations;
        int i = 0;
        while (i < allocations.size()) {
            final Reference<StreamAllocation> reference = allocations.get(i);
            if (reference.get() != null) {
                ++i;
            }
            else {
                Platform.get().logCloseableLeak("A connection to " + realConnection.route().address().url() + " was leaked. Did you forget to close a response body?", ((StreamAllocation.StreamAllocationReference)reference).callStackTrace);
                allocations.remove(i);
                realConnection.noNewStreams = true;
                if (allocations.isEmpty()) {
                    realConnection.idleAtNanos = n - this.keepAliveDurationNs;
                    return 0;
                }
                continue;
            }
        }
        return allocations.size();
    }
    
    long cleanup(long n) {
        int n2 = 0;
        int n3 = 0;
        RealConnection realConnection = null;
        long n4 = Long.MIN_VALUE;
        synchronized (this) {
            for (final RealConnection realConnection2 : this.connections) {
                if (this.pruneAndGetAllocationCount(realConnection2, n) > 0) {
                    ++n2;
                }
                else {
                    final int n5 = n3 + 1;
                    final long n6 = n - realConnection2.idleAtNanos;
                    n3 = n5;
                    if (n6 <= n4) {
                        continue;
                    }
                    n4 = n6;
                    realConnection = realConnection2;
                    n3 = n5;
                }
            }
            if (n4 >= this.keepAliveDurationNs || n3 > this.maxIdleConnections) {
                this.connections.remove(realConnection);
                // monitorexit(this)
                Util.closeQuietly(realConnection.socket());
                return 0L;
            }
            if (n3 > 0) {
                n = this.keepAliveDurationNs;
                return n - n4;
            }
        }
        if (n2 > 0) {
            n = this.keepAliveDurationNs;
            // monitorexit(this)
            return n;
        }
        this.cleanupRunning = false;
        // monitorexit(this)
        return -1L;
    }
    
    boolean connectionBecameIdle(final RealConnection realConnection) {
        assert Thread.holdsLock(this);
        if (realConnection.noNewStreams || this.maxIdleConnections == 0) {
            this.connections.remove(realConnection);
            return true;
        }
        this.notifyAll();
        return false;
    }
    
    public int connectionCount() {
        synchronized (this) {
            return this.connections.size();
        }
    }
    
    @Nullable
    Socket deduplicate(final Address address, final StreamAllocation streamAllocation) {
        final Socket socket = null;
        assert Thread.holdsLock(this);
        final Iterator<RealConnection> iterator = this.connections.iterator();
        RealConnection realConnection;
        do {
            final Socket releaseAndAcquire = socket;
            if (!iterator.hasNext()) {
                return releaseAndAcquire;
            }
            realConnection = iterator.next();
        } while (!realConnection.isEligible(address, null) || !realConnection.isMultiplexed() || realConnection == streamAllocation.connection());
        return streamAllocation.releaseAndAcquire(realConnection);
    }
    
    public void evictAll() {
        final ArrayList<RealConnection> list = new ArrayList<RealConnection>();
        synchronized (this) {
            final Iterator<RealConnection> iterator = this.connections.iterator();
            while (iterator.hasNext()) {
                final RealConnection realConnection = iterator.next();
                if (realConnection.allocations.isEmpty()) {
                    realConnection.noNewStreams = true;
                    list.add(realConnection);
                    iterator.remove();
                }
            }
        }
        // monitorexit(this)
        final List<RealConnection> list2;
        final Iterator<RealConnection> iterator2 = list2.iterator();
        while (iterator2.hasNext()) {
            Util.closeQuietly(iterator2.next().socket());
        }
    }
    
    @Nullable
    RealConnection get(final Address address, final StreamAllocation streamAllocation, final Route route) {
        assert Thread.holdsLock(this);
        for (final RealConnection realConnection : this.connections) {
            if (realConnection.isEligible(address, route)) {
                streamAllocation.acquire(realConnection, true);
                return realConnection;
            }
        }
        return null;
    }
    
    public int idleConnectionCount() {
        // monitorenter(this)
        int n = 0;
        try {
            final Iterator<RealConnection> iterator = this.connections.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().allocations.isEmpty()) {
                    ++n;
                }
            }
            return n;
        }
        finally {
        }
        // monitorexit(this)
    }
    
    void put(final RealConnection realConnection) {
        assert Thread.holdsLock(this);
        if (!this.cleanupRunning) {
            this.cleanupRunning = true;
            ConnectionPool.executor.execute(this.cleanupRunnable);
        }
        this.connections.add(realConnection);
    }
}
