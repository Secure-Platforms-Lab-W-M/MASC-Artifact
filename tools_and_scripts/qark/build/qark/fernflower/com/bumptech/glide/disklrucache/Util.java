package com.bumptech.glide.disklrucache;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

final class Util {
   static final Charset US_ASCII = Charset.forName("US-ASCII");
   static final Charset UTF_8 = Charset.forName("UTF-8");

   private Util() {
   }

   static void closeQuietly(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (RuntimeException var1) {
            throw var1;
         } catch (Exception var2) {
         }
      }
   }

   static void deleteContents(File var0) throws IOException {
      File[] var3 = var0.listFiles();
      StringBuilder var4;
      if (var3 != null) {
         int var2 = var3.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            var0 = var3[var1];
            if (var0.isDirectory()) {
               deleteContents(var0);
            }

            if (!var0.delete()) {
               var4 = new StringBuilder();
               var4.append("failed to delete file: ");
               var4.append(var0);
               throw new IOException(var4.toString());
            }
         }

      } else {
         var4 = new StringBuilder();
         var4.append("not a readable directory: ");
         var4.append(var0);
         throw new IOException(var4.toString());
      }
   }

   static String readFully(Reader var0) throws IOException {
      String var25;
      label158: {
         Throwable var10000;
         label157: {
            boolean var10001;
            StringWriter var2;
            char[] var3;
            try {
               var2 = new StringWriter();
               var3 = new char[1024];
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label157;
            }

            while(true) {
               int var1;
               try {
                  var1 = var0.read(var3);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break;
               }

               if (var1 == -1) {
                  try {
                     var25 = var2.toString();
                     break label158;
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var2.write(var3, 0, var1);
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var24 = var10000;
         var0.close();
         throw var24;
      }

      var0.close();
      return var25;
   }
}
