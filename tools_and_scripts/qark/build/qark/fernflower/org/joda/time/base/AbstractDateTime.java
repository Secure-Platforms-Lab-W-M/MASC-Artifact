package org.joda.time.base;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import org.joda.convert.ToString;
import org.joda.time.DateTimeFieldType;
import org.joda.time.ReadableDateTime;
import org.joda.time.format.DateTimeFormat;

public abstract class AbstractDateTime extends AbstractInstant implements ReadableDateTime {
   protected AbstractDateTime() {
   }

   public int get(DateTimeFieldType var1) {
      if (var1 != null) {
         return var1.getField(this.getChronology()).get(this.getMillis());
      } else {
         throw new IllegalArgumentException("The DateTimeFieldType must not be null");
      }
   }

   public int getCenturyOfEra() {
      return this.getChronology().centuryOfEra().get(this.getMillis());
   }

   public int getDayOfMonth() {
      return this.getChronology().dayOfMonth().get(this.getMillis());
   }

   public int getDayOfWeek() {
      return this.getChronology().dayOfWeek().get(this.getMillis());
   }

   public int getDayOfYear() {
      return this.getChronology().dayOfYear().get(this.getMillis());
   }

   public int getEra() {
      return this.getChronology().era().get(this.getMillis());
   }

   public int getHourOfDay() {
      return this.getChronology().hourOfDay().get(this.getMillis());
   }

   public int getMillisOfDay() {
      return this.getChronology().millisOfDay().get(this.getMillis());
   }

   public int getMillisOfSecond() {
      return this.getChronology().millisOfSecond().get(this.getMillis());
   }

   public int getMinuteOfDay() {
      return this.getChronology().minuteOfDay().get(this.getMillis());
   }

   public int getMinuteOfHour() {
      return this.getChronology().minuteOfHour().get(this.getMillis());
   }

   public int getMonthOfYear() {
      return this.getChronology().monthOfYear().get(this.getMillis());
   }

   public int getSecondOfDay() {
      return this.getChronology().secondOfDay().get(this.getMillis());
   }

   public int getSecondOfMinute() {
      return this.getChronology().secondOfMinute().get(this.getMillis());
   }

   public int getWeekOfWeekyear() {
      return this.getChronology().weekOfWeekyear().get(this.getMillis());
   }

   public int getWeekyear() {
      return this.getChronology().weekyear().get(this.getMillis());
   }

   public int getYear() {
      return this.getChronology().year().get(this.getMillis());
   }

   public int getYearOfCentury() {
      return this.getChronology().yearOfCentury().get(this.getMillis());
   }

   public int getYearOfEra() {
      return this.getChronology().yearOfEra().get(this.getMillis());
   }

   public Calendar toCalendar(Locale var1) {
      if (var1 == null) {
         var1 = Locale.getDefault();
      }

      Calendar var2 = Calendar.getInstance(this.getZone().toTimeZone(), var1);
      var2.setTime(this.toDate());
      return var2;
   }

   public GregorianCalendar toGregorianCalendar() {
      GregorianCalendar var1 = new GregorianCalendar(this.getZone().toTimeZone());
      var1.setTime(this.toDate());
      return var1;
   }

   @ToString
   public String toString() {
      return super.toString();
   }

   public String toString(String var1) {
      return var1 == null ? this.toString() : DateTimeFormat.forPattern(var1).print(this);
   }

   public String toString(String var1, Locale var2) throws IllegalArgumentException {
      return var1 == null ? this.toString() : DateTimeFormat.forPattern(var1).withLocale(var2).print(this);
   }
}
