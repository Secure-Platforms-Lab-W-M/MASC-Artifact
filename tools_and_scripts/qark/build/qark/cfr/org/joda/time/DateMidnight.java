/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.FromString
 *  org.joda.time.Chronology
 *  org.joda.time.DateMidnight$Property
 *  org.joda.time.DateTimeField
 *  org.joda.time.DateTimeFieldType
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.DurationField
 *  org.joda.time.DurationFieldType
 *  org.joda.time.Interval
 *  org.joda.time.YearMonthDay
 *  org.joda.time.format.DateTimeFormatter
 *  org.joda.time.format.ISODateTimeFormat
 */
package org.joda.time;

import java.io.Serializable;
import org.joda.convert.FromString;
import org.joda.time.Chronology;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.YearMonthDay;
import org.joda.time.base.BaseDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

@Deprecated
public final class DateMidnight
extends BaseDateTime
implements ReadableDateTime,
Serializable {
    private static final long serialVersionUID = 156371964018738L;

    public DateMidnight() {
    }

    public DateMidnight(int n, int n2, int n3) {
        super(n, n2, n3, 0, 0, 0, 0);
    }

    public DateMidnight(int n, int n2, int n3, Chronology chronology) {
        super(n, n2, n3, 0, 0, 0, 0, chronology);
    }

    public DateMidnight(int n, int n2, int n3, DateTimeZone dateTimeZone) {
        super(n, n2, n3, 0, 0, 0, 0, dateTimeZone);
    }

    public DateMidnight(long l) {
        super(l);
    }

    public DateMidnight(long l, Chronology chronology) {
        super(l, chronology);
    }

    public DateMidnight(long l, DateTimeZone dateTimeZone) {
        super(l, dateTimeZone);
    }

    public DateMidnight(Object object) {
        super(object, (Chronology)null);
    }

    public DateMidnight(Object object, Chronology chronology) {
        super(object, DateTimeUtils.getChronology((Chronology)chronology));
    }

    public DateMidnight(Object object, DateTimeZone dateTimeZone) {
        super(object, dateTimeZone);
    }

    public DateMidnight(Chronology chronology) {
        super(chronology);
    }

    public DateMidnight(DateTimeZone dateTimeZone) {
        super(dateTimeZone);
    }

    public static DateMidnight now() {
        return new DateMidnight();
    }

    public static DateMidnight now(Chronology chronology) {
        if (chronology != null) {
            return new DateMidnight(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }

    public static DateMidnight now(DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new DateMidnight(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }

    @FromString
    public static DateMidnight parse(String string) {
        return DateMidnight.parse(string, ISODateTimeFormat.dateTimeParser().withOffsetParsed());
    }

    public static DateMidnight parse(String string, DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.parseDateTime(string).toDateMidnight();
    }

    public  centuryOfEra() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    @Override
    protected long checkInstant(long l, Chronology chronology) {
        return chronology.dayOfMonth().roundFloor(l);
    }

    public  dayOfMonth() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  dayOfWeek() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  dayOfYear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  era() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public DateMidnight minus(long l) {
        return this.withDurationAdded(l, -1);
    }

    public DateMidnight minus(ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, -1);
    }

    public DateMidnight minus(ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, -1);
    }

    public DateMidnight minusDays(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().days().subtract(this.getMillis(), n));
    }

    public DateMidnight minusMonths(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().months().subtract(this.getMillis(), n));
    }

    public DateMidnight minusWeeks(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().weeks().subtract(this.getMillis(), n));
    }

    public DateMidnight minusYears(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().years().subtract(this.getMillis(), n));
    }

    public  monthOfYear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public DateMidnight plus(long l) {
        return this.withDurationAdded(l, 1);
    }

    public DateMidnight plus(ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, 1);
    }

    public DateMidnight plus(ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, 1);
    }

    public DateMidnight plusDays(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().days().add(this.getMillis(), n));
    }

    public DateMidnight plusMonths(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().months().add(this.getMillis(), n));
    }

    public DateMidnight plusWeeks(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().weeks().add(this.getMillis(), n));
    }

    public DateMidnight plusYears(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().years().add(this.getMillis(), n));
    }

    public  property(DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType != null) {
            Object object = dateTimeFieldType.getField(this.getChronology());
            if (object.isSupported()) {
                return new /* Unavailable Anonymous Inner Class!! */;
            }
            object = new StringBuilder();
            object.append("Field '");
            object.append((Object)dateTimeFieldType);
            object.append("' is not supported");
            throw new IllegalArgumentException(object.toString());
        }
        throw new IllegalArgumentException("The DateTimeFieldType must not be null");
    }

    public Interval toInterval() {
        Chronology chronology = this.getChronology();
        long l = this.getMillis();
        return new Interval(l, DurationFieldType.days().getField(chronology).add(l, 1), chronology);
    }

    public LocalDate toLocalDate() {
        return new LocalDate(this.getMillis(), this.getChronology());
    }

    @Deprecated
    public YearMonthDay toYearMonthDay() {
        return new YearMonthDay(this.getMillis(), this.getChronology());
    }

    public  weekOfWeekyear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  weekyear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public DateMidnight withCenturyOfEra(int n) {
        return this.withMillis(this.getChronology().centuryOfEra().set(this.getMillis(), n));
    }

    public DateMidnight withChronology(Chronology chronology) {
        if (chronology == this.getChronology()) {
            return this;
        }
        return new DateMidnight(this.getMillis(), chronology);
    }

    public DateMidnight withDayOfMonth(int n) {
        return this.withMillis(this.getChronology().dayOfMonth().set(this.getMillis(), n));
    }

    public DateMidnight withDayOfWeek(int n) {
        return this.withMillis(this.getChronology().dayOfWeek().set(this.getMillis(), n));
    }

    public DateMidnight withDayOfYear(int n) {
        return this.withMillis(this.getChronology().dayOfYear().set(this.getMillis(), n));
    }

    public DateMidnight withDurationAdded(long l, int n) {
        if (l != 0L) {
            if (n == 0) {
                return this;
            }
            return this.withMillis(this.getChronology().add(this.getMillis(), l, n));
        }
        return this;
    }

    public DateMidnight withDurationAdded(ReadableDuration readableDuration, int n) {
        if (readableDuration != null) {
            if (n == 0) {
                return this;
            }
            return this.withDurationAdded(readableDuration.getMillis(), n);
        }
        return this;
    }

    public DateMidnight withEra(int n) {
        return this.withMillis(this.getChronology().era().set(this.getMillis(), n));
    }

    public DateMidnight withField(DateTimeFieldType dateTimeFieldType, int n) {
        if (dateTimeFieldType != null) {
            return this.withMillis(dateTimeFieldType.getField(this.getChronology()).set(this.getMillis(), n));
        }
        throw new IllegalArgumentException("Field must not be null");
    }

    public DateMidnight withFieldAdded(DurationFieldType durationFieldType, int n) {
        if (durationFieldType != null) {
            if (n == 0) {
                return this;
            }
            return this.withMillis(durationFieldType.getField(this.getChronology()).add(this.getMillis(), n));
        }
        throw new IllegalArgumentException("Field must not be null");
    }

    public DateMidnight withFields(ReadablePartial readablePartial) {
        if (readablePartial == null) {
            return this;
        }
        return this.withMillis(this.getChronology().set(readablePartial, this.getMillis()));
    }

    public DateMidnight withMillis(long l) {
        Chronology chronology = this.getChronology();
        if ((l = this.checkInstant(l, chronology)) == this.getMillis()) {
            return this;
        }
        return new DateMidnight(l, chronology);
    }

    public DateMidnight withMonthOfYear(int n) {
        return this.withMillis(this.getChronology().monthOfYear().set(this.getMillis(), n));
    }

    public DateMidnight withPeriodAdded(ReadablePeriod readablePeriod, int n) {
        if (readablePeriod != null) {
            if (n == 0) {
                return this;
            }
            return this.withMillis(this.getChronology().add(readablePeriod, this.getMillis(), n));
        }
        return this;
    }

    public DateMidnight withWeekOfWeekyear(int n) {
        return this.withMillis(this.getChronology().weekOfWeekyear().set(this.getMillis(), n));
    }

    public DateMidnight withWeekyear(int n) {
        return this.withMillis(this.getChronology().weekyear().set(this.getMillis(), n));
    }

    public DateMidnight withYear(int n) {
        return this.withMillis(this.getChronology().year().set(this.getMillis(), n));
    }

    public DateMidnight withYearOfCentury(int n) {
        return this.withMillis(this.getChronology().yearOfCentury().set(this.getMillis(), n));
    }

    public DateMidnight withYearOfEra(int n) {
        return this.withMillis(this.getChronology().yearOfEra().set(this.getMillis(), n));
    }

    public DateMidnight withZoneRetainFields(DateTimeZone dateTimeZone) {
        DateTimeZone dateTimeZone2;
        if ((dateTimeZone = DateTimeUtils.getZone((DateTimeZone)dateTimeZone)) == (dateTimeZone2 = DateTimeUtils.getZone((DateTimeZone)this.getZone()))) {
            return this;
        }
        return new DateMidnight(dateTimeZone2.getMillisKeepLocal(dateTimeZone, this.getMillis()), this.getChronology().withZone(dateTimeZone));
    }

    public  year() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  yearOfCentury() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  yearOfEra() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }
}

