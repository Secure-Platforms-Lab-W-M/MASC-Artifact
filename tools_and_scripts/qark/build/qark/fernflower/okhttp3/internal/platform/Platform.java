package okhttp3.internal.platform;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.tls.BasicCertificateChainCleaner;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.TrustRootIndex;
import okio.Buffer;

public class Platform {
   public static final int INFO = 4;
   private static final Platform PLATFORM = findPlatform();
   public static final int WARN = 5;
   private static final Logger logger = Logger.getLogger(OkHttpClient.class.getName());

   public static List alpnProtocolNames(List var0) {
      ArrayList var3 = new ArrayList(var0.size());
      int var1 = 0;

      for(int var2 = var0.size(); var1 < var2; ++var1) {
         Protocol var4 = (Protocol)var0.get(var1);
         if (var4 != Protocol.HTTP_1_0) {
            var3.add(var4.toString());
         }
      }

      return var3;
   }

   static byte[] concatLengthPrefixed(List var0) {
      Buffer var3 = new Buffer();
      int var1 = 0;

      for(int var2 = var0.size(); var1 < var2; ++var1) {
         Protocol var4 = (Protocol)var0.get(var1);
         if (var4 != Protocol.HTTP_1_0) {
            var3.writeByte(var4.toString().length());
            var3.writeUtf8(var4.toString());
         }
      }

      return var3.readByteArray();
   }

   private static Platform findPlatform() {
      Platform var0 = AndroidPlatform.buildIfSupported();
      if (var0 != null) {
         return var0;
      } else {
         Jdk9Platform var1 = Jdk9Platform.buildIfSupported();
         if (var1 != null) {
            return var1;
         } else {
            var0 = JdkWithJettyBootPlatform.buildIfSupported();
            return var0 != null ? var0 : new Platform();
         }
      }
   }

   public static Platform get() {
      return PLATFORM;
   }

   static Object readFieldOrNull(Object var0, Class var1, String var2) {
      for(Class var3 = var0.getClass(); var3 != Object.class; var3 = var3.getSuperclass()) {
         boolean var10001;
         Object var11;
         try {
            Field var4 = var3.getDeclaredField(var2);
            var4.setAccessible(true);
            var11 = var4.get(var0);
         } catch (NoSuchFieldException var9) {
            var10001 = false;
            continue;
         } catch (IllegalAccessException var10) {
            var10001 = false;
            throw new AssertionError();
         }

         if (var11 == null) {
            return null;
         }

         try {
            if (!var1.isInstance(var11)) {
               return null;
            }
         } catch (NoSuchFieldException var7) {
            var10001 = false;
            continue;
         } catch (IllegalAccessException var8) {
            var10001 = false;
            throw new AssertionError();
         }

         try {
            var11 = var1.cast(var11);
            return var11;
         } catch (NoSuchFieldException var5) {
            var10001 = false;
         } catch (IllegalAccessException var6) {
            var10001 = false;
            throw new AssertionError();
         }
      }

      if (!var2.equals("delegate")) {
         var0 = readFieldOrNull(var0, Object.class, "delegate");
         if (var0 != null) {
            return readFieldOrNull(var0, var1, var2);
         }
      }

      return null;
   }

   public void afterHandshake(SSLSocket var1) {
   }

   public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager var1) {
      return new BasicCertificateChainCleaner(TrustRootIndex.get(var1));
   }

   public void configureTlsExtensions(SSLSocket var1, String var2, List var3) {
   }

   public void connectSocket(Socket var1, InetSocketAddress var2, int var3) throws IOException {
      var1.connect(var2, var3);
   }

   public String getPrefix() {
      return "OkHttp";
   }

   public String getSelectedProtocol(SSLSocket var1) {
      return null;
   }

   public Object getStackTraceForCloseable(String var1) {
      return logger.isLoggable(Level.FINE) ? new Throwable(var1) : null;
   }

   public boolean isCleartextTrafficPermitted(String var1) {
      return true;
   }

   public void log(int var1, String var2, Throwable var3) {
      Level var4;
      if (var1 == 5) {
         var4 = Level.WARNING;
      } else {
         var4 = Level.INFO;
      }

      logger.log(var4, var2, var3);
   }

   public void logCloseableLeak(String var1, Object var2) {
      String var3 = var1;
      if (var2 == null) {
         StringBuilder var4 = new StringBuilder();
         var4.append(var1);
         var4.append(" To see where this was allocated, set the OkHttpClient logger level to FINE: Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);");
         var3 = var4.toString();
      }

      this.log(5, var3, (Throwable)var2);
   }

   public X509TrustManager trustManager(SSLSocketFactory param1) {
      // $FF: Couldn't be decompiled
   }
}
