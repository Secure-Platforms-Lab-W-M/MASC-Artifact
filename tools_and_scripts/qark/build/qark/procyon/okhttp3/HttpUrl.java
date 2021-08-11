// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import java.net.URISyntaxException;
import okhttp3.internal.publicsuffix.PublicSuffixDatabase;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.net.UnknownHostException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import okhttp3.internal.Util;
import okio.Buffer;
import java.nio.charset.Charset;
import java.util.List;
import javax.annotation.Nullable;

public final class HttpUrl
{
    static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
    static final String FRAGMENT_ENCODE_SET = "";
    static final String FRAGMENT_ENCODE_SET_URI = " \"#<>\\^`{|}";
    private static final char[] HEX_DIGITS;
    static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
    static final String PATH_SEGMENT_ENCODE_SET_URI = "[]";
    static final String QUERY_COMPONENT_ENCODE_SET = " !\"#$&'(),/:;<=>?@[]\\^`{|}~";
    static final String QUERY_COMPONENT_ENCODE_SET_URI = "\\^`{|}";
    static final String QUERY_COMPONENT_REENCODE_SET = " \"'<>#&=";
    static final String QUERY_ENCODE_SET = " \"'<>#";
    static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    @Nullable
    private final String fragment;
    final String host;
    private final String password;
    private final List<String> pathSegments;
    final int port;
    @Nullable
    private final List<String> queryNamesAndValues;
    final String scheme;
    private final String url;
    private final String username;
    
    static {
        HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    }
    
    HttpUrl(final Builder builder) {
        final String s = null;
        this.scheme = builder.scheme;
        this.username = percentDecode(builder.encodedUsername, false);
        this.password = percentDecode(builder.encodedPassword, false);
        this.host = builder.host;
        this.port = builder.effectivePort();
        this.pathSegments = this.percentDecode(builder.encodedPathSegments, false);
        List<String> percentDecode;
        if (builder.encodedQueryNamesAndValues != null) {
            percentDecode = this.percentDecode(builder.encodedQueryNamesAndValues, true);
        }
        else {
            percentDecode = null;
        }
        this.queryNamesAndValues = percentDecode;
        String percentDecode2 = s;
        if (builder.encodedFragment != null) {
            percentDecode2 = percentDecode(builder.encodedFragment, false);
        }
        this.fragment = percentDecode2;
        this.url = builder.toString();
    }
    
    static String canonicalize(final String s, final int n, final int n2, final String s2, final boolean b, final boolean b2, final boolean b3, final boolean b4, final Charset charset) {
        int codePoint;
        for (int i = n; i < n2; i += Character.charCount(codePoint)) {
            codePoint = s.codePointAt(i);
            if (codePoint < 32 || codePoint == 127 || (codePoint >= 128 && b4) || s2.indexOf(codePoint) != -1 || (codePoint == 37 && (!b || (b2 && !percentEncoded(s, i, n2)))) || (codePoint == 43 && b3)) {
                final Buffer buffer = new Buffer();
                buffer.writeUtf8(s, n, i);
                canonicalize(buffer, s, i, n2, s2, b, b2, b3, b4, charset);
                return buffer.readUtf8();
            }
        }
        return s.substring(n, n2);
    }
    
    static String canonicalize(final String s, final String s2, final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        return canonicalize(s, 0, s.length(), s2, b, b2, b3, b4, null);
    }
    
    static String canonicalize(final String s, final String s2, final boolean b, final boolean b2, final boolean b3, final boolean b4, final Charset charset) {
        return canonicalize(s, 0, s.length(), s2, b, b2, b3, b4, charset);
    }
    
    static void canonicalize(final Buffer buffer, final String s, int i, final int n, final String s2, final boolean b, final boolean b2, final boolean b3, final boolean b4, final Charset charset) {
        Buffer buffer2 = null;
    Label_0064_Outer:
        while (i < n) {
            final int codePoint = s.codePointAt(i);
        Label_0064:
            while (true) {
                Label_0079: {
                    if (!b) {
                        break Label_0079;
                    }
                    Buffer buffer3 = buffer2;
                    if (codePoint != 9) {
                        buffer3 = buffer2;
                        if (codePoint != 10) {
                            buffer3 = buffer2;
                            if (codePoint != 12) {
                                if (codePoint != 13) {
                                    break Label_0079;
                                }
                                buffer3 = buffer2;
                            }
                        }
                    }
                    i += Character.charCount(codePoint);
                    buffer2 = buffer3;
                    continue Label_0064_Outer;
                }
                if (codePoint == 43 && b3) {
                    String s3;
                    if (b) {
                        s3 = "+";
                    }
                    else {
                        s3 = "%2B";
                    }
                    buffer.writeUtf8(s3);
                    final Buffer buffer3 = buffer2;
                    continue Label_0064;
                }
                if (codePoint >= 32 && codePoint != 127 && (codePoint < 128 || !b4) && s2.indexOf(codePoint) == -1 && (codePoint != 37 || (b && (!b2 || percentEncoded(s, i, n))))) {
                    buffer.writeUtf8CodePoint(codePoint);
                    final Buffer buffer3 = buffer2;
                    continue Label_0064;
                }
                Buffer buffer4;
                if ((buffer4 = buffer2) == null) {
                    buffer4 = new Buffer();
                }
                if (charset == null || charset.equals(Util.UTF_8)) {
                    buffer4.writeUtf8CodePoint(codePoint);
                }
                else {
                    buffer4.writeString(s, i, Character.charCount(codePoint) + i, charset);
                }
                while (true) {
                    final Buffer buffer3 = buffer4;
                    if (buffer4.exhausted()) {
                        continue Label_0064;
                    }
                    final int n2 = buffer4.readByte() & 0xFF;
                    buffer.writeByte(37);
                    buffer.writeByte((int)HttpUrl.HEX_DIGITS[n2 >> 4 & 0xF]);
                    buffer.writeByte((int)HttpUrl.HEX_DIGITS[n2 & 0xF]);
                }
                break;
            }
        }
    }
    
    public static int defaultPort(final String s) {
        if (s.equals("http")) {
            return 80;
        }
        if (s.equals("https")) {
            return 443;
        }
        return -1;
    }
    
    @Nullable
    public static HttpUrl get(final URI uri) {
        return parse(uri.toString());
    }
    
    @Nullable
    public static HttpUrl get(final URL url) {
        return parse(url.toString());
    }
    
    static HttpUrl getChecked(final String s) throws MalformedURLException, UnknownHostException {
        final Builder builder = new Builder();
        final ParseResult parse = builder.parse(null, s);
        switch (parse) {
            default: {
                throw new MalformedURLException("Invalid URL: " + parse + " for " + s);
            }
            case SUCCESS: {
                return builder.build();
            }
            case INVALID_HOST: {
                throw new UnknownHostException("Invalid host: " + s);
            }
        }
    }
    
    static void namesAndValuesToQueryString(final StringBuilder sb, final List<String> list) {
        for (int i = 0; i < list.size(); i += 2) {
            final String s = list.get(i);
            final String s2 = list.get(i + 1);
            if (i > 0) {
                sb.append('&');
            }
            sb.append(s);
            if (s2 != null) {
                sb.append('=');
                sb.append(s2);
            }
        }
    }
    
    @Nullable
    public static HttpUrl parse(final String s) {
        HttpUrl build = null;
        final Builder builder = new Builder();
        if (builder.parse(null, s) == ParseResult.SUCCESS) {
            build = builder.build();
        }
        return build;
    }
    
    static void pathSegmentsToString(final StringBuilder sb, final List<String> list) {
        for (int i = 0; i < list.size(); ++i) {
            sb.append('/');
            sb.append(list.get(i));
        }
    }
    
    static String percentDecode(final String s, final int n, final int n2, final boolean b) {
        for (int i = n; i < n2; ++i) {
            final char char1 = s.charAt(i);
            if (char1 == '%' || (char1 == '+' && b)) {
                final Buffer buffer = new Buffer();
                buffer.writeUtf8(s, n, i);
                percentDecode(buffer, s, i, n2, b);
                return buffer.readUtf8();
            }
        }
        return s.substring(n, n2);
    }
    
    static String percentDecode(final String s, final boolean b) {
        return percentDecode(s, 0, s.length(), b);
    }
    
    private List<String> percentDecode(final List<String> list, final boolean b) {
        final int size = list.size();
        final ArrayList list2 = new ArrayList<String>(size);
        for (int i = 0; i < size; ++i) {
            final String s = list.get(i);
            String percentDecode;
            if (s != null) {
                percentDecode = percentDecode(s, b);
            }
            else {
                percentDecode = null;
            }
            list2.add(percentDecode);
        }
        return Collections.unmodifiableList((List<? extends String>)list2);
    }
    
    static void percentDecode(final Buffer buffer, final String s, int i, final int n, final boolean b) {
    Label_0078_Outer:
        while (i < n) {
            final int codePoint = s.codePointAt(i);
            while (true) {
                Label_0111: {
                    if (codePoint == 37 && i + 2 < n) {
                        final int decodeHexDigit = Util.decodeHexDigit(s.charAt(i + 1));
                        final int decodeHexDigit2 = Util.decodeHexDigit(s.charAt(i + 2));
                        if (decodeHexDigit == -1 || decodeHexDigit2 == -1) {
                            break Label_0111;
                        }
                        buffer.writeByte((decodeHexDigit << 4) + decodeHexDigit2);
                        i += 2;
                    }
                    else {
                        if (codePoint != 43 || !b) {
                            break Label_0111;
                        }
                        buffer.writeByte(32);
                    }
                    i += Character.charCount(codePoint);
                    continue Label_0078_Outer;
                }
                buffer.writeUtf8CodePoint(codePoint);
                continue;
            }
        }
    }
    
    static boolean percentEncoded(final String s, final int n, final int n2) {
        return n + 2 < n2 && s.charAt(n) == '%' && Util.decodeHexDigit(s.charAt(n + 1)) != -1 && Util.decodeHexDigit(s.charAt(n + 2)) != -1;
    }
    
    static List<String> queryStringToNamesAndValues(final String s) {
        final ArrayList<String> list = new ArrayList<String>();
        int n;
        for (int i = 0; i <= s.length(); i = n + 1) {
            if ((n = s.indexOf(38, i)) == -1) {
                n = s.length();
            }
            final int index = s.indexOf(61, i);
            if (index == -1 || index > n) {
                list.add(s.substring(i, n));
                list.add(null);
            }
            else {
                list.add(s.substring(i, index));
                list.add(s.substring(index + 1, n));
            }
        }
        return list;
    }
    
    @Nullable
    public String encodedFragment() {
        if (this.fragment == null) {
            return null;
        }
        return this.url.substring(this.url.indexOf(35) + 1);
    }
    
    public String encodedPassword() {
        if (this.password.isEmpty()) {
            return "";
        }
        return this.url.substring(this.url.indexOf(58, this.scheme.length() + 3) + 1, this.url.indexOf(64));
    }
    
    public String encodedPath() {
        final int index = this.url.indexOf(47, this.scheme.length() + 3);
        return this.url.substring(index, Util.delimiterOffset(this.url, index, this.url.length(), "?#"));
    }
    
    public List<String> encodedPathSegments() {
        int i = this.url.indexOf(47, this.scheme.length() + 3);
        final int delimiterOffset = Util.delimiterOffset(this.url, i, this.url.length(), "?#");
        final ArrayList<String> list = new ArrayList<String>();
        while (i < delimiterOffset) {
            final int n = i + 1;
            i = Util.delimiterOffset(this.url, n, delimiterOffset, '/');
            list.add(this.url.substring(n, i));
        }
        return list;
    }
    
    @Nullable
    public String encodedQuery() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        final int n = this.url.indexOf(63) + 1;
        return this.url.substring(n, Util.delimiterOffset(this.url, n, this.url.length(), '#'));
    }
    
    public String encodedUsername() {
        if (this.username.isEmpty()) {
            return "";
        }
        final int n = this.scheme.length() + 3;
        return this.url.substring(n, Util.delimiterOffset(this.url, n, this.url.length(), ":@"));
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        return o instanceof HttpUrl && ((HttpUrl)o).url.equals(this.url);
    }
    
    @Nullable
    public String fragment() {
        return this.fragment;
    }
    
    @Override
    public int hashCode() {
        return this.url.hashCode();
    }
    
    public String host() {
        return this.host;
    }
    
    public boolean isHttps() {
        return this.scheme.equals("https");
    }
    
    public Builder newBuilder() {
        final Builder builder = new Builder();
        builder.scheme = this.scheme;
        builder.encodedUsername = this.encodedUsername();
        builder.encodedPassword = this.encodedPassword();
        builder.host = this.host;
        int port;
        if (this.port != defaultPort(this.scheme)) {
            port = this.port;
        }
        else {
            port = -1;
        }
        builder.port = port;
        builder.encodedPathSegments.clear();
        builder.encodedPathSegments.addAll(this.encodedPathSegments());
        builder.encodedQuery(this.encodedQuery());
        builder.encodedFragment = this.encodedFragment();
        return builder;
    }
    
    @Nullable
    public Builder newBuilder(final String s) {
        final Builder builder = new Builder();
        if (builder.parse(this, s) == ParseResult.SUCCESS) {
            return builder;
        }
        return null;
    }
    
    public String password() {
        return this.password;
    }
    
    public List<String> pathSegments() {
        return this.pathSegments;
    }
    
    public int pathSize() {
        return this.pathSegments.size();
    }
    
    public int port() {
        return this.port;
    }
    
    @Nullable
    public String query() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        namesAndValuesToQueryString(sb, this.queryNamesAndValues);
        return sb.toString();
    }
    
    @Nullable
    public String queryParameter(final String s) {
        if (this.queryNamesAndValues != null) {
            for (int i = 0; i < this.queryNamesAndValues.size(); i += 2) {
                if (s.equals(this.queryNamesAndValues.get(i))) {
                    return this.queryNamesAndValues.get(i + 1);
                }
            }
        }
        return null;
    }
    
    public String queryParameterName(final int n) {
        if (this.queryNamesAndValues == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.queryNamesAndValues.get(n * 2);
    }
    
    public Set<String> queryParameterNames() {
        if (this.queryNamesAndValues == null) {
            return Collections.emptySet();
        }
        final LinkedHashSet<String> set = new LinkedHashSet<String>();
        for (int i = 0; i < this.queryNamesAndValues.size(); i += 2) {
            set.add(this.queryNamesAndValues.get(i));
        }
        return (Set<String>)Collections.unmodifiableSet((Set<?>)set);
    }
    
    public String queryParameterValue(final int n) {
        if (this.queryNamesAndValues == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.queryNamesAndValues.get(n * 2 + 1);
    }
    
    public List<String> queryParameterValues(final String s) {
        if (this.queryNamesAndValues == null) {
            return Collections.emptyList();
        }
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < this.queryNamesAndValues.size(); i += 2) {
            if (s.equals(this.queryNamesAndValues.get(i))) {
                list.add(this.queryNamesAndValues.get(i + 1));
            }
        }
        return (List<String>)Collections.unmodifiableList((List<?>)list);
    }
    
    public int querySize() {
        if (this.queryNamesAndValues != null) {
            return this.queryNamesAndValues.size() / 2;
        }
        return 0;
    }
    
    public String redact() {
        return this.newBuilder("/...").username("").password("").build().toString();
    }
    
    @Nullable
    public HttpUrl resolve(final String s) {
        final Builder builder = this.newBuilder(s);
        if (builder != null) {
            return builder.build();
        }
        return null;
    }
    
    public String scheme() {
        return this.scheme;
    }
    
    @Override
    public String toString() {
        return this.url;
    }
    
    @Nullable
    public String topPrivateDomain() {
        if (Util.verifyAsIpAddress(this.host)) {
            return null;
        }
        return PublicSuffixDatabase.get().getEffectiveTldPlusOne(this.host);
    }
    
    public URI uri() {
        final String string = this.newBuilder().reencodeForUri().toString();
        try {
            return new URI(string);
        }
        catch (URISyntaxException ex) {
            try {
                return URI.create(string.replaceAll("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]", ""));
            }
            catch (Exception ex2) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    public URL url() {
        try {
            return new URL(this.url);
        }
        catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public String username() {
        return this.username;
    }
    
    public static final class Builder
    {
        @Nullable
        String encodedFragment;
        String encodedPassword;
        final List<String> encodedPathSegments;
        @Nullable
        List<String> encodedQueryNamesAndValues;
        String encodedUsername;
        @Nullable
        String host;
        int port;
        @Nullable
        String scheme;
        
        public Builder() {
            this.encodedUsername = "";
            this.encodedPassword = "";
            this.port = -1;
            (this.encodedPathSegments = new ArrayList<String>()).add("");
        }
        
        private Builder addPathSegments(final String s, final boolean b) {
            int n = 0;
            int delimiterOffset;
            do {
                delimiterOffset = Util.delimiterOffset(s, n, s.length(), "/\\");
                this.push(s, n, delimiterOffset, delimiterOffset < s.length(), b);
            } while ((n = delimiterOffset + 1) <= s.length());
            return this;
        }
        
        private static String canonicalizeHost(final String s, final int n, final int n2) {
            return Util.canonicalizeHost(HttpUrl.percentDecode(s, n, n2, false));
        }
        
        private boolean isDot(final String s) {
            return s.equals(".") || s.equalsIgnoreCase("%2e");
        }
        
        private boolean isDotDot(final String s) {
            return s.equals("..") || s.equalsIgnoreCase("%2e.") || s.equalsIgnoreCase(".%2e") || s.equalsIgnoreCase("%2e%2e");
        }
        
        private static int parsePort(final String s, int int1, final int n) {
            try {
                int1 = Integer.parseInt(HttpUrl.canonicalize(s, int1, n, "", false, false, false, true, null));
                if (int1 > 0 && int1 <= 65535) {
                    return int1;
                }
                return -1;
            }
            catch (NumberFormatException ex) {
                return -1;
            }
        }
        
        private void pop() {
            if (this.encodedPathSegments.remove(this.encodedPathSegments.size() - 1).isEmpty() && !this.encodedPathSegments.isEmpty()) {
                this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, "");
                return;
            }
            this.encodedPathSegments.add("");
        }
        
        private static int portColonOffset(final String s, int i, final int n) {
            while (i < n) {
                int n2 = i;
                final int n3 = i;
                int n4 = 0;
                Label_0042: {
                    switch (s.charAt(i)) {
                        default: {
                            n4 = i;
                            break;
                        }
                        case '[': {
                            do {
                                i = n2 + 1;
                                if ((n4 = i) >= n) {
                                    break Label_0042;
                                }
                                n2 = i;
                            } while (s.charAt(i) != ']');
                            n4 = i;
                            break;
                        }
                        case ':': {
                            return n3;
                        }
                    }
                }
                i = n4 + 1;
            }
            return n;
        }
        
        private void push(String canonicalize, final int n, final int n2, final boolean b, final boolean b2) {
            canonicalize = HttpUrl.canonicalize(canonicalize, n, n2, " \"<>^`{}|/\\?#", b2, false, false, true, null);
            if (!this.isDot(canonicalize)) {
                if (this.isDotDot(canonicalize)) {
                    this.pop();
                    return;
                }
                if (this.encodedPathSegments.get(this.encodedPathSegments.size() - 1).isEmpty()) {
                    this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, canonicalize);
                }
                else {
                    this.encodedPathSegments.add(canonicalize);
                }
                if (b) {
                    this.encodedPathSegments.add("");
                }
            }
        }
        
        private void removeAllCanonicalQueryParameters(final String s) {
            for (int i = this.encodedQueryNamesAndValues.size() - 2; i >= 0; i -= 2) {
                if (s.equals(this.encodedQueryNamesAndValues.get(i))) {
                    this.encodedQueryNamesAndValues.remove(i + 1);
                    this.encodedQueryNamesAndValues.remove(i);
                    if (this.encodedQueryNamesAndValues.isEmpty()) {
                        this.encodedQueryNamesAndValues = null;
                        break;
                    }
                }
            }
        }
        
        private void resolvePath(final String s, int i, final int n) {
            if (i != n) {
                final char char1 = s.charAt(i);
                if (char1 == '/' || char1 == '\\') {
                    this.encodedPathSegments.clear();
                    this.encodedPathSegments.add("");
                    ++i;
                }
                else {
                    this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, "");
                }
                while (i < n) {
                    final int delimiterOffset = Util.delimiterOffset(s, i, n, "/\\");
                    final boolean b = delimiterOffset < n;
                    this.push(s, i, delimiterOffset, b, true);
                    i = delimiterOffset;
                    if (b) {
                        i = delimiterOffset + 1;
                    }
                }
            }
        }
        
        private static int schemeDelimiterOffset(final String s, int i, final int n) {
            if (n - i < 2) {
                i = -1;
            }
            else {
                final char char1 = s.charAt(i);
                if ((char1 < 'a' || char1 > 'z') && (char1 < 'A' || char1 > 'Z')) {
                    return -1;
                }
                ++i;
                while (i < n) {
                    final char char2 = s.charAt(i);
                    if ((char2 >= 'a' && char2 <= 'z') || (char2 >= 'A' && char2 <= 'Z') || (char2 >= '0' && char2 <= '9') || char2 == '+' || char2 == '-' || char2 == '.') {
                        ++i;
                    }
                    else {
                        if (char2 != ':') {
                            return -1;
                        }
                        return i;
                    }
                }
                return -1;
            }
            return i;
        }
        
        private static int slashCount(final String s, int i, final int n) {
            int n2 = 0;
            while (i < n) {
                final char char1 = s.charAt(i);
                if (char1 != '\\' && char1 != '/') {
                    break;
                }
                ++n2;
                ++i;
            }
            return n2;
        }
        
        public Builder addEncodedPathSegment(final String s) {
            if (s == null) {
                throw new NullPointerException("encodedPathSegment == null");
            }
            this.push(s, 0, s.length(), false, true);
            return this;
        }
        
        public Builder addEncodedPathSegments(final String s) {
            if (s == null) {
                throw new NullPointerException("encodedPathSegments == null");
            }
            return this.addPathSegments(s, true);
        }
        
        public Builder addEncodedQueryParameter(String canonicalize, @Nullable final String s) {
            if (canonicalize == null) {
                throw new NullPointerException("encodedName == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                this.encodedQueryNamesAndValues = new ArrayList<String>();
            }
            this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(canonicalize, " \"'<>#&=", true, false, true, true));
            final List<String> encodedQueryNamesAndValues = this.encodedQueryNamesAndValues;
            if (s != null) {
                canonicalize = HttpUrl.canonicalize(s, " \"'<>#&=", true, false, true, true);
            }
            else {
                canonicalize = null;
            }
            encodedQueryNamesAndValues.add(canonicalize);
            return this;
        }
        
        public Builder addPathSegment(final String s) {
            if (s == null) {
                throw new NullPointerException("pathSegment == null");
            }
            this.push(s, 0, s.length(), false, false);
            return this;
        }
        
        public Builder addPathSegments(final String s) {
            if (s == null) {
                throw new NullPointerException("pathSegments == null");
            }
            return this.addPathSegments(s, false);
        }
        
        public Builder addQueryParameter(String canonicalize, @Nullable final String s) {
            if (canonicalize == null) {
                throw new NullPointerException("name == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                this.encodedQueryNamesAndValues = new ArrayList<String>();
            }
            this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(canonicalize, " !\"#$&'(),/:;<=>?@[]\\^`{|}~", false, false, true, true));
            final List<String> encodedQueryNamesAndValues = this.encodedQueryNamesAndValues;
            if (s != null) {
                canonicalize = HttpUrl.canonicalize(s, " !\"#$&'(),/:;<=>?@[]\\^`{|}~", false, false, true, true);
            }
            else {
                canonicalize = null;
            }
            encodedQueryNamesAndValues.add(canonicalize);
            return this;
        }
        
        public HttpUrl build() {
            if (this.scheme == null) {
                throw new IllegalStateException("scheme == null");
            }
            if (this.host == null) {
                throw new IllegalStateException("host == null");
            }
            return new HttpUrl(this);
        }
        
        int effectivePort() {
            if (this.port != -1) {
                return this.port;
            }
            return HttpUrl.defaultPort(this.scheme);
        }
        
        public Builder encodedFragment(@Nullable String canonicalize) {
            if (canonicalize != null) {
                canonicalize = HttpUrl.canonicalize(canonicalize, "", true, false, false, false);
            }
            else {
                canonicalize = null;
            }
            this.encodedFragment = canonicalize;
            return this;
        }
        
        public Builder encodedPassword(final String s) {
            if (s == null) {
                throw new NullPointerException("encodedPassword == null");
            }
            this.encodedPassword = HttpUrl.canonicalize(s, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
            return this;
        }
        
        public Builder encodedPath(final String s) {
            if (s == null) {
                throw new NullPointerException("encodedPath == null");
            }
            if (!s.startsWith("/")) {
                throw new IllegalArgumentException("unexpected encodedPath: " + s);
            }
            this.resolvePath(s, 0, s.length());
            return this;
        }
        
        public Builder encodedQuery(@Nullable final String s) {
            List<String> queryStringToNamesAndValues;
            if (s != null) {
                queryStringToNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(s, " \"'<>#", true, false, true, true));
            }
            else {
                queryStringToNamesAndValues = null;
            }
            this.encodedQueryNamesAndValues = queryStringToNamesAndValues;
            return this;
        }
        
        public Builder encodedUsername(final String s) {
            if (s == null) {
                throw new NullPointerException("encodedUsername == null");
            }
            this.encodedUsername = HttpUrl.canonicalize(s, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
            return this;
        }
        
        public Builder fragment(@Nullable String canonicalize) {
            if (canonicalize != null) {
                canonicalize = HttpUrl.canonicalize(canonicalize, "", false, false, false, false);
            }
            else {
                canonicalize = null;
            }
            this.encodedFragment = canonicalize;
            return this;
        }
        
        public Builder host(final String s) {
            if (s == null) {
                throw new NullPointerException("host == null");
            }
            final String canonicalizeHost = canonicalizeHost(s, 0, s.length());
            if (canonicalizeHost == null) {
                throw new IllegalArgumentException("unexpected host: " + s);
            }
            this.host = canonicalizeHost;
            return this;
        }
        
        ParseResult parse(@Nullable final HttpUrl httpUrl, final String s) {
            int skipLeadingAsciiWhitespace = Util.skipLeadingAsciiWhitespace(s, 0, s.length());
            final int skipTrailingAsciiWhitespace = Util.skipTrailingAsciiWhitespace(s, skipLeadingAsciiWhitespace, s.length());
            if (schemeDelimiterOffset(s, skipLeadingAsciiWhitespace, skipTrailingAsciiWhitespace) != -1) {
                if (s.regionMatches(true, skipLeadingAsciiWhitespace, "https:", 0, 6)) {
                    this.scheme = "https";
                    skipLeadingAsciiWhitespace += "https:".length();
                }
                else {
                    if (!s.regionMatches(true, skipLeadingAsciiWhitespace, "http:", 0, 5)) {
                        return ParseResult.UNSUPPORTED_SCHEME;
                    }
                    this.scheme = "http";
                    skipLeadingAsciiWhitespace += "http:".length();
                }
            }
            else {
                if (httpUrl == null) {
                    return ParseResult.MISSING_SCHEME;
                }
                this.scheme = httpUrl.scheme;
            }
            int n = 0;
            final boolean b = false;
            final int slashCount = slashCount(s, skipLeadingAsciiWhitespace, skipTrailingAsciiWhitespace);
            int n5 = 0;
            Label_0524: {
                if (slashCount >= 2 || httpUrl == null || !httpUrl.scheme.equals(this.scheme)) {
                    final int n2 = skipLeadingAsciiWhitespace + slashCount;
                    int n3 = b ? 1 : 0;
                    int n4 = n2;
                    int delimiterOffset = 0;
                Label_0207:
                    while (true) {
                        delimiterOffset = Util.delimiterOffset(s, n4, skipTrailingAsciiWhitespace, "@/\\?#");
                        int char1;
                        if (delimiterOffset != skipTrailingAsciiWhitespace) {
                            char1 = s.charAt(delimiterOffset);
                        }
                        else {
                            char1 = -1;
                        }
                        switch (char1) {
                            default: {
                                continue;
                            }
                            case -1:
                            case 35:
                            case 47:
                            case 63:
                            case 92: {
                                break Label_0207;
                            }
                            case 64: {
                                if (n3 == 0) {
                                    final int delimiterOffset2 = Util.delimiterOffset(s, n4, delimiterOffset, ':');
                                    String encodedUsername = HttpUrl.canonicalize(s, n4, delimiterOffset2, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true, null);
                                    if (n != 0) {
                                        encodedUsername = this.encodedUsername + "%40" + encodedUsername;
                                    }
                                    this.encodedUsername = encodedUsername;
                                    if (delimiterOffset2 != delimiterOffset) {
                                        n3 = 1;
                                        this.encodedPassword = HttpUrl.canonicalize(s, delimiterOffset2 + 1, delimiterOffset, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true, null);
                                    }
                                    n = 1;
                                }
                                else {
                                    this.encodedPassword = this.encodedPassword + "%40" + HttpUrl.canonicalize(s, n4, delimiterOffset, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true, null);
                                }
                                n4 = delimiterOffset + 1;
                                continue;
                            }
                        }
                    }
                    final int portColonOffset = portColonOffset(s, n4, delimiterOffset);
                    if (portColonOffset + 1 < delimiterOffset) {
                        this.host = canonicalizeHost(s, n4, portColonOffset);
                        this.port = parsePort(s, portColonOffset + 1, delimiterOffset);
                        if (this.port == -1) {
                            return ParseResult.INVALID_PORT;
                        }
                    }
                    else {
                        this.host = canonicalizeHost(s, n4, portColonOffset);
                        this.port = HttpUrl.defaultPort(this.scheme);
                    }
                    if (this.host == null) {
                        return ParseResult.INVALID_HOST;
                    }
                    n5 = delimiterOffset;
                }
                else {
                    this.encodedUsername = httpUrl.encodedUsername();
                    this.encodedPassword = httpUrl.encodedPassword();
                    this.host = httpUrl.host;
                    this.port = httpUrl.port;
                    this.encodedPathSegments.clear();
                    this.encodedPathSegments.addAll(httpUrl.encodedPathSegments());
                    if (skipLeadingAsciiWhitespace != skipTrailingAsciiWhitespace) {
                        n5 = skipLeadingAsciiWhitespace;
                        if (s.charAt(skipLeadingAsciiWhitespace) != '#') {
                            break Label_0524;
                        }
                    }
                    this.encodedQuery(httpUrl.encodedQuery());
                    n5 = skipLeadingAsciiWhitespace;
                }
            }
            final int delimiterOffset3 = Util.delimiterOffset(s, n5, skipTrailingAsciiWhitespace, "?#");
            this.resolvePath(s, n5, delimiterOffset3);
            int delimiterOffset4;
            if ((delimiterOffset4 = delimiterOffset3) < skipTrailingAsciiWhitespace) {
                delimiterOffset4 = delimiterOffset3;
                if (s.charAt(delimiterOffset3) == '?') {
                    delimiterOffset4 = Util.delimiterOffset(s, delimiterOffset3, skipTrailingAsciiWhitespace, '#');
                    this.encodedQueryNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(s, delimiterOffset3 + 1, delimiterOffset4, " \"'<>#", true, false, true, true, null));
                }
            }
            if (delimiterOffset4 < skipTrailingAsciiWhitespace && s.charAt(delimiterOffset4) == '#') {
                this.encodedFragment = HttpUrl.canonicalize(s, delimiterOffset4 + 1, skipTrailingAsciiWhitespace, "", true, false, false, false, null);
            }
            return ParseResult.SUCCESS;
        }
        
        public Builder password(final String s) {
            if (s == null) {
                throw new NullPointerException("password == null");
            }
            this.encodedPassword = HttpUrl.canonicalize(s, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
            return this;
        }
        
        public Builder port(final int port) {
            if (port <= 0 || port > 65535) {
                throw new IllegalArgumentException("unexpected port: " + port);
            }
            this.port = port;
            return this;
        }
        
        public Builder query(@Nullable final String s) {
            List<String> queryStringToNamesAndValues;
            if (s != null) {
                queryStringToNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(s, " \"'<>#", false, false, true, true));
            }
            else {
                queryStringToNamesAndValues = null;
            }
            this.encodedQueryNamesAndValues = queryStringToNamesAndValues;
            return this;
        }
        
        Builder reencodeForUri() {
            for (int i = 0; i < this.encodedPathSegments.size(); ++i) {
                this.encodedPathSegments.set(i, HttpUrl.canonicalize(this.encodedPathSegments.get(i), "[]", true, true, false, true));
            }
            if (this.encodedQueryNamesAndValues != null) {
                for (int j = 0; j < this.encodedQueryNamesAndValues.size(); ++j) {
                    final String s = this.encodedQueryNamesAndValues.get(j);
                    if (s != null) {
                        this.encodedQueryNamesAndValues.set(j, HttpUrl.canonicalize(s, "\\^`{|}", true, true, true, true));
                    }
                }
            }
            if (this.encodedFragment != null) {
                this.encodedFragment = HttpUrl.canonicalize(this.encodedFragment, " \"#<>\\^`{|}", true, true, false, false);
            }
            return this;
        }
        
        public Builder removeAllEncodedQueryParameters(final String s) {
            if (s == null) {
                throw new NullPointerException("encodedName == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                return this;
            }
            this.removeAllCanonicalQueryParameters(HttpUrl.canonicalize(s, " \"'<>#&=", true, false, true, true));
            return this;
        }
        
        public Builder removeAllQueryParameters(final String s) {
            if (s == null) {
                throw new NullPointerException("name == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                return this;
            }
            this.removeAllCanonicalQueryParameters(HttpUrl.canonicalize(s, " !\"#$&'(),/:;<=>?@[]\\^`{|}~", false, false, true, true));
            return this;
        }
        
        public Builder removePathSegment(final int n) {
            this.encodedPathSegments.remove(n);
            if (this.encodedPathSegments.isEmpty()) {
                this.encodedPathSegments.add("");
            }
            return this;
        }
        
        public Builder scheme(final String s) {
            if (s == null) {
                throw new NullPointerException("scheme == null");
            }
            if (s.equalsIgnoreCase("http")) {
                this.scheme = "http";
                return this;
            }
            if (s.equalsIgnoreCase("https")) {
                this.scheme = "https";
                return this;
            }
            throw new IllegalArgumentException("unexpected scheme: " + s);
        }
        
        public Builder setEncodedPathSegment(final int n, final String s) {
            if (s == null) {
                throw new NullPointerException("encodedPathSegment == null");
            }
            final String canonicalize = HttpUrl.canonicalize(s, 0, s.length(), " \"<>^`{}|/\\?#", true, false, false, true, null);
            this.encodedPathSegments.set(n, canonicalize);
            if (this.isDot(canonicalize) || this.isDotDot(canonicalize)) {
                throw new IllegalArgumentException("unexpected path segment: " + s);
            }
            return this;
        }
        
        public Builder setEncodedQueryParameter(final String s, @Nullable final String s2) {
            this.removeAllEncodedQueryParameters(s);
            this.addEncodedQueryParameter(s, s2);
            return this;
        }
        
        public Builder setPathSegment(final int n, final String s) {
            if (s == null) {
                throw new NullPointerException("pathSegment == null");
            }
            final String canonicalize = HttpUrl.canonicalize(s, 0, s.length(), " \"<>^`{}|/\\?#", false, false, false, true, null);
            if (this.isDot(canonicalize) || this.isDotDot(canonicalize)) {
                throw new IllegalArgumentException("unexpected path segment: " + s);
            }
            this.encodedPathSegments.set(n, canonicalize);
            return this;
        }
        
        public Builder setQueryParameter(final String s, @Nullable final String s2) {
            this.removeAllQueryParameters(s);
            this.addQueryParameter(s, s2);
            return this;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.scheme);
            sb.append("://");
            if (!this.encodedUsername.isEmpty() || !this.encodedPassword.isEmpty()) {
                sb.append(this.encodedUsername);
                if (!this.encodedPassword.isEmpty()) {
                    sb.append(':');
                    sb.append(this.encodedPassword);
                }
                sb.append('@');
            }
            if (this.host.indexOf(58) != -1) {
                sb.append('[');
                sb.append(this.host);
                sb.append(']');
            }
            else {
                sb.append(this.host);
            }
            final int effectivePort = this.effectivePort();
            if (effectivePort != HttpUrl.defaultPort(this.scheme)) {
                sb.append(':');
                sb.append(effectivePort);
            }
            HttpUrl.pathSegmentsToString(sb, this.encodedPathSegments);
            if (this.encodedQueryNamesAndValues != null) {
                sb.append('?');
                HttpUrl.namesAndValuesToQueryString(sb, this.encodedQueryNamesAndValues);
            }
            if (this.encodedFragment != null) {
                sb.append('#');
                sb.append(this.encodedFragment);
            }
            return sb.toString();
        }
        
        public Builder username(final String s) {
            if (s == null) {
                throw new NullPointerException("username == null");
            }
            this.encodedUsername = HttpUrl.canonicalize(s, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
            return this;
        }
        
        enum ParseResult
        {
            INVALID_HOST, 
            INVALID_PORT, 
            MISSING_SCHEME, 
            SUCCESS, 
            UNSUPPORTED_SCHEME;
        }
    }
}
