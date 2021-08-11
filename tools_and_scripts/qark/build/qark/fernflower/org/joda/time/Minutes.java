package org.joda.time;

import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public final class Minutes extends BaseSingleFieldPeriod {
   public static final Minutes MAX_VALUE = new Minutes(Integer.MAX_VALUE);
   public static final Minutes MIN_VALUE = new Minutes(Integer.MIN_VALUE);
   public static final Minutes ONE = new Minutes(1);
   private static final PeriodFormatter PARSER = ISOPeriodFormat.standard().withParseType(PeriodType.minutes());
   public static final Minutes THREE = new Minutes(3);
   public static final Minutes TWO = new Minutes(2);
   public static final Minutes ZERO = new Minutes(0);
   private static final long serialVersionUID = 87525275727380863L;

   private Minutes(int var1) {
      super(var1);
   }

   public static Minutes minutes(int var0) {
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
               return new Minutes(var0);
            }
         } else {
            return MAX_VALUE;
         }
      } else {
         return MIN_VALUE;
      }
   }

   public static Minutes minutesBetween(ReadableInstant var0, ReadableInstant var1) {
      return minutes(BaseSingleFieldPeriod.between(var0, var1, DurationFieldType.minutes()));
   }

   public static Minutes minutesBetween(ReadablePartial var0, ReadablePartial var1) {
      return var0 instanceof LocalTime && var1 instanceof LocalTime ? minutes(DateTimeUtils.getChronology(var0.getChronology()).minutes().getDifference(((LocalTime)var1).getLocalMillis(), ((LocalTime)var0).getLocalMillis())) : minutes(BaseSingleFieldPeriod.between((ReadablePartial)var0, (ReadablePartial)var1, (ReadablePeriod)ZERO));
   }

   public static Minutes minutesIn(ReadableInterval var0) {
      return var0 == null ? ZERO : minutes(BaseSingleFieldPeriod.between((ReadableInstant)var0.getStart(), (ReadableInstant)var0.getEnd(), (DurationFieldType)DurationFieldType.minutes()));
   }

   @FromString
   public static Minutes parseMinutes(String var0) {
      return var0 == null ? ZERO : minutes(PARSER.parsePeriod(var0).getMinutes());
   }

   private Object readResolve() {
      return minutes(this.getValue());
   }

   public static Minutes standardMinutesIn(ReadablePeriod var0) {
      return minutes(BaseSingleFieldPeriod.standardPeriodIn(var0, 60000L));
   }

   public Minutes dividedBy(int var1) {
      return var1 == 1 ? this : minutes(this.getValue() / var1);
   }

   public DurationFieldType getFieldType() {
      return DurationFieldType.minutes();
   }

   public int getMinutes() {
      return this.getValue();
   }

   public PeriodType getPeriodType() {
      return PeriodType.minutes();
   }

   public boolean isGreaterThan(Minutes var1) {
      if (var1 == null) {
         return this.getValue() > 0;
      } else {
         return this.getValue() > var1.getValue();
      }
   }

   public boolean isLessThan(Minutes var1) {
      if (var1 == null) {
         return this.getValue() < 0;
      } else {
         return this.getValue() < var1.getValue();
      }
   }

   public Minutes minus(int var1) {
      return this.plus(FieldUtils.safeNegate(var1));
   }

   public Minutes minus(Minutes var1) {
      return var1 == null ? this : this.minus(var1.getValue());
   }

   public Minutes multipliedBy(int var1) {
      return minutes(FieldUtils.safeMultiply(this.getValue(), var1));
   }

   public Minutes negated() {
      return minutes(FieldUtils.safeNegate(this.getValue()));
   }

   public Minutes plus(int var1) {
      return var1 == 0 ? this : minutes(FieldUtils.safeAdd(this.getValue(), var1));
   }

   public Minutes plus(Minutes var1) {
      return var1 == null ? this : this.plus(var1.getValue());
   }

   public Days toStandardDays() {
      return Days.days(this.getValue() / 1440);
   }

   public Duration toStandardDuration() {
      return new Duration((long)this.getValue() * 60000L);
   }

   public Hours toStandardHours() {
      return Hours.hours(this.getValue() / 60);
   }

   public Seconds toStandardSeconds() {
      return Seconds.seconds(FieldUtils.safeMultiply(this.getValue(), 60));
   }

   public Weeks toStandardWeeks() {
      return Weeks.weeks(this.getValue() / 10080);
   }

   @ToString
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("PT");
      var1.append(String.valueOf(this.getValue()));
      var1.append("M");
      return var1.toString();
   }
}
