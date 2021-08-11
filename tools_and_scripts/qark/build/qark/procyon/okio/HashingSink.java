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
import javax.annotation.Nullable;
import javax.crypto.Mac;

public final class HashingSink extends ForwardingSink
{
    @Nullable
    private final Mac mac;
    @Nullable
    private final MessageDigest messageDigest;
    
    private HashingSink(final Sink sink, final String s) {
        super(sink);
        try {
            this.messageDigest = MessageDigest.getInstance(s);
            this.mac = null;
        }
        catch (NoSuchAlgorithmException ex) {
            throw new AssertionError();
        }
    }
    
    private HashingSink(final Sink sink, final ByteString byteString, final String s) {
        super(sink);
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
    
    public static HashingSink hmacSha1(final Sink sink, final ByteString byteString) {
        return new HashingSink(sink, byteString, "HmacSHA1");
    }
    
    public static HashingSink hmacSha256(final Sink sink, final ByteString byteString) {
        return new HashingSink(sink, byteString, "HmacSHA256");
    }
    
    public static HashingSink hmacSha512(final Sink sink, final ByteString byteString) {
        return new HashingSink(sink, byteString, "HmacSHA512");
    }
    
    public static HashingSink md5(final Sink sink) {
        return new HashingSink(sink, "MD5");
    }
    
    public static HashingSink sha1(final Sink sink) {
        return new HashingSink(sink, "SHA-1");
    }
    
    public static HashingSink sha256(final Sink sink) {
        return new HashingSink(sink, "SHA-256");
    }
    
    public static HashingSink sha512(final Sink sink) {
        return new HashingSink(sink, "SHA-512");
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
    public void write(final Buffer buffer, final long n) throws IOException {
        Util.checkOffsetAndCount(buffer.size, 0L, n);
        long n2 = 0L;
        int n3;
        for (Segment segment = buffer.head; n2 < n; n2 += n3, segment = segment.next) {
            n3 = (int)Math.min(n - n2, segment.limit - segment.pos);
            if (this.messageDigest != null) {
                this.messageDigest.update(segment.data, segment.pos, n3);
            }
            else {
                this.mac.update(segment.data, segment.pos, n3);
            }
        }
        super.write(buffer, n);
    }
}
