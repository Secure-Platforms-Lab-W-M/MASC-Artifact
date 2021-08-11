package org.joda.time;

import java.io.Serializable;
import org.joda.convert.FromString;
import org.joda.time.base.BasePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public final class Period extends BasePeriod implements ReadablePeriod, Serializable {
   public static final Period ZERO = new Period();
   private static final long serialVersionUID = 741052353876488155L;

   public Period() {
      super(0L, (PeriodType)null, (Chronology)null);
   }

   public Period(int var1, int var2, int var3, int var4) {
      super(0, 0, 0, 0, var1, var2, var3, var4, PeriodType.standard());
   }

   public Period(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      super(var1, var2, var3, var4, var5, var6, var7, var8, PeriodType.standard());
   }

   public Period(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, PeriodType var9) {
      super(var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public Period(long var1) {
      super(var1);
   }

   public Period(long var1, long var3) {
      super(var1, var3, (PeriodType)null, (Chronology)null);
   }

   public Period(long var1, long var3, Chronology var5) {
      super(var1, var3, (PeriodType)null, var5);
   }

   public Period(long var1, long var3, PeriodType var5) {
      super(var1, var3, var5, (Chronology)null);
   }

   public Period(long var1, long var3, PeriodType var5, Chronology var6) {
      super(var1, var3, var5, var6);
   }

   public Period(long var1, Chronology var3) {
      super(var1, (PeriodType)null, var3);
   }

   public Period(long var1, PeriodType var3) {
      super(var1, var3, (Chronology)null);
   }

   public Period(long var1, PeriodType var3, Chronology var4) {
      super(var1, var3, var4);
   }

   public Period(Object var1) {
      super((Object)var1, (PeriodType)null, (Chronology)null);
   }

   public Period(Object var1, Chronology var2) {
      super((Object)var1, (PeriodType)null, (Chronology)var2);
   }

   public Period(Object var1, PeriodType var2) {
      super((Object)var1, (PeriodType)var2, (Chronology)null);
   }

   public Period(Object var1, PeriodType var2, Chronology var3) {
      super(var1, var2, var3);
   }

   public Period(ReadableDuration var1, ReadableInstant var2) {
      super((ReadableDuration)var1, (ReadableInstant)var2, (PeriodType)null);
   }

   public Period(ReadableDuration var1, ReadableInstant var2, PeriodType var3) {
      super(var1, var2, var3);
   }

   public Period(ReadableInstant var1, ReadableDuration var2) {
      super((ReadableInstant)var1, (ReadableDuration)var2, (PeriodType)null);
   }

   public Period(ReadableInstant var1, ReadableDuration var2, PeriodType var3) {
      super(var1, var2, var3);
   }

   public Period(ReadableInstant var1, ReadableInstant var2) {
      super((ReadableInstant)var1, (ReadableInstant)var2, (PeriodType)null);
   }

   public Period(ReadableInstant var1, ReadableInstant var2, PeriodType var3) {
      super(var1, var2, var3);
   }

   public Period(ReadablePartial var1, ReadablePartial var2) {
      super((ReadablePartial)var1, (ReadablePartial)var2, (PeriodType)null);
   }

   public Period(ReadablePartial var1, ReadablePartial var2, PeriodType var3) {
      super(var1, var2, var3);
   }

   private Period(int[] var1, PeriodType var2) {
      super(var1, var2);
   }

   private void checkYearsAndMonths(String var1) {
      StringBuilder var2;
      if (this.getMonths() == 0) {
         if (this.getYears() != 0) {
            var2 = new StringBuilder();
            var2.append("Cannot convert to ");
            var2.append(var1);
            var2.append(" as this period contains years and years vary in length");
            throw new UnsupportedOperationException(var2.toString());
         }
      } else {
         var2 = new StringBuilder();
         var2.append("Cannot convert to ");
         var2.append(var1);
         var2.append(" as this period contains months and months vary in length");
         throw new UnsupportedOperationException(var2.toString());
      }
   }

   public static Period days(int var0) {
      PeriodType var1 = PeriodType.standard();
      return new Period(new int[]{0, 0, 0, var0, 0, 0, 0, 0}, var1);
   }

   public static Period fieldDifference(ReadablePartial var0, ReadablePartial var1) {
      if (var0 != null && var1 != null) {
         if (var0.size() == var1.size()) {
            DurationFieldType[] var4 = new DurationFieldType[var0.size()];
            int[] var5 = new int[var0.size()];
            int var2 = 0;

            for(int var3 = var0.size(); var2 < var3; ++var2) {
               if (var0.getFieldType(var2) != var1.getFieldType(var2)) {
                  throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
               }

               var4[var2] = var0.getFieldType(var2).getDurationType();
               if (var2 > 0 && var4[var2 - 1] == var4[var2]) {
                  throw new IllegalArgumentException("ReadablePartial objects must not have overlapping fields");
               }

               var5[var2] = var1.getValue(var2) - var0.getValue(var2);
            }

            return new Period(var5, PeriodType.forFields(var4));
         } else {
            throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
         }
      } else {
         throw new IllegalArgumentException("ReadablePartial objects must not be null");
      }
   }

   public static Period hours(int var0) {
      PeriodType var1 = PeriodType.standard();
      return new Period(new int[]{0, 0, 0, 0, var0, 0, 0, 0}, var1);
   }

   public static Period millis(int var0) {
      PeriodType var1 = PeriodType.standard();
      return new Period(new int[]{0, 0, 0, 0, 0, 0, 0, var0}, var1);
   }

   public static Period minutes(int var0) {
      PeriodType var1 = PeriodType.standard();
      return new Period(new int[]{0, 0, 0, 0, 0, var0, 0, 0}, var1);
   }

   public static Period months(int var0) {
      PeriodType var1 = PeriodType.standard();
      return new Period(new int[]{0, var0, 0, 0, 0, 0, 0, 0}, var1);
   }

   @FromString
   public static Period parse(String var0) {
      return parse(var0, ISOPeriodFormat.standard());
   }

   public static Period parse(String var0, PeriodFormatter var1) {
      return var1.parsePeriod(var0);
   }

   public static Period seconds(int var0) {
      PeriodType var1 = PeriodType.standard();
      return new Period(new int[]{0, 0, 0, 0, 0, 0, var0, 0}, var1);
   }

   public static Period weeks(int var0) {
      PeriodType var1 = PeriodType.standard();
      return new Period(new int[]{0, 0, var0, 0, 0, 0, 0, 0}, var1);
   }

   public static Period years(int var0) {
      PeriodType var1 = PeriodType.standard();
      return new Period(new int[]{var0, 0, 0, 0, 0, 0, 0, 0, 0}, var1);
   }

   public int getDays() {
      return this.getPeriodType().getIndexedField(this, PeriodType.DAY_INDEX);
   }

   public int getHours() {
      return this.getPeriodType().getIndexedField(this, PeriodType.HOUR_INDEX);
   }

   public int getMillis() {
      return this.getPeriodType().getIndexedField(this, PeriodType.MILLI_INDEX);
   }

   public int getMinutes() {
      return this.getPeriodType().getIndexedField(this, PeriodType.MINUTE_INDEX);
   }

   public int getMonths() {
      return this.getPeriodType().getIndexedField(this, PeriodType.MONTH_INDEX);
   }

   public int getSeconds() {
      return this.getPeriodType().getIndexedField(this, PeriodType.SECOND_INDEX);
   }

   public int getWeeks() {
      return this.getPeriodType().getIndexedField(this, PeriodType.WEEK_INDEX);
   }

   public int getYears() {
      return this.getPeriodType().getIndexedField(this, PeriodType.YEAR_INDEX);
   }

   public Period minus(ReadablePeriod var1) {
      if (var1 == null) {
         return this;
      } else {
         int[] var2 = this.getValues();
         this.getPeriodType().addIndexedField(this, PeriodType.YEAR_INDEX, var2, -var1.get(DurationFieldType.YEARS_TYPE));
         this.getPeriodType().addIndexedField(this, PeriodType.MONTH_INDEX, var2, -var1.get(DurationFieldType.MONTHS_TYPE));
         this.getPeriodType().addIndexedField(this, PeriodType.WEEK_INDEX, var2, -var1.get(DurationFieldType.WEEKS_TYPE));
         this.getPeriodType().addIndexedField(this, PeriodType.DAY_INDEX, var2, -var1.get(DurationFieldType.DAYS_TYPE));
         this.getPeriodType().addIndexedField(this, PeriodType.HOUR_INDEX, var2, -var1.get(DurationFieldType.HOURS_TYPE));
         this.getPeriodType().addIndexedField(this, PeriodType.MINUTE_INDEX, var2, -var1.get(DurationFieldType.MINUTES_TYPE));
         this.getPeriodType().addIndexedField(this, PeriodType.SECOND_INDEX, var2, -var1.get(DurationFieldType.SECONDS_TYPE));
         this.getPeriodType().addIndexedField(this, PeriodType.MILLI_INDEX, var2, -var1.get(DurationFieldType.MILLIS_TYPE));
         return new Period(var2, this.getPeriodType());
      }
   }

   public Period minusDays(int var1) {
      return this.plusDays(-var1);
   }

   public Period minusHours(int var1) {
      return this.plusHours(-var1);
   }

   public Period minusMillis(int var1) {
      return this.plusMillis(-var1);
   }

   public Period minusMinutes(int var1) {
      return this.plusMinutes(-var1);
   }

   public Period minusMonths(int var1) {
      return this.plusMonths(-var1);
   }

   public Period minusSeconds(int var1) {
      return this.plusSeconds(-var1);
   }

   public Period minusWeeks(int var1) {
      return this.plusWeeks(-var1);
   }

   public Period minusYears(int var1) {
      return this.plusYears(-var1);
   }

   public Period multipliedBy(int var1) {
      if (this == ZERO) {
         return this;
      } else if (var1 == 1) {
         return this;
      } else {
         int[] var3 = this.getValues();

         for(int var2 = 0; var2 < var3.length; ++var2) {
            var3[var2] = FieldUtils.safeMultiply(var3[var2], var1);
         }

         return new Period(var3, this.getPeriodType());
      }
   }

   public Period negated() {
      return this.multipliedBy(-1);
   }

   public Period normalizedStandard() {
      return this.normalizedStandard(PeriodType.standard());
   }

   public Period normalizedStandard(PeriodType var1) {
      PeriodType var6 = DateTimeUtils.getPeriodType(var1);
      Period var7 = new Period((long)this.getMillis() + (long)this.getSeconds() * 1000L + (long)this.getMinutes() * 60000L + (long)this.getHours() * 3600000L + (long)this.getDays() * 86400000L + (long)this.getWeeks() * 604800000L, var6, ISOChronology.getInstanceUTC());
      int var2 = this.getYears();
      int var3 = this.getMonths();
      if (var2 == 0 && var3 == 0) {
         return var7;
      } else {
         long var4 = (long)var2 * 12L + (long)var3;
         if (var6.isSupported(DurationFieldType.YEARS_TYPE)) {
            var2 = FieldUtils.safeToInt(var4 / 12L);
            var7 = var7.withYears(var2);
            var4 -= (long)(var2 * 12);
         }

         if (var6.isSupported(DurationFieldType.MONTHS_TYPE)) {
            var2 = FieldUtils.safeToInt(var4);
            var7 = var7.withMonths(var2);
            var4 -= (long)var2;
         }

         if (var4 == 0L) {
            return var7;
         } else {
            StringBuilder var8 = new StringBuilder();
            var8.append("Unable to normalize as PeriodType is missing either years or months but period has a month/year amount: ");
            var8.append(this.toString());
            throw new UnsupportedOperationException(var8.toString());
         }
      }
   }

   public Period plus(ReadablePeriod var1) {
      if (var1 == null) {
         return this;
      } else {
         int[] var2 = this.getValues();
         this.getPeriodType().addIndexedField(this, PeriodType.YEAR_INDEX, var2, var1.get(DurationFieldType.YEARS_TYPE));
         this.getPeriodType().addIndexedField(this, PeriodType.MONTH_INDEX, var2, var1.get(DurationFieldType.MONTHS_TYPE));
         this.getPeriodType().addIndexedField(this, PeriodType.WEEK_INDEX, var2, var1.get(DurationFieldType.WEEKS_TYPE));
         this.getPeriodType().addIndexedField(this, PeriodType.DAY_INDEX, var2, var1.get(DurationFieldType.DAYS_TYPE));
         this.getPeriodType().addIndexedField(this, PeriodType.HOUR_INDEX, var2, var1.get(DurationFieldType.HOURS_TYPE));
         this.getPeriodType().addIndexedField(this, PeriodType.MINUTE_INDEX, var2, var1.get(DurationFieldType.MINUTES_TYPE));
         this.getPeriodType().addIndexedField(this, PeriodType.SECOND_INDEX, var2, var1.get(DurationFieldType.SECONDS_TYPE));
         this.getPeriodType().addIndexedField(this, PeriodType.MILLI_INDEX, var2, var1.get(DurationFieldType.MILLIS_TYPE));
         return new Period(var2, this.getPeriodType());
      }
   }

   public Period plusDays(int var1) {
      if (var1 == 0) {
         return this;
      } else {
         int[] var2 = this.getValues();
         this.getPeriodType().addIndexedField(this, PeriodType.DAY_INDEX, var2, var1);
         return new Period(var2, this.getPeriodType());
      }
   }

   public Period plusHours(int var1) {
      if (var1 == 0) {
         return this;
      } else {
         int[] var2 = this.getValues();
         this.getPeriodType().addIndexedField(this, PeriodType.HOUR_INDEX, var2, var1);
         return new Period(var2, this.getPeriodType());
      }
   }

   public Period plusMillis(int var1) {
      if (var1 == 0) {
         return this;
      } else {
         int[] var2 = this.getValues();
         this.getPeriodType().addIndexedField(this, PeriodType.MILLI_INDEX, var2, var1);
         return new Period(var2, this.getPeriodType());
      }
   }

   public Period plusMinutes(int var1) {
      if (var1 == 0) {
         return this;
      } else {
         int[] var2 = this.getValues();
         this.getPeriodType().addIndexedField(this, PeriodType.MINUTE_INDEX, var2, var1);
         return new Period(var2, this.getPeriodType());
      }
   }

   public Period plusMonths(int var1) {
      if (var1 == 0) {
         return this;
      } else {
         int[] var2 = this.getValues();
         this.getPeriodType().addIndexedField(this, PeriodType.MONTH_INDEX, var2, var1);
         return new Period(var2, this.getPeriodType());
      }
   }

   public Period plusSeconds(int var1) {
      if (var1 == 0) {
         return this;
      } else {
         int[] var2 = this.getValues();
         this.getPeriodType().addIndexedField(this, PeriodType.SECOND_INDEX, var2, var1);
         return new Period(var2, this.getPeriodType());
      }
   }

   public Period plusWeeks(int var1) {
      if (var1 == 0) {
         return this;
      } else {
         int[] var2 = this.getValues();
         this.getPeriodType().addIndexedField(this, PeriodType.WEEK_INDEX, var2, var1);
         return new Period(var2, this.getPeriodType());
      }
   }

   public Period plusYears(int var1) {
      if (var1 == 0) {
         return this;
      } else {
         int[] var2 = this.getValues();
         this.getPeriodType().addIndexedField(this, PeriodType.YEAR_INDEX, var2, var1);
         return new Period(var2, this.getPeriodType());
      }
   }

   public Period toPeriod() {
      return this;
   }

   public Days toStandardDays() {
      this.checkYearsAndMonths("Days");
      return Days.days(FieldUtils.safeToInt(FieldUtils.safeAdd(FieldUtils.safeAdd(((long)this.getMillis() + (long)this.getSeconds() * 1000L + (long)this.getMinutes() * 60000L + (long)this.getHours() * 3600000L) / 86400000L, (long)this.getDays()), (long)this.getWeeks() * 7L)));
   }

   public Duration toStandardDuration() {
      this.checkYearsAndMonths("Duration");
      return new Duration((long)this.getMillis() + (long)this.getSeconds() * 1000L + (long)this.getMinutes() * 60000L + (long)this.getHours() * 3600000L + (long)this.getDays() * 86400000L + (long)this.getWeeks() * 604800000L);
   }

   public Hours toStandardHours() {
      this.checkYearsAndMonths("Hours");
      return Hours.hours(FieldUtils.safeToInt(FieldUtils.safeAdd(FieldUtils.safeAdd(FieldUtils.safeAdd(((long)this.getMillis() + (long)this.getSeconds() * 1000L + (long)this.getMinutes() * 60000L) / 3600000L, (long)this.getHours()), (long)this.getDays() * 24L), (long)this.getWeeks() * 168L)));
   }

   public Minutes toStandardMinutes() {
      this.checkYearsAndMonths("Minutes");
      return Minutes.minutes(FieldUtils.safeToInt(FieldUtils.safeAdd(FieldUtils.safeAdd(FieldUtils.safeAdd(FieldUtils.safeAdd(((long)this.getMillis() + (long)this.getSeconds() * 1000L) / 60000L, (long)this.getMinutes()), (long)this.getHours() * 60L), (long)this.getDays() * 1440L), (long)this.getWeeks() * 10080L)));
   }

   public Seconds toStandardSeconds() {
      this.checkYearsAndMonths("Seconds");
      return Seconds.seconds(FieldUtils.safeToInt(FieldUtils.safeAdd(FieldUtils.safeAdd(FieldUtils.safeAdd(FieldUtils.safeAdd(FieldUtils.safeAdd((long)(this.getMillis() / 1000), (long)this.getSeconds()), (long)this.getMinutes() * 60L), (long)this.getHours() * 3600L), (long)this.getDays() * 86400L), (long)this.getWeeks() * 604800L)));
   }

   public Weeks toStandardWeeks() {
      this.checkYearsAndMonths("Weeks");
      long var1 = (long)this.getMillis();
      long var3 = (long)this.getSeconds();
      long var5 = (long)this.getMinutes();
      long var7 = (long)this.getHours();
      long var9 = (long)this.getDays();
      return Weeks.weeks(FieldUtils.safeToInt((long)this.getWeeks() + (var1 + var3 * 1000L + var5 * 60000L + var7 * 3600000L + var9 * 86400000L) / 604800000L));
   }

   public Period withDays(int var1) {
      int[] var2 = this.getValues();
      this.getPeriodType().setIndexedField(this, PeriodType.DAY_INDEX, var2, var1);
      return new Period(var2, this.getPeriodType());
   }

   public Period withField(DurationFieldType var1, int var2) {
      if (var1 != null) {
         int[] var3 = this.getValues();
         super.setFieldInto(var3, var1, var2);
         return new Period(var3, this.getPeriodType());
      } else {
         throw new IllegalArgumentException("Field must not be null");
      }
   }

   public Period withFieldAdded(DurationFieldType var1, int var2) {
      if (var1 != null) {
         if (var2 == 0) {
            return this;
         } else {
            int[] var3 = this.getValues();
            super.addFieldInto(var3, var1, var2);
            return new Period(var3, this.getPeriodType());
         }
      } else {
         throw new IllegalArgumentException("Field must not be null");
      }
   }

   public Period withFields(ReadablePeriod var1) {
      return var1 == null ? this : new Period(super.mergePeriodInto(this.getValues(), var1), this.getPeriodType());
   }

   public Period withHours(int var1) {
      int[] var2 = this.getValues();
      this.getPeriodType().setIndexedField(this, PeriodType.HOUR_INDEX, var2, var1);
      return new Period(var2, this.getPeriodType());
   }

   public Period withMillis(int var1) {
      int[] var2 = this.getValues();
      this.getPeriodType().setIndexedField(this, PeriodType.MILLI_INDEX, var2, var1);
      return new Period(var2, this.getPeriodType());
   }

   public Period withMinutes(int var1) {
      int[] var2 = this.getValues();
      this.getPeriodType().setIndexedField(this, PeriodType.MINUTE_INDEX, var2, var1);
      return new Period(var2, this.getPeriodType());
   }

   public Period withMonths(int var1) {
      int[] var2 = this.getValues();
      this.getPeriodType().setIndexedField(this, PeriodType.MONTH_INDEX, var2, var1);
      return new Period(var2, this.getPeriodType());
   }

   public Period withPeriodType(PeriodType var1) {
      var1 = DateTimeUtils.getPeriodType(var1);
      return var1.equals(this.getPeriodType()) ? this : new Period(this, var1);
   }

   public Period withSeconds(int var1) {
      int[] var2 = this.getValues();
      this.getPeriodType().setIndexedField(this, PeriodType.SECOND_INDEX, var2, var1);
      return new Period(var2, this.getPeriodType());
   }

   public Period withWeeks(int var1) {
      int[] var2 = this.getValues();
      this.getPeriodType().setIndexedField(this, PeriodType.WEEK_INDEX, var2, var1);
      return new Period(var2, this.getPeriodType());
   }

   public Period withYears(int var1) {
      int[] var2 = this.getValues();
      this.getPeriodType().setIndexedField(this, PeriodType.YEAR_INDEX, var2, var1);
      return new Period(var2, this.getPeriodType());
   }
}
