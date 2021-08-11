package net.sf.fmj.utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

public class IOUtils {
   private static final int BUFFER_SIZE = 2048;

   private IOUtils() {
   }

   public static void copyFile(File var0, File var1) throws IOException {
      FileInputStream var2 = new FileInputStream(var0);
      FileOutputStream var3 = new FileOutputStream(var1);
      copyStream(var2, var3);
      var2.close();
      var3.close();
   }

   public static void copyFile(String var0, String var1) throws IOException {
      copyFile(new File(var0), new File(var1));
   }

   public static void copyStream(InputStream var0, OutputStream var1) throws IOException {
      byte[] var3 = new byte[2048];

      while(true) {
         int var2 = var0.read(var3);
         if (var2 == -1) {
            return;
         }

         var1.write(var3, 0, var2);
      }
   }

   public static String readAll(Reader var0) throws IOException {
      StringBuilder var2 = new StringBuilder();

      while(true) {
         int var1 = var0.read();
         if (var1 == -1) {
            return var2.toString();
         }

         var2.append((char)var1);
      }
   }

   public static byte[] readAll(InputStream var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      copyStream(var0, var1);
      var0.close();
      return var1.toByteArray();
   }

   public static String readAllToString(InputStream var0) throws IOException {
      return new String(readAll(var0));
   }

   public static String readAllToString(InputStream var0, String var1) throws IOException {
      return new String(readAll(var0), var1);
   }

   public static void readAllToStringBuffer(InputStream var0, String var1, StringBuffer var2) throws IOException {
      var2.append(readAllToString(var0, var1));
   }

   public static void readAllToStringBuffer(InputStream var0, StringBuffer var1) throws IOException {
      var1.append(readAllToString(var0));
   }

   public static void readAllToStringBuilder(InputStream var0, String var1, StringBuilder var2) throws IOException {
      var2.append(readAllToString(var0, var1));
   }

   public static void readAllToStringBuilder(InputStream var0, StringBuilder var1) throws IOException {
      var1.append(readAllToString(var0));
   }

   public static void writeStringToFile(String var0, String var1) throws IOException {
      FileOutputStream var2 = new FileOutputStream(var1);
      var2.write(var0.getBytes());
      var2.close();
   }
}
