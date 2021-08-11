package org.joda.time;

public interface ReadableInstant extends Comparable {
   boolean equals(Object var1);

   int get(DateTimeFieldType var1);

   Chronology getChronology();

   long getMillis();

   DateTimeZone getZone();

   int hashCode();

   boolean isAfter(ReadableInstant var1);

   boolean isBefore(ReadableInstant var1);

   boolean isEqual(ReadableInstant var1);

   boolean isSupported(DateTimeFieldType var1);

   Instant toInstant();

   String toString();
}
