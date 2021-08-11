// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.convert.FromString;
import org.joda.time.format.ISODateTimeFormat;
import java.io.Serializable;
import org.joda.time.base.BaseDateTime;

public final class DateTime extends BaseDateTime implements ReadableDateTime, Serializable
{
    private static final long serialVersionUID = -5171125899451703815L;
    
    public DateTime() {
    }
    
    public DateTime(final int n, final int n2, final int n3, final int n4, final int n5) {
        super(n, n2, n3, n4, n5, 0, 0);
    }
    
    public DateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        super(n, n2, n3, n4, n5, n6, 0);
    }
    
    public DateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7) {
        super(n, n2, n3, n4, n5, n6, n7);
    }
    
    public DateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final Chronology chronology) {
        super(n, n2, n3, n4, n5, n6, n7, chronology);
    }
    
    public DateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final DateTimeZone dateTimeZone) {
        super(n, n2, n3, n4, n5, n6, n7, dateTimeZone);
    }
    
    public DateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final Chronology chronology) {
        super(n, n2, n3, n4, n5, n6, 0, chronology);
    }
    
    public DateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final DateTimeZone dateTimeZone) {
        super(n, n2, n3, n4, n5, n6, 0, dateTimeZone);
    }
    
    public DateTime(final int n, final int n2, final int n3, final int n4, final int n5, final Chronology chronology) {
        super(n, n2, n3, n4, n5, 0, 0, chronology);
    }
    
    public DateTime(final int n, final int n2, final int n3, final int n4, final int n5, final DateTimeZone dateTimeZone) {
        super(n, n2, n3, n4, n5, 0, 0, dateTimeZone);
    }
    
    public DateTime(final long n) {
        super(n);
    }
    
    public DateTime(final long n, final Chronology chronology) {
        super(n, chronology);
    }
    
    public DateTime(final long n, final DateTimeZone dateTimeZone) {
        super(n, dateTimeZone);
    }
    
    public DateTime(final Object o) {
        super(o, (Chronology)null);
    }
    
    public DateTime(final Object o, final Chronology chronology) {
        super(o, DateTimeUtils.getChronology(chronology));
    }
    
    public DateTime(final Object o, final DateTimeZone dateTimeZone) {
        super(o, dateTimeZone);
    }
    
    public DateTime(final Chronology chronology) {
        super(chronology);
    }
    
    public DateTime(final DateTimeZone dateTimeZone) {
        super(dateTimeZone);
    }
    
    public static DateTime now() {
        return new DateTime();
    }
    
    public static DateTime now(final Chronology chronology) {
        if (chronology != null) {
            return new DateTime(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }
    
    public static DateTime now(final DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new DateTime(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }
    
    @FromString
    public static DateTime parse(final String s) {
        return parse(s, ISODateTimeFormat.dateTimeParser().withOffsetParsed());
    }
    
    public static DateTime parse(final String s, final DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.parseDateTime(s);
    }
    
    public DateTime.DateTime$Property centuryOfEra() {
        return new DateTime.DateTime$Property(this, this.getChronology().centuryOfEra());
    }
    
    public DateTime.DateTime$Property dayOfMonth() {
        return new DateTime.DateTime$Property(this, this.getChronology().dayOfMonth());
    }
    
    public DateTime.DateTime$Property dayOfWeek() {
        return new DateTime.DateTime$Property(this, this.getChronology().dayOfWeek());
    }
    
    public DateTime.DateTime$Property dayOfYear() {
        return new DateTime.DateTime$Property(this, this.getChronology().dayOfYear());
    }
    
    public DateTime.DateTime$Property era() {
        return new DateTime.DateTime$Property(this, this.getChronology().era());
    }
    
    public DateTime.DateTime$Property hourOfDay() {
        return new DateTime.DateTime$Property(this, this.getChronology().hourOfDay());
    }
    
    public DateTime.DateTime$Property millisOfDay() {
        return new DateTime.DateTime$Property(this, this.getChronology().millisOfDay());
    }
    
    public DateTime.DateTime$Property millisOfSecond() {
        return new DateTime.DateTime$Property(this, this.getChronology().millisOfSecond());
    }
    
    public DateTime minus(final long n) {
        return this.withDurationAdded(n, -1);
    }
    
    public DateTime minus(final ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, -1);
    }
    
    public DateTime minus(final ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, -1);
    }
    
    public DateTime minusDays(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().days().subtract(this.getMillis(), n));
    }
    
    public DateTime minusHours(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().hours().subtract(this.getMillis(), n));
    }
    
    public DateTime minusMillis(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().millis().subtract(this.getMillis(), n));
    }
    
    public DateTime minusMinutes(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().minutes().subtract(this.getMillis(), n));
    }
    
    public DateTime minusMonths(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().months().subtract(this.getMillis(), n));
    }
    
    public DateTime minusSeconds(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().seconds().subtract(this.getMillis(), n));
    }
    
    public DateTime minusWeeks(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().weeks().subtract(this.getMillis(), n));
    }
    
    public DateTime minusYears(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().years().subtract(this.getMillis(), n));
    }
    
    public DateTime.DateTime$Property minuteOfDay() {
        return new DateTime.DateTime$Property(this, this.getChronology().minuteOfDay());
    }
    
    public DateTime.DateTime$Property minuteOfHour() {
        return new DateTime.DateTime$Property(this, this.getChronology().minuteOfHour());
    }
    
    public DateTime.DateTime$Property monthOfYear() {
        return new DateTime.DateTime$Property(this, this.getChronology().monthOfYear());
    }
    
    public DateTime plus(final long n) {
        return this.withDurationAdded(n, 1);
    }
    
    public DateTime plus(final ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, 1);
    }
    
    public DateTime plus(final ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, 1);
    }
    
    public DateTime plusDays(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().days().add(this.getMillis(), n));
    }
    
    public DateTime plusHours(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().hours().add(this.getMillis(), n));
    }
    
    public DateTime plusMillis(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().millis().add(this.getMillis(), n));
    }
    
    public DateTime plusMinutes(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().minutes().add(this.getMillis(), n));
    }
    
    public DateTime plusMonths(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().months().add(this.getMillis(), n));
    }
    
    public DateTime plusSeconds(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().seconds().add(this.getMillis(), n));
    }
    
    public DateTime plusWeeks(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().weeks().add(this.getMillis(), n));
    }
    
    public DateTime plusYears(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().years().add(this.getMillis(), n));
    }
    
    public DateTime.DateTime$Property property(final DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType == null) {
            throw new IllegalArgumentException("The DateTimeFieldType must not be null");
        }
        final DateTimeField field = dateTimeFieldType.getField(this.getChronology());
        if (field.isSupported()) {
            return new DateTime.DateTime$Property(this, field);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Field '");
        sb.append(dateTimeFieldType);
        sb.append("' is not supported");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public DateTime.DateTime$Property secondOfDay() {
        return new DateTime.DateTime$Property(this, this.getChronology().secondOfDay());
    }
    
    public DateTime.DateTime$Property secondOfMinute() {
        return new DateTime.DateTime$Property(this, this.getChronology().secondOfMinute());
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
        chronology = DateTimeUtils.getChronology(chronology);
        if (this.getChronology() == chronology) {
            return this;
        }
        return super.toDateTime(chronology);
    }
    
    @Override
    public DateTime toDateTime(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        if (this.getZone() == zone) {
            return this;
        }
        return super.toDateTime(zone);
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
    
    public DateTime.DateTime$Property weekOfWeekyear() {
        return new DateTime.DateTime$Property(this, this.getChronology().weekOfWeekyear());
    }
    
    public DateTime.DateTime$Property weekyear() {
        return new DateTime.DateTime$Property(this, this.getChronology().weekyear());
    }
    
    public DateTime withCenturyOfEra(final int n) {
        return this.withMillis(this.getChronology().centuryOfEra().set(this.getMillis(), n));
    }
    
    public DateTime withChronology(Chronology chronology) {
        chronology = DateTimeUtils.getChronology(chronology);
        if (chronology == this.getChronology()) {
            return this;
        }
        return new DateTime(this.getMillis(), chronology);
    }
    
    public DateTime withDate(final int n, final int n2, final int n3) {
        final Chronology chronology = this.getChronology();
        return this.withMillis(chronology.getZone().convertLocalToUTC(chronology.withUTC().getDateTimeMillis(n, n2, n3, this.getMillisOfDay()), false, this.getMillis()));
    }
    
    public DateTime withDate(final LocalDate localDate) {
        return this.withDate(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth());
    }
    
    public DateTime withDayOfMonth(final int n) {
        return this.withMillis(this.getChronology().dayOfMonth().set(this.getMillis(), n));
    }
    
    public DateTime withDayOfWeek(final int n) {
        return this.withMillis(this.getChronology().dayOfWeek().set(this.getMillis(), n));
    }
    
    public DateTime withDayOfYear(final int n) {
        return this.withMillis(this.getChronology().dayOfYear().set(this.getMillis(), n));
    }
    
    public DateTime withDurationAdded(final long n, final int n2) {
        if (n == 0L) {
            return this;
        }
        if (n2 == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().add(this.getMillis(), n, n2));
    }
    
    public DateTime withDurationAdded(final ReadableDuration readableDuration, final int n) {
        if (readableDuration == null) {
            return this;
        }
        if (n == 0) {
            return this;
        }
        return this.withDurationAdded(readableDuration.getMillis(), n);
    }
    
    public DateTime withEarlierOffsetAtOverlap() {
        return this.withMillis(this.getZone().adjustOffset(this.getMillis(), false));
    }
    
    public DateTime withEra(final int n) {
        return this.withMillis(this.getChronology().era().set(this.getMillis(), n));
    }
    
    public DateTime withField(final DateTimeFieldType dateTimeFieldType, final int n) {
        if (dateTimeFieldType != null) {
            return this.withMillis(dateTimeFieldType.getField(this.getChronology()).set(this.getMillis(), n));
        }
        throw new IllegalArgumentException("Field must not be null");
    }
    
    public DateTime withFieldAdded(final DurationFieldType durationFieldType, final int n) {
        if (durationFieldType == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        if (n == 0) {
            return this;
        }
        return this.withMillis(durationFieldType.getField(this.getChronology()).add(this.getMillis(), n));
    }
    
    public DateTime withFields(final ReadablePartial readablePartial) {
        if (readablePartial == null) {
            return this;
        }
        return this.withMillis(this.getChronology().set(readablePartial, this.getMillis()));
    }
    
    public DateTime withHourOfDay(final int n) {
        return this.withMillis(this.getChronology().hourOfDay().set(this.getMillis(), n));
    }
    
    public DateTime withLaterOffsetAtOverlap() {
        return this.withMillis(this.getZone().adjustOffset(this.getMillis(), true));
    }
    
    public DateTime withMillis(final long n) {
        if (n == this.getMillis()) {
            return this;
        }
        return new DateTime(n, this.getChronology());
    }
    
    public DateTime withMillisOfDay(final int n) {
        return this.withMillis(this.getChronology().millisOfDay().set(this.getMillis(), n));
    }
    
    public DateTime withMillisOfSecond(final int n) {
        return this.withMillis(this.getChronology().millisOfSecond().set(this.getMillis(), n));
    }
    
    public DateTime withMinuteOfHour(final int n) {
        return this.withMillis(this.getChronology().minuteOfHour().set(this.getMillis(), n));
    }
    
    public DateTime withMonthOfYear(final int n) {
        return this.withMillis(this.getChronology().monthOfYear().set(this.getMillis(), n));
    }
    
    public DateTime withPeriodAdded(final ReadablePeriod readablePeriod, final int n) {
        if (readablePeriod == null) {
            return this;
        }
        if (n == 0) {
            return this;
        }
        return this.withMillis(this.getChronology().add(readablePeriod, this.getMillis(), n));
    }
    
    public DateTime withSecondOfMinute(final int n) {
        return this.withMillis(this.getChronology().secondOfMinute().set(this.getMillis(), n));
    }
    
    public DateTime withTime(final int n, final int n2, final int n3, final int n4) {
        final Chronology chronology = this.getChronology();
        return this.withMillis(chronology.getZone().convertLocalToUTC(chronology.withUTC().getDateTimeMillis(this.getYear(), this.getMonthOfYear(), this.getDayOfMonth(), n, n2, n3, n4), false, this.getMillis()));
    }
    
    public DateTime withTime(final LocalTime localTime) {
        return this.withTime(localTime.getHourOfDay(), localTime.getMinuteOfHour(), localTime.getSecondOfMinute(), localTime.getMillisOfSecond());
    }
    
    public DateTime withTimeAtStartOfDay() {
        return this.toLocalDate().toDateTimeAtStartOfDay(this.getZone());
    }
    
    public DateTime withWeekOfWeekyear(final int n) {
        return this.withMillis(this.getChronology().weekOfWeekyear().set(this.getMillis(), n));
    }
    
    public DateTime withWeekyear(final int n) {
        return this.withMillis(this.getChronology().weekyear().set(this.getMillis(), n));
    }
    
    public DateTime withYear(final int n) {
        return this.withMillis(this.getChronology().year().set(this.getMillis(), n));
    }
    
    public DateTime withYearOfCentury(final int n) {
        return this.withMillis(this.getChronology().yearOfCentury().set(this.getMillis(), n));
    }
    
    public DateTime withYearOfEra(final int n) {
        return this.withMillis(this.getChronology().yearOfEra().set(this.getMillis(), n));
    }
    
    public DateTime withZone(final DateTimeZone dateTimeZone) {
        return this.withChronology(this.getChronology().withZone(dateTimeZone));
    }
    
    public DateTime withZoneRetainFields(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        final DateTimeZone zone2 = DateTimeUtils.getZone(this.getZone());
        if (zone == zone2) {
            return this;
        }
        return new DateTime(zone2.getMillisKeepLocal(zone, this.getMillis()), this.getChronology().withZone(zone));
    }
    
    public DateTime.DateTime$Property year() {
        return new DateTime.DateTime$Property(this, this.getChronology().year());
    }
    
    public DateTime.DateTime$Property yearOfCentury() {
        return new DateTime.DateTime$Property(this, this.getChronology().yearOfCentury());
    }
    
    public DateTime.DateTime$Property yearOfEra() {
        return new DateTime.DateTime$Property(this, this.getChronology().yearOfEra());
    }
}
