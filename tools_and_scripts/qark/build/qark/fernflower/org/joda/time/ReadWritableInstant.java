package org.joda.time;

public interface ReadWritableInstant extends ReadableInstant {
   void add(long var1);

   void add(DurationFieldType var1, int var2);

   void add(ReadableDuration var1);

   void add(ReadableDuration var1, int var2);

   void add(ReadablePeriod var1);

   void add(ReadablePeriod var1, int var2);

   void set(DateTimeFieldType var1, int var2);

   void setChronology(Chronology var1);

   void setMillis(long var1);

   void setMillis(ReadableInstant var1);

   void setZone(DateTimeZone var1);

   void setZoneRetainFields(DateTimeZone var1);
}
