/*
 * Decompiled with CFR 0_124.
 */
package org.joda.time;

import org.joda.time.Duration;
import org.joda.time.Period;

public interface ReadableDuration
extends Comparable<ReadableDuration> {
    public boolean equals(Object var1);

    public long getMillis();

    public int hashCode();

    public boolean isEqual(ReadableDuration var1);

    public boolean isLongerThan(ReadableDuration var1);

    public boolean isShorterThan(ReadableDuration var1);

    public Duration toDuration();

    public Period toPeriod();

    public String toString();
}

