// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import okhttp3.internal.http.HttpHeaders;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public final class CacheControl
{
    public static final CacheControl FORCE_CACHE;
    public static final CacheControl FORCE_NETWORK;
    @Nullable
    String headerValue;
    private final boolean immutable;
    private final boolean isPrivate;
    private final boolean isPublic;
    private final int maxAgeSeconds;
    private final int maxStaleSeconds;
    private final int minFreshSeconds;
    private final boolean mustRevalidate;
    private final boolean noCache;
    private final boolean noStore;
    private final boolean noTransform;
    private final boolean onlyIfCached;
    private final int sMaxAgeSeconds;
    
    static {
        FORCE_NETWORK = new Builder().noCache().build();
        FORCE_CACHE = new Builder().onlyIfCached().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS).build();
    }
    
    CacheControl(final Builder builder) {
        this.noCache = builder.noCache;
        this.noStore = builder.noStore;
        this.maxAgeSeconds = builder.maxAgeSeconds;
        this.sMaxAgeSeconds = -1;
        this.isPrivate = false;
        this.isPublic = false;
        this.mustRevalidate = false;
        this.maxStaleSeconds = builder.maxStaleSeconds;
        this.minFreshSeconds = builder.minFreshSeconds;
        this.onlyIfCached = builder.onlyIfCached;
        this.noTransform = builder.noTransform;
        this.immutable = builder.immutable;
    }
    
    private CacheControl(final boolean noCache, final boolean noStore, final int maxAgeSeconds, final int sMaxAgeSeconds, final boolean isPrivate, final boolean isPublic, final boolean mustRevalidate, final int maxStaleSeconds, final int minFreshSeconds, final boolean onlyIfCached, final boolean noTransform, final boolean immutable, @Nullable final String headerValue) {
        this.noCache = noCache;
        this.noStore = noStore;
        this.maxAgeSeconds = maxAgeSeconds;
        this.sMaxAgeSeconds = sMaxAgeSeconds;
        this.isPrivate = isPrivate;
        this.isPublic = isPublic;
        this.mustRevalidate = mustRevalidate;
        this.maxStaleSeconds = maxStaleSeconds;
        this.minFreshSeconds = minFreshSeconds;
        this.onlyIfCached = onlyIfCached;
        this.noTransform = noTransform;
        this.immutable = immutable;
        this.headerValue = headerValue;
    }
    
    private String headerValue() {
        final StringBuilder sb = new StringBuilder();
        if (this.noCache) {
            sb.append("no-cache, ");
        }
        if (this.noStore) {
            sb.append("no-store, ");
        }
        if (this.maxAgeSeconds != -1) {
            sb.append("max-age=").append(this.maxAgeSeconds).append(", ");
        }
        if (this.sMaxAgeSeconds != -1) {
            sb.append("s-maxage=").append(this.sMaxAgeSeconds).append(", ");
        }
        if (this.isPrivate) {
            sb.append("private, ");
        }
        if (this.isPublic) {
            sb.append("public, ");
        }
        if (this.mustRevalidate) {
            sb.append("must-revalidate, ");
        }
        if (this.maxStaleSeconds != -1) {
            sb.append("max-stale=").append(this.maxStaleSeconds).append(", ");
        }
        if (this.minFreshSeconds != -1) {
            sb.append("min-fresh=").append(this.minFreshSeconds).append(", ");
        }
        if (this.onlyIfCached) {
            sb.append("only-if-cached, ");
        }
        if (this.noTransform) {
            sb.append("no-transform, ");
        }
        if (this.immutable) {
            sb.append("immutable, ");
        }
        if (sb.length() == 0) {
            return "";
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
    
    public static CacheControl parse(final Headers headers) {
        boolean b = false;
        boolean b2 = false;
        int seconds = -1;
        int seconds2 = -1;
        boolean b3 = false;
        boolean b4 = false;
        boolean b5 = false;
        int seconds3 = -1;
        int seconds4 = -1;
        boolean b6 = false;
        boolean b7 = false;
        boolean b8 = false;
        int n = 1;
        String s = null;
        boolean b9;
        boolean b10;
        int n2;
        int n3;
        boolean b11;
        boolean b12;
        boolean b13;
        int n4;
        int n5;
        boolean b14;
        boolean b15;
        boolean b16;
        String s2;
        int n6;
        for (int i = 0; i < headers.size(); ++i, b = b9, b2 = b10, seconds = n2, seconds2 = n3, b3 = b11, b4 = b12, b5 = b13, seconds3 = n4, seconds4 = n5, b6 = b14, b7 = b15, b8 = b16, s = s2, n = n6) {
            final String name = headers.name(i);
            final String value = headers.value(i);
            if (name.equalsIgnoreCase("Cache-Control")) {
                if (s != null) {
                    n = 0;
                }
                else {
                    s = value;
                }
            }
            else {
                b9 = b;
                b10 = b2;
                n2 = seconds;
                n3 = seconds2;
                b11 = b3;
                b12 = b4;
                b13 = b5;
                n4 = seconds3;
                n5 = seconds4;
                b14 = b6;
                b15 = b7;
                b16 = b8;
                s2 = s;
                n6 = n;
                if (!name.equalsIgnoreCase("Pragma")) {
                    continue;
                }
                n = 0;
            }
            int n7 = 0;
            while (true) {
                b9 = b;
                b10 = b2;
                n2 = seconds;
                n3 = seconds2;
                b11 = b3;
                b12 = b4;
                b13 = b5;
                n4 = seconds3;
                n5 = seconds4;
                b14 = b6;
                b15 = b7;
                b16 = b8;
                s2 = s;
                n6 = n;
                if (n7 >= value.length()) {
                    break;
                }
                final int skipUntil = HttpHeaders.skipUntil(value, n7, "=,;");
                final String trim = value.substring(n7, skipUntil).trim();
                int skipUntil2;
                String s3;
                if (skipUntil == value.length() || value.charAt(skipUntil) == ',' || value.charAt(skipUntil) == ';') {
                    skipUntil2 = skipUntil + 1;
                    s3 = null;
                }
                else {
                    final int skipWhitespace = HttpHeaders.skipWhitespace(value, skipUntil + 1);
                    if (skipWhitespace < value.length() && value.charAt(skipWhitespace) == '\"') {
                        final int n8 = skipWhitespace + 1;
                        final int skipUntil3 = HttpHeaders.skipUntil(value, n8, "\"");
                        s3 = value.substring(n8, skipUntil3);
                        skipUntil2 = skipUntil3 + 1;
                    }
                    else {
                        skipUntil2 = HttpHeaders.skipUntil(value, skipWhitespace, ",;");
                        s3 = value.substring(skipWhitespace, skipUntil2).trim();
                    }
                }
                if ("no-cache".equalsIgnoreCase(trim)) {
                    b = true;
                    n7 = skipUntil2;
                }
                else if ("no-store".equalsIgnoreCase(trim)) {
                    b2 = true;
                    n7 = skipUntil2;
                }
                else if ("max-age".equalsIgnoreCase(trim)) {
                    seconds = HttpHeaders.parseSeconds(s3, -1);
                    n7 = skipUntil2;
                }
                else if ("s-maxage".equalsIgnoreCase(trim)) {
                    seconds2 = HttpHeaders.parseSeconds(s3, -1);
                    n7 = skipUntil2;
                }
                else if ("private".equalsIgnoreCase(trim)) {
                    b3 = true;
                    n7 = skipUntil2;
                }
                else if ("public".equalsIgnoreCase(trim)) {
                    b4 = true;
                    n7 = skipUntil2;
                }
                else if ("must-revalidate".equalsIgnoreCase(trim)) {
                    b5 = true;
                    n7 = skipUntil2;
                }
                else if ("max-stale".equalsIgnoreCase(trim)) {
                    seconds3 = HttpHeaders.parseSeconds(s3, Integer.MAX_VALUE);
                    n7 = skipUntil2;
                }
                else if ("min-fresh".equalsIgnoreCase(trim)) {
                    seconds4 = HttpHeaders.parseSeconds(s3, -1);
                    n7 = skipUntil2;
                }
                else if ("only-if-cached".equalsIgnoreCase(trim)) {
                    b6 = true;
                    n7 = skipUntil2;
                }
                else if ("no-transform".equalsIgnoreCase(trim)) {
                    b7 = true;
                    n7 = skipUntil2;
                }
                else {
                    n7 = skipUntil2;
                    if (!"immutable".equalsIgnoreCase(trim)) {
                        continue;
                    }
                    b8 = true;
                    n7 = skipUntil2;
                }
            }
        }
        if (n == 0) {
            s = null;
        }
        return new CacheControl(b, b2, seconds, seconds2, b3, b4, b5, seconds3, seconds4, b6, b7, b8, s);
    }
    
    public boolean immutable() {
        return this.immutable;
    }
    
    public boolean isPrivate() {
        return this.isPrivate;
    }
    
    public boolean isPublic() {
        return this.isPublic;
    }
    
    public int maxAgeSeconds() {
        return this.maxAgeSeconds;
    }
    
    public int maxStaleSeconds() {
        return this.maxStaleSeconds;
    }
    
    public int minFreshSeconds() {
        return this.minFreshSeconds;
    }
    
    public boolean mustRevalidate() {
        return this.mustRevalidate;
    }
    
    public boolean noCache() {
        return this.noCache;
    }
    
    public boolean noStore() {
        return this.noStore;
    }
    
    public boolean noTransform() {
        return this.noTransform;
    }
    
    public boolean onlyIfCached() {
        return this.onlyIfCached;
    }
    
    public int sMaxAgeSeconds() {
        return this.sMaxAgeSeconds;
    }
    
    @Override
    public String toString() {
        final String headerValue = this.headerValue;
        if (headerValue != null) {
            return headerValue;
        }
        return this.headerValue = this.headerValue();
    }
    
    public static final class Builder
    {
        boolean immutable;
        int maxAgeSeconds;
        int maxStaleSeconds;
        int minFreshSeconds;
        boolean noCache;
        boolean noStore;
        boolean noTransform;
        boolean onlyIfCached;
        
        public Builder() {
            this.maxAgeSeconds = -1;
            this.maxStaleSeconds = -1;
            this.minFreshSeconds = -1;
        }
        
        public CacheControl build() {
            return new CacheControl(this);
        }
        
        public Builder immutable() {
            this.immutable = true;
            return this;
        }
        
        public Builder maxAge(int maxAgeSeconds, final TimeUnit timeUnit) {
            if (maxAgeSeconds < 0) {
                throw new IllegalArgumentException("maxAge < 0: " + maxAgeSeconds);
            }
            final long seconds = timeUnit.toSeconds(maxAgeSeconds);
            if (seconds > 2147483647L) {
                maxAgeSeconds = Integer.MAX_VALUE;
            }
            else {
                maxAgeSeconds = (int)seconds;
            }
            this.maxAgeSeconds = maxAgeSeconds;
            return this;
        }
        
        public Builder maxStale(int maxStaleSeconds, final TimeUnit timeUnit) {
            if (maxStaleSeconds < 0) {
                throw new IllegalArgumentException("maxStale < 0: " + maxStaleSeconds);
            }
            final long seconds = timeUnit.toSeconds(maxStaleSeconds);
            if (seconds > 2147483647L) {
                maxStaleSeconds = Integer.MAX_VALUE;
            }
            else {
                maxStaleSeconds = (int)seconds;
            }
            this.maxStaleSeconds = maxStaleSeconds;
            return this;
        }
        
        public Builder minFresh(int minFreshSeconds, final TimeUnit timeUnit) {
            if (minFreshSeconds < 0) {
                throw new IllegalArgumentException("minFresh < 0: " + minFreshSeconds);
            }
            final long seconds = timeUnit.toSeconds(minFreshSeconds);
            if (seconds > 2147483647L) {
                minFreshSeconds = Integer.MAX_VALUE;
            }
            else {
                minFreshSeconds = (int)seconds;
            }
            this.minFreshSeconds = minFreshSeconds;
            return this;
        }
        
        public Builder noCache() {
            this.noCache = true;
            return this;
        }
        
        public Builder noStore() {
            this.noStore = true;
            return this;
        }
        
        public Builder noTransform() {
            this.noTransform = true;
            return this;
        }
        
        public Builder onlyIfCached() {
            this.onlyIfCached = true;
            return this;
        }
    }
}
