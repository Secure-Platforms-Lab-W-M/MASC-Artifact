// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import javax.annotation.Nullable;
import okhttp3.internal.Util;
import java.nio.charset.Charset;

public final class Challenge
{
    private final Charset charset;
    private final String realm;
    private final String scheme;
    
    public Challenge(final String s, final String s2) {
        this(s, s2, Util.ISO_8859_1);
    }
    
    private Challenge(final String scheme, final String realm, final Charset charset) {
        if (scheme == null) {
            throw new NullPointerException("scheme == null");
        }
        if (realm == null) {
            throw new NullPointerException("realm == null");
        }
        if (charset == null) {
            throw new NullPointerException("charset == null");
        }
        this.scheme = scheme;
        this.realm = realm;
        this.charset = charset;
    }
    
    public Charset charset() {
        return this.charset;
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        return o instanceof Challenge && ((Challenge)o).scheme.equals(this.scheme) && ((Challenge)o).realm.equals(this.realm) && ((Challenge)o).charset.equals(this.charset);
    }
    
    @Override
    public int hashCode() {
        return ((this.realm.hashCode() + 899) * 31 + this.scheme.hashCode()) * 31 + this.charset.hashCode();
    }
    
    public String realm() {
        return this.realm;
    }
    
    public String scheme() {
        return this.scheme;
    }
    
    @Override
    public String toString() {
        return this.scheme + " realm=\"" + this.realm + "\" charset=\"" + this.charset + "\"";
    }
    
    public Challenge withCharset(final Charset charset) {
        return new Challenge(this.scheme, this.realm, charset);
    }
}
