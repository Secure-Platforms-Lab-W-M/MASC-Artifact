// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import javax.crypto.Mac;

public final class HashingSource extends ForwardingSource
{
    private final Mac mac;
    private final MessageDigest messageDigest;
    
    private HashingSource(final Source source, final String s) {
        super(source);
        try {
            this.messageDigest = MessageDigest.getInstance(s);
            this.mac = null;
        }
        catch (NoSuchAlgorithmException ex) {
            throw new AssertionError();
        }
    }
    
    private HashingSource(final Source source, final ByteString byteString, final String s) {
        super(source);
        try {
            (this.mac = Mac.getInstance(s)).init(new SecretKeySpec(byteString.toByteArray(), s));
            this.messageDigest = null;
        }
        catch (NoSuchAlgorithmException ex2) {
            throw new AssertionError();
        }
        catch (InvalidKeyException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    public static HashingSource hmacSha1(final Source source, final ByteString byteString) {
        return new HashingSource(source, byteString, "HmacSHA1");
    }
    
    public static HashingSource hmacSha256(final Source source, final ByteString byteString) {
        return new HashingSource(source, byteString, "HmacSHA256");
    }
    
    public static HashingSource md5(final Source source) {
        return new HashingSource(source, "MD5");
    }
    
    public static HashingSource sha1(final Source source) {
        return new HashingSource(source, "SHA-1");
    }
    
    public static HashingSource sha256(final Source source) {
        return new HashingSource(source, "SHA-256");
    }
    
    public ByteString hash() {
        byte[] array;
        if (this.messageDigest != null) {
            array = this.messageDigest.digest();
        }
        else {
            array = this.mac.doFinal();
        }
        return ByteString.of(array);
    }
    
    @Override
    public long read(final Buffer buffer, long size) throws IOException {
        final long read = super.read(buffer, size);
        if (read != -1L) {
            final long n = buffer.size - read;
            size = buffer.size;
            Segment segment = buffer.head;
            long n2;
            Segment next;
            long n3;
            while (true) {
                n2 = size;
                next = segment;
                n3 = n;
                if (size <= n) {
                    break;
                }
                segment = segment.prev;
                size -= segment.limit - segment.pos;
            }
            while (n2 < buffer.size) {
                final int n4 = (int)(next.pos + n3 - n2);
                if (this.messageDigest != null) {
                    this.messageDigest.update(next.data, n4, next.limit - n4);
                }
                else {
                    this.mac.update(next.data, n4, next.limit - n4);
                }
                n2 = (n3 = n2 + (next.limit - next.pos));
                next = next.next;
            }
        }
        return read;
    }
}
