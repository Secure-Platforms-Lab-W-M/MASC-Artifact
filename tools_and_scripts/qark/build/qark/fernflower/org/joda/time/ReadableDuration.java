package org.joda.time;

public interface ReadableDuration extends Comparable {
   boolean equals(Object var1);

   long getMillis();

   int hashCode();

   boolean isEqual(ReadableDuration var1);

   boolean isLongerThan(ReadableDuration var1);

   boolean isShorterThan(ReadableDuration var1);

   Duration toDuration();

   Period toPeriod();

   String toString();
}
