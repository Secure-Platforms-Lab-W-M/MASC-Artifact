package org.joda.time;

import java.io.Serializable;
import org.joda.convert.FromString;
import org.joda.time.base.AbstractInstant;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public final class Instant extends AbstractInstant implements ReadableInstant, Serializable {
   private static final long serialVersionUID = 3299096530934209741L;
   private final long iMillis;

   public Instant() {
      this.iMillis = DateTimeUtils.currentTimeMillis();
   }

   public Instant(long var1) {
      this.iMillis = var1;
   }

   public Instant(Object var1) {
      this.iMillis = ConverterManager.getInstance().getInstantConverter(var1).getInstantMillis(var1, ISOChronology.getInstanceUTC());
   }

   public static Instant now() {
      return new Instant();
   }

   @FromString
   public static Instant parse(String var0) {
      return parse(var0, ISODateTimeFormat.dateTimeParser());
   }

   public static Instant parse(String var0, DateTimeFormatter var1) {
      return var1.parseDateTime(var0).toInstant();
   }

   public Chronology getChronology() {
      return ISOChronology.getInstanceUTC();
   }

   public long getMillis() {
      return this.iMillis;
   }

   public Instant minus(long var1) {
      return this.withDurationAdded(var1, -1);
   }

   public Instant minus(ReadableDuration var1) {
      return this.withDurationAdded(var1, -1);
   }

   public Instant plus(long var1) {
      return this.withDurationAdded(var1, 1);
   }

   public Instant plus(ReadableDuration var1) {
      return this.withDurationAdded(var1, 1);
   }

   public DateTime toDateTime() {
      return new DateTime(this.getMillis(), ISOChronology.getInstance());
   }

   @Deprecated
   public DateTime toDateTimeISO() {
      return this.toDateTime();
   }

   public Instant toInstant() {
      return this;
   }

   public MutableDateTime toMutableDateTime() {
      return new MutableDateTime(this.getMillis(), ISOChronology.getInstance());
   }

   @Deprecated
   public MutableDateTime toMutableDateTimeISO() {
      return this.toMutableDateTime();
   }

   public Instant withDurationAdded(long var1, int var3) {
      if (var1 != 0L) {
         return var3 == 0 ? this : this.withMillis(this.getChronology().add(this.getMillis(), var1, var3));
      } else {
         return this;
      }
   }

   public Instant withDurationAdded(ReadableDuration var1, int var2) {
      if (var1 != null) {
         return var2 == 0 ? this : this.withDurationAdded(var1.getMillis(), var2);
      } else {
         return this;
      }
   }

   public Instant withMillis(long var1) {
      return var1 == this.iMillis ? this : new Instant(var1);
   }
}
