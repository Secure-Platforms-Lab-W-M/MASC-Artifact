// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal;

import java.util.concurrent.ThreadFactory;
import java.io.InterruptedIOException;
import okio.Buffer;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import okhttp3.HttpUrl;
import okio.Source;
import javax.annotation.Nullable;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.Closeable;
import java.util.concurrent.TimeUnit;
import java.net.InetAddress;
import java.util.Locale;
import java.net.IDN;
import java.io.IOException;
import okio.BufferedSource;
import okhttp3.MediaType;
import java.util.regex.Pattern;
import okio.ByteString;
import java.util.TimeZone;
import java.util.Comparator;
import java.nio.charset.Charset;
import okhttp3.ResponseBody;
import okhttp3.RequestBody;

public final class Util
{
    public static final byte[] EMPTY_BYTE_ARRAY;
    public static final RequestBody EMPTY_REQUEST;
    public static final ResponseBody EMPTY_RESPONSE;
    public static final String[] EMPTY_STRING_ARRAY;
    public static final Charset ISO_8859_1;
    public static final Comparator<String> NATURAL_ORDER;
    public static final TimeZone UTC;
    private static final Charset UTF_16_BE;
    private static final ByteString UTF_16_BE_BOM;
    private static final Charset UTF_16_LE;
    private static final ByteString UTF_16_LE_BOM;
    private static final Charset UTF_32_BE;
    private static final ByteString UTF_32_BE_BOM;
    private static final Charset UTF_32_LE;
    private static final ByteString UTF_32_LE_BOM;
    public static final Charset UTF_8;
    private static final ByteString UTF_8_BOM;
    private static final Pattern VERIFY_AS_IP_ADDRESS;
    
    static {
        EMPTY_BYTE_ARRAY = new byte[0];
        EMPTY_STRING_ARRAY = new String[0];
        EMPTY_RESPONSE = ResponseBody.create(null, Util.EMPTY_BYTE_ARRAY);
        EMPTY_REQUEST = RequestBody.create(null, Util.EMPTY_BYTE_ARRAY);
        UTF_8_BOM = ByteString.decodeHex("efbbbf");
        UTF_16_BE_BOM = ByteString.decodeHex("feff");
        UTF_16_LE_BOM = ByteString.decodeHex("fffe");
        UTF_32_BE_BOM = ByteString.decodeHex("0000ffff");
        UTF_32_LE_BOM = ByteString.decodeHex("ffff0000");
        UTF_8 = Charset.forName("UTF-8");
        ISO_8859_1 = Charset.forName("ISO-8859-1");
        UTF_16_BE = Charset.forName("UTF-16BE");
        UTF_16_LE = Charset.forName("UTF-16LE");
        UTF_32_BE = Charset.forName("UTF-32BE");
        UTF_32_LE = Charset.forName("UTF-32LE");
        UTC = TimeZone.getTimeZone("GMT");
        NATURAL_ORDER = new Comparator<String>() {
            @Override
            public int compare(final String s, final String s2) {
                return s.compareTo(s2);
            }
        };
        VERIFY_AS_IP_ADDRESS = Pattern.compile("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
    }
    
    private Util() {
    }
    
    public static AssertionError assertionError(String s, final Exception ex) {
        s = (String)new AssertionError((Object)s);
        try {
            ((Throwable)s).initCause(ex);
            return (AssertionError)s;
        }
        catch (IllegalStateException ex2) {
            return (AssertionError)s;
        }
    }
    
    public static Charset bomAwareCharset(final BufferedSource bufferedSource, Charset utf_8) throws IOException {
        if (bufferedSource.rangeEquals(0L, Util.UTF_8_BOM)) {
            bufferedSource.skip(Util.UTF_8_BOM.size());
            utf_8 = Util.UTF_8;
        }
        else {
            if (bufferedSource.rangeEquals(0L, Util.UTF_16_BE_BOM)) {
                bufferedSource.skip(Util.UTF_16_BE_BOM.size());
                return Util.UTF_16_BE;
            }
            if (bufferedSource.rangeEquals(0L, Util.UTF_16_LE_BOM)) {
                bufferedSource.skip(Util.UTF_16_LE_BOM.size());
                return Util.UTF_16_LE;
            }
            if (bufferedSource.rangeEquals(0L, Util.UTF_32_BE_BOM)) {
                bufferedSource.skip(Util.UTF_32_BE_BOM.size());
                return Util.UTF_32_BE;
            }
            if (bufferedSource.rangeEquals(0L, Util.UTF_32_LE_BOM)) {
                bufferedSource.skip(Util.UTF_32_LE_BOM.size());
                return Util.UTF_32_LE;
            }
        }
        return utf_8;
    }
    
    public static String canonicalizeHost(String lowerCase) {
        if (lowerCase.contains(":")) {
            InetAddress inetAddress;
            if (lowerCase.startsWith("[") && lowerCase.endsWith("]")) {
                inetAddress = decodeIpv6(lowerCase, 1, lowerCase.length() - 1);
            }
            else {
                inetAddress = decodeIpv6(lowerCase, 0, lowerCase.length());
            }
            if (inetAddress == null) {
                lowerCase = null;
            }
            else {
                final byte[] address = inetAddress.getAddress();
                if (address.length == 16) {
                    return inet6AddressToAscii(address);
                }
                throw new AssertionError((Object)("Invalid IPv6 address: '" + lowerCase + "'"));
            }
        }
        else {
            try {
                lowerCase = IDN.toASCII(lowerCase).toLowerCase(Locale.US);
                if (lowerCase.isEmpty()) {
                    return null;
                }
                if (containsInvalidHostnameAsciiCodes(lowerCase)) {
                    return null;
                }
            }
            catch (IllegalArgumentException ex) {
                return null;
            }
        }
        return lowerCase;
    }
    
    public static int checkDuration(final String s, final long n, final TimeUnit timeUnit) {
        if (n < 0L) {
            throw new IllegalArgumentException(s + " < 0");
        }
        if (timeUnit == null) {
            throw new NullPointerException("unit == null");
        }
        final long millis = timeUnit.toMillis(n);
        if (millis > 2147483647L) {
            throw new IllegalArgumentException(s + " too large.");
        }
        if (millis == 0L && n > 0L) {
            throw new IllegalArgumentException(s + " too small.");
        }
        return (int)millis;
    }
    
    public static void checkOffsetAndCount(final long n, final long n2, final long n3) {
        if ((n2 | n3) < 0L || n2 > n || n - n2 < n3) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
    
    public static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {}
    }
    
    public static void closeQuietly(final ServerSocket serverSocket) {
        if (serverSocket == null) {
            return;
        }
        try {
            serverSocket.close();
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {}
    }
    
    public static void closeQuietly(final Socket socket) {
        if (socket == null) {
            return;
        }
        try {
            socket.close();
        }
        catch (AssertionError assertionError) {
            if (!isAndroidGetsocknameError(assertionError)) {
                throw assertionError;
            }
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {}
    }
    
    public static String[] concat(final String[] array, final String s) {
        final String[] array2 = new String[array.length + 1];
        System.arraycopy(array, 0, array2, 0, array.length);
        array2[array2.length - 1] = s;
        return array2;
    }
    
    private static boolean containsInvalidHostnameAsciiCodes(final String s) {
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (char1 <= '\u001f' || char1 >= '\u007f' || " #%/:?@[\\]".indexOf(char1) != -1) {
                return true;
            }
        }
        return false;
    }
    
    public static int decodeHexDigit(final char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        return -1;
    }
    
    private static boolean decodeIpv4Suffix(final String s, int n, final int n2, final byte[] array, final int n3) {
        int n4 = n3;
        int i = n;
        while (i < n2) {
            if (n4 != array.length) {
                n = i;
                if (n4 != n3) {
                    if (s.charAt(i) != '.') {
                        return false;
                    }
                    n = i + 1;
                }
                int n5 = 0;
                for (i = n; i < n2; ++i) {
                    final char char1 = s.charAt(i);
                    if (char1 < '0' || char1 > '9') {
                        break;
                    }
                    if (n5 == 0 && n != i) {
                        return false;
                    }
                    n5 = n5 * 10 + char1 - 48;
                    if (n5 > 255) {
                        return false;
                    }
                }
                if (i - n != 0) {
                    array[n4] = (byte)n5;
                    ++n4;
                    continue;
                }
            }
            return false;
        }
        if (n4 == n3 + 4) {
            return true;
        }
        return false;
    }
    
    @Nullable
    private static InetAddress decodeIpv6(final String s, int i, final int n) {
        final byte[] array = new byte[16];
        int n2 = 0;
        int n3 = -1;
        int n4 = -1;
        int n5 = i;
        while (true) {
            Label_0202: {
                int n6;
                while (true) {
                    i = n2;
                    n6 = n3;
                    if (n5 >= n) {
                        break;
                    }
                    if (n2 == array.length) {
                        return null;
                    }
                    int n9;
                    int n11;
                    if (n5 + 2 <= n && s.regionMatches(n5, "::", 0, 2)) {
                        if (n3 != -1) {
                            return null;
                        }
                        final int n7 = n5 + 2;
                        final int n10;
                        final int n8 = n9 = (n10 = n2 + 2);
                        n11 = n10;
                        if ((i = n7) == n) {
                            n6 = n10;
                            i = n8;
                            break;
                        }
                    }
                    else {
                        n9 = n2;
                        n11 = n3;
                        i = n5;
                        if (n2 != 0) {
                            if (!s.regionMatches(n5, ":", 0, 1)) {
                                break Label_0202;
                            }
                            i = n5 + 1;
                            n11 = n3;
                            n9 = n2;
                        }
                    }
                    int n12 = 0;
                    final int n13 = i;
                    while (i < n) {
                        final int decodeHexDigit = decodeHexDigit(s.charAt(i));
                        if (decodeHexDigit == -1) {
                            break;
                        }
                        n12 = (n12 << 4) + decodeHexDigit;
                        ++i;
                    }
                    final int n14 = i - n13;
                    if (n14 == 0 || n14 > 4) {
                        return null;
                    }
                    final int n15 = n9 + 1;
                    array[n9] = (byte)(n12 >>> 8 & 0xFF);
                    final int n16 = n15 + 1;
                    array[n15] = (byte)(n12 & 0xFF);
                    n2 = n16;
                    n3 = n11;
                    n4 = n13;
                    n5 = i;
                }
                if (i != array.length) {
                    if (n6 == -1) {
                        return null;
                    }
                    System.arraycopy(array, n6, array, array.length - (i - n6), i - n6);
                    Arrays.fill(array, n6, array.length - i + n6, (byte)0);
                }
                try {
                    return InetAddress.getByAddress(array);
                }
                catch (UnknownHostException ex) {
                    throw new AssertionError();
                }
                return null;
            }
            if (!s.regionMatches(n5, ".", 0, 1)) {
                return null;
            }
            if (!decodeIpv4Suffix(s, n4, n, array, n2 - 2)) {
                return null;
            }
            i = n2 + 2;
            int n6 = n3;
            continue;
        }
    }
    
    public static int delimiterOffset(final String s, int i, final int n, final char c) {
        while (i < n) {
            if (s.charAt(i) == c) {
                return i;
            }
            ++i;
        }
        return n;
    }
    
    public static int delimiterOffset(final String s, int i, final int n, final String s2) {
        while (i < n) {
            if (s2.indexOf(s.charAt(i)) != -1) {
                return i;
            }
            ++i;
        }
        return n;
    }
    
    public static boolean discard(final Source source, final int n, final TimeUnit timeUnit) {
        try {
            return skipAll(source, n, timeUnit);
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    public static boolean equal(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static String format(final String s, final Object... array) {
        return String.format(Locale.US, s, array);
    }
    
    public static String hostHeader(final HttpUrl httpUrl, final boolean b) {
        String s;
        if (httpUrl.host().contains(":")) {
            s = "[" + httpUrl.host() + "]";
        }
        else {
            s = httpUrl.host();
        }
        if (!b) {
            final String string = s;
            if (httpUrl.port() == HttpUrl.defaultPort(httpUrl.scheme())) {
                return string;
            }
        }
        return s + ":" + httpUrl.port();
    }
    
    public static <T> List<T> immutableList(final List<T> list) {
        return Collections.unmodifiableList((List<? extends T>)new ArrayList<T>((Collection<? extends T>)list));
    }
    
    public static <T> List<T> immutableList(final T... array) {
        return Collections.unmodifiableList((List<? extends T>)Arrays.asList((T[])array.clone()));
    }
    
    public static int indexOf(final Comparator<String> comparator, final String[] array, final String s) {
        for (int i = 0; i < array.length; ++i) {
            if (comparator.compare(array[i], s) == 0) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOfControlOrNonAscii(final String s) {
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (char1 <= '\u001f' || char1 >= '\u007f') {
                return i;
            }
        }
        return -1;
    }
    
    private static String inet6AddressToAscii(final byte[] array) {
        int n = -1;
        int n2 = 0;
        int n4;
        int n6;
        int n7;
        for (int i = 0; i < array.length; i = n4 + 2, n2 = n6, n = n7) {
            int n3 = i;
            while (true) {
                n4 = n3;
                if (n4 >= 16 || array[n4] != 0 || array[n4 + 1] != 0) {
                    break;
                }
                n3 = n4 + 2;
            }
            final int n5 = n4 - i;
            n6 = n2;
            n7 = n;
            if (n5 > n2) {
                n6 = n2;
                n7 = n;
                if (n5 >= 4) {
                    n6 = n5;
                    n7 = i;
                }
            }
        }
        final Buffer buffer = new Buffer();
        int j = 0;
        while (j < array.length) {
            if (j == n) {
                buffer.writeByte(58);
                final int n8 = j + n2;
                if ((j = n8) != 16) {
                    continue;
                }
                buffer.writeByte(58);
                j = n8;
            }
            else {
                if (j > 0) {
                    buffer.writeByte(58);
                }
                buffer.writeHexadecimalUnsignedLong((long)((array[j] & 0xFF) << 8 | (array[j + 1] & 0xFF)));
                j += 2;
            }
        }
        return buffer.readUtf8();
    }
    
    public static String[] intersect(final Comparator<? super String> comparator, final String[] array, final String[] array2) {
        final ArrayList<String> list = new ArrayList<String>();
        for (int length = array.length, i = 0; i < length; ++i) {
            final String s = array[i];
            for (int length2 = array2.length, j = 0; j < length2; ++j) {
                if (comparator.compare(s, array2[j]) == 0) {
                    list.add(s);
                    break;
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }
    
    public static boolean isAndroidGetsocknameError(final AssertionError assertionError) {
        return assertionError.getCause() != null && assertionError.getMessage() != null && assertionError.getMessage().contains("getsockname failed");
    }
    
    public static boolean nonEmptyIntersection(final Comparator<String> comparator, final String[] array, final String[] array2) {
        if (array != null && array2 != null && array.length != 0 && array2.length != 0) {
            for (int length = array.length, i = 0; i < length; ++i) {
                final String s = array[i];
                for (int length2 = array2.length, j = 0; j < length2; ++j) {
                    if (comparator.compare(s, array2[j]) == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean skipAll(final Source source, final int n, final TimeUnit timeUnit) throws IOException {
        final long nanoTime = System.nanoTime();
        Label_0106: {
            if (!source.timeout().hasDeadline()) {
                break Label_0106;
            }
            long n2 = source.timeout().deadlineNanoTime() - nanoTime;
            while (true) {
                source.timeout().deadlineNanoTime(Math.min(n2, timeUnit.toNanos(n)) + nanoTime);
                Label_0113: {
                    try {
                        final Buffer buffer = new Buffer();
                        while (source.read(buffer, 8192L) != -1L) {
                            buffer.clear();
                        }
                        break Label_0113;
                    }
                    catch (InterruptedIOException ex) {
                        if (n2 == Long.MAX_VALUE) {
                            source.timeout().clearDeadline();
                        }
                        else {
                            source.timeout().deadlineNanoTime(nanoTime + n2);
                        }
                        return false;
                        // iftrue(Label_0133:, n2 != 9223372036854775807L)
                        Block_7: {
                            break Block_7;
                            n2 = Long.MAX_VALUE;
                            continue;
                            Label_0133: {
                                source.timeout().deadlineNanoTime(nanoTime + n2);
                            }
                            return true;
                        }
                        source.timeout().clearDeadline();
                        return true;
                    }
                    finally {
                        while (true) {
                            if (n2 == Long.MAX_VALUE) {
                                source.timeout().clearDeadline();
                                break Label_0186;
                            }
                            source.timeout().deadlineNanoTime(nanoTime + n2);
                            break Label_0186;
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    public static int skipLeadingAsciiWhitespace(final String s, int i, final int n) {
        while (i < n) {
            switch (s.charAt(i)) {
                default: {
                    return i;
                }
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ': {
                    ++i;
                    continue;
                }
            }
        }
        return n;
    }
    
    public static int skipTrailingAsciiWhitespace(final String s, final int n, int n2) {
        --n2;
        int n3 = 0;
    Label_0072:
        while (true) {
            n3 = n;
            if (n2 < n) {
                break;
            }
            switch (s.charAt(n2)) {
                default: {
                    n3 = n2 + 1;
                    break Label_0072;
                }
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ': {
                    --n2;
                    continue;
                }
            }
        }
        return n3;
    }
    
    public static ThreadFactory threadFactory(final String s, final boolean b) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable runnable) {
                final Thread thread = new Thread(runnable, s);
                thread.setDaemon(b);
                return thread;
            }
        };
    }
    
    public static String trimSubstring(final String s, int skipLeadingAsciiWhitespace, final int n) {
        skipLeadingAsciiWhitespace = skipLeadingAsciiWhitespace(s, skipLeadingAsciiWhitespace, n);
        return s.substring(skipLeadingAsciiWhitespace, skipTrailingAsciiWhitespace(s, skipLeadingAsciiWhitespace, n));
    }
    
    public static boolean verifyAsIpAddress(final String s) {
        return Util.VERIFY_AS_IP_ADDRESS.matcher(s).matches();
    }
}
