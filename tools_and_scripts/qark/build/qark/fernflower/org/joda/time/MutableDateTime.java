package org.joda.time;

import java.io.Serializable;
import org.joda.convert.FromString;
import org.joda.time.MutableDateTime.Property;
import org.joda.time.base.BaseDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class MutableDateTime extends BaseDateTime implements ReadWritableDateTime, Cloneable, Serializable {
   public static final int ROUND_CEILING = 2;
   public static final int ROUND_FLOOR = 1;
   public static final int ROUND_HALF_CEILING = 4;
   public static final int ROUND_HALF_EVEN = 5;
   public static final int ROUND_HALF_FLOOR = 3;
   public static final int ROUND_NONE = 0;
   private static final long serialVersionUID = 2852608688135209575L;
   private DateTimeField iRoundingField;
   private int iRoundingMode;

   public MutableDateTime() {
   }

   public MutableDateTime(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   public MutableDateTime(int var1, int var2, int var3, int var4, int var5, int var6, int var7, Chronology var8) {
      super(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public MutableDateTime(int var1, int var2, int var3, int var4, int var5, int var6, int var7, DateTimeZone var8) {
      super(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public MutableDateTime(long var1) {
      super(var1);
   }

   public MutableDateTime(long var1, Chronology var3) {
      super(var1, var3);
   }

   public MutableDateTime(long var1, DateTimeZone var3) {
      super(var1, var3);
   }

   public MutableDateTime(Object var1) {
      super(var1, (Chronology)null);
   }

   public MutableDateTime(Object var1, Chronology var2) {
      super(var1, DateTimeUtils.getChronology(var2));
   }

   public MutableDateTime(Object var1, DateTimeZone var2) {
      super(var1, var2);
   }

   public MutableDateTime(Chronology var1) {
      super(var1);
   }

   public MutableDateTime(DateTimeZone var1) {
      super(var1);
   }

   public static MutableDateTime now() {
      return new MutableDateTime();
   }

   public static MutableDateTime now(Chronology var0) {
      if (var0 != null) {
         return new MutableDateTime(var0);
      } else {
         throw new NullPointerException("Chronology must not be null");
      }
   }

   public static MutableDateTime now(DateTimeZone var0) {
      if (var0 != null) {
         return new MutableDateTime(var0);
      } else {
         throw new NullPointerException("Zone must not be null");
      }
   }

   @FromString
   public static MutableDateTime parse(String var0) {
      return parse(var0, ISODateTimeFormat.dateTimeParser().withOffsetParsed());
   }

   public static MutableDateTime parse(String var0, DateTimeFormatter var1) {
      return var1.parseDateTime(var0).toMutableDateTime();
   }

   public void add(long var1) {
      this.setMillis(FieldUtils.safeAdd(this.getMillis(), var1));
   }

   public void add(DurationFieldType var1, int var2) {
      if (var1 != null) {
         if (var2 != 0) {
            this.setMillis(var1.getField(this.getChronology()).add(this.getMillis(), var2));
         }
      } else {
         throw new IllegalArgumentException("Field must not be null");
      }
   }

   public void add(ReadableDuration var1) {
      this.add((ReadableDuration)var1, 1);
   }

   public void add(ReadableDuration var1, int var2) {
      if (var1 != null) {
         this.add(FieldUtils.safeMultiply(var1.getMillis(), var2));
      }
   }

   public void add(ReadablePeriod var1) {
      this.add((ReadablePeriod)var1, 1);
   }

   public void add(ReadablePeriod var1, int var2) {
      if (var1 != null) {
         this.setMillis(this.getChronology().add(var1, this.getMillis(), var2));
      }
   }

   public void addDays(int var1) {
      if (var1 != 0) {
         this.setMillis(this.getChronology().days().add(this.getMillis(), var1));
      }
   }

   public void addHours(int var1) {
      if (var1 != 0) {
         this.setMillis(this.getChronology().hours().add(this.getMillis(), var1));
      }
   }

   public void addMillis(int var1) {
      if (var1 != 0) {
         this.setMillis(this.getChronology().millis().add(this.getMillis(), var1));
      }
   }

   public void addMinutes(int var1) {
      if (var1 != 0) {
         this.setMillis(this.getChronology().minutes().add(this.getMillis(), var1));
      }
   }

   public void addMonths(int var1) {
      if (var1 != 0) {
         this.setMillis(this.getChronology().months().add(this.getMillis(), var1));
      }
   }

   public void addSeconds(int var1) {
      if (var1 != 0) {
         this.setMillis(this.getChronology().seconds().add(this.getMillis(), var1));
      }
   }

   public void addWeeks(int var1) {
      if (var1 != 0) {
         this.setMillis(this.getChronology().weeks().add(this.getMillis(), var1));
      }
   }

   public void addWeekyears(int var1) {
      if (var1 != 0) {
         this.setMillis(this.getChronology().weekyears().add(this.getMillis(), var1));
      }
   }

   public void addYears(int var1) {
      if (var1 != 0) {
         this.setMillis(this.getChronology().years().add(this.getMillis(), var1));
      }
   }

   public Property centuryOfEra() {
      return new Property(this, this.getChronology().centuryOfEra());
   }

   public Object clone() {
      try {
         Object var1 = super.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new InternalError("Clone error");
      }
   }

   public MutableDateTime copy() {
      return (MutableDateTime)this.clone();
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

   public DateTimeField getRoundingField() {
      return this.iRoundingField;
   }

   public int getRoundingMode() {
      return this.iRoundingMode;
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

   public Property minuteOfDay() {
      return new Property(this, this.getChronology().minuteOfDay());
   }

   public Property minuteOfHour() {
      return new Property(this, this.getChronology().minuteOfHour());
   }

   public Property monthOfYear() {
      return new Property(this, this.getChronology().monthOfYear());
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

   public void set(DateTimeFieldType var1, int var2) {
      if (var1 != null) {
         this.setMillis(var1.getField(this.getChronology()).set(this.getMillis(), var2));
      } else {
         throw new IllegalArgumentException("Field must not be null");
      }
   }

   public void setChronology(Chronology var1) {
      super.setChronology(var1);
   }

   public void setDate(int var1, int var2, int var3) {
      this.setDate(this.getChronology().getDateTimeMillis(var1, var2, var3, 0));
   }

   public void setDate(long var1) {
      this.setMillis(this.getChronology().millisOfDay().set(var1, this.getMillisOfDay()));
   }

   public void setDate(ReadableInstant var1) {
      long var2 = DateTimeUtils.getInstantMillis(var1);
      if (var1 instanceof ReadableDateTime) {
         DateTimeZone var4 = DateTimeUtils.getChronology(((ReadableDateTime)var1).getChronology()).getZone();
         if (var4 != null) {
            var2 = var4.getMillisKeepLocal(this.getZone(), var2);
         }
      }

      this.setDate(var2);
   }

   public void setDateTime(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this.setMillis(this.getChronology().getDateTimeMillis(var1, var2, var3, var4, var5, var6, var7));
   }

   public void setDayOfMonth(int var1) {
      this.setMillis(this.getChronology().dayOfMonth().set(this.getMillis(), var1));
   }

   public void setDayOfWeek(int var1) {
      this.setMillis(this.getChronology().dayOfWeek().set(this.getMillis(), var1));
   }

   public void setDayOfYear(int var1) {
      this.setMillis(this.getChronology().dayOfYear().set(this.getMillis(), var1));
   }

   public void setHourOfDay(int var1) {
      this.setMillis(this.getChronology().hourOfDay().set(this.getMillis(), var1));
   }

   public void setMillis(long var1) {
      switch(this.iRoundingMode) {
      case 0:
      default:
         break;
      case 1:
         var1 = this.iRoundingField.roundFloor(var1);
         break;
      case 2:
         var1 = this.iRoundingField.roundCeiling(var1);
         break;
      case 3:
         var1 = this.iRoundingField.roundHalfFloor(var1);
         break;
      case 4:
         var1 = this.iRoundingField.roundHalfCeiling(var1);
         break;
      case 5:
         var1 = this.iRoundingField.roundHalfEven(var1);
      }

      super.setMillis(var1);
   }

   public void setMillis(ReadableInstant var1) {
      this.setMillis(DateTimeUtils.getInstantMillis(var1));
   }

   public void setMillisOfDay(int var1) {
      this.setMillis(this.getChronology().millisOfDay().set(this.getMillis(), var1));
   }

   public void setMillisOfSecond(int var1) {
      this.setMillis(this.getChronology().millisOfSecond().set(this.getMillis(), var1));
   }

   public void setMinuteOfDay(int var1) {
      this.setMillis(this.getChronology().minuteOfDay().set(this.getMillis(), var1));
   }

   public void setMinuteOfHour(int var1) {
      this.setMillis(this.getChronology().minuteOfHour().set(this.getMillis(), var1));
   }

   public void setMonthOfYear(int var1) {
      this.setMillis(this.getChronology().monthOfYear().set(this.getMillis(), var1));
   }

   public void setRounding(DateTimeField var1) {
      this.setRounding(var1, 1);
   }

   public void setRounding(DateTimeField var1, int var2) {
      if (var1 != null && (var2 < 0 || var2 > 5)) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Illegal rounding mode: ");
         var4.append(var2);
         throw new IllegalArgumentException(var4.toString());
      } else {
         DateTimeField var3;
         if (var2 == 0) {
            var3 = null;
         } else {
            var3 = var1;
         }

         this.iRoundingField = var3;
         if (var1 == null) {
            var2 = 0;
         }

         this.iRoundingMode = var2;
         this.setMillis(this.getMillis());
      }
   }

   public void setSecondOfDay(int var1) {
      this.setMillis(this.getChronology().secondOfDay().set(this.getMillis(), var1));
   }

   public void setSecondOfMinute(int var1) {
      this.setMillis(this.getChronology().secondOfMinute().set(this.getMillis(), var1));
   }

   public void setTime(int var1, int var2, int var3, int var4) {
      this.setMillis(this.getChronology().getDateTimeMillis(this.getMillis(), var1, var2, var3, var4));
   }

   public void setTime(long var1) {
      int var3 = ISOChronology.getInstanceUTC().millisOfDay().get(var1);
      this.setMillis(this.getChronology().millisOfDay().set(this.getMillis(), var3));
   }

   public void setTime(ReadableInstant var1) {
      long var2 = DateTimeUtils.getInstantMillis(var1);
      DateTimeZone var4 = DateTimeUtils.getInstantChronology(var1).getZone();
      if (var4 != null) {
         var2 = var4.getMillisKeepLocal(DateTimeZone.UTC, var2);
      }

      this.setTime(var2);
   }

   public void setWeekOfWeekyear(int var1) {
      this.setMillis(this.getChronology().weekOfWeekyear().set(this.getMillis(), var1));
   }

   public void setWeekyear(int var1) {
      this.setMillis(this.getChronology().weekyear().set(this.getMillis(), var1));
   }

   public void setYear(int var1) {
      this.setMillis(this.getChronology().year().set(this.getMillis(), var1));
   }

   public void setZone(DateTimeZone var1) {
      var1 = DateTimeUtils.getZone(var1);
      Chronology var2 = this.getChronology();
      if (var2.getZone() != var1) {
         this.setChronology(var2.withZone(var1));
      }
   }

   public void setZoneRetainFields(DateTimeZone var1) {
      var1 = DateTimeUtils.getZone(var1);
      DateTimeZone var4 = DateTimeUtils.getZone(this.getZone());
      if (var1 != var4) {
         long var2 = var4.getMillisKeepLocal(var1, this.getMillis());
         this.setChronology(this.getChronology().withZone(var1));
         this.setMillis(var2);
      }
   }

   public Property weekOfWeekyear() {
      return new Property(this, this.getChronology().weekOfWeekyear());
   }

   public Property weekyear() {
      return new Property(this, this.getChronology().weekyear());
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
