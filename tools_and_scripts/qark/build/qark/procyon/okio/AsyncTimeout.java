// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import java.io.InterruptedIOException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class AsyncTimeout extends Timeout
{
    private static final long IDLE_TIMEOUT_MILLIS;
    private static final long IDLE_TIMEOUT_NANOS;
    private static final int TIMEOUT_WRITE_SIZE = 65536;
    @Nullable
    static AsyncTimeout head;
    private boolean inQueue;
    @Nullable
    private AsyncTimeout next;
    private long timeoutAt;
    
    static {
        IDLE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60L);
        IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(AsyncTimeout.IDLE_TIMEOUT_MILLIS);
    }
    
    @Nullable
    static AsyncTimeout awaitTimeout() throws InterruptedException {
        final AsyncTimeout asyncTimeout = null;
        final AsyncTimeout next = AsyncTimeout.head.next;
        if (next == null) {
            final long nanoTime = System.nanoTime();
            AsyncTimeout.class.wait(AsyncTimeout.IDLE_TIMEOUT_MILLIS);
            AsyncTimeout head = asyncTimeout;
            if (AsyncTimeout.head.next == null) {
                head = asyncTimeout;
                if (System.nanoTime() - nanoTime >= AsyncTimeout.IDLE_TIMEOUT_NANOS) {
                    head = AsyncTimeout.head;
                }
            }
            return head;
        }
        final long remainingNanos = next.remainingNanos(System.nanoTime());
        if (remainingNanos > 0L) {
            final long n = remainingNanos / 1000000L;
            AsyncTimeout.class.wait(n, (int)(remainingNanos - n * 1000000L));
            return null;
        }
        AsyncTimeout.head.next = next.next;
        next.next = null;
        return next;
    }
    
    private static boolean cancelScheduledTimeout(final AsyncTimeout asyncTimeout) {
        synchronized (AsyncTimeout.class) {
            for (AsyncTimeout asyncTimeout2 = AsyncTimeout.head; asyncTimeout2 != null; asyncTimeout2 = asyncTimeout2.next) {
                if (asyncTimeout2.next == asyncTimeout) {
                    asyncTimeout2.next = asyncTimeout.next;
                    asyncTimeout.next = null;
                    return false;
                }
            }
            return true;
        }
    }
    
    private long remainingNanos(final long n) {
        return this.timeoutAt - n;
    }
    
    private static void scheduleTimeout(final AsyncTimeout next, long remainingNanos, final boolean b) {
    Label_0074_Outer:
        while (true) {
            while (true) {
                AsyncTimeout asyncTimeout = null;
                Label_0175: {
                    while (true) {
                        Label_0152: {
                            synchronized (AsyncTimeout.class) {
                                if (AsyncTimeout.head == null) {
                                    AsyncTimeout.head = new AsyncTimeout();
                                    new Watchdog().start();
                                }
                                final long nanoTime = System.nanoTime();
                                if (remainingNanos != 0L && b) {
                                    next.timeoutAt = Math.min(remainingNanos, next.deadlineNanoTime() - nanoTime) + nanoTime;
                                }
                                else {
                                    if (remainingNanos == 0L) {
                                        break Label_0152;
                                    }
                                    next.timeoutAt = nanoTime + remainingNanos;
                                }
                                remainingNanos = next.remainingNanos(nanoTime);
                                asyncTimeout = AsyncTimeout.head;
                                if (asyncTimeout.next == null || remainingNanos < asyncTimeout.next.remainingNanos(nanoTime)) {
                                    next.next = asyncTimeout.next;
                                    asyncTimeout.next = next;
                                    if (asyncTimeout == AsyncTimeout.head) {
                                        AsyncTimeout.class.notify();
                                    }
                                    return;
                                }
                                break Label_0175;
                            }
                        }
                        if (b) {
                            next.timeoutAt = next.deadlineNanoTime();
                            continue Label_0074_Outer;
                        }
                        break;
                    }
                    break;
                }
                asyncTimeout = asyncTimeout.next;
                continue;
            }
        }
        throw new AssertionError();
    }
    
    public final void enter() {
        if (this.inQueue) {
            throw new IllegalStateException("Unbalanced enter/exit");
        }
        final long timeoutNanos = this.timeoutNanos();
        final boolean hasDeadline = this.hasDeadline();
        if (timeoutNanos == 0L && !hasDeadline) {
            return;
        }
        this.inQueue = true;
        scheduleTimeout(this, timeoutNanos, hasDeadline);
    }
    
    final IOException exit(final IOException ex) throws IOException {
        if (!this.exit()) {
            return ex;
        }
        return this.newTimeoutException(ex);
    }
    
    final void exit(final boolean b) throws IOException {
        if (this.exit() && b) {
            throw this.newTimeoutException(null);
        }
    }
    
    public final boolean exit() {
        if (!this.inQueue) {
            return false;
        }
        this.inQueue = false;
        return cancelScheduledTimeout(this);
    }
    
    protected IOException newTimeoutException(@Nullable final IOException ex) {
        final InterruptedIOException ex2 = new InterruptedIOException("timeout");
        if (ex != null) {
            ex2.initCause(ex);
        }
        return ex2;
    }
    
    public final Sink sink(final Sink sink) {
        return new Sink() {
            @Override
            public void close() throws IOException {
                AsyncTimeout.this.enter();
                try {
                    sink.close();
                    AsyncTimeout.this.exit(true);
                }
                catch (IOException ex) {
                    throw AsyncTimeout.this.exit(ex);
                }
                finally {
                    AsyncTimeout.this.exit(false);
                }
            }
            
            @Override
            public void flush() throws IOException {
                AsyncTimeout.this.enter();
                try {
                    sink.flush();
                    AsyncTimeout.this.exit(true);
                }
                catch (IOException ex) {
                    throw AsyncTimeout.this.exit(ex);
                }
                finally {
                    AsyncTimeout.this.exit(false);
                }
            }
            
            @Override
            public Timeout timeout() {
                return AsyncTimeout.this;
            }
            
            @Override
            public String toString() {
                return "AsyncTimeout.sink(" + sink + ")";
            }
            
            @Override
            public void write(final Buffer buffer, long n) throws IOException {
                Util.checkOffsetAndCount(buffer.size, 0L, n);
            Label_0130:
                while (n > 0L) {
                    long n2 = 0L;
                    Segment segment = buffer.head;
                    while (true) {
                        long n3 = n2;
                        Label_0099: {
                            if (n2 < 65536L) {
                                n2 += segment.limit - segment.pos;
                                if (n2 < n) {
                                    break Label_0099;
                                }
                                n3 = n;
                            }
                            AsyncTimeout.this.enter();
                            try {
                                sink.write(buffer, n3);
                                n -= n3;
                                AsyncTimeout.this.exit(true);
                                break;
                                segment = segment.next;
                                continue;
                            }
                            catch (IOException ex) {
                                throw AsyncTimeout.this.exit(ex);
                            }
                            finally {
                                AsyncTimeout.this.exit(false);
                            }
                        }
                        break Label_0130;
                    }
                }
            }
        };
    }
    
    public final Source source(final Source source) {
        return new Source() {
            @Override
            public void close() throws IOException {
                try {
                    source.close();
                    AsyncTimeout.this.exit(true);
                }
                catch (IOException ex) {
                    throw AsyncTimeout.this.exit(ex);
                }
                finally {
                    AsyncTimeout.this.exit(false);
                }
            }
            
            @Override
            public long read(final Buffer buffer, long read) throws IOException {
                AsyncTimeout.this.enter();
                try {
                    read = source.read(buffer, read);
                    AsyncTimeout.this.exit(true);
                    return read;
                }
                catch (IOException ex) {
                    throw AsyncTimeout.this.exit(ex);
                }
                finally {
                    AsyncTimeout.this.exit(false);
                }
            }
            
            @Override
            public Timeout timeout() {
                return AsyncTimeout.this;
            }
            
            @Override
            public String toString() {
                return "AsyncTimeout.source(" + source + ")";
            }
        };
    }
    
    protected void timedOut() {
    }
    
    private static final class Watchdog extends Thread
    {
        Watchdog() {
            super("Okio Watchdog");
            this.setDaemon(true);
        }
        
        @Override
        public void run() {
        Label_0000_Outer:
            while (true) {
                // monitorexit(AsyncTimeout.class)
                while (true) {
                    try {
                        while (true) {
                            synchronized (AsyncTimeout.class) {
                                if (AsyncTimeout.awaitTimeout() == null) {
                                    continue Label_0000_Outer;
                                }
                                break;
                            }
                        }
                    }
                    catch (InterruptedException ex) {
                        continue Label_0000_Outer;
                    }
                    final AsyncTimeout asyncTimeout;
                    if (asyncTimeout == AsyncTimeout.head) {
                        break;
                    }
                    asyncTimeout.timedOut();
                    continue;
                }
            }
            AsyncTimeout.head = null;
        }
        // monitorexit(AsyncTimeout.class)
    }
}
