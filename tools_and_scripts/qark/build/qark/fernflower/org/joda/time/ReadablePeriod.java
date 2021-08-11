package org.joda.time;

public interface ReadablePeriod {
   boolean equals(Object var1);

   int get(DurationFieldType var1);

   DurationFieldType getFieldType(int var1);

   PeriodType getPeriodType();

   int getValue(int var1);

   int hashCode();

   boolean isSupported(DurationFieldType var1);

   int size();

   MutablePeriod toMutablePeriod();

   Period toPeriod();

   String toString();
}
