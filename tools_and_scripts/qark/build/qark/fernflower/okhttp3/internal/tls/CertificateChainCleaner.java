package okhttp3.internal.tls;

import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.X509TrustManager;
import okhttp3.internal.platform.Platform;

public abstract class CertificateChainCleaner {
   public static CertificateChainCleaner get(X509TrustManager var0) {
      return Platform.get().buildCertificateChainCleaner(var0);
   }

   public static CertificateChainCleaner get(X509Certificate... var0) {
      return new BasicCertificateChainCleaner(TrustRootIndex.get(var0));
   }

   public abstract List clean(List var1, String var2) throws SSLPeerUnverifiedException;
}
