package org.joda.time.base;

import java.io.Serializable;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

public abstract class BaseSingleFieldPeriod implements ReadablePeriod, Comparable, Serializable {
   private static final long START_1972 = 63072000000L;
   private static final long serialVersionUID = 9386874258972L;
   private volatile int iPeriod;

   protected BaseSingleFieldPeriod(int var1) {
      this.iPeriod = var1;
   }

   protected static int between(ReadableInstant var0, ReadableInstant var1, DurationFieldType var2) {
      if (var0 != null && var1 != null) {
         return var2.getField(DateTimeUtils.getInstantChronology(var0)).getDifference(var1.getMillis(), var0.getMillis());
      } else {
         throw new IllegalArgumentException("ReadableInstant objects must not be null");
      }
   }

   protected static int between(ReadablePartial var0, ReadablePartial var1, ReadablePeriod var2) {
      if (var0 != null && var1 != null) {
         if (var0.size() == var1.size()) {
            int var4 = var0.size();

            for(int var3 = 0; var3 < var4; ++var3) {
               if (var0.getFieldType(var3) != var1.getFieldType(var3)) {
                  throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
               }
            }

            if (DateTimeUtils.isContiguous(var0)) {
               Chronology var5 = DateTimeUtils.getChronology(var0.getChronology()).withUTC();
               return var5.get(var2, var5.set(var0, 63072000000L), var5.set(var1, 63072000000L))[0];
            } else {
               throw new IllegalArgumentException("ReadablePartial objects must be contiguous");
            }
         } else {
            throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
         }
      } else {
         throw new IllegalArgumentException("ReadablePartial objects must not be null");
      }
   }

   protected static int standardPeriodIn(ReadablePeriod var0, long var1) {
      int var3 = 0;
      if (var0 == null) {
         return 0;
      } else {
         ISOChronology var8 = ISOChronology.getInstanceUTC();

         long var5;
         for(var5 = 0L; var3 < var0.size(); ++var3) {
            int var4 = var0.getValue(var3);
            if (var4 != 0) {
               DurationField var7 = var0.getFieldType(var3).getField(var8);
               if (!var7.isPrecise()) {
                  StringBuilder var9 = new StringBuilder();
                  var9.append("Cannot convert period to duration as ");
                  var9.append(var7.getName());
                  var9.append(" is not precise in the period ");
                  var9.append(var0);
                  throw new IllegalArgumentException(var9.toString());
               }

               var5 = FieldUtils.safeAdd(var5, FieldUtils.safeMultiply(var7.getUnitMillis(), var4));
            }
         }

         return FieldUtils.safeToInt(var5 / var1);
      }
   }

   public int compareTo(BaseSingleFieldPeriod var1) {
      if (var1.getClass() == this.getClass()) {
         int var2 = var1.getValue();
         int var3 = this.getValue();
         if (var3 > var2) {
            return 1;
         } else {
            return var3 < var2 ? -1 : 0;
         }
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append(this.getClass());
         var4.append(" cannot be compared to ");
         var4.append(var1.getClass());
         throw new ClassCastException(var4.toString());
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ReadablePeriod)) {
         return false;
      } else {
         ReadablePeriod var2 = (ReadablePeriod)var1;
         return var2.getPeriodType() == this.getPeriodType() && var2.getValue(0) == this.getValue();
      }
   }

   public int get(DurationFieldType var1) {
      return var1 == this.getFieldType() ? this.getValue() : 0;
   }

   public abstract DurationFieldType getFieldType();

   public DurationFieldType getFieldType(int var1) {
      if (var1 == 0) {
         return this.getFieldType();
      } else {
         throw new IndexOutOfBoundsException(String.valueOf(var1));
      }
   }

   public abstract PeriodType getPeriodType();

   protected int getValue() {
      return this.iPeriod;
   }

   public int getValue(int var1) {
      if (var1 == 0) {
         return this.getValue();
      } else {
         throw new IndexOutOfBoundsException(String.valueOf(var1));
      }
   }

   public int hashCode() {
      return (459 + this.getValue()) * 27 + this.getFieldType().hashCode();
   }

   public boolean isSupported(DurationFieldType var1) {
      return var1 == this.getFieldType();
   }

   protected void setValue(int var1) {
      this.iPeriod = var1;
   }

   public int size() {
      return 1;
   }

   public MutablePeriod toMutablePeriod() {
      MutablePeriod var1 = new MutablePeriod();
      var1.add((ReadablePeriod)this);
      return var1;
   }

   public Period toPeriod() {
      return Period.ZERO.withFields(this);
   }
}
