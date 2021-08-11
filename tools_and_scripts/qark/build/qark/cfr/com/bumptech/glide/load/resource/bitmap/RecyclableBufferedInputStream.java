/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecyclableBufferedInputStream
extends FilterInputStream {
    private volatile byte[] buf;
    private final ArrayPool byteArrayPool;
    private int count;
    private int marklimit;
    private int markpos = -1;
    private int pos;

    public RecyclableBufferedInputStream(InputStream inputStream, ArrayPool arrayPool) {
        this(inputStream, arrayPool, 65536);
    }

    RecyclableBufferedInputStream(InputStream inputStream, ArrayPool arrayPool, int n) {
        super(inputStream);
        this.byteArrayPool = arrayPool;
        this.buf = arrayPool.get(n, byte[].class);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private int fillbuf(InputStream var1_1, byte[] var2_2) throws IOException {
        block4 : {
            block5 : {
                var3_3 = this.markpos;
                if (var3_3 == -1 || (var4_4 = this.pos) - var3_3 >= (var5_5 = this.marklimit)) break block4;
                if (var3_3 != 0 || var5_5 <= var2_2.length || this.count != var2_2.length) break block5;
                var3_3 = var4_4 = var2_2.length * 2;
                if (var4_4 > var5_5) {
                    var3_3 = this.marklimit;
                }
                var6_6 = this.byteArrayPool.get(var3_3, byte[].class);
                System.arraycopy(var2_2, 0, var6_6, 0, var2_2.length);
                this.buf = var6_6;
                this.byteArrayPool.put(var2_2);
                ** GOTO lbl-1000
            }
            var3_3 = this.markpos;
            var6_6 = var2_2;
            if (var3_3 > 0) {
                System.arraycopy(var2_2, var3_3, var2_2, 0, var2_2.length - var3_3);
            } else lbl-1000: // 2 sources:
            {
                var2_2 = var6_6;
            }
            this.pos = var3_3 = this.pos - this.markpos;
            this.markpos = 0;
            this.count = 0;
            var4_4 = var1_1.read(var2_2, var3_3, var2_2.length - var3_3);
            var3_3 = this.pos;
            if (var4_4 > 0) {
                var3_3 += var4_4;
            }
            this.count = var3_3;
            return var4_4;
        }
        var3_3 = var1_1.read(var2_2);
        if (var3_3 <= 0) return var3_3;
        this.markpos = -1;
        this.pos = 0;
        this.count = var3_3;
        return var3_3;
    }

    private static IOException streamClosed() throws IOException {
        throw new IOException("BufferedInputStream is closed");
    }

    @Override
    public int available() throws IOException {
        synchronized (this) {
            block5 : {
                InputStream inputStream = this.in;
                if (this.buf == null || inputStream == null) break block5;
                int n = this.count;
                int n2 = this.pos;
                int n3 = inputStream.available();
                return n - n2 + n3;
            }
            throw RecyclableBufferedInputStream.streamClosed();
        }
    }

    @Override
    public void close() throws IOException {
        if (this.buf != null) {
            this.byteArrayPool.put(this.buf);
            this.buf = null;
        }
        InputStream inputStream = this.in;
        this.in = null;
        if (inputStream != null) {
            inputStream.close();
        }
    }

    public void fixMarkLimit() {
        synchronized (this) {
            this.marklimit = this.buf.length;
            return;
        }
    }

    @Override
    public void mark(int n) {
        synchronized (this) {
            this.marklimit = Math.max(this.marklimit, n);
            this.markpos = this.pos;
            return;
        }
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int read() throws IOException {
        synchronized (this) {
            byte[] arrby = this.buf;
            byte[] arrby2 = this.in;
            if (arrby != null && arrby2 != null) {
                int n;
                if (this.pos >= this.count && (n = this.fillbuf((InputStream)arrby2, arrby)) == -1) {
                    return -1;
                }
                arrby2 = arrby;
                if (arrby != this.buf && (arrby2 = this.buf) == null) {
                    throw RecyclableBufferedInputStream.streamClosed();
                }
                if (this.count - this.pos > 0) {
                    n = this.pos;
                    this.pos = n + 1;
                    n = arrby2[n];
                    return n & 255;
                }
                return -1;
            }
            throw RecyclableBufferedInputStream.streamClosed();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public int read(byte[] arrby, int n, int n2) throws IOException {
        void var2_2;
        int n3;
        void var3_3;
        int n4;
        // MONITORENTER : this
        byte[] arrby2 = this.buf;
        if (arrby2 == null) throw RecyclableBufferedInputStream.streamClosed();
        if (var3_3 == false) {
            // MONITOREXIT : this
            return 0;
        }
        InputStream inputStream = this.in;
        if (inputStream == null) throw RecyclableBufferedInputStream.streamClosed();
        if (this.pos < this.count) {
            n3 = this.count - this.pos >= var3_3 ? var3_3 : this.count - this.pos;
            System.arraycopy(arrby2, this.pos, arrby, (int)var2_2, n3);
            this.pos += n3;
            if (n3 == var3_3) {
                // MONITOREXIT : this
                return n3;
            }
            n4 = inputStream.available();
            if (n4 == 0) {
                return n3;
            }
            n4 = var2_2 + n3;
            var2_2 = var3_3 - n3;
            n3 = n4;
        } else {
            n4 = var3_3;
            n3 = var2_2;
            var2_2 = n4;
        }
        do {
            int n5 = this.markpos;
            n4 = -1;
            if (n5 == -1 && var2_2 >= arrby2.length) {
                int n6;
                n5 = n6 = inputStream.read(arrby, n3, (int)var2_2);
                if (n6 == -1) {
                    if (var2_2 == var3_3) {
                        return n4;
                    }
                    n4 = var3_3 - var2_2;
                    // MONITOREXIT : this
                    return n4;
                }
            } else {
                n5 = this.fillbuf(inputStream, arrby2);
                if (n5 == -1) {
                    if (var2_2 == var3_3) {
                        return n4;
                    }
                    n4 = var3_3 - var2_2;
                    // MONITOREXIT : this
                    return n4;
                }
                byte[] arrby3 = arrby2;
                if (arrby2 != this.buf) {
                    arrby3 = this.buf;
                    if (arrby3 == null) throw RecyclableBufferedInputStream.streamClosed();
                }
                n4 = this.count - this.pos >= var2_2 ? var2_2 : this.count - this.pos;
                System.arraycopy(arrby3, this.pos, arrby, n3, n4);
                this.pos += n4;
                n5 = n4;
                arrby2 = arrby3;
            }
            if ((var2_2 -= n5) == false) {
                // MONITOREXIT : this
                return (int)var3_3;
            }
            n4 = inputStream.available();
            if (n4 == 0) {
                // MONITOREXIT : this
                return (int)(var3_3 - var2_2);
            }
            n3 += n5;
        } while (true);
    }

    public void release() {
        synchronized (this) {
            if (this.buf != null) {
                this.byteArrayPool.put(this.buf);
                this.buf = null;
            }
            return;
        }
    }

    @Override
    public void reset() throws IOException {
        synchronized (this) {
            if (this.buf != null) {
                if (-1 != this.markpos) {
                    this.pos = this.markpos;
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Mark has been invalidated, pos: ");
                stringBuilder.append(this.pos);
                stringBuilder.append(" markLimit: ");
                stringBuilder.append(this.marklimit);
                throw new InvalidMarkException(stringBuilder.toString());
            }
            throw new IOException("Stream is closed");
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public long skip(long l) throws IOException {
        synchronized (this) {
            block11 : {
                long l2;
                InputStream inputStream;
                block13 : {
                    block15 : {
                        block14 : {
                            byte[] arrby;
                            block12 : {
                                if (l < 1L) {
                                    return 0L;
                                }
                                arrby = this.buf;
                                if (arrby == null) throw RecyclableBufferedInputStream.streamClosed();
                                inputStream = this.in;
                                if (inputStream == null) break block11;
                                if ((long)(this.count - this.pos) < l) break block12;
                                this.pos = (int)((long)this.pos + l);
                                return l;
                            }
                            l2 = (long)this.count - (long)this.pos;
                            this.pos = this.count;
                            if (this.markpos == -1 || l > (long)this.marklimit) break block13;
                            int n = this.fillbuf(inputStream, arrby);
                            if (n != -1) break block14;
                            return l2;
                        }
                        if ((long)(this.count - this.pos) < l - l2) break block15;
                        this.pos = (int)((long)this.pos + l - l2);
                        return l;
                    }
                    l = this.count;
                    long l3 = this.pos;
                    this.pos = this.count;
                    return l + l2 - l3;
                }
                l = inputStream.skip(l - l2);
                return l + l2;
            }
            throw RecyclableBufferedInputStream.streamClosed();
        }
    }

    static class InvalidMarkException
    extends IOException {
        private static final long serialVersionUID = -4338378848813561757L;

        InvalidMarkException(String string2) {
            super(string2);
        }
    }

}

