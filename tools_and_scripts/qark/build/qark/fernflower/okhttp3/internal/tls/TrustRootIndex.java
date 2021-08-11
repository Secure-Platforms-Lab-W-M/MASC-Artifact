package okhttp3.internal.tls;

import java.lang.reflect.Method;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

public abstract class TrustRootIndex {
   public static TrustRootIndex get(X509TrustManager var0) {
      try {
         Method var1 = var0.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", X509Certificate.class);
         var1.setAccessible(true);
         TrustRootIndex.AndroidTrustRootIndex var3 = new TrustRootIndex.AndroidTrustRootIndex(var0, var1);
         return var3;
      } catch (NoSuchMethodException var2) {
         return get(var0.getAcceptedIssuers());
      }
   }

   public static TrustRootIndex get(X509Certificate... var0) {
      return new TrustRootIndex.BasicTrustRootIndex(var0);
   }

   public abstract X509Certificate findByIssuerAndSignature(X509Certificate var1);

   static final class AndroidTrustRootIndex extends TrustRootIndex {
      private final Method findByIssuerAndSignatureMethod;
      private final X509TrustManager trustManager;

      AndroidTrustRootIndex(X509TrustManager var1, Method var2) {
         this.findByIssuerAndSignatureMethod = var2;
         this.trustManager = var1;
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof TrustRootIndex.AndroidTrustRootIndex)) {
            return false;
         } else {
            TrustRootIndex.AndroidTrustRootIndex var2 = (TrustRootIndex.AndroidTrustRootIndex)var1;
            return this.trustManager.equals(var2.trustManager) && this.findByIssuerAndSignatureMethod.equals(var2.findByIssuerAndSignatureMethod);
         }
      }

      public X509Certificate findByIssuerAndSignature(X509Certificate param1) {
         // $FF: Couldn't be decompiled
      }

      public int hashCode() {
         return this.trustManager.hashCode() + this.findByIssuerAndSignatureMethod.hashCode() * 31;
      }
   }

   static final class BasicTrustRootIndex extends TrustRootIndex {
      private final Map subjectToCaCerts = new LinkedHashMap();

      BasicTrustRootIndex(X509Certificate... var1) {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            X509Certificate var6 = var1[var2];
            X500Principal var7 = var6.getSubjectX500Principal();
            Set var5 = (Set)this.subjectToCaCerts.get(var7);
            Object var4 = var5;
            if (var5 == null) {
               var4 = new LinkedHashSet(1);
               this.subjectToCaCerts.put(var7, var4);
            }

            ((Set)var4).add(var6);
         }

      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            return var1 instanceof TrustRootIndex.BasicTrustRootIndex && ((TrustRootIndex.BasicTrustRootIndex)var1).subjectToCaCerts.equals(this.subjectToCaCerts);
         }
      }

      public X509Certificate findByIssuerAndSignature(X509Certificate var1) {
         X500Principal var2 = var1.getIssuerX500Principal();
         Set var6 = (Set)this.subjectToCaCerts.get(var2);
         if (var6 == null) {
            return null;
         } else {
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               X509Certificate var3 = (X509Certificate)var7.next();
               PublicKey var4 = var3.getPublicKey();

               try {
                  var1.verify(var4);
                  return var3;
               } catch (Exception var5) {
               }
            }

            return null;
         }
      }

      public int hashCode() {
         return this.subjectToCaCerts.hashCode();
      }
   }
}
