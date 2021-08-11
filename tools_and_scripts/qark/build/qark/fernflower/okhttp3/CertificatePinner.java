package okhttp3;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;
import okio.ByteString;

public final class CertificatePinner {
   public static final CertificatePinner DEFAULT = (new CertificatePinner.Builder()).build();
   @Nullable
   private final CertificateChainCleaner certificateChainCleaner;
   private final Set pins;

   CertificatePinner(Set var1, @Nullable CertificateChainCleaner var2) {
      this.pins = var1;
      this.certificateChainCleaner = var2;
   }

   public static String pin(Certificate var0) {
      if (var0 instanceof X509Certificate) {
         StringBuilder var1 = new StringBuilder();
         var1.append("sha256/");
         var1.append(sha256((X509Certificate)var0).base64());
         return var1.toString();
      } else {
         throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
      }
   }

   static ByteString sha1(X509Certificate var0) {
      return ByteString.method_6(var0.getPublicKey().getEncoded()).sha1();
   }

   static ByteString sha256(X509Certificate var0) {
      return ByteString.method_6(var0.getPublicKey().getEncoded()).sha256();
   }

   public void check(String var1, List var2) throws SSLPeerUnverifiedException {
      List var10 = this.findMatchingPins(var1);
      if (!var10.isEmpty()) {
         CertificateChainCleaner var7 = this.certificateChainCleaner;
         List var9 = var2;
         if (var7 != null) {
            var9 = var7.clean(var2, var1);
         }

         int var3 = 0;

         int var4;
         for(int var5 = var9.size(); var3 < var5; ++var3) {
            X509Certificate var11 = (X509Certificate)var9.get(var3);
            ByteString var16 = null;
            ByteString var14 = null;
            var4 = 0;

            for(int var6 = var10.size(); var4 < var6; ++var4) {
               CertificatePinner.Pin var12 = (CertificatePinner.Pin)var10.get(var4);
               ByteString var8;
               if (var12.hashAlgorithm.equals("sha256/")) {
                  var8 = var14;
                  if (var14 == null) {
                     var8 = sha256(var11);
                  }

                  var14 = var8;
                  if (var12.hash.equals(var8)) {
                     return;
                  }
               } else {
                  if (!var12.hashAlgorithm.equals("sha1/")) {
                     throw new AssertionError();
                  }

                  var8 = var16;
                  if (var16 == null) {
                     var8 = sha1(var11);
                  }

                  var16 = var8;
                  if (var12.hash.equals(var8)) {
                     return;
                  }
               }
            }
         }

         StringBuilder var15 = new StringBuilder();
         var15.append("Certificate pinning failure!");
         var15 = var15.append("\n  Peer certificate chain:");
         var3 = 0;

         for(var4 = var9.size(); var3 < var4; ++var3) {
            X509Certificate var17 = (X509Certificate)var9.get(var3);
            var15.append("\n    ");
            var15.append(pin(var17));
            var15.append(": ");
            var15.append(var17.getSubjectDN().getName());
         }

         var15.append("\n  Pinned certificates for ");
         var15.append(var1);
         var15.append(":");
         var3 = 0;

         for(var4 = var10.size(); var3 < var4; ++var3) {
            CertificatePinner.Pin var13 = (CertificatePinner.Pin)var10.get(var3);
            var15.append("\n    ");
            var15.append(var13);
         }

         throw new SSLPeerUnverifiedException(var15.toString());
      }
   }

   public void check(String var1, Certificate... var2) throws SSLPeerUnverifiedException {
      this.check(var1, Arrays.asList(var2));
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof CertificatePinner && Util.equal(this.certificateChainCleaner, ((CertificatePinner)var1).certificateChainCleaner) && this.pins.equals(((CertificatePinner)var1).pins);
      }
   }

   List findMatchingPins(String var1) {
      Object var2 = Collections.emptyList();

      Object var3;
      for(Iterator var4 = this.pins.iterator(); var4.hasNext(); var2 = var3) {
         CertificatePinner.Pin var5 = (CertificatePinner.Pin)var4.next();
         var3 = var2;
         if (var5.matches(var1)) {
            var3 = var2;
            if (((List)var2).isEmpty()) {
               var3 = new ArrayList();
            }

            ((List)var3).add(var5);
         }
      }

      return (List)var2;
   }

   public int hashCode() {
      CertificateChainCleaner var2 = this.certificateChainCleaner;
      int var1;
      if (var2 != null) {
         var1 = var2.hashCode();
      } else {
         var1 = 0;
      }

      return var1 * 31 + this.pins.hashCode();
   }

   CertificatePinner withCertificateChainCleaner(CertificateChainCleaner var1) {
      return Util.equal(this.certificateChainCleaner, var1) ? this : new CertificatePinner(this.pins, var1);
   }

   public static final class Builder {
      private final List pins = new ArrayList();

      public CertificatePinner.Builder add(String var1, String... var2) {
         if (var1 == null) {
            throw new NullPointerException("pattern == null");
         } else {
            int var4 = var2.length;

            for(int var3 = 0; var3 < var4; ++var3) {
               String var5 = var2[var3];
               this.pins.add(new CertificatePinner.Pin(var1, var5));
            }

            return this;
         }
      }

      public CertificatePinner build() {
         return new CertificatePinner(new LinkedHashSet(this.pins), (CertificateChainCleaner)null);
      }
   }

   static final class Pin {
      private static final String WILDCARD = "*.";
      final String canonicalHostname;
      final ByteString hash;
      final String hashAlgorithm;
      final String pattern;

      Pin(String var1, String var2) {
         this.pattern = var1;
         StringBuilder var3;
         if (var1.startsWith("*.")) {
            var3 = new StringBuilder();
            var3.append("http://");
            var3.append(var1.substring("*.".length()));
            var1 = HttpUrl.parse(var3.toString()).host();
         } else {
            var3 = new StringBuilder();
            var3.append("http://");
            var3.append(var1);
            var1 = HttpUrl.parse(var3.toString()).host();
         }

         this.canonicalHostname = var1;
         StringBuilder var4;
         if (var2.startsWith("sha1/")) {
            this.hashAlgorithm = "sha1/";
            this.hash = ByteString.decodeBase64(var2.substring("sha1/".length()));
         } else {
            if (!var2.startsWith("sha256/")) {
               var4 = new StringBuilder();
               var4.append("pins must start with 'sha256/' or 'sha1/': ");
               var4.append(var2);
               throw new IllegalArgumentException(var4.toString());
            }

            this.hashAlgorithm = "sha256/";
            this.hash = ByteString.decodeBase64(var2.substring("sha256/".length()));
         }

         if (this.hash == null) {
            var4 = new StringBuilder();
            var4.append("pins must be base64: ");
            var4.append(var2);
            throw new IllegalArgumentException(var4.toString());
         }
      }

      public boolean equals(Object var1) {
         return var1 instanceof CertificatePinner.Pin && this.pattern.equals(((CertificatePinner.Pin)var1).pattern) && this.hashAlgorithm.equals(((CertificatePinner.Pin)var1).hashAlgorithm) && this.hash.equals(((CertificatePinner.Pin)var1).hash);
      }

      public int hashCode() {
         return ((17 * 31 + this.pattern.hashCode()) * 31 + this.hashAlgorithm.hashCode()) * 31 + this.hash.hashCode();
      }

      boolean matches(String var1) {
         if (this.pattern.startsWith("*.")) {
            int var2 = var1.indexOf(46);
            String var3 = this.canonicalHostname;
            return var1.regionMatches(false, var2 + 1, var3, 0, var3.length());
         } else {
            return var1.equals(this.canonicalHostname);
         }
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.hashAlgorithm);
         var1.append(this.hash.base64());
         return var1.toString();
      }
   }
}
