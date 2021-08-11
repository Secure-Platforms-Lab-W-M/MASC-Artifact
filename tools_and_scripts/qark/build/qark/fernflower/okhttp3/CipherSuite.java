package okhttp3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class CipherSuite {
   private static final Map INSTANCES;
   static final Comparator ORDER_BY_NAME = new Comparator() {
      public int compare(String var1, String var2) {
         int var3 = 4;

         int var4;
         for(var4 = Math.min(var1.length(), var2.length()); var3 < var4; ++var3) {
            char var5 = var1.charAt(var3);
            char var6 = var2.charAt(var3);
            if (var5 != var6) {
               if (var5 < var6) {
                  return -1;
               }

               return 1;
            }
         }

         var3 = var1.length();
         var4 = var2.length();
         if (var3 != var4) {
            if (var3 < var4) {
               return -1;
            } else {
               return 1;
            }
         } else {
            return 0;
         }
      }
   };
   public static final CipherSuite TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA;
   public static final CipherSuite TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA;
   public static final CipherSuite TLS_DHE_DSS_WITH_AES_128_CBC_SHA;
   public static final CipherSuite TLS_DHE_DSS_WITH_AES_128_CBC_SHA256;
   public static final CipherSuite TLS_DHE_DSS_WITH_AES_128_GCM_SHA256;
   public static final CipherSuite TLS_DHE_DSS_WITH_AES_256_CBC_SHA;
   public static final CipherSuite TLS_DHE_DSS_WITH_AES_256_CBC_SHA256;
   public static final CipherSuite TLS_DHE_DSS_WITH_AES_256_GCM_SHA384;
   public static final CipherSuite TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA;
   public static final CipherSuite TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA;
   public static final CipherSuite TLS_DHE_DSS_WITH_DES_CBC_SHA;
   public static final CipherSuite TLS_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA;
   public static final CipherSuite TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA;
   public static final CipherSuite TLS_DHE_RSA_WITH_AES_128_CBC_SHA;
   public static final CipherSuite TLS_DHE_RSA_WITH_AES_128_CBC_SHA256;
   public static final CipherSuite TLS_DHE_RSA_WITH_AES_128_GCM_SHA256;
   public static final CipherSuite TLS_DHE_RSA_WITH_AES_256_CBC_SHA;
   public static final CipherSuite TLS_DHE_RSA_WITH_AES_256_CBC_SHA256;
   public static final CipherSuite TLS_DHE_RSA_WITH_AES_256_GCM_SHA384;
   public static final CipherSuite TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA;
   public static final CipherSuite TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA;
   public static final CipherSuite TLS_DHE_RSA_WITH_DES_CBC_SHA;
   public static final CipherSuite TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA;
   public static final CipherSuite TLS_DH_anon_EXPORT_WITH_RC4_40_MD5;
   public static final CipherSuite TLS_DH_anon_WITH_3DES_EDE_CBC_SHA;
   public static final CipherSuite TLS_DH_anon_WITH_AES_128_CBC_SHA;
   public static final CipherSuite TLS_DH_anon_WITH_AES_128_CBC_SHA256;
   public static final CipherSuite TLS_DH_anon_WITH_AES_128_GCM_SHA256;
   public static final CipherSuite TLS_DH_anon_WITH_AES_256_CBC_SHA;
   public static final CipherSuite TLS_DH_anon_WITH_AES_256_CBC_SHA256;
   public static final CipherSuite TLS_DH_anon_WITH_AES_256_GCM_SHA384;
   public static final CipherSuite TLS_DH_anon_WITH_DES_CBC_SHA;
   public static final CipherSuite TLS_DH_anon_WITH_RC4_128_MD5;
   public static final CipherSuite TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA;
   public static final CipherSuite TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA;
   public static final CipherSuite TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256;
   public static final CipherSuite TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256;
   public static final CipherSuite TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA;
   public static final CipherSuite TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384;
   public static final CipherSuite TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384;
   public static final CipherSuite TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256;
   public static final CipherSuite TLS_ECDHE_ECDSA_WITH_NULL_SHA;
   public static final CipherSuite TLS_ECDHE_ECDSA_WITH_RC4_128_SHA;
   public static final CipherSuite TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA;
   public static final CipherSuite TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA;
   public static final CipherSuite TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA;
   public static final CipherSuite TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA;
   public static final CipherSuite TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256;
   public static final CipherSuite TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256;
   public static final CipherSuite TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA;
   public static final CipherSuite TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384;
   public static final CipherSuite TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384;
   public static final CipherSuite TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256;
   public static final CipherSuite TLS_ECDHE_RSA_WITH_NULL_SHA;
   public static final CipherSuite TLS_ECDHE_RSA_WITH_RC4_128_SHA;
   public static final CipherSuite TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA;
   public static final CipherSuite TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA;
   public static final CipherSuite TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256;
   public static final CipherSuite TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256;
   public static final CipherSuite TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA;
   public static final CipherSuite TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384;
   public static final CipherSuite TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384;
   public static final CipherSuite TLS_ECDH_ECDSA_WITH_NULL_SHA;
   public static final CipherSuite TLS_ECDH_ECDSA_WITH_RC4_128_SHA;
   public static final CipherSuite TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA;
   public static final CipherSuite TLS_ECDH_RSA_WITH_AES_128_CBC_SHA;
   public static final CipherSuite TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256;
   public static final CipherSuite TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256;
   public static final CipherSuite TLS_ECDH_RSA_WITH_AES_256_CBC_SHA;
   public static final CipherSuite TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384;
   public static final CipherSuite TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384;
   public static final CipherSuite TLS_ECDH_RSA_WITH_NULL_SHA;
   public static final CipherSuite TLS_ECDH_RSA_WITH_RC4_128_SHA;
   public static final CipherSuite TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA;
   public static final CipherSuite TLS_ECDH_anon_WITH_AES_128_CBC_SHA;
   public static final CipherSuite TLS_ECDH_anon_WITH_AES_256_CBC_SHA;
   public static final CipherSuite TLS_ECDH_anon_WITH_NULL_SHA;
   public static final CipherSuite TLS_ECDH_anon_WITH_RC4_128_SHA;
   public static final CipherSuite TLS_EMPTY_RENEGOTIATION_INFO_SCSV;
   public static final CipherSuite TLS_FALLBACK_SCSV;
   public static final CipherSuite TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5;
   public static final CipherSuite TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA;
   public static final CipherSuite TLS_KRB5_EXPORT_WITH_RC4_40_MD5;
   public static final CipherSuite TLS_KRB5_EXPORT_WITH_RC4_40_SHA;
   public static final CipherSuite TLS_KRB5_WITH_3DES_EDE_CBC_MD5;
   public static final CipherSuite TLS_KRB5_WITH_3DES_EDE_CBC_SHA;
   public static final CipherSuite TLS_KRB5_WITH_DES_CBC_MD5;
   public static final CipherSuite TLS_KRB5_WITH_DES_CBC_SHA;
   public static final CipherSuite TLS_KRB5_WITH_RC4_128_MD5;
   public static final CipherSuite TLS_KRB5_WITH_RC4_128_SHA;
   public static final CipherSuite TLS_PSK_WITH_3DES_EDE_CBC_SHA;
   public static final CipherSuite TLS_PSK_WITH_AES_128_CBC_SHA;
   public static final CipherSuite TLS_PSK_WITH_AES_256_CBC_SHA;
   public static final CipherSuite TLS_PSK_WITH_RC4_128_SHA;
   public static final CipherSuite TLS_RSA_EXPORT_WITH_DES40_CBC_SHA;
   public static final CipherSuite TLS_RSA_EXPORT_WITH_RC4_40_MD5;
   public static final CipherSuite TLS_RSA_WITH_3DES_EDE_CBC_SHA;
   public static final CipherSuite TLS_RSA_WITH_AES_128_CBC_SHA;
   public static final CipherSuite TLS_RSA_WITH_AES_128_CBC_SHA256;
   public static final CipherSuite TLS_RSA_WITH_AES_128_GCM_SHA256;
   public static final CipherSuite TLS_RSA_WITH_AES_256_CBC_SHA;
   public static final CipherSuite TLS_RSA_WITH_AES_256_CBC_SHA256;
   public static final CipherSuite TLS_RSA_WITH_AES_256_GCM_SHA384;
   public static final CipherSuite TLS_RSA_WITH_CAMELLIA_128_CBC_SHA;
   public static final CipherSuite TLS_RSA_WITH_CAMELLIA_256_CBC_SHA;
   public static final CipherSuite TLS_RSA_WITH_DES_CBC_SHA;
   public static final CipherSuite TLS_RSA_WITH_NULL_MD5;
   public static final CipherSuite TLS_RSA_WITH_NULL_SHA;
   public static final CipherSuite TLS_RSA_WITH_NULL_SHA256;
   public static final CipherSuite TLS_RSA_WITH_RC4_128_MD5;
   public static final CipherSuite TLS_RSA_WITH_RC4_128_SHA;
   public static final CipherSuite TLS_RSA_WITH_SEED_CBC_SHA;
   final String javaName;

   static {
      INSTANCES = new TreeMap(ORDER_BY_NAME);
      TLS_RSA_WITH_NULL_MD5 = method_42("SSL_RSA_WITH_NULL_MD5", 1);
      TLS_RSA_WITH_NULL_SHA = method_42("SSL_RSA_WITH_NULL_SHA", 2);
      TLS_RSA_EXPORT_WITH_RC4_40_MD5 = method_42("SSL_RSA_EXPORT_WITH_RC4_40_MD5", 3);
      TLS_RSA_WITH_RC4_128_MD5 = method_42("SSL_RSA_WITH_RC4_128_MD5", 4);
      TLS_RSA_WITH_RC4_128_SHA = method_42("SSL_RSA_WITH_RC4_128_SHA", 5);
      TLS_RSA_EXPORT_WITH_DES40_CBC_SHA = method_42("SSL_RSA_EXPORT_WITH_DES40_CBC_SHA", 8);
      TLS_RSA_WITH_DES_CBC_SHA = method_42("SSL_RSA_WITH_DES_CBC_SHA", 9);
      TLS_RSA_WITH_3DES_EDE_CBC_SHA = method_42("SSL_RSA_WITH_3DES_EDE_CBC_SHA", 10);
      TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA = method_42("SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA", 17);
      TLS_DHE_DSS_WITH_DES_CBC_SHA = method_42("SSL_DHE_DSS_WITH_DES_CBC_SHA", 18);
      TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA = method_42("SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA", 19);
      TLS_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA = method_42("SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA", 20);
      TLS_DHE_RSA_WITH_DES_CBC_SHA = method_42("SSL_DHE_RSA_WITH_DES_CBC_SHA", 21);
      TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA = method_42("SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA", 22);
      TLS_DH_anon_EXPORT_WITH_RC4_40_MD5 = method_42("SSL_DH_anon_EXPORT_WITH_RC4_40_MD5", 23);
      TLS_DH_anon_WITH_RC4_128_MD5 = method_42("SSL_DH_anon_WITH_RC4_128_MD5", 24);
      TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA = method_42("SSL_DH_anon_EXPORT_WITH_DES40_CBC_SHA", 25);
      TLS_DH_anon_WITH_DES_CBC_SHA = method_42("SSL_DH_anon_WITH_DES_CBC_SHA", 26);
      TLS_DH_anon_WITH_3DES_EDE_CBC_SHA = method_42("SSL_DH_anon_WITH_3DES_EDE_CBC_SHA", 27);
      TLS_KRB5_WITH_DES_CBC_SHA = method_42("TLS_KRB5_WITH_DES_CBC_SHA", 30);
      TLS_KRB5_WITH_3DES_EDE_CBC_SHA = method_42("TLS_KRB5_WITH_3DES_EDE_CBC_SHA", 31);
      TLS_KRB5_WITH_RC4_128_SHA = method_42("TLS_KRB5_WITH_RC4_128_SHA", 32);
      TLS_KRB5_WITH_DES_CBC_MD5 = method_42("TLS_KRB5_WITH_DES_CBC_MD5", 34);
      TLS_KRB5_WITH_3DES_EDE_CBC_MD5 = method_42("TLS_KRB5_WITH_3DES_EDE_CBC_MD5", 35);
      TLS_KRB5_WITH_RC4_128_MD5 = method_42("TLS_KRB5_WITH_RC4_128_MD5", 36);
      TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA = method_42("TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA", 38);
      TLS_KRB5_EXPORT_WITH_RC4_40_SHA = method_42("TLS_KRB5_EXPORT_WITH_RC4_40_SHA", 40);
      TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5 = method_42("TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5", 41);
      TLS_KRB5_EXPORT_WITH_RC4_40_MD5 = method_42("TLS_KRB5_EXPORT_WITH_RC4_40_MD5", 43);
      TLS_RSA_WITH_AES_128_CBC_SHA = method_42("TLS_RSA_WITH_AES_128_CBC_SHA", 47);
      TLS_DHE_DSS_WITH_AES_128_CBC_SHA = method_42("TLS_DHE_DSS_WITH_AES_128_CBC_SHA", 50);
      TLS_DHE_RSA_WITH_AES_128_CBC_SHA = method_42("TLS_DHE_RSA_WITH_AES_128_CBC_SHA", 51);
      TLS_DH_anon_WITH_AES_128_CBC_SHA = method_42("TLS_DH_anon_WITH_AES_128_CBC_SHA", 52);
      TLS_RSA_WITH_AES_256_CBC_SHA = method_42("TLS_RSA_WITH_AES_256_CBC_SHA", 53);
      TLS_DHE_DSS_WITH_AES_256_CBC_SHA = method_42("TLS_DHE_DSS_WITH_AES_256_CBC_SHA", 56);
      TLS_DHE_RSA_WITH_AES_256_CBC_SHA = method_42("TLS_DHE_RSA_WITH_AES_256_CBC_SHA", 57);
      TLS_DH_anon_WITH_AES_256_CBC_SHA = method_42("TLS_DH_anon_WITH_AES_256_CBC_SHA", 58);
      TLS_RSA_WITH_NULL_SHA256 = method_42("TLS_RSA_WITH_NULL_SHA256", 59);
      TLS_RSA_WITH_AES_128_CBC_SHA256 = method_42("TLS_RSA_WITH_AES_128_CBC_SHA256", 60);
      TLS_RSA_WITH_AES_256_CBC_SHA256 = method_42("TLS_RSA_WITH_AES_256_CBC_SHA256", 61);
      TLS_DHE_DSS_WITH_AES_128_CBC_SHA256 = method_42("TLS_DHE_DSS_WITH_AES_128_CBC_SHA256", 64);
      TLS_RSA_WITH_CAMELLIA_128_CBC_SHA = method_42("TLS_RSA_WITH_CAMELLIA_128_CBC_SHA", 65);
      TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA = method_42("TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA", 68);
      TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA = method_42("TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA", 69);
      TLS_DHE_RSA_WITH_AES_128_CBC_SHA256 = method_42("TLS_DHE_RSA_WITH_AES_128_CBC_SHA256", 103);
      TLS_DHE_DSS_WITH_AES_256_CBC_SHA256 = method_42("TLS_DHE_DSS_WITH_AES_256_CBC_SHA256", 106);
      TLS_DHE_RSA_WITH_AES_256_CBC_SHA256 = method_42("TLS_DHE_RSA_WITH_AES_256_CBC_SHA256", 107);
      TLS_DH_anon_WITH_AES_128_CBC_SHA256 = method_42("TLS_DH_anon_WITH_AES_128_CBC_SHA256", 108);
      TLS_DH_anon_WITH_AES_256_CBC_SHA256 = method_42("TLS_DH_anon_WITH_AES_256_CBC_SHA256", 109);
      TLS_RSA_WITH_CAMELLIA_256_CBC_SHA = method_42("TLS_RSA_WITH_CAMELLIA_256_CBC_SHA", 132);
      TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA = method_42("TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA", 135);
      TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA = method_42("TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA", 136);
      TLS_PSK_WITH_RC4_128_SHA = method_42("TLS_PSK_WITH_RC4_128_SHA", 138);
      TLS_PSK_WITH_3DES_EDE_CBC_SHA = method_42("TLS_PSK_WITH_3DES_EDE_CBC_SHA", 139);
      TLS_PSK_WITH_AES_128_CBC_SHA = method_42("TLS_PSK_WITH_AES_128_CBC_SHA", 140);
      TLS_PSK_WITH_AES_256_CBC_SHA = method_42("TLS_PSK_WITH_AES_256_CBC_SHA", 141);
      TLS_RSA_WITH_SEED_CBC_SHA = method_42("TLS_RSA_WITH_SEED_CBC_SHA", 150);
      TLS_RSA_WITH_AES_128_GCM_SHA256 = method_42("TLS_RSA_WITH_AES_128_GCM_SHA256", 156);
      TLS_RSA_WITH_AES_256_GCM_SHA384 = method_42("TLS_RSA_WITH_AES_256_GCM_SHA384", 157);
      TLS_DHE_RSA_WITH_AES_128_GCM_SHA256 = method_42("TLS_DHE_RSA_WITH_AES_128_GCM_SHA256", 158);
      TLS_DHE_RSA_WITH_AES_256_GCM_SHA384 = method_42("TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", 159);
      TLS_DHE_DSS_WITH_AES_128_GCM_SHA256 = method_42("TLS_DHE_DSS_WITH_AES_128_GCM_SHA256", 162);
      TLS_DHE_DSS_WITH_AES_256_GCM_SHA384 = method_42("TLS_DHE_DSS_WITH_AES_256_GCM_SHA384", 163);
      TLS_DH_anon_WITH_AES_128_GCM_SHA256 = method_42("TLS_DH_anon_WITH_AES_128_GCM_SHA256", 166);
      TLS_DH_anon_WITH_AES_256_GCM_SHA384 = method_42("TLS_DH_anon_WITH_AES_256_GCM_SHA384", 167);
      TLS_EMPTY_RENEGOTIATION_INFO_SCSV = method_42("TLS_EMPTY_RENEGOTIATION_INFO_SCSV", 255);
      TLS_FALLBACK_SCSV = method_42("TLS_FALLBACK_SCSV", 22016);
      TLS_ECDH_ECDSA_WITH_NULL_SHA = method_42("TLS_ECDH_ECDSA_WITH_NULL_SHA", 49153);
      TLS_ECDH_ECDSA_WITH_RC4_128_SHA = method_42("TLS_ECDH_ECDSA_WITH_RC4_128_SHA", 49154);
      TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA = method_42("TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA", 49155);
      TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA = method_42("TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA", 49156);
      TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA = method_42("TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA", 49157);
      TLS_ECDHE_ECDSA_WITH_NULL_SHA = method_42("TLS_ECDHE_ECDSA_WITH_NULL_SHA", 49158);
      TLS_ECDHE_ECDSA_WITH_RC4_128_SHA = method_42("TLS_ECDHE_ECDSA_WITH_RC4_128_SHA", 49159);
      TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA = method_42("TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA", 49160);
      TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA = method_42("TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA", 49161);
      TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA = method_42("TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA", 49162);
      TLS_ECDH_RSA_WITH_NULL_SHA = method_42("TLS_ECDH_RSA_WITH_NULL_SHA", 49163);
      TLS_ECDH_RSA_WITH_RC4_128_SHA = method_42("TLS_ECDH_RSA_WITH_RC4_128_SHA", 49164);
      TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA = method_42("TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA", 49165);
      TLS_ECDH_RSA_WITH_AES_128_CBC_SHA = method_42("TLS_ECDH_RSA_WITH_AES_128_CBC_SHA", 49166);
      TLS_ECDH_RSA_WITH_AES_256_CBC_SHA = method_42("TLS_ECDH_RSA_WITH_AES_256_CBC_SHA", 49167);
      TLS_ECDHE_RSA_WITH_NULL_SHA = method_42("TLS_ECDHE_RSA_WITH_NULL_SHA", 49168);
      TLS_ECDHE_RSA_WITH_RC4_128_SHA = method_42("TLS_ECDHE_RSA_WITH_RC4_128_SHA", 49169);
      TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA = method_42("TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA", 49170);
      TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA = method_42("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", 49171);
      TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA = method_42("TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", 49172);
      TLS_ECDH_anon_WITH_NULL_SHA = method_42("TLS_ECDH_anon_WITH_NULL_SHA", 49173);
      TLS_ECDH_anon_WITH_RC4_128_SHA = method_42("TLS_ECDH_anon_WITH_RC4_128_SHA", 49174);
      TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA = method_42("TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA", 49175);
      TLS_ECDH_anon_WITH_AES_128_CBC_SHA = method_42("TLS_ECDH_anon_WITH_AES_128_CBC_SHA", 49176);
      TLS_ECDH_anon_WITH_AES_256_CBC_SHA = method_42("TLS_ECDH_anon_WITH_AES_256_CBC_SHA", 49177);
      TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256 = method_42("TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256", 49187);
      TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384 = method_42("TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384", 49188);
      TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256 = method_42("TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256", 49189);
      TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384 = method_42("TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384", 49190);
      TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256 = method_42("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256", 49191);
      TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384 = method_42("TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384", 49192);
      TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256 = method_42("TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256", 49193);
      TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384 = method_42("TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384", 49194);
      TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256 = method_42("TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", 49195);
      TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 = method_42("TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", 49196);
      TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256 = method_42("TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256", 49197);
      TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384 = method_42("TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384", 49198);
      TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 = method_42("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", 49199);
      TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 = method_42("TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", 49200);
      TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256 = method_42("TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256", 49201);
      TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384 = method_42("TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384", 49202);
      TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA = method_42("TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA", 49205);
      TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA = method_42("TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA", 49206);
      TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256 = method_42("TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256", 52392);
      TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256 = method_42("TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256", 52393);
   }

   private CipherSuite(String var1) {
      if (var1 != null) {
         this.javaName = var1;
      } else {
         throw null;
      }
   }

   public static CipherSuite forJavaName(String var0) {
      synchronized(CipherSuite.class){}

      CipherSuite var1;
      label71: {
         Throwable var10000;
         label75: {
            boolean var10001;
            CipherSuite var2;
            try {
               var2 = (CipherSuite)INSTANCES.get(var0);
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label75;
            }

            var1 = var2;
            if (var2 != null) {
               break label71;
            }

            label66:
            try {
               var1 = new CipherSuite(var0);
               INSTANCES.put(var0, var1);
               break label71;
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label66;
            }
         }

         Throwable var9 = var10000;
         throw var9;
      }

      return var1;
   }

   static List forJavaNames(String... var0) {
      ArrayList var3 = new ArrayList(var0.length);
      int var2 = var0.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         var3.add(forJavaName(var0[var1]));
      }

      return Collections.unmodifiableList(var3);
   }

   // $FF: renamed from: of (java.lang.String, int) okhttp3.CipherSuite
   private static CipherSuite method_42(String var0, int var1) {
      return forJavaName(var0);
   }

   public String javaName() {
      return this.javaName;
   }

   public String toString() {
      return this.javaName;
   }
}
