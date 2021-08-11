package gnu.java.bigintcrypto;

import gnu.java.math.MPN;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Random;
import org.bouncycastle.crypto.prng.RandomGenerator;

public class BigIntegerCrypto extends Number implements Comparable {
   private static final int CEILING = 2;
   private static final int FLOOR = 1;
   public static final BigIntegerCrypto ONE;
   private static final int ROUND = 4;
   public static final BigIntegerCrypto TEN;
   private static final int TRUNCATE = 3;
   public static final BigIntegerCrypto ZERO;
   private static final byte[] bit4_count;
   // $FF: renamed from: k int[]
   private static final int[] field_77;
   private static final int maxFixNum = 1024;
   private static final int minFixNum = -100;
   private static final int numFixNum = 1125;
   private static final int[] primes;
   private static final long serialVersionUID = -3877199389772696545L;
   private static final BigIntegerCrypto[] smallFixNums = new BigIntegerCrypto[1125];
   // $FF: renamed from: t int[]
   private static final int[] field_78;
   private transient int ival;
   private byte[] magnitude;
   private int signum;
   private transient int[] words;

   static {
      int var0 = 1125;

      while(true) {
         --var0;
         if (var0 < 0) {
            BigIntegerCrypto[] var1 = smallFixNums;
            ZERO = var1[100];
            ONE = var1[101];
            TEN = var1[110];
            primes = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251};
            field_77 = new int[]{100, 150, 200, 250, 300, 350, 400, 500, 600, 800, 1250, Integer.MAX_VALUE};
            field_78 = new int[]{27, 18, 15, 12, 9, 8, 7, 6, 5, 4, 3, 2};
            bit4_count = new byte[]{0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4};
            return;
         }

         smallFixNums[var0] = new BigIntegerCrypto(var0 - 100);
      }
   }

   private BigIntegerCrypto() {
   }

   private BigIntegerCrypto(int var1) {
      this.ival = var1;
   }

   public BigIntegerCrypto(int var1, int var2, Random var3) {
      this();
      BigIntegerCrypto var4 = new BigIntegerCrypto();

      do {
         var4.init(var1, var3);
         var4 = var4.setBit(var1 - 1);
      } while(!var4.isProbablePrime(var2));

      this.ival = var4.ival;
      this.words = var4.words;
   }

   public BigIntegerCrypto(int var1, int var2, RandomGenerator var3) {
      this();
      BigIntegerCrypto var4 = new BigIntegerCrypto();

      do {
         var4.init(var1, var3);
         var4 = var4.setBit(var1 - 1);
      } while(!var4.isProbablePrime(var2));

      this.ival = var4.ival;
      this.words = var4.words;
   }

   public BigIntegerCrypto(int var1, Random var2) {
      this();
      if (var1 >= 0) {
         this.init(var1, var2);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public BigIntegerCrypto(int var1, RandomGenerator var2) {
      this();
      if (var1 >= 0) {
         this.init(var1, var2);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public BigIntegerCrypto(int var1, byte[] var2) {
      this();
      if (var2 != null && var1 <= 1 && var1 >= -1) {
         if (var1 != 0) {
            int[] var3 = byteArrayToIntArray(var2, 0);
            this.words = var3;
            BigIntegerCrypto var4 = make(var3, var3.length);
            this.ival = var4.ival;
            this.words = var4.words;
            if (var1 < 0) {
               this.setNegative();
            }

         } else {
            for(var1 = var2.length - 1; var1 >= 0 && var2[var1] == 0; --var1) {
            }

            if (var1 >= 0) {
               throw new NumberFormatException();
            }
         }
      } else {
         throw new NumberFormatException();
      }
   }

   public BigIntegerCrypto(String var1) {
      this(var1, 10);
   }

   public BigIntegerCrypto(String var1, int var2) {
      this();
      int var5 = var1.length();
      int var3;
      boolean var7;
      byte[] var8;
      if (var1.charAt(0) == '-') {
         var7 = true;
         var3 = 1;
         var8 = new byte[var5 - 1];
      } else {
         var7 = false;
         var3 = 0;
         var8 = new byte[var5];
      }

      int var4;
      for(var4 = 0; var3 < var5; ++var4) {
         int var6 = Character.digit(var1.charAt(var3), var2);
         if (var6 < 0) {
            StringBuilder var9 = new StringBuilder();
            var9.append("Invalid character at position #");
            var9.append(var3);
            throw new NumberFormatException(var9.toString());
         }

         var8[var4] = (byte)var6;
         ++var3;
      }

      BigIntegerCrypto var10;
      if (var5 <= 15 && var2 <= 16) {
         var10 = valueOf(Long.parseLong(var1, var2));
      } else {
         var10 = valueOf(var8, var4, var7, var2);
      }

      Arrays.fill(var8, (byte)0);
      this.ival = var10.ival;
      this.words = var10.words;
   }

   public BigIntegerCrypto(byte[] var1) {
      this();
      if (var1 != null && var1.length >= 1) {
         byte var2 = 0;
         if (var1[0] < 0) {
            var2 = -1;
         }

         int[] var3 = byteArrayToIntArray(var1, var2);
         this.words = var3;
         BigIntegerCrypto var4 = make(var3, var3.length);
         this.ival = var4.ival;
         this.words = var4.words;
      } else {
         throw new NumberFormatException();
      }
   }

   private static BigIntegerCrypto abs(BigIntegerCrypto var0) {
      return var0.isNegative() ? neg(var0) : var0;
   }

   private static BigIntegerCrypto add(int var0, int var1) {
      return valueOf((long)var0 + (long)var1);
   }

   private static BigIntegerCrypto add(BigIntegerCrypto var0, int var1) {
      if (var0.words == null) {
         return add(var0.ival, var1);
      } else {
         BigIntegerCrypto var2 = new BigIntegerCrypto(0);
         var2.setAdd(var0, var1);
         return var2.canonicalize();
      }
   }

   private static BigIntegerCrypto add(BigIntegerCrypto var0, BigIntegerCrypto var1, int var2) {
      if (var0.words == null && var1.words == null) {
         return valueOf((long)var2 * (long)var1.ival + (long)var0.ival);
      } else {
         BigIntegerCrypto var9 = var1;
         if (var2 != 1) {
            if (var2 == -1) {
               var9 = neg(var1);
            } else {
               var9 = times(var1, valueOf((long)var2));
            }
         }

         if (var0.words == null) {
            return add(var9, var0.ival);
         } else if (var9.words == null) {
            return add(var0, var9.ival);
         } else {
            BigIntegerCrypto var10 = var0;
            var1 = var9;
            if (var9.ival > var0.ival) {
               var1 = var0;
               var10 = var9;
            }

            var0 = alloc(var10.ival + 1);
            var2 = var1.ival;
            long var5 = (long)MPN.add_n(var0.words, var10.words, var1.words, var2);
            long var3;
            if (var1.words[var2 - 1] < 0) {
               var3 = 4294967295L;
            } else {
               var3 = 0L;
            }

            while(var2 < var10.ival) {
               var5 += ((long)var10.words[var2] & 4294967295L) + var3;
               var0.words[var2] = (int)var5;
               var5 >>>= 32;
               ++var2;
            }

            long var7 = var3;
            if (var10.words[var2 - 1] < 0) {
               var7 = var3 - 1L;
            }

            var0.words[var2] = (int)(var5 + var7);
            var0.ival = var2 + 1;
            return var0.canonicalize();
         }
      }
   }

   private static BigIntegerCrypto alloc(int var0) {
      BigIntegerCrypto var1 = new BigIntegerCrypto();
      if (var0 > 1) {
         var1.words = new int[var0];
      }

      return var1;
   }

   private static BigIntegerCrypto and(BigIntegerCrypto var0, int var1) {
      int[] var3 = var0.words;
      if (var3 == null) {
         return valueOf((long)(var0.ival & var1));
      } else if (var1 >= 0) {
         return valueOf((long)(var3[0] & var1));
      } else {
         int var2 = var0.ival;
         int[] var4 = new int[var2];
         var4[0] = var3[0] & var1;
         var1 = var2;

         while(true) {
            --var1;
            if (var1 <= 0) {
               return make(var4, var0.ival);
            }

            var4[var1] = var0.words[var1];
         }
      }
   }

   private static int bitCount(int var0) {
      int var1;
      for(var1 = 0; var0 != 0; var0 >>>= 4) {
         var1 += bit4_count[var0 & 15];
      }

      return var1;
   }

   private static int bitCount(int[] var0, int var1) {
      byte var3 = 0;
      int var2 = var1;
      var1 = var3;

      while(true) {
         --var2;
         if (var2 < 0) {
            return var1;
         }

         var1 += bitCount(var0[var2]);
      }
   }

   private static BigIntegerCrypto bitOp(int var0, BigIntegerCrypto var1, BigIntegerCrypto var2) {
      if (var0 != 0) {
         if (var0 != 1) {
            if (var0 != 3) {
               if (var0 != 5) {
                  if (var0 != 15) {
                     BigIntegerCrypto var3 = new BigIntegerCrypto();
                     setBitOp(var3, var0, var1, var2);
                     return var3.canonicalize();
                  } else {
                     return valueOf(-1L);
                  }
               } else {
                  return var2;
               }
            } else {
               return var1;
            }
         } else {
            return var1.and(var2);
         }
      } else {
         return ZERO;
      }
   }

   private static int[] byteArrayToIntArray(byte[] var0, int var1) {
      int[] var6 = new int[var0.length / 4 + 1];
      int var5 = var6.length;
      byte var4 = 0;
      int var3 = var0.length % 4;
      int var2 = var1;

      for(var1 = var4; var3 > 0; ++var1) {
         var2 = var2 << 8 | var0[var1] & 255;
         --var3;
      }

      var3 = var5 - 1;
      var6[var3] = var2;

      for(var2 = var3; var2 > 0; var1 = var5 + 1) {
         --var2;
         int var8 = var1 + 1;
         byte var7 = var0[var1];
         var3 = var8 + 1;
         byte var9 = var0[var8];
         var5 = var3 + 1;
         var6[var2] = var7 << 24 | (var9 & 255) << 16 | (var0[var3] & 255) << 8 | var0[var5] & 255;
      }

      return var6;
   }

   private BigIntegerCrypto canonicalize() {
      int[] var2 = this.words;
      int var1;
      if (var2 != null) {
         var1 = wordsNeeded(var2, this.ival);
         this.ival = var1;
         if (var1 <= 1) {
            if (var1 == 1) {
               this.ival = this.words[0];
            }

            Arrays.fill(this.words, 0);
            this.words = null;
         }
      }

      if (this.words == null) {
         var1 = this.ival;
         if (var1 >= -100 && var1 <= 1024) {
            return smallFixNums[var1 + 100];
         }
      }

      return this;
   }

   private boolean checkBits(int var1) {
      boolean var4 = false;
      boolean var3 = false;
      if (var1 <= 0) {
         return false;
      } else if (this.words != null) {
         int var2;
         for(var2 = 0; var2 < var1 >> 5; ++var2) {
            if (this.words[var2] != 0) {
               return true;
            }
         }

         var3 = var4;
         if ((var1 & 31) != 0) {
            var3 = var4;
            if ((this.words[var2] & (1 << (var1 & 31)) - 1) != 0) {
               var3 = true;
            }
         }

         return var3;
      } else {
         if (var1 > 31 || (this.ival & (1 << var1) - 1) != 0) {
            var3 = true;
         }

         return var3;
      }
   }

   private static int compareTo(BigIntegerCrypto var0, BigIntegerCrypto var1) {
      int[] var7 = var0.words;
      boolean var5 = false;
      byte var2 = 0;
      byte var4 = -1;
      int var3;
      if (var7 == null && var1.words == null) {
         var3 = var0.ival;
         int var10 = var1.ival;
         if (var3 < var10) {
            return -1;
         } else {
            if (var3 > var10) {
               var2 = 1;
            }

            return var2;
         }
      } else {
         boolean var6 = var0.isNegative();
         if (var6 != var1.isNegative()) {
            return var6 ? -1 : 1;
         } else {
            int var8;
            if (var0.words == null) {
               var8 = 1;
            } else {
               var8 = var0.ival;
            }

            if (var1.words == null) {
               var3 = 1;
            } else {
               var3 = var1.ival;
            }

            if (var8 != var3) {
               if (var8 > var3) {
                  var5 = true;
               }

               byte var9 = var4;
               if (var5 != var6) {
                  var9 = 1;
               }

               return var9;
            } else {
               return MPN.cmp(var0.words, var1.words, var8);
            }
         }
      }
   }

   private static void divide(long var0, long var2, BigIntegerCrypto var4, BigIntegerCrypto var5, int var6) {
      boolean var7;
      if (var0 < 0L) {
         var7 = true;
         if (var0 == Long.MIN_VALUE) {
            divide(valueOf(var0), valueOf(var2), var4, var5, var6);
            return;
         }

         var0 = -var0;
      } else {
         var7 = false;
         var0 = var0;
      }

      boolean var8;
      if (var2 < 0L) {
         var8 = true;
         if (var2 == Long.MIN_VALUE) {
            if (var6 == 3) {
               if (var4 != null) {
                  var4.set(0L);
               }

               if (var5 != null) {
                  var5.set(var0);
                  return;
               }
            } else {
               divide(valueOf(var0), valueOf(var2), var4, var5, var6);
            }

            return;
         }

         var2 = -var2;
      } else {
         var8 = false;
         var2 = var2;
      }

      long var11 = var0 / var2;
      long var13 = var0 % var2;
      boolean var10 = var7 ^ var8;
      boolean var9 = false;
      var8 = var9;
      boolean var15;
      if (var13 != 0L) {
         if (var6 != 1 && var6 != 2) {
            if (var6 != 4) {
               var8 = var9;
            } else {
               if (var13 > var2 - (var11 & 1L) >> 1) {
                  var15 = true;
               } else {
                  var15 = false;
               }

               var8 = var15;
            }
         } else {
            if (var6 == 1) {
               var15 = true;
            } else {
               var15 = false;
            }

            var8 = var9;
            if (var10 == var15) {
               var8 = true;
            }
         }
      }

      if (var4 != null) {
         var0 = var11;
         if (var8) {
            var0 = var11 + 1L;
         }

         var11 = var0;
         if (var10) {
            var11 = -var0;
         }

         var4.set(var11);
      }

      if (var5 != null) {
         var15 = var7;
         var0 = var13;
         if (var8) {
            var0 = var2 - var13;
            var15 = var7 ^ true;
         }

         var2 = var0;
         if (var15) {
            var2 = -var0;
         }

         var5.set(var2);
      }

   }

   private static void divide(BigIntegerCrypto var0, BigIntegerCrypto var1, BigIntegerCrypto var2, BigIntegerCrypto var3, int var4) {
      if ((var0.words == null || var0.ival <= 2) && (var1.words == null || var1.ival <= 2)) {
         long var11 = var0.longValue();
         long var13 = var1.longValue();
         if (var11 != Long.MIN_VALUE && var13 != Long.MIN_VALUE) {
            divide(var11, var13, var2, var3, var4);
            return;
         }
      }

      boolean var15 = var0.isNegative();
      boolean var16 = var1.isNegative();
      boolean var9 = var15 ^ var16;
      int var6;
      if (var1.words == null) {
         var6 = 1;
      } else {
         var6 = var1.ival;
      }

      int[] var18 = new int[var6];
      var1.getAbsolute(var18);

      while(var6 > 1 && var18[var6 - 1] == 0) {
         --var6;
      }

      int var5;
      if (var0.words == null) {
         var5 = 1;
      } else {
         var5 = var0.ival;
      }

      int[] var17 = new int[var5 + 2];
      var0.getAbsolute(var17);

      while(var5 > 1 && var17[var5 - 1] == 0) {
         --var5;
      }

      int var7 = MPN.cmp(var17, var5, var18, var6);
      byte var8;
      int[] var19;
      if (var7 < 0) {
         var19 = var17;
         var18[0] = 0;
         var6 = var5;
         var8 = 1;
         var17 = var18;
         var5 = var8;
      } else if (var7 == 0) {
         var17[0] = 1;
         var18[0] = 0;
         var6 = 1;
         var8 = 1;
         var19 = var18;
         var5 = var8;
      } else {
         int var22;
         if (var6 == 1) {
            if (var18[0] == 1 && var17[var5 - 1] < 0) {
               var6 = var5 + 1;
            } else {
               var6 = var5;
            }

            byte var21 = 1;
            var18[0] = MPN.divmod_1(var17, var17, var5, var18[0]);
            var22 = var6;
            var6 = var21;
            var19 = var18;
            var5 = var22;
         } else {
            int var10 = MPN.count_leading_zeros(var18[var6 - 1]);
            var22 = var5;
            if (var10 != 0) {
               MPN.lshift(var18, 0, var18, var6, var10);
               var17[var5] = MPN.lshift(var17, 0, var17, var5, var10);
               var22 = var5 + 1;
            }

            var7 = var22;
            if (var22 == var6) {
               var17[var22] = 0;
               var7 = var22 + 1;
            }

            MPN.divide(var17, var7, var18, var6);
            MPN.rshift0(var18, var17, 0, var6, var10);
            var22 = var7 + 1 - var6;
            if (var2 != null) {
               for(var5 = 0; var5 < var22; ++var5) {
                  var17[var5] = var17[var5 + var6];
               }
            }

            var5 = var22;
            var19 = var18;
         }
      }

      var7 = var6;
      if (var19[var6 - 1] < 0) {
         var19[var6] = 0;
         var7 = var6 + 1;
      }

      boolean var20;
      label220: {
         if (var7 > 1 || var19[0] != 0) {
            if (var4 != 1 && var4 != 2) {
               if (var4 == 4) {
                  BigIntegerCrypto var24;
                  if (var3 == null) {
                     var24 = new BigIntegerCrypto();
                  } else {
                     var24 = var3;
                  }

                  var24.set(var19, var7);
                  var24 = shift(var24, 1);
                  if (var16) {
                     var24.setNegative();
                  }

                  var6 = compareTo(var24, var1);
                  var4 = var6;
                  if (var16) {
                     var4 = -var6;
                  }

                  if (var4 == 1 || var4 == 0 && (var17[0] & 1) != 0) {
                     var20 = true;
                  } else {
                     var20 = false;
                  }
                  break label220;
               }
            } else {
               if (var4 == 1) {
                  var20 = true;
               } else {
                  var20 = false;
               }

               if (var9 == var20) {
                  var20 = true;
                  break label220;
               }
            }
         }

         var20 = false;
      }

      if (var2 != null) {
         var2.set(var17, var5);
         if (var9) {
            if (var20) {
               var2.setInvert();
            } else {
               var2.setNegative();
            }
         } else if (var20) {
            var2.setAdd(1);
         }
      }

      if (var3 != null) {
         var3.set(var19, var7);
         if (var20) {
            if (var1.words == null) {
               var2 = var3;
               if (var16) {
                  var4 = var19[0] + var1.ival;
               } else {
                  var4 = var19[0] - var1.ival;
               }

               var3.set((long)var4);
            } else {
               byte var23;
               if (var16) {
                  var23 = 1;
               } else {
                  var23 = -1;
               }

               var2 = add(var3, var1, var23);
            }

            if (var15) {
               var3.setNegative(var2);
            } else {
               var3.set(var2);
            }

            if (var2 != var3) {
               var2.zeroize();
            }

            return;
         }

         if (var15) {
            var3.setNegative();
            return;
         }
      }

   }

   private static boolean equals(BigIntegerCrypto var0, BigIntegerCrypto var1) {
      if (var0.words == null && var1.words == null) {
         return var0.ival == var1.ival;
      } else if (var0.words != null && var1.words != null) {
         if (var0.ival != var1.ival) {
            return false;
         } else {
            int var2 = var0.ival;

            int var3;
            do {
               var3 = var2 - 1;
               if (var3 < 0) {
                  return true;
               }

               var2 = var3;
            } while(var0.words[var3] == var1.words[var3]);

            return false;
         }
      } else {
         return false;
      }
   }

   private static void euclidInv(BigIntegerCrypto var0, BigIntegerCrypto var1, BigIntegerCrypto var2, BigIntegerCrypto[] var3) {
      if (!var1.isZero()) {
         if (var1.isOne()) {
            var3[0] = neg(var2);
            var3[1] = ONE;
         } else {
            if (var0.words == null) {
               int var4 = var1.ival;
               int var5 = var0.ival;
               int[] var8 = euclidInv(var4, var5 % var4, var5 / var4);
               var3[0] = new BigIntegerCrypto(var8[0]);
               var3[1] = new BigIntegerCrypto(var8[1]);
            } else {
               BigIntegerCrypto var6 = new BigIntegerCrypto();
               BigIntegerCrypto var7 = new BigIntegerCrypto();
               divide(var0, var1, var7, var6, 1);
               var6.canonicalize();
               var7.canonicalize();
               euclidInv(var1, var6, var7, var3);
            }

            var0 = var3[0];
            var3[0] = add(var3[1], times(var0, var2), -1);
            var3[1] = var0;
         }
      } else {
         throw new ArithmeticException("not invertible");
      }
   }

   private static int[] euclidInv(int var0, int var1, int var2) {
      if (var1 != 0) {
         if (var1 == 1) {
            return new int[]{-var2, 1};
         } else {
            int[] var3 = euclidInv(var1, var0 % var1, var0 / var1);
            var0 = var3[0];
            var3[0] = -var2 * var0 + var3[1];
            var3[1] = var0;
            return var3;
         }
      } else {
         throw new ArithmeticException("not invertible");
      }
   }

   private void format(int var1, StringBuffer var2) {
      if (this.words == null) {
         var2.append(Integer.toString(this.ival, var1));
      } else if (this.ival <= 2) {
         var2.append(Long.toString(this.longValue(), var1));
      } else {
         boolean var8 = this.isNegative();
         int[] var9;
         if (!var8 && var1 == 16) {
            var9 = this.words;
         } else {
            var9 = new int[this.ival];
            this.getAbsolute(var9);
         }

         int var4 = this.ival;
         int var5;
         if (var1 == 16) {
            if (var8) {
               var2.append('-');
            }

            var5 = var2.length();

            label56:
            while(true) {
               --var4;
               if (var4 < 0) {
                  return;
               }

               int var6 = var9[var4];
               var1 = 8;

               while(true) {
                  int var7;
                  do {
                     --var1;
                     if (var1 < 0) {
                        continue label56;
                     }

                     var7 = var6 >> var1 * 4 & 15;
                  } while(var7 <= 0 && var2.length() <= var5);

                  var2.append(Character.forDigit(var7, 16));
               }
            }
         } else {
            var5 = var2.length();

            do {
               var2.append(Character.forDigit(MPN.divmod_1(var9, var9, var4, var1), var1));

               while(var4 > 0 && var9[var4 - 1] == 0) {
                  --var4;
               }
            } while(var4 != 0);

            if (var8) {
               var2.append('-');
            }

            var1 = var2.length() - 1;

            for(var4 = var5; var4 < var1; --var1) {
               char var3 = var2.charAt(var4);
               var2.setCharAt(var4, var2.charAt(var1));
               var2.setCharAt(var1, var3);
               ++var4;
            }

         }
      }
   }

   private static int gcd(int var0, int var1) {
      int var3 = var0;
      int var2 = var1;
      if (var1 > var0) {
         var2 = var0;
         var3 = var1;
      }

      while(var2 != 0) {
         if (var2 == 1) {
            return var2;
         }

         var0 = var3 % var2;
         var3 = var2;
         var2 = var0;
      }

      return var3;
   }

   private void getAbsolute(int[] var1) {
      int var2;
      int var3;
      if (this.words == null) {
         var2 = 1;
         var1[0] = this.ival;
      } else {
         var3 = this.ival;
         var2 = var3;

         while(true) {
            int var4 = var2 - 1;
            var2 = var3;
            if (var4 < 0) {
               break;
            }

            var1[var4] = this.words[var4];
            var2 = var4;
         }
      }

      if (var1[var2 - 1] < 0) {
         negate(var1, var1, var2);
      }

      var3 = var1.length;

      while(true) {
         --var3;
         if (var3 <= var2) {
            return;
         }

         var1[var3] = 0;
      }
   }

   private void init(int var1, Random var2) {
      int var5 = var1 & 31;
      int var6 = (var5 + 7) / 8;
      int var3 = var5 % 8;
      int var4 = var3;
      if (var3 != 0) {
         var4 = 8 - var3;
      }

      byte[] var7 = new byte[var6];
      var3 = var5;
      if (var5 > 0) {
         var2.nextBytes(var7);
         var4 = (var7[var6 - 1] & 255) >>> var4;
         var5 = var6 - 2;

         while(true) {
            var3 = var4;
            if (var5 < 0) {
               break;
            }

            var4 = var4 << 8 | var7[var5] & 255;
            --var5;
         }
      }

      for(var1 /= 32; var3 == 0 && var1 > 0; --var1) {
         var3 = var2.nextInt();
      }

      if (var1 == 0 && var3 >= 0) {
         this.ival = var3;
      } else {
         if (var3 < 0) {
            var4 = var1 + 2;
         } else {
            var4 = var1 + 1;
         }

         this.ival = var4;
         int[] var8 = new int[var4];
         this.words = var8;
         var8[var1] = var3;

         while(true) {
            --var1;
            if (var1 < 0) {
               return;
            }

            this.words[var1] = var2.nextInt();
         }
      }
   }

   private void init(int var1, RandomGenerator var2) {
      int var5 = var1 & 31;
      int var6 = (var5 + 7) / 8;
      int var3 = var5 % 8;
      int var4 = var3;
      if (var3 != 0) {
         var4 = 8 - var3;
      }

      byte[] var7 = new byte[var6];
      var3 = var5;
      if (var5 > 0) {
         var2.nextBytes(var7);
         var4 = (var7[var6 - 1] & 255) >>> var4;
         var5 = var6 - 2;

         while(true) {
            var3 = var4;
            if (var5 < 0) {
               break;
            }

            var4 = var4 << 8 | var7[var5] & 255;
            --var5;
         }
      }

      var1 /= 32;

      for(var7 = new byte[4]; var3 == 0 && var1 > 0; --var1) {
         var2.nextBytes(var7);
         var3 = var7[0] | var7[1] << 8 | var7[2] << 16 | var7[3] << 24;
      }

      if (var1 == 0 && var3 >= 0) {
         this.ival = var3;
      } else {
         if (var3 < 0) {
            var4 = var1 + 2;
         } else {
            var4 = var1 + 1;
         }

         this.ival = var4;
         int[] var8 = new int[var4];
         this.words = var8;
         var8[var1] = var3;

         while(true) {
            --var1;
            if (var1 < 0) {
               return;
            }

            var2.nextBytes(var7);
            var8 = this.words;
            var8[var1] = var7[0];
            var8[var1] |= var7[1] << 8;
            var8[var1] |= var7[2] << 16;
            var8[var1] |= var7[3] << 24;
         }
      }
   }

   private boolean isNegative() {
      int[] var2 = this.words;
      int var1;
      if (var2 == null) {
         var1 = this.ival;
      } else {
         var1 = var2[this.ival - 1];
      }

      return var1 < 0;
   }

   private boolean isOne() {
      return this.words == null && this.ival == 1;
   }

   private boolean isZero() {
      return this.words == null && this.ival == 0;
   }

   private static BigIntegerCrypto make(int[] var0, int var1) {
      if (var0 == null) {
         return valueOf((long)var1);
      } else {
         var1 = wordsNeeded(var0, var1);
         if (var1 <= 1) {
            return var1 == 0 ? ZERO : valueOf((long)var0[0]);
         } else {
            BigIntegerCrypto var2 = new BigIntegerCrypto();
            var2.words = var0;
            var2.ival = var1;
            return var2;
         }
      }
   }

   private static BigIntegerCrypto neg(BigIntegerCrypto var0) {
      if (var0.words == null) {
         int var1 = var0.ival;
         if (var1 != Integer.MIN_VALUE) {
            return valueOf((long)(-var1));
         }
      }

      BigIntegerCrypto var2 = new BigIntegerCrypto(0);
      var2.setNegative(var0);
      return var2.canonicalize();
   }

   private static boolean negate(int[] var0, int[] var1, int var2) {
      long var5 = 1L;
      int var3 = var1[var2 - 1];
      boolean var8 = false;
      boolean var9;
      if (var3 < 0) {
         var9 = true;
      } else {
         var9 = false;
      }

      for(int var4 = 0; var4 < var2; ++var4) {
         var5 += (long)var1[var4] & 4294967295L;
         var0[var4] = (int)var5;
         var5 >>= 32;
      }

      boolean var7 = var8;
      if (var9) {
         var7 = var8;
         if (var0[var2 - 1] < 0) {
            var7 = true;
         }
      }

      return var7;
   }

   public static BigIntegerCrypto probablePrime(int var0, Random var1) {
      if (var0 >= 2) {
         return new BigIntegerCrypto(var0, 100, var1);
      } else {
         throw new ArithmeticException();
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      byte[] var3 = this.magnitude;
      if (var3.length != 0) {
         int var2 = this.signum;
         if (var2 != 0) {
            byte var6;
            if (var2 < 0) {
               var6 = -1;
            } else {
               var6 = 0;
            }

            this.words = byteArrayToIntArray(var3, var6);
            Arrays.fill(this.magnitude, (byte)0);
            int[] var4 = this.words;
            BigIntegerCrypto var5 = make(var4, var4.length);
            this.ival = var5.ival;
            this.words = var5.words;
            return;
         }
      }

      this.ival = 0;
      this.words = null;
   }

   private void realloc(int var1) {
      int[] var2;
      if (var1 == 0) {
         var2 = this.words;
         if (var2 != null) {
            if (this.ival > 0) {
               this.ival = var2[0];
               var2[0] = 0;
            }

            this.words = null;
            return;
         }
      } else {
         var2 = this.words;
         if (var2 == null || var2.length < var1 || var2.length > var1 + 2) {
            var2 = new int[var1];
            if (this.words == null) {
               var2[0] = this.ival;
               this.ival = 1;
            } else {
               if (var1 < this.ival) {
                  this.ival = var1;
               }

               System.arraycopy(this.words, 0, var2, 0, this.ival);
               Arrays.fill(this.words, 0);
            }

            this.words = var2;
         }
      }

   }

   private double roundToDouble(int var1, boolean var2, boolean var3) {
      int var6 = this.bitLength();
      int var5 = var1 + (var6 - 1);
      if (var5 < -1075) {
         return var2 ? 0.0D : 0.0D;
      } else if (var5 > 1023) {
         return var2 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
      } else {
         int var4;
         if (var5 >= -1022) {
            var4 = 53;
         } else {
            var4 = var5 + 53 + 1022;
         }

         int var7 = var6 - (var4 + 1);
         long var10;
         if (var7 > 0) {
            int[] var14 = this.words;
            if (var14 == null) {
               var10 = (long)(this.ival >> var7);
            } else {
               var10 = MPN.rshift_long(var14, this.ival, var7);
            }
         } else {
            var10 = this.longValue() << -var7;
         }

         if (var5 == 1023 && var10 >> 1 == 9007199254740991L) {
            if (!var3 && !this.checkBits(var6 - var4)) {
               return var2 ? -1.7976931348623157E308D : Double.MAX_VALUE;
            } else {
               return var2 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            }
         } else {
            long var12 = 0L;
            var1 = var5;
            long var8 = var10;
            if ((var10 & 1L) == 1L) {
               label78: {
                  if ((var10 & 2L) != 2L && !var3) {
                     var1 = var5;
                     var8 = var10;
                     if (!this.checkBits(var7)) {
                        break label78;
                     }
                  }

                  var10 += 2L;
                  if ((18014398509481984L & var10) != 0L) {
                     var1 = var5 + 1;
                     var8 = var10 >> 1;
                  } else {
                     var1 = var5;
                     var8 = var10;
                     if (var4 == 52) {
                        var1 = var5;
                        var8 = var10;
                        if ((9007199254740992L & var10) != 0L) {
                           var1 = var5 + 1;
                           var8 = var10;
                        }
                     }
                  }
               }
            }

            if (var2) {
               var10 = Long.MIN_VALUE;
            } else {
               var10 = 0L;
            }

            var1 += 1023;
            if (var1 > 0) {
               var12 = (long)var1 << 52;
            }

            return Double.longBitsToDouble(var10 | var12 | -4503599627370497L & var8 >> 1);
         }
      }
   }

   private void set(long var1) {
      int var3 = (int)var1;
      int[] var4;
      if ((long)var3 == var1) {
         this.ival = var3;
         var4 = this.words;
         if (var4 != null) {
            Arrays.fill(var4, 0);
         }

         this.words = null;
      } else {
         this.realloc(2);
         var4 = this.words;
         var4[0] = var3;
         var4[1] = (int)(var1 >> 32);
         this.ival = 2;
      }
   }

   private void set(BigIntegerCrypto var1) {
      if (var1.words == null) {
         this.set((long)var1.ival);
      } else {
         if (this != var1) {
            this.realloc(var1.ival);
            System.arraycopy(var1.words, 0, this.words, 0, var1.ival);
            this.ival = var1.ival;
         }

      }
   }

   private void set(int[] var1, int var2) {
      this.ival = var2;
      int[] var3 = this.words;
      if (var3 != null) {
         Arrays.fill(var3, 0);
      }

      this.words = var1;
   }

   private void setAdd(int var1) {
      this.setAdd(this, var1);
   }

   private void setAdd(BigIntegerCrypto var1, int var2) {
      if (var1.words == null) {
         this.set((long)var1.ival + (long)var2);
      } else {
         int var3 = var1.ival;
         this.realloc(var3 + 1);
         long var4 = (long)var2;

         for(var2 = 0; var2 < var3; ++var2) {
            var4 += (long)var1.words[var2] & 4294967295L;
            this.words[var2] = (int)var4;
            var4 >>= 32;
         }

         long var6 = var4;
         if (var1.words[var3 - 1] < 0) {
            var6 = var4 - 1L;
         }

         int[] var8 = this.words;
         var8[var3] = (int)var6;
         this.ival = wordsNeeded(var8, var3 + 1);
      }
   }

   private static void setBitOp(BigIntegerCrypto var0, int var1, BigIntegerCrypto var2, BigIntegerCrypto var3) {
      int var20 = var1;
      BigIntegerCrypto var44 = var2;
      BigIntegerCrypto var43 = var3;
      if (var3.words != null) {
         label194: {
            if (var2.words != null) {
               var20 = var1;
               var44 = var2;
               var43 = var3;
               if (var2.ival >= var3.ival) {
                  break label194;
               }
            }

            var20 = swappedOp(var1);
            var43 = var2;
            var44 = var3;
         }
      }

      int[] var45 = var43.words;
      int var19;
      if (var45 == null) {
         var1 = var43.ival;
         var19 = 1;
      } else {
         var1 = var45[0];
         var19 = var43.ival;
      }

      var45 = var44.words;
      int var4;
      int var18;
      if (var45 == null) {
         var4 = var44.ival;
         var18 = 1;
      } else {
         var4 = var45[0];
         var18 = var44.ival;
      }

      if (var18 > 1) {
         var0.realloc(var18);
      }

      byte var46;
      var45 = var0.words;
      int var7 = 0;
      int var8 = 0;
      int var11 = 0;
      int var5 = 0;
      int var9 = 0;
      int var10 = 0;
      int var12 = 0;
      int var16 = 0;
      int var13 = 0;
      int var14 = 0;
      int var17 = 0;
      byte var42 = 0;
      int var6 = 0;
      byte var41 = 0;
      int var21 = var1;
      int var24 = var4;
      int var22 = var1;
      int var26 = var4;
      int var23 = var1;
      int var28 = var4;
      int var30 = var1;
      int var29 = var1;
      int var31 = var4;
      int var25 = var1;
      int var33 = var4;
      int var27 = var1;
      int var36 = var4;
      int var34 = var1;
      int var37 = var4;
      int var15 = var1;
      int var32 = var1;
      int var39 = var4;
      int var35 = var1;
      int var40 = var4;
      int var38 = var1;
      var1 = var4;
      int var10000;
      byte var47;
      label180:
      switch(var20) {
      case 0:
         var4 = 0;
         var46 = var41;
         var1 = var42;
         break;
      case 1:
         while(true) {
            var6 = var1 & var38;
            if (var17 + 1 >= var19) {
               var1 = var17;
               var46 = var41;
               var4 = var6;
               if (var38 < 0) {
                  var46 = 1;
                  var1 = var17;
                  var4 = var6;
               }
               break label180;
            }

            var1 = var17 + 1;
            var45[var17] = var6;
            var4 = var44.words[var1];
            var38 = var43.words[var1];
            var17 = var1;
            var1 = var4;
         }
      case 2:
         while(true) {
            var6 = var35 & var40;
            if (var14 + 1 >= var19) {
               var1 = var14;
               var46 = var41;
               var4 = var6;
               if (var35 >= 0) {
                  var46 = 1;
                  var1 = var14;
                  var4 = var6;
               }
               break label180;
            }

            var1 = var14 + 1;
            var45[var14] = var6;
            var40 = var44.words[var1];
            var35 = var43.words[var1];
            var14 = var1;
         }
      case 3:
         var46 = 1;
         var1 = var42;
         break;
      case 4:
         while(true) {
            var6 = var39 & var32;
            if (var13 + 1 >= var19) {
               var1 = var13;
               var46 = var41;
               var4 = var6;
               if (var32 < 0) {
                  var46 = 2;
                  var1 = var13;
                  var4 = var6;
               }
               break label180;
            }

            var1 = var13 + 1;
            var45[var13] = var6;
            var39 = var44.words[var1];
            var32 = var43.words[var1];
            var13 = var1;
         }
      case 5:
         while(true) {
            var4 = var15;
            if (var16 + 1 >= var19) {
               var1 = var16;
               var46 = var41;
               break label180;
            }

            var1 = var16 + 1;
            var45[var16] = var15;
            var10000 = var44.words[var1];
            var15 = var43.words[var1];
            var16 = var1;
         }
      case 6:
         while(true) {
            var4 = var37 ^ var34;
            if (var12 + 1 >= var19) {
               if (var34 < 0) {
                  var47 = 2;
               } else {
                  var47 = 1;
               }

               var46 = var47;
               var1 = var12;
               break label180;
            }

            var1 = var12 + 1;
            var45[var12] = var4;
            var37 = var44.words[var1];
            var34 = var43.words[var1];
            var12 = var1;
         }
      case 7:
         while(true) {
            var6 = var36 | var27;
            if (var10 + 1 >= var19) {
               var1 = var10;
               var46 = var41;
               var4 = var6;
               if (var27 >= 0) {
                  var46 = 1;
                  var1 = var10;
                  var4 = var6;
               }
               break label180;
            }

            var1 = var10 + 1;
            var45[var10] = var6;
            var36 = var44.words[var1];
            var27 = var43.words[var1];
            var10 = var1;
         }
      case 8:
         while(true) {
            var6 = var33 | var25;
            if (var9 + 1 >= var19) {
               var1 = var9;
               var46 = var41;
               var4 = var6;
               if (var25 >= 0) {
                  var46 = 2;
                  var1 = var9;
                  var4 = var6;
               }
               break label180;
            }

            var1 = var9 + 1;
            var45[var9] = var6;
            var33 = var44.words[var1];
            var25 = var43.words[var1];
            var9 = var1;
         }
      case 9:
         while(true) {
            var6 = var31 ^ var29;
            if (var5 + 1 >= var19) {
               if (var29 >= 0) {
                  var47 = 2;
               } else {
                  var47 = 1;
               }

               var1 = var5;
               var46 = var47;
               var4 = var6;
               break label180;
            }

            var1 = var5 + 1;
            var45[var5] = var6;
            var31 = var44.words[var1];
            var29 = var43.words[var1];
            var5 = var1;
         }
      case 10:
         while(true) {
            var4 = var30;
            if (var11 + 1 >= var19) {
               var1 = var11;
               var46 = var41;
               break label180;
            }

            var1 = var11 + 1;
            var45[var11] = var30;
            var10000 = var44.words[var1];
            var30 = var43.words[var1];
            var11 = var1;
         }
      case 11:
         while(true) {
            var6 = var23 | var28;
            if (var8 + 1 >= var19) {
               var1 = var8;
               var46 = var41;
               var4 = var6;
               if (var23 < 0) {
                  var46 = 1;
                  var1 = var8;
                  var4 = var6;
               }
               break label180;
            }

            var1 = var8 + 1;
            var45[var8] = var6;
            var28 = var44.words[var1];
            var23 = var43.words[var1];
            var8 = var1;
         }
      case 12:
         var4 = var4;
         var46 = 2;
         var1 = var42;
         break;
      case 13:
         while(true) {
            var6 = var26 | var22;
            if (var7 + 1 >= var19) {
               var1 = var7;
               var46 = var41;
               var4 = var6;
               if (var22 >= 0) {
                  var46 = 2;
                  var1 = var7;
                  var4 = var6;
               }
               break label180;
            }

            var1 = var7 + 1;
            var45[var7] = var6;
            var26 = var44.words[var1];
            var22 = var43.words[var1];
            var7 = var1;
         }
      case 14:
         while(true) {
            var7 = var24 & var21;
            if (var6 + 1 >= var19) {
               var1 = var6;
               var46 = var41;
               var4 = var7;
               if (var21 < 0) {
                  var46 = 2;
                  var1 = var6;
                  var4 = var7;
               }
               break label180;
            }

            var1 = var6 + 1;
            var45[var6] = var7;
            var24 = var44.words[var1];
            var21 = var43.words[var1];
            var6 = var1;
         }
      default:
         var4 = -1;
         var1 = var42;
         var46 = var41;
      }

      if (var1 + 1 == var18) {
         var46 = 0;
      }

      if (var46 != 0) {
         if (var46 != 1) {
            if (var46 == 2) {
               var45[var1] = var4;

               while(true) {
                  var4 = var1 + 1;
                  var1 = var4;
                  if (var4 >= var18) {
                     break;
                  }

                  var45[var4] = var44.words[var4];
                  var1 = var4;
               }
            }
         } else {
            var45[var1] = var4;

            while(true) {
               var4 = var1 + 1;
               var1 = var4;
               if (var4 >= var18) {
                  break;
               }

               var45[var4] = var44.words[var4];
               var1 = var4;
            }
         }
      } else {
         if (var1 == 0 && var45 == null) {
            var0.ival = var4;
            return;
         }

         var45[var1] = var4;
         ++var1;
      }

      var0.ival = var1;
   }

   private void setInvert() {
      if (this.words == null) {
         this.ival = this.ival;
      } else {
         int var1 = this.ival;

         while(true) {
            --var1;
            if (var1 < 0) {
               return;
            }

            int[] var2 = this.words;
            var2[var1] = var2[var1];
         }
      }
   }

   private void setNegative() {
      this.setNegative(this);
   }

   private void setNegative(BigIntegerCrypto var1) {
      int var3 = var1.ival;
      if (var1.words == null) {
         if (var3 == Integer.MIN_VALUE) {
            this.set(-((long)var3));
         } else {
            this.set((long)(-var3));
         }
      } else {
         this.realloc(var3 + 1);
         int var2 = var3;
         if (negate(this.words, var1.words, var3)) {
            this.words[var3] = 0;
            var2 = var3 + 1;
         }

         this.ival = var2;
      }
   }

   private void setShift(BigIntegerCrypto var1, int var2) {
      if (var2 > 0) {
         this.setShiftLeft(var1, var2);
      } else {
         this.setShiftRight(var1, -var2);
      }
   }

   private void setShiftLeft(BigIntegerCrypto var1, int var2) {
      int var3;
      int[] var6;
      int[] var7;
      if (var1.words == null) {
         if (var2 < 32) {
            this.set((long)var1.ival << var2);
            return;
         }

         var6 = new int[]{var1.ival};
         var3 = 1;
         var7 = var6;
      } else {
         var6 = var1.words;
         var3 = var1.ival;
         var7 = var6;
      }

      int var4 = var2 >> 5;
      int var5 = var2 & 31;
      var2 = var3 + var4;
      if (var5 == 0) {
         this.realloc(var2);

         while(true) {
            --var3;
            if (var3 < 0) {
               break;
            }

            this.words[var3 + var4] = var7[var3];
         }
      } else {
         ++var2;
         this.realloc(var2);
         var3 = MPN.lshift(this.words, var4, var7, var3, var5);
         var5 = 32 - var5;
         this.words[var2 - 1] = var3 << var5 >> var5;
      }

      this.ival = var2;
      var2 = var4;

      while(true) {
         --var2;
         if (var2 < 0) {
            return;
         }

         this.words[var2] = 0;
      }
   }

   private void setShiftRight(BigIntegerCrypto var1, int var2) {
      int[] var8 = var1.words;
      long var5 = -1L;
      if (var8 == null) {
         if (var2 < 32) {
            var5 = (long)(var1.ival >> var2);
         } else if (var1.ival >= 0) {
            var5 = 0L;
         }

         this.set(var5);
      } else if (var2 == 0) {
         this.set(var1);
      } else {
         boolean var7 = var1.isNegative();
         int var3 = var2 >> 5;
         var2 &= 31;
         int var4 = var1.ival - var3;
         if (var4 <= 0) {
            if (!var7) {
               var5 = 0L;
            }

            this.set(var5);
         } else {
            var8 = this.words;
            if (var8 == null || var8.length < var4) {
               this.realloc(var4);
            }

            MPN.rshift0(this.words, var1.words, var3, var4, var2);
            this.ival = var4;
            if (var7) {
               int[] var9 = this.words;
               var3 = var4 - 1;
               var9[var3] |= -2 << 31 - var2;
            }

         }
      }
   }

   private static BigIntegerCrypto shift(BigIntegerCrypto var0, int var1) {
      if (var0.words == null) {
         if (var1 <= 0) {
            long var2;
            if (var1 > -32) {
               var2 = (long)(var0.ival >> -var1);
            } else if (var0.ival < 0) {
               var2 = -1L;
            } else {
               var2 = 0L;
            }

            return valueOf(var2);
         }

         if (var1 < 32) {
            return valueOf((long)var0.ival << var1);
         }
      }

      if (var1 == 0) {
         return var0;
      } else {
         BigIntegerCrypto var4 = new BigIntegerCrypto(0);
         var4.setShift(var0, var1);
         return var4.canonicalize();
      }
   }

   private static int swappedOp(int var0) {
      return "\u0000\u0001\u0004\u0005\u0002\u0003\u0006\u0007\b\t\f\r\n\u000b\u000e\u000f".charAt(var0);
   }

   private static BigIntegerCrypto times(BigIntegerCrypto var0, int var1) {
      if (var1 == 0) {
         return ZERO;
      } else if (var1 == 1) {
         return var0;
      } else {
         int[] var6 = var0.words;
         int var5 = var0.ival;
         if (var6 == null) {
            return valueOf((long)var5 * (long)var1);
         } else {
            BigIntegerCrypto var7 = alloc(var5 + 1);
            boolean var2;
            int[] var8;
            if (var6[var5 - 1] < 0) {
               var2 = true;
               negate(var7.words, var6, var5);
               var8 = var7.words;
            } else {
               var2 = false;
               var8 = var6;
            }

            boolean var4 = var2;
            int var3 = var1;
            if (var1 < 0) {
               var4 = var2 ^ true;
               var3 = -var1;
            }

            var6 = var7.words;
            var6[var5] = MPN.mul_1(var6, var8, var5, var3);
            var7.ival = var5 + 1;
            if (var4) {
               var7.setNegative();
            }

            return var7.canonicalize();
         }
      }
   }

   private static BigIntegerCrypto times(BigIntegerCrypto var0, BigIntegerCrypto var1) {
      if (var1.words == null) {
         return times(var0, var1.ival);
      } else if (var0.words == null) {
         return times(var1, var0.ival);
      } else {
         int var3 = var0.ival;
         int var4 = var1.ival;
         boolean var2;
         int[] var7;
         if (var0.isNegative()) {
            var2 = true;
            var7 = new int[var3];
            negate(var7, var0.words, var3);
         } else {
            var2 = false;
            var7 = var0.words;
         }

         int[] var8;
         if (var1.isNegative()) {
            var2 ^= true;
            var8 = new int[var4];
            negate(var8, var1.words, var4);
         } else {
            var8 = var1.words;
         }

         int var6 = var3;
         int var5 = var4;
         int[] var9 = var7;
         int[] var10 = var8;
         if (var3 < var4) {
            var10 = var7;
            var9 = var8;
            var5 = var3;
            var6 = var4;
         }

         BigIntegerCrypto var13 = alloc(var6 + var5);
         MPN.mul(var13.words, var9, var6, var10, var5);
         var8 = var0.words;
         int[] var11 = var9;
         if (var9 != var8) {
            var11 = var9;
            if (var10 != var8) {
               Arrays.fill(var9, 0);
               var11 = null;
            }
         }

         int[] var12 = var1.words;
         if (var10 != var12 && var11 != var12) {
            Arrays.fill(var10, 0);
         }

         var13.ival = var6 + var5;
         if (var2) {
            var13.setNegative();
         }

         return var13.canonicalize();
      }
   }

   public static BigIntegerCrypto valueOf(long var0) {
      if (var0 >= -100L && var0 <= 1024L) {
         return smallFixNums[(int)var0 + 100];
      } else {
         int var2 = (int)var0;
         if ((long)var2 == var0) {
            return new BigIntegerCrypto(var2);
         } else {
            BigIntegerCrypto var3 = alloc(2);
            var3.ival = 2;
            int[] var4 = var3.words;
            var4[0] = var2;
            var4[1] = (int)(var0 >> 32);
            return var3;
         }
      }
   }

   private static BigIntegerCrypto valueOf(byte[] var0, int var1, boolean var2, int var3) {
      int[] var4 = new int[var1 / MPN.chars_per_word(var3) + 1];
      var3 = MPN.set_str(var4, var0, var1, var3);
      if (var3 == 0) {
         return ZERO;
      } else {
         var1 = var3;
         if (var4[var3 - 1] < 0) {
            var4[var3] = 0;
            var1 = var3 + 1;
         }

         if (var2) {
            negate(var4, var4, var1);
         }

         return make(var4, var1);
      }
   }

   private static int wordsNeeded(int[] var0, int var1) {
      int var2 = var1;
      var1 = var1;
      if (var2 > 0) {
         var1 = var2 - 1;
         int var4 = var0[var1];
         var2 = var1;
         int var3 = var4;
         if (var4 == -1) {
            var2 = var1;

            do {
               var1 = var2;
               if (var2 <= 0) {
                  break;
               }

               var3 = var0[var2 - 1];
               var1 = var2;
               if (var3 >= 0) {
                  break;
               }

               var1 = var2 - 1;
               var2 = var1;
            } while(var3 == -1);
         } else {
            while(true) {
               var1 = var2;
               if (var3 != 0) {
                  break;
               }

               var1 = var2;
               if (var2 <= 0) {
                  break;
               }

               var4 = var0[var2 - 1];
               var3 = var4;
               var1 = var2;
               if (var4 < 0) {
                  break;
               }

               --var2;
            }
         }
      }

      return var1 + 1;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      int var2 = this.signum();
      this.signum = var2;
      byte[] var3;
      if (var2 == 0) {
         var3 = new byte[0];
      } else {
         var3 = this.toByteArray();
      }

      this.magnitude = var3;
      var1.defaultWriteObject();
      Arrays.fill(this.magnitude, (byte)0);
      this.magnitude = null;
   }

   public BigIntegerCrypto abs() {
      return abs(this);
   }

   public BigIntegerCrypto add(BigIntegerCrypto var1) {
      return add(this, var1, 1);
   }

   public BigIntegerCrypto and(BigIntegerCrypto var1) {
      if (var1.words == null) {
         return and(this, var1.ival);
      } else if (this.words == null) {
         return and(var1, this.ival);
      } else {
         BigIntegerCrypto var6 = this;
         BigIntegerCrypto var5 = var1;
         if (this.ival < var1.ival) {
            var5 = this;
            var6 = var1;
         }

         int var3;
         if (var5.isNegative()) {
            var3 = var6.ival;
         } else {
            var3 = var5.ival;
         }

         int[] var7 = new int[var3];
         int var2 = 0;

         while(true) {
            int var4 = var2;
            if (var2 >= var5.ival) {
               while(var4 < var3) {
                  var7[var4] = var6.words[var4];
                  ++var4;
               }

               return make(var7, var3);
            }

            var7[var2] = var6.words[var2] & var5.words[var2];
            ++var2;
         }
      }
   }

   public BigIntegerCrypto andNot(BigIntegerCrypto var1) {
      return this.and(var1.not());
   }

   public int bitCount() {
      int[] var3 = this.words;
      int var1;
      int var2;
      if (var3 == null) {
         var1 = 1;
         var2 = bitCount(this.ival);
      } else {
         var1 = this.ival;
         var2 = bitCount(var3, var1);
      }

      return this.isNegative() ? var1 * 32 - var2 : var2;
   }

   public int bitLength() {
      int[] var1 = this.words;
      return var1 == null ? MPN.intLength(this.ival) : MPN.intLength(var1, this.ival);
   }

   public BigIntegerCrypto clearBit(int var1) {
      if (var1 >= 0) {
         return this.and(ONE.shiftLeft(var1).not());
      } else {
         throw new ArithmeticException();
      }
   }

   public int compareTo(BigIntegerCrypto var1) {
      return compareTo(this, var1);
   }

   public BigIntegerCrypto divide(BigIntegerCrypto var1) {
      if (!var1.isZero()) {
         BigIntegerCrypto var2 = new BigIntegerCrypto();
         divide(this, var1, var2, (BigIntegerCrypto)null, 3);
         return var2.canonicalize();
      } else {
         throw new ArithmeticException("divisor is zero");
      }
   }

   public BigIntegerCrypto[] divideAndRemainder(BigIntegerCrypto var1) {
      if (!var1.isZero()) {
         BigIntegerCrypto[] var2 = new BigIntegerCrypto[]{new BigIntegerCrypto(), new BigIntegerCrypto()};
         divide(this, var1, var2[0], var2[1], 3);
         var2[0].canonicalize();
         var2[1].canonicalize();
         return var2;
      } else {
         throw new ArithmeticException("divisor is zero");
      }
   }

   public double doubleValue() {
      if (this.words == null) {
         return (double)this.ival;
      } else if (this.ival <= 2) {
         return (double)this.longValue();
      } else {
         return this.isNegative() ? neg(this).roundToDouble(0, true, false) : this.roundToDouble(0, false, false);
      }
   }

   public boolean equals(Object var1) {
      return !(var1 instanceof BigIntegerCrypto) ? false : equals(this, (BigIntegerCrypto)var1);
   }

   public BigIntegerCrypto flipBit(int var1) {
      if (var1 >= 0) {
         return this.xor(ONE.shiftLeft(var1));
      } else {
         throw new ArithmeticException();
      }
   }

   public float floatValue() {
      return (float)this.doubleValue();
   }

   public BigIntegerCrypto gcd(BigIntegerCrypto var1) {
      int var3 = this.ival;
      int var4 = var1.ival;
      int var2 = var3;
      if (this.words == null) {
         if (var3 == 0) {
            return abs(var1);
         }

         if (var1.words == null && var3 != Integer.MIN_VALUE && var4 != Integer.MIN_VALUE) {
            var2 = var3;
            if (var3 < 0) {
               var2 = -var3;
            }

            var3 = var4;
            if (var4 < 0) {
               var3 = -var4;
            }

            return valueOf((long)gcd(var2, var3));
         }

         var2 = 1;
      }

      var3 = var4;
      if (var1.words == null) {
         if (var4 == 0) {
            return abs(this);
         }

         var3 = 1;
      }

      if (var2 <= var3) {
         var2 = var3;
      }

      ++var2;
      int[] var5 = new int[var2];
      int[] var6 = new int[var2];
      this.getAbsolute(var5);
      var1.getAbsolute(var6);
      var2 = MPN.gcd(var5, var6, var2);
      var1 = new BigIntegerCrypto(0);
      var1.ival = var2;
      var1.words = var5;
      return var1.canonicalize();
   }

   public int getLowestSetBit() {
      if (this.isZero()) {
         return -1;
      } else {
         int[] var1 = this.words;
         return var1 == null ? MPN.findLowestBit(this.ival) : MPN.findLowestBit(var1);
      }
   }

   public int hashCode() {
      int[] var2 = this.words;
      if (var2 == null) {
         return this.ival;
      } else {
         int var1 = var2[0];
         return var2[this.ival - 1] + var1;
      }
   }

   public int intValue() {
      int[] var1 = this.words;
      return var1 == null ? this.ival : var1[0];
   }

   public boolean isProbablePrime(int var1) {
      if (var1 < 1) {
         return true;
      } else {
         BigIntegerCrypto var6 = new BigIntegerCrypto();
         int var2 = 0;

         while(true) {
            int[] var7 = primes;
            if (var2 >= var7.length) {
               BigIntegerCrypto var10 = add(this, -1);
               int var5 = var10.getLowestSetBit();
               BigIntegerCrypto var8 = var10.divide(valueOf(2L).pow(var5));
               int var3 = this.bitLength();
               var2 = 0;

               while(true) {
                  int[] var9 = field_77;
                  if (var2 >= var9.length || var3 <= var9[var2]) {
                     var3 = field_78[var2];
                     var2 = var3;
                     if (var1 > 80) {
                        var2 = var3 * 2;
                     }

                     for(var3 = 0; var3 < var2; ++var3) {
                        var6 = smallFixNums[primes[var3] + 100].modPow(var8, this);
                        if (!var6.isOne() && !var6.equals(var10)) {
                           var1 = 0;

                           int var4;
                           while(true) {
                              var4 = var1;
                              if (var1 >= var5) {
                                 break;
                              }

                              if (var6.isOne()) {
                                 return false;
                              }

                              ++var1;
                              if (var6.equals(var10)) {
                                 var4 = var1;
                                 break;
                              }

                              var6 = var6.modPow(valueOf(2L), this);
                           }

                           if (var4 == var5 && !var6.equals(var10)) {
                              return false;
                           }
                        }
                     }

                     return true;
                  }

                  ++var2;
               }
            }

            if (this.words == null && this.ival == var7[var2]) {
               return true;
            }

            divide(this, smallFixNums[primes[var2] + 100], (BigIntegerCrypto)null, var6, 3);
            if (var6.canonicalize().isZero()) {
               return false;
            }

            ++var2;
         }
      }
   }

   public long longValue() {
      int[] var1 = this.words;
      if (var1 == null) {
         return (long)this.ival;
      } else {
         return this.ival == 1 ? (long)var1[0] : ((long)var1[1] << 32) + ((long)var1[0] & 4294967295L);
      }
   }

   public BigIntegerCrypto max(BigIntegerCrypto var1) {
      return compareTo(this, var1) > 0 ? this : var1;
   }

   public BigIntegerCrypto min(BigIntegerCrypto var1) {
      return compareTo(this, var1) < 0 ? this : var1;
   }

   public BigIntegerCrypto mod(BigIntegerCrypto var1) {
      if (!var1.isNegative() && !var1.isZero()) {
         BigIntegerCrypto var2 = new BigIntegerCrypto();
         divide(this, var1, (BigIntegerCrypto)null, var2, 1);
         return var2.canonicalize();
      } else {
         throw new ArithmeticException("non-positive modulus");
      }
   }

   public BigIntegerCrypto modInverse(BigIntegerCrypto var1) {
      if (!var1.isNegative() && !var1.isZero()) {
         if (var1.isOne()) {
            return ZERO;
         } else if (this.isOne()) {
            return ONE;
         } else {
            BigIntegerCrypto var7 = new BigIntegerCrypto();
            boolean var2 = false;
            byte var6 = 0;
            if (var1.words != null) {
               if (this.isNegative()) {
                  var7 = this.mod(var1);
               } else {
                  var7 = this;
               }

               BigIntegerCrypto var9 = var7;
               BigIntegerCrypto var8 = var1;
               if (var7.compareTo(var1) < 0) {
                  var2 = true;
                  var8 = var7;
                  var9 = var1;
               }

               var1 = new BigIntegerCrypto();
               var7 = new BigIntegerCrypto();
               divide(var9, var8, var7, var1, 1);
               var1.canonicalize();
               var7.canonicalize();
               BigIntegerCrypto[] var10 = new BigIntegerCrypto[2];
               euclidInv(var8, var1, var7, var10);
               if (var2) {
                  var1 = var10[0];
               } else {
                  var1 = var10[1];
               }

               var7 = var1;
               if (var1.isNegative()) {
                  if (var2) {
                     var8 = var9;
                  }

                  var7 = add(var1, var8, 1);
               }

               return var7;
            } else {
               int var11;
               if (this.words == null && !this.isNegative()) {
                  var11 = this.ival;
               } else {
                  var11 = this.mod(var1).ival;
               }

               int var4 = var1.ival;
               int var5 = var11;
               int var3 = var4;
               if (var4 > var11) {
                  var6 = 1;
                  var3 = var11;
                  var5 = var4;
               }

               var11 = euclidInv(var3, var5 % var3, var5 / var3)[var6 ^ 1];
               var7.ival = var11;
               if (var11 < 0) {
                  var7.ival = var11 + var1.ival;
               }

               return var7;
            }
         }
      } else {
         throw new ArithmeticException("non-positive modulo");
      }
   }

   public BigIntegerCrypto modPow(BigIntegerCrypto var1, BigIntegerCrypto var2) {
      if (!var2.isNegative() && !var2.isZero()) {
         if (var1.isNegative()) {
            return this.modInverse(var2).modPow(var1.negate(), var2);
         } else if (var1.isOne()) {
            return this.mod(var2);
         } else {
            BigIntegerCrypto var5 = ONE;
            BigIntegerCrypto var3 = var1;
            var1 = this;

            BigIntegerCrypto var4;
            for(var4 = var5; !var3.isZero(); var4 = var5) {
               var5 = var4;
               if (var3.and(ONE).isOne()) {
                  var5 = times(var4, var1).mod(var2);
               }

               var3 = var3.shiftRight(1);
               var1 = times(var1, var1).mod(var2);
            }

            return var4;
         }
      } else {
         throw new ArithmeticException("non-positive modulo");
      }
   }

   public BigIntegerCrypto multiply(BigIntegerCrypto var1) {
      return times(this, var1);
   }

   public BigIntegerCrypto negate() {
      return neg(this);
   }

   public BigIntegerCrypto not() {
      return bitOp(12, this, ZERO);
   }

   // $FF: renamed from: or (gnu.java.bigintcrypto.BigIntegerCrypto) gnu.java.bigintcrypto.BigIntegerCrypto
   public BigIntegerCrypto method_25(BigIntegerCrypto var1) {
      return bitOp(7, this, var1);
   }

   public BigIntegerCrypto pow(int var1) {
      if (var1 <= 0) {
         if (var1 == 0) {
            return ONE;
         } else {
            throw new ArithmeticException("negative exponent");
         }
      } else if (this.isZero()) {
         return this;
      } else {
         int var2;
         if (this.words == null) {
            var2 = 1;
         } else {
            var2 = this.ival;
         }

         int var4 = (this.bitLength() * var1 >> 5) + var2 * 2;
         boolean var3;
         if (this.isNegative() && (var1 & 1) != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         int[] var7 = new int[var4];
         int[] var8 = new int[var4];
         int[] var6 = new int[var4];
         this.getAbsolute(var7);
         int var5 = 1;
         var8[0] = 1;
         var4 = var1;

         while(true) {
            int[] var10 = var8;
            int[] var9 = var6;
            var1 = var5;
            if ((var4 & 1) != 0) {
               MPN.mul(var6, var7, var2, var8, var5);

               for(var1 = var5 + var2; var6[var1 - 1] == 0; --var1) {
               }

               var9 = var8;
               var10 = var6;
            }

            var4 >>= 1;
            if (var4 == 0) {
               var2 = var1;
               if (var10[var1 - 1] < 0) {
                  var2 = var1 + 1;
               }

               if (var3) {
                  negate(var10, var10, var2);
               }

               return make(var10, var2);
            }

            MPN.mul(var9, var7, var2, var7, var2);
            var6 = var7;
            var7 = var9;

            for(var2 *= 2; var7[var2 - 1] == 0; --var2) {
            }

            var8 = var10;
            var5 = var1;
         }
      }
   }

   public BigIntegerCrypto remainder(BigIntegerCrypto var1) {
      if (!var1.isZero()) {
         BigIntegerCrypto var2 = new BigIntegerCrypto();
         divide(this, var1, (BigIntegerCrypto)null, var2, 3);
         return var2.canonicalize();
      } else {
         throw new ArithmeticException("divisor is zero");
      }
   }

   public BigIntegerCrypto setBit(int var1) {
      if (var1 >= 0) {
         return this.method_25(ONE.shiftLeft(var1));
      } else {
         throw new ArithmeticException();
      }
   }

   public BigIntegerCrypto shiftLeft(int var1) {
      return var1 == 0 ? this : shift(this, var1);
   }

   public BigIntegerCrypto shiftRight(int var1) {
      return var1 == 0 ? this : shift(this, -var1);
   }

   public int signum() {
      if (this.ival == 0 && this.words == null) {
         return 0;
      } else {
         int[] var3 = this.words;
         byte var2 = 1;
         int var1;
         if (var3 == null) {
            var1 = this.ival;
         } else {
            var1 = var3[this.ival - 1];
         }

         if (var1 < 0) {
            var2 = -1;
         }

         return var2;
      }
   }

   public BigIntegerCrypto subtract(BigIntegerCrypto var1) {
      return add(this, var1, -1);
   }

   public boolean testBit(int var1) {
      if (var1 >= 0) {
         return this.and(ONE.shiftLeft(var1)).isZero() ^ true;
      } else {
         throw new ArithmeticException();
      }
   }

   public byte[] toByteArray() {
      if (this.signum() == 0) {
         return new byte[1];
      } else {
         byte[] var5 = new byte[(this.bitLength() + 1 + 7) / 8];
         int var1 = var5.length;

         int var2;
         for(var2 = 0; var1 > 4; ++var2) {
            int var3 = this.words[var2];

            for(int var4 = 4; var4 > 0; var3 >>= 8) {
               --var1;
               var5[var1] = (byte)var3;
               --var4;
            }
         }

         int[] var6 = this.words;
         if (var6 == null) {
            var2 = this.ival;
         } else {
            var2 = var6[var2];
         }

         while(var1 > 0) {
            --var1;
            var5[var1] = (byte)var2;
            var2 >>= 8;
         }

         return var5;
      }
   }

   public String toString() {
      return this.toString(10);
   }

   public String toString(int var1) {
      if (this.words == null) {
         return Integer.toString(this.ival, var1);
      } else {
         int var2 = this.ival;
         if (var2 <= 2) {
            return Long.toString(this.longValue(), var1);
         } else {
            StringBuffer var3 = new StringBuffer(var2 * (MPN.chars_per_word(var1) + 1));
            this.format(var1, var3);
            return var3.toString();
         }
      }
   }

   public BigIntegerCrypto xor(BigIntegerCrypto var1) {
      return bitOp(6, this, var1);
   }

   public void zeroize() {
      int[] var1 = this.words;
      if (var1 != null) {
         Arrays.fill(var1, 0);
         this.words = null;
         this.ival = 0;
      }

   }
}
