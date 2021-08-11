// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time.base;

import org.joda.convert.ToString;
import org.joda.time.format.FormatUtils;
import org.joda.time.Period;
import org.joda.time.Duration;
import org.joda.time.ReadableDuration;

public abstract class AbstractDuration implements ReadableDuration
{
    protected AbstractDuration() {
    }
    
    @Override
    public int compareTo(final ReadableDuration readableDuration) {
        final long millis = this.getMillis();
        final long millis2 = readableDuration.getMillis();
        if (millis < millis2) {
            return -1;
        }
        if (millis > millis2) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof ReadableDuration && this.getMillis() == ((ReadableDuration)o).getMillis());
    }
    
    @Override
    public int hashCode() {
        final long millis = this.getMillis();
        return (int)(millis ^ millis >>> 32);
    }
    
    @Override
    public boolean isEqual(ReadableDuration zero) {
        if (zero == null) {
            zero = Duration.ZERO;
        }
        return this.compareTo(zero) == 0;
    }
    
    @Override
    public boolean isLongerThan(ReadableDuration zero) {
        if (zero == null) {
            zero = Duration.ZERO;
        }
        return this.compareTo(zero) > 0;
    }
    
    @Override
    public boolean isShorterThan(ReadableDuration zero) {
        if (zero == null) {
            zero = Duration.ZERO;
        }
        return this.compareTo(zero) < 0;
    }
    
    @Override
    public Duration toDuration() {
        return new Duration(this.getMillis());
    }
    
    @Override
    public Period toPeriod() {
        return new Period(this.getMillis());
    }
    
    @ToString
    @Override
    public String toString() {
        final long millis = this.getMillis();
        final StringBuffer sb = new StringBuffer();
        sb.append("PT");
        final boolean b = millis < 0L;
        FormatUtils.appendUnpaddedInteger(sb, millis);
        while (true) {
            final int length = sb.length();
            int n;
            if (b) {
                n = 7;
            }
            else {
                n = 6;
            }
            final int n2 = 3;
            if (length >= n) {
                break;
            }
            int n3;
            if (b) {
                n3 = n2;
            }
            else {
                n3 = 2;
            }
            sb.insert(n3, "0");
        }
        if (millis / 1000L * 1000L == millis) {
            sb.setLength(sb.length() - 3);
        }
        else {
            sb.insert(sb.length() - 3, ".");
        }
        sb.append('S');
        return sb.toString();
    }
}
