// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import org.joda.time.field.FieldUtils;
import java.util.Locale;
import org.joda.time.format.DateTimeFormat;
import org.joda.convert.ToString;
import java.util.TimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.convert.FromString;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Calendar;
import org.joda.time.convert.PartialConverter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.convert.ConverterManager;
import org.joda.time.chrono.ISOChronology;
import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;
import org.joda.time.base.BaseLocal;

public final class LocalDate extends BaseLocal implements ReadablePartial, Serializable
{
    private static final Set<DurationFieldType> DATE_DURATION_TYPES;
    private static final int DAY_OF_MONTH = 2;
    private static final int MONTH_OF_YEAR = 1;
    private static final int YEAR = 0;
    private static final long serialVersionUID = -8775358157899L;
    private final Chronology iChronology;
    private transient int iHash;
    private final long iLocalMillis;
    
    static {
        (DATE_DURATION_TYPES = new HashSet<DurationFieldType>()).add(DurationFieldType.days());
        LocalDate.DATE_DURATION_TYPES.add(DurationFieldType.weeks());
        LocalDate.DATE_DURATION_TYPES.add(DurationFieldType.months());
        LocalDate.DATE_DURATION_TYPES.add(DurationFieldType.weekyears());
        LocalDate.DATE_DURATION_TYPES.add(DurationFieldType.years());
        LocalDate.DATE_DURATION_TYPES.add(DurationFieldType.centuries());
        LocalDate.DATE_DURATION_TYPES.add(DurationFieldType.eras());
    }
    
    public LocalDate() {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance());
    }
    
    public LocalDate(final int n, final int n2, final int n3) {
        this(n, n2, n3, (Chronology)ISOChronology.getInstanceUTC());
    }
    
    public LocalDate(final int n, final int n2, final int n3, Chronology withUTC) {
        withUTC = DateTimeUtils.getChronology(withUTC).withUTC();
        final long dateTimeMillis = withUTC.getDateTimeMillis(n, n2, n3, 0);
        this.iChronology = withUTC;
        this.iLocalMillis = dateTimeMillis;
    }
    
    public LocalDate(final long n) {
        this(n, (Chronology)ISOChronology.getInstance());
    }
    
    public LocalDate(long millisKeepLocal, Chronology iChronology) {
        iChronology = DateTimeUtils.getChronology(iChronology);
        millisKeepLocal = iChronology.getZone().getMillisKeepLocal(DateTimeZone.UTC, millisKeepLocal);
        iChronology = iChronology.withUTC();
        this.iLocalMillis = iChronology.dayOfMonth().roundFloor(millisKeepLocal);
        this.iChronology = iChronology;
    }
    
    public LocalDate(final long n, final DateTimeZone dateTimeZone) {
        this(n, (Chronology)ISOChronology.getInstance(dateTimeZone));
    }
    
    public LocalDate(final Object o) {
        this(o, (Chronology)null);
    }
    
    public LocalDate(final Object o, Chronology chronology) {
        final PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter(o);
        chronology = DateTimeUtils.getChronology(partialConverter.getChronology(o, chronology));
        this.iChronology = chronology.withUTC();
        final int[] partialValues = partialConverter.getPartialValues((ReadablePartial)this, o, chronology, ISODateTimeFormat.localDateParser());
        this.iLocalMillis = this.iChronology.getDateTimeMillis(partialValues[0], partialValues[1], partialValues[2], 0);
    }
    
    public LocalDate(final Object o, final DateTimeZone dateTimeZone) {
        final PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter(o);
        final Chronology chronology = DateTimeUtils.getChronology(partialConverter.getChronology(o, dateTimeZone));
        this.iChronology = chronology.withUTC();
        final int[] partialValues = partialConverter.getPartialValues((ReadablePartial)this, o, chronology, ISODateTimeFormat.localDateParser());
        this.iLocalMillis = this.iChronology.getDateTimeMillis(partialValues[0], partialValues[1], partialValues[2], 0);
    }
    
    public LocalDate(final Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }
    
    public LocalDate(final DateTimeZone dateTimeZone) {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance(dateTimeZone));
    }
    
    public static LocalDate fromCalendarFields(final Calendar calendar) {
        if (calendar != null) {
            final int value = calendar.get(0);
            int value2 = calendar.get(1);
            if (value != 1) {
                value2 = 1 - value2;
            }
            return new LocalDate(value2, calendar.get(2) + 1, calendar.get(5));
        }
        throw new IllegalArgumentException("The calendar must not be null");
    }
    
    public static LocalDate fromDateFields(final Date time) {
        if (time == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        if (time.getTime() < 0L) {
            final GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(time);
            return fromCalendarFields(gregorianCalendar);
        }
        return new LocalDate(time.getYear() + 1900, time.getMonth() + 1, time.getDate());
    }
    
    public static LocalDate now() {
        return new LocalDate();
    }
    
    public static LocalDate now(final Chronology chronology) {
        if (chronology != null) {
            return new LocalDate(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }
    
    public static LocalDate now(final DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new LocalDate(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }
    
    @FromString
    public static LocalDate parse(final String s) {
        return parse(s, ISODateTimeFormat.localDateParser());
    }
    
    public static LocalDate parse(final String s, final DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.parseLocalDate(s);
    }
    
    private Object readResolve() {
        if (this.iChronology == null) {
            return new LocalDate(this.iLocalMillis, (Chronology)ISOChronology.getInstanceUTC());
        }
        if (!DateTimeZone.UTC.equals(this.iChronology.getZone())) {
            return new LocalDate(this.iLocalMillis, this.iChronology.withUTC());
        }
        return this;
    }
    
    public LocalDate.LocalDate$Property centuryOfEra() {
        return new LocalDate.LocalDate$Property(this, this.getChronology().centuryOfEra());
    }
    
    @Override
    public int compareTo(final ReadablePartial readablePartial) {
        if (this == readablePartial) {
            return 0;
        }
        if (readablePartial instanceof LocalDate) {
            final LocalDate localDate = (LocalDate)readablePartial;
            if (this.iChronology.equals(localDate.iChronology)) {
                final long iLocalMillis = this.iLocalMillis;
                final long iLocalMillis2 = localDate.iLocalMillis;
                if (iLocalMillis < iLocalMillis2) {
                    return -1;
                }
                if (iLocalMillis == iLocalMillis2) {
                    return 0;
                }
                return 1;
            }
        }
        return super.compareTo(readablePartial);
    }
    
    public LocalDate.LocalDate$Property dayOfMonth() {
        return new LocalDate.LocalDate$Property(this, this.getChronology().dayOfMonth());
    }
    
    public LocalDate.LocalDate$Property dayOfWeek() {
        return new LocalDate.LocalDate$Property(this, this.getChronology().dayOfWeek());
    }
    
    public LocalDate.LocalDate$Property dayOfYear() {
        return new LocalDate.LocalDate$Property(this, this.getChronology().dayOfYear());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof LocalDate) {
            final LocalDate localDate = (LocalDate)o;
            if (this.iChronology.equals(localDate.iChronology)) {
                return this.iLocalMillis == localDate.iLocalMillis;
            }
        }
        return super.equals(o);
    }
    
    public LocalDate.LocalDate$Property era() {
        return new LocalDate.LocalDate$Property(this, this.getChronology().era());
    }
    
    @Override
    public int get(final DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType == null) {
            throw new IllegalArgumentException("The DateTimeFieldType must not be null");
        }
        if (this.isSupported(dateTimeFieldType)) {
            return dateTimeFieldType.getField(this.getChronology()).get(this.getLocalMillis());
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Field '");
        sb.append(dateTimeFieldType);
        sb.append("' is not supported");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public int getCenturyOfEra() {
        return this.getChronology().centuryOfEra().get(this.getLocalMillis());
    }
    
    @Override
    public Chronology getChronology() {
        return this.iChronology;
    }
    
    public int getDayOfMonth() {
        return this.getChronology().dayOfMonth().get(this.getLocalMillis());
    }
    
    public int getDayOfWeek() {
        return this.getChronology().dayOfWeek().get(this.getLocalMillis());
    }
    
    public int getDayOfYear() {
        return this.getChronology().dayOfYear().get(this.getLocalMillis());
    }
    
    public int getEra() {
        return this.getChronology().era().get(this.getLocalMillis());
    }
    
    @Override
    protected DateTimeField getField(final int n, final Chronology chronology) {
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid index: ");
                sb.append(n);
                throw new IndexOutOfBoundsException(sb.toString());
            }
            case 2: {
                return chronology.dayOfMonth();
            }
            case 1: {
                return chronology.monthOfYear();
            }
            case 0: {
                return chronology.year();
            }
        }
    }
    
    @Override
    protected long getLocalMillis() {
        return this.iLocalMillis;
    }
    
    public int getMonthOfYear() {
        return this.getChronology().monthOfYear().get(this.getLocalMillis());
    }
    
    @Override
    public int getValue(final int n) {
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid index: ");
                sb.append(n);
                throw new IndexOutOfBoundsException(sb.toString());
            }
            case 2: {
                return this.getChronology().dayOfMonth().get(this.getLocalMillis());
            }
            case 1: {
                return this.getChronology().monthOfYear().get(this.getLocalMillis());
            }
            case 0: {
                return this.getChronology().year().get(this.getLocalMillis());
            }
        }
    }
    
    public int getWeekOfWeekyear() {
        return this.getChronology().weekOfWeekyear().get(this.getLocalMillis());
    }
    
    public int getWeekyear() {
        return this.getChronology().weekyear().get(this.getLocalMillis());
    }
    
    public int getYear() {
        return this.getChronology().year().get(this.getLocalMillis());
    }
    
    public int getYearOfCentury() {
        return this.getChronology().yearOfCentury().get(this.getLocalMillis());
    }
    
    public int getYearOfEra() {
        return this.getChronology().yearOfEra().get(this.getLocalMillis());
    }
    
    @Override
    public int hashCode() {
        final int iHash = this.iHash;
        if (iHash == 0) {
            return this.iHash = super.hashCode();
        }
        return iHash;
    }
    
    @Override
    public boolean isSupported(final DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType == null) {
            return false;
        }
        final DurationFieldType durationType = dateTimeFieldType.getDurationType();
        return (LocalDate.DATE_DURATION_TYPES.contains(durationType) || durationType.getField(this.getChronology()).getUnitMillis() >= this.getChronology().days().getUnitMillis()) && dateTimeFieldType.getField(this.getChronology()).isSupported();
    }
    
    public boolean isSupported(final DurationFieldType durationFieldType) {
        if (durationFieldType == null) {
            return false;
        }
        final DurationField field = durationFieldType.getField(this.getChronology());
        return (LocalDate.DATE_DURATION_TYPES.contains(durationFieldType) || field.getUnitMillis() >= this.getChronology().days().getUnitMillis()) && field.isSupported();
    }
    
    public LocalDate minus(final ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, -1);
    }
    
    public LocalDate minusDays(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().days().subtract(this.getLocalMillis(), n));
    }
    
    public LocalDate minusMonths(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().months().subtract(this.getLocalMillis(), n));
    }
    
    public LocalDate minusWeeks(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().weeks().subtract(this.getLocalMillis(), n));
    }
    
    public LocalDate minusYears(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().years().subtract(this.getLocalMillis(), n));
    }
    
    public LocalDate.LocalDate$Property monthOfYear() {
        return new LocalDate.LocalDate$Property(this, this.getChronology().monthOfYear());
    }
    
    public LocalDate plus(final ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, 1);
    }
    
    public LocalDate plusDays(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().days().add(this.getLocalMillis(), n));
    }
    
    public LocalDate plusMonths(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().months().add(this.getLocalMillis(), n));
    }
    
    public LocalDate plusWeeks(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().weeks().add(this.getLocalMillis(), n));
    }
    
    public LocalDate plusYears(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().years().add(this.getLocalMillis(), n));
    }
    
    public LocalDate.LocalDate$Property property(final DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType == null) {
            throw new IllegalArgumentException("The DateTimeFieldType must not be null");
        }
        if (this.isSupported(dateTimeFieldType)) {
            return new LocalDate.LocalDate$Property(this, dateTimeFieldType.getField(this.getChronology()));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Field '");
        sb.append(dateTimeFieldType);
        sb.append("' is not supported");
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public int size() {
        return 3;
    }
    
    public Date toDate() {
        final int dayOfMonth = this.getDayOfMonth();
        final Date date = new Date(this.getYear() - 1900, this.getMonthOfYear() - 1, dayOfMonth);
        LocalDate localDate = fromDateFields(date);
        if (localDate.isBefore(this)) {
            while (!localDate.equals(this)) {
                date.setTime(date.getTime() + 3600000L);
                localDate = fromDateFields(date);
            }
            while (date.getDate() == dayOfMonth) {
                date.setTime(date.getTime() - 1000L);
            }
            date.setTime(date.getTime() + 1000L);
            return date;
        }
        if (!localDate.equals(this)) {
            return date;
        }
        final Date date2 = new Date(date.getTime() - TimeZone.getDefault().getDSTSavings());
        if (date2.getDate() == dayOfMonth) {
            return date2;
        }
        return date;
    }
    
    @Deprecated
    public DateMidnight toDateMidnight() {
        return this.toDateMidnight(null);
    }
    
    @Deprecated
    public DateMidnight toDateMidnight(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        return new DateMidnight(this.getYear(), this.getMonthOfYear(), this.getDayOfMonth(), this.getChronology().withZone(zone));
    }
    
    public DateTime toDateTime(final LocalTime localTime) {
        return this.toDateTime(localTime, null);
    }
    
    public DateTime toDateTime(final LocalTime localTime, final DateTimeZone dateTimeZone) {
        if (localTime == null) {
            return this.toDateTimeAtCurrentTime(dateTimeZone);
        }
        if (this.getChronology() == localTime.getChronology()) {
            return new DateTime(this.getYear(), this.getMonthOfYear(), this.getDayOfMonth(), localTime.getHourOfDay(), localTime.getMinuteOfHour(), localTime.getSecondOfMinute(), localTime.getMillisOfSecond(), this.getChronology().withZone(dateTimeZone));
        }
        throw new IllegalArgumentException("The chronology of the time does not match");
    }
    
    public DateTime toDateTimeAtCurrentTime() {
        return this.toDateTimeAtCurrentTime(null);
    }
    
    public DateTime toDateTimeAtCurrentTime(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        final Chronology withZone = this.getChronology().withZone(zone);
        return new DateTime(withZone.set((ReadablePartial)this, DateTimeUtils.currentTimeMillis()), withZone);
    }
    
    @Deprecated
    public DateTime toDateTimeAtMidnight() {
        return this.toDateTimeAtMidnight(null);
    }
    
    @Deprecated
    public DateTime toDateTimeAtMidnight(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        return new DateTime(this.getYear(), this.getMonthOfYear(), this.getDayOfMonth(), 0, 0, 0, 0, this.getChronology().withZone(zone));
    }
    
    public DateTime toDateTimeAtStartOfDay() {
        return this.toDateTimeAtStartOfDay(null);
    }
    
    public DateTime toDateTimeAtStartOfDay(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        final Chronology withZone = this.getChronology().withZone(zone);
        return new DateTime(withZone.dayOfMonth().roundFloor(zone.convertLocalToUTC(this.getLocalMillis() + 21600000L, false)), withZone);
    }
    
    public Interval toInterval() {
        return this.toInterval(null);
    }
    
    public Interval toInterval(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        return new Interval((ReadableInstant)this.toDateTimeAtStartOfDay(zone), (ReadableInstant)this.plusDays(1).toDateTimeAtStartOfDay(zone));
    }
    
    public LocalDateTime toLocalDateTime(final LocalTime localTime) {
        if (localTime == null) {
            throw new IllegalArgumentException("The time must not be null");
        }
        if (this.getChronology() == localTime.getChronology()) {
            return new LocalDateTime(this.getLocalMillis() + localTime.getLocalMillis(), this.getChronology());
        }
        throw new IllegalArgumentException("The chronology of the time does not match");
    }
    
    @ToString
    @Override
    public String toString() {
        return ISODateTimeFormat.date().print((ReadablePartial)this);
    }
    
    public String toString(final String s) {
        if (s == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern(s).print((ReadablePartial)this);
    }
    
    public String toString(final String s, final Locale locale) throws IllegalArgumentException {
        if (s == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern(s).withLocale(locale).print((ReadablePartial)this);
    }
    
    public LocalDate.LocalDate$Property weekOfWeekyear() {
        return new LocalDate.LocalDate$Property(this, this.getChronology().weekOfWeekyear());
    }
    
    public LocalDate.LocalDate$Property weekyear() {
        return new LocalDate.LocalDate$Property(this, this.getChronology().weekyear());
    }
    
    public LocalDate withCenturyOfEra(final int n) {
        return this.withLocalMillis(this.getChronology().centuryOfEra().set(this.getLocalMillis(), n));
    }
    
    public LocalDate withDayOfMonth(final int n) {
        return this.withLocalMillis(this.getChronology().dayOfMonth().set(this.getLocalMillis(), n));
    }
    
    public LocalDate withDayOfWeek(final int n) {
        return this.withLocalMillis(this.getChronology().dayOfWeek().set(this.getLocalMillis(), n));
    }
    
    public LocalDate withDayOfYear(final int n) {
        return this.withLocalMillis(this.getChronology().dayOfYear().set(this.getLocalMillis(), n));
    }
    
    public LocalDate withEra(final int n) {
        return this.withLocalMillis(this.getChronology().era().set(this.getLocalMillis(), n));
    }
    
    public LocalDate withField(final DateTimeFieldType dateTimeFieldType, final int n) {
        if (dateTimeFieldType == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        if (this.isSupported(dateTimeFieldType)) {
            return this.withLocalMillis(dateTimeFieldType.getField(this.getChronology()).set(this.getLocalMillis(), n));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Field '");
        sb.append(dateTimeFieldType);
        sb.append("' is not supported");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public LocalDate withFieldAdded(final DurationFieldType durationFieldType, final int n) {
        if (durationFieldType == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        if (!this.isSupported(durationFieldType)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field '");
            sb.append(durationFieldType);
            sb.append("' is not supported");
            throw new IllegalArgumentException(sb.toString());
        }
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(durationFieldType.getField(this.getChronology()).add(this.getLocalMillis(), n));
    }
    
    public LocalDate withFields(final ReadablePartial readablePartial) {
        if (readablePartial == null) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().set(readablePartial, this.getLocalMillis()));
    }
    
    LocalDate withLocalMillis(long roundFloor) {
        roundFloor = this.iChronology.dayOfMonth().roundFloor(roundFloor);
        if (roundFloor == this.getLocalMillis()) {
            return this;
        }
        return new LocalDate(roundFloor, this.getChronology());
    }
    
    public LocalDate withMonthOfYear(final int n) {
        return this.withLocalMillis(this.getChronology().monthOfYear().set(this.getLocalMillis(), n));
    }
    
    public LocalDate withPeriodAdded(final ReadablePeriod readablePeriod, final int n) {
        if (readablePeriod == null) {
            return this;
        }
        if (n == 0) {
            return this;
        }
        long n2 = this.getLocalMillis();
        final Chronology chronology = this.getChronology();
        for (int i = 0; i < readablePeriod.size(); ++i) {
            final long n3 = FieldUtils.safeMultiply(readablePeriod.getValue(i), n);
            final DurationFieldType fieldType = readablePeriod.getFieldType(i);
            if (this.isSupported(fieldType)) {
                n2 = fieldType.getField(chronology).add(n2, n3);
            }
        }
        return this.withLocalMillis(n2);
    }
    
    public LocalDate withWeekOfWeekyear(final int n) {
        return this.withLocalMillis(this.getChronology().weekOfWeekyear().set(this.getLocalMillis(), n));
    }
    
    public LocalDate withWeekyear(final int n) {
        return this.withLocalMillis(this.getChronology().weekyear().set(this.getLocalMillis(), n));
    }
    
    public LocalDate withYear(final int n) {
        return this.withLocalMillis(this.getChronology().year().set(this.getLocalMillis(), n));
    }
    
    public LocalDate withYearOfCentury(final int n) {
        return this.withLocalMillis(this.getChronology().yearOfCentury().set(this.getLocalMillis(), n));
    }
    
    public LocalDate withYearOfEra(final int n) {
        return this.withLocalMillis(this.getChronology().yearOfEra().set(this.getLocalMillis(), n));
    }
    
    public LocalDate.LocalDate$Property year() {
        return new LocalDate.LocalDate$Property(this, this.getChronology().year());
    }
    
    public LocalDate.LocalDate$Property yearOfCentury() {
        return new LocalDate.LocalDate$Property(this, this.getChronology().yearOfCentury());
    }
    
    public LocalDate.LocalDate$Property yearOfEra() {
        return new LocalDate.LocalDate$Property(this, this.getChronology().yearOfEra());
    }
}
