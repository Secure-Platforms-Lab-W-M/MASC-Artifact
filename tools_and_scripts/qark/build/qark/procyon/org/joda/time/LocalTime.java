// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import java.util.Locale;
import org.joda.time.format.DateTimeFormat;
import org.joda.convert.ToString;
import org.joda.time.format.DateTimeFormatter;
import org.joda.convert.FromString;
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

public final class LocalTime extends BaseLocal implements ReadablePartial, Serializable
{
    private static final int HOUR_OF_DAY = 0;
    public static final LocalTime MIDNIGHT;
    private static final int MILLIS_OF_SECOND = 3;
    private static final int MINUTE_OF_HOUR = 1;
    private static final int SECOND_OF_MINUTE = 2;
    private static final Set<DurationFieldType> TIME_DURATION_TYPES;
    private static final long serialVersionUID = -12873158713873L;
    private final Chronology iChronology;
    private final long iLocalMillis;
    
    static {
        MIDNIGHT = new LocalTime(0, 0, 0, 0);
        (TIME_DURATION_TYPES = new HashSet<DurationFieldType>()).add(DurationFieldType.millis());
        LocalTime.TIME_DURATION_TYPES.add(DurationFieldType.seconds());
        LocalTime.TIME_DURATION_TYPES.add(DurationFieldType.minutes());
        LocalTime.TIME_DURATION_TYPES.add(DurationFieldType.hours());
    }
    
    public LocalTime() {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance());
    }
    
    public LocalTime(final int n, final int n2) {
        this(n, n2, 0, 0, (Chronology)ISOChronology.getInstanceUTC());
    }
    
    public LocalTime(final int n, final int n2, final int n3) {
        this(n, n2, n3, 0, (Chronology)ISOChronology.getInstanceUTC());
    }
    
    public LocalTime(final int n, final int n2, final int n3, final int n4) {
        this(n, n2, n3, n4, (Chronology)ISOChronology.getInstanceUTC());
    }
    
    public LocalTime(final int n, final int n2, final int n3, final int n4, Chronology withUTC) {
        withUTC = DateTimeUtils.getChronology(withUTC).withUTC();
        final long dateTimeMillis = withUTC.getDateTimeMillis(0L, n, n2, n3, n4);
        this.iChronology = withUTC;
        this.iLocalMillis = dateTimeMillis;
    }
    
    public LocalTime(final long n) {
        this(n, (Chronology)ISOChronology.getInstance());
    }
    
    public LocalTime(long millisKeepLocal, Chronology iChronology) {
        iChronology = DateTimeUtils.getChronology(iChronology);
        millisKeepLocal = iChronology.getZone().getMillisKeepLocal(DateTimeZone.UTC, millisKeepLocal);
        iChronology = iChronology.withUTC();
        this.iLocalMillis = iChronology.millisOfDay().get(millisKeepLocal);
        this.iChronology = iChronology;
    }
    
    public LocalTime(final long n, final DateTimeZone dateTimeZone) {
        this(n, (Chronology)ISOChronology.getInstance(dateTimeZone));
    }
    
    public LocalTime(final Object o) {
        this(o, (Chronology)null);
    }
    
    public LocalTime(final Object o, Chronology chronology) {
        final PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter(o);
        chronology = DateTimeUtils.getChronology(partialConverter.getChronology(o, chronology));
        this.iChronology = chronology.withUTC();
        final int[] partialValues = partialConverter.getPartialValues((ReadablePartial)this, o, chronology, ISODateTimeFormat.localTimeParser());
        this.iLocalMillis = this.iChronology.getDateTimeMillis(0L, partialValues[0], partialValues[1], partialValues[2], partialValues[3]);
    }
    
    public LocalTime(final Object o, final DateTimeZone dateTimeZone) {
        final PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter(o);
        final Chronology chronology = DateTimeUtils.getChronology(partialConverter.getChronology(o, dateTimeZone));
        this.iChronology = chronology.withUTC();
        final int[] partialValues = partialConverter.getPartialValues((ReadablePartial)this, o, chronology, ISODateTimeFormat.localTimeParser());
        this.iLocalMillis = this.iChronology.getDateTimeMillis(0L, partialValues[0], partialValues[1], partialValues[2], partialValues[3]);
    }
    
    public LocalTime(final Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }
    
    public LocalTime(final DateTimeZone dateTimeZone) {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance(dateTimeZone));
    }
    
    public static LocalTime fromCalendarFields(final Calendar calendar) {
        if (calendar != null) {
            return new LocalTime(calendar.get(11), calendar.get(12), calendar.get(13), calendar.get(14));
        }
        throw new IllegalArgumentException("The calendar must not be null");
    }
    
    public static LocalTime fromDateFields(final Date date) {
        if (date != null) {
            return new LocalTime(date.getHours(), date.getMinutes(), date.getSeconds(), ((int)(date.getTime() % 1000L) + 1000) % 1000);
        }
        throw new IllegalArgumentException("The date must not be null");
    }
    
    public static LocalTime fromMillisOfDay(final long n) {
        return fromMillisOfDay(n, null);
    }
    
    public static LocalTime fromMillisOfDay(final long n, final Chronology chronology) {
        return new LocalTime(n, DateTimeUtils.getChronology(chronology).withUTC());
    }
    
    public static LocalTime now() {
        return new LocalTime();
    }
    
    public static LocalTime now(final Chronology chronology) {
        if (chronology != null) {
            return new LocalTime(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }
    
    public static LocalTime now(final DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new LocalTime(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }
    
    @FromString
    public static LocalTime parse(final String s) {
        return parse(s, ISODateTimeFormat.localTimeParser());
    }
    
    public static LocalTime parse(final String s, final DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.parseLocalTime(s);
    }
    
    private Object readResolve() {
        if (this.iChronology == null) {
            return new LocalTime(this.iLocalMillis, (Chronology)ISOChronology.getInstanceUTC());
        }
        if (!DateTimeZone.UTC.equals(this.iChronology.getZone())) {
            return new LocalTime(this.iLocalMillis, this.iChronology.withUTC());
        }
        return this;
    }
    
    @Override
    public int compareTo(final ReadablePartial readablePartial) {
        if (this == readablePartial) {
            return 0;
        }
        if (readablePartial instanceof LocalTime) {
            final LocalTime localTime = (LocalTime)readablePartial;
            if (this.iChronology.equals(localTime.iChronology)) {
                final long iLocalMillis = this.iLocalMillis;
                final long iLocalMillis2 = localTime.iLocalMillis;
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
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof LocalTime) {
            final LocalTime localTime = (LocalTime)o;
            if (this.iChronology.equals(localTime.iChronology)) {
                return this.iLocalMillis == localTime.iLocalMillis;
            }
        }
        return super.equals(o);
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
    
    @Override
    public Chronology getChronology() {
        return this.iChronology;
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
                return chronology.millisOfSecond();
            }
            case 2: {
                return chronology.secondOfMinute();
            }
            case 1: {
                return chronology.minuteOfHour();
            }
            case 0: {
                return chronology.hourOfDay();
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
                return this.getChronology().millisOfSecond().get(this.getLocalMillis());
            }
            case 2: {
                return this.getChronology().secondOfMinute().get(this.getLocalMillis());
            }
            case 1: {
                return this.getChronology().minuteOfHour().get(this.getLocalMillis());
            }
            case 0: {
                return this.getChronology().hourOfDay().get(this.getLocalMillis());
            }
        }
    }
    
    public LocalTime.LocalTime$Property hourOfDay() {
        return new LocalTime.LocalTime$Property(this, this.getChronology().hourOfDay());
    }
    
    @Override
    public boolean isSupported(final DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType == null) {
            return false;
        }
        if (!this.isSupported(dateTimeFieldType.getDurationType())) {
            return false;
        }
        final DurationFieldType rangeDurationType = dateTimeFieldType.getRangeDurationType();
        return this.isSupported(rangeDurationType) || rangeDurationType == DurationFieldType.days();
    }
    
    public boolean isSupported(final DurationFieldType durationFieldType) {
        if (durationFieldType == null) {
            return false;
        }
        final DurationField field = durationFieldType.getField(this.getChronology());
        return (LocalTime.TIME_DURATION_TYPES.contains(durationFieldType) || field.getUnitMillis() < this.getChronology().days().getUnitMillis()) && field.isSupported();
    }
    
    public LocalTime.LocalTime$Property millisOfDay() {
        return new LocalTime.LocalTime$Property(this, this.getChronology().millisOfDay());
    }
    
    public LocalTime.LocalTime$Property millisOfSecond() {
        return new LocalTime.LocalTime$Property(this, this.getChronology().millisOfSecond());
    }
    
    public LocalTime minus(final ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, -1);
    }
    
    public LocalTime minusHours(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().hours().subtract(this.getLocalMillis(), n));
    }
    
    public LocalTime minusMillis(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().millis().subtract(this.getLocalMillis(), n));
    }
    
    public LocalTime minusMinutes(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().minutes().subtract(this.getLocalMillis(), n));
    }
    
    public LocalTime minusSeconds(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().seconds().subtract(this.getLocalMillis(), n));
    }
    
    public LocalTime.LocalTime$Property minuteOfHour() {
        return new LocalTime.LocalTime$Property(this, this.getChronology().minuteOfHour());
    }
    
    public LocalTime plus(final ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, 1);
    }
    
    public LocalTime plusHours(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().hours().add(this.getLocalMillis(), n));
    }
    
    public LocalTime plusMillis(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().millis().add(this.getLocalMillis(), n));
    }
    
    public LocalTime plusMinutes(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().minutes().add(this.getLocalMillis(), n));
    }
    
    public LocalTime plusSeconds(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().seconds().add(this.getLocalMillis(), n));
    }
    
    public LocalTime.LocalTime$Property property(final DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType == null) {
            throw new IllegalArgumentException("The DateTimeFieldType must not be null");
        }
        if (this.isSupported(dateTimeFieldType)) {
            return new LocalTime.LocalTime$Property(this, dateTimeFieldType.getField(this.getChronology()));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Field '");
        sb.append(dateTimeFieldType);
        sb.append("' is not supported");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public LocalTime.LocalTime$Property secondOfMinute() {
        return new LocalTime.LocalTime$Property(this, this.getChronology().secondOfMinute());
    }
    
    @Override
    public int size() {
        return 4;
    }
    
    public DateTime toDateTimeToday() {
        return this.toDateTimeToday(null);
    }
    
    public DateTime toDateTimeToday(final DateTimeZone dateTimeZone) {
        final Chronology withZone = this.getChronology().withZone(dateTimeZone);
        return new DateTime(withZone.set((ReadablePartial)this, DateTimeUtils.currentTimeMillis()), withZone);
    }
    
    @ToString
    @Override
    public String toString() {
        return ISODateTimeFormat.time().print((ReadablePartial)this);
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
    
    public LocalTime withField(final DateTimeFieldType dateTimeFieldType, final int n) {
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
    
    public LocalTime withFieldAdded(final DurationFieldType durationFieldType, final int n) {
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
    
    public LocalTime withFields(final ReadablePartial readablePartial) {
        if (readablePartial == null) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().set(readablePartial, this.getLocalMillis()));
    }
    
    public LocalTime withHourOfDay(final int n) {
        return this.withLocalMillis(this.getChronology().hourOfDay().set(this.getLocalMillis(), n));
    }
    
    LocalTime withLocalMillis(final long n) {
        if (n == this.getLocalMillis()) {
            return this;
        }
        return new LocalTime(n, this.getChronology());
    }
    
    public LocalTime withMillisOfDay(final int n) {
        return this.withLocalMillis(this.getChronology().millisOfDay().set(this.getLocalMillis(), n));
    }
    
    public LocalTime withMillisOfSecond(final int n) {
        return this.withLocalMillis(this.getChronology().millisOfSecond().set(this.getLocalMillis(), n));
    }
    
    public LocalTime withMinuteOfHour(final int n) {
        return this.withLocalMillis(this.getChronology().minuteOfHour().set(this.getLocalMillis(), n));
    }
    
    public LocalTime withPeriodAdded(final ReadablePeriod readablePeriod, final int n) {
        if (readablePeriod == null) {
            return this;
        }
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().add(readablePeriod, this.getLocalMillis(), n));
    }
    
    public LocalTime withSecondOfMinute(final int n) {
        return this.withLocalMillis(this.getChronology().secondOfMinute().set(this.getLocalMillis(), n));
    }
}
