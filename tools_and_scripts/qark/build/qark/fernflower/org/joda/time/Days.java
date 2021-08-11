package org.joda.time;

import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public final class Days extends BaseSingleFieldPeriod {
   public static final Days FIVE = new Days(5);
   public static final Days FOUR = new Days(4);
   public static final Days MAX_VALUE = new Days(Integer.MAX_VALUE);
   public static final Days MIN_VALUE = new Days(Integer.MIN_VALUE);
   public static final Days ONE = new Days(1);
   private static final PeriodFormatter PARSER = ISOPeriodFormat.standard().withParseType(PeriodType.days());
   public static final Days SEVEN = new Days(7);
   public static final Days SIX = new Days(6);
   public static final Days THREE = new Days(3);
   public static final Days TWO = new Days(2);
   public static final Days ZERO = new Days(0);
   private static final long serialVersionUID = 87525275727380865L;

   private Days(int var1) {
      super(var1);
   }

   public static Days days(int var0) {
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
            case 4:
               return FOUR;
            case 5:
               return FIVE;
            case 6:
               return SIX;
            case 7:
               return SEVEN;
            default:
               return new Days(var0);
            }
         } else {
            return MAX_VALUE;
         }
      } else {
         return MIN_VALUE;
      }
   }

   public static Days daysBetween(ReadableInstant var0, ReadableInstant var1) {
      return days(BaseSingleFieldPeriod.between(var0, var1, DurationFieldType.days()));
   }

   public static Days daysBetween(ReadablePartial var0, ReadablePartial var1) {
      return var0 instanceof LocalDate && var1 instanceof LocalDate ? days(DateTimeUtils.getChronology(var0.getChronology()).days().getDifference(((LocalDate)var1).getLocalMillis(), ((LocalDate)var0).getLocalMillis())) : days(BaseSingleFieldPeriod.between((ReadablePartial)var0, (ReadablePartial)var1, (ReadablePeriod)ZERO));
   }

   public static Days daysIn(ReadableInterval var0) {
      return var0 == null ? ZERO : days(BaseSingleFieldPeriod.between((ReadableInstant)var0.getStart(), (ReadableInstant)var0.getEnd(), (DurationFieldType)DurationFieldType.days()));
   }

   @FromString
   public static Days parseDays(String var0) {
      return var0 == null ? ZERO : days(PARSER.parsePeriod(var0).getDays());
   }

   private Object readResolve() {
      return days(this.getValue());
   }

   public static Days standardDaysIn(ReadablePeriod var0) {
      return days(BaseSingleFieldPeriod.standardPeriodIn(var0, 86400000L));
   }

   public Days dividedBy(int var1) {
      return var1 == 1 ? this : days(this.getValue() / var1);
   }

   public int getDays() {
      return this.getValue();
   }

   public DurationFieldType getFieldType() {
      return DurationFieldType.days();
   }

   public PeriodType getPeriodType() {
      return PeriodType.days();
   }

   public boolean isGreaterThan(Days var1) {
      if (var1 == null) {
         return this.getValue() > 0;
      } else {
         return this.getValue() > var1.getValue();
      }
   }

   public boolean isLessThan(Days var1) {
      if (var1 == null) {
         return this.getValue() < 0;
      } else {
         return this.getValue() < var1.getValue();
      }
   }

   public Days minus(int var1) {
      return this.plus(FieldUtils.safeNegate(var1));
   }

   public Days minus(Days var1) {
      return var1 == null ? this : this.minus(var1.getValue());
   }

   public Days multipliedBy(int var1) {
      return days(FieldUtils.safeMultiply(this.getValue(), var1));
   }

   public Days negated() {
      return days(FieldUtils.safeNegate(this.getValue()));
   }

   public Days plus(int var1) {
      return var1 == 0 ? this : days(FieldUtils.safeAdd(this.getValue(), var1));
   }

   public Days plus(Days var1) {
      return var1 == null ? this : this.plus(var1.getValue());
   }

   public Duration toStandardDuration() {
      return new Duration((long)this.getValue() * 86400000L);
   }

   public Hours toStandardHours() {
      return Hours.hours(FieldUtils.safeMultiply(this.getValue(), 24));
   }

   public Minutes toStandardMinutes() {
      return Minutes.minutes(FieldUtils.safeMultiply(this.getValue(), 1440));
   }

   public Seconds toStandardSeconds() {
      return Seconds.seconds(FieldUtils.safeMultiply(this.getValue(), 86400));
   }

   public Weeks toStandardWeeks() {
      return Weeks.weeks(this.getValue() / 7);
   }

   @ToString
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("P");
      var1.append(String.valueOf(this.getValue()));
      var1.append("D");
      return var1.toString();
   }
}
