package org.joda.time.base;

import java.util.Date;
import org.joda.convert.ToString;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public abstract class AbstractInstant implements ReadableInstant {
   protected AbstractInstant() {
   }

   public int compareTo(ReadableInstant var1) {
      if (this == var1) {
         return 0;
      } else {
         long var2 = var1.getMillis();
         long var4 = this.getMillis();
         if (var4 == var2) {
            return 0;
         } else {
            return var4 < var2 ? -1 : 1;
         }
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ReadableInstant)) {
         return false;
      } else {
         ReadableInstant var2 = (ReadableInstant)var1;
         return this.getMillis() == var2.getMillis() && FieldUtils.equals(this.getChronology(), var2.getChronology());
      }
   }

   public int get(DateTimeField var1) {
      if (var1 != null) {
         return var1.get(this.getMillis());
      } else {
         throw new IllegalArgumentException("The DateTimeField must not be null");
      }
   }

   public int get(DateTimeFieldType var1) {
      if (var1 != null) {
         return var1.getField(this.getChronology()).get(this.getMillis());
      } else {
         throw new IllegalArgumentException("The DateTimeFieldType must not be null");
      }
   }

   public DateTimeZone getZone() {
      return this.getChronology().getZone();
   }

   public int hashCode() {
      return (int)(this.getMillis() ^ this.getMillis() >>> 32) + this.getChronology().hashCode();
   }

   public boolean isAfter(long var1) {
      return this.getMillis() > var1;
   }

   public boolean isAfter(ReadableInstant var1) {
      return this.isAfter(DateTimeUtils.getInstantMillis(var1));
   }

   public boolean isAfterNow() {
      return this.isAfter(DateTimeUtils.currentTimeMillis());
   }

   public boolean isBefore(long var1) {
      return this.getMillis() < var1;
   }

   public boolean isBefore(ReadableInstant var1) {
      return this.isBefore(DateTimeUtils.getInstantMillis(var1));
   }

   public boolean isBeforeNow() {
      return this.isBefore(DateTimeUtils.currentTimeMillis());
   }

   public boolean isEqual(long var1) {
      return this.getMillis() == var1;
   }

   public boolean isEqual(ReadableInstant var1) {
      return this.isEqual(DateTimeUtils.getInstantMillis(var1));
   }

   public boolean isEqualNow() {
      return this.isEqual(DateTimeUtils.currentTimeMillis());
   }

   public boolean isSupported(DateTimeFieldType var1) {
      return var1 == null ? false : var1.getField(this.getChronology()).isSupported();
   }

   public Date toDate() {
      return new Date(this.getMillis());
   }

   public DateTime toDateTime() {
      return new DateTime(this.getMillis(), this.getZone());
   }

   public DateTime toDateTime(Chronology var1) {
      return new DateTime(this.getMillis(), var1);
   }

   public DateTime toDateTime(DateTimeZone var1) {
      Chronology var2 = DateTimeUtils.getChronology(this.getChronology()).withZone(var1);
      return new DateTime(this.getMillis(), var2);
   }

   public DateTime toDateTimeISO() {
      return new DateTime(this.getMillis(), ISOChronology.getInstance(this.getZone()));
   }

   public Instant toInstant() {
      return new Instant(this.getMillis());
   }

   public MutableDateTime toMutableDateTime() {
      return new MutableDateTime(this.getMillis(), this.getZone());
   }

   public MutableDateTime toMutableDateTime(Chronology var1) {
      return new MutableDateTime(this.getMillis(), var1);
   }

   public MutableDateTime toMutableDateTime(DateTimeZone var1) {
      Chronology var2 = DateTimeUtils.getChronology(this.getChronology()).withZone(var1);
      return new MutableDateTime(this.getMillis(), var2);
   }

   public MutableDateTime toMutableDateTimeISO() {
      return new MutableDateTime(this.getMillis(), ISOChronology.getInstance(this.getZone()));
   }

   @ToString
   public String toString() {
      return ISODateTimeFormat.dateTime().print(this);
   }

   public String toString(DateTimeFormatter var1) {
      return var1 == null ? this.toString() : var1.print(this);
   }
}
