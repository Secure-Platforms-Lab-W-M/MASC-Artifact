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
 *  org.joda.time.Interval
 *  org.joda.time.LocalDate$Property
 *  org.joda.time.chrono.ISOChronology
 *  org.joda.time.convert.ConverterManager
 *  org.joda.time.convert.PartialConverter
 *  org.joda.time.field.FieldUtils
 *  org.joda.time.format.DateTimeFormat
 *  org.joda.time.format.DateTimeFormatter
 *  org.joda.time.format.ISODateTimeFormat
 */
package org.joda.time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import org.joda.convert.FromString;
import org.joda.convert.ToString;
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
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.base.BaseLocal;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PartialConverter;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public final class LocalDate
extends BaseLocal
implements ReadablePartial,
Serializable {
    private static final Set<DurationFieldType> DATE_DURATION_TYPES = new HashSet<DurationFieldType>();
    private static final int DAY_OF_MONTH = 2;
    private static final int MONTH_OF_YEAR = 1;
    private static final int YEAR = 0;
    private static final long serialVersionUID = -8775358157899L;
    private final Chronology iChronology;
    private transient int iHash;
    private final long iLocalMillis;

    static {
        DATE_DURATION_TYPES.add(DurationFieldType.days());
        DATE_DURATION_TYPES.add(DurationFieldType.weeks());
        DATE_DURATION_TYPES.add(DurationFieldType.months());
        DATE_DURATION_TYPES.add(DurationFieldType.weekyears());
        DATE_DURATION_TYPES.add(DurationFieldType.years());
        DATE_DURATION_TYPES.add(DurationFieldType.centuries());
        DATE_DURATION_TYPES.add(DurationFieldType.eras());
    }

    public LocalDate() {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance());
    }

    public LocalDate(int n, int n2, int n3) {
        this(n, n2, n3, (Chronology)ISOChronology.getInstanceUTC());
    }

    public LocalDate(int n, int n2, int n3, Chronology chronology) {
        chronology = DateTimeUtils.getChronology((Chronology)chronology).withUTC();
        long l = chronology.getDateTimeMillis(n, n2, n3, 0);
        this.iChronology = chronology;
        this.iLocalMillis = l;
    }

    public LocalDate(long l) {
        this(l, (Chronology)ISOChronology.getInstance());
    }

    public LocalDate(long l, Chronology chronology) {
        chronology = DateTimeUtils.getChronology((Chronology)chronology);
        l = chronology.getZone().getMillisKeepLocal(DateTimeZone.UTC, l);
        chronology = chronology.withUTC();
        this.iLocalMillis = chronology.dayOfMonth().roundFloor(l);
        this.iChronology = chronology;
    }

    public LocalDate(long l, DateTimeZone dateTimeZone) {
        this(l, (Chronology)ISOChronology.getInstance((DateTimeZone)dateTimeZone));
    }

    public LocalDate(Object object) {
        this(object, (Chronology)null);
    }

    public LocalDate(Object arrn, Chronology chronology) {
        PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter((Object)arrn);
        chronology = DateTimeUtils.getChronology((Chronology)partialConverter.getChronology((Object)arrn, chronology));
        this.iChronology = chronology.withUTC();
        arrn = partialConverter.getPartialValues((ReadablePartial)this, (Object)arrn, chronology, ISODateTimeFormat.localDateParser());
        this.iLocalMillis = this.iChronology.getDateTimeMillis(arrn[0], arrn[1], arrn[2], 0);
    }

    public LocalDate(Object arrn, DateTimeZone dateTimeZone) {
        PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter((Object)arrn);
        dateTimeZone = DateTimeUtils.getChronology((Chronology)partialConverter.getChronology((Object)arrn, dateTimeZone));
        this.iChronology = dateTimeZone.withUTC();
        arrn = partialConverter.getPartialValues((ReadablePartial)this, (Object)arrn, (Chronology)dateTimeZone, ISODateTimeFormat.localDateParser());
        this.iLocalMillis = this.iChronology.getDateTimeMillis(arrn[0], arrn[1], arrn[2], 0);
    }

    public LocalDate(Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }

    public LocalDate(DateTimeZone dateTimeZone) {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance((DateTimeZone)dateTimeZone));
    }

    public static LocalDate fromCalendarFields(Calendar calendar) {
        if (calendar != null) {
            int n = calendar.get(0);
            int n2 = calendar.get(1);
            if (n != 1) {
                n2 = 1 - n2;
            }
            return new LocalDate(n2, calendar.get(2) + 1, calendar.get(5));
        }
        throw new IllegalArgumentException("The calendar must not be null");
    }

    public static LocalDate fromDateFields(Date date) {
        if (date != null) {
            if (date.getTime() < 0L) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(date);
                return LocalDate.fromCalendarFields(gregorianCalendar);
            }
            return new LocalDate(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
        }
        throw new IllegalArgumentException("The date must not be null");
    }

    public static LocalDate now() {
        return new LocalDate();
    }

    public static LocalDate now(Chronology chronology) {
        if (chronology != null) {
            return new LocalDate(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }

    public static LocalDate now(DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new LocalDate(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }

    @FromString
    public static LocalDate parse(String string) {
        return LocalDate.parse(string, ISODateTimeFormat.localDateParser());
    }

    public static LocalDate parse(String string, DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.parseLocalDate(string);
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

    public  centuryOfEra() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    @Override
    public int compareTo(ReadablePartial readablePartial) {
        if (this == readablePartial) {
            return 0;
        }
        if (readablePartial instanceof LocalDate) {
            LocalDate localDate = (LocalDate)readablePartial;
            if (this.iChronology.equals((Object)localDate.iChronology)) {
                long l = this.iLocalMillis;
                long l2 = localDate.iLocalMillis;
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

    public  dayOfMonth() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  dayOfWeek() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  dayOfYear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof LocalDate) {
            LocalDate localDate = (LocalDate)object;
            if (this.iChronology.equals((Object)localDate.iChronology)) {
                if (this.iLocalMillis == localDate.iLocalMillis) {
                    return true;
                }
                return false;
            }
        }
        return super.equals(object);
    }

    public  era() {
        return new /* Unavailable Anonymous Inner Class!! */;
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
    protected DateTimeField getField(int n, Chronology object) {
        switch (n) {
            default: {
                object = new StringBuilder();
                object.append("Invalid index: ");
                object.append(n);
                throw new IndexOutOfBoundsException(object.toString());
            }
            case 2: {
                return object.dayOfMonth();
            }
            case 1: {
                return object.monthOfYear();
            }
            case 0: 
        }
        return object.year();
    }

    @Override
    protected long getLocalMillis() {
        return this.iLocalMillis;
    }

    public int getMonthOfYear() {
        return this.getChronology().monthOfYear().get(this.getLocalMillis());
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
            case 2: {
                return this.getChronology().dayOfMonth().get(this.getLocalMillis());
            }
            case 1: {
                return this.getChronology().monthOfYear().get(this.getLocalMillis());
            }
            case 0: 
        }
        return this.getChronology().year().get(this.getLocalMillis());
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
        int n = this.iHash;
        if (n == 0) {
            this.iHash = n = super.hashCode();
            return n;
        }
        return n;
    }

    @Override
    public boolean isSupported(DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType == null) {
            return false;
        }
        DurationFieldType durationFieldType = dateTimeFieldType.getDurationType();
        if (!DATE_DURATION_TYPES.contains((Object)durationFieldType) && durationFieldType.getField(this.getChronology()).getUnitMillis() < this.getChronology().days().getUnitMillis()) {
            return false;
        }
        return dateTimeFieldType.getField(this.getChronology()).isSupported();
    }

    public boolean isSupported(DurationFieldType durationFieldType) {
        if (durationFieldType == null) {
            return false;
        }
        DurationField durationField = durationFieldType.getField(this.getChronology());
        if (!DATE_DURATION_TYPES.contains((Object)durationFieldType) && durationField.getUnitMillis() < this.getChronology().days().getUnitMillis()) {
            return false;
        }
        return durationField.isSupported();
    }

    public LocalDate minus(ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, -1);
    }

    public LocalDate minusDays(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().days().subtract(this.getLocalMillis(), n));
    }

    public LocalDate minusMonths(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().months().subtract(this.getLocalMillis(), n));
    }

    public LocalDate minusWeeks(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().weeks().subtract(this.getLocalMillis(), n));
    }

    public LocalDate minusYears(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().years().subtract(this.getLocalMillis(), n));
    }

    public  monthOfYear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public LocalDate plus(ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, 1);
    }

    public LocalDate plusDays(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().days().add(this.getLocalMillis(), n));
    }

    public LocalDate plusMonths(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().months().add(this.getLocalMillis(), n));
    }

    public LocalDate plusWeeks(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().weeks().add(this.getLocalMillis(), n));
    }

    public LocalDate plusYears(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().years().add(this.getLocalMillis(), n));
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

    @Override
    public int size() {
        return 3;
    }

    public Date toDate() {
        int n = this.getDayOfMonth();
        Date date = new Date(this.getYear() - 1900, this.getMonthOfYear() - 1, n);
        Comparable comparable = LocalDate.fromDateFields(date);
        if (comparable.isBefore(this)) {
            while (!comparable.equals(this)) {
                date.setTime(date.getTime() + 3600000L);
                comparable = LocalDate.fromDateFields(date);
            }
            while (date.getDate() == n) {
                date.setTime(date.getTime() - 1000L);
            }
            date.setTime(date.getTime() + 1000L);
            return date;
        }
        if (comparable.equals(this)) {
            comparable = new Date(date.getTime() - (long)TimeZone.getDefault().getDSTSavings());
            if (comparable.getDate() == n) {
                return comparable;
            }
            return date;
        }
        return date;
    }

    @Deprecated
    public DateMidnight toDateMidnight() {
        return this.toDateMidnight(null);
    }

    @Deprecated
    public DateMidnight toDateMidnight(DateTimeZone dateTimeZone) {
        dateTimeZone = DateTimeUtils.getZone((DateTimeZone)dateTimeZone);
        dateTimeZone = this.getChronology().withZone(dateTimeZone);
        return new DateMidnight(this.getYear(), this.getMonthOfYear(), this.getDayOfMonth(), (Chronology)dateTimeZone);
    }

    public DateTime toDateTime(LocalTime localTime) {
        return this.toDateTime(localTime, null);
    }

    public DateTime toDateTime(LocalTime localTime, DateTimeZone dateTimeZone) {
        if (localTime == null) {
            return this.toDateTimeAtCurrentTime(dateTimeZone);
        }
        if (this.getChronology() == localTime.getChronology()) {
            dateTimeZone = this.getChronology().withZone(dateTimeZone);
            return new DateTime(this.getYear(), this.getMonthOfYear(), this.getDayOfMonth(), localTime.getHourOfDay(), localTime.getMinuteOfHour(), localTime.getSecondOfMinute(), localTime.getMillisOfSecond(), (Chronology)dateTimeZone);
        }
        throw new IllegalArgumentException("The chronology of the time does not match");
    }

    public DateTime toDateTimeAtCurrentTime() {
        return this.toDateTimeAtCurrentTime(null);
    }

    public DateTime toDateTimeAtCurrentTime(DateTimeZone dateTimeZone) {
        dateTimeZone = DateTimeUtils.getZone((DateTimeZone)dateTimeZone);
        dateTimeZone = this.getChronology().withZone(dateTimeZone);
        return new DateTime(dateTimeZone.set((ReadablePartial)this, DateTimeUtils.currentTimeMillis()), (Chronology)dateTimeZone);
    }

    @Deprecated
    public DateTime toDateTimeAtMidnight() {
        return this.toDateTimeAtMidnight(null);
    }

    @Deprecated
    public DateTime toDateTimeAtMidnight(DateTimeZone dateTimeZone) {
        dateTimeZone = DateTimeUtils.getZone((DateTimeZone)dateTimeZone);
        dateTimeZone = this.getChronology().withZone(dateTimeZone);
        return new DateTime(this.getYear(), this.getMonthOfYear(), this.getDayOfMonth(), 0, 0, 0, 0, (Chronology)dateTimeZone);
    }

    public DateTime toDateTimeAtStartOfDay() {
        return this.toDateTimeAtStartOfDay(null);
    }

    public DateTime toDateTimeAtStartOfDay(DateTimeZone dateTimeZone) {
        dateTimeZone = DateTimeUtils.getZone((DateTimeZone)dateTimeZone);
        Chronology chronology = this.getChronology().withZone(dateTimeZone);
        long l = dateTimeZone.convertLocalToUTC(this.getLocalMillis() + 21600000L, false);
        return new DateTime(chronology.dayOfMonth().roundFloor(l), chronology);
    }

    public Interval toInterval() {
        return this.toInterval(null);
    }

    public Interval toInterval(DateTimeZone dateTimeZone) {
        dateTimeZone = DateTimeUtils.getZone((DateTimeZone)dateTimeZone);
        return new Interval((ReadableInstant)this.toDateTimeAtStartOfDay(dateTimeZone), (ReadableInstant)this.plusDays(1).toDateTimeAtStartOfDay(dateTimeZone));
    }

    public LocalDateTime toLocalDateTime(LocalTime localTime) {
        if (localTime != null) {
            if (this.getChronology() == localTime.getChronology()) {
                return new LocalDateTime(this.getLocalMillis() + localTime.getLocalMillis(), this.getChronology());
            }
            throw new IllegalArgumentException("The chronology of the time does not match");
        }
        throw new IllegalArgumentException("The time must not be null");
    }

    @ToString
    @Override
    public String toString() {
        return ISODateTimeFormat.date().print((ReadablePartial)this);
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

    public  weekOfWeekyear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  weekyear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public LocalDate withCenturyOfEra(int n) {
        return this.withLocalMillis(this.getChronology().centuryOfEra().set(this.getLocalMillis(), n));
    }

    public LocalDate withDayOfMonth(int n) {
        return this.withLocalMillis(this.getChronology().dayOfMonth().set(this.getLocalMillis(), n));
    }

    public LocalDate withDayOfWeek(int n) {
        return this.withLocalMillis(this.getChronology().dayOfWeek().set(this.getLocalMillis(), n));
    }

    public LocalDate withDayOfYear(int n) {
        return this.withLocalMillis(this.getChronology().dayOfYear().set(this.getLocalMillis(), n));
    }

    public LocalDate withEra(int n) {
        return this.withLocalMillis(this.getChronology().era().set(this.getLocalMillis(), n));
    }

    public LocalDate withField(DateTimeFieldType dateTimeFieldType, int n) {
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

    public LocalDate withFieldAdded(DurationFieldType durationFieldType, int n) {
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

    public LocalDate withFields(ReadablePartial readablePartial) {
        if (readablePartial == null) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().set(readablePartial, this.getLocalMillis()));
    }

    LocalDate withLocalMillis(long l) {
        l = this.iChronology.dayOfMonth().roundFloor(l);
        if (l == this.getLocalMillis()) {
            return this;
        }
        return new LocalDate(l, this.getChronology());
    }

    public LocalDate withMonthOfYear(int n) {
        return this.withLocalMillis(this.getChronology().monthOfYear().set(this.getLocalMillis(), n));
    }

    public LocalDate withPeriodAdded(ReadablePeriod readablePeriod, int n) {
        if (readablePeriod != null) {
            if (n == 0) {
                return this;
            }
            long l = this.getLocalMillis();
            Chronology chronology = this.getChronology();
            for (int i = 0; i < readablePeriod.size(); ++i) {
                long l2 = FieldUtils.safeMultiply((int)readablePeriod.getValue(i), (int)n);
                DurationFieldType durationFieldType = readablePeriod.getFieldType(i);
                if (!this.isSupported(durationFieldType)) continue;
                l = durationFieldType.getField(chronology).add(l, l2);
            }
            return this.withLocalMillis(l);
        }
        return this;
    }

    public LocalDate withWeekOfWeekyear(int n) {
        return this.withLocalMillis(this.getChronology().weekOfWeekyear().set(this.getLocalMillis(), n));
    }

    public LocalDate withWeekyear(int n) {
        return this.withLocalMillis(this.getChronology().weekyear().set(this.getLocalMillis(), n));
    }

    public LocalDate withYear(int n) {
        return this.withLocalMillis(this.getChronology().year().set(this.getLocalMillis(), n));
    }

    public LocalDate withYearOfCentury(int n) {
        return this.withLocalMillis(this.getChronology().yearOfCentury().set(this.getLocalMillis(), n));
    }

    public LocalDate withYearOfEra(int n) {
        return this.withLocalMillis(this.getChronology().yearOfEra().set(this.getLocalMillis(), n));
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

