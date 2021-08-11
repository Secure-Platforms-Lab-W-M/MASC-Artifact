package org.apache.commons.codec.cli;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Locale;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

public class Digest {
   private final String algorithm;
   private final String[] args;
   private final String[] inputs;

   private Digest(String[] var1) {
      if (var1 != null) {
         if (var1.length != 0) {
            this.args = var1;
            this.algorithm = var1[0];
            if (var1.length <= 1) {
               this.inputs = null;
            } else {
               String[] var2 = new String[var1.length - 1];
               this.inputs = var2;
               System.arraycopy(var1, 1, var2, 0, var2.length);
            }
         } else {
            throw new IllegalArgumentException(String.format("Usage: java %s [algorithm] [FILE|DIRECTORY|string] ...", Digest.class.getName()));
         }
      } else {
         throw new IllegalArgumentException("args");
      }
   }

   public static void main(String[] var0) throws IOException {
      (new Digest(var0)).run();
   }

   private void println(String var1, byte[] var2) {
      this.println(var1, var2, (String)null);
   }

   private void println(String var1, byte[] var2, String var3) {
      PrintStream var4 = System.out;
      StringBuilder var5 = new StringBuilder();
      var5.append(var1);
      var5.append(Hex.encodeHexString(var2));
      if (var3 != null) {
         StringBuilder var6 = new StringBuilder();
         var6.append("  ");
         var6.append(var3);
         var1 = var6.toString();
      } else {
         var1 = "";
      }

      var5.append(var1);
      var4.println(var5.toString());
   }

   private void run() throws IOException {
      if (!this.algorithm.equalsIgnoreCase("ALL") && !this.algorithm.equals("*")) {
         MessageDigest var1 = DigestUtils.getDigest(this.algorithm, (MessageDigest)null);
         if (var1 != null) {
            this.run("", var1);
         } else {
            this.run("", DigestUtils.getDigest(this.algorithm.toUpperCase(Locale.ROOT)));
         }
      } else {
         this.run(MessageDigestAlgorithms.values());
      }
   }

   private void run(String var1, String var2) throws IOException {
      this.run(var1, DigestUtils.getDigest(var2));
   }

   private void run(String var1, MessageDigest var2) throws IOException {
      String[] var5 = this.inputs;
      if (var5 == null) {
         this.println(var1, DigestUtils.digest(var2, System.in));
      } else {
         int var4 = var5.length;

         for(int var3 = 0; var3 < var4; ++var3) {
            String var6 = var5[var3];
            File var7 = new File(var6);
            if (var7.isFile()) {
               this.println(var1, DigestUtils.digest(var2, var7), var6);
            } else if (var7.isDirectory()) {
               File[] var8 = var7.listFiles();
               if (var8 != null) {
                  this.run(var1, var2, var8);
               }
            } else {
               this.println(var1, DigestUtils.digest(var2, var6.getBytes(Charset.defaultCharset())));
            }
         }

      }
   }

   private void run(String var1, MessageDigest var2, File[] var3) throws IOException {
      int var5 = var3.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         File var6 = var3[var4];
         if (var6.isFile()) {
            this.println(var1, DigestUtils.digest(var2, var6), var6.getName());
         }
      }

   }

   private void run(String[] var1) throws IOException {
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         String var4 = var1[var2];
         if (DigestUtils.isAvailable(var4)) {
            StringBuilder var5 = new StringBuilder();
            var5.append(var4);
            var5.append(" ");
            this.run(var5.toString(), var4);
         }
      }

   }

   public String toString() {
      return String.format("%s %s", super.toString(), Arrays.toString(this.args));
   }
}
