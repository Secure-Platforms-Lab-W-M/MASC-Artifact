/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.FromString
 *  org.joda.time.Chronology
 *  org.joda.time.DateTime$Property
 *  org.joda.time.DateTimeField
 *  org.joda.time.DateTimeFieldType
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.DurationField
 *  org.joda.time.DurationFieldType
 *  org.joda.time.TimeOfDay
 *  org.joda.time.YearMonthDay
 *  org.joda.time.chrono.ISOChronology
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
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.base.BaseDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public final class DateTime
extends BaseDateTime
implements ReadableDateTime,
Serializable {
    private static final long serialVersionUID = -5171125899451703815L;

    public DateTime() {
    }

    public DateTime(int n, int n2, int n3, int n4, int n5) {
        super(n, n2, n3, n4, n5, 0, 0);
    }

    public DateTime(int n, int n2, int n3, int n4, int n5, int n6) {
        super(n, n2, n3, n4, n5, n6, 0);
    }

    public DateTime(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        super(n, n2, n3, n4, n5, n6, n7);
    }

    public DateTime(int n, int n2, int n3, int n4, int n5, int n6, int n7, Chronology chronology) {
        super(n, n2, n3, n4, n5, n6, n7, chronology);
    }

    public DateTime(int n, int n2, int n3, int n4, int n5, int n6, int n7, DateTimeZone dateTimeZone) {
        super(n, n2, n3, n4, n5, n6, n7, dateTimeZone);
    }

    public DateTime(int n, int n2, int n3, int n4, int n5, int n6, Chronology chronology) {
        super(n, n2, n3, n4, n5, n6, 0, chronology);
    }

    public DateTime(int n, int n2, int n3, int n4, int n5, int n6, DateTimeZone dateTimeZone) {
        super(n, n2, n3, n4, n5, n6, 0, dateTimeZone);
    }

    public DateTime(int n, int n2, int n3, int n4, int n5, Chronology chronology) {
        super(n, n2, n3, n4, n5, 0, 0, chronology);
    }

    public DateTime(int n, int n2, int n3, int n4, int n5, DateTimeZone dateTimeZone) {
        super(n, n2, n3, n4, n5, 0, 0, dateTimeZone);
    }

    public DateTime(long l) {
        super(l);
    }

    public DateTime(long l, Chronology chronology) {
        super(l, chronology);
    }

    public DateTime(long l, DateTimeZone dateTimeZone) {
        super(l, dateTimeZone);
    }

    public DateTime(Object object) {
        super(object, (Chronology)null);
    }

    public DateTime(Object object, Chronology chronology) {
        super(object, DateTimeUtils.getChronology((Chronology)chronology));
    }

    public DateTime(Object object, DateTimeZone dateTimeZone) {
        super(object, dateTimeZone);
    }

    public DateTime(Chronology chronology) {
        super(chronology);
    }

    public DateTime(DateTimeZone dateTimeZone) {
        super(dateTimeZone);
    }

    public static DateTime now() {
        return new DateTime();
    }

    public static DateTime now(Chronology chronology) {
        if (chronology != null) {
            return new DateTime(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }

    public static DateTime now(DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new DateTime(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }

    @FromString
    public static DateTime parse(String string) {
        return DateTime.parse(string, ISODateTimeFormat.dateTimeParser().withOffsetParsed());
    }

    public static DateTime parse(String string, DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.parseDateTime(string);
    }

    public  centuryOfEra() {
        return new /* Unavailable Anonymous Inner Class!! */;
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

    public  hourOfDay() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  millisOfDay() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  millisOfSecond() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public DateTime minus(long l) {
        return this.withDurationAdded(l, -1);
    }

    public DateTime minus(ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, -1);
    }

    public DateTime minus(ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, -1);
    }

    public DateTime minusDays(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().days().subtract(this.getMillis(), n));
    }

    public DateTime minusHours(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().hours().subtract(this.getMillis(), n));
    }

    public DateTime minusMillis(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().millis().subtract(this.getMillis(), n));
    }

    public DateTime minusMinutes(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().minutes().subtract(this.getMillis(), n));
    }

    public DateTime minusMonths(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().months().subtract(this.getMillis(), n));
    }

    public DateTime minusSeconds(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().seconds().subtract(this.getMillis(), n));
    }

    public DateTime minusWeeks(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().weeks().subtract(this.getMillis(), n));
    }

    public DateTime minusYears(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().years().subtract(this.getMillis(), n));
    }

    public  minuteOfDay() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  minuteOfHour() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  monthOfYear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public DateTime plus(long l) {
        return this.withDurationAdded(l, 1);
    }

    public DateTime plus(ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, 1);
    }

    public DateTime plus(ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, 1);
    }

    public DateTime plusDays(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().days().add(this.getMillis(), n));
    }

    public DateTime plusHours(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().hours().add(this.getMillis(), n));
    }

    public DateTime plusMillis(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().millis().add(this.getMillis(), n));
    }

    public DateTime plusMinutes(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().minutes().add(this.getMillis(), n));
    }

    public DateTime plusMonths(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().months().add(this.getMillis(), n));
    }

    public DateTime plusSeconds(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().seconds().add(this.getMillis(), n));
    }

    public DateTime plusWeeks(int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().weeks().add(this.getMillis(), n));
    }

    public DateTime plusYears(int n) {
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

    public  secondOfDay() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  secondOfMinute() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    @Deprecated
    public DateMidnight toDateMidnight() {
        return new DateMidnight(this.getMillis(), this.getChronology());
    }

    @Override
    public DateTime toDateTime() {
        return this;
    }

    @Override
    public DateTime toDateTime(Chronology chronology) {
        chronology = DateTimeUtils.getChronology((Chronology)chronology);
        if (this.getChronology() == chronology) {
            return this;
        }
        return super.toDateTime(chronology);
    }

    @Override
    public DateTime toDateTime(DateTimeZone dateTimeZone) {
        dateTimeZone = DateTimeUtils.getZone((DateTimeZone)dateTimeZone);
        if (this.getZone() == dateTimeZone) {
            return this;
        }
        return super.toDateTime(dateTimeZone);
    }

    @Override
    public DateTime toDateTimeISO() {
        if (this.getChronology() == ISOChronology.getInstance()) {
            return this;
        }
        return super.toDateTimeISO();
    }

    public LocalDate toLocalDate() {
        return new LocalDate(this.getMillis(), this.getChronology());
    }

    public LocalDateTime toLocalDateTime() {
        return new LocalDateTime(this.getMillis(), this.getChronology());
    }

    public LocalTime toLocalTime() {
        return new LocalTime(this.getMillis(), this.getChronology());
    }

    @Deprecated
    public TimeOfDay toTimeOfDay() {
        return new TimeOfDay(this.getMillis(), this.getChronology());
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

    public DateTime withCenturyOfEra(int n) {
        return this.withMillis(this.getChronology().centuryOfEra().set(this.getMillis(), n));
    }

    public DateTime withChronology(Chronology chronology) {
        if ((chronology = DateTimeUtils.getChronology((Chronology)chronology)) == this.getChronology()) {
            return this;
        }
        return new DateTime(this.getMillis(), chronology);
    }

    public DateTime withDate(int n, int n2, int n3) {
        Chronology chronology = this.getChronology();
        long l = chronology.withUTC().getDateTimeMillis(n, n2, n3, this.getMillisOfDay());
        return this.withMillis(chronology.getZone().convertLocalToUTC(l, false, this.getMillis()));
    }

    public DateTime withDate(LocalDate localDate) {
        return this.withDate(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth());
    }

    public DateTime withDayOfMonth(int n) {
        return this.withMillis(this.getChronology().dayOfMonth().set(this.getMillis(), n));
    }

    public DateTime withDayOfWeek(int n) {
        return this.withMillis(this.getChronology().dayOfWeek().set(this.getMillis(), n));
    }

    public DateTime withDayOfYear(int n) {
        return this.withMillis(this.getChronology().dayOfYear().set(this.getMillis(), n));
    }

    public DateTime withDurationAdded(long l, int n) {
        if (l != 0L) {
            if (n == 0) {
                return this;
            }
            return this.withMillis(this.getChronology().add(this.getMillis(), l, n));
        }
        return this;
    }

    public DateTime withDurationAdded(ReadableDuration readableDuration, int n) {
        if (readableDuration != null) {
            if (n == 0) {
                return this;
            }
            return this.withDurationAdded(readableDuration.getMillis(), n);
        }
        return this;
    }

    public DateTime withEarlierOffsetAtOverlap() {
        return this.withMillis(this.getZone().adjustOffset(this.getMillis(), false));
    }

    public DateTime withEra(int n) {
        return this.withMillis(this.getChronology().era().set(this.getMillis(), n));
    }

    public DateTime withField(DateTimeFieldType dateTimeFieldType, int n) {
        if (dateTimeFieldType != null) {
            return this.withMillis(dateTimeFieldType.getField(this.getChronology()).set(this.getMillis(), n));
        }
        throw new IllegalArgumentException("Field must not be null");
    }

    public DateTime withFieldAdded(DurationFieldType durationFieldType, int n) {
        if (durationFieldType != null) {
            if (n == 0) {
                return this;
            }
            return this.withMillis(durationFieldType.getField(this.getChronology()).add(this.getMillis(), n));
        }
        throw new IllegalArgumentException("Field must not be null");
    }

    public DateTime withFields(ReadablePartial readablePartial) {
        if (readablePartial == null) {
            return this;
        }
        return this.withMillis(this.getChronology().set(readablePartial, this.getMillis()));
    }

    public DateTime withHourOfDay(int n) {
        return this.withMillis(this.getChronology().hourOfDay().set(this.getMillis(), n));
    }

    public DateTime withLaterOffsetAtOverlap() {
        return this.withMillis(this.getZone().adjustOffset(this.getMillis(), true));
    }

    public DateTime withMillis(long l) {
        if (l == this.getMillis()) {
            return this;
        }
        return new DateTime(l, this.getChronology());
    }

    public DateTime withMillisOfDay(int n) {
        return this.withMillis(this.getChronology().millisOfDay().set(this.getMillis(), n));
    }

    public DateTime withMillisOfSecond(int n) {
        return this.withMillis(this.getChronology().millisOfSecond().set(this.getMillis(), n));
    }

    public DateTime withMinuteOfHour(int n) {
        return this.withMillis(this.getChronology().minuteOfHour().set(this.getMillis(), n));
    }

    public DateTime withMonthOfYear(int n) {
        return this.withMillis(this.getChronology().monthOfYear().set(this.getMillis(), n));
    }

    public DateTime withPeriodAdded(ReadablePeriod readablePeriod, int n) {
        if (readablePeriod != null) {
            if (n == 0) {
                return this;
            }
            return this.withMillis(this.getChronology().add(readablePeriod, this.getMillis(), n));
        }
        return this;
    }

    public DateTime withSecondOfMinute(int n) {
        return this.withMillis(this.getChronology().secondOfMinute().set(this.getMillis(), n));
    }

    public DateTime withTime(int n, int n2, int n3, int n4) {
        Chronology chronology = this.getChronology();
        long l = chronology.withUTC().getDateTimeMillis(this.getYear(), this.getMonthOfYear(), this.getDayOfMonth(), n, n2, n3, n4);
        return this.withMillis(chronology.getZone().convertLocalToUTC(l, false, this.getMillis()));
    }

    public DateTime withTime(LocalTime localTime) {
        return this.withTime(localTime.getHourOfDay(), localTime.getMinuteOfHour(), localTime.getSecondOfMinute(), localTime.getMillisOfSecond());
    }

    public DateTime withTimeAtStartOfDay() {
        return this.toLocalDate().toDateTimeAtStartOfDay(this.getZone());
    }

    public DateTime withWeekOfWeekyear(int n) {
        return this.withMillis(this.getChronology().weekOfWeekyear().set(this.getMillis(), n));
    }

    public DateTime withWeekyear(int n) {
        return this.withMillis(this.getChronology().weekyear().set(this.getMillis(), n));
    }

    public DateTime withYear(int n) {
        return this.withMillis(this.getChronology().year().set(this.getMillis(), n));
    }

    public DateTime withYearOfCentury(int n) {
        return this.withMillis(this.getChronology().yearOfCentury().set(this.getMillis(), n));
    }

    public DateTime withYearOfEra(int n) {
        return this.withMillis(this.getChronology().yearOfEra().set(this.getMillis(), n));
    }

    public DateTime withZone(DateTimeZone dateTimeZone) {
        return this.withChronology(this.getChronology().withZone(dateTimeZone));
    }

    public DateTime withZoneRetainFields(DateTimeZone dateTimeZone) {
        DateTimeZone dateTimeZone2;
        if ((dateTimeZone = DateTimeUtils.getZone((DateTimeZone)dateTimeZone)) == (dateTimeZone2 = DateTimeUtils.getZone((DateTimeZone)this.getZone()))) {
            return this;
        }
        return new DateTime(dateTimeZone2.getMillisKeepLocal(dateTimeZone, this.getMillis()), this.getChronology().withZone(dateTimeZone));
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

