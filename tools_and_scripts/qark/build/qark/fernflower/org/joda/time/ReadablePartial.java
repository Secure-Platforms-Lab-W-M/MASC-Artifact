package org.joda.time;

public interface ReadablePartial extends Comparable {
   boolean equals(Object var1);

   int get(DateTimeFieldType var1);

   Chronology getChronology();

   DateTimeField getField(int var1);

   DateTimeFieldType getFieldType(int var1);

   int getValue(int var1);

   int hashCode();

   boolean isSupported(DateTimeFieldType var1);

   int size();

   DateTime toDateTime(ReadableInstant var1);

   String toString();
}
