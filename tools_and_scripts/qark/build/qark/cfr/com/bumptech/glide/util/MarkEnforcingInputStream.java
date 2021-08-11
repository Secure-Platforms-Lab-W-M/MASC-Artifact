/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MarkEnforcingInputStream
extends FilterInputStream {
    private static final int END_OF_STREAM = -1;
    private static final int UNSET = Integer.MIN_VALUE;
    private int availableBytes = Integer.MIN_VALUE;

    public MarkEnforcingInputStream(InputStream inputStream) {
        super(inputStream);
    }

    private long getBytesToRead(long l) {
        int n = this.availableBytes;
        if (n == 0) {
            return -1L;
        }
        if (n != Integer.MIN_VALUE && l > (long)n) {
            return n;
        }
        return l;
    }

    private void updateAvailableBytesAfterRead(long l) {
        int n = this.availableBytes;
        if (n != Integer.MIN_VALUE && l != -1L) {
            this.availableBytes = (int)((long)n - l);
        }
    }

    @Override
    public int available() throws IOException {
        int n = this.availableBytes;
        if (n == Integer.MIN_VALUE) {
            return super.available();
        }
        return Math.min(n, super.available());
    }

    @Override
    public void mark(int n) {
        synchronized (this) {
            super.mark(n);
            this.availableBytes = n;
            return;
        }
    }

    @Override
    public int read() throws IOException {
        if (this.getBytesToRead(1L) == -1L) {
            return -1;
        }
        int n = super.read();
        this.updateAvailableBytesAfterRead(1L);
        return n;
    }

    @Override
    public int read(byte[] arrby, int n, int n2) throws IOException {
        if ((n2 = (int)this.getBytesToRead(n2)) == -1) {
            return -1;
        }
        n = super.read(arrby, n, n2);
        this.updateAvailableBytesAfterRead(n);
        return n;
    }

    @Override
    public void reset() throws IOException {
        synchronized (this) {
            super.reset();
            this.availableBytes = Integer.MIN_VALUE;
            return;
        }
    }

    @Override
    public long skip(long l) throws IOException {
        if ((l = this.getBytesToRead(l)) == -1L) {
            return 0L;
        }
        l = super.skip(l);
        this.updateAvailableBytesAfterRead(l);
        return l;
    }
}

