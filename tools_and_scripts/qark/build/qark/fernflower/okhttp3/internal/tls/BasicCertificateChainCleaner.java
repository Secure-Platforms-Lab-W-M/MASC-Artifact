package okhttp3.internal.tls;

import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;

public final class BasicCertificateChainCleaner extends CertificateChainCleaner {
   private static final int MAX_SIGNERS = 9;
   private final TrustRootIndex trustRootIndex;

   public BasicCertificateChainCleaner(TrustRootIndex var1) {
      this.trustRootIndex = var1;
   }

   private boolean verifySignature(X509Certificate var1, X509Certificate var2) {
      if (!var1.getIssuerDN().equals(var2.getSubjectDN())) {
         return false;
      } else {
         try {
            var1.verify(var2.getPublicKey());
            return true;
         } catch (GeneralSecurityException var3) {
            return false;
         }
      }
   }

   public List clean(List var1, String var2) throws SSLPeerUnverifiedException {
      ArrayDeque var5 = new ArrayDeque(var1);
      ArrayList var8 = new ArrayList();
      var8.add(var5.removeFirst());
      boolean var4 = false;

      for(int var3 = 0; var3 < 9; ++var3) {
         X509Certificate var10 = (X509Certificate)var8.get(var8.size() - 1);
         X509Certificate var6 = this.trustRootIndex.findByIssuerAndSignature(var10);
         if (var6 == null) {
            Iterator var12 = var5.iterator();

            X509Certificate var7;
            do {
               if (!var12.hasNext()) {
                  if (var4) {
                     return var8;
                  }

                  StringBuilder var9 = new StringBuilder();
                  var9.append("Failed to find a trusted cert that signed ");
                  var9.append(var10);
                  throw new SSLPeerUnverifiedException(var9.toString());
               }

               var7 = (X509Certificate)var12.next();
            } while(!this.verifySignature(var10, var7));

            var12.remove();
            var8.add(var7);
         } else {
            if (var8.size() > 1 || !var10.equals(var6)) {
               var8.add(var6);
            }

            if (this.verifySignature(var6, var6)) {
               return var8;
            }

            var4 = true;
         }
      }

      StringBuilder var11 = new StringBuilder();
      var11.append("Certificate chain too long: ");
      var11.append(var8);
      throw new SSLPeerUnverifiedException(var11.toString());
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof BasicCertificateChainCleaner && ((BasicCertificateChainCleaner)var1).trustRootIndex.equals(this.trustRootIndex);
      }
   }

   public int hashCode() {
      return this.trustRootIndex.hashCode();
   }
}
