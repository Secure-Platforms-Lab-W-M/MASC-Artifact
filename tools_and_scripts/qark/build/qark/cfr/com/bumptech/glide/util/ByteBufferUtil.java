/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.util.concurrent.atomic.AtomicReference;

public final class ByteBufferUtil {
    private static final AtomicReference<byte[]> BUFFER_REF = new AtomicReference();
    private static final int BUFFER_SIZE = 16384;

    private ByteBufferUtil() {
    }

    public static ByteBuffer fromFile(File object) throws IOException {
        Object object2;
        AbstractInterruptibleChannel abstractInterruptibleChannel;
        Object object3;
        AbstractInterruptibleChannel abstractInterruptibleChannel2;
        block17 : {
            block18 : {
                block19 : {
                    long l;
                    object2 = null;
                    abstractInterruptibleChannel2 = null;
                    object3 = object2;
                    abstractInterruptibleChannel = abstractInterruptibleChannel2;
                    try {
                        l = object.length();
                        if (l > Integer.MAX_VALUE) break block17;
                        if (l == 0L) break block18;
                        object3 = object2;
                        abstractInterruptibleChannel = abstractInterruptibleChannel2;
                    }
                    catch (Throwable throwable) {
                        if (abstractInterruptibleChannel != null) {
                            try {
                                abstractInterruptibleChannel.close();
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                        }
                        if (object3 != null) {
                            try {
                                object3.close();
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                        }
                        throw throwable;
                    }
                    object3 = object = new RandomAccessFile((File)object, "r");
                    abstractInterruptibleChannel = abstractInterruptibleChannel2;
                    abstractInterruptibleChannel2 = object.getChannel();
                    object3 = object;
                    abstractInterruptibleChannel = abstractInterruptibleChannel2;
                    object2 = abstractInterruptibleChannel2.map(FileChannel.MapMode.READ_ONLY, 0L, l).load();
                    if (abstractInterruptibleChannel2 == null) break block19;
                    try {
                        abstractInterruptibleChannel2.close();
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                try {
                    object.close();
                    return object2;
                }
                catch (IOException iOException) {
                    return object2;
                }
            }
            object3 = object2;
            abstractInterruptibleChannel = abstractInterruptibleChannel2;
            throw new IOException("File unsuitable for memory mapping");
        }
        object3 = object2;
        abstractInterruptibleChannel = abstractInterruptibleChannel2;
        throw new IOException("File too large to map into memory");
    }

    public static ByteBuffer fromStream(InputStream arrby) throws IOException {
        int n;
        byte[] arrby2;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16384);
        byte[] arrby3 = arrby2 = (byte[])BUFFER_REF.getAndSet(null);
        if (arrby2 == null) {
            arrby3 = new byte[16384];
        }
        while ((n = arrby.read(arrby3)) >= 0) {
            byteArrayOutputStream.write(arrby3, 0, n);
        }
        BUFFER_REF.set(arrby3);
        arrby = byteArrayOutputStream.toByteArray();
        return (ByteBuffer)ByteBuffer.allocateDirect(arrby.length).put(arrby).position(0);
    }

    private static SafeArray getSafeArray(ByteBuffer byteBuffer) {
        if (!byteBuffer.isReadOnly() && byteBuffer.hasArray()) {
            return new SafeArray(byteBuffer.array(), byteBuffer.arrayOffset(), byteBuffer.limit());
        }
        return null;
    }

    public static byte[] toBytes(ByteBuffer byteBuffer) {
        byte[] arrby = ByteBufferUtil.getSafeArray(byteBuffer);
        if (arrby != null && arrby.offset == 0 && arrby.limit == arrby.data.length) {
            return byteBuffer.array();
        }
        byteBuffer = byteBuffer.asReadOnlyBuffer();
        arrby = new byte[byteBuffer.limit()];
        byteBuffer.position(0);
        byteBuffer.get(arrby);
        return arrby;
    }

    public static void toFile(ByteBuffer byteBuffer, File object) throws IOException {
        block17 : {
            AbstractInterruptibleChannel abstractInterruptibleChannel;
            byteBuffer.position(0);
            Object object2 = null;
            AbstractInterruptibleChannel abstractInterruptibleChannel2 = abstractInterruptibleChannel = null;
            try {
                object2 = object = new RandomAccessFile((File)object, "rw");
                abstractInterruptibleChannel2 = abstractInterruptibleChannel;
            }
            catch (Throwable throwable) {
                if (abstractInterruptibleChannel2 != null) {
                    try {
                        abstractInterruptibleChannel2.close();
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                if (object2 != null) {
                    try {
                        object2.close();
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                throw throwable;
            }
            abstractInterruptibleChannel = object.getChannel();
            object2 = object;
            abstractInterruptibleChannel2 = abstractInterruptibleChannel;
            abstractInterruptibleChannel.write(byteBuffer);
            object2 = object;
            abstractInterruptibleChannel2 = abstractInterruptibleChannel;
            abstractInterruptibleChannel.force(false);
            object2 = object;
            abstractInterruptibleChannel2 = abstractInterruptibleChannel;
            abstractInterruptibleChannel.close();
            object2 = object;
            abstractInterruptibleChannel2 = abstractInterruptibleChannel;
            object.close();
            if (abstractInterruptibleChannel == null) break block17;
            try {
                abstractInterruptibleChannel.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        try {
            object.close();
            return;
        }
        catch (IOException iOException) {
            return;
        }
    }

    public static InputStream toStream(ByteBuffer byteBuffer) {
        return new ByteBufferStream(byteBuffer);
    }

    public static void toStream(ByteBuffer byteBuffer, OutputStream outputStream) throws IOException {
        byte[] arrby;
        byte[] arrby2 = ByteBufferUtil.getSafeArray(byteBuffer);
        if (arrby2 != null) {
            outputStream.write(arrby2.data, arrby2.offset, arrby2.offset + arrby2.limit);
            return;
        }
        arrby2 = arrby = (byte[])BUFFER_REF.getAndSet(null);
        if (arrby == null) {
            arrby2 = new byte[16384];
        }
        while (byteBuffer.remaining() > 0) {
            int n = Math.min(byteBuffer.remaining(), arrby2.length);
            byteBuffer.get(arrby2, 0, n);
            outputStream.write(arrby2, 0, n);
        }
        BUFFER_REF.set(arrby2);
    }

    private static class ByteBufferStream
    extends InputStream {
        private static final int UNSET = -1;
        private final ByteBuffer byteBuffer;
        private int markPos = -1;

        ByteBufferStream(ByteBuffer byteBuffer) {
            this.byteBuffer = byteBuffer;
        }

        @Override
        public int available() {
            return this.byteBuffer.remaining();
        }

        @Override
        public void mark(int n) {
            synchronized (this) {
                this.markPos = this.byteBuffer.position();
                return;
            }
        }

        @Override
        public boolean markSupported() {
            return true;
        }

        @Override
        public int read() {
            if (!this.byteBuffer.hasRemaining()) {
                return -1;
            }
            return this.byteBuffer.get() & 255;
        }

        @Override
        public int read(byte[] arrby, int n, int n2) throws IOException {
            if (!this.byteBuffer.hasRemaining()) {
                return -1;
            }
            n2 = Math.min(n2, this.available());
            this.byteBuffer.get(arrby, n, n2);
            return n2;
        }

        @Override
        public void reset() throws IOException {
            synchronized (this) {
                if (this.markPos != -1) {
                    this.byteBuffer.position(this.markPos);
                    return;
                }
                throw new IOException("Cannot reset to unset mark position");
            }
        }

        @Override
        public long skip(long l) throws IOException {
            if (!this.byteBuffer.hasRemaining()) {
                return -1L;
            }
            l = Math.min(l, (long)this.available());
            ByteBuffer byteBuffer = this.byteBuffer;
            byteBuffer.position((int)((long)byteBuffer.position() + l));
            return l;
        }
    }

    static final class SafeArray {
        final byte[] data;
        final int limit;
        final int offset;

        SafeArray(byte[] arrby, int n, int n2) {
            this.data = arrby;
            this.offset = n;
            this.limit = n2;
        }
    }

}

