/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.ToString
 *  org.joda.time.format.FormatUtils
 */
package org.joda.time.base;

import org.joda.convert.ToString;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.ReadableDuration;
import org.joda.time.format.FormatUtils;

public abstract class AbstractDuration
implements ReadableDuration {
    protected AbstractDuration() {
    }

    @Override
    public int compareTo(ReadableDuration readableDuration) {
        long l;
        long l2 = this.getMillis();
        if (l2 < (l = readableDuration.getMillis())) {
            return -1;
        }
        if (l2 > l) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ReadableDuration)) {
            return false;
        }
        object = (ReadableDuration)object;
        if (this.getMillis() == object.getMillis()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        long l = this.getMillis();
        return (int)(l ^ l >>> 32);
    }

    @Override
    public boolean isEqual(ReadableDuration readableDuration) {
        if (readableDuration == null) {
            readableDuration = Duration.ZERO;
        }
        if (this.compareTo(readableDuration) == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isLongerThan(ReadableDuration readableDuration) {
        if (readableDuration == null) {
            readableDuration = Duration.ZERO;
        }
        if (this.compareTo(readableDuration) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isShorterThan(ReadableDuration readableDuration) {
        if (readableDuration == null) {
            readableDuration = Duration.ZERO;
        }
        if (this.compareTo(readableDuration) < 0) {
            return true;
        }
        return false;
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
        long l = this.getMillis();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("PT");
        boolean bl = l < 0L;
        FormatUtils.appendUnpaddedInteger((StringBuffer)stringBuffer, (long)l);
        do {
            int n = stringBuffer.length();
            int n2 = bl ? 7 : 6;
            int n3 = 3;
            if (n >= n2) break;
            n2 = bl ? n3 : 2;
            stringBuffer.insert(n2, "0");
        } while (true);
        if (l / 1000L * 1000L == l) {
            stringBuffer.setLength(stringBuffer.length() - 3);
        } else {
            stringBuffer.insert(stringBuffer.length() - 3, ".");
        }
        stringBuffer.append('S');
        return stringBuffer.toString();
    }
}

