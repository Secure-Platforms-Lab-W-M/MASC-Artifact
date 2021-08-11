package org.apache.http.conn.ssl;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.util.Args;

@Deprecated
public abstract class AbstractVerifier implements X509HostnameVerifier {
   static final String[] BAD_COUNTRY_2LDS;
   private final Log log = LogFactory.getLog(this.getClass());

   static {
      String[] var0 = new String[]{"ac", "co", "com", "ed", "edu", "go", "gouv", "gov", "info", "lg", "ne", "net", "or", "org"};
      BAD_COUNTRY_2LDS = var0;
      Arrays.sort(var0);
   }

   public static boolean acceptableCountryWildcard(String var0) {
      return validCountryWildcard(var0.split("\\."));
   }

   public static int countDots(String var0) {
      int var2 = 0;

      int var3;
      for(int var1 = 0; var1 < var0.length(); var2 = var3) {
         var3 = var2;
         if (var0.charAt(var1) == '.') {
            var3 = var2 + 1;
         }

         ++var1;
      }

      return var2;
   }

   public static String[] getCNs(X509Certificate var0) {
      String var1 = var0.getSubjectX500Principal().toString();
      String[] var3 = null;

      try {
         var1 = DefaultHostnameVerifier.extractCN(var1);
      } catch (SSLException var2) {
         return null;
      }

      if (var1 != null) {
         var3 = new String[]{var1};
      }

      return var3;
   }

   public static String[] getDNSSubjectAlts(X509Certificate var0) {
      List var2 = DefaultHostnameVerifier.getSubjectAltNames(var0);
      String[] var4 = null;
      if (var2 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList();
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            SubjectName var3 = (SubjectName)var5.next();
            if (var3.getType() == 2) {
               var1.add(var3.getValue());
            }
         }

         if (var1.isEmpty()) {
            var4 = (String[])var1.toArray(new String[var1.size()]);
         }

         return var4;
      }
   }

   private static boolean matchIdentity(String var0, String var1, boolean var2) {
      boolean var6 = false;
      if (var0 == null) {
         return false;
      } else {
         var0 = var0.toLowerCase(Locale.ROOT);
         var1 = var1.toLowerCase(Locale.ROOT);
         String[] var7 = var1.split("\\.");
         boolean var3;
         if (var7.length < 3 || !var7[0].endsWith("*") || var2 && !validCountryWildcard(var7)) {
            var3 = false;
         } else {
            var3 = true;
         }

         if (!var3) {
            return var0.equals(var1);
         } else {
            String var8 = var7[0];
            boolean var4;
            if (var8.length() > 1) {
               String var10 = var8.substring(0, var8.length() - 1);
               var8 = var1.substring(var8.length());
               String var9 = var0.substring(var10.length());
               if (var0.startsWith(var10) && var9.endsWith(var8)) {
                  var4 = true;
               } else {
                  var4 = false;
               }
            } else {
               var4 = var0.endsWith(var1.substring(1));
            }

            boolean var5 = var6;
            if (var4) {
               if (var2) {
                  var5 = var6;
                  if (countDots(var0) != countDots(var1)) {
                     return var5;
                  }
               }

               var5 = true;
            }

            return var5;
         }
      }
   }

   private static boolean validCountryWildcard(String[] var0) {
      if (var0.length == 3) {
         if (var0[2].length() != 2) {
            return true;
         } else {
            return Arrays.binarySearch(BAD_COUNTRY_2LDS, var0[1]) < 0;
         }
      } else {
         return true;
      }
   }

   public final void verify(String var1, X509Certificate var2) throws SSLException {
      List var3 = DefaultHostnameVerifier.getSubjectAltNames(var2);
      ArrayList var4 = new ArrayList();
      SubjectName var5;
      Iterator var7;
      if (!InetAddressUtils.isIPv4Address(var1) && !InetAddressUtils.isIPv6Address(var1)) {
         var7 = var3.iterator();

         while(var7.hasNext()) {
            var5 = (SubjectName)var7.next();
            if (var5.getType() == 2) {
               var4.add(var5.getValue());
            }
         }
      } else {
         var7 = var3.iterator();

         while(var7.hasNext()) {
            var5 = (SubjectName)var7.next();
            if (var5.getType() == 7) {
               var4.add(var5.getValue());
            }
         }
      }

      String var9 = DefaultHostnameVerifier.extractCN(var2.getSubjectX500Principal().getName("RFC2253"));
      String[] var8 = null;
      String[] var6;
      if (var9 != null) {
         var6 = new String[]{var9};
      } else {
         var6 = null;
      }

      if (!var4.isEmpty()) {
         var8 = (String[])var4.toArray(new String[var4.size()]);
      }

      this.verify(var1, var6, var8);
   }

   public final void verify(String var1, SSLSocket var2) throws IOException {
      Args.notNull(var1, "Host");
      SSLSession var4 = var2.getSession();
      SSLSession var3 = var4;
      if (var4 == null) {
         var2.getInputStream().available();
         var4 = var2.getSession();
         var3 = var4;
         if (var4 == null) {
            var2.startHandshake();
            var3 = var2.getSession();
         }
      }

      this.verify(var1, (X509Certificate)var3.getPeerCertificates()[0]);
   }

   public final void verify(String var1, String[] var2, String[] var3, boolean var4) throws SSLException {
      Iterator var6 = null;
      String var7;
      if (var2 != null && var2.length > 0) {
         var7 = var2[0];
      } else {
         var7 = null;
      }

      List var5 = var6;
      if (var3 != null) {
         var5 = var6;
         if (var3.length > 0) {
            var5 = Arrays.asList(var3);
         }
      }

      String var8;
      if (InetAddressUtils.isIPv6Address(var1)) {
         var8 = DefaultHostnameVerifier.normaliseAddress(var1.toLowerCase(Locale.ROOT));
      } else {
         var8 = var1;
      }

      StringBuilder var9;
      if (var5 != null) {
         var6 = var5.iterator();

         do {
            if (!var6.hasNext()) {
               var9 = new StringBuilder();
               var9.append("Certificate for <");
               var9.append(var1);
               var9.append("> doesn't match any ");
               var9.append("of the subject alternative names: ");
               var9.append(var5);
               throw new SSLException(var9.toString());
            }

            var7 = (String)var6.next();
            if (InetAddressUtils.isIPv6Address(var7)) {
               var7 = DefaultHostnameVerifier.normaliseAddress(var7);
            }
         } while(!matchIdentity(var8, var7, var4));

      } else if (var7 != null) {
         String var11;
         if (InetAddressUtils.isIPv6Address(var7)) {
            var11 = DefaultHostnameVerifier.normaliseAddress(var7);
         } else {
            var11 = var7;
         }

         if (!matchIdentity(var8, var11, var4)) {
            StringBuilder var10 = new StringBuilder();
            var10.append("Certificate for <");
            var10.append(var1);
            var10.append("> doesn't match ");
            var10.append("common name of the certificate subject: ");
            var10.append(var7);
            throw new SSLException(var10.toString());
         }
      } else {
         var9 = new StringBuilder();
         var9.append("Certificate subject for <");
         var9.append(var1);
         var9.append("> doesn't contain ");
         var9.append("a common name and does not have alternative names");
         throw new SSLException(var9.toString());
      }
   }

   public final boolean verify(String var1, SSLSession var2) {
      try {
         this.verify(var1, (X509Certificate)var2.getPeerCertificates()[0]);
         return true;
      } catch (SSLException var3) {
         if (this.log.isDebugEnabled()) {
            this.log.debug(var3.getMessage(), var3);
         }

         return false;
      }
   }
}
