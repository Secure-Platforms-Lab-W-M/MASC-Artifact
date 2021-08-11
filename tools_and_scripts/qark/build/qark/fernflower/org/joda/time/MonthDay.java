package org.joda.time;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.MonthDay.Property;
import org.joda.time.base.BasePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;

public final class MonthDay extends BasePartial implements ReadablePartial, Serializable {
   public static final int DAY_OF_MONTH = 1;
   private static final DateTimeFieldType[] FIELD_TYPES = new DateTimeFieldType[]{DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()};
   public static final int MONTH_OF_YEAR = 0;
   private static final DateTimeFormatter PARSER = (new DateTimeFormatterBuilder()).appendOptional(ISODateTimeFormat.localDateParser().getParser()).appendOptional(DateTimeFormat.forPattern("--MM-dd").getParser()).toFormatter();
   private static final long serialVersionUID = 2954560699050434609L;

   public MonthDay() {
   }

   public MonthDay(int var1, int var2) {
      this(var1, var2, (Chronology)null);
   }

   public MonthDay(int var1, int var2, Chronology var3) {
      super(new int[]{var1, var2}, var3);
   }

   public MonthDay(long var1) {
      super(var1);
   }

   public MonthDay(long var1, Chronology var3) {
      super(var1, var3);
   }

   public MonthDay(Object var1) {
      super(var1, (Chronology)null, ISODateTimeFormat.localDateParser());
   }

   public MonthDay(Object var1, Chronology var2) {
      super(var1, DateTimeUtils.getChronology(var2), ISODateTimeFormat.localDateParser());
   }

   public MonthDay(Chronology var1) {
      super(var1);
   }

   public MonthDay(DateTimeZone var1) {
      super(ISOChronology.getInstance(var1));
   }

   MonthDay(MonthDay var1, Chronology var2) {
      super((BasePartial)var1, (Chronology)var2);
   }

   MonthDay(MonthDay var1, int[] var2) {
      super((BasePartial)var1, (int[])var2);
   }

   public static MonthDay fromCalendarFields(Calendar var0) {
      if (var0 != null) {
         return new MonthDay(var0.get(2) + 1, var0.get(5));
      } else {
         throw new IllegalArgumentException("The calendar must not be null");
      }
   }

   public static MonthDay fromDateFields(Date var0) {
      if (var0 != null) {
         return new MonthDay(var0.getMonth() + 1, var0.getDate());
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static MonthDay now() {
      return new MonthDay();
   }

   public static MonthDay now(Chronology var0) {
      if (var0 != null) {
         return new MonthDay(var0);
      } else {
         throw new NullPointerException("Chronology must not be null");
      }
   }

   public static MonthDay now(DateTimeZone var0) {
      if (var0 != null) {
         return new MonthDay(var0);
      } else {
         throw new NullPointerException("Zone must not be null");
      }
   }

   @FromString
   public static MonthDay parse(String var0) {
      return parse(var0, PARSER);
   }

   public static MonthDay parse(String var0, DateTimeFormatter var1) {
      LocalDate var2 = var1.parseLocalDate(var0);
      return new MonthDay(var2.getMonthOfYear(), var2.getDayOfMonth());
   }

   private Object readResolve() {
      return !DateTimeZone.UTC.equals(this.getChronology().getZone()) ? new MonthDay(this, this.getChronology().withUTC()) : this;
   }

   public Property dayOfMonth() {
      return new Property(this, 1);
   }

   public int getDayOfMonth() {
      return this.getValue(1);
   }

   protected DateTimeField getField(int var1, Chronology var2) {
      switch(var1) {
      case 0:
         return var2.monthOfYear();
      case 1:
         return var2.dayOfMonth();
      default:
         StringBuilder var3 = new StringBuilder();
         var3.append("Invalid index: ");
         var3.append(var1);
         throw new IndexOutOfBoundsException(var3.toString());
      }
   }

   public DateTimeFieldType getFieldType(int var1) {
      return FIELD_TYPES[var1];
   }

   public DateTimeFieldType[] getFieldTypes() {
      return (DateTimeFieldType[])FIELD_TYPES.clone();
   }

   public int getMonthOfYear() {
      return this.getValue(0);
   }

   public MonthDay minus(ReadablePeriod var1) {
      return this.withPeriodAdded(var1, -1);
   }

   public MonthDay minusDays(int var1) {
      return this.withFieldAdded(DurationFieldType.days(), FieldUtils.safeNegate(var1));
   }

   public MonthDay minusMonths(int var1) {
      return this.withFieldAdded(DurationFieldType.months(), FieldUtils.safeNegate(var1));
   }

   public Property monthOfYear() {
      return new Property(this, 0);
   }

   public MonthDay plus(ReadablePeriod var1) {
      return this.withPeriodAdded(var1, 1);
   }

   public MonthDay plusDays(int var1) {
      return this.withFieldAdded(DurationFieldType.days(), var1);
   }

   public MonthDay plusMonths(int var1) {
      return this.withFieldAdded(DurationFieldType.months(), var1);
   }

   public Property property(DateTimeFieldType var1) {
      return new Property(this, this.indexOfSupported(var1));
   }

   public int size() {
      return 2;
   }

   public LocalDate toLocalDate(int var1) {
      return new LocalDate(var1, this.getMonthOfYear(), this.getDayOfMonth(), this.getChronology());
   }

   @ToString
   public String toString() {
      ArrayList var1 = new ArrayList();
      var1.add(DateTimeFieldType.monthOfYear());
      var1.add(DateTimeFieldType.dayOfMonth());
      return ISODateTimeFormat.forFields(var1, true, true).print(this);
   }

   public String toString(String var1) {
      return var1 == null ? this.toString() : DateTimeFormat.forPattern(var1).print(this);
   }

   public String toString(String var1, Locale var2) throws IllegalArgumentException {
      return var1 == null ? this.toString() : DateTimeFormat.forPattern(var1).withLocale(var2).print(this);
   }

   public MonthDay withChronologyRetainFields(Chronology var1) {
      var1 = DateTimeUtils.getChronology(var1).withUTC();
      if (var1 == this.getChronology()) {
         return this;
      } else {
         MonthDay var2 = new MonthDay(this, var1);
         var1.validate(var2, this.getValues());
         return var2;
      }
   }

   public MonthDay withDayOfMonth(int var1) {
      int[] var2 = this.getValues();
      return new MonthDay(this, this.getChronology().dayOfMonth().set(this, 1, var2, var1));
   }

   public MonthDay withField(DateTimeFieldType var1, int var2) {
      int var3 = this.indexOfSupported(var1);
      if (var2 == this.getValue(var3)) {
         return this;
      } else {
         int[] var4 = this.getValues();
         return new MonthDay(this, this.getField(var3).set(this, var3, var4, var2));
      }
   }

   public MonthDay withFieldAdded(DurationFieldType var1, int var2) {
      int var3 = this.indexOfSupported(var1);
      if (var2 == 0) {
         return this;
      } else {
         int[] var4 = this.getValues();
         return new MonthDay(this, this.getField(var3).add(this, var3, var4, var2));
      }
   }

   public MonthDay withMonthOfYear(int var1) {
      int[] var2 = this.getValues();
      return new MonthDay(this, this.getChronology().monthOfYear().set(this, 0, var2, var1));
   }

   public MonthDay withPeriodAdded(ReadablePeriod var1, int var2) {
      if (var1 != null) {
         if (var2 == 0) {
            return this;
         } else {
            int[] var5 = this.getValues();

            for(int var3 = 0; var3 < var1.size(); ++var3) {
               int var4 = this.indexOf(var1.getFieldType(var3));
               if (var4 >= 0) {
                  var5 = this.getField(var4).add(this, var4, var5, FieldUtils.safeMultiply(var1.getValue(var3), var2));
               }
            }

            return new MonthDay(this, var5);
         }
      } else {
         return this;
      }
   }
}
