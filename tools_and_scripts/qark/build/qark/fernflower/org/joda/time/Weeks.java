package org.joda.time;

import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public final class Weeks extends BaseSingleFieldPeriod {
   public static final Weeks MAX_VALUE = new Weeks(Integer.MAX_VALUE);
   public static final Weeks MIN_VALUE = new Weeks(Integer.MIN_VALUE);
   public static final Weeks ONE = new Weeks(1);
   private static final PeriodFormatter PARSER = ISOPeriodFormat.standard().withParseType(PeriodType.weeks());
   public static final Weeks THREE = new Weeks(3);
   public static final Weeks TWO = new Weeks(2);
   public static final Weeks ZERO = new Weeks(0);
   private static final long serialVersionUID = 87525275727380866L;

   private Weeks(int var1) {
      super(var1);
   }

   @FromString
   public static Weeks parseWeeks(String var0) {
      return var0 == null ? ZERO : weeks(PARSER.parsePeriod(var0).getWeeks());
   }

   private Object readResolve() {
      return weeks(this.getValue());
   }

   public static Weeks standardWeeksIn(ReadablePeriod var0) {
      return weeks(BaseSingleFieldPeriod.standardPeriodIn(var0, 604800000L));
   }

   public static Weeks weeks(int var0) {
      if (var0 != Integer.MIN_VALUE) {
         if (var0 != Integer.MAX_VALUE) {
            switch(var0) {
            case 0:
               return ZERO;
            case 1:
               return ONE;
            case 2:
               return TWO;
            case 3:
               return THREE;
            default:
               return new Weeks(var0);
            }
         } else {
            return MAX_VALUE;
         }
      } else {
         return MIN_VALUE;
      }
   }

   public static Weeks weeksBetween(ReadableInstant var0, ReadableInstant var1) {
      return weeks(BaseSingleFieldPeriod.between(var0, var1, DurationFieldType.weeks()));
   }

   public static Weeks weeksBetween(ReadablePartial var0, ReadablePartial var1) {
      return var0 instanceof LocalDate && var1 instanceof LocalDate ? weeks(DateTimeUtils.getChronology(var0.getChronology()).weeks().getDifference(((LocalDate)var1).getLocalMillis(), ((LocalDate)var0).getLocalMillis())) : weeks(BaseSingleFieldPeriod.between((ReadablePartial)var0, (ReadablePartial)var1, (ReadablePeriod)ZERO));
   }

   public static Weeks weeksIn(ReadableInterval var0) {
      return var0 == null ? ZERO : weeks(BaseSingleFieldPeriod.between((ReadableInstant)var0.getStart(), (ReadableInstant)var0.getEnd(), (DurationFieldType)DurationFieldType.weeks()));
   }

   public Weeks dividedBy(int var1) {
      return var1 == 1 ? this : weeks(this.getValue() / var1);
   }

   public DurationFieldType getFieldType() {
      return DurationFieldType.weeks();
   }

   public PeriodType getPeriodType() {
      return PeriodType.weeks();
   }

   public int getWeeks() {
      return this.getValue();
   }

   public boolean isGreaterThan(Weeks var1) {
      if (var1 == null) {
         return this.getValue() > 0;
      } else {
         return this.getValue() > var1.getValue();
      }
   }

   public boolean isLessThan(Weeks var1) {
      if (var1 == null) {
         return this.getValue() < 0;
      } else {
         return this.getValue() < var1.getValue();
      }
   }

   public Weeks minus(int var1) {
      return this.plus(FieldUtils.safeNegate(var1));
   }

   public Weeks minus(Weeks var1) {
      return var1 == null ? this : this.minus(var1.getValue());
   }

   public Weeks multipliedBy(int var1) {
      return weeks(FieldUtils.safeMultiply(this.getValue(), var1));
   }

   public Weeks negated() {
      return weeks(FieldUtils.safeNegate(this.getValue()));
   }

   public Weeks plus(int var1) {
      return var1 == 0 ? this : weeks(FieldUtils.safeAdd(this.getValue(), var1));
   }

   public Weeks plus(Weeks var1) {
      return var1 == null ? this : this.plus(var1.getValue());
   }

   public Days toStandardDays() {
      return Days.days(FieldUtils.safeMultiply(this.getValue(), 7));
   }

   public Duration toStandardDuration() {
      return new Duration((long)this.getValue() * 604800000L);
   }

   public Hours toStandardHours() {
      return Hours.hours(FieldUtils.safeMultiply(this.getValue(), 168));
   }

   public Minutes toStandardMinutes() {
      return Minutes.minutes(FieldUtils.safeMultiply(this.getValue(), 10080));
   }

   public Seconds toStandardSeconds() {
      return Seconds.seconds(FieldUtils.safeMultiply(this.getValue(), 604800));
   }

   @ToString
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("P");
      var1.append(String.valueOf(this.getValue()));
      var1.append("W");
      return var1.toString();
   }
}
