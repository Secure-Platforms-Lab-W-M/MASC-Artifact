package org.joda.time.base;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;

public abstract class AbstractPartial implements ReadablePartial, Comparable {
   protected AbstractPartial() {
   }

   public int compareTo(ReadablePartial var1) {
      if (this == var1) {
         return 0;
      } else if (this.size() == var1.size()) {
         int var3 = this.size();

         int var2;
         for(var2 = 0; var2 < var3; ++var2) {
            if (this.getFieldType(var2) != var1.getFieldType(var2)) {
               throw new ClassCastException("ReadablePartial objects must have matching field types");
            }
         }

         var3 = this.size();

         for(var2 = 0; var2 < var3; ++var2) {
            if (this.getValue(var2) > var1.getValue(var2)) {
               return 1;
            }

            if (this.getValue(var2) < var1.getValue(var2)) {
               return -1;
            }
         }

         return 0;
      } else {
         throw new ClassCastException("ReadablePartial objects must have matching field types");
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ReadablePartial)) {
         return false;
      } else {
         ReadablePartial var4 = (ReadablePartial)var1;
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

            return FieldUtils.equals(this.getChronology(), var4.getChronology());
         }
      }
   }

   public int get(DateTimeFieldType var1) {
      return this.getValue(this.indexOfSupported(var1));
   }

   public DateTimeField getField(int var1) {
      return this.getField(var1, this.getChronology());
   }

   protected abstract DateTimeField getField(int var1, Chronology var2);

   public DateTimeFieldType getFieldType(int var1) {
      return this.getField(var1, this.getChronology()).getType();
   }

   public DateTimeFieldType[] getFieldTypes() {
      DateTimeFieldType[] var2 = new DateTimeFieldType[this.size()];

      for(int var1 = 0; var1 < var2.length; ++var1) {
         var2[var1] = this.getFieldType(var1);
      }

      return var2;
   }

   public DateTimeField[] getFields() {
      DateTimeField[] var2 = new DateTimeField[this.size()];

      for(int var1 = 0; var1 < var2.length; ++var1) {
         var2[var1] = this.getField(var1);
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
      int var2 = 157;

      for(int var1 = 0; var1 < var3; ++var1) {
         var2 = (var2 * 23 + this.getValue(var1)) * 23 + this.getFieldType(var1).hashCode();
      }

      return var2 + this.getChronology().hashCode();
   }

   public int indexOf(DateTimeFieldType var1) {
      int var3 = this.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         if (this.getFieldType(var2) == var1) {
            return var2;
         }
      }

      return -1;
   }

   protected int indexOf(DurationFieldType var1) {
      int var3 = this.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         if (this.getFieldType(var2).getDurationType() == var1) {
            return var2;
         }
      }

      return -1;
   }

   protected int indexOfSupported(DateTimeFieldType var1) {
      int var2 = this.indexOf(var1);
      if (var2 != -1) {
         return var2;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Field '");
         var3.append(var1);
         var3.append("' is not supported");
         throw new IllegalArgumentException(var3.toString());
      }
   }

   protected int indexOfSupported(DurationFieldType var1) {
      int var2 = this.indexOf(var1);
      if (var2 != -1) {
         return var2;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Field '");
         var3.append(var1);
         var3.append("' is not supported");
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public boolean isAfter(ReadablePartial var1) {
      if (var1 != null) {
         return this.compareTo(var1) > 0;
      } else {
         throw new IllegalArgumentException("Partial cannot be null");
      }
   }

   public boolean isBefore(ReadablePartial var1) {
      if (var1 != null) {
         return this.compareTo(var1) < 0;
      } else {
         throw new IllegalArgumentException("Partial cannot be null");
      }
   }

   public boolean isEqual(ReadablePartial var1) {
      if (var1 != null) {
         return this.compareTo(var1) == 0;
      } else {
         throw new IllegalArgumentException("Partial cannot be null");
      }
   }

   public boolean isSupported(DateTimeFieldType var1) {
      return this.indexOf(var1) != -1;
   }

   public DateTime toDateTime(ReadableInstant var1) {
      Chronology var2 = DateTimeUtils.getInstantChronology(var1);
      return new DateTime(var2.set(this, DateTimeUtils.getInstantMillis(var1)), var2);
   }

   public String toString(DateTimeFormatter var1) {
      return var1 == null ? this.toString() : var1.print(this);
   }
}
