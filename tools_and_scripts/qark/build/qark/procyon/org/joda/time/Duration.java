// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import java.math.RoundingMode;
import org.joda.time.field.FieldUtils;
import org.joda.convert.FromString;
import java.io.Serializable;
import org.joda.time.base.BaseDuration;

public final class Duration extends BaseDuration implements ReadableDuration, Serializable
{
    public static final Duration ZERO;
    private static final long serialVersionUID = 2471658376918L;
    
    static {
        ZERO = new Duration(0L);
    }
    
    public Duration(final long n) {
        super(n);
    }
    
    public Duration(final long n, final long n2) {
        super(n, n2);
    }
    
    public Duration(final Object o) {
        super(o);
    }
    
    public Duration(final ReadableInstant readableInstant, final ReadableInstant readableInstant2) {
        super(readableInstant, readableInstant2);
    }
    
    public static Duration millis(final long n) {
        if (n == 0L) {
            return Duration.ZERO;
        }
        return new Duration(n);
    }
    
    @FromString
    public static Duration parse(final String s) {
        return new Duration(s);
    }
    
    public static Duration standardDays(final long n) {
        if (n == 0L) {
            return Duration.ZERO;
        }
        return new Duration(FieldUtils.safeMultiply(n, 86400000));
    }
    
    public static Duration standardHours(final long n) {
        if (n == 0L) {
            return Duration.ZERO;
        }
        return new Duration(FieldUtils.safeMultiply(n, 3600000));
    }
    
    public static Duration standardMinutes(final long n) {
        if (n == 0L) {
            return Duration.ZERO;
        }
        return new Duration(FieldUtils.safeMultiply(n, 60000));
    }
    
    public static Duration standardSeconds(final long n) {
        if (n == 0L) {
            return Duration.ZERO;
        }
        return new Duration(FieldUtils.safeMultiply(n, 1000));
    }
    
    public Duration dividedBy(final long n) {
        if (n == 1L) {
            return this;
        }
        return new Duration(FieldUtils.safeDivide(this.getMillis(), n));
    }
    
    public Duration dividedBy(final long n, final RoundingMode roundingMode) {
        if (n == 1L) {
            return this;
        }
        return new Duration(FieldUtils.safeDivide(this.getMillis(), n, roundingMode));
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
    
    public Duration minus(final long n) {
        return this.withDurationAdded(n, -1);
    }
    
    public Duration minus(final ReadableDuration readableDuration) {
        if (readableDuration == null) {
            return this;
        }
        return this.withDurationAdded(readableDuration.getMillis(), -1);
    }
    
    public Duration multipliedBy(final long n) {
        if (n == 1L) {
            return this;
        }
        return new Duration(FieldUtils.safeMultiply(this.getMillis(), n));
    }
    
    public Duration negated() {
        if (this.getMillis() != Long.MIN_VALUE) {
            return new Duration(-this.getMillis());
        }
        throw new ArithmeticException("Negation of this duration would overflow");
    }
    
    public Duration plus(final long n) {
        return this.withDurationAdded(n, 1);
    }
    
    public Duration plus(final ReadableDuration readableDuration) {
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
        return Days.days(FieldUtils.safeToInt(this.getStandardDays()));
    }
    
    public Hours toStandardHours() {
        return Hours.hours(FieldUtils.safeToInt(this.getStandardHours()));
    }
    
    public Minutes toStandardMinutes() {
        return Minutes.minutes(FieldUtils.safeToInt(this.getStandardMinutes()));
    }
    
    public Seconds toStandardSeconds() {
        return Seconds.seconds(FieldUtils.safeToInt(this.getStandardSeconds()));
    }
    
    public Duration withDurationAdded(long safeMultiply, final int n) {
        if (safeMultiply == 0L) {
            return this;
        }
        if (n == 0) {
            return this;
        }
        safeMultiply = FieldUtils.safeMultiply(safeMultiply, n);
        return new Duration(FieldUtils.safeAdd(this.getMillis(), safeMultiply));
    }
    
    public Duration withDurationAdded(final ReadableDuration readableDuration, final int n) {
        if (readableDuration == null) {
            return this;
        }
        if (n == 0) {
            return this;
        }
        return this.withDurationAdded(readableDuration.getMillis(), n);
    }
    
    public Duration withMillis(final long n) {
        if (n == this.getMillis()) {
            return this;
        }
        return new Duration(n);
    }
}
