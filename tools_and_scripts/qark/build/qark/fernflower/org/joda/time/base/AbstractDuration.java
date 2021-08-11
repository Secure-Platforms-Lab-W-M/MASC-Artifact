package org.joda.time.base;

import org.joda.convert.ToString;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.ReadableDuration;
import org.joda.time.format.FormatUtils;

public abstract class AbstractDuration implements ReadableDuration {
   protected AbstractDuration() {
   }

   public int compareTo(ReadableDuration var1) {
      long var2 = this.getMillis();
      long var4 = var1.getMillis();
      if (var2 < var4) {
         return -1;
      } else {
         return var2 > var4 ? 1 : 0;
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ReadableDuration)) {
         return false;
      } else {
         ReadableDuration var2 = (ReadableDuration)var1;
         return this.getMillis() == var2.getMillis();
      }
   }

   public int hashCode() {
      long var1 = this.getMillis();
      return (int)(var1 ^ var1 >>> 32);
   }

   public boolean isEqual(ReadableDuration var1) {
      if (var1 == null) {
         var1 = Duration.ZERO;
      }

      return this.compareTo((ReadableDuration)var1) == 0;
   }

   public boolean isLongerThan(ReadableDuration var1) {
      if (var1 == null) {
         var1 = Duration.ZERO;
      }

      return this.compareTo((ReadableDuration)var1) > 0;
   }

   public boolean isShorterThan(ReadableDuration var1) {
      if (var1 == null) {
         var1 = Duration.ZERO;
      }

      return this.compareTo((ReadableDuration)var1) < 0;
   }

   public Duration toDuration() {
      return new Duration(this.getMillis());
   }

   public Period toPeriod() {
      return new Period(this.getMillis());
   }

   @ToString
   public String toString() {
      long var5 = this.getMillis();
      StringBuffer var7 = new StringBuffer();
      var7.append("PT");
      boolean var1;
      if (var5 < 0L) {
         var1 = true;
      } else {
         var1 = false;
      }

      FormatUtils.appendUnpaddedInteger(var7, var5);

      while(true) {
         int var4 = var7.length();
         byte var2;
         if (var1) {
            var2 = 7;
         } else {
            var2 = 6;
         }

         byte var3 = 3;
         if (var4 >= var2) {
            if (var5 / 1000L * 1000L == var5) {
               var7.setLength(var7.length() - 3);
            } else {
               var7.insert(var7.length() - 3, ".");
            }

            var7.append('S');
            return var7.toString();
         }

         if (var1) {
            var2 = var3;
         } else {
            var2 = 2;
         }

         var7.insert(var2, "0");
      }
   }
}
