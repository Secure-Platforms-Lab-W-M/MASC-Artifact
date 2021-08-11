// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import java.util.Arrays;
import java.util.List;
import java.util.Comparator;
import okhttp3.internal.Util;
import javax.net.ssl.SSLSocket;
import javax.annotation.Nullable;

public final class ConnectionSpec
{
    private static final CipherSuite[] APPROVED_CIPHER_SUITES;
    public static final ConnectionSpec CLEARTEXT;
    public static final ConnectionSpec COMPATIBLE_TLS;
    public static final ConnectionSpec MODERN_TLS;
    @Nullable
    final String[] cipherSuites;
    final boolean supportsTlsExtensions;
    final boolean tls;
    @Nullable
    final String[] tlsVersions;
    
    static {
        APPROVED_CIPHER_SUITES = new CipherSuite[] { CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA };
        MODERN_TLS = new Builder(true).cipherSuites(ConnectionSpec.APPROVED_CIPHER_SUITES).tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0).supportsTlsExtensions(true).build();
        COMPATIBLE_TLS = new Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_0).supportsTlsExtensions(true).build();
        CLEARTEXT = new Builder(false).build();
    }
    
    ConnectionSpec(final Builder builder) {
        this.tls = builder.tls;
        this.cipherSuites = builder.cipherSuites;
        this.tlsVersions = builder.tlsVersions;
        this.supportsTlsExtensions = builder.supportsTlsExtensions;
    }
    
    private ConnectionSpec supportedSpec(final SSLSocket sslSocket, final boolean b) {
        String[] array;
        if (this.cipherSuites != null) {
            array = Util.intersect(CipherSuite.ORDER_BY_NAME, sslSocket.getEnabledCipherSuites(), this.cipherSuites);
        }
        else {
            array = sslSocket.getEnabledCipherSuites();
        }
        String[] array2;
        if (this.tlsVersions != null) {
            array2 = Util.intersect(Util.NATURAL_ORDER, sslSocket.getEnabledProtocols(), this.tlsVersions);
        }
        else {
            array2 = sslSocket.getEnabledProtocols();
        }
        final String[] supportedCipherSuites = sslSocket.getSupportedCipherSuites();
        final int index = Util.indexOf(CipherSuite.ORDER_BY_NAME, supportedCipherSuites, "TLS_FALLBACK_SCSV");
        String[] concat = array;
        if (b) {
            concat = array;
            if (index != -1) {
                concat = Util.concat(array, supportedCipherSuites[index]);
            }
        }
        return new Builder(this).cipherSuites(concat).tlsVersions(array2).build();
    }
    
    void apply(final SSLSocket sslSocket, final boolean b) {
        final ConnectionSpec supportedSpec = this.supportedSpec(sslSocket, b);
        if (supportedSpec.tlsVersions != null) {
            sslSocket.setEnabledProtocols(supportedSpec.tlsVersions);
        }
        if (supportedSpec.cipherSuites != null) {
            sslSocket.setEnabledCipherSuites(supportedSpec.cipherSuites);
        }
    }
    
    @Nullable
    public List<CipherSuite> cipherSuites() {
        if (this.cipherSuites != null) {
            return CipherSuite.forJavaNames(this.cipherSuites);
        }
        return null;
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        if (o instanceof ConnectionSpec) {
            if (o == this) {
                return true;
            }
            final ConnectionSpec connectionSpec = (ConnectionSpec)o;
            if (this.tls == connectionSpec.tls && (!this.tls || (Arrays.equals(this.cipherSuites, connectionSpec.cipherSuites) && Arrays.equals(this.tlsVersions, connectionSpec.tlsVersions) && this.supportsTlsExtensions == connectionSpec.supportsTlsExtensions))) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int n = 17;
        if (this.tls) {
            final int hashCode = Arrays.hashCode(this.cipherSuites);
            final int hashCode2 = Arrays.hashCode(this.tlsVersions);
            int n2;
            if (this.supportsTlsExtensions) {
                n2 = 0;
            }
            else {
                n2 = 1;
            }
            n = ((hashCode + 527) * 31 + hashCode2) * 31 + n2;
        }
        return n;
    }
    
    public boolean isCompatible(final SSLSocket sslSocket) {
        return this.tls && (this.tlsVersions == null || Util.nonEmptyIntersection(Util.NATURAL_ORDER, this.tlsVersions, sslSocket.getEnabledProtocols())) && (this.cipherSuites == null || Util.nonEmptyIntersection(CipherSuite.ORDER_BY_NAME, this.cipherSuites, sslSocket.getEnabledCipherSuites()));
    }
    
    public boolean isTls() {
        return this.tls;
    }
    
    public boolean supportsTlsExtensions() {
        return this.supportsTlsExtensions;
    }
    
    @Nullable
    public List<TlsVersion> tlsVersions() {
        if (this.tlsVersions != null) {
            return TlsVersion.forJavaNames(this.tlsVersions);
        }
        return null;
    }
    
    @Override
    public String toString() {
        if (!this.tls) {
            return "ConnectionSpec()";
        }
        String string;
        if (this.cipherSuites != null) {
            string = this.cipherSuites().toString();
        }
        else {
            string = "[all enabled]";
        }
        String string2;
        if (this.tlsVersions != null) {
            string2 = this.tlsVersions().toString();
        }
        else {
            string2 = "[all enabled]";
        }
        return "ConnectionSpec(cipherSuites=" + string + ", tlsVersions=" + string2 + ", supportsTlsExtensions=" + this.supportsTlsExtensions + ")";
    }
    
    public static final class Builder
    {
        @Nullable
        String[] cipherSuites;
        boolean supportsTlsExtensions;
        boolean tls;
        @Nullable
        String[] tlsVersions;
        
        public Builder(final ConnectionSpec connectionSpec) {
            this.tls = connectionSpec.tls;
            this.cipherSuites = connectionSpec.cipherSuites;
            this.tlsVersions = connectionSpec.tlsVersions;
            this.supportsTlsExtensions = connectionSpec.supportsTlsExtensions;
        }
        
        Builder(final boolean tls) {
            this.tls = tls;
        }
        
        public Builder allEnabledCipherSuites() {
            if (!this.tls) {
                throw new IllegalStateException("no cipher suites for cleartext connections");
            }
            this.cipherSuites = null;
            return this;
        }
        
        public Builder allEnabledTlsVersions() {
            if (!this.tls) {
                throw new IllegalStateException("no TLS versions for cleartext connections");
            }
            this.tlsVersions = null;
            return this;
        }
        
        public ConnectionSpec build() {
            return new ConnectionSpec(this);
        }
        
        public Builder cipherSuites(final String... array) {
            if (!this.tls) {
                throw new IllegalStateException("no cipher suites for cleartext connections");
            }
            if (array.length == 0) {
                throw new IllegalArgumentException("At least one cipher suite is required");
            }
            this.cipherSuites = array.clone();
            return this;
        }
        
        public Builder cipherSuites(final CipherSuite... array) {
            if (!this.tls) {
                throw new IllegalStateException("no cipher suites for cleartext connections");
            }
            final String[] array2 = new String[array.length];
            for (int i = 0; i < array.length; ++i) {
                array2[i] = array[i].javaName;
            }
            return this.cipherSuites(array2);
        }
        
        public Builder supportsTlsExtensions(final boolean supportsTlsExtensions) {
            if (!this.tls) {
                throw new IllegalStateException("no TLS extensions for cleartext connections");
            }
            this.supportsTlsExtensions = supportsTlsExtensions;
            return this;
        }
        
        public Builder tlsVersions(final String... array) {
            if (!this.tls) {
                throw new IllegalStateException("no TLS versions for cleartext connections");
            }
            if (array.length == 0) {
                throw new IllegalArgumentException("At least one TLS version is required");
            }
            this.tlsVersions = array.clone();
            return this;
        }
        
        public Builder tlsVersions(final TlsVersion... array) {
            if (!this.tls) {
                throw new IllegalStateException("no TLS versions for cleartext connections");
            }
            final String[] array2 = new String[array.length];
            for (int i = 0; i < array.length; ++i) {
                array2[i] = array[i].javaName;
            }
            return this.tlsVersions(array2);
        }
    }
}
