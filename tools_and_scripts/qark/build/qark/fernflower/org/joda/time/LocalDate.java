package org.joda.time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.LocalDate.Property;
import org.joda.time.base.BaseLocal;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PartialConverter;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public final class LocalDate extends BaseLocal implements ReadablePartial, Serializable {
   private static final Set DATE_DURATION_TYPES = new HashSet();
   private static final int DAY_OF_MONTH = 2;
   private static final int MONTH_OF_YEAR = 1;
   private static final int YEAR = 0;
   private static final long serialVersionUID = -8775358157899L;
   private final Chronology iChronology;
   private transient int iHash;
   private final long iLocalMillis;

   static {
      DATE_DURATION_TYPES.add(DurationFieldType.days());
      DATE_DURATION_TYPES.add(DurationFieldType.weeks());
      DATE_DURATION_TYPES.add(DurationFieldType.months());
      DATE_DURATION_TYPES.add(DurationFieldType.weekyears());
      DATE_DURATION_TYPES.add(DurationFieldType.years());
      DATE_DURATION_TYPES.add(DurationFieldType.centuries());
      DATE_DURATION_TYPES.add(DurationFieldType.eras());
   }

   public LocalDate() {
      this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance());
   }

   public LocalDate(int var1, int var2, int var3) {
      this(var1, var2, var3, ISOChronology.getInstanceUTC());
   }

   public LocalDate(int var1, int var2, int var3, Chronology var4) {
      var4 = DateTimeUtils.getChronology(var4).withUTC();
      long var5 = var4.getDateTimeMillis(var1, var2, var3, 0);
      this.iChronology = var4;
      this.iLocalMillis = var5;
   }

   public LocalDate(long var1) {
      this(var1, (Chronology)ISOChronology.getInstance());
   }

   public LocalDate(long var1, Chronology var3) {
      var3 = DateTimeUtils.getChronology(var3);
      var1 = var3.getZone().getMillisKeepLocal(DateTimeZone.UTC, var1);
      var3 = var3.withUTC();
      this.iLocalMillis = var3.dayOfMonth().roundFloor(var1);
      this.iChronology = var3;
   }

   public LocalDate(long var1, DateTimeZone var3) {
      this(var1, (Chronology)ISOChronology.getInstance(var3));
   }

   public LocalDate(Object var1) {
      this(var1, (Chronology)null);
   }

   public LocalDate(Object var1, Chronology var2) {
      PartialConverter var3 = ConverterManager.getInstance().getPartialConverter(var1);
      var2 = DateTimeUtils.getChronology(var3.getChronology(var1, var2));
      this.iChronology = var2.withUTC();
      int[] var4 = var3.getPartialValues(this, var1, var2, ISODateTimeFormat.localDateParser());
      this.iLocalMillis = this.iChronology.getDateTimeMillis(var4[0], var4[1], var4[2], 0);
   }

   public LocalDate(Object var1, DateTimeZone var2) {
      PartialConverter var3 = ConverterManager.getInstance().getPartialConverter(var1);
      Chronology var5 = DateTimeUtils.getChronology(var3.getChronology(var1, var2));
      this.iChronology = var5.withUTC();
      int[] var4 = var3.getPartialValues(this, var1, var5, ISODateTimeFormat.localDateParser());
      this.iLocalMillis = this.iChronology.getDateTimeMillis(var4[0], var4[1], var4[2], 0);
   }

   public LocalDate(Chronology var1) {
      this(DateTimeUtils.currentTimeMillis(), var1);
   }

   public LocalDate(DateTimeZone var1) {
      this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance(var1));
   }

   public static LocalDate fromCalendarFields(Calendar var0) {
      if (var0 != null) {
         int var2 = var0.get(0);
         int var1 = var0.get(1);
         if (var2 != 1) {
            var1 = 1 - var1;
         }

         return new LocalDate(var1, var0.get(2) + 1, var0.get(5));
      } else {
         throw new IllegalArgumentException("The calendar must not be null");
      }
   }

   public static LocalDate fromDateFields(Date var0) {
      if (var0 != null) {
         if (var0.getTime() < 0L) {
            GregorianCalendar var1 = new GregorianCalendar();
            var1.setTime(var0);
            return fromCalendarFields(var1);
         } else {
            return new LocalDate(var0.getYear() + 1900, var0.getMonth() + 1, var0.getDate());
         }
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static LocalDate now() {
      return new LocalDate();
   }

   public static LocalDate now(Chronology var0) {
      if (var0 != null) {
         return new LocalDate(var0);
      } else {
         throw new NullPointerException("Chronology must not be null");
      }
   }

   public static LocalDate now(DateTimeZone var0) {
      if (var0 != null) {
         return new LocalDate(var0);
      } else {
         throw new NullPointerException("Zone must not be null");
      }
   }

   @FromString
   public static LocalDate parse(String var0) {
      return parse(var0, ISODateTimeFormat.localDateParser());
   }

   public static LocalDate parse(String var0, DateTimeFormatter var1) {
      return var1.parseLocalDate(var0);
   }

   private Object readResolve() {
      if (this.iChronology == null) {
         return new LocalDate(this.iLocalMillis, ISOChronology.getInstanceUTC());
      } else {
         return !DateTimeZone.UTC.equals(this.iChronology.getZone()) ? new LocalDate(this.iLocalMillis, this.iChronology.withUTC()) : this;
      }
   }

   public Property centuryOfEra() {
      return new Property(this, this.getChronology().centuryOfEra());
   }

   public int compareTo(ReadablePartial var1) {
      if (this == var1) {
         return 0;
      } else {
         if (var1 instanceof LocalDate) {
            LocalDate var6 = (LocalDate)var1;
            if (this.iChronology.equals(var6.iChronology)) {
               long var2 = this.iLocalMillis;
               long var4 = var6.iLocalMillis;
               if (var2 < var4) {
                  return -1;
               }

               if (var2 == var4) {
                  return 0;
               }

               return 1;
            }
         }

         return super.compareTo(var1);
      }
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

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         if (var1 instanceof LocalDate) {
            LocalDate var2 = (LocalDate)var1;
            if (this.iChronology.equals(var2.iChronology)) {
               if (this.iLocalMillis == var2.iLocalMillis) {
                  return true;
               }

               return false;
            }
         }

         return super.equals(var1);
      }
   }

   public Property era() {
      return new Property(this, this.getChronology().era());
   }

   public int get(DateTimeFieldType var1) {
      if (var1 != null) {
         if (this.isSupported(var1)) {
            return var1.getField(this.getChronology()).get(this.getLocalMillis());
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Field '");
            var2.append(var1);
            var2.append("' is not supported");
            throw new IllegalArgumentException(var2.toString());
         }
      } else {
         throw new IllegalArgumentException("The DateTimeFieldType must not be null");
      }
   }

   public int getCenturyOfEra() {
      return this.getChronology().centuryOfEra().get(this.getLocalMillis());
   }

   public Chronology getChronology() {
      return this.iChronology;
   }

   public int getDayOfMonth() {
      return this.getChronology().dayOfMonth().get(this.getLocalMillis());
   }

   public int getDayOfWeek() {
      return this.getChronology().dayOfWeek().get(this.getLocalMillis());
   }

   public int getDayOfYear() {
      return this.getChronology().dayOfYear().get(this.getLocalMillis());
   }

   public int getEra() {
      return this.getChronology().era().get(this.getLocalMillis());
   }

   protected DateTimeField getField(int var1, Chronology var2) {
      switch(var1) {
      case 0:
         return var2.year();
      case 1:
         return var2.monthOfYear();
      case 2:
         return var2.dayOfMonth();
      default:
         StringBuilder var3 = new StringBuilder();
         var3.append("Invalid index: ");
         var3.append(var1);
         throw new IndexOutOfBoundsException(var3.toString());
      }
   }

   protected long getLocalMillis() {
      return this.iLocalMillis;
   }

   public int getMonthOfYear() {
      return this.getChronology().monthOfYear().get(this.getLocalMillis());
   }

   public int getValue(int var1) {
      switch(var1) {
      case 0:
         return this.getChronology().year().get(this.getLocalMillis());
      case 1:
         return this.getChronology().monthOfYear().get(this.getLocalMillis());
      case 2:
         return this.getChronology().dayOfMonth().get(this.getLocalMillis());
      default:
         StringBuilder var2 = new StringBuilder();
         var2.append("Invalid index: ");
         var2.append(var1);
         throw new IndexOutOfBoundsException(var2.toString());
      }
   }

   public int getWeekOfWeekyear() {
      return this.getChronology().weekOfWeekyear().get(this.getLocalMillis());
   }

   public int getWeekyear() {
      return this.getChronology().weekyear().get(this.getLocalMillis());
   }

   public int getYear() {
      return this.getChronology().year().get(this.getLocalMillis());
   }

   public int getYearOfCentury() {
      return this.getChronology().yearOfCentury().get(this.getLocalMillis());
   }

   public int getYearOfEra() {
      return this.getChronology().yearOfEra().get(this.getLocalMillis());
   }

   public int hashCode() {
      int var1 = this.iHash;
      if (var1 == 0) {
         var1 = super.hashCode();
         this.iHash = var1;
         return var1;
      } else {
         return var1;
      }
   }

   public boolean isSupported(DateTimeFieldType var1) {
      if (var1 == null) {
         return false;
      } else {
         DurationFieldType var2 = var1.getDurationType();
         return !DATE_DURATION_TYPES.contains(var2) && var2.getField(this.getChronology()).getUnitMillis() < this.getChronology().days().getUnitMillis() ? false : var1.getField(this.getChronology()).isSupported();
      }
   }

   public boolean isSupported(DurationFieldType var1) {
      if (var1 == null) {
         return false;
      } else {
         DurationField var2 = var1.getField(this.getChronology());
         return !DATE_DURATION_TYPES.contains(var1) && var2.getUnitMillis() < this.getChronology().days().getUnitMillis() ? false : var2.isSupported();
      }
   }

   public LocalDate minus(ReadablePeriod var1) {
      return this.withPeriodAdded(var1, -1);
   }

   public LocalDate minusDays(int var1) {
      return var1 == 0 ? this : this.withLocalMillis(this.getChronology().days().subtract(this.getLocalMillis(), var1));
   }

   public LocalDate minusMonths(int var1) {
      return var1 == 0 ? this : this.withLocalMillis(this.getChronology().months().subtract(this.getLocalMillis(), var1));
   }

   public LocalDate minusWeeks(int var1) {
      return var1 == 0 ? this : this.withLocalMillis(this.getChronology().weeks().subtract(this.getLocalMillis(), var1));
   }

   public LocalDate minusYears(int var1) {
      return var1 == 0 ? this : this.withLocalMillis(this.getChronology().years().subtract(this.getLocalMillis(), var1));
   }

   public Property monthOfYear() {
      return new Property(this, this.getChronology().monthOfYear());
   }

   public LocalDate plus(ReadablePeriod var1) {
      return this.withPeriodAdded(var1, 1);
   }

   public LocalDate plusDays(int var1) {
      return var1 == 0 ? this : this.withLocalMillis(this.getChronology().days().add(this.getLocalMillis(), var1));
   }

   public LocalDate plusMonths(int var1) {
      return var1 == 0 ? this : this.withLocalMillis(this.getChronology().months().add(this.getLocalMillis(), var1));
   }

   public LocalDate plusWeeks(int var1) {
      return var1 == 0 ? this : this.withLocalMillis(this.getChronology().weeks().add(this.getLocalMillis(), var1));
   }

   public LocalDate plusYears(int var1) {
      return var1 == 0 ? this : this.withLocalMillis(this.getChronology().years().add(this.getLocalMillis(), var1));
   }

   public Property property(DateTimeFieldType var1) {
      if (var1 != null) {
         if (this.isSupported(var1)) {
            return new Property(this, var1.getField(this.getChronology()));
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Field '");
            var2.append(var1);
            var2.append("' is not supported");
            throw new IllegalArgumentException(var2.toString());
         }
      } else {
         throw new IllegalArgumentException("The DateTimeFieldType must not be null");
      }
   }

   public int size() {
      return 3;
   }

   public Date toDate() {
      int var1 = this.getDayOfMonth();
      Date var3 = new Date(this.getYear() - 1900, this.getMonthOfYear() - 1, var1);
      LocalDate var2 = fromDateFields(var3);
      if (!var2.isBefore(this)) {
         if (var2.equals(this)) {
            Date var4 = new Date(var3.getTime() - (long)TimeZone.getDefault().getDSTSavings());
            return var4.getDate() == var1 ? var4 : var3;
         } else {
            return var3;
         }
      } else {
         while(!var2.equals(this)) {
            var3.setTime(var3.getTime() + 3600000L);
            var2 = fromDateFields(var3);
         }

         while(var3.getDate() == var1) {
            var3.setTime(var3.getTime() - 1000L);
         }

         var3.setTime(var3.getTime() + 1000L);
         return var3;
      }
   }

   @Deprecated
   public DateMidnight toDateMidnight() {
      return this.toDateMidnight((DateTimeZone)null);
   }

   @Deprecated
   public DateMidnight toDateMidnight(DateTimeZone var1) {
      var1 = DateTimeUtils.getZone(var1);
      Chronology var2 = this.getChronology().withZone(var1);
      return new DateMidnight(this.getYear(), this.getMonthOfYear(), this.getDayOfMonth(), var2);
   }

   public DateTime toDateTime(LocalTime var1) {
      return this.toDateTime(var1, (DateTimeZone)null);
   }

   public DateTime toDateTime(LocalTime var1, DateTimeZone var2) {
      if (var1 == null) {
         return this.toDateTimeAtCurrentTime(var2);
      } else if (this.getChronology() == var1.getChronology()) {
         Chronology var3 = this.getChronology().withZone(var2);
         return new DateTime(this.getYear(), this.getMonthOfYear(), this.getDayOfMonth(), var1.getHourOfDay(), var1.getMinuteOfHour(), var1.getSecondOfMinute(), var1.getMillisOfSecond(), var3);
      } else {
         throw new IllegalArgumentException("The chronology of the time does not match");
      }
   }

   public DateTime toDateTimeAtCurrentTime() {
      return this.toDateTimeAtCurrentTime((DateTimeZone)null);
   }

   public DateTime toDateTimeAtCurrentTime(DateTimeZone var1) {
      var1 = DateTimeUtils.getZone(var1);
      Chronology var2 = this.getChronology().withZone(var1);
      return new DateTime(var2.set(this, DateTimeUtils.currentTimeMillis()), var2);
   }

   @Deprecated
   public DateTime toDateTimeAtMidnight() {
      return this.toDateTimeAtMidnight((DateTimeZone)null);
   }

   @Deprecated
   public DateTime toDateTimeAtMidnight(DateTimeZone var1) {
      var1 = DateTimeUtils.getZone(var1);
      Chronology var2 = this.getChronology().withZone(var1);
      return new DateTime(this.getYear(), this.getMonthOfYear(), this.getDayOfMonth(), 0, 0, 0, 0, var2);
   }

   public DateTime toDateTimeAtStartOfDay() {
      return this.toDateTimeAtStartOfDay((DateTimeZone)null);
   }

   public DateTime toDateTimeAtStartOfDay(DateTimeZone var1) {
      var1 = DateTimeUtils.getZone(var1);
      Chronology var4 = this.getChronology().withZone(var1);
      long var2 = var1.convertLocalToUTC(this.getLocalMillis() + 21600000L, false);
      return new DateTime(var4.dayOfMonth().roundFloor(var2), var4);
   }

   public Interval toInterval() {
      return this.toInterval((DateTimeZone)null);
   }

   public Interval toInterval(DateTimeZone var1) {
      var1 = DateTimeUtils.getZone(var1);
      return new Interval(this.toDateTimeAtStartOfDay(var1), this.plusDays(1).toDateTimeAtStartOfDay(var1));
   }

   public LocalDateTime toLocalDateTime(LocalTime var1) {
      if (var1 != null) {
         if (this.getChronology() == var1.getChronology()) {
            return new LocalDateTime(this.getLocalMillis() + var1.getLocalMillis(), this.getChronology());
         } else {
            throw new IllegalArgumentException("The chronology of the time does not match");
         }
      } else {
         throw new IllegalArgumentException("The time must not be null");
      }
   }

   @ToString
   public String toString() {
      return ISODateTimeFormat.date().print(this);
   }

   public String toString(String var1) {
      return var1 == null ? this.toString() : DateTimeFormat.forPattern(var1).print(this);
   }

   public String toString(String var1, Locale var2) throws IllegalArgumentException {
      return var1 == null ? this.toString() : DateTimeFormat.forPattern(var1).withLocale(var2).print(this);
   }

   public Property weekOfWeekyear() {
      return new Property(this, this.getChronology().weekOfWeekyear());
   }

   public Property weekyear() {
      return new Property(this, this.getChronology().weekyear());
   }

   public LocalDate withCenturyOfEra(int var1) {
      return this.withLocalMillis(this.getChronology().centuryOfEra().set(this.getLocalMillis(), var1));
   }

   public LocalDate withDayOfMonth(int var1) {
      return this.withLocalMillis(this.getChronology().dayOfMonth().set(this.getLocalMillis(), var1));
   }

   public LocalDate withDayOfWeek(int var1) {
      return this.withLocalMillis(this.getChronology().dayOfWeek().set(this.getLocalMillis(), var1));
   }

   public LocalDate withDayOfYear(int var1) {
      return this.withLocalMillis(this.getChronology().dayOfYear().set(this.getLocalMillis(), var1));
   }

   public LocalDate withEra(int var1) {
      return this.withLocalMillis(this.getChronology().era().set(this.getLocalMillis(), var1));
   }

   public LocalDate withField(DateTimeFieldType var1, int var2) {
      if (var1 != null) {
         if (this.isSupported(var1)) {
            return this.withLocalMillis(var1.getField(this.getChronology()).set(this.getLocalMillis(), var2));
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("Field '");
            var3.append(var1);
            var3.append("' is not supported");
            throw new IllegalArgumentException(var3.toString());
         }
      } else {
         throw new IllegalArgumentException("Field must not be null");
      }
   }

   public LocalDate withFieldAdded(DurationFieldType var1, int var2) {
      if (var1 != null) {
         if (this.isSupported(var1)) {
            return var2 == 0 ? this : this.withLocalMillis(var1.getField(this.getChronology()).add(this.getLocalMillis(), var2));
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("Field '");
            var3.append(var1);
            var3.append("' is not supported");
            throw new IllegalArgumentException(var3.toString());
         }
      } else {
         throw new IllegalArgumentException("Field must not be null");
      }
   }

   public LocalDate withFields(ReadablePartial var1) {
      return var1 == null ? this : this.withLocalMillis(this.getChronology().set(var1, this.getLocalMillis()));
   }

   LocalDate withLocalMillis(long var1) {
      var1 = this.iChronology.dayOfMonth().roundFloor(var1);
      return var1 == this.getLocalMillis() ? this : new LocalDate(var1, this.getChronology());
   }

   public LocalDate withMonthOfYear(int var1) {
      return this.withLocalMillis(this.getChronology().monthOfYear().set(this.getLocalMillis(), var1));
   }

   public LocalDate withPeriodAdded(ReadablePeriod var1, int var2) {
      if (var1 != null) {
         if (var2 == 0) {
            return this;
         } else {
            long var4 = this.getLocalMillis();
            Chronology var8 = this.getChronology();

            for(int var3 = 0; var3 < var1.size(); ++var3) {
               long var6 = (long)FieldUtils.safeMultiply(var1.getValue(var3), var2);
               DurationFieldType var9 = var1.getFieldType(var3);
               if (this.isSupported(var9)) {
                  var4 = var9.getField(var8).add(var4, var6);
               }
            }

            return this.withLocalMillis(var4);
         }
      } else {
         return this;
      }
   }

   public LocalDate withWeekOfWeekyear(int var1) {
      return this.withLocalMillis(this.getChronology().weekOfWeekyear().set(this.getLocalMillis(), var1));
   }

   public LocalDate withWeekyear(int var1) {
      return this.withLocalMillis(this.getChronology().weekyear().set(this.getLocalMillis(), var1));
   }

   public LocalDate withYear(int var1) {
      return this.withLocalMillis(this.getChronology().year().set(this.getLocalMillis(), var1));
   }

   public LocalDate withYearOfCentury(int var1) {
      return this.withLocalMillis(this.getChronology().yearOfCentury().set(this.getLocalMillis(), var1));
   }

   public LocalDate withYearOfEra(int var1) {
      return this.withLocalMillis(this.getChronology().yearOfEra().set(this.getLocalMillis(), var1));
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
