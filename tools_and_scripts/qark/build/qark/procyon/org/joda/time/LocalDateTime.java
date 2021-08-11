// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import java.util.Locale;
import org.joda.time.format.DateTimeFormat;
import org.joda.convert.ToString;
import org.joda.time.format.DateTimeFormatter;
import org.joda.convert.FromString;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Date;
import org.joda.time.convert.PartialConverter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.convert.ConverterManager;
import org.joda.time.chrono.ISOChronology;
import java.io.Serializable;
import org.joda.time.base.BaseLocal;

public final class LocalDateTime extends BaseLocal implements ReadablePartial, Serializable
{
    private static final int DAY_OF_MONTH = 2;
    private static final int MILLIS_OF_DAY = 3;
    private static final int MONTH_OF_YEAR = 1;
    private static final int YEAR = 0;
    private static final long serialVersionUID = -268716875315837168L;
    private final Chronology iChronology;
    private final long iLocalMillis;
    
    public LocalDateTime() {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance());
    }
    
    public LocalDateTime(final int n, final int n2, final int n3, final int n4, final int n5) {
        this(n, n2, n3, n4, n5, 0, 0, (Chronology)ISOChronology.getInstanceUTC());
    }
    
    public LocalDateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        this(n, n2, n3, n4, n5, n6, 0, (Chronology)ISOChronology.getInstanceUTC());
    }
    
    public LocalDateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7) {
        this(n, n2, n3, n4, n5, n6, n7, (Chronology)ISOChronology.getInstanceUTC());
    }
    
    public LocalDateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, Chronology withUTC) {
        withUTC = DateTimeUtils.getChronology(withUTC).withUTC();
        final long dateTimeMillis = withUTC.getDateTimeMillis(n, n2, n3, n4, n5, n6, n7);
        this.iChronology = withUTC;
        this.iLocalMillis = dateTimeMillis;
    }
    
    public LocalDateTime(final long n) {
        this(n, (Chronology)ISOChronology.getInstance());
    }
    
    public LocalDateTime(final long n, Chronology chronology) {
        chronology = DateTimeUtils.getChronology(chronology);
        this.iLocalMillis = chronology.getZone().getMillisKeepLocal(DateTimeZone.UTC, n);
        this.iChronology = chronology.withUTC();
    }
    
    public LocalDateTime(final long n, final DateTimeZone dateTimeZone) {
        this(n, (Chronology)ISOChronology.getInstance(dateTimeZone));
    }
    
    public LocalDateTime(final Object o) {
        this(o, (Chronology)null);
    }
    
    public LocalDateTime(final Object o, Chronology chronology) {
        final PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter(o);
        chronology = DateTimeUtils.getChronology(partialConverter.getChronology(o, chronology));
        this.iChronology = chronology.withUTC();
        final int[] partialValues = partialConverter.getPartialValues((ReadablePartial)this, o, chronology, ISODateTimeFormat.localDateOptionalTimeParser());
        this.iLocalMillis = this.iChronology.getDateTimeMillis(partialValues[0], partialValues[1], partialValues[2], partialValues[3]);
    }
    
    public LocalDateTime(final Object o, final DateTimeZone dateTimeZone) {
        final PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter(o);
        final Chronology chronology = DateTimeUtils.getChronology(partialConverter.getChronology(o, dateTimeZone));
        this.iChronology = chronology.withUTC();
        final int[] partialValues = partialConverter.getPartialValues((ReadablePartial)this, o, chronology, ISODateTimeFormat.localDateOptionalTimeParser());
        this.iLocalMillis = this.iChronology.getDateTimeMillis(partialValues[0], partialValues[1], partialValues[2], partialValues[3]);
    }
    
    public LocalDateTime(final Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }
    
    public LocalDateTime(final DateTimeZone dateTimeZone) {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance(dateTimeZone));
    }
    
    private Date correctDstTransition(final Date time, final TimeZone timeZone) {
        final Calendar instance = Calendar.getInstance(timeZone);
        instance.setTime(time);
        LocalDateTime localDateTime = fromCalendarFields(instance);
        if (localDateTime.isBefore(this)) {
            while (localDateTime.isBefore(this)) {
                instance.setTimeInMillis(instance.getTimeInMillis() + 60000L);
                localDateTime = fromCalendarFields(instance);
            }
            while (!localDateTime.isBefore(this)) {
                instance.setTimeInMillis(instance.getTimeInMillis() - 1000L);
                localDateTime = fromCalendarFields(instance);
            }
            instance.setTimeInMillis(instance.getTimeInMillis() + 1000L);
        }
        else if (localDateTime.equals(this)) {
            final Calendar instance2 = Calendar.getInstance(timeZone);
            instance2.setTimeInMillis(instance.getTimeInMillis() - timeZone.getDSTSavings());
            if (fromCalendarFields(instance2).equals(this)) {
                return instance2.getTime();
            }
        }
        final Calendar instance2 = instance;
        return instance2.getTime();
    }
    
    public static LocalDateTime fromCalendarFields(final Calendar calendar) {
        if (calendar != null) {
            final int value = calendar.get(0);
            int value2 = calendar.get(1);
            if (value != 1) {
                value2 = 1 - value2;
            }
            return new LocalDateTime(value2, calendar.get(2) + 1, calendar.get(5), calendar.get(11), calendar.get(12), calendar.get(13), calendar.get(14));
        }
        throw new IllegalArgumentException("The calendar must not be null");
    }
    
    public static LocalDateTime fromDateFields(final Date time) {
        if (time == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        if (time.getTime() < 0L) {
            final GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(time);
            return fromCalendarFields(gregorianCalendar);
        }
        return new LocalDateTime(time.getYear() + 1900, time.getMonth() + 1, time.getDate(), time.getHours(), time.getMinutes(), time.getSeconds(), ((int)(time.getTime() % 1000L) + 1000) % 1000);
    }
    
    public static LocalDateTime now() {
        return new LocalDateTime();
    }
    
    public static LocalDateTime now(final Chronology chronology) {
        if (chronology != null) {
            return new LocalDateTime(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }
    
    public static LocalDateTime now(final DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new LocalDateTime(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }
    
    @FromString
    public static LocalDateTime parse(final String s) {
        return parse(s, ISODateTimeFormat.localDateOptionalTimeParser());
    }
    
    public static LocalDateTime parse(final String s, final DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.parseLocalDateTime(s);
    }
    
    private Object readResolve() {
        if (this.iChronology == null) {
            return new LocalDateTime(this.iLocalMillis, (Chronology)ISOChronology.getInstanceUTC());
        }
        if (!DateTimeZone.UTC.equals(this.iChronology.getZone())) {
            return new LocalDateTime(this.iLocalMillis, this.iChronology.withUTC());
        }
        return this;
    }
    
    public LocalDateTime.LocalDateTime$Property centuryOfEra() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().centuryOfEra());
    }
    
    @Override
    public int compareTo(final ReadablePartial readablePartial) {
        if (this == readablePartial) {
            return 0;
        }
        if (readablePartial instanceof LocalDateTime) {
            final LocalDateTime localDateTime = (LocalDateTime)readablePartial;
            if (this.iChronology.equals(localDateTime.iChronology)) {
                final long iLocalMillis = this.iLocalMillis;
                final long iLocalMillis2 = localDateTime.iLocalMillis;
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
    
    public LocalDateTime.LocalDateTime$Property dayOfMonth() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().dayOfMonth());
    }
    
    public LocalDateTime.LocalDateTime$Property dayOfWeek() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().dayOfWeek());
    }
    
    public LocalDateTime.LocalDateTime$Property dayOfYear() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().dayOfYear());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof LocalDateTime) {
            final LocalDateTime localDateTime = (LocalDateTime)o;
            if (this.iChronology.equals(localDateTime.iChronology)) {
                return this.iLocalMillis == localDateTime.iLocalMillis;
            }
        }
        return super.equals(o);
    }
    
    public LocalDateTime.LocalDateTime$Property era() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().era());
    }
    
    @Override
    public int get(final DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType != null) {
            return dateTimeFieldType.getField(this.getChronology()).get(this.getLocalMillis());
        }
        throw new IllegalArgumentException("The DateTimeFieldType must not be null");
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
            case 3: {
                return chronology.millisOfDay();
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
    
    public int getHourOfDay() {
        return this.getChronology().hourOfDay().get(this.getLocalMillis());
    }
    
    @Override
    protected long getLocalMillis() {
        return this.iLocalMillis;
    }
    
    public int getMillisOfDay() {
        return this.getChronology().millisOfDay().get(this.getLocalMillis());
    }
    
    public int getMillisOfSecond() {
        return this.getChronology().millisOfSecond().get(this.getLocalMillis());
    }
    
    public int getMinuteOfHour() {
        return this.getChronology().minuteOfHour().get(this.getLocalMillis());
    }
    
    public int getMonthOfYear() {
        return this.getChronology().monthOfYear().get(this.getLocalMillis());
    }
    
    public int getSecondOfMinute() {
        return this.getChronology().secondOfMinute().get(this.getLocalMillis());
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
            case 3: {
                return this.getChronology().millisOfDay().get(this.getLocalMillis());
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
    
    public LocalDateTime.LocalDateTime$Property hourOfDay() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().hourOfDay());
    }
    
    @Override
    public boolean isSupported(final DateTimeFieldType dateTimeFieldType) {
        return dateTimeFieldType != null && dateTimeFieldType.getField(this.getChronology()).isSupported();
    }
    
    public boolean isSupported(final DurationFieldType durationFieldType) {
        return durationFieldType != null && durationFieldType.getField(this.getChronology()).isSupported();
    }
    
    public LocalDateTime.LocalDateTime$Property millisOfDay() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().millisOfDay());
    }
    
    public LocalDateTime.LocalDateTime$Property millisOfSecond() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().millisOfSecond());
    }
    
    public LocalDateTime minus(final ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, -1);
    }
    
    public LocalDateTime minus(final ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, -1);
    }
    
    public LocalDateTime minusDays(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().days().subtract(this.getLocalMillis(), n));
    }
    
    public LocalDateTime minusHours(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().hours().subtract(this.getLocalMillis(), n));
    }
    
    public LocalDateTime minusMillis(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().millis().subtract(this.getLocalMillis(), n));
    }
    
    public LocalDateTime minusMinutes(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().minutes().subtract(this.getLocalMillis(), n));
    }
    
    public LocalDateTime minusMonths(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().months().subtract(this.getLocalMillis(), n));
    }
    
    public LocalDateTime minusSeconds(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().seconds().subtract(this.getLocalMillis(), n));
    }
    
    public LocalDateTime minusWeeks(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().weeks().subtract(this.getLocalMillis(), n));
    }
    
    public LocalDateTime minusYears(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().years().subtract(this.getLocalMillis(), n));
    }
    
    public LocalDateTime.LocalDateTime$Property minuteOfHour() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().minuteOfHour());
    }
    
    public LocalDateTime.LocalDateTime$Property monthOfYear() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().monthOfYear());
    }
    
    public LocalDateTime plus(final ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, 1);
    }
    
    public LocalDateTime plus(final ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, 1);
    }
    
    public LocalDateTime plusDays(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().days().add(this.getLocalMillis(), n));
    }
    
    public LocalDateTime plusHours(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().hours().add(this.getLocalMillis(), n));
    }
    
    public LocalDateTime plusMillis(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().millis().add(this.getLocalMillis(), n));
    }
    
    public LocalDateTime plusMinutes(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().minutes().add(this.getLocalMillis(), n));
    }
    
    public LocalDateTime plusMonths(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().months().add(this.getLocalMillis(), n));
    }
    
    public LocalDateTime plusSeconds(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().seconds().add(this.getLocalMillis(), n));
    }
    
    public LocalDateTime plusWeeks(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().weeks().add(this.getLocalMillis(), n));
    }
    
    public LocalDateTime plusYears(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().years().add(this.getLocalMillis(), n));
    }
    
    public LocalDateTime.LocalDateTime$Property property(final DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType == null) {
            throw new IllegalArgumentException("The DateTimeFieldType must not be null");
        }
        if (this.isSupported(dateTimeFieldType)) {
            return new LocalDateTime.LocalDateTime$Property(this, dateTimeFieldType.getField(this.getChronology()));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Field '");
        sb.append(dateTimeFieldType);
        sb.append("' is not supported");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public LocalDateTime.LocalDateTime$Property secondOfMinute() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().secondOfMinute());
    }
    
    @Override
    public int size() {
        return 4;
    }
    
    public Date toDate() {
        final Date date = new Date(this.getYear() - 1900, this.getMonthOfYear() - 1, this.getDayOfMonth(), this.getHourOfDay(), this.getMinuteOfHour(), this.getSecondOfMinute());
        date.setTime(date.getTime() + this.getMillisOfSecond());
        return this.correctDstTransition(date, TimeZone.getDefault());
    }
    
    public Date toDate(final TimeZone timeZone) {
        final Calendar instance = Calendar.getInstance(timeZone);
        instance.clear();
        instance.set(this.getYear(), this.getMonthOfYear() - 1, this.getDayOfMonth(), this.getHourOfDay(), this.getMinuteOfHour(), this.getSecondOfMinute());
        final Date time = instance.getTime();
        time.setTime(time.getTime() + this.getMillisOfSecond());
        return this.correctDstTransition(time, timeZone);
    }
    
    public DateTime toDateTime() {
        return this.toDateTime((DateTimeZone)null);
    }
    
    public DateTime toDateTime(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        return new DateTime(this.getYear(), this.getMonthOfYear(), this.getDayOfMonth(), this.getHourOfDay(), this.getMinuteOfHour(), this.getSecondOfMinute(), this.getMillisOfSecond(), this.iChronology.withZone(zone));
    }
    
    public LocalDate toLocalDate() {
        return new LocalDate(this.getLocalMillis(), this.getChronology());
    }
    
    public LocalTime toLocalTime() {
        return new LocalTime(this.getLocalMillis(), this.getChronology());
    }
    
    @ToString
    @Override
    public String toString() {
        return ISODateTimeFormat.dateTime().print((ReadablePartial)this);
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
    
    public LocalDateTime.LocalDateTime$Property weekOfWeekyear() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().weekOfWeekyear());
    }
    
    public LocalDateTime.LocalDateTime$Property weekyear() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().weekyear());
    }
    
    public LocalDateTime withCenturyOfEra(final int n) {
        return this.withLocalMillis(this.getChronology().centuryOfEra().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withDate(final int n, final int n2, final int n3) {
        final Chronology chronology = this.getChronology();
        return this.withLocalMillis(chronology.dayOfMonth().set(chronology.monthOfYear().set(chronology.year().set(this.getLocalMillis(), n), n2), n3));
    }
    
    public LocalDateTime withDayOfMonth(final int n) {
        return this.withLocalMillis(this.getChronology().dayOfMonth().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withDayOfWeek(final int n) {
        return this.withLocalMillis(this.getChronology().dayOfWeek().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withDayOfYear(final int n) {
        return this.withLocalMillis(this.getChronology().dayOfYear().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withDurationAdded(final ReadableDuration readableDuration, final int n) {
        if (readableDuration == null) {
            return this;
        }
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().add(this.getLocalMillis(), readableDuration.getMillis(), n));
    }
    
    public LocalDateTime withEra(final int n) {
        return this.withLocalMillis(this.getChronology().era().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withField(final DateTimeFieldType dateTimeFieldType, final int n) {
        if (dateTimeFieldType != null) {
            return this.withLocalMillis(dateTimeFieldType.getField(this.getChronology()).set(this.getLocalMillis(), n));
        }
        throw new IllegalArgumentException("Field must not be null");
    }
    
    public LocalDateTime withFieldAdded(final DurationFieldType durationFieldType, final int n) {
        if (durationFieldType == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(durationFieldType.getField(this.getChronology()).add(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withFields(final ReadablePartial readablePartial) {
        if (readablePartial == null) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().set(readablePartial, this.getLocalMillis()));
    }
    
    public LocalDateTime withHourOfDay(final int n) {
        return this.withLocalMillis(this.getChronology().hourOfDay().set(this.getLocalMillis(), n));
    }
    
    LocalDateTime withLocalMillis(final long n) {
        if (n == this.getLocalMillis()) {
            return this;
        }
        return new LocalDateTime(n, this.getChronology());
    }
    
    public LocalDateTime withMillisOfDay(final int n) {
        return this.withLocalMillis(this.getChronology().millisOfDay().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withMillisOfSecond(final int n) {
        return this.withLocalMillis(this.getChronology().millisOfSecond().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withMinuteOfHour(final int n) {
        return this.withLocalMillis(this.getChronology().minuteOfHour().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withMonthOfYear(final int n) {
        return this.withLocalMillis(this.getChronology().monthOfYear().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withPeriodAdded(final ReadablePeriod readablePeriod, final int n) {
        if (readablePeriod == null) {
            return this;
        }
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().add(readablePeriod, this.getLocalMillis(), n));
    }
    
    public LocalDateTime withSecondOfMinute(final int n) {
        return this.withLocalMillis(this.getChronology().secondOfMinute().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withTime(final int n, final int n2, final int n3, final int n4) {
        final Chronology chronology = this.getChronology();
        return this.withLocalMillis(chronology.millisOfSecond().set(chronology.secondOfMinute().set(chronology.minuteOfHour().set(chronology.hourOfDay().set(this.getLocalMillis(), n), n2), n3), n4));
    }
    
    public LocalDateTime withWeekOfWeekyear(final int n) {
        return this.withLocalMillis(this.getChronology().weekOfWeekyear().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withWeekyear(final int n) {
        return this.withLocalMillis(this.getChronology().weekyear().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withYear(final int n) {
        return this.withLocalMillis(this.getChronology().year().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withYearOfCentury(final int n) {
        return this.withLocalMillis(this.getChronology().yearOfCentury().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime withYearOfEra(final int n) {
        return this.withLocalMillis(this.getChronology().yearOfEra().set(this.getLocalMillis(), n));
    }
    
    public LocalDateTime.LocalDateTime$Property year() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().year());
    }
    
    public LocalDateTime.LocalDateTime$Property yearOfCentury() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().yearOfCentury());
    }
    
    public LocalDateTime.LocalDateTime$Property yearOfEra() {
        return new LocalDateTime.LocalDateTime$Property(this, this.getChronology().yearOfEra());
    }
}
