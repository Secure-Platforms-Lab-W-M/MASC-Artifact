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
 *  org.joda.time.LocalDateTime$Property
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
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
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
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.base.BaseLocal;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PartialConverter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public final class LocalDateTime
extends BaseLocal
implements ReadablePartial,
Serializable {
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

    public LocalDateTime(int n, int n2, int n3, int n4, int n5) {
        this(n, n2, n3, n4, n5, 0, 0, (Chronology)ISOChronology.getInstanceUTC());
    }

    public LocalDateTime(int n, int n2, int n3, int n4, int n5, int n6) {
        this(n, n2, n3, n4, n5, n6, 0, (Chronology)ISOChronology.getInstanceUTC());
    }

    public LocalDateTime(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        this(n, n2, n3, n4, n5, n6, n7, (Chronology)ISOChronology.getInstanceUTC());
    }

    public LocalDateTime(int n, int n2, int n3, int n4, int n5, int n6, int n7, Chronology chronology) {
        chronology = DateTimeUtils.getChronology((Chronology)chronology).withUTC();
        long l = chronology.getDateTimeMillis(n, n2, n3, n4, n5, n6, n7);
        this.iChronology = chronology;
        this.iLocalMillis = l;
    }

    public LocalDateTime(long l) {
        this(l, (Chronology)ISOChronology.getInstance());
    }

    public LocalDateTime(long l, Chronology chronology) {
        chronology = DateTimeUtils.getChronology((Chronology)chronology);
        this.iLocalMillis = chronology.getZone().getMillisKeepLocal(DateTimeZone.UTC, l);
        this.iChronology = chronology.withUTC();
    }

    public LocalDateTime(long l, DateTimeZone dateTimeZone) {
        this(l, (Chronology)ISOChronology.getInstance((DateTimeZone)dateTimeZone));
    }

    public LocalDateTime(Object object) {
        this(object, (Chronology)null);
    }

    public LocalDateTime(Object arrn, Chronology chronology) {
        PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter((Object)arrn);
        chronology = DateTimeUtils.getChronology((Chronology)partialConverter.getChronology((Object)arrn, chronology));
        this.iChronology = chronology.withUTC();
        arrn = partialConverter.getPartialValues((ReadablePartial)this, (Object)arrn, chronology, ISODateTimeFormat.localDateOptionalTimeParser());
        this.iLocalMillis = this.iChronology.getDateTimeMillis(arrn[0], arrn[1], arrn[2], arrn[3]);
    }

    public LocalDateTime(Object arrn, DateTimeZone dateTimeZone) {
        PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter((Object)arrn);
        dateTimeZone = DateTimeUtils.getChronology((Chronology)partialConverter.getChronology((Object)arrn, dateTimeZone));
        this.iChronology = dateTimeZone.withUTC();
        arrn = partialConverter.getPartialValues((ReadablePartial)this, (Object)arrn, (Chronology)dateTimeZone, ISODateTimeFormat.localDateOptionalTimeParser());
        this.iLocalMillis = this.iChronology.getDateTimeMillis(arrn[0], arrn[1], arrn[2], arrn[3]);
    }

    public LocalDateTime(Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }

    public LocalDateTime(DateTimeZone dateTimeZone) {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance((DateTimeZone)dateTimeZone));
    }

    private Date correctDstTransition(Date comparable, TimeZone timeZone) {
        block6 : {
            Calendar calendar;
            block5 : {
                block4 : {
                    calendar = Calendar.getInstance(timeZone);
                    calendar.setTime((Date)comparable);
                    comparable = LocalDateTime.fromCalendarFields(calendar);
                    if (!comparable.isBefore(this)) break block4;
                    while (comparable.isBefore(this)) {
                        calendar.setTimeInMillis(calendar.getTimeInMillis() + 60000L);
                        comparable = LocalDateTime.fromCalendarFields(calendar);
                    }
                    while (!comparable.isBefore(this)) {
                        calendar.setTimeInMillis(calendar.getTimeInMillis() - 1000L);
                        comparable = LocalDateTime.fromCalendarFields(calendar);
                    }
                    calendar.setTimeInMillis(calendar.getTimeInMillis() + 1000L);
                    break block5;
                }
                if (!comparable.equals(this)) break block5;
                comparable = Calendar.getInstance(timeZone);
                comparable.setTimeInMillis(calendar.getTimeInMillis() - (long)timeZone.getDSTSavings());
                if (LocalDateTime.fromCalendarFields((Calendar)comparable).equals(this)) break block6;
            }
            comparable = calendar;
        }
        return comparable.getTime();
    }

    public static LocalDateTime fromCalendarFields(Calendar calendar) {
        if (calendar != null) {
            int n = calendar.get(0);
            int n2 = calendar.get(1);
            if (n != 1) {
                n2 = 1 - n2;
            }
            return new LocalDateTime(n2, calendar.get(2) + 1, calendar.get(5), calendar.get(11), calendar.get(12), calendar.get(13), calendar.get(14));
        }
        throw new IllegalArgumentException("The calendar must not be null");
    }

    public static LocalDateTime fromDateFields(Date date) {
        if (date != null) {
            if (date.getTime() < 0L) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(date);
                return LocalDateTime.fromCalendarFields(gregorianCalendar);
            }
            return new LocalDateTime(date.getYear() + 1900, date.getMonth() + 1, date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds(), ((int)(date.getTime() % 1000L) + 1000) % 1000);
        }
        throw new IllegalArgumentException("The date must not be null");
    }

    public static LocalDateTime now() {
        return new LocalDateTime();
    }

    public static LocalDateTime now(Chronology chronology) {
        if (chronology != null) {
            return new LocalDateTime(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }

    public static LocalDateTime now(DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new LocalDateTime(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }

    @FromString
    public static LocalDateTime parse(String string) {
        return LocalDateTime.parse(string, ISODateTimeFormat.localDateOptionalTimeParser());
    }

    public static LocalDateTime parse(String string, DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.parseLocalDateTime(string);
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

    public  centuryOfEra() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    @Override
    public int compareTo(ReadablePartial readablePartial) {
        if (this == readablePartial) {
            return 0;
        }
        if (readablePartial instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime)readablePartial;
            if (this.iChronology.equals((Object)localDateTime.iChronology)) {
                long l = this.iLocalMillis;
                long l2 = localDateTime.iLocalMillis;
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
        if (object instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime)object;
            if (this.iChronology.equals((Object)localDateTime.iChronology)) {
                if (this.iLocalMillis == localDateTime.iLocalMillis) {
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
    protected DateTimeField getField(int n, Chronology object) {
        switch (n) {
            default: {
                object = new StringBuilder();
                object.append("Invalid index: ");
                object.append(n);
                throw new IndexOutOfBoundsException(object.toString());
            }
            case 3: {
                return object.millisOfDay();
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
    public int getValue(int n) {
        switch (n) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid index: ");
                stringBuilder.append(n);
                throw new IndexOutOfBoundsException(stringBuilder.toString());
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

    public  hourOfDay() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    @Override
    public boolean isSupported(DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType == null) {
            return false;
        }
        return dateTimeFieldType.getField(this.getChronology()).isSupported();
    }

    public boolean isSupported(DurationFieldType durationFieldType) {
        if (durationFieldType == null) {
            return false;
        }
        return durationFieldType.getField(this.getChronology()).isSupported();
    }

    public  millisOfDay() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  millisOfSecond() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public LocalDateTime minus(ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, -1);
    }

    public LocalDateTime minus(ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, -1);
    }

    public LocalDateTime minusDays(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().days().subtract(this.getLocalMillis(), n));
    }

    public LocalDateTime minusHours(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().hours().subtract(this.getLocalMillis(), n));
    }

    public LocalDateTime minusMillis(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().millis().subtract(this.getLocalMillis(), n));
    }

    public LocalDateTime minusMinutes(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().minutes().subtract(this.getLocalMillis(), n));
    }

    public LocalDateTime minusMonths(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().months().subtract(this.getLocalMillis(), n));
    }

    public LocalDateTime minusSeconds(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().seconds().subtract(this.getLocalMillis(), n));
    }

    public LocalDateTime minusWeeks(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().weeks().subtract(this.getLocalMillis(), n));
    }

    public LocalDateTime minusYears(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().years().subtract(this.getLocalMillis(), n));
    }

    public  minuteOfHour() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  monthOfYear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public LocalDateTime plus(ReadableDuration readableDuration) {
        return this.withDurationAdded(readableDuration, 1);
    }

    public LocalDateTime plus(ReadablePeriod readablePeriod) {
        return this.withPeriodAdded(readablePeriod, 1);
    }

    public LocalDateTime plusDays(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().days().add(this.getLocalMillis(), n));
    }

    public LocalDateTime plusHours(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().hours().add(this.getLocalMillis(), n));
    }

    public LocalDateTime plusMillis(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().millis().add(this.getLocalMillis(), n));
    }

    public LocalDateTime plusMinutes(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().minutes().add(this.getLocalMillis(), n));
    }

    public LocalDateTime plusMonths(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().months().add(this.getLocalMillis(), n));
    }

    public LocalDateTime plusSeconds(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().seconds().add(this.getLocalMillis(), n));
    }

    public LocalDateTime plusWeeks(int n) {
        if (n == 0) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().weeks().add(this.getLocalMillis(), n));
    }

    public LocalDateTime plusYears(int n) {
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

    public  secondOfMinute() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    @Override
    public int size() {
        return 4;
    }

    public Date toDate() {
        int n = this.getDayOfMonth();
        Date date = new Date(this.getYear() - 1900, this.getMonthOfYear() - 1, n, this.getHourOfDay(), this.getMinuteOfHour(), this.getSecondOfMinute());
        date.setTime(date.getTime() + (long)this.getMillisOfSecond());
        return this.correctDstTransition(date, TimeZone.getDefault());
    }

    public Date toDate(TimeZone timeZone) {
        Comparable comparable = Calendar.getInstance(timeZone);
        comparable.clear();
        comparable.set(this.getYear(), this.getMonthOfYear() - 1, this.getDayOfMonth(), this.getHourOfDay(), this.getMinuteOfHour(), this.getSecondOfMinute());
        comparable = comparable.getTime();
        comparable.setTime(comparable.getTime() + (long)this.getMillisOfSecond());
        return this.correctDstTransition((Date)comparable, timeZone);
    }

    public DateTime toDateTime() {
        return this.toDateTime(null);
    }

    public DateTime toDateTime(DateTimeZone dateTimeZone) {
        dateTimeZone = DateTimeUtils.getZone((DateTimeZone)dateTimeZone);
        dateTimeZone = this.iChronology.withZone(dateTimeZone);
        return new DateTime(this.getYear(), this.getMonthOfYear(), this.getDayOfMonth(), this.getHourOfDay(), this.getMinuteOfHour(), this.getSecondOfMinute(), this.getMillisOfSecond(), (Chronology)dateTimeZone);
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

    public LocalDateTime withCenturyOfEra(int n) {
        return this.withLocalMillis(this.getChronology().centuryOfEra().set(this.getLocalMillis(), n));
    }

    public LocalDateTime withDate(int n, int n2, int n3) {
        Chronology chronology = this.getChronology();
        long l = this.getLocalMillis();
        l = chronology.year().set(l, n);
        l = chronology.monthOfYear().set(l, n2);
        return this.withLocalMillis(chronology.dayOfMonth().set(l, n3));
    }

    public LocalDateTime withDayOfMonth(int n) {
        return this.withLocalMillis(this.getChronology().dayOfMonth().set(this.getLocalMillis(), n));
    }

    public LocalDateTime withDayOfWeek(int n) {
        return this.withLocalMillis(this.getChronology().dayOfWeek().set(this.getLocalMillis(), n));
    }

    public LocalDateTime withDayOfYear(int n) {
        return this.withLocalMillis(this.getChronology().dayOfYear().set(this.getLocalMillis(), n));
    }

    public LocalDateTime withDurationAdded(ReadableDuration readableDuration, int n) {
        if (readableDuration != null) {
            if (n == 0) {
                return this;
            }
            return this.withLocalMillis(this.getChronology().add(this.getLocalMillis(), readableDuration.getMillis(), n));
        }
        return this;
    }

    public LocalDateTime withEra(int n) {
        return this.withLocalMillis(this.getChronology().era().set(this.getLocalMillis(), n));
    }

    public LocalDateTime withField(DateTimeFieldType dateTimeFieldType, int n) {
        if (dateTimeFieldType != null) {
            return this.withLocalMillis(dateTimeFieldType.getField(this.getChronology()).set(this.getLocalMillis(), n));
        }
        throw new IllegalArgumentException("Field must not be null");
    }

    public LocalDateTime withFieldAdded(DurationFieldType durationFieldType, int n) {
        if (durationFieldType != null) {
            if (n == 0) {
                return this;
            }
            return this.withLocalMillis(durationFieldType.getField(this.getChronology()).add(this.getLocalMillis(), n));
        }
        throw new IllegalArgumentException("Field must not be null");
    }

    public LocalDateTime withFields(ReadablePartial readablePartial) {
        if (readablePartial == null) {
            return this;
        }
        return this.withLocalMillis(this.getChronology().set(readablePartial, this.getLocalMillis()));
    }

    public LocalDateTime withHourOfDay(int n) {
        return this.withLocalMillis(this.getChronology().hourOfDay().set(this.getLocalMillis(), n));
    }

    LocalDateTime withLocalMillis(long l) {
        if (l == this.getLocalMillis()) {
            return this;
        }
        return new LocalDateTime(l, this.getChronology());
    }

    public LocalDateTime withMillisOfDay(int n) {
        return this.withLocalMillis(this.getChronology().millisOfDay().set(this.getLocalMillis(), n));
    }

    public LocalDateTime withMillisOfSecond(int n) {
        return this.withLocalMillis(this.getChronology().millisOfSecond().set(this.getLocalMillis(), n));
    }

    public LocalDateTime withMinuteOfHour(int n) {
        return this.withLocalMillis(this.getChronology().minuteOfHour().set(this.getLocalMillis(), n));
    }

    public LocalDateTime withMonthOfYear(int n) {
        return this.withLocalMillis(this.getChronology().monthOfYear().set(this.getLocalMillis(), n));
    }

    public LocalDateTime withPeriodAdded(ReadablePeriod readablePeriod, int n) {
        if (readablePeriod != null) {
            if (n == 0) {
                return this;
            }
            return this.withLocalMillis(this.getChronology().add(readablePeriod, this.getLocalMillis(), n));
        }
        return this;
    }

    public LocalDateTime withSecondOfMinute(int n) {
        return this.withLocalMillis(this.getChronology().secondOfMinute().set(this.getLocalMillis(), n));
    }

    public LocalDateTime withTime(int n, int n2, int n3, int n4) {
        Chronology chronology = this.getChronology();
        long l = this.getLocalMillis();
        l = chronology.hourOfDay().set(l, n);
        l = chronology.minuteOfHour().set(l, n2);
        l = chronology.secondOfMinute().set(l, n3);
        return this.withLocalMillis(chronology.millisOfSecond().set(l, n4));
    }

    public LocalDateTime withWeekOfWeekyear(int n) {
        return this.withLocalMillis(this.getChronology().weekOfWeekyear().set(this.getLocalMillis(), n));
    }

    public LocalDateTime withWeekyear(int n) {
        return this.withLocalMillis(this.getChronology().weekyear().set(this.getLocalMillis(), n));
    }

    public LocalDateTime withYear(int n) {
        return this.withLocalMillis(this.getChronology().year().set(this.getLocalMillis(), n));
    }

    public LocalDateTime withYearOfCentury(int n) {
        return this.withLocalMillis(this.getChronology().yearOfCentury().set(this.getLocalMillis(), n));
    }

    public LocalDateTime withYearOfEra(int n) {
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

