/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.FromString
 *  org.joda.convert.ToString
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeField
 *  org.joda.time.DateTimeFieldType
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.DurationField
 *  org.joda.time.DurationFieldType
 *  org.joda.time.LocalTime$Property
 *  org.joda.time.chrono.ISOChronology
 *  org.joda.time.convert.ConverterManager
 *  org.joda.time.convert.PartialConverter
 *  org.joda.time.format.DateTimeFormat
 *  org.joda.time.format.DateTimeFormatter
 *  org.joda.time.format.ISODateTimeFormat
 */
package org.joda.time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalTime;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.base.BaseLocal;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PartialConverter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public final class LocalTime
extends BaseLocal
implements ReadablePartial,
Serializable {
    private static final int HOUR_OF_DAY = 0;
    public static final LocalTime MIDNIGHT = new LocalTime(0, 0, 0, 0);
    private static final int MILLIS_OF_SECOND = 3;
    private static final int MINUTE_OF_HOUR = 1;
    private static final int SECOND_OF_MINUTE = 2;
    private static final Set<DurationFieldType> TIME_DURATION_TYPES = new HashSet<DurationFieldType>();
    private static final long serialVersionUID = -12873158713873L;
    private final Chronology iChronology;
    private final long iLocalMillis;

    static {
        TIME_DURATION_TYPES.add(DurationFieldType.millis());
        TIME_DURATION_TYPES.add(DurationFieldType.seconds());
        TIME_DURATION_TYPES.add(DurationFieldType.minutes());
        TIME_DURATION_TYPES.add(DurationFieldType.hours());
    }

    public LocalTime() {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance());
    }

    public LocalTime(int n, int n2) {
        this(n, n2, 0, 0, (Chronology)ISOChronology.getInstanceUTC());
    }

    public LocalTime(int n, int n2, int n3) {
        this(n, n2, n3, 0, (Chronology)ISOChronology.getInstanceUTC());
    }

    public LocalTime(int n, int n2, int n3, int n4) {
        this(n, n2, n3, n4, (Chronology)ISOChronology.getInstanceUTC());
    }

    public LocalTime(int n, int n2, int n3, int n4, Chronology chronology) {
        chronology = DateTimeUtils.getChronology((Chronology)chronology).withUTC();
        long l = chronology.getDateTimeMillis(0L, n, n2, n3, n4);
        this.iChronology = chronology;
        this.iLocalMillis = l;
    }

    public LocalTime(long l) {
        this(l, (Chronology)ISOChronology.getInstance());
    }

    public LocalTime(long l, Chronology chronology) {
        chronology = DateTimeUtils.getChronology((Chronology)chronology);
        l = chronology.getZone().getMillisKeepLocal(DateTimeZone.UTC, l);
        chronology = chronology.withUTC();
        this.iLocalMillis = chronology.millisOfDay().get(l);
        this.iChronology = chronology;
    }

    public LocalTime(long l, DateTimeZone dateTimeZone) {
        this(l, (Chronology)ISOChronology.getInstance((DateTimeZone)dateTimeZone));
    }

    public LocalTime(Object object) {
        this(object, (Chronology)null);
    }

    public LocalTime(Object arrn, Chronology chronology) {
        PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter((Object)arrn);
        chronology = DateTimeUtils.getChronology((Chronology)partialConverter.getChronology((Object)arrn, chronology));
        this.iChronology = chronology.withUTC();
        arrn = partialConverter.getPartialValues((ReadablePartial)this, (Object)arrn, chronology, ISODateTimeFormat.localTimeParser());
        this.iLocalMillis = this.iChronology.getDateTimeMillis(0L, arrn[0], arrn[1], arrn[2], arrn[3]);
    }

    public LocalTime(Object arrn, DateTimeZone dateTimeZone) {
        PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter((Object)arrn);
        dateTimeZone = DateTimeUtils.getChronology((Chronology)partialConverter.getChronology((Object)arrn, dateTimeZone));
        this.iChronology = dateTimeZone.withUTC();
        arrn = partialConverter.getPartialValues((ReadablePartial)this, (Object)arrn, (Chronology)dateTimeZone, ISODateTimeFormat.localTimeParser());
        this.iLocalMillis = this.iChronology.getDateTimeMillis(0L, arrn[0], arrn[1], arrn[2], arrn[3]);
    }

    public LocalTime(Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }

    public LocalTime(DateTimeZone dateTimeZone) {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance((DateTimeZone)dateTimeZone));
    }

    public static LocalTime fromCalendarFields(Calendar calendar) {
        if (calendar != null) {
            return new LocalTime(calendar.get(11), calendar.get(12), calendar.get(13), calendar.get(14));
        }
        throw new IllegalArgumentException("The calendar must not be null");
    }

    public static LocalTime fromDateFields(Date date) {
        if (date != null) {
            return new LocalTime(date.getHours(), date.getMinutes(), date.getSeconds(), ((int)(date.getTime() % 1000L) + 1000) % 1000);
        }
        throw new IllegalArgumentException("The date must not be null");
    }

    public static LocalTime fromMillisOfDay(long l) {
        return LocalTime.fromMillisOfDay(l, null);
    }

    public static LocalTime fromMillisOfDay(long l, Chronology chronology) {
        return new LocalTime(l, DateTimeUtils.getChronology((Chronology)chronology).withUTC());
    }

    public static LocalTime now() {
        return new LocalTime();
    }

    public static LocalTime now(Chronology chronology) {
        if (chronology != null) {
            return new LocalTime(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }

    public static LocalTime now(DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new LocalTime(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }

    @FromString
    public static LocalTime parse(String string) {
        return LocalTime.parse(string, ISODateTimeFormat.localTimeParser());
    }

    public static LocalTime parse(String string, DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.parseLocalTime(string);
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
    public int compareTo(ReadablePartial readablePartial) {
        if (this == readablePartial) {
            return 0;
        }
        if (readablePartial instanceof LocalTime) {
            LocalTime localTime = (LocalTime)readablePartial;
            if (this.iChronology.equals((Object)localTime.iChronology)) {
                long l = this.iLocalMillis;
                long l2 = localTime.iLocalMillis;
                if (l < l2) {
                    return -1;
                }
                if (l == l2) {
                    return 0;
                }
                return 1;
            }
        }
        return super.compareTo(readablePartial);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof LocalTime) {
            LocalTime localTime = (LocalTime)object;
            if (this.iChronology.equals((Object)localTime.iChronology)) {
                if (this.iLocalMillis == localTime.iLocalMillis) {
                    return true;
                }
                return false;
            }
        }
        return super.equals(object);
    }

    @Override
    public int get(DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType != null) {
            if (this.isSupported(dateTimeFieldType)) {
                return dateTimeFieldType.getField(this.getChronology()).get(this.getLocalMillis());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Field '");
            stringBuilder.append((Object)dateTimeFieldType);
            stringBuilder.append("' is not supported");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        throw new IllegalArgumentException("The DateTimeFieldType must not be null");
    }

    @Override
    public Chronology getChronology() {
        return this.iChronology;
    }

    @Override
    protected DateTimeField getField(int n, Chronology object) {
        switch (n) {
            default: {
                object = new StringBuilder();
                object.append("Invalid index: ");
                object.append(n);
                throw new IndexOutOfBoundsException(object.toString());
            }
            case 3: {
                return object.millisOfSecond();
            }
            case 2: {
                return object.secondOfMinute();
            }
            case 1: {
                return object.minuteOfHour();
            }
            case 0: 
        }
        return object.hourOfDay();
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
    public int getValue(int n) {
        switch (n) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid index: ");
                stringBuilder.append(n);
                throw new IndexOutOfBoundsException(stringBuilder.toString());
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
            case 0: 
        }
        return this.getChronology().hourOfDay().get(this.getLocalMillis());
    }

    public  hourOfDay() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    @Override
    public boolean isSupported(DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType == null) {
            return false;
        }
        if (!this.isSupported(dateTimeFieldType.getDurationType())) {
            return false;
        }
        if (!this.isSupported((DurationFieldType)(dateTimeFieldType = dateTimeFieldType.getRangeDurationType())) && dateTimeFieldType != DurationFieldType.days()) {
            return false;
        }
        return true;
    }

    public boolean isSupported(DurationFieldType durationFieldType) {
        if (durationFieldType == null) {
            return false;
        }
        DurationField durationField = durationFieldType.getField(this.getChronology());
        if (!TIME_DURATION_TYPES.contains((Object)durationFieldType) && durationField.getUnitMillis() >= this.getChronology().days().getUnitMillis()) {
            return false;
        }
        return durationField.isSupported();
    }

    public  millisOfDay() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  millisOfSecond() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public LocalTime minus(ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, -1);
    }

    public LocalTime minusHours(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().hours().subtract(this.getLocalMillis(), n));
    }

    public LocalTime minusMillis(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().millis().subtract(this.getLocalMillis(), n));
    }

    public LocalTime minusMinutes(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().minutes().subtract(this.getLocalMillis(), n));
    }

    public LocalTime minusSeconds(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().seconds().subtract(this.getLocalMillis(), n));
    }

    public  minuteOfHour() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public LocalTime plus(ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, 1);
    }

    public LocalTime plusHours(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().hours().add(this.getLocalMillis(), n));
    }

    public LocalTime plusMillis(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().millis().add(this.getLocalMillis(), n));
    }

    public LocalTime plusMinutes(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().minutes().add(this.getLocalMillis(), n));
    }

    public LocalTime plusSeconds(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().seconds().add(this.getLocalMillis(), n));
    }

    public  property(DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType != null) {
            if (this.isSupported(dateTimeFieldType)) {
                return new /* Unavailable Anonymous Inner Class!! */;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Field '");
            stringBuilder.append((Object)dateTimeFieldType);
            stringBuilder.append("' is not supported");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        throw new IllegalArgumentException("The DateTimeFieldType must not be null");
    }

    public  secondOfMinute() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    @Override
    public int size() {
        return 4;
    }

    public DateTime toDateTimeToday() {
        return this.toDateTimeToday(null);
    }

    public DateTime toDateTimeToday(DateTimeZone dateTimeZone) {
        dateTimeZone = this.getChronology().withZone(dateTimeZone);
        return new DateTime(dateTimeZone.set((ReadablePartial)this, DateTimeUtils.currentTimeMillis()), (Chronology)dateTimeZone);
    }

    @ToString
    @Override
    public String toString() {
        return ISODateTimeFormat.time().print((ReadablePartial)this);
    }

    public String toString(String string) {
        if (string == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern((String)string).print((ReadablePartial)this);
    }

    public String toString(String string, Locale locale) throws IllegalArgumentException {
        if (string == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern((String)string).withLocale(locale).print((ReadablePartial)this);
    }

    public LocalTime withField(DateTimeFieldType dateTimeFieldType, int n) {
        if (dateTimeFieldType != null) {
            if (this.isSupported(dateTimeFieldType)) {
                return this.withLocalMillis(dateTimeFieldType.getField(this.getChronology()).set(this.getLocalMillis(), n));
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Field '");
            stringBuilder.append((Object)dateTimeFieldType);
            stringBuilder.append("' is not supported");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        throw new IllegalArgumentException("Field must not be null");
    }

    public LocalTime withFieldAdded(DurationFieldType durationFieldType, int n) {
        if (durationFieldType != null) {
            if (this.isSupported(durationFieldType)) {
                if (n == 0) {
                    return this;
                }
                return this.withLocalMillis(durationFieldType.getField(this.getChronology()).add(this.getLocalMillis(), n));
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Field '");
            stringBuilder.append((Object)durationFieldType);
            stringBuilder.append("' is not supported");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        throw new IllegalArgumentException("Field must not be null");
    }

    public LocalTime withFields(ReadablePartial readablePartial) {
        if (readablePartial == null) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().set(readablePartial, this.getLocalMillis()));
    }

    public LocalTime withHourOfDay(int n) {
        return this.withLocalMillis(this.getChronology().hourOfDay().set(this.getLocalMillis(), n));
    }

    LocalTime withLocalMillis(long l) {
        if (l == this.getLocalMillis()) {
            return this;
        }
        return new LocalTime(l, this.getChronology());
    }

    public LocalTime withMillisOfDay(int n) {
        return this.withLocalMillis(this.getChronology().millisOfDay().set(this.getLocalMillis(), n));
    }

    public LocalTime withMillisOfSecond(int n) {
        return this.withLocalMillis(this.getChronology().millisOfSecond().set(this.getLocalMillis(), n));
    }

    public LocalTime withMinuteOfHour(int n) {
        return this.withLocalMillis(this.getChronology().minuteOfHour().set(this.getLocalMillis(), n));
    }

    public LocalTime withPeriodAdded(ReadablePeriod readablePeriod, int n) {
        if (readablePeriod != null) {
            if (n == 0) {
                return this;
            }
            return this.withLocalMillis(this.getChronology().add(readablePeriod, this.getLocalMillis(), n));
        }
        return this;
    }

    public LocalTime withSecondOfMinute(int n) {
        return this.withLocalMillis(this.getChronology().secondOfMinute().set(this.getLocalMillis(), n));
    }
}

