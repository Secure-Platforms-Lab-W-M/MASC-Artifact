package org.apache.http.conn.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import org.apache.http.HttpHost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpInetSocketAddress;
import org.apache.http.conn.scheme.HostNameResolver;
import org.apache.http.conn.scheme.LayeredSchemeSocketFactory;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.SchemeLayeredSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.TextUtils;

@Deprecated
public class SSLSocketFactory implements LayeredConnectionSocketFactory, SchemeLayeredSocketFactory, LayeredSchemeSocketFactory, LayeredSocketFactory {
   public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
   public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = new BrowserCompatHostnameVerifier();
   public static final String SSL = "SSL";
   public static final String SSLV2 = "SSLv2";
   public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER = new StrictHostnameVerifier();
   public static final String TLS = "TLS";
   private volatile X509HostnameVerifier hostnameVerifier;
   private final HostNameResolver nameResolver;
   private final javax.net.ssl.SSLSocketFactory socketfactory;
   private final String[] supportedCipherSuites;
   private final String[] supportedProtocols;

   public SSLSocketFactory(String var1, KeyStore var2, String var3, KeyStore var4, SecureRandom var5, HostNameResolver var6) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      SSLContextBuilder var7 = SSLContexts.custom().useProtocol(var1).setSecureRandom(var5);
      char[] var8;
      if (var3 != null) {
         var8 = var3.toCharArray();
      } else {
         var8 = null;
      }

      this(var7.loadKeyMaterial(var2, var8).loadTrustMaterial(var4).build(), var6);
   }

   public SSLSocketFactory(String var1, KeyStore var2, String var3, KeyStore var4, SecureRandom var5, TrustStrategy var6, X509HostnameVerifier var7) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      SSLContextBuilder var8 = SSLContexts.custom().useProtocol(var1).setSecureRandom(var5);
      char[] var9;
      if (var3 != null) {
         var9 = var3.toCharArray();
      } else {
         var9 = null;
      }

      this(var8.loadKeyMaterial(var2, var9).loadTrustMaterial(var4, var6).build(), var7);
   }

   public SSLSocketFactory(String var1, KeyStore var2, String var3, KeyStore var4, SecureRandom var5, X509HostnameVerifier var6) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      SSLContextBuilder var7 = SSLContexts.custom().useProtocol(var1).setSecureRandom(var5);
      char[] var8;
      if (var3 != null) {
         var8 = var3.toCharArray();
      } else {
         var8 = null;
      }

      this(var7.loadKeyMaterial(var2, var8).loadTrustMaterial(var4).build(), var6);
   }

   public SSLSocketFactory(KeyStore var1) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      this(SSLContexts.custom().loadTrustMaterial(var1).build(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   public SSLSocketFactory(KeyStore var1, String var2) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      SSLContextBuilder var3 = SSLContexts.custom();
      char[] var4;
      if (var2 != null) {
         var4 = var2.toCharArray();
      } else {
         var4 = null;
      }

      this(var3.loadKeyMaterial(var1, var4).build(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   public SSLSocketFactory(KeyStore var1, String var2, KeyStore var3) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      SSLContextBuilder var4 = SSLContexts.custom();
      char[] var5;
      if (var2 != null) {
         var5 = var2.toCharArray();
      } else {
         var5 = null;
      }

      this(var4.loadKeyMaterial(var1, var5).loadTrustMaterial(var3).build(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   public SSLSocketFactory(SSLContext var1) {
      this(var1, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   public SSLSocketFactory(SSLContext var1, HostNameResolver var2) {
      this.socketfactory = var1.getSocketFactory();
      this.hostnameVerifier = BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
      this.nameResolver = var2;
      this.supportedProtocols = null;
      this.supportedCipherSuites = null;
   }

   public SSLSocketFactory(SSLContext var1, X509HostnameVerifier var2) {
      this((javax.net.ssl.SSLSocketFactory)((SSLContext)Args.notNull(var1, "SSL context")).getSocketFactory(), (String[])null, (String[])null, var2);
   }

   public SSLSocketFactory(SSLContext var1, String[] var2, String[] var3, X509HostnameVerifier var4) {
      this(((SSLContext)Args.notNull(var1, "SSL context")).getSocketFactory(), var2, var3, var4);
   }

   public SSLSocketFactory(javax.net.ssl.SSLSocketFactory var1, X509HostnameVerifier var2) {
      this((javax.net.ssl.SSLSocketFactory)var1, (String[])null, (String[])null, var2);
   }

   public SSLSocketFactory(javax.net.ssl.SSLSocketFactory var1, String[] var2, String[] var3, X509HostnameVerifier var4) {
      this.socketfactory = (javax.net.ssl.SSLSocketFactory)Args.notNull(var1, "SSL socket factory");
      this.supportedProtocols = var2;
      this.supportedCipherSuites = var3;
      if (var4 == null) {
         var4 = BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
      }

      this.hostnameVerifier = var4;
      this.nameResolver = null;
   }

   public SSLSocketFactory(TrustStrategy var1) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      this(SSLContexts.custom().loadTrustMaterial((KeyStore)null, var1).build(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   public SSLSocketFactory(TrustStrategy var1, X509HostnameVerifier var2) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
      this(SSLContexts.custom().loadTrustMaterial((KeyStore)null, var1).build(), var2);
   }

   public static SSLSocketFactory getSocketFactory() throws SSLInitializationException {
      return new SSLSocketFactory(SSLContexts.createDefault(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   public static SSLSocketFactory getSystemSocketFactory() throws SSLInitializationException {
      return new SSLSocketFactory((javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory.getDefault(), split(System.getProperty("https.protocols")), split(System.getProperty("https.cipherSuites")), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
   }

   private void internalPrepareSocket(SSLSocket var1) throws IOException {
      String[] var2 = this.supportedProtocols;
      if (var2 != null) {
         var1.setEnabledProtocols(var2);
      }

      var2 = this.supportedCipherSuites;
      if (var2 != null) {
         var1.setEnabledCipherSuites(var2);
      }

      this.prepareSocket(var1);
   }

   private static String[] split(String var0) {
      return TextUtils.isBlank(var0) ? null : var0.split(" *, *");
   }

   private void verifyHostname(SSLSocket var1, String var2) throws IOException {
      try {
         this.hostnameVerifier.verify(var2, var1);
      } catch (IOException var4) {
         try {
            var1.close();
         } catch (Exception var3) {
         }

         throw var4;
      }
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

      try {
         var2.connect(var4, var1);
      } catch (SocketTimeoutException var7) {
         StringBuilder var8 = new StringBuilder();
         var8.append("Connect to ");
         var8.append(var4);
         var8.append(" timed out");
         throw new ConnectTimeoutException(var8.toString());
      }

      if (var2 instanceof SSLSocket) {
         SSLSocket var9 = (SSLSocket)var2;
         var9.startHandshake();
         this.verifyHostname(var9, var3.getHostName());
         return var2;
      } else {
         return this.createLayeredSocket(var2, var3.getHostName(), var4.getPort(), var6);
      }
   }

   public Socket connectSocket(Socket var1, String var2, int var3, InetAddress var4, int var5, HttpParams var6) throws IOException, UnknownHostException, ConnectTimeoutException {
      HostNameResolver var7 = this.nameResolver;
      InetAddress var9;
      if (var7 != null) {
         var9 = var7.resolve(var2);
      } else {
         var9 = InetAddress.getByName(var2);
      }

      InetSocketAddress var8 = null;
      if (var4 != null || var5 > 0) {
         if (var5 <= 0) {
            var5 = 0;
         }

         var8 = new InetSocketAddress(var4, var5);
      }

      return this.connectSocket(var1, new HttpInetSocketAddress(new HttpHost(var2, var3), var9, var3), var8, var6);
   }

   public Socket connectSocket(Socket var1, InetSocketAddress var2, InetSocketAddress var3, HttpParams var4) throws IOException, UnknownHostException, ConnectTimeoutException {
      Args.notNull(var2, "Remote address");
      Args.notNull(var4, "HTTP parameters");
      HttpHost var7;
      if (var2 instanceof HttpInetSocketAddress) {
         var7 = ((HttpInetSocketAddress)var2).getHttpHost();
      } else {
         var7 = new HttpHost(var2.getHostName(), var2.getPort(), "https");
      }

      int var5 = HttpConnectionParams.getSoTimeout(var4);
      int var6 = HttpConnectionParams.getConnectionTimeout(var4);
      var1.setSoTimeout(var5);
      return this.connectSocket(var6, var1, var7, var2, var3, (HttpContext)null);
   }

   public Socket createLayeredSocket(Socket var1, String var2, int var3, HttpParams var4) throws IOException, UnknownHostException {
      return this.createLayeredSocket(var1, var2, var3, (HttpContext)null);
   }

   public Socket createLayeredSocket(Socket var1, String var2, int var3, HttpContext var4) throws IOException {
      SSLSocket var5 = (SSLSocket)this.socketfactory.createSocket(var1, var2, var3, true);
      this.internalPrepareSocket(var5);
      var5.startHandshake();
      this.verifyHostname(var5, var2);
      return var5;
   }

   public Socket createLayeredSocket(Socket var1, String var2, int var3, boolean var4) throws IOException, UnknownHostException {
      return this.createLayeredSocket(var1, var2, var3, (HttpContext)null);
   }

   public Socket createSocket() throws IOException {
      return this.createSocket((HttpContext)null);
   }

   public Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws IOException, UnknownHostException {
      return this.createLayeredSocket(var1, var2, var3, var4);
   }

   public Socket createSocket(HttpParams var1) throws IOException {
      return this.createSocket((HttpContext)null);
   }

   public Socket createSocket(HttpContext var1) throws IOException {
      return SocketFactory.getDefault().createSocket();
   }

   public X509HostnameVerifier getHostnameVerifier() {
      return this.hostnameVerifier;
   }

   public boolean isSecure(Socket var1) throws IllegalArgumentException {
      Args.notNull(var1, "Socket");
      Asserts.check(var1 instanceof SSLSocket, "Socket not created by this factory");
      Asserts.check(var1.isClosed() ^ true, "Socket is closed");
      return true;
   }

   protected void prepareSocket(SSLSocket var1) throws IOException {
   }

   public void setHostnameVerifier(X509HostnameVerifier var1) {
      Args.notNull(var1, "Hostname verifier");
      this.hostnameVerifier = var1;
   }
}
