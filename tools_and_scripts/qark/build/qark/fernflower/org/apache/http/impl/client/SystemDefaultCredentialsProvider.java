package org.apache.http.impl.client;

import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.Authenticator.RequestorType;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.util.Args;

public class SystemDefaultCredentialsProvider implements CredentialsProvider {
   private static final Map SCHEME_MAP;
   private final BasicCredentialsProvider internal = new BasicCredentialsProvider();

   static {
      ConcurrentHashMap var0 = new ConcurrentHashMap();
      SCHEME_MAP = var0;
      var0.put("Basic".toUpperCase(Locale.ROOT), "Basic");
      SCHEME_MAP.put("Digest".toUpperCase(Locale.ROOT), "Digest");
      SCHEME_MAP.put("NTLM".toUpperCase(Locale.ROOT), "NTLM");
      SCHEME_MAP.put("Negotiate".toUpperCase(Locale.ROOT), "SPNEGO");
      SCHEME_MAP.put("Kerberos".toUpperCase(Locale.ROOT), "Kerberos");
   }

   private static PasswordAuthentication getProxyCredentials(String var0, AuthScope var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(var0);
      var2.append(".proxyHost");
      String var13 = System.getProperty(var2.toString());
      if (var13 == null) {
         return null;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(var0);
         var3.append(".proxyPort");
         String var14 = System.getProperty(var3.toString());
         if (var14 == null) {
            return null;
         } else {
            boolean var10001;
            String var11;
            try {
               if (var1.match(new AuthScope(var13, Integer.parseInt(var14))) < 0) {
                  return null;
               }

               StringBuilder var10 = new StringBuilder();
               var10.append(var0);
               var10.append(".proxyUser");
               var11 = System.getProperty(var10.toString());
            } catch (NumberFormatException var8) {
               var10001 = false;
               return null;
            }

            if (var11 == null) {
               return null;
            } else {
               try {
                  var2 = new StringBuilder();
                  var2.append(var0);
                  var2.append(".proxyPassword");
                  var0 = System.getProperty(var2.toString());
               } catch (NumberFormatException var7) {
                  var10001 = false;
                  return null;
               }

               char[] var9;
               if (var0 != null) {
                  try {
                     var9 = var0.toCharArray();
                  } catch (NumberFormatException var6) {
                     var10001 = false;
                     return null;
                  }
               } else {
                  try {
                     var9 = new char[0];
                  } catch (NumberFormatException var5) {
                     var10001 = false;
                     return null;
                  }
               }

               try {
                  PasswordAuthentication var12 = new PasswordAuthentication(var11, var9);
                  return var12;
               } catch (NumberFormatException var4) {
                  var10001 = false;
                  return null;
               }
            }
         }
      }
   }

   private static PasswordAuthentication getSystemCreds(String var0, AuthScope var1, RequestorType var2) {
      return Authenticator.requestPasswordAuthentication(var1.getHost(), (InetAddress)null, var1.getPort(), var0, (String)null, translateScheme(var1.getScheme()), (URL)null, var2);
   }

   private static String translateScheme(String var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = (String)SCHEME_MAP.get(var0);
         return var1 != null ? var1 : var0;
      }
   }

   public void clear() {
      this.internal.clear();
   }

   public Credentials getCredentials(AuthScope var1) {
      Args.notNull(var1, "Auth scope");
      Credentials var2 = this.internal.getCredentials(var1);
      if (var2 != null) {
         return var2;
      } else {
         if (var1.getHost() != null) {
            HttpHost var6 = var1.getOrigin();
            String var3;
            if (var6 != null) {
               var3 = var6.getSchemeName();
            } else if (var1.getPort() == 443) {
               var3 = "https";
            } else {
               var3 = "http";
            }

            PasswordAuthentication var4 = getSystemCreds(var3, var1, RequestorType.SERVER);
            PasswordAuthentication var7 = var4;
            if (var4 == null) {
               var7 = getSystemCreds(var3, var1, RequestorType.PROXY);
            }

            PasswordAuthentication var9 = var7;
            if (var7 == null) {
               var7 = getProxyCredentials("http", var1);
               var9 = var7;
               if (var7 == null) {
                  var9 = getProxyCredentials("https", var1);
               }
            }

            if (var9 != null) {
               String var8 = System.getProperty("http.auth.ntlm.domain");
               if (var8 != null) {
                  return new NTCredentials(var9.getUserName(), new String(var9.getPassword()), (String)null, var8);
               }

               Object var5;
               if ("NTLM".equalsIgnoreCase(var1.getScheme())) {
                  var5 = new NTCredentials(var9.getUserName(), new String(var9.getPassword()), (String)null, (String)null);
               } else {
                  var5 = new UsernamePasswordCredentials(var9.getUserName(), new String(var9.getPassword()));
               }

               return (Credentials)var5;
            }
         }

         return null;
      }
   }

   public void setCredentials(AuthScope var1, Credentials var2) {
      this.internal.setCredentials(var1, var2);
   }
}
