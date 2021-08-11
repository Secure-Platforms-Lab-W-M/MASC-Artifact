/*
 * Decompiled with CFR 0_124.
 */
package com.google.protobuf;

import com.google.protobuf.ByteString;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import com.google.protobuf.WireFormat;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public final class CodedInputStream {
    private static final int BUFFER_SIZE = 4096;
    private static final int DEFAULT_RECURSION_LIMIT = 64;
    private static final int DEFAULT_SIZE_LIMIT = 67108864;
    private final byte[] buffer;
    private int bufferPos;
    private int bufferSize;
    private int bufferSizeAfterLimit;
    private int currentLimit = Integer.MAX_VALUE;
    private final InputStream input;
    private int lastTag;
    private int recursionDepth;
    private int recursionLimit = 64;
    private int sizeLimit = 67108864;
    private int totalBytesRetired;

    private CodedInputStream(InputStream inputStream) {
        this.buffer = new byte[4096];
        this.bufferSize = 0;
        this.bufferPos = 0;
        this.totalBytesRetired = 0;
        this.input = inputStream;
    }

    private CodedInputStream(byte[] arrby, int n, int n2) {
        this.buffer = arrby;
        this.bufferSize = n + n2;
        this.bufferPos = n;
        this.totalBytesRetired = - n;
        this.input = null;
    }

    public static int decodeZigZag32(int n) {
        return n >>> 1 ^ - (n & 1);
    }

    public static long decodeZigZag64(long l) {
        return l >>> 1 ^ - (1L & l);
    }

    public static CodedInputStream newInstance(InputStream inputStream) {
        return new CodedInputStream(inputStream);
    }

    public static CodedInputStream newInstance(byte[] arrby) {
        return CodedInputStream.newInstance(arrby, 0, arrby.length);
    }

    public static CodedInputStream newInstance(byte[] object, int n, int n2) {
        object = new CodedInputStream((byte[])object, n, n2);
        try {
            object.pushLimit(n2);
            return object;
        }
        catch (InvalidProtocolBufferException invalidProtocolBufferException) {
            throw new IllegalArgumentException(invalidProtocolBufferException);
        }
    }

    public static int readRawVarint32(int n, InputStream inputStream) throws IOException {
        int n2;
        int n3;
        block5 : {
            if ((n & 128) == 0) {
                return n;
            }
            n3 = n & 127;
            n = 7;
            do {
                if (n >= 32) break block5;
                n2 = inputStream.read();
                if (n2 == -1) break;
                n3 |= (n2 & 127) << n;
                if ((n2 & 128) == 0) {
                    return n3;
                }
                n += 7;
            } while (true);
            throw InvalidProtocolBufferException.truncatedMessage();
        }
        for (n2 = n; n2 < 64; n2 += 7) {
            n = inputStream.read();
            if (n != -1) {
                if ((n & 128) != 0) continue;
                return n3;
            }
            throw InvalidProtocolBufferException.truncatedMessage();
        }
        throw InvalidProtocolBufferException.malformedVarint();
    }

    static int readRawVarint32(InputStream inputStream) throws IOException {
        int n = inputStream.read();
        if (n != -1) {
            return CodedInputStream.readRawVarint32(n, inputStream);
        }
        throw InvalidProtocolBufferException.truncatedMessage();
    }

    private void recomputeBufferSizeAfterLimit() {
        int n;
        this.bufferSize = n = this.bufferSize + this.bufferSizeAfterLimit;
        int n2 = this.totalBytesRetired + n;
        int n3 = this.currentLimit;
        if (n2 > n3) {
            this.bufferSizeAfterLimit = n2 -= n3;
            this.bufferSize = n - n2;
            return;
        }
        this.bufferSizeAfterLimit = 0;
    }

    private boolean refillBuffer(boolean bl) throws IOException {
        int n = this.bufferPos;
        int n2 = this.bufferSize;
        if (n >= n2) {
            n = this.totalBytesRetired;
            if (n + n2 == this.currentLimit) {
                if (!bl) {
                    return false;
                }
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            this.totalBytesRetired = n + n2;
            this.bufferPos = 0;
            Object object = this.input;
            n2 = object == null ? -1 : object.read(this.buffer);
            this.bufferSize = n2;
            if (n2 != 0 && n2 >= -1) {
                if (n2 == -1) {
                    this.bufferSize = 0;
                    if (!bl) {
                        return false;
                    }
                    throw InvalidProtocolBufferException.truncatedMessage();
                }
                this.recomputeBufferSizeAfterLimit();
                n2 = this.totalBytesRetired + this.bufferSize + this.bufferSizeAfterLimit;
                if (n2 <= this.sizeLimit && n2 >= 0) {
                    return true;
                }
                throw InvalidProtocolBufferException.sizeLimitExceeded();
            }
            object = new StringBuilder();
            object.append("InputStream#read(byte[]) returned invalid result: ");
            object.append(this.bufferSize);
            object.append("\nThe InputStream implementation is buggy.");
            throw new IllegalStateException(object.toString());
        }
        throw new IllegalStateException("refillBuffer() called when buffer wasn't empty.");
    }

    public void checkLastTagWas(int n) throws InvalidProtocolBufferException {
        if (this.lastTag == n) {
            return;
        }
        throw InvalidProtocolBufferException.invalidEndTag();
    }

    public int getBytesUntilLimit() {
        int n = this.currentLimit;
        if (n == Integer.MAX_VALUE) {
            return -1;
        }
        return n - (this.totalBytesRetired + this.bufferPos);
    }

    public int getTotalBytesRead() {
        return this.totalBytesRetired + this.bufferPos;
    }

    public boolean isAtEnd() throws IOException {
        boolean bl;
        int n = this.bufferPos;
        int n2 = this.bufferSize;
        boolean bl2 = bl = false;
        if (n == n2) {
            bl2 = bl;
            if (!this.refillBuffer(false)) {
                bl2 = true;
            }
        }
        return bl2;
    }

    public void popLimit(int n) {
        this.currentLimit = n;
        this.recomputeBufferSizeAfterLimit();
    }

    public int pushLimit(int n) throws InvalidProtocolBufferException {
        if (n >= 0) {
            int n2 = this.currentLimit;
            if ((n += this.totalBytesRetired + this.bufferPos) <= n2) {
                this.currentLimit = n;
                this.recomputeBufferSizeAfterLimit();
                return n2;
            }
            throw InvalidProtocolBufferException.truncatedMessage();
        }
        throw InvalidProtocolBufferException.negativeSize();
    }

    public boolean readBool() throws IOException {
        if (this.readRawVarint32() != 0) {
            return true;
        }
        return false;
    }

    public ByteString readBytes() throws IOException {
        int n = this.readRawVarint32();
        if (n == 0) {
            return ByteString.EMPTY;
        }
        int n2 = this.bufferSize;
        int n3 = this.bufferPos;
        if (n <= n2 - n3 && n > 0) {
            ByteString byteString = ByteString.copyFrom(this.buffer, n3, n);
            this.bufferPos += n;
            return byteString;
        }
        return ByteString.copyFrom(this.readRawBytes(n));
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(this.readRawLittleEndian64());
    }

    public int readEnum() throws IOException {
        return this.readRawVarint32();
    }

    public int readFixed32() throws IOException {
        return this.readRawLittleEndian32();
    }

    public long readFixed64() throws IOException {
        return this.readRawLittleEndian64();
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(this.readRawLittleEndian32());
    }

    public <T extends MessageLite> T readGroup(int n, Parser<T> object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        int n2 = this.recursionDepth;
        if (n2 < this.recursionLimit) {
            this.recursionDepth = n2 + 1;
            object = (MessageLite)object.parsePartialFrom(this, extensionRegistryLite);
            this.checkLastTagWas(WireFormat.makeTag(n, 4));
            --this.recursionDepth;
            return (T)object;
        }
        throw InvalidProtocolBufferException.recursionLimitExceeded();
    }

    public void readGroup(int n, MessageLite.Builder builder, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        int n2 = this.recursionDepth;
        if (n2 < this.recursionLimit) {
            this.recursionDepth = n2 + 1;
            builder.mergeFrom(this, extensionRegistryLite);
            this.checkLastTagWas(WireFormat.makeTag(n, 4));
            --this.recursionDepth;
            return;
        }
        throw InvalidProtocolBufferException.recursionLimitExceeded();
    }

    public int readInt32() throws IOException {
        return this.readRawVarint32();
    }

    public long readInt64() throws IOException {
        return this.readRawVarint64();
    }

    public <T extends MessageLite> T readMessage(Parser<T> object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        int n = this.readRawVarint32();
        if (this.recursionDepth < this.recursionLimit) {
            n = this.pushLimit(n);
            ++this.recursionDepth;
            object = (MessageLite)object.parsePartialFrom(this, extensionRegistryLite);
            this.checkLastTagWas(0);
            --this.recursionDepth;
            this.popLimit(n);
            return (T)object;
        }
        throw InvalidProtocolBufferException.recursionLimitExceeded();
    }

    public void readMessage(MessageLite.Builder builder, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        int n = this.readRawVarint32();
        if (this.recursionDepth < this.recursionLimit) {
            n = this.pushLimit(n);
            ++this.recursionDepth;
            builder.mergeFrom(this, extensionRegistryLite);
            this.checkLastTagWas(0);
            --this.recursionDepth;
            this.popLimit(n);
            return;
        }
        throw InvalidProtocolBufferException.recursionLimitExceeded();
    }

    public byte readRawByte() throws IOException {
        if (this.bufferPos == this.bufferSize) {
            this.refillBuffer(true);
        }
        byte[] arrby = this.buffer;
        int n = this.bufferPos;
        this.bufferPos = n + 1;
        return arrby[n];
    }

    public byte[] readRawBytes(int n) throws IOException {
        if (n >= 0) {
            int n2 = this.totalBytesRetired;
            int n3 = this.bufferPos;
            int n4 = this.currentLimit;
            if (n2 + n3 + n <= n4) {
                byte[] arrby;
                byte[] arrby2;
                n4 = this.bufferSize;
                if (n <= n4 - n3) {
                    byte[] arrby3 = new byte[n];
                    System.arraycopy(this.buffer, n3, arrby3, 0, n);
                    this.bufferPos += n;
                    return arrby3;
                }
                if (n < 4096) {
                    byte[] arrby4 = new byte[n];
                    n2 = n4 - n3;
                    System.arraycopy(this.buffer, n3, arrby4, 0, n2);
                    this.bufferPos = this.bufferSize;
                    this.refillBuffer(true);
                    while (n - n2 > (n3 = this.bufferSize)) {
                        System.arraycopy(this.buffer, 0, arrby4, n2, n3);
                        n3 = this.bufferSize;
                        n2 += n3;
                        this.bufferPos = n3;
                        this.refillBuffer(true);
                    }
                    System.arraycopy(this.buffer, 0, arrby4, n2, n - n2);
                    this.bufferPos = n - n2;
                    return arrby4;
                }
                int n5 = this.bufferPos;
                int n6 = this.bufferSize;
                this.totalBytesRetired = n2 + n4;
                this.bufferPos = 0;
                this.bufferSize = 0;
                Object object = new ArrayList<byte[]>();
                for (n2 = n - (n6 - n5); n2 > 0; n2 -= arrby2.length) {
                    arrby2 = new byte[Math.min(n2, 4096)];
                    for (n3 = 0; n3 < arrby2.length; n3 += n4) {
                        arrby = this.input;
                        n4 = arrby == null ? -1 : arrby.read(arrby2, n3, arrby2.length - n3);
                        if (n4 != -1) {
                            this.totalBytesRetired += n4;
                            continue;
                        }
                        throw InvalidProtocolBufferException.truncatedMessage();
                    }
                    object.add((byte[])arrby2);
                }
                arrby2 = new byte[n];
                n = n6 - n5;
                System.arraycopy(this.buffer, n5, arrby2, 0, n);
                object = object.iterator();
                while (object.hasNext()) {
                    arrby = (byte[])object.next();
                    System.arraycopy(arrby, 0, arrby2, n, arrby.length);
                    n += arrby.length;
                }
                return arrby2;
            }
            this.skipRawBytes(n4 - n2 - n3);
            throw InvalidProtocolBufferException.truncatedMessage();
        }
        throw InvalidProtocolBufferException.negativeSize();
    }

    public int readRawLittleEndian32() throws IOException {
        return this.readRawByte() & 255 | (this.readRawByte() & 255) << 8 | (this.readRawByte() & 255) << 16 | (this.readRawByte() & 255) << 24;
    }

    public long readRawLittleEndian64() throws IOException {
        byte by = this.readRawByte();
        byte by2 = this.readRawByte();
        byte by3 = this.readRawByte();
        byte by4 = this.readRawByte();
        byte by5 = this.readRawByte();
        byte by6 = this.readRawByte();
        byte by7 = this.readRawByte();
        byte by8 = this.readRawByte();
        return (long)by & 255L | ((long)by2 & 255L) << 8 | ((long)by3 & 255L) << 16 | ((long)by4 & 255L) << 24 | ((long)by5 & 255L) << 32 | ((long)by6 & 255L) << 40 | ((long)by7 & 255L) << 48 | (255L & (long)by8) << 56;
    }

    public int readRawVarint32() throws IOException {
        int n = this.readRawByte();
        if (n >= 0) {
            return n;
        }
        n &= 127;
        int n2 = this.readRawByte();
        if (n2 >= 0) {
            return n | n2 << 7;
        }
        n |= (n2 & 127) << 7;
        n2 = this.readRawByte();
        if (n2 >= 0) {
            return n | n2 << 14;
        }
        n2 = n | (n2 & 127) << 14;
        byte by = this.readRawByte();
        if (by >= 0) {
            return n2 | by << 21;
        }
        n = this.readRawByte();
        n2 = n2 | (by & 127) << 21 | n << 28;
        if (n < 0) {
            for (n = 0; n < 5; ++n) {
                if (this.readRawByte() < 0) continue;
                return n2;
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }
        return n2;
    }

    public long readRawVarint64() throws IOException {
        long l = 0L;
        for (int i = 0; i < 64; i += 7) {
            byte by = this.readRawByte();
            l |= (long)(by & 127) << i;
            if ((by & 128) != 0) continue;
            return l;
        }
        throw InvalidProtocolBufferException.malformedVarint();
    }

    public int readSFixed32() throws IOException {
        return this.readRawLittleEndian32();
    }

    public long readSFixed64() throws IOException {
        return this.readRawLittleEndian64();
    }

    public int readSInt32() throws IOException {
        return CodedInputStream.decodeZigZag32(this.readRawVarint32());
    }

    public long readSInt64() throws IOException {
        return CodedInputStream.decodeZigZag64(this.readRawVarint64());
    }

    public String readString() throws IOException {
        int n = this.readRawVarint32();
        if (n <= this.bufferSize - this.bufferPos && n > 0) {
            String string2 = new String(this.buffer, this.bufferPos, n, "UTF-8");
            this.bufferPos += n;
            return string2;
        }
        return new String(this.readRawBytes(n), "UTF-8");
    }

    public int readTag() throws IOException {
        int n;
        if (this.isAtEnd()) {
            this.lastTag = 0;
            return 0;
        }
        this.lastTag = n = this.readRawVarint32();
        if (WireFormat.getTagFieldNumber(n) != 0) {
            return this.lastTag;
        }
        throw InvalidProtocolBufferException.invalidTag();
    }

    public int readUInt32() throws IOException {
        return this.readRawVarint32();
    }

    public long readUInt64() throws IOException {
        return this.readRawVarint64();
    }

    @Deprecated
    public void readUnknownGroup(int n, MessageLite.Builder builder) throws IOException {
        this.readGroup(n, builder, null);
    }

    public void resetSizeCounter() {
        this.totalBytesRetired = - this.bufferPos;
    }

    public int setRecursionLimit(int n) {
        if (n >= 0) {
            int n2 = this.recursionLimit;
            this.recursionLimit = n;
            return n2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Recursion limit cannot be negative: ");
        stringBuilder.append(n);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public int setSizeLimit(int n) {
        if (n >= 0) {
            int n2 = this.sizeLimit;
            this.sizeLimit = n;
            return n2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Size limit cannot be negative: ");
        stringBuilder.append(n);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public boolean skipField(int n) throws IOException {
        int n2 = WireFormat.getTagWireType(n);
        if (n2 != 0) {
            if (n2 != 1) {
                if (n2 != 2) {
                    if (n2 != 3) {
                        if (n2 != 4) {
                            if (n2 == 5) {
                                this.readRawLittleEndian32();
                                return true;
                            }
                            throw InvalidProtocolBufferException.invalidWireType();
                        }
                        return false;
                    }
                    this.skipMessage();
                    this.checkLastTagWas(WireFormat.makeTag(WireFormat.getTagFieldNumber(n), 4));
                    return true;
                }
                this.skipRawBytes(this.readRawVarint32());
                return true;
            }
            this.readRawLittleEndian64();
            return true;
        }
        this.readInt32();
        return true;
    }

    public void skipMessage() throws IOException {
        int n;
        while ((n = this.readTag()) != 0) {
            if (this.skipField(n)) continue;
            return;
        }
    }

    public void skipRawBytes(int n) throws IOException {
        if (n >= 0) {
            int n2 = this.totalBytesRetired;
            int n3 = this.bufferPos;
            int n4 = this.currentLimit;
            if (n2 + n3 + n <= n4) {
                n2 = this.bufferSize;
                if (n <= n2 - n3) {
                    this.bufferPos = n3 + n;
                    return;
                }
                n3 = n2 - n3;
                this.bufferPos = n2;
                this.refillBuffer(true);
                while (n - n3 > (n2 = this.bufferSize)) {
                    n3 += n2;
                    this.bufferPos = n2;
                    this.refillBuffer(true);
                }
                this.bufferPos = n - n3;
                return;
            }
            this.skipRawBytes(n4 - n2 - n3);
            throw InvalidProtocolBufferException.truncatedMessage();
        }
        throw InvalidProtocolBufferException.negativeSize();
    }
}

