package org.joda.time;

import java.io.Serializable;
import org.joda.convert.FromString;
import org.joda.time.base.BasePeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public class MutablePeriod extends BasePeriod implements ReadWritablePeriod, Cloneable, Serializable {
   private static final long serialVersionUID = 3436451121567212165L;

   public MutablePeriod() {
      super(0L, (PeriodType)null, (Chronology)null);
   }

   public MutablePeriod(int var1, int var2, int var3, int var4) {
      super(0, 0, 0, 0, var1, var2, var3, var4, PeriodType.standard());
   }

   public MutablePeriod(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      super(var1, var2, var3, var4, var5, var6, var7, var8, PeriodType.standard());
   }

   public MutablePeriod(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, PeriodType var9) {
      super(var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public MutablePeriod(long var1) {
      super(var1);
   }

   public MutablePeriod(long var1, long var3) {
      super(var1, var3, (PeriodType)null, (Chronology)null);
   }

   public MutablePeriod(long var1, long var3, Chronology var5) {
      super(var1, var3, (PeriodType)null, var5);
   }

   public MutablePeriod(long var1, long var3, PeriodType var5) {
      super(var1, var3, var5, (Chronology)null);
   }

   public MutablePeriod(long var1, long var3, PeriodType var5, Chronology var6) {
      super(var1, var3, var5, var6);
   }

   public MutablePeriod(long var1, Chronology var3) {
      super(var1, (PeriodType)null, var3);
   }

   public MutablePeriod(long var1, PeriodType var3) {
      super(var1, var3, (Chronology)null);
   }

   public MutablePeriod(long var1, PeriodType var3, Chronology var4) {
      super(var1, var3, var4);
   }

   public MutablePeriod(Object var1) {
      super((Object)var1, (PeriodType)null, (Chronology)null);
   }

   public MutablePeriod(Object var1, Chronology var2) {
      super((Object)var1, (PeriodType)null, (Chronology)var2);
   }

   public MutablePeriod(Object var1, PeriodType var2) {
      super((Object)var1, (PeriodType)var2, (Chronology)null);
   }

   public MutablePeriod(Object var1, PeriodType var2, Chronology var3) {
      super(var1, var2, var3);
   }

   public MutablePeriod(PeriodType var1) {
      super(0L, var1, (Chronology)null);
   }

   public MutablePeriod(ReadableDuration var1, ReadableInstant var2) {
      super((ReadableDuration)var1, (ReadableInstant)var2, (PeriodType)null);
   }

   public MutablePeriod(ReadableDuration var1, ReadableInstant var2, PeriodType var3) {
      super(var1, var2, var3);
   }

   public MutablePeriod(ReadableInstant var1, ReadableDuration var2) {
      super((ReadableInstant)var1, (ReadableDuration)var2, (PeriodType)null);
   }

   public MutablePeriod(ReadableInstant var1, ReadableDuration var2, PeriodType var3) {
      super(var1, var2, var3);
   }

   public MutablePeriod(ReadableInstant var1, ReadableInstant var2) {
      super((ReadableInstant)var1, (ReadableInstant)var2, (PeriodType)null);
   }

   public MutablePeriod(ReadableInstant var1, ReadableInstant var2, PeriodType var3) {
      super(var1, var2, var3);
   }

   @FromString
   public static MutablePeriod parse(String var0) {
      return parse(var0, ISOPeriodFormat.standard());
   }

   public static MutablePeriod parse(String var0, PeriodFormatter var1) {
      return var1.parsePeriod(var0).toMutablePeriod();
   }

   public void add(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      this.setPeriod(FieldUtils.safeAdd(this.getYears(), var1), FieldUtils.safeAdd(this.getMonths(), var2), FieldUtils.safeAdd(this.getWeeks(), var3), FieldUtils.safeAdd(this.getDays(), var4), FieldUtils.safeAdd(this.getHours(), var5), FieldUtils.safeAdd(this.getMinutes(), var6), FieldUtils.safeAdd(this.getSeconds(), var7), FieldUtils.safeAdd(this.getMillis(), var8));
   }

   public void add(long var1) {
      this.add((ReadablePeriod)(new Period(var1, this.getPeriodType())));
   }

   public void add(long var1, Chronology var3) {
      this.add((ReadablePeriod)(new Period(var1, this.getPeriodType(), var3)));
   }

   public void add(DurationFieldType var1, int var2) {
      super.addField(var1, var2);
   }

   public void add(ReadableDuration var1) {
      if (var1 != null) {
         this.add((ReadablePeriod)(new Period(var1.getMillis(), this.getPeriodType())));
      }
   }

   public void add(ReadableInterval var1) {
      if (var1 != null) {
         this.add((ReadablePeriod)var1.toPeriod(this.getPeriodType()));
      }
   }

   public void add(ReadablePeriod var1) {
      super.addPeriod(var1);
   }

   public void addDays(int var1) {
      super.addField(DurationFieldType.days(), var1);
   }

   public void addHours(int var1) {
      super.addField(DurationFieldType.hours(), var1);
   }

   public void addMillis(int var1) {
      super.addField(DurationFieldType.millis(), var1);
   }

   public void addMinutes(int var1) {
      super.addField(DurationFieldType.minutes(), var1);
   }

   public void addMonths(int var1) {
      super.addField(DurationFieldType.months(), var1);
   }

   public void addSeconds(int var1) {
      super.addField(DurationFieldType.seconds(), var1);
   }

   public void addWeeks(int var1) {
      super.addField(DurationFieldType.weeks(), var1);
   }

   public void addYears(int var1) {
      super.addField(DurationFieldType.years(), var1);
   }

   public void clear() {
      super.setValues(new int[this.size()]);
   }

   public Object clone() {
      try {
         Object var1 = super.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new InternalError("Clone error");
      }
   }

   public MutablePeriod copy() {
      return (MutablePeriod)this.clone();
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

   public void mergePeriod(ReadablePeriod var1) {
      super.mergePeriod(var1);
   }

   public void set(DurationFieldType var1, int var2) {
      super.setField(var1, var2);
   }

   public void setDays(int var1) {
      super.setField(DurationFieldType.days(), var1);
   }

   public void setHours(int var1) {
      super.setField(DurationFieldType.hours(), var1);
   }

   public void setMillis(int var1) {
      super.setField(DurationFieldType.millis(), var1);
   }

   public void setMinutes(int var1) {
      super.setField(DurationFieldType.minutes(), var1);
   }

   public void setMonths(int var1) {
      super.setField(DurationFieldType.months(), var1);
   }

   public void setPeriod(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      super.setPeriod(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void setPeriod(long var1) {
      this.setPeriod(var1, (Chronology)null);
   }

   public void setPeriod(long var1, long var3) {
      this.setPeriod(var1, var3, (Chronology)null);
   }

   public void setPeriod(long var1, long var3, Chronology var5) {
      this.setValues(DateTimeUtils.getChronology(var5).get(this, var1, var3));
   }

   public void setPeriod(long var1, Chronology var3) {
      this.setValues(DateTimeUtils.getChronology(var3).get(this, var1));
   }

   public void setPeriod(ReadableDuration var1) {
      this.setPeriod((ReadableDuration)var1, (Chronology)null);
   }

   public void setPeriod(ReadableDuration var1, Chronology var2) {
      this.setPeriod(DateTimeUtils.getDurationMillis(var1), var2);
   }

   public void setPeriod(ReadableInstant var1, ReadableInstant var2) {
      if (var1 == var2) {
         this.setPeriod(0L);
      } else {
         this.setPeriod(DateTimeUtils.getInstantMillis(var1), DateTimeUtils.getInstantMillis(var2), DateTimeUtils.getIntervalChronology(var1, var2));
      }
   }

   public void setPeriod(ReadableInterval var1) {
      if (var1 == null) {
         this.setPeriod(0L);
      } else {
         Chronology var2 = DateTimeUtils.getChronology(var1.getChronology());
         this.setPeriod(var1.getStartMillis(), var1.getEndMillis(), var2);
      }
   }

   public void setPeriod(ReadablePeriod var1) {
      super.setPeriod(var1);
   }

   public void setSeconds(int var1) {
      super.setField(DurationFieldType.seconds(), var1);
   }

   public void setValue(int var1, int var2) {
      super.setValue(var1, var2);
   }

   public void setWeeks(int var1) {
      super.setField(DurationFieldType.weeks(), var1);
   }

   public void setYears(int var1) {
      super.setField(DurationFieldType.years(), var1);
   }
}
