package com.jcraft.jzlib;

public final class Adler32 implements Checksum {
   private static final int BASE = 65521;
   private static final int NMAX = 5552;
   // $FF: renamed from: s1 long
   private long field_137 = 1L;
   // $FF: renamed from: s2 long
   private long field_138 = 0L;

   static long combine(long var0, long var2, long var4) {
      long var6 = var4 % 65521L;
      long var8 = var0 & 65535L;
      var4 = var8 + ((var2 & 65535L) + 65521L - 1L);
      var6 = var6 * var8 % 65521L + ((var0 >> 16 & 65535L) + (65535L & var2 >> 16) + 65521L - var6);
      var0 = var4;
      if (var4 >= 65521L) {
         var0 = var4 - 65521L;
      }

      var2 = var0;
      if (var0 >= 65521L) {
         var2 = var0 - 65521L;
      }

      var0 = var6;
      if (var6 >= 65521L << 1) {
         var0 = var6 - (65521L << 1);
      }

      var4 = var0;
      if (var0 >= 65521L) {
         var4 = var0 - 65521L;
      }

      return var4 << 16 | var2;
   }

   public Adler32 copy() {
      Adler32 var1 = new Adler32();
      var1.field_137 = this.field_137;
      var1.field_138 = this.field_138;
      return var1;
   }

   public long getValue() {
      return this.field_138 << 16 | this.field_137;
   }

   public void reset() {
      this.field_137 = 1L;
      this.field_138 = 0L;
   }

   public void reset(long var1) {
      this.field_137 = var1 & 65535L;
      this.field_138 = 65535L & var1 >> 16;
   }

   public void update(byte[] var1, int var2, int var3) {
      long var7;
      if (var3 == 1) {
         var7 = this.field_137 + (long)(var1[var2] & 255);
         this.field_137 = var7;
         long var9 = this.field_138 + var7;
         this.field_138 = var9;
         this.field_137 = var7 % 65521L;
         this.field_138 = var9 % 65521L;
      } else {
         int var5 = var3 / 5552;
         int var4 = var3;

         while(true) {
            int var6 = var4;
            if (var5 <= 0) {
               var4 = var3 % 5552;
               var3 = var2;

               for(var2 = var4; var2 > 0; --var2) {
                  var7 = this.field_137 + (long)(var1[var3] & 255);
                  this.field_137 = var7;
                  this.field_138 += var7;
                  ++var3;
               }

               this.field_137 %= 65521L;
               this.field_138 %= 65521L;
               return;
            }

            var4 = 5552;

            for(var6 -= 5552; var4 > 0; --var4) {
               var7 = this.field_137 + (long)(var1[var2] & 255);
               this.field_137 = var7;
               this.field_138 += var7;
               ++var2;
            }

            this.field_137 %= 65521L;
            this.field_138 %= 65521L;
            --var5;
            var4 = var6;
         }
      }
   }
}
