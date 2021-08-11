package org.apache.http.conn.ssl;

import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

@Deprecated
public class SSLContextBuilder {
   static final String SSL = "SSL";
   static final String TLS = "TLS";
   private final Set keymanagers = new LinkedHashSet();
   private String protocol;
   private SecureRandom secureRandom;
   private final Set trustmanagers = new LinkedHashSet();

   public SSLContext build() throws NoSuchAlgorithmException, KeyManagementException {
      String var2 = this.protocol;
      if (var2 == null) {
         var2 = "TLS";
      }

      SSLContext var4 = SSLContext.getInstance(var2);
      boolean var1 = this.keymanagers.isEmpty();
      TrustManager[] var3 = null;
      KeyManager[] var6;
      if (!var1) {
         Set var5 = this.keymanagers;
         var6 = (KeyManager[])var5.toArray(new KeyManager[var5.size()]);
      } else {
         var6 = null;
      }

      if (!this.trustmanagers.isEmpty()) {
         Set var7 = this.trustmanagers;
         var3 = (TrustManager[])var7.toArray(new TrustManager[var7.size()]);
      }

      var4.init(var6, var3, this.secureRandom);
      return var4;
   }

   public SSLContextBuilder loadKeyMaterial(KeyStore var1, char[] var2) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
      this.loadKeyMaterial(var1, var2, (PrivateKeyStrategy)null);
      return this;
   }

   public SSLContextBuilder loadKeyMaterial(KeyStore var1, char[] var2, PrivateKeyStrategy var3) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
      KeyManagerFactory var6 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      var6.init(var1, var2);
      KeyManager[] var7 = var6.getKeyManagers();
      if (var7 != null) {
         int var4;
         KeyManager var8;
         if (var3 != null) {
            for(var4 = 0; var4 < var7.length; ++var4) {
               var8 = var7[var4];
               if (var8 instanceof X509KeyManager) {
                  var7[var4] = new SSLContextBuilder.KeyManagerDelegate((X509KeyManager)var8, var3);
               }
            }
         }

         int var5 = var7.length;

         for(var4 = 0; var4 < var5; ++var4) {
            var8 = var7[var4];
            this.keymanagers.add(var8);
         }
      }

      return this;
   }

   public SSLContextBuilder loadTrustMaterial(KeyStore var1) throws NoSuchAlgorithmException, KeyStoreException {
      return this.loadTrustMaterial(var1, (TrustStrategy)null);
   }

   public SSLContextBuilder loadTrustMaterial(KeyStore var1, TrustStrategy var2) throws NoSuchAlgorithmException, KeyStoreException {
      TrustManagerFactory var5 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      var5.init(var1);
      TrustManager[] var6 = var5.getTrustManagers();
      if (var6 != null) {
         int var3;
         if (var2 != null) {
            for(var3 = 0; var3 < var6.length; ++var3) {
               TrustManager var8 = var6[var3];
               if (var8 instanceof X509TrustManager) {
                  var6[var3] = new SSLContextBuilder.TrustManagerDelegate((X509TrustManager)var8, var2);
               }
            }
         }

         int var4 = var6.length;

         for(var3 = 0; var3 < var4; ++var3) {
            TrustManager var7 = var6[var3];
            this.trustmanagers.add(var7);
         }
      }

      return this;
   }

   public SSLContextBuilder setSecureRandom(SecureRandom var1) {
      this.secureRandom = var1;
      return this;
   }

   public SSLContextBuilder useProtocol(String var1) {
      this.protocol = var1;
      return this;
   }

   public SSLContextBuilder useSSL() {
      this.protocol = "SSL";
      return this;
   }

   public SSLContextBuilder useTLS() {
      this.protocol = "TLS";
      return this;
   }

   static class KeyManagerDelegate implements X509KeyManager {
      private final PrivateKeyStrategy aliasStrategy;
      private final X509KeyManager keyManager;

      KeyManagerDelegate(X509KeyManager var1, PrivateKeyStrategy var2) {
         this.keyManager = var1;
         this.aliasStrategy = var2;
      }

      public String chooseClientAlias(String[] var1, Principal[] var2, Socket var3) {
         HashMap var8 = new HashMap();
         int var6 = var1.length;

         for(int var4 = 0; var4 < var6; ++var4) {
            String var9 = var1[var4];
            String[] var10 = this.keyManager.getClientAliases(var9, var2);
            if (var10 != null) {
               int var7 = var10.length;

               for(int var5 = 0; var5 < var7; ++var5) {
                  String var11 = var10[var5];
                  var8.put(var11, new PrivateKeyDetails(var9, this.keyManager.getCertificateChain(var11)));
               }
            }
         }

         return this.aliasStrategy.chooseAlias(var8, var3);
      }

      public String chooseServerAlias(String var1, Principal[] var2, Socket var3) {
         HashMap var6 = new HashMap();
         String[] var8 = this.keyManager.getServerAliases(var1, var2);
         if (var8 != null) {
            int var5 = var8.length;

            for(int var4 = 0; var4 < var5; ++var4) {
               String var7 = var8[var4];
               var6.put(var7, new PrivateKeyDetails(var1, this.keyManager.getCertificateChain(var7)));
            }
         }

         return this.aliasStrategy.chooseAlias(var6, var3);
      }

      public X509Certificate[] getCertificateChain(String var1) {
         return this.keyManager.getCertificateChain(var1);
      }

      public String[] getClientAliases(String var1, Principal[] var2) {
         return this.keyManager.getClientAliases(var1, var2);
      }

      public PrivateKey getPrivateKey(String var1) {
         return this.keyManager.getPrivateKey(var1);
      }

      public String[] getServerAliases(String var1, Principal[] var2) {
         return this.keyManager.getServerAliases(var1, var2);
      }
   }

   static class TrustManagerDelegate implements X509TrustManager {
      private final X509TrustManager trustManager;
      private final TrustStrategy trustStrategy;

      TrustManagerDelegate(X509TrustManager var1, TrustStrategy var2) {
         this.trustManager = var1;
         this.trustStrategy = var2;
      }

      public void checkClientTrusted(X509Certificate[] var1, String var2) throws CertificateException {
         this.trustManager.checkClientTrusted(var1, var2);
      }

      public void checkServerTrusted(X509Certificate[] var1, String var2) throws CertificateException {
         if (!this.trustStrategy.isTrusted(var1, var2)) {
            this.trustManager.checkServerTrusted(var1, var2);
         }

      }

      public X509Certificate[] getAcceptedIssuers() {
         return this.trustManager.getAcceptedIssuers();
      }
   }
}
