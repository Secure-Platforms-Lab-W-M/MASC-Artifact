package okhttp3.internal.publicsuffix;

import java.net.IDN;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import okhttp3.internal.Util;

public final class PublicSuffixDatabase {
   private static final String[] EMPTY_RULE = new String[0];
   private static final byte EXCEPTION_MARKER = 33;
   private static final String[] PREVAILING_RULE = new String[]{"*"};
   public static final String PUBLIC_SUFFIX_RESOURCE = "publicsuffixes.gz";
   private static final byte[] WILDCARD_LABEL = new byte[]{42};
   private static final PublicSuffixDatabase instance = new PublicSuffixDatabase();
   private final AtomicBoolean listRead = new AtomicBoolean(false);
   private byte[] publicSuffixExceptionListBytes;
   private byte[] publicSuffixListBytes;
   private final CountDownLatch readCompleteLatch = new CountDownLatch(1);

   private static String binarySearchBytes(byte[] var0, byte[][] var1, int var2) {
      int var3 = 0;

      int var4;
      for(int var7 = var0.length; var3 < var7; var7 = var4) {
         for(var4 = (var3 + var7) / 2; var4 > -1 && var0[var4] != 10; --var4) {
         }

         int var11 = var4 + 1;

         int var8;
         for(var8 = 1; var0[var11 + var8] != 10; ++var8) {
         }

         int var12 = var11 + var8 - var11;
         int var9 = var2;
         int var5 = 0;
         var4 = 0;
         boolean var6 = false;

         int var10;
         while(true) {
            if (var6) {
               var10 = 46;
               var6 = false;
            } else {
               var10 = var1[var9][var5] & 255;
            }

            var10 -= var0[var11 + var4] & 255;
            if (var10 != 0) {
               break;
            }

            ++var4;
            ++var5;
            if (var4 == var12) {
               break;
            }

            if (var1[var9].length == var5) {
               if (var9 == var1.length - 1) {
                  break;
               }

               ++var9;
               var6 = true;
               var5 = -1;
            }
         }

         if (var10 < 0) {
            var4 = var11 - 1;
         } else if (var10 > 0) {
            var3 = var11 + var8 + 1;
            var4 = var7;
         } else {
            int var13 = var12 - var4;
            var5 = var1[var9].length - var5;

            for(var4 = var9 + 1; var4 < var1.length; ++var4) {
               var5 += var1[var4].length;
            }

            if (var5 < var13) {
               var4 = var11 - 1;
            } else {
               if (var5 <= var13) {
                  return new String(var0, var11, var12, Util.UTF_8);
               }

               var3 = var11 + var8 + 1;
               var4 = var7;
            }
         }
      }

      return null;
   }

   private String[] findMatchingRule(String[] var1) {
      if (!this.listRead.get() && this.listRead.compareAndSet(false, true)) {
         this.readTheList();
      } else {
         try {
            this.readCompleteLatch.await();
         } catch (InterruptedException var19) {
         }
      }

      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label490: {
         label480: {
            try {
               if (this.publicSuffixListBytes != null) {
                  break label480;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label490;
            }

            try {
               throw new IllegalStateException("Unable to load publicsuffixes.gz resource from the classpath.");
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label490;
            }
         }

         byte[][] var6 = new byte[var1.length][];

         int var2;
         for(var2 = 0; var2 < var1.length; ++var2) {
            var6[var2] = var1[var2].getBytes(Util.UTF_8);
         }

         String var3 = null;
         var2 = 0;

         String var24;
         while(true) {
            var24 = var3;
            if (var2 >= var6.length) {
               break;
            }

            var24 = binarySearchBytes(this.publicSuffixListBytes, var6, var2);
            if (var24 != null) {
               break;
            }

            ++var2;
         }

         String var4 = null;
         var3 = var4;
         if (var6.length > 1) {
            byte[][] var5 = (byte[][])var6.clone();
            var2 = 0;

            while(true) {
               var3 = var4;
               if (var2 >= var5.length - 1) {
                  break;
               }

               var5[var2] = WILDCARD_LABEL;
               var3 = binarySearchBytes(this.publicSuffixListBytes, var5, var2);
               if (var3 != null) {
                  break;
               }

               ++var2;
            }
         }

         Object var27 = null;
         var4 = (String)var27;
         if (var3 != null) {
            var2 = 0;

            while(true) {
               var4 = (String)var27;
               if (var2 >= var6.length - 1) {
                  break;
               }

               var4 = binarySearchBytes(this.publicSuffixExceptionListBytes, var6, var2);
               if (var4 != null) {
                  break;
               }

               ++var2;
            }
         }

         if (var4 != null) {
            StringBuilder var25 = new StringBuilder();
            var25.append("!");
            var25.append(var4);
            return var25.toString().split("\\.");
         }

         if (var24 == null && var3 == null) {
            return PREVAILING_RULE;
         }

         if (var24 != null) {
            var1 = var24.split("\\.");
         } else {
            var1 = EMPTY_RULE;
         }

         String[] var26;
         if (var3 != null) {
            var26 = var3.split("\\.");
         } else {
            var26 = EMPTY_RULE;
         }

         if (var1.length > var26.length) {
            return var1;
         }

         return var26;
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   public static PublicSuffixDatabase get() {
      return instance;
   }

   private void readTheList() {
      // $FF: Couldn't be decompiled
   }

   public String getEffectiveTldPlusOne(String var1) {
      if (var1 == null) {
         throw new NullPointerException("domain == null");
      } else {
         String[] var3 = IDN.toUnicode(var1).split("\\.");
         String[] var4 = this.findMatchingRule(var3);
         if (var3.length == var4.length && var4[0].charAt(0) != '!') {
            return null;
         } else {
            int var2;
            if (var4[0].charAt(0) == '!') {
               var2 = var3.length - var4.length;
            } else {
               var2 = var3.length - (var4.length + 1);
            }

            StringBuilder var6 = new StringBuilder();

            for(String[] var5 = var1.split("\\."); var2 < var5.length; ++var2) {
               var6.append(var5[var2]);
               var6.append('.');
            }

            var6.deleteCharAt(var6.length() - 1);
            return var6.toString();
         }
      }
   }

   void setListBytes(byte[] var1, byte[] var2) {
      this.publicSuffixListBytes = var1;
      this.publicSuffixExceptionListBytes = var2;
      this.listRead.set(true);
      this.readCompleteLatch.countDown();
   }
}
