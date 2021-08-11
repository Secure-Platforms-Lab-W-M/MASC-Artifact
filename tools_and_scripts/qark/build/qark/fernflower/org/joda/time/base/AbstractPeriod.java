package org.joda.time.base;

import org.joda.convert.ToString;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public abstract class AbstractPeriod implements ReadablePeriod {
   protected AbstractPeriod() {
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ReadablePeriod)) {
         return false;
      } else {
         ReadablePeriod var4 = (ReadablePeriod)var1;
         if (this.size() != var4.size()) {
            return false;
         } else {
            int var3 = this.size();

            for(int var2 = 0; var2 < var3; ++var2) {
               if (this.getValue(var2) != var4.getValue(var2)) {
                  return false;
               }

               if (this.getFieldType(var2) != var4.getFieldType(var2)) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public int get(DurationFieldType var1) {
      int var2 = this.indexOf(var1);
      return var2 == -1 ? 0 : this.getValue(var2);
   }

   public DurationFieldType getFieldType(int var1) {
      return this.getPeriodType().getFieldType(var1);
   }

   public DurationFieldType[] getFieldTypes() {
      DurationFieldType[] var2 = new DurationFieldType[this.size()];

      for(int var1 = 0; var1 < var2.length; ++var1) {
         var2[var1] = this.getFieldType(var1);
      }

      return var2;
   }

   public int[] getValues() {
      int[] var2 = new int[this.size()];

      for(int var1 = 0; var1 < var2.length; ++var1) {
         var2[var1] = this.getValue(var1);
      }

      return var2;
   }

   public int hashCode() {
      int var3 = this.size();
      int var2 = 17;

      for(int var1 = 0; var1 < var3; ++var1) {
         var2 = (var2 * 27 + this.getValue(var1)) * 27 + this.getFieldType(var1).hashCode();
      }

      return var2;
   }

   public int indexOf(DurationFieldType var1) {
      return this.getPeriodType().indexOf(var1);
   }

   public boolean isSupported(DurationFieldType var1) {
      return this.getPeriodType().isSupported(var1);
   }

   public int size() {
      return this.getPeriodType().size();
   }

   public MutablePeriod toMutablePeriod() {
      return new MutablePeriod(this);
   }

   public Period toPeriod() {
      return new Period(this);
   }

   @ToString
   public String toString() {
      return ISOPeriodFormat.standard().print(this);
   }

   public String toString(PeriodFormatter var1) {
      return var1 == null ? this.toString() : var1.print(this);
   }
}
