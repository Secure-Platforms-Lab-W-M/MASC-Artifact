package org.apache.http.conn.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public final class PublicSuffixListParser {
   public PublicSuffixList parse(Reader var1) throws IOException {
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      BufferedReader var6 = new BufferedReader(var1);

      while(true) {
         String var7 = var6.readLine();
         String var3 = var7;
         if (var7 == null) {
            return new PublicSuffixList(DomainType.UNKNOWN, var4, var5);
         }

         if (!var7.isEmpty() && !var7.startsWith("//")) {
            var7 = var7;
            if (var3.startsWith(".")) {
               var7 = var3.substring(1);
            }

            boolean var2 = var7.startsWith("!");
            var3 = var7;
            if (var2) {
               var3 = var7.substring(1);
            }

            if (var2) {
               var5.add(var3);
            } else {
               var4.add(var3);
            }
         }
      }
   }

   public List parseByType(Reader var1) throws IOException {
      ArrayList var7 = new ArrayList(2);
      BufferedReader var8 = new BufferedReader(var1);
      new StringBuilder(256);
      DomainType var9 = null;
      ArrayList var3 = null;
      ArrayList var4 = null;

      while(true) {
         while(true) {
            while(true) {
               String var5;
               String var6;
               do {
                  var5 = var8.readLine();
                  var6 = var5;
                  if (var5 == null) {
                     return var7;
                  }
               } while(var5.isEmpty());

               if (var5.startsWith("//")) {
                  if (var9 == null) {
                     if (var5.contains("===BEGIN ICANN DOMAINS===")) {
                        var9 = DomainType.ICANN;
                     } else if (var5.contains("===BEGIN PRIVATE DOMAINS===")) {
                        var9 = DomainType.PRIVATE;
                     }
                  } else if (var5.contains("===END ICANN DOMAINS===") || var5.contains("===END PRIVATE DOMAINS===")) {
                     if (var3 != null) {
                        var7.add(new PublicSuffixList(var9, var3, var4));
                     }

                     var9 = null;
                     var3 = null;
                     var4 = null;
                  }
               } else if (var9 != null) {
                  var5 = var5;
                  if (var6.startsWith(".")) {
                     var5 = var6.substring(1);
                  }

                  boolean var2 = var5.startsWith("!");
                  var6 = var5;
                  if (var2) {
                     var6 = var5.substring(1);
                  }

                  ArrayList var10;
                  if (var2) {
                     var10 = var4;
                     if (var4 == null) {
                        var10 = new ArrayList();
                     }

                     var10.add(var6);
                     var4 = var10;
                  } else {
                     var10 = var3;
                     if (var3 == null) {
                        var10 = new ArrayList();
                     }

                     var10.add(var6);
                     var3 = var10;
                  }
               }
            }
         }
      }
   }
}
