package okhttp3;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.ssl.SSLSocket;
import okhttp3.internal.Util;

public final class ConnectionSpec {
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
      APPROVED_CIPHER_SUITES = new CipherSuite[]{CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA};
      ConnectionSpec var0 = (new ConnectionSpec.Builder(true)).cipherSuites(APPROVED_CIPHER_SUITES).tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0).supportsTlsExtensions(true).build();
      MODERN_TLS = var0;
      COMPATIBLE_TLS = (new ConnectionSpec.Builder(var0)).tlsVersions(TlsVersion.TLS_1_0).supportsTlsExtensions(true).build();
      CLEARTEXT = (new ConnectionSpec.Builder(false)).build();
   }

   ConnectionSpec(ConnectionSpec.Builder var1) {
      this.tls = var1.tls;
      this.cipherSuites = var1.cipherSuites;
      this.tlsVersions = var1.tlsVersions;
      this.supportsTlsExtensions = var1.supportsTlsExtensions;
   }

   private ConnectionSpec supportedSpec(SSLSocket var1, boolean var2) {
      String[] var4;
      if (this.cipherSuites != null) {
         var4 = Util.intersect(CipherSuite.ORDER_BY_NAME, var1.getEnabledCipherSuites(), this.cipherSuites);
      } else {
         var4 = var1.getEnabledCipherSuites();
      }

      String[] var5;
      if (this.tlsVersions != null) {
         var5 = Util.intersect(Util.NATURAL_ORDER, var1.getEnabledProtocols(), this.tlsVersions);
      } else {
         var5 = var1.getEnabledProtocols();
      }

      String[] var6 = var1.getSupportedCipherSuites();
      int var3 = Util.indexOf(CipherSuite.ORDER_BY_NAME, var6, "TLS_FALLBACK_SCSV");
      String[] var7 = var4;
      if (var2) {
         var7 = var4;
         if (var3 != -1) {
            var7 = Util.concat(var4, var6[var3]);
         }
      }

      return (new ConnectionSpec.Builder(this)).cipherSuites(var7).tlsVersions(var5).build();
   }

   void apply(SSLSocket var1, boolean var2) {
      ConnectionSpec var3 = this.supportedSpec(var1, var2);
      String[] var4 = var3.tlsVersions;
      if (var4 != null) {
         var1.setEnabledProtocols(var4);
      }

      String[] var5 = var3.cipherSuites;
      if (var5 != null) {
         var1.setEnabledCipherSuites(var5);
      }

   }

   @Nullable
   public List cipherSuites() {
      String[] var1 = this.cipherSuites;
      return var1 != null ? CipherSuite.forJavaNames(var1) : null;
   }

   public boolean equals(@Nullable Object var1) {
      if (!(var1 instanceof ConnectionSpec)) {
         return false;
      } else if (var1 == this) {
         return true;
      } else {
         ConnectionSpec var3 = (ConnectionSpec)var1;
         boolean var2 = this.tls;
         if (var2 != var3.tls) {
            return false;
         } else {
            if (var2) {
               if (!Arrays.equals(this.cipherSuites, var3.cipherSuites)) {
                  return false;
               }

               if (!Arrays.equals(this.tlsVersions, var3.tlsVersions)) {
                  return false;
               }

               if (this.supportsTlsExtensions != var3.supportsTlsExtensions) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int var1 = 17;
      if (this.tls) {
         var1 = ((17 * 31 + Arrays.hashCode(this.cipherSuites)) * 31 + Arrays.hashCode(this.tlsVersions)) * 31 + (this.supportsTlsExtensions ^ 1);
      }

      return var1;
   }

   public boolean isCompatible(SSLSocket var1) {
      if (!this.tls) {
         return false;
      } else if (this.tlsVersions != null && !Util.nonEmptyIntersection(Util.NATURAL_ORDER, this.tlsVersions, var1.getEnabledProtocols())) {
         return false;
      } else {
         return this.cipherSuites == null || Util.nonEmptyIntersection(CipherSuite.ORDER_BY_NAME, this.cipherSuites, var1.getEnabledCipherSuites());
      }
   }

   public boolean isTls() {
      return this.tls;
   }

   public boolean supportsTlsExtensions() {
      return this.supportsTlsExtensions;
   }

   @Nullable
   public List tlsVersions() {
      String[] var1 = this.tlsVersions;
      return var1 != null ? TlsVersion.forJavaNames(var1) : null;
   }

   public String toString() {
      if (!this.tls) {
         return "ConnectionSpec()";
      } else {
         String[] var1 = this.cipherSuites;
         String var2 = "[all enabled]";
         String var4;
         if (var1 != null) {
            var4 = this.cipherSuites().toString();
         } else {
            var4 = "[all enabled]";
         }

         if (this.tlsVersions != null) {
            var2 = this.tlsVersions().toString();
         }

         StringBuilder var3 = new StringBuilder();
         var3.append("ConnectionSpec(cipherSuites=");
         var3.append(var4);
         var3.append(", tlsVersions=");
         var3.append(var2);
         var3.append(", supportsTlsExtensions=");
         var3.append(this.supportsTlsExtensions);
         var3.append(")");
         return var3.toString();
      }
   }

   public static final class Builder {
      @Nullable
      String[] cipherSuites;
      boolean supportsTlsExtensions;
      boolean tls;
      @Nullable
      String[] tlsVersions;

      public Builder(ConnectionSpec var1) {
         this.tls = var1.tls;
         this.cipherSuites = var1.cipherSuites;
         this.tlsVersions = var1.tlsVersions;
         this.supportsTlsExtensions = var1.supportsTlsExtensions;
      }

      Builder(boolean var1) {
         this.tls = var1;
      }

      public ConnectionSpec.Builder allEnabledCipherSuites() {
         if (this.tls) {
            this.cipherSuites = null;
            return this;
         } else {
            throw new IllegalStateException("no cipher suites for cleartext connections");
         }
      }

      public ConnectionSpec.Builder allEnabledTlsVersions() {
         if (this.tls) {
            this.tlsVersions = null;
            return this;
         } else {
            throw new IllegalStateException("no TLS versions for cleartext connections");
         }
      }

      public ConnectionSpec build() {
         return new ConnectionSpec(this);
      }

      public ConnectionSpec.Builder cipherSuites(String... var1) {
         if (this.tls) {
            if (var1.length != 0) {
               this.cipherSuites = (String[])var1.clone();
               return this;
            } else {
               throw new IllegalArgumentException("At least one cipher suite is required");
            }
         } else {
            throw new IllegalStateException("no cipher suites for cleartext connections");
         }
      }

      public ConnectionSpec.Builder cipherSuites(CipherSuite... var1) {
         if (!this.tls) {
            throw new IllegalStateException("no cipher suites for cleartext connections");
         } else {
            String[] var3 = new String[var1.length];

            for(int var2 = 0; var2 < var1.length; ++var2) {
               var3[var2] = var1[var2].javaName;
            }

            return this.cipherSuites(var3);
         }
      }

      public ConnectionSpec.Builder supportsTlsExtensions(boolean var1) {
         if (this.tls) {
            this.supportsTlsExtensions = var1;
            return this;
         } else {
            throw new IllegalStateException("no TLS extensions for cleartext connections");
         }
      }

      public ConnectionSpec.Builder tlsVersions(String... var1) {
         if (this.tls) {
            if (var1.length != 0) {
               this.tlsVersions = (String[])var1.clone();
               return this;
            } else {
               throw new IllegalArgumentException("At least one TLS version is required");
            }
         } else {
            throw new IllegalStateException("no TLS versions for cleartext connections");
         }
      }

      public ConnectionSpec.Builder tlsVersions(TlsVersion... var1) {
         if (!this.tls) {
            throw new IllegalStateException("no TLS versions for cleartext connections");
         } else {
            String[] var3 = new String[var1.length];

            for(int var2 = 0; var2 < var1.length; ++var2) {
               var3[var2] = var1[var2].javaName;
            }

            return this.tlsVersions(var3);
         }
      }
   }
}
