package okhttp3.internal.connection;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.UnknownServiceException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLProtocolException;
import javax.net.ssl.SSLSocket;
import okhttp3.ConnectionSpec;
import okhttp3.internal.Internal;

public final class ConnectionSpecSelector {
   private final List connectionSpecs;
   private boolean isFallback;
   private boolean isFallbackPossible;
   private int nextModeIndex = 0;

   public ConnectionSpecSelector(List var1) {
      this.connectionSpecs = var1;
   }

   private boolean isFallbackPossible(SSLSocket var1) {
      for(int var2 = this.nextModeIndex; var2 < this.connectionSpecs.size(); ++var2) {
         if (((ConnectionSpec)this.connectionSpecs.get(var2)).isCompatible(var1)) {
            return true;
         }
      }

      return false;
   }

   public ConnectionSpec configureSecureSocket(SSLSocket var1) throws IOException {
      Object var5 = null;
      int var2 = this.nextModeIndex;
      int var3 = this.connectionSpecs.size();

      ConnectionSpec var4;
      while(true) {
         var4 = (ConnectionSpec)var5;
         if (var2 >= var3) {
            break;
         }

         var4 = (ConnectionSpec)this.connectionSpecs.get(var2);
         if (var4.isCompatible(var1)) {
            this.nextModeIndex = var2 + 1;
            break;
         }

         ++var2;
      }

      if (var4 != null) {
         this.isFallbackPossible = this.isFallbackPossible(var1);
         Internal.instance.apply(var4, var1, this.isFallback);
         return var4;
      } else {
         StringBuilder var6 = new StringBuilder();
         var6.append("Unable to find acceptable protocols. isFallback=");
         var6.append(this.isFallback);
         var6.append(", modes=");
         var6.append(this.connectionSpecs);
         var6.append(", supported protocols=");
         var6.append(Arrays.toString(var1.getEnabledProtocols()));
         throw new UnknownServiceException(var6.toString());
      }
   }

   public boolean connectionFailed(IOException var1) {
      boolean var2 = true;
      this.isFallback = true;
      if (!this.isFallbackPossible) {
         return false;
      } else if (var1 instanceof ProtocolException) {
         return false;
      } else if (var1 instanceof InterruptedIOException) {
         return false;
      } else if (var1 instanceof SSLHandshakeException && var1.getCause() instanceof CertificateException) {
         return false;
      } else if (var1 instanceof SSLPeerUnverifiedException) {
         return false;
      } else {
         if (!(var1 instanceof SSLHandshakeException)) {
            if (var1 instanceof SSLProtocolException) {
               return true;
            }

            var2 = false;
         }

         return var2;
      }
   }
}
