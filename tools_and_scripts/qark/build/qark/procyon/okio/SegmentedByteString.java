// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.util.Arrays;

final class SegmentedByteString extends ByteString
{
    final transient int[] directory;
    final transient byte[][] segments;
    
    SegmentedByteString(final Buffer buffer, final int n) {
        super(null);
        Util.checkOffsetAndCount(buffer.size, 0L, n);
        int i = 0;
        int n2 = 0;
        for (Segment segment = buffer.head; i < n; i += segment.limit - segment.pos, ++n2, segment = segment.next) {
            if (segment.limit == segment.pos) {
                throw new AssertionError((Object)"s.limit == s.pos");
            }
        }
        this.segments = new byte[n2][];
        this.directory = new int[n2 * 2];
        int j = 0;
        int n3 = 0;
        Segment segment2 = buffer.head;
        while (j < n) {
            this.segments[n3] = segment2.data;
            if ((j += segment2.limit - segment2.pos) > n) {
                j = n;
            }
            this.directory[n3] = j;
            this.directory[this.segments.length + n3] = segment2.pos;
            segment2.shared = true;
            ++n3;
            segment2 = segment2.next;
        }
    }
    
    private int segment(int binarySearch) {
        binarySearch = Arrays.binarySearch(this.directory, 0, this.segments.length, binarySearch + 1);
        if (binarySearch >= 0) {
            return binarySearch;
        }
        return ~binarySearch;
    }
    
    private ByteString toByteString() {
        return new ByteString(this.toByteArray());
    }
    
    private Object writeReplace() {
        return this.toByteString();
    }
    
    @Override
    public ByteBuffer asByteBuffer() {
        return ByteBuffer.wrap(this.toByteArray()).asReadOnlyBuffer();
    }
    
    @Override
    public String base64() {
        return this.toByteString().base64();
    }
    
    @Override
    public String base64Url() {
        return this.toByteString().base64Url();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof ByteString && ((ByteString)o).size() == this.size() && this.rangeEquals(0, (ByteString)o, 0, this.size()));
    }
    
    @Override
    public byte getByte(final int n) {
        Util.checkOffsetAndCount(this.directory[this.segments.length - 1], n, 1L);
        final int segment = this.segment(n);
        int n2;
        if (segment == 0) {
            n2 = 0;
        }
        else {
            n2 = this.directory[segment - 1];
        }
        return this.segments[segment][n - n2 + this.directory[this.segments.length + segment]];
    }
    
    @Override
    public int hashCode() {
        final int hashCode = this.hashCode;
        if (hashCode != 0) {
            return hashCode;
        }
        int hashCode2 = 1;
        int n = 0;
        for (int i = 0, length = this.segments.length; i < length; ++i) {
            final byte[] array = this.segments[i];
            final int n2 = this.directory[length + i];
            final int n3 = this.directory[i];
            for (int j = n2; j < n2 + (n3 - n); ++j) {
                hashCode2 = hashCode2 * 31 + array[j];
            }
            n = n3;
        }
        return this.hashCode = hashCode2;
    }
    
    @Override
    public String hex() {
        return this.toByteString().hex();
    }
    
    @Override
    public ByteString hmacSha1(final ByteString byteString) {
        return this.toByteString().hmacSha1(byteString);
    }
    
    @Override
    public ByteString hmacSha256(final ByteString byteString) {
        return this.toByteString().hmacSha256(byteString);
    }
    
    @Override
    public int indexOf(final byte[] array, final int n) {
        return this.toByteString().indexOf(array, n);
    }
    
    @Override
    byte[] internalArray() {
        return this.toByteArray();
    }
    
    @Override
    public int lastIndexOf(final byte[] array, final int n) {
        return this.toByteString().lastIndexOf(array, n);
    }
    
    @Override
    public ByteString md5() {
        return this.toByteString().md5();
    }
    
    @Override
    public boolean rangeEquals(int n, final ByteString byteString, int n2, int i) {
        if (n >= 0 && n <= this.size() - i) {
            final int segment = this.segment(n);
            int n3 = n;
            int n4;
            int min;
            for (n = segment; i > 0; i -= min, ++n) {
                if (n == 0) {
                    n4 = 0;
                }
                else {
                    n4 = this.directory[n - 1];
                }
                min = Math.min(i, n4 + (this.directory[n] - n4) - n3);
                if (!byteString.rangeEquals(n2, this.segments[n], n3 - n4 + this.directory[this.segments.length + n], min)) {
                    return false;
                }
                n3 += min;
                n2 += min;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean rangeEquals(int n, final byte[] array, int n2, int i) {
        if (n >= 0 && n <= this.size() - i && n2 >= 0 && n2 <= array.length - i) {
            final int segment = this.segment(n);
            int n3 = n;
            int n4;
            int min;
            for (n = segment; i > 0; i -= min, ++n) {
                if (n == 0) {
                    n4 = 0;
                }
                else {
                    n4 = this.directory[n - 1];
                }
                min = Math.min(i, n4 + (this.directory[n] - n4) - n3);
                if (!Util.arrayRangeEquals(this.segments[n], n3 - n4 + this.directory[this.segments.length + n], array, n2, min)) {
                    return false;
                }
                n3 += min;
                n2 += min;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public ByteString sha1() {
        return this.toByteString().sha1();
    }
    
    @Override
    public ByteString sha256() {
        return this.toByteString().sha256();
    }
    
    @Override
    public int size() {
        return this.directory[this.segments.length - 1];
    }
    
    @Override
    public String string(final Charset charset) {
        return this.toByteString().string(charset);
    }
    
    @Override
    public ByteString substring(final int n) {
        return this.toByteString().substring(n);
    }
    
    @Override
    public ByteString substring(final int n, final int n2) {
        return this.toByteString().substring(n, n2);
    }
    
    @Override
    public ByteString toAsciiLowercase() {
        return this.toByteString().toAsciiLowercase();
    }
    
    @Override
    public ByteString toAsciiUppercase() {
        return this.toByteString().toAsciiUppercase();
    }
    
    @Override
    public byte[] toByteArray() {
        final byte[] array = new byte[this.directory[this.segments.length - 1]];
        int n = 0;
        for (int i = 0, length = this.segments.length; i < length; ++i) {
            final int n2 = this.directory[length + i];
            final int n3 = this.directory[i];
            System.arraycopy(this.segments[i], n2, array, n, n3 - n);
            n = n3;
        }
        return array;
    }
    
    @Override
    public String toString() {
        return this.toByteString().toString();
    }
    
    @Override
    public String utf8() {
        return this.toByteString().utf8();
    }
    
    @Override
    public void write(final OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        int n = 0;
        for (int i = 0, length = this.segments.length; i < length; ++i) {
            final int n2 = this.directory[length + i];
            final int n3 = this.directory[i];
            outputStream.write(this.segments[i], n2, n3 - n);
            n = n3;
        }
    }
    
    @Override
    void write(final Buffer buffer) {
        int n = 0;
        for (int i = 0, length = this.segments.length; i < length; ++i) {
            final int n2 = this.directory[length + i];
            final int n3 = this.directory[i];
            final Segment head = new Segment(this.segments[i], n2, n2 + n3 - n, true, false);
            if (buffer.head == null) {
                head.prev = head;
                head.next = head;
                buffer.head = head;
            }
            else {
                buffer.head.prev.push(head);
            }
            n = n3;
        }
        buffer.size += n;
    }
}
