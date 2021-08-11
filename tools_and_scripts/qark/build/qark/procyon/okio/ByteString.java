// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import java.io.OutputStream;
import java.util.Arrays;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import javax.annotation.Nullable;
import java.io.Serializable;

public class ByteString implements Serializable, Comparable<ByteString>
{
    public static final ByteString EMPTY;
    static final char[] HEX_DIGITS;
    private static final long serialVersionUID = 1L;
    final byte[] data;
    transient int hashCode;
    transient String utf8;
    
    static {
        HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        EMPTY = of(new byte[0]);
    }
    
    ByteString(final byte[] data) {
        this.data = data;
    }
    
    static int codePointIndexToCharIndex(final String s, final int n) {
        int i = 0;
        int n2 = 0;
        while (i < s.length()) {
            if (n2 == n) {
                return i;
            }
            final int codePoint = s.codePointAt(i);
            if ((Character.isISOControl(codePoint) && codePoint != 10 && codePoint != 13) || codePoint == 65533) {
                return -1;
            }
            ++n2;
            i += Character.charCount(codePoint);
        }
        return s.length();
    }
    
    @Nullable
    public static ByteString decodeBase64(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("base64 == null");
        }
        final byte[] decode = Base64.decode(s);
        if (decode != null) {
            return new ByteString(decode);
        }
        return null;
    }
    
    public static ByteString decodeHex(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("hex == null");
        }
        if (s.length() % 2 != 0) {
            throw new IllegalArgumentException("Unexpected hex string: " + s);
        }
        final byte[] array = new byte[s.length() / 2];
        for (int i = 0; i < array.length; ++i) {
            array[i] = (byte)((decodeHexDigit(s.charAt(i * 2)) << 4) + decodeHexDigit(s.charAt(i * 2 + 1)));
        }
        return of(array);
    }
    
    private static int decodeHexDigit(final char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        throw new IllegalArgumentException("Unexpected hex digit: " + c);
    }
    
    private ByteString digest(final String s) {
        try {
            return of(MessageDigest.getInstance(s).digest(this.data));
        }
        catch (NoSuchAlgorithmException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    public static ByteString encodeString(final String s, final Charset charset) {
        if (s == null) {
            throw new IllegalArgumentException("s == null");
        }
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        return new ByteString(s.getBytes(charset));
    }
    
    public static ByteString encodeUtf8(final String utf8) {
        if (utf8 == null) {
            throw new IllegalArgumentException("s == null");
        }
        final ByteString byteString = new ByteString(utf8.getBytes(Util.UTF_8));
        byteString.utf8 = utf8;
        return byteString;
    }
    
    private ByteString hmac(final String s, final ByteString byteString) {
        try {
            final Mac instance = Mac.getInstance(s);
            instance.init(new SecretKeySpec(byteString.toByteArray(), s));
            return of(instance.doFinal(this.data));
        }
        catch (NoSuchAlgorithmException ex) {
            throw new AssertionError((Object)ex);
        }
        catch (InvalidKeyException ex2) {
            throw new IllegalArgumentException(ex2);
        }
    }
    
    public static ByteString of(final ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            throw new IllegalArgumentException("data == null");
        }
        final byte[] array = new byte[byteBuffer.remaining()];
        byteBuffer.get(array);
        return new ByteString(array);
    }
    
    public static ByteString of(final byte... array) {
        if (array == null) {
            throw new IllegalArgumentException("data == null");
        }
        return new ByteString(array.clone());
    }
    
    public static ByteString of(final byte[] array, final int n, final int n2) {
        if (array == null) {
            throw new IllegalArgumentException("data == null");
        }
        Util.checkOffsetAndCount(array.length, n, n2);
        final byte[] array2 = new byte[n2];
        System.arraycopy(array, n, array2, 0, n2);
        return new ByteString(array2);
    }
    
    public static ByteString read(final InputStream inputStream, final int n) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("in == null");
        }
        if (n < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + n);
        }
        final byte[] array = new byte[n];
        int read;
        for (int i = 0; i < n; i += read) {
            read = inputStream.read(array, i, n - i);
            if (read == -1) {
                throw new EOFException();
            }
        }
        return new ByteString(array);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException {
        final ByteString read = read(objectInputStream, objectInputStream.readInt());
        try {
            final Field declaredField = ByteString.class.getDeclaredField("data");
            declaredField.setAccessible(true);
            declaredField.set(this, read.data);
        }
        catch (NoSuchFieldException ex) {
            throw new AssertionError();
        }
        catch (IllegalAccessException ex2) {
            throw new AssertionError();
        }
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeInt(this.data.length);
        objectOutputStream.write(this.data);
    }
    
    public ByteBuffer asByteBuffer() {
        return ByteBuffer.wrap(this.data).asReadOnlyBuffer();
    }
    
    public String base64() {
        return Base64.encode(this.data);
    }
    
    public String base64Url() {
        return Base64.encodeUrl(this.data);
    }
    
    @Override
    public int compareTo(final ByteString byteString) {
        final int size = this.size();
        final int size2 = byteString.size();
        int i = 0;
        while (i < Math.min(size, size2)) {
            final int n = this.getByte(i) & 0xFF;
            final int n2 = byteString.getByte(i) & 0xFF;
            if (n == n2) {
                ++i;
            }
            else {
                if (n < n2) {
                    return -1;
                }
                return 1;
            }
        }
        if (size == size2) {
            return 0;
        }
        if (size >= size2) {
            return 1;
        }
        return -1;
    }
    
    public final boolean endsWith(final ByteString byteString) {
        return this.rangeEquals(this.size() - byteString.size(), byteString, 0, byteString.size());
    }
    
    public final boolean endsWith(final byte[] array) {
        return this.rangeEquals(this.size() - array.length, array, 0, array.length);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof ByteString && ((ByteString)o).size() == this.data.length && ((ByteString)o).rangeEquals(0, this.data, 0, this.data.length));
    }
    
    public byte getByte(final int n) {
        return this.data[n];
    }
    
    @Override
    public int hashCode() {
        final int hashCode = this.hashCode;
        if (hashCode != 0) {
            return hashCode;
        }
        return this.hashCode = Arrays.hashCode(this.data);
    }
    
    public String hex() {
        final char[] array = new char[this.data.length * 2];
        final byte[] data = this.data;
        final int length = data.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            final byte b = data[i];
            final int n2 = n + 1;
            array[n] = ByteString.HEX_DIGITS[b >> 4 & 0xF];
            n = n2 + 1;
            array[n2] = ByteString.HEX_DIGITS[b & 0xF];
            ++i;
        }
        return new String(array);
    }
    
    public ByteString hmacSha1(final ByteString byteString) {
        return this.hmac("HmacSHA1", byteString);
    }
    
    public ByteString hmacSha256(final ByteString byteString) {
        return this.hmac("HmacSHA256", byteString);
    }
    
    public ByteString hmacSha512(final ByteString byteString) {
        return this.hmac("HmacSHA512", byteString);
    }
    
    public final int indexOf(final ByteString byteString) {
        return this.indexOf(byteString.internalArray(), 0);
    }
    
    public final int indexOf(final ByteString byteString, final int n) {
        return this.indexOf(byteString.internalArray(), n);
    }
    
    public final int indexOf(final byte[] array) {
        return this.indexOf(array, 0);
    }
    
    public int indexOf(final byte[] array, int i) {
        for (i = Math.max(i, 0); i <= this.data.length - array.length; ++i) {
            if (Util.arrayRangeEquals(this.data, i, array, 0, array.length)) {
                return i;
            }
        }
        return -1;
    }
    
    byte[] internalArray() {
        return this.data;
    }
    
    public final int lastIndexOf(final ByteString byteString) {
        return this.lastIndexOf(byteString.internalArray(), this.size());
    }
    
    public final int lastIndexOf(final ByteString byteString, final int n) {
        return this.lastIndexOf(byteString.internalArray(), n);
    }
    
    public final int lastIndexOf(final byte[] array) {
        return this.lastIndexOf(array, this.size());
    }
    
    public int lastIndexOf(final byte[] array, int i) {
        for (i = Math.min(i, this.data.length - array.length); i >= 0; --i) {
            if (Util.arrayRangeEquals(this.data, i, array, 0, array.length)) {
                return i;
            }
        }
        return -1;
    }
    
    public ByteString md5() {
        return this.digest("MD5");
    }
    
    public boolean rangeEquals(final int n, final ByteString byteString, final int n2, final int n3) {
        return byteString.rangeEquals(n2, this.data, n, n3);
    }
    
    public boolean rangeEquals(final int n, final byte[] array, final int n2, final int n3) {
        return n >= 0 && n <= this.data.length - n3 && n2 >= 0 && n2 <= array.length - n3 && Util.arrayRangeEquals(this.data, n, array, n2, n3);
    }
    
    public ByteString sha1() {
        return this.digest("SHA-1");
    }
    
    public ByteString sha256() {
        return this.digest("SHA-256");
    }
    
    public ByteString sha512() {
        return this.digest("SHA-512");
    }
    
    public int size() {
        return this.data.length;
    }
    
    public final boolean startsWith(final ByteString byteString) {
        return this.rangeEquals(0, byteString, 0, byteString.size());
    }
    
    public final boolean startsWith(final byte[] array) {
        return this.rangeEquals(0, array, 0, array.length);
    }
    
    public String string(final Charset charset) {
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        return new String(this.data, charset);
    }
    
    public ByteString substring(final int n) {
        return this.substring(n, this.data.length);
    }
    
    public ByteString substring(final int n, final int n2) {
        if (n < 0) {
            throw new IllegalArgumentException("beginIndex < 0");
        }
        if (n2 > this.data.length) {
            throw new IllegalArgumentException("endIndex > length(" + this.data.length + ")");
        }
        final int n3 = n2 - n;
        if (n3 < 0) {
            throw new IllegalArgumentException("endIndex < beginIndex");
        }
        if (n == 0 && n2 == this.data.length) {
            return this;
        }
        final byte[] array = new byte[n3];
        System.arraycopy(this.data, n, array, 0, n3);
        return new ByteString(array);
    }
    
    public ByteString toAsciiLowercase() {
        int n = 0;
        ByteString byteString;
        while (true) {
            byteString = this;
            if (n >= this.data.length) {
                break;
            }
            final byte b = this.data[n];
            if (b >= 65 && b <= 90) {
                final byte[] array = this.data.clone();
                array[n] = (byte)(b + 32);
                for (int i = n + 1; i < array.length; ++i) {
                    final byte b2 = array[i];
                    if (b2 >= 65 && b2 <= 90) {
                        array[i] = (byte)(b2 + 32);
                    }
                }
                byteString = new ByteString(array);
                break;
            }
            ++n;
        }
        return byteString;
    }
    
    public ByteString toAsciiUppercase() {
        int n = 0;
        ByteString byteString;
        while (true) {
            byteString = this;
            if (n >= this.data.length) {
                break;
            }
            final byte b = this.data[n];
            if (b >= 97 && b <= 122) {
                final byte[] array = this.data.clone();
                array[n] = (byte)(b - 32);
                for (int i = n + 1; i < array.length; ++i) {
                    final byte b2 = array[i];
                    if (b2 >= 97 && b2 <= 122) {
                        array[i] = (byte)(b2 - 32);
                    }
                }
                byteString = new ByteString(array);
                break;
            }
            ++n;
        }
        return byteString;
    }
    
    public byte[] toByteArray() {
        return this.data.clone();
    }
    
    @Override
    public String toString() {
        if (this.data.length == 0) {
            return "[size=0]";
        }
        final String utf8 = this.utf8();
        final int codePointIndexToCharIndex = codePointIndexToCharIndex(utf8, 64);
        if (codePointIndexToCharIndex == -1) {
            if (this.data.length <= 64) {
                return "[hex=" + this.hex() + "]";
            }
            return "[size=" + this.data.length + " hex=" + this.substring(0, 64).hex() + "\u2026]";
        }
        else {
            final String replace = utf8.substring(0, codePointIndexToCharIndex).replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r");
            if (codePointIndexToCharIndex < utf8.length()) {
                return "[size=" + this.data.length + " text=" + replace + "\u2026]";
            }
            return "[text=" + replace + "]";
        }
    }
    
    public String utf8() {
        final String utf8 = this.utf8;
        if (utf8 != null) {
            return utf8;
        }
        return this.utf8 = new String(this.data, Util.UTF_8);
    }
    
    public void write(final OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        outputStream.write(this.data);
    }
    
    void write(final Buffer buffer) {
        buffer.write(this.data, 0, this.data.length);
    }
}
