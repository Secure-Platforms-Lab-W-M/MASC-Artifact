package okhttp3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum TlsVersion {
   SSL_3_0,
   TLS_1_0("TLSv1"),
   TLS_1_1("TLSv1.1"),
   TLS_1_2("TLSv1.2"),
   TLS_1_3("TLSv1.3");

   final String javaName;

   static {
      TlsVersion var0 = new TlsVersion("SSL_3_0", 4, "SSLv3");
      SSL_3_0 = var0;
   }

   private TlsVersion(String var3) {
      this.javaName = var3;
   }

   public static TlsVersion forJavaName(String var0) {
      byte var3;
      label51: {
         int var1 = var0.hashCode();
         if (var1 != 79201641) {
            if (var1 != 79923350) {
               switch(var1) {
               case -503070503:
                  if (var0.equals("TLSv1.1")) {
                     var3 = 2;
                     break label51;
                  }
                  break;
               case -503070502:
                  if (var0.equals("TLSv1.2")) {
                     var3 = 1;
                     break label51;
                  }
                  break;
               case -503070501:
                  if (var0.equals("TLSv1.3")) {
                     var3 = 0;
                     break label51;
                  }
               }
            } else if (var0.equals("TLSv1")) {
               var3 = 3;
               break label51;
            }
         } else if (var0.equals("SSLv3")) {
            var3 = 4;
            break label51;
         }

         var3 = -1;
      }

      if (var3 != 0) {
         if (var3 != 1) {
            if (var3 != 2) {
               if (var3 != 3) {
                  if (var3 == 4) {
                     return SSL_3_0;
                  } else {
                     StringBuilder var2 = new StringBuilder();
                     var2.append("Unexpected TLS version: ");
                     var2.append(var0);
                     throw new IllegalArgumentException(var2.toString());
                  }
               } else {
                  return TLS_1_0;
               }
            } else {
               return TLS_1_1;
            }
         } else {
            return TLS_1_2;
         }
      } else {
         return TLS_1_3;
      }
   }

   static List forJavaNames(String... var0) {
      ArrayList var3 = new ArrayList(var0.length);
      int var2 = var0.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         var3.add(forJavaName(var0[var1]));
      }

      return Collections.unmodifiableList(var3);
   }

   public String javaName() {
      return this.javaName;
   }
}
