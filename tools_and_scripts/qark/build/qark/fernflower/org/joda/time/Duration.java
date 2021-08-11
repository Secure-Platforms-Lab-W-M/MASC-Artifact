package org.joda.time;

import java.io.Serializable;
import java.math.RoundingMode;
import org.joda.convert.FromString;
import org.joda.time.base.BaseDuration;
import org.joda.time.field.FieldUtils;

public final class Duration extends BaseDuration implements ReadableDuration, Serializable {
   public static final Duration ZERO = new Duration(0L);
   private static final long serialVersionUID = 2471658376918L;

   public Duration(long var1) {
      super(var1);
   }

   public Duration(long var1, long var3) {
      super(var1, var3);
   }

   public Duration(Object var1) {
      super(var1);
   }

   public Duration(ReadableInstant var1, ReadableInstant var2) {
      super(var1, var2);
   }

   public static Duration millis(long var0) {
      return var0 == 0L ? ZERO : new Duration(var0);
   }

   @FromString
   public static Duration parse(String var0) {
      return new Duration(var0);
   }

   public static Duration standardDays(long var0) {
      return var0 == 0L ? ZERO : new Duration(FieldUtils.safeMultiply(var0, 86400000));
   }

   public static Duration standardHours(long var0) {
      return var0 == 0L ? ZERO : new Duration(FieldUtils.safeMultiply(var0, 3600000));
   }

   public static Duration standardMinutes(long var0) {
      return var0 == 0L ? ZERO : new Duration(FieldUtils.safeMultiply(var0, 60000));
   }

   public static Duration standardSeconds(long var0) {
      return var0 == 0L ? ZERO : new Duration(FieldUtils.safeMultiply(var0, 1000));
   }

   public Duration dividedBy(long var1) {
      return var1 == 1L ? this : new Duration(FieldUtils.safeDivide(this.getMillis(), var1));
   }

   public Duration dividedBy(long var1, RoundingMode var3) {
      return var1 == 1L ? this : new Duration(FieldUtils.safeDivide(this.getMillis(), var1, var3));
   }

   public long getStandardDays() {
      return this.getMillis() / 86400000L;
   }

   public long getStandardHours() {
      return this.getMillis() / 3600000L;
   }

   public long getStandardMinutes() {
      return this.getMillis() / 60000L;
   }

   public long getStandardSeconds() {
      return this.getMillis() / 1000L;
   }

   public Duration minus(long var1) {
      return this.withDurationAdded(var1, -1);
   }

   public Duration minus(ReadableDuration var1) {
      return var1 == null ? this : this.withDurationAdded(var1.getMillis(), -1);
   }

   public Duration multipliedBy(long var1) {
      return var1 == 1L ? this : new Duration(FieldUtils.safeMultiply(this.getMillis(), var1));
   }

   public Duration negated() {
      if (this.getMillis() != Long.MIN_VALUE) {
         return new Duration(-this.getMillis());
      } else {
         throw new ArithmeticException("Negation of this duration would overflow");
      }
   }

   public Duration plus(long var1) {
      return this.withDurationAdded(var1, 1);
   }

   public Duration plus(ReadableDuration var1) {
      return var1 == null ? this : this.withDurationAdded(var1.getMillis(), 1);
   }

   public Duration toDuration() {
      return this;
   }

   public Days toStandardDays() {
      return Days.days(FieldUtils.safeToInt(this.getStandardDays()));
   }

   public Hours toStandardHours() {
      return Hours.hours(FieldUtils.safeToInt(this.getStandardHours()));
   }

   public Minutes toStandardMinutes() {
      return Minutes.minutes(FieldUtils.safeToInt(this.getStandardMinutes()));
   }

   public Seconds toStandardSeconds() {
      return Seconds.seconds(FieldUtils.safeToInt(this.getStandardSeconds()));
   }

   public Duration withDurationAdded(long var1, int var3) {
      if (var1 != 0L) {
         if (var3 == 0) {
            return this;
         } else {
            var1 = FieldUtils.safeMultiply(var1, var3);
            return new Duration(FieldUtils.safeAdd(this.getMillis(), var1));
         }
      } else {
         return this;
      }
   }

   public Duration withDurationAdded(ReadableDuration var1, int var2) {
      if (var1 != null) {
         return var2 == 0 ? this : this.withDurationAdded(var1.getMillis(), var2);
      } else {
         return this;
      }
   }

   public Duration withMillis(long var1) {
      return var1 == this.getMillis() ? this : new Duration(var1);
   }
}
