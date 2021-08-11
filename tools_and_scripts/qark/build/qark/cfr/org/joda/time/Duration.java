/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.FromString
 *  org.joda.time.field.FieldUtils
 */
package org.joda.time;

import java.io.Serializable;
import java.math.RoundingMode;
import org.joda.convert.FromString;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.Seconds;
import org.joda.time.base.BaseDuration;
import org.joda.time.field.FieldUtils;

public final class Duration
extends BaseDuration
implements ReadableDuration,
Serializable {
    public static final Duration ZERO = new Duration(0L);
    private static final long serialVersionUID = 2471658376918L;

    public Duration(long l) {
        super(l);
    }

    public Duration(long l, long l2) {
        super(l, l2);
    }

    public Duration(Object object) {
        super(object);
    }

    public Duration(ReadableInstant readableInstant, ReadableInstant readableInstant2) {
        super(readableInstant, readableInstant2);
    }

    public static Duration millis(long l) {
        if (l == 0L) {
            return ZERO;
        }
        return new Duration(l);
    }

    @FromString
    public static Duration parse(String string) {
        return new Duration(string);
    }

    public static Duration standardDays(long l) {
        if (l == 0L) {
            return ZERO;
        }
        return new Duration(FieldUtils.safeMultiply((long)l, (int)86400000));
    }

    public static Duration standardHours(long l) {
        if (l == 0L) {
            return ZERO;
        }
        return new Duration(FieldUtils.safeMultiply((long)l, (int)3600000));
    }

    public static Duration standardMinutes(long l) {
        if (l == 0L) {
            return ZERO;
        }
        return new Duration(FieldUtils.safeMultiply((long)l, (int)60000));
    }

    public static Duration standardSeconds(long l) {
        if (l == 0L) {
            return ZERO;
        }
        return new Duration(FieldUtils.safeMultiply((long)l, (int)1000));
    }

    public Duration dividedBy(long l) {
        if (l == 1L) {
            return this;
        }
        return new Duration(FieldUtils.safeDivide((long)this.getMillis(), (long)l));
    }

    public Duration dividedBy(long l, RoundingMode roundingMode) {
        if (l == 1L) {
            return this;
        }
        return new Duration(FieldUtils.safeDivide((long)this.getMillis(), (long)l, (RoundingMode)roundingMode));
    }

    public long getStandardDays() {
        return this.getMillis() / 86400000L;
    }

    public long getStandardHours() {
        return this.getMillis() / 3600000L;
    }

    public long getStandardMinutes() {
        return this.getMillis() / 60000L;
    }

    public long getStandardSeconds() {
        return this.getMillis() / 1000L;
    }

    public Duration minus(long l) {
        return this.withDurationAdded(l, -1);
    }

    public Duration minus(ReadableDuration readableDuration) {
        if (readableDuration == null) {
            return this;
        }
        return this.withDurationAdded(readableDuration.getMillis(), -1);
    }

    public Duration multipliedBy(long l) {
        if (l == 1L) {
            return this;
        }
        return new Duration(FieldUtils.safeMultiply((long)this.getMillis(), (long)l));
    }

    public Duration negated() {
        if (this.getMillis() != Long.MIN_VALUE) {
            return new Duration(- this.getMillis());
        }
        throw new ArithmeticException("Negation of this duration would overflow");
    }

    public Duration plus(long l) {
        return this.withDurationAdded(l, 1);
    }

    public Duration plus(ReadableDuration readableDuration) {
        if (readableDuration == null) {
            return this;
        }
        return this.withDurationAdded(readableDuration.getMillis(), 1);
    }

    @Override
    public Duration toDuration() {
        return this;
    }

    public Days toStandardDays() {
        return Days.days(FieldUtils.safeToInt((long)this.getStandardDays()));
    }

    public Hours toStandardHours() {
        return Hours.hours(FieldUtils.safeToInt((long)this.getStandardHours()));
    }

    public Minutes toStandardMinutes() {
        return Minutes.minutes(FieldUtils.safeToInt((long)this.getStandardMinutes()));
    }

    public Seconds toStandardSeconds() {
        return Seconds.seconds(FieldUtils.safeToInt((long)this.getStandardSeconds()));
    }

    public Duration withDurationAdded(long l, int n) {
        if (l != 0L) {
            if (n == 0) {
                return this;
            }
            l = FieldUtils.safeMultiply((long)l, (int)n);
            return new Duration(FieldUtils.safeAdd((long)this.getMillis(), (long)l));
        }
        return this;
    }

    public Duration withDurationAdded(ReadableDuration readableDuration, int n) {
        if (readableDuration != null) {
            if (n == 0) {
                return this;
            }
            return this.withDurationAdded(readableDuration.getMillis(), n);
        }
        return this;
    }

    public Duration withMillis(long l) {
        if (l == this.getMillis()) {
            return this;
        }
        return new Duration(l);
    }
}

