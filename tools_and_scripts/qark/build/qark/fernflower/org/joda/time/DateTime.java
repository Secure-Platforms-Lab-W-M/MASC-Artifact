package org.joda.time;

import java.io.Serializable;
import org.joda.convert.FromString;
import org.joda.time.DateTime.Property;
import org.joda.time.base.BaseDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public final class DateTime extends BaseDateTime implements ReadableDateTime, Serializable {
   private static final long serialVersionUID = -5171125899451703815L;

   public DateTime() {
   }

   public DateTime(int var1, int var2, int var3, int var4, int var5) {
      super(var1, var2, var3, var4, var5, 0, 0);
   }

   public DateTime(int var1, int var2, int var3, int var4, int var5, int var6) {
      super(var1, var2, var3, var4, var5, var6, 0);
   }

   public DateTime(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   public DateTime(int var1, int var2, int var3, int var4, int var5, int var6, int var7, Chronology var8) {
      super(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public DateTime(int var1, int var2, int var3, int var4, int var5, int var6, int var7, DateTimeZone var8) {
      super(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public DateTime(int var1, int var2, int var3, int var4, int var5, int var6, Chronology var7) {
      super(var1, var2, var3, var4, var5, var6, 0, (Chronology)var7);
   }

   public DateTime(int var1, int var2, int var3, int var4, int var5, int var6, DateTimeZone var7) {
      super(var1, var2, var3, var4, var5, var6, 0, (DateTimeZone)var7);
   }

   public DateTime(int var1, int var2, int var3, int var4, int var5, Chronology var6) {
      super(var1, var2, var3, var4, var5, 0, 0, (Chronology)var6);
   }

   public DateTime(int var1, int var2, int var3, int var4, int var5, DateTimeZone var6) {
      super(var1, var2, var3, var4, var5, 0, 0, (DateTimeZone)var6);
   }

   public DateTime(long var1) {
      super(var1);
   }

   public DateTime(long var1, Chronology var3) {
      super(var1, var3);
   }

   public DateTime(long var1, DateTimeZone var3) {
      super(var1, var3);
   }

   public DateTime(Object var1) {
      super(var1, (Chronology)null);
   }

   public DateTime(Object var1, Chronology var2) {
      super(var1, DateTimeUtils.getChronology(var2));
   }

   public DateTime(Object var1, DateTimeZone var2) {
      super(var1, var2);
   }

   public DateTime(Chronology var1) {
      super(var1);
   }

   public DateTime(DateTimeZone var1) {
      super(var1);
   }

   public static DateTime now() {
      return new DateTime();
   }

   public static DateTime now(Chronology var0) {
      if (var0 != null) {
         return new DateTime(var0);
      } else {
         throw new NullPointerException("Chronology must not be null");
      }
   }

   public static DateTime now(DateTimeZone var0) {
      if (var0 != null) {
         return new DateTime(var0);
      } else {
         throw new NullPointerException("Zone must not be null");
      }
   }

   @FromString
   public static DateTime parse(String var0) {
      return parse(var0, ISODateTimeFormat.dateTimeParser().withOffsetParsed());
   }

   public static DateTime parse(String var0, DateTimeFormatter var1) {
      return var1.parseDateTime(var0);
   }

   public Property centuryOfEra() {
      return new Property(this, this.getChronology().centuryOfEra());
   }

   public Property dayOfMonth() {
      return new Property(this, this.getChronology().dayOfMonth());
   }

   public Property dayOfWeek() {
      return new Property(this, this.getChronology().dayOfWeek());
   }

   public Property dayOfYear() {
      return new Property(this, this.getChronology().dayOfYear());
   }

   public Property era() {
      return new Property(this, this.getChronology().era());
   }

   public Property hourOfDay() {
      return new Property(this, this.getChronology().hourOfDay());
   }

   public Property millisOfDay() {
      return new Property(this, this.getChronology().millisOfDay());
   }

   public Property millisOfSecond() {
      return new Property(this, this.getChronology().millisOfSecond());
   }

   public DateTime minus(long var1) {
      return this.withDurationAdded(var1, -1);
   }

   public DateTime minus(ReadableDuration var1) {
      return this.withDurationAdded(var1, -1);
   }

   public DateTime minus(ReadablePeriod var1) {
      return this.withPeriodAdded(var1, -1);
   }

   public DateTime minusDays(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().days().subtract(this.getMillis(), var1));
   }

   public DateTime minusHours(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().hours().subtract(this.getMillis(), var1));
   }

   public DateTime minusMillis(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().millis().subtract(this.getMillis(), var1));
   }

   public DateTime minusMinutes(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().minutes().subtract(this.getMillis(), var1));
   }

   public DateTime minusMonths(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().months().subtract(this.getMillis(), var1));
   }

   public DateTime minusSeconds(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().seconds().subtract(this.getMillis(), var1));
   }

   public DateTime minusWeeks(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().weeks().subtract(this.getMillis(), var1));
   }

   public DateTime minusYears(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().years().subtract(this.getMillis(), var1));
   }

   public Property minuteOfDay() {
      return new Property(this, this.getChronology().minuteOfDay());
   }

   public Property minuteOfHour() {
      return new Property(this, this.getChronology().minuteOfHour());
   }

   public Property monthOfYear() {
      return new Property(this, this.getChronology().monthOfYear());
   }

   public DateTime plus(long var1) {
      return this.withDurationAdded(var1, 1);
   }

   public DateTime plus(ReadableDuration var1) {
      return this.withDurationAdded(var1, 1);
   }

   public DateTime plus(ReadablePeriod var1) {
      return this.withPeriodAdded(var1, 1);
   }

   public DateTime plusDays(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().days().add(this.getMillis(), var1));
   }

   public DateTime plusHours(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().hours().add(this.getMillis(), var1));
   }

   public DateTime plusMillis(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().millis().add(this.getMillis(), var1));
   }

   public DateTime plusMinutes(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().minutes().add(this.getMillis(), var1));
   }

   public DateTime plusMonths(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().months().add(this.getMillis(), var1));
   }

   public DateTime plusSeconds(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().seconds().add(this.getMillis(), var1));
   }

   public DateTime plusWeeks(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().weeks().add(this.getMillis(), var1));
   }

   public DateTime plusYears(int var1) {
      return var1 == 0 ? this : this.withMillis(this.getChronology().years().add(this.getMillis(), var1));
   }

   public Property property(DateTimeFieldType var1) {
      if (var1 != null) {
         DateTimeField var2 = var1.getField(this.getChronology());
         if (var2.isSupported()) {
            return new Property(this, var2);
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("Field '");
            var3.append(var1);
            var3.append("' is not supported");
            throw new IllegalArgumentException(var3.toString());
         }
      } else {
         throw new IllegalArgumentException("The DateTimeFieldType must not be null");
      }
   }

   public Property secondOfDay() {
      return new Property(this, this.getChronology().secondOfDay());
   }

   public Property secondOfMinute() {
      return new Property(this, this.getChronology().secondOfMinute());
   }

   @Deprecated
   public DateMidnight toDateMidnight() {
      return new DateMidnight(this.getMillis(), this.getChronology());
   }

   public DateTime toDateTime() {
      return this;
   }

   public DateTime toDateTime(Chronology var1) {
      var1 = DateTimeUtils.getChronology(var1);
      return this.getChronology() == var1 ? this : super.toDateTime(var1);
   }

   public DateTime toDateTime(DateTimeZone var1) {
      var1 = DateTimeUtils.getZone(var1);
      return this.getZone() == var1 ? this : super.toDateTime(var1);
   }

   public DateTime toDateTimeISO() {
      return this.getChronology() == ISOChronology.getInstance() ? this : super.toDateTimeISO();
   }

   public LocalDate toLocalDate() {
      return new LocalDate(this.getMillis(), this.getChronology());
   }

   public LocalDateTime toLocalDateTime() {
      return new LocalDateTime(this.getMillis(), this.getChronology());
   }

   public LocalTime toLocalTime() {
      return new LocalTime(this.getMillis(), this.getChronology());
   }

   @Deprecated
   public TimeOfDay toTimeOfDay() {
      return new TimeOfDay(this.getMillis(), this.getChronology());
   }

   @Deprecated
   public YearMonthDay toYearMonthDay() {
      return new YearMonthDay(this.getMillis(), this.getChronology());
   }

   public Property weekOfWeekyear() {
      return new Property(this, this.getChronology().weekOfWeekyear());
   }

   public Property weekyear() {
      return new Property(this, this.getChronology().weekyear());
   }

   public DateTime withCenturyOfEra(int var1) {
      return this.withMillis(this.getChronology().centuryOfEra().set(this.getMillis(), var1));
   }

   public DateTime withChronology(Chronology var1) {
      var1 = DateTimeUtils.getChronology(var1);
      return var1 == this.getChronology() ? this : new DateTime(this.getMillis(), var1);
   }

   public DateTime withDate(int var1, int var2, int var3) {
      Chronology var6 = this.getChronology();
      long var4 = var6.withUTC().getDateTimeMillis(var1, var2, var3, this.getMillisOfDay());
      return this.withMillis(var6.getZone().convertLocalToUTC(var4, false, this.getMillis()));
   }

   public DateTime withDate(LocalDate var1) {
      return this.withDate(var1.getYear(), var1.getMonthOfYear(), var1.getDayOfMonth());
   }

   public DateTime withDayOfMonth(int var1) {
      return this.withMillis(this.getChronology().dayOfMonth().set(this.getMillis(), var1));
   }

   public DateTime withDayOfWeek(int var1) {
      return this.withMillis(this.getChronology().dayOfWeek().set(this.getMillis(), var1));
   }

   public DateTime withDayOfYear(int var1) {
      return this.withMillis(this.getChronology().dayOfYear().set(this.getMillis(), var1));
   }

   public DateTime withDurationAdded(long var1, int var3) {
      if (var1 != 0L) {
         return var3 == 0 ? this : this.withMillis(this.getChronology().add(this.getMillis(), var1, var3));
      } else {
         return this;
      }
   }

   public DateTime withDurationAdded(ReadableDuration var1, int var2) {
      if (var1 != null) {
         return var2 == 0 ? this : this.withDurationAdded(var1.getMillis(), var2);
      } else {
         return this;
      }
   }

   public DateTime withEarlierOffsetAtOverlap() {
      return this.withMillis(this.getZone().adjustOffset(this.getMillis(), false));
   }

   public DateTime withEra(int var1) {
      return this.withMillis(this.getChronology().era().set(this.getMillis(), var1));
   }

   public DateTime withField(DateTimeFieldType var1, int var2) {
      if (var1 != null) {
         return this.withMillis(var1.getField(this.getChronology()).set(this.getMillis(), var2));
      } else {
         throw new IllegalArgumentException("Field must not be null");
      }
   }

   public DateTime withFieldAdded(DurationFieldType var1, int var2) {
      if (var1 != null) {
         return var2 == 0 ? this : this.withMillis(var1.getField(this.getChronology()).add(this.getMillis(), var2));
      } else {
         throw new IllegalArgumentException("Field must not be null");
      }
   }

   public DateTime withFields(ReadablePartial var1) {
      return var1 == null ? this : this.withMillis(this.getChronology().set(var1, this.getMillis()));
   }

   public DateTime withHourOfDay(int var1) {
      return this.withMillis(this.getChronology().hourOfDay().set(this.getMillis(), var1));
   }

   public DateTime withLaterOffsetAtOverlap() {
      return this.withMillis(this.getZone().adjustOffset(this.getMillis(), true));
   }

   public DateTime withMillis(long var1) {
      return var1 == this.getMillis() ? this : new DateTime(var1, this.getChronology());
   }

   public DateTime withMillisOfDay(int var1) {
      return this.withMillis(this.getChronology().millisOfDay().set(this.getMillis(), var1));
   }

   public DateTime withMillisOfSecond(int var1) {
      return this.withMillis(this.getChronology().millisOfSecond().set(this.getMillis(), var1));
   }

   public DateTime withMinuteOfHour(int var1) {
      return this.withMillis(this.getChronology().minuteOfHour().set(this.getMillis(), var1));
   }

   public DateTime withMonthOfYear(int var1) {
      return this.withMillis(this.getChronology().monthOfYear().set(this.getMillis(), var1));
   }

   public DateTime withPeriodAdded(ReadablePeriod var1, int var2) {
      if (var1 != null) {
         return var2 == 0 ? this : this.withMillis(this.getChronology().add(var1, this.getMillis(), var2));
      } else {
         return this;
      }
   }

   public DateTime withSecondOfMinute(int var1) {
      return this.withMillis(this.getChronology().secondOfMinute().set(this.getMillis(), var1));
   }

   public DateTime withTime(int var1, int var2, int var3, int var4) {
      Chronology var7 = this.getChronology();
      long var5 = var7.withUTC().getDateTimeMillis(this.getYear(), this.getMonthOfYear(), this.getDayOfMonth(), var1, var2, var3, var4);
      return this.withMillis(var7.getZone().convertLocalToUTC(var5, false, this.getMillis()));
   }

   public DateTime withTime(LocalTime var1) {
      return this.withTime(var1.getHourOfDay(), var1.getMinuteOfHour(), var1.getSecondOfMinute(), var1.getMillisOfSecond());
   }

   public DateTime withTimeAtStartOfDay() {
      return this.toLocalDate().toDateTimeAtStartOfDay(this.getZone());
   }

   public DateTime withWeekOfWeekyear(int var1) {
      return this.withMillis(this.getChronology().weekOfWeekyear().set(this.getMillis(), var1));
   }

   public DateTime withWeekyear(int var1) {
      return this.withMillis(this.getChronology().weekyear().set(this.getMillis(), var1));
   }

   public DateTime withYear(int var1) {
      return this.withMillis(this.getChronology().year().set(this.getMillis(), var1));
   }

   public DateTime withYearOfCentury(int var1) {
      return this.withMillis(this.getChronology().yearOfCentury().set(this.getMillis(), var1));
   }

   public DateTime withYearOfEra(int var1) {
      return this.withMillis(this.getChronology().yearOfEra().set(this.getMillis(), var1));
   }

   public DateTime withZone(DateTimeZone var1) {
      return this.withChronology(this.getChronology().withZone(var1));
   }

   public DateTime withZoneRetainFields(DateTimeZone var1) {
      var1 = DateTimeUtils.getZone(var1);
      DateTimeZone var2 = DateTimeUtils.getZone(this.getZone());
      return var1 == var2 ? this : new DateTime(var2.getMillisKeepLocal(var1, this.getMillis()), this.getChronology().withZone(var1));
   }

   public Property year() {
      return new Property(this, this.getChronology().year());
   }

   public Property yearOfCentury() {
      return new Property(this, this.getChronology().yearOfCentury());
   }

   public Property yearOfEra() {
      return new Property(this, this.getChronology().yearOfEra());
   }
}
