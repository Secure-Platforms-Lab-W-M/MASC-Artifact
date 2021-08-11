package org.apache.commons.lang3.math;

import java.math.BigInteger;
import org.apache.commons.lang3.Validate;

public final class Fraction extends Number implements Comparable {
   public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);
   public static final Fraction ONE = new Fraction(1, 1);
   public static final Fraction ONE_FIFTH = new Fraction(1, 5);
   public static final Fraction ONE_HALF = new Fraction(1, 2);
   public static final Fraction ONE_QUARTER = new Fraction(1, 4);
   public static final Fraction ONE_THIRD = new Fraction(1, 3);
   public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
   public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
   public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
   public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
   public static final Fraction TWO_THIRDS = new Fraction(2, 3);
   public static final Fraction ZERO = new Fraction(0, 1);
   private static final long serialVersionUID = 65382027393090L;
   private final int denominator;
   private transient int hashCode = 0;
   private final int numerator;
   private transient String toProperString = null;
   private transient String toString = null;

   private Fraction(int var1, int var2) {
      this.numerator = var1;
      this.denominator = var2;
   }

   private static int addAndCheck(int var0, int var1) {
      long var2 = (long)var0 + (long)var1;
      if (var2 >= -2147483648L && var2 <= 2147483647L) {
         return (int)var2;
      } else {
         throw new ArithmeticException("overflow: add");
      }
   }

   private Fraction addSub(Fraction var1, boolean var2) {
      boolean var5;
      if (var1 != null) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "The fraction must not be null");
      if (this.numerator == 0) {
         return var2 ? var1 : var1.negate();
      } else if (var1.numerator == 0) {
         return this;
      } else {
         int var4 = greatestCommonDivisor(this.denominator, var1.denominator);
         int var3;
         if (var4 == 1) {
            var3 = mulAndCheck(this.numerator, var1.denominator);
            var4 = mulAndCheck(var1.numerator, this.denominator);
            if (var2) {
               var3 = addAndCheck(var3, var4);
            } else {
               var3 = subAndCheck(var3, var4);
            }

            return new Fraction(var3, mulPosAndCheck(this.denominator, var1.denominator));
         } else {
            BigInteger var6 = BigInteger.valueOf((long)this.numerator).multiply(BigInteger.valueOf((long)(var1.denominator / var4)));
            BigInteger var7 = BigInteger.valueOf((long)var1.numerator).multiply(BigInteger.valueOf((long)(this.denominator / var4)));
            if (var2) {
               var6 = var6.add(var7);
            } else {
               var6 = var6.subtract(var7);
            }

            var3 = var6.mod(BigInteger.valueOf((long)var4)).intValue();
            if (var3 == 0) {
               var3 = var4;
            } else {
               var3 = greatestCommonDivisor(var3, var4);
            }

            var6 = var6.divide(BigInteger.valueOf((long)var3));
            if (var6.bitLength() <= 31) {
               return new Fraction(var6.intValue(), mulPosAndCheck(this.denominator / var4, var1.denominator / var3));
            } else {
               throw new ArithmeticException("overflow: numerator too large after multiply");
            }
         }
      }
   }

   public static Fraction getFraction(double var0) {
      byte var16;
      if (var0 < 0.0D) {
         var16 = -1;
      } else {
         var16 = 1;
      }

      var0 = Math.abs(var0);
      if (var0 <= 2.147483647E9D && !Double.isNaN(var0)) {
         int var25 = (int)var0;
         double var10 = var0 - (double)var25;
         int var19 = 0;
         int var20 = 1;
         int var14 = 1;
         int var15 = 0;
         boolean var22 = false;
         boolean var21 = false;
         int var18 = (int)var10;
         double var2 = 1.0D;
         var0 = var10 - (double)var18;
         double var4 = Double.MAX_VALUE;
         int var17 = 1;

         while(true) {
            double var6 = var4;
            double var8 = var2;
            int var27 = (int)(var2 / var0);
            double var12 = (double)var27;
            int var26 = var18 * var14 + var19;
            var18 = var18 * var15 + var20;
            var4 = Math.abs(var10 - (double)var26 / (double)var18);
            var2 = var0;
            ++var17;
            if (var6 <= var4 || var18 > 10000 || var18 <= 0 || var17 >= 25) {
               if (var17 != 25) {
                  return getReducedFraction((var14 + var25 * var15) * var16, var15);
               } else {
                  throw new ArithmeticException("Unable to convert double to fraction");
               }
            }

            var0 = var8 - var12 * var0;
            var19 = var14;
            var20 = var15;
            var14 = var26;
            var15 = var18;
            var18 = var27;
         }
      } else {
         throw new ArithmeticException("The value must not be greater than Integer.MAX_VALUE or NaN");
      }
   }

   public static Fraction getFraction(int var0, int var1) {
      if (var1 == 0) {
         throw new ArithmeticException("The denominator must not be zero");
      } else {
         int var2 = var0;
         int var3 = var1;
         if (var1 < 0) {
            if (var0 == Integer.MIN_VALUE || var1 == Integer.MIN_VALUE) {
               throw new ArithmeticException("overflow: can't negate");
            }

            var2 = -var0;
            var3 = -var1;
         }

         return new Fraction(var2, var3);
      }
   }

   public static Fraction getFraction(int var0, int var1, int var2) {
      if (var2 != 0) {
         if (var2 >= 0) {
            if (var1 >= 0) {
               long var3;
               if (var0 < 0) {
                  var3 = (long)var0 * (long)var2 - (long)var1;
               } else {
                  var3 = (long)var0 * (long)var2 + (long)var1;
               }

               if (var3 >= -2147483648L && var3 <= 2147483647L) {
                  return new Fraction((int)var3, var2);
               } else {
                  throw new ArithmeticException("Numerator too large to represent as an Integer.");
               }
            } else {
               throw new ArithmeticException("The numerator must not be negative");
            }
         } else {
            throw new ArithmeticException("The denominator must not be negative");
         }
      } else {
         throw new ArithmeticException("The denominator must not be zero");
      }
   }

   public static Fraction getFraction(String var0) {
      boolean var3;
      if (var0 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "The string must not be null");
      if (var0.indexOf(46) >= 0) {
         return getFraction(Double.parseDouble(var0));
      } else {
         int var2 = var0.indexOf(32);
         int var1;
         if (var2 > 0) {
            var1 = Integer.parseInt(var0.substring(0, var2));
            var0 = var0.substring(var2 + 1);
            var2 = var0.indexOf(47);
            if (var2 >= 0) {
               return getFraction(var1, Integer.parseInt(var0.substring(0, var2)), Integer.parseInt(var0.substring(var2 + 1)));
            } else {
               throw new NumberFormatException("The fraction could not be parsed as the format X Y/Z");
            }
         } else {
            var1 = var0.indexOf(47);
            return var1 < 0 ? getFraction(Integer.parseInt(var0), 1) : getFraction(Integer.parseInt(var0.substring(0, var1)), Integer.parseInt(var0.substring(var1 + 1)));
         }
      }
   }

   public static Fraction getReducedFraction(int var0, int var1) {
      if (var1 == 0) {
         throw new ArithmeticException("The denominator must not be zero");
      } else if (var0 == 0) {
         return ZERO;
      } else {
         int var3 = var0;
         int var2 = var1;
         if (var1 == Integer.MIN_VALUE) {
            var3 = var0;
            var2 = var1;
            if ((var0 & 1) == 0) {
               var3 = var0 / 2;
               var2 = var1 / 2;
            }
         }

         var0 = var3;
         var1 = var2;
         if (var2 < 0) {
            if (var3 == Integer.MIN_VALUE || var2 == Integer.MIN_VALUE) {
               throw new ArithmeticException("overflow: can't negate");
            }

            var0 = -var3;
            var1 = -var2;
         }

         var2 = greatestCommonDivisor(var0, var1);
         return new Fraction(var0 / var2, var1 / var2);
      }
   }

   private static int greatestCommonDivisor(int var0, int var1) {
      if (var0 != 0 && var1 != 0) {
         if (Math.abs(var0) == 1) {
            return 1;
         } else if (Math.abs(var1) == 1) {
            return 1;
         } else {
            int var2 = var0;
            if (var0 > 0) {
               var2 = -var0;
            }

            var0 = var1;
            if (var1 > 0) {
               var0 = -var1;
            }

            int var3 = 0;

            for(var1 = var0; (var2 & 1) == 0 && (var1 & 1) == 0 && var3 < 31; ++var3) {
               var2 /= 2;
               var1 /= 2;
            }

            if (var3 == 31) {
               throw new ArithmeticException("overflow: gcd is 2^31");
            } else {
               if ((var2 & 1) == 1) {
                  var0 = var1;
               } else {
                  var0 = -(var2 / 2);
               }

               while(true) {
                  while((var0 & 1) != 0) {
                     int var4;
                     if (var0 > 0) {
                        var4 = -var0;
                     } else {
                        var1 = var0;
                        var4 = var2;
                     }

                     int var5 = (var1 - var4) / 2;
                     var0 = var5;
                     var2 = var4;
                     if (var5 == 0) {
                        return -var4 * (1 << var3);
                     }
                  }

                  var0 /= 2;
               }
            }
         }
      } else if (var0 != Integer.MIN_VALUE && var1 != Integer.MIN_VALUE) {
         return Math.abs(var0) + Math.abs(var1);
      } else {
         throw new ArithmeticException("overflow: gcd is 2^31");
      }
   }

   private static int mulAndCheck(int var0, int var1) {
      long var2 = (long)var0 * (long)var1;
      if (var2 >= -2147483648L && var2 <= 2147483647L) {
         return (int)var2;
      } else {
         throw new ArithmeticException("overflow: mul");
      }
   }

   private static int mulPosAndCheck(int var0, int var1) {
      long var2 = (long)var0 * (long)var1;
      if (var2 <= 2147483647L) {
         return (int)var2;
      } else {
         throw new ArithmeticException("overflow: mulPos");
      }
   }

   private static int subAndCheck(int var0, int var1) {
      long var2 = (long)var0 - (long)var1;
      if (var2 >= -2147483648L && var2 <= 2147483647L) {
         return (int)var2;
      } else {
         throw new ArithmeticException("overflow: add");
      }
   }

   public Fraction abs() {
      return this.numerator >= 0 ? this : this.negate();
   }

   public Fraction add(Fraction var1) {
      return this.addSub(var1, true);
   }

   public int compareTo(Fraction var1) {
      if (this == var1) {
         return 0;
      } else {
         return this.numerator == var1.numerator && this.denominator == var1.denominator ? 0 : Long.compare((long)this.numerator * (long)var1.denominator, (long)var1.numerator * (long)this.denominator);
      }
   }

   public Fraction divideBy(Fraction var1) {
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "The fraction must not be null");
      if (var1.numerator != 0) {
         return this.multiplyBy(var1.invert());
      } else {
         throw new ArithmeticException("The fraction to divide by must not be zero");
      }
   }

   public double doubleValue() {
      return (double)this.numerator / (double)this.denominator;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Fraction)) {
         return false;
      } else {
         Fraction var2 = (Fraction)var1;
         return this.getNumerator() == var2.getNumerator() && this.getDenominator() == var2.getDenominator();
      }
   }

   public float floatValue() {
      return (float)this.numerator / (float)this.denominator;
   }

   public int getDenominator() {
      return this.denominator;
   }

   public int getNumerator() {
      return this.numerator;
   }

   public int getProperNumerator() {
      return Math.abs(this.numerator % this.denominator);
   }

   public int getProperWhole() {
      return this.numerator / this.denominator;
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = (this.getNumerator() + 629) * 37 + this.getDenominator();
      }

      return this.hashCode;
   }

   public int intValue() {
      return this.numerator / this.denominator;
   }

   public Fraction invert() {
      int var1 = this.numerator;
      if (var1 != 0) {
         if (var1 != Integer.MIN_VALUE) {
            return var1 < 0 ? new Fraction(-this.denominator, -this.numerator) : new Fraction(this.denominator, this.numerator);
         } else {
            throw new ArithmeticException("overflow: can't negate numerator");
         }
      } else {
         throw new ArithmeticException("Unable to invert zero.");
      }
   }

   public long longValue() {
      return (long)this.numerator / (long)this.denominator;
   }

   public Fraction multiplyBy(Fraction var1) {
      boolean var4;
      if (var1 != null) {
         var4 = true;
      } else {
         var4 = false;
      }

      Validate.isTrue(var4, "The fraction must not be null");
      int var2 = this.numerator;
      if (var2 != 0 && var1.numerator != 0) {
         var2 = greatestCommonDivisor(var2, var1.denominator);
         int var3 = greatestCommonDivisor(var1.numerator, this.denominator);
         return getReducedFraction(mulAndCheck(this.numerator / var2, var1.numerator / var3), mulPosAndCheck(this.denominator / var3, var1.denominator / var2));
      } else {
         return ZERO;
      }
   }

   public Fraction negate() {
      if (this.numerator != Integer.MIN_VALUE) {
         return new Fraction(-this.numerator, this.denominator);
      } else {
         throw new ArithmeticException("overflow: too large to negate");
      }
   }

   public Fraction pow(int var1) {
      if (var1 == 1) {
         return this;
      } else if (var1 == 0) {
         return ONE;
      } else if (var1 < 0) {
         return var1 == Integer.MIN_VALUE ? this.invert().pow(2).pow(-(var1 / 2)) : this.invert().pow(-var1);
      } else {
         Fraction var2 = this.multiplyBy(this);
         if (var1 % 2 == 0) {
            return var2.pow(var1 / 2);
         } else {
            var1 /= 2;

            try {
               var2 = var2.pow(var1);
            } finally {
               ;
            }

            return var2.multiplyBy(this);
         }
      }
   }

   public Fraction reduce() {
      int var1 = this.numerator;
      if (var1 == 0) {
         return this.equals(ZERO) ? this : ZERO;
      } else {
         var1 = greatestCommonDivisor(Math.abs(var1), this.denominator);
         return var1 == 1 ? this : getFraction(this.numerator / var1, this.denominator / var1);
      }
   }

   public Fraction subtract(Fraction var1) {
      return this.addSub(var1, false);
   }

   public String toProperString() {
      if (this.toProperString == null) {
         int var2 = this.numerator;
         if (var2 == 0) {
            this.toProperString = "0";
         } else {
            int var1 = this.denominator;
            if (var2 == var1) {
               this.toProperString = "1";
            } else if (var2 == var1 * -1) {
               this.toProperString = "-1";
            } else {
               var1 = var2;
               if (var2 > 0) {
                  var1 = -var2;
               }

               StringBuilder var3;
               if (var1 < -this.denominator) {
                  var1 = this.getProperNumerator();
                  if (var1 == 0) {
                     this.toProperString = Integer.toString(this.getProperWhole());
                  } else {
                     var3 = new StringBuilder();
                     var3.append(this.getProperWhole());
                     var3.append(" ");
                     var3.append(var1);
                     var3.append("/");
                     var3.append(this.getDenominator());
                     this.toProperString = var3.toString();
                  }
               } else {
                  var3 = new StringBuilder();
                  var3.append(this.getNumerator());
                  var3.append("/");
                  var3.append(this.getDenominator());
                  this.toProperString = var3.toString();
               }
            }
         }
      }

      return this.toProperString;
   }

   public String toString() {
      if (this.toString == null) {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.getNumerator());
         var1.append("/");
         var1.append(this.getDenominator());
         this.toString = var1.toString();
      }

      return this.toString;
   }
}
