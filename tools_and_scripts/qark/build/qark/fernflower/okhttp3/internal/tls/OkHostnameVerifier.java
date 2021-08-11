package okhttp3.internal.tls;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import okhttp3.internal.Util;

public final class OkHostnameVerifier implements HostnameVerifier {
   private static final int ALT_DNS_NAME = 2;
   private static final int ALT_IPA_NAME = 7;
   public static final OkHostnameVerifier INSTANCE = new OkHostnameVerifier();

   private OkHostnameVerifier() {
   }

   public static List allSubjectAltNames(X509Certificate var0) {
      List var1 = getSubjectAltNames(var0, 7);
      List var3 = getSubjectAltNames(var0, 2);
      ArrayList var2 = new ArrayList(var1.size() + var3.size());
      var2.addAll(var1);
      var2.addAll(var3);
      return var2;
   }

   private static List getSubjectAltNames(X509Certificate var0, int var1) {
      ArrayList var2 = new ArrayList();

      boolean var10001;
      Collection var13;
      try {
         var13 = var0.getSubjectAlternativeNames();
      } catch (CertificateParsingException var12) {
         var10001 = false;
         return Collections.emptyList();
      }

      if (var13 == null) {
         try {
            return Collections.emptyList();
         } catch (CertificateParsingException var5) {
            var10001 = false;
         }
      } else {
         Iterator var14;
         try {
            var14 = var13.iterator();
         } catch (CertificateParsingException var11) {
            var10001 = false;
            return Collections.emptyList();
         }

         while(true) {
            List var3;
            try {
               if (!var14.hasNext()) {
                  return var2;
               }

               var3 = (List)var14.next();
            } catch (CertificateParsingException var9) {
               var10001 = false;
               break;
            }

            if (var3 != null) {
               try {
                  if (var3.size() < 2) {
                     continue;
                  }
               } catch (CertificateParsingException var10) {
                  var10001 = false;
                  break;
               }

               Integer var4;
               try {
                  var4 = (Integer)var3.get(0);
               } catch (CertificateParsingException var8) {
                  var10001 = false;
                  break;
               }

               if (var4 != null) {
                  String var15;
                  try {
                     if (var4 != var1) {
                        continue;
                     }

                     var15 = (String)var3.get(1);
                  } catch (CertificateParsingException var7) {
                     var10001 = false;
                     break;
                  }

                  if (var15 != null) {
                     try {
                        var2.add(var15);
                     } catch (CertificateParsingException var6) {
                        var10001 = false;
                        break;
                     }
                  }
               }
            }
         }
      }

      return Collections.emptyList();
   }

   private boolean verifyHostname(String var1, X509Certificate var2) {
      var1 = var1.toLowerCase(Locale.US);
      boolean var4 = false;
      List var6 = getSubjectAltNames(var2, 2);
      int var3 = 0;

      for(int var5 = var6.size(); var3 < var5; ++var3) {
         var4 = true;
         if (this.verifyHostname(var1, (String)var6.get(var3))) {
            return true;
         }
      }

      if (!var4) {
         String var7 = (new DistinguishedNameParser(var2.getSubjectX500Principal())).findMostSpecific("cn");
         if (var7 != null) {
            return this.verifyHostname(var1, var7);
         }
      }

      return false;
   }

   private boolean verifyIpAddress(String var1, X509Certificate var2) {
      List var5 = getSubjectAltNames(var2, 7);
      int var3 = 0;

      for(int var4 = var5.size(); var3 < var4; ++var3) {
         if (var1.equalsIgnoreCase((String)var5.get(var3))) {
            return true;
         }
      }

      return false;
   }

   public boolean verify(String var1, X509Certificate var2) {
      return Util.verifyAsIpAddress(var1) ? this.verifyIpAddress(var1, var2) : this.verifyHostname(var1, var2);
   }

   public boolean verify(String var1, SSLSession var2) {
      try {
         boolean var3 = this.verify(var1, (X509Certificate)var2.getPeerCertificates()[0]);
         return var3;
      } catch (SSLException var4) {
         return false;
      }
   }

   public boolean verifyHostname(String var1, String var2) {
      if (var1 != null && var1.length() != 0 && !var1.startsWith(".")) {
         if (var1.endsWith("..")) {
            return false;
         } else if (var2 != null && var2.length() != 0 && !var2.startsWith(".")) {
            if (var2.endsWith("..")) {
               return false;
            } else {
               String var4 = var1;
               if (!var1.endsWith(".")) {
                  StringBuilder var6 = new StringBuilder();
                  var6.append(var1);
                  var6.append('.');
                  var4 = var6.toString();
               }

               var1 = var2;
               if (!var2.endsWith(".")) {
                  StringBuilder var5 = new StringBuilder();
                  var5.append(var2);
                  var5.append('.');
                  var1 = var5.toString();
               }

               var1 = var1.toLowerCase(Locale.US);
               if (!var1.contains("*")) {
                  return var4.equals(var1);
               } else if (var1.startsWith("*.")) {
                  if (var1.indexOf(42, 1) != -1) {
                     return false;
                  } else if (var4.length() < var1.length()) {
                     return false;
                  } else if ("*.".equals(var1)) {
                     return false;
                  } else {
                     var1 = var1.substring(1);
                     if (!var4.endsWith(var1)) {
                        return false;
                     } else {
                        int var3 = var4.length() - var1.length();
                        return var3 <= 0 || var4.lastIndexOf(46, var3 - 1) == -1;
                     }
                  }
               } else {
                  return false;
               }
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
