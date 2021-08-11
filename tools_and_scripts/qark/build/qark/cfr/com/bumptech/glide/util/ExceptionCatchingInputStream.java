/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.util;

import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

public class ExceptionCatchingInputStream
extends InputStream {
    private static final Queue<ExceptionCatchingInputStream> QUEUE = Util.createQueue(0);
    private IOException exception;
    private InputStream wrapped;

    ExceptionCatchingInputStream() {
    }

    static void clearQueue() {
        while (!QUEUE.isEmpty()) {
            QUEUE.remove();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ExceptionCatchingInputStream obtain(InputStream inputStream) {
        ExceptionCatchingInputStream exceptionCatchingInputStream;
        Object object = QUEUE;
        synchronized (object) {
            exceptionCatchingInputStream = QUEUE.poll();
        }
        object = exceptionCatchingInputStream;
        if (exceptionCatchingInputStream == null) {
            object = new ExceptionCatchingInputStream();
        }
        object.setInputStream(inputStream);
        return object;
    }

    @Override
    public int available() throws IOException {
        return this.wrapped.available();
    }

    @Override
    public void close() throws IOException {
        this.wrapped.close();
    }

    public IOException getException() {
        return this.exception;
    }

    @Override
    public void mark(int n) {
        this.wrapped.mark(n);
    }

    @Override
    public boolean markSupported() {
        return this.wrapped.markSupported();
    }

    @Override
    public int read() {
        try {
            int n = this.wrapped.read();
            return n;
        }
        catch (IOException iOException) {
            this.exception = iOException;
            return -1;
        }
    }

    @Override
    public int read(byte[] arrby) {
        try {
            int n = this.wrapped.read(arrby);
            return n;
        }
        catch (IOException iOException) {
            this.exception = iOException;
            return -1;
        }
    }

    @Override
    public int read(byte[] arrby, int n, int n2) {
        try {
            n = this.wrapped.read(arrby, n, n2);
            return n;
        }
        catch (IOException iOException) {
            this.exception = iOException;
            return -1;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void release() {
        this.exception = null;
        this.wrapped = null;
        Queue<ExceptionCatchingInputStream> queue = QUEUE;
        synchronized (queue) {
            QUEUE.offer(this);
            return;
        }
    }

    @Override
    public void reset() throws IOException {
        synchronized (this) {
            this.wrapped.reset();
            return;
        }
    }

    void setInputStream(InputStream inputStream) {
        this.wrapped = inputStream;
    }

    @Override
    public long skip(long l) {
        try {
            l = this.wrapped.skip(l);
            return l;
        }
        catch (IOException iOException) {
            this.exception = iOException;
            return 0L;
        }
    }
}

