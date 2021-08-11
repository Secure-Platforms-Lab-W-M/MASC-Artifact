// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import okhttp3.internal.Util;
import java.util.Arrays;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.util.List;
import okio.ByteString;
import java.security.cert.X509Certificate;
import java.security.cert.Certificate;
import java.util.Set;
import javax.annotation.Nullable;
import okhttp3.internal.tls.CertificateChainCleaner;

public final class CertificatePinner
{
    public static final CertificatePinner DEFAULT;
    @Nullable
    private final CertificateChainCleaner certificateChainCleaner;
    private final Set<Pin> pins;
    
    static {
        DEFAULT = new Builder().build();
    }
    
    CertificatePinner(final Set<Pin> pins, @Nullable final CertificateChainCleaner certificateChainCleaner) {
        this.pins = pins;
        this.certificateChainCleaner = certificateChainCleaner;
    }
    
    public static String pin(final Certificate certificate) {
        if (!(certificate instanceof X509Certificate)) {
            throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
        }
        return "sha256/" + sha256((X509Certificate)certificate).base64();
    }
    
    static ByteString sha1(final X509Certificate x509Certificate) {
        return ByteString.of(x509Certificate.getPublicKey().getEncoded()).sha1();
    }
    
    static ByteString sha256(final X509Certificate x509Certificate) {
        return ByteString.of(x509Certificate.getPublicKey().getEncoded()).sha256();
    }
    
    public void check(final String s, final List<Certificate> list) throws SSLPeerUnverifiedException {
        final List<Pin> matchingPins = this.findMatchingPins(s);
        if (!matchingPins.isEmpty()) {
            Object clean = list;
            if (this.certificateChainCleaner != null) {
                clean = this.certificateChainCleaner.clean(list, s);
            }
            for (int i = 0; i < ((List)clean).size(); ++i) {
                final X509Certificate x509Certificate = ((List<X509Certificate>)clean).get(i);
                ByteString byteString = null;
                ByteString byteString2 = null;
                for (int j = 0; j < matchingPins.size(); ++j) {
                    final Pin pin = matchingPins.get(j);
                    if (pin.hashAlgorithm.equals("sha256/")) {
                        ByteString sha256;
                        if ((sha256 = byteString2) == null) {
                            sha256 = sha256(x509Certificate);
                        }
                        if (pin.hash.equals(sha256)) {
                            return;
                        }
                        byteString2 = sha256;
                    }
                    else {
                        if (!pin.hashAlgorithm.equals("sha1/")) {
                            throw new AssertionError((Object)("unsupported hashAlgorithm: " + pin.hashAlgorithm));
                        }
                        ByteString sha257;
                        if ((sha257 = byteString) == null) {
                            sha257 = sha1(x509Certificate);
                        }
                        byteString = sha257;
                        if (pin.hash.equals(sha257)) {
                            return;
                        }
                    }
                }
            }
            final StringBuilder append = new StringBuilder().append("Certificate pinning failure!").append("\n  Peer certificate chain:");
            for (int k = 0; k < ((List)clean).size(); ++k) {
                final X509Certificate x509Certificate2 = ((List<X509Certificate>)clean).get(k);
                append.append("\n    ").append(pin(x509Certificate2)).append(": ").append(x509Certificate2.getSubjectDN().getName());
            }
            append.append("\n  Pinned certificates for ").append(s).append(":");
            for (int l = 0; l < matchingPins.size(); ++l) {
                append.append("\n    ").append(matchingPins.get(l));
            }
            throw new SSLPeerUnverifiedException(append.toString());
        }
    }
    
    public void check(final String s, final Certificate... array) throws SSLPeerUnverifiedException {
        this.check(s, Arrays.asList(array));
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        return o == this || (o instanceof CertificatePinner && Util.equal(this.certificateChainCleaner, ((CertificatePinner)o).certificateChainCleaner) && this.pins.equals(((CertificatePinner)o).pins));
    }
    
    List<Pin> findMatchingPins(final String s) {
        List<Pin> emptyList = Collections.emptyList();
        for (final Pin pin : this.pins) {
            if (pin.matches(s)) {
                List<Pin> list = emptyList;
                if (emptyList.isEmpty()) {
                    list = new ArrayList<Pin>();
                }
                list.add(pin);
                emptyList = list;
            }
        }
        return emptyList;
    }
    
    @Override
    public int hashCode() {
        int hashCode;
        if (this.certificateChainCleaner != null) {
            hashCode = this.certificateChainCleaner.hashCode();
        }
        else {
            hashCode = 0;
        }
        return hashCode * 31 + this.pins.hashCode();
    }
    
    CertificatePinner withCertificateChainCleaner(@Nullable final CertificateChainCleaner certificateChainCleaner) {
        if (Util.equal(this.certificateChainCleaner, certificateChainCleaner)) {
            return this;
        }
        return new CertificatePinner(this.pins, certificateChainCleaner);
    }
    
    public static final class Builder
    {
        private final List<Pin> pins;
        
        public Builder() {
            this.pins = new ArrayList<Pin>();
        }
        
        public Builder add(final String s, final String... array) {
            if (s == null) {
                throw new NullPointerException("pattern == null");
            }
            for (int length = array.length, i = 0; i < length; ++i) {
                this.pins.add(new Pin(s, array[i]));
            }
            return this;
        }
        
        public CertificatePinner build() {
            return new CertificatePinner(new LinkedHashSet<Pin>((Collection<? extends Pin>)this.pins), null);
        }
    }
    
    static final class Pin
    {
        private static final String WILDCARD = "*.";
        final String canonicalHostname;
        final ByteString hash;
        final String hashAlgorithm;
        final String pattern;
        
        Pin(String s, final String s2) {
            this.pattern = s;
            if (s.startsWith("*.")) {
                s = HttpUrl.parse("http://" + s.substring("*.".length())).host();
            }
            else {
                s = HttpUrl.parse("http://" + s).host();
            }
            this.canonicalHostname = s;
            if (s2.startsWith("sha1/")) {
                this.hashAlgorithm = "sha1/";
                this.hash = ByteString.decodeBase64(s2.substring("sha1/".length()));
            }
            else {
                if (!s2.startsWith("sha256/")) {
                    throw new IllegalArgumentException("pins must start with 'sha256/' or 'sha1/': " + s2);
                }
                this.hashAlgorithm = "sha256/";
                this.hash = ByteString.decodeBase64(s2.substring("sha256/".length()));
            }
            if (this.hash == null) {
                throw new IllegalArgumentException("pins must be base64: " + s2);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Pin && this.pattern.equals(((Pin)o).pattern) && this.hashAlgorithm.equals(((Pin)o).hashAlgorithm) && this.hash.equals(((Pin)o).hash);
        }
        
        @Override
        public int hashCode() {
            return ((this.pattern.hashCode() + 527) * 31 + this.hashAlgorithm.hashCode()) * 31 + this.hash.hashCode();
        }
        
        boolean matches(final String s) {
            final boolean b = false;
            if (this.pattern.startsWith("*.")) {
                final int index = s.indexOf(46);
                boolean b2 = b;
                if (s.length() - index - 1 == this.canonicalHostname.length()) {
                    b2 = b;
                    if (s.regionMatches(false, index + 1, this.canonicalHostname, 0, this.canonicalHostname.length())) {
                        b2 = true;
                    }
                }
                return b2;
            }
            return s.equals(this.canonicalHostname);
        }
        
        @Override
        public String toString() {
            return this.hashAlgorithm + this.hash.base64();
        }
    }
}
