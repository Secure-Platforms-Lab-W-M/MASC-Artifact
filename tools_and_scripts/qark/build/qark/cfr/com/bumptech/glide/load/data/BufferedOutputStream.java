/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.data;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.IOException;
import java.io.OutputStream;

public final class BufferedOutputStream
extends OutputStream {
    private ArrayPool arrayPool;
    private byte[] buffer;
    private int index;
    private final OutputStream out;

    public BufferedOutputStream(OutputStream outputStream, ArrayPool arrayPool) {
        this(outputStream, arrayPool, 65536);
    }

    BufferedOutputStream(OutputStream outputStream, ArrayPool arrayPool, int n) {
        this.out = outputStream;
        this.arrayPool = arrayPool;
        this.buffer = arrayPool.get(n, byte[].class);
    }

    private void flushBuffer() throws IOException {
        int n = this.index;
        if (n > 0) {
            this.out.write(this.buffer, 0, n);
            this.index = 0;
        }
    }

    private void maybeFlushBuffer() throws IOException {
        if (this.index == this.buffer.length) {
            this.flushBuffer();
        }
    }

    private void release() {
        byte[] arrby = this.buffer;
        if (arrby != null) {
            this.arrayPool.put(arrby);
            this.buffer = null;
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.flush();
            this.release();
            return;
        }
        finally {
            this.out.close();
        }
    }

    @Override
    public void flush() throws IOException {
        this.flushBuffer();
        this.out.flush();
    }

    @Override
    public void write(int n) throws IOException {
        byte[] arrby = this.buffer;
        int n2 = this.index;
        this.index = n2 + 1;
        arrby[n2] = (byte)n;
        this.maybeFlushBuffer();
    }

    @Override
    public void write(byte[] arrby) throws IOException {
        this.write(arrby, 0, arrby.length);
    }

    @Override
    public void write(byte[] arrby, int n, int n2) throws IOException {
        int n3;
        int n4 = 0;
        do {
            n3 = n2 - n4;
            int n5 = n + n4;
            if (this.index == 0 && n3 >= this.buffer.length) {
                this.out.write(arrby, n5, n3);
                return;
            }
            n3 = Math.min(n3, this.buffer.length - this.index);
            System.arraycopy(arrby, n5, this.buffer, this.index, n3);
            this.index += n3;
            this.maybeFlushBuffer();
        } while ((n4 += n3) < n2);
    }
}

