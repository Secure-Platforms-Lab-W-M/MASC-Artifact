package org.apache.http.conn.ssl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

public class SSLConnectionSocketFactory implements LayeredConnectionSocketFactory {
   @Deprecated
   public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER;
   @Deprecated
   public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
   public static final String SSL = "SSL";
   public static final String SSLV2 = "SSLv2";
   @Deprecated
   public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER;
   public static final String TLS = "TLS";
   private static final String WEAK_CIPHERS = "^(TLS|SSL)_(.*)_WITH_(NULL|DES_CBC|DES40_CBC|DES_CBC_40|3DES_EDE_CBC|RC4_128|RC4_40|RC2_CBC_40)_(.*)";
   private static final List WEAK_CIPHER_SUITE_PATTERNS;
   private static final String WEAK_KEY_EXCHANGES = "^(TLS|SSL)_(NULL|ECDH_anon|DH_anon|DH_anon_EXPORT|DHE_RSA_EXPORT|DHE_DSS_EXPORT|DSS_EXPORT|DH_DSS_EXPORT|DH_RSA_EXPORT|RSA_EXPORT|KRB5_EXPORT)_(.*)";
   private final HostnameVerifier hostnameVerifier;
   private final Log log;
   private final javax.net.ssl.SSLSocketFactory socketfactory;
   private final String[] supportedCipherSuites;
   private final String[] supportedProtocols;

   static {
      ALLOW_ALL_HOSTNAME_VERIFIER = AllowAllHostnameVerifier.INSTANCE;
      BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = BrowserCompatHostnameVerifier.INSTANCE;
      STRICT_HOSTNAME_VERIFIER = StrictHostnameVerifier.INSTANCE;
      WEAK_CIPHER_SUITE_PATTERNS = Collections.unmodifiableList(Arrays.asList(Pattern.compile("^(TLS|SSL)_(NULL|ECDH_anon|DH_anon|DH_anon_EXPORT|DHE_RSA_EXPORT|DHE_DSS_EXPORT|DSS_EXPORT|DH_DSS_EXPORT|DH_RSA_EXPORT|RSA_EXPORT|KRB5_EXPORT)_(.*)", 2), Pattern.compile("^(TLS|SSL)_(.*)_WITH_(NULL|DES_CBC|DES40_CBC|DES_CBC_40|3DES_EDE_CBC|RC4_128|RC4_40|RC2_CBC_40)_(.*)", 2)));
   }

   public SSLConnectionSocketFactory(SSLContext var1) {
      this(var1, getDefaultHostnameVerifier());
   }

   public SSLConnectionSocketFactory(SSLContext var1, HostnameVerifier var2) {
      this((javax.net.ssl.SSLSocketFactory)((SSLContext)Args.notNull(var1, "SSL context")).getSocketFactory(), (String[])null, (String[])null, (HostnameVerifier)var2);
   }

   @Deprecated
   public SSLConnectionSocketFactory(SSLContext var1, X509HostnameVerifier var2) {
      this((javax.net.ssl.SSLSocketFactory)((SSLContext)Args.notNull(var1, "SSL context")).getSocketFactory(), (String[])null, (String[])null, (X509HostnameVerifier)var2);
   }

   public SSLConnectionSocketFactory(SSLContext var1, String[] var2, String[] var3, HostnameVerifier var4) {
      this(((SSLContext)Args.notNull(var1, "SSL context")).getSocketFactory(), var2, var3, var4);
   }

   @Deprecated
   public SSLConnectionSocketFactory(SSLContext var1, String[] var2, String[] var3, X509HostnameVerifier var4) {
      this(((SSLContext)Args.notNull(var1, "SSL context")).getSocketFactory(), var2, var3, var4);
   }

   public SSLConnectionSocketFactory(javax.net.ssl.SSLSocketFactory var1, HostnameVerifier var2) {
      this((javax.net.ssl.SSLSocketFactory)var1, (String[])null, (String[])null, (HostnameVerifier)var2);
   }

   @Deprecated
   public SSLConnectionSocketFactory(javax.net.ssl.SSLSocketFactory var1, X509HostnameVerifier var2) {
      this((javax.net.ssl.SSLSocketFactory)var1, (String[])null, (String[])null, (X509HostnameVerifier)var2);
   }

   public SSLConnectionSocketFactory(javax.net.ssl.SSLSocketFactory var1, String[] var2, String[] var3, HostnameVerifier var4) {
      this.log = LogFactory.getLog(this.getClass());
      this.socketfactory = (javax.net.ssl.SSLSocketFactory)Args.notNull(var1, "SSL socket factory");
      this.supportedProtocols = var2;
      this.supportedCipherSuites = var3;
      if (var4 == null) {
         var4 = getDefaultHostnameVerifier();
      }

      this.hostnameVerifier = var4;
   }

   @Deprecated
   public SSLConnectionSocketFactory(javax.net.ssl.SSLSocketFactory var1, String[] var2, String[] var3, X509HostnameVerifier var4) {
      this((javax.net.ssl.SSLSocketFactory)var1, var2, var3, (HostnameVerifier)var4);
   }

   public static HostnameVerifier getDefaultHostnameVerifier() {
      return new DefaultHostnameVerifier(PublicSuffixMatcherLoader.getDefault());
   }

   public static SSLConnectionSocketFactory getSocketFactory() throws SSLInitializationException {
      return new SSLConnectionSocketFactory(org.apache.http.ssl.SSLContexts.createDefault(), getDefaultHostnameVerifier());
   }

   public static SSLConnectionSocketFactory getSystemSocketFactory() throws SSLInitializationException {
      return new SSLConnectionSocketFactory((javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory.getDefault(), split(System.getProperty("https.protocols")), split(System.getProperty("https.cipherSuites")), getDefaultHostnameVerifier());
   }

   static boolean isWeakCipherSuite(String var0) {
      Iterator var1 = WEAK_CIPHER_SUITE_PATTERNS.iterator();

      do {
         if (!var1.hasNext()) {
            return false;
         }
      } while(!((Pattern)var1.next()).matcher(var0).matches());

      return true;
   }

   private static String[] split(String var0) {
      return TextUtils.isBlank(var0) ? null : var0.split(" *, *");
   }

   private void verifyHostname(SSLSocket param1, String param2) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public Socket connectSocket(int var1, Socket var2, HttpHost var3, InetSocketAddress var4, InetSocketAddress var5, HttpContext var6) throws IOException {
      Args.notNull(var3, "HTTP host");
      Args.notNull(var4, "Remote address");
      if (var2 == null) {
         var2 = this.createSocket(var6);
      }

      if (var5 != null) {
         var2.bind(var5);
      }

      label57: {
         IOException var10000;
         label58: {
            boolean var10001;
            if (var1 > 0) {
               try {
                  if (var2.getSoTimeout() == 0) {
                     var2.setSoTimeout(var1);
                  }
               } catch (IOException var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label58;
               }
            }

            try {
               if (this.log.isDebugEnabled()) {
                  Log var14 = this.log;
                  StringBuilder var7 = new StringBuilder();
                  var7.append("Connecting socket to ");
                  var7.append(var4);
                  var7.append(" with timeout ");
                  var7.append(var1);
                  var14.debug(var7.toString());
               }
            } catch (IOException var10) {
               var10000 = var10;
               var10001 = false;
               break label58;
            }

            try {
               var2.connect(var4, var1);
               break label57;
            } catch (IOException var9) {
               var10000 = var9;
               var10001 = false;
            }
         }

         IOException var12 = var10000;

         try {
            var2.close();
         } catch (IOException var8) {
         }

         throw var12;
      }

      if (var2 instanceof SSLSocket) {
         SSLSocket var13 = (SSLSocket)var2;
         this.log.debug("Starting handshake");
         var13.startHandshake();
         this.verifyHostname(var13, var3.getHostName());
         return var2;
      } else {
         return this.createLayeredSocket(var2, var3.getHostName(), var4.getPort(), var6);
      }
   }

   public Socket createLayeredSocket(Socket var1, String var2, int var3, HttpContext var4) throws IOException {
      SSLSocket var8 = (SSLSocket)this.socketfactory.createSocket(var1, var2, var3, true);
      String[] var9 = this.supportedProtocols;
      int var5;
      ArrayList var6;
      String var7;
      if (var9 != null) {
         var8.setEnabledProtocols(var9);
      } else {
         var9 = var8.getEnabledProtocols();
         var6 = new ArrayList(var9.length);
         var5 = var9.length;

         for(var3 = 0; var3 < var5; ++var3) {
            var7 = var9[var3];
            if (!var7.startsWith("SSL")) {
               var6.add(var7);
            }
         }

         if (!var6.isEmpty()) {
            var8.setEnabledProtocols((String[])var6.toArray(new String[var6.size()]));
         }
      }

      var9 = this.supportedCipherSuites;
      if (var9 != null) {
         var8.setEnabledCipherSuites(var9);
      } else {
         var9 = var8.getEnabledCipherSuites();
         var6 = new ArrayList(var9.length);
         var5 = var9.length;

         for(var3 = 0; var3 < var5; ++var3) {
            var7 = var9[var3];
            if (!isWeakCipherSuite(var7)) {
               var6.add(var7);
            }
         }

         if (!var6.isEmpty()) {
            var8.setEnabledCipherSuites((String[])var6.toArray(new String[var6.size()]));
         }
      }

      if (this.log.isDebugEnabled()) {
         Log var10 = this.log;
         StringBuilder var11 = new StringBuilder();
         var11.append("Enabled protocols: ");
         var11.append(Arrays.asList(var8.getEnabledProtocols()));
         var10.debug(var11.toString());
         var10 = this.log;
         var11 = new StringBuilder();
         var11.append("Enabled cipher suites:");
         var11.append(Arrays.asList(var8.getEnabledCipherSuites()));
         var10.debug(var11.toString());
      }

      this.prepareSocket(var8);
      this.log.debug("Starting handshake");
      var8.startHandshake();
      this.verifyHostname(var8, var2);
      return var8;
   }

   public Socket createSocket(HttpContext var1) throws IOException {
      return SocketFactory.getDefault().createSocket();
   }

   protected void prepareSocket(SSLSocket var1) throws IOException {
   }
}
