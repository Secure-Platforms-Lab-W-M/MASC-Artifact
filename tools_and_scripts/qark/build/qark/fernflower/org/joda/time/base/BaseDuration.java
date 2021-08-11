package org.joda.time.base;

import java.io.Serializable;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.convert.ConverterManager;
import org.joda.time.field.FieldUtils;

public abstract class BaseDuration extends AbstractDuration implements ReadableDuration, Serializable {
   private static final long serialVersionUID = 2581698638990L;
   private volatile long iMillis;

   protected BaseDuration(long var1) {
      this.iMillis = var1;
   }

   protected BaseDuration(long var1, long var3) {
      this.iMillis = FieldUtils.safeSubtract(var3, var1);
   }

   protected BaseDuration(Object var1) {
      this.iMillis = ConverterManager.getInstance().getDurationConverter(var1).getDurationMillis(var1);
   }

   protected BaseDuration(ReadableInstant var1, ReadableInstant var2) {
      if (var1 == var2) {
         this.iMillis = 0L;
      } else {
         long var3 = DateTimeUtils.getInstantMillis(var1);
         this.iMillis = FieldUtils.safeSubtract(DateTimeUtils.getInstantMillis(var2), var3);
      }
   }

   public long getMillis() {
      return this.iMillis;
   }

   protected void setMillis(long var1) {
      this.iMillis = var1;
   }

   public Interval toIntervalFrom(ReadableInstant var1) {
      return new Interval(var1, this);
   }

   public Interval toIntervalTo(ReadableInstant var1) {
      return new Interval(this, var1);
   }

   public Period toPeriod(Chronology var1) {
      return new Period(this.getMillis(), var1);
   }

   public Period toPeriod(PeriodType var1) {
      return new Period(this.getMillis(), var1);
   }

   public Period toPeriod(PeriodType var1, Chronology var2) {
      return new Period(this.getMillis(), var1, var2);
   }

   public Period toPeriodFrom(ReadableInstant var1) {
      return new Period(var1, this);
   }

   public Period toPeriodFrom(ReadableInstant var1, PeriodType var2) {
      return new Period(var1, this, var2);
   }

   public Period toPeriodTo(ReadableInstant var1) {
      return new Period(this, var1);
   }

   public Period toPeriodTo(ReadableInstant var1, PeriodType var2) {
      return new Period(this, var1, var2);
   }
}
