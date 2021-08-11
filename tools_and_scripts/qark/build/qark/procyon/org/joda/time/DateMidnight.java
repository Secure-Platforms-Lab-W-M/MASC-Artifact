// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import org.joda.time.format.DateTimeFormatter;
import org.joda.convert.FromString;
import org.joda.time.format.ISODateTimeFormat;
import java.io.Serializable;
import org.joda.time.base.BaseDateTime;

@Deprecated
public final class DateMidnight extends BaseDateTime implements ReadableDateTime, Serializable
{
    private static final long serialVersionUID = 156371964018738L;
    
    public DateMidnight() {
    }
    
    public DateMidnight(final int n, final int n2, final int n3) {
        super(n, n2, n3, 0, 0, 0, 0);
    }
    
    public DateMidnight(final int n, final int n2, final int n3, final Chronology chronology) {
        super(n, n2, n3, 0, 0, 0, 0, chronology);
    }
    
    public DateMidnight(final int n, final int n2, final int n3, final DateTimeZone dateTimeZone) {
        super(n, n2, n3, 0, 0, 0, 0, dateTimeZone);
    }
    
    public DateMidnight(final long n) {
        super(n);
    }
    
    public DateMidnight(final long n, final Chronology chronology) {
        super(n, chronology);
    }
    
    public DateMidnight(final long n, final DateTimeZone dateTimeZone) {
        super(n, dateTimeZone);
    }
    
    public DateMidnight(final Object o) {
        super(o, (Chronology)null);
    }
    
    public DateMidnight(final Object o, final Chronology chronology) {
        super(o, DateTimeUtils.getChronology(chronology));
    }
    
    public DateMidnight(final Object o, final DateTimeZone dateTimeZone) {
        super(o, dateTimeZone);
    }
    
    public DateMidnight(final Chronology chronology) {
        super(chronology);
    }
    
    public DateMidnight(final DateTimeZone dateTimeZone) {
        super(dateTimeZone);
    }
    
    public static DateMidnight now() {
        return new DateMidnight();
    }
    
    public static DateMidnight now(final Chronology chronology) {
        if (chronology != null) {
            return new DateMidnight(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }
    
    public static DateMidnight now(final DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new DateMidnight(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }
    
    @FromString
    public static DateMidnight parse(final String s) {
        return parse(s, ISODateTimeFormat.dateTimeParser().withOffsetParsed());
    }
    
    public static DateMidnight parse(final String s, final DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.parseDateTime(s).toDateMidnight();
    }
    
    public DateMidnight.DateMidnight$Property centuryOfEra() {
        return new DateMidnight.DateMidnight$Property(this, this.getChronology().centuryOfEra());
    }
    
    @Override
    protected long checkInstant(final long n, final Chronology chronology) {
        return chronology.dayOfMonth().roundFloor(n);
    }
    
    public DateMidnight.DateMidnight$Property dayOfMonth() {
        return new DateMidnight.DateMidnight$Property(this, this.getChronology().dayOfMonth());
    }
    
    public DateMidnight.DateMidnight$Property dayOfWeek() {
        return new DateMidnight.DateMidnight$Property(this, this.getChronology().dayOfWeek());
    }
    
    public DateMidnight.DateMidnight$Property dayOfYear() {
        return new DateMidnight.DateMidnight$Property(this, this.getChronology().dayOfYear());
    }
    
    public DateMidnight.DateMidnight$Property era() {
        return new DateMidnight.DateMidnight$Property(this, this.getChronology().era());
    }
    
    public DateMidnight minus(final long n) {
        return this.withDurationAdded(n, -1);
    }
    
    public DateMidnight minus(final ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, -1);
    }
    
    public DateMidnight minus(final ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, -1);
    }
    
    public DateMidnight minusDays(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().days().subtract(this.getMillis(), n));
    }
    
    public DateMidnight minusMonths(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().months().subtract(this.getMillis(), n));
    }
    
    public DateMidnight minusWeeks(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().weeks().subtract(this.getMillis(), n));
    }
    
    public DateMidnight minusYears(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().years().subtract(this.getMillis(), n));
    }
    
    public DateMidnight.DateMidnight$Property monthOfYear() {
        return new DateMidnight.DateMidnight$Property(this, this.getChronology().monthOfYear());
    }
    
    public DateMidnight plus(final long n) {
        return this.withDurationAdded(n, 1);
    }
    
    public DateMidnight plus(final ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, 1);
    }
    
    public DateMidnight plus(final ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, 1);
    }
    
    public DateMidnight plusDays(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().days().add(this.getMillis(), n));
    }
    
    public DateMidnight plusMonths(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().months().add(this.getMillis(), n));
    }
    
    public DateMidnight plusWeeks(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().weeks().add(this.getMillis(), n));
    }
    
    public DateMidnight plusYears(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().years().add(this.getMillis(), n));
    }
    
    public DateMidnight.DateMidnight$Property property(final DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType == null) {
            throw new IllegalArgumentException("The DateTimeFieldType must not be null");
        }
        final DateTimeField field = dateTimeFieldType.getField(this.getChronology());
        if (field.isSupported()) {
            return new DateMidnight.DateMidnight$Property(this, field);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Field '");
        sb.append(dateTimeFieldType);
        sb.append("' is not supported");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public Interval toInterval() {
        final Chronology chronology = this.getChronology();
        final long millis = this.getMillis();
        return new Interval(millis, DurationFieldType.days().getField(chronology).add(millis, 1), chronology);
    }
    
    public LocalDate toLocalDate() {
        return new LocalDate(this.getMillis(), this.getChronology());
    }
    
    @Deprecated
    public YearMonthDay toYearMonthDay() {
        return new YearMonthDay(this.getMillis(), this.getChronology());
    }
    
    public DateMidnight.DateMidnight$Property weekOfWeekyear() {
        return new DateMidnight.DateMidnight$Property(this, this.getChronology().weekOfWeekyear());
    }
    
    public DateMidnight.DateMidnight$Property weekyear() {
        return new DateMidnight.DateMidnight$Property(this, this.getChronology().weekyear());
    }
    
    public DateMidnight withCenturyOfEra(final int n) {
        return this.withMillis(this.getChronology().centuryOfEra().set(this.getMillis(), n));
    }
    
    public DateMidnight withChronology(final Chronology chronology) {
        if (chronology == this.getChronology()) {
            return this;
        }
        return new DateMidnight(this.getMillis(), chronology);
    }
    
    public DateMidnight withDayOfMonth(final int n) {
        return this.withMillis(this.getChronology().dayOfMonth().set(this.getMillis(), n));
    }
    
    public DateMidnight withDayOfWeek(final int n) {
        return this.withMillis(this.getChronology().dayOfWeek().set(this.getMillis(), n));
    }
    
    public DateMidnight withDayOfYear(final int n) {
        return this.withMillis(this.getChronology().dayOfYear().set(this.getMillis(), n));
    }
    
    public DateMidnight withDurationAdded(final long n, final int n2) {
        if (n == 0L) {
            return this;
        }
        if (n2 == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().add(this.getMillis(), n, n2));
    }
    
    public DateMidnight withDurationAdded(final ReadableDuration readableDuration, final int n) {
        if (readableDuration == null) {
            return this;
        }
        if (n == 0) {
            return this;
        }
        return this.withDurationAdded(readableDuration.getMillis(), n);
    }
    
    public DateMidnight withEra(final int n) {
        return this.withMillis(this.getChronology().era().set(this.getMillis(), n));
    }
    
    public DateMidnight withField(final DateTimeFieldType dateTimeFieldType, final int n) {
        if (dateTimeFieldType != null) {
            return this.withMillis(dateTimeFieldType.getField(this.getChronology()).set(this.getMillis(), n));
        }
        throw new IllegalArgumentException("Field must not be null");
    }
    
    public DateMidnight withFieldAdded(final DurationFieldType durationFieldType, final int n) {
        if (durationFieldType == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        if (n == 0) {
            return this;
        }
        return this.withMillis(durationFieldType.getField(this.getChronology()).add(this.getMillis(), n));
    }
    
    public DateMidnight withFields(final ReadablePartial readablePartial) {
        if (readablePartial == null) {
            return this;
        }
        return this.withMillis(this.getChronology().set(readablePartial, this.getMillis()));
    }
    
    public DateMidnight withMillis(long checkInstant) {
        final Chronology chronology = this.getChronology();
        checkInstant = this.checkInstant(checkInstant, chronology);
        if (checkInstant == this.getMillis()) {
            return this;
        }
        return new DateMidnight(checkInstant, chronology);
    }
    
    public DateMidnight withMonthOfYear(final int n) {
        return this.withMillis(this.getChronology().monthOfYear().set(this.getMillis(), n));
    }
    
    public DateMidnight withPeriodAdded(final ReadablePeriod readablePeriod, final int n) {
        if (readablePeriod == null) {
            return this;
        }
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().add(readablePeriod, this.getMillis(), n));
    }
    
    public DateMidnight withWeekOfWeekyear(final int n) {
        return this.withMillis(this.getChronology().weekOfWeekyear().set(this.getMillis(), n));
    }
    
    public DateMidnight withWeekyear(final int n) {
        return this.withMillis(this.getChronology().weekyear().set(this.getMillis(), n));
    }
    
    public DateMidnight withYear(final int n) {
        return this.withMillis(this.getChronology().year().set(this.getMillis(), n));
    }
    
    public DateMidnight withYearOfCentury(final int n) {
        return this.withMillis(this.getChronology().yearOfCentury().set(this.getMillis(), n));
    }
    
    public DateMidnight withYearOfEra(final int n) {
        return this.withMillis(this.getChronology().yearOfEra().set(this.getMillis(), n));
    }
    
    public DateMidnight withZoneRetainFields(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        final DateTimeZone zone2 = DateTimeUtils.getZone(this.getZone());
        if (zone == zone2) {
            return this;
        }
        return new DateMidnight(zone2.getMillisKeepLocal(zone, this.getMillis()), this.getChronology().withZone(zone));
    }
    
    public DateMidnight.DateMidnight$Property year() {
        return new DateMidnight.DateMidnight$Property(this, this.getChronology().year());
    }
    
    public DateMidnight.DateMidnight$Property yearOfCentury() {
        return new DateMidnight.DateMidnight$Property(this, this.getChronology().yearOfCentury());
    }
    
    public DateMidnight.DateMidnight$Property yearOfEra() {
        return new DateMidnight.DateMidnight$Property(this, this.getChronology().yearOfEra());
    }
}
