package okhttp3.internal.platform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

final class Jdk9Platform extends Platform {
   final Method getProtocolMethod;
   final Method setProtocolMethod;

   Jdk9Platform(Method var1, Method var2) {
      this.setProtocolMethod = var1;
      this.getProtocolMethod = var2;
   }

   public static Jdk9Platform buildIfSupported() {
      try {
         Jdk9Platform var0 = new Jdk9Platform(SSLParameters.class.getMethod("setApplicationProtocols", String[].class), SSLSocket.class.getMethod("getApplicationProtocol"));
         return var0;
      } catch (NoSuchMethodException var1) {
         return null;
      }
   }

   public void configureTlsExtensions(SSLSocket var1, String var2, List var3) {
      try {
         SSLParameters var6 = var1.getSSLParameters();
         var3 = alpnProtocolNames(var3);
         this.setProtocolMethod.invoke(var6, (Object)var3.toArray(new String[var3.size()]));
         var1.setSSLParameters(var6);
         return;
      } catch (IllegalAccessException var4) {
      } catch (InvocationTargetException var5) {
      }

      throw new AssertionError();
   }

   public String getSelectedProtocol(SSLSocket param1) {
      // $FF: Couldn't be decompiled
   }

   public X509TrustManager trustManager(SSLSocketFactory var1) {
      throw new UnsupportedOperationException("clientBuilder.sslSocketFactory(SSLSocketFactory) not supported on JDK 9+");
   }
}
