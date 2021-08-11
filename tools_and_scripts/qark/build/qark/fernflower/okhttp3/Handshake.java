package okhttp3;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import okhttp3.internal.Util;

public final class Handshake {
   private final CipherSuite cipherSuite;
   private final List localCertificates;
   private final List peerCertificates;
   private final TlsVersion tlsVersion;

   private Handshake(TlsVersion var1, CipherSuite var2, List var3, List var4) {
      this.tlsVersion = var1;
      this.cipherSuite = var2;
      this.peerCertificates = var3;
      this.localCertificates = var4;
   }

   public static Handshake get(SSLSession var0) {
      String var1 = var0.getCipherSuite();
      if (var1 != null) {
         CipherSuite var2 = CipherSuite.forJavaName(var1);
         var1 = var0.getProtocol();
         if (var1 != null) {
            TlsVersion var3 = TlsVersion.forJavaName(var1);

            Certificate[] var7;
            try {
               var7 = var0.getPeerCertificates();
            } catch (SSLPeerUnverifiedException var4) {
               var7 = null;
            }

            List var8;
            if (var7 != null) {
               var8 = Util.immutableList((Object[])var7);
            } else {
               var8 = Collections.emptyList();
            }

            Certificate[] var5 = var0.getLocalCertificates();
            List var6;
            if (var5 != null) {
               var6 = Util.immutableList((Object[])var5);
            } else {
               var6 = Collections.emptyList();
            }

            return new Handshake(var3, var2, var8, var6);
         } else {
            throw new IllegalStateException("tlsVersion == null");
         }
      } else {
         throw new IllegalStateException("cipherSuite == null");
      }
   }

   public static Handshake get(TlsVersion var0, CipherSuite var1, List var2, List var3) {
      if (var0 != null) {
         if (var1 != null) {
            return new Handshake(var0, var1, Util.immutableList(var2), Util.immutableList(var3));
         } else {
            throw new NullPointerException("cipherSuite == null");
         }
      } else {
         throw new NullPointerException("tlsVersion == null");
      }
   }

   public CipherSuite cipherSuite() {
      return this.cipherSuite;
   }

   public boolean equals(@Nullable Object var1) {
      if (!(var1 instanceof Handshake)) {
         return false;
      } else {
         Handshake var2 = (Handshake)var1;
         return this.tlsVersion.equals(var2.tlsVersion) && this.cipherSuite.equals(var2.cipherSuite) && this.peerCertificates.equals(var2.peerCertificates) && this.localCertificates.equals(var2.localCertificates);
      }
   }

   public int hashCode() {
      return (((17 * 31 + this.tlsVersion.hashCode()) * 31 + this.cipherSuite.hashCode()) * 31 + this.peerCertificates.hashCode()) * 31 + this.localCertificates.hashCode();
   }

   public List localCertificates() {
      return this.localCertificates;
   }

   @Nullable
   public Principal localPrincipal() {
      return !this.localCertificates.isEmpty() ? ((X509Certificate)this.localCertificates.get(0)).getSubjectX500Principal() : null;
   }

   public List peerCertificates() {
      return this.peerCertificates;
   }

   @Nullable
   public Principal peerPrincipal() {
      return !this.peerCertificates.isEmpty() ? ((X509Certificate)this.peerCertificates.get(0)).getSubjectX500Principal() : null;
   }

   public TlsVersion tlsVersion() {
      return this.tlsVersion;
   }
}
