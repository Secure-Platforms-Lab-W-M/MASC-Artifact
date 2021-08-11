package okhttp3.internal.platform;

import android.util.Log;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;

class AndroidPlatform extends Platform {
   private static final int MAX_LOG_LENGTH = 4000;
   private final AndroidPlatform.CloseGuard closeGuard = AndroidPlatform.CloseGuard.get();
   private final OptionalMethod getAlpnSelectedProtocol;
   private final OptionalMethod setAlpnProtocols;
   private final OptionalMethod setHostname;
   private final OptionalMethod setUseSessionTickets;
   private final Class sslParametersClass;

   AndroidPlatform(Class var1, OptionalMethod var2, OptionalMethod var3, OptionalMethod var4, OptionalMethod var5) {
      this.sslParametersClass = var1;
      this.setUseSessionTickets = var2;
      this.setHostname = var3;
      this.getAlpnSelectedProtocol = var4;
      this.setAlpnProtocols = var5;
   }

   public static Platform buildIfSupported() {
      Class var1;
      boolean var10001;
      try {
         var1 = Class.forName("com.android.org.conscrypt.SSLParametersImpl");
      } catch (ClassNotFoundException var12) {
         try {
            var1 = Class.forName("org.apache.harmony.xnet.provider.jsse.SSLParametersImpl");
         } catch (ClassNotFoundException var11) {
            var10001 = false;
            return null;
         }
      }

      OptionalMethod var4;
      OptionalMethod var5;
      try {
         var4 = new OptionalMethod((Class)null, "setUseSessionTickets", new Class[]{Boolean.TYPE});
         var5 = new OptionalMethod((Class)null, "setHostname", new Class[]{String.class});
      } catch (ClassNotFoundException var10) {
         var10001 = false;
         return null;
      }

      OptionalMethod var2 = null;
      OptionalMethod var0 = var2;

      OptionalMethod var3;
      label47: {
         label46: {
            label62: {
               try {
                  Class.forName("android.net.Network");
               } catch (ClassNotFoundException var8) {
                  var10001 = false;
                  break label62;
               }

               var0 = var2;

               try {
                  var2 = new OptionalMethod(byte[].class, "getAlpnSelectedProtocol", new Class[0]);
               } catch (ClassNotFoundException var7) {
                  var10001 = false;
                  break label62;
               }

               var0 = var2;

               try {
                  var3 = new OptionalMethod((Class)null, "setAlpnProtocols", new Class[]{byte[].class});
                  break label46;
               } catch (ClassNotFoundException var6) {
                  var10001 = false;
               }
            }

            var3 = null;
            var2 = var0;
            break label47;
         }

         var3 = var3;
      }

      try {
         AndroidPlatform var13 = new AndroidPlatform(var1, var4, var5, var2, var3);
         return var13;
      } catch (ClassNotFoundException var9) {
         var10001 = false;
         return null;
      }
   }

   public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager var1) {
      try {
         Class var2 = Class.forName("android.net.http.X509TrustManagerExtensions");
         AndroidPlatform.AndroidCertificateChainCleaner var4 = new AndroidPlatform.AndroidCertificateChainCleaner(var2.getConstructor(X509TrustManager.class).newInstance(var1), var2.getMethod("checkServerTrusted", X509Certificate[].class, String.class, String.class));
         return var4;
      } catch (Exception var3) {
         return super.buildCertificateChainCleaner(var1);
      }
   }

   public void configureTlsExtensions(SSLSocket var1, String var2, List var3) {
      if (var2 != null) {
         this.setUseSessionTickets.invokeOptionalWithoutCheckedException(var1, true);
         this.setHostname.invokeOptionalWithoutCheckedException(var1, var2);
      }

      OptionalMethod var4 = this.setAlpnProtocols;
      if (var4 != null && var4.isSupported(var1)) {
         byte[] var5 = concatLengthPrefixed(var3);
         this.setAlpnProtocols.invokeWithoutCheckedException(var1, var5);
      }

   }

   public void connectSocket(Socket var1, InetSocketAddress var2, int var3) throws IOException {
      try {
         var1.connect(var2, var3);
      } catch (AssertionError var4) {
         if (Util.isAndroidGetsocknameError(var4)) {
            throw new IOException(var4);
         } else {
            throw var4;
         }
      } catch (SecurityException var5) {
         IOException var6 = new IOException("Exception in connect");
         var6.initCause(var5);
         throw var6;
      }
   }

   public String getSelectedProtocol(SSLSocket var1) {
      OptionalMethod var3 = this.getAlpnSelectedProtocol;
      Object var2 = null;
      if (var3 == null) {
         return null;
      } else if (!var3.isSupported(var1)) {
         return null;
      } else {
         byte[] var5 = (byte[])this.getAlpnSelectedProtocol.invokeWithoutCheckedException(var1);
         String var4 = (String)var2;
         if (var5 != null) {
            var4 = new String(var5, Util.UTF_8);
         }

         return var4;
      }
   }

   public Object getStackTraceForCloseable(String var1) {
      return this.closeGuard.createAndOpen(var1);
   }

   public boolean isCleartextTrafficPermitted(String var1) {
      try {
         Class var3 = Class.forName("android.security.NetworkSecurityPolicy");
         Object var4 = var3.getMethod("getInstance").invoke((Object)null);
         boolean var2 = (Boolean)var3.getMethod("isCleartextTrafficPermitted", String.class).invoke(var4, var1);
         return var2;
      } catch (ClassNotFoundException var5) {
      } catch (NoSuchMethodException var6) {
      } catch (IllegalAccessException var7) {
         throw new AssertionError();
      } catch (IllegalArgumentException var8) {
         throw new AssertionError();
      } catch (InvocationTargetException var9) {
         throw new AssertionError();
      }

      return super.isCleartextTrafficPermitted(var1);
   }

   public void log(int var1, String var2, Throwable var3) {
      byte var4 = 5;
      if (var1 != 5) {
         var4 = 3;
      }

      String var8 = var2;
      if (var3 != null) {
         StringBuilder var9 = new StringBuilder();
         var9.append(var2);
         var9.append('\n');
         var9.append(Log.getStackTraceString(var3));
         var8 = var9.toString();
      }

      var1 = 0;

      int var6;
      for(int var7 = var8.length(); var1 < var7; var1 = var6 + 1) {
         int var5 = var8.indexOf(10, var1);
         if (var5 == -1) {
            var5 = var7;
         }

         do {
            var6 = Math.min(var5, var1 + 4000);
            Log.println(var4, "OkHttp", var8.substring(var1, var6));
            var1 = var6;
         } while(var6 < var5);
      }

   }

   public void logCloseableLeak(String var1, Object var2) {
      if (!this.closeGuard.warnIfOpen(var2)) {
         this.log(5, var1, (Throwable)null);
      }

   }

   public X509TrustManager trustManager(SSLSocketFactory var1) {
      Object var3 = readFieldOrNull(var1, this.sslParametersClass, "sslParameters");
      Object var2 = var3;
      if (var3 == null) {
         try {
            var2 = readFieldOrNull(var1, Class.forName("com.google.android.gms.org.conscrypt.SSLParametersImpl", false, var1.getClass().getClassLoader()), "sslParameters");
         } catch (ClassNotFoundException var4) {
            return super.trustManager(var1);
         }
      }

      X509TrustManager var5 = (X509TrustManager)readFieldOrNull(var2, X509TrustManager.class, "x509TrustManager");
      return var5 != null ? var5 : (X509TrustManager)readFieldOrNull(var2, X509TrustManager.class, "trustManager");
   }

   static final class AndroidCertificateChainCleaner extends CertificateChainCleaner {
      private final Method checkServerTrusted;
      private final Object x509TrustManagerExtensions;

      AndroidCertificateChainCleaner(Object var1, Method var2) {
         this.x509TrustManagerExtensions = var1;
         this.checkServerTrusted = var2;
      }

      public List clean(List var1, String var2) throws SSLPeerUnverifiedException {
         try {
            X509Certificate[] var5 = (X509Certificate[])var1.toArray(new X509Certificate[var1.size()]);
            var1 = (List)this.checkServerTrusted.invoke(this.x509TrustManagerExtensions, var5, "RSA", var2);
            return var1;
         } catch (InvocationTargetException var3) {
            SSLPeerUnverifiedException var6 = new SSLPeerUnverifiedException(var3.getMessage());
            var6.initCause(var3);
            throw var6;
         } catch (IllegalAccessException var4) {
            throw new AssertionError(var4);
         }
      }

      public boolean equals(Object var1) {
         return var1 instanceof AndroidPlatform.AndroidCertificateChainCleaner;
      }

      public int hashCode() {
         return 0;
      }
   }

   static final class CloseGuard {
      private final Method getMethod;
      private final Method openMethod;
      private final Method warnIfOpenMethod;

      CloseGuard(Method var1, Method var2, Method var3) {
         this.getMethod = var1;
         this.openMethod = var2;
         this.warnIfOpenMethod = var3;
      }

      static AndroidPlatform.CloseGuard get() {
         Method var0;
         Method var1;
         Method var2;
         try {
            Class var4 = Class.forName("dalvik.system.CloseGuard");
            var1 = var4.getMethod("get");
            var2 = var4.getMethod("open", String.class);
            var0 = var4.getMethod("warnIfOpen");
         } catch (Exception var3) {
            var1 = null;
            var2 = null;
            var0 = null;
         }

         return new AndroidPlatform.CloseGuard(var1, var2, var0);
      }

      Object createAndOpen(String var1) {
         Method var2 = this.getMethod;
         if (var2 != null) {
            try {
               Object var4 = var2.invoke((Object)null);
               this.openMethod.invoke(var4, var1);
               return var4;
            } catch (Exception var3) {
            }
         }

         return null;
      }

      boolean warnIfOpen(Object var1) {
         if (var1 != null) {
            try {
               this.warnIfOpenMethod.invoke(var1);
               return true;
            } catch (Exception var2) {
            }
         }

         return false;
      }
   }
}
