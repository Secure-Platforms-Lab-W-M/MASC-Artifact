package org.apache.http.conn.ssl;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.util.DomainType;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.conn.util.PublicSuffixMatcher;

public final class DefaultHostnameVerifier implements HostnameVerifier {
   private final Log log;
   private final PublicSuffixMatcher publicSuffixMatcher;

   public DefaultHostnameVerifier() {
      this((PublicSuffixMatcher)null);
   }

   public DefaultHostnameVerifier(PublicSuffixMatcher var1) {
      this.log = LogFactory.getLog(this.getClass());
      this.publicSuffixMatcher = var1;
   }

   static DefaultHostnameVerifier.HostNameType determineHostFormat(String var0) {
      if (InetAddressUtils.isIPv4Address(var0)) {
         return DefaultHostnameVerifier.HostNameType.IPv4;
      } else {
         String var2 = var0;
         if (var0.startsWith("[")) {
            var2 = var0;
            if (var0.endsWith("]")) {
               var2 = var0.substring(1, var0.length() - 1);
            }
         }

         return InetAddressUtils.isIPv6Address(var2) ? DefaultHostnameVerifier.HostNameType.IPv6 : DefaultHostnameVerifier.HostNameType.DNS;
      }
   }

   static String extractCN(String param0) throws SSLException {
      // $FF: Couldn't be decompiled
   }

   static List getSubjectAltNames(X509Certificate var0) {
      boolean var10001;
      Collection var12;
      try {
         var12 = var0.getSubjectAlternativeNames();
      } catch (CertificateParsingException var11) {
         var10001 = false;
         return Collections.emptyList();
      }

      if (var12 == null) {
         try {
            return Collections.emptyList();
         } catch (CertificateParsingException var5) {
            var10001 = false;
         }
      } else {
         ArrayList var2;
         Iterator var3;
         try {
            var2 = new ArrayList();
            var3 = var12.iterator();
         } catch (CertificateParsingException var10) {
            var10001 = false;
            return Collections.emptyList();
         }

         while(true) {
            List var4;
            Integer var13;
            label62: {
               try {
                  if (!var3.hasNext()) {
                     return var2;
                  }

                  var4 = (List)var3.next();
                  if (var4.size() >= 2) {
                     var13 = (Integer)var4.get(0);
                     break label62;
                  }
               } catch (CertificateParsingException var9) {
                  var10001 = false;
                  break;
               }

               var13 = null;
            }

            if (var13 != null) {
               try {
                  if (var13 != 2 && var13 != 7) {
                     continue;
                  }
               } catch (CertificateParsingException var7) {
                  var10001 = false;
                  break;
               }

               Object var14;
               try {
                  var14 = var4.get(1);
                  if (var14 instanceof String) {
                     var2.add(new SubjectName((String)var14, var13));
                     continue;
                  }
               } catch (CertificateParsingException var8) {
                  var10001 = false;
                  break;
               }

               try {
                  boolean var1 = var14 instanceof byte[];
               } catch (CertificateParsingException var6) {
                  var10001 = false;
                  break;
               }
            }
         }
      }

      return Collections.emptyList();
   }

   static void matchCN(String var0, String var1, PublicSuffixMatcher var2) throws SSLException {
      if (!matchIdentityStrict(var0.toLowerCase(Locale.ROOT), var1.toLowerCase(Locale.ROOT), var2)) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Certificate for <");
         var3.append(var0);
         var3.append("> doesn't match ");
         var3.append("common name of the certificate subject: ");
         var3.append(var1);
         throw new SSLPeerUnverifiedException(var3.toString());
      }
   }

   static void matchDNSName(String var0, List var1, PublicSuffixMatcher var2) throws SSLException {
      String var4 = var0.toLowerCase(Locale.ROOT);

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         SubjectName var5 = (SubjectName)var1.get(var3);
         if (var5.getType() == 2 && matchIdentityStrict(var4, var5.getValue().toLowerCase(Locale.ROOT), var2)) {
            return;
         }
      }

      StringBuilder var6 = new StringBuilder();
      var6.append("Certificate for <");
      var6.append(var0);
      var6.append("> doesn't match any ");
      var6.append("of the subject alternative names: ");
      var6.append(var1);
      throw new SSLPeerUnverifiedException(var6.toString());
   }

   static boolean matchDomainRoot(String var0, String var1) {
      boolean var3 = false;
      if (var1 == null) {
         return false;
      } else {
         boolean var2 = var3;
         if (var0.endsWith(var1)) {
            if (var0.length() != var1.length()) {
               var2 = var3;
               if (var0.charAt(var0.length() - var1.length() - 1) != '.') {
                  return var2;
               }
            }

            var2 = true;
         }

         return var2;
      }
   }

   static void matchIPAddress(String var0, List var1) throws SSLException {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         SubjectName var3 = (SubjectName)var1.get(var2);
         if (var3.getType() == 7 && var0.equals(var3.getValue())) {
            return;
         }
      }

      StringBuilder var4 = new StringBuilder();
      var4.append("Certificate for <");
      var4.append(var0);
      var4.append("> doesn't match any ");
      var4.append("of the subject alternative names: ");
      var4.append(var1);
      throw new SSLPeerUnverifiedException(var4.toString());
   }

   static void matchIPv6Address(String var0, List var1) throws SSLException {
      String var3 = normaliseAddress(var0);

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         SubjectName var4 = (SubjectName)var1.get(var2);
         if (var4.getType() == 7 && var3.equals(normaliseAddress(var4.getValue()))) {
            return;
         }
      }

      StringBuilder var5 = new StringBuilder();
      var5.append("Certificate for <");
      var5.append(var0);
      var5.append("> doesn't match any ");
      var5.append("of the subject alternative names: ");
      var5.append(var1);
      throw new SSLPeerUnverifiedException(var5.toString());
   }

   static boolean matchIdentity(String var0, String var1) {
      return matchIdentity(var0, var1, (PublicSuffixMatcher)null, false);
   }

   static boolean matchIdentity(String var0, String var1, PublicSuffixMatcher var2) {
      return matchIdentity(var0, var1, var2, false);
   }

   private static boolean matchIdentity(String var0, String var1, PublicSuffixMatcher var2, boolean var3) {
      if (var2 != null && var0.contains(".") && !matchDomainRoot(var0, var2.getDomainRoot(var1, DomainType.ICANN))) {
         return false;
      } else {
         int var4 = var1.indexOf(42);
         if (var4 != -1) {
            String var5 = var1.substring(0, var4);
            var1 = var1.substring(var4 + 1);
            if (!var5.isEmpty() && !var0.startsWith(var5)) {
               return false;
            } else if (!var1.isEmpty() && !var0.endsWith(var1)) {
               return false;
            } else {
               return !var3 || !var0.substring(var5.length(), var0.length() - var1.length()).contains(".");
            }
         } else {
            return var0.equalsIgnoreCase(var1);
         }
      }
   }

   static boolean matchIdentityStrict(String var0, String var1) {
      return matchIdentity(var0, var1, (PublicSuffixMatcher)null, true);
   }

   static boolean matchIdentityStrict(String var0, String var1, PublicSuffixMatcher var2) {
      return matchIdentity(var0, var1, var2, true);
   }

   static String normaliseAddress(String var0) {
      if (var0 == null) {
         return var0;
      } else {
         try {
            String var1 = InetAddress.getByName(var0).getHostAddress();
            return var1;
         } catch (UnknownHostException var2) {
            return var0;
         }
      }
   }

   public void verify(String var1, X509Certificate var2) throws SSLException {
      DefaultHostnameVerifier.HostNameType var4 = determineHostFormat(var1);
      List var5 = getSubjectAltNames(var2);
      if (var5 != null && !var5.isEmpty()) {
         int var3 = null.$SwitchMap$org$apache$http$conn$ssl$DefaultHostnameVerifier$HostNameType[var4.ordinal()];
         if (var3 != 1) {
            if (var3 != 2) {
               matchDNSName(var1, var5, this.publicSuffixMatcher);
            } else {
               matchIPv6Address(var1, var5);
            }
         } else {
            matchIPAddress(var1, var5);
         }
      } else {
         String var6 = extractCN(var2.getSubjectX500Principal().getName("RFC2253"));
         if (var6 != null) {
            matchCN(var1, var6, this.publicSuffixMatcher);
         } else {
            StringBuilder var7 = new StringBuilder();
            var7.append("Certificate subject for <");
            var7.append(var1);
            var7.append("> doesn't contain ");
            var7.append("a common name and does not have alternative names");
            throw new SSLException(var7.toString());
         }
      }
   }

   public boolean verify(String var1, SSLSession var2) {
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

   static enum HostNameType {
      DNS,
      IPv4(7),
      IPv6(7);

      final int subjectType;

      static {
         DefaultHostnameVerifier.HostNameType var0 = new DefaultHostnameVerifier.HostNameType("DNS", 2, 2);
         DNS = var0;
      }

      private HostNameType(int var3) {
         this.subjectType = var3;
      }
   }
}
