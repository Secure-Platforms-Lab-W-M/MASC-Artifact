/*
 * Decompiled with CFR 0_124.
 */
package com.google.protobuf;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.LiteralByteString;
import com.google.protobuf.RopeByteString;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class ByteString
implements Iterable<Byte> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final int CONCATENATE_BY_COPY_SIZE = 128;
    public static final ByteString EMPTY = new LiteralByteString(new byte[0]);
    static final int MAX_READ_FROM_CHUNK_SIZE = 8192;
    static final int MIN_READ_FROM_CHUNK_SIZE = 256;

    ByteString() {
    }

    private static ByteString balancedConcat(Iterator<ByteString> iterator, int n) {
        if (n == 1) {
            return iterator.next();
        }
        int n2 = n >>> 1;
        return ByteString.balancedConcat(iterator, n2).concat(ByteString.balancedConcat(iterator, n - n2));
    }

    public static ByteString copyFrom(Iterable<ByteString> arrayList) {
        if (!(arrayList instanceof Collection)) {
            ArrayList<ByteString> arrayList2 = new ArrayList<ByteString>();
            arrayList = arrayList.iterator();
            while (arrayList.hasNext()) {
                arrayList2.add((ByteString)arrayList.next());
            }
            arrayList = arrayList2;
        } else {
            arrayList = arrayList;
        }
        if (arrayList.isEmpty()) {
            return EMPTY;
        }
        return ByteString.balancedConcat(arrayList.iterator(), arrayList.size());
    }

    public static ByteString copyFrom(String string2, String string3) throws UnsupportedEncodingException {
        return new LiteralByteString(string2.getBytes(string3));
    }

    public static ByteString copyFrom(ByteBuffer byteBuffer) {
        return ByteString.copyFrom(byteBuffer, byteBuffer.remaining());
    }

    public static ByteString copyFrom(ByteBuffer byteBuffer, int n) {
        byte[] arrby = new byte[n];
        byteBuffer.get(arrby);
        return new LiteralByteString(arrby);
    }

    public static ByteString copyFrom(byte[] arrby) {
        return ByteString.copyFrom(arrby, 0, arrby.length);
    }

    public static ByteString copyFrom(byte[] arrby, int n, int n2) {
        byte[] arrby2 = new byte[n2];
        System.arraycopy(arrby, n, arrby2, 0, n2);
        return new LiteralByteString(arrby2);
    }

    public static ByteString copyFromUtf8(String object) {
        try {
            object = new LiteralByteString(object.getBytes("UTF-8"));
            return object;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuntimeException("UTF-8 not supported?", unsupportedEncodingException);
        }
    }

    static CodedBuilder newCodedBuilder(int n) {
        return new CodedBuilder(n);
    }

    public static Output newOutput() {
        return new Output(128);
    }

    public static Output newOutput(int n) {
        return new Output(n);
    }

    private static ByteString readChunk(InputStream inputStream, int n) throws IOException {
        int n2;
        int n3;
        byte[] arrby = new byte[n];
        for (n2 = 0; n2 < n && (n3 = inputStream.read(arrby, n2, n - n2)) != -1; n2 += n3) {
        }
        if (n2 == 0) {
            return null;
        }
        return ByteString.copyFrom(arrby, 0, n2);
    }

    public static ByteString readFrom(InputStream inputStream) throws IOException {
        return ByteString.readFrom(inputStream, 256, 8192);
    }

    public static ByteString readFrom(InputStream inputStream, int n) throws IOException {
        return ByteString.readFrom(inputStream, n, n);
    }

    public static ByteString readFrom(InputStream inputStream, int n, int n2) throws IOException {
        ArrayList<ByteString> arrayList = new ArrayList<ByteString>();
        ByteString byteString;
        while ((byteString = ByteString.readChunk(inputStream, n)) != null) {
            arrayList.add(byteString);
            n = Math.min(n * 2, n2);
        }
        return ByteString.copyFrom(arrayList);
    }

    public abstract ByteBuffer asReadOnlyByteBuffer();

    public abstract List<ByteBuffer> asReadOnlyByteBufferList();

    public abstract byte byteAt(int var1);

    public ByteString concat(ByteString object) {
        int n;
        int n2 = this.size();
        if ((long)n2 + (long)(n = object.size()) < Integer.MAX_VALUE) {
            return RopeByteString.concatenate(this, (ByteString)object);
        }
        object = new StringBuilder();
        object.append("ByteString would be too long: ");
        object.append(n2);
        object.append("+");
        object.append(n);
        throw new IllegalArgumentException(object.toString());
    }

    public abstract void copyTo(ByteBuffer var1);

    public void copyTo(byte[] arrby, int n) {
        this.copyTo(arrby, 0, n, this.size());
    }

    public void copyTo(byte[] object, int n, int n2, int n3) {
        if (n >= 0) {
            if (n2 >= 0) {
                if (n3 >= 0) {
                    if (n + n3 <= this.size()) {
                        if (n2 + n3 <= object.length) {
                            if (n3 > 0) {
                                this.copyToInternal((byte[])object, n, n2, n3);
                            }
                            return;
                        }
                        object = new StringBuilder();
                        object.append("Target end offset < 0: ");
                        object.append(n2 + n3);
                        throw new IndexOutOfBoundsException(object.toString());
                    }
                    object = new StringBuilder();
                    object.append("Source end offset < 0: ");
                    object.append(n + n3);
                    throw new IndexOutOfBoundsException(object.toString());
                }
                object = new StringBuilder();
                object.append("Length < 0: ");
                object.append(n3);
                throw new IndexOutOfBoundsException(object.toString());
            }
            object = new StringBuilder();
            object.append("Target offset < 0: ");
            object.append(n2);
            throw new IndexOutOfBoundsException(object.toString());
        }
        object = new StringBuilder();
        object.append("Source offset < 0: ");
        object.append(n);
        throw new IndexOutOfBoundsException(object.toString());
    }

    protected abstract void copyToInternal(byte[] var1, int var2, int var3, int var4);

    public abstract boolean equals(Object var1);

    protected abstract int getTreeDepth();

    public abstract int hashCode();

    protected abstract boolean isBalanced();

    public boolean isEmpty() {
        if (this.size() == 0) {
            return true;
        }
        return false;
    }

    public abstract boolean isValidUtf8();

    public abstract ByteIterator iterator();

    public abstract CodedInputStream newCodedInput();

    public abstract InputStream newInput();

    protected abstract int partialHash(int var1, int var2, int var3);

    protected abstract int partialIsValidUtf8(int var1, int var2, int var3);

    protected abstract int peekCachedHashCode();

    public abstract int size();

    public boolean startsWith(ByteString byteString) {
        boolean bl;
        int n = this.size();
        int n2 = byteString.size();
        boolean bl2 = bl = false;
        if (n >= n2) {
            bl2 = bl;
            if (this.substring(0, byteString.size()).equals(byteString)) {
                bl2 = true;
            }
        }
        return bl2;
    }

    public ByteString substring(int n) {
        return this.substring(n, this.size());
    }

    public abstract ByteString substring(int var1, int var2);

    public byte[] toByteArray() {
        int n = this.size();
        byte[] arrby = new byte[n];
        this.copyToInternal(arrby, 0, 0, n);
        return arrby;
    }

    public String toString() {
        return String.format("<ByteString@%s size=%d>", Integer.toHexString(System.identityHashCode(this)), this.size());
    }

    public abstract String toString(String var1) throws UnsupportedEncodingException;

    public String toStringUtf8() {
        try {
            String string2 = this.toString("UTF-8");
            return string2;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuntimeException("UTF-8 not supported?", unsupportedEncodingException);
        }
    }

    public abstract void writeTo(OutputStream var1) throws IOException;

    public static interface ByteIterator
    extends Iterator<Byte> {
        public byte nextByte();
    }

    static final class CodedBuilder {
        private final byte[] buffer;
        private final CodedOutputStream output;

        private CodedBuilder(int n) {
            byte[] arrby = new byte[n];
            this.buffer = arrby;
            this.output = CodedOutputStream.newInstance(arrby);
        }

        public ByteString build() {
            this.output.checkNoSpaceLeft();
            return new LiteralByteString(this.buffer);
        }

        public CodedOutputStream getCodedOutput() {
            return this.output;
        }
    }

    public static final class Output
    extends OutputStream {
        private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
        private byte[] buffer;
        private int bufferPos;
        private final ArrayList<ByteString> flushedBuffers;
        private int flushedBuffersTotalBytes;
        private final int initialCapacity;

        Output(int n) {
            if (n >= 0) {
                this.initialCapacity = n;
                this.flushedBuffers = new ArrayList();
                this.buffer = new byte[n];
                return;
            }
            throw new IllegalArgumentException("Buffer size < 0");
        }

        private byte[] copyArray(byte[] arrby, int n) {
            byte[] arrby2 = new byte[n];
            System.arraycopy(arrby, 0, arrby2, 0, Math.min(arrby.length, n));
            return arrby2;
        }

        private void flushFullBuffer(int n) {
            int n2;
            this.flushedBuffers.add(new LiteralByteString(this.buffer));
            this.flushedBuffersTotalBytes = n2 = this.flushedBuffersTotalBytes + this.buffer.length;
            this.buffer = new byte[Math.max(this.initialCapacity, Math.max(n, n2 >>> 1))];
            this.bufferPos = 0;
        }

        private void flushLastBuffer() {
            int n = this.bufferPos;
            byte[] arrby = this.buffer;
            if (n < arrby.length) {
                if (n > 0) {
                    arrby = this.copyArray(arrby, n);
                    this.flushedBuffers.add(new LiteralByteString(arrby));
                }
            } else {
                this.flushedBuffers.add(new LiteralByteString(arrby));
                this.buffer = EMPTY_BYTE_ARRAY;
            }
            this.flushedBuffersTotalBytes += this.bufferPos;
            this.bufferPos = 0;
        }

        public void reset() {
            synchronized (this) {
                this.flushedBuffers.clear();
                this.flushedBuffersTotalBytes = 0;
                this.bufferPos = 0;
                return;
            }
        }

        public int size() {
            synchronized (this) {
                int n = this.flushedBuffersTotalBytes;
                int n2 = this.bufferPos;
                return n + n2;
            }
        }

        public ByteString toByteString() {
            synchronized (this) {
                this.flushLastBuffer();
                ByteString byteString = ByteString.copyFrom(this.flushedBuffers);
                return byteString;
            }
        }

        public String toString() {
            return String.format("<ByteString.Output@%s size=%d>", Integer.toHexString(System.identityHashCode(this)), this.size());
        }

        @Override
        public void write(int n) {
            synchronized (this) {
                if (this.bufferPos == this.buffer.length) {
                    this.flushFullBuffer(1);
                }
                byte[] arrby = this.buffer;
                int n2 = this.bufferPos;
                this.bufferPos = n2 + 1;
                arrby[n2] = (byte)n;
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void write(byte[] arrby, int n, int n2) {
            synchronized (this) {
                void var2_2;
                void var3_3;
                if (var3_3 <= this.buffer.length - this.bufferPos) {
                    System.arraycopy(arrby, (int)var2_2, this.buffer, this.bufferPos, (int)var3_3);
                    this.bufferPos += var3_3;
                } else {
                    int n3 = this.buffer.length - this.bufferPos;
                    System.arraycopy(arrby, (int)var2_2, this.buffer, this.bufferPos, n3);
                    this.flushFullBuffer((int)(var3_3 -= n3));
                    System.arraycopy(arrby, (int)(var2_2 + n3), this.buffer, 0, (int)var3_3);
                    this.bufferPos = var3_3;
                }
                return;
            }
        }

        /*
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        public void writeTo(OutputStream outputStream) throws IOException {
            int n;
            void throwable;
            // MONITORENTER : this
            ByteString[] arrbyteString = this.flushedBuffers.toArray(new ByteString[this.flushedBuffers.size()]);
            byte[] arrby = this.buffer;
            try {
                n = this.bufferPos;
                // MONITOREXIT : this
            }
            catch (Throwable throwable2) {
                throw throwable;
            }
            int n2 = arrbyteString.length;
            int n3 = 0;
            do {
                if (n3 >= n2) {
                    outputStream.write(this.copyArray(arrby, n));
                    return;
                }
                arrbyteString[n3].writeTo(outputStream);
                ++n3;
            } while (true);
            catch (Throwable throwable3) {
                // empty catch block
            }
            throw throwable;
        }
    }

}

